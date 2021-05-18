package com.huawei.vm.console.storage.impl;
import com.huawei.vm.console.communication.VMTimerTask;
import com.huawei.vm.console.management.ConsoleControllers;
import com.huawei.vm.console.utils.DeviceIO;
import com.huawei.vm.console.utils.VMException;
import com.kvm.UDFExtendFile;
import java.util.Map;
public class FloppyDevice
  extends FloppyDriver
{
  private final DeviceIO device;
  private boolean needReOpen = false;
  private VMTimerTask timerTask = null;
  public FloppyDevice(String path) throws VMException {
    super(path);
    this.mustExist = true;
    this.device = new DeviceIO();
    if (!ConsoleControllers.isSetUp())
    {
      throw new VMException(210);
    }
    int result = this.device.open(this.deviceName);
    if (0 != result) {
      if (33 == result || 32 == result)
      {
        throw new VMException(223);
      }
      throw new VMException(220);
    } 
  }
  public FloppyDevice(String path, VMTimerTask timerTask) throws VMException {
    super(path);
    this.mustExist = true;
    this.device = new DeviceIO();
    if (!ConsoleControllers.isSetUp())
    {
      throw new VMException(210);
    }
    int result = this.device.open(this.deviceName);
    if (0 != result) {
      if (33 == result || 32 == result)
      {
        throw new VMException(223);
      }
      throw new VMException(220);
    } 
    if (0L > this.device.size()) {
      this.needReOpen = true;
      if (null != timerTask) {
        this.timerTask = timerTask;
        this.timerTask.startFloppyReopen(this);
      } 
    } 
  }
  protected void open(String path) throws VMException {
    int result = this.device.open(path);
    if (0 != result) {
      if (33 == result || 32 == result)
      {
        throw new VMException(223);
      }
      throw new VMException(220);
    } 
  }
  public void close() throws VMException {
    int result = this.device.close();
    if (null != this.timerTask) {
      this.timerTask.stopFloppyReopen();
      this.timerTask = null;
    } 
    if (0 > result)
    {
      throw new VMException(221);
    }
  }
  public boolean isWriteProtect() {
    return (super.isWriteProtect() || !this.device.canWrite());
  }
  public void formatUnit(int mediumType, int startCylinderNumber, int endCylinderNumber, int startHeadNumber, int endHeadNumber) throws VMException {
    this.device.setBadTrackNumber(0);
    int result = this.device.format(mediumType, startCylinderNumber, endCylinderNumber, startHeadNumber, endHeadNumber);
    if (0 != result)
    {
      throw new VMException(this.device.getBadTrackNumber());
    }
  }
  public int read(byte[] dataBuffer, long startPosition, int length) throws VMException {
    int result = this.device.read(startPosition, length, dataBuffer);
    if (0 > result)
    {
      throw new VMException(250);
    }
    return result;
  }
  public long getMediumSize() throws VMException {
    long size = this.device.size();
    if (0L > size)
    {
      throw new VMException(253);
    }
    if (this.needReOpen) {
      this.needReOpen = false;
      synchronized (this.device) {
        this.device.close();
        if (null != this.timerTask) {
          this.timerTask.stopFloppyReopen();
          this.timerTask = null;
        } 
        this.device.open(this.deviceName);
      } 
    } 
    return size;
  }
  public void write(byte[] dataBuffer, long startPosition, int length) throws VMException {
    int result = -1;
    if (!isWriteProtect())
    {
      result = this.device.write(startPosition, length, dataBuffer);
    }
    if (0 > result) {
      if (isWriteProtect())
      {
        throw new VMException(254);
      }
      if (0L > getMediumSize())
      {
        throw new VMException(253);
      }
      throw new VMException(250);
    } 
  }
  public void inquiry() {}
  public boolean isInited() {
    this.device.size();
    return this.device.isActive();
  }
  public int getBlockLength() {
    return this.device.getBytesPerSector();
  }
  protected void prepareChangeDisk(String localDirName, Map<Long, UDFExtendFile> memoryStructMap, String diskName) throws VMException {}
  public void eject() {}
  public void insert() throws VMException {}
}
