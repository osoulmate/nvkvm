package de.tu_darmstadt.informatik.rbg.bstickler.udflib;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.ExtendedFileEntry;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.FileEntry;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.FileIdentifierDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.UniqueIdDisposer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
public class UDFLayoutInformation
{
  private UniqueIdDisposer myUniqueIdDisposer;
  public int blockSize;
  public int metadataAllocationUnitSize;
  public int metadataAlignmentUnitSize;
  public long fileCount;
  public long directoryCount;
  public long AVDP1Block;
  public long AVDP2Block;
  public long MVDSStartingBlock;
  public long MVDSEndingBlock;
  public long RVDSStartingBlock;
  public long RVDSEndingBlock;
  public long LVIDSStartingBlock;
  public long LVIDSEndingBlock;
  public long physicalPartitionStartingBlock;
  public long physicalPartitionEndingBlock;
  public long metadataPartitionStartingBlock;
  public long metadataPartitionEndingBlock;
  public long mainMetadataFileBlock;
  public long mainMetadataFileLocation;
  public long mirrorMetadataFileBlock;
  public long mirrorMetadataFileLocation;
  public long metadataEmptyArea;
  public int partitionToStoreMetadataOn;
  public long FSDBlock;
  public long FSDLocation;
  public long rootFEBlock;
  public long rootFELocation;
  public long PVD1Block;
  public long PVD2Block;
  public long PD1Block;
  public long PD2Block;
  public long LVD1Block;
  public long LVD2Block;
  public long USD1Block;
  public long USD2Block;
  public long IUVD1Block;
  public long IUVD2Block;
  public long TD1Block;
  public long TD2Block;
  public long[] sizeTable;
  public long[] freespaceTable;
  public long nextUniqueId;
  public Hashtable<UDFImageBuilderFile, FileEntryPosition> fileEntryPositions;
  public ArrayList<UDFImageBuilderFile> linearUDFImageBuilderFileOrdering;
  public Hashtable<UDFImageBuilderFile, Long> uniqueIds;
  public class FileEntryPosition
  {
    long entryBlock;
    long entryLocation;
    long dataBlock;
    long dataLocation;
  }
  public UDFLayoutInformation(UDFImageBuilderFile rootUDFImageBuilderFile, UDFRevision myUDFRevision, int blockSize) throws Exception {
    this.myUniqueIdDisposer = new UniqueIdDisposer();
    this.fileEntryPositions = new Hashtable<UDFImageBuilderFile, FileEntryPosition>();
    this.linearUDFImageBuilderFileOrdering = new ArrayList<UDFImageBuilderFile>();
    this.uniqueIds = new Hashtable<UDFImageBuilderFile, Long>();
    this.blockSize = blockSize;
    this.metadataAllocationUnitSize = 32;
    this.metadataAlignmentUnitSize = 1;
    this.fileCount = rootUDFImageBuilderFile.getFileCount();
    this.directoryCount = rootUDFImageBuilderFile.getDirectoryCount();
    this.AVDP1Block = 256L;
    this.MVDSStartingBlock = 257L;
    this.MVDSEndingBlock = this.MVDSStartingBlock + 16L;
    this.PVD1Block = this.MVDSStartingBlock;
    this.PD1Block = this.MVDSStartingBlock + 1L;
    this.LVD1Block = this.MVDSStartingBlock + 2L;
    this.USD1Block = this.MVDSStartingBlock + 3L;
    this.IUVD1Block = this.MVDSStartingBlock + 4L;
    this.TD1Block = this.MVDSStartingBlock + 5L;
    this.LVIDSStartingBlock = this.MVDSEndingBlock;
    this.LVIDSEndingBlock = this.LVIDSStartingBlock + 4L;
    this.physicalPartitionStartingBlock = this.LVIDSEndingBlock;
    if (myUDFRevision == UDFRevision.Revision260) {
      this.partitionToStoreMetadataOn = 1;
      this.mainMetadataFileBlock = this.physicalPartitionStartingBlock + 1L;
      this.mainMetadataFileLocation = this.mainMetadataFileBlock - this.physicalPartitionStartingBlock;
      this.metadataPartitionStartingBlock = this.physicalPartitionStartingBlock + 2L;
      this.FSDBlock = this.physicalPartitionStartingBlock + 2L;
      this.FSDLocation = 0L;
      this.rootFEBlock = this.physicalPartitionStartingBlock + 3L;
      this.rootFELocation = 1L;
    }
    else {
      this.partitionToStoreMetadataOn = 0;
      this.FSDBlock = this.physicalPartitionStartingBlock + 1L;
      this.FSDLocation = 1L;
      this.rootFEBlock = this.physicalPartitionStartingBlock + 2L;
      this.rootFELocation = 2L;
    } 
    long[] currentBlock = recursiveGetFileEntryLocation(rootUDFImageBuilderFile, new long[] { this.rootFEBlock }, myUDFRevision);
    this.nextUniqueId = this.myUniqueIdDisposer.getNextUniqueId();
    if (myUDFRevision == UDFRevision.Revision260) {
      if ((currentBlock[0] - this.metadataPartitionStartingBlock) % this.metadataAllocationUnitSize != 0L) {
        this.metadataEmptyArea = this.metadataAllocationUnitSize - (currentBlock[0] - this.metadataPartitionStartingBlock) % this.metadataAllocationUnitSize;
        currentBlock[0] = currentBlock[0] + this.metadataEmptyArea;
      } 
      this.metadataPartitionEndingBlock = currentBlock[0];
      this.mirrorMetadataFileBlock = currentBlock[0] + currentBlock[1];
      this.mirrorMetadataFileLocation = this.mirrorMetadataFileBlock - this.physicalPartitionStartingBlock;
      this.physicalPartitionEndingBlock = this.mirrorMetadataFileBlock + 1L;
    }
    else {
      this.physicalPartitionEndingBlock = currentBlock[0] + currentBlock[1];
    } 
    Enumeration<UDFImageBuilderFile> myEnumeration = this.fileEntryPositions.keys();
    while (myEnumeration.hasMoreElements()) {
      UDFImageBuilderFile myUDFImageBuilderFile = myEnumeration.nextElement();
      if (myUDFImageBuilderFile.getFileType() == UDFImageBuilderFile.FileType.File) {
        FileEntryPosition myFileEntryPosition = this.fileEntryPositions.get(myUDFImageBuilderFile);
        if (myFileEntryPosition.dataBlock != -1L) {
          myFileEntryPosition.dataBlock += currentBlock[0];
          myFileEntryPosition.dataLocation += myFileEntryPosition.dataBlock - this.physicalPartitionStartingBlock;
        } 
      } 
    } 
    if (myUDFRevision == UDFRevision.Revision260) {
      this.sizeTable = new long[2];
      this.sizeTable[0] = this.physicalPartitionEndingBlock - this.physicalPartitionStartingBlock;
      this.sizeTable[1] = this.metadataPartitionEndingBlock - this.metadataPartitionStartingBlock;
      this.freespaceTable = new long[2];
    }
    else {
      this.sizeTable = new long[1];
      this.sizeTable[0] = this.physicalPartitionEndingBlock - this.physicalPartitionStartingBlock;
      this.freespaceTable = new long[1];
    } 
    this.RVDSStartingBlock = this.physicalPartitionEndingBlock;
    this.RVDSEndingBlock = this.RVDSStartingBlock + 16L;
    this.PVD2Block = this.RVDSStartingBlock;
    this.PD2Block = this.RVDSStartingBlock + 1L;
    this.LVD2Block = this.RVDSStartingBlock + 2L;
    this.USD2Block = this.RVDSStartingBlock + 3L;
    this.IUVD2Block = this.RVDSStartingBlock + 4L;
    this.TD2Block = this.RVDSStartingBlock + 5L;
    this.AVDP2Block = this.RVDSEndingBlock + 1L;
  }
  private long[] recursiveGetFileEntryLocation(UDFImageBuilderFile currentUDFImageBuilderFile, long[] currentBlock, UDFRevision myUDFRevision) throws Exception {
    if (currentUDFImageBuilderFile.getIdentifier().equals("")) {
      this.uniqueIds.put(currentUDFImageBuilderFile, new Long(0L));
    }
    else {
      this.uniqueIds.put(currentUDFImageBuilderFile, new Long(this.myUniqueIdDisposer.getNextUniqueId()));
    } 
    this.linearUDFImageBuilderFileOrdering.add(currentUDFImageBuilderFile);
    FileEntryPosition currentFileEntryPosition = new FileEntryPosition();
    currentFileEntryPosition.entryBlock = currentBlock[0];
    currentBlock[0] = currentBlock[0] + 1L;
    if (myUDFRevision == UDFRevision.Revision260) {
      currentFileEntryPosition.entryLocation = currentFileEntryPosition.entryBlock - this.metadataPartitionStartingBlock;
    }
    else {
      currentFileEntryPosition.entryLocation = currentFileEntryPosition.entryBlock - this.physicalPartitionStartingBlock;
    } 
    if (currentUDFImageBuilderFile.getFileType() == UDFImageBuilderFile.FileType.File) {
      if ((myUDFRevision == UDFRevision.Revision102 && currentUDFImageBuilderFile.getFileLength() > (this.blockSize - FileEntry.fixedPartLength)) || 
        currentUDFImageBuilderFile.getFileLength() > (this.blockSize - ExtendedFileEntry.fixedPartLength))
      {
        currentFileEntryPosition.dataBlock = currentBlock[1];
        currentBlock[1] = currentBlock[1] + currentUDFImageBuilderFile.getFileLength() / this.blockSize;
        if (currentUDFImageBuilderFile.getFileLength() % this.blockSize != 0L)
        {
          currentBlock[1] = currentBlock[1] + 1L;
        }
      }
      else
      {
        currentFileEntryPosition.dataBlock = -1L;
      }
    } else {
      long FileIdentifierDescriptorsLength = 0L;
      FileIdentifierDescriptor parentDirectoryFileIdentifierDescriptor = new FileIdentifierDescriptor();
      FileIdentifierDescriptorsLength += parentDirectoryFileIdentifierDescriptor.getLength();
      UDFImageBuilderFile[] childUDFImageBuilderFiles = currentUDFImageBuilderFile.getChilds();
      int i;
      for (i = 0; i < childUDFImageBuilderFiles.length; i++) {
        FileIdentifierDescriptor childFileIdentifierDescriptor = new FileIdentifierDescriptor();
        childFileIdentifierDescriptor.setFileIdentifier(childUDFImageBuilderFiles[i].getIdentifier());
        FileIdentifierDescriptorsLength += childFileIdentifierDescriptor.getLength();
      } 
      if ((myUDFRevision == UDFRevision.Revision102 && FileIdentifierDescriptorsLength > (this.blockSize - FileEntry.fixedPartLength)) || 
        FileIdentifierDescriptorsLength > (this.blockSize - ExtendedFileEntry.fixedPartLength)) {
        currentFileEntryPosition.dataBlock = currentBlock[0];
        if (myUDFRevision == UDFRevision.Revision260) {
          currentFileEntryPosition.dataLocation = currentFileEntryPosition.dataBlock - this.metadataPartitionStartingBlock;
        }
        else {
          currentFileEntryPosition.dataLocation = currentFileEntryPosition.dataBlock - this.physicalPartitionStartingBlock;
        } 
        currentBlock[0] = currentBlock[0] + FileIdentifierDescriptorsLength / this.blockSize;
        if (FileIdentifierDescriptorsLength % this.blockSize != 0L)
        {
          currentBlock[0] = currentBlock[0] + 1L;
        }
      }
      else {
        currentFileEntryPosition.dataBlock = -1L;
      } 
      for (i = 0; i < childUDFImageBuilderFiles.length; i++)
      {
        currentBlock = recursiveGetFileEntryLocation(childUDFImageBuilderFiles[i], currentBlock, myUDFRevision);
      }
    } 
    this.fileEntryPositions.put(currentUDFImageBuilderFile, currentFileEntryPosition);
    return currentBlock;
  }
}
