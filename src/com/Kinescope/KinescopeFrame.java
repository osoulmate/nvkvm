package com.Kinescope;
import java.awt.Image;
public class KinescopeFrame
{
  private int width;
  private int height;
  private long data;
  private int seqFrame;
  private int dqt = 0;
  private boolean isIFrame;
  private byte[] sourceData;
  private byte[] previImage;
  private Image image;
  public int getDqt() {
    return this.dqt;
  }
  public void setDqt(int dqt) {
    this.dqt = dqt;
  }
  public int getHeight() {
    return this.height;
  }
  public void setHeight(int height) {
    this.height = height;
  }
  public boolean isIFrame() {
    return this.isIFrame;
  }
  public void setIFrame(boolean isIFrame) {
    this.isIFrame = isIFrame;
  }
  public int getWidth() {
    return this.width;
  }
  public void setWidth(int width) {
    this.width = width;
  }
  public Image getImage() {
    return this.image;
  }
  public void setImage(Image image) {
    this.image = image;
  }
  public byte[] getPreviImage() {
    if (null != this.previImage)
    {
      return (byte[])this.previImage.clone();
    }
    return new byte[0];
  }
  public void setPreviImage(byte[] previImage) {
    if (null != previImage) {
      this.previImage = (byte[])previImage.clone();
    }
    else {
      this.previImage = null;
    } 
  }
  public long getData() {
    return this.data;
  }
  public void setData(long data) {
    this.data = data;
  }
  public byte[] getSourceData() {
    if (null != this.sourceData)
    {
      return (byte[])this.sourceData.clone();
    }
    return new byte[0];
  }
  public void setSourceData(byte[] sourceData) {
    if (null != sourceData) {
      this.sourceData = (byte[])sourceData.clone();
    }
    else {
      this.sourceData = null;
    } 
  }
  public int getSeqFrame() {
    return this.seqFrame;
  }
  public void setSeqFrame(int seqFrame) {
    this.seqFrame = seqFrame;
  }
}
