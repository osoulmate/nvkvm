package com.kvm;
class GetDiffOSPara
{
  private static int loginWindowsLength = 355;
  private static int loginWindowsWidth = 265;
  private static int copyrightWindowsLength = 275;
  private static int copyrightWindowsWidth = 200;
  private static int loginUbuntuLength = 348;
  private static int loginUbuntuWidth = 240;
  private static int copyrightUbuntuLength = 300;
  private static int copyrightUbuntuWidth = 180;
  private static int loginMacLength = 348;
  private static int loginMacWidth = 260;
  private static int copyrightMacLength = 275;
  private static int copyrightMacWidth = 190;
  private static int loginLength = 0;
  private static int loginWidth = 0;
  private static int copyrightLength = 0;
  private static int copyrightWidth = 0;
  public static void osParaInit() {
    if (KVMUtil.isWindowsOS()) {
      loginLength = loginWindowsLength;
      loginWidth = loginWindowsWidth;
      copyrightLength = copyrightWindowsLength;
      copyrightWidth = copyrightWindowsWidth;
    } 
    if (KVMUtil.isLinux()) {
      loginLength = loginUbuntuLength;
      loginWidth = loginUbuntuWidth;
      copyrightLength = copyrightUbuntuLength;
      copyrightWidth = copyrightUbuntuWidth;
    } 
    if (KVMUtil.isMacOS()) {
      loginLength = loginMacLength;
      loginWidth = loginMacWidth;
      copyrightLength = copyrightMacLength;
      copyrightWidth = copyrightMacWidth;
    } 
  }
  public static int getLoginLength() {
    return loginLength;
  }
  public static int getLoginWidth() {
    return loginWidth;
  }
  public static int getCopyrightLength() {
    return copyrightLength;
  }
  public static int getCopyrightWidth() {
    return copyrightWidth;
  }
}
