package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
public class Tag
{
  public int TagIdentifier;
  public int DescriptorVersion;
  public short TagChecksum;
  public byte Reserved;
  public int TagSerialNumber;
  public int DescriptorCRC;
  public int DescriptorCRCLength;
  public long TagLocation;
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.TagIdentifier = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.DescriptorVersion = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.TagChecksum = (short)myRandomAccessFile.readUnsignedByte();
    this.Reserved = myRandomAccessFile.readByte();
    this.TagSerialNumber = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.DescriptorCRC = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.DescriptorCRCLength = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.TagLocation = BinaryTools.readUInt32AsLong(myRandomAccessFile);
  }
  public int read(byte[] rawData, int startPosition) {
    int position = startPosition;
    this.TagIdentifier = (rawData[position++] & 0xFF) + (rawData[position++] & 0xFF) * 256;
    this.DescriptorVersion = (rawData[position++] & 0xFF) + (rawData[position++] & 0xFF) * 256;
    this.TagChecksum = (short)(rawData[position++] & 0xFF);
    this.Reserved = rawData[position++];
    this.TagSerialNumber = (rawData[position++] & 0xFF) + (rawData[position++] & 0xFF) * 256;
    this.DescriptorCRC = (rawData[position++] & 0xFF) + (rawData[position++] & 0xFF) * 256;
    this.DescriptorCRCLength = (rawData[position++] & 0xFF) + (rawData[position++] & 0xFF) * 256;
    this.TagLocation = ((rawData[position++] & 0xFF) + (rawData[position++] & 0xFF) * 256 + (rawData[position++] & 0xFF) * 256 * 256 + (rawData[position++] & 0xFF) * 256 * 256 * 256);
    return position;
  }
  public void write(RandomAccessFile myRandomAccessFile) throws IOException {
    myRandomAccessFile.write(getBytes());
  }
  public byte[] getBytes() {
    this.TagChecksum = calculateChecksum();
    byte[] rawBytes = new byte[16];
    int pos = 0;
    pos = BinaryTools.getUInt16BytesFromInt(this.TagIdentifier, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.DescriptorVersion, rawBytes, pos);
    rawBytes[pos++] = (byte)(this.TagChecksum & 0xFF);
    rawBytes[pos++] = this.Reserved;
    pos = BinaryTools.getUInt16BytesFromInt(this.TagSerialNumber, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.DescriptorCRC, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.DescriptorCRCLength, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.TagLocation, rawBytes, pos);
    return rawBytes;
  }
  public short calculateChecksum() {
    short checksum = 0;
    checksum = (short)(checksum + (this.TagIdentifier & 0xFF));
    checksum = (short)(checksum + (this.TagIdentifier >> 8 & 0xFF));
    checksum = (short)(checksum + (this.DescriptorVersion & 0xFF));
    checksum = (short)(checksum + (this.DescriptorVersion >> 8 & 0xFF));
    checksum = (short)(checksum + this.Reserved);
    checksum = (short)(checksum + (this.TagSerialNumber & 0xFF));
    checksum = (short)(checksum + (this.TagSerialNumber >> 8 & 0xFF));
    checksum = (short)(checksum + (this.DescriptorCRC & 0xFF));
    checksum = (short)(checksum + (this.DescriptorCRC >> 8 & 0xFF));
    checksum = (short)(checksum + (this.DescriptorCRCLength & 0xFF));
    checksum = (short)(checksum + (this.DescriptorCRCLength >> 8 & 0xFF));
    checksum = (short)(int)(checksum + (this.TagLocation & 0xFFL));
    checksum = (short)(int)(checksum + (this.TagLocation >> 8L & 0xFFL));
    checksum = (short)(int)(checksum + (this.TagLocation >> 16L & 0xFFL));
    checksum = (short)(int)(checksum + (this.TagLocation >> 24L & 0xFFL));
    return (short)(checksum & 0xFF);
  }
}
