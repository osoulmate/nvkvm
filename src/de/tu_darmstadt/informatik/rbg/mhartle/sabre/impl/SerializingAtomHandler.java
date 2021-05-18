package de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.Element;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.Fixup;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.HandlerException;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.StreamHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
public class SerializingAtomHandler
  implements StreamHandler
{
  private File file = null;
  private RandomAccessFile randomAccessFile = null;
  private long position = 0L;
  public SerializingAtomHandler(File file) throws FileNotFoundException {
    this.file = file;
    this.randomAccessFile = new RandomAccessFile(this.file, "rw");
  }
  public void startDocument() throws HandlerException {
    try {
      this.randomAccessFile.setLength(0L);
    }
    catch (IOException e) {
      throw new HandlerException(e);
    } 
  }
  public void startElement(Element element) throws HandlerException {}
  public void data(DataReference reference) throws HandlerException {
    InputStream inputStream = null;
    byte[] buffer = (byte[])null;
    int bytesToRead = 0;
    int bytesHandled = 0;
    int bufferLength = 65535;
    long lengthToWrite = 0L;
    long length = 0L;
    try {
      buffer = new byte[bufferLength];
      length = reference.getLength();
      lengthToWrite = length;
      inputStream = reference.createInputStream();
      this.randomAccessFile.seek(this.position);
      while (lengthToWrite > 0L)
      {
        if (lengthToWrite > bufferLength) {
          bytesToRead = bufferLength;
        }
        else {
          bytesToRead = (int)lengthToWrite;
        } 
        bytesHandled = inputStream.read(buffer, 0, bytesToRead);
        if (bytesHandled == -1)
        {
          throw new HandlerException("Cannot read all data from reference.");
        }
        this.randomAccessFile.write(buffer, 0, bytesHandled);
        lengthToWrite -= bytesHandled;
        this.position += bytesHandled;
      }
    }
    catch (IOException e) {
      throw new HandlerException(e);
    } finally {
      try {
        if (inputStream != null)
        {
          inputStream.close();
          inputStream = null;
        }
      } catch (IOException iOException) {}
    } 
  }
  public Fixup fixup(DataReference reference) throws HandlerException {
    Fixup fixup = null;
    fixup = new FileFixup(this.randomAccessFile, this.position, reference.getLength());
    data(reference);
    return fixup;
  }
  public long mark() throws HandlerException {
    return this.position;
  }
  public void endElement() throws HandlerException {}
  public void endDocument() throws HandlerException {
    try {
      this.randomAccessFile.close();
    }
    catch (IOException e) {
      throw new HandlerException(e);
    } 
  }
  public void extendData(boolean isExtendData, File extendFile) throws HandlerException {}
  public void isDataOver(boolean isOver) throws HandlerException {}
  public void isEndOver(boolean isOver) throws HandlerException {}
}
