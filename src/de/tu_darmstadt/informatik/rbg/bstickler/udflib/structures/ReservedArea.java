package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import java.io.IOException;
import java.io.RandomAccessFile;
public class ReservedArea
{
  public static boolean check(RandomAccessFile myRandomAccessFile) throws IOException {
    byte[] buffer = new byte[32768];
    myRandomAccessFile.read(buffer);
    for (int i = 0; i < buffer.length; i++) {
      if (buffer[i] != 0)
      {
        return false;
      }
    } 
    return true;
  }
  public static void write(RandomAccessFile myRandomAccessFile) throws IOException {
    byte[] buffer = new byte[32768];
    myRandomAccessFile.write(buffer);
  }
}
