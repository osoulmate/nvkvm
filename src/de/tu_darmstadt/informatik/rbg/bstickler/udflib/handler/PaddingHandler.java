package de.tu_darmstadt.informatik.rbg.bstickler.udflib.handler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.ContentHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.HandlerException;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.StructureHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.ByteArrayDataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.ChainingStreamHandler;
public class PaddingHandler
  extends ChainingStreamHandler
{
  private int blockSize = 2048;
  private long currentPosition = 0L;
  public PaddingHandler(StructureHandler myStructureHandler, ContentHandler myContentHandler) {
    super(myStructureHandler, myContentHandler);
  }
  public void setBlockSize(int blockSize) {
    this.blockSize = blockSize;
  }
  public void data(DataReference myDataReference) throws HandlerException {
    this.currentPosition += myDataReference.getLength();
    super.data(myDataReference);
  }
  public void endElement() throws HandlerException {
    if (this.currentPosition % this.blockSize != 0L) {
      int paddingLength = this.blockSize - (int)(this.currentPosition % this.blockSize);
      isEndOver(true);
      isDataOver(true);
      super.data((DataReference)new ByteArrayDataReference(new byte[paddingLength]));
      this.currentPosition += paddingLength;
    }
    else {
      isEndOver(true);
      super.data((DataReference)new ByteArrayDataReference(new byte[0]));
    } 
    super.endElement();
  }
}
