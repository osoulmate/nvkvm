package com.kvm;
import com.library.LibException;
import com.library.LoggerUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
public class ClientSocketCommunity
  extends Thread
{
  private boolean conn = false;
  private static final int KVM_COMMAND = 0;
  private static final int BLADE_POSITI = 1;
  private static final int IMAGEDATA_POSITI = 2;
  private ArrayList<String> compareBuf = new ArrayList<>(10);
  private static final int FILTER_SIZE = 20;
  private HashMap<String, Integer> bladeMap = new HashMap<>(10);
  public HashMap<String, Integer> getBladeMap() {
    return this.bladeMap;
  }
  public void setBladeMap(HashMap<String, Integer> bladeMap) {
    this.bladeMap = bladeMap;
  }
  private Hashtable<String, Byte> keyState = new Hashtable<>(10);
  public Hashtable<String, Byte> getKeyState() {
    Hashtable<String, Byte> tmp = this.keyState;
    return tmp;
  }
  public void setKeyState(Hashtable<String, Byte> keyState) {
    if (null != keyState) {
      this.keyState = (Hashtable<String, Byte>)keyState.clone();
    }
    else {
      this.keyState = null;
    } 
  }
  private Hashtable<String, byte[]> bladePresentInfo = (Hashtable)new Hashtable<>(10);
  public Hashtable<String, byte[]> getBladePresentInfo() {
    Hashtable<String, byte[]> tmp = this.bladePresentInfo;
    return tmp;
  }
  public void setBladePresentInfo(Hashtable<String, byte[]> bladePresentInfo) {
    if (null != bladePresentInfo) {
      this.bladePresentInfo = (Hashtable<String, byte[]>)bladePresentInfo.clone();
    }
    else {
      this.bladePresentInfo = null;
    } 
  }
  private Hashtable<Object, byte[]> bladeStateInfo = (Hashtable)new Hashtable<>(10);
  public Hashtable<Object, byte[]> getBladeStateInfo() {
    Hashtable<Object, byte[]> tmp = this.bladeStateInfo;
    return tmp;
  }
  public void setBladeStateInfo(Hashtable<Object, byte[]> bladeStateInfo) {
    if (null != bladeStateInfo) {
      this.bladeStateInfo = (Hashtable<Object, byte[]>)bladeStateInfo.clone();
    }
    else {
      this.bladeStateInfo = null;
    } 
  }
  private static String bladePreIndex = "0";
  public String getBladePreIndex() {
    return bladePreIndex;
  }
  public static void setBladePreIndex(String bladePreIndex) {
    ClientSocketCommunity.bladePreIndex = bladePreIndex;
  }
  private KVMInterface kvmInterface = null;
  private UnPackData unPack = null;
  public UnPackData getUnPack() {
    return this.unPack;
  }
  public void setUnPack(UnPackData unPack) {
    this.unPack = unPack;
  }
  public void setConn(boolean b) {
    this.conn = b;
  }
  public void run() {
    LoggerUtil.info( "super.start():run ");
    byte[] bytes = null;
    boolean check_style_flag210 = false;
    byte[] unPackData = null;
    while (this.conn) {
      try {
    	
        bytes = this.kvmInterface.getClient().getSmmData();
        String mystr = new String(bytes,"UTF-8");
        LoggerUtil.info( "mystr:" + mystr);
      }
      catch (Exception e) {
        break;
      } 
      if (bytes == null) {
        if (this.kvmInterface.getBase().getGetWaitTime() == -1L) {
          this.kvmInterface.getBase().setGetWaitTime(0L);
          break;
        } 
        LoggerUtil.info( "mystr continue:" + this.kvmInterface.getBase().getGetWaitTime());
        continue;
      } 
      this.kvmInterface.getKvmUtil().setStart(0);
      check_style_flag210 = this.kvmInterface.getKvmUtil().diviStreamNew(bytes, false);
      LoggerUtil.info( "check_style_flag210" + check_style_flag210);
      while (check_style_flag210) {
        this.unPack.setkvmType(this.kvmInterface.getKvmUtil().getResult());
        unPackData = null;
        unPackData = this.unPack.getData();
        LoggerUtil.info( "unPackData" + unPackData);
        if (unPackData == null) {
          continue;
        }
        switch (unPackData[0]) {
          case 1:
            distributeBladePresentData(unPackData);
            break;
          case 21:
            reportBladeState(unPackData);
            break;
          case 29:
            switchChannel(unPackData);
            break;
          case 33:
            rapCloseBlade(unPackData);
            break;
        } 
        check_style_flag210 = this.kvmInterface.getKvmUtil().diviStreamNew(bytes, false);
      } 
    } 
  }
  private void rapCloseBlade(byte[] unPackData) {
    byte bNO = unPackData[1];
    this.kvmInterface.getTabbedpane().getModel().removeChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
    if (this.kvmInterface.isFullScreen())
    {
      this.kvmInterface.getKvmUtil().returnToWin();
    }
    BladeThread bladeThread = (BladeThread)this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(bNO));
    bladeThread.getBladeCommu().setAutoFlag(false);
    this.kvmInterface.getKvmUtil().disconnectBlade(bNO);
    this.kvmInterface.getTabbedpane().getModel().addChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
    JOptionPane jOptionPane = new JOptionPane(this.kvmInterface.getKvmUtil().getString("connection.fail.rap") + bNO);
    JDialog jd = jOptionPane.createDialog(this.kvmInterface.getToolbar(), this.kvmInterface.getKvmUtil()
        .getString("connection.message"));
    DisposeTask dt = new DisposeTask(jd);
    dt.start();
    jd.setVisible(true);
    Debug.println("exit rapCloseBlade");
  }
  public boolean fileImageData(byte[] unPackData) {
    StringBuffer temBuffer = new StringBuffer();
    temBuffer.append(unPackData[1]);
    temBuffer.append(unPackData[2]);
    int IMAGEDATA_POSITI1 = 3;
    int IMAGEDATA_POSITI2 = 4;
    boolean judge = true;
    try {
      temBuffer.append(unPackData[IMAGEDATA_POSITI1]);
      temBuffer.append(unPackData[IMAGEDATA_POSITI2]);
    }
    catch (ArrayIndexOutOfBoundsException e) {
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
  public void distributeBladePresentData(byte[] unPackData) {
    byte[] bladePreInfo = new byte[2];
    bladePreInfo[0] = unPackData[2];
    bladePreInfo[1] = unPackData[1];
    if (this.kvmInterface.getBladeFlag() != null) {
      int tem = Integer.parseInt(this.kvmInterface.getBladeFlag() + '1', 2);
      bladePreInfo[0] = (byte)(bladePreInfo[0] & tem & 0xFF);
      bladePreInfo[1] = (byte)(bladePreInfo[1] & tem >>> 8 & 0xFF);
    } 
    this.bladePresentInfo.put(bladePreIndex, bladePreInfo);
  }
  public void reportBladeState(byte[] unPackData) {
    byte bNO = unPackData[1];
    Object lock = this.kvmInterface.getBase().getLock(bNO);
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
        if (b < 0)
        {
          unPackData[1] = 100;
        }
        byte[] tempStateInfo = new byte[unPackData.length - 1];
        System.arraycopy(unPackData, 1, tempStateInfo, 0, tempStateInfo.length);
        byte bladeNO = unPackData[1];
        ImagePane imagePane = this.kvmInterface.getKvmUtil().getImagePane(bladeNO);
        BladeThread bThread = (BladeThread)this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(bladeNO));
        if (bThread == null || imagePane == null) {
          return;
        }
        BladeState bladeState = bThread.getKvmUtil().getBladeStateAuto(bladeNO, "", tempStateInfo);
        if (bThread.getBladeCommu().isAutoFlag()) {
          bThread.getBladeCommu().setRequest(false);
          if (bladeState.isEnable() && this.kvmInterface
            .getBase().getThreadGroup().get(String.valueOf(bladeNO)) != null) {
            reConnBlade(bladeState, bladeNO, bThread, imagePane);
          }
          else {
            if (!bThread.getBladeCommu().getSocket().isClosed() || this.kvmInterface
              .getBase().getThreadGroup().get(String.valueOf(bladeNO)) == null) {
              return;
            }
            try {
              try {
                int i = 0;
                while (i < 1)
                {
                  lock.wait(1000L);
                  i++;
                }
              } catch (InterruptedException e) {
                Debug.printExc(e.getClass().getName());
              } 
              this.kvmInterface.getClient().sentData(this.kvmInterface.getPackData().reqBladeState(bladeNO, 
                    Base.getConnMode()));
              (this.kvmInterface.getClientSocket()).bladeMap.put(String.valueOf(bladeNO), 
                  Integer.valueOf(bladeNO));
            }
            catch (LibException ex) {
              Debug.printExc(ex.getClass().getName());
            } 
          } 
          return;
        } 
        BladeThread bladeThread = null;
        if (bladeState.isEnable()) {
          try {
            bladeThread = new BladeThread(bladeState.getBladeIP(), bladeState.getBladePort(), bladeNO, true);
            bladeThread.getBladeCommu().sentData(this.kvmInterface.getKvmUtil()
                .getImagePane(this.kvmInterface.getActionBlade())
                .getPack()
                .mouseModeControl((byte)36, (byte)2, bladeThread.getBladeNOByBladeThread()));
            bladeThread.setName("BladeThread" + bladeNO);
            bladeThread.setDrawThread(bThread.getDrawThread());
            bladeThread.setKvmUtil(bThread.getKvmUtil());
            bladeThread.setKvmInterface(bThread.getKvmInterface());
            bladeThread.setUnPackData(bThread.getUnPack());
            this.kvmInterface.getBase().getThreadGroup().remove(String.valueOf(bladeNO));
            this.kvmInterface.getBase().getThreadGroup().put(String.valueOf(bladeNO), bladeThread);
          }
          catch (LibException e) {
            imagePane.addKeyListener(imagePane.getKeyListener());
            imagePane.addMouseMotionListener(imagePane);
            imagePane.addMouseListener(imagePane);
            this.kvmInterface.getTabbedpane()
              .getModel()
              .removeChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
            if (this.kvmInterface.isFullScreen())
            {
              this.kvmInterface.getKvmUtil().returnToWin();
            }
            this.kvmInterface.getKvmUtil().disconnectBlade(bladeNO);
            this.kvmInterface.getTabbedpane()
              .getModel()
              .addChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
            if (this.kvmInterface.getBladeSize() == 1) {
              JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface.getKvmUtil()
                  .getString("Blade_disconnect_message"));
            }
            else {
              JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), "Blade" + bladeNO + ' ' + this.kvmInterface
                  .getKvmUtil().getString("Blade_disconnect_message"));
            } 
            return;
          } 
          bladeThread.getBladeCommu().setKvmInterface(this.kvmInterface);
          bladeThread.getBladeCommu().setClient(this.kvmInterface.getClient());
          bladeThread.getBladeCommu().setBladeNumber(bladeNO);
          imagePane.setNew(bladeState.isNew());
          imagePane.setBladeThread(bladeThread);
          bladeThread.setNew(true);
          if (!this.kvmInterface.getBase().isMstsc())
          {
            if (this.kvmInterface.isFullScreen())
            {
              this.kvmInterface.getFullScreen().setCursor((this.kvmInterface.getBase()).myCursor);
              imagePane.setCursor((this.kvmInterface.getBase()).myCursor);
              if (null != this.kvmInterface.getFullScreen().getToolBar().getMouseSynButton())
              {
                this.kvmInterface.getFullScreen().getToolBar().getMouseSynButton().setEnabled(false);
              }
            }
            else if (null != this.kvmInterface.getToolbar().getMouseSynButton())
            {
              this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(false);
            }
          }
        }
        else {
          imagePane.addKeyListener(imagePane.getKeyListener());
          imagePane.addMouseMotionListener(imagePane);
          imagePane.addMouseListener(imagePane);
          this.kvmInterface.getTabbedpane()
            .getModel()
            .removeChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
          if (this.kvmInterface.isFullScreen())
          {
            this.kvmInterface.getKvmUtil().returnToWin();
          }
          this.kvmInterface.getKvmUtil().disconnectBlade(bladeNO);
          this.kvmInterface.getTabbedpane().getModel().addChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
          if (this.kvmInterface.getBladeSize() == 1) {
            JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface
                .getKvmUtil().getString("Blade_disconnect_message"));
          }
          else {
            JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), "Blade" + bladeNO + ' ' + this.kvmInterface
                .getKvmUtil().getString("Blade_disconnect_message"));
          } 
          return;
        } 
        imagePane.setStatReceiveTask(new StatReceiveTask(imagePane));
        KVMUtil.startReceiveList(imagePane);
        imagePane.setMouseTimerTask(new MouseTimerTask(imagePane));
        KVMUtil.startMouseList(imagePane);
        bladeThread.setBladeHeartTimer(new BladeHeartTimer(bladeThread));
        bladeThread.getBladeHeartTimer().setName("BladeHeart" + bladeNO);
        bladeThread.getBladeHeartTimer().start();
        if (bladeThread.getKvmUtil() != null) {
          bladeThread.getKvmUtil().getResultDivi().clear();
          bladeThread.getKvmUtil().getDiviBuff().clear();
          bladeThread.getKvmUtil().resetBuf();
          bladeThread.getKvmUtil().setFirstJudge(true);
        } 
        bladeThread.getDrawThread().setBladeCommu(bladeThread.getBladeCommu());
        bladeThread.getDrawThread().setNew(bladeThread.isNew());
        bladeThread.getDrawThread().getlList().clear();
        bladeThread.getDrawThread().getComImage().clear();
        imagePane.addKeyListener(imagePane.getKeyListener());
        imagePane.addMouseMotionListener(imagePane);
        imagePane.addMouseListener(imagePane);
        bladeThread.start();
      } 
    } 
  }
  public void switchChannel(byte[] unPackData) {
    byte bladeNO = unPackData[1];
    boolean disFlag = false;
    if (this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(bladeNO)) != null) {
      disFlag = true;
    }
    else if (this.kvmInterface.isClickFlag()) {
      try {
        Thread.sleep(5000L);
      }
      catch (InterruptedException e) {
        LoggerUtil.error(e.getClass().getName());
      } 
      if (this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(bladeNO)) != null)
      {
        disFlag = true;
      }
      this.kvmInterface.setClickFlag(false);
    } 
    if (disFlag) {
      BladeThread bladeThread = (BladeThread)this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(bladeNO));
      bladeThread.getBladeCommu().sentData(this.kvmInterface.getPackData().interruptTempBlade(bladeNO));
      bladeThread.getBladeCommu().setAutoFlag(false);
      ImagePane imagePane = this.kvmInterface.getKvmUtil().getImagePane(bladeNO);
      imagePane.getMouseTimerTask().interrupt();
      imagePane.setMouseTimerTask((MouseTimerTask)null);
      imagePane.getReceiveList().cancel();
      imagePane.setStatReceiveTask((StatReceiveTask)null);
      bladeThread.getBladeHeartTimer().interrupt();
      imagePane.removeKeyListener(imagePane.getKeyListener());
      imagePane.removeMouseMotionListener(imagePane);
      imagePane.removeMouseListener(imagePane);
      bladeThread.setConn(false);
      try {
        bladeThread.getBladeCommu().getSocket().close();
      }
      catch (IOException e) {
        Debug.printExc(e.getClass().getName());
      } finally {
        try {
          bladeThread.getBladeCommu().getSocket().close();
        }
        catch (IOException e) {
          bladeThread.interrupt();
          Debug.printExc(e.getClass().getName());
        } 
      } 
      try {
        Thread.sleep(2000L);
      }
      catch (InterruptedException e1) {
        LoggerUtil.error(e1.getClass().getName());
      } 
      try {
        this.kvmInterface.getClient()
          .sentData(this.kvmInterface.getPackData().reqBladeState(bladeNO, Base.getConnMode()));
        this.bladeMap.put(String.valueOf(bladeNO), Integer.valueOf(bladeNO));
      }
      catch (LibException e) {
        if ("IO_ERRCODE".equals(e.getErrCode()))
        {
          JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface
              .getKvmUtil().getString("Network_interrupt_message"));
        }
      } 
    } 
  }
  private void reConnBlade(BladeState bladeState, int bladeNO, BladeThread bThread, ImagePane imagePane) {
    BladeCommu bladeCommu = null;
    try {
      bladeCommu = new BladeCommu(bladeState.getBladeIP(), bladeState.getBladePort());
    }
    catch (LibException e) {
      try {
        this.kvmInterface.getClient()
          .sentData(this.kvmInterface.getPackData().reqBladeState(bladeNO, Base.getConnMode()));
      }
      catch (LibException e1) {
        LoggerUtil.error(e1.getClass().getName());
      } 
      (this.kvmInterface.getClientSocket()).bladeMap.put(String.valueOf(bladeNO), Integer.valueOf(bladeNO));
      return;
    } 
    bThread.setBladeCommu(bladeCommu);
    bThread.setNew(bladeState.isNew());
    bThread.getBladeCommu().setKvmInterface(this.kvmInterface);
    bThread.getBladeCommu().setClient(this.kvmInterface.getClient());
    bThread.getBladeCommu().setBladeNumber(bladeNO);
    bThread.getDrawThread().setBladeCommu(bThread.getBladeCommu());
    imagePane.setNew(bladeState.isNew());
    bThread.getDrawThread().setNew(bladeState.isNew());
    bThread.getBladeCommu().sentData(this.kvmInterface.getKvmUtil()
        .getImagePane(this.kvmInterface.getActionBlade())
        .getPack()
        .mouseModeControl((byte)36, (byte)2, bThread.getBladeNOByBladeThread()));
    if (bThread.getBladeNOByBladeThread() == this.kvmInterface.getActionBlade()) {
      this.kvmInterface.getKvmUtil().setDrawDisplay(false);
      bThread.getDrawThread().setDisplay(true);
      bThread.getBladeCommu().sentData(this.kvmInterface.getPackData().contrRate(35, bThread
            .getBladeNOByBladeThread()));
    } 
    if (bladeState.isNew()) {
      if (!this.kvmInterface.getBase().isMstsc())
      {
        if (this.kvmInterface.isFullScreen())
        {
          this.kvmInterface.getFullScreen().setCursor((this.kvmInterface.getBase()).myCursor);
          imagePane.setCursor((this.kvmInterface.getBase()).myCursor);
          this.kvmInterface.getFullScreen().getToolBar().getMouseSynButton().setEnabled(false);
        }
        else if (null != this.kvmInterface.getToolbar().getMouseSynButton())
        {
          this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(false);
        }
      }
      else if (this.kvmInterface.isFullScreen())
      {
        this.kvmInterface.getFullScreen().setCursor(this.kvmInterface.getBase().getDefCursor());
        imagePane.setCursor(this.kvmInterface.getBase().getDefCursor());
        if (null != this.kvmInterface.getFullScreen().getToolBar().getMouseSynButton())
        {
          this.kvmInterface.getFullScreen().getToolBar().getMouseSynButton().setEnabled(true);
        }
      }
      else if (null != this.kvmInterface.getToolbar().getMouseSynButton())
      {
        this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(true);
      }
    }
    else if (this.kvmInterface.isFullScreen()) {
      this.kvmInterface.getFullScreen().setCursor(this.kvmInterface.getBase().getDefCursor());
      imagePane.setCursor(this.kvmInterface.getBase().getDefCursor());
      if (null != this.kvmInterface.getFullScreen().getToolBar().getMouseSynButton())
      {
        this.kvmInterface.getFullScreen().getToolBar().getMouseSynButton().setEnabled(true);
      }
    }
    else if (null != this.kvmInterface.getToolbar().getMouseSynButton()) {
      this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(true);
    } 
  }
  public void setKvmInterface(KVMInterface kvmInterface2) {
    this.kvmInterface = kvmInterface2;
  }
  public void setUnPackData(UnPackData unPack) {
    this.unPack = unPack;
  }
}
