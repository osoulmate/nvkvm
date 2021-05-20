package com.kvm;
import com.huawei.vm.console.utils.TestPrint;
import com.library.LibException;
import com.library.LoggerUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeListener;

//need
public class KVMUtil
{
  private byte[] bladePreInfo = new byte[2];
  public byte[] getBladePreInfo() {
    return (byte[])this.bladePreInfo.clone();
  }
  public void setBladePreInfo(byte[] bladePreInfo) {
    if (bladePreInfo != null) {
      this.bladePreInfo = (byte[])bladePreInfo.clone();
    }
    else {
      this.bladePreInfo = null;
    } 
  }
  private int times = 0;
  public int getTimes() {
    return this.times;
  }
  public void setTimes(int times) {
    this.times = times;
  }
  private boolean isConn = false;
  public boolean isConn() {
    return this.isConn;
  }
  public void setConn(boolean isConn) {
    this.isConn = isConn;
  }
  private byte number = 0;
  private byte tempNumber = 0;
  private boolean tempDiff = false;
  public boolean isTempDiff() {
    return this.tempDiff;
  }
  public void setTempDiff(boolean tempDiff) {
    this.tempDiff = tempDiff;
  }
  private boolean dispDiff = false;
  public boolean isDispDiff() {
    return this.dispDiff;
  }
  public void setDispDiff(boolean dispDiff) {
    this.dispDiff = dispDiff;
  }
  private int remoteX = 0;
  public int getRemoteX() {
    return this.remoteX;
  }
  public void setRemoteX(int remoteX) {
    this.remoteX = remoteX;
  }
  private int remoteY = 0;
  public int getRemoteY() {
    return this.remoteY;
  }
  public void setRemoteY(int remoteY) {
    this.remoteY = remoteY;
  }
  private byte colorBit = 0;
  public byte getColorBit() {
    return this.colorBit;
  }
  public void setColorBit(byte colorBit) {
    this.colorBit = colorBit;
  }
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private ArrayList<byte[]> diviBuff = (ArrayList)new ArrayList<>();
  public ArrayList<byte[]> getDiviBuff() {
    return this.diviBuff;
  }
  public void setDiviBuff(ArrayList<byte[]> diviBuff) {
    this.diviBuff = diviBuff;
  }
  private byte[] imageData = null;
  public byte[] getImageData() {
    return (byte[])this.imageData.clone();
  }
  public static final CCrc crc = new CCrc("CRC_16_H");
  private String resourcePath = "com.kvm.resource.KVMResource";
  public String getResourcePath() {
    return this.resourcePath;
  }
  public void setResourcePath(String resourcePath) {
    this.resourcePath = resourcePath;
  }
  private boolean resolutionCh = false;
  public boolean isResolutionCh() {
    return this.resolutionCh;
  }
  public void setResolutionCh(boolean resolutionCh) {
    this.resolutionCh = resolutionCh;
  }
  private boolean firstJudge = true;
  public boolean isFirstJudge() {
    return this.firstJudge;
  }
  public void setFirstJudge(boolean firstJudge) {
    this.firstJudge = firstJudge;
  }
  private ArrayList<ImagePane> imagePaneList = new ArrayList<>();
  private int imageWidth;
  private int imageHeight;
  public int getImageWidth() {
    return this.imageWidth;
  }
  public void setImageWidth(int imageWidth) {
    this.imageWidth = imageWidth;
  }
  public int getImageHeight() {
    return this.imageHeight;
  }
  public void setImageHeight(int imageHeight) {
    this.imageHeight = imageHeight;
  }
  private boolean diff = false;
  private Object[] bufferA = null;
  private Object[] bufferB = null;
  private int lenA = 0;
  private int lenB = 0;
  private byte nowDisplay = 0;
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private ArrayList<byte[]> resultDivi = (ArrayList)new ArrayList<>();
  public ArrayList<byte[]> getResultDivi() {
    return this.resultDivi;
  }
  public void setResultDivi(ArrayList<byte[]> resultDivi) {
    this.resultDivi = resultDivi;
  }
  private ResourceBundle bundle = null;
  private int packLenght = 0;
  private int packSum = 0;
  private int packTempSum = 0;
  private int packTempLenght = 0;
  private byte[] zipImageData = null;
  private BladeThread bladeThread = null;
  public BladeThread getBladeThread() {
    return this.bladeThread;
  }
  private static final int[][] keyCode = new int[][] { { 10, 40 }, { 8, 42 }, { 9, 43 }, { 3, 155 }, { 12, 156 }, { 16, 225 }, { 17, 224 }, { 18, 226 }, { 19, 72 }, { 20, 57 }, { 27, 41 }, { 32, 44 }, { 33, 75 }, { 34, 78 }, { 35, 77 }, { 36, 74 }, { 37, 80 }, { 38, 82 }, { 39, 79 }, { 40, 81 }, { 44, 54 }, { 45, 45 }, { 46, 55 }, { 47, 56 }, { 48, 39 }, { 49, 30 }, { 50, 31 }, { 51, 32 }, { 52, 33 }, { 53, 34 }, { 54, 35 }, { 55, 36 }, { 56, 37 }, { 57, 38 }, { 192, 53 }, { 59, 51 }, { 222, 52 }, { 61, 46 }, { 65, 4 }, { 66, 5 }, { 67, 6 }, { 68, 7 }, { 69, 8 }, { 70, 9 }, { 71, 10 }, { 72, 11 }, { 73, 12 }, { 74, 13 }, { 75, 14 }, { 76, 15 }, { 77, 16 }, { 78, 17 }, { 79, 18 }, { 80, 19 }, { 81, 20 }, { 82, 21 }, { 83, 22 }, { 84, 23 }, { 85, 24 }, { 86, 25 }, { 87, 26 }, { 88, 27 }, { 89, 28 }, { 90, 29 }, { 96, 98 }, { 97, 89 }, { 98, 90 }, { 99, 91 }, { 100, 92 }, { 101, 93 }, { 102, 94 }, { 103, 95 }, { 104, 96 }, { 105, 97 }, { 106, 85 }, { 107, 87 }, { 109, 86 }, { 110, 99 }, { 111, 84 }, { 127, 76 }, { 144, 83 }, { 145, 71 }, { 112, 58 }, { 113, 59 }, { 114, 60 }, { 115, 61 }, { 116, 62 }, { 117, 63 }, { 118, 64 }, { 119, 65 }, { 120, 66 }, { 121, 67 }, { 122, 68 }, { 123, 69 }, { 154, 70 }, { 155, 73 }, { 91, 47 }, { 92, 49 }, { 93, 48 }, { 525, 101 }, { 263, 138 }, { 243, 53 }, { 244, 53 }, { 240, 57 }, { 242, 136 }, { 245, 136 }, { 28, 138 }, { 29, 139 } };
  private static final int[][] keyCodeforJapan = new int[][] { { 10, 40 }, { 8, 42 }, { 9, 43 }, { 3, 155 }, { 16, 225 }, { 17, 224 }, { 18, 226 }, { 19, 72 }, { 20, 57 }, { 27, 41 }, { 32, 44 }, { 33, 75 }, { 34, 78 }, { 35, 77 }, { 36, 74 }, { 37, 80 }, { 38, 82 }, { 39, 79 }, { 40, 81 }, { 44, 54 }, { 45, 45 }, { 46, 55 }, { 47, 56 }, { 48, 39 }, { 49, 30 }, { 50, 31 }, { 51, 32 }, { 52, 33 }, { 53, 34 }, { 54, 35 }, { 55, 36 }, { 56, 37 }, { 57, 38 }, { 59, 51 }, { 61, 46 }, { 65, 4 }, { 66, 5 }, { 67, 6 }, { 68, 7 }, { 69, 8 }, { 70, 9 }, { 71, 10 }, { 72, 11 }, { 73, 12 }, { 74, 13 }, { 75, 14 }, { 76, 15 }, { 77, 16 }, { 78, 17 }, { 79, 18 }, { 80, 19 }, { 81, 20 }, { 82, 21 }, { 83, 22 }, { 84, 23 }, { 85, 24 }, { 86, 25 }, { 87, 26 }, { 88, 27 }, { 89, 28 }, { 90, 29 }, { 96, 98 }, { 97, 89 }, { 98, 90 }, { 99, 91 }, { 100, 92 }, { 101, 93 }, { 102, 94 }, { 103, 95 }, { 104, 96 }, { 105, 97 }, { 106, 85 }, { 107, 87 }, { 109, 86 }, { 110, 99 }, { 111, 84 }, { 127, 76 }, { 144, 83 }, { 145, 71 }, { 112, 58 }, { 113, 59 }, { 114, 60 }, { 115, 61 }, { 116, 62 }, { 117, 63 }, { 118, 64 }, { 119, 65 }, { 120, 66 }, { 121, 67 }, { 122, 68 }, { 123, 69 }, { 154, 70 }, { 155, 73 }, { 91, 48 }, { 93, 49 }, { 525, 101 }, { 263, 138 }, { 243, 53 }, { 244, 53 }, { 240, 57 }, { 242, 136 }, { 245, 136 }, { 28, 138 }, { 29, 139 }, { 512, 47 }, { 514, 46 }, { 513, 52 } };
  private KVMInterface kvmInterface = null;
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  private int start = 0;
  public int getStart() {
    return this.start;
  }
  public void setStart(int start) {
    this.start = start;
  }
  private int state = 0;
  private int resultStart = 0, head = 0;
  private int dlen = 0; private int rdlen = 0;
  private byte[] result = null;
  public byte[] getResult() {
    return (byte[])this.result.clone();
  }
  public void setResult(byte[] result) {
    if (result != null) {
      this.result = (byte[])result.clone();
    }
    else {
      this.result = null;
    } 
  }
  private long startTime = 0L;
  private int iWindosFocus = 0;
  public int getiWindosFocus() {
    return this.iWindosFocus;
  }
  public void setiWindosFocus(int iWindosFocus) {
    this.iWindosFocus = iWindosFocus;
  }
  public void setKvmInterface(KVMInterface kvmInterface2) {
    if (kvmInterface2 != null) {
      this.kvmInterface = kvmInterface2;
    }
    else {
      this.kvmInterface = null;
    } 
  }
  public void setUnPack(UnPackData unPack) {}
  public void setImageData(byte[] imageData) {
    if (null != imageData) {
      this.imageData = (byte[])imageData.clone();
    }
    else {
      this.imageData = null;
    } 
  }
  public static int javaCodeToUSB(KeyEvent e) {
    int temp = 0;
    int keyValue = 0;
    if (e.getKeyLocation() == 4 && numKey(e) != 0) {
      keyValue = numKey(e);
    }
    else {
      keyValue = e.getKeyCode();
    } 
    if (Base.getKeyboardLayout() == 2) {
      if (keyValue == 92) {
        if (e.getKeyChar() == '_')
        {
          temp = 135;
        }
        else
        {
          temp = 137;
        }
      }
      else if (keyValue == 48 && e.getKeyChar() == '~') {
        temp = 0;
      }
      else {
        for (int i = 0; i < keyCodeforJapan.length; i++)
        {
          if (keyCodeforJapan[i][0] == keyValue)
          {
            temp = keyCodeforJapan[i][1];
          }
        }
      } 
    } else {
      for (int i = 0; i < keyCode.length; i++) {
        if (keyCode[i][0] == keyValue)
        {
          temp = keyCode[i][1];
        }
      } 
    } 
    return temp;
  }
  private static int numKey(KeyEvent e) {
    int keyValue = 0;
    switch (e.getKeyCode())
    { case 155:
        keyValue = 96;
        return keyValue;case 127: keyValue = 110; return keyValue;case 35: keyValue = 97; return keyValue;case 40: case 225: keyValue = 98; return keyValue;case 34: keyValue = 99; return keyValue;case 37: case 226: keyValue = 100; return keyValue;case 12: case 65368: keyValue = 101; return keyValue;case 39: case 227: keyValue = 102; return keyValue;case 36: keyValue = 103; return keyValue;case 38: case 224: keyValue = 104; return keyValue;case 33: keyValue = 105; return keyValue; }  keyValue = 0; return keyValue;
  }
  public static int javaCodeToUSB(int keycode) {
    int temp = 0;
    for (int i = 0; i < keyCode.length; i++) {
      if (keyCode[i][0] == keycode)
      {
        temp = keyCode[i][1];
      }
    } 
    return temp;
  }
  public static int usbToJavaCode(int USBCode) {
    int temp = 0;
    for (int i = 0; i < keyCode.length; i++) {
      if (keyCode[i][1] == USBCode)
      {
        temp = keyCode[i][0];
      }
    } 
    return temp;
  }
  public static int byteToInt(byte[] bytesrc, int offset, int length) {
    int intdes = 0;
    for (int i = offset + length - 1; i >= offset; i--)
    {
      intdes = (intdes << 8) + (bytesrc[i] & 0xFF);
    }
    return intdes;
  }
  public static int byteToIntCon(byte[] bytesrc, int offset, int length) {
    int intdes = 0;
    try {
      for (int i = offset; i <= offset + length - 1; i++)
      {
        intdes = (intdes << 8) + (bytesrc[i] & 0xFF);
      }
    }
    catch (ArrayIndexOutOfBoundsException e) {
      intdes = 0;
    } 
    return intdes;
  }
  public static void intToByte(byte[] bytedest, int offset, int intsrc) {
    bytedest[offset + 0] = (byte)intsrc;
    bytedest[offset + 1] = (byte)(intsrc >> 8);
    bytedest[offset + 2] = (byte)(intsrc >> 16);
    bytedest[offset + 3] = (byte)(intsrc >> 24);
  }
  public static void intToByteCon(byte[] bytedest, int offset, int intsrc) {
    bytedest[offset + 3] = (byte)intsrc;
    bytedest[offset + 2] = (byte)(intsrc >> 8);
    bytedest[offset + 1] = (byte)(intsrc >> 16);
    bytedest[offset + 0] = (byte)(intsrc >> 24);
  }
  public static int[] perBitToInt(byte bytesrc) {
    int[] intdes = new int[8];
    int mask = 1;
    for (int i = 0; i < 8; i++) {
      intdes[i] = ((bytesrc & mask) == mask) ? 1 : 0;
      mask *= 2;
    } 
    return intdes;
  }
  public JButton createBladeButton(int bladeNO) {
    String imagesRes = "resource/images/blade1.gif";
    imagesRes = "resource/images/blade" + bladeNO + ".gif";
    JButton blade = new JButton(new ImageIcon(KVMUtil.class.getResource(imagesRes.trim())));
    return blade;
  }
  public static void translate(byte[] data) {
    for (int i = 0; i < data.length; i++) {
      byte red = data[i];
      byte green = data[i];
      byte blue = data[i];
      red = (byte)(red << 6 & 0xC0);
      green = (byte)(green << 1 & 0x38);
      blue = (byte)(blue >>> 5 & 0x6);
      data[i] = (byte)(red | green | blue);
    } 
  }
  private void swap() {
    this.bufferA = this.bufferB;
    this.bufferB = null;
    this.number = this.tempNumber;
    this.packLenght = this.packTempLenght;
    this.packSum = this.packTempSum;
    this.diff = this.tempDiff;
    this.tempNumber = 0;
    this.packTempLenght = 0;
    this.packTempSum = 0;
    this.tempDiff = false;
  }
  public static void doSort(LinkedList<byte[]> list) {
    for (int i = 0; i < list.size(); i++) {
      byte[] bytesA = list.get(i);
      int tempA = byteToIntCon(bytesA, 0, 2);
      int lSize = list.size();
      for (int j = i + 1; j < lSize; j++) {
        byte[] bytesB = list.get(j);
        int tempB = byteToIntCon(bytesB, 0, 2);
        if (tempA > tempB) {
          byte[] tmp = bytesA;
          bytesA = bytesB;
          bytesB = tmp;
          list.set(i, bytesA);
          list.set(j, bytesB);
          tempA = tempB;
        } 
      } 
    } 
  }
  public byte[] combine(Object[] list, boolean judge) {
    int length = 0;
    int index = 1;
    if (judge) {
      length = this.packLenght + 1;
    }
    else {
      length = this.packTempLenght + 1;
    } 
    byte[] data = new byte[length];
    byte[] buf = (byte[])list[0];
    int[] temp = perBitToInt(buf[7]);
    byte k = (byte)temp[7];
    StringBuffer tem = new StringBuffer(); int i;
    for (i = 6; i >= 0; i--)
    {
      tem.append(temp[i]);
    }
    temp = perBitToInt(buf[8]);
    for (i = 7; i >= 0; i--)
    {
      tem.append(temp[i]);
    }
    if (this.imageWidth != Integer.parseInt(tem.toString(), 2) || this.imageHeight != byteToIntCon(buf, 9, 2)) {
      this.imageWidth = Integer.parseInt(tem.toString(), 2);
      this.imageHeight = byteToIntCon(buf, 9, 2);
      this.resolutionCh = true;
    } 
    this.remoteX = byteToIntCon(buf, 12, 2);
    this.remoteY = byteToIntCon(buf, 14, 2);
    this.colorBit = buf[16];
    if (Base.getIsNewCompAlgorithm()) {
      data[0] = k;
      for (i = 1; i < list.length; i++)
      {
        byte[] bytes0 = (byte[])list[i];
        int temLen = bytes0.length - 3;
        System.arraycopy(bytes0, 3, data, index, temLen);
        index += temLen;
      }
    }
    else {
      for (i = 1; i < list.length; i++) {
        byte[] bytes0 = (byte[])list[i];
        int temLen = bytes0.length - 3;
        System.arraycopy(bytes0, 3, data, index, temLen);
        index += temLen;
      } 
    } 
    return data;
  }
  public boolean xorData(byte[] dataA, byte[] dataB) {
    boolean flage = false;
    if (dataA.length != dataB.length) {
      Debug.printExc("dataA = " + dataA.length + " dataB = " + dataB.length);
    }
    else {
      int len = 0;
      len = dataB.length;
      for (int i = 0; i < len; i++)
      {
        dataB[i] = (byte)(dataA[i] ^ dataB[i]);
      }
      flage = true;
    } 
    return flage;
  }
  private void setVar(byte[] bytes, boolean prem) {
    if (prem) {
      this.number = bytes[2];
      this.packLenght = byteToIntCon(bytes, 3, 4);
      this.lenA = this.packLenght / 220 + 1;
      if (this.packLenght % 220 != 0)
      {
        this.lenA++;
      }
      this.bufferA = new Object[this.lenA];
      try {
        int[] temp = perBitToInt(bytes[7]);
        if (1 == temp[7])
        {
          this.diff = true;
        }
        else
        {
          this.diff = false;
        }
      }
      catch (ArrayIndexOutOfBoundsException e) {
        this.diff = false;
      }
    } else {
      this.tempNumber = bytes[2];
      this.packTempLenght = byteToIntCon(bytes, 3, 4);
      this.lenB = this.packTempLenght / 220 + 1;
      if (this.packTempLenght % 220 != 0)
      {
        this.lenB++;
      }
      this.bufferB = new Object[this.lenB];
      try {
        int[] temp = perBitToInt(bytes[7]);
        if (1 == temp[7])
        {
          this.tempDiff = true;
        }
        else
        {
          this.tempDiff = false;
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        this.tempDiff = false;
      } 
    } 
  }
  public void resetBuf() {
    this.bufferA = null;
    this.bufferB = null;
    this.packLenght = 0;
    this.packSum = 0;
    this.packTempLenght = 0;
    this.packTempSum = 0;
    this.diff = false;
    this.tempDiff = false;
  }
  private void sentIFrame(int bladeNo, BladeCommu bladeCommu) {
    if (this.startTime == 0L) {
      this.startTime = System.currentTimeMillis();
    }
    else if (System.currentTimeMillis() - this.startTime > 500L) {
      bladeCommu.sentData(this.kvmInterface.getPackData().resendData(bladeNo));
      this.startTime = 0L;
    } 
  }
  private boolean setPremBuffer(int bladeNo, BladeCommu bladeCommu) {
    boolean flage = false;
    if (this.diff) {
      if (this.firstJudge) {
        sentIFrame(bladeNo, bladeCommu);
        resetBuf();
        return flage;
      } 
      if ((byte)(this.nowDisplay + 1) == this.number)
      {
        this.zipImageData = combine(this.bufferA, true);
        this.nowDisplay = this.number;
        this.dispDiff = this.diff;
        swap();
        flage = true;
      }
      else
      {
        resetBuf();
        sentIFrame(bladeNo, bladeCommu);
      }
    }
    else {
      this.zipImageData = combine(this.bufferA, true);
      this.nowDisplay = this.number;
      this.dispDiff = this.diff;
      swap();
      if (this.firstJudge)
      {
        this.firstJudge = false;
      }
      flage = true;
    } 
    return flage;
  }
  private boolean setMinorBuffer(byte[] bytes, int bladeNo, BladeCommu bladeCommu) {
    boolean judge = false;
    if (this.bufferB != null) {
      if (this.tempNumber == bytes[2]) {
        int tem = byteToIntCon(bytes, 0, 2);
        if (tem >= this.lenB) {
          resetBuf();
          sentIFrame(bladeNo, bladeCommu);
          return false;
        } 
        if (this.bufferB[tem] == null) {
          this.bufferB[tem] = bytes;
          this.packTempSum += bytes.length - 3;
        }
        else {
          byte[] old = (byte[])this.bufferB[tem];
          if (!Arrays.equals(old, bytes)) {
            Debug.println("Number is equals, but data no!");
            resetBuf();
            sentIFrame(bladeNo, bladeCommu);
            return false;
          } 
        } 
        if (this.packTempSum == this.packTempLenght)
        {
          if (this.tempDiff)
          {
            if (this.firstJudge) {
              sentIFrame(bladeNo, bladeCommu);
              resetBuf();
              return false;
            } 
            if ((byte)(this.nowDisplay + 1) == this.tempNumber)
            {
              this.zipImageData = combine(this.bufferB, false);
              this.nowDisplay = this.tempNumber;
              this.dispDiff = this.tempDiff;
              resetBuf();
              judge = true;
            }
            else
            {
              resetBuf();
              sentIFrame(bladeNo, bladeCommu);
            }
          }
          else
          {
            this.zipImageData = combine(this.bufferB, false);
            this.nowDisplay = this.tempNumber;
            this.dispDiff = this.tempDiff;
            resetBuf();
            if (this.firstJudge)
            {
              this.firstJudge = false;
            }
            judge = true;
          }
        }
      } else {
        resetBuf();
        sentIFrame(bladeNo, bladeCommu);
      }
    }
    else if (byteToIntCon(bytes, 0, 2) == 0) {
      setVar(bytes, false);
      this.bufferB[0] = bytes;
    }
    else {
      sentIFrame(bladeNo, bladeCommu);
    } 
    return judge;
  }
  public boolean isComplete(byte[] bytes, int bladeNo) {
    BladeThread bladeThread = (BladeThread)this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(bladeNo));
    if (bladeThread == null)
    {
      return false;
    }
    BladeCommu bladeCommu = bladeThread.getBladeCommu();
    boolean isComp = false;
    if (this.bufferA != null) {
      if (this.number == bytes[2])
      {
        int tem = byteToIntCon(bytes, 0, 2);
        if (tem >= this.lenA) {
          resetBuf();
          sentIFrame(bladeNo, bladeCommu);
          return false;
        } 
        if (this.bufferA[tem] == null) {
          this.bufferA[tem] = bytes;
          this.packSum += bytes.length - 3;
        }
        else {
          byte[] old = (byte[])this.bufferA[tem];
          if (!Arrays.equals(old, bytes)) {
            resetBuf();
            sentIFrame(bladeNo, bladeCommu);
            return false;
          } 
        } 
        if (this.packSum == this.packLenght) {
          if (setPremBuffer(bladeNo, bladeCommu))
          {
            isComp = true;
          }
          else
          {
            isComp = false;
          }
        } else {
          isComp = false;
        }
      }
      else if (setMinorBuffer(bytes, bladeNo, bladeCommu))
      {
        isComp = true;
      }
      else
      {
        isComp = false;
      }
    }
    else {
      if (byteToIntCon(bytes, 0, 2) == 0) {
        setVar(bytes, true);
        this.bufferA[0] = bytes;
      }
      else if (this.nowDisplay < bytes[2]) {
        int tem = bytes[2] - this.nowDisplay;
        if (tem < 128 && tem > 0)
        {
          sentIFrame(bladeNo, bladeCommu);
        }
      }
      else {
        int tem = this.nowDisplay - bytes[2];
        if (tem >= 128 || tem < 0)
        {
          sentIFrame(bladeNo, bladeCommu);
        }
      } 
      isComp = false;
    } 
    return isComp;
  }
  public byte[] getZipImageData() throws LibException {
    if (this.zipImageData != null)
    {
      return (byte[])this.zipImageData.clone();
    }
    throw new LibException("zipImageData is null");
  }
  public static int[][] transform(byte[] bytes) {
    int[][] bgrData = new int[bytes.length][3];
    for (int i = 0; i < bytes.length; i++) {
      bgrData[i][0] = (bgrData[i][0] << 8) + (bytes[i] & 0xC0);
      bgrData[i][1] = (bgrData[i][1] << 8) + (bytes[i] & 0x38) << 2;
      bgrData[i][2] = (bgrData[i][2] << 8) + (bytes[i] & 0x7) << 5;
    } 
    return bgrData;
  }
  public ArrayList<byte[]> diviStream(byte[] bytes, byte period1, byte period2) {
    if ("UDP".equals("TCP")) {
      this.resultDivi.clear();
      int packageLen = bytes.length;
      int dataLen = packageLen - 4;
      byte[] crcData = new byte[dataLen - 2];
      System.arraycopy(bytes, 6, crcData, 0, dataLen - 2);
      int returnCheck = crc.wCrc((short)0, crcData, (short)(dataLen - 2));
      byte[] tem = new byte[4];
      intToByte(tem, 0, returnCheck);
      if (tem[0] == bytes[5] && tem[1] == bytes[4]) {
        if (bytes[6] == 2 || bytes[6] == 4) {
          try {
            this.kvmInterface.getClient().sentData(this.kvmInterface.getPackData().replayToSMM(bytes[7], bytes[1]));
          }
          catch (LibException e) {
            if ("IO_ERRCODE".equals(e.getErrCode()))
            {
              JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface.getKvmUtil()
                  .getString("Network_interrupt_message"));
            }
          } 
        }
        byte[] data = new byte[packageLen - 2];
        System.arraycopy(bytes, 2, data, 0, packageLen - 2);
        this.resultDivi.add(data);
      }
      else {
        Debug.printByte(bytes);
      } 
    } 
    if ("TCP".equals("TCP"))
    {
      if (this.diviBuff.size() == 0) {
        this.resultDivi = doDivi(bytes, period1, period2);
      }
      else {
        byte[] temp = this.diviBuff.remove(0);
        byte[] data = new byte[temp.length + bytes.length];
        System.arraycopy(temp, 0, data, 0, temp.length);
        System.arraycopy(bytes, 0, data, temp.length, bytes.length);
        this.resultDivi = doDivi(data, period1, period2);
      } 
    }
    return this.resultDivi;
  }
  private ArrayList<byte[]> doDivi(byte[] bytes, byte period1, byte period2) {
    @SuppressWarnings({ "unchecked", "rawtypes" })
	ArrayList<byte[]> resultDo = (ArrayList)new ArrayList<>();
    if (bytes.length < 3) {
      this.diviBuff.clear();
      this.diviBuff.add(bytes);
      return resultDo;
    } 
    int start = 0;
    byte[] tem = new byte[4];
    byte[] lenData = new byte[2];
    boolean flag = false;
    for (int j = 0; j < bytes.length; j++) {
      if (bytes.length - start < 3) {
        byte[] remBytes = new byte[bytes.length - start];
        System.arraycopy(bytes, start, remBytes, 0, remBytes.length);
        this.diviBuff.add(remBytes);
        return resultDo;
      } 
      if (bytes[j] == period1 && bytes[j + 1] == period2 && bytes[j + 2] == 0 && j < bytes.length) {
        start = j;
        int i = start;
        while (i < bytes.length) {
          if (bytes.length >= start + 4) {
            lenData[0] = bytes[start + 2];
            lenData[1] = bytes[start + 3];
            int expectLen = byteToIntCon(lenData, 0, 2);
            if (bytes.length - start - 4 >= expectLen) {
              if (expectLen < 2) {
                this.diviBuff.clear();
                start = start + 4 + expectLen;
                j = start;
                flag = false;
                break;
              } 
              byte[] crcData = new byte[expectLen - 2];
              System.arraycopy(bytes, start + 6, crcData, 0, crcData.length);
              int returnCheck = crc.wCrc((short)0, crcData, (short)crcData.length);
              intToByte(tem, 0, returnCheck);
              i = start + expectLen + 4;
              if (tem[0] == bytes[start + 5] && tem[1] == bytes[start + 4]) {
                byte[] data = new byte[expectLen + 2];
                System.arraycopy(bytes, start + 2, data, 0, data.length);
                resultDo.add(data);
                start = i;
                j = i;
                if (i == bytes.length - 1) {
                  flag = false;
                  continue;
                } 
                flag = true;
                continue;
              } 
              this.diviBuff.clear();
              start = i;
              j = start;
              flag = false;
              continue;
            } 
            flag = true;
            j = bytes.length;
            break;
          } 
          flag = true;
          j = bytes.length;
        } 
        if (flag) {
          byte[] temp = new byte[bytes.length - start];
          System.arraycopy(bytes, start, temp, 0, temp.length);
          this.diviBuff.clear();
          this.diviBuff.add(temp);
        } 
      } 
    } 
    return resultDo;
  }
  public ArrayList<byte[]> diviStreamNew(byte[] bytes, byte period1, byte period2) {
    if (this.diviBuff.size() == 0) {
      this.resultDivi = doDiviNew(bytes, period1, period2);
    }
    else {
      byte[] temp = this.diviBuff.get(0);
      byte[] data = new byte[temp.length + bytes.length];
      System.arraycopy(temp, 0, data, 0, temp.length);
      System.arraycopy(bytes, 0, data, temp.length, bytes.length);
      this.resultDivi = doDiviNew(data, period1, period2);
    } 
    return this.resultDivi;
  }
  private ArrayList<byte[]> doDiviNew(byte[] bytes, byte period1, byte period2) {
    @SuppressWarnings({ "unchecked", "rawtypes" })
	ArrayList<byte[]> resultDo = (ArrayList)new ArrayList<>();
    if (bytes.length < 3) {
      this.diviBuff.clear();
      this.diviBuff.add(bytes);
      return resultDo;
    } 
    int start = 0;
    byte[] lenData = new byte[2];
    boolean flag = false;
    for (int j = 0; j < bytes.length; j++) {
      if (bytes.length - start < 3) {
        byte[] remBytes = new byte[bytes.length - start];
        System.arraycopy(bytes, start, remBytes, 0, remBytes.length);
        this.diviBuff.add(remBytes);
        return resultDo;
      } 
      if (bytes[j] == period1 && bytes[j + 1] == period2 && bytes[j + 2] == 0 && j < bytes.length) {
        int i = start;
        while (i < bytes.length) {
          if (bytes.length >= start + 4) {
            lenData[0] = bytes[start + 2];
            lenData[1] = bytes[start + 3];
            int expectLen = byteToIntCon(lenData, 0, 2);
            if (bytes.length - start - 4 >= expectLen) {
              if (expectLen < 3) {
                this.diviBuff.clear();
                start = start + 4 + expectLen;
                j = start;
                flag = false;
                break;
              } 
              i = start + expectLen + 4;
              byte[] data = new byte[expectLen + 2];
              System.arraycopy(bytes, start + 2, data, 0, data.length);
              resultDo.add(data);
              start = i;
              j = start;
              if (i == bytes.length - 1) {
                flag = false;
                continue;
              } 
              flag = true;
              continue;
            } 
            j = bytes.length;
            flag = true;
            break;
          } 
          j = bytes.length;
          flag = true;
        } 
        if (flag) {
          byte[] temp = new byte[bytes.length - start];
          System.arraycopy(bytes, start, temp, 0, temp.length);
          this.diviBuff.clear();
          this.diviBuff.add(temp);
        } 
      } 
    } 
    return resultDo;
  }
  public boolean diviStreamNew(byte[] bytes, boolean isNew) {
    while (this.start < bytes.length) {
      switch (this.state) {
        case 0:
          this.head <<= 8;
          this.head += bytes[this.start++] & 0xFF;
          if ((this.head & 0xFFFFFF) == 16709120)
          {
            this.state = 1;
          }
          continue;
        case 1:
          this.dlen = bytes[this.start++] & 0xFF;
          if (this.dlen < 3 || this.dlen > 250) {
            this.state = 0;
            this.head = this.dlen;
            continue;
          } 
          this.rdlen = 0;
          this.resultStart = 0;
          this.result = new byte[this.dlen];
          this.state = 2;
          continue;
        case 2:
          this.result[this.resultStart++] = bytes[this.start++];
          this.rdlen++;
          if (this.rdlen >= this.dlen) {
            this.state = 0;
            this.head = 0;
            return true;
          } 
          continue;
      } 
      this.state = 0;
      this.head = 0;
    } 
    return false;
  }
  public String getString(String key) {
    String value = null;
    if (null == key) {
      value = "key is null";
      return value;
    } 
    try {
      value = getResourceBundle().getString(key);
    }
    catch (MissingResourceException e) {
      LoggerUtil.error(e.getClass().getName());
      value = "Could not find resource: " + key + "  ";
    }
    catch (ClassCastException e2) {
      LoggerUtil.error(e2.getClass().getName());
      value = "key is not a string";
    } 
    return value;
  }
  private void updateResource(Locale locale) {
    if ("ja".equalsIgnoreCase(Base.getLocal())) {
      this
        .bundle = ResourceBundle.getBundle(this.resourcePath, new Locale("ja"), 
          ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }
    else if (locale != null || "zh".equalsIgnoreCase(Base.getLocal())) {
      this
        .bundle = ResourceBundle.getBundle(this.resourcePath, new Locale("zh"), 
          ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }
    else if ("fr".equalsIgnoreCase(Base.getLocal())) {
      this
        .bundle = ResourceBundle.getBundle(this.resourcePath, new Locale("fr"), 
          ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }
    else {
      this
        .bundle = ResourceBundle.getBundle(this.resourcePath, new Locale("en"), 
          ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    } 
  }
  public ResourceBundle getResourceBundle() {
    if (this.bundle == null)
    {
      updateResource(null);
    }
    return this.bundle;
  }
  public void setBundle(ResourceBundle bundle) {
    this.bundle = bundle;
  }
  public void setNumAndCapLock() {
    byte numAndCapLock = 0;
    int bladeNo = 0;
    if (!this.kvmInterface.isFullScreen()) {
      bladeNo = this.kvmInterface.getActionBlade();
    }
    else {
      bladeNo = this.kvmInterface.getFullScreen().getActionBlade();
    } 
    try {
      BladeThread bladeThread = (BladeThread)this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(bladeNo));
      numAndCapLock = ((Byte)bladeThread.getKeyState().get(String.valueOf(bladeNo))).byteValue();
    }
    catch (Exception e) {
      Debug.printExc(e.getClass().getName());
    } 
    int[] lock = perBitToInt(numAndCapLock);
    setNumKeyColor(lock[0]);
    setCapsKeyColor(lock[1]);
    setScrollKeyColor(lock[2]);
    ImagePane imagePane = getImagePane(bladeNo);
    if (imagePane != null) {
      imagePane.setCaps(lock[1]);
      imagePane.setNum(lock[0]);
      imagePane.setScroll(lock[2]);
      if (imagePane.getKvmInterface().getKvmUtil().getiWindosFocus() == 1) {
        if (MouseDisplacementImpl.getKeyBoardStatus((byte)-112) == imagePane.getNum() && 
          MouseDisplacementImpl.getKeyBoardStatus((byte)20) == imagePane.getCaps() && 
          MouseDisplacementImpl.getKeyBoardStatus((byte)-111) == imagePane.getScroll()) {
          return;
        }
        this.kvmInterface.setiImageFocused(1);
        this.kvmInterface.setiKeyPressControl(0);
        this.kvmInterface.setiKeyPressTotal(0);
        if (MouseDisplacementImpl.getKeyBoardStatus((byte)-112) != imagePane.getNum()) {
          this.kvmInterface.setiKeyPressTotal(this.kvmInterface.getiKeyPressTotal() + 1);
          MouseDisplacementImpl.setKeyBoardStatus((imagePane.getNum() == 1), (byte)-112);
        } 
        if (MouseDisplacementImpl.getKeyBoardStatus((byte)20) != imagePane.getCaps()) {
          this.kvmInterface.setiKeyPressTotal(this.kvmInterface.getiKeyPressTotal() + 1);
          MouseDisplacementImpl.setKeyBoardStatus((imagePane.getCaps() == 1), (byte)20);
        } 
        if (MouseDisplacementImpl.getKeyBoardStatus((byte)-111) != imagePane.getScroll()) {
          this.kvmInterface.setiKeyPressTotal(this.kvmInterface.getiKeyPressTotal() + 1);
          MouseDisplacementImpl.setKeyBoardStatus((imagePane.getScroll() == 1), (byte)-111);
        } 
      } 
    } 
  }
  public void setNumKeyColor(int lock) {
    if (lock == 1) {
      if (!this.kvmInterface.isFullScreen())
      {
        this.kvmInterface.getToolbar().setNumColor(Base.LIGHT_ON);
        this.kvmInterface.getToolbar().getNumColorButton().setBackground(this.kvmInterface.getToolbar().getNumColor());
      }
      else if (this.kvmInterface.getFullScreen().getToolBar() != null)
      {
        this.kvmInterface.getFullScreen().getToolBar().setNumColor(Base.LIGHT_ON);
        this.kvmInterface.getFullScreen()
          .getToolBar()
          .getNumColorButton()
          .setBackground(this.kvmInterface.getFullScreen().getToolBar().getNumColor());
      }
    }
    else if (!this.kvmInterface.isFullScreen()) {
      this.kvmInterface.getToolbar().setNumColor(Base.LIGHT_OFF);
      this.kvmInterface.getToolbar().getNumColorButton().setBackground(this.kvmInterface.getToolbar().getNumColor());
    }
    else if (this.kvmInterface.getFullScreen().getToolBar() != null) {
      this.kvmInterface.getFullScreen().getToolBar().setNumColor(Base.LIGHT_OFF);
      this.kvmInterface.getFullScreen()
        .getToolBar()
        .getNumColorButton()
        .setBackground(this.kvmInterface.getFullScreen().getToolBar().getNumColor());
    } 
  }
  public void setCapsKeyColor(int lock) {
    if (lock == 1) {
      if (!this.kvmInterface.isFullScreen())
      {
        this.kvmInterface.getToolbar().setCapsColor(Base.LIGHT_ON);
        this.kvmInterface.getToolbar().getCapsColorButton().setBackground(this.kvmInterface.getToolbar().getCapsColor());
      }
      else if (this.kvmInterface.getFullScreen().getToolBar() != null)
      {
        this.kvmInterface.getFullScreen().getToolBar().setCapsColor(Base.LIGHT_ON);
        this.kvmInterface.getFullScreen()
          .getToolBar()
          .getCapsColorButton()
          .setBackground(this.kvmInterface.getFullScreen().getToolBar().getCapsColor());
      }
    }
    else if (!this.kvmInterface.isFullScreen()) {
      this.kvmInterface.getToolbar().setCapsColor(Base.LIGHT_OFF);
      this.kvmInterface.getToolbar().getCapsColorButton().setBackground(this.kvmInterface.getToolbar().getCapsColor());
    }
    else if (this.kvmInterface.getFullScreen().getToolBar() != null) {
      this.kvmInterface.getFullScreen().getToolBar().setCapsColor(Base.LIGHT_OFF);
      this.kvmInterface.getFullScreen()
        .getToolBar()
        .getCapsColorButton()
        .setBackground(this.kvmInterface.getFullScreen().getToolBar().getCapsColor());
    } 
  }
  public void setScrollKeyColor(int lock) {
    if (lock == 1) {
      if (!this.kvmInterface.isFullScreen())
      {
        this.kvmInterface.getToolbar().setScrollColor(Base.LIGHT_ON);
        this.kvmInterface.getToolbar()
          .getScrollColorButton()
          .setBackground(this.kvmInterface.getToolbar().getScrollColor());
      }
      else if (this.kvmInterface.getFullScreen().getToolBar() != null)
      {
        this.kvmInterface.getFullScreen().getToolBar().setScrollColor(Base.LIGHT_ON);
        this.kvmInterface.getFullScreen()
          .getToolBar()
          .getScrollColorButton()
          .setBackground(this.kvmInterface.getFullScreen().getToolBar().getScrollColor());
      }
    }
    else if (!this.kvmInterface.isFullScreen()) {
      this.kvmInterface.getToolbar().setScrollColor(Base.LIGHT_OFF);
      this.kvmInterface.getToolbar()
        .getScrollColorButton()
        .setBackground(this.kvmInterface.getToolbar().getScrollColor());
    }
    else if (this.kvmInterface.getFullScreen().getToolBar() != null) {
      this.kvmInterface.getFullScreen().getToolBar().setScrollColor(Base.LIGHT_OFF);
      this.kvmInterface.getFullScreen()
        .getToolBar()
        .getScrollColorButton()
        .setBackground(this.kvmInterface.getFullScreen().getToolBar().getScrollColor());
    } 
  }
  public FloatToolbar getImageFloatToolbar(int bladeNO) {
    FloatToolbar floatKoobar = null;
    InterfaceContainer iContainer = null;
    for (int i = 0; i < this.kvmInterface.getBladeList().size(); i++) {
      iContainer = (InterfaceContainer)this.kvmInterface.getBladeList().get(i);
      if (iContainer.getBladeNumber() == bladeNO)
      {
        floatKoobar = iContainer.getFloatToolbar();
      }
    } 
    return floatKoobar;
  }
  public ImagePane getNewImagePane(int bladeNO) {
    return this.kvmInterface.createImagPanePanel(bladeNO);
  }
  public ImagePane getNewImagePane1(int bladeNO) {
    ImagePane imagePane = null;
    InterfaceContainer iContainer = null;
    VirtualMedia virtualMedia = null;
    for (int i = 0; i < this.kvmInterface.getBladeList().size(); i++) {
      iContainer = (InterfaceContainer)this.kvmInterface.getBladeList().get(i);
      imagePane = iContainer.getImagePane();
      if (iContainer.getBladeNumber() == bladeNO)
      {
        if (imagePane == null || imagePane.getBladeNumber() == 0) {
          if (Base.getConnMode() == 0) {
            virtualMedia = new VirtualMedia(Base.getLocal(), this.kvmInterface);
            virtualMedia.setStrIP(Base.getVmmConnIP());
            virtualMedia.setCodeKey(Base.getVmmCodeKey());
            virtualMedia.setPort(Base.getVmmPort());
            iContainer.setVirtualMedia(virtualMedia);
          } 
          imagePane = new ImagePane(this.kvmInterface);
          FloatToolbar floatToolbar = new FloatToolbar(imagePane, virtualMedia, this.kvmInterface);
          floatToolbar.setVirtualMedia(virtualMedia);
          iContainer.setImagePane(imagePane);
          iContainer.setKvmInterface(this.kvmInterface);
          iContainer.setFloatToolbar(floatToolbar);
          this.kvmInterface.setFloatToolbar(iContainer.getFloatToolbar());
          break;
        } 
      }
    } 
    return imagePane;
  }
  public ImagePane getImagePane(int bladeNO) {
    ImagePane imagePane = null;
    InterfaceContainer iContainer = null;
    for (int i = 0; i < this.kvmInterface.getBladeList().size(); i++) {
      iContainer = (InterfaceContainer)this.kvmInterface.getBladeList().get(i);
      imagePane = iContainer.getImagePane();
      if (iContainer.getBladeNumber() == bladeNO)
      {
        if (imagePane != null && imagePane.getBladeNumber() == bladeNO) {
          this.kvmInterface.setFloatToolbar(iContainer.getFloatToolbar());
          return imagePane;
        } 
      }
    } 
    return null;
  }
  public void setVMMSecretCodeKey(int bladeNO, byte[] codeKey, byte[] salt_vmm) {
    InterfaceContainer iContainer = null;
    VirtualMedia virtualMedia = null;
    for (int i = 0; i < this.kvmInterface.getBladeList().size(); i++) {
      iContainer = (InterfaceContainer)this.kvmInterface.getBladeList().get(i);
      if (iContainer.getBladeNumber() == bladeNO) {
        virtualMedia = iContainer.getVirtualMedia();
        virtualMedia.setNegotiCodeKey(codeKey, salt_vmm);
      } 
    } 
  }
  public void setVMMPort(int bladeNO, byte[] port) {
    InterfaceContainer iContainer = null;
    VirtualMedia virtualMedia = null;
    for (int i = 0; i < this.kvmInterface.getBladeList().size(); i++) {
      iContainer = (InterfaceContainer)this.kvmInterface.getBladeList().get(i);
      if (iContainer.getBladeNumber() == bladeNO) {
        virtualMedia = iContainer.getVirtualMedia();
        virtualMedia.setVMMPort(port);
      } 
    } 
  }
  public void setVMMPri(int bladeNO, boolean bPri) {
    InterfaceContainer iContainer = null;
    VirtualMedia virtualMedia = null;
    for (int i = 0; i < this.kvmInterface.getBladeList().size(); i++) {
      iContainer = (InterfaceContainer)this.kvmInterface.getBladeList().get(i);
      if (iContainer.getBladeNumber() == bladeNO) {
        virtualMedia = iContainer.getVirtualMedia();
        virtualMedia.setVMMPri(bPri);
      } 
    } 
  }
  public int getImagePaneCodeKey(int bladeNO) {
    int imagePaneCodeKey = 0;
    InterfaceContainer iContainer = null;
    for (int i = 0; i < this.kvmInterface.getBladeList().size(); i++) {
      iContainer = (InterfaceContainer)this.kvmInterface.getBladeList().get(i);
      if (iContainer.getBladeNumber() == bladeNO) {
        imagePaneCodeKey = iContainer.getCodeKey();
        return imagePaneCodeKey;
      } 
    } 
    return imagePaneCodeKey;
  }
  public ImagePane getImagePane_bak(int bladeNO) {
    if (bladeNO == 0) {
      if (this.kvmInterface.getImagePane1() == null) {
        this.kvmInterface.setImagePane1(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane1();
      } 
      if (this.kvmInterface.getImagePane1().getBladeNumber() == 0) {
        this.kvmInterface.setImagePane1(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane1();
      } 
      if (this.kvmInterface.getImagePane2() == null) {
        this.kvmInterface.setImagePane2(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane2();
      } 
      if (this.kvmInterface.getImagePane2().getBladeNumber() == 0) {
        this.kvmInterface.setImagePane2(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane2();
      } 
      if (this.kvmInterface.getImagePane3() == null) {
        this.kvmInterface.setImagePane3(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane3();
      } 
      if (this.kvmInterface.getImagePane3().getBladeNumber() == 0) {
        this.kvmInterface.setImagePane3(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane3();
      } 
      if (this.kvmInterface.getImagePane4() == null) {
        this.kvmInterface.setImagePane4(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane4();
      } 
      if (this.kvmInterface.getImagePane4().getBladeNumber() == 0) {
        this.kvmInterface.setImagePane4(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane4();
      } 
      if (this.kvmInterface.getImagePane5() == null) {
        this.kvmInterface.setImagePane5(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane5();
      } 
      if (this.kvmInterface.getImagePane5().getBladeNumber() == 0) {
        this.kvmInterface.setImagePane5(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane5();
      } 
      if (this.kvmInterface.getImagePane6() == null) {
        this.kvmInterface.setImagePane6(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane6();
      } 
      if (this.kvmInterface.getImagePane6().getBladeNumber() == 0) {
        this.kvmInterface.setImagePane6(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane6();
      } 
      if (this.kvmInterface.getImagePane7() == null) {
        this.kvmInterface.setImagePane7(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane7();
      } 
      if (this.kvmInterface.getImagePane7().getBladeNumber() == 0) {
        this.kvmInterface.setImagePane7(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane7();
      } 
      if (this.kvmInterface.getImagePane8() == null) {
        this.kvmInterface.setImagePane8(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane8();
      } 
      if (this.kvmInterface.getImagePane8().getBladeNumber() == 0) {
        this.kvmInterface.setImagePane8(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane8();
      } 
      if (this.kvmInterface.getImagePane9() == null) {
        this.kvmInterface.setImagePane9(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane9();
      } 
      if (this.kvmInterface.getImagePane9().getBladeNumber() == 0) {
        this.kvmInterface.setImagePane9(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane9();
      } 
      if (this.kvmInterface.getImagePane10() == null) {
        this.kvmInterface.setImagePane10(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane10();
      } 
      if (this.kvmInterface.getImagePane10().getBladeNumber() == 0) {
        this.kvmInterface.setImagePane10(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane10();
      } 
      if (this.kvmInterface.getImagePane11() == null) {
        this.kvmInterface.setImagePane11(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane11();
      } 
      if (this.kvmInterface.getImagePane11().getBladeNumber() == 0) {
        this.kvmInterface.setImagePane11(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane11();
      } 
      if (this.kvmInterface.getImagePane12() == null) {
        this.kvmInterface.setImagePane12(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane12();
      } 
      if (this.kvmInterface.getImagePane12().getBladeNumber() == 0) {
        this.kvmInterface.setImagePane12(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane12();
      } 
      if (this.kvmInterface.getImagePane13() == null) {
        this.kvmInterface.setImagePane13(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane13();
      } 
      if (this.kvmInterface.getImagePane13().getBladeNumber() == 0) {
        this.kvmInterface.setImagePane13(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane13();
      } 
      if (this.kvmInterface.getImagePane14() == null) {
        this.kvmInterface.setImagePane14(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane14();
      } 
      if (this.kvmInterface.getImagePane14().getBladeNumber() == 0)
      {
        this.kvmInterface.setImagePane14(new ImagePane(this.kvmInterface));
        return this.kvmInterface.getImagePane14();
      }
    }
    else {
      if (this.kvmInterface.getImagePane1() != null && this.kvmInterface.getImagePane1().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane1();
      }
      if (this.kvmInterface.getImagePane2() != null && this.kvmInterface.getImagePane2().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane2();
      }
      if (this.kvmInterface.getImagePane3() != null && this.kvmInterface.getImagePane3().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane3();
      }
      if (this.kvmInterface.getImagePane4() != null && this.kvmInterface.getImagePane4().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane4();
      }
      if (this.kvmInterface.getImagePane5() != null && this.kvmInterface.getImagePane5().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane5();
      }
      if (this.kvmInterface.getImagePane6() != null && this.kvmInterface.getImagePane6().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane6();
      }
      if (this.kvmInterface.getImagePane7() != null && this.kvmInterface.getImagePane7().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane7();
      }
      if (this.kvmInterface.getImagePane8() != null && this.kvmInterface.getImagePane8().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane8();
      }
      if (this.kvmInterface.getImagePane9() != null && this.kvmInterface.getImagePane9().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane9();
      }
      if (this.kvmInterface.getImagePane10() != null && this.kvmInterface.getImagePane10().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane10();
      }
      if (this.kvmInterface.getImagePane11() != null && this.kvmInterface.getImagePane11().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane11();
      }
      if (this.kvmInterface.getImagePane12() != null && this.kvmInterface.getImagePane12().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane12();
      }
      if (this.kvmInterface.getImagePane13() != null && this.kvmInterface.getImagePane13().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane13();
      }
      if (this.kvmInterface.getImagePane14() != null && this.kvmInterface.getImagePane14().getBladeNumber() == bladeNO)
      {
        return this.kvmInterface.getImagePane14();
      }
    } 
    return null;
  }
  public void setBladeButton() throws LibException {
    if (this.kvmInterface.getToolbar().isDynamicBlade()) {
      int bladeSize = this.kvmInterface.getBladeList().size();
      for (int i = 0; i < bladeSize; i++) {
        JButton blade = ((InterfaceContainer)this.kvmInterface.getBladeList().get(i)).getBladeButton();
        if (blade != null) {
          if (this.bladePreInfo[i] == 1 || this.bladePreInfo[i] == 7 || this.bladePreInfo[i] == 15 || this.bladePreInfo[i] == 23 || this.bladePreInfo[i] == 31) {
            blade.setEnabled(true);
          }
          else {
            blade.setEnabled(false);
          } 
          blade.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5), 
                BorderFactory.createLineBorder(Color.WHITE)));
        } 
      } 
    } 
  }
  public void setBladeEnable() {
    int[] temp1 = perBitToInt(this.bladePreInfo[0]);
    int[] temp2 = perBitToInt(this.bladePreInfo[1]);
    int[] PresentInfo = new int[14];
    System.arraycopy(temp1, 1, PresentInfo, 0, 7);
    System.arraycopy(temp2, 0, PresentInfo, 7, 7);
    if (this.kvmInterface.getToolbar().isDynamicBlade()) {
      int bladeSize = this.kvmInterface.getBladeList().size();
      for (int i = 0; i < bladeSize; i++) {
        JButton blade = ((InterfaceContainer)this.kvmInterface.getBladeList().get(i)).getBladeButton();
        if (blade != null) {
          if (PresentInfo[i] == 1 && this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(i + 1)) == null) {
            blade.setEnabled(true);
          }
          else {
            blade.setEnabled(false);
          } 
          blade.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5), 
                BorderFactory.createLineBorder(Color.WHITE)));
        } 
      } 
    } 
  }
  public void arrayImagePane() {
    this.imagePaneList.clear();
    this.imagePaneList.add(this.kvmInterface.getImagePane1());
    this.imagePaneList.add(this.kvmInterface.getImagePane2());
    this.imagePaneList.add(this.kvmInterface.getImagePane3());
    this.imagePaneList.add(this.kvmInterface.getImagePane4());
    for (int i = 0; i < 4; i++) {
      for (int j = i + 1; j < 4; j++) {
        ImagePane imaPane1 = this.imagePaneList.get(i);
        ImagePane imaPane2 = this.imagePaneList.get(j);
        if (imaPane1.getBladeNumber() > imaPane2.getBladeNumber()) {
          ImagePane tem = imaPane1;
          this.imagePaneList.set(i, imaPane2);
          this.imagePaneList.set(j, tem);
        } 
      } 
    } 
  }
  public void setImageTipText(boolean isShow) {
    if (isShow) {
      if (this.kvmInterface.getImagePane1() != null && this.kvmInterface.getImagePane1().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane1().setToolTipText("blade" + this.kvmInterface.getImagePane1().getBladeNumber());
      }
      if (this.kvmInterface.getImagePane2() != null && this.kvmInterface.getImagePane2().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane2().setToolTipText("blade" + this.kvmInterface.getImagePane2().getBladeNumber());
      }
      if (this.kvmInterface.getImagePane3() != null && this.kvmInterface.getImagePane3().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane3().setToolTipText("blade" + this.kvmInterface.getImagePane3().getBladeNumber());
      }
      if (this.kvmInterface.getImagePane4() != null && this.kvmInterface.getImagePane4().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane4().setToolTipText("blade" + this.kvmInterface.getImagePane4().getBladeNumber());
      }
      if (this.kvmInterface.getImagePane5() != null && this.kvmInterface.getImagePane5().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane5().setToolTipText("blade" + this.kvmInterface.getImagePane5().getBladeNumber());
      }
      if (this.kvmInterface.getImagePane6() != null && this.kvmInterface.getImagePane6().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane6().setToolTipText("blade" + this.kvmInterface.getImagePane6().getBladeNumber());
      }
      if (this.kvmInterface.getImagePane7() != null && this.kvmInterface.getImagePane7().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane7().setToolTipText("blade" + this.kvmInterface.getImagePane7().getBladeNumber());
      }
      if (this.kvmInterface.getImagePane8() != null && this.kvmInterface.getImagePane8().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane8().setToolTipText("blade" + this.kvmInterface.getImagePane8().getBladeNumber());
      }
      if (this.kvmInterface.getImagePane9() != null && this.kvmInterface.getImagePane9().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane9().setToolTipText("blade" + this.kvmInterface.getImagePane9().getBladeNumber());
      }
      if (this.kvmInterface.getImagePane10() != null && this.kvmInterface.getImagePane10().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane10().setToolTipText("blade" + this.kvmInterface.getImagePane10().getBladeNumber());
      }
      if (this.kvmInterface.getImagePane11() != null && this.kvmInterface.getImagePane11().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane11().setToolTipText("blade" + this.kvmInterface.getImagePane11().getBladeNumber());
      }
      if (this.kvmInterface.getImagePane12() != null && this.kvmInterface.getImagePane12().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane12().setToolTipText("blade" + this.kvmInterface.getImagePane12().getBladeNumber());
      }
      if (this.kvmInterface.getImagePane13() != null && this.kvmInterface.getImagePane13().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane13().setToolTipText("blade" + this.kvmInterface.getImagePane13().getBladeNumber());
      }
      if (this.kvmInterface.getImagePane14() != null && this.kvmInterface.getImagePane14().getBladeNumber() != 0)
      {
        this.kvmInterface.getImagePane14().setToolTipText("blade" + this.kvmInterface.getImagePane14().getBladeNumber());
      }
    }
    else {
      if (this.kvmInterface.getImagePane1() != null)
      {
        this.kvmInterface.getImagePane1().setToolTipText((String)null);
      }
      if (this.kvmInterface.getImagePane2() != null)
      {
        this.kvmInterface.getImagePane2().setToolTipText((String)null);
      }
      if (this.kvmInterface.getImagePane3() != null)
      {
        this.kvmInterface.getImagePane3().setToolTipText((String)null);
      }
      if (this.kvmInterface.getImagePane4() != null)
      {
        this.kvmInterface.getImagePane4().setToolTipText((String)null);
      }
      if (this.kvmInterface.getImagePane5() != null)
      {
        this.kvmInterface.getImagePane5().setToolTipText((String)null);
      }
      if (this.kvmInterface.getImagePane6() != null)
      {
        this.kvmInterface.getImagePane6().setToolTipText((String)null);
      }
      if (this.kvmInterface.getImagePane7() != null)
      {
        this.kvmInterface.getImagePane7().setToolTipText((String)null);
      }
      if (this.kvmInterface.getImagePane8() != null)
      {
        this.kvmInterface.getImagePane8().setToolTipText((String)null);
      }
      if (this.kvmInterface.getImagePane9() != null)
      {
        this.kvmInterface.getImagePane9().setToolTipText((String)null);
      }
      if (this.kvmInterface.getImagePane10() != null)
      {
        this.kvmInterface.getImagePane10().setToolTipText((String)null);
      }
      if (this.kvmInterface.getImagePane11() != null)
      {
        this.kvmInterface.getImagePane11().setToolTipText((String)null);
      }
      if (this.kvmInterface.getImagePane12() != null)
      {
        this.kvmInterface.getImagePane12().setToolTipText((String)null);
      }
      if (this.kvmInterface.getImagePane13() != null)
      {
        this.kvmInterface.getImagePane13().setToolTipText((String)null);
      }
      if (this.kvmInterface.getImagePane14() != null)
      {
        this.kvmInterface.getImagePane14().setToolTipText((String)null);
      }
    } 
  }
  public void setDrawDisplay(boolean isDisplay) {
    Iterator<Object> iter = this.kvmInterface.getBase().getThreadGroup().keySet().iterator();
    String name = "";
    if (isDisplay) {
      while (iter.hasNext())
      {
        name = (String)iter.next();
        BladeThread bladeThread = (BladeThread)this.kvmInterface.getBase().getThreadGroup().get(name);
        bladeThread.getDrawThread().setDisplay(isDisplay);
        bladeThread.getBladeCommu().sentData(this.kvmInterface.getPackData().contrRate(1, bladeThread
              .getBladeNOByBladeThread()));
      }
    } else {
      while (iter.hasNext()) {
        name = (String)iter.next();
        BladeThread bladeThread = (BladeThread)this.kvmInterface.getBase().getThreadGroup().get(name);
        bladeThread.getDrawThread().setDisplay(isDisplay);
        bladeThread.getBladeCommu().sentData(this.kvmInterface.getPackData().contrRate(0, bladeThread
              .getBladeNOByBladeThread()));
      } 
    } 
  }
  public BladeState getBladeStateAuto(int bladeNO, String bladeIP, byte[] stateBytes) {
    BladeState bladeState = new BladeState();
    int[] state = perBitToInt(stateBytes[1]);
    if (state[2] == 1) {
      bladeState.setEnable(false);
    }
    else {
      bladeState = showBladeAbsent(stateBytes, bladeNO, 10);
    } 
    return bladeState;
  }
  public BladeState getBladeState(int bladeNO) throws LibException {
	return null;
    // Byte code:
    //   0: new com/kvm/BladeState
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_2
    //   8: iconst_0
    //   9: istore_3
    //   10: aload_0
    //   11: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   14: invokevirtual getClientSocket : ()Lcom/kvm/ClientSocketCommunity;
    //   17: invokevirtual getBladeStateInfo : ()Ljava/util/Hashtable;
    //   20: iload_1
    //   21: invokestatic valueOf : (I)Ljava/lang/String;
    //   24: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   27: pop
    //   28: aload_0
    //   29: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   32: invokevirtual getClient : ()Lcom/kvm/Client;
    //   35: aload_0
    //   36: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   39: invokevirtual getPackData : ()Lcom/kvm/PackData;
    //   42: iload_1
    //   43: invokestatic getConnMode : ()I
    //   46: invokevirtual reqBladeState : (II)[B
    //   49: invokevirtual sentData : ([B)V
    //   52: goto -> 100
    //   55: astore #4
    //   57: ldc 'IO_ERRCODE'
    //   59: aload #4
    //   61: invokevirtual getErrCode : ()Ljava/lang/String;
    //   64: invokevirtual equals : (Ljava/lang/Object;)Z
    //   67: ifeq -> 92
    //   70: aload_0
    //   71: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   74: invokevirtual getToolbar : ()Lcom/kvm/KvmAppletToolBar;
    //   77: aload_0
    //   78: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   81: invokevirtual getKvmUtil : ()Lcom/kvm/KVMUtil;
    //   84: ldc 'Network_interrupt_message'
    //   86: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   89: invokestatic showMessageDialog : (Ljava/awt/Component;Ljava/lang/Object;)V
    //   92: new com/library/LibException
    //   95: dup
    //   96: invokespecial <init> : ()V
    //   99: athrow
    //   100: iload_3
    //   101: bipush #15
    //   103: if_icmpge -> 427
    //   106: aload_0
    //   107: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   110: invokevirtual getClientSocket : ()Lcom/kvm/ClientSocketCommunity;
    //   113: invokevirtual getBladeStateInfo : ()Ljava/util/Hashtable;
    //   116: iload_1
    //   117: invokestatic valueOf : (I)Ljava/lang/String;
    //   120: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   123: ifnonnull -> 206
    //   126: aload_0
    //   127: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   130: invokevirtual getClientSocket : ()Lcom/kvm/ClientSocketCommunity;
    //   133: invokevirtual getBladeStateInfo : ()Ljava/util/Hashtable;
    //   136: ldc_w '100'
    //   139: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   142: ifnonnull -> 206
    //   145: new java/lang/StringBuilder
    //   148: dup
    //   149: invokespecial <init> : ()V
    //   152: ldc_w 'blade'
    //   155: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   158: iload_1
    //   159: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   162: ldc_w ' = '
    //   165: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   168: iload_3
    //   169: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   172: invokevirtual toString : ()Ljava/lang/String;
    //   175: invokestatic println : (Ljava/lang/String;)V
    //   178: ldc2_w 1000
    //   181: invokestatic sleep : (J)V
    //   184: goto -> 200
    //   187: astore #4
    //   189: aload #4
    //   191: invokevirtual getClass : ()Ljava/lang/Class;
    //   194: invokevirtual getName : ()Ljava/lang/String;
    //   197: invokestatic printExc : (Ljava/lang/String;)V
    //   200: iinc #3, 1
    //   203: goto -> 100
    //   206: aload_0
    //   207: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   210: invokevirtual getClientSocket : ()Lcom/kvm/ClientSocketCommunity;
    //   213: invokevirtual getBladeStateInfo : ()Ljava/util/Hashtable;
    //   216: iload_1
    //   217: invokestatic valueOf : (I)Ljava/lang/String;
    //   220: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   223: ifnull -> 321
    //   226: aload_0
    //   227: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   230: invokevirtual getClientSocket : ()Lcom/kvm/ClientSocketCommunity;
    //   233: invokevirtual getBladeStateInfo : ()Ljava/util/Hashtable;
    //   236: iload_1
    //   237: invokestatic valueOf : (I)Ljava/lang/String;
    //   240: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   243: checkcast [B
    //   246: checkcast [B
    //   249: astore #4
    //   251: aload #4
    //   253: iconst_1
    //   254: baload
    //   255: invokestatic perBitToInt : (B)[I
    //   258: astore #5
    //   260: aload #5
    //   262: iconst_2
    //   263: iaload
    //   264: iconst_1
    //   265: if_icmpne -> 299
    //   268: aload_0
    //   269: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   272: invokevirtual getToolbar : ()Lcom/kvm/KvmAppletToolBar;
    //   275: aload_0
    //   276: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   279: invokevirtual getKvmUtil : ()Lcom/kvm/KVMUtil;
    //   282: ldc_w 'BMC_reset_message'
    //   285: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   288: invokestatic showMessageDialog : (Ljava/awt/Component;Ljava/lang/Object;)V
    //   291: aload_2
    //   292: iconst_0
    //   293: invokevirtual setEnable : (Z)V
    //   296: goto -> 308
    //   299: aload_0
    //   300: aload #4
    //   302: iload_1
    //   303: iconst_0
    //   304: invokespecial showBladeAbsent : ([BII)Lcom/kvm/BladeState;
    //   307: astore_2
    //   308: aload_0
    //   309: aload #4
    //   311: aload #4
    //   313: arraylength
    //   314: iconst_1
    //   315: isub
    //   316: baload
    //   317: iload_1
    //   318: invokevirtual setCodeKey : (BI)V
    //   321: aload_0
    //   322: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   325: invokevirtual getClientSocket : ()Lcom/kvm/ClientSocketCommunity;
    //   328: invokevirtual getBladeStateInfo : ()Ljava/util/Hashtable;
    //   331: ldc_w '100'
    //   334: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   337: ifnull -> 447
    //   340: aload_0
    //   341: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   344: invokevirtual getClientSocket : ()Lcom/kvm/ClientSocketCommunity;
    //   347: invokevirtual getBladeStateInfo : ()Ljava/util/Hashtable;
    //   350: ldc_w '100'
    //   353: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   356: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   359: checkcast [B
    //   362: checkcast [B
    //   365: astore #4
    //   367: aload #4
    //   369: iconst_1
    //   370: baload
    //   371: invokestatic perBitToInt : (B)[I
    //   374: astore #5
    //   376: aload #5
    //   378: iconst_2
    //   379: iaload
    //   380: iconst_1
    //   381: if_icmpne -> 415
    //   384: aload_0
    //   385: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   388: invokevirtual getToolbar : ()Lcom/kvm/KvmAppletToolBar;
    //   391: aload_0
    //   392: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   395: invokevirtual getKvmUtil : ()Lcom/kvm/KVMUtil;
    //   398: ldc_w 'BMC_reset_message'
    //   401: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   404: invokestatic showMessageDialog : (Ljava/awt/Component;Ljava/lang/Object;)V
    //   407: aload_2
    //   408: iconst_0
    //   409: invokevirtual setEnable : (Z)V
    //   412: goto -> 424
    //   415: aload_0
    //   416: aload #4
    //   418: iload_1
    //   419: iconst_0
    //   420: invokespecial showBladeAbsent : ([BII)Lcom/kvm/BladeState;
    //   423: astore_2
    //   424: goto -> 447
    //   427: aload_0
    //   428: getfield kvmInterface : Lcom/kvm/KVMInterface;
    //   431: invokevirtual getToolbar : ()Lcom/kvm/KvmAppletToolBar;
    //   434: aload_0
    //   435: ldc_w 'timeout'
    //   438: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   441: invokestatic showMessageDialog : (Ljava/awt/Component;Ljava/lang/Object;)V
    //   444: goto -> 447
    //   447: aload_2
    //   448: areturn
    // Line number table:
    //   Java source line number -> byte code offset
    //   #2763	-> 0
    //   #2764	-> 8
    //   #2765	-> 10
    //   #2768	-> 28
    //   #2778	-> 52
    //   #2770	-> 55
    //   #2772	-> 57
    //   #2774	-> 70
    //   #2775	-> 81
    //   #2774	-> 89
    //   #2777	-> 92
    //   #2781	-> 100
    //   #2783	-> 106
    //   #2784	-> 130
    //   #2786	-> 145
    //   #2789	-> 178
    //   #2794	-> 184
    //   #2791	-> 187
    //   #2793	-> 189
    //   #2795	-> 200
    //   #2799	-> 206
    //   #2801	-> 226
    //   #2802	-> 230
    //   #2803	-> 251
    //   #2804	-> 260
    //   #2806	-> 268
    //   #2807	-> 285
    //   #2806	-> 288
    //   #2808	-> 291
    //   #2812	-> 299
    //   #2819	-> 308
    //   #2821	-> 321
    //   #2823	-> 340
    //   #2824	-> 344
    //   #2825	-> 367
    //   #2826	-> 376
    //   #2828	-> 384
    //   #2829	-> 401
    //   #2828	-> 404
    //   #2830	-> 407
    //   #2834	-> 415
    //   #2836	-> 424
    //   #2842	-> 427
    //   #2843	-> 444
    //   #2846	-> 447
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   57	43	4	e	Lcom/library/LibException;
    //   189	11	4	e	Ljava/lang/InterruptedException;
    //   251	70	4	stateBytes	[B
    //   260	61	5	state	[I
    //   367	57	4	stateBytes	[B
    //   376	48	5	state	[I
    //   0	449	0	this	Lcom/kvm/KVMUtil;
    //   0	449	1	bladeNO	I
    //   8	441	2	bladeState	Lcom/kvm/BladeState;
    //   10	439	3	count	I
    // Exception table:
    //   from	to	target	type
    //   28	52	55	com/library/LibException
    //   178	184	187	java/lang/InterruptedException
  }
  private BladeState showBladeAbsent(byte[] bytes, int bladeNO, int count) {
    BladeState bladeState = new BladeState();
    if (bytes[0] == 100) {
      if (count <= 0)
      {
        JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), getString("SMMMaxContrConnection"));
      }
      return bladeState;
    } 
    int[] state = perBitToInt(bytes[1]);
    if (state[7] == 0) {
      if (count <= 0)
      {
        JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), getString("Absent"));
      }
    }
    else {
      bladeState = showBladeDown(state, bladeNO, bytes, count);
    } 
    return bladeState;
  }
  private BladeState showBladeDown(int[] state, int bladeNO, byte[] bytes, int count) {
    BladeState bladeState = new BladeState();
    if (state[5] == 0) {
      if (count <= 0)
      {
        JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), getString("nonsupportKVM"));
      }
    }
    else if (state[4] == 1) {
      if (state[3] == 0) {
        int tem1 = bytes[3], tem2 = bytes[4], tem3 = bytes[5], tem4 = bytes[6];
        if (bytes[3] < 0)
        {
          tem1 = 256 + bytes[3];
        }
        if (bytes[4] < 0)
        {
          tem2 = 256 + bytes[4];
        }
        if (bytes[5] < 0)
        {
          tem3 = 256 + bytes[5];
        }
        if (bytes[6] < 0)
        {
          tem4 = 256 + bytes[6];
        }
        StringBuffer strBuf = new StringBuffer("");
        String point = ".";
        strBuf.append(tem1);
        strBuf.append(point);
        strBuf.append(tem2);
        strBuf.append(point);
        strBuf.append(tem3);
        strBuf.append(point);
        strBuf.append(tem4);
        String bladeIP = strBuf.toString();
        if (Base.getConnMode() == 1) {
          String bladeIP1 = this.kvmInterface.getClient().getAddress().getHostAddress();
          if (0 != bladeIP.length() && !bladeIP.equals(bladeIP1))
          {
            bladeIP = bladeIP1;
          }
        } 
        int bladePort = byteToIntCon(bytes, 7, 2);
        bladeState.setBladeIP(bladeIP);
        bladeState.setBladePort(bladePort);
        bladeState.setEnable(true);
        bladeState.setNew(true);
        return bladeState;
      } 
      if (count <= 0)
      {
        JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), getString("KVM_now"));
        bladeState.setEnable(false);
        return bladeState;
      }
    }
    else {
      String bladeIP = this.kvmInterface.getClient().getAddress().getHostAddress();
      int bladePort = byteToIntCon(bytes, 7, 2);
      bladeState.setBladeIP(bladeIP);
      bladeState.setBladePort(bladePort);
      bladeState.setNew(false);
      bladeState.setEnable(showBladeBusy(state, bladeNO, bytes, count));
    } 
    return bladeState;
  }
  private boolean showBladeBusy(int[] state, int bladeNO, byte[] bytes, int count) {
    boolean connectEnable = false;
    if (state[2] == 0 && state[1] == 0 && state[0] == 1) {
      if (bytes[2] >= 4)
      {
        if (count <= 0)
        {
          JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), getString("over_bladeconnect"));
        }
      }
      else
      {
        int tem1 = bytes[3], tem2 = bytes[4], tem3 = bytes[5], tem4 = bytes[6];
        if (bytes[3] < 0)
        {
          tem1 = 256 + bytes[3];
        }
        if (bytes[4] < 0)
        {
          tem2 = 256 + bytes[4];
        }
        if (bytes[5] < 0)
        {
          tem3 = 256 + bytes[5];
        }
        if (bytes[6] < 0)
        {
          tem4 = 256 + bytes[6];
        }
        String point = ".";
        StringBuffer strBuf = new StringBuffer("(");
        strBuf.append(tem1);
        strBuf.append(point);
        strBuf.append(tem2);
        strBuf.append(point);
        strBuf.append(tem3);
        strBuf.append(point);
        strBuf.append(tem4);
        strBuf.append(")");
        StringBuffer nameBuf = new StringBuffer("");
        for (int i = 0; i < 16; i++)
        {
          nameBuf.append((char)bytes[7 + i]);
        }
        if (count <= 0)
        {
          JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), getString("KVM_now"));
        }
      }
    }
    else if (state[2] == 0 && state[1] == 1 && state[0] == 0) {
      if (count <= 0)
      {
        JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), getString("SOL_now"));
      }
    }
    else {
      connectEnable = showBladeFileLoad(state, count);
    } 
    return connectEnable;
  }
  private boolean showBladeFileLoad(int[] state, int count) {
    boolean connectEnable = false;
    if (state[2] == 0 && state[1] == 1 && state[0] == 1) {
      if (count <= 0)
      {
        JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), getString("load_file"));
      }
    }
    else if (state[2] == 0 && state[1] == 0 && state[0] == 0) {
      connectEnable = true;
    }
    else {
      Debug.printExc("error data");
    } 
    return connectEnable;
  }
  public void connectNewBlade(int bladeNO, boolean isControl, String bladeIP, int port, boolean isNew) throws LibException {
	LoggerUtil.info( "clickButton: "+bladeNO);
    clickButton(bladeNO, false);
    try {
      this.bladeThread = new BladeThread(bladeIP, port, bladeNO, isNew);
      this.bladeThread.setName("BladeThread" + bladeNO);
      LoggerUtil.info( "this.bladeThread: "+this.bladeThread.getName());
    }
    catch (LibException e) {
      if (this.kvmInterface.getBladeSize() == 1) {
        JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface
            .getKvmUtil().getString("Connect_lost_message_one") + " " + bladeIP);
      }
      else {
        JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface
            .getKvmUtil().getString("Connect_lost_message") + " " + bladeIP);
      } 
      clickButton(bladeNO, true);
      return;
    } 
    String bladeKey = Integer.toString(bladeNO);
    this.kvmInterface.getBase().getThreadGroup().put(bladeKey, this.bladeThread);
    KVMUtil kvmUtil = new KVMUtil();
    this.bladeThread.setKvmUtil(kvmUtil);
    this.bladeThread.setKvmInterface(this.kvmInterface);
    this.bladeThread.getBladeCommu().setClient(this.kvmInterface.getClient());
    this.bladeThread.setUnPackData(new UnPackData());
    this.bladeThread.setBladeNumb(bladeNO);
    this.bladeThread.getBladeCommu().setBladeNumber(bladeNO);
    this.bladeThread.getBladeCommu().setKvmInterface(this.kvmInterface);
    kvmUtil.setKvmInterface(this.kvmInterface);
    kvmUtil.setImageData(this.imageData);
    kvmUtil.firstJudge = true;
    if (null == getNewImagePane(bladeNO)) {
      TestPrint.println(3, "getNewImagePane failed.");
      return;
    } 
    ImagePane imagePane = getNewImagePane(bladeNO);
    if (imagePane == null) {
      return;
    }
    Action action = new EmptyAction();
    imagePane.getInputMap().put(KeyStroke.getKeyStroke("UP"), "UpPress");
    imagePane.getActionMap().put("UpPress", action);
    imagePane.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "DOWNPress");
    imagePane.getActionMap().put("DOWNPress", action);
    imagePane.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "LeftPress");
    imagePane.getActionMap().put("LeftPress", action);
    imagePane.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "RightPress");
    imagePane.getActionMap().put("RightPress", action);
    if (getImageFloatToolbar(bladeNO) != null && 
      getImageFloatToolbar(bladeNO).getVirtualMedia() != null)
    {
      getImageFloatToolbar(bladeNO).getVirtualMedia().setStrIP(bladeIP);
    }
    imagePane.setCursor((this.kvmInterface.getBase()).myCursor);
    PackData pack = new PackData();
    pack.setKvmInterface(this.kvmInterface);
    imagePane.setPack(pack);
    imagePane.setBladeNumber(bladeNO);
    imagePane.setControl(isControl);
    imagePane.setBladeThread(this.bladeThread);
    imagePane.setNew(isNew);
    DrawThread drawThread = new DrawThread(bladeNO, imagePane, kvmUtil);
    this.bladeThread.setDrawThread(drawThread);
    drawThread.setName("DrawThread" + bladeKey);
    drawThread.setConn(true);
    drawThread.setTotalConn(true);
    drawThread.setKvmInterface(this.kvmInterface);
    drawThread.setBladeCommu(this.bladeThread.getBladeCommu());
    this.kvmInterface.getBase().getTabbedList().add(bladeKey);
    Iterator<Object> iter = this.kvmInterface.getBase().getThreadGroup().keySet().iterator();
    String name = "";
    while (iter.hasNext()) {
      name = (String)iter.next();
      ((BladeThread)this.kvmInterface.getBase().getThreadGroup().get(name)).getDrawThread().setDisplay(false);
    } 
    drawThread.setDisplay(true);
    kvmUtil.resetBuf();
    this.bladeThread.setBladeHeartTimer(new BladeHeartTimer(this.bladeThread));
    this.bladeThread.getBladeHeartTimer().setName("BladeHeart" + bladeNO);
    this.bladeThread.getBladeHeartTimer().start();
    LoggerUtil.info( "this.bladeThread.start() ");
    this.bladeThread.start();
    LoggerUtil.info( "drawThread.start() ");
    drawThread.start();
    this.kvmInterface.getTabbedpane().setPreferredSize(new Dimension(kvmUtil.imageWidth + (imagePane.getLocation()).x, kvmUtil.imageHeight + 
          (imagePane.getLocation()).y));
    if (this.kvmInterface.getActionBlade() != 0) {
      BladeThread bladeThr = (BladeThread)this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(this.kvmInterface.getActionBlade()));
      if (bladeThr != null)
      {
        bladeThr.getBladeCommu().sentData(this.kvmInterface.getPackData().contrRate(0, bladeThr
              .getBladeNOByBladeThread()));
      }
    } 
    this.kvmInterface.setActionBlade(bladeNO);
    if (this.kvmInterface.getBladeList().size() == 1) {
      this.kvmInterface.getTabbedpane().add(imagePane);
    }
    else {
      this.kvmInterface.getTabbedpane().add("blade" + bladeNO, imagePane);
    } 
    this.kvmInterface.getTabbedpane().setSelectedComponent(imagePane);
    imagePane.setStatReceiveTask(new StatReceiveTask(imagePane));
    startReceiveList(imagePane);
    LoggerUtil.info( "isControl: "+isControl);
    if (isControl) {
      imagePane.setMouseTimerTask(new MouseTimerTask(imagePane));
      startMouseList(imagePane);
      if (this.kvmInterface.isNeedConsultation()) {
    	LoggerUtil.info( "sentData: "+this.kvmInterface.getPackData().getSuiteList(bladeNO));
        this.bladeThread.getBladeCommu().sentData(this.kvmInterface.getPackData().getSuiteList(bladeNO));
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException e) {
          Debug.printExc(e.getClass().getName());
        } 
      } 
      this.bladeThread.getBladeCommu().sentData(this.kvmInterface.getPackData().connectBlade(bladeNO, 
            getImagePane(bladeNO).getCustBit()));
      LoggerUtil.info( "this.kvmInterface.getBladeFlag(): "+this.kvmInterface.getBladeFlag());
      if (this.kvmInterface.getBladeFlag() != null) {
        try {
          Thread.sleep(5000L);
        }
        catch (InterruptedException e) {
          Debug.printExc(e.getClass().getName());
        } 
      }
      this.bladeThread.getBladeCommu().sentData(this.kvmInterface.getPackData().contrRate(35, this.bladeThread
            .getBladeNOByBladeThread()));
      this.bladeThread.getBladeCommu().sentData(this.kvmInterface.getKvmUtil()
          .getImagePane(this.kvmInterface.getActionBlade())
          .getPack()
          .mouseModeControl((byte)36, (byte)2, this.bladeThread.getBladeNOByBladeThread()));
    }
    else {
      this.bladeThread.getBladeCommu().sentData(this.kvmInterface.getPackData().monitorBlade(bladeNO));
      setMoniKeyState(this.kvmInterface.isFullScreen());
    } 
    setButtonEnable(true);
    LoggerUtil.info( "this.kvmInterface.getToolbar().getMouseSynButton(): "+this.kvmInterface.getToolbar().getMouseSynButton());
    if (null != this.kvmInterface.getToolbar().getMouseSynButton())
    {
      if (isNew)
      {
        if (this.kvmInterface.getBase().isMstsc()) {
          this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(true);
        }
        else {
          this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(false);
        } 
      }
    }
    MouseDisplacementImpl.setMode(0);
    this.kvmInterface.setClickFlag(false);
    LoggerUtil.info( "this.kvmInterface.setClickFlag: ");
  }
  private void clickButton(int bladeNO, boolean enable) {
    if (this.kvmInterface.getBladeList().size() == 1) {
      return;
    }
    this.kvmInterface.getToolbar().getComponent(4 + bladeNO).setEnabled(enable);
  }
  public void disconnectBlade(int bladeNO) {
    Object lock = this.kvmInterface.getBase().getLock(bladeNO);
    synchronized (lock) {
      if (this.kvmInterface.getBase().getThreadGroup().size() == 0 || this.kvmInterface
        .getBase().getThreadGroup().get(String.valueOf(bladeNO)) == null) {
        return;
      }
      BladeThread bladeThread = (BladeThread)this.kvmInterface.getBase().getThreadGroup().remove(String.valueOf(bladeNO));
      this.kvmInterface.getClientSocket().getBladeMap().remove(String.valueOf(bladeNO));
      ImagePane imagePane = getImagePane(bladeNO);
      imagePane.getKvmInterface().getFloatToolbar().destroyVmLink();
      if (!Base.getIsSynMouse())
      {
        imagePane.getMouseTimerTask().interrupt();
      }
      imagePane.setMouseTimerTask((MouseTimerTask)null);
      imagePane.getReceiveList().cancel();
      imagePane.setStatReceiveTask((StatReceiveTask)null);
      bladeThread.getBladeHeartTimer().interrupt();
      bladeThread.getKeyState().clear();
      DrawThread drawThread = bladeThread.getDrawThread();
      this.kvmInterface.getBase().getTabbedList().remove(String.valueOf(bladeNO));
      if (drawThread.getTimer() != null)
      {
        drawThread.getTimer().cancel();
      }
      drawThread.setConn(false);
      drawThread.getlList().add(new byte[] { 1, 1, 1, 1, 1 });
      drawThread.getlList().add(new byte[] { 1, 1, 1, 1, 1 });
      while (drawThread.isAlive()) {
        try {
          lock.wait(10L);
        }
        catch (InterruptedException interruptedException) {}
      } 
      if (!bladeThread.getBladeCommu().getSocket().isClosed())
      {
        bladeThread.getBladeCommu().sentData(this.kvmInterface.getPackData().interruptBlade(bladeNO));
      }
      drawThread.setComImage((LinkedList<byte[]>)null);
      drawThread.setPreviImage((byte[])null);
      (drawThread.getKvmUtil()).imageData = null;
      (drawThread.getKvmUtil()).bladePreInfo = null;
      drawThread.setImagePane((ImagePane)null);
      drawThread.setlList((Vector<Object>)null);
      bladeThread.setConn(false);
      try {
        bladeThread.getBladeCommu().getSocket().close();
      }
      catch (IOException e) {
        bladeThread.interrupt();
        Debug.printExc(e.getClass().getName());
      } 
      imagePane.setBladeNumber(0);
      imagePane.setColorBit((byte)0);
      imagePane.setCustBit((byte)0);
      setImagePaneRev(imagePane);
      this.kvmInterface.getTabbedpane().getModel().removeChangeListener(this.changeListener);
      synchronized (this.kvmInterface.getTabbedpane()) {
        this.kvmInterface.getTabbedpane().remove(imagePane);
      } 
      imagePane.releaseImagePanel();
      clickButton(bladeNO, true);
      if (this.kvmInterface.getTabbedpane().getTabCount() == 0) {
        setButtonEnable(false);
        this.kvmInterface.setActionBlade(0);
        this.kvmInterface.getToolbar().getNumColorButton().setBackground(Base.LIGHT_OFF);
        this.kvmInterface.getToolbar().getCapsColorButton().setBackground(Base.LIGHT_OFF);
        this.kvmInterface.getToolbar().getScrollColorButton().setBackground(Base.LIGHT_OFF);
      }
      else {
        if (null == this.kvmInterface.getTabbedpane().getSelectedComponent()) {
          return;
        }
        this.kvmInterface.setActionBlade(((ImagePane)this.kvmInterface.getTabbedpane().getSelectedComponent()).getBladeNumber());
        ImagePane imagePane1 = getImagePane(this.kvmInterface.getActionBlade());
        this.kvmInterface.getTabbedpane().setPreferredSize(new Dimension(imagePane1.getImagePaneWidth() + 
              (imagePane1.getLocation()).x, imagePane1.getImagePaneHeight() + (imagePane1.getLocation()).y + 5));
        if (null != this.kvmInterface.getToolbar().getMouseSynButton())
        {
          if (imagePane1.isNew()) {
            if (this.kvmInterface.getBase().isMstsc())
            {
              this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(true);
            }
            else
            {
              this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(false);
            }
          } else {
            this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(true);
          } 
        }
        BladeThread bThread = (BladeThread)this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(imagePane1.getBladeNumber()));
        bThread.getDrawThread().setDisplay(true);
        (bThread.getKvmUtil()).resultDivi.clear();
        (bThread.getKvmUtil()).diviBuff.clear();
        bThread.getKvmUtil().resetBuf();
        bThread.getDrawThread().getlList().clear();
        bThread.getDrawThread().getComImage().clear();
        bThread.getBladeCommu().sentData(this.kvmInterface.getPackData().connectBlade(this.kvmInterface.getActionBlade(), 
              getImagePane(this.kvmInterface.getActionBlade()).getCustBit()));
        bThread.getBladeCommu().sentData(this.kvmInterface.getPackData().contrRate(35, bThread
              .getBladeNOByBladeThread()));
        imagePane1.getKvmInterface()
          .getFloatToolbar()
          .setLocation((imagePane1.getKvmInterface().getFloatToolbar().getImgwidth() - imagePane1.getKvmInterface()
            .getFloatToolbar()
            .getWidth()) / 2, -1);
        if (imagePane1.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
          imagePane1.getKvmInterface()
            .getFloatToolbar()
            .setFlpLocation((imagePane1.getKvmInterface().getFloatToolbar().getImgwidth() - imagePane1.getKvmInterface()
              .getFloatToolbar()
              .getFlpWidth()) / 2, imagePane1
              .getKvmInterface().getFloatToolbar().getHeight() - 1);
          imagePane1.getKvmInterface()
            .getFloatToolbar()
            .setCDLocation((imagePane1.getKvmInterface().getFloatToolbar().getImgwidth() - imagePane1.getKvmInterface()
              .getFloatToolbar()
              .getCDWidth()) / 2, imagePane1
              .getKvmInterface().getFloatToolbar().getHeight() - 1);
        } 
      } 
    } 
  }
  public void setImagePaneRev(ImagePane imagePane) {
    imagePane.setImage(new byte[2304000]);
    imagePane.repaint();
    imagePane.getSource().newPixels(0, 0, 1920, 1200);
    imagePane.getBig().drawImage(imagePane.getImage(), 0, 0, imagePane);
    imagePane.getTransform().setToScale(Base.getScreenSize().getWidth() / 2.0D / 1920.0D, 
        Base.getScreenSize().getHeight() / 2.0D / 1200.0D);
    imagePane.applyFilter();
    imagePane.repaint();
  }
  public void setButtonEnable(boolean buttonEnable) {
    if (buttonEnable) {
      if (null != this.kvmInterface.getToolbar().getDisConnectBladeButton())
      {
        this.kvmInterface.getToolbar().getDisConnectBladeButton().setEnabled(true);
      }
      if (null != this.kvmInterface.getToolbar().getMouseSynButton())
      {
        this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(true);
      }
      if (null != this.kvmInterface.getToolbar().getSetColorBit())
      {
        this.kvmInterface.getToolbar().getSetColorBit().setEnabled(true);
      }
      this.kvmInterface.getToolbar().getCombineKey().setEnabled(true);
      this.kvmInterface.getToolbar().getFullButton().setEnabled(true);
      this.kvmInterface.getToolbar().getImageButton().setEnabled(true);
    }
    else {
      if (null != this.kvmInterface.getToolbar().getDisConnectBladeButton())
      {
        this.kvmInterface.getToolbar().getDisConnectBladeButton().setEnabled(false);
      }
      if (null != this.kvmInterface.getToolbar().getMouseSynButton())
      {
        this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(false);
      }
      if (null != this.kvmInterface.getToolbar().getSetColorBit())
      {
        this.kvmInterface.getToolbar().getSetColorBit().setEnabled(false);
      }
      this.kvmInterface.getToolbar().getCombineKey().setEnabled(false);
      this.kvmInterface.getToolbar().getFullButton().setEnabled(false);
      this.kvmInterface.getToolbar().getImageButton().setEnabled(true);
    } 
    if (this.kvmInterface.getBladeSize() > 1) {
      this.kvmInterface.getToolbar()
        .getDisConnectBladeButton()
        .setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), 
            BorderFactory.createLineBorder(Color.WHITE)));
      this.kvmInterface.getToolbar()
        .getCombineKey()
        .setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), 
            BorderFactory.createLineBorder(Color.WHITE)));
      this.kvmInterface.getToolbar()
        .getFullButton()
        .setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), 
            BorderFactory.createLineBorder(Color.WHITE)));
      this.kvmInterface.getToolbar()
        .getRefreshButton()
        .setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), 
            BorderFactory.createLineBorder(Color.WHITE)));
      this.kvmInterface.getToolbar()
        .getImageButton()
        .setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 3, 5, 3), 
            BorderFactory.createLineBorder(Color.WHITE)));
      this.kvmInterface.getToolbar()
        .getHelpButton()
        .setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 3, 5, 3), 
            BorderFactory.createLineBorder(Color.WHITE)));
    } 
  }
  public void setFullToolBar(boolean isDiv) {
    if (isDiv) {
      this.kvmInterface.getFullScreen().getToolBar().removeAll();
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getMouseSynButton());
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getCombineKey());
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getMouseModeButton());
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getReturnButton());
      if (this.kvmInterface.getFloatToolbar().isVirtualMedia()) {
        this.kvmInterface.getFullScreen().getToolBar()
          .add(this.kvmInterface.getFullScreen().getToolBar().getBtnCDMenu());
        this.kvmInterface.getFullScreen().getToolBar()
          .add(this.kvmInterface.getFullScreen().getToolBar().getBtnFlpMenu());
      } 
      if (null != this.kvmInterface.getFullScreen().getToolBar().getPowerMenuButton())
      {
        this.kvmInterface.getFullScreen().getToolBar()
          .add(this.kvmInterface.getFullScreen().getToolBar().getPowerMenuButton());
      }
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getHelpButton());
      this.kvmInterface.getFullScreen().getToolBar().getMouseSynButton().setEnabled(false);
      this.kvmInterface.getFullScreen().getToolBar().getCombineKey().setEnabled(false);
      this.kvmInterface.getFullScreen().getToolBar().getMouseModeButton().setEnabled(false);
    }
    else {
      this.kvmInterface.getFullScreen().getToolBar().removeAll();
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getMouseSynButton());
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getCombineKey());
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getMouseModeButton());
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getReturnButton());
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getBtnCreateImage());
      if (this.kvmInterface.getFloatToolbar().isVirtualMedia()) {
        this.kvmInterface.getFullScreen().getToolBar()
          .add(this.kvmInterface.getFullScreen().getToolBar().getBtnCDMenu());
        this.kvmInterface.getFullScreen().getToolBar().getBtnCDMenu().setEnabled(true);
        this.kvmInterface.getFullScreen().getToolBar()
          .add(this.kvmInterface.getFullScreen().getToolBar().getBtnFlpMenu());
        this.kvmInterface.getFullScreen().getToolBar().getBtnFlpMenu().setEnabled(true);
      } 
      if (null != this.kvmInterface.getFullScreen().getToolBar().getPowerMenuButton()) {
        this.kvmInterface.getFullScreen().getToolBar()
          .add(this.kvmInterface.getFullScreen().getToolBar().getPowerMenuButton());
        this.kvmInterface.getFullScreen().getToolBar().getPowerMenuButton().setEnabled(true);
      } 
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getHelpButton());
      this.kvmInterface.getFullScreen().getToolBar().getHelpButton().setEnabled(true);
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getLabelnum());
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getNumColorButton());
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getLabelcaps());
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getCapsColorButton());
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getLabelscroll());
      this.kvmInterface.getFullScreen().getToolBar()
        .add(this.kvmInterface.getFullScreen().getToolBar().getScrollColorButton());
      this.kvmInterface.getFullScreen().getToolBar().getMouseSynButton().setEnabled(true);
      this.kvmInterface.getFullScreen().getToolBar().getCombineKey().setEnabled(true);
      this.kvmInterface.getFullScreen().getToolBar().getMouseModeButton().setEnabled(true);
      this.kvmInterface.getFullScreen().getToolBar().getMouseMenuButton().setEnabled(true);
    } 
  }
  public void returnToWin() {
    this.kvmInterface.getFullScreen().getImageParentScrollPane().removeAll();
    while (this.kvmInterface.getFullScreen().isVisible())
    {
      this.kvmInterface.getFullScreen().setVisible(false);
    }
    this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(false);
    this.kvmInterface.setFullScreen(false);
    String name = "";
    int threadNum = this.kvmInterface.getBase().getTabbedList().size();
    for (int i = 0; i < threadNum; i++) {
      name = (String)this.kvmInterface.getBase().getTabbedList().get(i);
      DrawThread thread = ((BladeThread)this.kvmInterface.getBase().getThreadGroup().get(name)).getDrawThread();
      if (!thread.isDisplay()) {
        if (this.kvmInterface.getBladeList().size() == 1)
        {
          this.kvmInterface.getTabbedpane().add(getImagePane(thread.getBladeNoByDrawThread()));
        }
        else
        {
          this.kvmInterface.getTabbedpane().add("blade" + thread.getBladeNoByDrawThread(), 
              getImagePane(thread.getBladeNoByDrawThread()));
        }
      }
      else {
        if (this.kvmInterface.getBladeList().size() == 1) {
          this.kvmInterface.getTabbedpane().add(getImagePane(thread.getBladeNoByDrawThread()));
        }
        else {
          this.kvmInterface.getTabbedpane().add("blade" + thread.getBladeNoByDrawThread(), 
              getImagePane(thread.getBladeNoByDrawThread()));
        } 
        this.kvmInterface.setActionBlade(thread.getBladeNoByDrawThread());
      } 
    } 
    this.kvmInterface.getTabbedpane().setSelectedComponent(getImagePane(this.kvmInterface.getActionBlade()));
    setDrawDisplay(false);
    DrawThread drawThread = ((BladeThread)this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(this.kvmInterface.getActionBlade()))).getDrawThread();
    drawThread.setDisplay(true);
    drawThread.getBladeCommu().sentData(this.kvmInterface.getPackData().contrRate(35, drawThread
          .getBladeNum()));
    ImagePane imagePane = getImagePane(this.kvmInterface.getActionBlade());
    this.kvmInterface.getTabbedpane().setPreferredSize(new Dimension(imagePane.getImagePaneWidth() + 2, imagePane
          .getImagePaneHeight() + 29));
    if (imagePane.isContr()) {
      setNumKeyColor(imagePane.getNum());
      setCapsKeyColor(imagePane.getCaps());
      setScrollKeyColor(imagePane.getScroll());
    }
    else {
      setMoniKeyState(this.kvmInterface.isFullScreen());
    } 
    this.kvmInterface.getFullScreen().setCursor(this.kvmInterface.getBase().getDefCursor());
    imagePane.setCursor(this.kvmInterface.getBase().getDefCursor());
    if (null != this.kvmInterface.getToolbar().getMouseSynButton())
    {
      if (imagePane.isNew()) {
        if (this.kvmInterface.getBase().isMstsc())
        {
          this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(true);
        }
        else
        {
          this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(false);
        }
      } else {
        this.kvmInterface.getToolbar().getMouseSynButton().setEnabled(true);
      } 
    }
    imagePane.setVisible(true);
    imagePane.requestFocus();
    this.kvmInterface.getFloatToolbar().startStateMenu();
    this.kvmInterface.setReturnToWin(true);
    this.kvmInterface.getFloatToolbar().setVisible(true);
    if (Base.isSingleMouse()) {
      this.kvmInterface.getFloatToolbar().getPowerMenu().getSingleMouseMenu().setSelected(true);
    }
    else {
      this.kvmInterface.getFloatToolbar().getPowerMenu().getSingleMouseMenu().setSelected(false);
    } 
    if (Base.getKeyboardLayout() == 1) {
      (this.kvmInterface.getFloatToolbar().getKeyboardLayoutMenu()).selfAdaptionMenu.setSelected(true);
      (this.kvmInterface.getFloatToolbar().getKeyboardLayoutMenu()).japaneseKeyboardMenu.setSelected(false);
      (this.kvmInterface.getFloatToolbar().getKeyboardLayoutMenu()).frenchKeyboardMenu.setSelected(false);
    }
    else if (Base.getKeyboardLayout() == 2) {
      (this.kvmInterface.getFloatToolbar().getKeyboardLayoutMenu()).selfAdaptionMenu.setSelected(false);
      (this.kvmInterface.getFloatToolbar().getKeyboardLayoutMenu()).japaneseKeyboardMenu.setSelected(true);
      (this.kvmInterface.getFloatToolbar().getKeyboardLayoutMenu()).frenchKeyboardMenu.setSelected(false);
    }
    else {
      (this.kvmInterface.getFloatToolbar().getKeyboardLayoutMenu()).selfAdaptionMenu.setSelected(false);
      (this.kvmInterface.getFloatToolbar().getKeyboardLayoutMenu()).japaneseKeyboardMenu.setSelected(false);
      (this.kvmInterface.getFloatToolbar().getKeyboardLayoutMenu()).frenchKeyboardMenu.setSelected(true);
    } 
    this.kvmInterface.getFloatToolbar()
      .getPowerMenu()
      .setKineScopeDataCollect(this.kvmInterface.getFullScreen().getToolBar().getPowerMenu().getKineScopeDataCollect());
    if (null != this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect()) {
      if (this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect().isCollect())
      {
        (this.kvmInterface.getFloatToolbar().getPowerMenu()).localKinescopeMenu.setText(this.kvmInterface.getKvmUtil()
            .getString("Stop_KinScope"));
      }
      else
      {
        (this.kvmInterface.getFloatToolbar().getPowerMenu()).localKinescopeMenu.setText(this.kvmInterface.getKvmUtil()
            .getString("localKinescope"));
      }
    } else {
      (this.kvmInterface.getFloatToolbar().getPowerMenu()).localKinescopeMenu.setText(this.kvmInterface.getKvmUtil()
          .getString("localKinescope"));
    } 
    if (null != this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect()) {
      if (this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect().isCollect())
      {
        this.kvmInterface.getFloatToolbar()
          .getVideoButton()
          .setIcon(new ImageIcon(KVMUtil.class.getResource("resource/images/video_stop.png")));
        this.kvmInterface.getFloatToolbar()
          .getVideoButton()
          .setToolTipText(this.kvmInterface.getKvmUtil().getString("Stop_KinScope"));
      }
      else
      {
        this.kvmInterface.getFloatToolbar()
          .getVideoButton()
          .setIcon(new ImageIcon(KVMUtil.class.getResource("resource/images/video_start.png")));
        this.kvmInterface.getFloatToolbar()
          .getVideoButton()
          .setToolTipText(this.kvmInterface.getKvmUtil().getString("localKinescope"));
      }
    } else {
      this.kvmInterface.getFloatToolbar()
        .getVideoButton()
        .setIcon(new ImageIcon(KVMUtil.class.getResource("resource/images/video_start.png")));
      this.kvmInterface.getFloatToolbar()
        .getVideoButton()
        .setToolTipText(this.kvmInterface.getKvmUtil().getString("localKinescope"));
    } 
    if (!this.kvmInterface.getFloatToolbar().isShowPanel()) {
      this.kvmInterface.getFloatToolbar()
        .getBtnShow()
        .setIcon(new ImageIcon(KVMUtil.class.getResource("resource/images/float2.gif")));
    }
    else {
      this.kvmInterface.getFloatToolbar()
        .getBtnShow()
        .setIcon(new ImageIcon(KVMUtil.class.getResource("resource/images/float.gif")));
    } 
    this.kvmInterface.getFullScreen().setVisible(false);
    if (this.kvmInterface.getFloatToolbar().isVirtualMedia()) {
      this.kvmInterface.getFullScreen().getCdMenu().setVisible(false);
      this.kvmInterface.getFullScreen().getFlpMenu().setVisible(false);
      this.kvmInterface.getFloatToolbar().setVirtualMediaVisible(false, false);
      imagePane.add(this.kvmInterface.getFloatToolbar().getCDPanel());
      imagePane.add(this.kvmInterface.getFloatToolbar().getFlpPanel());
      this.kvmInterface.getFloatToolbar().setShowingCD(false);
      this.kvmInterface.getFloatToolbar().setShowingFlp(false);
    } 
    this.kvmInterface.getFullScreen().getImageMenu().setVisible(false);
    this.kvmInterface.getImageFile().setVisible(false);
    imagePane.add(this.kvmInterface.getImageFile());
    this.kvmInterface.getFloatToolbar().setShowingImagep(false);
    if (null != this.kvmInterface.getFloatToolbar().getHelpFrm())
    {
      this.kvmInterface.getFloatToolbar().getHelpFrm().setAlwaysOnTop(false);
    }
    if (this.kvmInterface.getToolFrame() != null)
    {
      this.kvmInterface.getToolFrame().setVisible(false);
    }
  }
  public static void startMouseList(ImagePane imagePane) {
    if (!Base.getIsSynMouse()) {
      imagePane.getMouseTimerTask().setName("MouseTimer");
      imagePane.getMouseTimerTask().start();
    } 
  }
  public static void startReceiveList(ImagePane imagePane) {
    imagePane.setReceiveList(new Timer("ReceiveTimer", true));
    imagePane.getReceiveList().schedule(imagePane.getStatReceiveTask(), 0L, 10000L);
  }
  public final ActionListener taskPerformer = new TaskPerformAction(this);
  public final ChangeListener changeListener = new KVMChangeListener(this);
  public void setMoniKeyState(boolean isFullScreen) {
    if (isFullScreen) {
      this.kvmInterface.getFullScreen().getToolBar().getNumColorButton().setBackground(Color.GRAY);
      this.kvmInterface.getFullScreen().getToolBar().getCapsColorButton().setBackground(Color.GRAY);
      this.kvmInterface.getFullScreen().getToolBar().getScrollColorButton().setBackground(Color.GRAY);
    }
    else {
      this.kvmInterface.getToolbar().getNumColorButton().setBackground(Color.GRAY);
      this.kvmInterface.getToolbar().getCapsColorButton().setBackground(Color.GRAY);
      this.kvmInterface.getToolbar().getScrollColorButton().setBackground(Color.GRAY);
    } 
  }
  public static int translateToUSBCode(KeyEvent e) {
    int usbCode = 0;
    if (e.isActionKey() || e.getKeyLocation() == 4 || 17 == e.getKeyCode() || 16 == e
      .getKeyCode() || 18 == e.getKeyCode() || 65406 == e
      .getKeyCode()) {
      usbCode = javaCodeToUSB(e);
    }
    else if (!e.isActionKey()) {
      int scancode = 0;
      if (isWindowsOS()) {
        scancode = MouseDisplacementImpl.getScanCode();
        int[][] key_map = Base.getKEY_MAP();
        if (key_map != null)
        {
          for (int i = 0; i < key_map.length; i++) {
            if (scancode == key_map[i][0]) {
              usbCode = key_map[i][1];
              break;
            } 
          } 
        }
      } else if (isMacOS() || isUnix()) {
        usbCode = javaCodeToUSB(e);
      }
      else if (isLinux()) {
        scancode = 0xFF & KeyboardImpl.getScanCode_s();
        int[][] key_map = Base.getKEY_MAP();
        if (key_map != null)
        {
          int i = 0;
          for (; i < key_map.length; i++) {
            if (scancode == key_map[i][0]) {
              usbCode = key_map[i][1];
              break;
            } 
          } 
          if (i >= key_map.length)
          {
            usbCode = javaCodeToUSB(e);
          }
        }
      }
    }
    else {
      usbCode = javaCodeToUSB(e);
    } 
    return usbCode;
  }
  public static Socket getProxySocket(String httpIp, int httpPort, String serverIp, int serverPort) throws URISyntaxException, IOException {
    return null;
  }
  public BladeState getBladStateBmc(int bladeNO) throws LibException {
    BladeState bladeState = new BladeState();
    String bladeIP = this.kvmInterface.getClient().getAddress().getHostAddress();
    int bladePort = Client.getPort();
    bladeState.setBladeIP(bladeIP);
    bladeState.setBladePort(bladePort);
    bladeState.setEnable(true);
    bladeState.setNew(true);
    return bladeState;
  }
  private static String osType = "";
  private static String osArch = "";
  private static String getOsName() {
    if (osType != null && "".equals(osType)) {
      osType = System.getProperty("os.name");
      LoggerUtil.info( "osType: "+ osType );
      if (null != osType)
      {
        osType = osType.toLowerCase(Locale.getDefault());
      }
    } 
    return osType;
  }
  public static String getOsArch() {
    if (osArch != null && "".equals(osArch)) {
      osArch = System.getProperty("os.arch");
      if (null != osArch)
      {
        osArch = osArch.toLowerCase(Locale.getDefault());
      }
    } 
    return osArch;
  }
  public static boolean isOSTypeByNmae(String oSTypeNmae) {
    if (null != getOsName() && getOsName().startsWith(oSTypeNmae))
    {
      return true;
    }
    return false;
  }
  public static boolean isWindowsOS() {
    return isOSTypeByNmae("windows");
  }
  public static boolean isUnix() {
    return isOSTypeByNmae("freebsd");
  }
  public static boolean isLinuxOS() {
    return (isOSTypeByNmae("linux") || isOSTypeByNmae("mac os x") || isOSTypeByNmae("freebsd"));
  }
  public static boolean isLinux() {
    return isOSTypeByNmae("linux");
  }
  public static boolean isMacOS() {
    return isOSTypeByNmae("mac os x");
  }
  public static boolean isOsArchByName(String osArch) {
    if (null != getOsArch() && getOsArch().startsWith(osArch))
    {
      return true;
    }
    return false;
  }
  public void setCodeKey(byte codeKeys, int bladeNO) {
    int[] state = perBitToInt(codeKeys);
    if (state[7] == 1) {
      InterfaceContainer iContainer = null;
      for (int i = 0; i < this.kvmInterface.getBladeList().size(); i++) {
        iContainer = (InterfaceContainer)this.kvmInterface.getBladeList().get(i);
        if (iContainer != null && iContainer.getBladeNumber() == bladeNO) {
          break;
        }
        iContainer = null;
      } 
      if (state[0] == 1) {
        if (iContainer != null)
        {
          iContainer.setCodeKey(this.kvmInterface.getCodeKey());
        }
      }
      else if (iContainer != null) {
        iContainer.setCodeKey(0);
      } 
    } 
  }
  public static boolean isAdmin() {
    if (Base.getPrivilege() == 4 || 
      Base.getPrivilege() == 3)
    {
      return true;
    }
    return false;
  }
}
