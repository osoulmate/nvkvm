package com.kvm;
import java.util.ArrayList;
import java.util.Iterator;
public class UdfLayoutInformation
{
  private UniqueIdDisposer myUniqueIdDisposer;
  public int blockSize;
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
  public int partitionToStoreMetadataOn;
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
  public long nextUniqueId;
  ArrayList<UDFImageBuilderFile> linearUDFImageBuilderFileOrdering;
  public static final int fixedPartLength = 176;
  public void init(UDFImageBuilderFile rootUDFImageBuilderFile, String version, int blockSize) {
    this.myUniqueIdDisposer = new UniqueIdDisposer();
    this.linearUDFImageBuilderFileOrdering = new ArrayList<>();
    this.blockSize = blockSize;
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
    this.partitionToStoreMetadataOn = 0;
    this.FSDLocation = 1L;
    this.rootFEBlock = this.physicalPartitionStartingBlock + 2L;
    this.rootFELocation = 2L;
    long[] initCurrentBlock = { this.rootFEBlock, 0L };
    long[] currentBlock = recursiveGetFileEntryLocation(rootUDFImageBuilderFile, initCurrentBlock, version);
    this.nextUniqueId = this.myUniqueIdDisposer.getNextUniqueId();
    this.physicalPartitionEndingBlock = currentBlock[0] + currentBlock[1];
    for (int i = 0; i < this.linearUDFImageBuilderFileOrdering.size(); i++) {
      UDFImageBuilderFile myUdfImageFile = this.linearUDFImageBuilderFileOrdering.get(i);
      if (myUdfImageFile.getFileType() == FileType.File && 
        myUdfImageFile.getFileEntryPosition().getDataBlock() != -1L) {
        myUdfImageFile.getFileEntryPosition().setDataBlock(myUdfImageFile
            .getFileEntryPosition().getDataBlock() + currentBlock[0]);
        myUdfImageFile.getFileEntryPosition().setDataLocation(myUdfImageFile
            .getFileEntryPosition().getDataBlock() - this.physicalPartitionStartingBlock);
      } 
    } 
    this.sizeTable = new long[1];
    this.sizeTable[0] = this.physicalPartitionEndingBlock - this.physicalPartitionStartingBlock;
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
  private long[] recursiveGetFileEntryLocation(UDFImageBuilderFile currentUDFImageBuilderFile, long[] currentBlock, String version) {
    FileEntryPosition myFileEntryPosition = new FileEntryPosition();
    if (currentUDFImageBuilderFile.getIdentifier().equals("")) {
      myFileEntryPosition.setUniqueIds(0L);
    } else {
      myFileEntryPosition.setUniqueIds(this.myUniqueIdDisposer.getNextUniqueId());
    } 
    this.linearUDFImageBuilderFileOrdering.add(currentUDFImageBuilderFile);
    myFileEntryPosition.setEntryBlock(currentBlock[0]);
    currentBlock[0] = currentBlock[0] + 1L;
    if (myFileEntryPosition.getEntryBlock() - this.physicalPartitionStartingBlock > 0L) {
      myFileEntryPosition.setEntryLocation(myFileEntryPosition
          .getEntryBlock() - this.physicalPartitionStartingBlock);
    }
    if (currentUDFImageBuilderFile.getFileType() == FileType.File) {
      if (currentUDFImageBuilderFile.getFileLength() > (this.blockSize - 176)) {
        myFileEntryPosition.setDataBlock(currentBlock[1]);
        currentBlock[1] = currentBlock[1] + (long)Math.ceil(currentUDFImageBuilderFile
            .getFileLength() / this.blockSize);
      } else {
        myFileEntryPosition.setDataBlock(-1L);
      }
    } else if (currentUDFImageBuilderFile.getFileType() == FileType.Directory) {
      long fidLength = 40L;
      ArrayList<UDFImageBuilderFile> childCurrentUDFImageBuilderFiles = currentUDFImageBuilderFile.getChilds();
      for (int i = 0; i < childCurrentUDFImageBuilderFiles.size(); i++) {
        UDFImageBuilderFile cudfImageFile = childCurrentUDFImageBuilderFiles.get(i);
        String cidentifier = cudfImageFile.getIdentifier();
        byte[] cib = stringToBytes1(cidentifier);
        long lengthWithoutPadding = (38 + cib.length);
        long paddingLength = (lengthWithoutPadding % 4L == 0L) ? 0L : (4L - lengthWithoutPadding % 4L);
        long cfidLength = lengthWithoutPadding + paddingLength;
        fidLength += cfidLength;
      } 
      if (fidLength > (this.blockSize - 176)) {
        myFileEntryPosition.setDataBlock(currentBlock[0]);
        if (myFileEntryPosition.getDataBlock() > this.physicalPartitionStartingBlock) {
          myFileEntryPosition.setDataLocation(myFileEntryPosition.getDataBlock() - this.physicalPartitionStartingBlock);
        }
        currentBlock[0] = currentBlock[0] + (long)Math.ceil(fidLength / this.blockSize);
      } else {
        myFileEntryPosition.setDataBlock(-1L);
      } 
      for (int j = 0; j < childCurrentUDFImageBuilderFiles.size(); j++) {
        currentBlock = recursiveGetFileEntryLocation(childCurrentUDFImageBuilderFiles
            .get(j), currentBlock, version);
      }
    } 
    currentUDFImageBuilderFile.setFileEntryPosition(myFileEntryPosition);
    return currentBlock;
  }
  private byte[] stringToBytes1(String str) {
    ArrayList<Byte> st8 = new ArrayList<>();
    byte st8h = 8;
    st8.add(Byte.valueOf(st8h));
    ArrayList<Byte> st16 = new ArrayList<>();
    byte st16h = 16;
    st16.add(Byte.valueOf(st16h));
    boolean is_utf16 = false;
    for (int i = 0; i < str.length(); i++) {
      int ch = str.charAt(i);
      if (ch > 0 && ch < 128) {
        st8.add(Byte.valueOf((byte)(ch & 0xFF)));
        st16.add(Byte.valueOf((byte)0));
        st16.add(Byte.valueOf((byte)(ch & 0xFF)));
      } else {
        st16.add(Byte.valueOf((byte)(ch >> 8 & 0xFF)));
        st16.add(Byte.valueOf((byte)(ch & 0xFF)));
        is_utf16 = true;
      } 
    } 
    byte[] byter8 = new byte[st8.size()];
    byte[] byter16 = new byte[st16.size()];
    int j = 0;
    for (Iterator<Byte> iterator1 = st8.iterator(); iterator1.hasNext(); ) { byte b = ((Byte)iterator1.next()).byteValue();
      byter8[j] = b;
      j++; }
    int k = 0;
    for (Iterator<Byte> iterator2 = st16.iterator(); iterator2.hasNext(); ) { byte b = ((Byte)iterator2.next()).byteValue();
      byter16[k++] = b; }
    return is_utf16 ? byter16 : byter8;
  }
}
