package com.kvm;
public class MouseDisplacementImpl
{
  public static int getMouseDisplacement(byte[] bytes) {
    if (KVMUtil.isLinuxOS())
    {
      return 0;
    }
    return MouseDisplacement.getMouseDisplacement(bytes);
  }
  public static int setMode(int model) {
    if (KVMUtil.isLinuxOS())
    {
      return 0;
    }
    return MouseDisplacement.setMode(model);
  }
  public static int setKeyBoardStatus(boolean isBright, byte keyValue) {
    if (KVMUtil.isLinuxOS())
    {
      return 0;
    }
    return MouseDisplacement.setKeyBoardStatus(isBright, keyValue);
  }
  public static int getKeyBoardStatus(byte keyValue) {
    if (KVMUtil.isLinuxOS())
    {
      return 0;
    }
    return MouseDisplacement.getKeyBoardStatus(keyValue);
  }
  public static int isMstsc() {
    if (KVMUtil.isLinuxOS())
    {
      return 0;
    }
    return MouseDisplacement.isMstsc();
  }
  public static int getScanCode() {
    if (KVMUtil.isLinuxOS())
    {
      return 0;
    }
    return MouseDisplacement.getScanCode();
  }
  public static int installHook() {
    if (KVMUtil.isLinuxOS()) {
      if (KVMUtil.isLinux())
      {
        KeyboardImpl.install_s();
      }
      return 1;
    } 
    int hookNum = MouseDisplacement.installHook();
    return hookNum;
  }
  public static int removeHook(int hookNum) {
    if (KVMUtil.isLinuxOS()) {
      if (KVMUtil.isLinux())
      {
        KeyboardImpl.remove_s();
      }
      return 0;
    } 
    return MouseDisplacement.removeHook(hookNum);
  }
}
