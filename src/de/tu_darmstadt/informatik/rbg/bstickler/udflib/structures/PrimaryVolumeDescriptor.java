package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.OSTAUnicode;
import java.io.IOException;
import java.io.RandomAccessFile;
public class PrimaryVolumeDescriptor
  extends VolumeDescriptorSequenceItem
{
  public long PrimaryVolumeDescriptorNumber;
  public byte[] VolumeIdentifier;
  public int VolumeSequenceNumber;
  public int MaximumVolumeSequenceNumber;
  public int InterchangeLevel;
  public int MaximumInterchangeLevel;
  public long CharacterSetList;
  public long MaximumCharacterSetList;
  public byte[] VolumeSetIdentifier;
  public CharSpec DescriptorCharacterSet;
  public CharSpec ExplanatoryCharacterSet;
  public Extend_ad VolumeAbstract;
  public Extend_ad VolumeCopyrightNotice;
  public EntityID ApplicationIdentifier;
  public Timestamp RecordingDateandTime;
  public EntityID ImplementationIdentifier;
  public byte[] ImplementationUse;
  public long PredecessorVolumeDescriptorSequenceLocation;
  public int Flags;
  public byte[] Reserved;
  public PrimaryVolumeDescriptor() {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.TagIdentifier = 1;
    this.VolumeIdentifier = new byte[32];
    this.VolumeSetIdentifier = new byte[128];
    this.DescriptorCharacterSet = new CharSpec();
    this.ExplanatoryCharacterSet = new CharSpec();
    this.VolumeAbstract = new Extend_ad();
    this.VolumeCopyrightNotice = new Extend_ad();
    this.ApplicationIdentifier = new EntityID();
    this.RecordingDateandTime = new Timestamp();
    this.ImplementationIdentifier = new EntityID();
    this.ImplementationUse = new byte[64];
    this.Reserved = new byte[22];
  }
  public void setVolumeIdentifier(String volumeIdentifier) throws Exception {
    if (volumeIdentifier.length() > 30)
    {
      throw new Exception("error: volume identifier length > 30 characters");
    }
    this.VolumeIdentifier = new byte[32];
    try {
      byte[] volumeIdentifierBytes = volumeIdentifier.getBytes("UTF-16");
      int compId = OSTAUnicode.getBestCompressionId(volumeIdentifierBytes);
      byte[] tmpIdentifier = OSTAUnicode.CompressUnicodeByte(volumeIdentifierBytes, compId);
      int length = (tmpIdentifier.length < 31) ? tmpIdentifier.length : 31;
      System.arraycopy(tmpIdentifier, 0, this.VolumeIdentifier, 0, length);
      this.VolumeIdentifier[this.VolumeIdentifier.length - 1] = (byte)length;
    }
    catch (Exception exception) {}
  }
  public void setVolumeSetIdentifier(String volumeSetIdentifier) throws Exception {
    if (volumeSetIdentifier.length() > 126)
    {
      throw new Exception("error: volume set identifier length > 126 characters");
    }
    this.VolumeSetIdentifier = new byte[128];
    try {
      byte[] volumeSetIdentifierBytes = volumeSetIdentifier.getBytes("UTF-16");
      int compId = OSTAUnicode.getBestCompressionId(volumeSetIdentifierBytes);
      byte[] tmpIdentifier = OSTAUnicode.CompressUnicodeByte(volumeSetIdentifierBytes, compId);
      int length = (tmpIdentifier.length < 127) ? tmpIdentifier.length : 127;
      System.arraycopy(tmpIdentifier, 0, this.VolumeSetIdentifier, 0, length);
      this.VolumeSetIdentifier[this.VolumeSetIdentifier.length - 1] = (byte)length;
    }
    catch (Exception exception) {}
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.read(myRandomAccessFile);
    this.VolumeDescriptorSequenceNumber = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.PrimaryVolumeDescriptorNumber = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.VolumeIdentifier = new byte[32];
    myRandomAccessFile.read(this.VolumeIdentifier);
    this.VolumeSequenceNumber = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.MaximumVolumeSequenceNumber = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.InterchangeLevel = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.MaximumInterchangeLevel = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.CharacterSetList = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.MaximumCharacterSetList = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.VolumeSetIdentifier = new byte[128];
    myRandomAccessFile.read(this.VolumeSetIdentifier);
    this.DescriptorCharacterSet = new CharSpec();
    this.DescriptorCharacterSet.read(myRandomAccessFile);
    this.ExplanatoryCharacterSet = new CharSpec();
    this.ExplanatoryCharacterSet.read(myRandomAccessFile);
    this.VolumeAbstract = new Extend_ad();
    this.VolumeAbstract.read(myRandomAccessFile);
    this.VolumeCopyrightNotice = new Extend_ad();
    this.VolumeCopyrightNotice.read(myRandomAccessFile);
    this.ApplicationIdentifier = new EntityID();
    this.ApplicationIdentifier.read(myRandomAccessFile);
    this.RecordingDateandTime = new Timestamp();
    this.RecordingDateandTime.read(myRandomAccessFile);
    this.ImplementationIdentifier = new EntityID();
    this.ImplementationIdentifier.read(myRandomAccessFile);
    this.ImplementationUse = new byte[64];
    myRandomAccessFile.read(this.ImplementationUse);
    this.PredecessorVolumeDescriptorSequenceLocation = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.Flags = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.Reserved = new byte[22];
    myRandomAccessFile.read(this.Reserved);
  }
  public byte[] getBytesWithoutDescriptorTag() {
    byte[] DescriptorCharacterSetBytes = this.DescriptorCharacterSet.getBytes();
    byte[] ExplanatoryCharacterSetBytes = this.ExplanatoryCharacterSet.getBytes();
    byte[] VolumeAbstractBytes = this.VolumeAbstract.getBytes();
    byte[] VolumeCopyrightNoticeBytes = this.VolumeCopyrightNotice.getBytes();
    byte[] ApplicationIdentifierBytes = this.ApplicationIdentifier.getBytes();
    byte[] RecordingDateandTimeBytes = this.RecordingDateandTime.getBytes();
    byte[] ImplementationIdentifierBytes = this.ImplementationIdentifier.getBytes();
    byte[] rawBytes = new byte[276 + 
        DescriptorCharacterSetBytes.length + 
        ExplanatoryCharacterSetBytes.length + 
        VolumeAbstractBytes.length + 
        VolumeCopyrightNoticeBytes.length + 
        ApplicationIdentifierBytes.length + 
        RecordingDateandTimeBytes.length + 
        ImplementationIdentifierBytes.length];
    int pos = 0;
    pos = BinaryTools.getUInt32BytesFromLong(this.VolumeDescriptorSequenceNumber, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.PrimaryVolumeDescriptorNumber, rawBytes, pos);
    System.arraycopy(this.VolumeIdentifier, 0, rawBytes, pos, this.VolumeIdentifier.length);
    pos += this.VolumeIdentifier.length;
    pos = BinaryTools.getUInt16BytesFromInt(this.VolumeSequenceNumber, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.MaximumVolumeSequenceNumber, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.InterchangeLevel, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.MaximumInterchangeLevel, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.CharacterSetList, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.MaximumCharacterSetList, rawBytes, pos);
    System.arraycopy(this.VolumeSetIdentifier, 0, rawBytes, pos, this.VolumeSetIdentifier.length);
    pos += this.VolumeSetIdentifier.length;
    System.arraycopy(DescriptorCharacterSetBytes, 0, rawBytes, pos, DescriptorCharacterSetBytes.length);
    pos += DescriptorCharacterSetBytes.length;
    System.arraycopy(ExplanatoryCharacterSetBytes, 0, rawBytes, pos, ExplanatoryCharacterSetBytes.length);
    pos += ExplanatoryCharacterSetBytes.length;
    System.arraycopy(VolumeAbstractBytes, 0, rawBytes, pos, VolumeAbstractBytes.length);
    pos += VolumeAbstractBytes.length;
    System.arraycopy(VolumeCopyrightNoticeBytes, 0, rawBytes, pos, VolumeCopyrightNoticeBytes.length);
    pos += VolumeCopyrightNoticeBytes.length;
    System.arraycopy(ApplicationIdentifierBytes, 0, rawBytes, pos, ApplicationIdentifierBytes.length);
    pos += ApplicationIdentifierBytes.length;
    System.arraycopy(RecordingDateandTimeBytes, 0, rawBytes, pos, RecordingDateandTimeBytes.length);
    pos += RecordingDateandTimeBytes.length;
    System.arraycopy(ImplementationIdentifierBytes, 0, rawBytes, pos, ImplementationIdentifierBytes.length);
    pos += ImplementationIdentifierBytes.length;
    System.arraycopy(this.ImplementationUse, 0, rawBytes, pos, this.ImplementationUse.length);
    pos += this.ImplementationUse.length;
    pos = BinaryTools.getUInt32BytesFromLong(this.PredecessorVolumeDescriptorSequenceLocation, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.Flags, rawBytes, pos);
    System.arraycopy(this.Reserved, 0, rawBytes, pos, this.Reserved.length);
    pos += this.Reserved.length;
    return rawBytes;
  }
}
