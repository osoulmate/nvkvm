package com.kvm;
import java.io.IOException;
import java.io.InputStream;
public interface DataReference {
  long getLength();
  InputStream createInputStream() throws IOException;
}
