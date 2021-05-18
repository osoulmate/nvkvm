package com.huawei.vm.console.storageV1.impl;
import com.huawei.vm.console.utilsV1.ImageIO;
import com.huawei.vm.console.utilsV1.TestPrint;
import com.huawei.vm.console.utilsV1.VMException;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.UDFExtendFile;
import java.util.Map;
public class FloppyImage
  extends FloppyDriver
{
  private ImageIO image = new ImageIO();
  private ImageIO newImage = null;
  public FloppyImage(String path, boolean isMustExist) throws VMException {
    super(path);
    this.mustExist = isMustExist;
    this.image.open(this.deviceName, this.mustExist, true);
    validCapacity(this.image);
  }
  private void validCapacity(ImageIO image) throws VMException {
    if ((getBlockLength() * getTotalBlocks()) != image.getMediumSize()) {
      try {
        this.image.close();
      }
      catch (VMException e) {
        TestPrint.println(1, "Floppy Image:close error");
      } 
      throw new VMException(335);
    } 
  }
  protected void open(String path) throws VMException {
    this.image.open(path, this.mustExist, true);
  }
  public boolean isWriteProtect() {
    return (super.isWriteProtect() || !this.image.canWrite());
  }
  public void formatUnit(int mediumType, int startCylinderNumber, int endCylinderNumber, int startHeadNumber, int endHeadNumber) {}
  public void write(byte[] dataBuffer, long startPosition, int length) throws VMException {
    if (!isWriteProtect()) {
      this.image.write(dataBuffer, startPosition, length);
    }
    else {
      throw new VMException(254);
    } 
  }
  public void inquiry() {}
  public int read(byte[] dataBuffer, long startPosition, int length) throws VMException {
    return this.image.read(dataBuffer, startPosition, length);
  }
  public long getMediumSize() throws VMException {
    long size = this.image.getMediumSize();
    if (0L > size)
    {
      throw new VMException(253);
    }
    return size;
  }
  public void close() throws VMException {
    this.image.close();
  }
  protected void prepareChangeDisk(String localDirName, Map<Long, UDFExtendFile> memoryStructMap, String diskName) throws VMException {
    this.newImage = new ImageIO();
    this.newImage.open(diskName, true, true);
    validCapacity(this.newImage);
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
  public boolean isInited() {
    return (this.image.isActive() && this.needInit);
  }
}
