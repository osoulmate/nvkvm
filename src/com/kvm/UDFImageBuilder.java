package com.kvm;
import com.library.LoggerUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class UDFImageBuilder
{
  private String imageIdentifier = "Test-Disc";
  private String applicationIdentifier = "*SabreUDFImageBuilder";
  private byte[] applicationIdentifierSuffix = new byte[] { 1, 0, 0, 0, 0, 0, 0, 0 };
  private int blockSize = 2048;
  private byte[] udfVersionIdentifierSuffix = new byte[] { 2, 1, 0, 0, 0, 0, 0, 0 };
  private UDFImageBuilderFile rootUDFImageBuilderFile;
  private String version = "Revision102";
  private int maximumAllocationLength = 1073739776;
  private Map<Long, UDFExtendFile> extendMap = new HashMap<>();
  private Map<Integer, Long> keyMap = new HashMap<>();
  ArrayList<Long> keylist = new ArrayList<>();
  private UdfLayoutInformation udfLayoutInformation;
  public void setRootPath(File rootPath) throws Exception {
    this.rootUDFImageBuilderFile = creatUdfImageBuilderFile(rootPath);
  }
  public UDFImageBuilderFile creatUdfImageBuilderFile(File dirFile) {
    UDFImageBuilderFile rootUdfImageBuilderFile = new UDFImageBuilderFile("");
    File[] childFiles = dirFile.listFiles();
    if (null != childFiles) {
      for (int i = 0; i < childFiles.length; i++)
      {
        rootUdfImageBuilderFile.addChild(childFiles[i]);
      }
    }
    return rootUdfImageBuilderFile;
  }
  public void setImageIdentifier(String imageIdentifier) throws IndexOutOfBoundsException {
    if (imageIdentifier.length() > 30)
    {
      throw new IndexOutOfBoundsException("捕获异常,id.length>30");
    }
    this.imageIdentifier = imageIdentifier;
  }
  public UDFImageBuilderFile getRootUDFImageBuilderFile() {
    return this.rootUDFImageBuilderFile;
  }
  public void excute() throws IOException {
    UdfLayoutInformation myUdfLayoutInformation = new UdfLayoutInformation();
    myUdfLayoutInformation.init(this.rootUDFImageBuilderFile, this.version, this.blockSize);
    this.udfLayoutInformation = myUdfLayoutInformation;
    for (int i = 0; i < myUdfLayoutInformation.linearUDFImageBuilderFileOrdering.size(); i++) {
      UDFImageBuilderFile udfImageFile = myUdfLayoutInformation.linearUDFImageBuilderFileOrdering.get(i);
      if (udfImageFile.getFileType() == FileType.File && udfImageFile.getFileLength() <= 1872L)
      {
        udfImageFile.setSourceFileArray(writeToByteArray(udfImageFile));
      }
    } 
    writeImage();
  }
  private byte[] writeToByteArray(UDFImageBuilderFile udfImageFile) {
    FileInputStream fi = null;
    byte[] buf = new byte[(int)udfImageFile.getSourceFile().length()];
    try {
      fi = new FileInputStream(udfImageFile.getSourceFile());
      int c = fi.read(buf, 0, buf.length);
      if (c == -1)
      {
        return buf;
      }
    }
    catch (FileNotFoundException e) {
      LoggerUtil.error("FILE NOT FOUND");
    } catch (IOException e) {
      LoggerUtil.error("IOException");
    } finally {
      try {
        if (fi != null) {
          fi.close();
        }
      } catch (IOException e) {
        LoggerUtil.error("IOException");
      } 
    } 
    return buf;
  }
  public Map<Long, UDFExtendFile> getExtendMap() {
    return this.extendMap;
  }
  private void writeImage() throws IOException {
    long recordingTimeMillis = (new Date()).getTime();
    if (this.udfLayoutInformation == null) {
      return;
    }
    UdfLayoutInformation myUDFLayoutInformation = this.udfLayoutInformation;
    this.keyMap.put(Integer.valueOf(0), Long.valueOf(0L));
    this.keylist.add(Long.valueOf(0L));
    writeReserve();
    writeVRS();
    writeEmptyArea();
    writeAVDP(myUDFLayoutInformation.AVDP1Block, myUDFLayoutInformation.MVDSStartingBlock, myUDFLayoutInformation.RVDSStartingBlock);
    writePVD(myUDFLayoutInformation.PVD1Block, recordingTimeMillis);
    writePD(myUDFLayoutInformation, myUDFLayoutInformation.PD1Block);
    writeLVD(myUDFLayoutInformation, myUDFLayoutInformation.LVD1Block);
    writeUSD(myUDFLayoutInformation.USD1Block);
    writeIUVD(myUDFLayoutInformation.IUVD1Block);
    writeTD(myUDFLayoutInformation.TD1Block);
    writeEmptyArea2();
    writeLVID(myUDFLayoutInformation, recordingTimeMillis);
    writeTD(myUDFLayoutInformation.LVIDSStartingBlock + 1L);
    writeEmptyArea3();
    writeEmptyArea4();
    writeFSD(myUDFLayoutInformation, recordingTimeMillis);
    for (int f = 0; f < myUDFLayoutInformation.linearUDFImageBuilderFileOrdering.size(); f++) {
      UDFImageBuilderFile udfImageFile = myUDFLayoutInformation.linearUDFImageBuilderFileOrdering.get(f);
      FileEntryPosition myFileEntyPosition = udfImageFile.getFileEntryPosition();
      if (udfImageFile.getFileType() == FileType.File) {
        int ICBTagFlags; long LogicalBlocksRecorded;
        int bufferLength = 2048;
        byte[] fe = new byte[bufferLength];
        long fileLength = udfImageFile.getFileLength();
        long l_ad = 0L;
        if (fileLength <= 1872L) {
          l_ad = fileLength;
          ICBTagFlags = 3;
          LogicalBlocksRecorded = 0L;
        } else {
          long restFileSize = fileLength;
          ICBTagFlags = 1;
          LogicalBlocksRecorded = (long)Math.ceil(fileLength / 2048.0D);
          while (restFileSize > 0L) {
            l_ad += 16L;
            restFileSize -= this.maximumAllocationLength;
          } 
        } 
        byte[] fe_body = new byte[(int)(160L + l_ad)];
        fe_body[0] = 0;
        fe_body[1] = 0;
        fe_body[2] = 0;
        fe_body[3] = 0;
        fe_body[4] = 4;
        fe_body[5] = 0;
        fe_body[8] = 1;
        fe_body[9] = 0;
        fe_body[11] = 5;
        fe_body[18] = (byte)(ICBTagFlags >> 0 & 0xFF);
        fe_body[19] = (byte)(ICBTagFlags >> 8 & 0xFF);
        fe_body[20] = -1;
        fe_body[21] = -1;
        fe_body[22] = -1;
        fe_body[23] = -1;
        fe_body[24] = -1;
        fe_body[25] = -1;
        fe_body[26] = -1;
        fe_body[27] = -1;
        fe_body[28] = -124;
        fe_body[29] = 16;
        fe_body[30] = 0;
        fe_body[31] = 0;
        fe_body[32] = (byte)(udfImageFile.getFileLinkCount() >> 0 & 0xFF);
        fe_body[33] = (byte)(udfImageFile.getFileLinkCount() >> 8 & 0xFF);
        fe_body[34] = 0;
        fe_body[35] = 0;
        fe_body[36] = 0;
        fe_body[37] = 0;
        fe_body[38] = 0;
        fe_body[39] = 0;
        byte[] InformationLength = int64ToBytes(fileLength);
        for (int i = 0; i < 8; i++)
        {
          fe_body[i + 40] = (byte)(InformationLength[i] & 0xFF);
        }
        byte[] LogicalBlocksRecordedUint64 = int64ToBytes(LogicalBlocksRecorded);
        for (int k = 0; k < 8; k++) {
          fe_body[k + 48] = LogicalBlocksRecordedUint64[k];
        }
        byte[] AccessTime2 = millis2TimeStamp(udfImageFile.getAccessTime());
        for (int j = 0; j < 12; j++) {
          fe_body[j + 56] = (byte)(AccessTime2[j] & 0xFF);
        }
        byte[] ModificationTime2 = millis2TimeStamp(udfImageFile.getModificationTime());
        for (int m = 0; m < 12; m++) {
          fe_body[m + 68] = (byte)(ModificationTime2[m] & 0xFF);
        }
        byte[] AttributeTime2 = millis2TimeStamp(udfImageFile.getAttributeTime());
        for (int n = 0; n < 12; n++) {
          fe_body[n + 80] = (byte)(AttributeTime2[n] & 0xFF);
        }
        fe_body[92] = 1;
        fe_body[93] = 0;
        fe_body[94] = 0;
        fe_body[95] = 0;
        fe_body[112] = 0;
        byte[] ApplicationIdentifier2 = stringToBytes(this.applicationIdentifier); int i1;
        for (i1 = 0; i1 < ApplicationIdentifier2.length; i1++) {
          fe_body[i1 + 113] = (byte)(ApplicationIdentifier2[i1] & 0xFF);
        }
        for (i1 = 0; i1 < 8; i1++) {
          fe_body[i1 + 136] = this.applicationIdentifierSuffix[i1];
        }
        byte[] UniqueID = int64ToBytes(myFileEntyPosition.getUniqueIds());
        for (int i2 = 0; i2 < 8; i2++) {
          fe_body[i2 + 144] = (byte)(UniqueID[i2] & 0xFF);
        }
        fe_body[156] = (byte)(int)(l_ad >> 0L & 0xFFL);
        fe_body[157] = (byte)(int)(l_ad >> 8L & 0xFFL);
        fe_body[158] = (byte)(int)(l_ad >> 16L & 0xFFL);
        fe_body[159] = (byte)(int)(l_ad >> 24L & 0xFFL);
        if (fileLength <= 1872L) {
          if (udfImageFile.getSourceFile() != null) {
            byte[] byteArray = udfImageFile.getSourceFileArray();
            for (int i4 = 0; i4 < byteArray.length; i4++) {
              fe_body[i4 + 160] = byteArray[i4];
            }
          } 
        } else {
          long currentExtentPosition = myFileEntyPosition.getDataLocation();
          long restFileSize = fileLength;
          int i4 = 0;
          while (restFileSize > 0L) {
            long ExtentLength = (restFileSize < this.maximumAllocationLength) ? restFileSize : this.maximumAllocationLength;
            fe_body[160 + i4 * 16 + 0] = (byte)(int)(ExtentLength >> 0L & 0xFFL);
            fe_body[160 + i4 * 16 + 1] = (byte)(int)(ExtentLength >> 8L & 0xFFL);
            fe_body[160 + i4 * 16 + 2] = (byte)(int)(ExtentLength >> 16L & 0xFFL);
            fe_body[160 + i4 * 16 + 3] = (byte)(int)(ExtentLength >> 24L & 0xFFL);
            fe_body[160 + i4 * 16 + 4] = (byte)(int)(currentExtentPosition >> 0L & 0xFFL);
            fe_body[160 + i4 * 16 + 5] = (byte)(int)(currentExtentPosition >> 8L & 0xFFL);
            fe_body[160 + i4 * 16 + 6] = (byte)(int)(currentExtentPosition >> 16L & 0xFFL);
            fe_body[160 + i4 * 16 + 7] = (byte)(int)(currentExtentPosition >> 24L & 0xFFL);
            i4++;
            restFileSize -= this.maximumAllocationLength;
            currentExtentPosition = (long)(currentExtentPosition + Math.ceil(this.maximumAllocationLength / 2048.0D));
          } 
        } 
        int DescriptorCRC = checkSum(fe_body);
        fe[0] = 5;
        fe[1] = 1;
        fe[2] = 2;
        fe[3] = 0;
        fe[6] = 1;
        fe[7] = 0;
        fe[8] = (byte)(DescriptorCRC >> 0 & 0xFF);
        fe[9] = (byte)(DescriptorCRC >> 8 & 0xFF);
        fe[10] = (byte)(int)(160L + l_ad >> 0L & 0xFFL);
        fe[11] = (byte)(int)(160L + l_ad >> 8L & 0xFFL);
        fe[12] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 0L & 0xFFL);
        fe[13] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 8L & 0xFFL);
        fe[14] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 16L & 0xFFL);
        fe[15] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 24L & 0xFFL);
        int checksum = fe[0] + fe[1] + fe[2] + fe[3] + fe[5] + fe[6] + fe[7] + fe[8] + fe[9] + fe[10] + fe[11] + fe[12] + fe[13] + fe[14] + fe[15] & 0xFF;
        fe[4] = (byte)(checksum & 0xFF);
        for (int i3 = 0; i3 < fe_body.length; i3++) {
          fe[i3 + 16] = fe_body[i3];
        }
        writeToMap(fe);
      } else {
        int ICBTagFlags;
        long LogicalBlocksRecorded;
        byte[] fe = new byte[2048];
        long fidLength = 40L;
        ArrayList<UDFImageBuilderFile> childUDFImageBuilderFiles = udfImageFile.getChilds();
        for (int i = 0; i < childUDFImageBuilderFiles.size(); i++) {
          long lengthWithoutPadding = (38 + (stringToBytes1(((UDFImageBuilderFile)childUDFImageBuilderFiles
              .get(i)).getIdentifier())).length);
          long paddingLength = (lengthWithoutPadding % 4L == 0L) ? 0L : (4L - lengthWithoutPadding % 4L);
          fidLength += lengthWithoutPadding + paddingLength;
        } 
        long l_ad = 0L;
        if (fidLength <= 1872L) {
          l_ad = fidLength;
          ICBTagFlags = 3;
          LogicalBlocksRecorded = 0L;
        } else {
          ICBTagFlags = 0;
          LogicalBlocksRecorded = (long)Math.ceil(fidLength / 2048.0D);
          l_ad = 8L;
        } 
        byte[] fe_body = new byte[(int)(160L + l_ad)];
        fe_body[0] = 0;
        fe_body[1] = 0;
        fe_body[2] = 0;
        fe_body[3] = 0;
        fe_body[4] = 4;
        fe_body[5] = 0;
        fe_body[8] = 1;
        fe_body[9] = 0;
        fe_body[11] = 4;
        fe_body[18] = (byte)(ICBTagFlags >> 0 & 0xFF);
        fe_body[19] = (byte)(ICBTagFlags >> 8 & 0xFF);
        fe_body[20] = -1;
        fe_body[21] = -1;
        fe_body[22] = -1;
        fe_body[23] = -1;
        fe_body[24] = -1;
        fe_body[25] = -1;
        fe_body[26] = -1;
        fe_body[27] = -1;
        fe_body[28] = -91;
        fe_body[29] = 20;
        fe_body[30] = 0;
        fe_body[31] = 0;
        fe_body[32] = (byte)(udfImageFile.getFileLinkCount() >> 0 & 0xFF);
        fe_body[33] = (byte)(udfImageFile.getFileLinkCount() >> 8 & 0xFF);
        fe_body[34] = 0;
        fe_body[35] = 0;
        fe_body[36] = 0;
        fe_body[37] = 0;
        fe_body[38] = 0;
        fe_body[39] = 0;
        byte[] InformationLength = int64ToBytes(fidLength);
        for (int j = 0; j < 8; j++) {
          fe_body[j + 40] = (byte)(InformationLength[j] & 0xFF);
        }
        byte[] LogicalBlocksRecordedUint64 = int64ToBytes(LogicalBlocksRecorded);
        for (int k = 0; k < 8; k++) {
          fe_body[k + 48] = (byte)(LogicalBlocksRecordedUint64[k] & 0xFF);
        }
        byte[] AccessTime = millis2TimeStamp(udfImageFile.getAccessTime());
        for (int m = 0; m < 12; m++) {
          fe_body[m + 56] = (byte)(AccessTime[m] & 0xFF);
        }
        byte[] ModificationTime = millis2TimeStamp(udfImageFile.getModificationTime());
        for (int n = 0; n < 12; n++) {
          fe_body[n + 68] = (byte)(ModificationTime[n] & 0xFF);
        }
        byte[] AttributeTime = millis2TimeStamp(udfImageFile.getAttributeTime());
        for (int i1 = 0; i1 < 12; i1++) {
          fe_body[i1 + 80] = (byte)(AttributeTime[i1] & 0xFF);
        }
        fe_body[92] = 1;
        fe_body[93] = 0;
        fe_body[94] = 0;
        fe_body[95] = 0;
        fe_body[112] = 0;
        byte[] ApplicationIdentifier = stringToBytes(this.applicationIdentifier); int i2;
        for (i2 = 0; i2 < ApplicationIdentifier.length; i2++) {
          fe_body[i2 + 113] = (byte)(ApplicationIdentifier[i2] & 0xFF);
        }
        for (i2 = 0; i2 < 8; i2++) {
          fe_body[i2 + 136] = this.applicationIdentifierSuffix[i2];
        }
        byte[] UniqueID = int64ToBytes(myFileEntyPosition.getUniqueIds());
        for (int i3 = 0; i3 < 8; i3++) {
          fe_body[i3 + 144] = (byte)(UniqueID[i3] & 0xFF);
        }
        fe_body[156] = (byte)(int)(l_ad >> 0L & 0xFFL);
        fe_body[157] = (byte)(int)(l_ad >> 8L & 0xFFL);
        fe_body[158] = (byte)(int)(l_ad >> 16L & 0xFFL);
        fe_body[159] = (byte)(int)(l_ad >> 24L & 0xFFL);
        byte[] fids = new byte[(int)fidLength];
        byte[] pFID = new byte[40];
        byte[] pFID_body = new byte[24];
        pFID_body[0] = 1;
        pFID_body[1] = 0;
        pFID_body[2] = 10;
        pFID_body[4] = 0;
        pFID_body[5] = 8;
        pFID_body[6] = 0;
        pFID_body[7] = 0;
        long parentDirectoryLocation = myFileEntyPosition.getEntryLocation();
        long parentDirectoryUniqueId = 0L;
        if (udfImageFile.getParent() != null) {
          parentDirectoryLocation = udfImageFile.getParent().getFileEntryPosition().getEntryLocation();
          parentDirectoryUniqueId = udfImageFile.getParent().getFileEntryPosition().getUniqueIds();
        } 
        pFID_body[8] = (byte)(int)(parentDirectoryLocation >> 0L & 0xFFL);
        pFID_body[9] = (byte)(int)(parentDirectoryLocation >> 8L & 0xFFL);
        pFID_body[10] = (byte)(int)(parentDirectoryLocation >> 16L & 0xFFL);
        pFID_body[11] = (byte)(int)(parentDirectoryLocation >> 24L & 0xFFL);
        pFID_body[12] = 0;
        pFID_body[13] = 0;
        pFID_body[16] = (byte)(int)(parentDirectoryUniqueId >> 0L & 0xFFL);
        pFID_body[17] = (byte)(int)(parentDirectoryUniqueId >> 8L & 0xFFL);
        pFID_body[18] = (byte)(int)(parentDirectoryUniqueId >> 16L & 0xFFL);
        pFID_body[19] = (byte)(int)(parentDirectoryUniqueId >> 24L & 0xFFL);
        int DescriptorCRC = checkSum(pFID_body);
        pFID[0] = 1;
        pFID[1] = 1;
        pFID[2] = 2;
        pFID[3] = 0;
        pFID[5] = 0;
        pFID[6] = 1;
        pFID[7] = 0;
        pFID[8] = (byte)(DescriptorCRC >> 0 & 0xFF);
        pFID[9] = (byte)(DescriptorCRC >> 8 & 0xFF);
        pFID[10] = 24;
        pFID[11] = 0;
        pFID[12] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 0L & 0xFFL);
        pFID[13] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 8L & 0xFFL);
        pFID[14] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 16L & 0xFFL);
        pFID[15] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 24L & 0xFFL);
        int checksum = pFID[0] + pFID[1] + pFID[2] + pFID[3] + pFID[5] + pFID[6] + pFID[7] + pFID[8] + pFID[9] + pFID[10] + pFID[11] + pFID[12] + pFID[13] + pFID[14] + pFID[15] & 0xFF;
        pFID[4] = (byte)(checksum & 0xFF);
        int i4;
        for (i4 = 0; i4 < pFID_body.length; i4++) {
          pFID[i4 + 16] = pFID_body[i4];
        }
        for (i4 = 0; i4 < 40; i4++) {
          fids[i4] = pFID[i4];
        }
        int fidPoint = 40;
        int[] everyFidStartPoint = new int[udfImageFile.getChilds().size() + 1];
        everyFidStartPoint[0] = 0;
        if (udfImageFile.getChilds().size() > 0)
          everyFidStartPoint[1] = 40;  int i5;
        for (i5 = 0; i5 < udfImageFile.getChilds().size(); i5++) {
          UDFImageBuilderFile cudfImageFile = udfImageFile.getChilds().get(i5);
          String cidentifier = cudfImageFile.getIdentifier();
          long lengthWithoutPadding = (38 + (stringToBytes1(((UDFImageBuilderFile)childUDFImageBuilderFiles
              .get(i5)).getIdentifier())).length);
          long paddingLength = (lengthWithoutPadding % 4L == 0L) ? 0L : (4L - lengthWithoutPadding % 4L);
          long cfidLength = lengthWithoutPadding + paddingLength;
          byte[] cFID = new byte[(int)cfidLength];
          byte[] cFID_body = new byte[(int)(cfidLength - 16L)];
          cFID_body[0] = 1;
          cFID_body[1] = 0;
          if (cudfImageFile.getFileType() == FileType.Directory) {
            cFID_body[2] = 2;
          } else {
            cFID_body[2] = 0;
          } 
          cFID_body[3] = (byte)((stringToBytes1(cidentifier)).length >> 0 & 0xFF);
          cFID_body[4] = 0;
          cFID_body[5] = 8;
          cFID_body[6] = 0;
          cFID_body[7] = 0;
          FileEntryPosition cfileEntyPosition = cudfImageFile.getFileEntryPosition();
          cFID_body[8] = (byte)(int)(cfileEntyPosition.getEntryLocation() >> 0L & 0xFFL);
          cFID_body[9] = (byte)(int)(cfileEntyPosition.getEntryLocation() >> 8L & 0xFFL);
          cFID_body[10] = (byte)(int)(cfileEntyPosition.getEntryLocation() >> 16L & 0xFFL);
          cFID_body[11] = (byte)(int)(cfileEntyPosition.getEntryLocation() >> 24L & 0xFFL);
          cFID_body[12] = 0;
          cFID_body[13] = 0;
          cFID_body[16] = (byte)(int)(cfileEntyPosition.getUniqueIds() >> 0L & 0xFFL);
          cFID_body[17] = (byte)(int)(cfileEntyPosition.getUniqueIds() >> 8L & 0xFFL);
          cFID_body[18] = (byte)(int)(cfileEntyPosition.getUniqueIds() >> 16L & 0xFFL);
          cFID_body[19] = (byte)(int)(cfileEntyPosition.getUniqueIds() >> 24L & 0xFFL);
          byte[] cFileIdentifier = stringToBytes1(cidentifier);
          for (int i6 = 0; i6 < cFileIdentifier.length; i6++) {
            cFID_body[22 + i6] = cFileIdentifier[i6];
          }
          int DescriptorCRC2 = checkSum(cFID_body);
          cFID[0] = 1;
          cFID[1] = 1;
          cFID[2] = 2;
          cFID[3] = 0;
          cFID[5] = 0;
          cFID[6] = 1;
          cFID[7] = 0;
          cFID[8] = (byte)(DescriptorCRC2 >> 0 & 0xFF);
          cFID[9] = (byte)(DescriptorCRC2 >> 8 & 0xFF);
          cFID[10] = (byte)(int)(cfidLength - 16L >> 0L & 0xFFL);
          cFID[11] = (byte)(int)(cfidLength - 16L >> 8L & 0xFFL);
          cFID[12] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 0L & 0xFFL);
          cFID[13] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 8L & 0xFFL);
          cFID[14] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 16L & 0xFFL);
          cFID[15] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 24L & 0xFFL);
          int checksum2 = cFID[0] + cFID[1] + cFID[2] + cFID[3] + cFID[5] + cFID[6] + cFID[7] + cFID[8] + cFID[9] + cFID[10] + cFID[11] + cFID[12] + cFID[13] + cFID[14] + cFID[15] & 0xFF;
          cFID[4] = (byte)(checksum2 & 0xFF);
          int i7;
          for (i7 = 0; i7 < cFID_body.length; i7++) {
            cFID[i7 + 16] = cFID_body[i7];
          }
          for (i7 = 0; i7 < cfidLength; i7++) {
            fids[fidPoint++] = cFID[i7];
          }
          if (i5 < udfImageFile.getChilds().size() - 1) {
            everyFidStartPoint[i5 + 2] = fidPoint;
          }
        } 
        if (fidLength <= 1872L) {
          for (i5 = 0; i5 < fids.length; i5++) {
            fe_body[i5 + 160] = fids[i5];
          }
          int DescriptorCRC3 = checkSum(fe_body);
          fe[0] = 5;
          fe[1] = 1;
          fe[2] = 2;
          fe[3] = 0;
          fe[5] = 0;
          fe[6] = 1;
          fe[7] = 0;
          fe[8] = (byte)(DescriptorCRC3 >> 0 & 0xFF);
          fe[9] = (byte)(DescriptorCRC3 >> 8 & 0xFF);
          fe[10] = (byte)(int)(160L + l_ad >> 0L & 0xFFL);
          fe[11] = (byte)(int)(160L + l_ad >> 8L & 0xFFL);
          fe[12] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 0L & 0xFFL);
          fe[13] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 8L & 0xFFL);
          fe[14] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 16L & 0xFFL);
          fe[15] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 24L & 0xFFL);
          int checksum3 = fe[0] + fe[1] + fe[2] + fe[3] + fe[5] + fe[6] + fe[7] + fe[8] + fe[9] + fe[10] + fe[11] + fe[12] + fe[13] + fe[14] + fe[15] & 0xFF;
          fe[4] = (byte)(checksum3 & 0xFF);
          for (int i6 = 0; i6 < fe_body.length; i6++) {
            fe[i6 + 16] = fe_body[i6];
          }
          writeToMap(fe);
        }
        else {
          fe_body[160] = (byte)(int)(fidLength >> 0L & 0xFFL);
          fe_body[161] = (byte)(int)(fidLength >> 8L & 0xFFL);
          fe_body[162] = (byte)(int)(fidLength >> 16L & 0xFFL);
          fe_body[163] = (byte)(int)(fidLength >> 24L & 0xFFL);
          fe_body[164] = (byte)(int)(myFileEntyPosition.getDataLocation() >> 0L & 0xFFL);
          fe_body[165] = (byte)(int)(myFileEntyPosition.getDataLocation() >> 8L & 0xFFL);
          fe_body[166] = (byte)(int)(myFileEntyPosition.getDataLocation() >> 16L & 0xFFL);
          fe_body[167] = (byte)(int)(myFileEntyPosition.getDataLocation() >> 24L & 0xFFL);
          int DescriptorCRC4 = checkSum(fe_body);
          fe[0] = 5;
          fe[1] = 1;
          fe[2] = 2;
          fe[3] = 0;
          fe[5] = 0;
          fe[6] = 1;
          fe[7] = 0;
          fe[8] = (byte)(DescriptorCRC4 >> 0 & 0xFF);
          fe[9] = (byte)(DescriptorCRC4 >> 8 & 0xFF);
          fe[10] = (byte)(int)(160L + l_ad >> 0L & 0xFFL);
          fe[11] = (byte)(int)(160L + l_ad >> 8L & 0xFFL);
          fe[12] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 0L & 0xFFL);
          fe[13] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 8L & 0xFFL);
          fe[14] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 16L & 0xFFL);
          fe[15] = (byte)(int)(myFileEntyPosition.getEntryLocation() >> 24L & 0xFFL);
          int checksum5 = fe[0] + fe[1] + fe[2] + fe[3] + fe[5] + fe[6] + fe[7] + fe[8] + fe[9] + fe[10] + fe[11] + fe[12] + fe[13] + fe[14] + fe[15] & 0xFF;
          fe[4] = (byte)(checksum5 & 0xFF);
          for (int i6 = 0; i6 < fe_body.length; i6++) {
            fe[i6 + 16] = fe_body[i6];
          }
          long currentRealPosition = 2048L * myFileEntyPosition.getDataLocation();
          for (int i7 = 0; i7 < everyFidStartPoint.length; i7++) {
            long tagLocationBlock = (long)Math.floor(currentRealPosition / 2048.0D);
            fids[everyFidStartPoint[i7] + 4] = (byte)(fids[everyFidStartPoint[i7] + 4] - fids[everyFidStartPoint[i7] + 12]);
            fids[everyFidStartPoint[i7] + 4] = (byte)(fids[everyFidStartPoint[i7] + 4] - fids[everyFidStartPoint[i7] + 13]);
            fids[everyFidStartPoint[i7] + 4] = (byte)(fids[everyFidStartPoint[i7] + 4] - fids[everyFidStartPoint[i7] + 14]);
            fids[everyFidStartPoint[i7] + 4] = (byte)(fids[everyFidStartPoint[i7] + 4] - fids[everyFidStartPoint[i7] + 15]);
            fids[everyFidStartPoint[i7] + 12] = (byte)(int)(tagLocationBlock >> 0L & 0xFFL);
            fids[everyFidStartPoint[i7] + 13] = (byte)(int)(tagLocationBlock >> 8L & 0xFFL);
            fids[everyFidStartPoint[i7] + 14] = (byte)(int)(tagLocationBlock >> 16L & 0xFFL);
            fids[everyFidStartPoint[i7] + 15] = (byte)(int)(tagLocationBlock >> 24L & 0xFFL);
            fids[everyFidStartPoint[i7] + 4] = (byte)(fids[everyFidStartPoint[i7] + 4] + fids[everyFidStartPoint[i7] + 12]);
            fids[everyFidStartPoint[i7] + 4] = (byte)(fids[everyFidStartPoint[i7] + 4] + fids[everyFidStartPoint[i7] + 13]);
            fids[everyFidStartPoint[i7] + 4] = (byte)(fids[everyFidStartPoint[i7] + 4] + fids[everyFidStartPoint[i7] + 14]);
            fids[everyFidStartPoint[i7] + 4] = (byte)(fids[everyFidStartPoint[i7] + 4] + fids[everyFidStartPoint[i7] + 15]);
            if (i7 < everyFidStartPoint.length - 1) {
              currentRealPosition += (everyFidStartPoint[i7 + 1] - everyFidStartPoint[i7]);
            }
          } 
          long paddingLength = (fidLength % 2048L == 0L) ? 0L : (2048L - fidLength % 2048L);
          byte[] paddingArray = new byte[(int)paddingLength];
          byte[] fesum = new byte[fe.length + fids.length + paddingArray.length];
          for (int i10 = 0; i10 < fe.length; i10++) {
            fesum[i10] = fe[i10];
          }
          for (int i9 = 0; i9 < fids.length; i9++)
          {
            fesum[i9 + fe.length] = fids[i9];
          }
          for (int i8 = 0; i8 < paddingArray.length; i8++)
          {
            fesum[i8 + fe.length + fids.length] = paddingArray[i8];
          }
          writeToMap(fesum);
        } 
      } 
    } 
    Iterator<UDFImageBuilderFile> myIterator = myUDFLayoutInformation.linearUDFImageBuilderFileOrdering.iterator();
    while (myIterator.hasNext()) {
      UDFImageBuilderFile myUDFImageBuilderFile = myIterator.next();
      FileEntryPosition myFileEntryPosition = myUDFImageBuilderFile.getFileEntryPosition();
      if (myUDFImageBuilderFile.getFileType() == FileType.File && myFileEntryPosition
        .getDataBlock() != -1L)
      {
        writeToMapLarge(myUDFImageBuilderFile.getSourceFile());
      }
    } 
    writePVD(myUDFLayoutInformation.PVD2Block, recordingTimeMillis);
    writePD(myUDFLayoutInformation, myUDFLayoutInformation.PD2Block);
    writeLVD(myUDFLayoutInformation, myUDFLayoutInformation.LVD2Block);
    writeUSD(myUDFLayoutInformation.USD2Block);
    writeIUVD(myUDFLayoutInformation.IUVD2Block);
    writeTD(myUDFLayoutInformation.TD2Block);
    writeEmptyBlock((int)(myUDFLayoutInformation.RVDSEndingBlock - myUDFLayoutInformation.RVDSStartingBlock - 5L));
    writeAVDP(myUDFLayoutInformation.AVDP2Block, myUDFLayoutInformation.MVDSStartingBlock, myUDFLayoutInformation.RVDSStartingBlock);
  }
  private void writeFSD(UdfLayoutInformation myUDFLayoutInformation, long recordingTimeMillis) {
    byte[] fsd = new byte[2048];
    byte[] fsd_body = new byte[496];
    byte[] mytimestamp = millis2TimeStamp(recordingTimeMillis);
    for (int i = 0; i < 12; i++) {
      fsd_body[i] = mytimestamp[i];
    }
    fsd_body[12] = 3;
    fsd_body[13] = 0;
    fsd_body[14] = 3;
    fsd_body[15] = 0;
    fsd_body[16] = 1;
    fsd_body[17] = 0;
    fsd_body[18] = 0;
    fsd_body[19] = 0;
    fsd_body[20] = 1;
    fsd_body[21] = 0;
    fsd_body[22] = 0;
    fsd_body[23] = 0;
    fsd_body[24] = 0;
    fsd_body[25] = 0;
    fsd_body[26] = 0;
    fsd_body[27] = 0;
    fsd_body[28] = 0;
    fsd_body[29] = 0;
    fsd_body[30] = 0;
    fsd_body[31] = 0;
    fsd_body[32] = 0;
    byte[] DescriptorCharacterSet = stringToBytes("OSTA Compressed Unicode");
    for (int j = 0; j < DescriptorCharacterSet.length; j++) {
      fsd_body[j + 33] = (byte)(DescriptorCharacterSet[j] & 0xFF);
    }
    byte[] LogicalVolumeIdentifier = stringToBytes1(this.imageIdentifier); int k;
    for (k = 0; k < LogicalVolumeIdentifier.length; k++) {
      fsd_body[k + 96] = LogicalVolumeIdentifier[k];
    }
    fsd_body[223] = (byte)(LogicalVolumeIdentifier.length & 0xFF);
    fsd_body[224] = 0;
    for (k = 0; k < DescriptorCharacterSet.length; k++) {
      fsd_body[k + 225] = (byte)(DescriptorCharacterSet[k] & 0xFF);
    }
    byte[] FileSetIdentifier = stringToBytes1(this.imageIdentifier);
    for (int m = 0; m < FileSetIdentifier.length; m++) {
      fsd_body[m + 288] = FileSetIdentifier[m];
    }
    fsd_body[319] = (byte)(FileSetIdentifier.length & 0xFF);
    fsd_body[384] = 0;
    fsd_body[385] = 8;
    fsd_body[386] = 0;
    fsd_body[387] = 0;
    fsd_body[388] = (byte)(int)(myUDFLayoutInformation.rootFELocation >> 0L & 0xFFL);
    fsd_body[389] = (byte)(int)(myUDFLayoutInformation.rootFELocation >> 8L & 0xFFL);
    fsd_body[390] = (byte)(int)(myUDFLayoutInformation.rootFELocation >> 16L & 0xFFL);
    fsd_body[391] = (byte)(int)(myUDFLayoutInformation.rootFELocation >> 24L & 0xFFL);
    fsd_body[392] = (byte)(myUDFLayoutInformation.partitionToStoreMetadataOn >> 0 & 0xFF);
    fsd_body[393] = (byte)(myUDFLayoutInformation.partitionToStoreMetadataOn >> 8 & 0xFF);
    fsd_body[400] = 0;
    byte[] DomainIdentifier = stringToBytes("*OSTA UDF Compliant"); int n;
    for (n = 0; n < DomainIdentifier.length; n++) {
      fsd_body[401 + n] = DomainIdentifier[n];
    }
    for (n = 0; n < 8; n++) {
      fsd_body[424 + n] = this.udfVersionIdentifierSuffix[n];
    }
    int DescriptorCRC = checkSum(fsd_body);
    fsd[0] = 0;
    fsd[1] = 1;
    fsd[2] = 2;
    fsd[3] = 0;
    fsd[5] = 0;
    fsd[6] = 1;
    fsd[7] = 0;
    fsd[8] = (byte)(DescriptorCRC >> 0 & 0xFF);
    fsd[9] = (byte)(DescriptorCRC >> 8 & 0xFF);
    fsd[10] = -16;
    fsd[11] = 1;
    fsd[12] = (byte)(int)(myUDFLayoutInformation.FSDLocation >> 0L & 0xFFL);
    fsd[13] = (byte)(int)(myUDFLayoutInformation.FSDLocation >> 8L & 0xFFL);
    fsd[14] = (byte)(int)(myUDFLayoutInformation.FSDLocation >> 16L & 0xFFL);
    fsd[15] = (byte)(int)(myUDFLayoutInformation.FSDLocation >> 24L & 0xFFL);
    int checksum = fsd[0] + fsd[1] + fsd[2] + fsd[3] + fsd[5] + fsd[6] + fsd[7] + fsd[8] + fsd[9] + fsd[10] + fsd[11] + fsd[12] + fsd[13] + fsd[14] + fsd[15] & 0xFF;
    fsd[4] = (byte)(checksum & 0xFF);
    for (int i1 = 0; i1 < fsd_body.length; i1++) {
      fsd[i1 + 16] = fsd_body[i1];
    }
    writeToMap(fsd);
  }
  private void writeLVID(UdfLayoutInformation myUDFLayoutInformation, long recordingTimeMillis) {
    byte[] lvid = new byte[2048];
    byte[] lvid_body = new byte[118];
    byte[] mytimestamp = millis2TimeStamp(recordingTimeMillis);
    for (int i = 0; i < 12; i++) {
      lvid_body[i] = mytimestamp[i];
    }
    lvid_body[12] = 1;
    lvid_body[13] = 0;
    lvid_body[14] = 0;
    lvid_body[15] = 0;
    byte[] nextUniqueId = int64ToBytes(myUDFLayoutInformation.nextUniqueId);
    for (int j = 0; j < 8; j++) {
      lvid_body[j + 24] = nextUniqueId[j];
    }
    lvid_body[56] = 1;
    lvid_body[57] = 0;
    lvid_body[58] = 0;
    lvid_body[59] = 0;
    lvid_body[60] = 46;
    lvid_body[61] = 0;
    lvid_body[62] = 0;
    lvid_body[63] = 0;
    long SizeTable = myUDFLayoutInformation.physicalPartitionEndingBlock - myUDFLayoutInformation.physicalPartitionStartingBlock;
    lvid_body[68] = (byte)(int)(SizeTable >> 0L & 0xFFL);
    lvid_body[69] = (byte)(int)(SizeTable >> 8L & 0xFFL);
    lvid_body[70] = (byte)(int)(SizeTable >> 16L & 0xFFL);
    lvid_body[71] = (byte)(int)(SizeTable >> 24L & 0xFFL);
    lvid_body[72] = 0;
    byte[] ApplicationIdentifier = stringToBytes(this.applicationIdentifier); int k;
    for (k = 0; k < ApplicationIdentifier.length; k++) {
      lvid_body[k + 73] = (byte)(ApplicationIdentifier[k] & 0xFF);
    }
    for (k = 0; k < 8; k++) {
      lvid_body[k + 96] = this.applicationIdentifierSuffix[k];
    }
    lvid_body[104] = (byte)(int)(myUDFLayoutInformation.fileCount >> 0L & 0xFFL);
    lvid_body[105] = (byte)(int)(myUDFLayoutInformation.fileCount >> 8L & 0xFFL);
    lvid_body[106] = (byte)(int)(myUDFLayoutInformation.fileCount >> 16L & 0xFFL);
    lvid_body[107] = (byte)(int)(myUDFLayoutInformation.fileCount >> 24L & 0xFFL);
    lvid_body[108] = (byte)(int)(myUDFLayoutInformation.directoryCount >> 0L & 0xFFL);
    lvid_body[109] = (byte)(int)(myUDFLayoutInformation.directoryCount >> 8L & 0xFFL);
    lvid_body[110] = (byte)(int)(myUDFLayoutInformation.directoryCount >> 16L & 0xFFL);
    lvid_body[111] = (byte)(int)(myUDFLayoutInformation.directoryCount >> 24L & 0xFFL);
    lvid_body[112] = 2;
    lvid_body[113] = 1;
    lvid_body[114] = 2;
    lvid_body[115] = 1;
    lvid_body[116] = 2;
    lvid_body[117] = 1;
    int DescriptorCRC = checkSum(lvid_body);
    lvid[0] = 9;
    lvid[1] = 0;
    lvid[2] = 2;
    lvid[3] = 0;
    lvid[5] = 0;
    lvid[6] = 1;
    lvid[7] = 0;
    lvid[8] = (byte)(DescriptorCRC >> 0 & 0xFF);
    lvid[9] = (byte)(DescriptorCRC >> 8 & 0xFF);
    lvid[10] = 118;
    lvid[11] = 0;
    lvid[12] = (byte)(int)(myUDFLayoutInformation.LVIDSStartingBlock >> 0L & 0xFFL);
    lvid[13] = (byte)(int)(myUDFLayoutInformation.LVIDSStartingBlock >> 8L & 0xFFL);
    lvid[14] = (byte)(int)(myUDFLayoutInformation.LVIDSStartingBlock >> 16L & 0xFFL);
    lvid[15] = (byte)(int)(myUDFLayoutInformation.LVIDSStartingBlock >> 24L & 0xFFL);
    int checksum = lvid[0] + lvid[1] + lvid[2] + lvid[3] + lvid[5] + lvid[6] + lvid[7] + lvid[8] + lvid[9] + lvid[10] + lvid[11] + lvid[12] + lvid[13] + lvid[14] + lvid[15] & 0xFF;
    lvid[4] = (byte)(checksum & 0xFF);
    for (int m = 0; m < lvid_body.length; m++) {
      lvid[m + 16] = lvid_body[m];
    }
    writeToMap(lvid);
  }
  private void writePVD(long PVDBlock, long recordingTimeMillis) {
    byte[] pvd = new byte[2048];
    byte[] pvd_body = new byte[496];
    pvd_body[0] = 1;
    pvd_body[1] = 0;
    pvd_body[2] = 0;
    pvd_body[3] = 0;
    pvd_body[4] = 0;
    pvd_body[5] = 0;
    pvd_body[6] = 0;
    pvd_body[7] = 0;
    byte[] VolumeIdentifier = stringToBytes1(this.imageIdentifier);
    for (int i = 0; i < VolumeIdentifier.length; i++) {
      pvd_body[i + 8] = (byte)(VolumeIdentifier[i] & 0xFF);
    }
    pvd_body[39] = (byte)(VolumeIdentifier.length & 0xFF);
    pvd_body[40] = 1;
    pvd_body[41] = 0;
    pvd_body[42] = 1;
    pvd_body[43] = 0;
    pvd_body[44] = 2;
    pvd_body[45] = 0;
    pvd_body[46] = 3;
    pvd_body[47] = 0;
    pvd_body[48] = 1;
    pvd_body[49] = 0;
    pvd_body[50] = 0;
    pvd_body[51] = 0;
    pvd_body[52] = 1;
    pvd_body[53] = 0;
    pvd_body[54] = 0;
    pvd_body[55] = 0;
    byte[] VolumeSetIdentifier = stringToBytes1(
        Long.toHexString(recordingTimeMillis) + " " + this.imageIdentifier);
    for (int j = 0; j < VolumeSetIdentifier.length; j++) {
      pvd_body[j + 56] = (byte)(VolumeSetIdentifier[j] & 0xFF);
    }
    pvd_body[183] = (byte)(VolumeSetIdentifier.length & 0xFF);
    pvd_body[184] = 0;
    byte[] DescriptorCharacterSet = stringToBytes("OSTA Compressed Unicode");
    for (int k = 0; k < DescriptorCharacterSet.length; k++) {
      pvd_body[k + 185] = (byte)(DescriptorCharacterSet[k] & 0xFF);
    }
    pvd_body[248] = 0;
    byte[] SpecExplanatoryCharacterSet = stringToBytes("OSTA Compressed Unicode");
    for (int m = 0; m < SpecExplanatoryCharacterSet.length; m++) {
      pvd_body[m + 249] = (byte)(SpecExplanatoryCharacterSet[m] & 0xFF);
    }
    pvd_body[328] = 0;
    byte[] ApplicationIdentifier = stringToBytes(this.applicationIdentifier); int n;
    for (n = 0; n < ApplicationIdentifier.length; n++) {
      pvd_body[n + 329] = (byte)(ApplicationIdentifier[n] & 0xFF);
    }
    for (n = 0; n < 8; n++) {
      pvd_body[n + 329 + 23] = this.applicationIdentifierSuffix[n];
    }
    byte[] mytimestamp = millis2TimeStamp(recordingTimeMillis);
    for (int i1 = 0; i1 < 12; i1++) {
      pvd_body[i1 + 360] = mytimestamp[i1];
    }
    pvd_body[372] = 0;
    byte[] ImplementationIdentifier = stringToBytes(this.applicationIdentifier);
    for (int i2 = 0; i2 < ImplementationIdentifier.length; i2++) {
      pvd_body[i2 + 373] = (byte)(ImplementationIdentifier[i2] & 0xFF);
    }
    pvd_body[468] = 0;
    pvd_body[469] = 0;
    pvd_body[470] = 0;
    pvd_body[471] = 0;
    pvd_body[472] = 1;
    pvd_body[473] = 0;
    int DescriptorCRC = checkSum(pvd_body);
    pvd[0] = 1;
    pvd[1] = 0;
    pvd[2] = 2;
    pvd[3] = 0;
    pvd[5] = 0;
    pvd[6] = 1;
    pvd[7] = 0;
    pvd[8] = (byte)(DescriptorCRC >> 0 & 0xFF);
    pvd[9] = (byte)(DescriptorCRC >> 8 & 0xFF);
    pvd[10] = -16;
    pvd[11] = 1;
    pvd[12] = (byte)(int)(PVDBlock >> 0L & 0xFFL);
    pvd[13] = (byte)(int)(PVDBlock >> 8L & 0xFFL);
    pvd[14] = (byte)(int)(PVDBlock >> 16L & 0xFFL);
    pvd[15] = (byte)(int)(PVDBlock >> 24L & 0xFFL);
    int checksum = pvd[0] + pvd[1] + pvd[2] + pvd[3] + pvd[5] + pvd[6] + pvd[7] + pvd[8] + pvd[9] + pvd[10] + pvd[11] + pvd[12] + pvd[13] + pvd[14] + pvd[15] & 0xFF;
    pvd[4] = (byte)(checksum & 0xFF);
    for (int i3 = 0; i3 < pvd_body.length; i3++) {
      pvd[i3 + 16] = pvd_body[i3];
    }
    writeToMap(pvd);
  }
  private byte[] millis2TimeStamp(long mills) {
    Date mydate = new Date(mills);
    Calendar cal = Calendar.getInstance();
    cal.setTime(mydate);
    byte[] mytimestamp = new byte[12];
    int twelveBitSignedValue = -(cal.get(15) + cal.get(16)) / 60000;
    if (twelveBitSignedValue < 0) {
      twelveBitSignedValue *= -1;
    }
    int TypeAndTimezone = 0x1000 | twelveBitSignedValue;
    mytimestamp[0] = (byte)(TypeAndTimezone >> 0 & 0xFF);
    mytimestamp[1] = (byte)(TypeAndTimezone >> 8 & 0xFF);
    mytimestamp[2] = (byte)(cal.get(1) - 1900 >> 0 & 0xFF);
    mytimestamp[3] = (byte)(cal.get(1) - 1900 >> 8 & 0xFF);
    mytimestamp[4] = (byte)(cal.get(2) + 1 & 0xFF);
    mytimestamp[5] = (byte)(cal.get(5) & 0xFF);
    mytimestamp[6] = (byte)(cal.get(11) & 0xFF);
    mytimestamp[7] = (byte)(cal.get(12) & 0xFF);
    mytimestamp[8] = (byte)(cal.get(13) & 0xFF);
    mytimestamp[9] = 0;
    mytimestamp[10] = 14;
    mytimestamp[11] = 0;
    return mytimestamp;
  }
  private void writeTD(long TDBlock) {
    byte[] td = new byte[2048];
    byte[] td_body = new byte[496];
    int DescriptorCRC = checkSum(td_body);
    td[0] = 8;
    td[1] = 0;
    td[2] = 2;
    td[3] = 0;
    td[5] = 0;
    td[6] = 1;
    td[7] = 0;
    td[8] = (byte)(DescriptorCRC >> 0 & 0xFF);
    td[9] = (byte)(DescriptorCRC >> 8 & 0xFF);
    td[10] = -16;
    td[11] = 1;
    td[12] = (byte)(int)(TDBlock >> 0L & 0xFFL);
    td[13] = (byte)(int)(TDBlock >> 8L & 0xFFL);
    td[14] = (byte)(int)(TDBlock >> 16L & 0xFFL);
    td[15] = (byte)(int)(TDBlock >> 24L & 0xFFL);
    int checksum = td[0] + td[1] + td[2] + td[3] + td[5] + td[6] + td[7] + td[8] + td[9] + td[10] + td[11] + td[12] + td[13] + td[14] + td[15] & 0xFF;
    td[4] = (byte)(checksum & 0xFF);
    for (int i = 0; i < td_body.length; i++) {
      td[i + 16] = td_body[i];
    }
    writeToMap(td);
  }
  private void writeIUVD(long IUVDBlock) {
    byte[] iuvd = new byte[2048];
    byte[] iuvd_body = new byte[496];
    iuvd_body[0] = 5;
    iuvd_body[1] = 0;
    iuvd_body[2] = 0;
    iuvd_body[3] = 0;
    iuvd_body[4] = 0;
    byte[] ImplementationIdentifier = stringToBytes("*UDF LV Info"); int i;
    for (i = 0; i < ImplementationIdentifier.length; i++) {
      iuvd_body[i + 5] = (byte)(ImplementationIdentifier[i] & 0xFF);
    }
    for (i = 0; i < 8; i++) {
      iuvd_body[i + 28] = this.udfVersionIdentifierSuffix[i];
    }
    iuvd_body[36] = 0;
    byte[] DescriptorCharacterSet = stringToBytes("OSTA Compressed Unicode");
    for (int j = 0; j < DescriptorCharacterSet.length; j++) {
      iuvd_body[j + 37] = (byte)(DescriptorCharacterSet[j] & 0xFF);
    }
    byte[] LogicalVolumeIdentifier = stringToBytes1(this.imageIdentifier);
    for (int k = 0; k < LogicalVolumeIdentifier.length; k++) {
      iuvd_body[k + 100] = LogicalVolumeIdentifier[k];
    }
    iuvd_body[227] = (byte)(LogicalVolumeIdentifier.length & 0xFF);
    iuvd_body[336] = 0;
    byte[] ImplementationIdentifier2 = stringToBytes(this.applicationIdentifier);
    byte[] ImplementationIdentifierSuffix = this.applicationIdentifierSuffix; int m;
    for (m = 0; m < ImplementationIdentifier.length; m++) {
      iuvd_body[337 + m] = (byte)(ImplementationIdentifier2[m] & 0xFF);
    }
    for (m = 0; m < ImplementationIdentifierSuffix.length; m++) {
      iuvd_body[360 + m] = (byte)(ImplementationIdentifierSuffix[m] & 0xFF);
    }
    int DescriptorCRC = checkSum(iuvd_body);
    iuvd[0] = 4;
    iuvd[1] = 0;
    iuvd[2] = 2;
    iuvd[3] = 0;
    iuvd[5] = 0;
    iuvd[6] = 1;
    iuvd[7] = 0;
    iuvd[8] = (byte)(DescriptorCRC >> 0 & 0xFF);
    iuvd[9] = (byte)(DescriptorCRC >> 8 & 0xFF);
    iuvd[10] = -16;
    iuvd[11] = 1;
    iuvd[12] = (byte)(int)(IUVDBlock >> 0L & 0xFFL);
    iuvd[13] = (byte)(int)(IUVDBlock >> 8L & 0xFFL);
    iuvd[14] = (byte)(int)(IUVDBlock >> 16L & 0xFFL);
    iuvd[15] = (byte)(int)(IUVDBlock >> 24L & 0xFFL);
    int checksum = iuvd[0] + iuvd[1] + iuvd[2] + iuvd[3] + iuvd[5] + iuvd[6] + iuvd[7] + iuvd[8] + iuvd[9] + iuvd[10] + iuvd[11] + iuvd[12] + iuvd[13] + iuvd[14] + iuvd[15] & 0xFF;
    iuvd[4] = (byte)(checksum & 0xFF);
    for (int n = 0; n < iuvd_body.length; n++) {
      iuvd[n + 16] = iuvd_body[n];
    }
    writeToMap(iuvd);
  }
  private void writeUSD(long USDBlock) {
    byte[] usd = new byte[2048];
    byte[] usd_body = new byte[16];
    usd_body[0] = 4;
    usd_body[1] = 0;
    usd_body[2] = 0;
    usd_body[3] = 0;
    usd_body[4] = 1;
    usd_body[5] = 0;
    usd_body[6] = 0;
    usd_body[7] = 0;
    long usd_length = 485376L;
    usd_body[8] = (byte)(int)(usd_length >> 0L & 0xFFL);
    usd_body[9] = (byte)(int)(usd_length >> 8L & 0xFFL);
    usd_body[10] = (byte)(int)(usd_length >> 16L & 0xFFL);
    usd_body[11] = (byte)(int)(usd_length >> 24L & 0xFFL);
    usd_body[12] = 19;
    usd_body[13] = 0;
    usd_body[14] = 0;
    usd_body[15] = 0;
    int DescriptorCRC = checkSum(usd_body);
    usd[0] = 7;
    usd[1] = 0;
    usd[2] = 2;
    usd[3] = 0;
    usd[5] = 0;
    usd[6] = 1;
    usd[7] = 0;
    usd[8] = (byte)(DescriptorCRC >> 0 & 0xFF);
    usd[9] = (byte)(DescriptorCRC >> 8 & 0xFF);
    usd[10] = 16;
    usd[11] = 0;
    usd[12] = (byte)(int)(USDBlock >> 0L & 0xFFL);
    usd[13] = (byte)(int)(USDBlock >> 8L & 0xFFL);
    usd[14] = (byte)(int)(USDBlock >> 16L & 0xFFL);
    usd[15] = (byte)(int)(USDBlock >> 24L & 0xFFL);
    int checksum = usd[0] + usd[1] + usd[2] + usd[3] + usd[5] + usd[6] + usd[7] + usd[8] + usd[9] + usd[10] + usd[11] + usd[12] + usd[13] + usd[14] + usd[15] & 0xFF;
    usd[4] = (byte)(checksum & 0xFF);
    for (int i = 0; i < usd_body.length; i++) {
      usd[i + 16] = usd_body[i];
    }
    writeToMap(usd);
  }
  private void writeLVD(UdfLayoutInformation myUDFLayoutInformation, long LVDBlock) {
    byte[] lvd = new byte[2048];
    byte[] lvd_body = new byte[430];
    lvd_body[0] = 3;
    lvd_body[1] = 0;
    lvd_body[2] = 0;
    lvd_body[3] = 0;
    lvd_body[4] = 0;
    byte[] DescriptorCharacterSet = stringToBytes("OSTA Compressed Unicode");
    for (int i = 0; i < DescriptorCharacterSet.length; i++) {
      lvd_body[i + 5] = (byte)(DescriptorCharacterSet[i] & 0xFF);
    }
    byte[] LogicalVolumeIdentifier = stringToBytes1(this.imageIdentifier);
    for (int j = 0; j < LogicalVolumeIdentifier.length; j++) {
      lvd_body[j + 68] = LogicalVolumeIdentifier[j];
    }
    lvd_body[195] = (byte)(LogicalVolumeIdentifier.length & 0xFF);
    lvd_body[196] = 0;
    lvd_body[197] = 8;
    lvd_body[198] = 0;
    lvd_body[199] = 0;
    lvd_body[200] = 0;
    byte[] DomainIdentifier = stringToBytes("*OSTA UDF Compliant"); int k;
    for (k = 0; k < DomainIdentifier.length; k++) {
      lvd_body[201 + k] = DomainIdentifier[k];
    }
    for (k = 0; k < 8; k++) {
      lvd_body[224 + k] = this.udfVersionIdentifierSuffix[k];
    }
    lvd_body[232] = 0;
    lvd_body[233] = 8;
    lvd_body[234] = 0;
    lvd_body[235] = 0;
    lvd_body[236] = (byte)(int)(myUDFLayoutInformation.FSDLocation >> 0L & 0xFFL);
    lvd_body[237] = (byte)(int)(myUDFLayoutInformation.FSDLocation >> 8L & 0xFFL);
    lvd_body[238] = (byte)(int)(myUDFLayoutInformation.FSDLocation >> 16L & 0xFFL);
    lvd_body[239] = (byte)(int)(myUDFLayoutInformation.FSDLocation >> 24L & 0xFFL);
    lvd_body[240] = (byte)(myUDFLayoutInformation.partitionToStoreMetadataOn >> 16 & 0xFF);
    lvd_body[241] = (byte)(myUDFLayoutInformation.partitionToStoreMetadataOn >> 24 & 0xFF);
    lvd_body[248] = 6;
    lvd_body[249] = 0;
    lvd_body[250] = 0;
    lvd_body[251] = 0;
    lvd_body[252] = 1;
    lvd_body[253] = 0;
    lvd_body[254] = 0;
    lvd_body[255] = 0;
    lvd_body[256] = 0;
    byte[] ImplementationIdentifier = stringToBytes(this.applicationIdentifier); int m;
    for (m = 0; m < ImplementationIdentifier.length; m++) {
      lvd_body[m + 257] = (byte)(ImplementationIdentifier[m] & 0xFF);
    }
    for (m = 0; m < 8; m++) {
      lvd_body[m + 280] = this.applicationIdentifierSuffix[m];
    }
    long lvid_length = (myUDFLayoutInformation.LVIDSEndingBlock - myUDFLayoutInformation.LVIDSStartingBlock) * 2048L;
    lvd_body[416] = (byte)(int)(lvid_length >> 0L & 0xFFL);
    lvd_body[417] = (byte)(int)(lvid_length >> 8L & 0xFFL);
    lvd_body[418] = (byte)(int)(lvid_length >> 16L & 0xFFL);
    lvd_body[419] = (byte)(int)(lvid_length >> 24L & 0xFFL);
    lvd_body[420] = (byte)(int)(myUDFLayoutInformation.LVIDSStartingBlock >> 0L & 0xFFL);
    lvd_body[421] = (byte)(int)(myUDFLayoutInformation.LVIDSStartingBlock >> 8L & 0xFFL);
    lvd_body[422] = (byte)(int)(myUDFLayoutInformation.LVIDSStartingBlock >> 16L & 0xFFL);
    lvd_body[423] = (byte)(int)(myUDFLayoutInformation.LVIDSStartingBlock >> 24L & 0xFFL);
    lvd_body[424] = 1;
    lvd_body[425] = 6;
    lvd_body[426] = 1;
    lvd_body[427] = 0;
    lvd_body[428] = 0;
    lvd_body[429] = 0;
    int DescriptorCRC = checkSum(lvd_body);
    lvd[0] = 6;
    lvd[1] = 0;
    lvd[2] = 2;
    lvd[3] = 0;
    lvd[5] = 0;
    lvd[6] = 1;
    lvd[7] = 0;
    lvd[8] = (byte)(DescriptorCRC >> 0 & 0xFF);
    lvd[9] = (byte)(DescriptorCRC >> 8 & 0xFF);
    lvd[10] = -82;
    lvd[11] = 1;
    lvd[12] = (byte)(int)(LVDBlock >> 0L & 0xFFL);
    lvd[13] = (byte)(int)(LVDBlock >> 8L & 0xFFL);
    lvd[14] = (byte)(int)(LVDBlock >> 16L & 0xFFL);
    lvd[15] = (byte)(int)(LVDBlock >> 24L & 0xFFL);
    int checksum = lvd[0] + lvd[1] + lvd[2] + lvd[3] + lvd[5] + lvd[6] + lvd[7] + lvd[8] + lvd[9] + lvd[10] + lvd[11] + lvd[12] + lvd[13] + lvd[14] + lvd[15] & 0xFF;
    lvd[4] = (byte)(checksum & 0xFF);
    for (int n = 0; n < lvd_body.length; n++) {
      lvd[n + 16] = lvd_body[n];
    }
    writeToMap(lvd);
  }
  private void writePD(UdfLayoutInformation myUDFLayoutInformation, long PDBlock) {
    byte[] pd = new byte[2048];
    byte[] pd_body = new byte[496];
    pd_body[0] = 2;
    pd_body[1] = 0;
    pd_body[2] = 0;
    pd_body[3] = 0;
    pd_body[4] = 1;
    pd_body[5] = 0;
    pd_body[6] = 0;
    pd_body[7] = 0;
    pd_body[8] = 0;
    byte[] PartitionContentIdentifie = stringToBytes("+NSR02");
    for (int i = 0; i < PartitionContentIdentifie.length; i++) {
      pd_body[9 + i] = (byte)(PartitionContentIdentifie[i] & 0xFF);
    }
    pd_body[168] = 1;
    pd_body[169] = 0;
    pd_body[170] = 0;
    pd_body[171] = 0;
    pd_body[172] = (byte)(int)(myUDFLayoutInformation.physicalPartitionStartingBlock >> 0L & 0xFFL);
    pd_body[173] = (byte)(int)(myUDFLayoutInformation.physicalPartitionStartingBlock >> 8L & 0xFFL);
    pd_body[174] = (byte)(int)(myUDFLayoutInformation.physicalPartitionStartingBlock >> 16L & 0xFFL);
    pd_body[175] = (byte)(int)(myUDFLayoutInformation.physicalPartitionStartingBlock >> 24L & 0xFFL);
    long pd_part_length = myUDFLayoutInformation.physicalPartitionEndingBlock - myUDFLayoutInformation.physicalPartitionStartingBlock;
    pd_body[176] = (byte)(int)(pd_part_length >> 0L & 0xFFL);
    pd_body[177] = (byte)(int)(pd_part_length >> 8L & 0xFFL);
    pd_body[178] = (byte)(int)(pd_part_length >> 16L & 0xFFL);
    pd_body[179] = (byte)(int)(pd_part_length >> 24L & 0xFFL);
    pd_body[180] = 0;
    byte[] ImplementationIdentifier = stringToBytes(this.applicationIdentifier);
    byte[] ImplementationIdentifierSuffix = this.applicationIdentifierSuffix; int j;
    for (j = 0; j < ImplementationIdentifier.length; j++) {
      pd_body[181 + j] = (byte)(ImplementationIdentifier[j] & 0xFF);
    }
    for (j = 0; j < ImplementationIdentifierSuffix.length; j++) {
      pd_body[204 + j] = (byte)(ImplementationIdentifierSuffix[j] & 0xFF);
    }
    int DescriptorCRC = checkSum(pd_body);
    pd[0] = 5;
    pd[1] = 0;
    pd[2] = 2;
    pd[3] = 0;
    pd[5] = 0;
    pd[6] = 1;
    pd[7] = 0;
    pd[8] = (byte)(DescriptorCRC >> 0 & 0xFF);
    pd[9] = (byte)(DescriptorCRC >> 8 & 0xFF);
    pd[10] = -16;
    pd[11] = 1;
    pd[12] = (byte)(int)(PDBlock >> 0L & 0xFFL);
    pd[13] = (byte)(int)(PDBlock >> 8L & 0xFFL);
    pd[14] = (byte)(int)(PDBlock >> 16L & 0xFFL);
    pd[15] = (byte)(int)(PDBlock >> 24L & 0xFFL);
    int checksum = pd[0] + pd[1] + pd[2] + pd[3] + pd[5] + pd[6] + pd[7] + pd[8] + pd[9] + pd[10] + pd[11] + pd[12] + pd[13] + pd[14] + pd[15] & 0xFF;
    pd[4] = (byte)(checksum & 0xFF);
    for (int k = 0; k < pd_body.length; k++) {
      pd[k + 16] = pd_body[k];
    }
    writeToMap(pd);
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
  private byte[] stringToBytes(String str) {
    try {
      byte[] b = str.getBytes("UTF-8");
      return b;
    } catch (UnsupportedEncodingException e) {
      LoggerUtil.error("UnsupportedEncodingException");
      byte[] b2 = new byte[1];
      return b2;
    } 
  }
  private byte[] millis2TimeStamp(Calendar cal) {
    byte[] mytimestamp = new byte[12];
    int YY = cal.get(1);
    int MM = cal.get(2) + 1;
    int DD = cal.get(5);
    int HH = cal.get(11);
    int mm = cal.get(12);
    int SS = cal.get(13);
    int MI = cal.get(14);
    int twelveBitSignedValue = -(cal.get(15) + cal.get(16)) / 60000;
    if (twelveBitSignedValue < 0) {
      twelveBitSignedValue *= -1;
    }
    int TypeAndTimezone = 0x1000 | twelveBitSignedValue;
    mytimestamp[0] = (byte)(TypeAndTimezone >> 0 & 0xFF);
    mytimestamp[1] = (byte)(TypeAndTimezone >> 8 & 0xFF);
    mytimestamp[2] = (byte)(YY >> 0 & 0xFF);
    mytimestamp[3] = (byte)(YY >> 8 & 0xFF);
    mytimestamp[4] = (byte)(MM & 0xFF);
    mytimestamp[5] = (byte)(DD & 0xFF);
    mytimestamp[6] = (byte)(HH & 0xFF);
    mytimestamp[7] = (byte)(mm & 0xFF);
    mytimestamp[8] = (byte)(SS & 0xFF);
    mytimestamp[9] = (byte)(MI / 100 & 0xFF);
    mytimestamp[10] = (byte)(MI % 100 & 0xFF);
    mytimestamp[11] = 0;
    return mytimestamp;
  }
  public byte[] int64ToBytes(long value) {
    byte[] src = new byte[8];
    src[0] = (byte)(int)(value & 0xFFL);
    src[1] = (byte)(int)(value >> 8L & 0xFFL);
    src[2] = (byte)(int)(value >> 16L & 0xFFL);
    src[3] = (byte)(int)(value >> 24L & 0xFFL);
    src[4] = (byte)(int)(value >> 32L & 0xFFL);
    src[5] = (byte)(int)(value >> 40L & 0xFFL);
    src[6] = (byte)(int)(value >> 48L & 0xFFL);
    src[7] = (byte)(int)(value >> 56L & 0xFFL);
    return src;
  }
  private void writeEmptyBlock(int blocks) {
    int bufferLength = blocks * 2048;
    byte[] buffer = new byte[bufferLength];
    writeToMap(buffer);
  }
  private void writeReserve() {
    writeEmptyBlock(16);
  }
  private void writeEmptyArea() {
    writeEmptyBlock(237);
  }
  private void writeEmptyArea2() {
    writeEmptyBlock(10);
  }
  private void writeEmptyArea3() {
    writeEmptyBlock(2);
  }
  private void writeEmptyArea4() {
    writeEmptyBlock(1);
  }
  private void writeAVDP(long tagLocation, long MVDSBlock, long RVDSBlock) {
    int bufferLength = 2048;
    byte[] avdp = new byte[bufferLength];
    byte[] avdp_body = new byte[496];
    long mvds_len = 32768L;
    avdp_body[0] = (byte)(int)(mvds_len >> 0L & 0xFFL);
    avdp_body[1] = (byte)(int)(mvds_len >> 8L & 0xFFL);
    avdp_body[2] = (byte)(int)(mvds_len >> 16L & 0xFFL);
    avdp_body[3] = (byte)(int)(mvds_len >> 24L & 0xFFL);
    long mvds_loc = MVDSBlock;
    avdp_body[4] = (byte)(int)(mvds_loc >> 0L & 0xFFL);
    avdp_body[5] = (byte)(int)(mvds_loc >> 8L & 0xFFL);
    avdp_body[6] = (byte)(int)(mvds_loc >> 16L & 0xFFL);
    avdp_body[7] = (byte)(int)(mvds_loc >> 24L & 0xFFL);
    long rvds_len = 32768L;
    avdp_body[8] = (byte)(int)(rvds_len >> 0L & 0xFFL);
    avdp_body[9] = (byte)(int)(rvds_len >> 8L & 0xFFL);
    avdp_body[10] = (byte)(int)(rvds_len >> 16L & 0xFFL);
    avdp_body[11] = (byte)(int)(rvds_len >> 24L & 0xFFL);
    long rvds_loc = RVDSBlock;
    avdp_body[12] = (byte)(int)(rvds_loc >> 0L & 0xFFL);
    avdp_body[13] = (byte)(int)(rvds_loc >> 8L & 0xFFL);
    avdp_body[14] = (byte)(int)(rvds_loc >> 16L & 0xFFL);
    avdp_body[15] = (byte)(int)(rvds_loc >> 24L & 0xFFL);
    int DescriptorCRC = checkSum(avdp_body);
    avdp[0] = 2;
    avdp[1] = 0;
    avdp[2] = 2;
    avdp[3] = 0;
    avdp[5] = 0;
    avdp[6] = 1;
    avdp[7] = 0;
    avdp[8] = (byte)(DescriptorCRC >> 0 & 0xFF);
    avdp[9] = (byte)(DescriptorCRC >> 8 & 0xFF);
    avdp[10] = -16;
    avdp[11] = 1;
    avdp[12] = (byte)(int)(tagLocation >> 0L & 0xFFL);
    avdp[13] = (byte)(int)(tagLocation >> 8L & 0xFFL);
    avdp[14] = (byte)(int)(tagLocation >> 16L & 0xFFL);
    avdp[15] = (byte)(int)(tagLocation >> 24L & 0xFFL);
    int checksum = avdp[0] + avdp[1] + avdp[2] + avdp[3] + avdp[5] + avdp[6] + avdp[7] + avdp[8] + avdp[9] + avdp[10] + avdp[11] + avdp[12] + avdp[13] + avdp[14] + avdp[15] & 0xFF;
    avdp[4] = (byte)(checksum & 0xFF);
    for (int i = 0; i < avdp_body.length; i++) {
      avdp[i + 16] = avdp_body[i];
    }
    writeToMap(avdp);
  }
  private int checkSum(byte[] s) {
    int[] crc_table = { 0, 4129, 8258, 12387, 16516, 20645, 24774, 28903, 33032, 37161, 41290, 45419, 49548, 53677, 57806, 61935, 4657, 528, 12915, 8786, 21173, 17044, 29431, 25302, 37689, 33560, 45947, 41818, 54205, 50076, 62463, 58334, 9314, 13379, 1056, 5121, 25830, 29895, 17572, 21637, 42346, 46411, 34088, 38153, 58862, 62927, 50604, 54669, 13907, 9842, 5649, 1584, 30423, 26358, 22165, 18100, 46939, 42874, 38681, 34616, 63455, 59390, 55197, 51132, 18628, 22757, 26758, 30887, 2112, 6241, 10242, 14371, 51660, 55789, 59790, 63919, 35144, 39273, 43274, 47403, 23285, 19156, 31415, 27286, 6769, 2640, 14899, 10770, 56317, 52188, 64447, 60318, 39801, 35672, 47931, 43802, 27814, 31879, 19684, 23749, 11298, 15363, 3168, 7233, 60846, 64911, 52716, 56781, 44330, 48395, 36200, 40265, 32407, 28342, 24277, 20212, 15891, 11826, 7761, 3696, 65439, 61374, 57309, 53244, 48923, 44858, 40793, 36728, 37256, 33193, 45514, 41451, 53516, 49453, 61774, 57711, 4224, 161, 12482, 8419, 20484, 16421, 28742, 24679, 33721, 37784, 41979, 46042, 49981, 54044, 58239, 62302, 689, 4752, 8947, 13010, 16949, 21012, 25207, 29270, 46570, 42443, 38312, 34185, 62830, 58703, 54572, 50445, 13538, 9411, 5280, 1153, 29798, 25671, 21540, 17413, 42971, 47098, 34713, 38840, 59231, 63358, 50973, 55100, 9939, 14066, 1681, 5808, 26199, 30326, 17941, 22068, 55628, 51565, 63758, 59695, 39368, 35305, 47498, 43435, 22596, 18533, 30726, 26663, 6336, 2273, 14466, 10403, 52093, 56156, 60223, 64286, 35833, 39896, 43963, 48026, 19061, 23124, 27191, 31254, 2801, 6864, 10931, 14994, 64814, 60687, 56684, 52557, 48554, 44427, 40424, 36297, 31782, 27655, 23652, 19525, 15522, 11395, 7392, 3265, 61215, 65342, 53085, 57212, 44955, 49082, 36825, 40952, 28183, 32310, 20053, 24180, 11923, 16050, 3793, 7920 };
    int crc = 0;
    for (int n = 0; n < s.length; n++) {
      crc = crc_table[(crc >> 8 ^ s[n]) & 0xFF] ^ crc << 8;
    }
    return crc & 0xFFFF;
  }
  private void writeVRS() {
    byte[] vrsBegin = new byte[2048];
    vrsBegin[0] = 0;
    byte[] vrsBeginId = stringToBytes("BEA01");
    for (int i = 0; i < 5; i++) {
      vrsBegin[i + 1] = (byte)(vrsBeginId[i] & 0xFF);
    }
    vrsBegin[6] = 1;
    byte[] vrsDescriptor = new byte[2048];
    vrsDescriptor[0] = 0;
    byte[] vrsDescriptorId = stringToBytes("NSR02");
    for (int j = 0; j < 5; j++) {
      vrsDescriptor[j + 1] = (byte)(vrsDescriptorId[j] & 0xFF);
    }
    vrsDescriptor[6] = 1;
    byte[] vrsTerminating = new byte[2048];
    vrsTerminating[0] = 0;
    byte[] vrsTerminatingId = stringToBytes("TEA01");
    for (int k = 0; k < 5; k++) {
      vrsTerminating[k + 1] = (byte)(vrsTerminatingId[k] & 0xFF);
    }
    vrsTerminating[6] = 1;
    byte[] vrssum = new byte[6144];
    for (int i1 = 0; i1 < 2048; i1++)
      vrssum[i1] = vrsBegin[i1]; 
    for (int i2 = 2048; i2 < 4096; i2++)
      vrssum[i2] = vrsDescriptor[i2 - 2048]; 
    for (int i3 = 4096; i3 < 6144; i3++)
      vrssum[i3] = vrsTerminating[i3 - 4096]; 
    writeToMap(vrssum);
  }
  private void writeToMap(byte[] buf) {
    UDFExtendFile udfFile = new UDFExtendFile();
    udfFile.setContent(buf);
    udfFile.setExtendData(false);
    udfFile.setFileActalLen(0L);
    udfFile.setFileLen(buf.length);
    long lastposition = ((Long)this.keylist.get(this.keylist.size() - 1)).longValue();
    udfFile.setPosition(lastposition);
    long nowposition = lastposition + udfFile.getFileLen();
    this.keylist.add(Long.valueOf(nowposition));
    this.extendMap.put(Long.valueOf(udfFile.getPosition()), udfFile);
  }
  private void writeToMapLarge(File sourceFile) {
    UDFExtendFile udfFile = new UDFExtendFile();
    try {
      udfFile.setContent(sourceFile.getAbsolutePath().getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      LoggerUtil.error("UnsupportedEncodingException ");
    } 
    udfFile.setExtendData(true);
    long fileLen = sourceFile.length();
    udfFile.setFileActalLen(fileLen);
    if (0L == fileLen % this.blockSize) {
      udfFile.setFileLen(fileLen);
    }
    else {
      udfFile.setFileLen(fileLen + this.blockSize - fileLen % this.blockSize);
    } 
    long lastposition = ((Long)this.keylist.get(this.keylist.size() - 1)).longValue();
    udfFile.setPosition(lastposition);
    long nowposition = lastposition + udfFile.getFileLen();
    this.keylist.add(Long.valueOf(nowposition));
    this.extendMap.put(Long.valueOf(udfFile.getPosition()), udfFile);
  }
}
