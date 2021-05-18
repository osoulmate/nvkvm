package com.huawei.vm.console.communicationV1;
import com.huawei.vm.console.utilsV1.VMException;
public interface Receiver {
  void receive(byte[] paramArrayOfbyte, int paramInt) throws VMException;
  int receiveImmediate(byte[] paramArrayOfbyte, int paramInt) throws VMException;
  boolean receiveByLimit(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws VMException;
  void setOverTime(int paramInt);
}
