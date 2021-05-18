package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
public class Extend_ad
{
  public long len;
  public long loc;
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.len = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.loc = BinaryTools.readUInt32AsLong(myRandomAccessFile);
  }
  public void write(RandomAccessFile myRandomAccessFile) throws IOException {
    myRandomAccessFile.write(getBytes());
  }
  public byte[] getBytes() {
    byte[] rawBytes = new byte[8];
    int pos = 0;
    pos = BinaryTools.getUInt32BytesFromLong(this.len, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.loc, rawBytes, pos);
    return rawBytes;
  }
}
