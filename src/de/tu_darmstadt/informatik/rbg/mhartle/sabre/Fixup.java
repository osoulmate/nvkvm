package de.tu_darmstadt.informatik.rbg.mhartle.sabre;
public interface Fixup extends ContentHandler {
  void close() throws HandlerException;
  boolean isClosed();
}
