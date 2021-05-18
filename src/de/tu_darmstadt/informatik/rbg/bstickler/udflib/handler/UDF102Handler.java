package de.tu_darmstadt.informatik.rbg.bstickler.udflib.handler;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.SabreUDFElement;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.AnchorVolumeDescriptorPointer;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.EntityID;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.Extend_ad;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.ExtendedFileEntry;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.FileEntry;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.FileIdentifierDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.FileSetDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.ImplementationUseVolumeDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.LogicalVolumeDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.LogicalVolumeIntegrityDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.Long_ad;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.PartitionDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.PartitionMapType1;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.PartitionMapType2;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.PrimaryVolumeDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.Short_ad;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.TerminatingDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.Timestamp;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.UnallocatedSpaceDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.VolumeRecognitionSequence;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.Permissions;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.ContentHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.Element;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.HandlerException;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.StructureHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.ByteArrayDataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.ChainingStreamHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.WordDataReference;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;
public class UDF102Handler extends ChainingStreamHandler {
  protected int blockSize = 2048;
  protected long maximumAllocationLength = 1073739776L;
  protected int tagSerialNumber = 1;
  protected byte[] udfVersionIdentifierSuffix = new byte[] { 2, 1 };
  protected int minimumUDFReadRevision = 258;
  protected int minimumUDFWriteRevision = 258;
  protected int maximumUDFWriteRevision = 258;
  protected int descriptorVersion = 2;
  protected Stack<Element> elementStack;
  protected Stack<DataReference> dataReferenceStack;
  public UDF102Handler(StructureHandler myStructureHandler, ContentHandler myContentHandler) {
    super(myStructureHandler, myContentHandler);
    this.elementStack = new Stack<Element>();
    this.dataReferenceStack = new Stack<DataReference>();
  }
  public void startDocument() throws HandlerException {
    super.startDocument();
  }
  public void endDocument() throws HandlerException {
    super.endDocument();
  }
  public void startElement(Element myElement) throws HandlerException {
    this.elementStack.push(myElement);
    super.startElement(myElement);
  }
  public void endElement() throws HandlerException {
    Element myElement = this.elementStack.pop();
    if (myElement.getId() == SabreUDFElement.UDFElementType.VolumeRecognitionSequence) {
      createAndPassVRS();
    }
    else if (myElement.getId() == SabreUDFElement.UDFElementType.AnchorVolumeDescriptorPointer) {
      createAndPassAVDP();
    }
    else if (myElement.getId() == SabreUDFElement.UDFElementType.PrimaryVolumeDescriptor) {
      createAndPassPVD();
    }
    else if (myElement.getId() == SabreUDFElement.UDFElementType.PartitionDescriptor) {
      createAndPassPD();
    }
    else if (myElement.getId() == SabreUDFElement.UDFElementType.LogicalVolumeDescriptor) {
      createAndPassLVD();
    }
    else if (myElement.getId() == SabreUDFElement.UDFElementType.UnallocatedSpaceDescriptor) {
      createAndPassUSD();
    }
    else if (myElement.getId() == SabreUDFElement.UDFElementType.ImplementationUseVolumeDescriptor) {
      createAndPassIUVD();
    }
    else if (myElement.getId() == SabreUDFElement.UDFElementType.TerminatingDescriptor) {
      createAndPassTD();
    }
    else if (myElement.getId() == SabreUDFElement.UDFElementType.LogicalVolumeIntegrityDescriptor) {
      createAndPassLVID();
    }
    else if (myElement.getId() == SabreUDFElement.UDFElementType.FileSetDescriptor) {
      createAndPassFSD();
    }
    else if (myElement.getId() == SabreUDFElement.UDFElementType.FileEntry) {
      createAndPassFE();
    }
    else if (myElement.getId() == SabreUDFElement.UDFElementType.MetadataFile) {
      createAndPassMetadataFile();
    } 
    super.endElement();
  }
  public void data(DataReference myDataReference) throws HandlerException {
    if (this.elementStack.size() > 0 && ((
      (Element)this.elementStack.peek()).getId() == SabreUDFElement.UDFElementType.AnchorVolumeDescriptorPointer || (
      (Element)this.elementStack.peek()).getId() == SabreUDFElement.UDFElementType.PrimaryVolumeDescriptor || (
      (Element)this.elementStack.peek()).getId() == SabreUDFElement.UDFElementType.PartitionDescriptor || (
      (Element)this.elementStack.peek()).getId() == SabreUDFElement.UDFElementType.LogicalVolumeDescriptor || (
      (Element)this.elementStack.peek()).getId() == SabreUDFElement.UDFElementType.UnallocatedSpaceDescriptor || (
      (Element)this.elementStack.peek()).getId() == SabreUDFElement.UDFElementType.ImplementationUseVolumeDescriptor || (
      (Element)this.elementStack.peek()).getId() == SabreUDFElement.UDFElementType.TerminatingDescriptor || (
      (Element)this.elementStack.peek()).getId() == SabreUDFElement.UDFElementType.LogicalVolumeIntegrityDescriptor || (
      (Element)this.elementStack.peek()).getId() == SabreUDFElement.UDFElementType.FileSetDescriptor || (
      (Element)this.elementStack.peek()).getId() == SabreUDFElement.UDFElementType.FileEntry || (
      (Element)this.elementStack.peek()).getId() == SabreUDFElement.UDFElementType.MetadataFile)) {
      this.dataReferenceStack.push(myDataReference);
    }
    else {
      super.data(myDataReference);
    } 
  }
  protected void createAndPassVRS() throws HandlerException {
    VolumeRecognitionSequence myVolumeRecognitionSequene = new VolumeRecognitionSequence(VolumeRecognitionSequence.NSRVersion.NSR02);
    isDataOver(true);
    isEndOver(true);
    super.data((DataReference)new ByteArrayDataReference(myVolumeRecognitionSequene.getBytes()));
  }
  protected void createAndPassAVDP() throws HandlerException {
    InputStream myInputStream = null;
    long tagLocation = 0L;
    long MVDSBlock = 0L;
    long RVDSBlock = 0L;
    try {
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      RVDSBlock = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      MVDSBlock = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      tagLocation = BinaryTools.readUInt32AsLong(myInputStream);
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
    AnchorVolumeDescriptorPointer myAnchorVolumeDescriptorPointer = new AnchorVolumeDescriptorPointer();
    myAnchorVolumeDescriptorPointer.MainVolumeDescriptorSequenceExtend.len = (16 * this.blockSize);
    myAnchorVolumeDescriptorPointer.MainVolumeDescriptorSequenceExtend.loc = MVDSBlock;
    myAnchorVolumeDescriptorPointer.ReserveVolumeDescriptorSequenceExtend.len = (16 * this.blockSize);
    myAnchorVolumeDescriptorPointer.ReserveVolumeDescriptorSequenceExtend.loc = RVDSBlock;
    super.startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
    super.data((DataReference)new WordDataReference(2L));
    super.data((DataReference)new WordDataReference(tagLocation));
    super.data((DataReference)new WordDataReference(this.tagSerialNumber));
    super.data((DataReference)new WordDataReference(this.descriptorVersion));
    super.data((DataReference)new ByteArrayDataReference(myAnchorVolumeDescriptorPointer.getBytesWithoutDescriptorTag()));
    super.endElement();
  }
  protected void createAndPassPVD() throws HandlerException {
    InputStream myInputStream = null;
    long tagLocation = 0L;
    long volumeDescriptorSequenceNumber = 0L;
    Calendar recordingTimeCalendar = Calendar.getInstance();
    String imageIdentifier = "";
    String applicationIdentifier = "";
    byte[] applicationIdentifierSuffix = new byte[0];
    try {
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
      myDataReference = this.dataReferenceStack.pop();
      myInputStream = myDataReference.createInputStream();
      imageIdentifier = new String(BinaryTools.readByteArray(myInputStream, (int)myDataReference.getLength()));
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      recordingTimeCalendar.setTimeInMillis(BinaryTools.readUInt64AsLong(myInputStream));
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      volumeDescriptorSequenceNumber = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      tagLocation = BinaryTools.readUInt32AsLong(myInputStream);
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
    PrimaryVolumeDescriptor myPrimaryVolumeDescriptor = new PrimaryVolumeDescriptor();
    myPrimaryVolumeDescriptor.VolumeDescriptorSequenceNumber = volumeDescriptorSequenceNumber;
    myPrimaryVolumeDescriptor.PrimaryVolumeDescriptorNumber = 0L;
    myPrimaryVolumeDescriptor.VolumeSequenceNumber = 1;
    myPrimaryVolumeDescriptor.MaximumVolumeSequenceNumber = 1;
    myPrimaryVolumeDescriptor.InterchangeLevel = 2;
    myPrimaryVolumeDescriptor.MaximumInterchangeLevel = 3;
    myPrimaryVolumeDescriptor.CharacterSetList = 1L;
    myPrimaryVolumeDescriptor.MaximumCharacterSetList = 1L;
    String volumeSetIdentifier = String.valueOf(Long.toHexString(recordingTimeCalendar.getTimeInMillis())) + " " + imageIdentifier;
    try {
      myPrimaryVolumeDescriptor.setVolumeIdentifier(imageIdentifier);
      myPrimaryVolumeDescriptor.setVolumeSetIdentifier(volumeSetIdentifier);
      myPrimaryVolumeDescriptor.ApplicationIdentifier.setIdentifier(applicationIdentifier);
      myPrimaryVolumeDescriptor.ImplementationIdentifier.setIdentifier(applicationIdentifier);
    }
    catch (Exception myException) {
      throw new HandlerException(myException);
    } 
    myPrimaryVolumeDescriptor.ApplicationIdentifier.IdentifierSuffix = applicationIdentifierSuffix;
    myPrimaryVolumeDescriptor.RecordingDateandTime.set(recordingTimeCalendar);
    myPrimaryVolumeDescriptor.PredecessorVolumeDescriptorSequenceLocation = 0L;
    myPrimaryVolumeDescriptor.Flags = 1;
    super.startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
    super.data((DataReference)new WordDataReference(1L));
    super.data((DataReference)new WordDataReference(tagLocation));
    super.data((DataReference)new WordDataReference(this.tagSerialNumber));
    super.data((DataReference)new WordDataReference(this.descriptorVersion));
    super.data((DataReference)new ByteArrayDataReference(myPrimaryVolumeDescriptor.getBytesWithoutDescriptorTag()));
    super.endElement();
  }
  protected void createAndPassPD() throws HandlerException {
    InputStream myInputStream = null;
    long tagLocation = 0L;
    long volumeDescriptorSequenceNumber = 0L;
    long partitionStartingBlock = 0L;
    long partitionEndingBlock = 0L;
    String applicationIdentifier = "";
    byte[] applicationIdentifierSuffix = new byte[0];
    try {
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
      partitionEndingBlock = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      partitionStartingBlock = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      volumeDescriptorSequenceNumber = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      tagLocation = BinaryTools.readUInt32AsLong(myInputStream);
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
    PartitionDescriptor myPartitionDescriptor = new PartitionDescriptor();
    myPartitionDescriptor.VolumeDescriptorSequenceNumber = volumeDescriptorSequenceNumber;
    myPartitionDescriptor.PartitionFlags = 1;
    myPartitionDescriptor.PartitionNumber = 0;
    try {
      myPartitionDescriptor.PartitionContents.setIdentifier("+NSR02");
      myPartitionDescriptor.ImplementationIdentifier.setIdentifier(applicationIdentifier);
    }
    catch (Exception myException) {
      throw new HandlerException(myException);
    } 
    myPartitionDescriptor.AccessType = 1L;
    myPartitionDescriptor.PartitonStartingLocation = partitionStartingBlock;
    myPartitionDescriptor.PartitionLength = partitionEndingBlock - partitionStartingBlock;
    myPartitionDescriptor.ImplementationIdentifier.IdentifierSuffix = applicationIdentifierSuffix;
    super.startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
    super.data((DataReference)new WordDataReference(5L));
    super.data((DataReference)new WordDataReference(tagLocation));
    super.data((DataReference)new WordDataReference(this.tagSerialNumber));
    super.data((DataReference)new WordDataReference(this.descriptorVersion));
    super.data((DataReference)new ByteArrayDataReference(myPartitionDescriptor.getBytesWithoutDescriptorTag()));
    super.endElement();
  }
  protected void createAndPassLVD() throws HandlerException {
    String imageIdentifier;
    InputStream myInputStream = null;
    long tagLocation = 0L;
    long volumeDescriptorSequenceNumber = 0L;
    long fileSetDescriptorLocation = 0L;
    int fileSetDescriptorPartition = 0;
    long mirrorMetadataFileLocation = 0L;
    long mainMetadataFileLocation = 0L;
    int metadataAlignmentUnitSize = 0;
    int metadataAllocationUnitSize = 0;
    long logicalVolumeIntegritySequenceStartingBlock = 0L;
    long logicalVolumeIntegritySequenceEndingBlock = 0L;
    String applicationIdentifier = "";
    byte[] applicationIdentifierSuffix = new byte[0];
    try {
      DataReference myDataReference = this.dataReferenceStack.pop();
      myInputStream = myDataReference.createInputStream();
      imageIdentifier = new String(BinaryTools.readByteArray(myInputStream, (int)myDataReference.getLength()));
      myInputStream.close();
      myInputStream = null;
      myDataReference = this.dataReferenceStack.pop();
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
      fileSetDescriptorLocation = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      fileSetDescriptorPartition = (int)BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      mirrorMetadataFileLocation = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      mainMetadataFileLocation = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      metadataAlignmentUnitSize = (int)BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      metadataAllocationUnitSize = (int)BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      logicalVolumeIntegritySequenceEndingBlock = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      logicalVolumeIntegritySequenceStartingBlock = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      volumeDescriptorSequenceNumber = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      tagLocation = BinaryTools.readUInt32AsLong(myInputStream);
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
    LogicalVolumeDescriptor myLogicalVolumeDescriptor = new LogicalVolumeDescriptor();
    try {
      myLogicalVolumeDescriptor.VolumeDescriptorSequenceNumber = volumeDescriptorSequenceNumber;
      myLogicalVolumeDescriptor.setLogicalVolumeIdentifier(imageIdentifier);
      myLogicalVolumeDescriptor.LogicalBlockSize = this.blockSize;
      myLogicalVolumeDescriptor.DomainIdentifier.setIdentifier("*OSTA UDF Compliant");
      myLogicalVolumeDescriptor.DomainIdentifier.IdentifierSuffix = this.udfVersionIdentifierSuffix;
      myLogicalVolumeDescriptor.LogicalVolumeContentsUse.ExtentLength = this.blockSize;
      myLogicalVolumeDescriptor.LogicalVolumeContentsUse.ExtentLocation.part_num = fileSetDescriptorPartition;
      myLogicalVolumeDescriptor.LogicalVolumeContentsUse.ExtentLocation.lb_num = fileSetDescriptorLocation;
      myLogicalVolumeDescriptor.ImplementationIdentifier.setIdentifier(applicationIdentifier);
      myLogicalVolumeDescriptor.ImplementationIdentifier.IdentifierSuffix = applicationIdentifierSuffix;
      PartitionMapType1 myPartitionMapType1 = new PartitionMapType1();
      byte[] myPartitionMapType1Bytes = myPartitionMapType1.getBytes();
      if (mainMetadataFileLocation > 0L)
      {
        PartitionMapType2 myPartitionMapType2 = new PartitionMapType2();
        EntityID partitionTypeIdentifier = new EntityID();
        partitionTypeIdentifier.setIdentifier("*UDF Metadata Partition");
        partitionTypeIdentifier.IdentifierSuffix = this.udfVersionIdentifierSuffix;
        myPartitionMapType2.setupMetadataPartitionMap(partitionTypeIdentifier, 1, 0, mainMetadataFileLocation, mirrorMetadataFileLocation, -1L, metadataAllocationUnitSize, metadataAlignmentUnitSize, (byte)0);
        byte[] myPartitionMapType2Bytes = myPartitionMapType2.getBytes();
        myLogicalVolumeDescriptor.NumberofPartitionMaps = 2L;
        myLogicalVolumeDescriptor.PartitionMaps = new byte[myPartitionMapType1Bytes.length + myPartitionMapType2Bytes.length];
        System.arraycopy(myPartitionMapType1Bytes, 0, myLogicalVolumeDescriptor.PartitionMaps, 0, myPartitionMapType1Bytes.length);
        System.arraycopy(myPartitionMapType2Bytes, 0, myLogicalVolumeDescriptor.PartitionMaps, 6, myPartitionMapType2Bytes.length);
      }
      else
      {
        myLogicalVolumeDescriptor.NumberofPartitionMaps = 1L;
        myLogicalVolumeDescriptor.PartitionMaps = myPartitionMapType1Bytes;
      }
    } catch (Exception myException) {
      throw new HandlerException(myException);
    } 
    myLogicalVolumeDescriptor.MapTableLength = myLogicalVolumeDescriptor.PartitionMaps.length;
    myLogicalVolumeDescriptor.IntegritySequenceExtent.loc = logicalVolumeIntegritySequenceStartingBlock;
    myLogicalVolumeDescriptor.IntegritySequenceExtent.len = (logicalVolumeIntegritySequenceEndingBlock - logicalVolumeIntegritySequenceStartingBlock) * this.blockSize;
    super.startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
    super.data((DataReference)new WordDataReference(6L));
    super.data((DataReference)new WordDataReference(tagLocation));
    super.data((DataReference)new WordDataReference(this.tagSerialNumber));
    super.data((DataReference)new WordDataReference(this.descriptorVersion));
    super.data((DataReference)new ByteArrayDataReference(myLogicalVolumeDescriptor.getBytesWithoutDescriptorTag()));
    super.endElement();
  }
  protected void createAndPassUSD() throws HandlerException {
    InputStream myInputStream = null;
    long tagLocation = 0L;
    long volumeDescriptorSequenceNumber = 0L;
    long unallocatedSpaceStartBlock = 0L;
    long unallocatedSpaceEndBlock = 0L;
    try {
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      unallocatedSpaceEndBlock = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      unallocatedSpaceStartBlock = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      volumeDescriptorSequenceNumber = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      tagLocation = BinaryTools.readUInt32AsLong(myInputStream);
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
    UnallocatedSpaceDescriptor myUnallocatedSpaceDescriptor = new UnallocatedSpaceDescriptor();
    myUnallocatedSpaceDescriptor.VolumeDescriptorSequenceNumber = volumeDescriptorSequenceNumber;
    myUnallocatedSpaceDescriptor.NumberofAllocationDescriptors = 1L;
    myUnallocatedSpaceDescriptor.AllocationDescriptors = new Extend_ad[1];
    myUnallocatedSpaceDescriptor.AllocationDescriptors[0] = new Extend_ad();
    (myUnallocatedSpaceDescriptor.AllocationDescriptors[0]).loc = unallocatedSpaceStartBlock;
    (myUnallocatedSpaceDescriptor.AllocationDescriptors[0]).len = (unallocatedSpaceEndBlock - unallocatedSpaceStartBlock) * this.blockSize;
    super.startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
    super.data((DataReference)new WordDataReference(7L));
    super.data((DataReference)new WordDataReference(tagLocation));
    super.data((DataReference)new WordDataReference(this.tagSerialNumber));
    super.data((DataReference)new WordDataReference(this.descriptorVersion));
    super.data((DataReference)new ByteArrayDataReference(myUnallocatedSpaceDescriptor.getBytesWithoutDescriptorTag()));
    super.endElement();
  }
  protected void createAndPassIUVD() throws HandlerException {
    String imageIdentifier;
    InputStream myInputStream = null;
    long tagLocation = 0L;
    long volumeDescriptorSequenceNumber = 0L;
    String applicationIdentifier = "";
    byte[] applicationIdentifierSuffix = new byte[0];
    try {
      DataReference myDataReference = this.dataReferenceStack.pop();
      myInputStream = myDataReference.createInputStream();
      imageIdentifier = new String(BinaryTools.readByteArray(myInputStream, (int)myDataReference.getLength()));
      myInputStream.close();
      myInputStream = null;
      myDataReference = this.dataReferenceStack.pop();
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
      volumeDescriptorSequenceNumber = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      tagLocation = BinaryTools.readUInt32AsLong(myInputStream);
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
    ImplementationUseVolumeDescriptor myImplementationUseVolumeDescriptor = new ImplementationUseVolumeDescriptor();
    myImplementationUseVolumeDescriptor.VolumeDescriptorSequenceNumber = volumeDescriptorSequenceNumber;
    myImplementationUseVolumeDescriptor.ImplementationIdentifier.IdentifierSuffix = this.udfVersionIdentifierSuffix;
    try {
      myImplementationUseVolumeDescriptor.ImplementationIdentifier.setIdentifier("*UDF LV Info");
      myImplementationUseVolumeDescriptor.ImplementationUse.ImplementationID.setIdentifier(applicationIdentifier);
      myImplementationUseVolumeDescriptor.ImplementationUse.setLogicalVolumeIdentifier(imageIdentifier);
    }
    catch (Exception myException) {
      throw new HandlerException(myException);
    } 
    myImplementationUseVolumeDescriptor.ImplementationUse.ImplementationID.IdentifierSuffix = applicationIdentifierSuffix;
    super.startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
    super.data((DataReference)new WordDataReference(4L));
    super.data((DataReference)new WordDataReference(tagLocation));
    super.data((DataReference)new WordDataReference(this.tagSerialNumber));
    super.data((DataReference)new WordDataReference(this.descriptorVersion));
    super.data((DataReference)new ByteArrayDataReference(myImplementationUseVolumeDescriptor.getBytesWithoutDescriptorTag()));
    super.endElement();
  }
  protected void createAndPassTD() throws HandlerException {
    InputStream myInputStream = null;
    long tagLocation = 0L;
    try {
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      tagLocation = BinaryTools.readUInt32AsLong(myInputStream);
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
    TerminatingDescriptor myTerminatingDescriptor = new TerminatingDescriptor();
    super.startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
    super.data((DataReference)new WordDataReference(8L));
    super.data((DataReference)new WordDataReference(tagLocation));
    super.data((DataReference)new WordDataReference(this.tagSerialNumber));
    super.data((DataReference)new WordDataReference(this.descriptorVersion));
    super.data((DataReference)new ByteArrayDataReference(myTerminatingDescriptor.getBytesWithoutDescriptorTag()));
    super.endElement();
  }
  protected void createAndPassLVID() throws HandlerException {
    InputStream myInputStream = null;
    long tagLocation = 0L;
    long numberOfFiles = 0L;
    long numberOfDirectories = 0L;
    Calendar recordingTimeCalendar = Calendar.getInstance();
    String applicationIdentifier = "";
    byte[] applicationIdentifierSuffix = new byte[0];
    long numberOfPartitions = 0L;
    long[] sizeTable = new long[0];
    long[] freespaceTable = new long[0];
    long nextUniqueId = 0L;
    try {
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      nextUniqueId = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      numberOfPartitions = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      sizeTable = new long[(int)numberOfPartitions];
      freespaceTable = new long[(int)numberOfPartitions];
      for (int i = (int)numberOfPartitions - 1; i >= 0; i--) {
        myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
        freespaceTable[i] = BinaryTools.readUInt32AsLong(myInputStream);
        myInputStream.close();
        myInputStream = null;
        myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
        sizeTable[i] = BinaryTools.readUInt32AsLong(myInputStream);
        myInputStream.close();
        myInputStream = null;
      } 
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
      numberOfDirectories = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      numberOfFiles = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      long debug = BinaryTools.readUInt64AsLong(myInputStream);
      recordingTimeCalendar.setTimeInMillis(debug);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      tagLocation = BinaryTools.readUInt32AsLong(myInputStream);
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
    LogicalVolumeIntegrityDescriptor myLogicalVolumeIntegrityDescriptor = new LogicalVolumeIntegrityDescriptor();
    myLogicalVolumeIntegrityDescriptor.RecordingDateAndTime.set(recordingTimeCalendar);
    myLogicalVolumeIntegrityDescriptor.IntegrityType = 1L;
    myLogicalVolumeIntegrityDescriptor.NumberOfPartitions = sizeTable.length;
    myLogicalVolumeIntegrityDescriptor.FreeSpaceTable = freespaceTable;
    myLogicalVolumeIntegrityDescriptor.SizeTable = sizeTable;
    myLogicalVolumeIntegrityDescriptor.LogicalVolumeContensUse.UniqueID = nextUniqueId;
    myLogicalVolumeIntegrityDescriptor.LengthOfImplementationUse = 46L;
    EntityID implementationId = new EntityID();
    try {
      implementationId.setIdentifier(applicationIdentifier);
      implementationId.IdentifierSuffix = applicationIdentifierSuffix;
    }
    catch (Exception myException) {
      throw new HandlerException(myException);
    } 
    myLogicalVolumeIntegrityDescriptor.setImplementationUse(implementationId, numberOfFiles, numberOfDirectories, this.minimumUDFReadRevision, this.minimumUDFWriteRevision, this.maximumUDFWriteRevision);
    super.startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
    super.data((DataReference)new WordDataReference(9L));
    super.data((DataReference)new WordDataReference(tagLocation));
    super.data((DataReference)new WordDataReference(this.tagSerialNumber));
    super.data((DataReference)new WordDataReference(this.descriptorVersion));
    super.data((DataReference)new ByteArrayDataReference(myLogicalVolumeIntegrityDescriptor.getBytesWithoutDescriptorTag()));
    super.endElement();
  }
  protected void createAndPassFSD() throws HandlerException {
    InputStream myInputStream = null;
    long tagLocation = 0L;
    long rootDirectoryLocation = 0L;
    int partitionToStoreMetadataOn = 0;
    Calendar recordingTimeCalendar = Calendar.getInstance();
    String imageIdentifier = "";
    try {
      DataReference myDataReference = this.dataReferenceStack.pop();
      myInputStream = myDataReference.createInputStream();
      imageIdentifier = new String(BinaryTools.readByteArray(myInputStream, (int)myDataReference.getLength()));
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      partitionToStoreMetadataOn = (int)BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      rootDirectoryLocation = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      recordingTimeCalendar.setTimeInMillis(BinaryTools.readUInt64AsLong(myInputStream));
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      tagLocation = BinaryTools.readUInt32AsLong(myInputStream);
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
    FileSetDescriptor myFilesetDescriptor = new FileSetDescriptor();
    myFilesetDescriptor.RecordingDateandTime.set(recordingTimeCalendar);
    myFilesetDescriptor.InterchangeLevel = 3;
    myFilesetDescriptor.MaximumInterchangeLevel = 3;
    myFilesetDescriptor.CharacterSetList = 1L;
    myFilesetDescriptor.MaximumCharacterSetList = 1L;
    myFilesetDescriptor.FileSetNumber = 0L;
    myFilesetDescriptor.FileSetDescriptorNumber = 0L;
    myFilesetDescriptor.setLogicalVolumeIdentifier(imageIdentifier);
    myFilesetDescriptor.setFileSetIdentifier(imageIdentifier);
    myFilesetDescriptor.RootDirectoryICB.ExtentLength = this.blockSize;
    myFilesetDescriptor.RootDirectoryICB.ExtentLocation.part_num = partitionToStoreMetadataOn;
    myFilesetDescriptor.RootDirectoryICB.ExtentLocation.lb_num = rootDirectoryLocation;
    try {
      myFilesetDescriptor.DomainIdentifier.setIdentifier("*OSTA UDF Compliant");
    }
    catch (Exception myException) {
      throw new HandlerException(myException);
    } 
    myFilesetDescriptor.DomainIdentifier.IdentifierSuffix = this.udfVersionIdentifierSuffix;
    super.startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
    super.data((DataReference)new WordDataReference(256L));
    super.data((DataReference)new WordDataReference(tagLocation));
    super.data((DataReference)new WordDataReference(this.tagSerialNumber));
    super.data((DataReference)new WordDataReference(this.descriptorVersion));
    super.data((DataReference)new ByteArrayDataReference(myFilesetDescriptor.getBytesWithoutDescriptorTag()));
    super.endElement();
  }
  protected void createAndPassFE() throws HandlerException {
    InputStream myInputStream = null;
    long tagLocation = 0L;
    int fileLinkCount = 0;
    Calendar accessTime = Calendar.getInstance();
    Calendar modificationTime = Calendar.getInstance();
    Calendar attributeTime = Calendar.getInstance();
    Calendar creationTime = Calendar.getInstance();
    long uniqueId = 0L;
    String applicationIdentifier = "";
    byte[] applicationIdentifierSuffix = new byte[0];
    int fileType = 0;
    try {
      this.dataReferenceStack.pop();
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      fileType = (int)BinaryTools.readUInt32AsLong(myInputStream);
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
      uniqueId = BinaryTools.readUInt64AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      creationTime.setTimeInMillis(BinaryTools.readUInt64AsLong(myInputStream));
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      attributeTime.setTimeInMillis(BinaryTools.readUInt64AsLong(myInputStream));
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      modificationTime.setTimeInMillis(BinaryTools.readUInt64AsLong(myInputStream));
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      accessTime.setTimeInMillis(BinaryTools.readUInt64AsLong(myInputStream));
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      fileLinkCount = (int)BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      tagLocation = BinaryTools.readUInt32AsLong(myInputStream);
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
    FileEntry myFileEntry = new FileEntry();
    myFileEntry.DescriptorTag.TagSerialNumber = this.tagSerialNumber;
    myFileEntry.DescriptorTag.DescriptorVersion = this.descriptorVersion;
    myFileEntry.DescriptorTag.TagLocation = tagLocation;
    myFileEntry.Uid = -1L;
    myFileEntry.Gid = -1L;
    myFileEntry.Permissions = (Permissions.OTHER_Read | Permissions.GROUP_Read | Permissions.OWNER_Read);
    myFileEntry.FileLinkCount = fileLinkCount;
    myFileEntry.RecordFormat = 0;
    myFileEntry.RecordDisplayAttributes = 0;
    myFileEntry.RecordLength = 0L;
    myFileEntry.AccessTime = new Timestamp(accessTime);
    myFileEntry.ModificationTime = new Timestamp(modificationTime);
    myFileEntry.AttributeTime = new Timestamp(attributeTime);
    myFileEntry.Checkpoint = 1L;
    try {
      myFileEntry.ImplementationIdentifier.setIdentifier(applicationIdentifier);
    }
    catch (Exception myException) {
      throw new HandlerException(myException);
    } 
    myFileEntry.ImplementationIdentifier.IdentifierSuffix = applicationIdentifierSuffix;
    myFileEntry.ICBTag.PriorRecordedNumberofDirectEntries = 0L;
    myFileEntry.ICBTag.NumberofEntries = 1;
    myFileEntry.ICBTag.StrategyType = 4;
    myFileEntry.UniqueID = uniqueId;
    if (fileType == 0) {
      myFileEntry.ICBTag.FileType = 5;
      createAndPassNormalFE(myFileEntry);
    }
    else if (fileType == 1) {
      myFileEntry.ICBTag.FileType = 4;
      createAndPassDirectoryFE(myFileEntry);
    } 
  }
  private void createAndPassNormalFE(FileEntry myFileEntry) throws HandlerException {
    InputStream myInputStream = null;
    long fileSize = 0L;
    long dataLocation = 0L;
    byte[] fileData = new byte[0];
    try {
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      dataLocation = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      DataReference myDataReference = this.dataReferenceStack.pop();
      fileSize = myDataReference.getLength();
      myInputStream = myDataReference.createInputStream();
      if (fileSize <= (this.blockSize - ExtendedFileEntry.fixedPartLength))
      {
        fileData = BinaryTools.readByteArray(myInputStream, (int)fileSize);
      }
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
    myFileEntry.ICBTag.FileType = 5;
    myFileEntry.InformationLength = fileSize;
    if (fileSize <= (this.blockSize - ExtendedFileEntry.fixedPartLength)) {
      myFileEntry.ICBTag.Flags = 3;
      myFileEntry.LogicalBlocksRecorded = 0L;
      myFileEntry.LengthofAllocationDescriptors = fileSize;
      myFileEntry.AllocationDescriptors = fileData;
    }
    else {
      myFileEntry.ICBTag.Flags = 1;
      myFileEntry.LogicalBlocksRecorded = fileSize / this.blockSize;
      if (fileSize % this.blockSize != 0L)
      {
        myFileEntry.LogicalBlocksRecorded++;
      }
      ArrayList<Long_ad> allocationDescriptors = new ArrayList<Long_ad>();
      long restFileSize = fileSize;
      long currentExtentPosition = dataLocation;
      while (restFileSize > 0L) {
        Long_ad allocationDescriptor = new Long_ad();
        if (restFileSize < this.maximumAllocationLength) {
          allocationDescriptor.ExtentLength = restFileSize;
        }
        else {
          allocationDescriptor.ExtentLength = this.maximumAllocationLength;
        } 
        allocationDescriptor.ExtentLocation.part_num = 0;
        allocationDescriptor.ExtentLocation.lb_num = currentExtentPosition;
        allocationDescriptors.add(allocationDescriptor);
        restFileSize -= this.maximumAllocationLength;
        currentExtentPosition += this.maximumAllocationLength / this.blockSize;
        if (this.maximumAllocationLength % this.blockSize != 0L)
        {
          currentExtentPosition++;
        }
      } 
      byte[] allocationDescriptorBytes = new byte[allocationDescriptors.size() * 16];
      int allocationDescriptorBytesPosition = 0;
      for (int i = 0; i < allocationDescriptors.size(); i++) {
        byte[] singleAllocationDescriptorBytes = ((Long_ad)allocationDescriptors.get(i)).getBytes();
        System.arraycopy(singleAllocationDescriptorBytes, 0, allocationDescriptorBytes, allocationDescriptorBytesPosition, singleAllocationDescriptorBytes.length);
        allocationDescriptorBytesPosition += singleAllocationDescriptorBytes.length;
      } 
      myFileEntry.AllocationDescriptors = allocationDescriptorBytes;
      myFileEntry.LengthofAllocationDescriptors = allocationDescriptorBytes.length;
    } 
    super.startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
    super.data((DataReference)new WordDataReference(261L));
    super.data((DataReference)new WordDataReference(myFileEntry.DescriptorTag.TagLocation));
    super.data((DataReference)new WordDataReference(this.tagSerialNumber));
    super.data((DataReference)new WordDataReference(this.descriptorVersion));
    super.data((DataReference)new ByteArrayDataReference(myFileEntry.getBytesWithoutDescriptorTag()));
    super.endElement();
  }
  private void createAndPassDirectoryFE(FileEntry myFileEntry) throws HandlerException {
    InputStream myInputStream = null;
    ArrayList<FileIdentifierDescriptor> childFileIdentifierDescriptors = new ArrayList<FileIdentifierDescriptor>();
    long dataLocation = 0L;
    int partitionToStoreMetadataOn = 0;
    try {
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      dataLocation = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      partitionToStoreMetadataOn = (int)BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      long parentDirectoryLocation = BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      long parentDirectoryUniqueId = BinaryTools.readUInt64AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      FileIdentifierDescriptor parentDirectoryFileIdentifierDescriptor = new FileIdentifierDescriptor();
      parentDirectoryFileIdentifierDescriptor.DescriptorTag.TagLocation = myFileEntry.DescriptorTag.TagLocation;
      parentDirectoryFileIdentifierDescriptor.DescriptorTag.TagSerialNumber = this.tagSerialNumber;
      parentDirectoryFileIdentifierDescriptor.DescriptorTag.DescriptorVersion = this.descriptorVersion;
      parentDirectoryFileIdentifierDescriptor.ICB.ExtentLength = this.blockSize;
      parentDirectoryFileIdentifierDescriptor.ICB.ExtentLocation.part_num = partitionToStoreMetadataOn;
      parentDirectoryFileIdentifierDescriptor.FileVersionNumber = 1;
      parentDirectoryFileIdentifierDescriptor.FileCharacteristics = 10;
      parentDirectoryFileIdentifierDescriptor.ICB.ExtentLocation.lb_num = parentDirectoryLocation;
      parentDirectoryFileIdentifierDescriptor.ICB.implementationUse = new byte[6];
      parentDirectoryFileIdentifierDescriptor.ICB.implementationUse[2] = (byte)(int)(parentDirectoryUniqueId & 0xFFL);
      parentDirectoryFileIdentifierDescriptor.ICB.implementationUse[3] = (byte)(int)(parentDirectoryUniqueId >> 8L & 0xFFL);
      parentDirectoryFileIdentifierDescriptor.ICB.implementationUse[4] = (byte)(int)(parentDirectoryUniqueId >> 16L & 0xFFL);
      parentDirectoryFileIdentifierDescriptor.ICB.implementationUse[5] = (byte)(int)(parentDirectoryUniqueId >> 32L & 0xFFL);
      childFileIdentifierDescriptors.add(parentDirectoryFileIdentifierDescriptor);
      myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
      int numberOfChildFiles = (int)BinaryTools.readUInt32AsLong(myInputStream);
      myInputStream.close();
      myInputStream = null;
      for (int j = 0; j < numberOfChildFiles; j++)
      {
        myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
        int childFileType = (int)BinaryTools.readUInt32AsLong(myInputStream);
        myInputStream.close();
        myInputStream = null;
        DataReference myDataReference = this.dataReferenceStack.pop();
        myInputStream = myDataReference.createInputStream();
        String childFileIdentifier = new String(BinaryTools.readByteArray(myInputStream, (int)myDataReference.getLength()));
        myInputStream.close();
        myInputStream = null;
        myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
        long childFileLocation = BinaryTools.readUInt32AsLong(myInputStream);
        myInputStream.close();
        myInputStream = null;
        myInputStream = ((DataReference)this.dataReferenceStack.pop()).createInputStream();
        long childFileUniqueId = BinaryTools.readUInt64AsLong(myInputStream);
        myInputStream.close();
        myInputStream = null;
        FileIdentifierDescriptor childFileIdentifierDescriptor = new FileIdentifierDescriptor();
        childFileIdentifierDescriptor.DescriptorTag.TagLocation = myFileEntry.DescriptorTag.TagLocation;
        childFileIdentifierDescriptor.DescriptorTag.TagSerialNumber = this.tagSerialNumber;
        childFileIdentifierDescriptor.DescriptorTag.DescriptorVersion = this.descriptorVersion;
        childFileIdentifierDescriptor.ICB.ExtentLength = this.blockSize;
        childFileIdentifierDescriptor.ICB.ExtentLocation.lb_num = childFileLocation;
        childFileIdentifierDescriptor.ICB.ExtentLocation.part_num = partitionToStoreMetadataOn;
        childFileIdentifierDescriptor.ICB.implementationUse = new byte[6];
        childFileIdentifierDescriptor.ICB.implementationUse[2] = (byte)(int)(childFileUniqueId & 0xFFL);
        childFileIdentifierDescriptor.ICB.implementationUse[3] = (byte)(int)(childFileUniqueId >> 8L & 0xFFL);
        childFileIdentifierDescriptor.ICB.implementationUse[4] = (byte)(int)(childFileUniqueId >> 16L & 0xFFL);
        childFileIdentifierDescriptor.ICB.implementationUse[5] = (byte)(int)(childFileUniqueId >> 32L & 0xFFL);
        childFileIdentifierDescriptor.FileVersionNumber = 1;
        try {
          childFileIdentifierDescriptor.setFileIdentifier(childFileIdentifier);
        }
        catch (Exception myException) {
          throw new HandlerException(myException);
        } 
        if (childFileType == 1)
        {
          childFileIdentifierDescriptor.FileCharacteristics = 2;
        }
        childFileIdentifierDescriptors.add(childFileIdentifierDescriptor);
      }
    } catch (IOException myIOException) {
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
    int directoryFileDataLength = 0;
    for (int i = 0; i < childFileIdentifierDescriptors.size(); i++)
    {
      directoryFileDataLength += ((FileIdentifierDescriptor)childFileIdentifierDescriptors.get(i)).getLength();
    }
    myFileEntry.InformationLength = directoryFileDataLength;
    if (directoryFileDataLength <= this.blockSize - ExtendedFileEntry.fixedPartLength) {
      myFileEntry.ICBTag.Flags = 3;
      myFileEntry.LogicalBlocksRecorded = 0L;
      myFileEntry.LengthofAllocationDescriptors = directoryFileDataLength;
      myFileEntry.AllocationDescriptors = new byte[directoryFileDataLength];
      int pos = 0;
      for (int j = 0; j < childFileIdentifierDescriptors.size(); j++) {
        byte[] childFileIdentifierDescriptorBytes = ((FileIdentifierDescriptor)childFileIdentifierDescriptors.get(j)).getBytes();
        System.arraycopy(childFileIdentifierDescriptorBytes, 0, myFileEntry.AllocationDescriptors, pos, childFileIdentifierDescriptorBytes.length);
        pos += childFileIdentifierDescriptorBytes.length;
      } 
      super.startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
      super.data((DataReference)new WordDataReference(261L));
      super.data((DataReference)new WordDataReference(myFileEntry.DescriptorTag.TagLocation));
      super.data((DataReference)new WordDataReference(this.tagSerialNumber));
      super.data((DataReference)new WordDataReference(this.descriptorVersion));
      super.data((DataReference)new ByteArrayDataReference(myFileEntry.getBytesWithoutDescriptorTag()));
      super.endElement();
    }
    else {
      myFileEntry.ICBTag.Flags = 0;
      myFileEntry.LogicalBlocksRecorded = (directoryFileDataLength / this.blockSize);
      if (directoryFileDataLength % this.blockSize != 0)
      {
        myFileEntry.LogicalBlocksRecorded++;
      }
      Short_ad allocationDescriptor = new Short_ad();
      allocationDescriptor.ExtentLength = directoryFileDataLength;
      allocationDescriptor.ExtentPosition = dataLocation;
      if (directoryFileDataLength % this.blockSize != 0)
      {
        directoryFileDataLength += this.blockSize - directoryFileDataLength % this.blockSize;
      }
      byte[] data = new byte[directoryFileDataLength];
      long currentRealPosition = dataLocation * this.blockSize;
      int pos = 0;
      for (int j = 0; j < childFileIdentifierDescriptors.size(); j++) {
        long tagLocationBlock = currentRealPosition / this.blockSize;
        FileIdentifierDescriptor childFileIdentifierDescriptor = childFileIdentifierDescriptors.get(j);
        childFileIdentifierDescriptor.DescriptorTag.TagLocation = tagLocationBlock;
        byte[] childFileIdentifierDescriptorBytes = ((FileIdentifierDescriptor)childFileIdentifierDescriptors.get(j)).getBytes();
        System.arraycopy(childFileIdentifierDescriptorBytes, 0, data, pos, childFileIdentifierDescriptorBytes.length);
        pos += childFileIdentifierDescriptorBytes.length;
        currentRealPosition += childFileIdentifierDescriptorBytes.length;
      } 
      myFileEntry.AllocationDescriptors = allocationDescriptor.getBytes();
      myFileEntry.LengthofAllocationDescriptors = myFileEntry.AllocationDescriptors.length;
      super.startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
      super.data((DataReference)new WordDataReference(261L));
      super.data((DataReference)new WordDataReference(myFileEntry.DescriptorTag.TagLocation));
      super.data((DataReference)new WordDataReference(this.tagSerialNumber));
      super.data((DataReference)new WordDataReference(this.descriptorVersion));
      super.data((DataReference)new ByteArrayDataReference(myFileEntry.getBytesWithoutDescriptorTag()));
      super.endElement();
      super.data((DataReference)new ByteArrayDataReference(data));
    } 
  }
  protected void createAndPassMetadataFile() throws HandlerException {
    InputStream myInputStream = null;
    try {
      this.dataReferenceStack.pop();
      this.dataReferenceStack.pop();
      this.dataReferenceStack.pop();
      this.dataReferenceStack.pop();
      this.dataReferenceStack.pop();
      this.dataReferenceStack.pop();
      this.dataReferenceStack.pop();
      this.dataReferenceStack.pop();
    }
    catch (Exception myException) {
      throw new HandlerException(myException);
    }
    finally {
      if (myInputStream != null)
        try {
          myInputStream.close();
        }
        catch (IOException iOException) {} 
    } 
  }
}
