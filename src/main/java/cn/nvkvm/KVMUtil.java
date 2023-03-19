package cn.nvkvm;
//import cn.library.LoggerUtil;
import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory; 
import java.util.Locale;
public class KVMUtil
{
  private static String osType = "";
  private static String osArch = "";
  private final static Log log = LogFactory.getLog(Main.class);
  private static String getOsName() {
    if (osType != null && "".equals(osType)) {
      osType = System.getProperty("os.name");
      //LoggerUtil.info( "osType: "+ osType );
      log.info( "osType: "+ osType );
      if (null != osType)
      {
        osType = osType.toLowerCase(Locale.getDefault());
      }
    } 
    return osType;
  }
  public static String getOsArch() {
    if (osArch != null && "".equals(osArch)) {
      osArch = System.getProperty("os.arch");
      if (null != osArch)
      {
        osArch = osArch.toLowerCase(Locale.getDefault());
      }
    } 
    return osArch;
  }
  public static boolean isOSTypeByNmae(String oSTypeNmae) {
    if (null != getOsName() && getOsName().startsWith(oSTypeNmae))
    {
      return true;
    }
    return false;
  }
  public static boolean isWindowsOS() {
    return isOSTypeByNmae("windows");
  }
  public static boolean isUnix() {
    return isOSTypeByNmae("freebsd");
  }
  public static boolean isLinuxOS() {
    return (isOSTypeByNmae("linux") || isOSTypeByNmae("mac os x") || isOSTypeByNmae("freebsd"));
  }
  public static boolean isLinux() {
    return isOSTypeByNmae("linux");
  }
  public static boolean isMacOS() {
    return isOSTypeByNmae("mac os x");
  }
  public static boolean isOsArchByName(String osArch) {
    if (null != getOsArch() && getOsArch().startsWith(osArch))
    {
      return true;
    }
    return false;
  }
}
