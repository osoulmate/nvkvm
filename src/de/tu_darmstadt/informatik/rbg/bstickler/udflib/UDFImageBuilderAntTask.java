package de.tu_darmstadt.informatik.rbg.bstickler.udflib;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
public class UDFImageBuilderAntTask
  extends Task
{
  private String imageIdentifier;
  private String imageOutputFile;
  public static class FileLocation
  {
    private String location;
    private boolean childfilesonly = false;
    public void setLocation(String location) {
      this.location = location;
    }
    public void setChildfilesonly(boolean childfilesonly) {
      this.childfilesonly = childfilesonly;
    }
  }
  private String udfRevision = "2.60";
  private List<FileLocation> fileLocations = new ArrayList<FileLocation>();
  public void setImageIdentifier(String imageIdentifier) {
    this.imageIdentifier = imageIdentifier;
  }
  public void setImageOutputFile(String imageOutputFile) {
    this.imageOutputFile = imageOutputFile;
  }
  public void setUdfRevision(String udfRevision) {
    this.udfRevision = udfRevision;
  }
  public void addFileLocation(FileLocation myFileLocation) {
    this.fileLocations.add(myFileLocation);
  }
  public void execute() throws BuildException {
    try {
      log("Creating UDF image " + this.imageOutputFile);
      UDFImageBuilder myUDFImageBuilder = new UDFImageBuilder();
      myUDFImageBuilder.setImageIdentifier(this.imageIdentifier);
      for (int i = 0; i < this.fileLocations.size(); i++) {
        FileLocation myFileLocation = this.fileLocations.get(i);
        File myFile = new File(myFileLocation.location);
        if (myFileLocation.childfilesonly && myFile.isDirectory()) {
          File[] childFiles = myFile.listFiles();
          for (int j = 0; j < childFiles.length; j++) {
            myUDFImageBuilder.addFileToRootDirectory(childFiles[j]);
          }
        } else {
          myUDFImageBuilder.addFileToRootDirectory(myFile);
        } 
      } 
      UDFRevision myUDFRevision = UDFRevision.Revision201;
      if (this.udfRevision.equals("1.02")) {
        myUDFRevision = UDFRevision.Revision102;
      } else if (this.udfRevision.equals("2.01")) {
        myUDFRevision = UDFRevision.Revision201;
      } else if (this.udfRevision.equals("2.60")) {
        myUDFRevision = UDFRevision.Revision260;
      } else {
        throw new BuildException("Unkown UDF-Revision [" + this.udfRevision + "]");
      } 
      myUDFImageBuilder.writeImage(this.imageOutputFile, myUDFRevision);
    }
    catch (Exception ex) {
      throw new BuildException(ex.toString());
    } 
  }
}
