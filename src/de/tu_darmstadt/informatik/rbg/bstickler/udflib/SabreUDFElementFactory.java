package de.tu_darmstadt.informatik.rbg.bstickler.udflib;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.DataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.HandlerException;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.StreamHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.ByteArrayDataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.ByteDataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.DWordDataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.FileDataReference;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl.WordDataReference;
import java.io.File;
public class SabreUDFElementFactory
{
  private int blockSize = 2048;
  private StreamHandler myStreamHandler = null;
  public SabreUDFElementFactory(StreamHandler myStreamHandler) {
    this.myStreamHandler = myStreamHandler;
  }
  public void startUDFImage() throws HandlerException {
    this.myStreamHandler.startDocument();
  }
  public void endUDFImage() throws HandlerException {
    this.myStreamHandler.endDocument();
  }
  public void startReservedArea() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.ReservedArea));
    this.myStreamHandler.isDataOver(true);
    this.myStreamHandler.isEndOver(true);
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(new byte[32768]));
  }
  public void endReservedArea() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startVRS() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.VolumeRecognitionSequence));
  }
  public void endVRS() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startEmptyArea() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.EmptyArea));
  }
  public void doEmptyArea(int lengthInBytes) throws HandlerException {
    this.myStreamHandler.isEndOver(true);
    this.myStreamHandler.isDataOver(true);
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(new byte[lengthInBytes]));
  }
  public void endEmptyArea() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startAVDP() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.AnchorVolumeDescriptorPointer));
  }
  public void doAVDP(long selfBlock, long MVDSBlock, long RVDSBlock) throws HandlerException {
    this.myStreamHandler.data((DataReference)new WordDataReference(selfBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(MVDSBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(RVDSBlock));
  }
  public void endAVDP() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startPVD() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.PrimaryVolumeDescriptor));
  }
  public void doPVD(long selfBlock, long volumeDescriptorSequenceNumber, long recordingTimeMillis, String imageIdentifier, String applicationIdentifier, byte[] applicationIdentifierSuffix) throws HandlerException {
    this.myStreamHandler.data((DataReference)new WordDataReference(selfBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(volumeDescriptorSequenceNumber));
    this.myStreamHandler.data((DataReference)new DWordDataReference(recordingTimeMillis));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(imageIdentifier.getBytes()));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifier.getBytes()));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifierSuffix));
  }
  public void endPVD() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startPD() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.PartitionDescriptor));
  }
  public void doPD(long selfBlock, long volumeDescriptorSequenceNumber, long partitionStartingBlock, long partitionEndingBlock, String applicationIdentifier, byte[] applicationIdentifierSuffix) throws HandlerException {
    this.myStreamHandler.data((DataReference)new WordDataReference(selfBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(volumeDescriptorSequenceNumber));
    this.myStreamHandler.data((DataReference)new WordDataReference(partitionStartingBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(partitionEndingBlock));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifier.getBytes()));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifierSuffix));
  }
  public void endPD() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startLVD() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.LogicalVolumeDescriptor));
  }
  public void doLVD(long selfBlock, long volumeDescriptorSequenceNumber, long logicalVolumeIntegritySequenceStartingBlock, long logicalVolumeIntegritySequenceEndingBlock, int metadataAllocationUnitSize, int metadataAlignmentUnitSize, long mainMetadataFileLocation, long mirrorMetadataFileLocation, int fileSetDescriptorPartition, long fileSetDescriptorLocation, String applicationIdentifier, byte[] applicationIdentifierSuffix, String imageIdentifier) throws HandlerException {
    this.myStreamHandler.data((DataReference)new WordDataReference(selfBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(volumeDescriptorSequenceNumber));
    this.myStreamHandler.data((DataReference)new WordDataReference(logicalVolumeIntegritySequenceStartingBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(logicalVolumeIntegritySequenceEndingBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(metadataAllocationUnitSize));
    this.myStreamHandler.data((DataReference)new WordDataReference(metadataAlignmentUnitSize));
    this.myStreamHandler.data((DataReference)new WordDataReference(mainMetadataFileLocation));
    this.myStreamHandler.data((DataReference)new WordDataReference(mirrorMetadataFileLocation));
    this.myStreamHandler.data((DataReference)new WordDataReference(fileSetDescriptorPartition));
    this.myStreamHandler.data((DataReference)new WordDataReference(fileSetDescriptorLocation));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifier.getBytes()));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifierSuffix));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(imageIdentifier.getBytes()));
  }
  public void endLVD() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startUSD() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.UnallocatedSpaceDescriptor));
  }
  public void doUSD(long selfBlock, long volumeDescriptorSequenceNumber, long unallocatedSpaceStartBlock, long unallocatedSpaceEndBlock) throws HandlerException {
    this.myStreamHandler.data((DataReference)new WordDataReference(selfBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(volumeDescriptorSequenceNumber));
    this.myStreamHandler.data((DataReference)new WordDataReference(unallocatedSpaceStartBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(unallocatedSpaceEndBlock));
  }
  public void endUSD() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startIUVD() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.ImplementationUseVolumeDescriptor));
  }
  public void doIUVD(long selfBlock, long volumeDescriptorSequenceNumber, String applicationIdentifier, byte[] applicationIdentifierSuffix, String imageIdentifier) throws HandlerException {
    this.myStreamHandler.data((DataReference)new WordDataReference(selfBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(volumeDescriptorSequenceNumber));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifier.getBytes()));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifierSuffix));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(imageIdentifier.getBytes()));
  }
  public void endIUVD() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startTD() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.TerminatingDescriptor));
  }
  public void doTD(long selfBlock) throws HandlerException {
    this.myStreamHandler.data((DataReference)new WordDataReference(selfBlock));
  }
  public void endTD() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startLVID() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.LogicalVolumeIntegrityDescriptor));
  }
  public void doLVID(long selfBlock, long recordingTimeMillis, long numberOfFiles, long numberOfDirectories, String applicationIdentifier, byte[] applicationIdentifierSuffix, long[] sizeTable, long[] freespaceTable, long nextUniqueId) throws HandlerException {
    this.myStreamHandler.data((DataReference)new WordDataReference(selfBlock));
    this.myStreamHandler.data((DataReference)new DWordDataReference(recordingTimeMillis));
    this.myStreamHandler.data((DataReference)new WordDataReference(numberOfFiles));
    this.myStreamHandler.data((DataReference)new WordDataReference(numberOfDirectories));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifier.getBytes()));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifierSuffix));
    for (int i = 0; i < sizeTable.length; i++) {
      this.myStreamHandler.data((DataReference)new WordDataReference(sizeTable[i]));
      this.myStreamHandler.data((DataReference)new WordDataReference(freespaceTable[i]));
    } 
    this.myStreamHandler.data((DataReference)new WordDataReference(sizeTable.length));
    this.myStreamHandler.data((DataReference)new WordDataReference(nextUniqueId));
  }
  public void endLVID() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startFSD() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.FileSetDescriptor));
  }
  public void doFSD(long selfBlock, long recordingTimeMillis, long rootDirectoryLocation, int partitionToStoreMetadataOn, String imageIdentifier) throws HandlerException {
    this.myStreamHandler.data((DataReference)new WordDataReference(selfBlock));
    this.myStreamHandler.data((DataReference)new DWordDataReference(recordingTimeMillis));
    this.myStreamHandler.data((DataReference)new WordDataReference(rootDirectoryLocation));
    this.myStreamHandler.data((DataReference)new WordDataReference(partitionToStoreMetadataOn));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(imageIdentifier.getBytes()));
  }
  public void endFSD() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startFE() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.FileEntry));
  }
  public void doFE(UDFImageBuilderFile myUDFImageBuilderFile, UDFLayoutInformation myUDFLayoutInformation, String applicationIdentifier, byte[] applicationIdentifierSuffix) throws HandlerException {
    long selfBlock = ((UDFLayoutInformation.FileEntryPosition)myUDFLayoutInformation.fileEntryPositions.get(myUDFImageBuilderFile)).entryLocation;
    long dataLocation = ((UDFLayoutInformation.FileEntryPosition)myUDFLayoutInformation.fileEntryPositions.get(myUDFImageBuilderFile)).dataLocation;
    long uniqueId = ((Long)myUDFLayoutInformation.uniqueIds.get(myUDFImageBuilderFile)).longValue();
    int fileLinkCount = myUDFImageBuilderFile.getFileLinkCount();
    long accessTimeMillis = myUDFImageBuilderFile.getAccessTime().getTimeInMillis();
    long modificationTimeMillis = myUDFImageBuilderFile.getModificationTime().getTimeInMillis();
    long attributeTimeMillis = myUDFImageBuilderFile.getAttributeTime().getTimeInMillis();
    long creationTimeMillis = myUDFImageBuilderFile.getCreationTime().getTimeInMillis();
    int fileType = -1;
    if (myUDFImageBuilderFile.getFileType() == UDFImageBuilderFile.FileType.File) {
      fileType = 0;
    }
    else if (myUDFImageBuilderFile.getFileType() == UDFImageBuilderFile.FileType.Directory) {
      fileType = 1;
    } 
    if (myUDFImageBuilderFile.getFileType() == UDFImageBuilderFile.FileType.File) {
      this.myStreamHandler.data((DataReference)new FileDataReference(myUDFImageBuilderFile.getSourceFile()));
      this.myStreamHandler.data((DataReference)new WordDataReference(dataLocation));
    }
    else if (myUDFImageBuilderFile.getFileType() == UDFImageBuilderFile.FileType.Directory) {
      UDFImageBuilderFile[] childUDFImageBuilderFiles = myUDFImageBuilderFile.getChilds();
      for (int i = childUDFImageBuilderFiles.length - 1; i >= 0; i--) {
        long childFileUniqueId = ((Long)myUDFLayoutInformation.uniqueIds.get(childUDFImageBuilderFiles[i])).longValue();
        long childFileLocation = 
          ((UDFLayoutInformation.FileEntryPosition)myUDFLayoutInformation.fileEntryPositions.get(childUDFImageBuilderFiles[i])).entryLocation;
        int childFileType = -1;
        if (childUDFImageBuilderFiles[i].getFileType() == UDFImageBuilderFile.FileType.File) {
          childFileType = 0;
        }
        else if (childUDFImageBuilderFiles[i].getFileType() == UDFImageBuilderFile.FileType.Directory) {
          childFileType = 1;
        } 
        this.myStreamHandler.data((DataReference)new DWordDataReference(childFileUniqueId));
        this.myStreamHandler.data((DataReference)new WordDataReference(childFileLocation));
        this.myStreamHandler.data((DataReference)new ByteArrayDataReference(childUDFImageBuilderFiles[i].getIdentifier().getBytes()));
        this.myStreamHandler.data((DataReference)new WordDataReference(childFileType));
      } 
      this.myStreamHandler.data((DataReference)new WordDataReference(childUDFImageBuilderFiles.length));
      long parentDirectoryUniqueId = 0L;
      long parentDirectoryLocation = 
        ((UDFLayoutInformation.FileEntryPosition)myUDFLayoutInformation.fileEntryPositions.get(myUDFImageBuilderFile)).entryLocation;
      if (myUDFImageBuilderFile.getParent() != null) {
        parentDirectoryUniqueId = ((Long)myUDFLayoutInformation.uniqueIds.get(myUDFImageBuilderFile.getParent())).longValue();
        parentDirectoryLocation = 
          ((UDFLayoutInformation.FileEntryPosition)myUDFLayoutInformation.fileEntryPositions.get(myUDFImageBuilderFile.getParent())).entryLocation;
      } 
      this.myStreamHandler.data((DataReference)new DWordDataReference(parentDirectoryUniqueId));
      this.myStreamHandler.data((DataReference)new WordDataReference(parentDirectoryLocation));
      this.myStreamHandler.data((DataReference)new WordDataReference(myUDFLayoutInformation.partitionToStoreMetadataOn));
      this.myStreamHandler.data((DataReference)new WordDataReference(dataLocation));
    } 
    this.myStreamHandler.data((DataReference)new WordDataReference(selfBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(fileLinkCount));
    this.myStreamHandler.data((DataReference)new DWordDataReference(accessTimeMillis));
    this.myStreamHandler.data((DataReference)new DWordDataReference(modificationTimeMillis));
    this.myStreamHandler.data((DataReference)new DWordDataReference(attributeTimeMillis));
    this.myStreamHandler.data((DataReference)new DWordDataReference(creationTimeMillis));
    this.myStreamHandler.data((DataReference)new DWordDataReference(uniqueId));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifier.getBytes()));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifierSuffix));
    this.myStreamHandler.data((DataReference)new WordDataReference(fileType));
    this.myStreamHandler.data((DataReference)new WordDataReference(myUDFLayoutInformation.partitionToStoreMetadataOn));
  }
  public void endFE() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startRawFileData() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.RawFileData));
  }
  public void doRawFileData(File sourceFile) throws HandlerException {
    this.myStreamHandler.extendData(true, sourceFile);
    if (sourceFile.length() % this.blockSize == 0L)
    {
      this.myStreamHandler.isDataOver(true);
    }
    this.myStreamHandler.data((DataReference)new FileDataReference(sourceFile));
  }
  public void endRawFileData() throws HandlerException {
    this.myStreamHandler.endElement();
  }
  public void startMetadataFile() throws HandlerException {
    this.myStreamHandler.startElement(new SabreUDFElement(SabreUDFElement.UDFElementType.MetadataFile));
  }
  public void doMetadataFile(long recordingTimeMillis, String applicationIdentifier, byte[] applicationIdentifierSuffix, UDFLayoutInformation myUDFLayoutInformation, long metadataFileLocation, byte fileType) throws HandlerException {
    this.myStreamHandler.data((DataReference)new DWordDataReference(recordingTimeMillis));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifier.getBytes()));
    this.myStreamHandler.data((DataReference)new ByteArrayDataReference(applicationIdentifierSuffix));
    this.myStreamHandler.data((DataReference)new WordDataReference(myUDFLayoutInformation.metadataPartitionStartingBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(myUDFLayoutInformation.metadataPartitionEndingBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(myUDFLayoutInformation.physicalPartitionStartingBlock));
    this.myStreamHandler.data((DataReference)new WordDataReference(metadataFileLocation));
    this.myStreamHandler.data((DataReference)new ByteDataReference(fileType));
  }
  public void endMetadataFile() throws HandlerException {
    this.myStreamHandler.endElement();
  }
}
