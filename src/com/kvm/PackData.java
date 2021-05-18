package com.kvm;
import com.huawei.vm.console.utils.TestPrint;
import com.library.LoggerUtil;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;
public class PackData
{
  private byte[] keyData = new byte[8];
  public byte[] getKeyData() {
    return (byte[])this.keyData.clone();
  }
  public void setKeyData(byte[] keyData) {
    if (keyData != null) {
      this.keyData = (byte[])keyData.clone();
    }
    else {
      this.keyData = null;
    } 
  }
  private byte[] mousData = new byte[] { 0, 0, 0, 0, 0, 0 }; private static final byte PACKHEAD1 = -2; private static final byte PACKHEAD2 = -10; public static final String crc16 = "CRC_16"; public static final String crcITI = "CRC_CCITT"; public static final String crc16H = "CRC_16_H"; public static final String crc32 = "CRC_32"; private static final byte REQ_BLADE_PRESENT = 11; private static final byte REQ_BLADE_STATE = 20; private static final byte REQ_BLADE_STATE_TRANSMIT = 33; private static final byte CONNECT_BLADE = 6; private static final byte INTERRUPT_BLADE = 7; private static final byte MOUSE_PACK = 5; private static final byte KEY_PACK = 3; private static final byte I_REQ = 8; private static final byte HEART_BEAT = 9; private static final byte REQ_BLADE_MONITOR = 23;
  private static final byte INTERRUPT_MONITOR = 24;
  public byte[] getMousData() {
    return (byte[])this.mousData.clone();
  }
  private static final byte DELETE_USER = 25; private static final byte REPLAY_SMM = 26; private static final byte COLOR_BIT = 27; private static final byte FRAME_COMM = 28; private static final byte RETRY_CONN = 30; public static final byte KVM_CMD_POWEROFF = 32; public static final byte KVM_CMD_POWERON = 33; public static final byte KVM_CMD_RESTART = 34; public static final byte KVM_CMD_SAFETY_RESTART = 35; public static final byte KVM_CMD_SAVE_POWEROFF = 37; public static final byte KVM_CMD_USBRESET = 48; public static final byte KVM_CMD_SECURITY = 51; public static final byte MOUSE_MODE_SET = 36; public static final byte REQ_VMM_CODEKEY = 49; public static final byte REQ_VMM_PORT = 53; public static final byte GET_SUITE = 66; public static final byte SET_SUITE = 68; public static final byte DQT_MODE_SET = 39;
  public void setMousData(byte[] mousData) {
    if (mousData != null) {
      this.mousData = (byte[])mousData.clone();
    }
    else {
      this.mousData = null;
    } 
  }
  private int meta = 0;
  public int getMeta() {
    return this.meta;
  }
  public void setMeta(int meta) {
    this.meta = meta;
  }
  private int customkeystate = 0;
  public int getCustomkeystate() {
    return this.customkeystate;
  }
  public void setCustomkeystate(int customkeystate) {
    this.customkeystate = customkeystate;
  }
  private KVMInterface kvmInterface = null;
  public void setKvmInterface(KVMInterface kvmInterface2) {
    this.kvmInterface = kvmInterface2;
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
    }  if (e.isMetaDown() || 524 == e.getKeyCode())
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
  private byte[] makePackData(int id, byte[] data, int length) {
    byte low = (byte)(length + 2 & 0xFF);
    byte high = (byte)((length + 2 & 0xFF00) >> 8);
    byte[] packData = new byte[length + 8 + 2];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[length];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = high;
    packData[3] = low;
    KVMUtil.intToByteCon(packData, 4, id);
    System.arraycopy(data, 0, packData, 10, length);
    System.arraycopy(packData, 10, checkTemp, 0, length);
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[8] = temp[1];
    packData[9] = temp[0];
    return packData;
  }
  private byte[] makeEncrypPackData(byte[] id, byte[] data, int length) {
    int len = length & 0x7FFF;
    byte low = (byte)(length + 2 & 0xFF);
    byte high = (byte)((length + 2 & 0xFF00) >> 8);
    byte[] packData = new byte[len + 28 + 2];
    byte[] temp = new byte[4];
    byte[] checkTemp = new byte[len];
    int checkResult = 0;
    packData[0] = -2;
    packData[1] = -10;
    packData[2] = high;
    packData[3] = low;
    System.arraycopy(id, 0, packData, 4, 24);
    System.arraycopy(data, 0, packData, 30, len);
    System.arraycopy(packData, 30, checkTemp, 0, len);
    checkResult = KVMUtil.crc.wCrc((short)0, checkTemp, (short)checkTemp.length);
    KVMUtil.intToByte(temp, 0, checkResult);
    packData[28] = temp[1];
    packData[29] = temp[0];
    LoggerUtil.info( "packData: "+ packData );
    return packData;
  }
  public byte[] reqBladePresent() {
    byte[] data = new byte[1];
    data[0] = 11;
    return makePackData(this.kvmInterface.getCodeKey(), data, data.length);
  }
  public byte[] reqBladeState(int bladeNO, int connMode) {
    byte[] data = new byte[2];
    data[0] = 20;
    if (connMode == 1)
    {
      data[0] = 33;
    }
    data[1] = (byte)bladeNO;
    return makePackData(this.kvmInterface.getCodeKey(), data, data.length);
  }
  public byte[] reqVMCodeKey(int bladeNO) {
    byte[] data = new byte[2];
    data[0] = 49;
    data[1] = (byte)bladeNO;
    return makePackData(this.kvmInterface.getCodeKey(), data, data.length);
  }
  public byte[] reqVMPort(int bladeNO) {
    byte[] data = new byte[2];
    data[0] = 53;
    data[1] = (byte)bladeNO;
    return makePackData(this.kvmInterface.getCodeKey(), data, data.length);
  }
  public byte[] getSuiteList(int bladeNO) {
	LoggerUtil.info( "getSuiteList: "+bladeNO);
    byte[] data = new byte[2];
    data[0] = 66;
    data[1] = (byte)bladeNO;
    return makePackData(this.kvmInterface.getCodeKey(), data, data.length);
  }
  public byte[] setSuitePack(int bladeNO, int iter, int hmac) {
    byte[] data = new byte[7];
    data[0] = 68;
    data[1] = (byte)bladeNO;
    data[2] = (byte)hmac;
    data[3] = (byte)((iter & 0xFF000000) >> 24);
    data[4] = (byte)((iter & 0xFF0000) >> 16);
    data[5] = (byte)((iter & 0xFF00) >> 8);
    data[6] = (byte)(iter & 0xFF);
    return makePackData(this.kvmInterface.getCodeKey(), data, data.length);
  }
  public byte[] connectBlade(int bladeNo, byte colorBit) {
    int length = 0;
    LoggerUtil.info( "bladeNo: "+ bladeNo + " colorBit: "+ colorBit);
    if (Base.getCompress() == 0) {
      byte[] arrayOfByte = new byte[133];
      length = 5;
      arrayOfByte[0] = 6;
      arrayOfByte[1] = (byte)bladeNo;
      arrayOfByte[2] = colorBit;
      arrayOfByte[3] = 1;
      arrayOfByte[4] = 1;
      if (this.kvmInterface.getReconnKey() != null) {
        System.arraycopy(this.kvmInterface.getReconnKey(), 0, arrayOfByte, 5, 128);
        length += 128;
      } 
      return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), arrayOfByte, length);
    } 
    byte[] data = new byte[133];
    length = 32773;
    data[0] = 6;
    data[1] = (byte)bladeNo;
    data[2] = colorBit;
    data[3] = 1;
    data[4] = 1;
    if (this.kvmInterface.getReconnKey() != null) {
      System.arraycopy(this.kvmInterface.getReconnKey(), 0, data, 5, 128);
      length = 0x8000 | data.length;
    } 
    return makeEncrypPackData(this.kvmInterface.getEncodeKey(), data, length);
  }
  public byte[] reConnectBlade(int bladeNo, byte colorBit) {
    byte[] data = new byte[5];
    data[0] = 6;
    data[1] = (byte)bladeNo;
    data[2] = colorBit;
    data[3] = 1;
    data[4] = 1;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] monitorBlade(int bladeNo) {
    byte[] data = new byte[3];
    data[0] = 23;
    data[1] = (byte)bladeNo;
    data[2] = 1;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] interruptBlade(int bladeNo) {
    byte[] data = new byte[2];
    data[0] = 7;
    data[1] = (byte)bladeNo;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] interruptMonitor(int bladeNo) {
    byte[] data = new byte[3];
    data[0] = 24;
    data[1] = (byte)bladeNo;
    data[2] = 1;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] interruptTempBlade(int bladeNo) {
    byte[] data = new byte[4];
    data[0] = 7;
    data[1] = (byte)bladeNo;
    data[2] = 1;
    data[3] = 1;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] deleteUser() {
    byte[] data = new byte[1];
    data[0] = 25;
    return makePackData(this.kvmInterface.getCodeKey(), data, data.length);
  }
  public byte[] mousePack(int x, int y, int bladeNo) {
    if (0 == Base.getCompress()) {
      byte[] arrayOfByte1 = new byte[8];
      byte[] arrayOfByte2 = new byte[8];
      arrayOfByte1[0] = 5;
      arrayOfByte1[1] = (byte)bladeNo;
      arrayOfByte1[2] = this.mousData[0];
      KVMUtil.intToByte(arrayOfByte2, 0, x);
      arrayOfByte1[3] = arrayOfByte2[1];
      arrayOfByte1[4] = arrayOfByte2[0];
      KVMUtil.intToByte(arrayOfByte2, 2, y);
      arrayOfByte1[5] = arrayOfByte2[3];
      arrayOfByte1[6] = arrayOfByte2[2];
      arrayOfByte1[7] = this.mousData[5];
      return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), arrayOfByte1, arrayOfByte1.length);
    } 
    byte[] temDes = null;
    byte[] data = new byte[18];
    byte[] temp = new byte[8];
    byte[] mouse_data = new byte[6];
    data[0] = 5;
    data[1] = (byte)bladeNo;
    KVMUtil.intToByte(temp, 0, x);
    KVMUtil.intToByte(temp, 2, y);
    mouse_data[0] = this.mousData[0];
    mouse_data[1] = temp[1];
    mouse_data[2] = temp[0];
    mouse_data[3] = temp[3];
    mouse_data[4] = temp[2];
    mouse_data[5] = this.mousData[5];
    temDes = AESHandler.secure_encry(mouse_data, Base.getKvm_key(), 6);
    if (null != temDes && 16 == temDes.length)
    {
      System.arraycopy(temDes, 0, data, 2, temDes.length);
    }
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] mousePackNew(byte x, byte y, int bladeNo) {
    if (0 == Base.getCompress()) {
      byte[] arrayOfByte = new byte[6];
      arrayOfByte[0] = 5;
      arrayOfByte[1] = (byte)bladeNo;
      this.mousData[1] = x;
      this.mousData[2] = y;
      arrayOfByte[2] = this.mousData[0];
      arrayOfByte[3] = this.mousData[1];
      arrayOfByte[4] = this.mousData[2];
      arrayOfByte[5] = this.mousData[3];
      return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), arrayOfByte, arrayOfByte.length);
    } 
    byte[] data = new byte[18];
    byte[] mouse_data = new byte[4];
    byte[] temDes = null;
    data[0] = 5;
    data[1] = (byte)bladeNo;
    this.mousData[1] = x;
    this.mousData[2] = y;
    mouse_data[0] = this.mousData[0];
    mouse_data[1] = this.mousData[1];
    mouse_data[2] = this.mousData[2];
    mouse_data[3] = this.mousData[3];
    temDes = AESHandler.secure_encry(mouse_data, Base.getKvm_key(), 4);
    if (null != temDes && 16 == temDes.length)
    {
      System.arraycopy(temDes, 0, data, 2, temDes.length);
    }
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] mousePackNew_abs(int x, int y, int bladeNo) {
    if (0 == Base.getCompress()) {
      byte[] arrayOfByte1 = new byte[8];
      byte[] arrayOfByte2 = new byte[8];
      arrayOfByte1[0] = 5;
      arrayOfByte1[1] = (byte)bladeNo;
      x = x * 3000 / this.kvmInterface.getKvmUtil().getImagePane(bladeNo).getImagePaneWidth();
      y = y * 3000 / this.kvmInterface.getKvmUtil().getImagePane(bladeNo).getImagePaneHeight();
      KVMUtil.intToByte(arrayOfByte2, 0, x);
      KVMUtil.intToByte(arrayOfByte2, 2, y);
      arrayOfByte1[2] = this.mousData[0];
      arrayOfByte1[3] = arrayOfByte2[1];
      arrayOfByte1[4] = arrayOfByte2[0];
      arrayOfByte1[5] = arrayOfByte2[3];
      arrayOfByte1[6] = arrayOfByte2[2];
      arrayOfByte1[7] = this.mousData[5];
      return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), arrayOfByte1, arrayOfByte1.length);
    } 
    byte[] data = new byte[18];
    byte[] temp = new byte[8];
    byte[] mouse_data = new byte[6];
    byte[] temDes = null;
    data[0] = 5;
    data[1] = (byte)bladeNo;
    x = x * 3000 / this.kvmInterface.getKvmUtil().getImagePane(bladeNo).getImagePaneWidth();
    y = y * 3000 / this.kvmInterface.getKvmUtil().getImagePane(bladeNo).getImagePaneHeight();
    KVMUtil.intToByte(temp, 0, x);
    KVMUtil.intToByte(temp, 2, y);
    mouse_data[0] = this.mousData[0];
    mouse_data[1] = temp[1];
    mouse_data[2] = temp[0];
    mouse_data[3] = temp[3];
    mouse_data[4] = temp[2];
    mouse_data[5] = this.mousData[5];
    temDes = AESHandler.secure_encry(mouse_data, Base.getKvm_key(), 6);
    if (null != temDes && 16 == temDes.length)
    {
      System.arraycopy(temDes, 0, data, 2, temDes.length);
    }
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  private int keyDataProc(int bladeNo, byte[] key, byte[] data) {
    byte[] keyEnData = new byte[16];
    if (((BladeThread)this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(bladeNo))).isNew()) {
      encry(key, keyEnData, 8, bladeNo);
      System.arraycopy(keyEnData, 0, data, 2, 16);
      return 18;
    } 
    System.arraycopy(key, 0, data, 2, 8);
    return 10;
  }
  public byte[] combinKeyCS(int bladeNo) {
    int length = 0;
    byte[] data = new byte[18];
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, new byte[] { 3, 0, 0, 0, 0, 0, 0, 0 }, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] combinKeyCE(int bladeNo) {
    int length = 0;
    byte[] data = new byte[18];
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, new byte[] { 1, 0, 41, 0, 0, 0, 0, 0 }, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] combinKeyCAD(int bladeNo) {
    int length = 0;
    byte[] data = new byte[18];
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, new byte[] { 5, 0, 76, 0, 0, 0, 0, 0 }, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] combinKeyAT(int bladeNo) {
    int length = 0;
    byte[] data = new byte[18];
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, new byte[] { 4, 0, 43, 0, 0, 0, 0, 0 }, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] combinKeyCSP(int bladeNo) {
    int length = 0;
    byte[] data = new byte[18];
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, new byte[] { 1, 0, 44, 0, 0, 0, 0, 0 }, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] combinKeyT(int bladeNo) {
    int length = 0;
    byte[] data = new byte[18];
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, new byte[] { 0, 0, 43, 0, 0, 0, 0, 0 }, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] combinKeyNum(int bladeNo) {
    int length = 0;
    byte[] data = new byte[18];
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, new byte[] { 0, 0, 83, 0, 0, 0, 0, 0 }, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] combinKeyCtrl(int bladeNo) {
    int length = 0;
    byte[] data = new byte[18];
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, new byte[] { 1, 0, 0, 0, 0, 0, 0, 0 }, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] combinKeyCtrlAlt(int bladeNo) {
    int length = 0;
    byte[] data = new byte[18];
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, new byte[] { 5, 0, 0, 0, 0, 0, 0, 0 }, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] combinKeyCtrlAltDel(int bladeNo) {
    int length = 0;
    byte[] data = new byte[18];
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, new byte[] { 5, 0, 76, 0, 0, 0, 0, 0 }, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] combinKeyCustom(int bladeNo, CombinationKey combinationKey) {
    int length = 0;
    byte[] data = new byte[18];
    byte[] temKeyData = new byte[8];
    data[0] = 3;
    data[1] = (byte)bladeNo;
    if (combinationKey.keyValue1 != 0)
    {
      if (224 == combinationKey.keyValue1 || 225 == combinationKey.keyValue1 || 226 == combinationKey.keyValue1) {
        temKeyData[0] = (byte)customKey(combinationKey.keyValue1);
      }
      else {
        temKeyData[2] = (byte)combinationKey.keyValue1;
      } 
    }
    if (combinationKey.keyValue2 != 0)
    {
      if (224 == combinationKey.keyValue2 || 225 == combinationKey.keyValue2 || 226 == combinationKey.keyValue2) {
        temKeyData[0] = (byte)customKey(combinationKey.keyValue2);
      }
      else {
        temKeyData[3] = (byte)combinationKey.keyValue2;
      } 
    }
    if (combinationKey.keyValue3 != 0)
    {
      if (224 == combinationKey.keyValue3 || 225 == combinationKey.keyValue3 || 226 == combinationKey.keyValue3) {
        temKeyData[0] = (byte)customKey(combinationKey.keyValue3);
      }
      else {
        temKeyData[4] = (byte)combinationKey.keyValue3;
      } 
    }
    if (combinationKey.keyValue4 != 0)
    {
      if (224 == combinationKey.keyValue4 || 225 == combinationKey.keyValue4 || 226 == combinationKey.keyValue4) {
        temKeyData[0] = (byte)customKey(combinationKey.keyValue4);
      }
      else {
        temKeyData[5] = (byte)combinationKey.keyValue4;
      } 
    }
    if (combinationKey.keyValue5 != 0)
    {
      if (224 == combinationKey.keyValue5 || 225 == combinationKey.keyValue5 || 226 == combinationKey.keyValue5) {
        temKeyData[0] = (byte)customKey(combinationKey.keyValue5);
      }
      else {
        temKeyData[6] = (byte)combinationKey.keyValue5;
      } 
    }
    if (combinationKey.keyValue6 != 0)
    {
      if (224 == combinationKey.keyValue6 || 225 == combinationKey.keyValue6 || 226 == combinationKey.keyValue6) {
        temKeyData[0] = (byte)customKey(combinationKey.keyValue6);
      }
      else {
        temKeyData[7] = (byte)combinationKey.keyValue6;
      } 
    }
    temKeyData[1] = 0;
    length = keyDataProc(bladeNo, temKeyData, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] clearKey(int bladeNO) {
    int length = 0;
    byte[] data = new byte[18];
    Arrays.fill(this.keyData, (byte)0);
    BladeThread bladeThread = (BladeThread)this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(bladeNO));
    data[0] = 3;
    data[1] = (byte)bladeNO;
    length = keyDataProc(bladeNO, this.keyData, data);
    if (((BladeThread)this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(bladeNO))).isNew()) {
      byte[] keyEnData = bladeThread.getKeyEnData();
      encry(this.keyData, keyEnData, 8, bladeNO);
      bladeThread.setKeyEnData(keyEnData);
    } 
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNO), data, length);
  }
  public byte[] resetKey(int bladeNo) {
    int length = 0;
    byte[] data = new byte[18];
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, this.keyData, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] keyBoardState(int bladeNo) {
    byte[] data = new byte[3];
    data[0] = 4;
    data[1] = (byte)bladeNo;
    data[2] = 1;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] resendData(int bladeNo) {
    byte[] data = new byte[2];
    data[0] = 8;
    data[1] = (byte)bladeNo;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
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
    int length = 0;
    byte[] data = new byte[18];
    byte[] temp = new byte[10];
    if (17 == e.getKeyCode() || 16 == e.getKeyCode() || 18 == e
      .getKeyCode() || 65406 == e.getKeyCode() || 157 == e
      .getKeyCode() || 524 == e.getKeyCode()) {
      KVMUtil.intToByte(temp, 0, virtualKey(e));
      this.keyData[0] = temp[0];
    }
    else {
      if (KVMUtil.isMacOS()) {
        KVMUtil.intToByte(temp, 0, virtualKey(e));
        this.keyData[0] = temp[0];
      } 
      for (int i = 2; i < 8; i++) {
        if (this.keyData[i] == 0) {
          KVMUtil.intToByte(temp, 0, KVMUtil.translateToUSBCode(e));
          this.keyData[i] = temp[0];
          break;
        } 
      } 
    } 
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, this.keyData, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] mousePackMstsc(byte x, byte y, int bladeNo) {
    byte[] data = new byte[6];
    data[0] = 5;
    data[1] = (byte)bladeNo;
    this.mousData[1] = x;
    this.mousData[2] = y;
    data[2] = this.mousData[0];
    data[3] = this.mousData[1];
    data[4] = this.mousData[2];
    data[5] = this.mousData[3];
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] keyRePressedPack(KeyEvent e, int bladeNo, boolean isNew) {
    int length = 0;
    byte[] data = new byte[18];
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, this.keyData, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] keyReleasedPack(KeyEvent e, int bladeNo, boolean isNew) {
    int length = 0;
    byte[] data = new byte[18];
    byte[] temp = new byte[10];
    if (17 == e.getKeyCode() || 16 == e.getKeyCode() || 18 == e
      .getKeyCode() || 65406 == e.getKeyCode() || 157 == e
      .getKeyCode()) {
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
    data[0] = 3;
    data[1] = (byte)bladeNo;
    length = keyDataProc(bladeNo, this.keyData, data);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, length);
  }
  public byte[] heartBeat() {
    byte[] data = new byte[2];
    data[0] = 9;
    data[1] = 0;
    return makePackData(this.kvmInterface.getCodeKey(), data, data.length);
  }
  public byte[] heartBeat(int bladeNO) {
    byte[] data = new byte[2];
    data[0] = 9;
    data[1] = (byte)bladeNO;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNO), data, data.length);
  }
  public byte[] replayToSMM(byte bladeNO, byte number) {
    byte[] data = new byte[3];
    data[0] = 26;
    data[1] = bladeNO;
    data[2] = number;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNO), data, data.length);
  }
  public byte[] setColorBit(int bladeNO, byte colorBit) {
    byte[] data = new byte[3];
    data[0] = 27;
    data[1] = (byte)bladeNO;
    data[2] = colorBit;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNO), data, data.length);
  }
  public byte[] contrRate(int frameNum) {
    byte[] data = new byte[2];
    data[0] = 28;
    data[1] = (byte)frameNum;
    return makePackData(this.kvmInterface.getCodeKey(), data, data.length);
  }
  public byte[] contrRate(int frameNum, int bladeNo) {
    byte[] data = new byte[2];
    data[0] = 28;
    data[1] = (byte)frameNum;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] kvmCmdPowerControl(byte cmd) {
    byte[] data = new byte[2];
    data[0] = cmd;
    data[1] = 0;
    return makePackData(this.kvmInterface.getCodeKey(), data, data.length);
  }
  public byte[] kvmCmdPowerControl(byte cmd, int bladeNo) {
    if (0 == Base.getCompress()) {
      byte[] arrayOfByte = new byte[2];
      arrayOfByte[0] = cmd;
      arrayOfByte[1] = 0;
      return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), arrayOfByte, arrayOfByte.length);
    } 
    byte[] temDes = null;
    byte[] data = new byte[18];
    byte[] cmd_data = new byte[16];
    cmd_data[15] = cmd;
    temDes = AESHandler.kvm_encry(cmd_data, Base.getKvm_key(), cmd_data.length);
    if (null == temDes)
    {
      return temDes;
    }
    data[0] = 51;
    data[1] = 0;
    System.arraycopy(temDes, 0, data, 2, 16);
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] kvmCmdvideoControl(int bladeNo) {
    byte[] data = new byte[2];
    data[0] = 64;
    data[1] = 0;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] kvmCmdvideounControl(int bladeNo) {
    byte[] data = new byte[2];
    data[0] = 65;
    data[1] = 0;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] mouseModeControl(byte cmd, byte mode, int bladeNo) {
    byte[] data = new byte[5];
    data[0] = cmd;
    data[1] = 0;
    data[2] = mode;
    data[3] = 0;
    data[4] = 0;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] DQTModeControl(byte mode, int bladeNo, byte type) {
    byte[] data = new byte[5];
    data[0] = 39;
    data[1] = 0;
    data[2] = mode;
    data[3] = type;
    data[4] = 0;
    return makePackData(this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNo), data, data.length);
  }
  public byte[] retryConn() {
    byte[] data = new byte[1];
    data[0] = 30;
    return makePackData(this.kvmInterface.getCodeKey(), data, data.length);
  }
  private void encry(byte[] src, byte[] des, int len, int bladeNO) {
    int keyCode = this.kvmInterface.getKvmUtil().getImagePaneCodeKey(bladeNO);
    if (Base.getCompress() == 0) {
      byte[] temDes = null;
      temDes = AESHandler.encry(src, keyCode, 8);
      if (null != des && null != temDes)
      {
        System.arraycopy(temDes, 0, des, 0, temDes.length);
      }
      else
      {
        TestPrint.println(3, "linuxos aes encry error");
      }
    } else {
      byte[] temDes = null;
      temDes = AESHandler.secure_encry(src, Base.getKvm_key(), 8);
      if (null != des && null != temDes) {
        System.arraycopy(temDes, 0, des, 0, temDes.length);
      }
      else {
        TestPrint.println(3, "windows os aes encry error");
      } 
    } 
  }
}
