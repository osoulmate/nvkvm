package com.KinescopeV1;
import com.kvmV1.Debug;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
public class KinescopeDataCollect
  extends Thread
{
  private String path = "";
  private Thread dataCollectThread;
  private boolean isCollect = false;
  public List<KinescopeFrame> frameList = null;
  private KinescopeFrame kinescopeFrame = null;
  private RandomAccessFile file;
  private static int indexCount = 20;
  private static byte FILETOP = 1;
  private static byte SEQINDEX = 2;
  private static byte IMAGEDATA = 3;
  private long fileIndexPos = 0L;
  private int indexPos = 0;
  public int frameSeqIndex = 0;
  public KinescopeDataCollect(String path) {
    this.path = path;
    try {
      this.file = new RandomAccessFile(path, "rw");
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    } 
    this.frameList = new ArrayList<KinescopeFrame>();
  }
  public void startCollectThread() {
    this.dataCollectThread = new Thread(this);
    this.dataCollectThread.setName("dataCollectThread");
    this.dataCollectThread.start();
  }
  public void run() {
    try {
      this.file.write(fileTopData(0, 0L));
      while (isCollect()) {
        if (this.frameList.size() == 0) {
          try {
            Thread.sleep(10L);
            continue;
          } catch (InterruptedException e) {
            Debug.printExc(e.getMessage());
          } 
        }
        this.kinescopeFrame = this.frameList.remove(0);
        if (this.kinescopeFrame.isIFrame())
        {
          if (this.fileIndexPos == 0L) {
            addSeqIndex(this.kinescopeFrame.getSeqFrame());
          }
          else if (this.indexPos < indexCount) {
            updateSeqIndex(this.kinescopeFrame.getSeqFrame());
          }
          else {
            updateSeqIndexPos(this.file.length());
            addSeqIndex(this.kinescopeFrame.getSeqFrame());
          } 
        }
        this.file.write(fileImageData(this.kinescopeFrame));
        this.file.seek(0L);
        this.file.write(fileTopData(this.kinescopeFrame.getSeqFrame(), this.file.length()));
        this.file.seek(this.file.length());
        this.kinescopeFrame = null;
      } 
      if (this.frameList != null && this.frameList.size() > 0) {
        for (int i = 0; i < this.frameList.size(); i++) {
          KinescopeFrame frame = this.frameList.remove(i);
          this.file.write(fileImageData(frame));
          this.file.seek(0L);
          this.file.write(fileTopData(this.kinescopeFrame.getSeqFrame(), this.file.length()));
          this.file.seek(this.file.length());
          frame = null;
        } 
        this.frameList = null;
      } 
      this.file.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    } 
  }
  public void addSeqIndex(int frameNum) {
    try {
      this.fileIndexPos = this.file.length();
      this.file.write(seqIndexData());
      this.file.seek(this.file.length() - 1L);
      this.indexPos = 0;
      updateSeqIndex(frameNum);
    }
    catch (IOException e) {
      e.printStackTrace();
    } 
  }
  public void updateSeqIndex(int frameNum) {
    try {
      this.file.seek(this.fileIndexPos + (this.indexPos * 12) + 7L);
      byte[] iframeIndex = new byte[12];
      iframeIndex[0] = (byte)(frameNum >> 24);
      iframeIndex[1] = (byte)(frameNum >> 16);
      iframeIndex[2] = (byte)(frameNum >> 8);
      iframeIndex[3] = (byte)(frameNum >> 0);
      iframeIndex[4] = (byte)(int)(this.file.length() >> 56L);
      iframeIndex[5] = (byte)(int)(this.file.length() >> 48L);
      iframeIndex[6] = (byte)(int)(this.file.length() >> 40L);
      iframeIndex[7] = (byte)(int)(this.file.length() >> 32L);
      iframeIndex[8] = (byte)(int)(this.file.length() >> 24L);
      iframeIndex[9] = (byte)(int)(this.file.length() >> 16L);
      iframeIndex[10] = (byte)(int)(this.file.length() >> 8L);
      iframeIndex[11] = (byte)(int)(this.file.length() >> 0L);
      this.file.write(iframeIndex);
      this.indexPos++;
      this.file.seek(this.file.length());
    }
    catch (IOException e) {
      e.printStackTrace();
    } 
  }
  public void updateSeqIndexPos(long pos) {
    try {
      this.file.seek(this.fileIndexPos + (this.indexPos * 12) + 7L);
      byte[] datas = new byte[8];
      datas[0] = (byte)(int)(pos >> 56L);
      datas[1] = (byte)(int)(pos >> 48L);
      datas[2] = (byte)(int)(pos >> 40L);
      datas[3] = (byte)(int)(pos >> 32L);
      datas[4] = (byte)(int)(pos >> 24L);
      datas[5] = (byte)(int)(pos >> 16L);
      datas[6] = (byte)(int)(pos >> 8L);
      datas[7] = (byte)(int)(pos >> 0L);
      this.file.write(datas);
      this.file.seek(this.file.length());
    }
    catch (IOException e) {
      e.printStackTrace();
    } 
  }
  public byte[] fileImageData(KinescopeFrame kinescopeFrame) {
    byte[] imageData = new byte[(kinescopeFrame.getSourceData()).length + 25];
    imageData[0] = -2;
    imageData[1] = -10;
    imageData[2] = (byte)(imageData.length >> 24);
    imageData[3] = (byte)(imageData.length >> 16);
    imageData[4] = (byte)(imageData.length >> 8);
    imageData[5] = (byte)(imageData.length >> 0);
    imageData[6] = IMAGEDATA;
    imageData[7] = (byte)(kinescopeFrame.isIFrame() ? 0 : 1);
    imageData[8] = 0;
    imageData[9] = (byte)(kinescopeFrame.getSeqFrame() >> 24);
    imageData[10] = (byte)(kinescopeFrame.getSeqFrame() >> 16);
    imageData[11] = (byte)(kinescopeFrame.getSeqFrame() >> 8);
    imageData[12] = (byte)(kinescopeFrame.getSeqFrame() >> 0);
    imageData[13] = (byte)(kinescopeFrame.getWidth() >> 8);
    imageData[14] = (byte)(kinescopeFrame.getWidth() >> 0);
    imageData[15] = (byte)(kinescopeFrame.getHeight() >> 8);
    imageData[16] = (byte)(kinescopeFrame.getHeight() >> 0);
    long nowTime = System.currentTimeMillis();
    imageData[17] = (byte)(int)(nowTime >> 56L);
    imageData[18] = (byte)(int)(nowTime >> 48L);
    imageData[19] = (byte)(int)(nowTime >> 40L);
    imageData[20] = (byte)(int)(nowTime >> 32L);
    imageData[21] = (byte)(int)(nowTime >> 24L);
    imageData[22] = (byte)(int)(nowTime >> 16L);
    imageData[23] = (byte)(int)(nowTime >> 8L);
    imageData[24] = (byte)(int)(nowTime >> 0L);
    System.arraycopy(kinescopeFrame.getSourceData(), 0, imageData, 25, imageData.length - 25);
    return imageData;
  }
  public byte[] fileTopData(int totalFrame, long fileLength) {
    byte[] datas = new byte[52];
    datas[0] = -2;
    datas[1] = -10;
    datas[2] = (byte)(datas.length >> 24);
    datas[3] = (byte)(datas.length >> 16);
    datas[4] = (byte)(datas.length >> 8);
    datas[5] = (byte)(datas.length >> 0);
    datas[6] = FILETOP;
    datas[7] = 0;
    datas[8] = (byte)(totalFrame >> 24);
    datas[9] = (byte)(totalFrame >> 16);
    datas[10] = (byte)(totalFrame >> 8);
    datas[11] = (byte)totalFrame;
    datas[12] = 0;
    datas[13] = 0;
    datas[14] = 0;
    datas[15] = 0;
    datas[16] = 0;
    datas[17] = 0;
    datas[18] = 0;
    datas[19] = 0;
    datas[20] = (byte)(int)(fileLength >> 56L);
    datas[21] = (byte)(int)(fileLength >> 48L);
    datas[22] = (byte)(int)(fileLength >> 40L);
    datas[23] = (byte)(int)(fileLength >> 32L);
    datas[24] = (byte)(int)(fileLength >> 24L);
    datas[25] = (byte)(int)(fileLength >> 16L);
    datas[26] = (byte)(int)(fileLength >> 8L);
    datas[27] = (byte)(int)(fileLength >> 0L);
    datas[28] = 1;
    datas[29] = 0;
    return datas;
  }
  public byte[] seqIndexData() {
    byte[] datas = new byte[indexCount * 12 + 7 + 8];
    datas[0] = -2;
    datas[1] = -10;
    datas[2] = (byte)(datas.length >> 24);
    datas[3] = (byte)(datas.length >> 16);
    datas[4] = (byte)(datas.length >> 8);
    datas[5] = (byte)(datas.length >> 0);
    datas[6] = SEQINDEX;
    return datas;
  }
  public static byte[] intToBytes(int intsrc) {
    byte[] temp = new byte[4];
    temp[0] = (byte)intsrc;
    temp[1] = (byte)(intsrc >> 8);
    temp[2] = (byte)(intsrc >> 16);
    temp[3] = (byte)(intsrc >> 24);
    return temp;
  }
  public static byte[] longToBytes(long intsrc) {
    byte[] temp = new byte[8];
    temp[0] = (byte)(int)intsrc;
    temp[1] = (byte)(int)(intsrc >> 8L);
    temp[2] = (byte)(int)(intsrc >> 16L);
    temp[3] = (byte)(int)(intsrc >> 24L);
    temp[4] = (byte)(int)(intsrc >> 32L);
    temp[5] = (byte)(int)(intsrc >> 40L);
    temp[6] = (byte)(int)(intsrc >> 48L);
    temp[7] = (byte)(int)(intsrc >> 56L);
    return temp;
  }
  public Thread getDataCollectThread() {
    return this.dataCollectThread;
  }
  public boolean isCollect() {
    return this.isCollect;
  }
  public String getPath() {
    return this.path;
  }
  public void setDataCollectThread(Thread dataCollectThread) {
    this.dataCollectThread = dataCollectThread;
  }
  public void setCollect(boolean isCollect) {
    this.isCollect = isCollect;
  }
  public void setPath(String path) {
    this.path = path;
  }
  public static void main(String[] args) {}
}
