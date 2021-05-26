package com.library.decoder;
import com.library.LibException;
import com.library.LoggerUtil;
import java.awt.Image;
import java.util.ArrayList;
public class ImageDecoder
{
  private static int blockImageWidth = 64;
  private static int blockImageHeight = 64;
  final byte[] flagValue = new byte[] { 0, 1, 3, 7, 15, 31, 63, Byte.MAX_VALUE, -1 };
  int blockXcount = 0;
  int blockYcount = 0;
  int blockcount = 0;
  int blockCutWidth = 0;
  int blockCutHeight = 0;
  int imageWidth = 0;
  int imageHeight = 0;
  ImageBlock[] imageBlocks = null;
  ImageCreater imageCreater = null;
  private ArrayList<byte[]> zipImages;
  int num;
  int tnum;
  private byte[] zipImage;
  void init(int imageWidth, int imageHeight) {
    this.imageWidth = imageWidth;
    this.imageHeight = imageHeight;
    this.blockXcount = imageWidth / blockImageWidth + ((imageWidth % blockImageWidth == 0) ? 0 : 1);
    this.blockYcount = imageHeight / blockImageHeight + ((imageHeight % blockImageHeight == 0) ? 0 : 1);
    this.blockcount = this.blockXcount * this.blockYcount;
    this.blockCutWidth = blockImageWidth - this.blockXcount * blockImageWidth - imageWidth;
    this.blockCutHeight = blockImageHeight - this.blockYcount * blockImageHeight - imageHeight;
    this.imageBlocks = new ImageBlock[this.blockcount];
  }
  public void reInit(int imageWidth, int imageHeight) {
    this.imageWidth = imageWidth;
    this.imageHeight = imageHeight;
    this.blockXcount = imageWidth / blockImageWidth + ((imageWidth % blockImageWidth == 0) ? 0 : 1);
    this.blockYcount = imageHeight / blockImageHeight + ((imageHeight % blockImageHeight == 0) ? 0 : 1);
    this.blockcount = this.blockXcount * this.blockYcount;
    this.blockCutWidth = blockImageWidth - this.blockXcount * blockImageWidth - imageWidth;
    this.blockCutHeight = blockImageHeight - this.blockYcount * blockImageHeight - imageHeight;
    this.imageBlocks = new ImageBlock[this.blockcount];
  }
  public ImageDecoder() { 
	this.zipImages = (ArrayList)new ArrayList<>(10);
    this.num = 0;
    this.tnum = 0;
    this.zipImage = null; 
    init(1024, 1024); 
    this.imageCreater = new ImageCreater(); 
    }
  public ImageDecoder(int imageWidth, int imageHeight) { 
	this.zipImages = (ArrayList)new ArrayList<>(10); 
	this.num = 0; 
	this.tnum = 0; 
	this.zipImage = null;
    init(imageWidth, imageHeight);
    this.imageCreater = new ImageCreater(); }
  public Object decodeRLEorJPEG0(byte[] zipDatas, int imageWidth, int imageHeight) throws LibException { Object obj = null;
    try {
      this.zipImage = (byte[])zipDatas.clone();
      obj = decodeRLEorJPEG1(this.zipImage, imageWidth, imageHeight);
    }
    catch (Exception e1) {
      LoggerUtil.error(e1.getClass().getName());
    } 
    return obj; }
  public Object decodeRLEorJPEG3(byte[] zipDatas, int imageWidth, int imageHeight) throws LibException {
    int syclen = 0;
    int subSyclen = 0;
    int zipType = 0;
    int rZipType = 0;
    int len = 0;
    byte bufColor1 = 0;
    byte bufColor2 = 0;
    byte bufColor3 = 0;
    byte bgr233 = 0;
    int subLen = 0;
    int indexType = 0;
    int change_flag = 0;
    int tmpdata = 0;
    int lastnum = 0;
    int subPixlen = 0;
    byte[] imageData = new byte[12288];
    byte[] zipData = null;
    int butcolor1 = 0;
    int butcolor2 = 0;
    int butcolor3 = 0;
    int blocknum = 0;
    int rgb322Color = 0;
    ImageBlock imageBlock = null;
    byte[] temBlockData = null;
    byte[] temBlockData1 = null;
    int index = 0;
    if (this.imageWidth != imageWidth || this.imageHeight != imageHeight)
    {
      init(imageWidth, imageHeight);
    }
    int i;
    for (i = 8; i < zipDatas.length; i += syclen) {
      int j, m;
      byte[] temcolor1;
      Image JPEGimage;
      int k;
      zipType = (zipDatas[i] & 0xFF & 0xE0) >> 5;
      rZipType = (zipDatas[i] & 0xFF & 0x1C) >> 2;
      imageBlock = null;
      switch (zipType) {
        case 0:
        case 1:
          switch (rZipType) {
            case 0:
              bufColor1 = zipDatas[i + 1];
              bufColor2 = zipDatas[i + 2];
              bufColor3 = zipDatas[i + 3];
              bgr233 = ColorConverter.ycbcr2rgb332(bufColor1, bufColor2, bufColor3);
              imageData = new byte[4096];
              for (j = 0; j < imageData.length; j++)
              {
                imageData[j] = bgr233;
              }
              syclen = 4;
              break;
            case 1:
              tmpdata = 0;
              lastnum = 0;
              butcolor1 = zipDatas[i + 3];
              butcolor2 = zipDatas[i + 3 + 1];
              butcolor3 = zipDatas[i + 3 + 2];
              subSyclen = 0;
              len = ((zipDatas[i + 1] & 0xFF) << 8) + (zipDatas[i + 2] & 0xFF);
              temBlockData = new byte[len];
              System.arraycopy(zipDatas, i + 1 + 2 + 6, temBlockData, 0, len);
              for (m = 0; m < temBlockData.length; m++) {
                if (lastnum <= 8) {
                  tmpdata = tmpdata & 0xFFFF | (temBlockData[m] & 0xFF) << 8 - lastnum;
                  lastnum += 8;
                } 
                subPixlen = ((tmpdata & 0xFC00) >> 10) + 1;
                tmpdata = tmpdata << 6 & 0xFFFF;
                lastnum -= 6;
                if (subPixlen < 64) {
                  change_flag = 0;
                }
                else {
                  change_flag = (tmpdata & 0x200) >> 1;
                  tmpdata <<= 1;
                  lastnum--;
                } 
                rgb322Color = ColorConverter.ycbcr2rgb332(butcolor1, butcolor2, butcolor3);
                imageData = new byte[4096];
                for (int n = 0; n < subPixlen; n++)
                {
                  imageData[subSyclen + n] = (byte)rgb322Color;
                }
                subSyclen += subPixlen;
                if (change_flag == 0)
                {
                  if ((byte)butcolor1 == zipDatas[i + 3 + 0] && (byte)butcolor1 == zipDatas[i + 3 + 1] && (byte)butcolor1 == zipDatas[i + 3 + 2]) {
                    butcolor1 = zipDatas[i + 3 + 3];
                    butcolor2 = zipDatas[i + 3 + 4];
                    butcolor3 = zipDatas[i + 3 + 5];
                  }
                  else {
                    butcolor1 = zipDatas[i + 3 + 0];
                    butcolor2 = zipDatas[i + 3 + 1];
                    butcolor3 = zipDatas[i + 3 + 2];
                  } 
                }
              } 
              syclen = 9 + len;
              break;
            case 2:
            case 3:
              temcolor1 = new byte[] { ColorConverter.ycbcr2rgb332(zipDatas[i + 3 + 0], zipDatas[i + 3 + 1], zipDatas[i + 3 + 2]), ColorConverter.ycbcr2rgb332(zipDatas[i + 3 + 0], zipDatas[i + 3 + 1], zipDatas[i + 3 + 2]), ColorConverter.ycbcr2rgb332(zipDatas[i + 3 + 0], zipDatas[i + 3 + 1], zipDatas[i + 3 + 2]) };
              subSyclen = 0;
              indexType = 4;
              if (rZipType == 2)
              {
                indexType = 3;
              }
              len = ((zipDatas[i + 1] & 0xFF) << 8) + (zipDatas[i + 2] & 0xFF);
              temBlockData1 = new byte[len];
              System.arraycopy(zipDatas, i + 1 + 2 + indexType * 3, temBlockData1, 0, len);
              for (k = 0; k < temBlockData1.length; k++) {
                subLen = (temBlockData1[i] & this.flagValue[6]) >> 2;
                index = temBlockData1[i] & this.flagValue[2];
                imageData = new byte[4096];
                for (int n = 0; n < subLen; n++)
                {
                  imageData[subSyclen + n] = temcolor1[index];
                }
                subSyclen += subLen * 3;
              } 
              syclen = 3 + indexType * 3 + len;
              break;
            case 4:
              imageBlock = new ImageBlock();
              imageBlock.setImage(this.imageBlocks[blocknum - 1].getImage());
              imageBlock.setFill(this.imageBlocks[blocknum - 1].isFill());
              imageBlock.setCutWidth(this.imageBlocks[blocknum - 1].getCutWidth());
              imageBlock.setCutHeight(this.imageBlocks[blocknum - 1].getCutHeight());
              this.imageBlocks[blocknum] = imageBlock;
              syclen = 1;
              break;
            case 6:
              imageBlock = new ImageBlock();
              imageBlock.setImage(this.imageBlocks[blocknum - this.blockXcount].getImage());
              imageBlock.setFill(this.imageBlocks[blocknum - this.blockXcount].isFill());
              imageBlock.setCutWidth(this.imageBlocks[blocknum - this.blockXcount].getCutWidth());
              imageBlock.setCutHeight(this.imageBlocks[blocknum - this.blockXcount].getCutHeight());
              this.imageBlocks[blocknum] = imageBlock;
              syclen = 1;
              break;
            case 5:
            case 7:
              syclen = 3 + len;
              break;
            default:
              throw new LibException("RLE imagedata error");
          } 
          if (rZipType != 4 && rZipType != 6) {
            if (blocknum > this.blockcount)
            {
              LoggerUtil.error("zip data fail! ");
            }
            Image imageRLE = this.imageCreater.createRLEImg_0(imageData, blockImageWidth, blockImageHeight);
            imageBlock = new ImageBlock();
            imageBlock.setImage(imageRLE);
            this.imageBlocks[blocknum] = imageBlock;
          } 
          break;
        case 2:
        case 3:
          len = ((zipDatas[i + 1] & 0xFF) << 8) + (zipDatas[i + 2] & 0xFF);
          zipData = new byte[len];
          System.arraycopy(zipDatas, i + 3, zipData, 0, len);
          syclen = 3 + len;
          JPEGimage = this.imageCreater.JPEGDecodeAsImage(zipData);
          imageBlock = new ImageBlock();
          imageBlock.setImage(JPEGimage);
          this.imageBlocks[blocknum] = imageBlock;
          break;
        case 4:
          syclen = 1;
          break;
        case 5:
          imageBlock = new ImageBlock();
          imageBlock.setImage(this.imageBlocks[blocknum - this.blockXcount].getImage());
          imageBlock.setFill(this.imageBlocks[blocknum - this.blockXcount].isFill());
          imageBlock.setCutHeight(this.imageBlocks[blocknum - this.blockXcount].getCutHeight());
          imageBlock.setCutWidth(this.imageBlocks[blocknum - this.blockXcount].getCutWidth());
          this.imageBlocks[blocknum] = imageBlock;
          syclen = 1;
          break;
        case 6:
          imageBlock = new ImageBlock();
          imageBlock.setImage(this.imageBlocks[blocknum - 1].getImage());
          imageBlock.setFill(this.imageBlocks[blocknum - 1].isFill());
          imageBlock.setCutWidth(this.imageBlocks[blocknum - 1].getCutWidth());
          imageBlock.setCutHeight(this.imageBlocks[blocknum - 1].getCutHeight());
          this.imageBlocks[blocknum] = imageBlock;
          syclen = 1;
          break;
        default:
          throw new LibException("imagedata error");
      } 
      if (imageBlock != null) {
        imageBlock.setY(blocknum / this.blockXcount * blockImageHeight);
        imageBlock.setX(blocknum % this.blockXcount * blockImageWidth);
        if (zipType == 1 || zipType == 3) {
          imageBlock.setFill(true);
          imageBlock.setCutHeight(this.blockCutHeight);
          imageBlock.setCutWidth(this.blockCutWidth);
          if (blocknum / this.blockXcount < this.blockYcount - 1) {
            imageBlock.setCutHeight(blockImageHeight);
          }
          else if (blocknum != this.blockcount - 1) {
            imageBlock.setCutWidth(blockImageWidth);
          } 
        } 
      } 
      blocknum++;
    } 
    return this.imageBlocks.clone();
  } public Object decodeRLEorJPEG(byte[] zipDatas, int imageWidth, int imageHeight) throws LibException { Object obj = null; int width = 0; int height = 0; try {
      if (this.zipImages.size() == 0)
        LoggerUtil.error("zipImages size is null!");  width = (((byte[])this.zipImages.get(this.num))[4] & 0xFF) << 8 | ((byte[])this.zipImages.get(this.num))[5] & 0xFF;
      height = (((byte[])this.zipImages.get(this.num))[6] & 0xFF) << 8 | ((byte[])this.zipImages.get(this.num))[7] & 0xFF;
      obj = decodeRLEorJPEG1(this.zipImages.get(this.num), width, height);
      this.tnum++;
      if (this.tnum >= 5) {
        this.num++;
        this.tnum = 0;
      } 
      if (this.num >= this.zipImages.size())
        this.num = 0; 
    } catch (Exception e1) {
      LoggerUtil.error(e1.getClass().getName());
    } 
    return obj; } public Object decodeRLEorJPEG1(byte[] zipDatas, int imageWidth, int imageHeight) throws LibException { int syclen = 0;
    int zipType = 0;
    int rZipType = 0;
    int len = 0;
    byte[] zipData = null;
    byte[] pixColors = null;
    int blocknum = 0;
    ImageBlock imageBlock = null;
    Object[] reObj = null;
    if (this.imageWidth != imageWidth || this.imageHeight != imageHeight)
    {
      init(imageWidth, imageHeight);
    }
    int i;
    for (i = 1; i < zipDatas.length; i += syclen) {
      int collen4;
      byte[] cols5, synHeadData;
      int cols6;
      byte[] cols7;
      Image JPEGimage;
      zipType = (zipDatas[i] & 0xFF & 0xE0) >> 5;
      rZipType = (zipDatas[i] & 0xFF & 0x1C) >> 2;
      switch (zipType) {
        case 0:
        case 1:
          switch (rZipType) {
            case 0:
            case 1:
            case 2:
            case 3:
              reObj = decodeRle(zipDatas, i, blocknum, rZipType, null);
              imageBlock = (ImageBlock)reObj[1];
              this.imageBlocks[blocknum] = imageBlock;
              syclen = ((Integer)reObj[0]).intValue();
              break;
            case 4:
              if (0 == this.imageBlocks[blocknum - 1].getBlockRleType()) {
                imageBlock = new ImageBlock();
                imageBlock.setImage(this.imageBlocks[blocknum - 1].getImage());
                imageBlock.setFill(this.imageBlocks[blocknum - 1].isFill());
                imageBlock.setCutWidth(this.imageBlocks[blocknum - 1].getCutWidth());
                imageBlock.setCutHeight(this.imageBlocks[blocknum - 1].getCutHeight());
                imageBlock.setBlockType(this.imageBlocks[blocknum - 1].getBlockType());
                imageBlock.setBlockRleType(this.imageBlocks[blocknum - 1].getBlockRleType());
                this.imageBlocks[blocknum] = imageBlock;
                syclen = 1;
                break;
              } 
              collen4 = (this.imageBlocks[blocknum - 1].getPixColors()).length;
              pixColors = new byte[collen4];
              System.arraycopy(this.imageBlocks[blocknum - 1].getPixColors(), 0, pixColors, 0, collen4);
              reObj = decodeRle(zipDatas, i, blocknum, this.imageBlocks[blocknum - 1]
                  .getBlockRleType(), pixColors);
              imageBlock = (ImageBlock)reObj[1];
              this.imageBlocks[blocknum] = imageBlock;
              syclen = ((Integer)reObj[0]).intValue();
              break;
            case 5:
              cols5 = this.imageBlocks[blocknum - 1].getPixColors();
              pixColors = new byte[cols5.length];
              if (1 == this.imageBlocks[blocknum - 1].getBlockRleType()) {
                pixColors[0] = cols5[3];
                pixColors[1] = cols5[4];
                pixColors[2] = cols5[5];
                pixColors[3] = cols5[0];
                pixColors[4] = cols5[1];
                pixColors[5] = cols5[2];
              }
              else {
                System.arraycopy(this.imageBlocks[blocknum - 1].getPixColors(), 0, pixColors, 0, cols5.length);
              } 
              reObj = decodeRle(zipDatas, i, blocknum, this.imageBlocks[blocknum - 1].getBlockRleType(), pixColors);
              imageBlock = (ImageBlock)reObj[1];
              this.imageBlocks[blocknum] = imageBlock;
              syclen = ((Integer)reObj[0]).intValue();
              break;
            case 6:
              if (0 == this.imageBlocks[blocknum - this.blockXcount].getBlockRleType()) {
                imageBlock = new ImageBlock();
                imageBlock.setImage(this.imageBlocks[blocknum - this.blockXcount].getImage());
                imageBlock.setFill(this.imageBlocks[blocknum - this.blockXcount].isFill());
                imageBlock.setCutWidth(this.imageBlocks[blocknum - this.blockXcount].getCutWidth());
                imageBlock.setCutHeight(this.imageBlocks[blocknum - this.blockXcount].getCutHeight());
                imageBlock.setBlockType(this.imageBlocks[blocknum - this.blockXcount].getBlockType());
                imageBlock.setBlockRleType(this.imageBlocks[blocknum - this.blockXcount].getBlockRleType());
                this.imageBlocks[blocknum] = imageBlock;
                syclen = 1;
                break;
              } 
              cols6 = (this.imageBlocks[blocknum - this.blockXcount].getPixColors()).length;
              pixColors = new byte[cols6];
              System.arraycopy(this.imageBlocks[blocknum - this.blockXcount].getPixColors(), 0, pixColors, 0, cols6);
              reObj = decodeRle(zipDatas, i, blocknum, this.imageBlocks[blocknum - this.blockXcount]
                  .getBlockRleType(), pixColors);
              imageBlock = (ImageBlock)reObj[1];
              this.imageBlocks[blocknum] = imageBlock;
              syclen = ((Integer)reObj[0]).intValue();
              break;
            case 7:
              cols7 = this.imageBlocks[blocknum - this.blockXcount].getPixColors();
              pixColors = new byte[cols7.length];
              if (1 == this.imageBlocks[blocknum - this.blockXcount].getBlockRleType()) {
                pixColors[0] = cols7[3];
                pixColors[1] = cols7[4];
                pixColors[2] = cols7[5];
                pixColors[3] = cols7[0];
                pixColors[4] = cols7[1];
                pixColors[5] = cols7[2];
              }
              else {
                System.arraycopy(this.imageBlocks[blocknum - this.blockXcount].getPixColors(), 0, pixColors, 0, cols7.length);
              } 
              reObj = decodeRle(zipDatas, i, blocknum, this.imageBlocks[blocknum - this.blockXcount]
                  .getBlockRleType(), pixColors);
              imageBlock = (ImageBlock)reObj[1];
              this.imageBlocks[blocknum] = imageBlock;
              syclen = ((Integer)reObj[0]).intValue();
              break;
          } 
          throw new LibException("RLE imagedata error");
        case 2:
        case 3:
          len = ((zipDatas[i + 1] & 0xFF) << 8) + (zipDatas[i + 2] & 0xFF);
          synHeadData = JPEGData.createSynHeadData();
          zipData = new byte[len + synHeadData.length + JPEGData.TAIL.length];
          System.arraycopy(synHeadData, 0, zipData, 0, synHeadData.length);
          System.arraycopy(zipDatas, i + 3, zipData, synHeadData.length, len);
          System.arraycopy(JPEGData.TAIL, 0, zipData, synHeadData.length + len, JPEGData.TAIL.length);
          syclen = 3 + len;
          JPEGimage = this.imageCreater.JPEGDecodeAsImage(zipData);
          imageBlock = new ImageBlock();
          imageBlock.setImage(JPEGimage);
          imageBlock.setBlockType(zipType);
          this.imageBlocks[blocknum] = imageBlock;
          break;
        case 4:
          syclen = 1;
          break;
        case 5:
          imageBlock = new ImageBlock();
          imageBlock.setImage(this.imageBlocks[blocknum - this.blockXcount].getImage());
          imageBlock.setFill(this.imageBlocks[blocknum - this.blockXcount].isFill());
          imageBlock.setCutWidth(this.imageBlocks[blocknum - this.blockXcount].getCutWidth());
          imageBlock.setCutHeight(this.imageBlocks[blocknum - this.blockXcount].getCutHeight());
          imageBlock.setBlockType(this.imageBlocks[blocknum - this.blockXcount].getBlockType());
          imageBlock.setBlockRleType(this.imageBlocks[blocknum - this.blockXcount].getBlockRleType());
          this.imageBlocks[blocknum] = imageBlock;
          syclen = 1;
          break;
        case 6:
          imageBlock = new ImageBlock();
          imageBlock.setImage(this.imageBlocks[blocknum - 1].getImage());
          imageBlock.setFill(this.imageBlocks[blocknum - 1].isFill());
          imageBlock.setCutWidth(this.imageBlocks[blocknum - 1].getCutWidth());
          imageBlock.setCutHeight(this.imageBlocks[blocknum - 1].getCutHeight());
          imageBlock.setBlockType(this.imageBlocks[blocknum - 1].getBlockType());
          imageBlock.setBlockRleType(this.imageBlocks[blocknum - 1].getBlockRleType());
          this.imageBlocks[blocknum] = imageBlock;
          syclen = 1;
          break;
        default:
          throw new LibException("imagedata error");
      } 
      if (imageBlock != null) {
        imageBlock.setX(blocknum % this.blockXcount * blockImageWidth);
        imageBlock.setY(blocknum / this.blockXcount * blockImageHeight);
        if (zipType == 1 || zipType == 3) {
          imageBlock.setFill(true);
          imageBlock.setCutWidth(this.blockCutWidth);
          imageBlock.setCutHeight(this.blockCutHeight);
          if (blocknum / this.blockXcount < this.blockYcount - 1) {
            imageBlock.setCutHeight(blockImageHeight);
          }
          else if (blocknum != this.blockcount - 1) {
            imageBlock.setCutWidth(blockImageWidth);
          } 
        } 
      } 
      blocknum++;
      imageBlock = null;
      reObj = null;
    } 
    return this.imageBlocks.clone(); }
  public Object[] decodeRle(byte[] zipDatas, int srcPos, int blocknum, int type, byte[] pixColors) throws LibException {
    int j, change_flag, tmpdata, lastnum;
    boolean isLastCyc;
    int bufColor1, bufColor2, bufColor3;
    byte[] temBlockData;
    int m_temp, m, index, temcolor[];
    byte[] temBlockData1;
    int k, len = 0;
    int syclen = 0;
    int rZipType = 0;
    int subSyclen = 0;
    int indexType = 0;
    int subPixlen = 0;
    int collen = 0;
    int srcColPos = 0;
    byte[] butPixColors = null;
    int coefficient = 1;
    int[] imageData = null;
    ImageBlock imageBlock = null;
    rZipType = type;
    if (pixColors != null) {
      butPixColors = new byte[pixColors.length];
      System.arraycopy(pixColors, 0, butPixColors, 0, pixColors.length);
      srcColPos = 0;
      coefficient = 0;
    }
    else {
      srcColPos = srcPos + 1;
      if (rZipType != 0)
      {
        srcColPos = srcPos + 3;
      }
      collen = (zipDatas.length <= srcColPos + 12) ? (zipDatas.length - srcColPos) : 12;
      butPixColors = new byte[collen];
      System.arraycopy(zipDatas, srcColPos, butPixColors, 0, butPixColors.length);
      srcColPos = 0;
      coefficient = 1;
    } 
    switch (rZipType) {
      case 0:
        imageData = new int[4096];
        for (j = 0; j < imageData.length; j++)
        {
          imageData[j] = 
            ColorConverter.ycbcr2rgb(butPixColors[srcColPos + 0], butPixColors[srcColPos + 1], butPixColors[srcColPos + 2]);
        }
        syclen = 4;
        break;
      case 1:
        change_flag = 0;
        tmpdata = 0;
        lastnum = 0;
        isLastCyc = false;
        bufColor1 = 0;
        bufColor2 = 0;
        bufColor3 = 0;
        bufColor1 = ColorConverter.ycbcr2rgb(butPixColors[srcColPos + 0], butPixColors[srcColPos + 1], butPixColors[srcColPos + 2]);
        bufColor2 = ColorConverter.ycbcr2rgb(butPixColors[srcColPos + 3], butPixColors[srcColPos + 4], butPixColors[srcColPos + 5]);
        bufColor3 = bufColor1;
        subSyclen = 0;
        len = ((zipDatas[srcPos + 1] & 0xFF) << 8) + (zipDatas[srcPos + 2] & 0xFF);
        temBlockData = new byte[len];
        System.arraycopy(zipDatas, srcPos + 1 + 2 + 6 * coefficient, temBlockData, 0, len);
        imageData = new int[4096];
        m_temp = 0;
        for (m = 0; m < temBlockData.length; m = m_temp) {
          m_temp = m;
          if (!isLastCyc && lastnum < 8) {
            tmpdata = tmpdata & 0xFFFF | (temBlockData[m_temp] & 0xFF) << 8 - lastnum;
            lastnum += 8;
            m_temp++;
          } 
          subPixlen = ((tmpdata & 0xFC00) >> 10) + 1;
          tmpdata = tmpdata << 6 & 0xFFFF;
          lastnum -= 6;
          if (subPixlen < 64) {
            change_flag = 0;
          }
          else {
            change_flag = (tmpdata & 0x8000) >> 15;
            tmpdata = tmpdata << 1 & 0xFFFF;
            lastnum--;
          } 
          for (int n = 0; n < subPixlen; n++) {
            if (subSyclen + n >= imageData.length)
            {
              LoggerUtil.error("RLE 2 decode error: ");
            }
            imageData[subSyclen + n] = bufColor3;
          } 
          subSyclen += subPixlen;
          if (change_flag == 0)
          {
            if (bufColor3 == bufColor1) {
              bufColor3 = bufColor2;
            }
            else {
              bufColor3 = bufColor1;
            } 
          }
          if (isLastCyc || m_temp >= len) {
            if (isLastCyc) {
              m_temp++;
              isLastCyc = false;
            } 
            if (tmpdata != 0 || (lastnum != 0 && subSyclen < imageData.length)) {
              isLastCyc = true;
              m_temp--;
            } 
          } 
        } 
        syclen = 3 + 6 * coefficient + len;
        break;
      case 2:
      case 3:
        index = 0;
        temcolor = new int[] { ColorConverter.ycbcr2rgb(butPixColors[srcColPos + 0], butPixColors[srcColPos + 1], butPixColors[srcColPos + 2]), ColorConverter.ycbcr2rgb(butPixColors[srcColPos + 3], butPixColors[srcColPos + 4], butPixColors[srcColPos + 5]), ColorConverter.ycbcr2rgb(butPixColors[srcColPos + 6], butPixColors[srcColPos + 7], butPixColors[srcColPos + 8]), ColorConverter.ycbcr2rgb(butPixColors[srcColPos + 9], butPixColors[srcColPos + 10], butPixColors[srcColPos + 11]) };
        subSyclen = 0;
        indexType = 4;
        if (rZipType == 2)
        {
          indexType = 3;
        }
        len = ((zipDatas[srcPos + 1] & 0xFF) << 8) + (zipDatas[srcPos + 2] & 0xFF);
        temBlockData1 = new byte[len];
        System.arraycopy(zipDatas, srcPos + 1 + 2 + indexType * 3 * coefficient, temBlockData1, 0, len);
        imageData = new int[4096];
        for (k = 0; k < temBlockData1.length; k++) {
          subPixlen = ((temBlockData1[k] & 0xFC) >> 2) + 1;
          index = temBlockData1[k] & 0x3;
          for (int n = 0; n < subPixlen; n++)
          {
            imageData[subSyclen + n] = temcolor[index];
          }
          subSyclen += subPixlen;
        } 
        syclen = 3 + indexType * 3 * coefficient + len;
        break;
      default:
        throw new LibException("RLE imagedata error");
    } 
    Image imageRLE = this.imageCreater.createRLEImg(imageData, blockImageWidth, blockImageHeight);
    imageBlock = new ImageBlock();
    imageBlock.setBlockRleType(rZipType);
    imageBlock.setBlockType(0);
    imageBlock.setPixColors(butPixColors);
    imageBlock.setImage(imageRLE);
    int len1 = (zipDatas.length - srcPos < imageBlock.zipDataBak.length) ? (zipDatas.length - srcPos) : imageBlock.zipDataBak.length;
    for (int i = 0; i < len1; i++)
    {
      imageBlock.zipDataBak[i] = zipDatas[srcPos + i] & 0xFF;
    }
    imageBlock.zipDataBak[7] = srcPos;
    imageBlock.zipDataBak[8] = syclen;
    imageBlock.zipDataBak[9] = len;
    Object[] reObj = { Integer.valueOf(syclen), imageBlock };
    return reObj;
  }
  public Object[] decodeRle_0(byte[] zipDatas, int srcPos, int blocknum, int type, byte[] pixColors) throws LibException {
    byte bgr233;
    int j, change_flag, tmpdata, lastnum;
    boolean isLastCyc;
    byte bufColor1, bufColor2, bufColor3, temBlockData[];
    int m_temp, m, index;
    byte[] temcolor1, temBlockData1;
    int k, len = 0;
    int syclen = 0;
    int rZipType = 0;
    int subSyclen = 0;
    int indexType = 0;
    int subPixlen = 0;
    byte[] butPixColors = null;
    int srcColPos = 0;
    int collen = 0;
    int coefficient = 1;
    byte[] imageData = null;
    ImageBlock imageBlock = null;
    rZipType = type;
    if (pixColors != null) {
      butPixColors = new byte[pixColors.length];
      System.arraycopy(pixColors, 0, butPixColors, 0, pixColors.length);
      srcColPos = 0;
      coefficient = 0;
    }
    else {
      srcColPos = srcPos + 1;
      if (rZipType != 0)
      {
        srcColPos = srcPos + 3;
      }
      collen = (zipDatas.length <= srcColPos + 12) ? (zipDatas.length - srcColPos) : 12;
      butPixColors = new byte[collen];
      System.arraycopy(zipDatas, srcColPos, butPixColors, 0, butPixColors.length);
      srcColPos = 0;
      coefficient = 1;
    } 
    switch (rZipType) {
      case 0:
        bgr233 = ColorConverter.ycbcr2rgb332(butPixColors[srcColPos + 0], butPixColors[srcColPos + 1], butPixColors[srcColPos + 2]);
        imageData = new byte[4096];
        for (j = 0; j < imageData.length; j++)
        {
          imageData[j] = bgr233;
        }
        syclen = 4;
        break;
      case 1:
        change_flag = 0;
        tmpdata = 0;
        lastnum = 0;
        isLastCyc = false;
        bufColor1 = 0;
        bufColor2 = 0;
        bufColor3 = 0;
        bufColor1 = ColorConverter.ycbcr2rgb332(butPixColors[srcColPos + 0], butPixColors[srcColPos + 1], butPixColors[srcColPos + 2]);
        bufColor2 = ColorConverter.ycbcr2rgb332(butPixColors[srcColPos + 3], butPixColors[srcColPos + 4], butPixColors[srcColPos + 5]);
        bufColor3 = bufColor1;
        subSyclen = 0;
        len = ((zipDatas[srcPos + 1] & 0xFF) << 8) + (zipDatas[srcPos + 2] & 0xFF);
        temBlockData = new byte[len];
        System.arraycopy(zipDatas, srcPos + 1 + 2 + 6 * coefficient, temBlockData, 0, len);
        imageData = new byte[4096];
        m_temp = 0;
        for (m = 0; m < temBlockData.length; m = m_temp) {
          m_temp = m;
          if (!isLastCyc && lastnum <= 8) {
            tmpdata = tmpdata & 0xFFFF | (temBlockData[m_temp] & 0xFF) << 8 - lastnum;
            lastnum += 8;
            m_temp++;
          } 
          subPixlen = ((tmpdata & 0xFC00) >> 10) + 1;
          tmpdata = tmpdata << 6 & 0xFFFF;
          lastnum -= 6;
          if (subPixlen < 64) {
            change_flag = 0;
          }
          else {
            change_flag = (tmpdata & 0x8000) >> 15;
            tmpdata = tmpdata << 1 & 0xFFFF;
            lastnum--;
          } 
          for (int n = 0; n < subPixlen; n++) {
            if (subSyclen + n >= imageData.length)
            {
              LoggerUtil.error("RLE 2 decode error");
            }
            imageData[subSyclen + n] = bufColor3;
          } 
          subSyclen += subPixlen;
          if (change_flag == 0)
          {
            if (bufColor3 == bufColor1) {
              bufColor3 = bufColor2;
            }
            else {
              bufColor3 = bufColor1;
            } 
          }
          if (isLastCyc || len <= m_temp) {
            if (isLastCyc) {
              isLastCyc = false;
              m_temp++;
            } 
            if (tmpdata != 0) {
              isLastCyc = true;
              m_temp--;
            } 
          } 
        } 
        syclen = 3 + 6 * coefficient + len;
        break;
      case 2:
      case 3:
        index = 0;
        temcolor1 = new byte[] { ColorConverter.ycbcr2rgb332(butPixColors[srcColPos + 0], butPixColors[srcColPos + 1], butPixColors[srcColPos + 2]), ColorConverter.ycbcr2rgb332(butPixColors[srcColPos + 3], butPixColors[srcColPos + 4], butPixColors[srcColPos + 5]), ColorConverter.ycbcr2rgb332(butPixColors[srcColPos + 6], butPixColors[srcColPos + 7], butPixColors[srcColPos + 8]), ColorConverter.ycbcr2rgb332(butPixColors[srcColPos + 9], butPixColors[srcColPos + 10], butPixColors[srcColPos + 11]) };
        subSyclen = 0;
        indexType = 4;
        if (rZipType == 2)
        {
          indexType = 3;
        }
        len = ((zipDatas[srcPos + 1] & 0xFF) << 8) + (zipDatas[srcPos + 2] & 0xFF);
        temBlockData1 = new byte[len];
        System.arraycopy(zipDatas, srcPos + 1 + 2 + indexType * 3 * coefficient, temBlockData1, 0, len);
        imageData = new byte[4096];
        for (k = 0; k < temBlockData1.length; k++) {
          subPixlen = ((temBlockData1[k] & 0xFC) >> 2) + 1;
          index = temBlockData1[k] & 0x3;
          for (int n = 0; n < subPixlen; n++)
          {
            imageData[subSyclen + n] = temcolor1[index];
          }
          subSyclen += subPixlen;
        } 
        syclen = 3 + indexType * 3 * coefficient + len;
        break;
      default:
        throw new LibException("RLE imagedata error");
    } 
    Image imageRLE = this.imageCreater.createRLEImg_0(imageData, blockImageWidth, blockImageHeight);
    imageBlock = new ImageBlock();
    imageBlock.setBlockRleType(rZipType);
    imageBlock.setBlockType(0);
    imageBlock.setPixColors(butPixColors);
    imageBlock.setImage(imageRLE);
    int len1 = (zipDatas.length - srcPos < imageBlock.zipDataBak.length) ? (zipDatas.length - srcPos) : imageBlock.zipDataBak.length;
    for (int i = 0; i < len1; i++)
    {
      imageBlock.zipDataBak[i] = zipDatas[srcPos + i] & 0xFF;
    }
    imageBlock.zipDataBak[7] = srcPos;
    imageBlock.zipDataBak[8] = syclen;
    imageBlock.zipDataBak[9] = len;
    Object[] reObj = { Integer.valueOf(syclen), imageBlock };
    return reObj;
  }
  public byte[] decodeOldRLE(byte[] bytes, int imageWidth, int imageHeight) throws LibException {
    byte[] imageData = null;
    int pixNumber = imageWidth * imageHeight;
    imageData = new byte[pixNumber];
    int countNum = 0;
    int temLength = 0;
    int bytesLenght = bytes.length;
    boolean flagRem = false;
    byte bufColor = 0;
    int i;
    for (i = 1; i < bytesLenght; i += temLength) {
      if (flagRem) {
        if (i + 1 >= bytesLenght) {
          byte[] tmp = null;
          return tmp;
        } 
        bufColor = (byte)(bytes[i] | bytes[i + 1] >>> 4 & 0xF);
        byte tem = (byte)(bytes[i + 1] & 0xF);
        if (0 != tem) {
          int temInt = tem;
          if (temInt + countNum > pixNumber)
          {
            temInt = pixNumber - countNum;
          }
          int jend = temInt + countNum;
          for (int j = countNum; j < jend; j++)
          {
            imageData[j] = bufColor;
          }
          countNum = jend;
          if (countNum == pixNumber)
          {
            return imageData;
          }
          temLength = 2;
          if (bytesLenght == i + 3)
          {
            temLength = 3;
          }
          flagRem = false;
        } else {
          int extendNum6; int size6; int j; int extendNum10; int size10; int k; int extendNum18; int size18; int m; int extendNum22; int size22;
          int n;
          if (i + 2 >= bytesLenght) {
            byte[] tmp = null;
            return tmp;
          } 
          switch (bytes[i + 2] & 0xC0) {
            case 0:
              if (i + 2 >= bytesLenght) {
                byte[] tmp = null;
                return tmp;
              } 
              extendNum6 = bytes[i + 2] & 0x3F;
              size6 = countNum + extendNum6;
              size6 = (size6 > pixNumber) ? pixNumber : size6;
              for (j = countNum; j < size6; j++)
              {
                imageData[j] = bufColor;
              }
              countNum = size6;
              if (countNum == pixNumber)
              {
                return imageData;
              }
              temLength = 3;
              if (bytesLenght == i + 4)
              {
                temLength = 4;
              }
              flagRem = false;
              break;
            case 64:
              if (i + 3 >= bytesLenght) {
                byte[] tmp = null;
                return tmp;
              } 
              extendNum10 = (bytes[i + 2] << 4 & 0x3F0) + (bytes[i + 3] >>> 4 & 0xF);
              size10 = countNum + extendNum10;
              size10 = (size10 > pixNumber) ? pixNumber : size10;
              for (k = countNum; k < size10; k++)
              {
                imageData[k] = bufColor;
              }
              countNum = size10;
              if (countNum == pixNumber)
              {
                return imageData;
              }
              temLength = 3;
              if (bytesLenght == i + 4) {
                temLength = 4;
                break;
              } 
              if (i + 3 >= bytesLenght) {
                byte[] tmp = null;
                return tmp;
              } 
              bytes[i + 3] = (byte)(bytes[i + 3] << 4);
              flagRem = true;
              break;
            case 128:
              if (i + 4 >= bytesLenght) {
                byte[] tmp = null;
                return tmp;
              } 
              extendNum18 = (bytes[i + 2] << 12 & 0x3F000) + (bytes[i + 3] << 4 & 0xFF0) + (bytes[i + 4] >>> 4 & 0xF);
              size18 = countNum + extendNum18;
              size18 = (size18 > pixNumber) ? pixNumber : size18;
              for (m = countNum; m < size18; m++)
              {
                imageData[m] = bufColor;
              }
              countNum = size18;
              if (countNum == pixNumber)
              {
                return imageData;
              }
              temLength = 4;
              if (bytesLenght == i + 5) {
                temLength = 5;
                break;
              } 
              if (bytesLenght <= i + 4) {
                byte[] tmp = null;
                return tmp;
              } 
              bytes[i + 4] = (byte)(bytes[i + 4] << 4);
              flagRem = true;
              break;
            case 192:
              if (bytesLenght <= i + 4) {
                byte[] tmp = null;
                return tmp;
              } 
              extendNum22 = (bytes[i + 2] << 16 & 0x3F0000) + (bytes[i + 3] << 8 & 0xFF00) + (bytes[i + 4] & 0xFF);
              size22 = extendNum22 + countNum;
              size22 = (size22 > pixNumber) ? pixNumber : size22;
              for (n = countNum; n < size22; n++)
              {
                imageData[n] = bufColor;
              }
              countNum = size22;
              if (pixNumber == countNum)
              {
                return imageData;
              }
              temLength = 5;
              if (bytesLenght == i + 6)
              {
                temLength = 6;
              }
              flagRem = false;
              break;
            default:
              throw new LibException("imagedata error");
          } 
        } 
      } else {
        byte tem = 0;
        bufColor = bytes[i];
        try {
          if (bytesLenght <= i + 1) {
            byte[] tmp = null;
            return tmp;
          } 
          tem = (byte)(bytes[i + 1] >>> 4 & 0xF);
        }
        catch (ArrayIndexOutOfBoundsException ex) {
          LoggerUtil.error("i = " + i);
        } 
        if (0 != tem) {
          int temInt = tem;
          if (temInt + countNum > pixNumber)
          {
            temInt = pixNumber - countNum;
          }
          int jend = countNum + temInt;
          for (int j = countNum; j < jend; j++)
          {
            imageData[j] = bufColor;
          }
          countNum = jend;
          if (countNum == pixNumber)
          {
            return imageData;
          }
          temLength = 1;
          if (bytesLenght == i + 2) {
            temLength = 2;
          }
          else {
            if (bytesLenght <= i + 1) {
              byte[] tmp = null;
              return tmp;
            } 
            bytes[i + 1] = (byte)(bytes[i + 1] << 4);
            flagRem = true;
          } 
        } else {
          int extendNum6; int size6; int j; int extendNum10; int size10; int k; int extendNum18; int size18; int m; int extendNum22; int size22;
          int n;
          if (bytesLenght <= i + 1) {
            byte[] tmp = null;
            return tmp;
          } 
          switch (bytes[i + 1] & 0xC) {
            case 0:
              if (bytesLenght <= i + 2) {
                byte[] tmp = null;
                return tmp;
              } 
              extendNum6 = (bytes[i + 1] << 4 & 0x30) + (bytes[i + 2] >>> 4 & 0xF);
              size6 = extendNum6 + countNum;
              size6 = (size6 > pixNumber) ? pixNumber : size6;
              for (j = countNum; j < size6; j++)
              {
                imageData[j] = bufColor;
              }
              countNum = size6;
              if (countNum == pixNumber)
              {
                return imageData;
              }
              temLength = 2;
              if (bytesLenght == i + 3) {
                temLength = 3;
                break;
              } 
              if (i + 2 >= bytesLenght) {
                byte[] tmp = null;
                return tmp;
              } 
              bytes[i + 2] = (byte)(bytes[i + 2] << 4);
              flagRem = true;
              break;
            case 4:
              if (i + 2 >= bytesLenght) {
                byte[] tmp = null;
                return tmp;
              } 
              extendNum10 = (bytes[i + 1] << 8 & 0x300) + (bytes[i + 2] & 0xFF);
              size10 = countNum + extendNum10;
              size10 = (size10 > pixNumber) ? pixNumber : size10;
              for (k = countNum; k < size10; k++)
              {
                imageData[k] = bufColor;
              }
              countNum = size10;
              if (countNum == pixNumber)
              {
                return imageData;
              }
              temLength = 3;
              if (bytesLenght == i + 4)
              {
                temLength = 4;
              }
              flagRem = false;
              break;
            case 8:
              if (i + 3 >= bytesLenght) {
                byte[] tmp = null;
                return tmp;
              } 
              extendNum18 = (bytes[i + 1] << 16 & 0x30000) + (bytes[i + 2] << 8 & 0xFF00) + (bytes[i + 3] & 0xFF);
              size18 = countNum + extendNum18;
              size18 = (size18 > pixNumber) ? pixNumber : size18;
              for (m = countNum; m < size18; m++)
              {
                imageData[m] = bufColor;
              }
              countNum = size18;
              if (countNum == pixNumber)
              {
                return imageData;
              }
              temLength = 4;
              if (bytesLenght == i + 5)
              {
                temLength = 5;
              }
              flagRem = false;
              break;
            case 12:
              if (i + 4 >= bytesLenght) {
                byte[] tmp = null;
                return tmp;
              } 
              extendNum22 = (bytes[i + 1] << 20 & 0x300000) + (bytes[i + 2] << 12 & 0xFF000) + (bytes[i + 3] << 4 & 0xFF0) + (bytes[i + 4] >>> 4 & 0xF);
              size22 = countNum + extendNum22;
              size22 = (size22 > pixNumber) ? pixNumber : size22;
              for (n = countNum; n < size22; n++)
              {
                imageData[n] = bufColor;
              }
              countNum = size22;
              if (countNum == pixNumber)
              {
                return imageData;
              }
              temLength = 4;
              if (bytesLenght == i + 5) {
                temLength = 5;
                break;
              } 
              if (i + 4 >= bytesLenght) {
                byte[] tmp = null;
                return tmp;
              } 
              bytes[i + 4] = (byte)(bytes[i + 4] << 4);
              flagRem = true;
              break;
            default:
              throw new LibException("imagedata error");
          } 
        } 
      } 
    } 
    return imageData;
  }
}
