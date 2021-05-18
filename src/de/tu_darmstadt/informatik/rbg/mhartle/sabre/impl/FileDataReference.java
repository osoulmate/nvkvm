package de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.io.LimitingInputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
public class FileDataReference
  implements DataReference {
  private File file = null;
  private long position = 0L;
  private long length = 0L;
  public FileDataReference(File file) {
    this.file = file;
    this.position = 0L;
    this.length = -1L;
  }
  public FileDataReference(File file, int position, int length) {
    this.file = file;
    this.position = position;
    this.length = length;
  }
  public long getLength() {
    long length = 0L;
    if (this.length == -1L) {
      length = this.file.length();
    } else {
      length = this.length;
    } 
    return length;
  }
  public InputStream createInputStream() throws IOException {
    LimitingInputStream limitingInputStream = null;
    InputStream fileInputStream = null;
    fileInputStream = new FileInputStream(this.file);
    if (this.position > 0L) {
      long skipped = 0L;
      while (skipped != this.position) {
        skipped = fileInputStream.skip(this.position - skipped);
      }
    } 
    if (this.length != -1L)
    {
      limitingInputStream = new LimitingInputStream(fileInputStream, (int)this.length);
    }
    return new BufferedInputStream((InputStream)limitingInputStream);
  }
}
