package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.Checksum;
import java.io.IOException;
import java.io.RandomAccessFile;
public class LogicalVolumeIntegrityDescriptor
{
  public Tag DescriptorTag;
  public Timestamp RecordingDateAndTime;
  public long IntegrityType;
  public Extend_ad NextIntegrityExtent;
  public LogicalVolumeHeaderDescriptor LogicalVolumeContensUse;
  public long NumberOfPartitions;
  public long LengthOfImplementationUse;
  public long[] FreeSpaceTable;
  public long[] SizeTable;
  public byte[] ImplementationUse;
  public LogicalVolumeIntegrityDescriptor() {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.TagIdentifier = 9;
    this.RecordingDateAndTime = new Timestamp();
    this.NextIntegrityExtent = new Extend_ad();
    this.LogicalVolumeContensUse = new LogicalVolumeHeaderDescriptor();
    this.FreeSpaceTable = new long[0];
    this.SizeTable = new long[0];
    this.ImplementationUse = new byte[0];
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.read(myRandomAccessFile);
    this.RecordingDateAndTime = new Timestamp();
    this.RecordingDateAndTime.read(myRandomAccessFile);
    this.IntegrityType = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.NextIntegrityExtent = new Extend_ad();
    this.NextIntegrityExtent.read(myRandomAccessFile);
    this.LogicalVolumeContensUse = new LogicalVolumeHeaderDescriptor();
    this.LogicalVolumeContensUse.read(myRandomAccessFile);
    this.NumberOfPartitions = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.LengthOfImplementationUse = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.FreeSpaceTable = new long[(int)this.NumberOfPartitions]; int i;
    for (i = 0; i < this.FreeSpaceTable.length; i++)
    {
      this.FreeSpaceTable[i] = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    }
    this.SizeTable = new long[(int)this.NumberOfPartitions];
    for (i = 0; i < this.FreeSpaceTable.length; i++)
    {
      this.SizeTable[i] = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    }
    this.ImplementationUse = new byte[(int)this.LengthOfImplementationUse];
    myRandomAccessFile.read(this.ImplementationUse);
  }
  public void write(RandomAccessFile myRandomAccessFile, int blockSize) throws IOException {
    byte[] rawBytes = getBytesWithoutDescriptorTag();
    this.DescriptorTag.DescriptorCRCLength = rawBytes.length;
    this.DescriptorTag.DescriptorCRC = Checksum.cksum(rawBytes);
    this.DescriptorTag.write(myRandomAccessFile);
    myRandomAccessFile.write(rawBytes);
    int bytesWritten = rawBytes.length + 16;
    byte[] emptyBytesInBlock = new byte[blockSize - bytesWritten];
    myRandomAccessFile.write(emptyBytesInBlock);
  }
  public byte[] getBytesWithoutDescriptorTag() {
    byte[] RecordingDateAndTimeBytes = this.RecordingDateAndTime.getBytes();
    byte[] NextIntegrityExtentBytes = this.NextIntegrityExtent.getBytes();
    byte[] LogicalVolumeContentsUseBytes = this.LogicalVolumeContensUse.getBytes();
    byte[] rawBytes = new byte[12 + 
        RecordingDateAndTimeBytes.length + 
        NextIntegrityExtentBytes.length + 
        LogicalVolumeContentsUseBytes.length + 
        this.FreeSpaceTable.length * 4 + 
        this.SizeTable.length * 4 + 
        this.ImplementationUse.length];
    int pos = 0;
    System.arraycopy(RecordingDateAndTimeBytes, 0, rawBytes, pos, RecordingDateAndTimeBytes.length);
    pos += RecordingDateAndTimeBytes.length;
    pos = BinaryTools.getUInt32BytesFromLong(this.IntegrityType, rawBytes, pos);
    System.arraycopy(NextIntegrityExtentBytes, 0, rawBytes, pos, NextIntegrityExtentBytes.length);
    pos += NextIntegrityExtentBytes.length;
    System.arraycopy(LogicalVolumeContentsUseBytes, 0, rawBytes, pos, LogicalVolumeContentsUseBytes.length);
    pos += LogicalVolumeContentsUseBytes.length;
    pos = BinaryTools.getUInt32BytesFromLong(this.NumberOfPartitions, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.LengthOfImplementationUse, rawBytes, pos);
    int i;
    for (i = 0; i < this.FreeSpaceTable.length; i++)
    {
      pos = BinaryTools.getUInt32BytesFromLong(this.FreeSpaceTable[i], rawBytes, pos);
    }
    for (i = 0; i < this.SizeTable.length; i++)
    {
      pos = BinaryTools.getUInt32BytesFromLong(this.SizeTable[i], rawBytes, pos);
    }
    System.arraycopy(this.ImplementationUse, 0, rawBytes, pos, this.ImplementationUse.length);
    pos += this.ImplementationUse.length;
    return rawBytes;
  }
  public byte[] getBytes(int blockSize) {
    byte[] bytesWithoutDescriptorTag = getBytesWithoutDescriptorTag();
    this.DescriptorTag.DescriptorCRCLength = bytesWithoutDescriptorTag.length;
    this.DescriptorTag.DescriptorCRC = Checksum.cksum(bytesWithoutDescriptorTag);
    byte[] descriptorTagBytes = this.DescriptorTag.getBytes();
    int paddedLength = descriptorTagBytes.length + bytesWithoutDescriptorTag.length;
    if (paddedLength % blockSize != 0)
    {
      paddedLength += blockSize - paddedLength % blockSize;
    }
    byte[] rawBytes = new byte[paddedLength];
    int pos = 0;
    System.arraycopy(descriptorTagBytes, 0, rawBytes, pos, descriptorTagBytes.length);
    pos += descriptorTagBytes.length;
    System.arraycopy(bytesWithoutDescriptorTag, 0, rawBytes, pos, bytesWithoutDescriptorTag.length);
    pos += bytesWithoutDescriptorTag.length;
    return rawBytes;
  }
  public void setImplementationUse(EntityID implementationID, long numberOfFiles, long numberOfDirectories, int minimumUDFReadRevision, int minimumUDFWriteRevision, int maximumUDFWriteRevision) {
    this.ImplementationUse = new byte[46];
    byte[] implementationIDBytes = implementationID.getBytes();
    System.arraycopy(implementationIDBytes, 0, this.ImplementationUse, 0, implementationIDBytes.length);
    int pos = implementationIDBytes.length;
    pos = BinaryTools.getUInt32BytesFromLong(numberOfFiles, this.ImplementationUse, pos);
    pos = BinaryTools.getUInt32BytesFromLong(numberOfDirectories, this.ImplementationUse, pos);
    pos = BinaryTools.getUInt16BytesFromInt(minimumUDFReadRevision, this.ImplementationUse, pos);
    pos = BinaryTools.getUInt16BytesFromInt(minimumUDFWriteRevision, this.ImplementationUse, pos);
    pos = BinaryTools.getUInt16BytesFromInt(maximumUDFWriteRevision, this.ImplementationUse, pos);
  }
}
