package de.tu_darmstadt.informatik.rbg.bstickler.udflib;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.handler.DescriptorTagHandler;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.handler.PaddingHandler;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.handler.SerializationHandler;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.handler.UDF102Handler;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.handler.UDF201Handler;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.handler.UDF260Handler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.ContentHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.HandlerException;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.StreamHandler;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.StructureHandler;
import java.io.File;
import java.util.Calendar;
import java.util.Iterator;
public class SabreUDFImageBuilder
{
  private String imageIdentifier = "SabreUDFImageBuilder Disc";
  private String applicationIdentifier = "*SabreUDFImageBuilder";
  private byte[] applicationIdentifierSuffix = new byte[] { 1 };
  private int blockSize = 2048;
  private UDFImageBuilderFile rootUDFImageBuilderFile;
  SabreUDFElementFactory mySabreUDFElementFactory = null;
  public SabreUDFElementFactory getMySabreUDFElementFactory() {
    return this.mySabreUDFElementFactory;
  }
  private StreamHandler serializationHandler = null;
  public StreamHandler getSerializationHandler() {
    return this.serializationHandler;
  }
  public SabreUDFImageBuilder() {
    this.rootUDFImageBuilderFile = new UDFImageBuilderFile("");
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
  public UDFImageBuilderFile getRootUDFImageBuilderFile() {
    return this.rootUDFImageBuilderFile;
  }
  private StreamHandler buildStreamHandlerPipeline(UDFRevision myUDFRevision, String outputFilename) throws HandlerException {
    UDF102Handler uDF102Handler = null;
    UDF260Handler uDF260Handler = null;
    this.serializationHandler = (StreamHandler)new SerializationHandler(new File(outputFilename));
    PaddingHandler paddingHandler = new PaddingHandler((StructureHandler)this.serializationHandler, (ContentHandler)this.serializationHandler);
    DescriptorTagHandler descriptorTagHandler = new DescriptorTagHandler((StructureHandler)paddingHandler, (ContentHandler)paddingHandler);
    if (myUDFRevision == UDFRevision.Revision102) {
      uDF102Handler = new UDF102Handler((StructureHandler)descriptorTagHandler, (ContentHandler)descriptorTagHandler);
    } else {
      UDF201Handler uDF201Handler = null; if (myUDFRevision == UDFRevision.Revision201) {
        uDF201Handler = new UDF201Handler((StructureHandler)uDF102Handler, (ContentHandler)uDF102Handler);
      }
      else if (myUDFRevision == UDFRevision.Revision260) {
        uDF260Handler = new UDF260Handler((StructureHandler)uDF201Handler, (ContentHandler)uDF201Handler);
      } 
    } 
    return (StreamHandler)uDF260Handler;
  }
  public void writeImage(String outputFilename, UDFRevision myUDFRevision) throws HandlerException {
    long recordingTimeMillis = Calendar.getInstance().getTimeInMillis();
    UDFLayoutInformation myUDFLayoutInformation = null;
    try {
      myUDFLayoutInformation = new UDFLayoutInformation(this.rootUDFImageBuilderFile, myUDFRevision, this.blockSize);
    }
    catch (Exception ex) {
      throw new HandlerException(ex);
    } 
    StreamHandler myStreamHandler = buildStreamHandlerPipeline(myUDFRevision, outputFilename);
    this.mySabreUDFElementFactory = new SabreUDFElementFactory(myStreamHandler);
    this.mySabreUDFElementFactory.startUDFImage();
    this.mySabreUDFElementFactory.startReservedArea();
    this.mySabreUDFElementFactory.endReservedArea();
    this.mySabreUDFElementFactory.startVRS();
    this.mySabreUDFElementFactory.endVRS();
    this.mySabreUDFElementFactory.startEmptyArea();
    this.mySabreUDFElementFactory.doEmptyArea(237 * this.blockSize);
    this.mySabreUDFElementFactory.endEmptyArea();
    this.mySabreUDFElementFactory.startAVDP();
    this.mySabreUDFElementFactory.doAVDP(myUDFLayoutInformation.AVDP1Block, 
        myUDFLayoutInformation.MVDSStartingBlock, 
        myUDFLayoutInformation.RVDSStartingBlock);
    this.mySabreUDFElementFactory.endAVDP();
    this.mySabreUDFElementFactory.startPVD();
    this.mySabreUDFElementFactory.doPVD(myUDFLayoutInformation.PVD1Block, 
        1L, 
        recordingTimeMillis, 
        this.imageIdentifier, 
        this.applicationIdentifier, 
        this.applicationIdentifierSuffix);
    this.mySabreUDFElementFactory.endPVD();
    this.mySabreUDFElementFactory.startPD();
    this.mySabreUDFElementFactory.doPD(myUDFLayoutInformation.PD1Block, 
        2L, 
        myUDFLayoutInformation.physicalPartitionStartingBlock, 
        myUDFLayoutInformation.physicalPartitionEndingBlock, 
        this.applicationIdentifier, 
        this.applicationIdentifierSuffix);
    this.mySabreUDFElementFactory.endPD();
    this.mySabreUDFElementFactory.startLVD();
    this.mySabreUDFElementFactory.doLVD(myUDFLayoutInformation.LVD1Block, 
        3L, 
        myUDFLayoutInformation.LVIDSStartingBlock, 
        myUDFLayoutInformation.LVIDSEndingBlock, 
        myUDFLayoutInformation.metadataAllocationUnitSize, 
        myUDFLayoutInformation.metadataAlignmentUnitSize, 
        myUDFLayoutInformation.mainMetadataFileLocation, 
        myUDFLayoutInformation.mirrorMetadataFileLocation, 
        myUDFLayoutInformation.partitionToStoreMetadataOn, 
        myUDFLayoutInformation.FSDLocation, 
        this.applicationIdentifier, 
        this.applicationIdentifierSuffix, 
        this.imageIdentifier);
    this.mySabreUDFElementFactory.endLVD();
    this.mySabreUDFElementFactory.startUSD();
    this.mySabreUDFElementFactory.doUSD(myUDFLayoutInformation.USD1Block, 4L, 19L, 256L);
    this.mySabreUDFElementFactory.endUSD();
    this.mySabreUDFElementFactory.startIUVD();
    this.mySabreUDFElementFactory.doIUVD(myUDFLayoutInformation.IUVD1Block, 
        5L, 
        this.applicationIdentifier, 
        this.applicationIdentifierSuffix, 
        this.imageIdentifier);
    this.mySabreUDFElementFactory.endIUVD();
    this.mySabreUDFElementFactory.startTD();
    this.mySabreUDFElementFactory.doTD(myUDFLayoutInformation.TD1Block);
    this.mySabreUDFElementFactory.endTD();
    this.mySabreUDFElementFactory.startEmptyArea();
    this.mySabreUDFElementFactory.doEmptyArea(
        (int)(myUDFLayoutInformation.MVDSEndingBlock - myUDFLayoutInformation.MVDSStartingBlock - 6L) * 
        this.blockSize);
    this.mySabreUDFElementFactory.endEmptyArea();
    this.mySabreUDFElementFactory.startLVID();
    this.mySabreUDFElementFactory.doLVID(myUDFLayoutInformation.LVIDSStartingBlock + 0L, 
        recordingTimeMillis, 
        myUDFLayoutInformation.fileCount, 
        myUDFLayoutInformation.directoryCount, 
        this.applicationIdentifier, 
        this.applicationIdentifierSuffix, 
        myUDFLayoutInformation.sizeTable, 
        myUDFLayoutInformation.freespaceTable, 
        myUDFLayoutInformation.nextUniqueId);
    this.mySabreUDFElementFactory.endLVID();
    this.mySabreUDFElementFactory.startTD();
    this.mySabreUDFElementFactory.doTD(myUDFLayoutInformation.LVIDSStartingBlock + 1L);
    this.mySabreUDFElementFactory.endTD();
    this.mySabreUDFElementFactory.startEmptyArea();
    this.mySabreUDFElementFactory.doEmptyArea(
        (int)(myUDFLayoutInformation.LVIDSEndingBlock - myUDFLayoutInformation.LVIDSStartingBlock - 2L) * 
        this.blockSize);
    this.mySabreUDFElementFactory.endEmptyArea();
    this.mySabreUDFElementFactory.startEmptyArea();
    this.mySabreUDFElementFactory.doEmptyArea(this.blockSize);
    this.mySabreUDFElementFactory.endEmptyArea();
    this.mySabreUDFElementFactory.startMetadataFile();
    this.mySabreUDFElementFactory.doMetadataFile(recordingTimeMillis, 
        this.applicationIdentifier, 
        this.applicationIdentifierSuffix, 
        myUDFLayoutInformation, 
        myUDFLayoutInformation.mainMetadataFileLocation, (byte)
        -6);
    this.mySabreUDFElementFactory.endMetadataFile();
    this.mySabreUDFElementFactory.startFSD();
    this.mySabreUDFElementFactory.doFSD(myUDFLayoutInformation.FSDLocation, 
        recordingTimeMillis, 
        myUDFLayoutInformation.rootFELocation, 
        myUDFLayoutInformation.partitionToStoreMetadataOn, 
        this.imageIdentifier);
    this.mySabreUDFElementFactory.endFSD();
    Iterator<UDFImageBuilderFile> myIterator = myUDFLayoutInformation.linearUDFImageBuilderFileOrdering.iterator();
    while (myIterator.hasNext()) {
      UDFImageBuilderFile myUDFImageBuilderFile = myIterator.next();
      this.mySabreUDFElementFactory.startFE();
      this.mySabreUDFElementFactory.doFE(myUDFImageBuilderFile, 
          myUDFLayoutInformation, 
          this.applicationIdentifier, 
          this.applicationIdentifierSuffix);
      this.mySabreUDFElementFactory.endFE();
    } 
    this.mySabreUDFElementFactory.startEmptyArea();
    this.mySabreUDFElementFactory.doEmptyArea((int)(myUDFLayoutInformation.metadataEmptyArea * this.blockSize));
    this.mySabreUDFElementFactory.endEmptyArea();
    myIterator = myUDFLayoutInformation.linearUDFImageBuilderFileOrdering.iterator();
    while (myIterator.hasNext()) {
      UDFImageBuilderFile myUDFImageBuilderFile = myIterator.next();
      UDFLayoutInformation.FileEntryPosition myFileEntryPosition = 
        myUDFLayoutInformation.fileEntryPositions.get(myUDFImageBuilderFile);
      if (myUDFImageBuilderFile.getFileType() == UDFImageBuilderFile.FileType.File && 
        myFileEntryPosition.dataBlock != -1L) {
        this.mySabreUDFElementFactory.startRawFileData();
        this.mySabreUDFElementFactory.doRawFileData(myUDFImageBuilderFile.getSourceFile());
        this.mySabreUDFElementFactory.endRawFileData();
      } 
    } 
    this.mySabreUDFElementFactory.startMetadataFile();
    this.mySabreUDFElementFactory.doMetadataFile(recordingTimeMillis, 
        this.applicationIdentifier, 
        this.applicationIdentifierSuffix, 
        myUDFLayoutInformation, 
        myUDFLayoutInformation.mirrorMetadataFileLocation, (byte)
        -5);
    this.mySabreUDFElementFactory.endMetadataFile();
    this.mySabreUDFElementFactory.startPVD();
    this.mySabreUDFElementFactory.doPVD(myUDFLayoutInformation.PVD2Block, 
        1L, 
        recordingTimeMillis, 
        this.imageIdentifier, 
        this.applicationIdentifier, 
        this.applicationIdentifierSuffix);
    this.mySabreUDFElementFactory.endPVD();
    this.mySabreUDFElementFactory.startPD();
    this.mySabreUDFElementFactory.doPD(myUDFLayoutInformation.PD2Block, 
        2L, 
        myUDFLayoutInformation.physicalPartitionStartingBlock, 
        myUDFLayoutInformation.physicalPartitionEndingBlock, 
        this.applicationIdentifier, 
        this.applicationIdentifierSuffix);
    this.mySabreUDFElementFactory.endPD();
    this.mySabreUDFElementFactory.startLVD();
    this.mySabreUDFElementFactory.doLVD(myUDFLayoutInformation.LVD2Block, 
        3L, 
        myUDFLayoutInformation.LVIDSStartingBlock, 
        myUDFLayoutInformation.LVIDSEndingBlock, 
        myUDFLayoutInformation.metadataAllocationUnitSize, 
        myUDFLayoutInformation.metadataAlignmentUnitSize, 
        myUDFLayoutInformation.mainMetadataFileLocation, 
        myUDFLayoutInformation.mirrorMetadataFileLocation, 
        myUDFLayoutInformation.partitionToStoreMetadataOn, 
        myUDFLayoutInformation.FSDLocation, 
        this.applicationIdentifier, 
        this.applicationIdentifierSuffix, 
        this.imageIdentifier);
    this.mySabreUDFElementFactory.endLVD();
    this.mySabreUDFElementFactory.startUSD();
    this.mySabreUDFElementFactory.doUSD(myUDFLayoutInformation.USD2Block, 4L, 19L, 256L);
    this.mySabreUDFElementFactory.endUSD();
    this.mySabreUDFElementFactory.startIUVD();
    this.mySabreUDFElementFactory.doIUVD(myUDFLayoutInformation.IUVD2Block, 
        5L, 
        this.applicationIdentifier, 
        this.applicationIdentifierSuffix, 
        this.imageIdentifier);
    this.mySabreUDFElementFactory.endIUVD();
    this.mySabreUDFElementFactory.startTD();
    this.mySabreUDFElementFactory.doTD(myUDFLayoutInformation.TD2Block);
    this.mySabreUDFElementFactory.endTD();
    this.mySabreUDFElementFactory.startEmptyArea();
    this.mySabreUDFElementFactory.doEmptyArea(
        (int)(myUDFLayoutInformation.RVDSEndingBlock - myUDFLayoutInformation.RVDSStartingBlock - 5L) * 
        this.blockSize);
    this.mySabreUDFElementFactory.endEmptyArea();
    this.mySabreUDFElementFactory.startAVDP();
    this.mySabreUDFElementFactory.doAVDP(myUDFLayoutInformation.AVDP2Block, 
        myUDFLayoutInformation.MVDSStartingBlock, 
        myUDFLayoutInformation.RVDSStartingBlock);
    this.mySabreUDFElementFactory.endAVDP();
    this.mySabreUDFElementFactory.endUDFImage();
  }
}
