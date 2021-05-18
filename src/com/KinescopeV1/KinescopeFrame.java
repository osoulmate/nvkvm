package com.KinescopeV1;
import java.awt.Image;
public class KinescopeFrame
{
  private int width;
  private int height;
  private long data;
  private int seqFrame;
  private boolean isIFrame;
  private byte[] sourceData;
  private byte[] previImage;
  private Image image;
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
    return this.previImage;
  }
  public void setPreviImage(byte[] previImage) {
    this.previImage = previImage;
  }
  public long getData() {
    return this.data;
  }
  public void setData(long data) {
    this.data = data;
  }
  public byte[] getSourceData() {
    return this.sourceData;
  }
  public void setSourceData(byte[] sourceData) {
    this.sourceData = sourceData;
  }
  public int getSeqFrame() {
    return this.seqFrame;
  }
  public void setSeqFrame(int seqFrame) {
    this.seqFrame = seqFrame;
  }
}
