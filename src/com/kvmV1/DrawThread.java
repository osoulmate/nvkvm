package com.kvmV1;
import com.KinescopeV1.KinescopeFrame;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
public class DrawThread
  extends Thread
{
  private BladeCommu bladeCommu = null;
  private KVMUtil kvmUtil;
  private byte[] currentData;
  private boolean conn = false;
  private byte[] previImage = null;
  private boolean firstJudge = true;
  private LinkedList<byte[]> comImage = (LinkedList)new LinkedList<byte[]>();
  private boolean isFocusOwner = true;
  private boolean isNew = true;
  public boolean isDisplay = false;
  public int bladeNo;
  public ImagePane imagePane;
  public boolean totalConn = false;
  public Vector<byte[]> lList = (Vector)new Vector<byte[]>();
  public Timer timer = null;
  public KVMInterface kvmInterface = null;
  public boolean isIFrameSend = false;
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public DrawThread(int bladeNo, ImagePane imagePane, KVMUtil kvmUtil) {
    this.bladeNo = bladeNo;
    this.imagePane = imagePane;
    this.kvmUtil = kvmUtil;
    this.isNew = imagePane.isNew();
  }
  public void setTotalConn(boolean total) {
    this.totalConn = total;
  }
  public void setConn(boolean conn) {
    this.conn = conn;
  }
  public Timer getTimer() {
    return this.timer;
  }
  public void run() {
    this.timer = new Timer("DrawImageTimer" + this.bladeNo, true);
    this.timer.scheduleAtFixedRate(new DrawImageTimer(), 0L, 50L);
    int count = 0;
    byte solidColor = -1;
    label136: while (this.totalConn && this.conn) {
      byte[] imageData = null;
      byte[] aImagePackedData = null;
      while (this.lList.size() < 2) {
        try {
          Thread.sleep(5L);
        }
        catch (InterruptedException e) {
          Debug.printExc(e.getMessage());
        } 
      } 
      this.currentData = this.lList.remove(0);
      if (this.currentData == null) {
        continue;
      }
      imageData = new byte[this.currentData.length - 2];
      System.arraycopy(this.currentData, 2, imageData, 0, imageData.length);
      if (this.isNew) {
        if (this.kvmUtil.isComplete(imageData, this.bladeNo)) {
          if (!this.kvmInterface.base.isDiv && this.kvmUtil.getImageFloatToolbar(this.bladeNo) != null)
          {
            this.kvmUtil.getImageFloatToolbar(this.bladeNo).startStateMenu();
          }
          try {
            aImagePackedData = this.kvmUtil.getZipImageData();
          }
          catch (KVMException kvme) {
            Debug.println(kvme.getMessage());
            continue;
          } 
          try {
            if (!this.kvmInterface.base.isDiv && this.isDisplay)
            {
              this.kvmInterface.statusBar.frameNum++;
            }
            if (aImagePackedData.length != 6 || aImagePackedData[1] != 0 || !this.kvmUtil.dispDiff || this.kvmUtil.resolutionCh || this.firstJudge)
            {
              if (aImagePackedData.length <= 6 && !this.kvmUtil.dispDiff) {
                if (solidColor == aImagePackedData[1]) {
                  solidColor = aImagePackedData[1];
                  continue;
                } 
                solidColor = aImagePackedData[1];
              }
              else {
                solidColor = -1;
              } 
              if (null != this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect && this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect.isCollect())
              {
                if (!video(aImagePackedData)) {
                  continue;
                }
              }
              byte[] data = this.kvmUtil.unZipData(aImagePackedData);
              aImagePackedData = null;
              if (data == null) {
                Debug.println("unzip error");
                this.bladeCommu.sentData(this.kvmInterface.packData.resendData(this.bladeNo));
                continue;
              } 
              if (this.firstJudge || this.kvmUtil.resolutionCh)
              {
                this.previImage = new byte[data.length];
              }
              if (this.kvmUtil.dispDiff) {
                if (!this.kvmUtil.xorData(data, this.previImage)) {
                  Debug.printExc("xor error");
                  this.bladeCommu.sentData(this.kvmInterface.packData.resendData(this.bladeNo));
                  data = null;
                  continue;
                } 
              } else {
                System.arraycopy(data, 0, this.previImage, 0, this.previImage.length);
              } 
              if (!this.kvmInterface.base.isDiv) {
                this.imagePane.remotePreX = this.imagePane.remoteX;
                this.imagePane.remotePreY = this.imagePane.remoteY;
                this.imagePane.remoteX = this.kvmUtil.remoteX;
                this.imagePane.remoteY = this.kvmUtil.remoteY;
              } 
              if (this.firstJudge || this.kvmUtil.resolutionCh) {
                this.imagePane.resolutionCh = true;
                this.imagePane.width = this.kvmUtil.imageWidth;
                this.imagePane.height = this.kvmUtil.imageHeight;
                this.imagePane.setImage(this.previImage);
                this.imagePane.colorBit = this.kvmUtil.colorBit;
                if (this.kvmInterface.isFullScreen)
                {
                  if (!this.kvmInterface.base.isDiv)
                  {
                    if (this.kvmInterface.fullScreen != null) {
                      this.kvmInterface.fullScreen.setVisible(true);
                    }
                    else {
                      Debug.printExc("KVMInterface.fullScreen == null");
                    } 
                  }
                }
                this.firstJudge = false;
                this.kvmUtil.resolutionCh = false;
                continue;
              } 
              this.imagePane.setImage(this.previImage);
              this.imagePane.colorBit = this.kvmUtil.colorBit;
            }
          }
          catch (KVMException ex) {
            Debug.println(ex.getMessage());
          } 
        } 
        continue;
      } 
      if (arrayImage(imageData)) {
        count = 0;
      }
      else {
        count = 1;
      } 
      imageData = null;
      this.currentData = null;
      while (true) {
        if (this.kvmUtil.isComplete(this.comImage.remove(0), this.bladeNo)) {
          try {
            aImagePackedData = this.kvmUtil.getZipImageData();
          }
          catch (KVMException kvme) {
            Debug.println(kvme.getMessage());
          } 
          try {
            if (!this.kvmInterface.base.isDiv && this.isDisplay)
            {
              this.kvmInterface.statusBar.frameNum++;
            }
            if (aImagePackedData.length != 6 || aImagePackedData[1] != 0 || !this.kvmUtil.dispDiff || this.kvmUtil.resolutionCh || this.firstJudge) {
              byte[] data = this.kvmUtil.unZipData(aImagePackedData);
              aImagePackedData = null;
              if (data == null) {
                Debug.println("unzip error");
                this.bladeCommu.sentData(this.kvmInterface.packData.resendData(this.bladeNo));
                continue label136;
              } 
              if (this.firstJudge || this.kvmUtil.resolutionCh)
              {
                this.previImage = new byte[data.length];
              }
              if (this.kvmUtil.dispDiff) {
                if (!this.kvmUtil.xorData(data, this.previImage)) {
                  Debug.printExc("xor error");
                  this.bladeCommu.sentData(this.kvmInterface.packData.resendData(this.bladeNo));
                  data = null;
                  continue label136;
                } 
              } else {
                System.arraycopy(data, 0, this.previImage, 0, this.previImage.length);
              } 
              if (!this.kvmInterface.base.isDiv) {
                this.imagePane.remotePreX = this.imagePane.remoteX;
                this.imagePane.remotePreY = this.imagePane.remoteY;
                this.imagePane.remoteX = this.kvmUtil.remoteX;
                this.imagePane.remoteY = this.kvmUtil.remoteY;
              } 
              if (this.firstJudge || this.kvmUtil.resolutionCh)
              {
                this.imagePane.resolutionCh = true;
                this.imagePane.width = this.kvmUtil.imageWidth;
                this.imagePane.height = this.kvmUtil.imageHeight;
                this.imagePane.setImage(this.previImage);
                this.imagePane.colorBit = this.kvmUtil.colorBit;
                if (!this.kvmInterface.isFullScreen) {
                  this.kvmInterface.tabbedpane.setPreferredSize(new Dimension(this.kvmUtil.imageWidth + (this.imagePane.getLocation()).x, this.kvmUtil.imageHeight + (this.imagePane.getLocation()).y + 5));
                  this.kvmInterface.tabbedpane.setVisible(false);
                  this.kvmInterface.tabbedpane.setVisible(true);
                }
                else if (!this.kvmInterface.base.isDiv) {
                  this.kvmInterface.fullScreen.imageParentPane.setPreferredSize(new Dimension(this.kvmUtil.imageWidth, this.kvmUtil.imageHeight));
                  this.kvmInterface.fullScreen.imageParentPane.setVisible(false);
                  this.kvmInterface.fullScreen.imageParentPane.setVisible(true);
                  if (this.kvmInterface.fullScreen != null) {
                    this.kvmInterface.fullScreen.setVisible(true);
                  }
                  else {
                    Debug.printExc("KVMInterface.fullScreen == null");
                  } 
                } 
                this.imagePane.requestFocus();
                this.firstJudge = false;
                this.kvmUtil.resolutionCh = false;
              }
              else
              {
                this.imagePane.setImage(this.previImage);
                this.imagePane.colorBit = this.kvmUtil.colorBit;
              }
            } 
          } catch (KVMException ex) {
            Debug.println(ex.getMessage());
          } 
        } 
        count++;
        if (count >= 2) {
          continue label136;
        }
      } 
    } 
  }
  private boolean video(byte[] aImagePackedData) {
    if (this.kvmUtil.dispDiff && !this.kvmInterface.floatToolbar.powerMenu.dissflag) {
      this.bladeCommu.sentData(this.kvmInterface.packData.resendData(this.bladeNo));
      this.kvmInterface.floatToolbar.powerMenu.dissflag = true;
      return false;
    } 
    if (!this.kvmUtil.dispDiff)
    {
      this.kvmInterface.floatToolbar.powerMenu.isIdiss = true;
    }
    if (this.kvmInterface.floatToolbar.powerMenu.isIdiss) {
      if (this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect.frameSeqIndex % 200 == 0 && this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect.frameSeqIndex > 1)
      {
        if (this.kvmUtil.dispDiff) {
          if (!this.isIFrameSend) {
            this.bladeCommu.sentData(this.kvmInterface.packData.resendData(this.bladeNo));
            this.isIFrameSend = true;
          } 
          return false;
        } 
      }
      this.isIFrameSend = false;
      byte[] collectTempData = new byte[aImagePackedData.length];
      System.arraycopy(aImagePackedData, 0, collectTempData, 0, collectTempData.length);
      KinescopeFrame kinescopeFrame = new KinescopeFrame();
      kinescopeFrame.setSourceData(collectTempData);
      if (!this.kvmUtil.dispDiff) {
        kinescopeFrame.setIFrame(true);
      }
      else {
        kinescopeFrame.setIFrame(false);
      } 
      this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect.frameSeqIndex++;
      kinescopeFrame.setSeqFrame(this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect.frameSeqIndex);
      kinescopeFrame.setData(System.currentTimeMillis());
      kinescopeFrame.setWidth(this.kvmUtil.imageWidth);
      kinescopeFrame.setHeight(this.kvmUtil.imageHeight);
      this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect.frameList.add(kinescopeFrame);
    } 
    return true;
  }
  public void setFirstJudge(boolean firstJudge) {
    this.firstJudge = firstJudge;
  }
  private boolean arrayImage(byte[] imageData) {
    boolean sequ = false;
    if (this.comImage.size() == 0) {
      this.currentData = this.lList.remove(0);
      byte[] imageDataNext = new byte[this.currentData.length - 2];
      System.arraycopy(this.currentData, 2, imageDataNext, 0, imageDataNext.length);
      if (imageData[2] == imageDataNext[2]) {
        if (KVMUtil.byteToIntCon(imageData, 0, 2) < KVMUtil.byteToIntCon(imageDataNext, 0, 2))
        {
          this.comImage.add(imageData);
          this.comImage.add(imageDataNext);
          if (KVMUtil.byteToIntCon(imageDataNext, 0, 2) - KVMUtil.byteToIntCon(imageData, 0, 2) == 1)
          {
            sequ = true;
          }
        }
        else
        {
          this.comImage.add(imageDataNext);
          this.comImage.add(imageData);
          if (KVMUtil.byteToIntCon(imageData, 0, 2) - KVMUtil.byteToIntCon(imageDataNext, 0, 2) == 1)
          {
            sequ = true;
          }
        }
      }
      else if (imageData[2] < imageDataNext[2]) {
        int tem = imageDataNext[2] - imageData[2];
        if (tem <= 128 && tem > 0)
        {
          this.comImage.add(imageData);
          this.comImage.add(imageDataNext);
        }
        else
        {
          this.comImage.add(imageDataNext);
          this.comImage.add(imageData);
        }
      } else {
        int tem = imageData[2] - imageDataNext[2];
        if (tem <= 128 && tem > 0)
        {
          this.comImage.add(imageDataNext);
          this.comImage.add(imageData);
        }
        else
        {
          this.comImage.add(imageData);
          this.comImage.add(imageDataNext);
        }
      }
    } else {
      byte[] imageDataPre = this.comImage.remove(0);
      if (imageDataPre[2] == imageData[2]) {
        if (KVMUtil.byteToIntCon(imageDataPre, 0, 2) < KVMUtil.byteToIntCon(imageData, 0, 2))
        {
          this.comImage.add(imageDataPre);
          this.comImage.add(imageData);
          if (KVMUtil.byteToIntCon(imageData, 0, 2) - KVMUtil.byteToIntCon(imageDataPre, 0, 2) == 1)
          {
            sequ = true;
          }
        }
        else
        {
          this.comImage.add(imageData);
          this.comImage.add(imageDataPre);
          if (KVMUtil.byteToIntCon(imageDataPre, 0, 2) - KVMUtil.byteToIntCon(imageData, 0, 2) == 1)
          {
            sequ = true;
          }
        }
      }
      else if (imageDataPre[2] < imageData[2]) {
        int tem = imageData[2] - imageDataPre[2];
        if (tem <= 128 && tem > 0)
        {
          this.comImage.add(imageDataPre);
          this.comImage.add(imageData);
        }
        else
        {
          this.comImage.add(imageData);
          this.comImage.add(imageDataPre);
        }
      } else {
        int tem = imageDataPre[2] - imageData[2];
        if (tem <= 128 && tem > 0) {
          this.comImage.add(imageData);
          this.comImage.add(imageDataPre);
        }
        else {
          this.comImage.add(imageDataPre);
          this.comImage.add(imageData);
        } 
      } 
    } 
    return sequ;
  }
  public class DrawImageTimer
    extends TimerTask {
    int count = 0;
    public void run() {
      if (!DrawThread.this.kvmInterface.base.isDiv && DrawThread.this.isDisplay)
      {
        if (DrawThread.this.isFocusOwner) {
          if (this.count == 10) {
            if (!DrawThread.this.imagePane.isFocusOwner())
            {
              DrawThread.this.bladeCommu.sentData(DrawThread.this.imagePane.pack.clearKey(DrawThread.this.imagePane.bladeNO));
              if (DrawThread.this.kvmInterface.isFullScreen && DrawThread.this.isNew)
              {
                MouseDisplacementImpl.setMode(0);
              }
              DrawThread.this.isFocusOwner = false;
              this.count = 0;
            }
            else
            {
              this.count = 0;
            }
          } else {
            this.count++;
          }
        }
        else if (DrawThread.this.imagePane.isFocusOwner()) {
          this.count = 0;
          DrawThread.this.isFocusOwner = true;
        }
        else if (this.count == 20) {
          DrawThread.this.bladeCommu.sentData(DrawThread.this.imagePane.pack.clearKey(DrawThread.this.imagePane.bladeNO));
          if (DrawThread.this.kvmInterface.isFullScreen && DrawThread.this.isNew)
          {
            MouseDisplacementImpl.setMode(0);
          }
          this.count = 0;
        }
        else {
          this.count++;
        } 
      }
    }
  }
  public void setComImage(LinkedList<byte[]> comImage) {
    this.comImage = comImage;
  }
  public void setKvmUtil(KVMUtil kvmUtil) {
    this.kvmUtil = kvmUtil;
  }
  public KVMUtil getKvmUtil() {
    return this.kvmUtil;
  }
  public LinkedList<byte[]> getComImage() {
    return this.comImage;
  }
  public void setPreviImage(byte[] previImage) {
    this.previImage = previImage;
  }
  public byte[] getPreviImage() {
    return this.previImage;
  }
  public int getBladeNo() {
    return this.bladeNo;
  }
  public void setBladeNo(int bladeNo) {
    this.bladeNo = bladeNo;
  }
  public void setBladeCommu(BladeCommu bladeCommu) {
    this.bladeCommu = bladeCommu;
  }
  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }
  public BladeCommu getBladeCommu() {
    return this.bladeCommu;
  }
}
