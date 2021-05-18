package com.huawei.vm.console.utils;
import com.library.LoggerUtil;
public class TestPrint
{
  private static int isPrint = Integer.parseInt(ResourceUtil.getConfigItem("com.huawei.vm.console.config.print.level"));
  public static final int INFO = 1;
  public static final int DEBUG = 2;
  public static final int ERROR = 3;
  public static final int FATAL = 4;
  public static void println(int lev, String args) {
    if (isPrint < lev)
    {
      LoggerUtil.error(args);
    }
  }
  public static void setIsPrint(int nIsPrint) {
    isPrint = nIsPrint;
  }
}
