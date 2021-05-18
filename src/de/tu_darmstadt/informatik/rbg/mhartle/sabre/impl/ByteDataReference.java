package de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
public class ByteDataReference
  implements DataReference {
  private long value = 0L;
  public ByteDataReference(long value) {
    this.value = value;
  }
  public long getLength() {
    return 1L;
  }
  public InputStream createInputStream() throws IOException {
    byte[] buffer = (byte[])null;
    buffer = new byte[1];
    buffer[0] = (byte)(int)(this.value & 0xFFL);
    return new ByteArrayInputStream(buffer);
  }
}
