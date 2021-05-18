package de.tu_darmstadt.informatik.rbg.bstickler.udflib.handler;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.SabreUDFElement;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.ExtendedFileEntry;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.FileEntry;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.FileIdentifierDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.Long_ad;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.PartitionDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.Short_ad;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.Timestamp;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.VolumeRecognitionSequence;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.Permissions;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.ContentHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.Element;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.HandlerException;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.StructureHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.ByteArrayDataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.WordDataReference;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
public class UDF201Handler
  extends UDF102Handler {
  public UDF201Handler(StructureHandler myStructureHandler, ContentHandler myContentHandler) {
    super(myStructureHandler, myContentHandler);
    this.udfVersionIdentifierSuffix = new byte[] { 1, 2 };
    this.minimumUDFReadRevision = 513;
    this.minimumUDFWriteRevision = 513;
    this.maximumUDFWriteRevision = 513;
    this.descriptorVersion = 3;
  }
  protected void createAndPassVRS() throws HandlerException {
    VolumeRecognitionSequence myVolumeRecognitionSequene = new VolumeRecognitionSequence(VolumeRecognitionSequence.NSRVersion.NSR03);
    data((DataReference)new ByteArrayDataReference(myVolumeRecognitionSequene.getBytes()));
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
    myPartitionDescriptor.DescriptorTag.TagSerialNumber = this.tagSerialNumber;
    myPartitionDescriptor.DescriptorTag.DescriptorVersion = this.descriptorVersion;
    myPartitionDescriptor.DescriptorTag.TagLocation = tagLocation;
    myPartitionDescriptor.VolumeDescriptorSequenceNumber = volumeDescriptorSequenceNumber;
    myPartitionDescriptor.PartitionFlags = 1;
    myPartitionDescriptor.PartitionNumber = 0;
    try {
      myPartitionDescriptor.PartitionContents.setIdentifier("+NSR03");
      myPartitionDescriptor.ImplementationIdentifier.setIdentifier(applicationIdentifier);
    }
    catch (Exception myException) {
      throw new HandlerException(myException);
    } 
    myPartitionDescriptor.AccessType = 1L;
    myPartitionDescriptor.PartitonStartingLocation = partitionStartingBlock;
    myPartitionDescriptor.PartitionLength = partitionEndingBlock - partitionStartingBlock;
    myPartitionDescriptor.ImplementationIdentifier.IdentifierSuffix = applicationIdentifierSuffix;
    startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
    data((DataReference)new WordDataReference(5L));
    data((DataReference)new WordDataReference(tagLocation));
    data((DataReference)new WordDataReference(this.tagSerialNumber));
    data((DataReference)new WordDataReference(this.descriptorVersion));
    data((DataReference)new ByteArrayDataReference(myPartitionDescriptor.getBytesWithoutDescriptorTag()));
    endElement();
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
    ExtendedFileEntry myExtendedFileEntry = new ExtendedFileEntry();
    myExtendedFileEntry.DescriptorTag.TagSerialNumber = this.tagSerialNumber;
    myExtendedFileEntry.DescriptorTag.DescriptorVersion = this.descriptorVersion;
    myExtendedFileEntry.DescriptorTag.TagLocation = tagLocation;
    myExtendedFileEntry.Uid = -1L;
    myExtendedFileEntry.Gid = -1L;
    myExtendedFileEntry.Permissions = (Permissions.OTHER_Read | Permissions.GROUP_Read | Permissions.OWNER_Read);
    myExtendedFileEntry.FileLinkCount = fileLinkCount;
    myExtendedFileEntry.RecordFormat = 0;
    myExtendedFileEntry.RecordDisplayAttributes = 0;
    myExtendedFileEntry.RecordLength = 0L;
    myExtendedFileEntry.AccessTime = new Timestamp(accessTime);
    myExtendedFileEntry.ModificationTime = new Timestamp(modificationTime);
    myExtendedFileEntry.AttributeTime = new Timestamp(attributeTime);
    myExtendedFileEntry.CreationTime = new Timestamp(creationTime);
    myExtendedFileEntry.Checkpoint = 1L;
    try {
      myExtendedFileEntry.ImplementationIdentifier.setIdentifier(applicationIdentifier);
    }
    catch (Exception myException) {
      throw new HandlerException(myException);
    } 
    myExtendedFileEntry.ImplementationIdentifier.IdentifierSuffix = applicationIdentifierSuffix;
    myExtendedFileEntry.ICBTag.PriorRecordedNumberofDirectEntries = 0L;
    myExtendedFileEntry.ICBTag.NumberofEntries = 1;
    myExtendedFileEntry.ICBTag.StrategyType = 4;
    myExtendedFileEntry.UniqueID = uniqueId;
    if (fileType == 0) {
      myExtendedFileEntry.ICBTag.FileType = 5;
      createAndPassNormalFE(myExtendedFileEntry);
    }
    else if (fileType == 1) {
      myExtendedFileEntry.ICBTag.FileType = 4;
      createAndPassDirectoryFE(myExtendedFileEntry);
    } 
  }
  private void createAndPassNormalFE(ExtendedFileEntry myExtendedFileEntry) throws HandlerException {
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
    myExtendedFileEntry.ICBTag.FileType = 5;
    myExtendedFileEntry.InformationLength = fileSize;
    myExtendedFileEntry.ObjectSize = myExtendedFileEntry.InformationLength;
    if (fileSize <= (this.blockSize - ExtendedFileEntry.fixedPartLength)) {
      myExtendedFileEntry.ICBTag.Flags = 3;
      myExtendedFileEntry.LogicalBlocksRecorded = 0L;
      myExtendedFileEntry.LengthofAllocationDescriptors = fileSize;
      myExtendedFileEntry.AllocationDescriptors = fileData;
    }
    else {
      myExtendedFileEntry.ICBTag.Flags = 1;
      myExtendedFileEntry.LogicalBlocksRecorded = fileSize / this.blockSize;
      if (fileSize % this.blockSize != 0L)
      {
        ((FileEntry)myExtendedFileEntry).LogicalBlocksRecorded++;
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
      myExtendedFileEntry.AllocationDescriptors = allocationDescriptorBytes;
      myExtendedFileEntry.LengthofAllocationDescriptors = allocationDescriptorBytes.length;
    } 
    startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
    data((DataReference)new WordDataReference(266L));
    data((DataReference)new WordDataReference(myExtendedFileEntry.DescriptorTag.TagLocation));
    data((DataReference)new WordDataReference(this.tagSerialNumber));
    data((DataReference)new WordDataReference(this.descriptorVersion));
    data((DataReference)new ByteArrayDataReference(myExtendedFileEntry.getBytesWithoutDescriptorTag()));
    endElement();
  }
  private void createAndPassDirectoryFE(ExtendedFileEntry myExtendedFileEntry) throws HandlerException {
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
      parentDirectoryFileIdentifierDescriptor.DescriptorTag.TagLocation = myExtendedFileEntry.DescriptorTag.TagLocation;
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
        childFileIdentifierDescriptor.DescriptorTag.TagLocation = myExtendedFileEntry.DescriptorTag.TagLocation;
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
    myExtendedFileEntry.InformationLength = directoryFileDataLength;
    myExtendedFileEntry.ObjectSize = myExtendedFileEntry.InformationLength;
    if (directoryFileDataLength <= this.blockSize - ExtendedFileEntry.fixedPartLength) {
      myExtendedFileEntry.ICBTag.Flags = 3;
      myExtendedFileEntry.LogicalBlocksRecorded = 0L;
      myExtendedFileEntry.LengthofAllocationDescriptors = directoryFileDataLength;
      myExtendedFileEntry.AllocationDescriptors = new byte[directoryFileDataLength];
      int pos = 0;
      for (int j = 0; j < childFileIdentifierDescriptors.size(); j++) {
        byte[] childFileIdentifierDescriptorBytes = ((FileIdentifierDescriptor)childFileIdentifierDescriptors.get(j)).getBytes();
        System.arraycopy(childFileIdentifierDescriptorBytes, 0, myExtendedFileEntry.AllocationDescriptors, pos, childFileIdentifierDescriptorBytes.length);
        pos += childFileIdentifierDescriptorBytes.length;
      } 
      startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
      data((DataReference)new WordDataReference(266L));
      data((DataReference)new WordDataReference(myExtendedFileEntry.DescriptorTag.TagLocation));
      data((DataReference)new WordDataReference(this.tagSerialNumber));
      data((DataReference)new WordDataReference(this.descriptorVersion));
      data((DataReference)new ByteArrayDataReference(myExtendedFileEntry.getBytesWithoutDescriptorTag()));
      endElement();
    }
    else {
      myExtendedFileEntry.ICBTag.Flags = 0;
      myExtendedFileEntry.LogicalBlocksRecorded = (directoryFileDataLength / this.blockSize);
      if (directoryFileDataLength % this.blockSize != 0)
      {
        ((FileEntry)myExtendedFileEntry).LogicalBlocksRecorded++;
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
      myExtendedFileEntry.AllocationDescriptors = allocationDescriptor.getBytes();
      myExtendedFileEntry.LengthofAllocationDescriptors = myExtendedFileEntry.AllocationDescriptors.length;
      startElement((Element)new SabreUDFElement(SabreUDFElement.UDFElementType.DescriptorTag));
      data((DataReference)new WordDataReference(266L));
      data((DataReference)new WordDataReference(myExtendedFileEntry.DescriptorTag.TagLocation));
      data((DataReference)new WordDataReference(this.tagSerialNumber));
      data((DataReference)new WordDataReference(this.descriptorVersion));
      data((DataReference)new ByteArrayDataReference(myExtendedFileEntry.getBytesWithoutDescriptorTag()));
      endElement();
      data((DataReference)new ByteArrayDataReference(data));
    } 
  }
}
