package com.huawei.vm.console.storageV1.impl;
import com.huawei.vm.console.utilsV1.LocalDirImageIO;
import com.huawei.vm.console.utilsV1.ResourceUtil;
import com.huawei.vm.console.utilsV1.TestPrint;
import com.huawei.vm.console.utilsV1.VMException;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.UDFExtendFile;
import java.util.Map;
public class CDROMLocalDir
  extends CDROMDriver
{
  private LocalDirImageIO image = new LocalDirImageIO();
  protected Map<Long, UDFExtendFile> memoryStructMap = null;
  private LocalDirImageIO newImage = null;
  public Map<Long, UDFExtendFile> getMemoryStructMap() {
    return this.memoryStructMap;
  }
  public CDROMLocalDir(String path, boolean isMustExist, String localDirName, Map<Long, UDFExtendFile> memoryStruct, ResourceUtil util) throws VMException {
    super(path);
    this.mustExist = isMustExist;
    this.memoryStructMap = memoryStruct;
    this.image.openMemoryISO(this.deviceName, this.mustExist, localDirName, memoryStruct);
  }
  protected void open(String path) throws VMException {
    this.image.openMemoryISO(path, this.mustExist, this.localDirName, this.memoryStructMap);
  }
  public void close() throws VMException {
    this.image.close();
  }
  public void inquiry() {}
  public int modeSense(byte[] dataBuffer, int pc, int pageCode) {
    dataBuffer[0] = 0;
    dataBuffer[1] = 6;
    dataBuffer[2] = (byte) ((0 != testUnitReady()) ? 1 : 112);
    dataBuffer[3] = 0;
    dataBuffer[4] = 0;
    dataBuffer[5] = 0;
    dataBuffer[6] = 0;
    dataBuffer[7] = 0;
    return 8;
  }
  public int read(byte[] dataBuffer, long startPosition, int length) throws VMException {
    return this.image.readMemeoryISO(dataBuffer, startPosition, length);
  }
  public long getMediumSize() throws VMException {
    long result = this.image.getMediumSize();
    if (0L > result)
    {
      throw new VMException(253);
    }
    return result;
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
  public void startStopUnit(boolean isEject, boolean isStart) throws VMException {
    if (isEject && !isStart) {
      this.isIsoDiskChanged = true;
      setDeviceState(0);
      this.needInit = false;
    }
    else if (isEject && isStart) {
      setDeviceState(3);
      this.needInit = true;
    }
    else {
      throw new VMException(252);
    } 
  }
  protected void prepareChangeDisk(String localDirName, Map<Long, UDFExtendFile> memoryStructMap, String diskName) throws VMException {
    this.newImage = new LocalDirImageIO();
    this.newImage.openMemoryISO(diskName, true, localDirName, memoryStructMap);
  }
  public void eject() {
    try {
      setDeviceState(0);
      this.deviceName = null;
      this.needInit = false;
      close();
    }
    catch (VMException e) {
      TestPrint.println(3, "Image file close fail!");
    } 
  }
  public void insert() throws VMException {
    if (null != this.newImage) {
      this.image = this.newImage;
      this.newImage = null;
      setDeviceState(0);
      this.deviceName = this.newDiskName;
      this.newDiskName = null;
    } 
    this.needInit = true;
  }
  public boolean preventAllowMediumRemoval(boolean isPrevent) {
    return true;
  }
  public boolean isInited() {
    return (this.image.isActive() || !this.needInit);
  }
}
