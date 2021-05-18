package de.tu_darmstadt.informatik.rbg.bstickler.udflib.handler;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.SabreUDFElement;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.Tag;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.Checksum;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.ContentHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.Element;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.HandlerException;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.StructureHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.ByteArrayDataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.ChainingStreamHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
public class DescriptorTagHandler
  extends ChainingStreamHandler
{
  protected Stack<Element> elementStack;
  protected Stack<DataReference> dataReferenceStack;
  protected Map<Long, byte[]> fileMap = (Map)new HashMap<Long, byte[]>();
  public Map<Long, byte[]> getFileMap() {
    return this.fileMap;
  }
  public DescriptorTagHandler(StructureHandler myStructureHandler, ContentHandler myContentHandler) {
    super(myStructureHandler, myContentHandler);
    this.elementStack = new Stack<Element>();
    this.dataReferenceStack = new Stack<DataReference>();
  }
  public void startElement(Element myElement) throws HandlerException {
    this.elementStack.push(myElement);
    super.startElement(myElement);
  }
  public void endElement() throws HandlerException {
    Element myElement = this.elementStack.pop();
    if (myElement.getId() == SabreUDFElement.UDFElementType.DescriptorTag)
    {
      createAndPassDescriptorTag();
    }
    super.endElement();
  }
  private void createAndPassDescriptorTag() throws HandlerException {
    InputStream myInputStream = null;
    Tag descriptorTag = new Tag();
    byte[] payload = new byte[0];
    try {
      DataReference myDataReference = this.dataReferenceStack.pop();
      myInputStream = myDataReference.createInputStream();
      payload = BinaryTools.readByteArray(myInputStream, (int)myDataReference.getLength());
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      descriptorTag.DescriptorVersion = (int)BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      descriptorTag.TagSerialNumber = (int)BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      descriptorTag.TagLocation = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      descriptorTag.TagIdentifier = (int)BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
    }
    catch (IOException myIOException) {
      throw new HandlerException(myIOException);
    }
    finally {
      if (myInputStream != null) {
        try {
          myInputStream.close();
        }
        catch (IOException iOException) {}
      }
    } 
    descriptorTag.DescriptorCRCLength = payload.length;
    descriptorTag.DescriptorCRC = Checksum.cksum(payload);
    super.data((DataReference)new ByteArrayDataReference(descriptorTag.getBytes()));
    super.data((DataReference)new ByteArrayDataReference(payload));
    isDataOver(true);
  }
  public void data(DataReference myDataReference) throws HandlerException {
    if (this.elementStack.size() != 0 && ((Element)this.elementStack.peek()).getId() == SabreUDFElement.UDFElementType.DescriptorTag) {
      this.dataReferenceStack.push(myDataReference);
    }
    else {
      super.data(myDataReference);
    } 
  }
}
