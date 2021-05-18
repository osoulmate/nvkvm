package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import java.io.IOException;
import java.io.RandomAccessFile;
public class VolumeStructureDescriptor
{
  public byte StructureType;
  public byte[] StandardIdentifier = new byte[5];
  public byte[] StructureData = new byte[2041];
  public byte StructureVersion;
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.StructureType = myRandomAccessFile.readByte();
    this.StandardIdentifier = new byte[5];
    myRandomAccessFile.read(this.StandardIdentifier);
    this.StructureVersion = myRandomAccessFile.readByte();
    this.StructureData = new byte[2041];
    myRandomAccessFile.read(this.StructureData);
  }
  public void write(RandomAccessFile myRandomAccessFile) throws IOException {
    myRandomAccessFile.writeByte(this.StructureType);
    myRandomAccessFile.write(this.StandardIdentifier);
    myRandomAccessFile.writeByte(this.StructureVersion);
    myRandomAccessFile.write(this.StructureData);
  }
  public byte[] getBytes() {
    byte[] rawBytes = new byte[2048];
    int pos = 0;
    rawBytes[pos++] = this.StructureType;
    System.arraycopy(this.StandardIdentifier, 0, rawBytes, pos, this.StandardIdentifier.length);
    pos += this.StandardIdentifier.length;
    rawBytes[pos++] = this.StructureVersion;
    System.arraycopy(this.StructureData, 0, rawBytes, pos, this.StructureData.length);
    pos += this.StructureData.length;
    return rawBytes;
  }
}
