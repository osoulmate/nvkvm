package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
public class LogicalVolumeHeaderDescriptor
{
  public long UniqueID;
  public byte[] Reserved = new byte[24];
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.UniqueID = BinaryTools.readUInt64AsLong(myRandomAccessFile);
    this.Reserved = new byte[24];
    myRandomAccessFile.read(this.Reserved);
  }
  public void write(RandomAccessFile myRandomAccessFile) throws IOException {
    byte[] rawBytes = getBytes();
    myRandomAccessFile.write(rawBytes);
  }
  public byte[] getBytes() {
    byte[] rawBytes = new byte[8 + this.Reserved.length];
    int pos = 0;
    pos = BinaryTools.getUInt64BytesFromLong(this.UniqueID, rawBytes, pos);
    System.arraycopy(this.Reserved, 0, rawBytes, pos, this.Reserved.length);
    pos += this.Reserved.length;
    return rawBytes;
  }
}
