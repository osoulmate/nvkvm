package com.huawei.vm.console.storage.impl;
import com.huawei.vm.console.utils.LocalDirImageIO;
import com.huawei.vm.console.utils.ResourceUtil;
import com.huawei.vm.console.utils.TestPrint;
import com.huawei.vm.console.utils.VMException;
import com.kvm.UDFExtendFile;
import java.util.Map;
public class CDROMLocalDir
  extends CDROMDriver
{
  private LocalDirImageIO image = new LocalDirImageIO();
  private LocalDirImageIO newImage = null;
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
  public void startStopUnit(boolean isEject, boolean isStart) throws VMException {
    if (isEject && !isStart) {
      setIsoDiskChanged(true);
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
