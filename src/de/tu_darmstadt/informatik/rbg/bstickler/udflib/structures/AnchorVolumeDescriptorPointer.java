package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.Checksum;
import java.io.IOException;
import java.io.RandomAccessFile;
public class AnchorVolumeDescriptorPointer
{
  public Tag DescriptorTag;
  public Extend_ad MainVolumeDescriptorSequenceExtend;
  public Extend_ad ReserveVolumeDescriptorSequenceExtend;
  public byte[] reserved;
  public AnchorVolumeDescriptorPointer() {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.TagIdentifier = 2;
    this.MainVolumeDescriptorSequenceExtend = new Extend_ad();
    this.ReserveVolumeDescriptorSequenceExtend = new Extend_ad();
    this.reserved = new byte[480];
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.read(myRandomAccessFile);
    this.MainVolumeDescriptorSequenceExtend = new Extend_ad();
    this.MainVolumeDescriptorSequenceExtend.read(myRandomAccessFile);
    this.ReserveVolumeDescriptorSequenceExtend = new Extend_ad();
    this.ReserveVolumeDescriptorSequenceExtend.read(myRandomAccessFile);
    this.reserved = new byte[480];
    myRandomAccessFile.read(this.reserved);
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
  public byte[] getBytesWithoutDescriptorTag() {
    byte[] MainVolumeDescriptorSequenceExtendBytes = this.MainVolumeDescriptorSequenceExtend.getBytes();
    byte[] ReserveVolumeDescriptorSequenceExtendBytes = this.ReserveVolumeDescriptorSequenceExtend.getBytes();
    byte[] rawBytes = new byte[MainVolumeDescriptorSequenceExtendBytes.length + 
        ReserveVolumeDescriptorSequenceExtendBytes.length + 
        this.reserved.length];
    System.arraycopy(MainVolumeDescriptorSequenceExtendBytes, 0, rawBytes, 0, MainVolumeDescriptorSequenceExtendBytes.length);
    System.arraycopy(ReserveVolumeDescriptorSequenceExtendBytes, 0, rawBytes, MainVolumeDescriptorSequenceExtendBytes.length, ReserveVolumeDescriptorSequenceExtendBytes.length);
    System.arraycopy(this.reserved, 0, rawBytes, MainVolumeDescriptorSequenceExtendBytes.length + ReserveVolumeDescriptorSequenceExtendBytes.length, this.reserved.length);
    return rawBytes;
  }
}
