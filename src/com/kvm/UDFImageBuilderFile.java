package com.kvm;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
public class UDFImageBuilderFile
{
  private String identifier = null;
  private File sourceFile = null;
  private ArrayList<UDFImageBuilderFile> childs;
  private FileType fileType;
  private Calendar accessTime;
  private Calendar attributeTime;
  private Calendar creationTime;
  private Calendar modificationTime;
  private int fileLinkCount = 1;
  private UDFImageBuilderFile parent;
  private FileEntryPosition fileEntryPosition;
  private byte[] sourceFileArray;
  public byte[] getSourceFileArray() {
    return (byte[])this.sourceFileArray.clone();
  }
  public void setSourceFileArray(byte[] sourceFileArray) {
    if (sourceFileArray != null) {
      this.sourceFileArray = (byte[])sourceFileArray.clone();
    }
    else {
      this.sourceFileArray = null;
    } 
  }
  public UDFImageBuilderFile(File sourceFile) {
    this.childs = new ArrayList<>();
    this.identifier = sourceFile.getName();
    this.sourceFile = sourceFile;
    this.accessTime = Calendar.getInstance();
    this.attributeTime = Calendar.getInstance();
    this.attributeTime.setTimeInMillis(sourceFile.lastModified());
    this.creationTime = Calendar.getInstance();
    this.creationTime.setTimeInMillis(sourceFile.lastModified());
    this.modificationTime = Calendar.getInstance();
    this.modificationTime.setTimeInMillis(sourceFile.lastModified());
    if (sourceFile.isDirectory()) {
      this.fileType = FileType.Directory;
      File[] childFiles = sourceFile.listFiles();
      if (childFiles != null) {
        for (int i = 0; i < childFiles.length; i++)
        {
          addChild(childFiles[i]);
        }
      }
    }
    else {
      this.fileType = FileType.File;
    } 
  }
  public UDFImageBuilderFile(String directoryIdentifier) {
    this.childs = new ArrayList<>();
    this.accessTime = Calendar.getInstance();
    this.attributeTime = Calendar.getInstance();
    this.creationTime = Calendar.getInstance();
    this.modificationTime = Calendar.getInstance();
    this.identifier = directoryIdentifier;
    this.fileType = FileType.Directory;
    this.fileLinkCount = 1;
    this.parent = null;
    this.sourceFile = null;
  }
  public FileEntryPosition getFileEntryPosition() {
    return this.fileEntryPosition;
  }
  public void setFileEntryPosition(FileEntryPosition fileEntryPosition) {
    this.fileEntryPosition = fileEntryPosition;
  }
  public String getIdentifier() {
    return this.identifier;
  }
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
  public File getSourceFile() {
    return this.sourceFile;
  }
  public void setSourceFile(File sourceFile) {
    this.sourceFile = sourceFile;
  }
  public ArrayList<UDFImageBuilderFile> getChilds() {
    return this.childs;
  }
  public void setChilds(ArrayList<UDFImageBuilderFile> childs) {
    this.childs = childs;
  }
  public FileType getFileType() {
    return this.fileType;
  }
  public void setFileType(FileType fileType) {
    this.fileType = fileType;
  }
  public Calendar getAccessTime() {
    return this.accessTime;
  }
  public void setAccessTime(Calendar accessTime) {
    this.accessTime = accessTime;
  }
  public Calendar getAttributeTime() {
    return this.attributeTime;
  }
  public void setAttributeTime(Calendar attributeTime) {
    this.attributeTime = attributeTime;
  }
  public Calendar getCreationTime() {
    return this.creationTime;
  }
  public void setCreationTime(Calendar creationTime) {
    this.creationTime = creationTime;
  }
  public Calendar getModificationTime() {
    return this.modificationTime;
  }
  public void setModificationTime(Calendar modificationTime) {
    this.modificationTime = modificationTime;
  }
  public int getFileLinkCount() {
    return this.fileLinkCount;
  }
  public void setFileLinkCount(int fileLinkCount) {
    this.fileLinkCount = fileLinkCount;
  }
  public UDFImageBuilderFile getParent() {
    return this.parent;
  }
  public void setParent(UDFImageBuilderFile parent) {
    this.parent = parent;
  }
  public long getFileCount() {
    long fileCount = 0L;
    if (this.fileType == FileType.Directory) {
      for (int i = 0; i < this.childs.size(); i++) {
        UDFImageBuilderFile childUDFImageBuilderFile = this.childs.get(i);
        if (childUDFImageBuilderFile.fileType == FileType.Directory) {
          fileCount += childUDFImageBuilderFile.getFileCount();
        } else if (childUDFImageBuilderFile.fileType == FileType.File) {
          fileCount++;
        }
      } 
    } else if (this.fileType == FileType.File) {
      fileCount = 1L;
    } 
    return fileCount;
  }
  public long getDirectoryCount() {
    long dirCount = 0L;
    if (this.fileType == FileType.Directory) {
      dirCount++;
      for (int i = 0; i < this.childs.size(); i++) {
        UDFImageBuilderFile childUDFImageBuilderFile = this.childs.get(i);
        if (childUDFImageBuilderFile.fileType == FileType.Directory) {
          dirCount += childUDFImageBuilderFile.getDirectoryCount();
        }
      } 
    } 
    return dirCount;
  }
  public long getFileLength() {
    if (this.sourceFile != null) {
      return this.sourceFile.length();
    }
    return 0L;
  }
  public final void addChild(File childFile) {
    UDFImageBuilderFile childUDFImageBuilderFile = new UDFImageBuilderFile(childFile);
    if (childUDFImageBuilderFile.getFileType() == FileType.Directory)
    {
      this.fileLinkCount++;
    }
    childUDFImageBuilderFile.setParent(this);
    this.childs.add(childUDFImageBuilderFile);
  }
  public UDFImageBuilderFile getChild(String identifier) {
    for (int i = 0; i < this.childs.size(); i++) {
      UDFImageBuilderFile myUDFImageBuilderFile = this.childs.get(i);
      if (myUDFImageBuilderFile.identifier.equals(identifier))
      {
        return myUDFImageBuilderFile;
      }
    } 
    return null;
  }
}
