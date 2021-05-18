package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
public class Short_ad
{
  public long ExtentLength;
  public long ExtentPosition;
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.ExtentLength = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.ExtentPosition = BinaryTools.readUInt32AsLong(myRandomAccessFile);
  }
  public byte[] getBytes() {
    byte[] rawBytes = new byte[8];
    int pos = 0;
    pos = BinaryTools.getUInt32BytesFromLong(this.ExtentLength, rawBytes, pos);
    pos = BinaryTools.getUInt32BytesFromLong(this.ExtentPosition, rawBytes, pos);
    return rawBytes;
  }
}
