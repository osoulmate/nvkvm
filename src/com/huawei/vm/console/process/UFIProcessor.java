package com.huawei.vm.console.process;
import com.huawei.vm.console.communication.CommunicationSender;
import com.huawei.vm.console.communication.ProtocolCode;
import com.huawei.vm.console.communication.ProtocolProcessor;
import com.huawei.vm.console.storage.impl.FloppyDriver;
import com.huawei.vm.console.utils.VMException;
import com.kvm.AESHandler;
import com.library.LoggerUtil;
public class UFIProcessor
  extends USBProcessor
  implements Runnable
{
  private FloppyDriver ufiDevice;
  protected final byte[] inquiryData = new byte[] { 0, Byte.MIN_VALUE, 0, 1, 31, 0, 0, 0, 86, 105, 114, 116, 117, 97, 108, 32, 70, 76, 79, 80, 80, 89, 32, 86, 77, 32, 49, 46, 49, 46, 48, 32, 32, 32, 32, 32 };
  protected final byte[] capacityList = new byte[] { 0, 0, 0, 16, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 11, 64, 0, 0, 2, 0 };
  public UFIProcessor(FloppyDriver floppy, CommunicationSender sender) {
    super(sender);
    this.ufiDevice = floppy;
  }
  public void run() {
    while (!this.exitFlag)
    {
      processCommand();
    }
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
        LoggerUtil.error(e.getClass().getName());
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
        doFormat();
        break;
      case 18:
        doInquiry();
        break;
      case 85:
        doModeSelect();
        break;
      case 90:
        doModeSense();
        break;
      case 30:
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
      case 43:
        setSenseKeys(0, 0, 0, 0);
        break;
      case 29:
        doSendDiagnostic();
        break;
      case 27:
        doStartStopUnit();
        break;
      case 0:
        doTestUnitRead();
        break;
      case 47:
        doVerify();
        break;
      case 42:
      case 46:
      case 170:
        doWrite();
        break;
      default:
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
      setSenseKeys(5, 26, 0, 0);
      return;
    } 
    if (this.ufiDevice.isWriteProtect()) {
      setSenseKeys(7, 39, 0, 0);
    }
    else if (this.ufiDevice.getTotalBlocks() == ProtocolCode.getInt32bits(paramList, 5) && this.ufiDevice
      .getBlockLength() == ProtocolCode.getInt24bits(paramList, 10)) {
      int track = this.command[2] & 0xFF;
      int side = paramList[1] & 0x1;
      try {
        this.ufiDevice.formatUnit(2, track, track, side, side);
        setSenseKeys(0, 0, 0, 0);
      }
      catch (VMException e) {
        setSenseKeys(3, 49, 1, e.getKey());
      }
    } else {
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
      if (startPos >= 0L && startPos < this.ufiDevice.getMediumSize())
      {
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
          setSenseKeys(5, 33, 0, 0);
        }
      }
      else
      {
        setSenseKeys(5, 33, 0, 0);
      }
    } catch (VMException e) {
      switch (e.getKey()) {
        case 250:
          setSenseKeys(3, 16, 0, (int)(startPos / this.ufiDevice.getBlockLength()));
          break;
        case 253:
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
      if (startPos >= 0L && startPos < this.ufiDevice.getMediumSize() && startPos + length <= this.ufiDevice
        .getMediumSize())
      {
        int bufferLen = this.dataBuffer.length;
        while (length > 0) {
          if (bufferLen < length) {
            curWrite = bufferLen;
          }
          else {
            curWrite = length;
          } 
          if (0 == this.sender.getConsole().getVmm_compress_state()) {
            curWrite = getData(this.dataBuffer, curWrite);
          }
          else {
            curWrite = getDataEncry(this.dataBuffer, curWrite);
          } 
          if (0 == curWrite) {
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
        setSenseKeys(5, 33, 0, 0);
      }
    } catch (VMException ie) {
      switch (ie.getKey()) {
        case 250:
          setSenseKeys(3, 16, 0, 0);
          break;
        case 253:
          this.ufiDevice.setDeviceState(0);
          setSenseKeys(2, 58, 0, 0);
          break;
        case 254:
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
    int curLength = 0;
    int curState = 1;
    int send_len = 0;
    byte[] curData = null;
    boolean flag = false;
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
      if (this.sender.getConsole().getVmm_compress_state() == 1) {
        byte[] datalen = new byte[4];
        byte[] data = new byte[curLength];
        datalen[0] = (byte)(curLength >> 24 & 0xFF);
        datalen[1] = (byte)(curLength >> 16 & 0xFF);
        datalen[2] = (byte)(curLength >> 8 & 0xFF);
        datalen[3] = (byte)(curLength & 0xFF);
        System.arraycopy(dataBuffer, off, data, 0, curLength);
        byte[] temDes = AESHandler.vmmencry(data, this.sender.getConsole().getSecretKey(), curLength, this.sender.getConsole().getSecretIvBMC());
        if (temDes == null || 0 == temDes.length) {
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
      flag = this.sender.send(curData, send_len);
      while (!flag && !this.exitFlag) {
        try {
          Thread.sleep(100L);
        }
        catch (InterruptedException ie) {}
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
