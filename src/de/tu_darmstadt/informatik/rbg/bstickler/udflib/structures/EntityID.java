package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import java.io.IOException;
import java.io.RandomAccessFile;
public class EntityID
{
  public byte Flags;
  public byte[] Identifier = new byte[23];
  public byte[] IdentifierSuffix = new byte[8];
  public void setIdentifier(String identifier) throws Exception {
    if (identifier.length() > 23)
    {
      throw new Exception("error: identifier length exceeds maximum length of 23 characters");
    }
    this.Identifier = new byte[23];
    for (int i = 0; i < identifier.length() && i < 23; i++)
    {
      this.Identifier[i] = (byte)identifier.charAt(i);
    }
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.Flags = myRandomAccessFile.readByte();
    this.Identifier = new byte[23];
    myRandomAccessFile.read(this.Identifier);
    this.IdentifierSuffix = new byte[8];
    myRandomAccessFile.read(this.IdentifierSuffix);
  }
  public void write(RandomAccessFile myRandomAccessFile) throws IOException {
    myRandomAccessFile.write(getBytes());
  }
  public byte[] getBytes() {
    byte[] rawBytes = new byte[32];
    rawBytes[0] = this.Flags;
    System.arraycopy(this.Identifier, 0, rawBytes, 1, this.Identifier.length);
    System.arraycopy(this.IdentifierSuffix, 0, rawBytes, this.Identifier.length + 1, this.IdentifierSuffix.length);
    return rawBytes;
  }
}
