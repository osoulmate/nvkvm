package com.kvm;
public class UnPackData
{
  private String kvmType = "";
  private byte[] sourceData = null;
  private static final int KVM_DATA_TYPE = 2;
  public static final byte PRESENT_BLADE = 1;
  public static final byte RAPCONNECT_BLADE = 33;
  public static final byte IMAGE_DATA = 2;
  public static final byte KEY_STATE = 4;
  public static final byte BLADE_STATE = 21;
  public static final byte CHANNEL_SWITCH = 29;
  public static final byte CONNECT_STATE = 8;
  public static final byte VMM_CODEKEY_REPORT = 50;
  public static final byte VMM_PORT_REPORT = 54;
  public static final byte KVM_KEY_SET = 64;
  public static final byte VMM_CODEKEY_REPORT_LEN = 20;
  public static final byte KVM_SUITE_LIST = 67;
  public static final byte NOT_PRI = 81;
  public static final byte MOUSE_MODE = 37;
  public static final byte DQT_MODE = 40;
  public void setkvmType(byte[] data) {
    this.sourceData = (byte[])data.clone();
    switch (data[2]) {
      case 1:
        this.kvmType = "1";
        break;
      case 2:
        this.kvmType = "2";
        break;
      case 4:
        this.kvmType = "4";
        break;
      case 21:
        this.kvmType = "21";
        break;
      case 29:
        this.kvmType = "29";
        break;
      case 33:
        this.kvmType = "33";
        break;
      case 8:
        this.kvmType = "8";
        break;
      case 37:
        this.kvmType = "25";
        break;
      case 40:
        this.kvmType = "28";
        break;
      case 64:
        this.kvmType = "40";
        break;
      case 50:
        this.kvmType = "50";
        break;
      case 67:
        this.kvmType = "43";
        break;
      case 81:
        this.kvmType = "51";
        break;
      case 54:
        this.kvmType = "54";
        break;
    } 
  }
  public byte[] getData() {
    byte[] tempData = null;
    if ("1".equals(this.kvmType))
    {
      tempData = presentBladeInfo();
    }
    if ("2".equals(this.kvmType))
    {
      tempData = imageData();
    }
    if ("4".equals(this.kvmType))
    {
      tempData = keySate();
    }
    if ("21".equals(this.kvmType))
    {
      tempData = bladeSate();
    }
    if ("29".equals(this.kvmType))
    {
      tempData = channelSwitch();
    }
    if ("33".equals(this.kvmType)) {
      Debug.println("enter UnPackData.getData 33");
      tempData = rapCloseBlade();
    } 
    if ("8".equals(this.kvmType))
    {
      tempData = connectSate();
    }
    if ("25".equals(this.kvmType))
    {
      tempData = mouseModeSate();
    }
    if ("28".equals(this.kvmType))
    {
      tempData = dqtModeSate();
    }
    if ("40".equals(this.kvmType))
    {
      tempData = set_kvm_key();
    }
    if ("50".equals(this.kvmType))
    {
      tempData = vmmCodeKeyNego();
    }
    if ("43".equals(this.kvmType))
    {
      tempData = kvm_consultation();
    }
    if ("51".equals(this.kvmType))
    {
      tempData = kvm_no_pri();
    }
    if ("54".equals(this.kvmType))
    {
      tempData = vmmPort();
    }
    return tempData;
  }
  private byte[] presentBladeInfo() {
    byte[] buf = new byte[3];
    buf[0] = 1;
    buf[1] = this.sourceData[3];
    buf[2] = this.sourceData[4];
    return buf;
  }
  private byte[] imageData() {
    byte[] buf = new byte[this.sourceData.length - 2];
    buf[0] = 2;
    System.arraycopy(this.sourceData, 3, buf, 1, buf.length - 1);
    return buf;
  }
  private byte[] keySate() {
    byte[] buf = new byte[3];
    buf[0] = 4;
    buf[1] = this.sourceData[3];
    buf[2] = this.sourceData[4];
    return buf;
  }
  private byte[] bladeSate() {
    byte[] buf = new byte[this.sourceData.length - 2];
    System.arraycopy(this.sourceData, 2, buf, 0, buf.length);
    return buf;
  }
  private byte[] rapCloseBlade() {
    byte[] buf = new byte[2];
    buf[0] = 33;
    buf[1] = this.sourceData[3];
    return buf;
  }
  private byte[] channelSwitch() {
    byte[] buf = new byte[2];
    buf[0] = 29;
    buf[1] = this.sourceData[3];
    return buf;
  }
  private byte[] connectSate() {
    byte[] buf = new byte[3];
    buf[0] = 8;
    buf[1] = this.sourceData[3];
    buf[2] = this.sourceData[4];
    return buf;
  }
  private byte[] mouseModeSate() {
    byte[] buf = new byte[3];
    buf[0] = 37;
    buf[1] = this.sourceData[3];
    buf[2] = this.sourceData[4];
    return buf;
  }
  private byte[] dqtModeSate() {
    byte[] buf = new byte[3];
    buf[0] = 40;
    buf[1] = this.sourceData[3];
    buf[2] = this.sourceData[4];
    return buf;
  }
  private byte[] set_kvm_key() {
    byte[] buf = new byte[this.sourceData.length - 2];
    System.arraycopy(this.sourceData, 2, buf, 0, buf.length);
    return buf;
  }
  private byte[] vmmCodeKeyNego() {
    byte[] buf = new byte[this.sourceData.length - 2];
    System.arraycopy(this.sourceData, 2, buf, 0, buf.length);
    return buf;
  }
  private byte[] kvm_no_pri() {
    byte[] buf = new byte[3];
    buf[0] = 81;
    buf[1] = this.sourceData[3];
    buf[2] = this.sourceData[4];
    return buf;
  }
  private byte[] kvm_consultation() {
    byte[] buf = new byte[this.sourceData.length - 2];
    System.arraycopy(this.sourceData, 2, buf, 0, buf.length);
    return buf;
  }
  private byte[] vmmPort() {
    byte[] buf = new byte[this.sourceData.length - 2];
    System.arraycopy(this.sourceData, 2, buf, 0, buf.length);
    return buf;
  }
}
