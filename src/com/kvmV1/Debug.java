package com.kvmV1;
public class Debug
{
  public static boolean printFlag = false;
  public static void print(String aString) {
    if (printFlag)
    {
      System.out.print(aString);
    }
  }
  public static void println(String aString) {
    if (printFlag)
    {
      System.out.println(aString);
    }
  }
  public static void printByte(byte[] bytes) {
    if (printFlag) {
      for (int i = 0; i < bytes.length; i++)
      {
        System.out.print(bytes[i] + " ");
      }
      System.out.println("");
    } 
  }
  public static void printExc(String aString) {
    if (printFlag)
    {
      System.err.println(aString);
    }
  }
  public static void printf(String astring, Object[] obj) {
    if (printFlag)
    {
      System.out.printf(astring, obj);
    }
  }
}
