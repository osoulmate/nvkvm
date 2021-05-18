package com.kvmV1;
import java.util.ArrayList;
import java.util.Hashtable;
public class BladeThread
  extends Thread
{
  private static final int KVM_COMMAND = 0;
  private static final int BLADE_POSITI = 1;
  private static final int KEYSTATE_POSITI = 2;
  private static final int IMAGEDATA_POSITI = 2;
  private static final int FILTER_SIZE = 20;
  private boolean isConn = true;
  private int bladeNO = 0;
  private boolean isNew = true;
  private ArrayList<String> compareBuf = new ArrayList<String>();
  private DrawThread drawThread = null;
  public BladeCommu bladeCommu = null;
  public KVMInterface kvmInterface = null;
  public UnPackData unPack = null;
  public Hashtable<String, Byte> keyState = new Hashtable<String, Byte>();
  public KVMUtil kvmUtil = null;
  private boolean encryted = false;
  public boolean getEncrytedStatus() {
    return this.encryted;
  }
  public void setEncrytedStatus(boolean status) {
    this.encryted = status;
  }
  public boolean negotiatedKeyState = false;
  public void setNegotaitedKeyState(boolean state) {
    this.negotiatedKeyState = state;
  }
  private byte[] bladeKvmkey = null;
  private byte[] bladeKbdkey = null;
  private byte[] bladeVmmkey = null;
  public void initBladeNegotiatedKey(char[] inputPlainKey, byte[] salt) {
    byte[] completeKey = null;
    try {
      completeKey = AESHandler.generateStoredPasswordHash(inputPlainKey, 48, salt);
    }
    catch (Exception ex) {}
    byte[] kvmSecretKey = new byte[16];
    byte[] kbdSecretKey = new byte[16];
    byte[] vmmSecretKey = new byte[16];
    System.arraycopy(completeKey, 0, kvmSecretKey, 0, 16);
    System.arraycopy(completeKey, 16, kbdSecretKey, 0, 16);
    System.arraycopy(completeKey, 32, vmmSecretKey, 0, 16);
    this.bladeKvmkey = new byte[16];
    KVMUtil.perIntToByteCon(this.bladeKvmkey, 0, kvmSecretKey);
    this.bladeKbdkey = new byte[16];
    KVMUtil.perIntToByteCon(this.bladeKbdkey, 0, kbdSecretKey);
    this.bladeVmmkey = new byte[16];
    KVMUtil.perIntToByteCon(this.bladeVmmkey, 0, vmmSecretKey);
  }
  public BladeHeartTimer bladeHeartTimer = null;
  public byte[] keyEnData = new byte[16];
  public byte[] getBladeKvmkey() {
    return this.negotiatedKeyState ? this.bladeKvmkey : Base.getKvmSecretKeyBigEnd();
  }
  public byte[] getBladeKbdkey() {
    return this.negotiatedKeyState ? this.bladeKbdkey : Base.getKbdSecretKeyBigEnd();
  }
  public byte[] getBladeKeyIV() {
    return this.negotiatedKeyState ? this.bladeVmmkey : Base.getVmmSecretKeyBigEnd();
  }
  public void setKvmUtil(KVMUtil kvmUtil) {
    this.kvmUtil = kvmUtil;
  }
  public BladeThread(String ip, int port, int bladeNO, boolean isNew) throws KVMException {
    this.bladeCommu = new BladeCommu(ip, port);
    this.isNew = isNew;
    this.bladeNO = bladeNO;
  }
  public void run() {
    ImagePane imagePane = this.kvmUtil.getImagePane(this.bladeNO);
    byte[] bytes = null;
    while (this.isConn) {
      while (this.drawThread.lList.size() > 2000) {
        try {
          Thread.sleep(10L);
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        } 
      } 
      bytes = this.bladeCommu.getData();
      if (bytes == null) {
        if (this.bladeCommu.isAutoFlag()) {
          continue;
        }
        break;
      } 
      imagePane.imageReceive++;
      if (!this.drawThread.isDisplay) {
        continue;
      }
      this.kvmUtil.start = 0;
      while (this.kvmUtil.diviStreamNew(bytes, this.isNew)) {
        this.unPack.setkvmType(this.kvmUtil.result);
        byte[] unPackData = null;
        unPackData = this.unPack.getData();
        if (unPackData == null) {
          continue;
        }
        switch (unPackData[0]) {
          case 2:
            if (getEncrytedStatus()) {
              int pack_len = this.kvmUtil.dlen;
              int offset = 0xFF00 & unPackData[2] << 8 | 0xFF & unPackData[3];
              if (offset != 0) {
                int encrydata_len = pack_len - 2 - 6;
                int origdata_len = unPackData[5] & 0xFF;
                byte[] encrytedData = new byte[encrydata_len];
                byte[] tempUnPackData = new byte[origdata_len + 5];
                System.arraycopy(unPackData, 0, tempUnPackData, 0, 5);
                System.arraycopy(unPackData, 6, encrytedData, 0, encrydata_len);
                byte[] decryted_data = AESHandler.aes_cbc_128_decrypt(encrytedData, getBladeKvmkey(), getBladeKeyIV());
                System.arraycopy(decryted_data, 0, tempUnPackData, 5, origdata_len);
                unPackData = tempUnPackData;
              } 
            } 
            if (!this.isNew) {
              if (fileImageData(unPackData))
              {
                distributeImageData(unPackData);
              }
              continue;
            } 
            distributeImageData(unPackData);
            continue;
          case 4:
            distributeKeyStateData(unPackData);
            continue;
          case 8:
            distributeConnectStateData(unPackData);
            continue;
          case 37:
            distributeMouseModeData(unPackData);
            continue;
          case 50:
            distributeNegotiVMMCodeKey(unPackData);
            continue;
        } 
        Debug.println("error data");
      } 
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
    if (this.drawThread != null && this.drawThread.imagePane != null)
    {
      this.drawThread.lList.add(unPackData);
    }
  }
  public void distributeKeyStateData(byte[] unPackData) {
    ImagePane imagePane = this.kvmUtil.getImagePane(this.bladeNO);
    if (imagePane != null) {
      this.keyState.clear();
      this.keyState.put(this.bladeNO + "", Byte.valueOf(unPackData[2]));
      if (this.bladeNO == this.kvmInterface.actionBlade || (this.kvmInterface.fullScreen != null && this.bladeNO == this.kvmInterface.fullScreen.actionBlade))
      {
        this.kvmUtil.setNumAndCapLock();
      }
    } 
  }
  public void distributeConnectStateData(byte[] unPackData) {
    int conState = unPackData[2];
    switch (conState) {
      case 2:
        this.drawThread.imagePane.quitConn("over_userconnect");
        break;
      case 3:
        this.drawThread.imagePane.quitConn("str_errorname");
        break;
      case 4:
        this.drawThread.imagePane.quitConn("SIGNAL_OUT_RANGE");
        break;
      case 5:
        this.drawThread.imagePane.quitConn("Server_Disabled");
        break;
      case 6:
        this.drawThread.imagePane.quitConn("encry_not_match");
        break;
    } 
  }
  public void distributeMouseModeData(byte[] unPackData) {
    int conState = 0;
    conState = unPackData[2];
    switch (conState) {
      case 0:
        Base.isSynMouse = false;
        if (null != this.drawThread.imagePane && null != this.drawThread.imagePane.mouseTimerTask && !this.drawThread.imagePane.mouseTimerTask.isAlive()) {
          this.drawThread.imagePane.getClass(); 
          this.drawThread.imagePane.mouseTimerTask = this.drawThread.imagePane.new MouseTimerTask();        
          KVMUtil.startMouseList(this.drawThread.imagePane);
        } 
        if (null != this.kvmInterface.fullScreen && null != this.kvmInterface.fullScreen.toolBar && null != this.kvmInterface.fullScreen.toolBar.mouseModeButton)
        {
          this.kvmInterface.fullScreen.toolBar.mouseModeButton.setEnabled(true);
        }
        if (null != this.kvmInterface.floatToolbar.powerMenu && this.kvmInterface.floatToolbar.powerMenu.mouseModeSwitchMenu.isSelected())
        {
          this.kvmInterface.floatToolbar.powerMenu.mouseModeSwitchMenu.setSelected(false);
        }
        System.out.printf("mouse mode :rel(%s) \n", new Object[] { Boolean.valueOf(Base.isSynMouse) });
        break;
      case 1:
        Base.isSynMouse = true;
        Base.isSingleMouse = false;
        this.kvmInterface.floatToolbar.powerMenu.SingleMouseMenu.setSelected(false);
        if (null != this.kvmInterface.fullScreen && null != this.kvmInterface.fullScreen.toolBar && null != this.kvmInterface.fullScreen.toolBar.mouseModeButton) {
          this.kvmInterface.fullScreen.toolBar.mouseSynButton.setEnabled(false);
          this.kvmInterface.fullScreen.toolBar.mouseModeButton.setEnabled(false);
          this.kvmInterface.base.isMstsc = false;
        } 
        if (null != this.kvmInterface.floatToolbar.powerMenu && !this.kvmInterface.floatToolbar.powerMenu.mouseModeSwitchMenu.isSelected())
        {
          this.kvmInterface.floatToolbar.powerMenu.mouseModeSwitchMenu.setSelected(true);
        }
        System.out.printf("mouse mode :abs(%s) \n", new Object[] { Boolean.valueOf(Base.isSynMouse) });
        break;
    } 
  }
  private void distributeNegotiVMMCodeKey(byte[] unPackData) {
    if (unPackData == null) {
      return;
    }
    int data_len = unPackData.length - 2;
    if (data_len > 0) {
      byte[] codekey = null;
      byte[] salt = null;
      byte[] nego_data = new byte[data_len];
      byte[] key_salt_data = nego_data;
      System.arraycopy(unPackData, 2, nego_data, 0, data_len);
      if (this.kvmInterface.kvmUtil.getSecretKVM(this.bladeNO)) {
        byte[] key = getBladeKvmkey();
        byte[] iv = getBladeKeyIV();
        byte[] decryted_data = AESHandler.aes_cbc_128_decrypt(nego_data, key, iv);
        key_salt_data = decryted_data;
      } 
      if (data_len >= 20) {
        codekey = new byte[20];
        System.arraycopy(key_salt_data, 0, codekey, 0, 20);
      } 
      if (data_len >= 36) {
        salt = new byte[16];
        System.arraycopy(key_salt_data, 20, salt, 0, 16);
      } 
      this.kvmInterface.kvmUtil.setVMMSecretCodeKey(this.bladeNO, codekey);
      this.kvmInterface.kvmUtil.setVMMSecretSalt(this.bladeNO, salt);
    } 
  }
  public void setDrawThread(DrawThread drawThread) {
    this.drawThread = drawThread;
  }
  public DrawThread getDrawThread() {
    return this.drawThread;
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public void setUnPackData(UnPackData unPack) {
    this.unPack = unPack;
  }
  public void setBladeNO(int bladeNO) {
    this.bladeNO = bladeNO;
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
  public int getBladeNO() {
    return this.bladeNO;
  }
}
