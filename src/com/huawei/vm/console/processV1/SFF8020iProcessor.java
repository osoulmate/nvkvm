package com.huawei.vm.console.processV1;
import com.huawei.vm.console.communicationV1.CommunicationSender;
import com.huawei.vm.console.communicationV1.ProtocolCode;
import com.huawei.vm.console.communicationV1.ProtocolProcessor;
import com.huawei.vm.console.storageV1.impl.CDROMDriver;
import com.huawei.vm.console.utilsV1.TestPrint;
import com.huawei.vm.console.utilsV1.VMException;
import com.kvmV1.AESHandler;
public class SFF8020iProcessor
  extends USBProcessor
  implements Runnable
{
  private final CDROMDriver cdrom;
  private int readErrNum = 0;
  private long readErrAreaBegin = 0L;
  private long readErrAreaEnd = 0L;
  public static final int READ_ERR_LIMIT = 2;
  protected final byte[] inquiryData = new byte[] { 5, Byte.MIN_VALUE, 0, 33, 31, 0, 0, 0, 72, 85, 65, 87, 69, 73, 32, 32, 68, 86, 68, 45, 82, 79, 77, 32, 86, 77, 32, 49, 46, 49, 46, 48, 32, 50, 50, 53 };
  protected byte[] dataCacheBuffer = new byte[8192];
  int cacheBlockNum;
  long cacheLba; int cacheReadLen; public void run() { TestPrint.println(1, "SFF processor start"); while (!this.exitFlag)
      processCommand();  TestPrint.println(1, Thread.currentThread().getName() + " close. arr num:" + this.dataArray.getBigArrNum() + "; small arr num:" + this.dataArray.getSmallArrNum() + "; data num :" + this.dataArray.getDataNum()); } private void checkChangeDisk() { if (this.cdrom.isChangeDisk())
      try { if (this.cdrom.isEject()) { this.cdrom.eject(); } else { this.cdrom.insert(); }  } catch (VMException e) { TestPrint.println(3, "Error happened while changing disk."); }   } public SFF8020iProcessor(CDROMDriver cdrom, CommunicationSender sender) { super(sender);
    this.cacheBlockNum = 0;
    this.cacheLba = 0L;
    this.cacheReadLen = 0; this.cdrom = cdrom; }
  public void processCommand() { int paramListLength; getCommand(); checkChangeDisk(); if (this.exitFlag)
      return;  this.cdrom.refreshState(); switch (this.command[0] & 0xFF) { case 18: TestPrint.println(1, "SFF processor:INQUIRY"); doInquiry(); break;case 189: TestPrint.println(1, "SFF processor:MECHANISM_STATUS"); setSenseKeys(5, 36, 0, 0); break;case 85: TestPrint.println(1, "SFF processor:MODE_SELECT"); paramListLength = ProtocolCode.getInt16bits(this.command, 8); if (paramListLength != getData(this.dataBuffer, paramListLength)) { setSenseKeys(5, 26, 0, 0); break; }  setSenseKeys(0, 0, 0, 0); break;case 90: doModeSense(); break;case 30: TestPrint.println(1, "SFF processor:PREVENT_ALLOW_MEDIUM_REMOVAL"); doPreventAllowMediumRemoval(this.command); break;case 40: case 168: doRead(this.command); break;case 37: doReadCapacity(); break;case 190: doReadCD(); break;case 185: doReadMSF(); break;case 68: doReadHeader(); break;
      case 66: doReadSubChannel(); break;
      case 67: doReadToc(this.command); break;
      case 3: doRequsetSense(); break;
      case 43: TestPrint.println(1, "SFF processor:SEEK_10"); setSenseKeys(0, 0, 0, 0); break;
      case 78: TestPrint.println(1, "SFF processor:STOP_PLAY_SCAN"); setSenseKeys(5, 36, 0, 0); break;
      case 27: TestPrint.println(1, "SFF processor:START_STOP"); doStartStopUnit(this.command); break;
      case 0: doTestUnitRead(); break;
      default: TestPrint.println(2, "SFF processor:Unknown command" + this.command[0] + 'ÿ'); setSenseKeys(5, 36, 0, 0); break; }  commandFinish(); } private void doRead(byte[] command) { long lba = ProtocolCode.getInt32bits(command, 3);
    int blockNum = 0;
    if (40 == command[0]) {
      blockNum = ProtocolCode.getInt16bits(command, 8);
    }
    else {
      blockNum = ProtocolCode.getInt32bits(command, 7);
    } 
    int length = blockNum * 2048;
    if (this.cacheBlockNum != 0 && this.cacheBlockNum == blockNum && this.cacheLba != 0L && this.cacheLba == lba) {
      sendData(length, this.dataBuffer2, true);
      setSenseKeys(0, 0, 0, 0);
    }
    else {
      int curRead = read(lba, blockNum, false);
      sendData(curRead, this.dataBuffer2, true);
      setSenseKeys(0, 0, 0, 0);
    } 
    this.cacheBlockNum = blockNum;
    this.cacheLba = lba + blockNum;
    this.cacheReadLen = read(this.cacheLba, this.cacheBlockNum, true); } private void doInquiry() { sendData(this.inquiryData.length, this.inquiryData, true); setSenseKeys(0, 0, 0, 0); }
  private void doModeSense() { int length = this.cdrom.modeSense(this.dataBuffer, this.command[2] >> 6 & 0xFF, this.command[2] & 0x3F); if (0 == length) { setSenseKeys(5, 36, 0, 0); }
    else
    { setSenseKeys(0, 0, 0, 0); sendData(length, this.dataBuffer, true); }
     }
  private void doPreventAllowMediumRemoval(byte[] command) { boolean isPrevent = false; if (1 == (command[4] & 0x1))
      isPrevent = true;  boolean support = this.cdrom.preventAllowMediumRemoval(isPrevent); if (support) {
      setSenseKeys(0, 0, 0, 0);
    } else {
      setSenseKeys(5, 36, 0, 0);
    }  }
  private int read(long lba, int blockNum, boolean cache) { long startPos = lba * 2048L;
    TestPrint.println(2, "SFF Processor: Read operation's start position:" + startPos + "; LBA--" + lba);
    int length = 0;
    int curRead = 0;
    length = blockNum * 2048;
    try {
      if (this.readErrNum >= 2 && this.readErrAreaBegin <= lba && lba < this.readErrAreaEnd)
      {
        if (!cache)
        {
          setSenseKeys(3, 16, 0, (int)(startPos / 2048L));
        }
        this.cacheBlockNum = 0;
        this.cacheLba = 0L;
      }
      else if (startPos >= 0L && startPos < this.cdrom.getMediumSize())
      {
        int readLength = 0;
        int bufferLen = this.dataBuffer2.length;
        if (bufferLen < length) {
          readLength = bufferLen;
        }
        else {
          readLength = length;
        } 
        curRead = this.cdrom.read(this.dataBuffer2, startPos, readLength);
        startPos += curRead;
        if (curRead == readLength) {
          length -= readLength;
        }
        else {
          length = -1;
        } 
        if (0 == length) {
          if (!cache)
          {
            setSenseKeys(0, 0, 0, 0);
          }
        }
        else {
          TestPrint.println(3, "SFF Processor: Read -- address error(sensekey:ILOGICAL BLOCK ADDRESS OUT OF RANGE)");
          if (!cache)
          {
            setSenseKeys(5, 33, 0, 0);
          }
          this.cacheBlockNum = 0;
          this.cacheLba = 0L;
        } 
        this.readErrNum = 0;
      }
      else
      {
        TestPrint.println(3, "SFF Processor: Read -- address error(sensekey:ILOGICAL BLOCK ADDRESS OUT OF RANGE)");
        if (!cache)
        {
          setSenseKeys(5, 33, 0, 0);
        }
        this.cacheBlockNum = 0;
        this.cacheLba = 0L;
      }
    } catch (VMException e) {
      this.cacheBlockNum = 0;
      this.cacheLba = 0L;
      switch (e.getKey()) {
        case 250:
          TestPrint.println(3, "SFF Processor: Read -- read error(sensekey:ID CRC ERROR)");
          if (!cache)
          {
            setSenseKeys(3, 16, 0, (int)(startPos / 2048L));
          }
          this.readErrNum++;
          this.readErrAreaBegin = lba;
          this.readErrAreaEnd = lba + blockNum;
          TestPrint.println(2, "SFF Processor: Read -- Error num:" + this.readErrNum + ";Error begin:" + this.readErrAreaBegin + ";Error End:" + this.readErrAreaEnd);
          break;
        case 251:
          TestPrint.println(3, "SFF Processor: Read -- address error(sensekey:ILOGICAL BLOCK ADDRESS OUT OF RANGE)");
          if (!cache)
          {
            setSenseKeys(5, 33, 0, 0);
          }
          break;
        case 253:
          TestPrint.println(3, "SFF Processor:Read -- media not present.");
          this.cdrom.setDeviceState(0);
          if (!cache)
          {
            setSenseKeys(2, 58, 0, 0);
          }
          break;
      } 
    } 
    return curRead; }
  private void doReadCapacity() {
    switch (this.cdrom.testUnitReady()) {
      case 0:
        setSenseKeys(2, 58, 0, 0);
        break;
      case 2:
        setSenseKeys(6, 40, 0, 0);
        break;
      case 3:
        try {
          long mediumSize = this.cdrom.getMediumSize();
          long lba = mediumSize / 2048L - 1L;
          this.dataBuffer[0] = (byte)(int)(lba >> 24L & 0xFFL);
          this.dataBuffer[1] = (byte)(int)(lba >> 16L & 0xFFL);
          this.dataBuffer[2] = (byte)(int)(lba >> 8L & 0xFFL);
          this.dataBuffer[3] = (byte)(int)(lba & 0xFFL);
          this.dataBuffer[4] = 0;
          this.dataBuffer[5] = 0;
          this.dataBuffer[6] = 8;
          this.dataBuffer[7] = 0;
          setSenseKeys(0, 0, 0, 0);
          sendData(8, this.dataBuffer, true);
        }
        catch (VMException e) {
          this.cdrom.setDeviceState(0);
          setSenseKeys(2, 58, 0, 0);
        } 
        break;
    } 
  }
  private void doReadCD() {
    TestPrint.println(1, "Read CD requested!");
    setSenseKeys(5, 36, 0, 0);
  }
  private void doReadMSF() {
    TestPrint.println(1, "Read MSF requested!");
    setSenseKeys(5, 36, 0, 0);
  }
  private void doReadHeader() {
    TestPrint.println(1, "Read header requested!");
    setSenseKeys(5, 36, 0, 0);
  }
  private void doReadSubChannel() {
    TestPrint.println(1, "Read sub-channel requested!");
    setSenseKeys(5, 36, 0, 0);
  }
  private void doReadToc(byte[] command) {
    boolean isMSF = ((command[1] & 0x2) == 2);
    int format = command[2] & 0x7;
    if (0 == format)
    {
      format = command[9] >> 6 & 0x3;
    }
    int allocLength = ProtocolCode.getInt16bits(command, 8);
    int startTrack = command[6];
    int tocDataLen = 0;
    try {
      tocDataLen = this.cdrom.readTOC(this.dataBuffer, isMSF, format, startTrack);
      setSenseKeys(0, 0, 0, 0);
    }
    catch (VMException e) {
      if (252 == e.getKey()) {
        setSenseKeys(5, 36, 0, 0);
      }
      else {
        this.cdrom.setDeviceState(0);
        setSenseKeys(2, 58, 0, 0);
      } 
    } 
    if (allocLength < tocDataLen)
    {
      tocDataLen = allocLength;
    }
    sendData(tocDataLen, this.dataBuffer, true);
  }
  private void doRequsetSense() {
    byte[] senseData = this.senseData;
    sendData(senseData.length, senseData, true);
  }
  private void doStartStopUnit(byte[] command) {
    boolean isEject = false;
    boolean isStart = false;
    if (2 == (command[4] & 0x2))
    {
      isEject = true;
    }
    if (1 == (command[4] & 0x1))
    {
      isStart = true;
    }
    try {
      this.cdrom.startStopUnit(isEject, isStart);
      setSenseKeys(0, 0, 0, 0);
    }
    catch (VMException e) {
      setSenseKeys(5, 36, 0, 0);
    } 
  }
  private void doTestUnitRead() {
    int state = this.cdrom.testUnitReady();
    switch (state) {
      case 0:
        this.cdrom.setDeviceState(0);
        setSenseKeys(2, 58, 0, 0);
        this.readErrNum = 0;
        return;
      case 2:
        TestPrint.println(2, "SFF Processor: media changed.");
        this.cdrom.setDeviceState(2);
        setSenseKeys(6, 40, 0, 0);
        this.readErrNum = 0;
        return;
      case 3:
        this.cdrom.setDeviceState(3);
        setSenseKeys(0, 0, 0, 0);
        return;
      case 5:
        this.cdrom.setDeviceState(0);
        setSenseKeys(2, 48, 0, 0);
        this.readErrNum = 0;
        return;
      case 4:
        this.cdrom.setDeviceState(2);
        setSenseKeys(2, 4, 0, 0);
        this.readErrNum = 0;
        return;
    } 
    this.cdrom.setDeviceState(0);
    setSenseKeys(2, 58, 0, 0);
    this.readErrNum = 0;
  }
  public void sendData(int dataLength, byte[] dataBuffer, boolean isLast) {
    int off = 0;
    int curLength = dataLength;
    byte[] curPack = null;
    byte[] curData = null;
    int send_len = 0;
    if (0 == dataLength && isLast) {
      curPack = ProtocolProcessor.sffDataPak(1, 3, dataLength, this.commandID);
      this.sender.sendImmediate(curPack, 12);
    } 
    while (dataLength > 0 && !this.exitFlag) {
      if (ProtocolCode.CDROM_PACKET_SIZE < dataLength) {
        curLength = ProtocolCode.CDROM_PACKET_SIZE;
        curPack = ProtocolProcessor.sffDataPak(1, 1, curLength, this.commandID);
      }
      else {
        curLength = dataLength;
        if (isLast) {
          curPack = ProtocolProcessor.sffDataPak(1, 3, curLength, this.commandID);
        }
        else {
          curPack = ProtocolProcessor.sffDataPak(1, 1, curLength, this.commandID);
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
        int ComLen = temDes.length + 4;
        curPack[4] = (byte)(ComLen >> 24 & 0xFF);
        curPack[5] = (byte)(ComLen >> 16 & 0xFF);
        curPack[6] = (byte)(ComLen >> 8 & 0xFF);
        curPack[7] = (byte)(ComLen & 0xFF);
        curData = this.sender.getByteArr(12 + temDes.length + 4);
        System.arraycopy(curPack, 0, curData, 0, 12);
        System.arraycopy(datalen, 0, curData, 12, 4);
        System.arraycopy(temDes, 0, curData, 16, temDes.length);
        send_len = 12 + temDes.length + 4;
      }
      else {
        curData = this.sender.getByteArr(12 + curLength);
        System.arraycopy(curPack, 0, curData, 0, 12);
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
    byte[] cmpltPak = this.sender.getByteArr(12);
    ProtocolProcessor.sffCmpltPak(cmpltPak, 0, result, this.commandID);
    this.sender.sendImmediate(cmpltPak, 12);
  }
}
