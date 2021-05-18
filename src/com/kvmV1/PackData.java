package com.kvmV1;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
public class PackData
{
  public byte[] keyData = new byte[8];
  public byte[] mousData = new byte[] { 0, 0, 0, 0, 0, 0 };
  private static final byte PACKHEAD1 = -2;
  private static final byte PACKHEAD2 = -10;
  private static byte LEN_HEIGHT = 0;
  private static final int ONE = 1;
  public static final String crc16 = "CRC_16";
  public static final String crcITI = "CRC_CCITT";
  public static final String crc16H = "CRC_16_H";
  public static final String crc32 = "CRC_32";
  private static final byte REQ_BLADE_PRESENT = 11;
  private static final byte REQ_BLADE_STATE = 20;
  private static final byte REQ_BLADE_STATE_TRANSMIT = 33;
  private static final byte CONNECT_BLADE = 6;
  private static final byte INTERRUPT_BLADE = 7;
  private static final byte MOUSE_PACK = 5;
  private static final byte KEY_PACK = 3;
  private static final byte I_REQ = 8;
  private static final byte HEART_BEAT = 9;
  private static final byte REQ_BLADE_MONITOR = 23;
  private static final byte INTERRUPT_MONITOR = 24;
  private static final byte DELETE_USER = 25;
  private static final byte REPLAY_SMM = 26;
  private static final byte COLOR_BIT = 27;
  private static final byte FRAME_COMM = 28;
  private static final byte RETRY_CONN = 30;
  public static final byte KVM_CMD_POWEROFF = 32;
  public static final byte KVM_CMD_POWERON = 33;
  public static final byte KVM_CMD_RESTART = 34;
  public static final byte KVM_CMD_SAFETY_RESTART = 35;
  public static final byte KVM_CMD_SAVE_POWEROFF = 37;
  public static final byte KVM_CMD_USBRESET = 48;
  public static final byte MOUSE_MODE_SET = 36;
  public static final byte REQ_VMM_CODEKEY = 49;
  public static final byte KVM_CMD_DEVICE_CONTROL = 51;
  public static final byte KVM_CMD_DEVICE_CONTROL_LEN = 16;
  public int xCoordinate = 0;
  public int yCoordinate = 0;
  public int meta = 0;
  public int customkeystate = 0;
  public KVMInterface kvmInterface = null;
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public void setPackLenHead(boolean secureKvm) {
    LEN_HEIGHT = secureKvm ? Byte.MIN_VALUE : 0;
  }
  private static byte getLenHeigthByte(boolean secureKvm) {
    return secureKvm ? LEN_HEIGHT : 0;
  }
  private static int stateKey(int meta, int alt, int shift, int ctrl) {
    int keyState = 0;
    if (1 == meta) {
      if (1 == alt)
      {
        if (1 == shift)
        {
          if (1 == ctrl)
          {
            keyState = 15;
          }
          else
          {
            keyState = 14;
          }
        }
        else if (1 == ctrl)
        {
          keyState = 13;
        }
        else
        {
          keyState = 12;
        }
      }
      else if (1 == shift)
      {
        if (1 == ctrl)
        {
          keyState = 11;
        }
        else
        {
          keyState = 10;
        }
      }
      else if (1 == ctrl)
      {
        keyState = 9;
      }
      else
      {
        keyState = 8;
      }
    }
    else if (1 == alt) {
      if (1 == shift)
      {
        if (1 == ctrl)
        {
          keyState = 7;
        }
        else
        {
          keyState = 6;
        }
      }
      else if (1 == ctrl)
      {
        keyState = 5;
      }
      else
      {
        keyState = 4;
      }
    }
    else if (1 == shift) {
      if (1 == ctrl)
      {
        keyState = 3;
      }
      else
      {
        keyState = 2;
      }
    }
    else if (1 == ctrl) {
      keyState = 1;
    }
    else {
      keyState = 0;
    } 
    return keyState;
  }
  public static int virtualKey(KeyEvent e) {
    int location = 0;
    int keyState = 0;
    location = e.getKeyLocation();
    if (65406 == e.getKeyCode())
    {
      keyState |= 0x40;
    }
    if (e.isControlDown())
    {
      if (location == 3) {
        keyState |= 0x10;
      } else {
        keyState |= 0x1;
      }  } 
    if (e.isShiftDown())
    {
      if (location == 3) {
        keyState |= 0x20;
      } else {
        keyState |= 0x2;
      }  } 
    if (e.isAltDown()) {
      if (location == 3 && KVMUtil.isWindowsOS())
      {
        keyState &= 0xEE;
      }
      if (location == 3 || (location == 1 && !KVMUtil.isMacOS())) {
        keyState |= 0x40;
      } else {
        keyState |= 0x4;
      } 
    }  if (e.isMetaDown())
    {
      if (location == 3) {
        keyState |= 0x80;
      } else {
        keyState |= 0x8;
      } 
    }
    return keyState;
  }
  public int customKey(int usbCode) {
    if (224 == usbCode)
    {
      this.customkeystate |= 0x1;
    }
    if (225 == usbCode)
    {
      this.customkeystate |= 0x2;
    }
    if (226 == usbCode)
    {
      this.customkeystate |= 0x4;
    }
    if (this.meta != 0)
    {
      this.customkeystate |= 0x8;
    }
    return this.customkeystate;
  }
  public byte[] reqBladePresent() {
    int sessidLen = Base.securekvm ? 24 : 4;
    int packLen = sessidLen + 7;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[1];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(Base.securekvm);
    packData[3] = 3;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getSMMCodeKey_bytes());
    packData[sessidLen + 6] = 11;
    checkTemp[0] = packData[sessidLen + 6];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] reqBladeState(int bladeNO, int connMode) {
    int sessidLen = Base.securekvm ? 24 : 4;
    int packLen = sessidLen + 8;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[2];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(Base.securekvm);
    packData[3] = 4;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getSMMCodeKey_bytes());
    packData[sessidLen + 6] = 20;
    if (connMode == 1)
    {
      packData[sessidLen + 6] = 33;
    }
    KVMUtil.intToByte(temp, 0, bladeNO);
    packData[sessidLen + 7] = temp[0];
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] reqVMCodeKey(int bladeNO) {
    int sessidLen = Base.securekvm ? 24 : 4;
    int packLen = sessidLen + 8;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[2];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(Base.securekvm);
    packData[3] = 4;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getSMMCodeKey_bytes());
    packData[sessidLen + 6] = 49;
    KVMUtil.intToByte(temp, 0, bladeNO);
    packData[sessidLen + 7] = temp[0];
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] connectBlade(int bladeNo, byte colorBit) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 10 + sessidLen;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[4];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 6;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 6;
    packData[sessidLen + 7] = (byte)bladeNo;
    packData[sessidLen + 8] = colorBit;
    packData[sessidLen + 9] = 0;
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkTemp[2] = packData[sessidLen + 8];
    checkTemp[3] = packData[sessidLen + 9];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] reConnectBlade(int bladeNo, byte colorBit) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 10 + sessidLen;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[4];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 6;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 6;
    packData[sessidLen + 7] = (byte)bladeNo;
    packData[sessidLen + 8] = colorBit;
    packData[sessidLen + 9] = 1;
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkTemp[2] = packData[sessidLen + 8];
    checkTemp[3] = packData[sessidLen + 9];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] monitorBlade(int bladeNo) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 9 + sessidLen;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[3];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 5;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 23;
    KVMUtil.intToByte(temp, 0, bladeNo);
    packData[sessidLen + 7] = temp[0];
    packData[sessidLen + 8] = 1;
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkTemp[2] = packData[sessidLen + 8];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] interruptBlade(int bladeNo, BladeThread bThread) {
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 8 + sessidLen;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[2];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 4;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo, bThread));
    packData[sessidLen + 6] = 7;
    packData[sessidLen + 7] = (byte)bladeNo;
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] interruptBlade(int bladeNo) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 8 + sessidLen;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[2];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 4;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 7;
    packData[sessidLen + 7] = (byte)bladeNo;
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] interruptMonitor(int bladeNo) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 9 + sessidLen;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[3];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 5;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 24;
    KVMUtil.intToByte(temp, 0, bladeNo);
    packData[sessidLen + 7] = temp[0];
    packData[sessidLen + 8] = 1;
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkTemp[2] = packData[sessidLen + 8];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    Debug.println("checkResult interruptMonitor = " + checkResult);
    return packData;
  }
  public byte[] interruptTempBlade(int bladeNo) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 10 + sessidLen;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[4];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 6;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 7;
    packData[sessidLen + 7] = (byte)bladeNo;
    packData[sessidLen + 8] = 1;
    packData[sessidLen + 9] = 1;
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkTemp[2] = packData[sessidLen + 8];
    checkTemp[3] = packData[sessidLen + 9];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] deleteUser() {
    int sessidLen = Base.securekvm ? 24 : 4;
    int packLen = sessidLen + 7;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[1];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(Base.securekvm);
    packData[3] = 3;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getSMMCodeKey_bytes());
    packData[sessidLen + 6] = 25;
    checkTemp[0] = packData[sessidLen + 6];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] mousePack(int x, int y, int bladeNo) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    boolean bEncrted = (bThread != null && bThread.getEncrytedStatus());
    int sessidLen = bEncrted ? 24 : 4;
    int packLen = bEncrted ? (24 + sessidLen) : (14 + sessidLen);
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[bEncrted ? 18 : 8];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = (byte) (bEncrted ? 20 : 10);
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 5;
    packData[sessidLen + 7] = (byte)bladeNo;
    KVMUtil.intToByte(temp, 0, x);
    KVMUtil.intToByte(temp, 2, y);
    if (!bEncrted) {
      packData[sessidLen + 8] = this.mousData[0];
      packData[sessidLen + 9] = temp[1];
      packData[sessidLen + 10] = temp[0];
      packData[sessidLen + 11] = temp[3];
      packData[sessidLen + 12] = temp[2];
      packData[sessidLen + 13] = this.mousData[5];
    }
    else {
      byte[] mouse_data = new byte[6];
      byte[] temDes = null;
      mouse_data[0] = this.mousData[0];
      mouse_data[1] = temp[1];
      mouse_data[2] = temp[0];
      mouse_data[3] = temp[3];
      mouse_data[4] = temp[2];
      mouse_data[5] = this.mousData[5];
      temDes = AESHandler.encry_bytes(mouse_data, bThread.getBladeKbdkey(), bThread.getBladeKeyIV(), 6);
      if (null != temDes)
      {
        System.arraycopy(temDes, 0, packData, sessidLen + 8, temDes.length);
      }
    } 
    System.arraycopy(packData, sessidLen + 6, temp, 0, bEncrted ? 18 : 8);
    checkResult = KVMUtil.crc.wCrc((short)0, temp, (short)(bEncrted ? 18 : 8));
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] mousePackNew(byte x, byte y, int bladeNo) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    boolean bEncrted = (bThread != null && bThread.getEncrytedStatus());
    int sessidLen = bEncrted ? 24 : 4;
    int packLen = bEncrted ? (24 + sessidLen) : (12 + sessidLen);
    byte[] packData = new byte[packLen];
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = (byte) (bEncrted ? 20 : 8);
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 5;
    packData[sessidLen + 7] = (byte)bladeNo;
    this.mousData[1] = x;
    this.mousData[2] = y;
    if (!bEncrted) {
      packData[sessidLen + 8] = this.mousData[0];
      packData[sessidLen + 9] = this.mousData[1];
      packData[sessidLen + 10] = this.mousData[2];
      packData[sessidLen + 11] = this.mousData[3];
    }
    else {
      byte[] mouse_data = new byte[4];
      byte[] temDes = null;
      mouse_data[0] = this.mousData[0];
      mouse_data[1] = this.mousData[1];
      mouse_data[2] = this.mousData[2];
      mouse_data[3] = this.mousData[3];
      temDes = AESHandler.encry_bytes(mouse_data, bThread.getBladeKbdkey(), bThread.getBladeKeyIV(), 4);
      if (null != temDes)
      {
        System.arraycopy(temDes, 0, packData, sessidLen + 8, temDes.length);
      }
    } 
    packData[sessidLen + 4] = 0;
    packData[sessidLen + 5] = 0;
    return packData;
  }
  public byte[] mousePackNew_abs(int x, int y, int bladeNo) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    boolean bEncrted = (bThread != null && bThread.getEncrytedStatus());
    int sessidLen = bEncrted ? 24 : 4;
    int packLen = bEncrted ? (24 + sessidLen) : (14 + sessidLen);
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[bEncrted ? 18 : 8];
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = (byte) (bEncrted ? 20 : 10);
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 5;
    packData[sessidLen + 7] = (byte)bladeNo;
    try {
      x = x * 3000 / (this.kvmInterface.kvmUtil.getImagePane(bladeNo)).width;
      y = y * 3000 / (this.kvmInterface.kvmUtil.getImagePane(bladeNo)).height;
    }
    catch (ArithmeticException ae) {}
    KVMUtil.intToByte(temp, 0, x);
    KVMUtil.intToByte(temp, 2, y);
    if (!bEncrted) {
      packData[sessidLen + 8] = this.mousData[0];
      packData[sessidLen + 9] = temp[1];
      packData[sessidLen + 10] = temp[0];
      packData[sessidLen + 11] = temp[3];
      packData[sessidLen + 12] = temp[2];
      packData[sessidLen + 13] = this.mousData[5];
    }
    else {
      byte[] mouse_data = new byte[6];
      byte[] temDes = null;
      mouse_data[0] = this.mousData[0];
      mouse_data[1] = temp[1];
      mouse_data[2] = temp[0];
      mouse_data[3] = temp[3];
      mouse_data[4] = temp[2];
      mouse_data[5] = this.mousData[5];
      temDes = AESHandler.encry_bytes(mouse_data, bThread.getBladeKbdkey(), bThread.getBladeKeyIV(), 6);
      if (null != temDes)
      {
        System.arraycopy(temDes, 0, packData, sessidLen + 8, temDes.length);
      }
    } 
    packData[sessidLen + 4] = 0;
    packData[sessidLen + 5] = 0;
    return packData;
  }
  public byte[] mousePackMstsc(byte x, byte y, int bladeNo) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    boolean bEncrted = (bThread != null && bThread.getEncrytedStatus());
    int sessidLen = bEncrted ? 24 : 4;
    int packLen = bEncrted ? (24 + sessidLen) : (12 + sessidLen);
    byte[] packData = new byte[packLen];
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = (byte) (bEncrted ? 20 : 8);
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 5;
    packData[sessidLen + 7] = (byte)bladeNo;
    this.mousData[1] = x;
    this.mousData[2] = y;
    if (!bEncrted) {
      packData[sessidLen + 8] = this.mousData[0];
      packData[sessidLen + 9] = this.mousData[1];
      packData[sessidLen + 10] = this.mousData[2];
      packData[sessidLen + 11] = this.mousData[3];
    }
    else {
      byte[] mouse_data = new byte[4];
      byte[] temDes = null;
      mouse_data[0] = this.mousData[0];
      mouse_data[1] = this.mousData[1];
      mouse_data[2] = this.mousData[2];
      mouse_data[3] = this.mousData[3];
      temDes = AESHandler.encry_bytes(mouse_data, bThread.getBladeKbdkey(), bThread.getBladeKeyIV(), 4);
      if (null != temDes)
      {
        System.arraycopy(temDes, 0, packData, sessidLen + 8, temDes.length);
      }
    } 
    packData[sessidLen + 4] = 0;
    packData[sessidLen + 5] = 0;
    return packData;
  }
  private byte[] keyboardPackCommon(int bladeNo, byte[] kbdData) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 24 + sessidLen;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[10];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 20;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 3;
    packData[sessidLen + 7] = (byte)bladeNo;
    byte[] keyEnData = new byte[16];
    if (((BladeThread)this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo))).isNew()) {
      encry(kbdData, keyEnData, 8, bladeNo);
      System.arraycopy(keyEnData, 0, packData, sessidLen + 8, 16);
      packData[sessidLen + 4] = 0;
      packData[sessidLen + 5] = 0;
    }
    else {
      packData[3] = 12;
      System.arraycopy(kbdData, 0, packData, sessidLen + 8, 8);
      temp[0] = 3;
      temp[1] = (byte)bladeNo;
      System.arraycopy(packData, sessidLen + 8, temp, 2, 8);
      checkResult = KVMUtil.crc.wCrc((short)0, temp, (short)temp.length);
      KVMUtil.intToByte(temp, 0, checkResult);
      packData[sessidLen + 4] = temp[1];
      packData[sessidLen + 5] = temp[0];
    } 
    return packData;
  }
  public byte[] combinKeyCS(int bladeNo) {
    return keyboardPackCommon(bladeNo, new byte[] { 3, 0, 0, 0, 0, 0, 0, 0 });
  }
  public byte[] combinKeyCE(int bladeNo) {
    return keyboardPackCommon(bladeNo, new byte[] { 1, 0, 41, 0, 0, 0, 0, 0 });
  }
  public byte[] combinKeyCAD(int bladeNo) {
    return keyboardPackCommon(bladeNo, new byte[] { 5, 0, 76, 0, 0, 0, 0, 0 });
  }
  public byte[] combinKeyAT(int bladeNo) {
    return keyboardPackCommon(bladeNo, new byte[] { 4, 0, 43, 0, 0, 0, 0, 0 });
  }
  public byte[] combinKeyCSP(int bladeNo) {
    return keyboardPackCommon(bladeNo, new byte[] { 1, 0, 44, 0, 0, 0, 0, 0 });
  }
  public byte[] combinKeyT(int bladeNo) {
    return keyboardPackCommon(bladeNo, new byte[] { 0, 0, 43, 0, 0, 0, 0, 0 });
  }
  public byte[] combinKeyNum(int bladeNo) {
    return keyboardPackCommon(bladeNo, new byte[] { 0, 0, 83, 0, 0, 0, 0, 0 });
  }
  public byte[] combinKeyCtrl(int bladeNo) {
    return keyboardPackCommon(bladeNo, new byte[] { 1, 0, 0, 0, 0, 0, 0, 0 });
  }
  public byte[] combinKeyCtrlAlt(int bladeNo) {
    return keyboardPackCommon(bladeNo, new byte[] { 5, 0, 0, 0, 0, 0, 0, 0 });
  }
  public byte[] combinKeyCtrlAltDel(int bladeNo) {
    return keyboardPackCommon(bladeNo, new byte[] { 5, 0, 76, 0, 0, 0, 0, 0 });
  }
  public byte[] combinKeyCustom(int bladeNo, CombinationKey combinationKey) {
    byte[] combinkey = new byte[8];
    if (combinationKey.keyValue1 != 0)
    {
      if (224 == combinationKey.keyValue1 || 225 == combinationKey.keyValue1 || 226 == combinationKey.keyValue1) {
        combinkey[0] = (byte)customKey(combinationKey.keyValue1);
      }
      else {
        combinkey[2] = (byte)combinationKey.keyValue1;
      } 
    }
    if (combinationKey.keyValue2 != 0)
    {
      if (224 == combinationKey.keyValue2 || 225 == combinationKey.keyValue2 || 226 == combinationKey.keyValue2) {
        combinkey[0] = (byte)customKey(combinationKey.keyValue2);
      }
      else {
        combinkey[3] = (byte)combinationKey.keyValue2;
      } 
    }
    if (combinationKey.keyValue3 != 0)
    {
      if (224 == combinationKey.keyValue3 || 225 == combinationKey.keyValue3 || 226 == combinationKey.keyValue3) {
        combinkey[0] = (byte)customKey(combinationKey.keyValue3);
      }
      else {
        combinkey[4] = (byte)combinationKey.keyValue3;
      } 
    }
    if (combinationKey.keyValue4 != 0)
    {
      if (224 == combinationKey.keyValue4 || 225 == combinationKey.keyValue4 || 226 == combinationKey.keyValue4) {
        combinkey[0] = (byte)customKey(combinationKey.keyValue4);
      }
      else {
        combinkey[5] = (byte)combinationKey.keyValue4;
      } 
    }
    if (combinationKey.keyValue5 != 0)
    {
      if (224 == combinationKey.keyValue5 || 225 == combinationKey.keyValue5 || 226 == combinationKey.keyValue5) {
        combinkey[0] = (byte)customKey(combinationKey.keyValue5);
      }
      else {
        combinkey[6] = (byte)combinationKey.keyValue5;
      } 
    }
    if (combinationKey.keyValue6 != 0)
    {
      if (224 == combinationKey.keyValue6 || 225 == combinationKey.keyValue6 || 226 == combinationKey.keyValue6) {
        combinkey[0] = (byte)customKey(combinationKey.keyValue6);
      }
      else {
        combinkey[7] = (byte)combinationKey.keyValue6;
      } 
    }
    combinkey[1] = 0;
    return keyboardPackCommon(bladeNo, combinkey);
  }
  public byte[] clearKey(int bladeNO) {
    Arrays.fill(this.keyData, (byte)0);
    return keyboardPackCommon(bladeNO, this.keyData);
  }
  public byte[] resetKey(int bladeNo) {
    return keyboardPackCommon(bladeNo, this.keyData);
  }
  public byte[] keyBoardState(int bladeNo) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 9 + sessidLen;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[3];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 5;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 4;
    KVMUtil.intToByte(temp, 0, bladeNo);
    packData[sessidLen + 7] = temp[0];
    packData[sessidLen + 8] = 1;
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkTemp[2] = packData[sessidLen + 8];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] resendData(int bladeNo) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 8 + sessidLen;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[2];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 4;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 8;
    packData[sessidLen + 7] = (byte)bladeNo;
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    Debug.println("checkResult resendData = " + checkResult);
    return packData;
  }
  public void mousePressedPack(MouseEvent e) {
    if (e.getButton() == 1)
    {
      this.mousData[0] = (byte)(this.mousData[0] | 0x1);
    }
    if (e.getButton() == 2)
    {
      this.mousData[0] = (byte)(this.mousData[0] | 0x4);
    }
    if (e.getButton() == 3)
    {
      this.mousData[0] = (byte)(this.mousData[0] | 0x2);
    }
  }
  public void mouseReleasedPack(MouseEvent e) {
    if (e.getButton() == 1)
    {
      this.mousData[0] = (byte)(this.mousData[0] & 0x6);
    }
    if (e.getButton() == 2)
    {
      this.mousData[0] = (byte)(this.mousData[0] & 0x3);
    }
    if (e.getButton() == 3)
    {
      this.mousData[0] = (byte)(this.mousData[0] & 0x5);
    }
  }
  public void mousePressedPackNew(byte mouseKey) {
    if (mouseKey == 1)
    {
      this.mousData[0] = (byte)(this.mousData[0] | 0x1);
    }
    if (mouseKey == 4)
    {
      this.mousData[0] = (byte)(this.mousData[0] | 0x4);
    }
    if (mouseKey == 2)
    {
      this.mousData[0] = (byte)(this.mousData[0] | 0x2);
    }
  }
  public void mouseWheelMovedPack(MouseWheelEvent e) {
    this.mousData[5] = (byte)e.getWheelRotation();
  }
  public byte[] keyPressedPack(KeyEvent e, int bladeNo, boolean isNew) {
    byte[] temp = new byte[10];
    if (17 == e.getKeyCode() || 16 == e.getKeyCode() || 18 == e.getKeyCode() || 157 == e.getKeyCode() || 65406 == e.getKeyCode()) {
      KVMUtil.intToByte(temp, 0, virtualKey(e));
      this.keyData[0] = temp[0];
    }
    else {
      for (int i = 2; i < 8; i++) {
        if (this.keyData[i] == 0) {
          KVMUtil.intToByte(temp, 0, KVMUtil.translateToUSBCode(e));
          this.keyData[i] = temp[0];
          break;
        } 
      } 
    } 
    return keyboardPackCommon(bladeNo, this.keyData);
  }
  public byte[] keyRePressedPack(KeyEvent e, int bladeNo, boolean isNew) {
    return keyboardPackCommon(bladeNo, this.keyData);
  }
  public byte[] keyReleasedPack(KeyEvent e, int bladeNo, boolean isNew) {
    byte[] temp = new byte[10];
    if (17 == e.getKeyCode() || 16 == e.getKeyCode() || 18 == e.getKeyCode() || 157 == e.getKeyCode() || 65406 == e.getKeyCode()) {
      KVMUtil.intToByte(temp, 0, virtualKey(e));
      this.keyData[0] = temp[0];
    }
    else {
      for (int i = 2; i < 8; i++) {
        if (this.keyData[i] == KVMUtil.translateToUSBCode(e)) {
          this.keyData[i] = 0;
          break;
        } 
      } 
    } 
    return keyboardPackCommon(bladeNo, this.keyData);
  }
  public byte[] heartBeat() {
    int sessidLen = Base.securekvm ? 24 : 4;
    int packLen = sessidLen + 8;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[2];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(Base.securekvm);
    packData[3] = 4;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getSMMCodeKey_bytes());
    packData[sessidLen + 6] = 9;
    packData[sessidLen + 7] = 0;
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] heartBeat(int bladeNO) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 8 + sessidLen;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[2];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 4;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNO));
    packData[sessidLen + 6] = 9;
    packData[sessidLen + 7] = (byte)bladeNO;
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] replayToSMM(byte bladeNO, byte number) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 9 + sessidLen;
    byte[] packData = new byte[packLen];
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 5;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNO));
    packData[sessidLen + 4] = 0;
    packData[sessidLen + 5] = 0;
    packData[sessidLen + 6] = 26;
    packData[sessidLen + 7] = bladeNO;
    packData[sessidLen + 8] = number;
    return packData;
  }
  public byte[] setColorBit(int bladeNO, byte colorBit) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 9 + sessidLen;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[3];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 5;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNO));
    packData[sessidLen + 6] = 27;
    packData[sessidLen + 7] = (byte)bladeNO;
    packData[sessidLen + 8] = colorBit;
    checkTemp[0] = packData[sessidLen + 6];
    checkTemp[1] = packData[sessidLen + 7];
    checkTemp[2] = packData[sessidLen + 8];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  public byte[] contrRate(int frameNum) {
    int sessidLen = Base.securekvm ? 24 : 4;
    int packLen = sessidLen + 8;
    byte[] packData = new byte[packLen];
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(Base.securekvm);
    packData[3] = 4;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getSMMCodeKey_bytes());
    packData[sessidLen + 6] = 28;
    packData[sessidLen + 7] = (byte)frameNum;
    packData[sessidLen + 4] = 0;
    packData[sessidLen + 5] = 0;
    return packData;
  }
  public byte[] contrRate(int frameNum, int bladeNo) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 8 + sessidLen;
    byte[] packData = new byte[packLen];
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 4;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = 28;
    packData[sessidLen + 7] = (byte)frameNum;
    packData[sessidLen + 4] = 0;
    packData[sessidLen + 5] = 0;
    return packData;
  }
  public byte[] kvmCmdPowerControl(byte cmd) {
    byte[] packData = new byte[12];
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = LEN_HEIGHT;
    packData[3] = 4;
    KVMUtil.intToByteCon(packData, 4, this.kvmInterface.codeKey);
    packData[10] = cmd;
    packData[11] = 0;
    packData[8] = 0;
    packData[9] = 0;
    return packData;
  }
  public byte[] kvmCmdPowerControl(byte cmd, int bladeNo) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 8 + sessidLen;
    boolean seceretCmd = (bThread != null && bThread.getEncrytedStatus() && this.kvmInterface.kvmUtil.getAuthVMM(bladeNo));
    if (seceretCmd == true)
    {
      packLen += 16;
    }
    byte[] packData = new byte[packLen];
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 4;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    if (seceretCmd) {
      try {
        SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
        secureRandomGenerator.setSeed(System.currentTimeMillis());
        byte[] randBytes = new byte[16];
        secureRandomGenerator.nextBytes(randBytes);
        randBytes[15] = cmd;
        byte[] encryBytes = AESHandler.encry_bytes(randBytes, bThread.getBladeKvmkey(), bThread.getBladeKeyIV(), 16);
        packData[3] = 20;
        packData[sessidLen + 4] = 0;
        packData[sessidLen + 5] = 0;
        packData[sessidLen + 6] = 51;
        packData[sessidLen + 7] = 0;
        if (encryBytes != null)
        {
          System.arraycopy(encryBytes, 0, packData, sessidLen + 8, 16);
        }
      }
      catch (NoSuchAlgorithmException e) {}
    }
    else {
      packData[sessidLen + 6] = cmd;
      packData[sessidLen + 7] = 0;
      packData[sessidLen + 4] = 0;
      packData[sessidLen + 5] = 0;
    } 
    return packData;
  }
  public byte[] mouseModeControl(byte cmd, byte mode, int bladeNo) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    int sessidLen = (bThread != null && bThread.getEncrytedStatus()) ? 24 : 4;
    int packLen = 11 + sessidLen;
    byte[] packData = new byte[packLen];
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(bThread.getEncrytedStatus());
    packData[3] = 7;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getImagePaneCodeKey_bytes(bladeNo));
    packData[sessidLen + 6] = cmd;
    packData[sessidLen + 7] = 0;
    packData[sessidLen + 8] = mode;
    packData[sessidLen + 9] = 0;
    packData[sessidLen + 10] = 0;
    packData[sessidLen + 4] = 0;
    packData[sessidLen + 5] = 0;
    System.out.printf("mouse mode cmd(%d) \n", new Object[] { Byte.valueOf(mode) });
    return packData;
  }
  public byte[] retryConn() {
    int sessidLen = Base.securekvm ? 24 : 4;
    int packLen = sessidLen + 7;
    byte[] packData = new byte[packLen];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[1];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = getLenHeigthByte(Base.securekvm);
    packData[3] = 3;
    KVMUtil.perIntToByteCon(packData, 4, this.kvmInterface.kvmUtil.getSMMCodeKey_bytes());
    packData[sessidLen + 6] = 30;
    checkTemp[0] = packData[sessidLen + 6];
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[sessidLen + 4] = temp[1];
    packData[sessidLen + 5] = temp[0];
    return packData;
  }
  private void encry(byte[] src, byte[] des, int len, int bladeNO) {
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO));
    byte[] temDes = null;
    if (bThread.getEncrytedStatus()) {
      temDes = AESHandler.encry_bytes(src, bThread.getBladeKbdkey(), bThread.getBladeKeyIV(), 8);
      if (null != des)
      {
        System.arraycopy(temDes, 0, des, 0, temDes.length);
      }
    }
    else {
      int keyCode = this.kvmInterface.kvmUtil.getImagePaneCodeKey(bladeNO);
      temDes = AESHandler.encry(src, keyCode, 8);
      if (null != des)
      {
        System.arraycopy(temDes, 0, des, 0, temDes.length);
      }
    } 
  }
}
