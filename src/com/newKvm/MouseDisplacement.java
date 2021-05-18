package com.newKvm;
import com.kvmV1.Base;
import com.kvmV1.KVMUtil;
public class MouseDisplacement {
  public static native int getMouseDisplacement(byte[] paramArrayOfbyte);
  public static native int setMode(int paramInt);
  public static native int keyBoardEncrypt(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt1, int paramInt2);
  public static native int setKeyBoardStatus(boolean paramBoolean, byte paramByte);
  static {
    String s1 = System.getProperty("file.separator");
    String s2 = System.getProperty("java.io.tmpdir");
    String mouseLib = Base.MOUSE_LIB;
    String mouseLibExt = ".dll";
    if (KVMUtil.isLinuxOS()) {
      mouseLib = "libkeyboard_encrypt";
      mouseLibExt = ".so";
    } 
    if (s2 != null) {
      if (s1 != null)
      {
        if (!s2.endsWith(s1))
        {
          s2 = s2 + s1;
        }
      }
      s2 = s2 + mouseLib + Base.libID + mouseLibExt;
      System.load(s2);
    } 
  }
  public static native int getKeyBoardStatus(byte paramByte);
  public static native int isMstsc();
  public static native int getScanCode();
  public static native int installHook();
  public static native int removeHook(int paramInt);
}
