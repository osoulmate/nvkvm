package com.kvm;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
public class Base
{
  public static final String TYPE = "USB";
  private static long typeData = 0L;
  public static long getTypeData() {
    return typeData;
  }
  public static void setTypeData(long typeData) {
    Base.typeData = typeData;
  }
  private static int bladeSize = 14;
  private static ArrayList<Object> bladeList = new ArrayList(10);
  private static int connMode;
  private static String vmmConnIP;
  private static int vmmCodeKey;
  public static ArrayList<Object> getBladeList() {
    return bladeList;
  }
  public static String getVmmConnIP() {
    return vmmConnIP;
  }
  public static void setVmmConnIP(String vmmConnIP) {
    Base.vmmConnIP = vmmConnIP;
  }
  public static int getVmmCodeKey() {
    return vmmCodeKey;
  }
  public static void setVmmCodeKey(int vmmCodeKey) {
    Base.vmmCodeKey = vmmCodeKey;
  }
  private static boolean bvmmCodeKeyNego = false;
  private static int vmmPort = 0;
  public static int getVmmPort() {
    return vmmPort;
  }
  public static void setVmmPort(int vmmPort) {
    Base.vmmPort = vmmPort;
  }
  private static int compress = 0;
  public static int getCompress() {
    return compress;
  }
  public static void setCompress(int compress) {
    Base.compress = compress;
  }
  private static int vmm_compress_state = 0;
  private static byte[] kvm_key = null;
  public static byte[] getKvm_key() {
    if (null != kvm_key)
    {
      return (byte[])kvm_key.clone();
    }
    byte[] tmp = null;
    return tmp;
  }
  public static void setKvm_key(byte[] kvm_key) {
    if (null != kvm_key) {
      Base.kvm_key = (byte[])kvm_key.clone();
    }
    else {
      Base.kvm_key = null;
    } 
  }
  private static byte[] user_key = null;
  public static byte[] getUser_key() {
    if (null != user_key)
    {
      return (byte[])user_key.clone();
    }
    byte[] tmp = null;
    return tmp;
  }
  public static void setUser_key(byte[] user_key) {
    if (null != user_key) {
      Base.user_key = (byte[])user_key.clone();
    }
    else {
      Base.user_key = null;
    } 
  }
  private static byte[] user_iv = null;
  private static int privilege;
  public static byte[] getUser_iv() {
    if (null != user_iv)
    {
      return (byte[])user_iv.clone();
    }
    byte[] tmp = null;
    return tmp;
  }
  public static final int admin = 4;
  public static final int operator = 3;
  public static void setUser_iv(byte[] user_iv) {
    if (null != user_iv) {
      Base.user_iv = (byte[])user_iv.clone();
    }
    else {
      Base.user_iv = null;
    } 
  }
  public static final int user = 2;
  public static final int callback = 1;
  public static int getPrivilege() {
    return privilege;
  }
  public static void setPrivilege(int privilege) {
    Base.privilege = privilege;
  }
  private static int currentDqtSize = 7;
  private static int isStartVideo = 1;
  private static int isIFrame = 1;
  private static boolean isSynMouse = false;
  private static boolean isSingleMouse = false;
  public static final int englishKeyboardLayout = 1;
  public static final int japaneseKeyboardLayout = 2;
  public static final int frenchKeyboardLayout = 3;
  private static int keyboardLayout = 1;
  private static boolean isNewCompAlgorithm = true;
  public static boolean getIsNewCompAlgorithm() {
    return isNewCompAlgorithm;
  }
  private static int dqtzSize = 4;
  private static String local = "zh";
  public static final int CONNECT_COUNT = 5;
  public static final int BLADE_CONNECT_COUNT = 15;
  public static final int CONNECT_TIME = 1000;
  public static final int BLADE_RECEIVE_TIME = 10000;
  public static final int BLADE_HEART_TIME = 5000;
  public static final int RAPMSG_CLOSE_TIME = 5000;
  private int imageWidth = 1024;
  public int getImageWidth() {
    return this.imageWidth;
  }
  private int imageHeight = 768;
  public int getImageHeight() {
    return this.imageHeight;
  }
  private static int appletWidth = 800;
  public static int getAppletWidth() {
    return appletWidth;
  }
  private static int appletHeight = 600;
  public static int getAppletHeight() {
    return appletHeight;
  }
  private boolean isDiv = false;
  public boolean isDiv() {
    return this.isDiv;
  }
  public void setDiv(boolean isDiv) {
    this.isDiv = isDiv;
  }
  private Vector<Object> tabbedList = new Vector(10);
  public Vector<Object> getTabbedList() {
    return this.tabbedList;
  }
  public void setTabbedList(Vector<Object> tabbedList) {
    this.tabbedList = tabbedList;
  }
  private static int libID = 0;
  public static int getLibID() {
    return libID;
  }
  public static void setLibID(int libID) {
    Base.libID = libID;
  }
  private long newConnTime = 0L;
  public long getNewConnTime() {
    return this.newConnTime;
  }
  public void setNewConnTime(long newConnTime) {
    this.newConnTime = newConnTime;
  }
  private long getWaitTime = 0L;
  public long getGetWaitTime() {
    return this.getWaitTime;
  }
  public void setGetWaitTime(long getWaitTime) {
    this.getWaitTime = getWaitTime;
  }
  private int hookNum = 0;
  public static final int INFRAME_WIDTH = 800;
  public static final int INFRAME_HEIGHT = 600;
  public static final int BIG_WIDTH = 1920;
  public static final int BIG_HEIGHT = 1200;
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
  private boolean isMstsc = false;
  public boolean isMstsc() {
    return this.isMstsc;
  }
  public void setMstsc(boolean isMstsc) {
    this.isMstsc = isMstsc;
  }
  public final Toolkit tk = Toolkit.getDefaultToolkit();
  public final Cursor myCursor = this.tk.createCustomCursor(this.tk.getImage("resource/images/cursor.GIF"), new Point(0, 0), "myCursor");
  private final Cursor defCursor = new Cursor(0);
  public static final short CRC_CHECKCODE = 0;
  public Cursor getDefCursor() {
    return this.defCursor;
  }
  private Hashtable<Object, Object> threadGroup = new Hashtable<>(10);
  public Hashtable<Object, Object> getThreadGroup() {
    Hashtable<Object, Object> tmp = this.threadGroup;
    return tmp;
  }
  public void setThreadGroup(Hashtable<Object, Object> threadGroup) {
    if (null != threadGroup) {
      this.threadGroup = (Hashtable<Object, Object>)threadGroup.clone();
    }
    else {
      this.threadGroup = null;
    } 
  }
  public static void setStartVideo(int value) {
    isStartVideo = value;
  }
  public static int getStartVideo() {
    return isStartVideo;
  }
  public static Dimension getScreenSize() {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }
  private Hashtable<Integer, Object> table = new Hashtable<>(10);
  public static final int S_KEY_ESC = 1;
  protected synchronized Object getLock(int slotNO) {
    Integer sNO = Integer.valueOf(slotNO);
    Object lock = this.table.get(sNO);
    if (null == lock) {
      lock = new Object();
      this.table.put(sNO, lock);
    } 
    return lock;
  }
  public static final int S_KEY_1 = 2;
  public static final int S_KEY_2 = 3;
  public static final int S_KEY_3 = 4;
  public static final int S_KEY_4 = 5;
  public static final int S_KEY_5 = 6;
  public static final int S_KEY_6 = 7;
  public static final int S_KEY_7 = 8;
  public static final int S_KEY_8 = 9;
  public static final int S_KEY_9 = 10;
  public static final int S_KEY_0 = 11;
  public static final int S_KEY_MIN = 12;
  public static final int S_KEY_PLUS = 13;
  public static final int S_KEY_BACK = 14;
  public static final int S_KEY_TAB = 15;
  public static final int S_KEY_Q = 16;
  public static final int S_KEY_W = 17;
  public static final int S_KEY_E = 18;
  public static final int S_KEY_R = 19;
  public static final int S_KEY_T = 20;
  public static final int S_KEY_Y = 21;
  public static final int S_KEY_U = 22;
  public static final int S_KEY_I = 23;
  public static final int S_KEY_O = 24;
  public static final int S_KEY_P = 25;
  public static final int S_KEY_BR1 = 26;
  public static final int S_KEY_BR2 = 27;
  public static final int S_KEY_ENTER = 28;
  public static final int S_KEY_ENTERP = 28;
  public static final int S_KEY_CTRL = 29;
  public static final int S_KEY_RCTRL = 29;
  public static final int S_KEY_A = 30;
  public static final int S_KEY_S = 31;
  public static final int S_KEY_D = 32;
  public static final int S_KEY_F = 33;
  public static final int S_KEY_G = 34;
  public static final int S_KEY_H = 35;
  public static final int S_KEY_J = 36;
  public static final int S_KEY_K = 37;
  public static final int S_KEY_L = 38;
  public static final int S_KEY_SEMI = 39;
  public static final int S_KEY_QUOT = 40;
  public static final int S_KEY_QUOTO = 41;
  public static final int S_KEY_LSHIFT = 42;
  public static final int S_KEY_OR = 43;
  public static final int S_KEY_Z = 44;
  public static final int S_KEY_X = 45;
  public static final int S_KEY_C = 46;
  public static final int S_KEY_V = 47;
  public static final int S_KEY_B = 48;
  public static final int S_KEY_N = 49;
  public static final int S_KEY_M = 50;
  public static final int S_KEY_COMMA = 51;
  public static final int S_KEY_DOT = 52;
  public static final int S_KEY_DIV = 53;
  public static final int S_KEY_DIVP = 53;
  public static final int S_KEY_RSHIFT = 54;
  public static final int S_KEY_PRNT = 55;
  public static final int S_KEY_MUL = 55;
  public static final int S_KEY_ALT = 56;
  public static final int S_KEY_RALT = 56;
  public static final int S_KEY_SPACE = 57;
  public static final int S_KEY_CAPS = 58;
  public static final int S_KEY_F1 = 59;
  public static final int S_KEY_F2 = 60;
  public static final int S_KEY_F3 = 61;
  public static final int S_KEY_F4 = 62;
  public static final int S_KEY_F5 = 63;
  public static final int S_KEY_F6 = 64;
  public static final int S_KEY_F7 = 65;
  public static final int S_KEY_F8 = 66;
  public static final int S_KEY_F9 = 67;
  public static final int S_KEY_F10 = 68;
  public static final int S_KEY_BREAK = 69;
  public static final int S_KEY_NUMP = 69;
  public static final int S_KEY_SCROLL = 70;
  public static final int S_KEY_HOME = 71;
  public static final int S_KEY_7P = 71;
  public static final int S_KEY_UP = 72;
  public static final int S_KEY_8P = 72;
  public static final int S_KEY_PGUP = 73;
  public static final int S_KEY_9P = 73;
  public static final int S_KEY_MINP = 74;
  public static final int S_KEY_LEFT = 75;
  public static final int S_KEY_4P = 75;
  public static final int S_KEY_5P = 76;
  public static final int S_KEY_RIGHT = 77;
  public static final int S_KEY_6P = 77;
  public static final int S_KEY_PLUSP = 78;
  public static final int S_KEY_END = 79;
  public static final int S_KEY_1P = 79;
  public static final int S_KEY_DOWN = 80;
  public static final int S_KEY_2P = 80;
  public static final int S_KEY_PGDN = 81;
  public static final int S_KEY_3P = 81;
  public static final int S_KEY_INSERT = 82;
  public static final int S_KEY_0P = 82;
  public static final int S_KEY_DEL = 83;
  public static final int S_KEY_DELP = 83;
  public static final int S_KEY_OR_1 = 86;
  public static final int S_KEY_F11 = 87;
  public static final int S_KEY_F12 = 88;
  public static final int S_KEY_KANJI1 = 115;
  public static final int S_KEY_KANJI2 = 112;
  public static final int S_KEY_KANJI3 = 125;
  public static final int S_KEY_KANJI4 = 121;
  public static final int S_KEY_KANJI5 = 123;
  public static final int USB_KEY_A = 4;
  public static final int USB_KEY_B = 5;
  public static final int USB_KEY_C = 6;
  public static final int USB_KEY_D = 7;
  public static final int USB_KEY_E = 8;
  public static final int USB_KEY_F = 9;
  public static final int USB_KEY_G = 10;
  public static final int USB_KEY_H = 11;
  public static final int USB_KEY_I = 12;
  public static final int USB_KEY_J = 13;
  public static final int USB_KEY_K = 14;
  public static final int USB_KEY_L = 15;
  public static final int USB_KEY_M = 16;
  public static final int USB_KEY_N = 17;
  public static final int USB_KEY_O = 18;
  public static final int USB_KEY_P = 19;
  public static final int USB_KEY_Q = 20;
  public static final int USB_KEY_R = 21;
  public static final int USB_KEY_S = 22;
  public static final int USB_KEY_T = 23;
  public static final int USB_KEY_U = 24;
  public static final int USB_KEY_V = 25;
  public static final int USB_KEY_W = 26;
  public static final int USB_KEY_X = 27;
  public static final int USB_KEY_Y = 28;
  public static final int USB_KEY_Z = 29;
  public static final int USB_KEY_1 = 30;
  public static final int USB_KEY_2 = 31;
  public static final int USB_KEY_3 = 32;
  public static final int USB_KEY_4 = 33;
  public static final int USB_KEY_5 = 34;
  public static final int USB_KEY_6 = 35;
  public static final int USB_KEY_7 = 36;
  public static final int USB_KEY_8 = 37;
  public static final int USB_KEY_9 = 38;
  public static final int USB_KEY_0 = 39;
  public static final int USB_KEY_ENTER = 40;
  public static final int USB_KEY_ENTERP = 88;
  public static final int USB_KEY_ESC = 41;
  public static final int USB_KEY_BACK = 42;
  public static final int USB_KEY_TAB = 43;
  public static final int USB_KEY_SPACE = 44;
  public static final int USB_KEY_MIN = 45;
  public static final int USB_KEY_PLUS = 46;
  public static final int USB_KEY_BR1 = 47;
  public static final int USB_KEY_BR2 = 48;
  public static final int USB_KEY_OR = 49;
  public static final int USB_KEY_SEMI = 51;
  public static final int USB_KEY_QUOT = 52;
  public static final int USB_KEY_QUOTO = 53;
  public static final int USB_KEY_COMMA = 54;
  public static final int USB_KEY_DOT = 55;
  public static final int USB_KEY_DIV = 56;
  public static final int USB_KEY_CAPS = 57;
  public static final int USB_KEY_F1 = 58;
  public static final int USB_KEY_F2 = 59;
  public static final int USB_KEY_F3 = 60;
  public static final int USB_KEY_F4 = 61;
  public static final int USB_KEY_F5 = 62;
  public static final int USB_KEY_F6 = 63;
  public static final int USB_KEY_F7 = 64;
  public static final int USB_KEY_F8 = 65;
  public static final int USB_KEY_F9 = 66;
  public static final int USB_KEY_F10 = 67;
  public static final int USB_KEY_F11 = 68;
  public static final int USB_KEY_F12 = 69;
  public static final int USB_KEY_PRNT = 70;
  public static final int USB_KEY_SCROLL = 71;
  public static final int USB_KEY_BREAK = 72;
  public static final int USB_KEY_INSERT = 73;
  public static final int USB_KEY_HOME = 74;
  public static final int USB_KEY_PGUP = 75;
  public static final int USB_KEY_DEL = 76;
  public static final int USB_KEY_END = 77;
  public static final int USB_KEY_PGDN = 78;
  public static final int USB_KEY_RIGHT = 79;
  public static final int USB_KEY_LEFT = 80;
  public static final int USB_KEY_DOWN = 81;
  public static final int USB_KEY_UP = 82;
  public static final int USB_KEY_NUMP = 83;
  public static final int USB_KEY_DIVP = 84;
  public static final int USB_KEY_MULP = 85;
  public static final int USB_KEY_MINP = 86;
  public static final int USB_KEY_PLUSP = 87;
  public static final int USB_KEY_1P = 89;
  public static final int USB_KEY_2P = 90;
  public static final int USB_KEY_3P = 91;
  public static final int USB_KEY_4P = 92;
  public static final int USB_KEY_5P = 93;
  public static final int USB_KEY_6P = 94;
  public static final int USB_KEY_7P = 95;
  public static final int USB_KEY_8P = 96;
  public static final int USB_KEY_9P = 97;
  public static final int USB_KEY_0P = 98;
  public static final int USB_KEY_DELP = 99;
  public static final int USB_KEY_OR_1 = 100;
  public static final int USB_KEY_CTRL = 224;
  public static final int USB_KEY_SHIFT = 225;
  public static final int USB_KEY_ALT = 226;
  public static final int USB_KEY_KANJI1 = 135;
  public static final int USB_KEY_KANJI2 = 136;
  public static final int USB_KEY_KANJI3 = 137;
  public static final int USB_KEY_KANJI4 = 138;
  public static final int USB_KEY_KANJI5 = 139;
  private static int[][] KEY_MAP = new int[][] { { 1, 41 }, { 2, 30 }, { 3, 31 }, { 4, 32 }, { 5, 33 }, { 6, 34 }, { 7, 35 }, { 8, 36 }, { 9, 37 }, { 10, 38 }, { 11, 39 }, { 12, 45 }, { 13, 46 }, { 14, 42 }, { 15, 43 }, { 16, 20 }, { 17, 26 }, { 18, 8 }, { 19, 21 }, { 20, 23 }, { 21, 28 }, { 22, 24 }, { 23, 12 }, { 24, 18 }, { 25, 19 }, { 26, 47 }, { 27, 48 }, { 28, 40 }, { 29, 224 }, { 29, 224 }, { 30, 4 }, { 31, 22 }, { 32, 7 }, { 33, 9 }, { 34, 10 }, { 35, 11 }, { 36, 13 }, { 37, 14 }, { 38, 15 }, { 39, 51 }, { 40, 52 }, { 41, 53 }, { 42, 225 }, { 43, 49 }, { 44, 29 }, { 45, 27 }, { 46, 6 }, { 47, 25 }, { 48, 5 }, { 49, 17 }, { 50, 16 }, { 51, 54 }, { 52, 55 }, { 53, 56 }, { 53, 84 }, { 54, 225 }, { 55, 70 }, { 56, 226 }, { 56, 226 }, { 57, 44 }, { 58, 57 }, { 82, 73 }, { 83, 76 }, { 86, 100 }, { 115, 135 }, { 112, 136 }, { 125, 137 }, { 121, 138 }, { 123, 139 } };
  public static int[][] getKEY_MAP() {
    if (null != KEY_MAP)
    {
      return (int[][])KEY_MAP.clone();
    }
    int[][] tmp = (int[][])null;
    return tmp;
  }
  private static boolean isProxy = false;
  public static boolean getIsProxy() {
    return isProxy;
  }
  public static void setIsProxy(boolean isProxy) {
    Base.isProxy = isProxy;
  }
  private String parameterIP = "";
  public String getParameterIP() {
    return this.parameterIP;
  }
  public void setParameterIP(String parameterIP) {
    this.parameterIP = parameterIP;
  }
  private int parameterPort = 0; public static final String CONNECT_TYPE_PROXY = "proxy";
  public static final int PORT_PROXY = 0;
  public static final int PORT_80 = 0;
  public static final String CONNECTION_RAP = "connection.fail.rap";
  public static final String CONNECTION_MSG = "connection.message";
  public int getParameterPort() {
    return this.parameterPort;
  }
  public void setParameterPort(int parameterPort) {
    this.parameterPort = parameterPort;
  }
  private static String MOUSE_LIB = "";
  public static final String MOUSE_LIB_EXT = ".dll";
  public static final String MOUSE_LINUX_LIB = "libkeyboard_encrypt";
  public static final String MOUSE_LINUX_LIB_32 = "libkeyboard_encrypt_32";
  public static final String MOUSE_LINUX_LIB_64 = "libkeyboard_encrypt_64";
  public static final String MOUSE_LINUX_LIB_EXT = ".so";
  public static final String OS_LINUX_X86_64 = "x86_64";
  public static final String OS_LINUX_I386 = "i386";
  private static String KEYBOARD_LIB = "";
  public static final String KEYBOARD_LIB_EXT = ".so";
  public static int getBladeSize() {
    return bladeSize;
  }
  public static void setBladeSize(int nBladeSize) {
    bladeSize = nBladeSize;
  }
  public static int getConnMode() {
    return connMode;
  }
  public static void setConnMode(int nConnMode) {
    connMode = nConnMode;
  }
  public static boolean getBvmmCodeKeyNego() {
    return bvmmCodeKeyNego;
  }
  public static void setBvmmCodeKeyNego(boolean nBvmmCodeKeyNego) {
    bvmmCodeKeyNego = nBvmmCodeKeyNego;
  }
  public static int getDqtzSize() {
    return dqtzSize;
  }
  public static void setDqtzSize(int nDqtzSize) {
    dqtzSize = nDqtzSize;
  }
  public static boolean getIsSynMouse() {
    return isSynMouse;
  }
  public static void setIsSynMouse(boolean nIsSynMouse) {
    isSynMouse = nIsSynMouse;
  }
  public static int getKeyboardLayout() {
    return keyboardLayout;
  }
  public static void setKeyboardLayout(int keyboardLayout) {
    Base.keyboardLayout = keyboardLayout;
  }
  public static int getCurrentDqtSize() {
    return currentDqtSize;
  }
  public static void setCurrentDqtSize(int currentDqtSize) {
    Base.currentDqtSize = currentDqtSize;
  }
  public static int getIsIFrame() {
    return isIFrame;
  }
  public static void setIsIFrame(int isIFrame) {
    Base.isIFrame = isIFrame;
  }
  public static boolean isSingleMouse() {
    return isSingleMouse;
  }
  public static void setSingleMouse(boolean isSingleMouse) {
    Base.isSingleMouse = isSingleMouse;
  }
  public static String getKEYBOARD_LIB() {
    return KEYBOARD_LIB;
  }
  public static void setKEYBOARD_LIB(String kEYBOARD_LIB) {
    KEYBOARD_LIB = kEYBOARD_LIB;
  }
  public static String getLocal() {
    return local;
  }
  public static void setLocal(String local) {
    Base.local = local;
  }
  public static String getMOUSE_LIB() {
    return MOUSE_LIB;
  }
  public static void setMOUSE_LIB(String mOUSE_LIB) {
    MOUSE_LIB = mOUSE_LIB;
  }
  public static int getVmm_compress_state() {
    return vmm_compress_state;
  }
  public static void setVmm_compress_state(int vmm_compress_state) {
    Base.vmm_compress_state = vmm_compress_state;
  }
}
