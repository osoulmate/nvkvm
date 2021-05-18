package com.huawei.vm.console.storageV1.impl;
import com.huawei.vm.console.storageV1.MassStorageDevice;
import com.huawei.vm.console.utilsV1.VMException;
public abstract class CDROMDriver
  extends MassStorageDevice
{
  public static final int BLOCK_LENGTH = 2048;
  public CDROMDriver(String path) throws VMException {
    super(path);
  }
  public abstract int readTOC(byte[] paramArrayOfbyte, boolean paramBoolean, int paramInt1, int paramInt2) throws VMException;
  public abstract void startStopUnit(boolean paramBoolean1, boolean paramBoolean2) throws VMException;
  public abstract boolean preventAllowMediumRemoval(boolean paramBoolean);
  public void setWriteProtect(boolean isWP) {
    this.isWP = true;
  }
}
