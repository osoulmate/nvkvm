package com.huawei.vm.console.utilsV1;
import java.io.PrintStream;
public class TestPrint
{
  public static int isPrint = Integer.parseInt(ResourceUtil.getConfigItem("com.huawei.vm.console.config.print.level"));
  public static final int INFO = 1;
  public static final int DEBUG = 2;
  public static final int ERROR = 3;
  public static final int FATAL = 4;
  private static boolean isTime = false;
  private static PrintStream out = System.out;
  public static void println(int lev, String args) {
    if (isPrint < lev)
    {
      out.println(args);
    }
  }
  public static void println(int lev, byte args) {
    if (isPrint < lev)
    {
      out.println(args);
    }
  }
  public static void println(int lev, Object args) {
    if (isPrint < lev)
    {
      out.println(args);
    }
  }
  public static void println(int lev, int args) {
    if (isPrint < lev)
    {
      out.println(args);
    }
  }
  public static void print(int lev, String args) {
    if (isPrint < lev)
    {
      out.print(args);
    }
  }
  public static void print(int lev, byte args) {
    if (isPrint < lev)
    {
      out.print(args);
    }
  }
  public static void print(int lev, Object args) {
    if (isPrint < lev)
    {
      out.print(args);
    }
  }
  public static void print(int lev, int args) {
    if (isPrint < lev)
    {
      out.print(args);
    }
  }
  public static void printTime(String title) {
    if (isTime);
  }
  public static void printArray(int lev, byte[] array, int startPos, int len, String descrip) {
    if (isPrint < lev && null != array) {
      int dataLen = len;
      int arrLen = array.length;
      if (512 < arrLen)
      {
        dataLen = 12;
      }
      if (startPos <= arrLen - 1) {
        String byteStr = null;
        int strLen = 0;
        if (startPos + dataLen > arrLen)
        {
          dataLen = arrLen - startPos;
        }
        for (int i = startPos; i < dataLen; i++) {
          if (i > 0)
          {
            if ((i - startPos) % 12 != 0)
            {
              if ((i - startPos) % 4 == 0)
              {
                System.out.print("  "); } 
            }
          }
          byteStr = Integer.toHexString(array[i]);
          strLen = byteStr.length();
          if (strLen < 2) {
            byteStr = "0" + byteStr;
          }
          else if (strLen > 2) {
            byteStr = byteStr.substring(strLen - 2);
          } 
          System.out.print(byteStr + " ");
        } 
      } 
    } 
  }
  public static void printFor852T(byte[] packet) {
    int temLength = packet.length;
    for (int i = 0; i < temLength; i++);
  }
}
