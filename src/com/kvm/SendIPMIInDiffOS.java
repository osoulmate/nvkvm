package com.kvm;
import com.library.LoggerUtil;
import java.io.File;
class SendIPMIInDiffOS
{
  public static final String WINDOWS_IPMITOOL_PATH = "ipmitool/ipmitool.exe";
  public static final String LINUX_IPMITOOL_PATH = "ipmitool";
  public Process useIpmitoolInDiffOS(String ipmiOEM) {
    String command = "";
    String ipmitoolPath = "";
    String absolutePath = "";
    Process process = null;
    if (KVMUtil.isWindowsOS()) {
      ipmitoolPath = "ipmitool/ipmitool.exe";
      File file = new File(ipmitoolPath);
      absolutePath = file.getAbsolutePath();
      LoggerUtil.info(absolutePath);
      command = absolutePath + " " + ipmiOEM;
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
