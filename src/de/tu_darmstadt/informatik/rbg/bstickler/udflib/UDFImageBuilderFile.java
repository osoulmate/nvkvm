package de.tu_darmstadt.informatik.rbg.bstickler.udflib;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
public class UDFImageBuilderFile
  implements Comparable<UDFImageBuilderFile>
{
  private String identifier = null;
  private File sourceFile = null;
  private ArrayList<UDFImageBuilderFile> childs;
  private FileType fileType;
  private Calendar AccessTime;
  private Calendar AttributeTime;
  private Calendar CreationTime;
  private Calendar ModificationTime;
  private int FileLinkCount = 1;
  private UDFImageBuilderFile parent;
  public enum FileType
  {
    File,
    Directory;
  }
  public UDFImageBuilderFile(File sourceFile) throws Exception {
    this.childs = new ArrayList<UDFImageBuilderFile>();
    this.identifier = sourceFile.getName();
    this.sourceFile = sourceFile;
    this.AccessTime = Calendar.getInstance();
    this.AttributeTime = Calendar.getInstance();
    this.AttributeTime.setTimeInMillis(sourceFile.lastModified());
    this.CreationTime = Calendar.getInstance();
    this.CreationTime.setTimeInMillis(sourceFile.lastModified());
    this.ModificationTime = Calendar.getInstance();
    this.ModificationTime.setTimeInMillis(sourceFile.lastModified());
    if (sourceFile.isDirectory()) {
      this.fileType = FileType.Directory;
      File[] childFiles = sourceFile.listFiles();
      if (childFiles != null && childFiles.length != 0)
      {
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
    this.childs = new ArrayList<UDFImageBuilderFile>();
    this.AccessTime = Calendar.getInstance();
    this.AttributeTime = Calendar.getInstance();
    this.CreationTime = Calendar.getInstance();
    this.ModificationTime = Calendar.getInstance();
    this.identifier = directoryIdentifier;
    this.fileType = FileType.Directory;
  }
  public FileType getFileType() {
    return this.fileType;
  }
  public void addChild(UDFImageBuilderFile childUDFImageBuilderFile) throws Exception {
    if (this.fileType != FileType.Directory)
    {
      throw new Exception("error: trying to add child file to non-directory file");
    }
    if (getChild(childUDFImageBuilderFile.identifier) != null)
    {
      throw new Exception("error: trying to add child file with an already existing identifer");
    }
    if (childUDFImageBuilderFile.getFileType() == FileType.Directory)
    {
      this.FileLinkCount++;
    }
    childUDFImageBuilderFile.setParent(this);
    this.childs.add(childUDFImageBuilderFile);
  }
  public void addChild(File childFile) throws Exception {
    if (this.fileType != FileType.Directory)
    {
      throw new Exception("error: trying to add child file to non-directory file");
    }
    if (getChild(childFile.getName()) != null)
    {
      throw new Exception("error: trying to add child file with an already existing identifer");
    }
    UDFImageBuilderFile childUDFImageBuilderFile = new UDFImageBuilderFile(childFile);
    if (childUDFImageBuilderFile.getFileType() == FileType.Directory)
    {
      this.FileLinkCount++;
    }
    childUDFImageBuilderFile.setParent(this);
    this.childs.add(childUDFImageBuilderFile);
  }
  public String getIdentifier() {
    return this.identifier;
  }
  public int getFileLinkCount() {
    return this.FileLinkCount;
  }
  public int compareTo(UDFImageBuilderFile myUDFImageBuilderFile) {
    return this.identifier.compareTo(myUDFImageBuilderFile.identifier);
  }
  public void removeChild(String identifier) {
    for (int i = 0; i < this.childs.size(); i++) {
      UDFImageBuilderFile myUDFImageBuilderFile = this.childs.get(i);
      if (myUDFImageBuilderFile.identifier == identifier) {
        this.childs.remove(i);
        break;
      } 
    } 
  }
  public UDFImageBuilderFile getChild(String identifier) {
    for (int i = 0; i < this.childs.size(); i++) {
      UDFImageBuilderFile myUDFImageBuilderFile = this.childs.get(i);
      if (myUDFImageBuilderFile.identifier == identifier)
      {
        return myUDFImageBuilderFile;
      }
    } 
    return null;
  }
  public UDFImageBuilderFile[] getChilds() {
    Collections.sort(this.childs);
    return this.childs.<UDFImageBuilderFile>toArray(new UDFImageBuilderFile[this.childs.size()]);
  }
  public Calendar getModificationTime() {
    return this.ModificationTime;
  }
  public Calendar getAccessTime() {
    return this.AccessTime;
  }
  public Calendar getCreationTime() {
    return this.CreationTime;
  }
  public Calendar getAttributeTime() {
    return this.AttributeTime;
  }
  public long getFileLength() {
    if (this.sourceFile != null)
    {
      return this.sourceFile.length();
    }
    return 0L;
  }
  public void readFileData(byte[] buffer) throws IOException {
    if (this.sourceFile != null) {
      RandomAccessFile sourceRandomAccessFile = new RandomAccessFile(this.sourceFile, "r");
      sourceRandomAccessFile.read(buffer);
      sourceRandomAccessFile.close();
    } 
  }
  public long getDirectoryCount() {
    long directoryCount = 0L;
    if (this.fileType == FileType.Directory) {
      directoryCount++;
      for (int i = 0; i < this.childs.size(); i++) {
        UDFImageBuilderFile childUDFImageBuilderFile = this.childs.get(i);
        if (childUDFImageBuilderFile.getFileType() == FileType.Directory)
        {
          directoryCount += childUDFImageBuilderFile.getDirectoryCount();
        }
      } 
    } 
    return directoryCount;
  }
  public long getFileCount() {
    long fileCount = 0L;
    if (this.fileType == FileType.Directory) {
      for (int i = 0; i < this.childs.size(); i++) {
        UDFImageBuilderFile childUDFImageBuilderFile = this.childs.get(i);
        if (childUDFImageBuilderFile.getFileType() == FileType.Directory)
        {
          fileCount += childUDFImageBuilderFile.getFileCount();
        }
        else if (childUDFImageBuilderFile.getFileType() == FileType.File)
        {
          fileCount++;
        }
      } 
    } else if (this.fileType == FileType.File) {
      fileCount = 1L;
    } 
    return fileCount;
  }
  public File getSourceFile() {
    return this.sourceFile;
  }
  public UDFImageBuilderFile getParent() {
    return this.parent;
  }
  public void setParent(UDFImageBuilderFile parent) {
    this.parent = parent;
  }
}
