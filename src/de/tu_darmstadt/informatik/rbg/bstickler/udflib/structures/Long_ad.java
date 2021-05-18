package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
public class Long_ad
{
  public long ExtentLength;
  public Lb_addr ExtentLocation = new Lb_addr();
  public byte[] implementationUse = new byte[6];
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.ExtentLength = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.ExtentLocation = new Lb_addr();
    this.ExtentLocation.read(myRandomAccessFile);
    this.implementationUse = new byte[6];
    myRandomAccessFile.read(this.implementationUse);
  }
  public int read(byte[] rawBytes, int startPosition) {
    int position = startPosition;
    this.ExtentLength = ((rawBytes[position++] & 0xFF) + (
      rawBytes[position++] & 0xFF) * 256 + (
      rawBytes[position++] & 0xFF) * 256 * 256 + (rawBytes[position++] & 0xFF) * 256 * 256 * 256);
    this.ExtentLocation = new Lb_addr();
    position = this.ExtentLocation.read(rawBytes, position);
    this.implementationUse = new byte[6];
    System.arraycopy(rawBytes, position, this.implementationUse, 0, this.implementationUse.length);
    position += this.implementationUse.length;
    return position;
  }
  public void write(RandomAccessFile myRandomAccessFile) throws IOException {
    byte[] rawBytes = getBytes();
    myRandomAccessFile.write(rawBytes);
  }
  public byte[] getBytes() {
    byte[] ExtentLocationBytes = this.ExtentLocation.getBytes();
    byte[] rawBytes = new byte[10 + ExtentLocationBytes.length];
    int pos = 0;
    pos = BinaryTools.getUInt32BytesFromLong(this.ExtentLength, rawBytes, pos);
    System.arraycopy(ExtentLocationBytes, 0, rawBytes, pos, ExtentLocationBytes.length);
    pos += ExtentLocationBytes.length;
    System.arraycopy(this.implementationUse, 0, rawBytes, pos, this.implementationUse.length);
    pos += this.implementationUse.length;
    return rawBytes;
  }
}
