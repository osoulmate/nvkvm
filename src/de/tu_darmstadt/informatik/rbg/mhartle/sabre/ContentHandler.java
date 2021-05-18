package de.tu_darmstadt.informatik.rbg.mhartle.sabre;
import java.io.File;
public interface ContentHandler {
  void data(DataReference paramDataReference) throws HandlerException;
  Fixup fixup(DataReference paramDataReference) throws HandlerException;
  long mark() throws HandlerException;
  void isEndOver(boolean paramBoolean) throws HandlerException;
  void isDataOver(boolean paramBoolean) throws HandlerException;
  void extendData(boolean paramBoolean, File paramFile) throws HandlerException;
}
