package com.kvmV1;
import com.library.LoggerUtil;
import com.newKvm.KeyboardImpl;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Timer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class KVMUtil
{
  public byte[] bladePreInfo = new byte[2];
  public int times = 0;
  public boolean isConn = false;
  public int frame = 0;
  public int tempFrame = 0;
  public byte number = 0;
  public byte tempNumber = 0;
  public boolean tempDiff = false;
  public boolean dispDiff = false;
  public int remoteX = 0;
  public int remoteY = 0;
  public byte colorBit = 0;
  public ArrayList<byte[]> diviBuff = new ArrayList<byte[]>();
  public byte[] imageData = null;
  public static CCrc crc = new CCrc("CRC_16_H");
  public String resourcePath = "com.kvm.resource.KVMResource";
  public boolean resolutionCh = false;
  public boolean firstJudge = true;
  public ArrayList<ImagePane> imagePaneList = new ArrayList<ImagePane>();
  public int imageWidth;
  public int imageHeight;
  private boolean diff = false;
  private Object[] bufferA = null;
  private Object[] bufferB = null;
  private int lenA = 0;
  private int lenB = 0;
  public byte nowDisplay = 0;
  public ArrayList<byte[]> resultDivi = new ArrayList<byte[]>();
  private ResourceBundle bundle = null;
  private int packLenght = 0;
  private int packSum = 0;
  private int packTempSum = 0;
  private int packTempLenght = 0;
  private byte[] zipImageData = null;
  private static final String BLADE = "blade";
  private static final int NUM_LOCATION = 4;
  private static final String FAKE_BLADENO = "100";
  private BladeThread bladeThread = null;
  public BladeThread getBladeThread() {
    return this.bladeThread;
  }
  private static final int[][] keyCode = new int[][] { { 10, 40 }, { 8, 42 }, { 9, 43 }, { 3, 155 }, { 12, 156 }, { 16, 225 }, { 17, 224 }, { 18, 226 }, { 19, 72 }, { 20, 57 }, { 27, 41 }, { 32, 44 }, { 33, 75 }, { 34, 78 }, { 35, 77 }, { 36, 74 }, { 37, 80 }, { 38, 82 }, { 39, 79 }, { 40, 81 }, { 44, 54 }, { 45, 45 }, { 46, 55 }, { 47, 56 }, { 48, 39 }, { 49, 30 }, { 50, 31 }, { 51, 32 }, { 52, 33 }, { 53, 34 }, { 54, 35 }, { 55, 36 }, { 56, 37 }, { 57, 38 }, { 192, 53 }, { 59, 51 }, { 222, 52 }, { 61, 46 }, { 65, 4 }, { 66, 5 }, { 67, 6 }, { 68, 7 }, { 69, 8 }, { 70, 9 }, { 71, 10 }, { 72, 11 }, { 73, 12 }, { 74, 13 }, { 75, 14 }, { 76, 15 }, { 77, 16 }, { 78, 17 }, { 79, 18 }, { 80, 19 }, { 81, 20 }, { 82, 21 }, { 83, 22 }, { 84, 23 }, { 85, 24 }, { 86, 25 }, { 87, 26 }, { 88, 27 }, { 89, 28 }, { 90, 29 }, { 96, 98 }, { 97, 89 }, { 98, 90 }, { 99, 91 }, { 100, 92 }, { 101, 93 }, { 102, 94 }, { 103, 95 }, { 104, 96 }, { 105, 97 }, { 106, 85 }, { 107, 87 }, { 109, 86 }, { 110, 99 }, { 111, 84 }, { 127, 76 }, { 144, 83 }, { 145, 71 }, { 112, 58 }, { 113, 59 }, { 114, 60 }, { 115, 61 }, { 116, 62 }, { 117, 63 }, { 118, 64 }, { 119, 65 }, { 120, 66 }, { 121, 67 }, { 122, 68 }, { 123, 69 }, { 154, 70 }, { 155, 73 }, { 91, 47 }, { 92, 49 }, { 93, 48 } };
  private KVMInterface kvmInterface = null;
  public UnPackData unPack = null;
  public int start = 0;
  public int state = 0;
  public int resultStart = 0, head = 0;
  public int dlen = 0; public int rdlen = 0;
  public byte[] result = null;
  public boolean isReqFrame = false;
  public long startTime = 0L;
  private int iWindosFocus = 0;
  public int getiWindosFocus() {
    return this.iWindosFocus;
  }
  public void setiWindosFocus(int iWindosFocus) {
    this.iWindosFocus = iWindosFocus;
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public void setUnPack(UnPackData unPack) {
    this.unPack = unPack;
  }
  public void setImageData(byte[] imageData) {
    this.imageData = imageData;
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
    for (int i = 0; i < keyCode.length; i++) {
      if (keyCode[i][0] == keyValue)
      {
        temp = keyCode[i][1];
      }
    } 
    return temp;
  }
  private static int numKey(KeyEvent e) {
    int keyValue = 0;
    switch (e.getKeyCode())
    { case 155:
        keyValue = 96;
        return keyValue;case 127: keyValue = 110; return keyValue;case 35: keyValue = 97; return keyValue;case 40: case 225: keyValue = 98; return keyValue;case 34: keyValue = 99; return keyValue;case 37: case 226: keyValue = 100; return keyValue;case 12: keyValue = 101; return keyValue;case 39: case 227: keyValue = 102; return keyValue;case 36: keyValue = 103; return keyValue;case 38: case 224: keyValue = 104; return keyValue;case 33: keyValue = 105; return keyValue; }  keyValue = 0; return keyValue;
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
  public static void perIntToByteCon(byte[] bytedest, int offset, byte[] bytesrc) {
    int count = bytesrc.length / 4;
    int t_offset = 0;
    for (int i = 0; i < count; i++) {
      t_offset = offset + i * 4;
      bytedest[t_offset + 3] = bytesrc[i * 4 + 0];
      bytedest[t_offset + 2] = bytesrc[i * 4 + 1];
      bytedest[t_offset + 1] = bytesrc[i * 4 + 2];
      bytedest[t_offset + 0] = bytesrc[i * 4 + 3];
    } 
  }
  public static byte[] intToByte_ret(int intsrc) {
    byte[] bytedest = new byte[4];
    bytedest[0] = (byte)intsrc;
    bytedest[1] = (byte)(intsrc >> 8);
    bytedest[2] = (byte)(intsrc >> 16);
    bytedest[3] = (byte)(intsrc >> 24);
    return bytedest;
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
    JButton blade = new JButton(new ImageIcon(getClass().getResource(imagesRes.trim())));
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
    this.bufferA = null;
    Object[] C = this.bufferA;
    this.bufferA = this.bufferB;
    this.bufferB = C;
    this.frame = this.tempFrame;
    this.number = this.tempNumber;
    this.packLenght = this.packTempLenght;
    this.packSum = this.packTempSum;
    this.diff = this.tempDiff;
    this.tempFrame = 0;
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
    if (!this.kvmInterface.base.isDiv) {
      this.remoteX = byteToIntCon(buf, 12, 2);
      this.remoteY = byteToIntCon(buf, 14, 2);
    } 
    this.colorBit = buf[16];
    data[0] = k;
    for (i = 1; i < list.length; i++) {
      byte[] bytes0 = (byte[])list[i];
      int temLen = bytes0.length - 3;
      System.arraycopy(bytes0, 3, data, index, temLen);
      index += temLen;
    } 
    return data;
  }
  public boolean xorData(byte[] dataA, byte[] dataB) {
    boolean flage = false;
    if (dataA.length != dataB.length) {
      Debug.printExc("dataA = " + dataA.length + " dataB = " + dataB.length);
    }
    else {
      int len = dataB.length;
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
      this.frame = byteToIntCon(bytes, 0, 2);
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
      } catch (ArrayIndexOutOfBoundsException e) {
        this.diff = false;
      }
    } else {
      this.tempFrame = byteToIntCon(bytes, 0, 2);
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
    this.frame = 0;
    this.tempFrame = 0;
    this.diff = false;
    this.tempDiff = false;
  }
  private void sentIFrame(int bladeNo, BladeCommu bladeCommu) {
    Debug.println("I frame request...");
    if (this.startTime == 0L) {
      this.startTime = System.currentTimeMillis();
    }
    else if (System.currentTimeMillis() - this.startTime > 500L) {
      bladeCommu.sentData(this.kvmInterface.packData.resendData(bladeNo));
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
        Debug.println("buffA discontinuity this = " + this.number + " nowDisplay =  " + this.nowDisplay);
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
      this.isReqFrame = false;
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
            this.isReqFrame = false;
            judge = true;
          }
        }
      } else {
        resetBuf();
        sentIFrame(bladeNo, bladeCommu);
        Debug.println("third image");
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
    BladeThread bladeThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
    if (bladeThread == null)
    {
      return false;
    }
    BladeCommu bladeCommu = bladeThread.bladeCommu;
    boolean isComp = false;
    if (this.bufferA != null) {
      if (this.number == bytes[2])
      {
        int tem = byteToIntCon(bytes, 0, 2);
        if (tem >= this.lenA) {
          resetBuf();
          Debug.println("bufferA outof!!!");
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
            Debug.println("Number is equals, but data no!");
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
          Debug.println("bufferA isn't head nowDisplay = " + this.nowDisplay + "bytes = " + bytes[2]);
          sentIFrame(bladeNo, bladeCommu);
        }
      } else {
        int tem = this.nowDisplay - bytes[2];
        if (tem >= 128 || tem < 0) {
          Debug.println("bufferA isn't head nowDisplay = " + this.nowDisplay + "bytes = " + bytes[2]);
          sentIFrame(bladeNo, bladeCommu);
        } 
      } 
      isComp = false;
    } 
    return isComp;
  }
  public byte[] getZipImageData() throws KVMException {
    if (this.zipImageData != null)
    {
      return this.zipImageData;
    }
    throw new KVMException("zipImageData is null");
  }
  public byte[] unZipData(byte[] bytes) throws KVMException {
    int pixNumber = this.imageWidth * this.imageHeight;
    if (this.imageData.length != pixNumber)
    {
      this.imageData = new byte[pixNumber];
    }
    int countNum = 0;
    int temLength = 0;
    int bytesLenght = bytes.length;
    boolean flagRem = false;
    byte bufColor = 0;
    int i;
    for (i = 1; i < bytesLenght; i += temLength) {
      if (flagRem) {
        if (i + 1 >= bytesLenght)
        {
          return null;
        }
        bufColor = (byte)(bytes[i] | bytes[i + 1] >>> 4 & 0xF);
        byte tem = (byte)(bytes[i + 1] & 0xF);
        if (tem != 0) {
          int temInt = tem;
          if (temInt + countNum > pixNumber)
          {
            temInt = pixNumber - countNum;
          }
          int jend = temInt + countNum;
          for (int j = countNum; j < jend; j++)
          {
            this.imageData[j] = bufColor;
          }
          countNum = jend;
          if (countNum == pixNumber)
          {
            return this.imageData;
          }
          temLength = 2;
          if (bytesLenght == i + 3)
          {
            temLength = 3;
          }
          flagRem = false;
        } else {
          int extendNum6; int size6; int j; int extendNum10; int size10; int k; int extendNum18; int size18; int m; int extendNum22; int size22;
          int n;
          if (i + 2 >= bytesLenght)
          {
            return null;
          }
          switch (bytes[i + 2] & 0xC0) {
            case 0:
              if (i + 2 >= bytesLenght)
              {
                return null;
              }
              extendNum6 = bytes[i + 2] & 0x3F;
              size6 = countNum + extendNum6;
              if (size6 > pixNumber)
              {
                size6 = pixNumber;
              }
              for (j = countNum; j < size6; j++)
              {
                this.imageData[j] = bufColor;
              }
              countNum = size6;
              if (countNum == pixNumber)
              {
                return this.imageData;
              }
              temLength = 3;
              if (bytesLenght == i + 4)
              {
                temLength = 4;
              }
              flagRem = false;
              break;
            case 64:
              if (i + 3 >= bytesLenght)
              {
                return null;
              }
              extendNum10 = (bytes[i + 2] << 4 & 0x3F0) + (bytes[i + 3] >>> 4 & 0xF);
              size10 = countNum + extendNum10;
              if (size10 > pixNumber)
              {
                size10 = pixNumber;
              }
              for (k = countNum; k < size10; k++)
              {
                this.imageData[k] = bufColor;
              }
              countNum = size10;
              if (countNum == pixNumber)
              {
                return this.imageData;
              }
              temLength = 3;
              if (bytesLenght == i + 4) {
                temLength = 4;
                break;
              } 
              if (i + 3 >= bytesLenght)
              {
                return null;
              }
              bytes[i + 3] = (byte)(bytes[i + 3] << 4);
              flagRem = true;
              break;
            case 128:
              if (i + 4 >= bytesLenght)
              {
                return null;
              }
              extendNum18 = (bytes[i + 2] << 12 & 0x3F000) + (bytes[i + 3] << 4 & 0xFF0) + (bytes[i + 4] >>> 4 & 0xF);
              size18 = countNum + extendNum18;
              if (size18 > pixNumber)
              {
                size18 = pixNumber;
              }
              for (m = countNum; m < size18; m++)
              {
                this.imageData[m] = bufColor;
              }
              countNum = size18;
              if (countNum == pixNumber)
              {
                return this.imageData;
              }
              temLength = 4;
              if (bytesLenght == i + 5) {
                temLength = 5;
                break;
              } 
              if (i + 4 >= bytesLenght)
              {
                return null;
              }
              bytes[i + 4] = (byte)(bytes[i + 4] << 4);
              flagRem = true;
              break;
            case 192:
              if (i + 4 >= bytesLenght)
              {
                return null;
              }
              extendNum22 = (bytes[i + 2] << 16 & 0x3F0000) + (bytes[i + 3] << 8 & 0xFF00) + (bytes[i + 4] & 0xFF);
              size22 = countNum + extendNum22;
              if (size22 > pixNumber)
              {
                size22 = pixNumber;
              }
              for (n = countNum; n < size22; n++)
              {
                this.imageData[n] = bufColor;
              }
              countNum = size22;
              if (countNum == pixNumber)
              {
                return this.imageData;
              }
              temLength = 5;
              if (bytesLenght == i + 6)
              {
                temLength = 6;
              }
              flagRem = false;
              break;
            default:
              throw new KVMException("imagedata error");
          } 
        } 
      } else {
        bufColor = bytes[i];
        byte tem = 0;
        try {
          if (i + 1 >= bytesLenght)
          {
            return null;
          }
          tem = (byte)(bytes[i + 1] >>> 4 & 0xF);
        }
        catch (ArrayIndexOutOfBoundsException ex) {
          Debug.println("i = " + i);
        } 
        if (tem != 0) {
          int temInt = tem;
          if (temInt + countNum > pixNumber)
          {
            temInt = pixNumber - countNum;
          }
          int jend = temInt + countNum;
          for (int j = countNum; j < jend; j++)
          {
            this.imageData[j] = bufColor;
          }
          countNum = jend;
          if (countNum == pixNumber)
          {
            return this.imageData;
          }
          temLength = 1;
          if (bytesLenght == i + 2) {
            temLength = 2;
          }
          else {
            if (i + 1 >= bytesLenght)
            {
              return null;
            }
            bytes[i + 1] = (byte)(bytes[i + 1] << 4);
            flagRem = true;
          } 
        } else {
          int extendNum6; int size6; int j; int extendNum10; int size10; int k; int extendNum18; int size18; int m; int extendNum22; int size22;
          int n;
          if (i + 1 >= bytesLenght)
          {
            return null;
          }
          switch (bytes[i + 1] & 0xC) {
            case 0:
              if (i + 2 >= bytesLenght)
              {
                return null;
              }
              extendNum6 = (bytes[i + 1] << 4 & 0x30) + (bytes[i + 2] >>> 4 & 0xF);
              size6 = countNum + extendNum6;
              if (size6 > pixNumber)
              {
                size6 = pixNumber;
              }
              for (j = countNum; j < size6; j++)
              {
                this.imageData[j] = bufColor;
              }
              countNum = size6;
              if (countNum == pixNumber)
              {
                return this.imageData;
              }
              if (countNum == pixNumber)
              {
                return this.imageData;
              }
              temLength = 2;
              if (bytesLenght == i + 3) {
                temLength = 3;
                break;
              } 
              if (i + 2 >= bytesLenght)
              {
                return null;
              }
              bytes[i + 2] = (byte)(bytes[i + 2] << 4);
              flagRem = true;
              break;
            case 4:
              if (i + 2 >= bytesLenght)
              {
                return null;
              }
              extendNum10 = (bytes[i + 1] << 8 & 0x300) + (bytes[i + 2] & 0xFF);
              size10 = countNum + extendNum10;
              if (size10 > pixNumber)
              {
                size10 = pixNumber;
              }
              for (k = countNum; k < size10; k++)
              {
                this.imageData[k] = bufColor;
              }
              countNum = size10;
              if (countNum == pixNumber)
              {
                return this.imageData;
              }
              temLength = 3;
              if (bytesLenght == i + 4)
              {
                temLength = 4;
              }
              flagRem = false;
              break;
            case 8:
              if (i + 3 >= bytesLenght)
              {
                return null;
              }
              extendNum18 = (bytes[i + 1] << 16 & 0x30000) + (bytes[i + 2] << 8 & 0xFF00) + (bytes[i + 3] & 0xFF);
              size18 = countNum + extendNum18;
              if (size18 > pixNumber)
              {
                size18 = pixNumber;
              }
              for (m = countNum; m < size18; m++)
              {
                this.imageData[m] = bufColor;
              }
              countNum = size18;
              if (countNum == pixNumber)
              {
                return this.imageData;
              }
              temLength = 4;
              if (bytesLenght == i + 5)
              {
                temLength = 5;
              }
              flagRem = false;
              break;
            case 12:
              if (i + 4 >= bytesLenght)
              {
                return null;
              }
              extendNum22 = (bytes[i + 1] << 20 & 0x300000) + (bytes[i + 2] << 12 & 0xFF000) + (bytes[i + 3] << 4 & 0xFF0) + (bytes[i + 4] >>> 4 & 0xF);
              size22 = countNum + extendNum22;
              if (size22 > pixNumber)
              {
                size22 = pixNumber;
              }
              for (n = countNum; n < size22; n++)
              {
                this.imageData[n] = bufColor;
              }
              countNum = size22;
              if (countNum == pixNumber)
              {
                return this.imageData;
              }
              temLength = 4;
              if (bytesLenght == i + 5) {
                temLength = 5;
                break;
              } 
              if (i + 4 >= bytesLenght)
              {
                return null;
              }
              bytes[i + 4] = (byte)(bytes[i + 4] << 4);
              flagRem = true;
              break;
            default:
              throw new KVMException("imagedata error");
          } 
        } 
      } 
    } 
    countNum = 0;
    return this.imageData;
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
    if ("UDP".equals(IManaKVMApplet.protocol)) {
      this.resultDivi.clear();
      byte[] data = null;
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
            this.kvmInterface.client.sentData(this.kvmInterface.packData.replayToSMM(bytes[7], bytes[1]));
          }
          catch (KVMException e) {
            if ("IO_ERRCODE".equals(e.getErrCode()))
            {
              JOptionPane.showMessageDialog(this.kvmInterface.toolbar, this.kvmInterface.kvmUtil.getString("Network_interrupt_message"));
            }
          } 
        }
        data = new byte[packageLen - 2];
        System.arraycopy(bytes, 2, data, 0, packageLen - 2);
        this.resultDivi.add(data);
      }
      else {
        Debug.printByte(bytes);
      } 
    } 
    if ("TCP".equals(IManaKVMApplet.protocol))
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
    ArrayList<byte[]> resultDo = (ArrayList)new ArrayList<byte[]>();
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
                j = start;
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
    ArrayList<byte[]> resultDo = (ArrayList)new ArrayList<byte[]>();
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
          try {
            Thread.sleep(0L);
          }
          catch (InterruptedException e) {
            e.printStackTrace();
          } 
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
    try {
      value = getResourceBundle().getString(key);
    }
    catch (MissingResourceException e) {
      Debug.println("java.util.MissingResourceException: Couldn't find value for: " + key);
    } 
    if (value == null)
    {
      value = "Could not find resource: " + key + "  ";
    }
    return value;
  }
  public ResourceBundle getResourceBundle() {
    if (this.bundle == null)
    {
      if ("en".equalsIgnoreCase(Base.local)) {
        this.bundle = ResourceBundle.getBundle(this.resourcePath, new Locale("en"), ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
      }
      else {
        this.bundle = ResourceBundle.getBundle(this.resourcePath, new Locale("zh"), ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
      } 
    }
    return this.bundle;
  }
  public void setBundle(ResourceBundle bundle) {
    this.bundle = bundle;
  }
  public void setNumAndCapLock() {
    byte numAndCapLock = 0;
    int bladeNo = 0;
    if (!this.kvmInterface.isFullScreen) {
      bladeNo = this.kvmInterface.actionBlade;
    }
    else {
      bladeNo = this.kvmInterface.fullScreen.actionBlade;
    } 
    try {
      BladeThread bladeThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNo));
      numAndCapLock = ((Byte)bladeThread.keyState.get(String.valueOf(bladeNo))).byteValue();
    }
    catch (Exception e) {
      Debug.printExc(e.getMessage());
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
    } 
  }
  public void setNumKeyColor(int lock) {
    if (lock == 1) {
      if (!this.kvmInterface.isFullScreen)
      {
        this.kvmInterface.toolbar.NumColor = Base.LIGHT_ON;
        this.kvmInterface.toolbar.numColorButton.setBackground(this.kvmInterface.toolbar.NumColor);
      }
      else if (this.kvmInterface.fullScreen.toolBar != null)
      {
        this.kvmInterface.fullScreen.toolBar.numColor = Base.LIGHT_ON;
        this.kvmInterface.fullScreen.toolBar.numColorButton.setBackground(this.kvmInterface.fullScreen.toolBar.numColor);
      }
    }
    else if (!this.kvmInterface.isFullScreen) {
      this.kvmInterface.toolbar.NumColor = Base.LIGHT_OFF;
      this.kvmInterface.toolbar.numColorButton.setBackground(this.kvmInterface.toolbar.NumColor);
    }
    else if (this.kvmInterface.fullScreen.toolBar != null) {
      this.kvmInterface.fullScreen.toolBar.numColor = Base.LIGHT_OFF;
      this.kvmInterface.fullScreen.toolBar.numColorButton.setBackground(this.kvmInterface.fullScreen.toolBar.numColor);
    } 
  }
  public void setCapsKeyColor(int lock) {
    if (lock == 1) {
      if (!this.kvmInterface.isFullScreen)
      {
        this.kvmInterface.toolbar.CapsColor = Base.LIGHT_ON;
        this.kvmInterface.toolbar.capsColorButton.setBackground(this.kvmInterface.toolbar.CapsColor);
      }
      else if (this.kvmInterface.fullScreen.toolBar != null)
      {
        this.kvmInterface.fullScreen.toolBar.capsColor = Base.LIGHT_ON;
        this.kvmInterface.fullScreen.toolBar.capsColorButton.setBackground(this.kvmInterface.fullScreen.toolBar.capsColor);
      }
    }
    else if (!this.kvmInterface.isFullScreen) {
      this.kvmInterface.toolbar.CapsColor = Base.LIGHT_OFF;
      this.kvmInterface.toolbar.capsColorButton.setBackground(this.kvmInterface.toolbar.CapsColor);
    }
    else if (this.kvmInterface.fullScreen.toolBar != null) {
      this.kvmInterface.fullScreen.toolBar.capsColor = Base.LIGHT_OFF;
      this.kvmInterface.fullScreen.toolBar.capsColorButton.setBackground(this.kvmInterface.fullScreen.toolBar.capsColor);
    } 
  }
  public void setScrollKeyColor(int lock) {
    if (lock == 1) {
      if (!this.kvmInterface.isFullScreen)
      {
        this.kvmInterface.toolbar.ScrollColor = Base.LIGHT_ON;
        this.kvmInterface.toolbar.scrollColorButton.setBackground(this.kvmInterface.toolbar.ScrollColor);
      }
      else if (this.kvmInterface.fullScreen.toolBar != null)
      {
        this.kvmInterface.fullScreen.toolBar.scrollColor = Base.LIGHT_ON;
        this.kvmInterface.fullScreen.toolBar.scrollColorButton.setBackground(this.kvmInterface.fullScreen.toolBar.scrollColor);
      }
    }
    else if (!this.kvmInterface.isFullScreen) {
      this.kvmInterface.toolbar.ScrollColor = Base.LIGHT_OFF;
      this.kvmInterface.toolbar.scrollColorButton.setBackground(this.kvmInterface.toolbar.ScrollColor);
    }
    else if (this.kvmInterface.fullScreen.toolBar != null) {
      this.kvmInterface.fullScreen.toolBar.scrollColor = Base.LIGHT_OFF;
      this.kvmInterface.fullScreen.toolBar.scrollColorButton.setBackground(this.kvmInterface.fullScreen.toolBar.scrollColor);
    } 
  }
  public FloatToolbar getImageFloatToolbar(int bladeNO) {
    FloatToolbar floatKoobar = null;
    InterfaceContainer iContainer = null;
    for (int i = 0; i < this.kvmInterface.bladeList.size(); i++) {
      iContainer = this.kvmInterface.bladeList.get(i);
      if (iContainer.getBladeNO() == bladeNO)
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
    for (int i = 0; i < this.kvmInterface.bladeList.size(); i++) {
      iContainer = this.kvmInterface.bladeList.get(i);
      imagePane = iContainer.getImagePane();
      if (iContainer.getBladeNO() == bladeNO)
      {
        if (imagePane == null || (imagePane != null && imagePane.bladeNO == 0)) {
          if (Base.connMode == 0) {
            virtualMedia = new VirtualMedia(Base.local, this.kvmInterface);
            virtualMedia.setStrIP(Base.vmmConnIP);
            virtualMedia.setCodeKey(Base.vmmCodeKey);
            virtualMedia.setPort(Base.vmmPort);
            iContainer.setVirtualMedia(virtualMedia);
          } 
          imagePane = new ImagePane(this.kvmInterface);
          FloatToolbar floatToolbar = new FloatToolbar(imagePane, virtualMedia, this.kvmInterface);
          floatToolbar.setVirtualMedia(virtualMedia);
          iContainer.setImagePane(imagePane);
          iContainer.setKvmInterface(this.kvmInterface);
          iContainer.setFloatToolbar(floatToolbar);
          this.kvmInterface.floatToolbar = iContainer.getFloatToolbar();
          break;
        } 
      }
    } 
    return imagePane;
  }
  public ImagePane getImagePane(int bladeNO) {
    ImagePane imagePane = null;
    InterfaceContainer iContainer = null;
    for (int i = 0; i < this.kvmInterface.bladeList.size(); i++) {
      iContainer = this.kvmInterface.bladeList.get(i);
      imagePane = iContainer.getImagePane();
      if (iContainer.getBladeNO() == bladeNO)
      {
        if (imagePane != null && imagePane.bladeNO == bladeNO) {
          this.kvmInterface.floatToolbar = iContainer.getFloatToolbar();
          return imagePane;
        } 
      }
    } 
    return null;
  }
  public boolean getSecretKVM(int bladeNO) {
    InterfaceContainer iContainer = null;
    boolean isSecretKVM = false;
    for (int i = 0; i < this.kvmInterface.bladeList.size(); i++) {
      iContainer = this.kvmInterface.bladeList.get(i);
      if (iContainer.getBladeNO() == bladeNO)
      {
        return iContainer.getSecretKVM();
      }
    } 
    return isSecretKVM;
  }
  public boolean getSecretVMM(int bladeNO) {
    InterfaceContainer iContainer = null;
    boolean isSecretVMM = false;
    for (int i = 0; i < this.kvmInterface.bladeList.size(); i++) {
      iContainer = this.kvmInterface.bladeList.get(i);
      if (iContainer.getBladeNO() == bladeNO)
      {
        return iContainer.getSecretVMM();
      }
    } 
    return isSecretVMM;
  }
  public boolean getAuthVMM(int bladeNO) {
    InterfaceContainer iContainer = null;
    boolean isAuthVMM = true;
    for (int i = 0; i < this.kvmInterface.bladeList.size(); i++) {
      iContainer = this.kvmInterface.bladeList.get(i);
      if (iContainer.getBladeNO() == bladeNO)
      {
        return iContainer.getAuthVMM();
      }
    } 
    return isAuthVMM;
  }
  public void setVMMSecretCodeKey(int bladeNO, byte[] codeKey) {
    InterfaceContainer iContainer = null;
    VirtualMedia virtualMedia = null;
    for (int i = 0; i < this.kvmInterface.bladeList.size(); i++) {
      iContainer = this.kvmInterface.bladeList.get(i);
      if (iContainer.getBladeNO() == bladeNO) {
        virtualMedia = iContainer.getVirtualMedia();
        virtualMedia.setNegotiCodeKey(codeKey);
      } 
    } 
  }
  public void setVMMSecretSalt(int bladeNO, byte[] salt) {
    InterfaceContainer iContainer = null;
    VirtualMedia virtualMedia = null;
    for (int i = 0; i < this.kvmInterface.bladeList.size(); i++) {
      iContainer = this.kvmInterface.bladeList.get(i);
      if (iContainer.getBladeNO() == bladeNO) {
        virtualMedia = iContainer.getVirtualMedia();
        virtualMedia.setNegotiSalt(salt);
      } 
    } 
  }
  public int getImagePaneCodeKey(int bladeNO) {
    int imagePaneCodeKey = 0;
    InterfaceContainer iContainer = null;
    for (int i = 0; i < this.kvmInterface.bladeList.size(); i++) {
      iContainer = this.kvmInterface.bladeList.get(i);
      if (iContainer.getBladeNO() == bladeNO) {
        imagePaneCodeKey = iContainer.getCodeKey();
        return imagePaneCodeKey;
      } 
    } 
    return imagePaneCodeKey;
  }
  public byte[] getImagePaneCodeKey_bytes(int bladeNO) {
    int imagePaneCodeKey = 0;
    InterfaceContainer iContainer = null;
    for (int i = 0; i < this.kvmInterface.bladeList.size(); i++) {
      iContainer = this.kvmInterface.bladeList.get(i);
      if (iContainer.getBladeNO() == bladeNO) {
        imagePaneCodeKey = iContainer.getCodeKey();
        break;
      } 
    } 
    BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO));
    return (bThread != null && bThread.getEncrytedStatus()) ? Base.getSessionID() : intToByte_ret(imagePaneCodeKey);
  }
  public byte[] getImagePaneCodeKey_bytes(int bladeNO, BladeThread bThread) {
    int imagePaneCodeKey = 0;
    InterfaceContainer iContainer = null;
    for (int i = 0; i < this.kvmInterface.bladeList.size(); i++) {
      iContainer = this.kvmInterface.bladeList.get(i);
      if (iContainer.getBladeNO() == bladeNO) {
        imagePaneCodeKey = iContainer.getCodeKey();
        break;
      } 
    } 
    return (bThread != null && bThread.getEncrytedStatus()) ? Base.getSessionID() : intToByte_ret(imagePaneCodeKey);
  }
  public byte[] getSMMCodeKey_bytes() {
    return Base.securekvm ? Base.getSessionID() : intToByte_ret(this.kvmInterface.codeKey);
  }
  public ImagePane getImagePane_bak(int bladeNO) {
    if (bladeNO == 0) {
      if (this.kvmInterface.imagePane1 == null) {
        this.kvmInterface.imagePane1 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane1;
      } 
      if (this.kvmInterface.imagePane1.bladeNO == 0) {
        this.kvmInterface.imagePane1 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane1;
      } 
      if (this.kvmInterface.imagePane2 == null) {
        this.kvmInterface.imagePane2 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane2;
      } 
      if (this.kvmInterface.imagePane2.bladeNO == 0) {
        this.kvmInterface.imagePane2 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane2;
      } 
      if (this.kvmInterface.imagePane3 == null) {
        this.kvmInterface.imagePane3 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane3;
      } 
      if (this.kvmInterface.imagePane3.bladeNO == 0) {
        this.kvmInterface.imagePane3 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane3;
      } 
      if (this.kvmInterface.imagePane4 == null) {
        this.kvmInterface.imagePane4 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane4;
      } 
      if (this.kvmInterface.imagePane4.bladeNO == 0) {
        this.kvmInterface.imagePane4 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane4;
      } 
      if (this.kvmInterface.imagePane5 == null) {
        this.kvmInterface.imagePane5 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane5;
      } 
      if (this.kvmInterface.imagePane5.bladeNO == 0) {
        this.kvmInterface.imagePane5 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane5;
      } 
      if (this.kvmInterface.imagePane6 == null) {
        this.kvmInterface.imagePane6 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane6;
      } 
      if (this.kvmInterface.imagePane6.bladeNO == 0) {
        this.kvmInterface.imagePane6 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane6;
      } 
      if (this.kvmInterface.imagePane7 == null) {
        this.kvmInterface.imagePane7 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane7;
      } 
      if (this.kvmInterface.imagePane7.bladeNO == 0) {
        this.kvmInterface.imagePane7 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane7;
      } 
      if (this.kvmInterface.imagePane8 == null) {
        this.kvmInterface.imagePane8 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane8;
      } 
      if (this.kvmInterface.imagePane8.bladeNO == 0) {
        this.kvmInterface.imagePane8 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane8;
      } 
      if (this.kvmInterface.imagePane9 == null) {
        this.kvmInterface.imagePane9 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane9;
      } 
      if (this.kvmInterface.imagePane9.bladeNO == 0) {
        this.kvmInterface.imagePane9 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane9;
      } 
      if (this.kvmInterface.imagePane10 == null) {
        this.kvmInterface.imagePane10 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane10;
      } 
      if (this.kvmInterface.imagePane10.bladeNO == 0) {
        this.kvmInterface.imagePane10 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane10;
      } 
      if (this.kvmInterface.imagePane11 == null) {
        this.kvmInterface.imagePane11 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane11;
      } 
      if (this.kvmInterface.imagePane11.bladeNO == 0) {
        this.kvmInterface.imagePane11 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane11;
      } 
      if (this.kvmInterface.imagePane12 == null) {
        this.kvmInterface.imagePane12 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane12;
      } 
      if (this.kvmInterface.imagePane12.bladeNO == 0) {
        this.kvmInterface.imagePane12 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane12;
      } 
      if (this.kvmInterface.imagePane13 == null) {
        this.kvmInterface.imagePane13 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane13;
      } 
      if (this.kvmInterface.imagePane13.bladeNO == 0) {
        this.kvmInterface.imagePane13 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane13;
      } 
      if (this.kvmInterface.imagePane14 == null) {
        this.kvmInterface.imagePane14 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane14;
      } 
      if (this.kvmInterface.imagePane14.bladeNO == 0)
      {
        this.kvmInterface.imagePane14 = new ImagePane(this.kvmInterface);
        return this.kvmInterface.imagePane14;
      }
    }
    else {
      if (this.kvmInterface.imagePane1 != null && this.kvmInterface.imagePane1.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane1;
      }
      if (this.kvmInterface.imagePane2 != null && this.kvmInterface.imagePane2.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane2;
      }
      if (this.kvmInterface.imagePane3 != null && this.kvmInterface.imagePane3.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane3;
      }
      if (this.kvmInterface.imagePane4 != null && this.kvmInterface.imagePane4.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane4;
      }
      if (this.kvmInterface.imagePane5 != null && this.kvmInterface.imagePane5.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane5;
      }
      if (this.kvmInterface.imagePane6 != null && this.kvmInterface.imagePane6.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane6;
      }
      if (this.kvmInterface.imagePane7 != null && this.kvmInterface.imagePane7.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane7;
      }
      if (this.kvmInterface.imagePane8 != null && this.kvmInterface.imagePane8.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane8;
      }
      if (this.kvmInterface.imagePane9 != null && this.kvmInterface.imagePane9.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane9;
      }
      if (this.kvmInterface.imagePane10 != null && this.kvmInterface.imagePane10.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane10;
      }
      if (this.kvmInterface.imagePane11 != null && this.kvmInterface.imagePane11.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane11;
      }
      if (this.kvmInterface.imagePane12 != null && this.kvmInterface.imagePane12.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane12;
      }
      if (this.kvmInterface.imagePane13 != null && this.kvmInterface.imagePane13.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane13;
      }
      if (this.kvmInterface.imagePane14 != null && this.kvmInterface.imagePane14.bladeNO == bladeNO)
      {
        return this.kvmInterface.imagePane14;
      }
    } 
    return null;
  }
  public void setBladeButton() throws KVMException {
    if (this.kvmInterface.toolbar.dynamicBlade) {
      int bladeSize = this.kvmInterface.bladeList.size();
      for (int i = 0; i < bladeSize; i++) {
        JButton blade = ((InterfaceContainer)this.kvmInterface.bladeList.get(i)).getBladeButton();
        if (blade != null) {
          if (this.bladePreInfo[i] == 1 || this.bladePreInfo[i] == 7 || this.bladePreInfo[i] == 15 || this.bladePreInfo[i] == 23 || this.bladePreInfo[i] == 31) {
            blade.setEnabled(true);
            blade.addMouseListener(bladeMouseListener(true));
          }
          else {
            blade.setEnabled(false);
            blade.addMouseListener(bladeMouseListener(false));
          } 
          blade.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5), BorderFactory.createLineBorder(Color.WHITE)));
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
    if (this.kvmInterface.toolbar.dynamicBlade) {
      int bladeSize = this.kvmInterface.bladeList.size();
      for (int i = 0; i < bladeSize; i++) {
        JButton blade = ((InterfaceContainer)this.kvmInterface.bladeList.get(i)).getBladeButton();
        if (blade != null) {
          if (PresentInfo[i] == 1 && this.kvmInterface.base.threadGroup.get(String.valueOf(i + 1)) == null) {
            blade.setEnabled(true);
            blade.addMouseListener(bladeMouseListener(true));
          }
          else {
            blade.setEnabled(false);
            blade.addMouseListener(bladeMouseListener(false));
          } 
          blade.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5), BorderFactory.createLineBorder(Color.WHITE)));
        } 
      } 
    } 
  }
  public void arrayImagePane() {
    this.imagePaneList.clear();
    this.imagePaneList.add(this.kvmInterface.imagePane1);
    this.imagePaneList.add(this.kvmInterface.imagePane2);
    this.imagePaneList.add(this.kvmInterface.imagePane3);
    this.imagePaneList.add(this.kvmInterface.imagePane4);
    for (int i = 0; i < 4; i++) {
      for (int j = i + 1; j < 4; j++) {
        ImagePane imaPane1 = this.imagePaneList.get(i);
        ImagePane imaPane2 = this.imagePaneList.get(j);
        if (imaPane1.bladeNO > imaPane2.bladeNO) {
          ImagePane tem = imaPane1;
          this.imagePaneList.set(i, imaPane2);
          this.imagePaneList.set(j, tem);
        } 
      } 
    } 
  }
  public void setImageTipText(boolean isShow) {
    if (isShow) {
      if (this.kvmInterface.imagePane1 != null && this.kvmInterface.imagePane1.bladeNO != 0)
      {
        this.kvmInterface.imagePane1.setToolTipText("blade" + this.kvmInterface.imagePane1.bladeNO);
      }
      if (this.kvmInterface.imagePane2 != null && this.kvmInterface.imagePane2.bladeNO != 0)
      {
        this.kvmInterface.imagePane2.setToolTipText("blade" + this.kvmInterface.imagePane2.bladeNO);
      }
      if (this.kvmInterface.imagePane3 != null && this.kvmInterface.imagePane3.bladeNO != 0)
      {
        this.kvmInterface.imagePane3.setToolTipText("blade" + this.kvmInterface.imagePane3.bladeNO);
      }
      if (this.kvmInterface.imagePane4 != null && this.kvmInterface.imagePane4.bladeNO != 0)
      {
        this.kvmInterface.imagePane4.setToolTipText("blade" + this.kvmInterface.imagePane4.bladeNO);
      }
      if (this.kvmInterface.imagePane5 != null && this.kvmInterface.imagePane5.bladeNO != 0)
      {
        this.kvmInterface.imagePane5.setToolTipText("blade" + this.kvmInterface.imagePane5.bladeNO);
      }
      if (this.kvmInterface.imagePane6 != null && this.kvmInterface.imagePane6.bladeNO != 0)
      {
        this.kvmInterface.imagePane6.setToolTipText("blade" + this.kvmInterface.imagePane6.bladeNO);
      }
      if (this.kvmInterface.imagePane7 != null && this.kvmInterface.imagePane7.bladeNO != 0)
      {
        this.kvmInterface.imagePane7.setToolTipText("blade" + this.kvmInterface.imagePane7.bladeNO);
      }
      if (this.kvmInterface.imagePane8 != null && this.kvmInterface.imagePane8.bladeNO != 0)
      {
        this.kvmInterface.imagePane8.setToolTipText("blade" + this.kvmInterface.imagePane8.bladeNO);
      }
      if (this.kvmInterface.imagePane9 != null && this.kvmInterface.imagePane9.bladeNO != 0)
      {
        this.kvmInterface.imagePane9.setToolTipText("blade" + this.kvmInterface.imagePane9.bladeNO);
      }
      if (this.kvmInterface.imagePane10 != null && this.kvmInterface.imagePane10.bladeNO != 0)
      {
        this.kvmInterface.imagePane10.setToolTipText("blade" + this.kvmInterface.imagePane10.bladeNO);
      }
      if (this.kvmInterface.imagePane11 != null && this.kvmInterface.imagePane11.bladeNO != 0)
      {
        this.kvmInterface.imagePane11.setToolTipText("blade" + this.kvmInterface.imagePane11.bladeNO);
      }
      if (this.kvmInterface.imagePane12 != null && this.kvmInterface.imagePane12.bladeNO != 0)
      {
        this.kvmInterface.imagePane12.setToolTipText("blade" + this.kvmInterface.imagePane12.bladeNO);
      }
      if (this.kvmInterface.imagePane13 != null && this.kvmInterface.imagePane13.bladeNO != 0)
      {
        this.kvmInterface.imagePane13.setToolTipText("blade" + this.kvmInterface.imagePane13.bladeNO);
      }
      if (this.kvmInterface.imagePane14 != null && this.kvmInterface.imagePane14.bladeNO != 0)
      {
        this.kvmInterface.imagePane14.setToolTipText("blade" + this.kvmInterface.imagePane14.bladeNO);
      }
    }
    else {
      if (this.kvmInterface.imagePane1 != null)
      {
        this.kvmInterface.imagePane1.setToolTipText((String)null);
      }
      if (this.kvmInterface.imagePane2 != null)
      {
        this.kvmInterface.imagePane2.setToolTipText((String)null);
      }
      if (this.kvmInterface.imagePane3 != null)
      {
        this.kvmInterface.imagePane3.setToolTipText((String)null);
      }
      if (this.kvmInterface.imagePane4 != null)
      {
        this.kvmInterface.imagePane4.setToolTipText((String)null);
      }
      if (this.kvmInterface.imagePane5 != null)
      {
        this.kvmInterface.imagePane5.setToolTipText((String)null);
      }
      if (this.kvmInterface.imagePane6 != null)
      {
        this.kvmInterface.imagePane6.setToolTipText((String)null);
      }
      if (this.kvmInterface.imagePane7 != null)
      {
        this.kvmInterface.imagePane7.setToolTipText((String)null);
      }
      if (this.kvmInterface.imagePane8 != null)
      {
        this.kvmInterface.imagePane8.setToolTipText((String)null);
      }
      if (this.kvmInterface.imagePane9 != null)
      {
        this.kvmInterface.imagePane9.setToolTipText((String)null);
      }
      if (this.kvmInterface.imagePane10 != null)
      {
        this.kvmInterface.imagePane10.setToolTipText((String)null);
      }
      if (this.kvmInterface.imagePane11 != null)
      {
        this.kvmInterface.imagePane11.setToolTipText((String)null);
      }
      if (this.kvmInterface.imagePane12 != null)
      {
        this.kvmInterface.imagePane12.setToolTipText((String)null);
      }
      if (this.kvmInterface.imagePane13 != null)
      {
        this.kvmInterface.imagePane13.setToolTipText((String)null);
      }
      if (this.kvmInterface.imagePane14 != null)
      {
        this.kvmInterface.imagePane14.setToolTipText((String)null);
      }
    } 
  }
  public void setDrawDisplay(boolean isDisplay) {
    Iterator<String> iter = this.kvmInterface.base.threadGroup.keySet().iterator();
    String name = "";
    if (isDisplay) {
      while (iter.hasNext())
      {
        name = iter.next();
        BladeThread bladeThread = this.kvmInterface.base.threadGroup.get(name);
        (bladeThread.getDrawThread()).isDisplay = isDisplay;
        bladeThread.bladeCommu.sentData(this.kvmInterface.packData.contrRate(1, bladeThread.getBladeNO()));
      }
    }
    else {
      while (iter.hasNext()) {
        name = iter.next();
        BladeThread bladeThread = this.kvmInterface.base.threadGroup.get(name);
        (bladeThread.getDrawThread()).isDisplay = isDisplay;
        bladeThread.bladeCommu.sentData(this.kvmInterface.packData.contrRate(0, bladeThread.getBladeNO()));
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
  public BladeState getBladeState(int bladeNO) throws KVMException {
	return null;
    // Byte code:
    //   0: new com/kvmV1/BladeState
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_2
    //   8: iconst_0
    //   9: istore_3
    //   10: aload_0
    //   11: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   14: getfield clientSocket : Lcom/kvmV1/ClientSocketCommunity;
    //   17: getfield bladeStateInfo : Ljava/util/Hashtable;
    //   20: iload_1
    //   21: invokestatic valueOf : (I)Ljava/lang/String;
    //   24: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   27: pop
    //   28: aload_0
    //   29: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   32: getfield client : Lcom/kvmV1/Client;
    //   35: aload_0
    //   36: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   39: getfield packData : Lcom/kvmV1/PackData;
    //   42: iload_1
    //   43: getstatic com/kvmV1/Base.connMode : I
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
    //   71: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   74: getfield toolbar : Lcom/kvmV1/KVMInterface$KvmToolBar;
    //   77: aload_0
    //   78: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   81: getfield kvmUtil : Lcom/kvmV1/KVMUtil;
    //   84: ldc 'Network_interrupt_message'
    //   86: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   89: invokestatic showMessageDialog : (Ljava/awt/Component;Ljava/lang/Object;)V
    //   92: new com/kvmV1/KVMException
    //   95: dup
    //   96: invokespecial <init> : ()V
    //   99: athrow
    //   100: iload_3
    //   101: bipush #15
    //   103: if_icmpge -> 424
    //   106: aload_0
    //   107: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   110: getfield clientSocket : Lcom/kvmV1/ClientSocketCommunity;
    //   113: getfield bladeStateInfo : Ljava/util/Hashtable;
    //   116: iload_1
    //   117: invokestatic valueOf : (I)Ljava/lang/String;
    //   120: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   123: ifnonnull -> 203
    //   126: aload_0
    //   127: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   130: getfield clientSocket : Lcom/kvmV1/ClientSocketCommunity;
    //   133: getfield bladeStateInfo : Ljava/util/Hashtable;
    //   136: ldc_w '100'
    //   139: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   142: ifnonnull -> 203
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
    //   184: goto -> 197
    //   187: astore #4
    //   189: aload #4
    //   191: invokevirtual getMessage : ()Ljava/lang/String;
    //   194: invokestatic printExc : (Ljava/lang/String;)V
    //   197: iinc #3, 1
    //   200: goto -> 100
    //   203: aload_0
    //   204: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   207: getfield clientSocket : Lcom/kvmV1/ClientSocketCommunity;
    //   210: getfield bladeStateInfo : Ljava/util/Hashtable;
    //   213: iload_1
    //   214: invokestatic valueOf : (I)Ljava/lang/String;
    //   217: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   220: ifnull -> 318
    //   223: aload_0
    //   224: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   227: getfield clientSocket : Lcom/kvmV1/ClientSocketCommunity;
    //   230: getfield bladeStateInfo : Ljava/util/Hashtable;
    //   233: iload_1
    //   234: invokestatic valueOf : (I)Ljava/lang/String;
    //   237: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   240: checkcast [B
    //   243: checkcast [B
    //   246: astore #4
    //   248: aload #4
    //   250: iconst_1
    //   251: baload
    //   252: invokestatic perBitToInt : (B)[I
    //   255: astore #5
    //   257: aload #5
    //   259: iconst_2
    //   260: iaload
    //   261: iconst_1
    //   262: if_icmpne -> 296
    //   265: aload_0
    //   266: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   269: getfield toolbar : Lcom/kvmV1/KVMInterface$KvmToolBar;
    //   272: aload_0
    //   273: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   276: getfield kvmUtil : Lcom/kvmV1/KVMUtil;
    //   279: ldc_w 'BMC_reset_message'
    //   282: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   285: invokestatic showMessageDialog : (Ljava/awt/Component;Ljava/lang/Object;)V
    //   288: aload_2
    //   289: iconst_0
    //   290: invokevirtual setEnable : (Z)V
    //   293: goto -> 305
    //   296: aload_0
    //   297: aload #4
    //   299: iload_1
    //   300: iconst_0
    //   301: invokespecial showBladeAbsent : ([BII)Lcom/kvmV1/BladeState;
    //   304: astore_2
    //   305: aload_0
    //   306: aload #4
    //   308: aload #4
    //   310: arraylength
    //   311: iconst_1
    //   312: isub
    //   313: baload
    //   314: iload_1
    //   315: invokevirtual setCodeKey : (BI)V
    //   318: aload_0
    //   319: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   322: getfield clientSocket : Lcom/kvmV1/ClientSocketCommunity;
    //   325: getfield bladeStateInfo : Ljava/util/Hashtable;
    //   328: ldc_w '100'
    //   331: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   334: ifnull -> 444
    //   337: aload_0
    //   338: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   341: getfield clientSocket : Lcom/kvmV1/ClientSocketCommunity;
    //   344: getfield bladeStateInfo : Ljava/util/Hashtable;
    //   347: ldc_w '100'
    //   350: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   353: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   356: checkcast [B
    //   359: checkcast [B
    //   362: astore #4
    //   364: aload #4
    //   366: iconst_1
    //   367: baload
    //   368: invokestatic perBitToInt : (B)[I
    //   371: astore #5
    //   373: aload #5
    //   375: iconst_2
    //   376: iaload
    //   377: iconst_1
    //   378: if_icmpne -> 412
    //   381: aload_0
    //   382: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   385: getfield toolbar : Lcom/kvmV1/KVMInterface$KvmToolBar;
    //   388: aload_0
    //   389: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   392: getfield kvmUtil : Lcom/kvmV1/KVMUtil;
    //   395: ldc_w 'BMC_reset_message'
    //   398: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   401: invokestatic showMessageDialog : (Ljava/awt/Component;Ljava/lang/Object;)V
    //   404: aload_2
    //   405: iconst_0
    //   406: invokevirtual setEnable : (Z)V
    //   409: goto -> 421
    //   412: aload_0
    //   413: aload #4
    //   415: iload_1
    //   416: iconst_0
    //   417: invokespecial showBladeAbsent : ([BII)Lcom/kvmV1/BladeState;
    //   420: astore_2
    //   421: goto -> 444
    //   424: aload_0
    //   425: getfield kvmInterface : Lcom/kvmV1/KVMInterface;
    //   428: getfield toolbar : Lcom/kvmV1/KVMInterface$KvmToolBar;
    //   431: aload_0
    //   432: ldc_w 'timeout'
    //   435: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   438: invokestatic showMessageDialog : (Ljava/awt/Component;Ljava/lang/Object;)V
    //   441: goto -> 444
    //   444: aload_2
    //   445: areturn
    // Line number table:
    //   Java source line number -> byte code offset
    //   #3108	-> 0
    //   #3109	-> 8
    //   #3110	-> 10
    //   #3113	-> 28
    //   #3123	-> 52
    //   #3115	-> 55
    //   #3117	-> 57
    //   #3119	-> 70
    //   #3122	-> 92
    //   #3127	-> 100
    //   #3130	-> 106
    //   #3133	-> 145
    //   #3136	-> 178
    //   #3141	-> 184
    //   #3138	-> 187
    //   #3140	-> 189
    //   #3142	-> 197
    //   #3147	-> 203
    //   #3149	-> 223
    //   #3151	-> 248
    //   #3152	-> 257
    //   #3154	-> 265
    //   #3156	-> 288
    //   #3160	-> 296
    //   #3168	-> 305
    //   #3170	-> 318
    //   #3172	-> 337
    //   #3174	-> 364
    //   #3175	-> 373
    //   #3177	-> 381
    //   #3179	-> 404
    //   #3183	-> 412
    //   #3186	-> 421
    //   #3193	-> 424
    //   #3194	-> 441
    //   #3197	-> 444
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   57	43	4	e	Lcom/kvmV1/KVMException;
    //   189	8	4	e	Ljava/lang/InterruptedException;
    //   248	70	4	stateBytes	[B
    //   257	61	5	state	[I
    //   364	57	4	stateBytes	[B
    //   373	48	5	state	[I
    //   0	446	0	this	Lcom/kvmV1/KVMUtil;
    //   0	446	1	bladeNO	I
    //   8	438	2	bladeState	Lcom/kvmV1/BladeState;
    //   10	436	3	count	I
    // Exception table:
    //   from	to	target	type
    //   28	52	55	com/kvmV1/KVMException
    //   178	184	187	java/lang/InterruptedException
  }
  private BladeState showBladeAbsent(byte[] bytes, int bladeNO, int count) {
    BladeState bladeState = new BladeState();
    if (bytes[0] == 100) {
      if (count <= 0)
      {
        JOptionPane.showMessageDialog(this.kvmInterface.toolbar, getString("SMMMaxContrConnection"));
      }
      return bladeState;
    } 
    int[] state = perBitToInt(bytes[1]);
    if (state[7] == 0) {
      if (count <= 0)
      {
        JOptionPane.showMessageDialog(this.kvmInterface.toolbar, getString("Absent"));
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
        JOptionPane.showMessageDialog(this.kvmInterface.toolbar, getString("nonsupportKVM"));
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
        String point = ".";
        StringBuffer strBuf = new StringBuffer("");
        strBuf.append(tem1);
        strBuf.append(point);
        strBuf.append(tem2);
        strBuf.append(point);
        strBuf.append(tem3);
        strBuf.append(point);
        strBuf.append(tem4);
        String bladeIP = strBuf.toString();
        if (Base.connMode == 1) {
          String bladeIP1 = this.kvmInterface.client.address.getHostAddress();
          if (bladeIP != null && !bladeIP.equals(bladeIP1))
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
        JOptionPane.showMessageDialog(this.kvmInterface.toolbar, getString("KVM_now"));
        bladeState.setEnable(false);
        return bladeState;
      }
    }
    else {
      String bladeIP = this.kvmInterface.client.address.getHostAddress();
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
          JOptionPane.showMessageDialog(this.kvmInterface.toolbar, getString("over_bladeconnect"));
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
          JOptionPane.showMessageDialog(this.kvmInterface.toolbar, getString("KVM_now"));
        }
      }
    }
    else if (state[2] == 0 && state[1] == 1 && state[0] == 0) {
      if (count <= 0)
      {
        JOptionPane.showMessageDialog(this.kvmInterface.toolbar, getString("SOL_now"));
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
        JOptionPane.showMessageDialog(this.kvmInterface.toolbar, getString("load_file"));
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
  public void connectNewBlade(int bladeNO, boolean isControl, String bladeIP, int port, boolean isNew, boolean secureKvm) throws KVMException {
    Debug.println("connectNewBlade::::::::" + bladeNO);
    clickButton(bladeNO, false);
    Debug.println("connect ip :" + bladeIP + " port :" + port);
    try {
      this.bladeThread = new BladeThread(bladeIP, port, bladeNO, isNew);
      this.bladeThread.setName("BladeThread" + bladeNO);
      this.bladeThread.setEncrytedStatus(secureKvm);
    }
    catch (KVMException e) {
      if (this.kvmInterface.getBladeSize() == 1) {
        JOptionPane.showMessageDialog(this.kvmInterface.toolbar, this.kvmInterface.kvmUtil.getString("Connect_lost_message_one") + " " + bladeIP);
      }
      else {
        JOptionPane.showMessageDialog(this.kvmInterface.toolbar, this.kvmInterface.kvmUtil.getString("Connect_lost_message") + " " + bladeIP);
      } 
      clickButton(bladeNO, true);
      return;
    } 
    String bladeKey = Integer.toString(bladeNO);
    this.kvmInterface.base.threadGroup.put(bladeKey, this.bladeThread);
    KVMUtil kvmUtil = new KVMUtil();
    this.bladeThread.setKvmUtil(kvmUtil);
    this.bladeThread.setKvmInterface(this.kvmInterface);
    this.bladeThread.bladeCommu.setClient(this.kvmInterface.client);
    this.bladeThread.setUnPackData(new UnPackData());
    this.bladeThread.setBladeNO(bladeNO);
    this.bladeThread.bladeCommu.setBladeNO(bladeNO);
    this.bladeThread.bladeCommu.setKvmInterface(this.kvmInterface);
    kvmUtil.setKvmInterface(this.kvmInterface);
    kvmUtil.setImageData(this.imageData);
    kvmUtil.firstJudge = true;
    ImagePane imagePane = getNewImagePane(bladeNO);
    if (getImageFloatToolbar(bladeNO) != null && getImageFloatToolbar(bladeNO).getVirtualMedia() != null) {
      getImageFloatToolbar(bladeNO).getVirtualMedia().setStrIP(bladeIP);
      Debug.println(" vmm link IP :" + bladeIP);
    } 
    imagePane.setCursor(this.kvmInterface.base.myCursor);
    PackData pack = new PackData();
    pack.setKvmInterface(this.kvmInterface);
    pack.setPackLenHead(this.bladeThread.getEncrytedStatus());
    imagePane.setPack(pack);
    imagePane.bladeNO = bladeNO;
    imagePane.setControl(isControl);
    imagePane.setBladeThread(this.bladeThread);
    imagePane.setNew(isNew);
    DrawThread drawThread = new DrawThread(bladeNO, imagePane, kvmUtil);
    this.bladeThread.setDrawThread(drawThread);
    drawThread.setName("DrawThread" + bladeKey);
    drawThread.setConn(true);
    drawThread.setTotalConn(true);
    drawThread.setKvmInterface(this.kvmInterface);
    drawThread.setBladeCommu(this.bladeThread.bladeCommu);
    this.kvmInterface.base.tabbedList.add(bladeKey);
    Iterator<String> iter = this.kvmInterface.base.threadGroup.keySet().iterator();
    String name = "";
    while (iter.hasNext()) {
      name = iter.next();
      (((BladeThread)this.kvmInterface.base.threadGroup.get(name)).getDrawThread()).isDisplay = false;
    } 
    drawThread.isDisplay = true;
    kvmUtil.resetBuf();
    this.bladeThread.bladeHeartTimer = new BladeHeartTimer(this.bladeThread);
    this.bladeThread.bladeHeartTimer.setName("BladeHeart" + bladeNO);
    this.bladeThread.bladeHeartTimer.start();
    this.bladeThread.start();
    drawThread.start();
    this.kvmInterface.tabbedpane.setPreferredSize(new Dimension(kvmUtil.imageWidth + (imagePane.getLocation()).x, kvmUtil.imageHeight + (imagePane.getLocation()).y));
    if (this.kvmInterface.actionBlade != 0) {
      BladeThread bladeThr = this.kvmInterface.base.threadGroup.get(String.valueOf(this.kvmInterface.actionBlade));
      if (bladeThr != null)
      {
        bladeThr.bladeCommu.sentData(this.kvmInterface.packData.contrRate(0, bladeThr.getBladeNO()));
      }
    } 
    this.kvmInterface.actionBlade = bladeNO;
    if (this.kvmInterface.bladeList.size() == 1) {
      this.kvmInterface.tabbedpane.add(imagePane);
    }
    else {
      this.kvmInterface.tabbedpane.add("blade" + bladeNO, imagePane);
    } 
    this.kvmInterface.tabbedpane.setSelectedComponent(imagePane);
    imagePane.getClass(); 
    imagePane.statReceiveTask = imagePane.new StatReceiveTask();
    startReceiveList(imagePane);
    LoggerUtil.info( "isControl: "+isControl);
    if (isControl) {
      imagePane.getClass(); 
      imagePane.mouseTimerTask = imagePane.new MouseTimerTask();
      startMouseList(imagePane);
      LoggerUtil.info( "this.kvmInterface.packData.connectBlade: "+this.kvmInterface.packData.connectBlade(bladeNO, (getImagePane(bladeNO)).custBit));
      this.bladeThread.bladeCommu.sentData(this.kvmInterface.packData.connectBlade(bladeNO, (getImagePane(bladeNO)).custBit));
      if (this.kvmInterface.getBladeFlag() != null) {
        try {
          Thread.sleep(5000L);
        }
        catch (InterruptedException e) {
          Debug.printExc(e.getMessage());
        } 
      }
      this.bladeThread.bladeCommu.sentData(this.kvmInterface.packData.contrRate(35, this.bladeThread.getBladeNO()));
      this.bladeThread.bladeCommu.sentData((this.kvmInterface.kvmUtil.getImagePane(this.kvmInterface.actionBlade)).pack.mouseModeControl((byte)36, (byte)2, this.bladeThread.getBladeNO()));
    }
    else {
      this.bladeThread.bladeCommu.sentData(this.kvmInterface.packData.monitorBlade(bladeNO));
      setMoniKeyState(this.kvmInterface.isFullScreen);
    } 
    setButtonEnable(true);
    if (null != this.kvmInterface.toolbar.mouseSynButton)
    {
      if (isNew)
      {
        if (this.kvmInterface.base.isMstsc) {
          this.kvmInterface.toolbar.mouseSynButton.setEnabled(true);
        }
        else {
          this.kvmInterface.toolbar.mouseSynButton.setEnabled(false);
        } 
      }
    }
    MouseDisplacementImpl.setMode(0);
    this.kvmInterface.clickFlag = false;
    
  }
  private void clickButton(int bladeNO, boolean enable) {
    if (this.kvmInterface.bladeList.size() == 1) {
      return;
    }
    this.kvmInterface.toolbar.getComponent(4 + bladeNO).setEnabled(enable);
    this.kvmInterface.toolbar.getComponent(4 + bladeNO).addMouseListener(bladeMouseListener(enable));
  }
  public void disconnectBlade(int bladeNO) {
    Object lock = this.kvmInterface.base.getLock(bladeNO);
    synchronized (lock) {
      if (this.kvmInterface.base.threadGroup.size() == 0 || this.kvmInterface.base.threadGroup.get(String.valueOf(bladeNO)) == null) {
        return;
      }
      BladeThread bladeThread = this.kvmInterface.base.threadGroup.remove(String.valueOf(bladeNO));
      this.kvmInterface.clientSocket.bladeMap.remove(String.valueOf(bladeNO));
      ImagePane imagePane = getImagePane(bladeNO);
      imagePane.kvmInterface.floatToolbar.destroyVmLink();
      if (!Base.isSynMouse)
      {
        imagePane.mouseTimerTask = null;
      }
      imagePane.mouseTimerTask = null;
      imagePane.receiveList.cancel();
      imagePane.statReceiveTask = null;
      bladeThread.bladeHeartTimer = null;
      bladeThread.keyState.clear();
      DrawThread drawThread = bladeThread.getDrawThread();
      this.kvmInterface.base.tabbedList.remove(String.valueOf(bladeNO));
      if (drawThread.timer != null)
      {
        drawThread.timer.cancel();
      }
      drawThread.setConn(false);
      drawThread.lList.add(new byte[] { 1, 1, 1, 1, 1 });
      drawThread.lList.add(new byte[] { 1, 1, 1, 1, 1 });
      while (drawThread.isAlive()) {
        try {
          Thread.sleep(10L);
        }
        catch (InterruptedException e) {}
      } 
      if (!bladeThread.bladeCommu.socket.isClosed())
      {
        bladeThread.bladeCommu.sentData(this.kvmInterface.packData.interruptBlade(bladeNO, bladeThread));
      }
      drawThread.setComImage((LinkedList<byte[]>)null);
      drawThread.setPreviImage((byte[])null);
      (drawThread.getKvmUtil()).imageData = null;
      (drawThread.getKvmUtil()).bladePreInfo = null;
      drawThread.imagePane = null;
      drawThread.lList = null;
      drawThread = null;
      bladeThread.setConn(false);
      try {
        bladeThread.bladeCommu.socket.close();
      }
      catch (IOException e) {
        bladeThread = null;
        Debug.printExc(e.getMessage());
      } 
      imagePane.bladeNO = 0;
      imagePane.colorBit = 0;
      imagePane.custBit = 0;
      setImagePaneRev(imagePane);
      this.kvmInterface.tabbedpane.getModel().removeChangeListener(this.changeListener);
      synchronized (this.kvmInterface.tabbedpane) {
        this.kvmInterface.tabbedpane.remove(imagePane);
      } 
      imagePane.releaseImagePanel();
      imagePane = null;
      clickButton(bladeNO, true);
      if (this.kvmInterface.tabbedpane.getTabCount() == 0) {
        setButtonEnable(false);
        this.kvmInterface.actionBlade = 0;
        this.kvmInterface.toolbar.numColorButton.setBackground(Base.LIGHT_OFF);
        this.kvmInterface.toolbar.capsColorButton.setBackground(Base.LIGHT_OFF);
        this.kvmInterface.toolbar.scrollColorButton.setBackground(Base.LIGHT_OFF);
      }
      else {
        this.kvmInterface.actionBlade = ((ImagePane)this.kvmInterface.tabbedpane.getSelectedComponent()).bladeNO;
        ImagePane imagePane1 = getImagePane(this.kvmInterface.actionBlade);
        this.kvmInterface.tabbedpane.setPreferredSize(new Dimension(imagePane1.width + (imagePane1.getLocation()).x, imagePane1.height + (imagePane1.getLocation()).y + 5));
        if (null != this.kvmInterface.toolbar.mouseSynButton)
        {
          if (imagePane1.isNew()) {
            if (this.kvmInterface.base.isMstsc)
            {
              this.kvmInterface.toolbar.mouseSynButton.setEnabled(true);
            }
            else
            {
              this.kvmInterface.toolbar.mouseSynButton.setEnabled(false);
            }
          } else {
            this.kvmInterface.toolbar.mouseSynButton.setEnabled(true);
          } 
        }
        BladeThread bThread = this.kvmInterface.base.threadGroup.get(String.valueOf(imagePane1.bladeNO));
        (bThread.getDrawThread()).isDisplay = true;
        bThread.kvmUtil.resultDivi.clear();
        bThread.kvmUtil.diviBuff.clear();
        bThread.kvmUtil.resetBuf();
        (bThread.getDrawThread()).lList.clear();
        bThread.getDrawThread().getComImage().clear();
        bThread.bladeCommu.sentData(this.kvmInterface.packData.connectBlade(this.kvmInterface.actionBlade, (getImagePane(this.kvmInterface.actionBlade)).custBit));
        bThread.bladeCommu.sentData(this.kvmInterface.packData.contrRate(35, bThread.getBladeNO()));
        imagePane1.kvmInterface.floatToolbar.setLocation((imagePane1.kvmInterface.floatToolbar.imgwidth - imagePane1.kvmInterface.floatToolbar.getWidth()) / 2, -1);
        if (imagePane1.kvmInterface.floatToolbar.isVirtualMedia()) {
          imagePane1.kvmInterface.floatToolbar.setFlpLocation((imagePane1.kvmInterface.floatToolbar.imgwidth - imagePane1.kvmInterface.floatToolbar.getFlpWidth()) / 2, imagePane1.kvmInterface.floatToolbar.getHeight() - 1);
          imagePane1.kvmInterface.floatToolbar.setCDLocation((imagePane1.kvmInterface.floatToolbar.imgwidth - imagePane1.kvmInterface.floatToolbar.getCDWidth()) / 2, imagePane1.kvmInterface.floatToolbar.getHeight() - 1);
        } 
      } 
    } 
  }
  public void setImagePaneRev(ImagePane imagePane) {
    imagePane.setImage(new byte[1310720]);
    imagePane.repaint();
    this.kvmInterface.base.isDiv = true;
    imagePane.source.newPixels(0, 0, 1280, 1024);
    imagePane.big.drawImage(imagePane.image, 0, 0, imagePane);
    imagePane.transform.setToScale(Base.getScreenSize().getWidth() / 2.0D / 1280.0D, Base.getScreenSize().getHeight() / 2.0D / 1024.0D);
    imagePane.applyFilter();
    imagePane.repaint();
    this.kvmInterface.base.isDiv = false;
  }
  public void setButtonEnable(boolean buttonEnable) {
    Debug.println("setButtonEnable:::::::" + buttonEnable);
    if (buttonEnable) {
      if (null != this.kvmInterface.toolbar.disConnectBladeButton)
      {
        this.kvmInterface.toolbar.disConnectBladeButton.setEnabled(true);
      }
      if (null != this.kvmInterface.toolbar.divButton)
      {
        this.kvmInterface.toolbar.divButton.setEnabled(true);
      }
      if (null != this.kvmInterface.toolbar.mouseSynButton)
      {
        this.kvmInterface.toolbar.mouseSynButton.setEnabled(true);
      }
      if (null != this.kvmInterface.toolbar.setColorBit)
      {
        this.kvmInterface.toolbar.setColorBit.setEnabled(true);
      }
      this.kvmInterface.toolbar.combineKey.setEnabled(true);
      this.kvmInterface.toolbar.fullButton.setEnabled(true);
      this.kvmInterface.toolbar.imageButton.setEnabled(true);
    }
    else {
      if (null != this.kvmInterface.toolbar.disConnectBladeButton)
      {
        this.kvmInterface.toolbar.disConnectBladeButton.setEnabled(false);
      }
      if (null != this.kvmInterface.toolbar.divButton)
      {
        this.kvmInterface.toolbar.divButton.setEnabled(false);
      }
      if (null != this.kvmInterface.toolbar.mouseSynButton)
      {
        this.kvmInterface.toolbar.mouseSynButton.setEnabled(false);
      }
      if (null != this.kvmInterface.toolbar.setColorBit)
      {
        this.kvmInterface.toolbar.setColorBit.setEnabled(false);
      }
      this.kvmInterface.toolbar.combineKey.setEnabled(false);
      this.kvmInterface.toolbar.fullButton.setEnabled(false);
      this.kvmInterface.toolbar.imageButton.setEnabled(true);
    } 
    if (this.kvmInterface.getBladeSize() > 1) {
      this.kvmInterface.toolbar.disConnectBladeButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), BorderFactory.createLineBorder(Color.WHITE)));
      this.kvmInterface.toolbar.combineKey.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), BorderFactory.createLineBorder(Color.WHITE)));
      this.kvmInterface.toolbar.fullButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), BorderFactory.createLineBorder(Color.WHITE)));
      this.kvmInterface.toolbar.divButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), BorderFactory.createLineBorder(Color.WHITE)));
      this.kvmInterface.toolbar.refreshButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), BorderFactory.createLineBorder(Color.WHITE)));
      this.kvmInterface.toolbar.imageButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 3, 5, 3), BorderFactory.createLineBorder(Color.WHITE)));
      this.kvmInterface.toolbar.disConnectBladeButton.addMouseListener(buttonMouseListener(buttonEnable));
      this.kvmInterface.toolbar.refreshButton.addMouseListener(buttonMouseListener(true));
      this.kvmInterface.toolbar.divButton.addMouseListener(buttonMouseListener(buttonEnable));
      this.kvmInterface.toolbar.combineKey.addMouseListener(buttonMouseListener(buttonEnable));
      this.kvmInterface.toolbar.fullButton.addMouseListener(buttonMouseListener(buttonEnable));
      this.kvmInterface.toolbar.imageButton.addMouseListener(buttonMouseListener(true));
      this.kvmInterface.toolbar.helpButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 3, 5, 3), BorderFactory.createLineBorder(Color.WHITE)));
      this.kvmInterface.toolbar.helpButton.addMouseListener(buttonMouseListener(true));
    } 
  }
  public MouseListener buttonMouseListener(final boolean flag) {
    MouseAdapter adapter = new MouseAdapter()
      {
        public void mouseEntered(MouseEvent e)
        {
          if (flag) {
            JButton button = (JButton)e.getSource();
            button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(0), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
            button.setBackground(new Color(237, 235, 235));
          }
          else {
            JButton button = (JButton)e.getSource();
            button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), BorderFactory.createLineBorder(Color.WHITE)));
            button.setBackground((Color)null);
          } 
        }
        public void mouseExited(MouseEvent e) {
          JButton button = (JButton)e.getSource();
          button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), BorderFactory.createLineBorder(Color.WHITE)));
          button.setBackground((Color)null);
        }
      };
    return adapter;
  }
  public MouseListener bladeMouseListener(final boolean flag) {
    MouseAdapter adapter = new MouseAdapter()
      {
        public void mouseEntered(MouseEvent e)
        {
          if (flag) {
            JButton button = (JButton)e.getSource();
            button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(0), BorderFactory.createLineBorder(Color.WHITE)), BorderFactory.createEmptyBorder(6, 3, 6, 3)));
            button.setBackground(new Color(237, 235, 235));
          }
          else {
            JButton button = (JButton)e.getSource();
            button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5), BorderFactory.createLineBorder(Color.WHITE)));
            button.setBackground((Color)null);
          } 
        }
        public void mouseExited(MouseEvent e) {
          JButton button = (JButton)e.getSource();
          button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5), BorderFactory.createLineBorder(Color.WHITE)));
          button.setBackground((Color)null);
        }
      };
    return adapter;
  }
  public void setFullToolBar(boolean isDiv) {
    if (isDiv) {
      this.kvmInterface.fullScreen.toolBar.removeAll();
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.divButton);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.mouseSynButton);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.combineKey);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.mouseModeButton);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.returnButton);
      if (this.kvmInterface.floatToolbar.isVirtualMedia()) {
        this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.btnCDMenu);
        this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.btnFlpMenu);
      } 
      if (null != this.kvmInterface.fullScreen.toolBar.powerMenuButton)
      {
        this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.powerMenuButton);
      }
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.helpButton);
      this.kvmInterface.fullScreen.toolBar.divButton.setEnabled(false);
      this.kvmInterface.fullScreen.toolBar.mouseSynButton.setEnabled(false);
      this.kvmInterface.fullScreen.toolBar.combineKey.setEnabled(false);
      this.kvmInterface.fullScreen.toolBar.mouseModeButton.setEnabled(false);
    }
    else {
      this.kvmInterface.fullScreen.toolBar.removeAll();
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.divButton);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.mouseSynButton);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.combineKey);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.mouseModeButton);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.returnButton);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.combo);
      if (this.kvmInterface.floatToolbar.isVirtualMedia()) {
        this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.btnCDMenu);
        this.kvmInterface.fullScreen.toolBar.btnCDMenu.setEnabled(true);
        this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.btnFlpMenu);
        this.kvmInterface.fullScreen.toolBar.btnFlpMenu.setEnabled(true);
      } 
      if (null != this.kvmInterface.fullScreen.toolBar.powerMenuButton) {
        this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.powerMenuButton);
        this.kvmInterface.fullScreen.toolBar.powerMenuButton.setEnabled(true);
      } 
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.helpButton);
      this.kvmInterface.fullScreen.toolBar.helpButton.setEnabled(true);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.labelnum);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.numColorButton);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.labelcaps);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.capsColorButton);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.labelscroll);
      this.kvmInterface.fullScreen.toolBar.add(this.kvmInterface.fullScreen.toolBar.scrollColorButton);
      this.kvmInterface.fullScreen.toolBar.divButton.setEnabled(true);
      this.kvmInterface.fullScreen.toolBar.mouseSynButton.setEnabled(true);
      this.kvmInterface.fullScreen.toolBar.combineKey.setEnabled(true);
      this.kvmInterface.fullScreen.toolBar.mouseModeButton.setEnabled(true);
    } 
  }
  public void returnToWin() {
    if (this.kvmInterface.base.isDiv) {
      this.kvmInterface.fullScreen.imageParentPane.removeAll();
      this.kvmInterface.fullScreen.setVisible(false);
      this.kvmInterface.fullScreen.toolBarFrame.setVisible(false);
      this.kvmInterface.base.isDiv = false;
      setImageTipText(false);
      setDrawDisplay(false);
      this.kvmInterface.isFullScreen = false;
      int threadNum = this.kvmInterface.base.tabbedList.size();
      String name = "";
      for (int i = 0; i < threadNum; i++) {
        name = this.kvmInterface.base.tabbedList.get(i);
        BladeThread bladeThread = this.kvmInterface.base.threadGroup.get(name);
        DrawThread thread = bladeThread.getDrawThread();
        if (name.equals(String.valueOf(this.kvmInterface.actionBlade))) {
          thread.isDisplay = true;
          bladeThread.bladeCommu.sentData(this.kvmInterface.packData.contrRate(35, bladeThread.getBladeNO()));
        } 
        if (this.kvmInterface.bladeList.size() == 1) {
          this.kvmInterface.tabbedpane.add(getImagePane(thread.bladeNo));
        }
        else {
          this.kvmInterface.tabbedpane.add("blade" + thread.bladeNo, getImagePane(thread.bladeNo));
        } 
      } 
      this.kvmInterface.tabbedpane.setSelectedComponent(getImagePane(this.kvmInterface.actionBlade));
    }
    else {
      this.kvmInterface.fullScreen.imageParentScrollPane.removeAll();
      while (this.kvmInterface.fullScreen.isVisible())
      {
        this.kvmInterface.fullScreen.setVisible(false);
      }
      this.kvmInterface.fullScreen.toolBarFrame.setVisible(false);
      this.kvmInterface.isFullScreen = false;
      String name = "";
      int threadNum = this.kvmInterface.base.tabbedList.size();
      for (int i = 0; i < threadNum; i++) {
        name = this.kvmInterface.base.tabbedList.get(i);
        DrawThread thread = ((BladeThread)this.kvmInterface.base.threadGroup.get(name)).getDrawThread();
        if (!thread.isDisplay) {
          if (this.kvmInterface.bladeList.size() == 1)
          {
            this.kvmInterface.tabbedpane.add(getImagePane(thread.bladeNo));
          }
          else
          {
            this.kvmInterface.tabbedpane.add("blade" + thread.bladeNo, getImagePane(thread.bladeNo));
          }
        }
        else {
          if (this.kvmInterface.bladeList.size() == 1) {
            this.kvmInterface.tabbedpane.add(getImagePane(thread.bladeNo));
          }
          else {
            this.kvmInterface.tabbedpane.add("blade" + thread.bladeNo, getImagePane(thread.bladeNo));
          } 
          this.kvmInterface.actionBlade = thread.bladeNo;
        } 
      } 
      this.kvmInterface.tabbedpane.setSelectedComponent(getImagePane(this.kvmInterface.actionBlade));
      setDrawDisplay(false);
      DrawThread drawThread = ((BladeThread)this.kvmInterface.base.threadGroup.get(String.valueOf(this.kvmInterface.actionBlade))).getDrawThread();
      drawThread.isDisplay = true;
      drawThread.getBladeCommu().sentData(this.kvmInterface.packData.contrRate(35, drawThread.getBladeNo()));
    } 
    ImagePane imagePane = getImagePane(this.kvmInterface.actionBlade);
    this.kvmInterface.tabbedpane.setPreferredSize(new Dimension(imagePane.width + 2, imagePane.height + 29));
    if (imagePane.isContr()) {
      setNumKeyColor(imagePane.getNum());
      setCapsKeyColor(imagePane.getCaps());
      setScrollKeyColor(imagePane.getScroll());
    }
    else {
      setMoniKeyState(this.kvmInterface.isFullScreen);
    } 
    this.kvmInterface.fullScreen.setCursor(this.kvmInterface.base.defCursor);
    imagePane.setCursor(this.kvmInterface.base.defCursor);
    if (null != this.kvmInterface.toolbar.mouseSynButton)
    {
      if (imagePane.isNew()) {
        if (this.kvmInterface.base.isMstsc)
        {
          this.kvmInterface.toolbar.mouseSynButton.setEnabled(true);
        }
        else
        {
          this.kvmInterface.toolbar.mouseSynButton.setEnabled(false);
        }
      } else {
        this.kvmInterface.toolbar.mouseSynButton.setEnabled(true);
      } 
    }
    imagePane.setVisible(true);
    imagePane.requestFocus();
    this.kvmInterface.floatToolbar.startStateMenu();
    this.kvmInterface.isReturnToWin = true;
    this.kvmInterface.floatToolbar.setVisible(true);
    if (Base.isSingleMouse) {
      this.kvmInterface.floatToolbar.powerMenu.SingleMouseMenu.setSelected(true);
    }
    else {
      this.kvmInterface.floatToolbar.powerMenu.SingleMouseMenu.setSelected(false);
    } 
    if (null != this.kvmInterface.fullScreen.toolBar.powerMenu)
    {
      this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect = this.kvmInterface.fullScreen.toolBar.powerMenu.kineScopeDataCollect;
    }
    if (null != this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect) {
      if (this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect.isCollect())
      {
        this.kvmInterface.floatToolbar.powerMenu.localKinescopeMenu.setText(this.kvmInterface.kvmUtil.getString("Stop_KinScope"));
      }
      else
      {
        this.kvmInterface.floatToolbar.powerMenu.localKinescopeMenu.setText(this.kvmInterface.kvmUtil.getString("localKinescope"));
      }
    } else {
      this.kvmInterface.floatToolbar.powerMenu.localKinescopeMenu.setText(this.kvmInterface.kvmUtil.getString("localKinescope"));
    } 
    this.kvmInterface.floatToolbar.btnShow.setIcon(new ImageIcon(getClass().getResource("resource/images/float.gif")));
    this.kvmInterface.fullScreen.setVisible(false);
    if (this.kvmInterface.floatToolbar.isVirtualMedia()) {
      this.kvmInterface.fullScreen.cdMenu.setVisible(false);
      this.kvmInterface.fullScreen.flpMenu.setVisible(false);
      this.kvmInterface.floatToolbar.setVirtualMediaVisible(false, false);
      imagePane.add(this.kvmInterface.floatToolbar.getCDPanel());
      imagePane.add(this.kvmInterface.floatToolbar.getFlpPanel());
      this.kvmInterface.floatToolbar.isShowingCD = false;
      this.kvmInterface.floatToolbar.isShowingFlp = false;
    } 
    if (isLinuxOS() && this.kvmInterface.fullScreen.powerPanelDialog.isShowing())
    {
      this.kvmInterface.fullScreen.powerPanelDialog.setVisible(false);
    }
    if (null != this.kvmInterface.floatToolbar.helpFrm)
    {
      this.kvmInterface.floatToolbar.helpFrm.setAlwaysOnTop(false);
    }
    if (this.kvmInterface.toolFrame != null)
    {
      this.kvmInterface.toolFrame.setVisible(false);
    }
  }
  public static void startMouseList(ImagePane imagePane) {
    if (!Base.isSynMouse) {
      imagePane.mouseTimerTask.setName("MouseTimer");
      imagePane.mouseTimerTask.start();
    } 
  }
  public static void startReceiveList(ImagePane imagePane) {
    imagePane.receiveList = new Timer("ReceiveTimer", true);
    imagePane.receiveList.scheduleAtFixedRate(imagePane.statReceiveTask, 0L, 10000L);
  }
  public void addEmptyPane() {
    int num = 4 - this.kvmInterface.base.threadGroup.size();
    if (num <= 2) {
      for (int i = 0; i < num; i++)
      {
        FloatPanel temPane = new FloatPanel(this.kvmInterface);
        temPane.setShowtoolBar(false);
        temPane.setBackground(Color.black);
        this.kvmInterface.fullScreen.imageParentPane.add(temPane);
      }
    } else {
      for (int i = 0; i < num; i++) {
        FloatPanel temPane = new FloatPanel(this.kvmInterface);
        if (i == 0) {
          temPane.setShowtoolBar(true);
        }
        else {
          temPane.setShowtoolBar(false);
        } 
        temPane.setBackground(Color.black);
        this.kvmInterface.fullScreen.imageParentPane.add(temPane);
      } 
    } 
  }
  public ActionListener taskPerformer = new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        if (KVMUtil.this.kvmInterface.clientSocket.bladePresentInfo.size() != 0) {
          KVMUtil.this.bladePreInfo = KVMUtil.this.kvmInterface.clientSocket.bladePresentInfo.remove(KVMUtil.this.kvmInterface.clientSocket.bladePreIndex);
          Debug.println("blade present info===========");
          Debug.printByte(KVMUtil.this.bladePreInfo);
          KVMUtil.this.isConn = true;
        }
        else {
          KVMUtil.this.times++;
        } 
      }
    };
  public ChangeListener changeListener = new ChangeListener()
    {
      public void stateChanged(ChangeEvent e)
      {
        if (KVMUtil.this.kvmInterface.tabbedpane.getTabCount() > 0) {
          int slotNO = ((ImagePane)KVMUtil.this.kvmInterface.tabbedpane.getSelectedComponent()).bladeNO;
          KVMUtil.this.kvmInterface.actionBlade = slotNO;
          ImagePane imagePane = KVMUtil.this.getImagePane(slotNO);
          KVMUtil.this.kvmInterface.tabbedpane.setPreferredSize(new Dimension(imagePane.width + (imagePane.getLocation()).x, imagePane.height + (imagePane.getLocation()).y + 5));
          KVMUtil.this.setDrawDisplay(false);
          BladeThread bladeThread = KVMUtil.this.kvmInterface.base.threadGroup.get(String.valueOf(slotNO));
          DrawThread drawThread = bladeThread.getDrawThread();
          (drawThread.getKvmUtil()).firstJudge = true;
          bladeThread.kvmUtil.resetBuf();
          drawThread.lList.clear();
          drawThread.getComImage().clear();
          drawThread.isDisplay = true;
          bladeThread.kvmUtil.isReqFrame = false;
          bladeThread.bladeCommu.sentData(KVMUtil.this.kvmInterface.packData.connectBlade(slotNO, (KVMUtil.this.getImagePane(slotNO)).custBit));
          bladeThread.bladeCommu.sentData(KVMUtil.this.kvmInterface.packData.contrRate(35, bladeThread.getBladeNO()));
          if (null != KVMUtil.this.kvmInterface.toolbar.mouseSynButton)
          {
            if (bladeThread.isNew()) {
              if (!KVMUtil.this.kvmInterface.base.isMstsc)
              {
                KVMUtil.this.kvmInterface.toolbar.mouseSynButton.setEnabled(false);
              }
            }
            else {
              KVMUtil.this.kvmInterface.toolbar.mouseSynButton.setEnabled(true);
            } 
          }
          imagePane.kvmInterface.floatToolbar.setLocation((imagePane.kvmInterface.floatToolbar.imgwidth - imagePane.kvmInterface.floatToolbar.getWidth()) / 2, -1);
          imagePane.kvmInterface.floatToolbar.setVisible(true);
          if (imagePane.kvmInterface.floatToolbar.isVirtualMedia()) {
            imagePane.kvmInterface.floatToolbar.setFlpLocation((imagePane.kvmInterface.floatToolbar.imgwidth - imagePane.kvmInterface.floatToolbar.getFlpWidth()) / 2, imagePane.kvmInterface.floatToolbar.getHeight() - 1);
            imagePane.kvmInterface.floatToolbar.setCDLocation((imagePane.kvmInterface.floatToolbar.imgwidth - imagePane.kvmInterface.floatToolbar.getCDWidth()) / 2, imagePane.kvmInterface.floatToolbar.getHeight() - 1);
          } 
        } 
      }
    };
  public void setMoniKeyState(boolean isFullScreen) {
    if (isFullScreen) {
      this.kvmInterface.fullScreen.toolBar.numColorButton.setBackground(Color.GRAY);
      this.kvmInterface.fullScreen.toolBar.capsColorButton.setBackground(Color.GRAY);
      this.kvmInterface.fullScreen.toolBar.scrollColorButton.setBackground(Color.GRAY);
    }
    else {
      this.kvmInterface.toolbar.numColorButton.setBackground(Color.GRAY);
      this.kvmInterface.toolbar.capsColorButton.setBackground(Color.GRAY);
      this.kvmInterface.toolbar.scrollColorButton.setBackground(Color.GRAY);
    } 
  }
  public static int translateToUSBCode(KeyEvent e) {
    int usbCode = 0;
    if (e.isActionKey() || e.getKeyLocation() == 4 || 17 == e.getKeyCode() || 16 == e.getKeyCode() || 18 == e.getKeyCode() || 65406 == e.getKeyCode()) {
      usbCode = javaCodeToUSB(e);
    }
    else if (!e.isActionKey()) {
      int scancode = 0;
      if (isWindowsOS()) {
        scancode = MouseDisplacementImpl.getScanCode();
        for (int i = 0; i < Base.KEY_MAP.length; i++) {
          if (scancode == Base.KEY_MAP[i][0]) {
            usbCode = Base.KEY_MAP[i][1];
            break;
          } 
        } 
      } else if (isMacOS()) {
        usbCode = javaCodeToUSB(e);
      }
      else if (isLinux()) {
        if (KeyboardImpl.getScanCode() == 0) {
          usbCode = javaCodeToUSB(e);
        } else {
          scancode = 0xFF & KeyboardImpl.getScanCode();
          for (int i = 0; i < Base.KEY_MAP.length; i++) {
            if (scancode == Base.KEY_MAP[i][0]) {
              usbCode = Base.KEY_MAP[i][1];
              break;
            } 
          } 
        } 
      } 
    } else {
      usbCode = javaCodeToUSB(e);
    } 
    return usbCode;
  }
  public static void main(String[] args) {
    String tmp1 = "/10.85.138.73:80".substring(1);
    String proxyIp = tmp1.split(":")[0];
    Debug.printExc("proxy ip:" + proxyIp);
    Debug.printExc("proxy port:31035");
  }
  public static Socket getProxySocket(String httpIp, int httpPort, String serverIp, int serverPort) throws URISyntaxException, IOException {
    Debug.printExc("smm ip:" + httpIp);
    ProxySelector selector = ProxySelector.getDefault();
    List<Proxy> proxys = selector.select(new URI("http://" + httpIp + ":" + httpPort));
    for (Iterator<Proxy> it = proxys.listIterator(); it.hasNext(); ) {
      Proxy proxy = it.next();
      Debug.printExc("proxy:" + proxy.address());
      if (Proxy.Type.HTTP.name().equals(proxy.type().name())) {
        String tmp1 = proxy.address().toString().substring(1);
        String proxyIp = tmp1.split(":")[0];
        Debug.printExc("proxy ip:" + proxyIp);
        Debug.printExc("proxy port:31035");
        InetAddress ia = InetAddress.getByName(proxyIp);
        InetSocketAddress isa = new InetSocketAddress(ia, 31035);
        Socket socket = new Socket(new Proxy(Proxy.Type.SOCKS, isa));
        Debug.printExc("server ip:" + serverIp);
        Debug.printExc("server port:" + serverPort);
        InetAddress ia1 = InetAddress.getByName(serverIp);
        InetSocketAddress ept = new InetSocketAddress(ia1, serverPort);
        socket.connect(ept);
        return socket;
      } 
    } 
    return null;
  }
  public BladeState getBladStateBmc(int bladeNO) throws KVMException {
    BladeState bladeState = new BladeState();
    String bladeIP = this.kvmInterface.client.address.getHostAddress();
    int bladePort = Client.port;
    bladeState.setBladeIP(bladeIP);
    bladeState.setBladePort(bladePort);
    bladeState.setEnable(true);
    bladeState.setNew(true);
    return bladeState;
  }
  private static String osType = "";
  private static String osArch = "";
  private static String getOsName() {
    if (osType != null && "".equals(osType))
    {
      osType = System.getProperty("os.name").toLowerCase();
    }
    return osType;
  }
  public static String getOsArch() {
    if (osArch != null && "".equals(osArch))
    {
      osArch = System.getProperty("os.arch").toLowerCase();
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
  public static boolean isLinuxOS1() {
    return isOSTypeByNmae("linux");
  }
  public static boolean isLinuxOS() {
    return (isOSTypeByNmae("linux") || isOSTypeByNmae("mac os x"));
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
      for (int i = 0; i < this.kvmInterface.bladeList.size(); i++) {
        iContainer = this.kvmInterface.bladeList.get(i);
        if (iContainer != null && iContainer.getBladeNO() == bladeNO) {
          break;
        }
        iContainer = null;
      } 
      if (state[0] == 1) {
        if (iContainer != null)
        {
          iContainer.setCodeKey(this.kvmInterface.codeKey);
        }
      }
      else if (iContainer != null) {
        iContainer.setCodeKey(0);
      } 
    } 
  }
  public static boolean isAdmin() {
    if (Base.privilege == 4 || Base.privilege == 3)
    {
      return true;
    }
    return false;
  }
  public static int byteArrayToInt(byte[] b, int offset) {
    int value = 0;
    for (int i = 0; i < 4; i++) {
      int shift = (3 - i) * 8;
      value += (b[i + offset] & 0xFF) << shift;
    } 
    return value;
  }
}
