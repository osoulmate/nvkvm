package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.OSTAUnicode;
import java.io.IOException;
import java.io.RandomAccessFile;
public class LVInformation
{
  public CharSpec LVICharset = new CharSpec();
  public byte[] LogicalVolumeIdentifier = new byte[128];
  public byte[] LVInfo1 = new byte[36];
  public byte[] LVInfo2 = new byte[36];
  public byte[] LVInfo3 = new byte[36];
  public EntityID ImplementationID = new EntityID();
  public byte[] ImplementationUse = new byte[128];
  public void setLogicalVolumeIdentifier(String volumeIdentifier) throws Exception {
    if (volumeIdentifier.length() > 126)
    {
      throw new Exception("error: logical volume identifier length > 126 characters");
    }
    this.LogicalVolumeIdentifier = new byte[128];
    try {
      byte[] volumeIdentifierBytes = volumeIdentifier.getBytes("UTF-16");
      int compId = OSTAUnicode.getBestCompressionId(volumeIdentifierBytes);
      byte[] tmpIdentifier = OSTAUnicode.CompressUnicodeByte(volumeIdentifierBytes, compId);
      int length = (tmpIdentifier.length < 127) ? tmpIdentifier.length : 127;
      System.arraycopy(tmpIdentifier, 0, this.LogicalVolumeIdentifier, 0, length);
      this.LogicalVolumeIdentifier[this.LogicalVolumeIdentifier.length - 1] = (byte)length;
    }
    catch (Exception exception) {}
  }
  public void setLVInfo1(String lvInfo) throws Exception {
    if (lvInfo.length() > 34)
    {
      throw new Exception("error: lvInfo length > 34 characters");
    }
    this.LVInfo1 = new byte[36];
    try {
      byte[] lvInfoBytes = lvInfo.getBytes("UTF-16");
      int compId = OSTAUnicode.getBestCompressionId(lvInfoBytes);
      byte[] tmpIdentifier = OSTAUnicode.CompressUnicodeByte(lvInfoBytes, compId);
      int length = (tmpIdentifier.length < 35) ? tmpIdentifier.length : 35;
      System.arraycopy(tmpIdentifier, 0, this.LVInfo1, 0, length);
      this.LogicalVolumeIdentifier[this.LVInfo1.length - 1] = (byte)length;
    }
    catch (Exception exception) {}
  }
  public void setLVInfo2(String lvInfo) throws Exception {
    if (lvInfo.length() > 34)
    {
      throw new Exception("error: lvInfo length > 34 characters");
    }
    this.LVInfo2 = new byte[36];
    try {
      byte[] lvInfoBytes = lvInfo.getBytes("UTF-16");
      int compId = OSTAUnicode.getBestCompressionId(lvInfoBytes);
      byte[] tmpIdentifier = OSTAUnicode.CompressUnicodeByte(lvInfoBytes, compId);
      int length = (tmpIdentifier.length < 35) ? tmpIdentifier.length : 35;
      System.arraycopy(tmpIdentifier, 0, this.LVInfo2, 0, length);
      this.LogicalVolumeIdentifier[this.LVInfo2.length - 1] = (byte)length;
    }
    catch (Exception exception) {}
  }
  public void setLVInfo3(String lvInfo) throws Exception {
    if (lvInfo.length() > 34)
    {
      throw new Exception("error: lvInfo length > 34 characters");
    }
    this.LVInfo3 = new byte[36];
    try {
      byte[] lvInfoBytes = lvInfo.getBytes("UTF-16");
      int compId = OSTAUnicode.getBestCompressionId(lvInfoBytes);
      byte[] tmpIdentifier = OSTAUnicode.CompressUnicodeByte(lvInfoBytes, compId);
      int length = (tmpIdentifier.length < 35) ? tmpIdentifier.length : 35;
      System.arraycopy(tmpIdentifier, 0, this.LVInfo3, 0, length);
      this.LogicalVolumeIdentifier[this.LVInfo3.length - 1] = (byte)length;
    }
    catch (Exception exception) {}
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.LVICharset = new CharSpec();
    this.LVICharset.read(myRandomAccessFile);
    this.LogicalVolumeIdentifier = new byte[128];
    myRandomAccessFile.read(this.LogicalVolumeIdentifier);
    this.LVInfo1 = new byte[36];
    myRandomAccessFile.read(this.LVInfo1);
    this.LVInfo2 = new byte[36];
    myRandomAccessFile.read(this.LVInfo2);
    this.LVInfo3 = new byte[36];
    myRandomAccessFile.read(this.LVInfo3);
    this.ImplementationID = new EntityID();
    this.ImplementationID.read(myRandomAccessFile);
    this.ImplementationUse = new byte[128];
    myRandomAccessFile.read(this.ImplementationUse);
  }
  public byte[] getBytes() {
    byte[] LVICharsetBytes = this.LVICharset.getBytes();
    byte[] ImplementationIDBytes = this.ImplementationID.getBytes();
    byte[] rawBytes = new byte[364 + 
        LVICharsetBytes.length + 
        ImplementationIDBytes.length];
    int pos = 0;
    System.arraycopy(LVICharsetBytes, 0, rawBytes, pos, LVICharsetBytes.length);
    pos += LVICharsetBytes.length;
    System.arraycopy(this.LogicalVolumeIdentifier, 0, rawBytes, pos, this.LogicalVolumeIdentifier.length);
    pos += this.LogicalVolumeIdentifier.length;
    System.arraycopy(this.LVInfo1, 0, rawBytes, pos, this.LVInfo1.length);
    pos += this.LVInfo1.length;
    System.arraycopy(this.LVInfo2, 0, rawBytes, pos, this.LVInfo2.length);
    pos += this.LVInfo2.length;
    System.arraycopy(this.LVInfo3, 0, rawBytes, pos, this.LVInfo3.length);
    pos += this.LVInfo3.length;
    System.arraycopy(ImplementationIDBytes, 0, rawBytes, pos, ImplementationIDBytes.length);
    pos += ImplementationIDBytes.length;
    System.arraycopy(this.ImplementationUse, 0, rawBytes, pos, this.ImplementationUse.length);
    pos += this.ImplementationUse.length;
    return rawBytes;
  }
}
