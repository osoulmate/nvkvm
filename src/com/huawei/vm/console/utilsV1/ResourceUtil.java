package com.huawei.vm.console.utilsV1;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
public class ResourceUtil
{
  private static final String configPath = "com.huawei.vm.console.vmconfigResource_iMana";
  private static final String resultPath = "com.huawei.vm.console.vmResult_iMana";
  private static final String resourcePath = "com.huawei.vm.console.vmResource_iMana";
  private static final String keyPrefix = "com.huawei.vm.console.error.";
  private ResourceBundle resource = null;
  public static final String COMPILE_JAR_NAME = "com.huawei.vm.console.config.jarname";
  public static final String CREATE_IMAGE_SAVE_IMAGE = "com.huawei.vm.console.creatImage.saveTitle";
  public static final String ERROR_UNKNOWN_ERROR = "com.huawei.vm.console.error.unknown";
  public static final String CONFIG_VM_VERSION = "com.huawei.vm.console.config.version";
  public static String CONFIG_VM_LIBARY = "";
  private static ResourceBundle config = null;
  public static final String CONFIG_VM_LIB_EXT = "com.huawei.vm.console.config.libExt";
  public static final String CONFIG_VM_LIBARY_PATH = "com.huawei.vm.console.config.library.path";
  public String getCONFIG_VM_LIBARY() {
    return CONFIG_VM_LIBARY;
  }
  public static final String CONFIG_VM_READ_RETRY = "com.huawei.vm.console.config.device.read.retry"; public static final String CONFIG_VM_RECEIVE_OVERTIME = "com.huawei.vm.console.config.receiver.overtime";
  public static final String CONFIG_VM_BUSINESS_OVERTIME = "com.huawei.vm.console.config.business.overtime";
  public static final String CONFIG_VM_PRINT_LEVEL = "com.huawei.vm.console.config.print.level";
  public static final String CONFIG_VM_MAX_PACKET_SIZE = "com.huawei.vm.console.config.datapacket.size";
  public void setCONFIG_VM_LIBARY(String cONFIGVMLIBARY) {
    CONFIG_VM_LIBARY = cONFIGVMLIBARY;
  }
  public static final String CONFIG_VM_MAX_CDROM_PACKET_SIZE = "com.huawei.vm.console.config.cdrom.datapacket.size";
  public static final String CONFIG_VM_MAX_FLOPPY_PACKET_SIZE = "com.huawei.vm.console.config.floppy.datapacket.size";
  public static final String CONFIG_VM_HEART_BIT_INTERVAL = "com.huawei.vm.console.config.heartBit.interval";
  public ResourceUtil() {
    if ("zh".equals(Locale.getDefault().getLanguage())) {
      this.resource = ResourceBundle.getBundle("com.huawei.vm.console.vmResource_iMana", new Locale("zh"), ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }
    else {
      this.resource = ResourceBundle.getBundle("com.huawei.vm.console.vmResource_iMana", new Locale("en"), ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    } 
  }
  public void setLanguage(String language) {
    if ("en".equalsIgnoreCase(language)) {
      this.resource = ResourceBundle.getBundle("com.huawei.vm.console.vmResource_iMana", new Locale("en"), ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }
    else {
      this.resource = ResourceBundle.getBundle("com.huawei.vm.console.vmResource_iMana", new Locale("zh"), ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    } 
  }
  public void dosetLanguage(String language) {
    if ("en".equalsIgnoreCase(language)) {
      this.resource = ResourceBundle.getBundle("com.huawei.vm.console.vmResult_iMana", new Locale("en"), ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }
    else {
      this.resource = ResourceBundle.getBundle("com.huawei.vm.console.vmResult_iMana", new Locale("zh"), ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    } 
  }
  public static String getConfigItem(String key) {
    String configResource = null;
    config = ResourceBundle.getBundle("com.huawei.vm.console.vmconfigResource_iMana", new Locale("en"), ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    try {
      configResource = config.getString(key);
    }
    catch (MissingResourceException e) {
      TestPrint.println(3, "Resource Util :can not find config info");
      if ("com.huawei.vm.console.config.business.overtime".equals(key)) {
        configResource = "20000";
      }
      else if ("com.huawei.vm.console.config.libExt".equals(key)) {
        configResource = ".dll";
      }
      else if ("VMConsoleLib_iMana".equals(key)) {
        configResource = "VMConsoleLib_iMana";
      }
      else if ("VMConsoleLib_iMana_x64".equals(key)) {
        configResource = "VMConsoleLib_iMana_x64";
      }
      else if ("com.huawei.vm.console.config.library.path".equals(key)) {
        configResource = "com/huawei/vm/console/";
      }
      else if ("com.huawei.vm.console.config.device.read.retry".equals(key)) {
        configResource = "10";
      }
      else if ("com.huawei.vm.console.config.receiver.overtime".equals(key)) {
        configResource = "3000";
      }
      else if ("com.huawei.vm.console.config.version".equals(key)) {
        configResource = "2.20.5.52";
      }
      else if ("com.huawei.vm.console.config.print.level".equals(key)) {
        configResource = "100";
      }
      else if ("com.huawei.vm.console.config.datapacket.size".equals(key)) {
        configResource = "1024";
      }
      else if ("com.huawei.vm.console.config.heartBit.interval".equals(key)) {
        configResource = "10000";
      }
      else if ("USB".equals(key)) {
        configResource = "USB";
      }
      else if ("PS2".equals(key)) {
        configResource = "PS2";
      }
      else if ("JDM".equals(key)) {
        configResource = "JDM";
      }
      else {
        configResource = "0";
      } 
    } 
    return configResource;
  }
  public String getResource(String key) {
    String configResource = null;
    try {
      configResource = this.resource.getString(key);
    }
    catch (MissingResourceException e) {
      TestPrint.println(3, "Resource Util :can not find resource info");
      if ("com.huawei.vm.console.creatImage.saveTitle".equals(key)) {
        configResource = "Save Image File";
      }
      else {
        configResource = "Virtual Media";
      } 
    } 
    return configResource;
  }
  public String getErrMessage(int errCode) {
    try {
      return this.resource.getString("com.huawei.vm.console.error." + errCode);
    }
    catch (MissingResourceException me) {
      return this.resource.getString("com.huawei.vm.console.error.unknown");
    } 
  }
}
