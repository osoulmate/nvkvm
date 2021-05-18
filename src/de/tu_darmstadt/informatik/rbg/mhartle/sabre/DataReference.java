package de.tu_darmstadt.informatik.rbg.mhartle.sabre;
import java.io.IOException;
import java.io.InputStream;
public interface DataReference {
  long getLength();
  InputStream createInputStream() throws IOException;
}
