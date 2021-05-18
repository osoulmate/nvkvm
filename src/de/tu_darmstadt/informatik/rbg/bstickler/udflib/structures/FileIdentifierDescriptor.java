package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.Checksum;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.OSTAUnicode;
import java.io.UnsupportedEncodingException;
public class FileIdentifierDescriptor
{
  public Tag DescriptorTag;
  public int FileVersionNumber;
  public short FileCharacteristics;
  public short LengthofFileIdentifier;
  public Long_ad ICB;
  public int LengthofImplementationUse;
  public byte[] ImplementationUse;
  public byte[] FileIdentifier;
  private byte[] Padding;
  public FileIdentifierDescriptor() {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.TagIdentifier = 257;
    this.FileVersionNumber = 1;
    this.ICB = new Long_ad();
    this.ImplementationUse = new byte[0];
    this.FileIdentifier = new byte[0];
    this.Padding = new byte[0];
  }
  public int read(byte[] rawBytes, int startPosition) {
    int position = startPosition;
    this.DescriptorTag = new Tag();
    position = this.DescriptorTag.read(rawBytes, position);
    this.FileVersionNumber = (rawBytes[position++] & 0xFF) + (rawBytes[position++] & 0xFF) * 256;
    this.FileCharacteristics = (short)(rawBytes[position++] & 0xFF);
    this.LengthofFileIdentifier = (short)(rawBytes[position++] & 0xFF);
    this.ICB = new Long_ad();
    position = this.ICB.read(rawBytes, position);
    this.LengthofImplementationUse = (rawBytes[position++] & 0xFF) + (rawBytes[position++] & 0xFF) * 256;
    this.ImplementationUse = new byte[this.LengthofImplementationUse];
    System.arraycopy(rawBytes, position, this.ImplementationUse, 0, this.ImplementationUse.length);
    position += this.ImplementationUse.length;
    this.FileIdentifier = new byte[this.LengthofFileIdentifier];
    System.arraycopy(rawBytes, position, this.FileIdentifier, 0, this.FileIdentifier.length);
    position += this.FileIdentifier.length;
    this.Padding = new byte[4 - (position - startPosition) % 4];
    System.arraycopy(rawBytes, position, this.Padding, 0, this.Padding.length);
    position += this.Padding.length;
    return position;
  }
  public byte[] getBytes() {
    byte[] rawBytesWithoutDescriptorTag = getBytesWithoutDescriptorTag();
    this.DescriptorTag.DescriptorCRCLength = rawBytesWithoutDescriptorTag.length;
    this.DescriptorTag.DescriptorCRC = Checksum.cksum(rawBytesWithoutDescriptorTag);
    byte[] descriptorTagBytes = this.DescriptorTag.getBytes();
    byte[] rawBytes = new byte[16 + rawBytesWithoutDescriptorTag.length];
    System.arraycopy(descriptorTagBytes, 0, rawBytes, 0, descriptorTagBytes.length);
    System.arraycopy(rawBytesWithoutDescriptorTag, 0, rawBytes, descriptorTagBytes.length, rawBytesWithoutDescriptorTag.length);
    return rawBytes;
  }
  public byte[] getBytesWithoutDescriptorTag() {
    byte[] ICBBytes = this.ICB.getBytes();
    int lengthWithoutPadding = 6 + ICBBytes.length + this.ImplementationUse.length + this.FileIdentifier.length;
    this.Padding = (lengthWithoutPadding % 4 != 0) ? new byte[4 - lengthWithoutPadding % 4] : new byte[0];
    byte[] rawBytes = new byte[lengthWithoutPadding + this.Padding.length];
    int pos = 0;
    pos = BinaryTools.getUInt16BytesFromInt(this.FileVersionNumber, rawBytes, pos);
    rawBytes[pos++] = (byte)(this.FileCharacteristics & 0xFF);
    rawBytes[pos++] = (byte)(this.LengthofFileIdentifier & 0xFF);
    System.arraycopy(ICBBytes, 0, rawBytes, pos, ICBBytes.length);
    pos += ICBBytes.length;
    rawBytes[pos++] = (byte)(this.LengthofImplementationUse & 0xFF);
    rawBytes[pos++] = (byte)(this.LengthofImplementationUse >> 8 & 0xFF);
    System.arraycopy(this.ImplementationUse, 0, rawBytes, pos, this.ImplementationUse.length);
    pos += this.ImplementationUse.length;
    System.arraycopy(this.FileIdentifier, 0, rawBytes, pos, this.FileIdentifier.length);
    pos += this.FileIdentifier.length;
    System.arraycopy(this.Padding, 0, rawBytes, pos, this.Padding.length);
    pos += this.Padding.length;
    return rawBytes;
  }
  public int getLength() {
    int lengthWithoutPadding = 38 + this.ImplementationUse.length + this.FileIdentifier.length;
    int paddingLength = (lengthWithoutPadding % 4 != 0) ? (4 - lengthWithoutPadding % 4) : 0;
    return lengthWithoutPadding + paddingLength;
  }
  public void setFileIdentifier(String fileIdentifier) throws Exception {
    byte[] TempFileIdentifier = new byte[255];
    if (fileIdentifier.length() > 255)
    {
      throw new Exception("FileIdentifier length > 255 characters not allowed");
    }
    try {
      byte[] fileIdentiferBytes = fileIdentifier.getBytes("UTF-16");
      int compId = OSTAUnicode.getBestCompressionId(fileIdentiferBytes);
      this.FileIdentifier = OSTAUnicode.CompressUnicodeByte(fileIdentiferBytes, compId);
    }
    catch (UnsupportedEncodingException unsupportedEncodingException) {}
    this.LengthofFileIdentifier = (short)this.FileIdentifier.length;
    if (this.LengthofFileIdentifier > 255) {
      this.LengthofFileIdentifier = 255;
      System.arraycopy(this.FileIdentifier, 0, TempFileIdentifier, 0, this.LengthofFileIdentifier - 2);
      TempFileIdentifier[253] = 0;
      TempFileIdentifier[254] = 36;
      this.FileIdentifier = TempFileIdentifier;
    } 
  }
}
