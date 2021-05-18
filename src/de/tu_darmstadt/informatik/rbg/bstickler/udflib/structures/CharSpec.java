package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import java.io.IOException;
import java.io.RandomAccessFile;
public class CharSpec
{
  public byte CharacterSetType;
  public byte[] CharacterSetInfo;
  public CharSpec() {
    this.CharacterSetType = 0;
    this.CharacterSetInfo = new byte[63];
    this.CharacterSetInfo[0] = 79;
    this.CharacterSetInfo[1] = 83;
    this.CharacterSetInfo[2] = 84;
    this.CharacterSetInfo[3] = 65;
    this.CharacterSetInfo[4] = 32;
    this.CharacterSetInfo[5] = 67;
    this.CharacterSetInfo[6] = 111;
    this.CharacterSetInfo[7] = 109;
    this.CharacterSetInfo[8] = 112;
    this.CharacterSetInfo[9] = 114;
    this.CharacterSetInfo[10] = 101;
    this.CharacterSetInfo[11] = 115;
    this.CharacterSetInfo[12] = 115;
    this.CharacterSetInfo[13] = 101;
    this.CharacterSetInfo[14] = 100;
    this.CharacterSetInfo[15] = 32;
    this.CharacterSetInfo[16] = 85;
    this.CharacterSetInfo[17] = 110;
    this.CharacterSetInfo[18] = 105;
    this.CharacterSetInfo[19] = 99;
    this.CharacterSetInfo[20] = 111;
    this.CharacterSetInfo[21] = 100;
    this.CharacterSetInfo[22] = 101;
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.CharacterSetType = myRandomAccessFile.readByte();
    this.CharacterSetInfo = new byte[63];
    myRandomAccessFile.read(this.CharacterSetInfo);
  }
  public void write(RandomAccessFile myRandomAccessFile) throws IOException {
    myRandomAccessFile.write(getBytes());
  }
  public byte[] getBytes() {
    byte[] rawBytes = new byte[64];
    rawBytes[0] = this.CharacterSetType;
    System.arraycopy(this.CharacterSetInfo, 0, rawBytes, 1, this.CharacterSetInfo.length);
    return rawBytes;
  }
}
