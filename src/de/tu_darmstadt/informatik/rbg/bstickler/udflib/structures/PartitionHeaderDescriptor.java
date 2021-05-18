package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import java.io.IOException;
import java.io.RandomAccessFile;
public class PartitionHeaderDescriptor
{
  public Short_ad UnallocatedSpaceTable = new Short_ad();
  public Short_ad UnallocatedSpaceBitmap = new Short_ad();
  public Short_ad PartitionIntegrityTable = new Short_ad();
  public Short_ad FreedSpaceTable = new Short_ad();
  public Short_ad FreedSpaceBitmap = new Short_ad();
  public byte[] Reserved = new byte[88];
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.UnallocatedSpaceTable = new Short_ad();
    this.UnallocatedSpaceTable.read(myRandomAccessFile);
    this.UnallocatedSpaceBitmap = new Short_ad();
    this.UnallocatedSpaceBitmap.read(myRandomAccessFile);
    this.PartitionIntegrityTable = new Short_ad();
    this.PartitionIntegrityTable.read(myRandomAccessFile);
    this.FreedSpaceTable = new Short_ad();
    this.FreedSpaceTable.read(myRandomAccessFile);
    this.FreedSpaceBitmap = new Short_ad();
    this.FreedSpaceBitmap.read(myRandomAccessFile);
    this.Reserved = new byte[88];
    myRandomAccessFile.read(this.Reserved);
  }
  public byte[] getBytes() {
    byte[] UnallocatedSpaceTableBytes = this.UnallocatedSpaceTable.getBytes();
    byte[] UnallocatedSpaceBitmapBytes = this.UnallocatedSpaceBitmap.getBytes();
    byte[] PartitionIntegrityTableBytes = this.PartitionIntegrityTable.getBytes();
    byte[] FreedSpaceTableByte = this.FreedSpaceTable.getBytes();
    byte[] FreedSpaceBitmapByte = this.FreedSpaceBitmap.getBytes();
    byte[] rawBytes = new byte[88 + 
        UnallocatedSpaceTableBytes.length + 
        UnallocatedSpaceBitmapBytes.length + 
        PartitionIntegrityTableBytes.length + 
        FreedSpaceTableByte.length + 
        FreedSpaceBitmapByte.length];
    int pos = 0;
    System.arraycopy(UnallocatedSpaceTableBytes, 0, rawBytes, pos, UnallocatedSpaceTableBytes.length);
    pos += UnallocatedSpaceTableBytes.length;
    System.arraycopy(UnallocatedSpaceBitmapBytes, 0, rawBytes, pos, UnallocatedSpaceBitmapBytes.length);
    pos += UnallocatedSpaceBitmapBytes.length;
    System.arraycopy(PartitionIntegrityTableBytes, 0, rawBytes, pos, PartitionIntegrityTableBytes.length);
    pos += PartitionIntegrityTableBytes.length;
    System.arraycopy(FreedSpaceTableByte, 0, rawBytes, pos, FreedSpaceTableByte.length);
    pos += FreedSpaceTableByte.length;
    System.arraycopy(FreedSpaceBitmapByte, 0, rawBytes, pos, FreedSpaceBitmapByte.length);
    pos += FreedSpaceBitmapByte.length;
    System.arraycopy(this.Reserved, 0, rawBytes, pos, this.Reserved.length);
    pos += this.Reserved.length;
    return rawBytes;
  }
}
