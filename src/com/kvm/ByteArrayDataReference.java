package com.kvm;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
public class ByteArrayDataReference implements DataReference {
  private byte[] buffer = null;
  private int start = 0;
  private int length = 0;
  public ByteArrayDataReference(byte[] buffer) {
    if (buffer != null) {
      this.buffer = (byte[])buffer.clone();
      this.length = this.buffer.length;
    }
    else {
      this.buffer = null;
      this.length = 0;
    } 
    this.start = 0;
  }
  public ByteArrayDataReference(byte[] buffer, int start, int length) {
    if (buffer != null) {
      this.buffer = (byte[])buffer.clone();
    }
    else {
      this.buffer = null;
    } 
    this.start = start;
    this.length = length;
  }
  public long getLength() {
    return this.length;
  }
  public InputStream createInputStream() throws IOException {
    return new ByteArrayInputStream(this.buffer, this.start, this.length);
  }
}
