package de.tu_darmstadt.informatik.rbg.bstickler.udflib;
import java.io.File;
import java.util.Calendar;
public class UDFTest
{
  private void testUDFImageBuilder() {
    try {
      UDFImageBuilder myUDFImageBuilder = new UDFImageBuilder();
      File testFile = new File("C:\\Program Files (x86)\\Microsoft Visual Studio 8");
      File[] testChildFiles = testFile.listFiles();
      for (int i = 0; i < testChildFiles.length; i++)
      {
        myUDFImageBuilder.addFileToRootDirectory(testChildFiles[i]);
      }
      myUDFImageBuilder.setImageIdentifier("Test-Disc");
      myUDFImageBuilder.writeImage("c:\\temp\\test-disc.iso", UDFRevision.Revision102);
    }
    catch (Exception myException) {
      System.out.println(myException.toString());
      myException.printStackTrace();
    } 
  }
  public void testSabreUDFImageBuilder() {
    try {
      SabreUDFImageBuilder mySabreUDF = new SabreUDFImageBuilder();
      File testFile = new File("C:\\Program Files (x86)\\Microsoft Visual Studio 8");
      File[] testChildFiles = testFile.listFiles();
      for (int i = 0; i < testChildFiles.length; i++)
      {
        mySabreUDF.addFileToRootDirectory(testChildFiles[i]);
      }
      mySabreUDF.setImageIdentifier("Test-Disc");
      mySabreUDF.writeImage("c:\\temp\\test-disc.iso", UDFRevision.Revision102);
    }
    catch (Exception myException) {
      System.out.println(myException.toString());
      myException.printStackTrace();
    } 
  }
  public UDFTest() {
    System.out.println("UDFTest\n");
    long startTime = Calendar.getInstance().getTimeInMillis();
    testSabreUDFImageBuilder();
    System.out.println("Run-Time: " + (Calendar.getInstance().getTimeInMillis() - startTime) + " Milliseconds");
  }
  public static void main(String[] args) {}
}
