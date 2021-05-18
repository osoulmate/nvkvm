package com.huawei.vm.console.storage.impl;
import com.huawei.vm.console.storage.MassStorageDevice;
import com.huawei.vm.console.utils.TestPrint;
import com.huawei.vm.console.utils.VMException;
public abstract class CDROMDriver
  extends MassStorageDevice
{
  public static final int BLOCK_LENGTH = 2048;
  public CDROMDriver(String path) throws VMException {
    super(path);
  }
  public int readTOC(byte[] dataBuffer, boolean isMSF, int format, int startTrack) throws VMException {
    int totalBlocks = (int)(getMediumSize() / 2048L);
    double i = totalBlocks / 75.0D + 2.0D;
    int m = (int)i / 60;
    int s = (int)i % 60;
    int f = (int)((i - (int)i) * 75.0D);
    int len = 4;
    if (0 == format) {
      if (startTrack > 1 && startTrack != 170)
      {
        throw new VMException(252);
      }
      dataBuffer[2] = 1;
      dataBuffer[3] = 1;
      if (startTrack <= 1) {
        dataBuffer[len++] = 0;
        dataBuffer[len++] = 20;
        dataBuffer[len++] = 1;
        dataBuffer[len++] = 0;
        dataBuffer[len++] = 0;
        dataBuffer[len++] = 0;
        dataBuffer[len++] = (byte)(isMSF ? 2 : 0);
        dataBuffer[len++] = 0;
      } 
      dataBuffer[len++] = 0;
      dataBuffer[len++] = 20;
      dataBuffer[len++] = -86;
      dataBuffer[len++] = 0;
      if (!isMSF)
      {
        dataBuffer[len++] = (byte)(totalBlocks >> 24 & 0xFF);
        dataBuffer[len++] = (byte)(totalBlocks >> 16 & 0xFF);
        dataBuffer[len++] = (byte)(totalBlocks >> 8 & 0xFF);
        dataBuffer[len++] = (byte)(totalBlocks & 0xFF);
      }
      else
      {
        dataBuffer[len++] = 0;
        dataBuffer[len++] = (byte)m;
        dataBuffer[len++] = (byte)s;
        dataBuffer[len++] = (byte)f;
      }
    } else if (1 == format) {
      dataBuffer[2] = 1;
      dataBuffer[3] = 1;
      dataBuffer[len++] = 0;
      dataBuffer[len++] = 0;
      dataBuffer[len++] = 0;
      dataBuffer[len++] = 0;
      dataBuffer[len++] = 0;
      dataBuffer[len++] = 0;
      dataBuffer[len++] = 0;
      dataBuffer[len++] = 0;
    }
    else if (2 == format) {
      dataBuffer[2] = 1;
      dataBuffer[3] = 1;
      for (int j = 0; j < 4; j++) {
        dataBuffer[len++] = 1;
        dataBuffer[len++] = 20;
        dataBuffer[len++] = 0;
        if (j < 3) {
          dataBuffer[len++] = (byte)(160 + j);
        }
        else {
          dataBuffer[len++] = 1;
        } 
        dataBuffer[len++] = 0;
        dataBuffer[len++] = 0;
        dataBuffer[len++] = 0;
        if (j < 2) {
          dataBuffer[len++] = 0;
          dataBuffer[len++] = 1;
          dataBuffer[len++] = 0;
          dataBuffer[len++] = 0;
        }
        else if (j == 2) {
          if (isMSF)
          {
            dataBuffer[len++] = 0;
            dataBuffer[len++] = (byte)m;
            dataBuffer[len++] = (byte)s;
            dataBuffer[len++] = (byte)f;
          }
          else
          {
            dataBuffer[len++] = (byte)(totalBlocks >> 24 & 0xFF);
            dataBuffer[len++] = (byte)(totalBlocks >> 16 & 0xFF);
            dataBuffer[len++] = (byte)(totalBlocks >> 8 & 0xFF);
            dataBuffer[len++] = (byte)(totalBlocks & 0xFF);
          }
        } else {
          dataBuffer[len++] = 0;
          dataBuffer[len++] = 0;
          dataBuffer[len++] = 0;
          dataBuffer[len++] = 0;
        }
      } 
    } else {
      TestPrint.println(1, "Read TCO format 10 requested!");
      throw new VMException(252);
    } 
    dataBuffer[0] = (byte)(len - 2 >> 8 & 0xFF);
    dataBuffer[1] = (byte)(len - 2 & 0xFF);
    return len;
  }
  public abstract void startStopUnit(boolean paramBoolean1, boolean paramBoolean2) throws VMException;
  public abstract boolean preventAllowMediumRemoval(boolean paramBoolean);
  public void setWriteProtect(boolean isWP) {
    this.isWP = true;
  }
}
