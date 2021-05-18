package com.huawei.vm.console.process;
import com.huawei.vm.console.communication.CommunicationSender;
import com.huawei.vm.console.utils.DataArray;
import com.huawei.vm.console.utils.DataElement;
import com.kvm.AESHandler;
public abstract class USBProcessor
{
  protected DataArray dataArray;
  protected int commandID;
  protected byte[] command = null;
  protected byte[] dataBuffer = null;
  protected byte[] dataBuffer2 = null;
  protected final byte[] senseData = new byte[] { 112, 0, 0, 6, 0, 0, 0, 10, 0, 0, 0, 0, 41, 0, 0, 0, 0, 0 };
  protected boolean exitFlag = false;
  protected CommunicationSender sender;
  public static final int USB_REQUEST_LENGTH = 12;
  public static final int FORMAT_UNIT = 4;
  public static final int INQUIRY = 18;
  public static final int START_STOP = 27;
  public static final int MODE_SELECT = 85;
  public static final int MODE_SENSE = 90;
  public static final int PREVENT_ALLOW_MEDIUM_REMOVAL = 30;
  public static final int READ_10 = 40;
  public static final int READ_12 = 168;
  public static final int READ_CAPACITY = 37;
  public static final int READ_FORMAT_CAPACITY = 35;
  public static final int REQUEST_SENSE = 3;
  public static final int REZERO_UNIT = 1;
  public static final int SEEK_10 = 43;
  public static final int SEND_DIAGNOSTIC = 29;
  public static final int TEST_UNIT_READY = 0;
  public static final int TEST_UNIT_READY_EXP = 74;
  public static final int VERIFY = 47;
  public static final int WRITE_10 = 42;
  public static final int WRITE_12 = 170;
  public static final int WRITE_AND_VERIFY = 46;
  public static final int MECHANISM_STATUS = 189;
  public static final int READ_CD = 190;
  public static final int READ_CD_MSF = 185;
  public static final int READ_HEADER = 68;
  public static final int READ_SUB_CHANNEL = 66;
  public static final int READ_TOC = 67;
  public static final int STOP_PLAY_SCAN = 78;
  public USBProcessor(CommunicationSender sender) {
    this.dataArray = new DataArray();
    this.dataArray.initArrList(15, 5);
    this.sender = sender;
    this.command = new byte[12];
    this.dataBuffer = new byte[32768];
    this.dataBuffer2 = new byte[131072];
  }
  public DataArray getArray() {
    return this.dataArray;
  }
  public abstract void processCommand();
  public int getData(byte[] dataBuffer, int length) {
    int off = 0;
    byte[] temData = null;
    int temDataL = 0;
    DataElement curElement = null;
    while (!this.exitFlag && length > 0) {
      try {
        curElement = this.dataArray.getFirst();
        if (null == curElement) {
          Thread.sleep(100L);
          continue;
        } 
        if (curElement.isData()) {
          temDataL = curElement.getContentLength();
          temData = curElement.getContent();
          if (temDataL > length) {
            off = 0;
            break;
          } 
          this.dataArray.getAndRemoveFirst();
          System.arraycopy(temData, 0, dataBuffer, off, temDataL);
          this.dataArray.release(temData);
          off += temDataL;
          length -= temDataL;
          continue;
        } 
        off = 0;
        break;
      } catch (InterruptedException ie) {}
    } 
    if (this.exitFlag)
    {
      off = 0;
    }
    return off;
  }
  public int getDataEncry(byte[] dataBuffer, int length) {
    DataElement curElement = null;
    int off = 0;
    byte[] temData = null;
    int temDataL = 0;
    while (!this.exitFlag && length > 0) {
      try {
        curElement = this.dataArray.getFirst();
        if (null == curElement) {
          Thread.sleep(100L);
          continue;
        } 
        if (curElement.isData()) {
          temData = curElement.getContent();
          temDataL = curElement.getContentLength();
          int real_len = (temData[0] & 0xFF) << 24 | (temData[1] & 0xFF) << 16 | (temData[2] & 0xFF) << 8 | temData[3] & 0xFF;
          if (real_len > length) {
            off = 0;
            break;
          } 
          this.dataArray.getAndRemoveFirst();
          byte[] data = new byte[temDataL - 4];
          System.arraycopy(temData, 4, data, 0, temDataL - 4);
          byte[] decryData = AESHandler.vmm_decry(data, this.sender.getConsole().getSecretKey(), temDataL - 4, this.sender.getConsole().getSecretIvBMC());
          System.arraycopy(decryData, 0, dataBuffer, off, real_len);
          this.dataArray.release(temData);
          off += real_len;
          length -= real_len;
          continue;
        } 
        off = 0;
        break;
      } catch (InterruptedException ie) {}
    } 
    if (this.exitFlag)
    {
      off = 0;
    }
    return off;
  }
  public void getCommand() {
    DataElement curElement = null;
    while (!this.exitFlag) {
      curElement = this.dataArray.getAndRemoveFirstByBlock();
      if (curElement != null && curElement.isCommand()) {
        byte[] arrTemp = curElement.getContent();
        System.arraycopy(arrTemp, 0, this.command, 0, 12);
        this.dataArray.release(arrTemp);
        this.commandID = curElement.getUsbID();
        break;
      } 
    } 
  }
  protected void setSenseKeys(int senseKey, int ASC, int ASCQ, int information) {
    if (0 == information) {
      this.senseData[0] = 112;
      this.senseData[3] = 0;
      this.senseData[4] = 0;
      this.senseData[5] = 0;
      this.senseData[6] = 0;
    }
    else {
      this.senseData[0] = -16;
      this.senseData[3] = (byte)(information >> 24 & 0xFF);
      this.senseData[4] = (byte)(information >> 16 & 0xFF);
      this.senseData[5] = (byte)(information >> 8 & 0xFF);
      this.senseData[6] = (byte)(information & 0xFF);
    } 
    this.senseData[2] = (byte)senseKey;
    this.senseData[12] = (byte)ASC;
    this.senseData[13] = (byte)ASCQ;
  }
  public byte getSenseKey() {
    return this.senseData[2];
  }
  public abstract void sendData(int paramInt, byte[] paramArrayOfbyte, boolean paramBoolean);
  public abstract void commandFinish();
  public void setExit() {
    this.exitFlag = true;
  }
}
