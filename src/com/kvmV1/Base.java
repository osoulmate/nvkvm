package com.kvmV1;
import com.huawei.vm.console.utilsV1.ResourceUtil;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
public class Base
{
  public static final String TYPE = ResourceUtil.getConfigItem("com.huawei.vm.console.config.jarname");
  public static long typeData = 0L;
  public static int bladeSize = 14;
  public static ArrayList<InterfaceContainer> bladeList = new ArrayList<InterfaceContainer>();
  public static int[] bladeViews;
  public static int connMode;
  public static String vmmConnIP;
  public static int vmmCodeKey;
  public static int vmmPort = 8208;
  public static int privilege;
  public static final int admin = 4;
  public static final int operator = 3;
  public static final int user = 2;
  public static final int callback = 1;
  public static boolean isSynMouse = false;
  public static boolean isSingleMouse = false;
  public static String local = "zh";
  public static final int SERVER_PORT = 4000;
  public static final int FREQUENCY = 1000;
  public static final long TIME_OUT = 20000L;
  public static final int CONNECT_COUNT = 5;
  public static final int BLADE_CONNECT_COUNT = 15;
  public static final int BLADE_AUTOCONNECT_COUNT = 300;
  public static final int CONNECT_TIME = 1000;
  public static final int BLADE_RECEIVE_TIME = 10000;
  public static final int BLADE_HEART_TIME = 5000;
  public static final int RAPMSG_CLOSE_TIME = 5000;
  public int imageWidth = 1024;
  public int imageHeight = 768;
  public boolean isDiv = false;
  public int sendNum = 0;
  public Vector<String> tabbedList = new Vector<String>();
  public static int libID = 0;
  public int count = 0;
  public long newConnTime = 0L;
  public long getWaitTime = 0L;
  private int hookNum = 0; public static final int INFRAME_WIDTH = 800; public static final int INFRAME_HEIGHT = 600; public static final int BIG_WIDTH = 1280; public static final int BIG_HEIGHT = 1024;
  public static final int WIDTH_800X600 = 800;
  public static final int HIGHT_800X600 = 600;
  public static final int WIDTH_640X480 = 640;
  public static final int HIGHT_640X480 = 480;
  public int getHookNum() {
    return this.hookNum;
  }
  public void setHookNum(int hookNum) {
    this.hookNum = hookNum;
  }
  public static final Color LIGHT_ON = Color.green;
  public static final Color LIGHT_OFF = Color.black;
  public static final String kvmResource = "com.kvm.resource.KVMResource";
  public static final int ZERO_FRAME = 0;
  public static final int ONE_FRAME = 1;
  public static final int THIRTY_FRAME = 35;
  public boolean isMstsc = false;
  public final Toolkit tk = Toolkit.getDefaultToolkit();
  public final Cursor myCursor = this.tk.createCustomCursor(this.tk.getImage("resource/images/cursor.GIF"), new Point(0, 0), "myCursor");
  public final Cursor defCursor = new Cursor(0);
  public static final short CRC_CHECKCODE = 0;
  public Hashtable<String, BladeThread> threadGroup = new Hashtable<String, BladeThread>();
  public boolean isLocalMouse = true;
  public void setImageWidth(int Widht) {
    this.imageWidth = Widht;
  }
  public void setImageHeight(int Height) {
    this.imageHeight = Height;
  }
  public static Dimension getScreenSize() {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }
  public Hashtable<Integer, Object> table = new Hashtable<Integer, Object>(); public static final String OSTA_SHELF_TYPE = "0"; public static final String CGA_SHELF_TYPE = "1"; public static final String OSTA = "OSTA"; public static final String ATCA = "ATCA"; public static final String CGA = "CGA";
  public static final int BLADE_NO_RACK = 1;
  public synchronized Object getLock(int slotNO) {
    Integer sNO = Integer.valueOf(slotNO);
    Object lock = this.table.get(sNO);
    if (null == lock) {
      lock = new Object();
      this.table.put(sNO, lock);
    } 
    return lock;
  }
  public String shelfType = "OSTA";
  public static boolean securevmm = false;
  public static boolean securekvm = false;
  public static final int sessionIdLen = 24;
  public static final int passKeyLen = 16;
  public static final int saltIVLen = 16;
  private static byte[] sessionID = null;
  private static byte[] kvmSecretKey = null;
  private static byte[] kvmSecretKeyBigEnd = null;
  private static byte[] vmmSecretKey = null;
  private static byte[] vmmSecretKeyBigEnd = null;
  private static byte[] kbdSecretKey = null;
  private static byte[] kbdSecretKeyBigEnd = null;
  public static byte[] invalidSessionID = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
  public static byte[] negotiatepass = null;
  public static byte[] negotiateiv = null; public static final int S_KEY_ESC = 1; public static final int S_KEY_1 = 2; public static final int S_KEY_2 = 3; public static final int S_KEY_3 = 4; public static final int S_KEY_4 = 5; public static final int S_KEY_5 = 6; public static final int S_KEY_6 = 7; public static final int S_KEY_7 = 8; public static final int S_KEY_8 = 9; public static final int S_KEY_9 = 10; public static final int S_KEY_0 = 11; public static final int S_KEY_MIN = 12; public static final int S_KEY_PLUS = 13; public static final int S_KEY_BACK = 14; public static final int S_KEY_TAB = 15; public static final int S_KEY_Q = 16; public static final int S_KEY_W = 17; public static final int S_KEY_E = 18; public static final int S_KEY_R = 19; public static final int S_KEY_T = 20; public static final int S_KEY_Y = 21; public static final int S_KEY_U = 22; public static final int S_KEY_I = 23; public static final int S_KEY_O = 24; public static final int S_KEY_P = 25; public static final int S_KEY_BR1 = 26; public static final int S_KEY_BR2 = 27; public static final int S_KEY_ENTER = 28; public static final int S_KEY_ENTERP = 28; public static final int S_KEY_CTRL = 29; public static final int S_KEY_RCTRL = 29; public static final int S_KEY_A = 30; public static final int S_KEY_S = 31; public static final int S_KEY_D = 32; public static final int S_KEY_F = 33; public static final int S_KEY_G = 34; public static final int S_KEY_H = 35; public static final int S_KEY_J = 36; public static final int S_KEY_K = 37; public static final int S_KEY_L = 38; public static final int S_KEY_SEMI = 39; public static final int S_KEY_QUOT = 40; public static final int S_KEY_QUOTO = 41; public static final int S_KEY_LSHIFT = 42; public static final int S_KEY_OR = 43; public static final int S_KEY_Z = 44; public static final int S_KEY_X = 45; public static final int S_KEY_C = 46;
  public static final int S_KEY_V = 47;
  public static final int S_KEY_B = 48;
  public static byte[] negotiatesalt = null; public static final int S_KEY_N = 49; public static final int S_KEY_M = 50; public static final int S_KEY_COMMA = 51; public static final int S_KEY_DOT = 52; public static final int S_KEY_DIV = 53; public static final int S_KEY_DIVP = 53; public static final int S_KEY_RSHIFT = 54; public static final int S_KEY_PRNT = 55; public static final int S_KEY_MUL = 55; public static final int S_KEY_ALT = 56; public static final int S_KEY_RALT = 56; public static final int S_KEY_SPACE = 57; public static final int S_KEY_CAPS = 58; public static final int S_KEY_F1 = 59; public static final int S_KEY_F2 = 60; public static final int S_KEY_F3 = 61; public static final int S_KEY_F4 = 62; public static final int S_KEY_F5 = 63; public static final int S_KEY_F6 = 64; public static final int S_KEY_F7 = 65; public static final int S_KEY_F8 = 66; public static final int S_KEY_F9 = 67; public static final int S_KEY_F10 = 68; public static final int S_KEY_BREAK = 69; public static final int S_KEY_NUMP = 69; public static final int S_KEY_SCROLL = 70; public static final int S_KEY_HOME = 71; public static final int S_KEY_7P = 71; public static final int S_KEY_UP = 72; public static final int S_KEY_8P = 72; public static final int S_KEY_PGUP = 73; public static final int S_KEY_9P = 73; public static final int S_KEY_MINP = 74; public static final int S_KEY_LEFT = 75; public static final int S_KEY_4P = 75; public static final int S_KEY_5P = 76; public static final int S_KEY_RIGHT = 77; public static final int S_KEY_6P = 77; public static final int S_KEY_PLUSP = 78; public static final int S_KEY_END = 79; public static final int S_KEY_1P = 79; public static final int S_KEY_DOWN = 80; public static final int S_KEY_2P = 80; public static final int S_KEY_PGDN = 81; public static final int S_KEY_3P = 81; public static final int S_KEY_INSERT = 82; public static final int S_KEY_0P = 82;
  public static final int S_KEY_DEL = 83;
  public static final int S_KEY_DELP = 83;
  public static final int S_KEY_OR_1 = 86;
  public static String toHex(byte[] array) {
    BigInteger bi = new BigInteger(1, array);
    String hex = bi.toString(16);
    int paddingLength = array.length * 2 - hex.length();
    if (paddingLength > 0)
    {
      return String.format("%0" + paddingLength + "d", new Object[] { Integer.valueOf(0) }) + hex;
    }
    return hex;
  }
  public static final int S_KEY_F11 = 87; public static final int S_KEY_F12 = 88; public static final int USB_KEY_A = 4; public static final int USB_KEY_B = 5; public static final int USB_KEY_C = 6; public static final int USB_KEY_D = 7; public static final int USB_KEY_E = 8; public static final int USB_KEY_F = 9; public static final int USB_KEY_G = 10; public static final int USB_KEY_H = 11; public static final int USB_KEY_I = 12; public static final int USB_KEY_J = 13; public static final int USB_KEY_K = 14; public static final int USB_KEY_L = 15; public static final int USB_KEY_M = 16; public static final int USB_KEY_N = 17; public static final int USB_KEY_O = 18; public static final int USB_KEY_P = 19; public static final int USB_KEY_Q = 20; public static final int USB_KEY_R = 21; public static final int USB_KEY_S = 22; public static final int USB_KEY_T = 23; public static final int USB_KEY_U = 24; public static final int USB_KEY_V = 25; public static final int USB_KEY_W = 26; public static final int USB_KEY_X = 27; public static final int USB_KEY_Y = 28; public static final int USB_KEY_Z = 29; public static final int USB_KEY_1 = 30; public static final int USB_KEY_2 = 31; public static final int USB_KEY_3 = 32; public static final int USB_KEY_4 = 33; public static final int USB_KEY_5 = 34; public static final int USB_KEY_6 = 35; public static final int USB_KEY_7 = 36; public static final int USB_KEY_8 = 37; public static final int USB_KEY_9 = 38; public static final int USB_KEY_0 = 39; public static final int USB_KEY_ENTER = 40; public static final int USB_KEY_ENTERP = 88; public static final int USB_KEY_ESC = 41; public static final int USB_KEY_BACK = 42; public static final int USB_KEY_TAB = 43; public static final int USB_KEY_SPACE = 44; public static final int USB_KEY_MIN = 45; public static final int USB_KEY_PLUS = 46; public static final int USB_KEY_BR1 = 47; public static final int USB_KEY_BR2 = 48; public static final int USB_KEY_OR = 49;
  public static final int USB_KEY_SEMI = 51;
  public static void initSessionIDAndKey(int userKey) {
    byte[] completeKey = null;
    try {
      completeKey = AESHandler.generateStoredPasswordHash(String.valueOf(userKey).toCharArray(), 72, negotiatesalt);
    }
    catch (Exception ex) {}
    sessionID = new byte[24];
    kvmSecretKey = new byte[16];
    kbdSecretKey = new byte[16];
    vmmSecretKey = new byte[16];
    System.arraycopy(completeKey, 0, sessionID, 0, 24);
    System.arraycopy(completeKey, 24, kvmSecretKey, 0, 16);
    System.arraycopy(completeKey, 40, kbdSecretKey, 0, 16);
    System.arraycopy(completeKey, 56, vmmSecretKey, 0, 16);
    kvmSecretKeyBigEnd = new byte[16];
    KVMUtil.perIntToByteCon(kvmSecretKeyBigEnd, 0, kvmSecretKey);
    kbdSecretKeyBigEnd = new byte[16];
    KVMUtil.perIntToByteCon(kbdSecretKeyBigEnd, 0, kbdSecretKey);
    vmmSecretKeyBigEnd = new byte[16];
    KVMUtil.perIntToByteCon(vmmSecretKeyBigEnd, 0, vmmSecretKey);
  }
  public static final int USB_KEY_QUOT = 52; public static final int USB_KEY_QUOTO = 53; public static final int USB_KEY_COMMA = 54; public static final int USB_KEY_DOT = 55; public static final int USB_KEY_DIV = 56; public static final int USB_KEY_CAPS = 57; public static final int USB_KEY_F1 = 58; public static final int USB_KEY_F2 = 59; public static final int USB_KEY_F3 = 60; public static final int USB_KEY_F4 = 61; public static final int USB_KEY_F5 = 62; public static final int USB_KEY_F6 = 63; public static final int USB_KEY_F7 = 64; public static final int USB_KEY_F8 = 65; public static final int USB_KEY_F9 = 66; public static final int USB_KEY_F10 = 67; public static final int USB_KEY_F11 = 68; public static final int USB_KEY_F12 = 69; public static final int USB_KEY_PRNT = 70; public static final int USB_KEY_SCROLL = 71; public static final int USB_KEY_BREAK = 72; public static final int USB_KEY_INSERT = 73; public static final int USB_KEY_HOME = 74; public static final int USB_KEY_PGUP = 75;
  public static final int USB_KEY_DEL = 76;
  public static byte[] getSessionID() {
    return sessionID;
  }
  public static final int USB_KEY_END = 77; public static final int USB_KEY_PGDN = 78; public static final int USB_KEY_RIGHT = 79; public static final int USB_KEY_LEFT = 80; public static final int USB_KEY_DOWN = 81; public static final int USB_KEY_UP = 82; public static final int USB_KEY_NUMP = 83; public static final int USB_KEY_DIVP = 84; public static final int USB_KEY_MULP = 85; public static final int USB_KEY_MINP = 86; public static final int USB_KEY_PLUSP = 87; public static final int USB_KEY_1P = 89; public static final int USB_KEY_2P = 90; public static final int USB_KEY_3P = 91; public static final int USB_KEY_4P = 92; public static final int USB_KEY_5P = 93; public static final int USB_KEY_6P = 94; public static final int USB_KEY_7P = 95; public static final int USB_KEY_8P = 96; public static final int USB_KEY_9P = 97; public static final int USB_KEY_0P = 98; public static final int USB_KEY_DELP = 99; public static final int USB_KEY_OR_1 = 100; public static final int USB_KEY_CTRL = 224; public static final int USB_KEY_SHIFT = 225; public static final int USB_KEY_ALT = 226;
  public static byte[] getKvmSecretKey() {
    return kvmSecretKey;
  }
  public static byte[] getKvmSecretKeyBigEnd() {
    return kvmSecretKeyBigEnd;
  }
  public static byte[] getVmmSecretKey() {
    return vmmSecretKey;
  }
  public static byte[] getVmmSecretKeyBigEnd() {
    return vmmSecretKeyBigEnd;
  }
  public static byte[] getKbdSecretKey() {
    return kbdSecretKey;
  }
  public static byte[] getKbdSecretKeyBigEnd() {
    return kbdSecretKeyBigEnd;
  }
  public static final int[][] KEY_MAP = new int[][] { { 1, 41 }, { 2, 30 }, { 3, 31 }, { 4, 32 }, { 5, 33 }, { 6, 34 }, { 7, 35 }, { 8, 36 }, { 9, 37 }, { 10, 38 }, { 11, 39 }, { 12, 45 }, { 13, 46 }, { 14, 42 }, { 15, 43 }, { 16, 20 }, { 17, 26 }, { 18, 8 }, { 19, 21 }, { 20, 23 }, { 21, 28 }, { 22, 24 }, { 23, 12 }, { 24, 18 }, { 25, 19 }, { 26, 47 }, { 27, 48 }, { 28, 40 }, { 29, 224 }, { 29, 224 }, { 30, 4 }, { 31, 22 }, { 32, 7 }, { 33, 9 }, { 34, 10 }, { 35, 11 }, { 36, 13 }, { 37, 14 }, { 38, 15 }, { 39, 51 }, { 40, 52 }, { 41, 53 }, { 42, 225 }, { 43, 49 }, { 44, 29 }, { 45, 27 }, { 46, 6 }, { 47, 25 }, { 48, 5 }, { 49, 17 }, { 50, 16 }, { 51, 54 }, { 52, 55 }, { 53, 56 }, { 53, 84 }, { 54, 225 }, { 55, 70 }, { 56, 226 }, { 56, 226 }, { 57, 44 }, { 58, 57 }, { 82, 73 }, { 83, 76 }, { 86, 100 } };
  public static boolean isProxy = false;
  public String parameterIP = "";
  public int parameterPort = 0;
  public static final String CONNECT_TYPE_PROXY = "proxy";
  public static final int PORT_PROXY = 31035;
  public static final int PORT_80 = 80;
  public static final String CONNECTION_RAP = "connection.fail.rap";
  public static final String CONNECTION_MSG = "connection.message";
  public static String MOUSE_LIB = "";
  public static final String MOUSE_LIB_EXT = ".dll";
  public static final String MOUSE_LINUX_LIB = "libkeyboard_encrypt";
  public static final String MOUSE_LINUX_LIB_32 = "libkeyboard_encrypt_32";
  public static final String MOUSE_LINUX_LIB_64 = "libkeyboard_encrypt_64";
  public static final String MOUSE_LINUX_LIB_EXT = ".so";
  public static final String OS_LINUX_X86_64 = "x86_64";
  public static final String OS_LINUX_I386 = "i386";
  public static String KEYBOARD_LIB = "";
  public static final String KEYBOARD_LIB_EXT = ".so";
}
