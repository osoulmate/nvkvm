package com.kvmV1;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
public class ClientSocketCommunity
  extends Thread
{
  private boolean conn = false;
  private static final int KVM_COMMAND = 0;
  private static final int BLADE_POSITI = 1;
  private static final int KEYSTATE_POSITI = 2;
  private static final int IMAGEDATA_POSITI = 2;
  private ArrayList<String> compareBuf = new ArrayList<String>();
  private static final int FILTER_SIZE = 20;
  public HashMap<String, Integer> bladeMap = new HashMap<String, Integer>();
  public Hashtable<String, Byte> keyState = new Hashtable<String, Byte>();
  public Hashtable<String, byte[]> bladePresentInfo = (Hashtable)new Hashtable<String, byte[]>();
  public Hashtable<String, byte[]> bladeStateInfo = (Hashtable)new Hashtable<String, byte[]>();
  public String bladePreIndex = "0";
  public ClientSocketCommunity clientSocket;
  public KVMInterface kvmInterface = null;
  public UnPackData unPack = null;
  public void setConn(boolean b) {
    this.conn = b;
  }
  public boolean getConn() {
    return this.conn;
  }
  public void run() {
    byte[] bytes = null;
    while (this.conn) {
      try {
        bytes = this.kvmInterface.client.getSmmData();
      }
      catch (Exception e) {
        break;
      } 
      if (bytes == null) {
        if (this.kvmInterface.base.getWaitTime == -1L) {
          this.kvmInterface.base.getWaitTime = 0L;
          break;
        } 
        continue;
      } 
      this.kvmInterface.kvmUtil.start = 0;
      while (this.kvmInterface.kvmUtil.diviStreamNew(bytes, false)) {
        this.unPack.setkvmType(this.kvmInterface.kvmUtil.result);
        byte[] unPackData = null;
        unPackData = this.unPack.getData();
        if (unPackData == null) {
          continue;
        }
        Debug.println("KVM_COMMAND:" + unPackData[0]);
        switch (unPackData[0]) {
          case 1:
            distributeBladePresentData(unPackData);
            continue;
          case 21:
            reportBladeState(unPackData);
            continue;
          case 29:
            switchChannel(unPackData);
            continue;
          case 33:
            Debug.println("rapconnect_balde command.......");
            rapCloseBlade(unPackData);
            continue;
        } 
        Debug.println("control error data::" + unPackData[0]);
      } 
    } 
  }
  private void rapCloseBlade(byte[] unPackData) {
    Debug.println("enter rapCloseBlade........");
    byte bNO = unPackData[1];
    this.kvmInterface.tabbedpane.getModel().removeChangeListener(this.kvmInterface.kvmUtil.changeListener);
    if (this.kvmInterface.isFullScreen)
    {
      this.kvmInterface.kvmUtil.returnToWin();
    }
    BladeThread bladeThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bNO));
    bladeThread.bladeCommu.setAutoFlag(false);
    this.kvmInterface.kvmUtil.disconnectBlade(bNO);
    this.kvmInterface.tabbedpane.getModel().addChangeListener(this.kvmInterface.kvmUtil.changeListener);
    JOptionPane jOptionPane = new JOptionPane(this.kvmInterface.kvmUtil.getString("connection.fail.rap") + bNO);
    JDialog jd = jOptionPane.createDialog(this.kvmInterface.toolbar, this.kvmInterface.kvmUtil.getString("connection.message"));
    DisposeTask dt = new DisposeTask(jd);
    dt.start();
    jd.setVisible(true);
    Debug.println("exit rapCloseBlade");
  }
  public boolean fileImageData(byte[] unPackData) {
    boolean judge = true;
    StringBuffer temBuffer = new StringBuffer();
    temBuffer.append(unPackData[1]);
    temBuffer.append(unPackData[2]);
    int IMAGEDATA_POSITI1 = 3;
    int IMAGEDATA_POSITI2 = 4;
    try {
      temBuffer.append(unPackData[IMAGEDATA_POSITI1]);
      temBuffer.append(unPackData[IMAGEDATA_POSITI2]);
    }
    catch (ArrayIndexOutOfBoundsException ex) {
      return false;
    } 
    int count = this.compareBuf.size();
    for (int i = 0; i < count; i++) {
      if (temBuffer.toString().equals(this.compareBuf.get(i))) {
        judge = false;
        break;
      } 
    } 
    if (judge)
    {
      if (this.compareBuf.size() == 20) {
        this.compareBuf.remove(0);
        this.compareBuf.add(temBuffer.toString());
      }
      else {
        this.compareBuf.add(temBuffer.toString());
      } 
    }
    return judge;
  }
  public void distributeImageData(byte[] unPackData) {
    Iterator<String> iter = this.kvmInterface.base.threadGroup.keySet().iterator();
    String name = "";
    Byte b = Byte.valueOf(unPackData[1]);
    if (b.byteValue() == 0) {
      return;
    }
    boolean errorFlag = true;
    while (iter.hasNext()) {
      name = iter.next();
      if (name.equals(String.valueOf(b))) {
        BladeThread bladeThread = this.kvmInterface.base.threadGroup.get(name);
        (this.kvmInterface.kvmUtil.getImagePane(b.intValue())).imageReceive++;
        if ((bladeThread.getDrawThread()).isDisplay)
        {
          (bladeThread.getDrawThread()).lList.add(unPackData);
        }
        errorFlag = false;
        break;
      } 
    } 
    if (errorFlag) {
      Debug.printExc("name = " + name + "bladeNO = " + b);
      try {
        this.kvmInterface.client.sentData(this.kvmInterface.packData.interruptBlade(b.intValue()));
        this.kvmInterface.client.sentData(this.kvmInterface.packData.interruptMonitor(b.intValue()));
      }
      catch (KVMException e) {
        if ("IO_ERRCODE".equals(e.getErrCode()))
        {
          JOptionPane.showMessageDialog(this.kvmInterface.toolbar, this.kvmInterface.kvmUtil.getString("Network_interrupt_message"));
        }
      } 
    } 
  }
  public void distributeKeyStateData(byte[] unPackData) {
    Byte b = Byte.valueOf(unPackData[1]);
    ImagePane imagePane = this.kvmInterface.kvmUtil.getImagePane(b.intValue());
    if (imagePane != null) {
      this.keyState.clear();
      this.keyState.put(b.toString(), Byte.valueOf(unPackData[2]));
      imagePane.imageReceive++;
      if (!this.kvmInterface.isFullScreen) {
        if (b.intValue() == this.kvmInterface.actionBlade)
        {
          this.kvmInterface.kvmUtil.setNumAndCapLock();
        }
      }
      else if (b.intValue() == this.kvmInterface.fullScreen.actionBlade) {
        this.kvmInterface.kvmUtil.setNumAndCapLock();
      } 
    } 
  }
  public void distributeBladePresentData(byte[] unPackData) {
    byte[] bladePreInfo = new byte[2];
    bladePreInfo[0] = unPackData[2];
    bladePreInfo[1] = unPackData[1];
    if (this.kvmInterface.getBladeFlag() != null) {
      int tem = Integer.parseInt(this.kvmInterface.getBladeFlag() + "1", 2);
      bladePreInfo[0] = (byte)(bladePreInfo[0] & tem & 0xFF);
      bladePreInfo[1] = (byte)(bladePreInfo[1] & tem >>> 8 & 0xFF);
    } 
    this.bladePresentInfo.put(this.bladePreIndex, bladePreInfo);
  }
  public void reportBladeState(byte[] unPackData) {
    byte bNO = unPackData[1];
    Object lock = this.kvmInterface.base.getLock(bNO);
    synchronized (lock) {
      if (this.bladeMap.get(String.valueOf(bNO)) == null) {
        byte b = unPackData[1];
        if (b < 0) {
          b = 100;
          unPackData[1] = 100;
        } 
        byte[] tempStateInfo = new byte[unPackData.length - 1];
        System.arraycopy(unPackData, 1, tempStateInfo, 0, tempStateInfo.length);
        Debug.printByte(unPackData);
        this.bladeStateInfo.put(b + "", tempStateInfo);
      }
      else {
        this.bladeMap.remove(String.valueOf(bNO));
        byte b = unPackData[1];
        if (b < 0) {
          b = 100;
          unPackData[1] = 100;
        } 
        byte[] tempStateInfo = new byte[unPackData.length - 1];
        System.arraycopy(unPackData, 1, tempStateInfo, 0, tempStateInfo.length);
        byte bladeNO = unPackData[1];
        ImagePane imagePane = this.kvmInterface.kvmUtil.getImagePane(bladeNO);
        BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO));
        if (bThread == null || imagePane == null) {
          return;
        }
        BladeState bladeState = bThread.kvmUtil.getBladeStateAuto(bladeNO, "", tempStateInfo);
        if (bThread.bladeCommu.isAutoFlag()) {
          bThread.bladeCommu.setRequest(false);
          if (bladeState.isEnable() && this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO)) != null) {
            reConnBlade(bladeState, bladeNO, bThread, imagePane);
          }
          else {
            if (!bThread.bladeCommu.socket.isClosed() || this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO)) == null) {
              return;
            }
            try {
              try {
                Thread.sleep(1000L);
              }
              catch (InterruptedException e) {
                Debug.printExc(e.getMessage());
              } 
              this.kvmInterface.client.sentData(this.kvmInterface.packData.reqBladeState(bladeNO, Base.connMode));
              this.kvmInterface.clientSocket.bladeMap.put(String.valueOf(bladeNO), Integer.valueOf(bladeNO));
            }
            catch (KVMException ex) {
              Debug.printExc(ex.getMessage());
            } 
          } 
          return;
        } 
        BladeThread bladeThread = null;
        if (bladeState.isEnable()) {
          try {
            bladeThread = new BladeThread(bladeState.getBladeIP(), bladeState.getBladePort(), bladeNO, true);
            bladeThread.bladeCommu.sentData((this.kvmInterface.kvmUtil.getImagePane(this.kvmInterface.actionBlade)).pack.mouseModeControl((byte)36, (byte)2, bladeThread.getBladeNO()));
            bladeThread.setName("BladeThread" + bladeNO);
            bladeThread.setDrawThread(bThread.getDrawThread());
            bladeThread.setKvmUtil(bThread.kvmUtil);
            bladeThread.setKvmInterface(bThread.kvmInterface);
            bladeThread.setUnPackData(bThread.unPack);
            this.kvmInterface.base.threadGroup.remove(String.valueOf(bladeNO));
            this.kvmInterface.base.threadGroup.put(String.valueOf(bladeNO), bladeThread);
          }
          catch (KVMException e) {
            imagePane.addKeyListener(imagePane.keyListener);
            imagePane.addMouseMotionListener(imagePane);
            imagePane.addMouseListener(imagePane);
            this.kvmInterface.tabbedpane.getModel().removeChangeListener(this.kvmInterface.kvmUtil.changeListener);
            if (this.kvmInterface.isFullScreen)
            {
              this.kvmInterface.kvmUtil.returnToWin();
            }
            this.kvmInterface.kvmUtil.disconnectBlade(bladeNO);
            this.kvmInterface.tabbedpane.getModel().addChangeListener(this.kvmInterface.kvmUtil.changeListener);
            if (this.kvmInterface.getBladeSize() == 1) {
              JOptionPane.showMessageDialog(this.kvmInterface.toolbar, this.kvmInterface.kvmUtil.getString("Blade_disconnect_message"));
            }
            else {
              JOptionPane.showMessageDialog(this.kvmInterface.toolbar, "Blade" + bladeNO + " " + this.kvmInterface.kvmUtil.getString("Blade_disconnect_message"));
            } 
            return;
          } 
          bladeThread.bladeCommu.setKvmInterface(this.kvmInterface);
          bladeThread.bladeCommu.setClient(this.kvmInterface.client);
          bladeThread.bladeCommu.setBladeNO(bladeNO);
          imagePane.setNew(bladeState.isNew());
          imagePane.setBladeThread(bladeThread);
          bladeThread.setNew(true);
          if (!this.kvmInterface.base.isMstsc)
          {
            if (this.kvmInterface.isFullScreen) {
              this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.myCursor);
              imagePane.setCursor(this.kvmInterface.base.myCursor);
              if (null != this.kvmInterface.fullScreen.toolBar.mouseSynButton)
              {
                this.kvmInterface.fullScreen.toolBar.mouseSynButton.setEnabled(false);
              }
            }
            else if (null != this.kvmInterface.toolbar.mouseSynButton) {
              this.kvmInterface.toolbar.mouseSynButton.setEnabled(false);
            } 
            if (this.kvmInterface.base.isDiv)
            {
              this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.defCursor);
              imagePane.setCursor(this.kvmInterface.base.defCursor);
            }
          }
        } else {
          imagePane.addKeyListener(imagePane.keyListener);
          imagePane.addMouseMotionListener(imagePane);
          imagePane.addMouseListener(imagePane);
          this.kvmInterface.tabbedpane.getModel().removeChangeListener(this.kvmInterface.kvmUtil.changeListener);
          if (this.kvmInterface.isFullScreen)
          {
            this.kvmInterface.kvmUtil.returnToWin();
          }
          this.kvmInterface.kvmUtil.disconnectBlade(bladeNO);
          this.kvmInterface.tabbedpane.getModel().addChangeListener(this.kvmInterface.kvmUtil.changeListener);
          if (this.kvmInterface.getBladeSize() == 1) {
            JOptionPane.showMessageDialog(this.kvmInterface.toolbar, this.kvmInterface.kvmUtil.getString("Blade_disconnect_message"));
          }
          else {
            JOptionPane.showMessageDialog(this.kvmInterface.toolbar, "Blade" + bladeNO + " " + this.kvmInterface.kvmUtil.getString("Blade_disconnect_message"));
          } 
          return;
        } 
        imagePane.getClass(); 
        //imagePane.statReceiveTask = new ImagePane.StatReceiveTask(imagePane);
        KVMUtil.startReceiveList(imagePane);
        imagePane.getClass(); 
        //imagePane.mouseTimerTask = new ImagePane.MouseTimerTask(imagePane);
        KVMUtil.startMouseList(imagePane);
        bladeThread.bladeHeartTimer = new BladeHeartTimer(bladeThread);
        bladeThread.bladeHeartTimer.setName("BladeHeart" + bladeNO);
        bladeThread.bladeHeartTimer.start();
        if (bladeThread.kvmUtil != null) {
          bladeThread.kvmUtil.resultDivi.clear();
          bladeThread.kvmUtil.diviBuff.clear();
          bladeThread.kvmUtil.resetBuf();
          bladeThread.kvmUtil.firstJudge = true;
        } 
        bladeThread.getDrawThread().setBladeCommu(bladeThread.bladeCommu);
        bladeThread.getDrawThread().setNew(bladeThread.isNew());
        (bladeThread.getDrawThread()).lList.clear();
        bladeThread.getDrawThread().getComImage().clear();
        imagePane.addKeyListener(imagePane.keyListener);
        imagePane.addMouseMotionListener(imagePane);
        imagePane.addMouseListener(imagePane);
        bladeThread.start();
        bladeThread.bladeCommu.sentData(this.kvmInterface.packData.connectBlade(bladeNO, imagePane.custBit));
      } 
    } 
  }
  public void switchChannel(byte[] unPackData) {
    byte bladeNO = unPackData[1];
    boolean disFlag = false;
    if (this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO)) != null) {
      disFlag = true;
    }
    else if (this.kvmInterface.clickFlag) {
      try {
        Thread.sleep(5000L);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      } 
      if (this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO)) != null)
      {
        disFlag = true;
      }
      this.kvmInterface.clickFlag = false;
    } 
    if (disFlag) {
      BladeThread bladeThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO));
      bladeThread.bladeCommu.sentData(this.kvmInterface.packData.interruptTempBlade(bladeNO));
      bladeThread.bladeCommu.setAutoFlag(false);
      ImagePane imagePane = this.kvmInterface.kvmUtil.getImagePane(bladeNO);
      imagePane.mouseTimerTask = null;
      imagePane.receiveList.cancel();
      imagePane.statReceiveTask = null;
      bladeThread.bladeHeartTimer = null;
      imagePane.removeKeyListener(imagePane.keyListener);
      imagePane.removeMouseMotionListener(imagePane);
      imagePane.removeMouseListener(imagePane);
      bladeThread.setConn(false);
      try {
        bladeThread.bladeCommu.socket.close();
      }
      catch (IOException e) {
        Debug.printExc(e.getMessage());
      } finally {
        try {
          bladeThread.bladeCommu.socket.close();
        }
        catch (IOException e) {
          bladeThread = null;
          Debug.printExc(e.getMessage());
        } 
      } 
      try {
        Thread.sleep(2000L);
      }
      catch (InterruptedException e1) {
        e1.printStackTrace();
      } 
      try {
        this.kvmInterface.client.sentData(this.kvmInterface.packData.reqBladeState(bladeNO, Base.connMode));
        this.bladeMap.put(String.valueOf(bladeNO), Integer.valueOf(bladeNO));
      }
      catch (KVMException e) {
        if ("IO_ERRCODE".equals(e.getErrCode()))
        {
          JOptionPane.showMessageDialog(this.kvmInterface.toolbar, this.kvmInterface.kvmUtil.getString("Network_interrupt_message"));
        }
      } 
    } 
  }
  private void reConnBlade(BladeState bladeState, int bladeNO, BladeThread bThread, ImagePane imagePane) {
    BladeCommu bladeCommu = null;
    try {
      bladeCommu = new BladeCommu(bladeState.getBladeIP(), bladeState.getBladePort());
    }
    catch (KVMException e) {
      try {
        this.kvmInterface.client.sentData(this.kvmInterface.packData.reqBladeState(bladeNO, Base.connMode));
        this.kvmInterface.clientSocket.bladeMap.put(String.valueOf(bladeNO), Integer.valueOf(bladeNO));
      }
      catch (KVMException ex1) {}
      return;
    } 
    bThread.bladeCommu = bladeCommu;
    bThread.setNew(bladeState.isNew());
    bThread.bladeCommu.setKvmInterface(this.kvmInterface);
    bThread.bladeCommu.setClient(this.kvmInterface.client);
    bThread.bladeCommu.setBladeNO(bladeNO);
    bThread.getDrawThread().setBladeCommu(bThread.bladeCommu);
    imagePane.setNew(bladeState.isNew());
    bThread.getDrawThread().setNew(bladeState.isNew());
    bThread.bladeCommu.sentData(this.kvmInterface.packData.connectBlade(bladeNO, imagePane.custBit));
    bThread.bladeCommu.sentData((this.kvmInterface.kvmUtil.getImagePane(this.kvmInterface.actionBlade)).pack.mouseModeControl((byte)36, (byte)2, bThread.getBladeNO()));
    if (bThread.getBladeNO() == this.kvmInterface.actionBlade) {
      this.kvmInterface.kvmUtil.setDrawDisplay(false);
      (bThread.getDrawThread()).isDisplay = true;
      bThread.bladeCommu.sentData(this.kvmInterface.packData.contrRate(35, bThread.getBladeNO()));
    } 
    if (bThread == null) {
      return;
    }
    if (bladeState.isNew()) {
      if (!this.kvmInterface.base.isMstsc)
      {
        if (this.kvmInterface.isFullScreen) {
          this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.myCursor);
          imagePane.setCursor(this.kvmInterface.base.myCursor);
          this.kvmInterface.fullScreen.toolBar.mouseSynButton.setEnabled(false);
        }
        else if (null != this.kvmInterface.toolbar.mouseSynButton) {
          this.kvmInterface.toolbar.mouseSynButton.setEnabled(false);
        } 
        if (this.kvmInterface.base.isDiv)
        {
          this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.defCursor);
          imagePane.setCursor(this.kvmInterface.base.defCursor);
        }
      }
      else if (this.kvmInterface.isFullScreen)
      {
        this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.defCursor);
        imagePane.setCursor(this.kvmInterface.base.defCursor);
        if (null != this.kvmInterface.fullScreen.toolBar.mouseSynButton)
        {
          this.kvmInterface.fullScreen.toolBar.mouseSynButton.setEnabled(true);
        }
      }
      else if (null != this.kvmInterface.toolbar.mouseSynButton)
      {
        this.kvmInterface.toolbar.mouseSynButton.setEnabled(true);
      }
    }
    else if (this.kvmInterface.isFullScreen) {
      this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.defCursor);
      imagePane.setCursor(this.kvmInterface.base.defCursor);
      if (null != this.kvmInterface.fullScreen.toolBar.mouseSynButton)
      {
        this.kvmInterface.fullScreen.toolBar.mouseSynButton.setEnabled(true);
      }
    }
    else if (null != this.kvmInterface.toolbar.mouseSynButton) {
      this.kvmInterface.toolbar.mouseSynButton.setEnabled(true);
    } 
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public void setUnPackData(UnPackData unPack) {
    this.unPack = unPack;
  }
  class DisposeTask
    extends Thread {
    JDialog jd = null;
    public DisposeTask(JDialog jdt) {
      this.jd = jdt;
    }
    public void run() {
      try {
        sleep(5000L);
      }
      catch (InterruptedException e) {
        Debug.println("DisposeTask sleep error.");
        e.printStackTrace();
      } 
      this.jd.dispose();
      this.jd = null;
    }
  }
}
