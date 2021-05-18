package de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
public class BinaryTools
{
  public static long readUInt64AsLong(RandomAccessFile myRandomAccessFile) throws IOException {
    long result = (myRandomAccessFile.readUnsignedByte() | 
      myRandomAccessFile.readUnsignedByte() << 8 | 
      myRandomAccessFile.readUnsignedByte() << 16 | 
      myRandomAccessFile.readUnsignedByte() << 24 | 
      myRandomAccessFile.readUnsignedByte() << 32 | 
      myRandomAccessFile.readUnsignedByte() << 40 | 
      myRandomAccessFile.readUnsignedByte() << 48 | 
      myRandomAccessFile.readUnsignedByte() << 56);
    return result;
  }
  public static long readUInt32AsLong(RandomAccessFile myRandomAccessFile) throws IOException {
    long result = (myRandomAccessFile.readUnsignedByte() | 
      myRandomAccessFile.readUnsignedByte() << 8 | 
      myRandomAccessFile.readUnsignedByte() << 16 | 
      myRandomAccessFile.readUnsignedByte() << 24);
    return result;
  }
  public static int readUInt32AsInt(RandomAccessFile myRandomAccessFile) throws IOException {
    int result = myRandomAccessFile.readUnsignedByte() | 
      myRandomAccessFile.readUnsignedByte() << 8 | 
      myRandomAccessFile.readUnsignedByte() << 16 | 
      myRandomAccessFile.readUnsignedByte() << 24;
    return result;
  }
  public static int readUInt16AsInt(RandomAccessFile myRandomAccessFile) throws IOException {
    int result = myRandomAccessFile.readUnsignedByte() | 
      myRandomAccessFile.readUnsignedByte() << 8;
    return result;
  }
  public static int getUInt64BytesFromLong(long value, byte[] target, int start) {
    int pos = start;
    target[pos++] = (byte)(int)(value & 0xFFL);
    target[pos++] = (byte)(int)(value >> 8L & 0xFFL);
    target[pos++] = (byte)(int)(value >> 16L & 0xFFL);
    target[pos++] = (byte)(int)(value >> 24L & 0xFFL);
    target[pos++] = (byte)(int)(value >> 32L & 0xFFL);
    target[pos++] = (byte)(int)(value >> 40L & 0xFFL);
    target[pos++] = (byte)(int)(value >> 48L & 0xFFL);
    target[pos++] = (byte)(int)(value >> 56L & 0xFFL);
    return pos;
  }
  public static int getUInt32BytesFromLong(long value, byte[] target, int start) {
    int pos = start;
    target[pos++] = (byte)(int)(value & 0xFFL);
    target[pos++] = (byte)(int)(value >> 8L & 0xFFL);
    target[pos++] = (byte)(int)(value >> 16L & 0xFFL);
    target[pos++] = (byte)(int)(value >> 24L & 0xFFL);
    return pos;
  }
  public static int getUInt16BytesFromInt(int value, byte[] target, int start) {
    int pos = start;
    target[pos++] = (byte)(value & 0xFF);
    target[pos++] = (byte)(value >> 8 & 0xFF);
    return pos;
  }
  public static long readUInt32AsLong(InputStream myInputStream) throws IOException {
    long result = ((myInputStream.read() & 0xFF) << 24 | (
      myInputStream.read() & 0xFF) << 16 | (
      myInputStream.read() & 0xFF) << 8 | 
      myInputStream.read() & 0xFF);
    return result;
  }
  public static long readUInt64AsLong(InputStream myInputStream) throws IOException {
    long result = (myInputStream.read() & 0xFF) << 56L | (
      myInputStream.read() & 0xFF) << 48L | (
      myInputStream.read() & 0xFF) << 40L | (
      myInputStream.read() & 0xFF) << 32L | (
      myInputStream.read() & 0xFF) << 24L | (
      myInputStream.read() & 0xFF) << 16L | (
      myInputStream.read() & 0xFF) << 8L | (
      myInputStream.read() & 0xFF);
    return result;
  }
  public static byte[] readByteArray(InputStream myInputStream, int length) throws IOException {
    byte[] result = new byte[length];
    int currentPosition = 0;
    while (currentPosition < length) {
      int bytesRead = myInputStream.read(result, currentPosition, length - currentPosition);
      currentPosition += bytesRead;
    } 
    return result;
  }
}
