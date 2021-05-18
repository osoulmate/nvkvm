package com.huawei.vm.console.storageV1.impl;
import com.huawei.vm.console.storageV1.MassStorageDevice;
import com.huawei.vm.console.utilsV1.VMException;
public abstract class FloppyDriver
  extends MassStorageDevice
{
  public static final int MEDIUM_TYPE_CODE = 148;
  public static final int TOTAL_BLOCKS = 2880;
  public static final int BLOCK_LENGTH = 512;
  public FloppyDriver(String path) throws VMException {
    super(path);
  }
  public abstract void write(byte[] paramArrayOfbyte, long paramLong, int paramInt) throws VMException;
  public abstract void formatUnit(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) throws VMException;
  public int getBlockLength() {
    return 512;
  }
  public int getMediumTypeCode() {
    return 148;
  }
  public int getTotalBlocks() {
    return 2880;
  }
  public int modeSense(byte[] dataBuffer, int pc, int pageCode) {
    int size = 8;
    dataBuffer[0] = 0;
    dataBuffer[2] = (byte)getMediumTypeCode();
    dataBuffer[3] = isWriteProtect() ? Byte.MIN_VALUE : 0;
    dataBuffer[4] = 0;
    dataBuffer[5] = 0;
    dataBuffer[6] = 0;
    dataBuffer[7] = 0;
    if (1 == pageCode || 63 == pageCode) {
      dataBuffer[size + 0] = 1;
      dataBuffer[size + 1] = 10;
      dataBuffer[size + 2] = 0;
      dataBuffer[size + 3] = 3;
      dataBuffer[size + 4] = 0;
      dataBuffer[size + 5] = 0;
      dataBuffer[size + 6] = 0;
      dataBuffer[size + 7] = 0;
      dataBuffer[size + 8] = 3;
      dataBuffer[size + 9] = 0;
      dataBuffer[size + 10] = 0;
      dataBuffer[size + 11] = 0;
      size += 12;
    } 
    if (5 == pageCode || 63 == pageCode) {
      dataBuffer[size + 0] = 5;
      dataBuffer[size + 1] = 30;
      dataBuffer[size + 2] = 3;
      dataBuffer[size + 3] = -24;
      dataBuffer[size + 4] = 2;
      dataBuffer[size + 5] = 18;
      dataBuffer[size + 6] = 2;
      dataBuffer[size + 7] = 0;
      dataBuffer[size + 8] = 0;
      dataBuffer[size + 9] = 80;
      dataBuffer[size + 10] = 0;
      dataBuffer[size + 11] = 0;
      dataBuffer[size + 12] = 0;
      dataBuffer[size + 13] = 0;
      dataBuffer[size + 14] = 0;
      dataBuffer[size + 15] = 0;
      dataBuffer[size + 16] = 0;
      dataBuffer[size + 17] = 0;
      dataBuffer[size + 18] = 0;
      dataBuffer[size + 19] = 8;
      dataBuffer[size + 20] = 30;
      dataBuffer[size + 21] = 0;
      dataBuffer[size + 22] = 0;
      dataBuffer[size + 23] = 0;
      dataBuffer[size + 24] = 0;
      dataBuffer[size + 25] = 0;
      dataBuffer[size + 26] = 0;
      dataBuffer[size + 27] = 0;
      dataBuffer[size + 28] = 2;
      dataBuffer[size + 29] = 88;
      dataBuffer[size + 30] = 0;
      dataBuffer[size + 31] = 0;
      size += 32;
    } 
    if (27 == pageCode || 63 == pageCode) {
      dataBuffer[size + 0] = 27;
      dataBuffer[size + 1] = 10;
      dataBuffer[size + 2] = Byte.MIN_VALUE;
      dataBuffer[size + 3] = 1;
      dataBuffer[size + 4] = 0;
      dataBuffer[size + 5] = 0;
      dataBuffer[size + 6] = 0;
      dataBuffer[size + 7] = 0;
      dataBuffer[size + 8] = 0;
      dataBuffer[size + 9] = 0;
      dataBuffer[size + 10] = 0;
      dataBuffer[size + 11] = 0;
      size += 12;
    } 
    if (28 == pageCode || 63 == pageCode) {
      dataBuffer[size + 0] = 28;
      dataBuffer[size + 1] = 6;
      dataBuffer[size + 2] = 0;
      dataBuffer[size + 3] = 5;
      dataBuffer[size + 4] = 0;
      dataBuffer[size + 5] = 0;
      dataBuffer[size + 6] = 0;
      dataBuffer[size + 7] = 0;
      size += 8;
    } 
    dataBuffer[1] = (byte)((byte)size - 2);
    return size;
  }
}
