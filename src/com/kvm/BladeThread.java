package com.kvm;
import com.library.LibException;
import com.library.LoggerUtil;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
public class BladeThread
  extends Thread
{
  private static final int KVM_COMMAND = 0;
  private static final int BLADE_POSITI = 1;
  private static final int KEYSTATE_POSITI = 2;
  private static final int IMAGEDATA_POSITI = 2;
  private static final int FILTER_SIZE = 20;
  private static final int RECONN_KEY_LEN = 128;
  private static final int NEGO_DATA_PWD_LEN = 32;
  private static final int NEGO_DATA_SALT_LEN = 16;
  private boolean isConn = true;
  private int bladeNO = 0;
  private boolean isNew = true;
  private ArrayList<String> compareBuf = new ArrayList<>(10);
  private DrawThread drawThread = null;
  private BladeCommu bladeCommu = null;
  public BladeCommu getBladeCommu() {
    return this.bladeCommu;
  }
  public void setBladeCommu(BladeCommu bladeCommu) {
    this.bladeCommu = bladeCommu;
  }
  private KVMInterface kvmInterface = null;
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  private UnPackData unPack = null;
  public UnPackData getUnPack() {
    return this.unPack;
  }
  public void setUnPack(UnPackData unPack) {
    this.unPack = unPack;
  }
  private Hashtable<Object, Byte> keyState = new Hashtable<>(10);
  public Hashtable<Object, Byte> getKeyState() {
    Hashtable<Object, Byte> tmp = this.keyState;
    return tmp;
  }
  public void setKeyState(Hashtable<Object, Byte> keyState) {
    if (null != keyState) {
      this.keyState = (Hashtable<Object, Byte>)keyState.clone();
    }
    else {
      this.keyState = null;
    } 
  }
  private KVMUtil kvmUtil = null;
  public KVMUtil getKvmUtil() {
    return this.kvmUtil;
  }
  private BladeHeartTimer bladeHeartTimer = null;
  public BladeHeartTimer getBladeHeartTimer() {
    return this.bladeHeartTimer;
  }
  public void setBladeHeartTimer(BladeHeartTimer bladeHeartTimer) {
    this.bladeHeartTimer = bladeHeartTimer;
  }
  private byte[] keyEnData = new byte[16];
  public void setKeyEnData(byte[] keyEnData) {
    if (keyEnData == null) {
      this.keyEnData = null;
    }
    else {
      this.keyEnData = (byte[])keyEnData.clone();
    } 
  }
  public byte[] getKeyEnData() {
    if (null != this.keyEnData)
    {
      return (byte[])this.keyEnData.clone();
    }
    byte[] tmp = null;
    return tmp;
  }
  public void setKvmUtil(KVMUtil kvmUtil) {
    this.kvmUtil = kvmUtil;
  }
  public BladeThread(String ip, int port, int bladeNO, boolean isNew) throws LibException {
    this.bladeCommu = new BladeCommu(ip, port);
    this.isNew = isNew;
    this.bladeNO = bladeNO;
  }
  public void run() {
    ImagePane imagePane = this.kvmUtil.getImagePane(this.bladeNO);
    byte[] bytes = null;
    byte[] unPackData = null;
    boolean flag = false;
    int size = 0;
    try {
      while (this.isConn) {
        size = this.drawThread.getlList().size();
        while (size > 2000) {
          Thread.sleep(10L);
          size = this.drawThread.getlList().size();
        } 
        bytes = this.bladeCommu.getData();
        if (bytes == null) {
          if (this.bladeCommu.isAutoFlag()) {
            continue;
          }
          break;
        } 
        imagePane.setImageReceive(imagePane.getImageReceive() + 1);
        if (!this.drawThread.isDisplay()) {
          continue;
        }
        this.kvmUtil.setStart(0);
        flag = this.kvmUtil.diviStreamNew(bytes, this.isNew);
        while (flag)
        {
          this.unPack.setkvmType(this.kvmUtil.getResult());
          unPackData = null;
          unPackData = this.unPack.getData();
          if (unPackData == null) {
            continue;
          }
          switch (unPackData[0]) {
            case 67:
              distributeConsultation(unPackData);
              break;
            case 2:
              if (!this.isNew) {
                if (fileImageData(unPackData))
                {
                  distributeImageData(unPackData);
                }
                break;
              } 
              distributeImageData(unPackData);
              break;
            case 4:
              distributeKeyStateData(unPackData);
              break;
            case 8:
              distributeConnectStateData(unPackData);
              break;
            case 37:
              distributeMouseModeData(unPackData);
              break;
            case 40:
              distributeDQTModeData(unPackData);
              break;
            case 64:
              distributeKVMSetKey(unPackData);
              this.bladeCommu.setAuthStatus(true);
              break;
            case 50:
              distributeNegotiVMMCodeKey(unPackData);
              break;
            case 54:
              distributeVMMPort(unPackData);
              break;
            case 81:
              distributeNoVMMPri(unPackData);
              show_message(unPackData);
              break;
            default:
              Debug.println("error data"); break;
          } 
          flag = this.kvmUtil.diviStreamNew(bytes, this.isNew);
        }
      } 
    } catch (InterruptedException e) {
      LoggerUtil.error(e.getClass().getName());
    } 
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
    if (judge) {
      if (this.compareBuf.size() == 20)
      {
        this.compareBuf.remove(0);
      }
      this.compareBuf.add(temBuffer.toString());
    } 
    return judge;
  }
  public void distributeImageData(byte[] unPackData) {
    if (this.drawThread != null && this.drawThread.getImagePane() != null)
    {
      this.drawThread.getlList().add(unPackData);
    }
  }
  public void distributeKeyStateData(byte[] unPackData) {
    ImagePane imagePane = this.kvmUtil.getImagePane(this.bladeNO);
    if (imagePane != null) {
      this.keyState.clear();
      this.keyState.put(this.bladeNO + "", Byte.valueOf(unPackData[2]));
      if (this.bladeNO == this.kvmInterface.getActionBlade() || (this.kvmInterface
        .getFullScreen() != null && this.bladeNO == this.kvmInterface.getFullScreen().getActionBlade()))
      {
        this.kvmUtil.setNumAndCapLock();
      }
    } 
  }
  public void distributeConsultation(byte[] unPackData) {
    int k = 0;
    int count = unPackData[2];
    int iterations = 0;
    String[] version = System.getProperty("java.version").split("\\.");
    boolean isSupportSha256 = (Integer.parseInt(version[1]) >= 8);
    if (count * 5 + 3 != unPackData.length) {
      Debug.println("consultation failed.");
      return;
    } 
    while (k < count) {
      if (unPackData[k * 5 + 3] == 2 && !isSupportSha256) {
        iterations = unPackData[k * 5 + 4] << 24 | unPackData[k * 5 + 5] << 16 | unPackData[k * 5 + 6] << 8 | unPackData[k * 5 + 7];
        this.kvmInterface.setIterations(iterations);
        this.kvmInterface.setHmac("PBKDF2WithHmacSHA1");
        this.bladeCommu.sentData(this.kvmInterface.getPackData().setSuitePack(this.bladeNO, iterations, 2));
        break;
      } 
      if (unPackData[k * 5 + 3] == 3 && isSupportSha256) {
        iterations = unPackData[k * 5 + 4] << 24 | unPackData[k * 5 + 5] << 16 | unPackData[k * 5 + 6] << 8 | unPackData[k * 5 + 7];
        this.kvmInterface.setIterations(iterations);
        this.kvmInterface.setHmac("PBKDF2WithHmacSHA256");
        this.bladeCommu.sentData(this.kvmInterface.getPackData().setSuitePack(this.bladeNO, iterations, 3));
        break;
      } 
      k++;
    } 
    if (k == count)
    {
      this.bladeCommu.sentData(this.kvmInterface.getPackData().setSuitePack(this.bladeNO, 5000, 1));
    }
    if (0 != Base.getCompress()) {
      try {
        this.kvmInterface.setEncodeKey(AESHandler.getcodekey(this.kvmInterface.getVerifyValueExt(), 24, Base.getUser_iv(), this.kvmInterface.getHmac(), this.kvmInterface.getIterations()));
      }
      catch (NoSuchAlgorithmException e) {
        LoggerUtil.error(e.getClass().getName());
      }
      catch (InvalidKeySpecException e) {
        LoggerUtil.error(e.getClass().getName());
      } 
      int j = 0;
      byte[] tmp = new byte[1];
      for (int i = 0; i < 6; i++) {
        j = i * 4;
        tmp[0] = this.kvmInterface.getEncodeKey()[j];
        byte[] encodeKey = this.kvmInterface.getEncodeKey();
        encodeKey[j] = encodeKey[j + 3];
        encodeKey[j + 3] = tmp[0];
        tmp[0] = encodeKey[j + 1];
        encodeKey[j + 1] = encodeKey[j + 2];
        encodeKey[j + 2] = tmp[0];
        this.kvmInterface.setEncodeKey(encodeKey);
      } 
    } 
  }
  public void distributeConnectStateData(byte[] unPackData) {
    int conState = unPackData[2];
    switch (conState) {
      case 2:
        this.drawThread.getImagePane().quitConn("over_userconnect");
        break;
      case 3:
        if (this.kvmInterface.getReconnectState()) {
          this.kvmInterface.setReconnectUserErrorTimes(this.kvmInterface.getReconnectUserErrorTimes() + 1);
          if (this.kvmInterface.getReconnectUserErrorTimes() > 10)
          {
            this.drawThread.getImagePane().quitConn("No_bladeData");
          }
          break;
        } 
        this.drawThread.getImagePane().quitConn("str_errorname");
        break;
      case 4:
        this.drawThread.getImagePane().quitConn("SIGNAL_OUT_RANGE");
        break;
      case 5:
        this.drawThread.getImagePane().quitConn("Server_Disabled");
        break;
      case 7:
        this.drawThread.getImagePane().quitConn("User_Delect");
        break;
      case 17:
        this.drawThread.getImagePane().quitConn("session_timeout");
        break;
    } 
  }
  public void distributeMouseModeData(byte[] unPackData) {
    int conState = 0;
    conState = unPackData[2];
    switch (conState) {
      case 0:
        Base.setIsSynMouse(false);
        if (null != this.drawThread.getImagePane() && null != this.drawThread.getImagePane().getMouseTimerTask() && 
          !this.drawThread.getImagePane().getMouseTimerTask().isAlive()) {
          this.drawThread.getImagePane().setMouseTimerTask(new MouseTimerTask(this.drawThread.getImagePane()));
          KVMUtil.startMouseList(this.drawThread.getImagePane());
        } 
        if (null != this.kvmInterface.getFullScreen() && null != this.kvmInterface.getFullScreen().getToolBar() && null != this.kvmInterface
          .getFullScreen().getToolBar().getMouseModeButton())
        {
          this.kvmInterface.getFullScreen().getToolBar().getMouseModeButton().setEnabled(true);
        }
        if (null != this.kvmInterface.getFloatToolbar().getPowerMenu() && this.kvmInterface
          .getFloatToolbar().getPowerMenu().getMouseModeSwitchMenu().isSelected())
        {
          this.kvmInterface.getFloatToolbar().getPowerMenu().getMouseModeSwitchMenu().setSelected(false);
        }
        if (null != this.kvmInterface.getFloatToolbar().getMouseMenu() && 
          (this.kvmInterface.getFloatToolbar().getMouseMenu()).mouseModeSwitchMenu.isSelected())
        {
          (this.kvmInterface.getFloatToolbar().getMouseMenu()).mouseModeSwitchMenu.setSelected(false);
        }
        break;
      case 1:
        Base.setIsSynMouse(true);
        Base.setSingleMouse(false);
        if (null != this.kvmInterface.getFloatToolbar().getPowerMenu())
        {
          this.kvmInterface.getFloatToolbar().getPowerMenu().getSingleMouseMenu().setSelected(false);
        }
        if (null != this.kvmInterface.getFloatToolbar().getMouseMenu())
        {
          (this.kvmInterface.getFloatToolbar().getMouseMenu()).singleMouseMenu.setSelected(false);
        }
        if (null != this.kvmInterface.getFullScreen() && null != this.kvmInterface.getFullScreen().getToolBar() && null != this.kvmInterface
          .getFullScreen().getToolBar().getMouseModeButton()) {
          this.kvmInterface.getFullScreen().getToolBar().getMouseSynButton().setEnabled(false);
          this.kvmInterface.getFullScreen().getToolBar().getMouseModeButton().setEnabled(false);
          this.kvmInterface.getBase().setMstsc(false);
        } 
        if (null != this.kvmInterface.getFloatToolbar().getPowerMenu() && 
          !this.kvmInterface.getFloatToolbar().getPowerMenu().getMouseModeSwitchMenu().isSelected())
        {
          this.kvmInterface.getFloatToolbar().getPowerMenu().getMouseModeSwitchMenu().setSelected(true);
        }
        if (null != this.kvmInterface.getFloatToolbar().getMouseMenu() && 
          !(this.kvmInterface.getFloatToolbar().getMouseMenu()).mouseModeSwitchMenu.isSelected())
        {
          (this.kvmInterface.getFloatToolbar().getMouseMenu()).mouseModeSwitchMenu.setSelected(true);
        }
        break;
    } 
  }
  public void distributeDQTModeData(byte[] unPackData) {
    int conState = 0;
    conState = unPackData[2];
    Base.setDqtzSize(conState / 10 - 1);
    int dqtValue = (Base.getDqtzSize() + 1) * 10;
    if (dqtValue >= 70)
    {
      dqtValue -= 10;
    }
    this.kvmInterface.getFloatToolbar().getDqtSlider().setValue(dqtValue);
  }
  private static String convert(byte[] data, int len) {
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++)
    {
      sb.append((char)data[i]);
    }
    return sb.toString();
  }
  private void distributeKVMSetKey(byte[] unPackData) {
    boolean cdstate = false;
    boolean floppystate = false;
    byte[] tmp = new byte[1];
    byte[] salt = new byte[16];
    int j = 0;
    int data_len = unPackData.length - 2;
    if (data_len == 48) {
      byte[] encryted_data = new byte[data_len];
      System.arraycopy(unPackData, 2, encryted_data, 0, data_len);
      byte[] decryted_data = AESHandler.aes_cbc_128_decrypt(encryted_data, Base.getUser_key(), Base.getUser_iv());
      if (decryted_data != null && decryted_data.length == 48)
      {
        String de_str = convert(decryted_data, 32);
        System.arraycopy(decryted_data, 32, salt, 0, 16);
        try {
          Base.setKvm_key(AESHandler.getcodekey(de_str, 48, salt, this.kvmInterface.getHmac(), this.kvmInterface.getIterations()));
        }
        catch (NoSuchAlgorithmException e) {
          LoggerUtil.error(e.getClass().getName());
        }
        catch (InvalidKeySpecException e) {
          LoggerUtil.error(e.getClass().getName());
        } 
        byte[] kvm_key = Base.getKvm_key();
        if (null != kvm_key)
        {
          for (int i = 0; i < 12; i++) {
            j = i * 4;
            tmp[0] = kvm_key[j];
            kvm_key[j] = kvm_key[j + 3];
            kvm_key[j + 3] = tmp[0];
            tmp[0] = kvm_key[j + 1];
            kvm_key[j + 1] = kvm_key[j + 2];
            kvm_key[j + 2] = tmp[0];
          } 
          Base.setKvm_key(kvm_key);
        }
      }
    } else if (128 == data_len) {
      byte[] encryted_data = new byte[data_len];
      System.arraycopy(unPackData, 2, encryted_data, 0, data_len);
      byte[] decryted_data = AESHandler.aes_cbc_128_decrypt(encryted_data, Base.getUser_key(), Base.getUser_iv());
      if (decryted_data != null && decryted_data.length == 128) {
        this.kvmInterface.setReconnectUserErrorTimes(0);
        this.kvmInterface.setReconnKey(decryted_data);
        cdstate = this.kvmInterface.getFloatToolbar().getVirtualMedia().getVmApplet().getConsole().getCdromReconnState();
        floppystate = this.kvmInterface.getFloatToolbar().getVirtualMedia().getVmApplet().getConsole().getFloppyReconnState();
        if (floppystate && cdstate) {
          TimerTask task = new VirtualMediaLink(this.kvmInterface.getFloatToolbar().getVirtualMedia().getCdCon(), this.kvmInterface.getFloatToolbar().getVirtualMedia().getFcb(), this.kvmInterface.getFloatToolbar().getVirtualMedia().getVmApplet().getConsole());
          (new Timer("cdrom Link")).schedule(task, 1000L);
        }
        else if (cdstate) {
          TimerTask task = new CdromLink(this.kvmInterface.getFloatToolbar().getVirtualMedia().getCdCon());
          (new Timer("cdrom Link")).schedule(task, 1000L);
        }
        else if (floppystate) {
          TimerTask task = new FloppyLink(this.kvmInterface.getFloatToolbar().getVirtualMedia().getFcb());
          (new Timer("floppy Link")).schedule(task, 1000L);
        } 
      } 
    } 
  }
  public void show_message(byte[] unPackData) {
    Showthread mythread = new Showthread(this);
    mythread.showthread(unPackData[2]);
    mythread.start();
  }
  private void distributeNegotiVMMCodeKey(byte[] unPackData) {
    if (unPackData == null) {
      return;
    }
    int data_len = unPackData.length - 2;
    byte[] nego_data = new byte[data_len];
    byte[] conde_data = null;
    byte[] code_key_data = new byte[20];
    byte[] salt = new byte[16];
    System.arraycopy(unPackData, 2, nego_data, 0, data_len);
    if (Base.getCompress() == 1) {
      conde_data = AESHandler.decry(nego_data, Base.getKvm_key(), data_len);
      if (conde_data == null || conde_data.length < code_key_data.length + salt.length) {
        return;
      }
      System.arraycopy(conde_data, 0, code_key_data, 0, code_key_data.length);
      System.arraycopy(conde_data, code_key_data.length, salt, 0, salt.length);
    }
    else {
      System.arraycopy(nego_data, 0, code_key_data, 0, code_key_data.length);
      System.arraycopy(nego_data, code_key_data.length, salt, 0, salt.length);
    } 
    this.kvmInterface.getKvmUtil().setVMMSecretCodeKey(this.bladeNO, code_key_data, salt);
  }
  private void distributeVMMPort(byte[] unPackData) {
    if (unPackData == null) {
      return;
    }
    int data_len = unPackData.length - 2;
    byte[] port_data = new byte[data_len];
    byte[] conde_data = null;
    byte[] port = new byte[2];
    System.arraycopy(unPackData, 2, port_data, 0, data_len);
    if (1 == Base.getCompress()) {
      conde_data = AESHandler.decry(port_data, Base.getKvm_key(), data_len);
      System.arraycopy(conde_data, 0, port, 0, port.length);
    }
    else {
      System.arraycopy(port_data, 0, port, 0, port.length);
    } 
    this.kvmInterface.getKvmUtil().setVMMPort(this.bladeNO, port);
  }
  private void distributeNoVMMPri(byte[] unPackData) {
    if (unPackData == null) {
      return;
    }
    int state = unPackData[2];
    if (state == 2 || state == 3)
    {
      this.kvmInterface.getKvmUtil().setVMMPri(this.bladeNO, false);
    }
  }
  public void setDrawThread(DrawThread drawThread) {
    this.drawThread = drawThread;
  }
  public DrawThread getDrawThread() {
    return this.drawThread;
  }
  public void setKvmInterface(KVMInterface kvmInterface2) {
    this.kvmInterface = kvmInterface2;
  }
  public void setUnPackData(UnPackData unPack) {
    this.unPack = unPack;
  }
  public void setBladeNumb(int bladeNumber) {
    this.bladeNO = bladeNumber;
  }
  public void setConn(boolean isConn) {
    this.isConn = isConn;
  }
  public boolean isNew() {
    return this.isNew;
  }
  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }
  public int getBladeNOByBladeThread() {
    return this.bladeNO;
  }
}
