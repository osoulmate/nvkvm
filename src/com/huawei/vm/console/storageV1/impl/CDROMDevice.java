package com.huawei.vm.console.storageV1.impl;
import com.huawei.vm.console.managementV1.ConsoleControllers;
import com.huawei.vm.console.newUtils.DeviceIO;
import com.huawei.vm.console.utilsV1.TestPrint;
import com.huawei.vm.console.utilsV1.VMException;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.UDFExtendFile;
import java.util.Map;
public class CDROMDevice
  extends CDROMDriver
{
  private final DeviceIO device;
  public CDROMDevice(String path) throws VMException {
    super(path);
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
  public void open(String path) throws VMException {
    if (this.device.isActive())
    {
      this.device.close();
    }
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
    if (0 > result)
    {
      throw new VMException(221);
    }
  }
  public int modeSense(byte[] dataBuffer, int pc, int pageCode) {
    int mediumType = this.device.getMediumType();
    dataBuffer[0] = 0;
    dataBuffer[1] = 6;
    dataBuffer[2] = (byte)mediumType;
    dataBuffer[3] = 0;
    dataBuffer[4] = 0;
    dataBuffer[5] = 0;
    dataBuffer[6] = 0;
    dataBuffer[7] = 0;
    return 8;
  }
  public int read(byte[] dataBuffer, long startPosition, int length) throws VMException {
    int result = this.device.read(startPosition, length, dataBuffer);
    if (0 >= result) {
      getMediumSize();
      if (0 > result)
      {
        throw new VMException(250);
      }
    } 
    return result;
  }
  public long getMediumSize() throws VMException {
    long size = this.device.getCapaticy();
    if (size < 0L)
    {
      throw new VMException(253);
    }
    return size;
  }
  public int readTOC(byte[] dataBuffer, boolean isMSF, int format, int startTrack) throws VMException {
    int dataLen = this.device.readTOC(dataBuffer, isMSF, format, startTrack);
    if (0 > dataLen)
    {
      throw new VMException(253);
    }
    return dataLen;
  }
  public int testUnitReady() {
    int curState = this.device.testUnitReady();
    TestPrint.println(3, "CDROM Device : device state:" + getDeviceState() + ";Cur state:" + curState);
    if (2 == curState) {
      this.device.close();
      this.device.open(this.deviceName);
    } 
    if (0 > curState)
    {
      curState = 0;
    }
    return curState;
  }
  public boolean preventAllowMediumRemoval(boolean isPrevent) {
    return (this.device.preventAllowMediumRemoval(isPrevent) >= 0);
  }
  public void startStopUnit(boolean isEject, boolean isStart) throws VMException {
    if (isEject && !isStart) {
      if (0 == this.device.eject(true))
      {
        setDeviceState(0);
      }
      else
      {
        throw new VMException(252);
      }
    } else if (isEject && isStart) {
      if (0 == this.device.eject(false))
      {
        refreshState();
      }
      else
      {
        throw new VMException(252);
      }
    } else {
      throw new VMException(252);
    } 
  }
  protected void prepareChangeDisk(String localDirName, Map<Long, UDFExtendFile> memoryStructMap, String diskName) {}
  public void eject() throws VMException {
    startStopUnit(true, false);
  }
  public void insert() throws VMException {
    startStopUnit(true, true);
  }
  public boolean isInited() {
    return this.device.isActive();
  }
  public void inquiry() {}
}
