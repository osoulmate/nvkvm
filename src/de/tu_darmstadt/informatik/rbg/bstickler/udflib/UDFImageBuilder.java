package de.tu_darmstadt.informatik.rbg.bstickler.udflib;
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
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.ReservedArea;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.Short_ad;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.TerminatingDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.Timestamp;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.UnallocatedSpaceDescriptor;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures.VolumeRecognitionSequence;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.Permissions;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;

import com.kvm.UniqueIdDisposer;
public class UDFImageBuilder {
  private int blockSize = 2048;
  private String imageIdentifier = "UDFImageBuilder Disc";
  private String applicationIdentifier = "*UDFImageBuilder";
  private byte[] applicationIdentifierSuffix = new byte[] { 1 };
  private UDFImageBuilderFile rootUDFImageBuilderFile;
  private UniqueIdDisposer myUniqueIdDisposer;
  private long maximumAllocationLength = 1073739776L;
  public UDFImageBuilder() {
    this.rootUDFImageBuilderFile = new UDFImageBuilderFile("");
    this.myUniqueIdDisposer = new UniqueIdDisposer();
  }
  public void setImageIdentifier(String imageIdentifier) throws Exception {
    if (imageIdentifier.length() > 30)
    {
      throw new Exception("error: image identifier length > 30 characters");
    }
    this.imageIdentifier = imageIdentifier;
  }
  public void addFileToRootDirectory(UDFImageBuilderFile myUDFImageBuilderFile) throws Exception {
    this.rootUDFImageBuilderFile.addChild(myUDFImageBuilderFile);
  }
  public void addFileToRootDirectory(File myFile) throws Exception {
    this.rootUDFImageBuilderFile.addChild(myFile);
  }
  public void writeImage(String filename, UDFRevision udfRevision) throws Exception {
    if (udfRevision == UDFRevision.Revision102) {
      writeImageV102(filename);
    }
    else if (udfRevision == UDFRevision.Revision201) {
      writeImageV201(filename);
    }
    else if (udfRevision == UDFRevision.Revision260) {
      writeImageV260(filename);
    } 
  }
  private void writeImageV102(String filename) throws Exception {
    int serialNumberForTags = 1;
    int descriptorVersion = 2;
    Calendar recordingTimeCalendar = Calendar.getInstance();
    byte[] udfVersionIdentifierSuffix = { 2, 1 };
    int minimumUDFReadRevision = 258;
    int minimumUDFWriteRevision = udfVersionIdentifierSuffix[1] << 8 | udfVersionIdentifierSuffix[0];
    int maximumUDFWriteRevision = udfVersionIdentifierSuffix[1] << 8 | udfVersionIdentifierSuffix[0];
    File outFile = new File(filename);
    if (outFile.exists())
    {
      outFile.delete();
    }
    RandomAccessFile myRandomAccessFile = new RandomAccessFile(filename, "rw");
    ReservedArea.write(myRandomAccessFile);
    VolumeRecognitionSequence myVolumeRecognitionSequence = new VolumeRecognitionSequence(VolumeRecognitionSequence.NSRVersion.NSR02);
    myVolumeRecognitionSequence.write(myRandomAccessFile);
    int LVIDSequenceStartingBlock = 273;
    int LVIDSequenceLength = this.blockSize * 4;
    long partitionStartingBlock = 277L;
    long currentBlock = partitionStartingBlock + 1L;
    writeFilesetDescriptor(myRandomAccessFile, 
        currentBlock, 
        currentBlock + 1L, 
        0, 
        partitionStartingBlock, 
        recordingTimeCalendar, 
        serialNumberForTags, 
        udfVersionIdentifierSuffix, 
        descriptorVersion);
    currentBlock++;
    long nextFreeBlock = recursiveWriteFilesystem(myRandomAccessFile, 
        partitionStartingBlock, 
        this.blockSize, 
        serialNumberForTags, 
        this.rootUDFImageBuilderFile, 
        currentBlock, 
        null, 
        0L, 
        false, 
        descriptorVersion);
    long partitionEndingBlock = nextFreeBlock;
    long AVDP1Block = 256L;
    long AVDP2Block = partitionEndingBlock + 16L;
    long MVDSBlock = 257L;
    long RVDSBlock = partitionEndingBlock;
    writeAnchorVolumeDescriptorPointer(myRandomAccessFile, AVDP1Block, MVDSBlock, RVDSBlock, serialNumberForTags, descriptorVersion);
    writeAnchorVolumeDescriptorPointer(myRandomAccessFile, AVDP2Block, MVDSBlock, RVDSBlock, serialNumberForTags, descriptorVersion);
    long PVD1Block = MVDSBlock;
    long PVD2Block = RVDSBlock;
    long PD1Block = MVDSBlock + 1L;
    long PD2Block = RVDSBlock + 1L;
    long LVD1Block = MVDSBlock + 2L;
    long LVD2Block = RVDSBlock + 2L;
    long USD1Block = MVDSBlock + 3L;
    long USD2Block = RVDSBlock + 3L;
    long IUVD1Block = MVDSBlock + 4L;
    long IUVD2Block = RVDSBlock + 4L;
    long TD1Block = MVDSBlock + 5L;
    long TD2Block = RVDSBlock + 5L;
    writePrimaryVolumeDescriptor(myRandomAccessFile, 1L, PVD1Block, recordingTimeCalendar, serialNumberForTags, descriptorVersion);
    writePrimaryVolumeDescriptor(myRandomAccessFile, 1L, PVD2Block, recordingTimeCalendar, serialNumberForTags, descriptorVersion);
    writePartitionDescriptor(myRandomAccessFile, 2L, PD1Block, partitionStartingBlock, partitionEndingBlock, serialNumberForTags, descriptorVersion);
    writePartitionDescriptor(myRandomAccessFile, 2L, PD2Block, partitionStartingBlock, partitionEndingBlock, serialNumberForTags, descriptorVersion);
    writeLogicalVolumeDescriptor(myRandomAccessFile, 3L, LVD1Block, LVIDSequenceStartingBlock, LVIDSequenceLength, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeLogicalVolumeDescriptor(myRandomAccessFile, 3L, LVD2Block, LVIDSequenceStartingBlock, LVIDSequenceLength, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeUnallocatedSpaceDescriptor(myRandomAccessFile, 4L, USD1Block, 19L, 256L, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeUnallocatedSpaceDescriptor(myRandomAccessFile, 4L, USD2Block, 19L, 256L, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeImplementationUseVolumeDescriptor(myRandomAccessFile, 5L, IUVD1Block, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeImplementationUseVolumeDescriptor(myRandomAccessFile, 5L, IUVD2Block, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeTerminatingDescriptor(myRandomAccessFile, TD1Block, serialNumberForTags, descriptorVersion);
    writeTerminatingDescriptor(myRandomAccessFile, TD2Block, serialNumberForTags, descriptorVersion);
    int currentLVIDSBlock = LVIDSequenceStartingBlock;
    long[] sizeTable = new long[1];
    long[] freeSpaceTable = new long[1];
    sizeTable[0] = partitionEndingBlock - partitionStartingBlock;
    freeSpaceTable[0] = 0L;
    writeLogicalVolumeIntegrityDescriptor(myRandomAccessFile, currentLVIDSBlock, recordingTimeCalendar, serialNumberForTags, minimumUDFReadRevision, minimumUDFWriteRevision, maximumUDFWriteRevision, descriptorVersion, sizeTable, freeSpaceTable);
    currentLVIDSBlock++;
    writeTerminatingDescriptor(myRandomAccessFile, currentLVIDSBlock, serialNumberForTags, descriptorVersion);
    myRandomAccessFile.close();
  }
  private void writeImageV201(String filename) throws Exception {
    int serialNumberForTags = 1;
    int descriptorVersion = 3;
    Calendar recordingTimeCalendar = Calendar.getInstance();
    byte[] udfVersionIdentifierSuffix = { 1, 2 };
    int minimumUDFReadRevision = 513;
    int minimumUDFWriteRevision = udfVersionIdentifierSuffix[1] << 8 | udfVersionIdentifierSuffix[0];
    int maximumUDFWriteRevision = udfVersionIdentifierSuffix[1] << 8 | udfVersionIdentifierSuffix[0];
    File outFile = new File(filename);
    if (outFile.exists())
    {
      outFile.delete();
    }
    RandomAccessFile myRandomAccessFile = new RandomAccessFile(filename, "rw");
    ReservedArea.write(myRandomAccessFile);
    VolumeRecognitionSequence myVolumeRecognitionSequence = new VolumeRecognitionSequence(VolumeRecognitionSequence.NSRVersion.NSR03);
    myVolumeRecognitionSequence.write(myRandomAccessFile);
    int LVIDSequenceStartingBlock = 273;
    int LVIDSequenceLength = this.blockSize * 4;
    long partitionStartingBlock = 277L;
    long currentBlock = partitionStartingBlock + 1L;
    writeFilesetDescriptor(myRandomAccessFile, 
        currentBlock, 
        currentBlock + 1L, 
        0, 
        partitionStartingBlock, 
        recordingTimeCalendar, 
        serialNumberForTags, 
        udfVersionIdentifierSuffix, 
        descriptorVersion);
    currentBlock++;
    long nextFreeBlock = recursiveWriteFilesystem(myRandomAccessFile, 
        partitionStartingBlock, 
        this.blockSize, 
        serialNumberForTags, 
        this.rootUDFImageBuilderFile, 
        currentBlock, 
        null, 
        0L, 
        true, 
        descriptorVersion);
    long partitionEndingBlock = nextFreeBlock;
    long AVDP1Block = 256L;
    long AVDP2Block = partitionEndingBlock + 16L;
    long MVDSBlock = 257L;
    long RVDSBlock = partitionEndingBlock;
    writeAnchorVolumeDescriptorPointer(myRandomAccessFile, AVDP1Block, MVDSBlock, RVDSBlock, serialNumberForTags, descriptorVersion);
    writeAnchorVolumeDescriptorPointer(myRandomAccessFile, AVDP2Block, MVDSBlock, RVDSBlock, serialNumberForTags, descriptorVersion);
    long PVD1Block = MVDSBlock;
    long PVD2Block = RVDSBlock;
    long PD1Block = MVDSBlock + 1L;
    long PD2Block = RVDSBlock + 1L;
    long LVD1Block = MVDSBlock + 2L;
    long LVD2Block = RVDSBlock + 2L;
    long USD1Block = MVDSBlock + 3L;
    long USD2Block = RVDSBlock + 3L;
    long IUVD1Block = MVDSBlock + 4L;
    long IUVD2Block = RVDSBlock + 4L;
    long TD1Block = MVDSBlock + 5L;
    long TD2Block = RVDSBlock + 5L;
    writePrimaryVolumeDescriptor(myRandomAccessFile, 1L, PVD1Block, recordingTimeCalendar, serialNumberForTags, descriptorVersion);
    writePrimaryVolumeDescriptor(myRandomAccessFile, 1L, PVD2Block, recordingTimeCalendar, serialNumberForTags, descriptorVersion);
    writePartitionDescriptor(myRandomAccessFile, 2L, PD1Block, partitionStartingBlock, partitionEndingBlock, serialNumberForTags, descriptorVersion);
    writePartitionDescriptor(myRandomAccessFile, 2L, PD2Block, partitionStartingBlock, partitionEndingBlock, serialNumberForTags, descriptorVersion);
    writeLogicalVolumeDescriptor(myRandomAccessFile, 3L, LVD1Block, LVIDSequenceStartingBlock, LVIDSequenceLength, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeLogicalVolumeDescriptor(myRandomAccessFile, 3L, LVD2Block, LVIDSequenceStartingBlock, LVIDSequenceLength, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeUnallocatedSpaceDescriptor(myRandomAccessFile, 4L, USD1Block, 19L, 256L, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeUnallocatedSpaceDescriptor(myRandomAccessFile, 4L, USD2Block, 19L, 256L, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeImplementationUseVolumeDescriptor(myRandomAccessFile, 5L, IUVD1Block, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeImplementationUseVolumeDescriptor(myRandomAccessFile, 5L, IUVD2Block, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeTerminatingDescriptor(myRandomAccessFile, TD1Block, serialNumberForTags, descriptorVersion);
    writeTerminatingDescriptor(myRandomAccessFile, TD2Block, serialNumberForTags, descriptorVersion);
    int currentLVIDSBlock = LVIDSequenceStartingBlock;
    long[] sizeTable = new long[1];
    long[] freeSpaceTable = new long[1];
    sizeTable[0] = partitionEndingBlock - partitionStartingBlock;
    freeSpaceTable[0] = 0L;
    writeLogicalVolumeIntegrityDescriptor(myRandomAccessFile, currentLVIDSBlock, recordingTimeCalendar, serialNumberForTags, minimumUDFReadRevision, minimumUDFWriteRevision, maximumUDFWriteRevision, descriptorVersion, sizeTable, freeSpaceTable);
    currentLVIDSBlock++;
    writeTerminatingDescriptor(myRandomAccessFile, currentLVIDSBlock, serialNumberForTags, descriptorVersion);
    myRandomAccessFile.close();
  }
  private void writeImageV260(String filename) throws Exception {
    int serialNumberForTags = 1;
    int descriptorVersion = 3;
    Calendar recordingTimeCalendar = Calendar.getInstance();
    byte[] udfVersionIdentifierSuffix = { 96, 2 };
    int minimumUDFReadRevision = 592;
    int minimumUDFWriteRevision = udfVersionIdentifierSuffix[1] << 8 | udfVersionIdentifierSuffix[0];
    int maximumUDFWriteRevision = udfVersionIdentifierSuffix[1] << 8 | udfVersionIdentifierSuffix[0];
    File outFile = new File(filename);
    if (outFile.exists())
    {
      outFile.delete();
    }
    RandomAccessFile myRandomAccessFile = new RandomAccessFile(filename, "rw");
    ReservedArea.write(myRandomAccessFile);
    VolumeRecognitionSequence myVolumeRecognitionSequence = new VolumeRecognitionSequence(VolumeRecognitionSequence.NSRVersion.NSR03);
    myVolumeRecognitionSequence.write(myRandomAccessFile);
    int LVIDSequenceStartingBlock = 273;
    int LVIDSequenceLength = this.blockSize * 4;
    long partitionStartingBlock = 277L;
    long mainMetadataFileBlock = partitionStartingBlock + 1L;
    long metadataPartitionStartingBlock = partitionStartingBlock + 2L;
    long filesetDescriptorBlock = partitionStartingBlock + 2L;
    long rootDirectoryBlock = partitionStartingBlock + 3L;
    int metadataAllocationUnitSize = 32;
    int metadataAlignmentUnitSize = 1;
    writeFilesetDescriptor(myRandomAccessFile, 
        filesetDescriptorBlock, 
        rootDirectoryBlock, 
        1, 
        metadataPartitionStartingBlock, 
        recordingTimeCalendar, 
        serialNumberForTags, 
        udfVersionIdentifierSuffix, 
        descriptorVersion);
    ExtendedFileEntry metadataExtendedFileEntry = new ExtendedFileEntry();
    metadataExtendedFileEntry.DescriptorTag.TagSerialNumber = serialNumberForTags;
    metadataExtendedFileEntry.DescriptorTag.DescriptorVersion = 3;
    metadataExtendedFileEntry.Uid = -1L;
    metadataExtendedFileEntry.Gid = -1L;
    metadataExtendedFileEntry.AccessTime = new Timestamp(recordingTimeCalendar);
    metadataExtendedFileEntry.ModificationTime = new Timestamp(recordingTimeCalendar);
    metadataExtendedFileEntry.AttributeTime = new Timestamp(recordingTimeCalendar);
    metadataExtendedFileEntry.CreationTime = new Timestamp(recordingTimeCalendar);
    metadataExtendedFileEntry.Checkpoint = 1L;
    metadataExtendedFileEntry.ImplementationIdentifier.setIdentifier(this.applicationIdentifier);
    metadataExtendedFileEntry.ImplementationIdentifier.IdentifierSuffix = this.applicationIdentifierSuffix;
    metadataExtendedFileEntry.ICBTag.Flags = 0;
    metadataExtendedFileEntry.ICBTag.PriorRecordedNumberofDirectEntries = 0L;
    metadataExtendedFileEntry.ICBTag.NumberofEntries = 1;
    metadataExtendedFileEntry.ICBTag.StrategyType = 4;
    long metadataFileLength = 1L + recursiveGetMetadataFileLength(this.rootUDFImageBuilderFile, this.blockSize);
    if (metadataFileLength % metadataAllocationUnitSize != 0L)
    {
      metadataFileLength += metadataAllocationUnitSize - metadataFileLength % metadataAllocationUnitSize;
    }
    Short_ad metadataAllocationDescriptor = new Short_ad();
    metadataAllocationDescriptor.ExtentPosition = metadataPartitionStartingBlock - partitionStartingBlock;
    metadataAllocationDescriptor.ExtentLength = metadataFileLength * this.blockSize;
    metadataExtendedFileEntry.LogicalBlocksRecorded = metadataFileLength;
    metadataExtendedFileEntry.InformationLength = metadataFileLength * this.blockSize;
    metadataExtendedFileEntry.ObjectSize = metadataFileLength * this.blockSize;
    metadataExtendedFileEntry.AllocationDescriptors = metadataAllocationDescriptor.getBytes();
    metadataExtendedFileEntry.LengthofAllocationDescriptors = metadataExtendedFileEntry.AllocationDescriptors.length;
    metadataExtendedFileEntry.DescriptorTag.TagLocation = mainMetadataFileBlock - partitionStartingBlock;
    metadataExtendedFileEntry.ICBTag.FileType = -6;
    myRandomAccessFile.seek(mainMetadataFileBlock * this.blockSize);
    metadataExtendedFileEntry.write(myRandomAccessFile, this.blockSize);
    long currentMetadataBlock = filesetDescriptorBlock + 1L;
    long currentFiledataBlock = filesetDescriptorBlock + 1L + metadataFileLength;
    long[] nextFreeBlocks = recursiveWriteFilesystemWithMetadata(myRandomAccessFile, 
        partitionStartingBlock, 
        metadataPartitionStartingBlock, 
        this.blockSize, 
        serialNumberForTags, 
        this.rootUDFImageBuilderFile, 
        currentMetadataBlock, 
        currentFiledataBlock, 
        null, 
        0L, 
        descriptorVersion);
    long mirrorMetadataFileBlock = nextFreeBlocks[1];
    metadataExtendedFileEntry.DescriptorTag.TagLocation = mirrorMetadataFileBlock - partitionStartingBlock;
    metadataExtendedFileEntry.ICBTag.FileType = -5;
    myRandomAccessFile.seek(mirrorMetadataFileBlock * this.blockSize);
    metadataExtendedFileEntry.write(myRandomAccessFile, this.blockSize);
    long partitionEndingBlock = mirrorMetadataFileBlock + 1L;
    long AVDP1Block = 256L;
    long AVDP2Block = partitionEndingBlock + 16L;
    long MVDSBlock = 257L;
    long RVDSBlock = partitionEndingBlock;
    writeAnchorVolumeDescriptorPointer(myRandomAccessFile, AVDP1Block, MVDSBlock, RVDSBlock, serialNumberForTags, descriptorVersion);
    writeAnchorVolumeDescriptorPointer(myRandomAccessFile, AVDP2Block, MVDSBlock, RVDSBlock, serialNumberForTags, descriptorVersion);
    long PVD1Block = MVDSBlock;
    long PVD2Block = RVDSBlock;
    long PD1Block = MVDSBlock + 1L;
    long PD2Block = RVDSBlock + 1L;
    long LVD1Block = MVDSBlock + 2L;
    long LVD2Block = RVDSBlock + 2L;
    long USD1Block = MVDSBlock + 3L;
    long USD2Block = RVDSBlock + 3L;
    long IUVD1Block = MVDSBlock + 4L;
    long IUVD2Block = RVDSBlock + 4L;
    long TD1Block = MVDSBlock + 5L;
    long TD2Block = RVDSBlock + 5L;
    writePrimaryVolumeDescriptor(myRandomAccessFile, 1L, PVD1Block, recordingTimeCalendar, serialNumberForTags, descriptorVersion);
    writePrimaryVolumeDescriptor(myRandomAccessFile, 1L, PVD2Block, recordingTimeCalendar, serialNumberForTags, descriptorVersion);
    writePartitionDescriptor(myRandomAccessFile, 2L, PD1Block, partitionStartingBlock, partitionEndingBlock, serialNumberForTags, descriptorVersion);
    writePartitionDescriptor(myRandomAccessFile, 2L, PD2Block, partitionStartingBlock, partitionEndingBlock, serialNumberForTags, descriptorVersion);
    writeLogicalVolumeDescriptor(myRandomAccessFile, 3L, LVD1Block, LVIDSequenceStartingBlock, LVIDSequenceLength, serialNumberForTags, mainMetadataFileBlock - partitionStartingBlock, mirrorMetadataFileBlock - partitionStartingBlock, metadataAllocationUnitSize, metadataAlignmentUnitSize, udfVersionIdentifierSuffix, descriptorVersion, 1, filesetDescriptorBlock - metadataPartitionStartingBlock);
    writeLogicalVolumeDescriptor(myRandomAccessFile, 3L, LVD2Block, LVIDSequenceStartingBlock, LVIDSequenceLength, serialNumberForTags, mainMetadataFileBlock - partitionStartingBlock, mirrorMetadataFileBlock - partitionStartingBlock, metadataAllocationUnitSize, metadataAlignmentUnitSize, udfVersionIdentifierSuffix, descriptorVersion, 1, filesetDescriptorBlock - metadataPartitionStartingBlock);
    writeUnallocatedSpaceDescriptor(myRandomAccessFile, 4L, USD1Block, 19L, 256L, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeUnallocatedSpaceDescriptor(myRandomAccessFile, 4L, USD2Block, 19L, 256L, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeImplementationUseVolumeDescriptor(myRandomAccessFile, 5L, IUVD1Block, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeImplementationUseVolumeDescriptor(myRandomAccessFile, 5L, IUVD2Block, serialNumberForTags, udfVersionIdentifierSuffix, descriptorVersion);
    writeTerminatingDescriptor(myRandomAccessFile, TD1Block, serialNumberForTags, descriptorVersion);
    writeTerminatingDescriptor(myRandomAccessFile, TD2Block, serialNumberForTags, descriptorVersion);
    int currentLVIDSBlock = LVIDSequenceStartingBlock;
    long[] sizeTable = new long[2];
    long[] freeSpaceTable = new long[2];
    sizeTable[0] = partitionEndingBlock - partitionStartingBlock;
    sizeTable[1] = metadataFileLength;
    freeSpaceTable[0] = 0L;
    freeSpaceTable[1] = 0L;
    writeLogicalVolumeIntegrityDescriptor(myRandomAccessFile, currentLVIDSBlock, recordingTimeCalendar, serialNumberForTags, minimumUDFReadRevision, minimumUDFWriteRevision, maximumUDFWriteRevision, descriptorVersion, sizeTable, freeSpaceTable);
    currentLVIDSBlock++;
    writeTerminatingDescriptor(myRandomAccessFile, currentLVIDSBlock, serialNumberForTags, descriptorVersion);
    myRandomAccessFile.close();
  }
  private long recursiveWriteFilesystem(RandomAccessFile myRandomAccessFile, long partitionStartingBlock, int blockSize, int serialNumberForTags, UDFImageBuilderFile currentUDFImageBuilderFile, long currentBlock, FileEntry parentFileEntry, long uniqueID, boolean writeExtendedFileEntries, int descriptorVersion) throws Exception {
    ExtendedFileEntry extendedFileEntry = null;
    FileEntry myFileEntry = null;
    if (!writeExtendedFileEntries) {
      myFileEntry = new FileEntry();
    }
    else {
      extendedFileEntry = new ExtendedFileEntry();
    } 
    ((FileEntry)extendedFileEntry).DescriptorTag.TagSerialNumber = serialNumberForTags;
    ((FileEntry)extendedFileEntry).DescriptorTag.DescriptorVersion = descriptorVersion;
    ((FileEntry)extendedFileEntry).DescriptorTag.TagLocation = currentBlock - partitionStartingBlock;
    ((FileEntry)extendedFileEntry).Uid = -1L;
    ((FileEntry)extendedFileEntry).Gid = -1L;
    ((FileEntry)extendedFileEntry).Permissions = (Permissions.OTHER_Read | Permissions.GROUP_Read | Permissions.OWNER_Read);
    ((FileEntry)extendedFileEntry).FileLinkCount = currentUDFImageBuilderFile.getFileLinkCount();
    ((FileEntry)extendedFileEntry).RecordFormat = 0;
    ((FileEntry)extendedFileEntry).RecordDisplayAttributes = 0;
    ((FileEntry)extendedFileEntry).RecordLength = 0L;
    ((FileEntry)extendedFileEntry).AccessTime = new Timestamp(currentUDFImageBuilderFile.getAccessTime());
    ((FileEntry)extendedFileEntry).ModificationTime = new Timestamp(currentUDFImageBuilderFile.getModificationTime());
    ((FileEntry)extendedFileEntry).AttributeTime = new Timestamp(currentUDFImageBuilderFile.getAttributeTime());
    ((FileEntry)extendedFileEntry).Checkpoint = 1L;
    ((FileEntry)extendedFileEntry).ImplementationIdentifier.setIdentifier(this.applicationIdentifier);
    ((FileEntry)extendedFileEntry).ImplementationIdentifier.IdentifierSuffix = this.applicationIdentifierSuffix;
    ((FileEntry)extendedFileEntry).ICBTag.PriorRecordedNumberofDirectEntries = 0L;
    ((FileEntry)extendedFileEntry).ICBTag.NumberofEntries = 1;
    ((FileEntry)extendedFileEntry).ICBTag.StrategyType = 4;
    ((FileEntry)extendedFileEntry).UniqueID = uniqueID;
    long nextFreeBlock = currentBlock + 1L;
    if (currentUDFImageBuilderFile.getFileType() == UDFImageBuilderFile.FileType.Directory) {
      ((FileEntry)extendedFileEntry).ICBTag.FileType = 4;
      ((FileEntry)extendedFileEntry).Permissions |= (Permissions.OTHER_Execute | Permissions.GROUP_Execute | Permissions.OWNER_Execute);
      UDFImageBuilderFile[] childUDFImageBuilderFiles = currentUDFImageBuilderFile.getChilds();
      ArrayList<FileIdentifierDescriptor> childFileIdentifierDescriptors = new ArrayList<FileIdentifierDescriptor>();
      FileIdentifierDescriptor parentDirectoryFileIdentifierDescriptor = new FileIdentifierDescriptor();
      parentDirectoryFileIdentifierDescriptor.DescriptorTag.TagLocation = currentBlock - partitionStartingBlock;
      parentDirectoryFileIdentifierDescriptor.DescriptorTag.TagSerialNumber = serialNumberForTags;
      parentDirectoryFileIdentifierDescriptor.DescriptorTag.DescriptorVersion = descriptorVersion;
      parentDirectoryFileIdentifierDescriptor.ICB.ExtentLength = blockSize;
      parentDirectoryFileIdentifierDescriptor.ICB.ExtentLocation.part_num = 0;
      parentDirectoryFileIdentifierDescriptor.FileVersionNumber = 1;
      parentDirectoryFileIdentifierDescriptor.FileCharacteristics = 10;
      if (parentFileEntry == null) {
        parentDirectoryFileIdentifierDescriptor.ICB.ExtentLocation.lb_num = currentBlock - partitionStartingBlock;
      }
      else {
        parentDirectoryFileIdentifierDescriptor.ICB.ExtentLocation.lb_num = parentFileEntry.DescriptorTag.TagLocation;
        parentDirectoryFileIdentifierDescriptor.ICB.implementationUse = new byte[6];
        parentDirectoryFileIdentifierDescriptor.ICB.implementationUse[2] = (byte)(int)(parentFileEntry.UniqueID & 0xFFL);
        parentDirectoryFileIdentifierDescriptor.ICB.implementationUse[3] = (byte)(int)(parentFileEntry.UniqueID >> 8L & 0xFFL);
        parentDirectoryFileIdentifierDescriptor.ICB.implementationUse[4] = (byte)(int)(parentFileEntry.UniqueID >> 16L & 0xFFL);
        parentDirectoryFileIdentifierDescriptor.ICB.implementationUse[5] = (byte)(int)(parentFileEntry.UniqueID >> 32L & 0xFFL);
      } 
      childFileIdentifierDescriptors.add(parentDirectoryFileIdentifierDescriptor);
      for (int i = 0; i < childUDFImageBuilderFiles.length; i++) {
        long childFileUniqueID = this.myUniqueIdDisposer.getNextUniqueId();
        FileIdentifierDescriptor childFileIdentifierDescriptor = new FileIdentifierDescriptor();
        childFileIdentifierDescriptor.DescriptorTag.TagLocation = currentBlock - partitionStartingBlock;
        childFileIdentifierDescriptor.DescriptorTag.TagSerialNumber = serialNumberForTags;
        childFileIdentifierDescriptor.DescriptorTag.DescriptorVersion = descriptorVersion;
        childFileIdentifierDescriptor.ICB.ExtentLength = blockSize;
        childFileIdentifierDescriptor.ICB.ExtentLocation.lb_num = nextFreeBlock - partitionStartingBlock;
        childFileIdentifierDescriptor.ICB.ExtentLocation.part_num = 0;
        childFileIdentifierDescriptor.ICB.implementationUse = new byte[6];
        childFileIdentifierDescriptor.ICB.implementationUse[2] = (byte)(int)(childFileUniqueID & 0xFFL);
        childFileIdentifierDescriptor.ICB.implementationUse[3] = (byte)(int)(childFileUniqueID >> 8L & 0xFFL);
        childFileIdentifierDescriptor.ICB.implementationUse[4] = (byte)(int)(childFileUniqueID >> 16L & 0xFFL);
        childFileIdentifierDescriptor.ICB.implementationUse[5] = (byte)(int)(childFileUniqueID >> 32L & 0xFFL);
        childFileIdentifierDescriptor.FileVersionNumber = 1;
        childFileIdentifierDescriptor.setFileIdentifier(childUDFImageBuilderFiles[i].getIdentifier());
        if (childUDFImageBuilderFiles[i].getFileType() == UDFImageBuilderFile.FileType.Directory)
        {
          childFileIdentifierDescriptor.FileCharacteristics = 2;
        }
        childFileIdentifierDescriptors.add(childFileIdentifierDescriptor);
        nextFreeBlock = recursiveWriteFilesystem(myRandomAccessFile, partitionStartingBlock, blockSize, serialNumberForTags, childUDFImageBuilderFiles[i], nextFreeBlock, (FileEntry)extendedFileEntry, childFileUniqueID, writeExtendedFileEntries, descriptorVersion);
      } 
      int directoryFileDataLength = 0;
      for (int j = 0; j < childFileIdentifierDescriptors.size(); j++)
      {
        directoryFileDataLength += ((FileIdentifierDescriptor)childFileIdentifierDescriptors.get(j)).getLength();
      }
      ((FileEntry)extendedFileEntry).InformationLength = directoryFileDataLength;
      if ((writeExtendedFileEntries && directoryFileDataLength <= blockSize - ExtendedFileEntry.fixedPartLength) || (
        !writeExtendedFileEntries && directoryFileDataLength <= blockSize - FileEntry.fixedPartLength))
      {
        ((FileEntry)extendedFileEntry).ICBTag.Flags = 3;
        ((FileEntry)extendedFileEntry).LogicalBlocksRecorded = 0L;
        ((FileEntry)extendedFileEntry).LengthofAllocationDescriptors = directoryFileDataLength;
        ((FileEntry)extendedFileEntry).AllocationDescriptors = new byte[directoryFileDataLength];
        int pos = 0;
        for (int k = 0; k < childFileIdentifierDescriptors.size(); k++)
        {
          byte[] childFileIdentifierDescriptorBytes = ((FileIdentifierDescriptor)childFileIdentifierDescriptors.get(k)).getBytes();
          System.arraycopy(childFileIdentifierDescriptorBytes, 0, ((FileEntry)extendedFileEntry).AllocationDescriptors, pos, childFileIdentifierDescriptorBytes.length);
          pos += childFileIdentifierDescriptorBytes.length;
        }
      }
      else
      {
        ((FileEntry)extendedFileEntry).ICBTag.Flags = 0;
        ((FileEntry)extendedFileEntry).LogicalBlocksRecorded = (directoryFileDataLength / blockSize);
        if (directoryFileDataLength % blockSize != 0)
        {
          ((FileEntry)extendedFileEntry).LogicalBlocksRecorded++;
        }
        Short_ad allocationDescriptor = new Short_ad();
        allocationDescriptor.ExtentLength = directoryFileDataLength;
        allocationDescriptor.ExtentPosition = nextFreeBlock - partitionStartingBlock;
        long currentRealPosition = nextFreeBlock * blockSize;
        myRandomAccessFile.seek(currentRealPosition);
        for (int k = 0; k < childFileIdentifierDescriptors.size(); k++) {
          long tagLocationBlock = currentRealPosition / blockSize - partitionStartingBlock;
          FileIdentifierDescriptor childFileIdentifierDescriptor = childFileIdentifierDescriptors.get(k);
          childFileIdentifierDescriptor.DescriptorTag.TagLocation = tagLocationBlock;
          byte[] childFileIdentifierDescriptorBytes = ((FileIdentifierDescriptor)childFileIdentifierDescriptors.get(k)).getBytes();
          myRandomAccessFile.write(childFileIdentifierDescriptorBytes);
          currentRealPosition += childFileIdentifierDescriptorBytes.length;
        } 
        nextFreeBlock += ((FileEntry)extendedFileEntry).LogicalBlocksRecorded;
        ((FileEntry)extendedFileEntry).AllocationDescriptors = allocationDescriptor.getBytes();
        ((FileEntry)extendedFileEntry).LengthofAllocationDescriptors = ((FileEntry)extendedFileEntry).AllocationDescriptors.length;
      }
    }
    else if (currentUDFImageBuilderFile.getFileType() == UDFImageBuilderFile.FileType.File) {
      ((FileEntry)extendedFileEntry).ICBTag.FileType = 5;
      long fileSize = currentUDFImageBuilderFile.getFileLength();
      ((FileEntry)extendedFileEntry).InformationLength = fileSize;
      if ((writeExtendedFileEntries && fileSize <= (blockSize - ExtendedFileEntry.fixedPartLength)) || (
        !writeExtendedFileEntries && fileSize <= (blockSize - FileEntry.fixedPartLength))) {
        ((FileEntry)extendedFileEntry).ICBTag.Flags = 3;
        ((FileEntry)extendedFileEntry).LogicalBlocksRecorded = 0L;
        ((FileEntry)extendedFileEntry).LengthofAllocationDescriptors = fileSize;
        ((FileEntry)extendedFileEntry).AllocationDescriptors = new byte[(int)fileSize];
        currentUDFImageBuilderFile.readFileData(((FileEntry)extendedFileEntry).AllocationDescriptors);
      }
      else {
        ((FileEntry)extendedFileEntry).ICBTag.Flags = 1;
        ((FileEntry)extendedFileEntry).LogicalBlocksRecorded = fileSize / blockSize;
        if (fileSize % blockSize != 0L)
        {
          ((FileEntry)extendedFileEntry).LogicalBlocksRecorded++;
        }
        ArrayList<Long_ad> allocationDescriptors = new ArrayList<Long_ad>();
        long restFileSize = fileSize;
        long currentExtentPosition = nextFreeBlock - partitionStartingBlock;
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
          currentExtentPosition += this.maximumAllocationLength / blockSize;
          if (this.maximumAllocationLength % blockSize != 0L)
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
        myRandomAccessFile.seek(nextFreeBlock * blockSize);
        writeFileData(myRandomAccessFile, currentUDFImageBuilderFile.getSourceFile());
        nextFreeBlock += ((FileEntry)extendedFileEntry).LogicalBlocksRecorded;
        ((FileEntry)extendedFileEntry).AllocationDescriptors = allocationDescriptorBytes;
        ((FileEntry)extendedFileEntry).LengthofAllocationDescriptors = allocationDescriptorBytes.length;
      } 
    } 
    if (writeExtendedFileEntries) {
      ExtendedFileEntry myExtendedFileEntry = extendedFileEntry;
      myExtendedFileEntry.ObjectSize = ((FileEntry)extendedFileEntry).InformationLength;
      myExtendedFileEntry.CreationTime = new Timestamp(currentUDFImageBuilderFile.getCreationTime());
    } 
    myRandomAccessFile.seek(currentBlock * blockSize);
    extendedFileEntry.write(myRandomAccessFile, blockSize);
    return nextFreeBlock;
  }
  private long[] recursiveWriteFilesystemWithMetadata(RandomAccessFile myRandomAccessFile, long partitionStartingBlock, long metadataPartitionStartingBlock, int blockSize, int serialNumberForTags, UDFImageBuilderFile currentUDFImageBuilderFile, long currentMetadataBlock, long currentFiledataBlock, FileEntry parentFileEntry, long uniqueID, int descriptorVersion) throws Exception {
    long[] nextFreeBlocks = new long[2];
    ExtendedFileEntry myExtendedFileEntry = new ExtendedFileEntry();
    myExtendedFileEntry.DescriptorTag.TagSerialNumber = serialNumberForTags;
    myExtendedFileEntry.DescriptorTag.DescriptorVersion = descriptorVersion;
    myExtendedFileEntry.DescriptorTag.TagLocation = currentMetadataBlock - metadataPartitionStartingBlock;
    myExtendedFileEntry.Uid = -1L;
    myExtendedFileEntry.Gid = -1L;
    myExtendedFileEntry.Permissions = (Permissions.OTHER_Read | Permissions.GROUP_Read | Permissions.OWNER_Read);
    myExtendedFileEntry.FileLinkCount = currentUDFImageBuilderFile.getFileLinkCount();
    myExtendedFileEntry.RecordFormat = 0;
    myExtendedFileEntry.RecordDisplayAttributes = 0;
    myExtendedFileEntry.RecordLength = 0L;
    myExtendedFileEntry.AccessTime = new Timestamp(currentUDFImageBuilderFile.getAccessTime());
    myExtendedFileEntry.ModificationTime = new Timestamp(currentUDFImageBuilderFile.getModificationTime());
    myExtendedFileEntry.AttributeTime = new Timestamp(currentUDFImageBuilderFile.getAttributeTime());
    myExtendedFileEntry.Checkpoint = 1L;
    myExtendedFileEntry.ImplementationIdentifier.setIdentifier(this.applicationIdentifier);
    myExtendedFileEntry.ImplementationIdentifier.IdentifierSuffix = this.applicationIdentifierSuffix;
    myExtendedFileEntry.ICBTag.PriorRecordedNumberofDirectEntries = 0L;
    myExtendedFileEntry.ICBTag.NumberofEntries = 1;
    myExtendedFileEntry.ICBTag.StrategyType = 4;
    myExtendedFileEntry.UniqueID = uniqueID;
    nextFreeBlocks[0] = currentMetadataBlock + 1L;
    nextFreeBlocks[1] = currentFiledataBlock;
    if (currentUDFImageBuilderFile.getFileType() == UDFImageBuilderFile.FileType.Directory) {
      myExtendedFileEntry.ICBTag.FileType = 4;
      myExtendedFileEntry.Permissions |= (Permissions.OTHER_Execute | Permissions.GROUP_Execute | Permissions.OWNER_Execute);
      UDFImageBuilderFile[] childUDFImageBuilderFiles = currentUDFImageBuilderFile.getChilds();
      ArrayList<FileIdentifierDescriptor> childFileIdentifierDescriptors = new ArrayList<FileIdentifierDescriptor>();
      FileIdentifierDescriptor parentDirectoryFileIdentifierDescriptor = new FileIdentifierDescriptor();
      parentDirectoryFileIdentifierDescriptor.DescriptorTag.TagLocation = currentMetadataBlock - metadataPartitionStartingBlock;
      parentDirectoryFileIdentifierDescriptor.DescriptorTag.TagSerialNumber = serialNumberForTags;
      parentDirectoryFileIdentifierDescriptor.DescriptorTag.DescriptorVersion = descriptorVersion;
      parentDirectoryFileIdentifierDescriptor.ICB.ExtentLength = blockSize;
      parentDirectoryFileIdentifierDescriptor.ICB.ExtentLocation.part_num = 1;
      parentDirectoryFileIdentifierDescriptor.FileVersionNumber = 1;
      parentDirectoryFileIdentifierDescriptor.FileCharacteristics = 10;
      if (parentFileEntry == null) {
        parentDirectoryFileIdentifierDescriptor.ICB.ExtentLocation.lb_num = currentMetadataBlock - metadataPartitionStartingBlock;
      }
      else {
        parentDirectoryFileIdentifierDescriptor.ICB.ExtentLocation.lb_num = parentFileEntry.DescriptorTag.TagLocation;
        parentDirectoryFileIdentifierDescriptor.ICB.implementationUse = new byte[6];
        parentDirectoryFileIdentifierDescriptor.ICB.implementationUse[2] = (byte)(int)(parentFileEntry.UniqueID & 0xFFL);
        parentDirectoryFileIdentifierDescriptor.ICB.implementationUse[3] = (byte)(int)(parentFileEntry.UniqueID >> 8L & 0xFFL);
        parentDirectoryFileIdentifierDescriptor.ICB.implementationUse[4] = (byte)(int)(parentFileEntry.UniqueID >> 16L & 0xFFL);
        parentDirectoryFileIdentifierDescriptor.ICB.implementationUse[5] = (byte)(int)(parentFileEntry.UniqueID >> 32L & 0xFFL);
      } 
      childFileIdentifierDescriptors.add(parentDirectoryFileIdentifierDescriptor);
      for (int i = 0; i < childUDFImageBuilderFiles.length; i++) {
        long childFileUniqueID = this.myUniqueIdDisposer.getNextUniqueId();
        FileIdentifierDescriptor childFileIdentifierDescriptor = new FileIdentifierDescriptor();
        childFileIdentifierDescriptor.DescriptorTag.TagLocation = currentMetadataBlock - metadataPartitionStartingBlock;
        childFileIdentifierDescriptor.DescriptorTag.TagSerialNumber = serialNumberForTags;
        childFileIdentifierDescriptor.DescriptorTag.DescriptorVersion = descriptorVersion;
        childFileIdentifierDescriptor.ICB.ExtentLength = blockSize;
        childFileIdentifierDescriptor.ICB.ExtentLocation.lb_num = nextFreeBlocks[0] - metadataPartitionStartingBlock;
        childFileIdentifierDescriptor.ICB.ExtentLocation.part_num = 1;
        childFileIdentifierDescriptor.ICB.implementationUse = new byte[6];
        childFileIdentifierDescriptor.ICB.implementationUse[2] = (byte)(int)(childFileUniqueID & 0xFFL);
        childFileIdentifierDescriptor.ICB.implementationUse[3] = (byte)(int)(childFileUniqueID >> 8L & 0xFFL);
        childFileIdentifierDescriptor.ICB.implementationUse[4] = (byte)(int)(childFileUniqueID >> 16L & 0xFFL);
        childFileIdentifierDescriptor.ICB.implementationUse[5] = (byte)(int)(childFileUniqueID >> 32L & 0xFFL);
        childFileIdentifierDescriptor.FileVersionNumber = 1;
        childFileIdentifierDescriptor.setFileIdentifier(childUDFImageBuilderFiles[i].getIdentifier());
        if (childUDFImageBuilderFiles[i].getFileType() == UDFImageBuilderFile.FileType.Directory)
        {
          childFileIdentifierDescriptor.FileCharacteristics = 2;
        }
        childFileIdentifierDescriptors.add(childFileIdentifierDescriptor);
        nextFreeBlocks = recursiveWriteFilesystemWithMetadata(myRandomAccessFile, partitionStartingBlock, metadataPartitionStartingBlock, blockSize, serialNumberForTags, childUDFImageBuilderFiles[i], nextFreeBlocks[0], nextFreeBlocks[1], (FileEntry)myExtendedFileEntry, childFileUniqueID, descriptorVersion);
      } 
      int directoryFileDataLength = 0;
      for (int j = 0; j < childFileIdentifierDescriptors.size(); j++)
      {
        directoryFileDataLength += ((FileIdentifierDescriptor)childFileIdentifierDescriptors.get(j)).getLength();
      }
      myExtendedFileEntry.InformationLength = directoryFileDataLength;
      if (directoryFileDataLength <= blockSize - ExtendedFileEntry.fixedPartLength)
      {
        myExtendedFileEntry.ICBTag.Flags = 3;
        myExtendedFileEntry.LogicalBlocksRecorded = 0L;
        myExtendedFileEntry.LengthofAllocationDescriptors = directoryFileDataLength;
        myExtendedFileEntry.AllocationDescriptors = new byte[directoryFileDataLength];
        int pos = 0;
        for (int k = 0; k < childFileIdentifierDescriptors.size(); k++)
        {
          byte[] childFileIdentifierDescriptorBytes = ((FileIdentifierDescriptor)childFileIdentifierDescriptors.get(k)).getBytes();
          System.arraycopy(childFileIdentifierDescriptorBytes, 0, myExtendedFileEntry.AllocationDescriptors, pos, childFileIdentifierDescriptorBytes.length);
          pos += childFileIdentifierDescriptorBytes.length;
        }
      }
      else
      {
        myExtendedFileEntry.ICBTag.Flags = 0;
        myExtendedFileEntry.LogicalBlocksRecorded = (directoryFileDataLength / blockSize);
        if (directoryFileDataLength % blockSize != 0)
        {
          ((FileEntry)myExtendedFileEntry).LogicalBlocksRecorded++;
        }
        Short_ad allocationDescriptor = new Short_ad();
        allocationDescriptor.ExtentLength = directoryFileDataLength;
        allocationDescriptor.ExtentPosition = nextFreeBlocks[0] - metadataPartitionStartingBlock;
        long currentRealPosition = nextFreeBlocks[0] * blockSize;
        myRandomAccessFile.seek(currentRealPosition);
        for (int k = 0; k < childFileIdentifierDescriptors.size(); k++) {
          long tagLocationBlock = currentRealPosition / blockSize - metadataPartitionStartingBlock;
          FileIdentifierDescriptor childFileIdentifierDescriptor = childFileIdentifierDescriptors.get(k);
          childFileIdentifierDescriptor.DescriptorTag.TagLocation = tagLocationBlock;
          byte[] childFileIdentifierDescriptorBytes = ((FileIdentifierDescriptor)childFileIdentifierDescriptors.get(k)).getBytes();
          myRandomAccessFile.write(childFileIdentifierDescriptorBytes);
          currentRealPosition += childFileIdentifierDescriptorBytes.length;
        } 
        nextFreeBlocks[0] = nextFreeBlocks[0] + myExtendedFileEntry.LogicalBlocksRecorded;
        myExtendedFileEntry.AllocationDescriptors = allocationDescriptor.getBytes();
        myExtendedFileEntry.LengthofAllocationDescriptors = myExtendedFileEntry.AllocationDescriptors.length;
      }
    }
    else if (currentUDFImageBuilderFile.getFileType() == UDFImageBuilderFile.FileType.File) {
      myExtendedFileEntry.ICBTag.FileType = 5;
      long fileSize = currentUDFImageBuilderFile.getFileLength();
      myExtendedFileEntry.InformationLength = fileSize;
      if (fileSize <= (blockSize - ExtendedFileEntry.fixedPartLength)) {
        myExtendedFileEntry.ICBTag.Flags = 3;
        myExtendedFileEntry.LogicalBlocksRecorded = 0L;
        myExtendedFileEntry.LengthofAllocationDescriptors = fileSize;
        myExtendedFileEntry.AllocationDescriptors = new byte[(int)fileSize];
        currentUDFImageBuilderFile.readFileData(myExtendedFileEntry.AllocationDescriptors);
      }
      else {
        myExtendedFileEntry.ICBTag.Flags = 1;
        myExtendedFileEntry.LogicalBlocksRecorded = fileSize / blockSize;
        if (fileSize % blockSize != 0L)
        {
          ((FileEntry)myExtendedFileEntry).LogicalBlocksRecorded++;
        }
        ArrayList<Long_ad> allocationDescriptors = new ArrayList<Long_ad>();
        long restFileSize = fileSize;
        long currentExtentPosition = nextFreeBlocks[1] - partitionStartingBlock;
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
          currentExtentPosition += this.maximumAllocationLength / blockSize;
          if (this.maximumAllocationLength % blockSize != 0L)
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
        myRandomAccessFile.seek(nextFreeBlocks[1] * blockSize);
        writeFileData(myRandomAccessFile, currentUDFImageBuilderFile.getSourceFile());
        nextFreeBlocks[1] = nextFreeBlocks[1] + myExtendedFileEntry.LogicalBlocksRecorded;
        myExtendedFileEntry.AllocationDescriptors = allocationDescriptorBytes;
        myExtendedFileEntry.LengthofAllocationDescriptors = allocationDescriptorBytes.length;
      } 
    } 
    myExtendedFileEntry.ObjectSize = myExtendedFileEntry.InformationLength;
    myExtendedFileEntry.CreationTime = new Timestamp(currentUDFImageBuilderFile.getCreationTime());
    myRandomAccessFile.seek(currentMetadataBlock * blockSize);
    myExtendedFileEntry.write(myRandomAccessFile, blockSize);
    return nextFreeBlocks;
  }
  private void writeFileData(RandomAccessFile myRandomAccessFile, File sourceFile) throws IOException {
    RandomAccessFile sourceRandomAccessFile = new RandomAccessFile(sourceFile, "r");
    byte[] buffer = new byte[32768];
    int bytesRead = 0;
    while ((bytesRead = sourceRandomAccessFile.read(buffer)) > 0)
    {
      myRandomAccessFile.write(buffer, 0, bytesRead);
    }
    sourceRandomAccessFile.close();
  }
  private void writeFilesetDescriptor(RandomAccessFile myRandomAccessFile, long targetBlock, long rootDirectoryBlock, int partitionNumber, long partitionStartingBlock, Calendar recordingTimeCalendar, int tagSerialNumber, byte[] udfVersionIdentifierSuffix, int descriptorVersion) throws Exception {
    FileSetDescriptor myFilesetDescriptor = new FileSetDescriptor();
    myFilesetDescriptor.DescriptorTag.TagSerialNumber = tagSerialNumber;
    myFilesetDescriptor.DescriptorTag.DescriptorVersion = descriptorVersion;
    myFilesetDescriptor.DescriptorTag.TagLocation = targetBlock - partitionStartingBlock;
    myFilesetDescriptor.RecordingDateandTime.set(recordingTimeCalendar);
    myFilesetDescriptor.InterchangeLevel = 3;
    myFilesetDescriptor.MaximumInterchangeLevel = 3;
    myFilesetDescriptor.CharacterSetList = 1L;
    myFilesetDescriptor.MaximumCharacterSetList = 1L;
    myFilesetDescriptor.FileSetNumber = 0L;
    myFilesetDescriptor.FileSetDescriptorNumber = 0L;
    myFilesetDescriptor.setLogicalVolumeIdentifier(this.imageIdentifier);
    myFilesetDescriptor.setFileSetIdentifier(this.imageIdentifier);
    myFilesetDescriptor.RootDirectoryICB.ExtentLength = this.blockSize;
    myFilesetDescriptor.RootDirectoryICB.ExtentLocation.part_num = partitionNumber;
    myFilesetDescriptor.RootDirectoryICB.ExtentLocation.lb_num = rootDirectoryBlock - partitionStartingBlock;
    myFilesetDescriptor.DomainIdentifier.setIdentifier("*OSTA UDF Compliant");
    myFilesetDescriptor.DomainIdentifier.IdentifierSuffix = udfVersionIdentifierSuffix;
    myRandomAccessFile.seek(targetBlock * this.blockSize);
    myFilesetDescriptor.write(myRandomAccessFile, this.blockSize);
  }
  private void writeAnchorVolumeDescriptorPointer(RandomAccessFile myRandomAccessFile, long targetBlock, long MVDSBlock, long RVDSBlock, int tagSerialNumber, int descriptorVersion) throws IOException {
    AnchorVolumeDescriptorPointer myAnchorVolumeDescriptorPointer = new AnchorVolumeDescriptorPointer();
    myAnchorVolumeDescriptorPointer.DescriptorTag.TagSerialNumber = tagSerialNumber;
    myAnchorVolumeDescriptorPointer.DescriptorTag.DescriptorVersion = descriptorVersion;
    myAnchorVolumeDescriptorPointer.DescriptorTag.TagLocation = targetBlock;
    myAnchorVolumeDescriptorPointer.MainVolumeDescriptorSequenceExtend.len = (16 * this.blockSize);
    myAnchorVolumeDescriptorPointer.MainVolumeDescriptorSequenceExtend.loc = MVDSBlock;
    myAnchorVolumeDescriptorPointer.ReserveVolumeDescriptorSequenceExtend.len = (16 * this.blockSize);
    myAnchorVolumeDescriptorPointer.ReserveVolumeDescriptorSequenceExtend.loc = RVDSBlock;
    myRandomAccessFile.seek(targetBlock * this.blockSize);
    myAnchorVolumeDescriptorPointer.write(myRandomAccessFile, this.blockSize);
  }
  private void writePrimaryVolumeDescriptor(RandomAccessFile myRandomAccessFile, long volumeDescriptorSequenceNumber, long targetBlock, Calendar recordingTimeCalendar, int tagSerialNumber, int descriptorVersion) throws Exception {
    PrimaryVolumeDescriptor myPrimaryVolumeDescriptor = new PrimaryVolumeDescriptor();
    myPrimaryVolumeDescriptor.DescriptorTag.TagSerialNumber = tagSerialNumber;
    myPrimaryVolumeDescriptor.DescriptorTag.DescriptorVersion = descriptorVersion;
    myPrimaryVolumeDescriptor.DescriptorTag.TagLocation = targetBlock;
    myPrimaryVolumeDescriptor.VolumeDescriptorSequenceNumber = volumeDescriptorSequenceNumber;
    myPrimaryVolumeDescriptor.PrimaryVolumeDescriptorNumber = 0L;
    myPrimaryVolumeDescriptor.setVolumeIdentifier(this.imageIdentifier);
    myPrimaryVolumeDescriptor.VolumeSequenceNumber = 1;
    myPrimaryVolumeDescriptor.MaximumVolumeSequenceNumber = 1;
    myPrimaryVolumeDescriptor.InterchangeLevel = 2;
    myPrimaryVolumeDescriptor.MaximumInterchangeLevel = 3;
    myPrimaryVolumeDescriptor.CharacterSetList = 1L;
    myPrimaryVolumeDescriptor.MaximumCharacterSetList = 1L;
    String volumeSetIdentifier = String.valueOf(Long.toHexString(recordingTimeCalendar.getTimeInMillis())) + " " + this.imageIdentifier;
    myPrimaryVolumeDescriptor.setVolumeSetIdentifier(volumeSetIdentifier);
    myPrimaryVolumeDescriptor.ApplicationIdentifier.setIdentifier(this.applicationIdentifier);
    myPrimaryVolumeDescriptor.ApplicationIdentifier.IdentifierSuffix = this.applicationIdentifierSuffix;
    myPrimaryVolumeDescriptor.RecordingDateandTime.set(recordingTimeCalendar);
    myPrimaryVolumeDescriptor.ImplementationIdentifier.setIdentifier(this.applicationIdentifier);
    myPrimaryVolumeDescriptor.PredecessorVolumeDescriptorSequenceLocation = 0L;
    myPrimaryVolumeDescriptor.Flags = 1;
    myRandomAccessFile.seek(targetBlock * this.blockSize);
    myPrimaryVolumeDescriptor.write(myRandomAccessFile, this.blockSize);
  }
  private void writePartitionDescriptor(RandomAccessFile myRandomAccessFile, long volumeDescriptorSequenceNumber, long targetBlock, long partitionStartingBlock, long partitionEndingBlock, int tagSerialNumber, int descriptorVersion) throws Exception {
    PartitionDescriptor myPartitionDescriptor = new PartitionDescriptor();
    myPartitionDescriptor.DescriptorTag.TagSerialNumber = tagSerialNumber;
    myPartitionDescriptor.DescriptorTag.DescriptorVersion = descriptorVersion;
    myPartitionDescriptor.DescriptorTag.TagLocation = targetBlock;
    myPartitionDescriptor.VolumeDescriptorSequenceNumber = volumeDescriptorSequenceNumber;
    myPartitionDescriptor.PartitionFlags = 1;
    myPartitionDescriptor.PartitionNumber = 0;
    if (descriptorVersion == 2) {
      myPartitionDescriptor.PartitionContents.setIdentifier("+NSR02");
    }
    else {
      myPartitionDescriptor.PartitionContents.setIdentifier("+NSR03");
    } 
    myPartitionDescriptor.AccessType = 1L;
    myPartitionDescriptor.PartitonStartingLocation = partitionStartingBlock;
    myPartitionDescriptor.PartitionLength = partitionEndingBlock - partitionStartingBlock;
    myPartitionDescriptor.ImplementationIdentifier.setIdentifier(this.applicationIdentifier);
    myPartitionDescriptor.ImplementationIdentifier.IdentifierSuffix = this.applicationIdentifierSuffix;
    myRandomAccessFile.seek(targetBlock * this.blockSize);
    myPartitionDescriptor.write(myRandomAccessFile, this.blockSize);
  }
  private void writeLogicalVolumeDescriptor(RandomAccessFile myRandomAccessFile, long volumeDescriptorSequenceNumber, long targetBlock, long LVIDSequenceStartingBlock, long LVIDSequenceLength, int tagSerialNumber, byte[] udfVersionIdentifierSuffix, int descriptorVersion) throws Exception {
    writeLogicalVolumeDescriptor(myRandomAccessFile, volumeDescriptorSequenceNumber, targetBlock, LVIDSequenceStartingBlock, LVIDSequenceLength, tagSerialNumber, -1L, -1L, 0, 0, udfVersionIdentifierSuffix, descriptorVersion, 0, 1L);
  }
  private void writeLogicalVolumeDescriptor(RandomAccessFile myRandomAccessFile, long volumeDescriptorSequenceNumber, long targetBlock, long LVIDSequenceStartingBlock, long LVIDSequenceLength, int tagSerialNumber, long metadataFileLocation1, long metadataFileLocation2, int metadataAllocationUnitSize, int metadataAlignmentUnitSize, byte[] udfVersionIdentifierSuffix, int descriptorVersion, int filesetPartition, long filesetLocation) throws Exception {
    LogicalVolumeDescriptor myLogicalVolumeDescriptor = new LogicalVolumeDescriptor();
    myLogicalVolumeDescriptor.DescriptorTag.TagSerialNumber = tagSerialNumber;
    myLogicalVolumeDescriptor.DescriptorTag.DescriptorVersion = descriptorVersion;
    myLogicalVolumeDescriptor.DescriptorTag.TagLocation = targetBlock;
    myLogicalVolumeDescriptor.VolumeDescriptorSequenceNumber = volumeDescriptorSequenceNumber;
    myLogicalVolumeDescriptor.setLogicalVolumeIdentifier(this.imageIdentifier);
    myLogicalVolumeDescriptor.LogicalBlockSize = this.blockSize;
    myLogicalVolumeDescriptor.DomainIdentifier.setIdentifier("*OSTA UDF Compliant");
    myLogicalVolumeDescriptor.DomainIdentifier.IdentifierSuffix = udfVersionIdentifierSuffix;
    myLogicalVolumeDescriptor.LogicalVolumeContentsUse.ExtentLength = this.blockSize;
    myLogicalVolumeDescriptor.LogicalVolumeContentsUse.ExtentLocation.part_num = filesetPartition;
    myLogicalVolumeDescriptor.LogicalVolumeContentsUse.ExtentLocation.lb_num = filesetLocation;
    myLogicalVolumeDescriptor.ImplementationIdentifier.setIdentifier(this.applicationIdentifier);
    myLogicalVolumeDescriptor.ImplementationIdentifier.IdentifierSuffix = this.applicationIdentifierSuffix;
    PartitionMapType1 myPartitionMapType1 = new PartitionMapType1();
    byte[] myPartitionMapType1Bytes = myPartitionMapType1.getBytes();
    if (metadataFileLocation1 > 0L) {
      PartitionMapType2 myPartitionMapType2 = new PartitionMapType2();
      EntityID partitionTypeIdentifier = new EntityID();
      partitionTypeIdentifier.setIdentifier("*UDF Metadata Partition");
      partitionTypeIdentifier.IdentifierSuffix = udfVersionIdentifierSuffix;
      myPartitionMapType2.setupMetadataPartitionMap(partitionTypeIdentifier, 1, 0, metadataFileLocation1, metadataFileLocation2, -1L, metadataAllocationUnitSize, metadataAlignmentUnitSize, (byte)0);
      byte[] myPartitionMapType2Bytes = myPartitionMapType2.getBytes();
      myLogicalVolumeDescriptor.NumberofPartitionMaps = 2L;
      myLogicalVolumeDescriptor.PartitionMaps = new byte[myPartitionMapType1Bytes.length + myPartitionMapType2Bytes.length];
      System.arraycopy(myPartitionMapType1Bytes, 0, myLogicalVolumeDescriptor.PartitionMaps, 0, myPartitionMapType1Bytes.length);
      System.arraycopy(myPartitionMapType2Bytes, 0, myLogicalVolumeDescriptor.PartitionMaps, 6, myPartitionMapType2Bytes.length);
    }
    else {
      myLogicalVolumeDescriptor.NumberofPartitionMaps = 1L;
      myLogicalVolumeDescriptor.PartitionMaps = myPartitionMapType1Bytes;
    } 
    myLogicalVolumeDescriptor.MapTableLength = myLogicalVolumeDescriptor.PartitionMaps.length;
    myLogicalVolumeDescriptor.IntegritySequenceExtent.loc = LVIDSequenceStartingBlock;
    myLogicalVolumeDescriptor.IntegritySequenceExtent.len = LVIDSequenceLength;
    myRandomAccessFile.seek(targetBlock * this.blockSize);
    myLogicalVolumeDescriptor.write(myRandomAccessFile, this.blockSize);
  }
  private void writeUnallocatedSpaceDescriptor(RandomAccessFile myRandomAccessFile, long volumeDescriptorSequenceNumber, long targetBlock, long unallocatedSpaceStartBlock, long unallocatedSpaceEndBlock, int tagSerialNumber, byte[] udfVersionIdentifierSuffix, int descriptorVersion) throws IOException {
    UnallocatedSpaceDescriptor myUnallocatedSpaceDescriptor = new UnallocatedSpaceDescriptor();
    myUnallocatedSpaceDescriptor.DescriptorTag.TagSerialNumber = tagSerialNumber;
    myUnallocatedSpaceDescriptor.DescriptorTag.DescriptorVersion = descriptorVersion;
    myUnallocatedSpaceDescriptor.DescriptorTag.TagLocation = targetBlock;
    myUnallocatedSpaceDescriptor.VolumeDescriptorSequenceNumber = volumeDescriptorSequenceNumber;
    myUnallocatedSpaceDescriptor.NumberofAllocationDescriptors = 1L;
    myUnallocatedSpaceDescriptor.AllocationDescriptors = new Extend_ad[1];
    myUnallocatedSpaceDescriptor.AllocationDescriptors[0] = new Extend_ad();
    (myUnallocatedSpaceDescriptor.AllocationDescriptors[0]).loc = unallocatedSpaceStartBlock;
    (myUnallocatedSpaceDescriptor.AllocationDescriptors[0]).len = (unallocatedSpaceEndBlock - unallocatedSpaceStartBlock) * this.blockSize;
    myRandomAccessFile.seek(targetBlock * this.blockSize);
    myUnallocatedSpaceDescriptor.write(myRandomAccessFile, this.blockSize);
  }
  private void writeImplementationUseVolumeDescriptor(RandomAccessFile myRandomAccessFile, long volumeDescriptorSequenceNumber, long targetBlock, int tagSerialNumber, byte[] udfVersionIdentifierSuffix, int descriptorVersion) throws Exception {
    ImplementationUseVolumeDescriptor myImplementationUseVolumeDescriptor = new ImplementationUseVolumeDescriptor();
    myImplementationUseVolumeDescriptor.DescriptorTag.TagSerialNumber = tagSerialNumber;
    myImplementationUseVolumeDescriptor.DescriptorTag.DescriptorVersion = descriptorVersion;
    myImplementationUseVolumeDescriptor.DescriptorTag.TagLocation = targetBlock;
    myImplementationUseVolumeDescriptor.VolumeDescriptorSequenceNumber = volumeDescriptorSequenceNumber;
    myImplementationUseVolumeDescriptor.ImplementationIdentifier.setIdentifier("*UDF LV Info");
    myImplementationUseVolumeDescriptor.ImplementationIdentifier.IdentifierSuffix = udfVersionIdentifierSuffix;
    myImplementationUseVolumeDescriptor.ImplementationUse.ImplementationID.setIdentifier(this.applicationIdentifier);
    myImplementationUseVolumeDescriptor.ImplementationUse.ImplementationID.IdentifierSuffix = this.applicationIdentifierSuffix;
    myImplementationUseVolumeDescriptor.ImplementationUse.setLogicalVolumeIdentifier(this.imageIdentifier);
    myRandomAccessFile.seek(targetBlock * this.blockSize);
    myImplementationUseVolumeDescriptor.write(myRandomAccessFile, this.blockSize);
  }
  private void writeTerminatingDescriptor(RandomAccessFile myRandomAccessFile, long targetBlock, int tagSerialNumber, int descriptorVersion) throws IOException {
    TerminatingDescriptor myTerminatingDescriptor = new TerminatingDescriptor();
    myTerminatingDescriptor.DescriptorTag.TagSerialNumber = tagSerialNumber;
    myTerminatingDescriptor.DescriptorTag.DescriptorVersion = descriptorVersion;
    myTerminatingDescriptor.DescriptorTag.TagLocation = targetBlock;
    myRandomAccessFile.seek(targetBlock * this.blockSize);
    myTerminatingDescriptor.write(myRandomAccessFile, this.blockSize);
  }
  private void writeLogicalVolumeIntegrityDescriptor(RandomAccessFile myRandomAccessFile, long targetBlock, Calendar recordingTimeCalendar, int tagSerialNumber, int minimumUDFReadRevision, int minimumUDFWriteRevision, int maximumUDFWriteRevision, int descriptorVersion, long[] sizeTable, long[] freeSpaceTable) throws Exception {
    LogicalVolumeIntegrityDescriptor myLogicalVolumeIntegrityDescriptor = new LogicalVolumeIntegrityDescriptor();
    myLogicalVolumeIntegrityDescriptor.DescriptorTag.TagLocation = targetBlock;
    myLogicalVolumeIntegrityDescriptor.DescriptorTag.DescriptorVersion = descriptorVersion;
    myLogicalVolumeIntegrityDescriptor.DescriptorTag.TagSerialNumber = tagSerialNumber;
    myLogicalVolumeIntegrityDescriptor.RecordingDateAndTime.set(recordingTimeCalendar);
    myLogicalVolumeIntegrityDescriptor.IntegrityType = 1L;
    myLogicalVolumeIntegrityDescriptor.NumberOfPartitions = sizeTable.length;
    myLogicalVolumeIntegrityDescriptor.FreeSpaceTable = freeSpaceTable;
    myLogicalVolumeIntegrityDescriptor.SizeTable = sizeTable;
    myLogicalVolumeIntegrityDescriptor.LogicalVolumeContensUse.UniqueID = this.myUniqueIdDisposer.getNextUniqueId();
    myLogicalVolumeIntegrityDescriptor.LengthOfImplementationUse = 46L;
    EntityID implementationId = new EntityID();
    implementationId.setIdentifier(this.applicationIdentifier);
    implementationId.IdentifierSuffix = this.applicationIdentifierSuffix;
    long numberOfFiles = this.rootUDFImageBuilderFile.getFileCount();
    long numberOfDirectories = this.rootUDFImageBuilderFile.getDirectoryCount();
    myLogicalVolumeIntegrityDescriptor.setImplementationUse(implementationId, numberOfFiles, numberOfDirectories, minimumUDFReadRevision, minimumUDFWriteRevision, maximumUDFWriteRevision);
    myRandomAccessFile.seek(targetBlock * this.blockSize);
    myLogicalVolumeIntegrityDescriptor.write(myRandomAccessFile, this.blockSize);
  }
  private long recursiveGetMetadataFileLength(UDFImageBuilderFile myUDFImageBuilderFile, int blockSize) throws Exception {
    long wholeMetadataLengthInBlocks = 0L;
    if (myUDFImageBuilderFile.getFileType() == UDFImageBuilderFile.FileType.File) {
      wholeMetadataLengthInBlocks = 1L;
    }
    else {
      long FileIdentifierDescriptorsLength = 0L;
      wholeMetadataLengthInBlocks++;
      FileIdentifierDescriptor parentDirectoryFileIdentifierDescriptor = new FileIdentifierDescriptor();
      FileIdentifierDescriptorsLength += parentDirectoryFileIdentifierDescriptor.getLength();
      UDFImageBuilderFile[] childUDFImageBuilderFiles = myUDFImageBuilderFile.getChilds();
      for (int i = 0; i < childUDFImageBuilderFiles.length; i++) {
        FileIdentifierDescriptor childFileIdentifierDescriptor = new FileIdentifierDescriptor();
        childFileIdentifierDescriptor.setFileIdentifier(childUDFImageBuilderFiles[i].getIdentifier());
        FileIdentifierDescriptorsLength += childFileIdentifierDescriptor.getLength();
        wholeMetadataLengthInBlocks += recursiveGetMetadataFileLength(childUDFImageBuilderFiles[i], blockSize);
      } 
      if (FileIdentifierDescriptorsLength > (blockSize - ExtendedFileEntry.fixedPartLength)) {
        long additionalBlocks = FileIdentifierDescriptorsLength / blockSize;
        if (FileIdentifierDescriptorsLength % blockSize != 0L)
        {
          additionalBlocks++;
        }
        wholeMetadataLengthInBlocks += additionalBlocks;
      } 
    } 
    return wholeMetadataLengthInBlocks;
  }
}
