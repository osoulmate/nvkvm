package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
public class IcbTag
{
  public long PriorRecordedNumberofDirectEntries;
  public int StrategyType;
  public byte[] StrateryParameter = new byte[2];
  public Lb_addr ParentICBLocation = new Lb_addr(); public int NumberofEntries;
  public byte Reserved;
  public byte FileType;
  public int Flags;
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.PriorRecordedNumberofDirectEntries = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.StrategyType = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.StrateryParameter = new byte[2];
    myRandomAccessFile.read(this.StrateryParameter);
    this.NumberofEntries = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.Reserved = myRandomAccessFile.readByte();
    this.FileType = myRandomAccessFile.readByte();
    this.ParentICBLocation = new Lb_addr();
    this.ParentICBLocation.read(myRandomAccessFile);
    this.Flags = BinaryTools.readUInt16AsInt(myRandomAccessFile);
  }
  public void write(RandomAccessFile myRandomAccessFile) throws IOException {
    byte[] rawBytes = getBytes();
    myRandomAccessFile.write(rawBytes);
  }
  public byte[] getBytes() {
    byte[] ParentICBLocationBytes = this.ParentICBLocation.getBytes();
    byte[] rawBytes = new byte[14 + ParentICBLocationBytes.length];
    int pos = 0;
    pos = BinaryTools.getUInt32BytesFromLong(this.PriorRecordedNumberofDirectEntries, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.StrategyType, rawBytes, pos);
    System.arraycopy(this.StrateryParameter, 0, rawBytes, pos, this.StrateryParameter.length);
    pos += this.StrateryParameter.length;
    pos = BinaryTools.getUInt16BytesFromInt(this.NumberofEntries, rawBytes, pos);
    rawBytes[pos++] = this.Reserved;
    rawBytes[pos++] = this.FileType;
    System.arraycopy(ParentICBLocationBytes, 0, rawBytes, pos, ParentICBLocationBytes.length);
    pos += ParentICBLocationBytes.length;
    pos = BinaryTools.getUInt16BytesFromInt(this.Flags, rawBytes, pos);
    return rawBytes;
  }
}
