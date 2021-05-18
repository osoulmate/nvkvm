package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.Checksum;
import java.io.IOException;
import java.io.RandomAccessFile;
public abstract class VolumeDescriptorSequenceItem
{
  public Tag DescriptorTag;
  public long VolumeDescriptorSequenceNumber;
  public abstract byte[] getBytesWithoutDescriptorTag();
  public abstract void read(RandomAccessFile paramRandomAccessFile) throws IOException;
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
}
