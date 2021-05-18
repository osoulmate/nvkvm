package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
public class PartitionMapType1
{
  public byte PartitionMapType = 1;
  public byte PartitionMapLength = 6;
  public int VolumeSequenceNumber = 1;
  public int PartitionNumber = 0;
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.PartitionMapType = myRandomAccessFile.readByte();
    this.PartitionMapLength = myRandomAccessFile.readByte();
    this.VolumeSequenceNumber = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.PartitionNumber = BinaryTools.readUInt16AsInt(myRandomAccessFile);
  }
  public void write(RandomAccessFile myRandomAccessFile) throws IOException {
    myRandomAccessFile.write(getBytes());
  }
  public byte[] getBytes() {
    byte[] rawBytes = new byte[6];
    int pos = 0;
    rawBytes[pos++] = this.PartitionMapType;
    rawBytes[pos++] = this.PartitionMapLength;
    pos = BinaryTools.getUInt16BytesFromInt(this.VolumeSequenceNumber, rawBytes, pos);
    BinaryTools.getUInt16BytesFromInt(this.PartitionNumber, rawBytes, pos);
    return rawBytes;
  }
}
