package com.huawei.vm.console.process;
import com.huawei.vm.console.storage.MassStorageDevice;
import com.huawei.vm.console.storage.impl.CDROMDevice;
import com.huawei.vm.console.storage.impl.FloppyDevice;
import com.huawei.vm.console.utils.ImageIO;
import com.huawei.vm.console.utils.ResourceUtil;
import com.huawei.vm.console.utils.TestPrint;
import com.huawei.vm.console.utils.VMException;
import com.library.LoggerUtil;
import java.io.File;
import java.io.IOException;
public class CreateImageFile
  implements Runnable
{
  private int curState;
  private int createState;
  private ImageIO image;
  private MassStorageDevice device;
  private long mediumSize;
  private int bufferSize;
  private static final int READ_RETRY = Integer.parseInt(ResourceUtil.getConfigItem("com.huawei.vm.console.config.device.read.retry"));
  private boolean exitFlag;
  private String imagePath;
  private String imageExt;
  public static final String FILE_EXT_ISO = ".iso";
  public static final String FILE_EXT_IMG = ".img";
  public static final int STATE_INIT = 700;
  public static final int STATE_CREATING = 701;
  public static final int STATE_STOPING = 702;
  public CreateImageFile() {
    init();
  }
  private void init() {
    this.createState = 0;
    this.mediumSize = 0L;
    this.exitFlag = false;
    this.bufferSize = 0;
    this.curState = 700;
  }
  public void create(String device, String fileSavePath, int type) {
    init();
    this.curState = 701;
    try {
      this.imagePath = fileSavePath;
      if (3 == type) {
        this.device = (MassStorageDevice)new CDROMDevice(device);
        this.bufferSize = 65536;
        this.imageExt = ".iso";
      }
      else if (1 == type) {
        this.device = (MassStorageDevice)new FloppyDevice(device);
        this.bufferSize = 40960;
        this.imageExt = ".img";
      }
      else {
        TestPrint.println(3, "Create Image:The device is a unsupported device");
        throw new VMException(324);
      } 
      this.mediumSize = this.device.getMediumSize();
      if (0L > this.mediumSize)
      {
        throw new VMException(331);
      }
      this.image = new ImageIO();
      try {
        this.image.open(fileSavePath, false);
      }
      catch (IOException e) {
        LoggerUtil.error("Invalid file name");
      } 
      this.image.setImageLength(this.mediumSize);
    }
    catch (VMException e) {
      this.exitFlag = true;
      this.createState = e.getKey();
    } 
    Thread thread = new Thread(this);
    thread.start();
  }
  private void adjustFileName() throws IOException {
    String fileName = this.imagePath;
    int sepIndex = this.imagePath.lastIndexOf(System.getProperty("file.separator"));
    if (-1 != sepIndex)
    {
      fileName = this.imagePath.substring(sepIndex + 1, this.imagePath.length());
    }
    if (fileName.indexOf('.') == -1) {
      this.imagePath += this.imageExt;
      if (this.image != null)
      {
        this.image.rename(this.imagePath);
      }
    } 
  }
  public void run() {
    int retryCount = 0;
    long off = 0L;
    long length = this.mediumSize;
    int curRead = 0;
    int curPro = 0;
    byte[] buffer = new byte[this.bufferSize];
    if (0 == this.createState && 0L == length)
    {
      this.createState = 100;
    }
    while (length > 0L && !this.exitFlag) {
      if (length > this.bufferSize) {
        curPro = this.bufferSize;
      }
      else {
        curPro = (int)length;
      } 
      try {
        curRead = this.device.read(buffer, off, curPro);
        this.image.write(buffer, off, curRead);
        off += curRead;
        if (curRead == curPro) {
          length -= curRead;
        }
        else {
          length = 0L;
          this.mediumSize = off;
        } 
        this.createState = (int)((float)off / (float)this.mediumSize * 100.0F);
        retryCount = 0;
      }
      catch (VMException e) {
        retryCount++;
        try {
          this.device.getMediumSize();
        }
        catch (VMException e1) {
          this.exitFlag = true;
          this.createState = e.getKey();
          continue;
        } 
        if (323 == e.getKey() && READ_RETRY == retryCount) {
          this.exitFlag = true;
          this.createState = e.getKey();
          continue;
        } 
        if (READ_RETRY == retryCount) {
          this.exitFlag = true;
          this.createState = 325;
          continue;
        } 
        this.device.testUnitReady();
      } 
    } 
    try {
      if (null != this.device)
      {
        this.device.close();
      }
    }
    catch (VMException e) {
      LoggerUtil.error(e.getClass().getName());
    }
    finally {
      if (this.exitFlag) {
        try {
          if (null != this.image)
          {
            this.image.close();
          }
        }
        catch (VMException e) {
          LoggerUtil.error(e.getClass().getName());
        } 
        if (332 == this.createState)
        {
          this.createState = 0;
        }
        if (null != this.imagePath) {
          File file = new File(this.imagePath);
          if (file.exists() && file.isFile() && !file.delete()) {
            TestPrint.println(3, "Exception happend.Delete the file");
            file.deleteOnExit();
          } 
        } 
      } else {
        try {
          if (null != this.image)
          {
            this.image.setImageLength(off);
          }
        }
        catch (VMException e) {
          TestPrint.println(3, "Adjust file size error");
        } 
        try {
          if (null != this.image)
          {
            this.image.close();
          }
        }
        catch (VMException e) {
          TestPrint.println(3, "Image file close error!");
        } 
        try {
          adjustFileName();
        }
        catch (IOException e) {
          LoggerUtil.error("Invalid file name");
        } 
      } 
      this.curState = 700;
    } 
  }
  public int getState() {
    return this.curState;
  }
  public void setExitFlag(boolean exitFlag) {
    if (701 == this.curState) {
      this.exitFlag = exitFlag;
      this.createState = 332;
      this.curState = 702;
    } 
  }
  public int getCreateState() {
    return this.createState;
  }
  public String getAbsoluteImagePath() {
    return this.image.getAbsoluteImagePath();
  }
}
