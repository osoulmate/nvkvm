package com.kvm;
public class UDFExtendFile
{
  private boolean isExtendData;
  private long position;
  private byte[] content = new byte[0];
  private long fileLen;
  private long fileActalLen;
  public long getFileActalLen() {
    return this.fileActalLen;
  }
  public void setFileActalLen(long fileActalLen) {
    this.fileActalLen = fileActalLen;
  }
  public long getFileLen() {
    return this.fileLen;
  }
  public void setFileLen(long fileLen) {
    this.fileLen = fileLen;
  }
  public byte[] getContent() {
    return (byte[])this.content.clone();
  }
  public void setContent(byte[] content) {
    if (content != null) {
      this.content = (byte[])content.clone();
    }
    else {
      this.content = null;
    } 
  }
  public boolean isExtendData() {
    return this.isExtendData;
  }
  public void setExtendData(boolean isExtendData) {
    this.isExtendData = isExtendData;
  }
  public long getPosition() {
    return this.position;
  }
  public void setPosition(long position) {
    this.position = position;
  }
}
