package com.huawei.vm.console.storage.impl;
import com.huawei.vm.console.utils.ImageIO;
import com.huawei.vm.console.utils.VMException;
import com.kvm.UDFExtendFile;
import com.library.LoggerUtil;
import java.io.IOException;
import java.util.Map;
public class CDROMImage
  extends CDROMDriver
{
  private ImageIO image = new ImageIO();
  private ImageIO newImage = null;
  public CDROMImage(String path, boolean isMustExist) throws VMException {
    super(path);
    this.mustExist = isMustExist;
    try {
      this.image.open(this.deviceName, this.mustExist);
    }
    catch (IOException e) {
      LoggerUtil.error("Invalid file name");
    } 
  }
  protected void open(String path) throws VMException {
    try {
      this.image.open(path, this.mustExist);
    }
    catch (IOException e) {
      LoggerUtil.error("Invalid file name");
    } 
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
    return this.image.read(dataBuffer, startPosition, length);
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
    this.newImage = new ImageIO();
    try {
      this.newImage.open(diskName, true);
    }
    catch (IOException e) {
      LoggerUtil.error("Invalid file name");
    } 
  }
  public void eject() {
    try {
      this.deviceName = null;
      this.needInit = false;
      close();
    }
    catch (VMException e) {
      LoggerUtil.error(e.getClass().getName());
    } 
  }
  public void insert() throws VMException {
    if (this.newImage != null) {
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
    return (!this.needInit || this.image.isActive());
  }
}
