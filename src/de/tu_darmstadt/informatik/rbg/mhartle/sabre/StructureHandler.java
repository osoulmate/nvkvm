package de.tu_darmstadt.informatik.rbg.mhartle.sabre;
public interface StructureHandler {
  void startDocument() throws HandlerException;
  void startElement(Element paramElement) throws HandlerException;
  void endElement() throws HandlerException;
  void endDocument() throws HandlerException;
}
