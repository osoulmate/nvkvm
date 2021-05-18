package com.library.decoder;
import com.library.LoggerUtil;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
public class ImageCreater
  extends Component
{
  private static final long serialVersionUID = 1L;
  private transient DirectColorModel cm1 = null;
  private transient MemoryImageSource memoryimagesource = null;
  private transient Image imageBuf = null;
  public ImageCreater() {
    this(64, 64);
  }
  public ImageCreater(int imageWidth, int imageHeight) {
    int[] inipixels = new int[imageWidth * imageHeight];
    this.cm1 = new DirectColorModel(24, 16711680, 65280, 255);
    this.memoryimagesource = new MemoryImageSource(imageWidth, imageHeight, this.cm1, inipixels, 0, imageWidth);
    this.memoryimagesource.setAnimated(true);
    this.imageBuf = createImage(this.memoryimagesource);
  }
  public final BufferedImage createRLEImg_test(byte[] pixels, int imageWidth, int imageHeight) {
    byte[] newpixels = new byte[12288];
    for (int j = 0; j < newpixels.length; j += 3) {
      newpixels[j] = 7;
      newpixels[j + 1] = 7;
      newpixels[j + 2] = 7;
    } 
    MemoryImageSource memoryimagesource = new MemoryImageSource(64, 64, null, newpixels, 0, 192);
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
  public BufferedImage createRLEImg3(byte[] pixels, int imageWidth, int imageHeight) {
    BufferedImage changedImage = null;
    Graphics2D g2d = null;
    this.cm1 = new DirectColorModel(8, 7, 56, 192);
    this.memoryimagesource = new MemoryImageSource(64, 64, this.cm1, pixels, 0, 64);
    this.memoryimagesource.setAnimated(true);
    this.imageBuf = createImage(this.memoryimagesource);
    try {
      this.imageBuf = createImage(this.memoryimagesource);
      this.memoryimagesource.newPixels(pixels, this.cm1, 0, 64);
    }
    catch (NoSuchMethodError e) {
      LoggerUtil.error(e.getClass().getName());
    } 
    changedImage = new BufferedImage(imageWidth, imageHeight, 5);
    g2d = changedImage.createGraphics();
    g2d.drawImage(this.imageBuf, 0, 0, this);
    g2d.dispose();
    return changedImage;
  }
  public BufferedImage createRLEImg(int[] pixels, int imageWidth, int imageHeight) {
    BufferedImage changedImage = null;
    Graphics2D g2d = null;
    try {
      this.memoryimagesource.newPixels(pixels, this.cm1, 0, imageWidth);
    }
    catch (NoSuchMethodError e) {
      LoggerUtil.error(e.getClass().getName());
    } 
    changedImage = new BufferedImage(imageWidth, imageHeight, 5);
    g2d = changedImage.createGraphics();
    g2d.drawImage(this.imageBuf, 0, 0, this);
    g2d.dispose();
    return changedImage;
  }
  public BufferedImage createRLEImg_0(byte[] pixels, int imageWidth, int imageHeight) {
    BufferedImage changedImage = null;
    Graphics2D g2d = null;
    try {
      this.memoryimagesource.newPixels(pixels, (ColorModel)null, 0, imageWidth);
    }
    catch (NoSuchMethodError e) {
      LoggerUtil.error(e.getClass().getName());
    } 
    changedImage = new BufferedImage(imageWidth, imageHeight, 5);
    g2d = changedImage.createGraphics();
    g2d.drawImage(this.imageBuf, 0, 0, this);
    g2d.dispose();
    return changedImage;
  }
  public BufferedImage JPEGDecodeAsImage(byte[] images) {
    byte[] buf = images;
    BufferedImage image = null;
    ImageInputStream imageInputstream = new MemoryCacheImageInputStream(new ByteArrayInputStream(buf));
    try {
      image = ImageIO.read(imageInputstream);
    } catch (Exception e) {
      LoggerUtil.error(e.getClass().getName());
    } 
    return image;
  }
}
