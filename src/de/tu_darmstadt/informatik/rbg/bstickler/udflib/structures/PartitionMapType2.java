package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
public class PartitionMapType2
{
  public byte PartitionMapType = 2;
  public byte PartitionMapLength = 64;
  public byte[] PartitionIdentifier = new byte[62];
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.PartitionMapType = myRandomAccessFile.readByte();
    this.PartitionMapLength = myRandomAccessFile.readByte();
    this.PartitionIdentifier = new byte[62];
    myRandomAccessFile.read(this.PartitionIdentifier);
  }
  public void write(RandomAccessFile myRandomAccessFile) throws IOException {
    myRandomAccessFile.write(getBytes());
  }
  public byte[] getBytes() {
    byte[] rawBytes = new byte[64];
    rawBytes[0] = this.PartitionMapType;
    rawBytes[1] = this.PartitionMapLength;
    System.arraycopy(this.PartitionIdentifier, 0, rawBytes, 2, this.PartitionIdentifier.length);
    return rawBytes;
  }
  public void setupMetadataPartitionMap(EntityID partitionTypeIdentifier, int VolumeSequenceNumber, int PartitionNumber, long MetadataFileLocation, long MetadataMirrorFileLocation, long MetadataBitmapFileLocation, long AllocationUnitSize, int AlignmentUnitSize, byte Flags) {
    byte[] Reserved1 = new byte[2];
    byte[] Reserved2 = new byte[5];
    byte[] partitionTypeIdentifierBytes = partitionTypeIdentifier.getBytes();
    int pos = 0;
    System.arraycopy(Reserved1, 0, this.PartitionIdentifier, pos, Reserved1.length);
    pos += Reserved1.length;
    System.arraycopy(partitionTypeIdentifierBytes, 0, this.PartitionIdentifier, pos, partitionTypeIdentifierBytes.length);
    pos += partitionTypeIdentifierBytes.length;
    pos = BinaryTools.getUInt16BytesFromInt(VolumeSequenceNumber, this.PartitionIdentifier, pos);
    pos = BinaryTools.getUInt16BytesFromInt(PartitionNumber, this.PartitionIdentifier, pos);
    pos = BinaryTools.getUInt32BytesFromLong(MetadataFileLocation, this.PartitionIdentifier, pos);
    pos = BinaryTools.getUInt32BytesFromLong(MetadataMirrorFileLocation, this.PartitionIdentifier, pos);
    pos = BinaryTools.getUInt32BytesFromLong(MetadataBitmapFileLocation, this.PartitionIdentifier, pos);
    pos = BinaryTools.getUInt32BytesFromLong(AllocationUnitSize, this.PartitionIdentifier, pos);
    pos = BinaryTools.getUInt16BytesFromInt(AlignmentUnitSize, this.PartitionIdentifier, pos);
    this.PartitionIdentifier[pos++] = Flags;
    System.arraycopy(Reserved2, 0, this.PartitionIdentifier, pos, Reserved2.length);
    pos += Reserved2.length;
  }
}
