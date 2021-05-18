package com.huawei.vm.console.utilsV1;
public class DataElement
{
  public static final int COMMAND = 0;
  public static final int DATA = 1;
  private int type;
  private int id;
  private byte[] data;
  private int contentLength;
  public static final DataElement getComPakInstance(byte[] packet, int pakLength) {
    DataElement ele = null;
    if (null != packet && 12 == pakLength) {
      ele = new DataElement();
      ele.addCommand(packet, pakLength);
    } 
    return ele;
  }
  public static final DataElement getUSBRequestInstance(byte[] request, int ID, int reqLength) {
    DataElement ele = null;
    if (null != request && 12 == reqLength) {
      ele = new DataElement();
      ele.setId(ID);
      ele.addCommand(request, reqLength);
    } 
    return ele;
  }
  public static final DataElement getDataInstance(byte[] data, int dataLength) {
    DataElement ele = null;
    if (null != data && data.length > 0) {
      ele = new DataElement();
      ele.addData(data, dataLength);
    } 
    return ele;
  }
  public byte[] getContent() {
    return this.data;
  }
  public final void setId(int id) {
    this.id = id;
  }
  public final int getID() {
    return this.id;
  }
  public int getContentLength() {
    return this.contentLength;
  }
  private void addData(byte[] data, int dataLength) {
    this.type = 1;
    this.data = data;
    this.contentLength = dataLength;
  }
  private void addCommand(byte[] data, int dataLen) {
    this.type = 0;
    this.data = data;
    this.contentLength = dataLen;
  }
  public boolean isCommand() {
    return (0 == this.type);
  }
  public boolean isData() {
    return (1 == this.type);
  }
}
