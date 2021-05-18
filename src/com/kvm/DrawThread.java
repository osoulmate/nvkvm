package com.kvm;
import com.Kinescope.KinescopeFrame;
import com.library.LibException;
import com.library.decoder.ImageDecoder;
import com.library.decoder.JPEGData;
import com.library.decoder.RLEJPEGUtil;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Timer;
import java.util.Vector;
public class DrawThread
  extends Thread
{
  private BladeCommu bladeCommu = null;
  private KVMUtil kvmUtil;
  private byte[] currentData;
  private boolean conn = false;
  private byte[] previImage = null;
  private Object previImages = null;
  private boolean firstJudge = true;
  private LinkedList<byte[]> comImage = (LinkedList)new LinkedList<>();
  private boolean isFocusOwner = true;
  private boolean isNew = true;
  private boolean isDisplay = false;
  private int bladeNo;
  private ImagePane imagePane;
  public boolean isDisplay() {
    return this.isDisplay;
  }
  public void setDisplay(boolean isDisplay) {
    this.isDisplay = isDisplay;
  }
  public int getBladeNoByDrawThread() {
    return this.bladeNo;
  }
  public ImagePane getImagePane() {
    return this.imagePane;
  }
  public void setImagePane(ImagePane imagePane) {
    this.imagePane = imagePane;
  }
  private boolean totalConn = false;
  private Vector<Object> lList = new Vector(10);
  private Timer timer;
  public Vector<Object> getlList() {
    return this.lList;
  }
  public void setlList(Vector<Object> lList) {
    this.lList = lList;
  }
  public void setTimer(Timer timer) {
    this.timer = timer;
  }
  private KVMInterface kvmInterface = null;
  private boolean isIFrameSend = false;
  ImageDecoder imageDecoder;
  public void setKvmInterface(KVMInterface kvmInterface2) {
    this.kvmInterface = kvmInterface2;
  }
  public DrawThread(int bladeNo, ImagePane imagePane, KVMUtil kvmUtil) {
    this.bladeNo = bladeNo;
    this.imagePane = imagePane;
    this.kvmUtil = kvmUtil;
    this.isNew = imagePane.isNew();
    this.imageDecoder = new ImageDecoder(kvmUtil.getImageWidth(), kvmUtil.getImageHeight());
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
    this.timer.schedule(new DrawImageTimer(this), 0L, 50L);
    int reallen = 0;
    int iFocus = 0;
    int size = 0;
    byte[] imageData = null;
    byte[] aImagePackedData = null;
    byte[] CompressData = null;
    byte[] temDes = null;
    byte[] RealData = null;
    byte[] UnCompressData = null;
    try {
      while (this.totalConn && this.conn)
      {
        imageData = null;
        aImagePackedData = null;
        CompressData = null;
        temDes = null;
        RealData = null;
        UnCompressData = null;
        reallen = 0;
        size = this.lList.size();
        while (size < 2) {
          Thread.sleep(5L);
          size = this.lList.size();
        } 
        this.currentData = (byte[])this.lList.remove(0);
        if (this.currentData == null) {
          continue;
        }
        if ((this.currentData[2] & 0xFF) == 0 && (this.currentData[3] & 0xFF) == 0) {
          Base.setCurrentDqtSize(this.currentData[18] & 0xF);
          if (Base.getStartVideo() != 0 && null != this.kvmInterface
            .getFloatToolbar().getPowerMenu().getKineScopeDataCollect() && this.kvmInterface.getFloatToolbar()
            .getPowerMenu()
            .getKineScopeDataCollect()
            .isCollect())
          {
            Base.setStartVideo(this.currentData[18] >> 7 & 0x1);
          }
          Base.setIsIFrame(this.currentData[18] >> 7 & 0x1);
          JPEGData.setDqttags(Base.getCurrentDqtSize() - 1);
        } 
        if (Base.getIsNewCompAlgorithm() && this.currentData[2] == 0 && this.currentData[3] == 0) {
          int isSameFrame = (this.currentData[9] & 0x80) >> 7;
          if (1 == isSameFrame) {
            if (this.isDisplay) {
              int frameNum = this.kvmInterface.getStatusBar().getFrameNum();
              frameNum++;
              this.kvmInterface.getStatusBar().setFrameNum(frameNum);
            } 
            continue;
          } 
        } else if (Base.getCompress() == 1) {
          reallen += this.currentData[5] & 0xFF;
          CompressData = new byte[this.currentData.length - 6];
          UnCompressData = new byte[this.currentData.length];
          System.arraycopy(this.currentData, 6, CompressData, 0, CompressData.length);
          temDes = AESHandler.decry(CompressData, Base.getKvm_key(), CompressData.length);
          if (null == temDes || 0 == temDes.length) {
            continue;
          }
          System.arraycopy(this.currentData, 0, UnCompressData, 0, 5);
          System.arraycopy(temDes, 0, UnCompressData, 5, reallen);
        } 
        if (Base.getCompress() == 1 && (this.currentData[2] != 0 || this.currentData[3] != 0)) {
          reallen = reallen + 5 - 2;
          RealData = UnCompressData;
        }
        else {
          reallen = this.currentData.length - 2;
          RealData = this.currentData;
        } 
        imageData = new byte[reallen];
        System.arraycopy(RealData, 2, imageData, 0, imageData.length);
        if (this.kvmUtil.isComplete(imageData, this.bladeNo))
        {
          if (this.kvmUtil.getImageFloatToolbar(this.bladeNo) != null)
          {
            this.kvmUtil.getImageFloatToolbar(this.bladeNo).startStateMenu();
          }
          try {
            aImagePackedData = this.kvmUtil.getZipImageData();
          }
          catch (LibException kvme) {
            Debug.println(kvme.getClass().getName());
            continue;
          } 
          if (this.isDisplay) {
            int frameNum = this.kvmInterface.getStatusBar().getFrameNum();
            frameNum++;
            this.kvmInterface.getStatusBar().setFrameNum(frameNum);
          } 
          if (!Base.getIsNewCompAlgorithm()) {
            if (aImagePackedData.length == 6 && aImagePackedData[1] == 0 && this.kvmUtil.isDispDiff() && 
              !this.kvmUtil.isResolutionCh() && !this.firstJudge) {
              continue;
            }
            if (null != this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect() && this.kvmInterface
              .getFloatToolbar().getPowerMenu().getKineScopeDataCollect().isCollect())
            {
              if (!video(aImagePackedData)) {
                continue;
              }
            }
            byte[] data = RLEJPEGUtil.unZipData(aImagePackedData, this.kvmUtil.getImageWidth(), this.kvmUtil.getImageHeight());
            this.kvmUtil.setImageData(data);
            aImagePackedData = null;
            if (data == null) {
              Debug.println("unzip error");
              this.bladeCommu.sentData(this.kvmInterface.getPackData().resendData(this.bladeNo));
              continue;
            } 
            if (this.firstJudge || this.kvmUtil.isResolutionCh())
            {
              this.previImage = new byte[data.length];
            }
            if (this.kvmUtil.isDispDiff()) {
              if (!this.kvmUtil.xorData(data, this.previImage)) {
                Debug.printExc("xor error");
                this.bladeCommu.sentData(this.kvmInterface.getPackData().resendData(this.bladeNo));
                continue;
              } 
            } else {
              System.arraycopy(data, 0, this.previImage, 0, this.previImage.length);
            }
          }
          else {
            if (this.kvmUtil.isDispDiff() && 
              !this.kvmUtil.isResolutionCh() && !this.firstJudge) {
              continue;
            }
            if (null != this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect() && this.kvmInterface
              .getFloatToolbar().getPowerMenu().getKineScopeDataCollect().isCollect())
            {
              video_new(aImagePackedData);
            }
            this
              .previImages = this.imageDecoder.decodeRLEorJPEG0(aImagePackedData, this.kvmUtil
                .getImageWidth(), this.kvmUtil
                .getImageHeight());
            aImagePackedData = null;
            if (this.previImages == null) {
              continue;
            }
          } 
          this.imagePane.setRemotePreX(this.imagePane.getRemoteX());
          this.imagePane.setRemotePreY(this.imagePane.getRemoteY());
          this.imagePane.setRemoteX(this.kvmUtil.getRemoteX());
          this.imagePane.setRemoteY(this.kvmUtil.getRemoteY());
          if (this.firstJudge || this.kvmUtil.isResolutionCh()) {
            iFocus = this.kvmInterface.getKvmUtil().getiWindosFocus();
            this.imagePane.setResolutionCh(true);
            this.imagePane.setImagePaneWidth(this.kvmUtil.getImageWidth());
            this.imagePane.setImagePaneHeight(this.kvmUtil.getImageHeight());
            if (!Base.getIsNewCompAlgorithm()) {
              this.imagePane.setImage(this.previImage);
            }
            else {
              this.imagePane.setImages(this.previImages);
            } 
            this.imagePane.setColorBit(this.kvmUtil.getColorBit());
            if (!this.kvmInterface.isFullScreen()) {
              this.kvmInterface.getTabbedpane().setPreferredSize(new Dimension(this.kvmUtil.getImageWidth() + 
                    (this.imagePane.getLocation()).x, this.kvmUtil.getImageHeight() + (this.imagePane.getLocation()).y + 5));
              this.kvmInterface.getTabbedpane().setVisible(false);
              this.kvmInterface.getTabbedpane().setVisible(true);
            }
            else if (this.kvmInterface.getFullScreen() != null) {
              this.kvmInterface.getFullScreen()
                .getImageParentPane()
                .setPreferredSize(new Dimension(this.kvmUtil.getImageWidth(), this.kvmUtil.getImageHeight()));
              this.kvmInterface.getFullScreen().getImageParentPane().setVisible(false);
              this.kvmInterface.getFullScreen().getImageParentPane().setVisible(true);
              this.kvmInterface.getFullScreen().setVisible(true);
            }
            else {
              Debug.printExc("KVMInterface.fullScreen == null");
            } 
            if (1 == iFocus)
            {
              this.imagePane.requestFocus();
            }
            this.firstJudge = false;
            this.kvmUtil.setResolutionCh(false);
            continue;
          } 
          if (!Base.getIsNewCompAlgorithm()) {
            this.imagePane.setImage(this.previImage);
          }
          else {
            this.imagePane.setImages(this.previImages);
          } 
          this.imagePane.setColorBit(this.kvmUtil.getColorBit());
        }
      }
    }
    catch (LibException ex) {
      Debug.println(ex.getClass().getName());
    }
    catch (InterruptedException e) {
      Debug.printExc(e.getClass().getName());
    } 
  }
  private boolean video(byte[] aImagePackedData) {
    if (this.kvmUtil.isDispDiff() && !this.kvmInterface.getFloatToolbar().getPowerMenu().isDissflag()) {
      this.bladeCommu.sentData(this.kvmInterface.getPackData().resendData(this.bladeNo));
      this.kvmInterface.getFloatToolbar().getPowerMenu().setDissflag(true);
      return false;
    } 
    if (!this.kvmUtil.isDispDiff())
    {
      this.kvmInterface.getFloatToolbar().getPowerMenu().setIdiss(true);
    }
    if (this.kvmInterface.getFloatToolbar().getPowerMenu().isIdiss()) {
      if (this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect().getFrameSeqIndex() % 200 == 0 && this.kvmInterface
        .getFloatToolbar().getPowerMenu().getKineScopeDataCollect().getFrameSeqIndex() > 1)
      {
        if (this.kvmUtil.isDispDiff()) {
          if (!this.isIFrameSend) {
            this.bladeCommu.sentData(this.kvmInterface.getPackData().resendData(this.bladeNo));
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
      if (!this.kvmUtil.isDispDiff()) {
        kinescopeFrame.setIFrame(true);
      }
      else {
        kinescopeFrame.setIFrame(false);
      } 
      this.kvmInterface.getFloatToolbar()
        .getPowerMenu()
        .getKineScopeDataCollect()
        .setFrameSeqIndex(this.kvmInterface.getFloatToolbar()
          .getPowerMenu()
          .getKineScopeDataCollect()
          .getFrameSeqIndex() + 1);
      kinescopeFrame.setSeqFrame(this.kvmInterface.getFloatToolbar()
          .getPowerMenu()
          .getKineScopeDataCollect()
          .getFrameSeqIndex());
      kinescopeFrame.setData(System.currentTimeMillis());
      kinescopeFrame.setWidth(this.kvmUtil.getImageWidth());
      kinescopeFrame.setHeight(this.kvmUtil.getImageHeight());
      this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect().getFrameList().add(kinescopeFrame);
    } 
    return true;
  }
  private boolean video_new(byte[] aImagePackedData) {
    if (this.kvmUtil.isDispDiff() && !this.kvmInterface.getFloatToolbar().getPowerMenu().isDissflag()) {
      this.bladeCommu.sentData(this.kvmInterface.getPackData().resendData(this.bladeNo));
      this.kvmInterface.getFloatToolbar().getPowerMenu().setDissflag(true);
      return false;
    } 
    if (1 == Base.getStartVideo())
    {
      return false;
    }
    if (!this.kvmUtil.isDispDiff())
    {
      this.kvmInterface.getFloatToolbar().getPowerMenu().setIdiss(true);
    }
    if (this.kvmInterface.getFloatToolbar().getPowerMenu().isIdiss()) {
      if (this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect().getFrameSeqIndex() % 200 == 0 && this.kvmInterface
        .getFloatToolbar().getPowerMenu().getKineScopeDataCollect().getFrameSeqIndex() > 1)
      {
        if (this.kvmUtil.isDispDiff()) {
          if (!this.isIFrameSend) {
            this.bladeCommu.sentData(this.kvmInterface.getPackData().resendData(this.bladeNo));
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
      kinescopeFrame.setDqt(Base.getCurrentDqtSize());
      if (Base.getIsIFrame() == 0) {
        kinescopeFrame.setIFrame(true);
      }
      else {
        kinescopeFrame.setIFrame(false);
      } 
      this.kvmInterface.getFloatToolbar()
        .getPowerMenu()
        .getKineScopeDataCollect()
        .setFrameSeqIndex(this.kvmInterface.getFloatToolbar()
          .getPowerMenu()
          .getKineScopeDataCollect()
          .getFrameSeqIndex() + 1);
      kinescopeFrame.setSeqFrame(this.kvmInterface.getFloatToolbar()
          .getPowerMenu()
          .getKineScopeDataCollect()
          .getFrameSeqIndex());
      kinescopeFrame.setData(System.currentTimeMillis());
      kinescopeFrame.setWidth(this.kvmUtil.getImageWidth());
      kinescopeFrame.setHeight(this.kvmUtil.getImageHeight());
      this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect().getFrameList().add(kinescopeFrame);
    } 
    return true;
  }
  public void setFirstJudge(boolean firstJudge) {
    this.firstJudge = firstJudge;
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
    if (null != previImage) {
      this.previImage = (byte[])previImage.clone();
    }
    else {
      this.previImage = null;
    } 
  }
  public byte[] getPreviImage() {
    if (null != this.previImage)
    {
      return (byte[])this.previImage.clone();
    }
    byte[] tmp = null;
    return tmp;
  }
  public void setFlageImage(boolean flageImage) {}
  public int getBladeNum() {
    return this.bladeNo;
  }
  public void setBladeNum(int bladeNo) {
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
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public boolean isFocusOwner() {
    return this.isFocusOwner;
  }
  public void setFocusOwner(boolean isFocusOwner) {
    this.isFocusOwner = isFocusOwner;
  }
  public boolean isNew() {
    return this.isNew;
  }
}
