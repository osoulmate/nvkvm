package com.huawei.vm.console.processV1;
import com.huawei.vm.console.storageV1.MassStorageDevice;
import com.huawei.vm.console.storageV1.impl.CDROMDevice;
import com.huawei.vm.console.storageV1.impl.FloppyDevice;
import com.huawei.vm.console.utilsV1.ImageIO;
import com.huawei.vm.console.utilsV1.ResourceUtil;
import com.huawei.vm.console.utilsV1.TestPrint;
import com.huawei.vm.console.utilsV1.VMException;
import java.io.File;
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
  public static final String FILE_FILTER_EXT_ISO = "*.iso";
  public static final String FILE_FILTER_EXT_IMG = "*.img";
  public static final String FILE_FILTER_EXT_ISO_IMG = "*.iso;*.img";
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
      TestPrint.println(1, "Create Image:Beging to prepare for create.Device:" + device + ";Image:" + fileSavePath);
      this.imagePath = fileSavePath;
      if (3 == type) {
        TestPrint.println(1, "Create Image:The device is a cdrom");
        this.device = (MassStorageDevice)new CDROMDevice(device);
        this.bufferSize = 65536;
        this.imageExt = ".iso";
      }
      else if (1 == type) {
        TestPrint.println(1, "Create Image:The device is a floppy");
        this.device = (MassStorageDevice)new FloppyDevice(device);
        this.bufferSize = 40960;
        this.imageExt = ".img";
      }
      else {
        TestPrint.println(1, "Create Image:The device is a unsupported device");
        throw new VMException(324);
      } 
      this.mediumSize = this.device.getMediumSize();
      TestPrint.println(1, "Create Image:The size of medium in the driver is :" + this.mediumSize);
      if (0L > this.mediumSize)
      {
        throw new VMException(331);
      }
      this.image = new ImageIO();
      this.image.open(fileSavePath, false);
      this.image.setImageLength(this.mediumSize);
    }
    catch (VMException e) {
      TestPrint.println(4, "Create Image: error happened" + e.getKey());
      this.exitFlag = true;
      this.createState = e.getKey();
    } 
    Thread thread = new Thread(this);
    thread.start();
  }
  private void adjustFileName() {
    String fileName = this.imagePath;
    int sepIndex = this.imagePath.lastIndexOf(System.getProperty("file.separator"));
    if (-1 != sepIndex) {
      fileName = this.imagePath.substring(sepIndex + 1, this.imagePath.length());
      TestPrint.println(1, "Create Image: adjust file name -- imagePath:" + this.imagePath + "; Image name:" + fileName);
    } 
    if (fileName.indexOf('.') == -1) {
      this.imagePath += this.imageExt;
      this.image.rename(this.imagePath);
    } 
  }
  public void run() {
    int retryCount = 0;
    long off = 0L;
    long length = this.mediumSize;
    int curRead = 0;
    int curPro = this.bufferSize;
    byte[] buffer = new byte[this.bufferSize];
    if (0 == this.createState && 0L == length)
    {
      this.createState = 100;
    }
    TestPrint.println(1, "Create Image: Begin to create");
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
        }
        else {
          if (READ_RETRY == retryCount) {
            this.exitFlag = true;
            this.createState = 325;
            continue;
          } 
          this.device.testUnitReady();
          continue;
        } 
        TestPrint.println(2, e.getKey());
      } 
    } 
    try {
      if (null != this.device)
      {
        this.device.close();
      }
    }
    catch (VMException e) {
      TestPrint.println(1, "Device close error");
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
          TestPrint.println(1, "Image file close error!");
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
          this.image.setImageLength(off);
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
        adjustFileName();
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
