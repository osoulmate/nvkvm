package com.kvm;
import com.library.LoggerUtil;
public class KeyboardImpl
{
  static {
    String s1 = System.getProperty("file.separator");
    String s2 = System.getProperty("java.io.tmpdir");
    String keyboardLib = Base.getKEYBOARD_LIB();
    String keyboardLibExt = ".so";
    if (KVMUtil.isLinuxOS())
    {
      if (s2 != null) {
        if (s1 != null)
        {
          if (!s2.endsWith(s1))
          {
            s2 = s2 + s1;
          }
        }
        s2 = s2 + keyboardLib + Base.getLibID() + keyboardLibExt;
        try {
          System.load(s2);
        }
        catch (Throwable e) {
          LoggerUtil.error("load keyboard library error.");
        } 
      } 
    }
  }
  private static boolean initial = false;
  public static void install_s() {
    try {
      install();
      initial = true;
    }
    catch (Throwable e) {
      LoggerUtil.error("install keyboard library error.");
    } 
  }
  public static void remove_s() {
    try {
      remove();
      initial = false;
    }
    catch (Throwable e) {
      LoggerUtil.error("remove keyboard library error.");
    } 
  }
  public static int getScanCode_s() {
    int code = 0;
    try {
      if (initial)
      {
        code = getScanCode();
      }
    }
    catch (Throwable e) {
      LoggerUtil.error("get scancode error.");
    } 
    return code;
  }
  public static native int install();
  public static native int remove();
  public static native int getScanCode();
}
