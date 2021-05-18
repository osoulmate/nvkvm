package com.library.decoder;
import java.awt.Image;
public class ImageBlock
{
  int id = 0;
  int blockType = 0;
  int blockRleType = 0;
  boolean isFill = false;
  boolean isFrontSame = false;
  boolean isAboveSame = false;
  boolean isLeftSame = false;
  int X = 0;
  int Y = 0;
  int width = 64;
  int height = 64;
  int cutWidth = 64;
  int cutHeight = 64;
  Image image = null;
  byte[] pixColors = null;
  int[] zipDataBak = new int[10];
  public int getId() {
    return this.id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public int getBlockType() {
    return this.blockType;
  }
  public void setBlockType(int blockType) {
    this.blockType = blockType;
  }
  public int getBlockRleType() {
    return this.blockRleType;
  }
  public void setBlockRleType(int blockRleType) {
    this.blockRleType = blockRleType;
  }
  public boolean isFill() {
    return this.isFill;
  }
  public void setFill(boolean isFill) {
    this.isFill = isFill;
  }
  public boolean isFrontSame() {
    return this.isFrontSame;
  }
  public void setFrontSame(boolean isFrontSame) {
    this.isFrontSame = isFrontSame;
  }
  public boolean isAboveSame() {
    return this.isAboveSame;
  }
  public void setAboveSame(boolean isAboveSame) {
    this.isAboveSame = isAboveSame;
  }
  public boolean isLeftSame() {
    return this.isLeftSame;
  }
  public void setLeftSame(boolean isLeftSame) {
    this.isLeftSame = isLeftSame;
  }
  public int getX() {
    return this.X;
  }
  public void setX(int x) {
    this.X = x;
  }
  public int getY() {
    return this.Y;
  }
  public void setY(int y) {
    this.Y = y;
  }
  public int getWidth() {
    return this.width;
  }
  public void setWidth(int width) {
    this.width = width;
  }
  public int getHeight() {
    return this.height;
  }
  public void setHeight(int height) {
    this.height = height;
  }
  public int getCutWidth() {
    return this.cutWidth;
  }
  public void setCutWidth(int cutWidth) {
    this.cutWidth = cutWidth;
  }
  public int getCutHeight() {
    return this.cutHeight;
  }
  public void setCutHeight(int cutHeight) {
    this.cutHeight = cutHeight;
  }
  public Image getImage() {
    return this.image;
  }
  public void setImage(Image image) {
    this.image = image;
  }
  public byte[] getPixColors() {
    if (null != this.pixColors)
    {
      return (byte[])this.pixColors.clone();
    }
    return new byte[0];
  }
  public void setPixColors(byte[] pixColors) {
    if (null != pixColors) {
      this.pixColors = (byte[])pixColors.clone();
    }
    else {
      this.pixColors = null;
    } 
  }
  public boolean isRleType() {
    return (this.blockType == 0);
  }
  public boolean isJPEGType() {
    return (this.blockType == 1);
  }
}
