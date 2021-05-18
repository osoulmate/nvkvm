package com.kvmV1;
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
  public static final byte VMM_CODEKEY_REPORT_KEY_LEN = 20;
  public static final byte VMM_CODEKEY_REPORT_SALT_LEN = 16;
  public static final byte MOUSE_MODE = 37;
  public static final byte SECRET_KEY_NEGO = 64;
  public void setkvmType(byte[] data) {
    this.sourceData = data;
    switch (data[2]) {
      case 1:
        this.kvmType = "1";
        return;
      case 2:
        this.kvmType = "2";
        return;
      case 4:
        this.kvmType = "4";
        return;
      case 21:
        this.kvmType = "21";
        return;
      case 29:
        this.kvmType = "29";
        return;
      case 33:
        this.kvmType = "33";
        return;
      case 8:
        this.kvmType = "8";
        return;
      case 37:
        this.kvmType = "25";
        return;
      case 64:
        this.kvmType = "40";
        return;
      case 50:
        this.kvmType = "50";
        return;
    } 
    Debug.println("error");
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
    if ("40".equals(this.kvmType))
    {
      tempData = secretkeyNego();
    }
    if ("50".equals(this.kvmType))
    {
      tempData = vmmCodeKeyNego();
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
  private byte[] vmmCodeKeyNego() {
    byte[] buf = new byte[this.sourceData.length - 2];
    System.arraycopy(this.sourceData, 2, buf, 0, buf.length);
    return buf;
  }
  private byte[] secretkeyNego() {
    byte[] buf = new byte[this.sourceData.length - 2];
    System.arraycopy(this.sourceData, 2, buf, 0, buf.length);
    return buf;
  }
}
