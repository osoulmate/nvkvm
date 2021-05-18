package com.library.decoder;
import com.library.LibException;
import com.library.LoggerUtil;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
public class RLEJPEGUtil
  extends Component
{
  private static final long serialVersionUID = 1L;
  public int byteToIntCon(byte[] bytesrc, int offset, int length) {
    int intdes = 0;
    try {
      for (int i = offset; i <= offset + length - 1; i++)
      {
        intdes = (intdes << 8) + (bytesrc[i] & 0xFF);
      }
    }
    catch (ArrayIndexOutOfBoundsException e) {
      intdes = 0;
    } 
    return intdes;
  }
  public final BufferedImage getUnzipImg(byte[] pixels, int imageWidth, int imageHeight) {
    DirectColorModel cm = new DirectColorModel(8, 7, 56, 192);
    MemoryImageSource memoryimagesource = new MemoryImageSource(imageWidth, imageHeight, cm, pixels, 0, imageWidth);
    Image imageBuf = null;
    try {
      memoryimagesource.setAnimated(true);
      memoryimagesource.setFullBufferUpdates(true);
      imageBuf = createImage(memoryimagesource);
      memoryimagesource.newPixels();
    }
    catch (NoSuchMethodError e) {
      LoggerUtil.error(e.getClass().getName());
    } 
    BufferedImage changedImage = new BufferedImage(imageWidth, imageHeight, 5);
    Graphics2D g2d = changedImage.createGraphics();
    g2d.drawImage(imageBuf, 0, 0, this);
    return changedImage;
  }
  transient DirectColorModel cm = null;
  transient MemoryImageSource memoryimagesource = null;
  transient BufferedImage changedImage = null;
  transient Graphics2D g2d = null;
  public final Image getUnzipImg1(byte[] pixels, int imageWidth, int imageHeight) {
    Image imageBuf = null;
    if (this.cm == null)
    {
      this.cm = new DirectColorModel(8, 7, 56, 192);
    }
    if (this.memoryimagesource == null)
    {
      this.memoryimagesource = new MemoryImageSource(imageWidth, imageHeight, this.cm, pixels, 0, imageWidth);
    }
    try {
      imageBuf = createImage(this.memoryimagesource);
      this.memoryimagesource.newPixels(pixels, this.cm, 0, imageWidth);
    }
    catch (NoSuchMethodError e) {
      LoggerUtil.error(e.getClass().getName());
    } 
    this.changedImage = new BufferedImage(imageWidth, imageHeight, 5);
    this.g2d = this.changedImage.createGraphics();
    this.g2d.drawImage(imageBuf, 0, 0, this);
    this.g2d.dispose();
    return this.changedImage;
  }
  public final Image getUnzipImg2(byte[] pixels, int imageWidth, int imageHeight) {
    byte[] oldpixels = new byte[imageWidth * imageHeight];
    Image imageBuf = null;
    if (this.cm == null)
    {
      this.cm = new DirectColorModel(8, 7, 56, 192);
    }
    if (this.memoryimagesource == null)
    {
      this.memoryimagesource = new MemoryImageSource(imageWidth, imageHeight, this.cm, oldpixels, 0, imageWidth);
    }
    try {
      imageBuf = createImage(this.memoryimagesource);
      this.memoryimagesource.newPixels(pixels, this.cm, 0, imageWidth);
    }
    catch (NoSuchMethodError e) {
      LoggerUtil.error(e.getClass().getName());
    } 
    return imageBuf;
  }
  public final BufferedImage[] getUnzipImgs(Object[] pixelss, int imageWidth, int imageHeight) {
    BufferedImage[] changedImages = new BufferedImage[pixelss.length];
    byte[] oldpixels = new byte[imageWidth * imageHeight];
    DirectColorModel cm = new DirectColorModel(8, 7, 56, 192);
    Graphics2D g2d = null;
    BufferedImage changedImage = null;
    MemoryImageSource memoryimagesource = null;
    Image imageBuf = null;
    try {
      for (int i = 0; i < pixelss.length; i++)
      {
        memoryimagesource = new MemoryImageSource(imageWidth, imageHeight, cm, oldpixels, 0, imageWidth);
        imageBuf = null;
        memoryimagesource.setAnimated(true);
        memoryimagesource.setFullBufferUpdates(true);
        imageBuf = createImage(memoryimagesource);
        memoryimagesource.newPixels((byte[])pixelss[i], cm, 0, imageWidth);
        changedImage = new BufferedImage(imageWidth, imageHeight, 5);
        g2d = changedImage.createGraphics();
        g2d.drawImage(imageBuf, 0, 0, this);
        changedImages[i] = changedImage;
      }
    } catch (NoSuchMethodError e) {
      LoggerUtil.error(e.getClass().getName());
    } 
    return changedImages;
  }
  public static byte[] unZipData(byte[] bytes, int imageWidth, int imageHeight) throws LibException {
    int countNum = 0;
    int temLength = 0;
    int pixNumber = imageWidth * imageHeight;
    byte[] imageData = new byte[pixNumber];
    int bytesLenght = bytes.length;
    byte bufColor = 0;
    boolean flagRem = false;
    int i = 0;
    try {
      for (i = 1; i < bytesLenght; i += temLength) {
        if (flagRem) {
          if (i + 1 >= bytesLenght) {
            byte[] tmp = null;
            return tmp;
          } 
          bufColor = (byte)(bytes[i] | bytes[i + 1] >>> 4 & 0xF);
          byte tem = (byte)(bytes[i + 1] & 0xF);
          if (tem != 0) {
            int temInt = tem;
            if (temInt + countNum > pixNumber)
            {
              temInt = pixNumber - countNum;
            }
            int jend = countNum + temInt;
            for (int j = countNum; j < jend; j++)
            {
              imageData[j] = bufColor;
            }
            countNum = jend;
            if (pixNumber == countNum)
            {
              return (byte[])imageData.clone();
            }
            temLength = 2;
            if (bytesLenght == i + 3)
            {
              temLength = 3;
            }
            flagRem = false;
          } else {
            int extendNum6; int size6; int j; int extendNum10; int size10; int k; int extendNum18; int size18; int m; int extendNum22; int size22;
            int n;
            if (i + 2 >= bytesLenght) {
              byte[] tmp = null;
              return tmp;
            } 
            switch (bytes[i + 2] & 0xC0) {
              case 0:
                if (i + 2 >= bytesLenght) {
                  byte[] tmp = null;
                  return tmp;
                } 
                extendNum6 = bytes[i + 2] & 0x3F;
                size6 = countNum + extendNum6;
                size6 = (size6 > pixNumber) ? pixNumber : size6;
                for (j = countNum; j < size6; j++)
                {
                  imageData[j] = bufColor;
                }
                countNum = size6;
                if (countNum == pixNumber)
                {
                  return (byte[])imageData.clone();
                }
                temLength = 3;
                if (bytesLenght == i + 4)
                {
                  temLength = 4;
                }
                flagRem = false;
                break;
              case 64:
                if (i + 3 >= bytesLenght) {
                  byte[] tmp = null;
                  return tmp;
                } 
                extendNum10 = (bytes[i + 2] << 4 & 0x3F0) + (bytes[i + 3] >>> 4 & 0xF);
                size10 = countNum + extendNum10;
                size10 = (size10 > pixNumber) ? pixNumber : size10;
                for (k = countNum; k < size10; k++)
                {
                  imageData[k] = bufColor;
                }
                countNum = size10;
                if (countNum == pixNumber)
                {
                  return (byte[])imageData.clone();
                }
                temLength = 3;
                if (bytesLenght == i + 4) {
                  temLength = 4;
                  break;
                } 
                if (i + 3 >= bytesLenght) {
                  byte[] tmp = null;
                  return tmp;
                } 
                bytes[i + 3] = (byte)(bytes[i + 3] << 4);
                flagRem = true;
                break;
              case 128:
                if (i + 4 >= bytesLenght) {
                  byte[] tmp = null;
                  return tmp;
                } 
                extendNum18 = (bytes[i + 2] << 12 & 0x3F000) + (bytes[i + 3] << 4 & 0xFF0) + (bytes[i + 4] >>> 4 & 0xF);
                size18 = extendNum18 + countNum;
                size18 = (size18 > pixNumber) ? pixNumber : size18;
                for (m = countNum; m < size18; m++)
                {
                  imageData[m] = bufColor;
                }
                countNum = size18;
                if (countNum == pixNumber)
                {
                  return (byte[])imageData.clone();
                }
                temLength = 4;
                if (bytesLenght == i + 5) {
                  temLength = 5;
                  break;
                } 
                if (i + 4 >= bytesLenght) {
                  byte[] tmp = null;
                  return tmp;
                } 
                bytes[i + 4] = (byte)(bytes[i + 4] << 4);
                flagRem = true;
                break;
              case 192:
                if (i + 4 >= bytesLenght) {
                  byte[] tmp = null;
                  return tmp;
                } 
                extendNum22 = (bytes[i + 2] << 16 & 0x3F0000) + (bytes[i + 3] << 8 & 0xFF00) + (bytes[i + 4] & 0xFF);
                size22 = countNum + extendNum22;
                size22 = (size22 > pixNumber) ? pixNumber : size22;
                for (n = countNum; n < size22; n++)
                {
                  imageData[n] = bufColor;
                }
                countNum = size22;
                if (countNum == pixNumber)
                {
                  return (byte[])imageData.clone();
                }
                temLength = 5;
                if (bytesLenght == i + 6)
                {
                  temLength = 6;
                }
                flagRem = false;
                break;
              default:
                throw new LibException("imagedata error");
            } 
          } 
        } else {
          bufColor = bytes[i];
          byte tem = 0;
          if (i + 1 >= bytesLenght) {
            byte[] tmp = null;
            return tmp;
          } 
          tem = (byte)(bytes[i + 1] >>> 4 & 0xF);
          if (tem != 0) {
            int temInt = tem;
            if (temInt + countNum > pixNumber)
            {
              temInt = pixNumber - countNum;
            }
            int jend = temInt + countNum;
            for (int j = countNum; j < jend; j++)
            {
              imageData[j] = bufColor;
            }
            countNum = jend;
            if (countNum == pixNumber)
            {
              return (byte[])imageData.clone();
            }
            temLength = 1;
            if (bytesLenght == i + 2) {
              temLength = 2;
            }
            else {
              if (i + 1 >= bytesLenght) {
                byte[] tmp = null;
                return tmp;
              } 
              bytes[i + 1] = (byte)(bytes[i + 1] << 4);
              flagRem = true;
            } 
          } else {
            int extendNum6; int size6; int j; int extendNum10; int size10; int k; int extendNum18; int size18; int m; int extendNum22; int size22;
            int n;
            if (i + 1 >= bytesLenght) {
              byte[] tmp = null;
              return tmp;
            } 
            switch (bytes[i + 1] & 0xC) {
              case 0:
                if (i + 2 >= bytesLenght) {
                  byte[] tmp = null;
                  return tmp;
                } 
                extendNum6 = (bytes[i + 1] << 4 & 0x30) + (bytes[i + 2] >>> 4 & 0xF);
                size6 = countNum + extendNum6;
                size6 = (size6 > pixNumber) ? pixNumber : size6;
                for (j = countNum; j < size6; j++)
                {
                  imageData[j] = bufColor;
                }
                countNum = size6;
                if (countNum == pixNumber)
                {
                  return (byte[])imageData.clone();
                }
                if (countNum == pixNumber)
                {
                  return (byte[])imageData.clone();
                }
                temLength = 2;
                if (bytesLenght == i + 3) {
                  temLength = 3;
                  break;
                } 
                if (i + 2 >= bytesLenght) {
                  byte[] tmp = null;
                  return tmp;
                } 
                bytes[i + 2] = (byte)(bytes[i + 2] << 4);
                flagRem = true;
                break;
              case 4:
                if (i + 2 >= bytesLenght) {
                  byte[] tmp = null;
                  return tmp;
                } 
                extendNum10 = (bytes[i + 1] << 8 & 0x300) + (bytes[i + 2] & 0xFF);
                size10 = countNum + extendNum10;
                size10 = (size10 > pixNumber) ? pixNumber : size10;
                for (k = countNum; k < size10; k++)
                {
                  imageData[k] = bufColor;
                }
                countNum = size10;
                if (countNum == pixNumber)
                {
                  return (byte[])imageData.clone();
                }
                temLength = 3;
                if (bytesLenght == i + 4)
                {
                  temLength = 4;
                }
                flagRem = false;
                break;
              case 8:
                if (i + 3 >= bytesLenght) {
                  byte[] tmp = null;
                  return tmp;
                } 
                extendNum18 = (bytes[i + 1] << 16 & 0x30000) + (bytes[i + 2] << 8 & 0xFF00) + (bytes[i + 3] & 0xFF);
                size18 = countNum + extendNum18;
                size18 = (size18 > pixNumber) ? pixNumber : size18;
                for (m = countNum; m < size18; m++)
                {
                  imageData[m] = bufColor;
                }
                countNum = size18;
                if (countNum == pixNumber)
                {
                  return (byte[])imageData.clone();
                }
                temLength = 4;
                if (i + 5 == bytesLenght)
                {
                  temLength = 5;
                }
                flagRem = false;
                break;
              case 12:
                if (bytesLenght <= i + 4) {
                  byte[] tmp = null;
                  return tmp;
                } 
                extendNum22 = (bytes[i + 1] << 20 & 0x300000) + (bytes[i + 2] << 12 & 0xFF000) + (bytes[i + 3] << 4 & 0xFF0) + (bytes[i + 4] >>> 4 & 0xF);
                size22 = extendNum22 + countNum;
                size22 = (size22 > pixNumber) ? pixNumber : size22;
                for (n = countNum; n < size22; n++)
                {
                  imageData[n] = bufColor;
                }
                countNum = size22;
                if (countNum == pixNumber)
                {
                  return (byte[])imageData.clone();
                }
                temLength = 4;
                if (bytesLenght == i + 5) {
                  temLength = 5;
                  break;
                } 
                if (i + 4 >= bytesLenght) {
                  byte[] tmp = null;
                  return tmp;
                } 
                bytes[i + 4] = (byte)(bytes[i + 4] << 4);
                flagRem = true;
                break;
              default:
                throw new LibException("imagedata error");
            } 
          } 
        } 
      } 
    } catch (ArrayIndexOutOfBoundsException ex) {
      LoggerUtil.error("i = " + i);
    } 
    return (byte[])imageData.clone();
  }
}
