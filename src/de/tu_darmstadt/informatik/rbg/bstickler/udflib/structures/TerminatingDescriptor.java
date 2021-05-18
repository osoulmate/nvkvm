package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.Checksum;
import java.io.IOException;
import java.io.RandomAccessFile;
public class TerminatingDescriptor
{
  public Tag DescriptorTag;
  public byte[] Reserved;
  public TerminatingDescriptor() {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.TagIdentifier = 8;
    this.Reserved = new byte[496];
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.read(myRandomAccessFile);
    this.Reserved = new byte[496];
    myRandomAccessFile.read(this.Reserved);
  }
  public void write(RandomAccessFile myRandomAccessFile, int blockSize) throws IOException {
    this.DescriptorTag.DescriptorCRCLength = this.Reserved.length;
    this.DescriptorTag.DescriptorCRC = Checksum.cksum(this.Reserved);
    this.DescriptorTag.write(myRandomAccessFile);
    myRandomAccessFile.write(this.Reserved);
    int bytesWritten = this.Reserved.length + 16;
    byte[] emptyBytesInBlock = new byte[blockSize - bytesWritten];
    myRandomAccessFile.write(emptyBytesInBlock);
  }
  public byte[] getBytes(int blockSize) {
    this.DescriptorTag.DescriptorCRCLength = this.Reserved.length;
    this.DescriptorTag.DescriptorCRC = Checksum.cksum(this.Reserved);
    byte[] descriptorTagBytes = this.DescriptorTag.getBytes();
    int paddedLength = descriptorTagBytes.length + this.Reserved.length;
    if (paddedLength % blockSize != 0)
    {
      paddedLength += blockSize - paddedLength % blockSize;
    }
    byte[] rawBytes = new byte[paddedLength];
    int pos = 0;
    System.arraycopy(descriptorTagBytes, 0, rawBytes, pos, descriptorTagBytes.length);
    pos += descriptorTagBytes.length;
    System.arraycopy(this.Reserved, 0, rawBytes, pos, this.Reserved.length);
    pos += this.Reserved.length;
    return rawBytes;
  }
  public byte[] getBytesWithoutDescriptorTag() {
    return this.Reserved;
  }
}
