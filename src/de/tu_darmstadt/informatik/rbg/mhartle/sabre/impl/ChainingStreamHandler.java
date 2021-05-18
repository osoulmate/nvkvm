package de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.ContentHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.Element;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.Fixup;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.HandlerException;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.StreamHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.StructureHandler;
import java.io.File;
public class ChainingStreamHandler
  implements StreamHandler {
  private StructureHandler chainedStructureHandler = null;
  private ContentHandler chainedContentHandler = null;
  public ChainingStreamHandler(StructureHandler chainedStructureHandler, ContentHandler chainedContentHandler) {
    this.chainedStructureHandler = chainedStructureHandler;
    this.chainedContentHandler = chainedContentHandler;
  }
  public void startDocument() throws HandlerException {
    if (this.chainedStructureHandler != null) {
      this.chainedStructureHandler.startDocument();
    }
  }
  public void startElement(Element element) throws HandlerException {
    if (this.chainedStructureHandler != null) {
      this.chainedStructureHandler.startElement(element);
    }
  }
  public void data(DataReference reference) throws HandlerException {
    if (this.chainedContentHandler != null) {
      this.chainedContentHandler.data(reference);
    }
  }
  public Fixup fixup(DataReference reference) throws HandlerException {
    Fixup fixup = null;
    if (this.chainedContentHandler != null) {
      fixup = this.chainedContentHandler.fixup(reference);
    }
    return fixup;
  }
  public long mark() throws HandlerException {
    long mark = -1L;
    if (this.chainedContentHandler != null) {
      mark = this.chainedContentHandler.mark();
    }
    return mark;
  }
  public void endElement() throws HandlerException {
    if (this.chainedStructureHandler != null) {
      this.chainedStructureHandler.endElement();
    }
  }
  public void endDocument() throws HandlerException {
    if (this.chainedStructureHandler != null) {
      this.chainedStructureHandler.endDocument();
    }
  }
  public void extendData(boolean isExtendData, File extendFile) throws HandlerException {
    if (this.chainedContentHandler != null)
    {
      this.chainedContentHandler.extendData(isExtendData, extendFile);
    }
  }
  public void isDataOver(boolean isOver) throws HandlerException {
    if (this.chainedContentHandler != null)
    {
      this.chainedContentHandler.isDataOver(isOver);
    }
  }
  public void isEndOver(boolean isOver) throws HandlerException {
    if (this.chainedContentHandler != null)
    {
      this.chainedContentHandler.isEndOver(isOver);
    }
  }
}
