package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.Checksum;
import java.io.IOException;
import java.io.RandomAccessFile;
public class SpaceBitmapDescriptor
{
  public Tag DescriptorTag;
  public long NumberOfBits;
  public long NumberOfBytes;
  public byte[] Bitmap;
  public SpaceBitmapDescriptor() {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.DescriptorVersion = 3;
    this.DescriptorTag.TagIdentifier = 264;
    this.Bitmap = new byte[0];
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.read(myRandomAccessFile);
    this.NumberOfBits = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.NumberOfBytes = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.Bitmap = new byte[(int)this.NumberOfBytes];
    myRandomAccessFile.read(this.Bitmap);
  }
  public void write(RandomAccessFile myRandomAccessFile, int blockSize) throws IOException {
    byte[] rawBytes = getBytesWithoutDescriptorTag();
    this.DescriptorTag.DescriptorCRCLength = 8;
    this.DescriptorTag.DescriptorCRC = Checksum.cksum(getFirst8Bytes());
    this.DescriptorTag.write(myRandomAccessFile);
    myRandomAccessFile.write(rawBytes);
    int bytesWritten = rawBytes.length + 16;
    byte[] emptyBytesInBlock = new byte[blockSize - bytesWritten % blockSize];
    myRandomAccessFile.write(emptyBytesInBlock);
  }
  public byte[] getFirst8Bytes() {
    byte[] rawBytes = new byte[8];
    int pos = 0;
    pos = BinaryTools.getUInt32BytesFromLong(this.NumberOfBits, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.NumberOfBytes, rawBytes, pos);
    return rawBytes;
  }
  public byte[] getBytesWithoutDescriptorTag() {
    byte[] rawBytes = new byte[8 + this.Bitmap.length];
    int pos = 0;
    pos = BinaryTools.getUInt32BytesFromLong(this.NumberOfBits, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.NumberOfBytes, rawBytes, pos);
    System.arraycopy(this.Bitmap, 0, rawBytes, pos, this.Bitmap.length);
    pos += this.Bitmap.length;
    return rawBytes;
  }
  public long getFullBlockLength(int blockSize) {
    long length = (24 + this.Bitmap.length);
    if (length % blockSize != 0L)
    {
      length += blockSize - length % blockSize;
    }
    return length;
  }
}
