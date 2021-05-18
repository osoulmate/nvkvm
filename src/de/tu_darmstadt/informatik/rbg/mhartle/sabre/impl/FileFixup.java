package de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.Fixup;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.FixupListener;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.HandlerException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
public class FileFixup
  implements Fixup
{
  private RandomAccessFile randomAccessFile = null;
  private long position = 0L;
  private long available = 0L;
  private boolean closed = false;
  public FileFixup(RandomAccessFile file, long position, long available) {
    this.randomAccessFile = file;
    this.position = position;
    this.available = available;
  }
  public void data(DataReference reference) throws HandlerException {
    InputStream inputStream = null;
    byte[] buffer = (byte[])null;
    int bytesRead = 0;
    if (!this.closed) {
      if (reference.getLength() > this.available)
      {
        throw new HandlerException("Fixup larger than available space.");
      }
      try {
        this.randomAccessFile.seek(this.position);
        buffer = new byte[1024];
        inputStream = reference.createInputStream();
        while ((bytesRead = inputStream.read(buffer, 0, 1024)) != -1)
        {
          this.randomAccessFile.write(buffer, 0, bytesRead);
        }
        this.position += reference.getLength();
        this.available -= reference.getLength();
      }
      catch (FileNotFoundException e) {
        throw new HandlerException(e);
      }
      catch (IOException e) {
        throw new HandlerException(e);
      } 
    } 
  }
  public Fixup fixup(DataReference reference) throws HandlerException {
    throw new RuntimeException("Cannot yet handle fixup in fixup.");
  }
  public long mark() throws HandlerException {
    return this.position;
  }
  public void close() throws HandlerException {
    this.closed = true;
  }
  public boolean isClosed() {
    return this.closed;
  }
  public void addFixupListener(FixupListener listener) throws HandlerException {}
  public void removeFixupListener(FixupListener listener) throws HandlerException {}
  public RandomAccessFile getFile() {
    return this.randomAccessFile;
  }
  public long getPosition() {
    return this.position;
  }
  public long getAvailable() {
    return this.available;
  }
  public void extendData(boolean isExtendData, File extendFile) throws HandlerException {}
  public void isDataOver(boolean isOver) throws HandlerException {}
  public void isEndOver(boolean isOver) throws HandlerException {}
}
