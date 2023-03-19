package cn.nvkvm;
import cn.library.LoggerUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
class SendIPMIInDiffOS
{
  //public static final String WINDOWS_IPMITOOL_PATH = "ipmitool/ipmitool.exe";
  public static final String WINDOWS_IPMITOOL_PATH = System.getProperty("user.home")+File.separator+"AppData"+File.separator+"local"+File.separator+"ipmitool"+File.separator;
  public static final String LINUX_IPMITOOL_PATH = "ipmitool";
  public void copyExeToJarDir() throws IOException {
	  copyFile(WINDOWS_IPMITOOL_PATH,"cygcrypto-1.0.0.dll");
	  copyFile(WINDOWS_IPMITOOL_PATH,"cyggcc_s-1.dll");
	  copyFile(WINDOWS_IPMITOOL_PATH,"cygwin1.dll");
	  copyFile(WINDOWS_IPMITOOL_PATH,"cygz.dll");
	  copyFile(WINDOWS_IPMITOOL_PATH,"ipmitool.exe");
  }
  private boolean copyFile(String folderName, String fileName) {
	// TODO Auto-generated method stub
	  File libFolder = new File(folderName);
	  if(!libFolder.exists()) {
		  libFolder.mkdir();
	  }
	  File libFile = new File(libFolder,fileName);
	  if(!libFile.exists()) {
		  try {
              InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("ipmitool/"+fileName);
              FileOutputStream fos = new FileOutputStream(libFile);
              byte[] buf = new byte[1024];
              int c;
              while ((c = inputStream.read(buf)) != -1) {
                  fos.write(buf, 0, c);
              }
              inputStream.close();
              fos.close();
		  }catch(Exception e) {
			  e.printStackTrace();
			  return false;
		  }
	  }
	  return true;
	
}
public Process useIpmitoolInDiffOS(String ipmiOEM) {
    String command = "";
    String ipmitoolPath = "";
    String absolutePath = "";
    Process process = null;
    if (KVMUtil.isWindowsOS()) {
      try {
		copyExeToJarDir();
      } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
      }
      ipmitoolPath = WINDOWS_IPMITOOL_PATH+"/ipmitool.exe";
      //File file = new File(ipmitoolPath);
      //absolutePath = file.getAbsolutePath();
      command = ipmitoolPath + " " + ipmiOEM;
      try {
        process = Runtime.getRuntime().exec(command);
        process.waitFor();
      } catch (Exception e) {
        LoggerUtil.error(e.getClass().getName());
      }
    } else if (KVMUtil.isLinux()) {
      absolutePath = "ipmitool";
      command = absolutePath + " " + ipmiOEM;
      String[] cmds = { "/bin/bash", "-c", command };
      try {
        process = Runtime.getRuntime().exec(cmds);
        process.waitFor();
      } catch (Exception e) {
        LoggerUtil.error(e.getClass().getName());
      }
    } else if (KVMUtil.isMacOS()) {
      absolutePath = "ipmitool";
      command = absolutePath + " " + ipmiOEM;
      String[] cmds = { "/bin/bash", "-c", command };
      try {
        process = Runtime.getRuntime().exec(cmds);
        process.waitFor();
      } catch (Exception e) {
        LoggerUtil.error(e.getClass().getName());
      } 
    } 
    return process;
  }
}
