package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
public class PartitionDescriptor
  extends VolumeDescriptorSequenceItem
{
  public int PartitionFlags;
  public int PartitionNumber;
  public EntityID PartitionContents;
  public PartitionHeaderDescriptor PartitionContentsUse;
  public long AccessType;
  public long PartitonStartingLocation;
  public long PartitionLength;
  public EntityID ImplementationIdentifier;
  public byte[] ImplementationUse;
  public byte[] Reserved;
  public PartitionDescriptor() {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.TagIdentifier = 5;
    this.PartitionContents = new EntityID();
    this.PartitionContentsUse = new PartitionHeaderDescriptor();
    this.ImplementationIdentifier = new EntityID();
    this.ImplementationUse = new byte[128];
    this.Reserved = new byte[156];
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.read(myRandomAccessFile);
    this.VolumeDescriptorSequenceNumber = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.PartitionFlags = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.PartitionNumber = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.PartitionContents = new EntityID();
    this.PartitionContents.read(myRandomAccessFile);
    this.PartitionContentsUse = new PartitionHeaderDescriptor();
    this.PartitionContentsUse.read(myRandomAccessFile);
    this.AccessType = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.PartitonStartingLocation = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.PartitionLength = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.ImplementationIdentifier = new EntityID();
    this.ImplementationIdentifier.read(myRandomAccessFile);
    this.ImplementationUse = new byte[128];
    myRandomAccessFile.read(this.ImplementationUse);
    this.Reserved = new byte[156];
    myRandomAccessFile.read(this.Reserved);
  }
  public byte[] getBytesWithoutDescriptorTag() {
    byte[] PartitionContentsBytes = this.PartitionContents.getBytes();
    byte[] ImplementationIdentifierBytes = this.ImplementationIdentifier.getBytes();
    byte[] PartitionContentsUseBytes = this.PartitionContentsUse.getBytes();
    byte[] rawBytes = new byte[304 + 
        PartitionContentsBytes.length + 
        ImplementationIdentifierBytes.length + 
        PartitionContentsUseBytes.length];
    int pos = 0;
    pos = BinaryTools.getUInt32BytesFromLong(this.VolumeDescriptorSequenceNumber, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.PartitionFlags, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.PartitionNumber, rawBytes, pos);
    System.arraycopy(PartitionContentsBytes, 0, rawBytes, pos, PartitionContentsBytes.length);
    pos += PartitionContentsBytes.length;
    System.arraycopy(PartitionContentsUseBytes, 0, rawBytes, pos, PartitionContentsUseBytes.length);
    pos += PartitionContentsUseBytes.length;
    pos = BinaryTools.getUInt32BytesFromLong(this.AccessType, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.PartitonStartingLocation, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.PartitionLength, rawBytes, pos);
    System.arraycopy(ImplementationIdentifierBytes, 0, rawBytes, pos, ImplementationIdentifierBytes.length);
    pos += ImplementationIdentifierBytes.length;
    System.arraycopy(this.ImplementationUse, 0, rawBytes, pos, this.ImplementationUse.length);
    pos += this.ImplementationUse.length;
    System.arraycopy(this.Reserved, 0, rawBytes, pos, this.Reserved.length);
    pos += this.Reserved.length;
    return rawBytes;
  }
}
