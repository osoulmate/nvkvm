package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
public class Lb_addr
{
  public long lb_num;
  public int part_num;
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.lb_num = (myRandomAccessFile.readUnsignedByte() + myRandomAccessFile.readUnsignedByte() * 256 + myRandomAccessFile.readUnsignedByte() * 256 * 256 + myRandomAccessFile.readUnsignedByte() * 256 * 256 * 256);
    this.part_num = myRandomAccessFile.readUnsignedByte() + myRandomAccessFile.readUnsignedByte() * 256;
  }
  public int read(byte[] rawBytes, int startPosition) {
    int position = startPosition;
    this.lb_num = ((rawBytes[position++] & 0xFF) + (
      rawBytes[position++] & 0xFF) * 256 + (
      rawBytes[position++] & 0xFF) * 256 * 256 + (
      rawBytes[position++] & 0xFF) * 256 * 256 * 256);
    this.part_num = (rawBytes[position++] & 0xFF) + (rawBytes[position++] & 0xFF) * 256;
    return position;
  }
  public void write(RandomAccessFile myRandomAccessFile) throws IOException {
    byte[] rawBytes = getBytes();
    myRandomAccessFile.write(rawBytes);
  }
  public byte[] getBytes() {
    byte[] rawBytes = new byte[6];
    int pos = 0;
    pos = BinaryTools.getUInt32BytesFromLong(this.lb_num, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.part_num, rawBytes, pos);
    return rawBytes;
  }
}
