package com.huawei.vm.console.processV1;
import com.huawei.vm.console.communicationV1.CommunicationSender;
import com.huawei.vm.console.communicationV1.ProtocolCode;
import com.huawei.vm.console.communicationV1.ProtocolProcessor;
import com.huawei.vm.console.storageV1.impl.FloppyDriver;
import com.huawei.vm.console.utilsV1.TestPrint;
import com.huawei.vm.console.utilsV1.VMException;
import com.kvmV1.AESHandler;
public class UFIProcessor
  extends USBProcessor
  implements Runnable
{
  private FloppyDriver ufiDevice;
  protected final byte[] inquiryData = new byte[] { 0, Byte.MIN_VALUE, 0, 1, 31, 0, 0, 0, 72, 85, 65, 87, 69, 73, 32, 32, 70, 76, 79, 80, 80, 89, 32, 86, 77, 32, 49, 46, 49, 46, 48, 32, 32, 32, 32, 32 };
  protected final byte[] capacityList = new byte[] { 0, 0, 0, 16, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 11, 64, 0, 0, 2, 0 };
  public UFIProcessor(FloppyDriver floppy, CommunicationSender sender) {
    super(sender);
    this.ufiDevice = floppy;
  }
  public void run() {
    TestPrint.println(1, "UFI processor : start");
    while (!this.exitFlag)
    {
      processCommand();
    }
    TestPrint.println(1, Thread.currentThread().getName() + " close. arr num:" + this.dataArray.getBigArrNum() + "; small arr num:" + this.dataArray.getSmallArrNum() + "; data num :" + this.dataArray.getDataNum());
  }
  private void checkChangeDisk() {
    if (this.ufiDevice.isChangeDisk()) {
      try {
        if (this.ufiDevice.isEject())
        {
          this.ufiDevice.eject();
        }
        else
        {
          this.ufiDevice.insert();
        }
      } catch (VMException e) {
        TestPrint.println(3, "UFI processor : Error happened when changing disk");
      } 
    }
  }
  public void processCommand() {
    getCommand();
    checkChangeDisk();
    if (this.exitFlag) {
      return;
    }
    this.ufiDevice.refreshState();
    switch (this.command[0] & 0xFF) {
      case 4:
        TestPrint.println(1, "UFI processor : FORMAT_UNIT");
        doFormat();
        break;
      case 18:
        doInquiry();
        break;
      case 85:
        TestPrint.println(1, "UFI processor : MODE_SELECT");
        doModeSelect();
        break;
      case 90:
        doModeSense();
        break;
      case 30:
        TestPrint.println(1, "UFI processor : PREVENT_ALLOW_MEDIUM_REMOVAL");
        doPreventAllowMediumRemoval();
        break;
      case 40:
      case 168:
        doRead();
        break;
      case 37:
        doReadCapacity();
        break;
      case 35:
        doReadFormatCapacity();
        break;
      case 3:
        doRequestSense();
        break;
      case 1:
        TestPrint.println(1, "UFI processor : Rezero requested!");
        setSenseKeys(0, 0, 0, 0);
        break;
      case 43:
        TestPrint.println(1, "UFI processor : Seek requested!");
        setSenseKeys(0, 0, 0, 0);
        break;
      case 29:
        TestPrint.println(1, "UFI processor : SEND_DIAGNOSTIC");
        doSendDiagnostic();
        break;
      case 27:
        TestPrint.println(1, "UFI processor : START_STOP");
        doStartStopUnit();
        break;
      case 0:
        doTestUnitRead();
        break;
      case 47:
        TestPrint.println(1, "UFI processor : VERIFY");
        doVerify();
        break;
      case 42:
      case 46:
      case 170:
        doWrite();
        break;
      default:
        TestPrint.println(1, "UFI processor : Unknown command" + this.command[0] + 'ÿ');
        setSenseKeys(5, 36, 0, 0);
        break;
    } 
    commandFinish();
  }
  private void doFormat() {
    int paramListLength = ProtocolCode.getInt16bits(this.command, 8);
    byte[] paramList = new byte[paramListLength];
    paramListLength = getData(paramList, paramListLength);
    if (0 == paramListLength) {
      TestPrint.println(3, "UFI Processor: Format -- PARAMETER LIST LENGTH ERROR");
      setSenseKeys(5, 26, 0, 0);
      return;
    } 
    if (this.ufiDevice.isWriteProtect()) {
      TestPrint.println(3, "UFI Processor: Format -- Write Protect.");
      setSenseKeys(7, 39, 0, 0);
    }
    else if (this.ufiDevice.getTotalBlocks() == ProtocolCode.getInt32bits(paramList, 5) && this.ufiDevice.getBlockLength() == ProtocolCode.getInt24bits(paramList, 10)) {
      int track = this.command[2] & 0xFF;
      int side = paramList[1] & 0x1;
      try {
        this.ufiDevice.formatUnit(2, track, track, side, side);
        setSenseKeys(0, 0, 0, 0);
      }
      catch (VMException e) {
        TestPrint.println(3, "UFI Processor: Format -- FORMAT COMMAND FAILED");
        setSenseKeys(3, 49, 1, e.getKey());
      }
    } else {
      TestPrint.println(3, "UFI Processor: Format -- INVALID FIELD IN PARAMETER");
      setSenseKeys(5, 38, 0, 0);
    } 
  }
  private void doInquiry() {
    if ((this.command[1] & 0xE0) != 0) {
      setSenseKeys(5, 37, 0, 0);
    }
    else if ((this.command[1] & 0x1) != 0) {
      setSenseKeys(5, 36, 0, 0);
    }
    else {
      sendData(this.inquiryData.length, this.inquiryData, true);
      setSenseKeys(0, 0, 0, 0);
    } 
  }
  private void doModeSelect() {
    int paramListLength = ProtocolCode.getInt16bits(this.command, 8);
    if (0 == getData(this.dataBuffer, paramListLength)) {
      setSenseKeys(5, 26, 0, 0);
    }
    else {
      setSenseKeys(0, 0, 0, 0);
    } 
  }
  private void doModeSense() {
    int length = this.ufiDevice.modeSense(this.dataBuffer, this.command[2] >> 6 & 0x3, this.command[2] & 0x3F);
    setSenseKeys(0, 0, 0, 0);
    sendData(length, this.dataBuffer, true);
  }
  private void doPreventAllowMediumRemoval() {
    if ((this.command[4] & 0x1) != 0) {
      setSenseKeys(5, 36, 0, 0);
    }
    else {
      setSenseKeys(0, 0, 0, 0);
    } 
  }
  private void doRead() {
    long lba = ProtocolCode.getInt32bits(this.command, 3);
    long startPos = lba * this.ufiDevice.getBlockLength();
    int length = 0;
    int curRead = 0;
    if (40 == this.command[0]) {
      length = ProtocolCode.getInt16bits(this.command, 8) * this.ufiDevice.getBlockLength();
    }
    else {
      length = ProtocolCode.getInt32bits(this.command, 7) * this.ufiDevice.getBlockLength();
    } 
    try {
      if (startPos >= 0L && startPos < this.ufiDevice.getMediumSize()) {
        int readLength = 0;
        boolean isLast = false;
        int bufferLen = this.dataBuffer.length;
        while (length > 0) {
          if (bufferLen < length) {
            readLength = bufferLen;
          }
          else {
            readLength = length;
            isLast = true;
          } 
          curRead = this.ufiDevice.read(this.dataBuffer, startPos, readLength);
          startPos += curRead;
          if (curRead == readLength) {
            sendData(readLength, this.dataBuffer, isLast);
            length -= readLength;
            continue;
          } 
          sendData(curRead, this.dataBuffer, true);
          length = -1;
        } 
        if (0 == length)
        {
          setSenseKeys(0, 0, 0, 0);
        }
        else
        {
          TestPrint.println(3, "UFI Processor: Read -- address error(sensekey:ILOGICAL BLOCK ADDRESS OUT OF RANGE)");
          setSenseKeys(5, 33, 0, 0);
        }
      } else {
        TestPrint.println(3, "UFI Processor: Read -- address error(sensekey:ILOGICAL BLOCK ADDRESS OUT OF RANGE)");
        setSenseKeys(5, 33, 0, 0);
      }
    } catch (VMException e) {
      switch (e.getKey()) {
        case 250:
          TestPrint.println(3, "UFI Processor:Read -- read error(ID CRC ERROR)");
          setSenseKeys(3, 16, 0, (int)(startPos / this.ufiDevice.getBlockLength()));
          break;
        case 253:
          TestPrint.println(3, "UFI Processor : Read -- Meida not present");
          this.ufiDevice.setDeviceState(0);
          setSenseKeys(2, 58, 0, 0);
          break;
      } 
    } 
  }
  private void doReadCapacity() {
    int lba;
    int blockLength;
    switch (this.ufiDevice.testUnitReady()) {
      case 0:
        setSenseKeys(2, 58, 0, 0);
        break;
      case 2:
        setSenseKeys(6, 40, 0, 0);
        break;
      case 3:
        lba = this.ufiDevice.getTotalBlocks() - 1;
        blockLength = this.ufiDevice.getBlockLength();
        this.dataBuffer[0] = (byte)(lba >> 24 & 0xFF);
        this.dataBuffer[1] = (byte)(lba >> 16 & 0xFF);
        this.dataBuffer[2] = (byte)(lba >> 8 & 0xFF);
        this.dataBuffer[3] = (byte)(lba & 0xFF);
        this.dataBuffer[4] = (byte)(blockLength >> 24 & 0xFF);
        this.dataBuffer[5] = (byte)(blockLength >> 16 & 0xFF);
        this.dataBuffer[6] = (byte)(blockLength >> 8 & 0xFF);
        this.dataBuffer[7] = (byte)blockLength;
        setSenseKeys(0, 0, 0, 0);
        sendData(8, this.dataBuffer, true);
        break;
    } 
  }
  private void doReadFormatCapacity() {
    if (2 == this.ufiDevice.testUnitReady()) {
      this.ufiDevice.setDeviceState(3);
      setSenseKeys(6, 40, 0, 0);
    }
    else {
      setSenseKeys(0, 0, 0, 0);
    } 
    byte[] capacityList = this.capacityList;
    int totalBlocks = this.ufiDevice.getTotalBlocks();
    int blockLength = this.ufiDevice.getBlockLength();
    capacityList[4] = (byte)(totalBlocks >> 24 & 0xFF);
    capacityList[5] = (byte)(totalBlocks >> 16 & 0xFF);
    capacityList[6] = (byte)(totalBlocks >> 8 & 0xFF);
    capacityList[7] = (byte)(totalBlocks & 0xFF);
    capacityList[9] = (byte)(blockLength >> 16 & 0xFF);
    capacityList[10] = (byte)(blockLength >> 8 & 0xFF);
    capacityList[11] = (byte)(blockLength & 0xFF);
    int dataLen = capacityList.length;
    if (2880 != totalBlocks)
    {
      dataLen = 12;
    }
    sendData(dataLen, capacityList, true);
  }
  private void doRequestSense() {
    byte[] senseData = this.senseData;
    sendData(senseData.length, senseData, true);
  }
  private void doSendDiagnostic() {
    this.ufiDevice.setDeviceState(2);
    setSenseKeys(0, 0, 0, 0);
  }
  private void doStartStopUnit() {
    if (0 != (this.command[4] & 0x2)) {
      setSenseKeys(5, 36, 0, 0);
    }
    else {
      setSenseKeys(0, 0, 0, 0);
    } 
  }
  private void doTestUnitRead() {
    if ((this.command[1] & 0xE0) != 0) {
      setSenseKeys(5, 36, 0, 0);
    }
    else {
      switch (this.ufiDevice.testUnitReady()) {
        case 0:
          setSenseKeys(2, 58, 0, 0);
          break;
        case 2:
          setSenseKeys(6, 40, 0, 0);
          this.ufiDevice.setDeviceState(3);
          break;
        case 3:
          setSenseKeys(0, 0, 0, 0);
          break;
      } 
    } 
  }
  private void doVerify() {
    TestPrint.println(1, "Verify requested!");
    setSenseKeys(0, 0, 0, 0);
  }
  private void doWrite() {
    long startPos = ProtocolCode.getInt32bits(this.command, 3) * this.ufiDevice.getBlockLength();
    int length = 0;
    if (-86 == this.command[0]) {
      length = ProtocolCode.getInt32bits(this.command, 7);
    }
    else {
      length = ProtocolCode.getInt16bits(this.command, 8);
    } 
    length *= this.ufiDevice.getBlockLength();
    if (0 == length) {
      setSenseKeys(0, 0, 0, 0);
      return;
    } 
    int curWrite = 0;
    try {
      if (startPos >= 0L && startPos < this.ufiDevice.getMediumSize() && startPos + length <= this.ufiDevice.getMediumSize())
      {
        int bufferLen = this.dataBuffer.length;
        while (length > 0) {
          if (bufferLen < length) {
            curWrite = bufferLen;
          }
          else {
            curWrite = length;
          } 
          if (this.bSecretVMM == true) {
            curWrite = getDataEncry(this.dataBuffer, curWrite);
          }
          else {
            curWrite = getData(this.dataBuffer, curWrite);
          } 
          if (0 == curWrite) {
            TestPrint.println(3, "UFI Processor: Write -- address error(sensekey:INVALID FIELD IN COMMAND PACKET)");
            setSenseKeys(5, 36, 0, 0);
            return;
          } 
          if (this.ufiDevice.isWriteProtect())
          {
            throw new VMException(254);
          }
          this.ufiDevice.write(this.dataBuffer, startPos, curWrite);
          startPos += curWrite;
          length -= curWrite;
        } 
        setSenseKeys(0, 0, 0, 0);
      }
      else
      {
        TestPrint.println(3, "UFI Processor: Write -- address error(sensekey:LOGICAL BLOCK ADDRESS OUT OF RANGE)");
        setSenseKeys(5, 33, 0, 0);
      }
    } catch (VMException ie) {
      TestPrint.println(3, "UFI Processor : Write operation error! Error id:" + ie.getKey());
      switch (ie.getKey()) {
        case 250:
          TestPrint.println(3, "UFI Processor:Write -- Write error(ID CRC ERROR)");
          setSenseKeys(3, 16, 0, 0);
          break;
        case 253:
          TestPrint.println(3, "UFI Processor:Write -- Meida not present.");
          this.ufiDevice.setDeviceState(0);
          setSenseKeys(2, 58, 0, 0);
          break;
        case 254:
          TestPrint.println(3, "UFI Processor:Write -- Write protect");
          setSenseKeys(7, 39, 0, 0);
          break;
      } 
    } 
  }
  public void sendData(int dataLength, byte[] dataBuffer, boolean isLast) {
    if (dataLength <= 0) {
      return;
    }
    int off = 0;
    int curLength = dataLength;
    int curState = 1;
    int send_len = 0;
    byte[] curPack = null;
    byte[] curData = null;
    if (0 == dataLength && isLast) {
      curPack = ProtocolProcessor.ufiDataPak(1, 3, dataLength, this.commandID);
      this.sender.sendImmediate(curPack, 12);
    } 
    while (dataLength > 0 && !this.exitFlag) {
      if (ProtocolCode.FLOPPY_PACKET_SIZE < dataLength) {
        curLength = ProtocolCode.FLOPPY_PACKET_SIZE;
        curState = 1;
      }
      else {
        curLength = dataLength;
        if (isLast) {
          curState = 3;
        }
        else {
          curState = 1;
        } 
      } 
      if (this.bSecretVMM == true) {
        byte[] data = new byte[curLength];
        byte[] datalen = new byte[4];
        datalen[0] = (byte)(curLength >> 24 & 0xFF);
        datalen[1] = (byte)(curLength >> 16 & 0xFF);
        datalen[2] = (byte)(curLength >> 8 & 0xFF);
        datalen[3] = (byte)(curLength & 0xFF);
        System.arraycopy(dataBuffer, off, data, 0, curLength);
        byte[] temDes = AESHandler.encry_bytes(data, this.secretKey, this.secretIV, curLength);
        if (0 == temDes.length) {
          return;
        }
        curData = this.sender.getByteArr(12 + temDes.length + 4);
        ProtocolProcessor.ufiDataPak(curData, 0, 1, curState, temDes.length + 4, this.commandID);
        System.arraycopy(datalen, 0, curData, 12, 4);
        System.arraycopy(temDes, 0, curData, 16, temDes.length);
        send_len = 12 + temDes.length + 4;
      }
      else {
        curData = this.sender.getByteArr(12 + curLength);
        ProtocolProcessor.ufiDataPak(curData, 0, 1, curState, curLength, this.commandID);
        System.arraycopy(dataBuffer, off, curData, 12, curLength);
        send_len = 12 + curLength;
      } 
      while (!this.sender.send(curData, send_len) && !this.exitFlag) {
        try {
          Thread.sleep(100L);
        }
        catch (InterruptedException e) {}
      } 
      off += curLength;
      dataLength -= curLength;
    } 
  }
  public void commandFinish() {
    byte result = 0;
    if (0 != getSenseKey() && 3 != (this.command[0] & 0xFF))
    {
      result = 1;
    }
    byte[] cmpltArr = this.sender.getByteArr(12);
    ProtocolProcessor.ufiCmpltPak(cmpltArr, 0, result, this.commandID);
    this.sender.sendImmediate(cmpltArr, 12);
  }
}
