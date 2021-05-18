package com.huawei.vm.console.utilsV1;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.UDFExtendFile;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.NonReadableChannelException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class LocalDirImageIO
{
  private long imageSize = -1L;
  private int zeroOffSet = 0;
  private String localDirName = null;
  private Map<Long, UDFExtendFile> memoryISOStruct = null;
  byte[] extendFileContentByte = null;
  private Map<Long, byte[]> lastContentMap = (Map)new HashMap<Long, byte[]>();
  private byte[] moreFileContent = null;
  private int moreFileOffset = 0;
  private int moreFileReadLen = 0;
  public String getLocalDirName() {
    return this.localDirName;
  }
  public Map<Long, UDFExtendFile> getMemoryISOStruct() {
    return this.memoryISOStruct;
  }
  public void openMemoryISO(String path, boolean isMustExist, String localDirName, Map<Long, UDFExtendFile> memoryStruct) throws VMException {
    if (null == memoryStruct || memoryStruct.size() == 0)
    {
      throw new VMException(253);
    }
    this.zeroOffSet = 0;
    this.localDirName = localDirName;
    this.memoryISOStruct = memoryStruct;
    this.lastContentMap.clear();
    byte[] head = new byte[512];
    int curRead = readMemeoryISO(head, 0L, 512);
    if (curRead >= 512 && head[0] == 67 && head[1] == 80 && head[2] == 81 && head[3] == 82 && head[4] == 70 && head[5] == 66 && head[6] == 76 && head[7] == 79)
    {
      this.zeroOffSet = head[14] | head[15] << 8;
    }
    Iterator<Long> key = memoryStruct.keySet().iterator();
    long fileLen = 0L;
    while (key.hasNext()) {
      UDFExtendFile file = memoryStruct.get(key.next());
      fileLen += file.getFileLen();
    } 
    this.imageSize = fileLen - this.zeroOffSet;
  }
  public int readMemeoryISO(byte[] dataBuffer, long startPosition, int length) throws VMException {
    if (null == this.memoryISOStruct)
    {
      throw new VMException(253);
    }
    startPosition += this.zeroOffSet;
    int readSize = 0;
    UDFExtendFile file = null;
    this.moreFileContent = null;
    this.moreFileOffset = 0;
    this.moreFileReadLen = 0;
    long fileStartPosition = fileStartPosition(startPosition);
    if (-1L == fileStartPosition) {
      System.arraycopy(new byte[length], 0, dataBuffer, 0, length);
      readSize = length;
      return readSize;
    } 
    file = this.memoryISOStruct.get(Long.valueOf(fileStartPosition));
    long fileTotalLength = file.getFileLen();
    long fileActalLength = file.getFileActalLen();
    boolean isExtendData = file.isExtendData();
    long offset = startPosition - fileStartPosition;
    if (offset + length <= fileTotalLength) {
      if (isExtendData) {
        try {
          if (startPosition + length < fileStartPosition + fileActalLength)
          {
            RandomAccessFile randFile = new RandomAccessFile(new String(file.getContent()), "r");
            FileChannel ch = randFile.getChannel();
            ByteBuffer readbuffer = ByteBuffer.allocate(length);
            readSize = ch.read(readbuffer, offset);
            System.arraycopy(readbuffer.array(), 0, dataBuffer, 0, readSize);
            readSize = length;
            readbuffer.clear();
            ch.close();
            randFile.close();
            ch = null;
            randFile = null;
            readbuffer = null;
          }
          else
          {
            RandomAccessFile randFile = new RandomAccessFile(new String(file.getContent()), "r");
            FileChannel ch = randFile.getChannel();
            ByteBuffer readbuffer = ByteBuffer.allocate((int)(fileActalLength - offset));
            readSize = ch.read(readbuffer, offset);
            System.arraycopy(readbuffer.array(), 0, dataBuffer, 0, readSize);
            readSize = length;
            readbuffer.clear();
            ch.close();
            randFile.close();
            ch = null;
            randFile = null;
            readbuffer = null;
          }
        } catch (NonReadableChannelException ne) {
          System.arraycopy(new byte[length], 0, dataBuffer, 0, length);
          return -1;
        }
        catch (IllegalArgumentException ie) {
          System.arraycopy(new byte[length], 0, dataBuffer, 0, length);
          return -1;
        }
        catch (IOException e1) {
          System.arraycopy(new byte[length], 0, dataBuffer, 0, length);
          return -1;
        }
        catch (Exception e) {
          System.arraycopy(new byte[length], 0, dataBuffer, 0, length);
          return -1;
        } 
      } else {
        try {
          System.arraycopy(file.getContent(), (int)offset, dataBuffer, 0, length);
          readSize = length;
        }
        catch (Exception e) {
          System.arraycopy(new byte[length], 0, dataBuffer, 0, length);
          return -1;
        } 
      } 
    } else {
      try {
        this.moreFileContent = new byte[length];
        this.moreFileReadLen = length;
        circleGetMorefile(startPosition, length);
        System.arraycopy(this.moreFileContent, 0, dataBuffer, 0, length);
        readSize = length;
      }
      catch (Exception e) {
        System.arraycopy(new byte[length], 0, dataBuffer, 0, length);
        return -1;
      } 
    } 
    return readSize;
  }
  public void circleGetMorefile(long startPosition, int length) {
    int readLength = 0;
    UDFExtendFile file = null;
    long nextPosition = 0L;
    long fileStartPosition = fileStartPosition(startPosition);
    file = this.memoryISOStruct.get(Long.valueOf(fileStartPosition));
    boolean isExtendData = file.isExtendData();
    long offset = startPosition - fileStartPosition;
    if (isExtendData) {
      readLength = getBigFileContent(startPosition, fileStartPosition, file, offset, length);
    }
    else {
      readLength = getLittleFileContent(startPosition, fileStartPosition, file, (int)offset, length);
    } 
    if (this.moreFileReadLen - this.moreFileOffset > 0) {
      nextPosition = startPosition + readLength;
      circleGetMorefile(nextPosition, length - readLength);
    } 
  }
  public int getBigFileContent(long startPosition, long fileStartPosition, UDFExtendFile file, long offset, int length) {
    int readSize = 0;
    RandomAccessFile randFile = null;
    FileChannel ch = null;
    ByteBuffer readbuffer = null;
    try {
      long fileTotalLen = file.getFileLen();
      long fileActalLen = file.getFileActalLen();
      if (offset + length >= fileTotalLen) {
        if (startPosition <= fileStartPosition + fileActalLen)
        {
          int readDataLength = (int)(fileStartPosition + fileActalLen - startPosition);
          randFile = new RandomAccessFile(new String(file.getContent()), "r");
          ch = randFile.getChannel();
          readbuffer = ByteBuffer.allocate(readDataLength);
          readSize = ch.read(readbuffer, offset);
          System.arraycopy(readbuffer.array(), 0, this.moreFileContent, this.moreFileOffset, readSize);
          int blankLength = (int)(fileTotalLen - fileActalLen);
          readSize = readDataLength + blankLength;
          readbuffer.clear();
          ch.close();
          randFile.close();
          ch = null;
          randFile = null;
          readbuffer = null;
        }
        else
        {
          int blankLength = (int)(fileTotalLen - startPosition - fileStartPosition);
          if (blankLength > 0)
          {
            System.arraycopy(new byte[blankLength], 0, this.moreFileContent, this.moreFileOffset, blankLength);
            readSize = blankLength;
          }
        }
      }
      else if (startPosition + length <= fileStartPosition + fileActalLen) {
        randFile = new RandomAccessFile(new String(file.getContent()), "r");
        ch = randFile.getChannel();
        readbuffer = ByteBuffer.allocate(length);
        readSize = ch.read(readbuffer, offset);
        System.arraycopy(readbuffer.array(), 0, this.moreFileContent, this.moreFileOffset, readSize);
        readSize = length;
        readbuffer.clear();
        ch.close();
        randFile.close();
        ch = null;
        randFile = null;
        readbuffer = null;
      }
      else {
        randFile = new RandomAccessFile(new String(file.getContent()), "r");
        ch = randFile.getChannel();
        readbuffer = ByteBuffer.allocate((int)(fileActalLen - offset));
        readSize = ch.read(readbuffer, offset);
        System.arraycopy(readbuffer.array(), 0, this.moreFileContent, this.moreFileOffset, readSize);
        readSize = length;
        readbuffer.clear();
        ch.close();
        randFile.close();
        ch = null;
        randFile = null;
        readbuffer = null;
      } 
      this.moreFileOffset += readSize;
    }
    catch (NonReadableChannelException ne) {
      System.arraycopy(new byte[length], 0, this.moreFileContent, this.moreFileOffset, length);
      return -1;
    }
    catch (IllegalArgumentException ie) {
      System.arraycopy(new byte[length], 0, this.moreFileContent, this.moreFileOffset, length);
      return -1;
    }
    catch (IOException e1) {
      System.arraycopy(new byte[length], 0, this.moreFileContent, this.moreFileOffset, length);
      return -1;
    }
    catch (Exception e) {
      System.arraycopy(new byte[length], 0, this.moreFileContent, this.moreFileOffset, length);
      return -1;
    } finally {}
    return readSize;
  }
  public int getLittleFileContent(long startPosition, long fileStartPosition, UDFExtendFile file, int offset, int length) {
    int readSize = 0;
    int fileTotalLen = (int)file.getFileLen();
    try {
      if (offset + length >= fileTotalLen)
      {
        System.arraycopy(file.getContent(), offset, this.moreFileContent, this.moreFileOffset, fileTotalLen - offset);
        readSize = fileTotalLen - offset;
      }
      else
      {
        System.arraycopy(file.getContent(), offset, this.moreFileContent, this.moreFileOffset, length);
        readSize = length;
      }
    } catch (Exception e) {
      System.arraycopy(new byte[length], 0, this.moreFileContent, this.moreFileOffset, length);
      return -1;
    } 
    this.moreFileOffset += readSize;
    return readSize;
  }
  public long fileStartPosition(long reqPosition) {
    long retPos = -1L;
    long filePosition = -1L;
    long fileLen = -1L;
    UDFExtendFile file = null;
    Iterator<Long> mapKey = this.memoryISOStruct.keySet().iterator();
    while (mapKey.hasNext()) {
      file = this.memoryISOStruct.get(mapKey.next());
      filePosition = file.getPosition();
      fileLen = file.getFileLen();
      if (filePosition <= reqPosition && reqPosition < filePosition + fileLen) {
        retPos = filePosition;
        return retPos;
      } 
    } 
    return retPos;
  }
  public int read2kMemeoryISO(byte[] dataBuffer, long startPosition, int length) throws VMException {
    int readSize = 0;
    return readSize;
  }
  public int readMore2kMemeoryISO(byte[] dataBuffer, long startPosition, int length) throws VMException {
    int readSize = 0;
    return readSize;
  }
  public void close() {
    this.localDirName = null;
    this.memoryISOStruct = null;
    this.extendFileContentByte = null;
    this.lastContentMap.clear();
    this.imageSize = -1L;
  }
  public long getMediumSize() {
    try {
      if (null == this.memoryISOStruct)
      {
        return -1L;
      }
      if (-1L == this.imageSize) {
        Iterator<Long> key = this.memoryISOStruct.keySet().iterator();
        long fileLen = 0L;
        while (key.hasNext()) {
          UDFExtendFile file = this.memoryISOStruct.get(key.next());
          fileLen += file.getFileLen();
        } 
        this.imageSize = fileLen - this.zeroOffSet;
      } 
      return this.imageSize;
    }
    catch (Exception e) {
      return -1L;
    } 
  }
  public boolean isActive() {
    return (null != this.memoryISOStruct);
  }
  public boolean canWrite() {
    return false;
  }
}
