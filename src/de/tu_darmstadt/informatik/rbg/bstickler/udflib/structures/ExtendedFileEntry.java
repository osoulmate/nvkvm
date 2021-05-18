package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
public class ExtendedFileEntry
  extends FileEntry
{
  public long ObjectSize;
  public Timestamp CreationTime;
  byte[] Reserved;
  public Long_ad StreamDirectoryICB;
  public static int fixedPartLength = 224;
  public ExtendedFileEntry() {
    this.DescriptorTag.TagIdentifier = 266;
    this.CreationTime = new Timestamp();
    this.Reserved = new byte[4];
    this.StreamDirectoryICB = new Long_ad();
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.read(myRandomAccessFile);
    this.ICBTag = new IcbTag();
    this.ICBTag.read(myRandomAccessFile);
    this.Uid = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.Gid = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.Permissions = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.FileLinkCount = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.RecordFormat = (short)myRandomAccessFile.readUnsignedByte();
    this.RecordDisplayAttributes = (short)myRandomAccessFile.readUnsignedByte();
    this.RecordLength = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.InformationLength = BinaryTools.readUInt64AsLong(myRandomAccessFile);
    this.ObjectSize = BinaryTools.readUInt64AsLong(myRandomAccessFile);
    this.LogicalBlocksRecorded = BinaryTools.readUInt64AsLong(myRandomAccessFile);
    this.AccessTime = new Timestamp();
    this.AccessTime.read(myRandomAccessFile);
    this.ModificationTime = new Timestamp();
    this.ModificationTime.read(myRandomAccessFile);
    this.CreationTime = new Timestamp();
    this.CreationTime.read(myRandomAccessFile);
    this.AttributeTime = new Timestamp();
    this.AttributeTime.read(myRandomAccessFile);
    this.Checkpoint = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.Reserved = new byte[4];
    myRandomAccessFile.read(this.Reserved);
    this.ExtendedAttributeICB = new Long_ad();
    this.ExtendedAttributeICB.read(myRandomAccessFile);
    this.StreamDirectoryICB = new Long_ad();
    this.StreamDirectoryICB.read(myRandomAccessFile);
    this.ImplementationIdentifier = new EntityID();
    this.ImplementationIdentifier.read(myRandomAccessFile);
    this.UniqueID = BinaryTools.readUInt64AsLong(myRandomAccessFile);
    this.LengthofExtendedAttributes = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.LengthofAllocationDescriptors = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.ExtendedAttributes = new byte[(int)this.LengthofExtendedAttributes];
    myRandomAccessFile.read(this.ExtendedAttributes);
    this.AllocationDescriptors = new byte[(int)this.LengthofAllocationDescriptors];
    myRandomAccessFile.read(this.AllocationDescriptors);
  }
  public byte[] getBytesWithoutDescriptorTag() {
    byte[] ICBTagBytes = this.ICBTag.getBytes();
    byte[] AccessTimeBytes = this.AccessTime.getBytes();
    byte[] ModificationTimeBytes = this.ModificationTime.getBytes();
    byte[] AttributeTimeBytes = this.AttributeTime.getBytes();
    byte[] ExtendedAttributeICBBytes = this.ExtendedAttributeICB.getBytes();
    byte[] ImplementationIdentifierBytes = this.ImplementationIdentifier.getBytes();
    byte[] CreationTimeBytes = this.CreationTime.getBytes();
    byte[] StreamDirectoryICBBytes = this.StreamDirectoryICB.getBytes();
    byte[] rawBytes = new byte[68 + 
        ICBTagBytes.length + 
        AccessTimeBytes.length + 
        ModificationTimeBytes.length + 
        AttributeTimeBytes.length + 
        ExtendedAttributeICBBytes.length + 
        ImplementationIdentifierBytes.length + 
        this.ExtendedAttributes.length + 
        this.AllocationDescriptors.length + 
        CreationTimeBytes.length + 
        StreamDirectoryICBBytes.length];
    int pos = 0;
    System.arraycopy(ICBTagBytes, 0, rawBytes, pos, ICBTagBytes.length);
    pos += ICBTagBytes.length;
    pos = BinaryTools.getUInt32BytesFromLong(this.Uid, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.Gid, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.Permissions, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.FileLinkCount, rawBytes, pos);
    rawBytes[pos++] = (byte)(this.RecordFormat & 0xFF);
    rawBytes[pos++] = (byte)(this.RecordDisplayAttributes & 0xFF);
    pos = BinaryTools.getUInt32BytesFromLong(this.RecordLength, rawBytes, pos);
    pos = BinaryTools.getUInt64BytesFromLong(this.InformationLength, rawBytes, pos);
    pos = BinaryTools.getUInt64BytesFromLong(this.ObjectSize, rawBytes, pos);
    pos = BinaryTools.getUInt64BytesFromLong(this.LogicalBlocksRecorded, rawBytes, pos);
    System.arraycopy(AccessTimeBytes, 0, rawBytes, pos, AccessTimeBytes.length);
    pos += AccessTimeBytes.length;
    System.arraycopy(ModificationTimeBytes, 0, rawBytes, pos, ModificationTimeBytes.length);
    pos += ModificationTimeBytes.length;
    System.arraycopy(CreationTimeBytes, 0, rawBytes, pos, CreationTimeBytes.length);
    pos += CreationTimeBytes.length;
    System.arraycopy(AttributeTimeBytes, 0, rawBytes, pos, AttributeTimeBytes.length);
    pos += AttributeTimeBytes.length;
    pos = BinaryTools.getUInt32BytesFromLong(this.Checkpoint, rawBytes, pos);
    System.arraycopy(this.Reserved, 0, rawBytes, pos, this.Reserved.length);
    pos += this.Reserved.length;
    System.arraycopy(ExtendedAttributeICBBytes, 0, rawBytes, pos, ExtendedAttributeICBBytes.length);
    pos += ExtendedAttributeICBBytes.length;
    System.arraycopy(StreamDirectoryICBBytes, 0, rawBytes, pos, StreamDirectoryICBBytes.length);
    pos += StreamDirectoryICBBytes.length;
    System.arraycopy(ImplementationIdentifierBytes, 0, rawBytes, pos, ImplementationIdentifierBytes.length);
    pos += ImplementationIdentifierBytes.length;
    pos = BinaryTools.getUInt64BytesFromLong(this.UniqueID, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.LengthofExtendedAttributes, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.LengthofAllocationDescriptors, rawBytes, pos);
    System.arraycopy(this.ExtendedAttributes, 0, rawBytes, pos, this.ExtendedAttributes.length);
    pos += this.ExtendedAttributes.length;
    pos += this.AllocationDescriptors.length;
    return rawBytes;
  }
}
