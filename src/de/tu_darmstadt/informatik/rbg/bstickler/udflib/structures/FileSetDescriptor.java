package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.Checksum;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.OSTAUnicode;
import java.io.IOException;
import java.io.RandomAccessFile;
public class FileSetDescriptor
{
  public Tag DescriptorTag;
  public Timestamp RecordingDateandTime;
  public int InterchangeLevel;
  public int MaximumInterchangeLevel;
  public long CharacterSetList;
  public long MaximumCharacterSetList;
  public long FileSetNumber;
  public long FileSetDescriptorNumber;
  public CharSpec LogicalVolumeIdentifierCharacterSet;
  public byte[] LogicalVolumeIdentifier;
  public CharSpec FileSetCharacterSet;
  public byte[] FileSetIdentifier;
  public byte[] CopyrightFileIdentifier;
  public byte[] AbstractFileIdentifier;
  public Long_ad RootDirectoryICB;
  public EntityID DomainIdentifier;
  public Long_ad NextExtent;
  public byte[] Reserved;
  public FileSetDescriptor() {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.TagIdentifier = 256;
    this.RecordingDateandTime = new Timestamp();
    this.LogicalVolumeIdentifierCharacterSet = new CharSpec();
    this.LogicalVolumeIdentifier = new byte[128];
    this.FileSetCharacterSet = new CharSpec();
    this.FileSetIdentifier = new byte[32];
    this.CopyrightFileIdentifier = new byte[32];
    this.AbstractFileIdentifier = new byte[32];
    this.RootDirectoryICB = new Long_ad();
    this.DomainIdentifier = new EntityID();
    this.NextExtent = new Long_ad();
    this.Reserved = new byte[48];
  }
  public void setLogicalVolumeIdentifier(String logicalVolumeIdentifier) {
    this.LogicalVolumeIdentifier = new byte[128];
    try {
      byte[] logicalVolumeIdentifierBytes = logicalVolumeIdentifier.getBytes("UTF-16");
      int compId = OSTAUnicode.getBestCompressionId(logicalVolumeIdentifierBytes);
      byte[] tmpIdentifier = OSTAUnicode.CompressUnicodeByte(logicalVolumeIdentifierBytes, compId);
      int length = (tmpIdentifier.length < 127) ? tmpIdentifier.length : 127;
      System.arraycopy(tmpIdentifier, 0, this.LogicalVolumeIdentifier, 0, length);
      this.LogicalVolumeIdentifier[this.LogicalVolumeIdentifier.length - 1] = (byte)length;
    }
    catch (Exception exception) {}
  }
  public void setFileSetIdentifier(String fileSetIdentifier) {
    this.FileSetIdentifier = new byte[32];
    try {
      byte[] fileSetIdentifierBytes = fileSetIdentifier.getBytes("UTF-16");
      int compId = OSTAUnicode.getBestCompressionId(fileSetIdentifierBytes);
      byte[] tmpIdentifier = OSTAUnicode.CompressUnicodeByte(fileSetIdentifierBytes, compId);
      int length = (tmpIdentifier.length < 31) ? tmpIdentifier.length : 31;
      System.arraycopy(tmpIdentifier, 0, this.FileSetIdentifier, 0, length);
      this.FileSetIdentifier[this.FileSetIdentifier.length - 1] = (byte)length;
    }
    catch (Exception exception) {}
  }
  public void setAbstractFileIdentifier(String abstractFileIdentifier) {
    this.AbstractFileIdentifier = new byte[32];
    try {
      byte[] abstractFileIdentifierBytes = abstractFileIdentifier.getBytes("UTF-16");
      int compId = OSTAUnicode.getBestCompressionId(abstractFileIdentifierBytes);
      byte[] tmpIdentifier = OSTAUnicode.CompressUnicodeByte(abstractFileIdentifierBytes, compId);
      int length = (tmpIdentifier.length < 31) ? tmpIdentifier.length : 31;
      System.arraycopy(tmpIdentifier, 0, this.AbstractFileIdentifier, 0, length);
      this.AbstractFileIdentifier[this.AbstractFileIdentifier.length - 1] = (byte)length;
    }
    catch (Exception exception) {}
  }
  public void setCopyrightFileIdentifier(String copyrightFileIdentifier) {
    this.CopyrightFileIdentifier = new byte[32];
    try {
      byte[] copyrightFileIdentifierBytes = copyrightFileIdentifier.getBytes("UTF-16");
      int compId = OSTAUnicode.getBestCompressionId(copyrightFileIdentifierBytes);
      byte[] tmpIdentifier = OSTAUnicode.CompressUnicodeByte(copyrightFileIdentifierBytes, compId);
      int length = (tmpIdentifier.length < 31) ? tmpIdentifier.length : 31;
      System.arraycopy(tmpIdentifier, 0, this.CopyrightFileIdentifier, 0, length);
      this.CopyrightFileIdentifier[this.CopyrightFileIdentifier.length - 1] = (byte)length;
    }
    catch (Exception exception) {}
  }
  public void Load(RandomAccessFile myRandomAccessFile) throws IOException {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.read(myRandomAccessFile);
    this.RecordingDateandTime = new Timestamp();
    this.RecordingDateandTime.read(myRandomAccessFile);
    this.InterchangeLevel = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.MaximumInterchangeLevel = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.CharacterSetList = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.MaximumCharacterSetList = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.FileSetNumber = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.FileSetDescriptorNumber = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.LogicalVolumeIdentifierCharacterSet = new CharSpec();
    this.LogicalVolumeIdentifierCharacterSet.read(myRandomAccessFile);
    this.LogicalVolumeIdentifier = new byte[128];
    myRandomAccessFile.read(this.LogicalVolumeIdentifier);
    this.FileSetCharacterSet = new CharSpec();
    this.FileSetCharacterSet.read(myRandomAccessFile);
    this.FileSetIdentifier = new byte[32];
    myRandomAccessFile.read(this.FileSetIdentifier);
    this.CopyrightFileIdentifier = new byte[32];
    myRandomAccessFile.read(this.CopyrightFileIdentifier);
    this.AbstractFileIdentifier = new byte[32];
    myRandomAccessFile.read(this.AbstractFileIdentifier);
    this.RootDirectoryICB = new Long_ad();
    this.RootDirectoryICB.read(myRandomAccessFile);
    this.DomainIdentifier = new EntityID();
    this.DomainIdentifier.read(myRandomAccessFile);
    this.NextExtent = new Long_ad();
    this.NextExtent.read(myRandomAccessFile);
    this.Reserved = new byte[48];
    myRandomAccessFile.read(this.Reserved);
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
    byte[] RecordingDateandTimeBytes = this.RecordingDateandTime.getBytes();
    byte[] LogicalVolumeIdentifierCharacterSetBytes = this.LogicalVolumeIdentifierCharacterSet.getBytes();
    byte[] FileSetCharacterSetBytes = this.FileSetCharacterSet.getBytes();
    byte[] RootDirectoryICBBytes = this.RootDirectoryICB.getBytes();
    byte[] DomainIdentifierBytes = this.DomainIdentifier.getBytes();
    byte[] NextExtentBytes = this.NextExtent.getBytes();
    byte[] rawBytes = new byte[292 + 
        RecordingDateandTimeBytes.length + 
        LogicalVolumeIdentifierCharacterSetBytes.length + 
        FileSetCharacterSetBytes.length + 
        RootDirectoryICBBytes.length + 
        DomainIdentifierBytes.length + 
        NextExtentBytes.length];
    int pos = 0;
    System.arraycopy(RecordingDateandTimeBytes, 0, rawBytes, pos, RecordingDateandTimeBytes.length);
    pos += RecordingDateandTimeBytes.length;
    pos = BinaryTools.getUInt16BytesFromInt(this.InterchangeLevel, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.MaximumInterchangeLevel, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.CharacterSetList, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.MaximumCharacterSetList, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.FileSetNumber, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.FileSetDescriptorNumber, rawBytes, pos);
    System.arraycopy(LogicalVolumeIdentifierCharacterSetBytes, 0, rawBytes, pos, LogicalVolumeIdentifierCharacterSetBytes.length);
    pos += LogicalVolumeIdentifierCharacterSetBytes.length;
    System.arraycopy(this.LogicalVolumeIdentifier, 0, rawBytes, pos, this.LogicalVolumeIdentifier.length);
    pos += this.LogicalVolumeIdentifier.length;
    System.arraycopy(FileSetCharacterSetBytes, 0, rawBytes, pos, FileSetCharacterSetBytes.length);
    pos += FileSetCharacterSetBytes.length;
    System.arraycopy(this.FileSetIdentifier, 0, rawBytes, pos, this.FileSetIdentifier.length);
    pos += this.FileSetIdentifier.length;
    System.arraycopy(this.CopyrightFileIdentifier, 0, rawBytes, pos, this.CopyrightFileIdentifier.length);
    pos += this.CopyrightFileIdentifier.length;
    System.arraycopy(this.AbstractFileIdentifier, 0, rawBytes, pos, this.AbstractFileIdentifier.length);
    pos += this.AbstractFileIdentifier.length;
    System.arraycopy(RootDirectoryICBBytes, 0, rawBytes, pos, RootDirectoryICBBytes.length);
    pos += RootDirectoryICBBytes.length;
    System.arraycopy(DomainIdentifierBytes, 0, rawBytes, pos, DomainIdentifierBytes.length);
    pos += DomainIdentifierBytes.length;
    System.arraycopy(NextExtentBytes, 0, rawBytes, pos, NextExtentBytes.length);
    pos += NextExtentBytes.length;
    System.arraycopy(this.Reserved, 0, rawBytes, pos, this.Reserved.length);
    pos += this.Reserved.length;
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
}
