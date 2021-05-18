package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.Checksum;
import java.io.IOException;
import java.io.RandomAccessFile;
public class FileEntry
{
  public Tag DescriptorTag;
  public IcbTag ICBTag;
  public long Uid;
  public long Gid;
  public long Permissions;
  public int FileLinkCount;
  public short RecordFormat;
  public short RecordDisplayAttributes;
  public long RecordLength;
  public long InformationLength;
  public long LogicalBlocksRecorded;
  public Timestamp AccessTime;
  public Timestamp ModificationTime;
  public Timestamp AttributeTime;
  public long Checkpoint;
  public Long_ad ExtendedAttributeICB;
  public EntityID ImplementationIdentifier;
  public long UniqueID;
  public long LengthofExtendedAttributes;
  public long LengthofAllocationDescriptors;
  public byte[] ExtendedAttributes;
  public byte[] AllocationDescriptors;
  public static int fixedPartLength = 176;
  public FileEntry() {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.TagIdentifier = 261;
    this.ICBTag = new IcbTag();
    this.AccessTime = new Timestamp();
    this.ModificationTime = new Timestamp();
    this.AttributeTime = new Timestamp();
    this.ExtendedAttributeICB = new Long_ad();
    this.ImplementationIdentifier = new EntityID();
    this.ExtendedAttributes = new byte[0];
    this.AllocationDescriptors = new byte[0];
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
    this.LogicalBlocksRecorded = BinaryTools.readUInt64AsLong(myRandomAccessFile);
    this.AccessTime = new Timestamp();
    this.AccessTime.read(myRandomAccessFile);
    this.ModificationTime = new Timestamp();
    this.ModificationTime.read(myRandomAccessFile);
    this.AttributeTime = new Timestamp();
    this.AttributeTime.read(myRandomAccessFile);
    this.Checkpoint = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.ExtendedAttributeICB = new Long_ad();
    this.ExtendedAttributeICB.read(myRandomAccessFile);
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
    byte[] ICBTagBytes = this.ICBTag.getBytes();
    byte[] AccessTimeBytes = this.AccessTime.getBytes();
    byte[] ModificationTimeBytes = this.ModificationTime.getBytes();
    byte[] AttributeTimeBytes = this.AttributeTime.getBytes();
    byte[] ExtendedAttributeICBBytes = this.ExtendedAttributeICB.getBytes();
    byte[] ImplementationIdentifierBytes = this.ImplementationIdentifier.getBytes();
    byte[] rawBytes = new byte[56 + 
        ICBTagBytes.length + 
        AccessTimeBytes.length + 
        ModificationTimeBytes.length + 
        AttributeTimeBytes.length + 
        ExtendedAttributeICBBytes.length + 
        ImplementationIdentifierBytes.length + 
        this.ExtendedAttributes.length + 
        this.AllocationDescriptors.length];
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
    pos = BinaryTools.getUInt64BytesFromLong(this.LogicalBlocksRecorded, rawBytes, pos);
    System.arraycopy(AccessTimeBytes, 0, rawBytes, pos, AccessTimeBytes.length);
    pos += AccessTimeBytes.length;
    System.arraycopy(ModificationTimeBytes, 0, rawBytes, pos, ModificationTimeBytes.length);
    pos += ModificationTimeBytes.length;
    System.arraycopy(AttributeTimeBytes, 0, rawBytes, pos, AttributeTimeBytes.length);
    pos += AttributeTimeBytes.length;
    pos = BinaryTools.getUInt32BytesFromLong(this.Checkpoint, rawBytes, pos);
    System.arraycopy(ExtendedAttributeICBBytes, 0, rawBytes, pos, ExtendedAttributeICBBytes.length);
    pos += ExtendedAttributeICBBytes.length;
    System.arraycopy(ImplementationIdentifierBytes, 0, rawBytes, pos, ImplementationIdentifierBytes.length);
    pos += ImplementationIdentifierBytes.length;
    pos = BinaryTools.getUInt64BytesFromLong(this.UniqueID, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.LengthofExtendedAttributes, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.LengthofAllocationDescriptors, rawBytes, pos);
    System.arraycopy(this.ExtendedAttributes, 0, rawBytes, pos, this.ExtendedAttributes.length);
    pos += this.ExtendedAttributes.length;
    System.arraycopy(this.AllocationDescriptors, 0, rawBytes, pos, this.AllocationDescriptors.length);
    pos += this.AllocationDescriptors.length;
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
