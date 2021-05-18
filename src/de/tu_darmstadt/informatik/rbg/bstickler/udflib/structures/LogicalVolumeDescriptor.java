package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.OSTAUnicode;
import java.io.IOException;
import java.io.RandomAccessFile;
public class LogicalVolumeDescriptor
  extends VolumeDescriptorSequenceItem
{
  public CharSpec DescriptorCharacterSet;
  public byte[] LogicalVolumeIdentifier;
  public long LogicalBlockSize;
  public EntityID DomainIdentifier;
  public Long_ad LogicalVolumeContentsUse;
  public long MapTableLength;
  public long NumberofPartitionMaps;
  public EntityID ImplementationIdentifier;
  public byte[] ImplementationUse;
  public Extend_ad IntegritySequenceExtent;
  public byte[] PartitionMaps;
  public LogicalVolumeDescriptor() {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.TagIdentifier = 6;
    this.DescriptorCharacterSet = new CharSpec();
    this.LogicalVolumeIdentifier = new byte[128];
    this.DomainIdentifier = new EntityID();
    this.LogicalVolumeContentsUse = new Long_ad();
    this.ImplementationIdentifier = new EntityID();
    this.ImplementationUse = new byte[128];
    this.IntegritySequenceExtent = new Extend_ad();
    this.PartitionMaps = new byte[0];
  }
  public void setLogicalVolumeIdentifier(String volumeIdentifier) throws Exception {
    if (volumeIdentifier.length() > 126)
    {
      throw new Exception("error: logical volume identifier length > 126 characters");
    }
    this.LogicalVolumeIdentifier = new byte[128];
    try {
      byte[] volumeIdentifierBytes = volumeIdentifier.getBytes("UTF-16");
      int compId = OSTAUnicode.getBestCompressionId(volumeIdentifierBytes);
      byte[] tmpIdentifier = OSTAUnicode.CompressUnicodeByte(volumeIdentifierBytes, compId);
      int length = (tmpIdentifier.length < 127) ? tmpIdentifier.length : 127;
      System.arraycopy(tmpIdentifier, 0, this.LogicalVolumeIdentifier, 0, length);
      this.LogicalVolumeIdentifier[this.LogicalVolumeIdentifier.length - 1] = (byte)length;
    }
    catch (Exception exception) {}
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.read(myRandomAccessFile);
    this.VolumeDescriptorSequenceNumber = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.DescriptorCharacterSet = new CharSpec();
    this.DescriptorCharacterSet.read(myRandomAccessFile);
    this.LogicalVolumeIdentifier = new byte[128];
    myRandomAccessFile.read(this.LogicalVolumeIdentifier);
    this.LogicalBlockSize = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.DomainIdentifier = new EntityID();
    this.DomainIdentifier.read(myRandomAccessFile);
    this.LogicalVolumeContentsUse = new Long_ad();
    this.LogicalVolumeContentsUse.read(myRandomAccessFile);
    this.MapTableLength = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.NumberofPartitionMaps = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.ImplementationIdentifier = new EntityID();
    this.ImplementationIdentifier.read(myRandomAccessFile);
    this.ImplementationUse = new byte[128];
    myRandomAccessFile.read(this.ImplementationUse);
    this.IntegritySequenceExtent = new Extend_ad();
    this.IntegritySequenceExtent.read(myRandomAccessFile);
    this.PartitionMaps = new byte[(int)this.MapTableLength];
    myRandomAccessFile.read(this.PartitionMaps);
  }
  public byte[] getBytesWithoutDescriptorTag() {
    byte[] DescriptorCharacterSetBytes = this.DescriptorCharacterSet.getBytes();
    byte[] DomainIdentifierBytes = this.DomainIdentifier.getBytes();
    byte[] LogicalVolumeContentsUseBytes = this.LogicalVolumeContentsUse.getBytes();
    byte[] ImplementationIdentifierBytes = this.ImplementationIdentifier.getBytes();
    byte[] IntegritySequenceExtentBytes = this.IntegritySequenceExtent.getBytes();
    byte[] rawBytes = new byte[272 + 
        DescriptorCharacterSetBytes.length + 
        DomainIdentifierBytes.length + 
        LogicalVolumeContentsUseBytes.length + 
        ImplementationIdentifierBytes.length + 
        IntegritySequenceExtentBytes.length + 
        this.PartitionMaps.length];
    int pos = 0;
    pos = BinaryTools.getUInt32BytesFromLong(this.VolumeDescriptorSequenceNumber, rawBytes, pos);
    System.arraycopy(DescriptorCharacterSetBytes, 0, rawBytes, pos, DescriptorCharacterSetBytes.length);
    pos += DescriptorCharacterSetBytes.length;
    System.arraycopy(this.LogicalVolumeIdentifier, 0, rawBytes, pos, this.LogicalVolumeIdentifier.length);
    pos += this.LogicalVolumeIdentifier.length;
    pos = BinaryTools.getUInt32BytesFromLong(this.LogicalBlockSize, rawBytes, pos);
    System.arraycopy(DomainIdentifierBytes, 0, rawBytes, pos, DomainIdentifierBytes.length);
    pos += DomainIdentifierBytes.length;
    System.arraycopy(LogicalVolumeContentsUseBytes, 0, rawBytes, pos, LogicalVolumeContentsUseBytes.length);
    pos += LogicalVolumeContentsUseBytes.length;
    pos = BinaryTools.getUInt32BytesFromLong(this.MapTableLength, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.NumberofPartitionMaps, rawBytes, pos);
    System.arraycopy(ImplementationIdentifierBytes, 0, rawBytes, pos, ImplementationIdentifierBytes.length);
    pos += ImplementationIdentifierBytes.length;
    System.arraycopy(this.ImplementationUse, 0, rawBytes, pos, this.ImplementationUse.length);
    pos += this.ImplementationUse.length;
    System.arraycopy(IntegritySequenceExtentBytes, 0, rawBytes, pos, IntegritySequenceExtentBytes.length);
    pos += IntegritySequenceExtentBytes.length;
    System.arraycopy(this.PartitionMaps, 0, rawBytes, pos, this.PartitionMaps.length);
    pos += this.PartitionMaps.length;
    return rawBytes;
  }
}
