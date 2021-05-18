package de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
public class DWordDataReference
  implements DataReference {
  private long value = 0L;
  public DWordDataReference(long value) {
    this.value = value;
  }
  public long getLength() {
    return 8L;
  }
  public InputStream createInputStream() throws IOException {
    byte[] buffer = (byte[])null;
    buffer = new byte[8];
    buffer[0] = (byte)(int)(this.value >> 56L);
    buffer[1] = (byte)(int)(this.value >> 48L);
    buffer[2] = (byte)(int)(this.value >> 40L);
    buffer[3] = (byte)(int)(this.value >> 32L);
    buffer[4] = (byte)(int)(this.value >> 24L);
    buffer[5] = (byte)(int)(this.value >> 16L);
    buffer[6] = (byte)(int)(this.value >> 8L);
    buffer[7] = (byte)(int)(this.value >> 0L);
    return new ByteArrayInputStream(buffer);
  }
}
