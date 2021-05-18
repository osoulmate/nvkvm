package de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
public class ThreeByteDataReference
  implements DataReference {
  private long value = 0L;
  public ThreeByteDataReference(long value) {
    this.value = value;
  }
  public long getLength() {
    return 3L;
  }
  public InputStream createInputStream() throws IOException {
    byte[] buffer = (byte[])null;
    buffer = new byte[3];
    buffer[0] = (byte)(int)((this.value & 0xFF0000L) >> 16L);
    buffer[1] = (byte)(int)((this.value & 0xFF00L) >> 8L);
    buffer[2] = (byte)(int)(this.value & 0xFFL);
    return new ByteArrayInputStream(buffer);
  }
}
