package com.newKvm;
import com.kvmV1.Base;
import com.kvmV1.KVMUtil;
public class KeyboardImpl {
  public static native int install();
  static {
    String s1 = System.getProperty("file.separator");
    String s2 = System.getProperty("java.io.tmpdir");
    String keyboardLib = Base.KEYBOARD_LIB;
    String keyboardLibExt = ".so";
    if (KVMUtil.isLinuxOS()) {
      if (!s2.endsWith(s1))
      {
        s2 = s2 + s1;
      }
      s2 = s2 + keyboardLib + Base.libID + keyboardLibExt;
      System.load(s2);
    } 
  }
  public static native int remove();
  public static native int getScanCode();
}
