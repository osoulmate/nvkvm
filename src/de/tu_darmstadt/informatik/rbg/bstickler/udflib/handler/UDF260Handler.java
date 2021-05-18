package de.tu_darmstadt.informatik.rbg.bstickler.udflib.handler;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.SabreUDFElement;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.ExtendedFileEntry;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.Short_ad;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.Timestamp;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.ContentHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.Element;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.HandlerException;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.StructureHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.ByteArrayDataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.WordDataReference;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
public class UDF260Handler
  extends UDF201Handler
{
  public UDF260Handler(StructureHandler myStructureHandler, ContentHandler myContentHandler) {
    super(myStructureHandler, myContentHandler);
    this.udfVersionIdentifierSuffix = new byte[] { 96, 2 };
    this.minimumUDFReadRevision = 592;
    this.minimumUDFWriteRevision = 608;
    this.maximumUDFWriteRevision = 608;
    this.descriptorVersion = 3;
  }
  protected void createAndPassMetadataFile() throws HandlerException {
    InputStream myInputStream = null;
    Calendar recordingTimeCalendar = Calendar.getInstance();
    String applicationIdentifier = "";
    byte[] applicationIdentifierSuffix = new byte[0];
    long metadataFileLocation = 0L;
    long physicalPartitionStartingBlock = 0L;
    long metadataPartitionStartingBlock = 0L;
    long metadataPartitionEndingBlock = 0L;
    byte fileType = 0;
    try {
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      fileType = (byte)myInputStream.read();
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      metadataFileLocation = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      physicalPartitionStartingBlock = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      metadataPartitionEndingBlock = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      metadataPartitionStartingBlock = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      DataReference myDataReference = this.dataReferenceStack.pop();
      myInputStream = myDataReference.createInputStream();
      applicationIdentifierSuffix = BinaryTools.readByteArray(myInputStream, (int)myDataReference.getLength());
      myInputStream.close();
      myInputStream = null;
      myDataReference = this.dataReferenceStack.pop();
      myInputStream = myDataReference.createInputStream();
      applicationIdentifier = new String(BinaryTools.readByteArray(myInputStream, (int)myDataReference.getLength()));
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      recordingTimeCalendar.setTimeInMillis(BinaryTools.readUInt64AsLong(myInputStream));
      myInputStream.close();
      myInputStream = null;
    }
    catch (Exception myException) {
      throw new HandlerException(myException);
    }
    finally {
      if (myInputStream != null) {
        try {
          myInputStream.close();
        }
        catch (IOException iOException) {}
      }
    } 
    ExtendedFileEntry metadataExtendedFileEntry = new ExtendedFileEntry();
    metadataExtendedFileEntry.Uid = -1L;
    metadataExtendedFileEntry.Gid = -1L;
    metadataExtendedFileEntry.AccessTime = new Timestamp(recordingTimeCalendar);
    metadataExtendedFileEntry.ModificationTime = new Timestamp(recordingTimeCalendar);
    metadataExtendedFileEntry.AttributeTime = new Timestamp(recordingTimeCalendar);
    metadataExtendedFileEntry.CreationTime = new Timestamp(recordingTimeCalendar);
    metadataExtendedFileEntry.Checkpoint = 1L;
    try {
      metadataExtendedFileEntry.ImplementationIdentifier.setIdentifier(applicationIdentifier);
    }
    catch (Exception myException) {
      throw new HandlerException(myException);
    } 
    metadataExtendedFileEntry.ImplementationIdentifier.IdentifierSuffix = applicationIdentifierSuffix;
    metadataExtendedFileEntry.ICBTag.Flags = 0;
    metadataExtendedFileEntry.ICBTag.PriorRecordedNumberofDirectEntries = 0L;
    metadataExtendedFileEntry.ICBTag.NumberofEntries = 1;
    metadataExtendedFileEntry.ICBTag.StrategyType = 4;
    long metadataFileLength = metadataPartitionEndingBlock - metadataPartitionStartingBlock;
    Short_ad metadataAllocationDescriptor = new Short_ad();
    metadataAllocationDescriptor.ExtentPosition = metadataPartitionStartingBlock - physicalPartitionStartingBlock;
    metadataAllocationDescriptor.ExtentLength = metadataFileLength * this.blockSize;
    metadataExtendedFileEntry.LogicalBlocksRecorded = metadataFileLength;
    metadataExtendedFileEntry.InformationLength = metadataFileLength * this.blockSize;
    metadataExtendedFileEntry.ObjectSize = metadataFileLength * this.blockSize;
    metadataExtendedFileEntry.AllocationDescriptors = metadataAllocationDescriptor.getBytes();
    metadataExtendedFileEntry.LengthofAllocationDescriptors = metadataExtendedFileEntry.AllocationDescriptors.length;
    metadataExtendedFileEntry.DescriptorTag.TagLocation = metadataFileLocation;
    metadataExtendedFileEntry.ICBTag.FileType = fileType;
    startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
    data((DataReference)new WordDataReference(266L));
    data((DataReference)new WordDataReference(metadataFileLocation));
    data((DataReference)new WordDataReference(this.tagSerialNumber));
    data((DataReference)new WordDataReference(this.descriptorVersion));
    data((DataReference)new ByteArrayDataReference(metadataExtendedFileEntry.getBytesWithoutDescriptorTag()));
    endElement();
  }
}
