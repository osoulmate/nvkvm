package com.huawei.vm.console.storageV1;
import com.huawei.vm.console.utilsV1.TestPrint;
import com.huawei.vm.console.utilsV1.VMException;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.UDFExtendFile;
import java.util.HashMap;
import java.util.Map;
public abstract class MassStorageDevice
{
  public static final int DEVICE_TYPE_FLOPPY_DEVICE = 0;
  public static final int DEVICE_TYPE_FLOPPY_IMAGE = 1;
  public static final int DEVICE_TYPE_CDROM_DEVICE = 2;
  public static final int DEVICE_TYPE_CDROM_IMAGE = 3;
  public static final int DEVICE_TYPE_UNSUPPORT = 4;
  public static final int DEVICE_TYPE_LOCAL_DIR_IMAGE = 5;
  public static final int UNKNOWN = 0;
  public static final int F5_1PT2_512 = 1;
  public static final int F3_1PT44_512 = 2;
  public static final int F3_2PT88_512 = 3;
  public static final int F3_720_512 = 5;
  public static final int REMOVABLE_MEDIA = 11;
  public static final int FIXEDMEDIA = 12;
  public static final int CD_ROM = 51;
  public static final int CD_R = 52;
  public static final int CD_RW = 53;
  public static final int DVD_ROM = 54;
  public static final int DVD_R = 55;
  public static final int DVD_RW = 56;
  public static final int STATE_MEDIUM_NOTPRESENT = 0;
  public static final int STATE_MEDIUM_CHANGE = 2;
  public static final int STATE_MEDIUM_READY = 3;
  public static final int STATE_NOT_READY = 4;
  public static final int STATE_BAD_MEDIA = 5;
  private int deviceState = 3;
  protected String deviceName;
  protected boolean isWP = true;
  protected boolean mustExist;
  private boolean isDiskChanged = false;
  public volatile boolean isIsoDiskChanged = false;
  protected String newDiskName = null;
  protected boolean needInit = true;
  protected String localDirName = null;
  public String getLocalDirName() {
    return this.localDirName;
  }
  public void setLocalDirName(String localDirName) {
    this.localDirName = localDirName;
  }
  public Map<Long, UDFExtendFile> getMemoryStructMap() {
    return this.memoryStructMap;
  }
  public void setMemoryStructMap(Map<Long, UDFExtendFile> memoryStructMap) {
    this.memoryStructMap = memoryStructMap;
  }
  protected Map<Long, UDFExtendFile> memoryStructMap = new HashMap<Long, UDFExtendFile>();
  public MassStorageDevice(String path) throws VMException {
    if (null == path || "".equals(path))
    {
      throw new VMException(333);
    }
    this.deviceName = path;
  }
  protected abstract void open(String paramString) throws VMException;
  public abstract void close() throws VMException;
  public void setWriteProtect(boolean isWP) {
    this.isWP = isWP;
  }
  public boolean isWriteProtect() {
    return this.isWP;
  }
  public abstract void eject() throws VMException;
  public abstract void insert() throws VMException;
  public abstract int read(byte[] paramArrayOfbyte, long paramLong, int paramInt) throws VMException;
  public abstract long getMediumSize() throws VMException;
  public int testUnitReady() {
    long size = 0L;
    int curState = 3;
    try {
      size = getMediumSize();
    }
    catch (VMException e) {
      size = -1L;
    } 
    TestPrint.println(2, "MassStorage Device:refresh data--size:" + size);
    if (size < 0L) {
      curState = 0;
    }
    else if (0 == getDeviceState()) {
      TestPrint.println(2, "MassStorage Device:refresh data--not present" + size);
      curState = 2;
    }
    else if (2 == getDeviceState()) {
      curState = 3;
    } 
    setDeviceState(curState);
    return curState;
  }
  public abstract int modeSense(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  public abstract void inquiry();
  public abstract boolean isInited();
  public void refreshState() {
    int curState = 3;
    try {
      if (!isInited())
      {
        TestPrint.println(2, "MassStorage Deivce: Refresh data--device not inited, re open it.");
        open(this.deviceName);
        curState = 0;
        setDeviceState(curState);
      }
    } catch (VMException e) {
      curState = 0;
      setDeviceState(curState);
    } 
  }
  public int getDeviceState() {
    return this.deviceState;
  }
  public void setDeviceState(int state) {
    this.deviceState = state;
  }
  public boolean isChangeDisk() {
    try {
      return this.isDiskChanged;
    }
    finally {
      this.isDiskChanged = false;
    } 
  }
  public void changeDisk(String diskName) throws VMException {
    if (null != diskName && !"".equals(diskName))
    {
      prepareChangeDisk(null, null, diskName);
    }
    this.newDiskName = diskName;
    this.isDiskChanged = true;
  }
  public void changeLocalDirDisk(String localDirName, Map<Long, UDFExtendFile> memoryStruct, String diskName) throws VMException {
    if (null != localDirName && !"".equals(localDirName))
    {
      prepareChangeDisk(localDirName, memoryStruct, diskName);
    }
    this.newDiskName = diskName;
    this.isDiskChanged = true;
  }
  public boolean isEject() {
    return (null == this.newDiskName || "".equals(this.newDiskName));
  }
  protected abstract void prepareChangeDisk(String paramString1, Map<Long, UDFExtendFile> paramMap, String paramString2) throws VMException;
}
