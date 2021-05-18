package com.kvmV1;
public class CCrc
{
  private int[] crc16Table = null;
  private int[] crc32Table = null;
  private String[] crcType = new String[] { "CRC_16", "CRC_CCITT", "CRC_16_H", "CRC_32" };
  private String type = "";
  public CCrc(String cType) {
    int wPoly = 0;
    this.type = cType;
    if (this.crcType[0].equals(cType)) {
      wPoly = 40961;
      this.crc16Table = new int[256];
    } 
    if (this.crcType[1].equals(cType)) {
      wPoly = 33800;
      this.crc16Table = new int[256];
    } 
    if (this.crcType[2].equals(cType)) {
      wPoly = 4129;
      this.crc16Table = new int[256];
    } 
    if (this.crcType[3].equals(cType))
    {
      this.crc32Table = new int[256];
    }
    if (!this.crcType[0].equals(cType) && !this.crcType[1].equals(cType) && !this.crcType[2].equals(cType) && !this.crcType[3].equals(cType)) {
      wPoly = 4129;
      this.crc16Table = new int[256];
    } 
    if (this.crc16Table != null)
    {
      for (int i = 0; i < 256; i++) {
        int w;
        if (!"CRC_16_H".equals(cType)) {
          int j;
          for (w = i, j = 0; j < 8; j++) {
            boolean judge = true;
            if ((w & 0x1) == 0)
            {
              judge = false;
            }
            w = judge ? (w >> 1 ^ wPoly) : (w >> 1);
          } 
        } else {
          int j;
          for (w = i << 8, j = 0; j < 8; j++) {
            boolean judge = true;
            if ((w & 0x8000) == 0)
            {
              judge = false;
            }
            w = judge ? (w << 1 ^ wPoly) : (w << 1);
          } 
        } 
        this.crc16Table[i] = w;
      } 
    }
    if (this.crc32Table != null)
    {
      for (int i = 0; i < 256; i++) {
        int j; int dw;
        for (dw = i, j = 0; j < 8; j++) {
          boolean judge = true;
          if ((dw & 0x1) == 0)
          {
            judge = false;
          }
          dw = judge ? (dw >> 1 ^ 0xEDB88320) : (dw >> 1);
        } 
        this.crc32Table[i] = dw;
      } 
    }
  }
  public int dWCrc(int startCrc, byte[] addr, int size) {
    if (this.crc32Table == null)
    {
      return 0;
    }
    for (int i = 0; i < size; i++)
    {
      startCrc = this.crc32Table[(byte)startCrc ^ addr[i]] ^ startCrc;
    }
    return startCrc;
  }
  public int wCrc(short startCrc, byte[] addr, short size) {
    short crcResult = 0;
    if (this.crc16Table == null)
    {
      return 0;
    }
    if (!"CRC_16_H".equals(this.type)) {
      short i;
      for (i = 0; i < size; i = (short)(i + 1)) {
        byte tem = (byte)(startCrc ^ addr[i]);
        int temp = tem;
        if (tem < 0)
        {
          temp = tem + 256;
        }
        crcResult = (short)(this.crc16Table[temp] ^ startCrc >> 8);
        startCrc = crcResult;
      } 
    } else {
      short i;
      for (i = 0; i < size; i = (short)(i + 1)) {
        byte tem = (byte)(startCrc >> 8 & 0xFF ^ addr[i]);
        int temp = tem;
        if (tem < 0)
        {
          temp = tem + 256;
        }
        crcResult = (short)(this.crc16Table[temp] ^ startCrc << 8);
        startCrc = crcResult;
      } 
    } 
    return crcResult;
  }
}
