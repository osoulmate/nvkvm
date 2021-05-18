package com.huawei.vm.console.newUtils;
import com.huawei.vm.console.managementV1.ConsoleControllers;
import com.huawei.vm.console.utilsV1.ResourceUtil;
import com.huawei.vm.console.utilsV1.TestPrint;
public class DeviceIO
{
  public static final int ERROR_SHARING_VIOLATION = 32;
  public static final int ERROR_LOCK_VIOLATION = 33;
  public static final int RESULT_FAULT = -1;
  public static final int RESULT_OK = 0;
  public int fileHandle = -1;
  public int badTrackNumber = 0;
  public int cylinders = 0;
  public int tracksPerCylinder = 0;
  public int sectorsPerTrack = 0;
  public int bytesPerSector = 0;
  public int getBytesPerSector() {
    return this.bytesPerSector;
  }
  public void setBytesPerSector(int bytesPerSector) {
    this.bytesPerSector = bytesPerSector;
  }
  public native int open(String paramString);
  public native long size();
  public native long getCapaticy();
  public native int format(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  public native int getMediumType();
  public native int read(long paramLong, int paramInt, byte[] paramArrayOfbyte);
  public native int readTOC(byte[] paramArrayOfbyte, boolean paramBoolean, int paramInt1, int paramInt2);
  public native int write(long paramLong, int paramInt, byte[] paramArrayOfbyte);
  public native int close();
  public static native String getFloppyDevices();
  public static native String getCDROMDevices();
  public native int preventAllowMediumRemoval(boolean paramBoolean);
  public native int eject(boolean paramBoolean);
  public native int testUnitReady();
  public native boolean canWrite();
  public boolean isActive() {
    return (-1 != this.fileHandle);
  }
  protected void finalize() throws Throwable {
    if (this.fileHandle != -1)
    {
      close();
    }
    super.finalize();
  }
  static {
    try {
      System.load(ResourceUtil.getConfigItem("com.huawei.vm.console.config.library.path") + ResourceUtil.getConfigItem(ResourceUtil.CONFIG_VM_LIBARY));
    }
    catch (UnsatisfiedLinkError ue) {
      if (!ConsoleControllers.isSetUp())
      {
        ConsoleControllers.libarayPrepare();
      }
      try {
        String s1 = System.getProperty("file.separator");
        String s2 = System.getProperty("java.io.tmpdir");
        if (!s2.endsWith(s1))
        {
          s2 = s2 + s1;
        }
        s2 = s2 + ResourceUtil.getConfigItem(ResourceUtil.CONFIG_VM_LIBARY) + ConsoleControllers.getLibID() + ResourceUtil.getConfigItem("com.huawei.vm.console.config.libExt");
        System.load(s2);
      }
      catch (UnsatisfiedLinkError ufe) {
        TestPrint.println(4, "Can not find libaray!");
      } 
    } 
  }
}
