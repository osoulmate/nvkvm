package com.huawei.vm.console.utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
public class ImageIO
{
  private File file;
  private RandomAccessFile image;
  private long imageSize = -1L;
  private int zeroOffSet;
  private static final String READ_ONLY = "r";
  private static final String READ_WRITE = "rw";
  private String fileAttr = "r";
  private String absoluteImagePath;
  private boolean isCreateImage = false;
  public void open(String path, boolean isMustExist) throws VMException, IOException {
    this.isCreateImage = !isMustExist;
    if (this.image != null)
    {
      close();
    }
    this.zeroOffSet = 0;
    if (null == path || "".equals(path))
    {
      throw new VMException(334);
    }
    this.file = new File(path);
    if (!this.file.exists() && isMustExist)
    {
      throw new VMException(320);
    }
    if (this.file.isDirectory())
    {
      throw new VMException(321);
    }
    if (!isMustExist)
    {
      this.fileAttr = "rw";
    }
    try {
      this.image = new RandomAccessFile(this.file, this.fileAttr);
    }
    catch (FileNotFoundException e) {
      throw new VMException(326);
    } 
    byte[] head = new byte[512];
    int curRead = read(head, 0L, 512);
    if (curRead >= 512 && head[0] == 67 && head[1] == 80 && head[2] == 81 && head[3] == 82 && head[4] == 70 && head[5] == 66 && head[6] == 76 && head[7] == 79)
    {
      this.zeroOffSet = head[14] & 0xFF | head[15] << 8;
    }
    this.absoluteImagePath = this.file.getCanonicalPath();
    try {
      this.imageSize = this.image.length() - this.zeroOffSet;
    }
    catch (IOException e) {
      this.imageSize = -1L;
    } 
  }
  public void open(String path, boolean isMustExist, boolean isFloppy) throws VMException, IOException {
    this.isCreateImage = !isMustExist;
    if (null != this.image)
    {
      close();
    }
    this.zeroOffSet = 0;
    if (null == path || "".equals(path))
    {
      throw new VMException(334);
    }
    this.file = new File(path);
    if (!this.file.exists() && isMustExist)
    {
      throw new VMException(320);
    }
    if (this.file.isDirectory())
    {
      throw new VMException(321);
    }
    if (isFloppy && this.file.canWrite())
    {
      this.fileAttr = "rw";
    }
    try {
      this.image = new RandomAccessFile(this.file, this.fileAttr);
    }
    catch (FileNotFoundException e) {
      throw new VMException(326);
    } 
    byte[] head = new byte[512];
    int curRead = read(head, 0L, 512);
    if (curRead >= 512 && head[0] == 67 && head[1] == 80 && head[2] == 81 && head[3] == 82 && head[4] == 70 && head[5] == 66 && head[6] == 76 && head[7] == 79)
    {
      this.zeroOffSet = head[14] & 0xFF | head[15] << 8;
    }
    this.absoluteImagePath = this.file.getCanonicalPath();
    try {
      this.imageSize = this.image.length() - this.zeroOffSet;
    }
    catch (IOException e) {
      this.imageSize = -1L;
    } 
  }
  public void close() throws VMException {
    try {
      if (null != this.image)
      {
        this.image.close();
      }
    }
    catch (IOException e) {
      throw new VMException();
    }
    finally {
      this.file = null;
      this.image = null;
      this.imageSize = -1L;
    } 
  }
  public long getMediumSize() {
    try {
      if (null == this.image)
      {
        return -1L;
      }
      if (-1L == this.imageSize)
      {
        this.imageSize = this.image.length() - this.zeroOffSet;
      }
      return this.imageSize;
    }
    catch (IOException e) {
      return -1L;
    } 
  }
  public int read(byte[] dataBuffer, long startPosition, int length) throws VMException {
    if (null == this.image)
    {
      throw new VMException(253);
    }
    startPosition += this.zeroOffSet;
    int readSize = 0;
    try {
      this.image.seek(startPosition);
      readSize = this.image.read(dataBuffer, 0, length);
    }
    catch (IOException e) {
      throw new VMException(250);
    } 
    if (-1 == readSize)
    {
      readSize = 0;
    }
    return readSize;
  }
  public void write(byte[] dataBuffer, long startPosition, int length) throws VMException {
    if (null == this.image)
    {
      throw new VMException(253);
    }
    startPosition += this.zeroOffSet;
    try {
      this.image.seek(startPosition);
      this.image.write(dataBuffer, 0, length);
    }
    catch (IOException e) {
      if (this.isCreateImage)
      {
        throw new VMException(323);
      }
      if (this.fileAttr.equals("r"))
      {
        throw new VMException(254);
      }
      throw new VMException(250);
    } 
  }
  public void setImageLength(long size) throws VMException {
    try {
      if (null == this.image)
      {
        throw new VMException(320);
      }
      if (this.isCreateImage)
      {
        this.image.setLength(size);
        this.imageSize = size;
      }
    } catch (IOException e) {
      throw new VMException(323, e.getMessage());
    } 
  }
  public boolean isActive() {
    return (null != this.image);
  }
  public String getAbsoluteImagePath() {
    return this.absoluteImagePath;
  }
  public boolean canWrite() {
    return this.fileAttr.equals("rw");
  }
  public void rename(String imageName) throws IOException {
    boolean isNull = false;
    if (null == this.file) {
      this.file = new File(this.absoluteImagePath);
      isNull = true;
    } 
    File newFile = new File(imageName);
    if (this.file.renameTo(newFile))
    {
      this.absoluteImagePath = newFile.getCanonicalPath();
    }
    if (isNull)
    {
      this.file = null;
    }
  }
}
