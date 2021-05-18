package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
public class Timestamp
{
  public int TypeAndTimezone;
  public int Year;
  public byte Month;
  public byte Day;
  public byte Hour;
  public byte Minute;
  public byte Second;
  public byte Centiseconds;
  public byte HundredsofMicroseconds;
  public byte Microseconds;
  public Timestamp() {}
  public Timestamp(Calendar myCalendar) {
    set(myCalendar);
  }
  public void set(Calendar myCalendar) {
    int offsetInMillisecons = myCalendar.get(15) + myCalendar.get(16);
    int offsetInMinutes = offsetInMillisecons / 60000;
    int twelveBitSignedValue = offsetInMinutes;
    if (twelveBitSignedValue < 0) {
      twelveBitSignedValue *= -1;
      twelveBitSignedValue ^= 0xFFF;
      twelveBitSignedValue++;
    } 
    this.TypeAndTimezone = 0x1000 | twelveBitSignedValue;
    this.Year = myCalendar.get(1);
    this.Month = (byte)(myCalendar.get(2) + 1);
    this.Day = (byte)myCalendar.get(5);
    this.Hour = (byte)myCalendar.get(11);
    this.Minute = (byte)myCalendar.get(12);
    this.Second = (byte)myCalendar.get(13);
    this.Centiseconds = (byte)(myCalendar.get(14) / 100);
    this.HundredsofMicroseconds = (byte)(myCalendar.get(14) % 100);
    this.Microseconds = 0;
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.TypeAndTimezone = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.Year = BinaryTools.readUInt16AsInt(myRandomAccessFile);
    this.Month = myRandomAccessFile.readByte();
    this.Day = myRandomAccessFile.readByte();
    this.Hour = myRandomAccessFile.readByte();
    this.Minute = myRandomAccessFile.readByte();
    this.Second = myRandomAccessFile.readByte();
    this.Centiseconds = myRandomAccessFile.readByte();
    this.HundredsofMicroseconds = myRandomAccessFile.readByte();
    this.Microseconds = myRandomAccessFile.readByte();
  }
  public void write(RandomAccessFile myRandomAccessFile) throws IOException {
    myRandomAccessFile.write(getBytes());
  }
  public byte[] getBytes() {
    byte[] rawBytes = new byte[12];
    int pos = 0;
    pos = BinaryTools.getUInt16BytesFromInt(this.TypeAndTimezone, rawBytes, pos);
    pos = BinaryTools.getUInt16BytesFromInt(this.Year, rawBytes, pos);
    rawBytes[pos++] = (byte)(this.Month & 0xFF);
    rawBytes[pos++] = (byte)(this.Day & 0xFF);
    rawBytes[pos++] = (byte)(this.Hour & 0xFF);
    rawBytes[pos++] = (byte)(this.Minute & 0xFF);
    rawBytes[pos++] = (byte)(this.Second & 0xFF);
    rawBytes[pos++] = (byte)(this.Centiseconds & 0xFF);
    rawBytes[pos++] = (byte)(this.HundredsofMicroseconds & 0xFF);
    rawBytes[pos++] = (byte)(this.Microseconds & 0xFF);
    return rawBytes;
  }
}
