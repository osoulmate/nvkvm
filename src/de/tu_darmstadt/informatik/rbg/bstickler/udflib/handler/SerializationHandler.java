package de.tu_darmstadt.informatik.rbg.bstickler.udflib.handler;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.UDFExtendFile;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.Element;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.Fixup;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.HandlerException;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.StreamHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.FileFixup;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
public class SerializationHandler
  implements StreamHandler
{
  private int blockSize = 2048;
  private File myOutputFile;
  private long position;
  private boolean isExtendData = false;
  private File extendFile = null;
  private Map<Long, UDFExtendFile> extendMap = new HashMap<Long, UDFExtendFile>();
  private boolean isEndOver = false;
  private boolean isDataOver = false;
  private long curPosition = 0L;
  private ByteArrayOutputStream bout = null;
  private DataOutputStream myDataOutputStream;
  public Map<Long, UDFExtendFile> getExtendMap() {
    return this.extendMap;
  }
  public ByteArrayOutputStream getBout() {
    return this.bout;
  }
  public SerializationHandler(File outputFile) throws HandlerException {
    this.myOutputFile = outputFile;
    this.position = 0L;
  }
  public void startDocument() throws HandlerException {
    this.position = 0L;
    this.curPosition = 0L;
    this.extendMap.clear();
    try {
      this.bout = new ByteArrayOutputStream();
      this.myDataOutputStream = new DataOutputStream(this.bout);
    }
    catch (Exception myIOException) {
      throw new HandlerException(myIOException);
    } 
  }
  public void endDocument() throws HandlerException {
    try {
      this.myDataOutputStream.close();
    }
    catch (IOException myIOException) {
      throw new HandlerException(myIOException);
    } 
  }
  public void startElement(Element myElement) throws HandlerException {}
  public void endElement() throws HandlerException {}
  public void data(DataReference myDataReference) throws HandlerException {
    InputStream myInputStream = null;
    try {
      int bufferLength = 32768;
      byte[] buffer = new byte[bufferLength];
      long length = myDataReference.getLength();
      long lengthToWrite = length;
      int bytesToRead = 0;
      int bytesHandled = 0;
      if (this.isExtendData) {
        this.position += myDataReference.getLength();
      }
      else {
        myInputStream = myDataReference.createInputStream();
        while (lengthToWrite > 0L) {
          if (lengthToWrite > bufferLength) {
            bytesToRead = bufferLength;
          }
          else {
            bytesToRead = (int)lengthToWrite;
          } 
          bytesHandled = myInputStream.read(buffer, 0, bytesToRead);
          if (bytesHandled == -1)
          {
            throw new HandlerException("Cannot read all data from reference.");
          }
          this.myDataOutputStream.write(buffer, 0, bytesHandled);
          lengthToWrite -= bytesHandled;
          this.position += bytesHandled;
        } 
        this.myDataOutputStream.flush();
      } 
      if (this.isDataOver && this.isEndOver)
      {
        UDFExtendFile file = new UDFExtendFile();
        if (this.isExtendData) {
          file.setContent(this.extendFile.getAbsolutePath().getBytes());
          long fileLen = this.extendFile.length();
          file.setFileActalLen(fileLen);
          if (0L == fileLen % this.blockSize)
          {
            file.setFileLen(fileLen);
          }
          else
          {
            file.setFileLen(fileLen + this.blockSize - fileLen % this.blockSize);
          }
        }
        else {
          file.setContent(this.bout.toByteArray());
          file.setFileLen((this.bout.toByteArray()).length);
        } 
        file.setExtendData(this.isExtendData);
        file.setPosition(this.curPosition);
        this.extendMap.put(Long.valueOf(this.curPosition), file);
        this.curPosition = this.position;
        this.bout.reset();
        this.isExtendData = false;
        this.isDataOver = false;
        this.isEndOver = false;
      }
    } catch (IOException myIOException) {
      throw new HandlerException(myIOException);
    } finally {
      try {
        if (myInputStream != null)
        {
          myInputStream.close();
          myInputStream = null;
        }
      } catch (IOException iOException) {}
    } 
  }
  public Fixup fixup(DataReference myDataReference) throws HandlerException {
    try {
      FileFixup fileFixup = 
        new FileFixup(new RandomAccessFile(this.myOutputFile, "rw"), this.position, myDataReference.getLength());
      data(myDataReference);
      return (Fixup)fileFixup;
    }
    catch (FileNotFoundException e) {
      throw new HandlerException(e);
    } 
  }
  public long mark() throws HandlerException {
    return this.position;
  }
  public void extendData(boolean isExtendData, File extendFile) throws HandlerException {
    this.isExtendData = isExtendData;
    this.extendFile = extendFile;
  }
  public void isEndOver(boolean isOver) throws HandlerException {
    this.isEndOver = isOver;
  }
  public void isDataOver(boolean isOver) throws HandlerException {
    this.isDataOver = isOver;
  }
}
