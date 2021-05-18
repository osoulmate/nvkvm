package de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools;
public class Permissions
{
  public static int OTHER_Execute = 1;
  public static int OTHER_Write = 2;
  public static int OTHER_Read = 4;
  public static int OTHER_ChAttr = 8;
  public static int OTHER_Delete = 16;
  public static int GROUP_Execute = 32;
  public static int GROUP_Write = 64;
  public static int GROUP_Read = 128;
  public static int GROUP_ChAttr = 256;
  public static int GROUP_Delete = 512;
  public static int OWNER_Execute = 1024;
  public static int OWNER_Write = 2048;
  public static int OWNER_Read = 4096;
  public static int OWNER_ChAttr = 8192;
  public static int OWNER_Delete = 16384;
}
