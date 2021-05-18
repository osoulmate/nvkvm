package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
public class UnallocatedSpaceDescriptor
  extends VolumeDescriptorSequenceItem
{
  public long NumberofAllocationDescriptors;
  public Extend_ad[] AllocationDescriptors;
  public UnallocatedSpaceDescriptor() {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.TagIdentifier = 7;
    this.AllocationDescriptors = new Extend_ad[0];
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.read(myRandomAccessFile);
    this.VolumeDescriptorSequenceNumber = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.NumberofAllocationDescriptors = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.AllocationDescriptors = new Extend_ad[(int)this.NumberofAllocationDescriptors];
    for (int i = 0; i < this.NumberofAllocationDescriptors; i++) {
      this.AllocationDescriptors[i] = new Extend_ad();
      this.AllocationDescriptors[i].read(myRandomAccessFile);
    } 
  }
  public byte[] getBytesWithoutDescriptorTag() {
    byte[] rawBytes = new byte[8 + (int)this.NumberofAllocationDescriptors * 8];
    int pos = 0;
    pos = BinaryTools.getUInt32BytesFromLong(this.VolumeDescriptorSequenceNumber, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.NumberofAllocationDescriptors, rawBytes, pos);
    for (int i = 0; i < this.AllocationDescriptors.length; i++) {
      byte[] allocationDescriptorBytes = this.AllocationDescriptors[i].getBytes();
      System.arraycopy(allocationDescriptorBytes, 0, rawBytes, pos, allocationDescriptorBytes.length);
      pos += allocationDescriptorBytes.length;
    } 
    return rawBytes;
  }
}
