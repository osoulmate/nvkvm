package com.huawei.vm.virtualconnection;
public abstract class VirtualConnect {
  public abstract String getErrMsg(int paramInt);
  public abstract int connect(int paramInt1, String paramString1, int paramInt2, long paramLong, String paramString2);
  public abstract int connect(int paramInt1, String paramString1, int paramInt2, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, boolean paramBoolean1, boolean paramBoolean2, String paramString2);
  public abstract int disconnect();
}
