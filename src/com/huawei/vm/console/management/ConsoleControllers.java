package com.huawei.vm.console.management;
import com.huawei.vm.console.process.CreateImageFile;
import com.huawei.vm.console.utils.DeviceIO;
import com.huawei.vm.console.utils.ResourceUtil;
import com.huawei.vm.console.utils.TestPrint;
import com.huawei.vm.console.utils.VMException;
import com.kvm.UDFExtendFile;
import com.library.LoggerUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;
import java.util.Map;
public class ConsoleControllers
  implements Runnable
{
  private int vmState = 0;
  private VMConsole console;
  private CreateImageFile imageCreat;
  private String floppyDevices = "";
  private String cdromDevices = "";
  private static boolean libraryInstall = false;
  private static int libID = 0;
  private int linkType;
  private String hmac = "PBKDF2WithHmacSHA1";
  private int iter = 5000;
  public void setPbkdf2Params(String h, int i) {
    this.hmac = h;
    this.iter = i;
  }
  private final Object lock = new Object();
  private boolean isCreateVMLink = false;
  private String devicePathForLink = null;
  private String serverIPAddressForLink = null;
  private int serverPortForLink = -1;
  private byte[] certifyIDForLink = null;
  private byte[] vmmdataForSalt = null;
  private boolean bCodeKeyNego = true;
  private boolean isWriteProtectForLink = false;
  private boolean isDestoryVMLink = false;
  private boolean isDestoryFloppyVMLink = false;
  private boolean isDestoryCdromVMLink = false;
  private boolean checkFileExist = false;
  private final Object lockForDestoryLink = new Object();
  private boolean isDestoryVMLinkOK = false;
  private boolean isCreateImage = false;
  private final Object lockForCreateImage = new Object();
  private boolean isCreateImageOK = false;
  private String deviceForCreateImage = null;
  private String imagePathForCreateImage = null;
  private boolean isChangeFloppyImg = false;
  private final Object lockForFloppyImg = new Object();
  private boolean isChangeFloppyImgOK = false;
  private int resultForFloppyImg = 0;
  private String pathForChangeFloppyImg = null;
  private boolean isChangeCdromImg = false;
  private final Object lockForCdromImg = new Object();
  private final Object lockForCdLocalDir = new Object();
  private final Object lockForCheckFileExsit = new Object();
  private boolean isChangeCdromImgOK = false;
  private boolean isChangeCdLocalDirOK = false;
  private boolean isCheckFileExsitOK = false;
  private int resultForCdromImg = 0;
  private boolean resultForCheckFileExsit = false;
  private String pathForChangeCdromImg = null;
  private String pathForCreateImg = null;
  private boolean exitFlag = false;
  private Thread appletThread;
  private ResourceUtil resouceUtil = new ResourceUtil();
  private String localdirName = null;
  private Map<Long, UDFExtendFile> memoryStruct = null;
  private ResourceUtil util;
  private int srcType = 0;
  private boolean isChangeMemoryImg = false;
  public ConsoleControllers() {
    init();
    start();
  }
  public ConsoleControllers(boolean isCreateImage) {
    init();
    if (!isCreateImage)
    {
      start();
    }
  }
  public void init() {
    this.console = new VMConsole();
    this.console.setResouceUtil(this.resouceUtil);
    this.vmState = 0;
    try {
      cleanLib();
      if (!libarayPrepare())
      {
        this.vmState = 210;
      }
    }
    catch (AccessControlException ae) {
      this.vmState = 211;
    } 
    if ("zh".equals(Locale.getDefault().getLanguage())) {
      setLanguage("zh");
    }
    else if ("ja".equals(Locale.getDefault().getLanguage())) {
      setLanguage("ja");
    }
    else if ("fr".equals(Locale.getDefault().getLanguage())) {
      setLanguage("fr");
    }
    else {
      setLanguage("en");
    } 
    this.imageCreat = new CreateImageFile();
  }
  public void threadStart(String threadName) {
    if (null == this.appletThread) {
      this.appletThread = new Thread(this);
      this.appletThread.setName(threadName);
      this.appletThread.start();
    } 
  }
  public void threadDestroy() {
    if (null != this.appletThread) {
      this.appletThread.interrupt();
      this.appletThread = null;
    } 
  }
  public void start() {
    try {
      synchronized (this.lock) {
        this.exitFlag = false;
      } 
      this.appletThread = new Thread(this);
      this.appletThread.setName("Applet Thread");
      this.appletThread.start();
      getFloppyDevices();
      getCDROMDevices();
    }
    catch (AccessControlException ae) {
      this.vmState = 211;
    }
    catch (UnsatisfiedLinkError ufe) {
      this.vmState = 210;
    } 
  }
  public void stop() {
    synchronized (this.lock) {
      this.exitFlag = true;
      this.lock.notifyAll();
    } 
  }
  public void destroy() {
    try {
      if (null != this.imageCreat)
      {
        this.imageCreat.setExitFlag(true);
      }
      if (null != this.console)
      {
        this.console.destoryVMLink(0);
      }
      cleanLib();
    }
    catch (AccessControlException ae) {
      this.vmState = 211;
    } 
  }
  public void run() {
    synchronized (this.lock) {
      while (!this.exitFlag) {
        if (this.isCreateImage) {
          this.isCreateImage = false;
          doCreateImageFile(this.deviceForCreateImage, this.imagePathForCreateImage);
          synchronized (this.lockForCreateImage) {
            this.isCreateImageOK = true;
            this.lockForCreateImage.notifyAll();
          }  continue;
        } 
        if (this.isCreateVMLink) {
          this.isCreateVMLink = false;
          try {
            doCreatVMLink(this.linkType, this.devicePathForLink, this.serverIPAddressForLink, this.serverPortForLink, this.certifyIDForLink, this.isWriteProtectForLink, this.bCodeKeyNego, this.vmmdataForSalt, this.localdirName, this.memoryStruct, this.util, this.srcType);
          }
          catch (NoSuchAlgorithmException e) {
            LoggerUtil.error(e.getClass().getName());
          }
          catch (InvalidKeySpecException e) {
            LoggerUtil.error(e.getClass().getName());
          }  continue;
        } 
        if (this.isDestoryVMLink) {
          this.isDestoryVMLink = false;
          if (this.isDestoryFloppyVMLink) {
            this.isDestoryFloppyVMLink = false;
            doDestroyVMLink(1);
          }
          else if (this.isDestoryCdromVMLink) {
            this.isDestoryCdromVMLink = false;
            doDestroyVMLink(2);
          } 
          synchronized (this.lockForDestoryLink) {
            this.isDestoryVMLinkOK = true;
            this.lockForDestoryLink.notifyAll();
          } 
          continue;
        } 
        if (this.isChangeCdromImg) {
          this.isChangeCdromImg = false;
          this.resultForCdromImg = doChangeCdromImage(this.pathForChangeCdromImg);
          synchronized (this.lockForCdromImg) {
            this.isChangeCdromImgOK = true;
            this.lockForCdromImg.notifyAll();
          } 
          continue;
        } 
        if (this.isChangeMemoryImg) {
          this.isChangeMemoryImg = false;
          this.resultForCdromImg = doChangeCdromImage(this.pathForChangeCdromImg);
          synchronized (this.lockForCdLocalDir) {
            this.isChangeCdLocalDirOK = true;
            this.lockForCdLocalDir.notifyAll();
          }  continue;
        } 
        if (this.isChangeFloppyImg) {
          this.isChangeFloppyImg = false;
          this.resultForFloppyImg = doChangeFloppyImage(this.pathForChangeFloppyImg);
          synchronized (this.lockForFloppyImg) {
            this.isChangeFloppyImgOK = true;
            this.lockForFloppyImg.notifyAll();
          } 
          continue;
        } 
        if (this.checkFileExist) {
          this.checkFileExist = false;
          this.resultForCheckFileExsit = doCheckFileExsit(this.pathForCreateImg);
          synchronized (this.lockForCheckFileExsit) {
            this.isCheckFileExsitOK = true;
            this.lockForCheckFileExsit.notifyAll();
          } 
          continue;
        } 
        try {
          this.lock.wait();
        }
        catch (InterruptedException e) {}
      } 
    } 
  }
  public void doCreateImageFile(String device, String imagePath) {
    int imageType = -1;
    if (null == device || "".equals(device)) {
      imageType = 4;
    }
    else if (-1 != this.cdromDevices.indexOf(device)) {
      imageType = 3;
    }
    else if (-1 != this.floppyDevices.indexOf(device)) {
      imageType = 1;
    }
    else {
      imageType = 4;
    } 
    this.imageCreat.create(device, imagePath, imageType);
  }
  public void createImageFile(String device, String imagePath) {
    if (211 == this.vmState) {
      return;
    }
    if (701 == this.imageCreat.getState()) {
      return;
    }
    synchronized (this.lock) {
      this.isCreateImage = true;
      this.deviceForCreateImage = device;
      this.imagePathForCreateImage = imagePath;
      this.isCreateImageOK = false;
      this.lock.notifyAll();
    } 
    boolean isCreated = false;
    String flag = null;
    while (true) {
      synchronized (this.lock) {
        isCreated = this.isCreateImageOK;
        flag = "do";
      } 
      try {
        while (!isCreated) {
          synchronized (this.lockForCreateImage) {
            if (flag.equals("do"))
            {
              this.lockForCreateImage.wait();
            }
          } 
          synchronized (this.lock) {
            isCreated = this.isCreateImageOK;
          } 
        } 
        break;
      } catch (InterruptedException ie) {}
    } 
  }
  public String getAbsoluteImagePath() {
    return this.imageCreat.getAbsoluteImagePath();
  }
  public int getImageCreateProcess() {
    int process = this.imageCreat.getCreateState();
    if (332 == process)
    {
      process = 0;
    }
    return process;
  }
  public boolean isImageCreateOK() {
    return (700 == this.imageCreat.getState() && isLibOK());
  }
  public void stopImageCreate() {
    this.imageCreat.setExitFlag(true);
  }
  public void doCreatVMLink(int type, String devicePath, String serverIPAddress, int serverPort, byte[] certifyID, boolean isWriteProtect, boolean bCodeKeyNego, byte[] vmm_salt, String localDirName, Map<Long, UDFExtendFile> memoryStruct, ResourceUtil util, int srcType) throws NoSuchAlgorithmException, InvalidKeySpecException {
    if (null != devicePath)
    {
      if ("".equals(devicePath) || "null".equalsIgnoreCase(devicePath) || "undefined"
        .equalsIgnoreCase(devicePath))
      {
        devicePath = null;
      }
    }
    this.console.creatVMLink(type, serverIPAddress, serverPort, certifyID, devicePath, srcType, isWriteProtect, bCodeKeyNego, vmm_salt, localDirName, memoryStruct, util, this.hmac, this.iter);
  }
  public String creatVMLink(int type, String devicePath, String serverIPAddress, int serverPort, byte[] certifyID, boolean isWriteProtect, boolean bCodeKeyNego, byte[] salt_vmm, String localDirName, Map<Long, UDFExtendFile> memoryStruct, ResourceUtil util, int srcType) {
    String resultForCreateVMLink = null;
    if (211 == this.vmState)
    {
      resultForCreateVMLink = getStatement(211);
    }
    synchronized (this.lock) {
      this.isCreateVMLink = true;
      this.devicePathForLink = devicePath;
      this.serverIPAddressForLink = serverIPAddress;
      this.serverPortForLink = serverPort;
      this.certifyIDForLink = new byte[certifyID.length];
      System.arraycopy(certifyID, 0, this.certifyIDForLink, 0, certifyID.length);
      this.vmmdataForSalt = new byte[salt_vmm.length];
      System.arraycopy(salt_vmm, 0, this.vmmdataForSalt, 0, salt_vmm.length);
      this.bCodeKeyNego = bCodeKeyNego;
      this.isWriteProtectForLink = isWriteProtect;
      this.linkType = type;
      this.localdirName = localDirName;
      this.memoryStruct = memoryStruct;
      this.util = util;
      this.srcType = srcType;
      this.lock.notifyAll();
    } 
    return resultForCreateVMLink;
  }
  public void doDestroyVMLink(int type) {
    if (211 == this.vmState) {
      return;
    }
    this.console.destoryVMLink(type);
  }
  public void destroyVMLink(int type) {
    synchronized (this.lock) {
      this.isDestoryVMLink = true;
      if (2 == type) {
        this.isDestoryCdromVMLink = true;
      }
      else if (1 == type) {
        this.isDestoryFloppyVMLink = true;
      } 
      this.isDestoryVMLinkOK = false;
      this.lock.notifyAll();
    } 
    boolean isDestory = false;
    String flag = null;
    while (true) {
      synchronized (this.lock) {
        isDestory = this.isDestoryVMLinkOK;
        flag = "do";
      } 
      try {
        while (!isDestory) {
          synchronized (this.lockForDestoryLink) {
            if (flag.equals("do"))
            {
              this.lockForDestoryLink.wait();
            }
          } 
          synchronized (this.lock) {
            isDestory = this.isDestoryVMLinkOK;
          } 
        } 
        break;
      } catch (InterruptedException ie) {}
    } 
  }
  public int doChangeFloppyImage(String path) {
    try {
      this.console.changeFloppyImg(path);
      return 0;
    }
    catch (VMException e) {
      return e.getKey();
    } 
  }
  public int changeFloppyImage(String path) {
    synchronized (this.lock) {
      this.isChangeFloppyImg = true;
      this.pathForChangeFloppyImg = path;
      this.isChangeFloppyImgOK = false;
      this.lock.notifyAll();
    } 
    boolean isChanged = false;
    String flag = null;
    while (true) {
      synchronized (this.lock) {
        isChanged = this.isChangeFloppyImgOK;
        flag = "do";
      } 
      try {
        while (!isChanged) {
          synchronized (this.lockForFloppyImg) {
            if (flag.equals("do"))
            {
              this.lockForFloppyImg.wait();
            }
          } 
          synchronized (this.lock) {
            isChanged = this.isChangeFloppyImgOK;
          } 
        } 
        break;
      } catch (InterruptedException ie) {}
    } 
    return this.resultForFloppyImg;
  }
  public int doChangeCdromImage(String path) {
    try {
      this.console.changeCDROMImg(this.localdirName, this.memoryStruct, path);
      return 0;
    }
    catch (VMException e) {
      return e.getKey();
    } 
  }
  public int changeCD(String type, String path, String localDirName, Map<Long, UDFExtendFile> memoryStructMap) {
    if (type.equals("cdrom"))
    {
      return changeCdromImage(path);
    }
    if (type.equals("cdlocal"))
    {
      return changeLocalDir(localDirName, memoryStructMap, path);
    }
    return -1;
  }
  public int changeCdromImage(String path) {
    synchronized (this.lock) {
      this.isChangeCdromImg = true;
      this.pathForChangeCdromImg = path;
      this.isChangeCdromImgOK = false;
      this.lock.notifyAll();
    } 
    boolean isChanged = false;
    String flag = null;
    while (true) {
      synchronized (this.lock) {
        isChanged = this.isChangeCdromImgOK;
        flag = "do";
      } 
      try {
        while (!isChanged) {
          synchronized (this.lockForCdromImg) {
            if (flag.equals("do"))
            {
              this.lockForCdromImg.wait();
            }
          } 
          synchronized (this.lock) {
            isChanged = this.isChangeCdromImgOK;
          } 
        } 
        break;
      } catch (InterruptedException ie) {}
    } 
    return this.resultForCdromImg;
  }
  public int changeLocalDir(String localDirName, Map<Long, UDFExtendFile> memoryISOStruct, String path) {
    synchronized (this.lock) {
      this.isChangeMemoryImg = true;
      this.pathForChangeCdromImg = path;
      this.localdirName = localDirName;
      this.memoryStruct = memoryISOStruct;
      this.isChangeCdLocalDirOK = false;
      this.lock.notifyAll();
    } 
    boolean isChanged = false;
    String flag = null;
    while (true) {
      synchronized (this.lock) {
        isChanged = this.isChangeCdLocalDirOK;
        flag = "do";
      } 
      try {
        while (!isChanged) {
          synchronized (this.lockForCdLocalDir) {
            if (flag.equals("do"))
            {
              this.lockForCdLocalDir.wait();
            }
          } 
          synchronized (this.lock) {
            isChanged = this.isChangeCdLocalDirOK;
          } 
        } 
        break;
      } catch (InterruptedException ie) {}
    } 
    return this.resultForCdromImg;
  }
  public String getFloppyDevices() {
    this.floppyDevices = "";
    if (isSetUp()) {
      this.floppyDevices = DeviceIO.getFloppyDevices();
      if (null == this.floppyDevices)
      {
        this.floppyDevices = "";
      }
    } 
    return this.floppyDevices;
  }
  public String getCDROMDevices() {
    this.cdromDevices = "";
    if (isSetUp()) {
      this.cdromDevices = DeviceIO.getCDROMDevices();
      if (null == this.cdromDevices)
      {
        this.cdromDevices = "";
      }
    } 
    return this.cdromDevices;
  }
  public boolean isConsoleOK(int type) {
    boolean result = false;
    if (this.console.getConsoleState() == 0) {
      result = true;
    }
    else if (1 == type) {
      result = (0 == this.console.getFloppyState());
    }
    else if (2 == type) {
      result = (0 == this.console.getCdRomState());
    } 
    return result;
  }
  public boolean isVMLinkCrt(int type) {
    boolean result = false;
    if (1 == type) {
      result = (4 == this.console.getFloppyState());
    }
    else if (2 == type) {
      result = (4 == this.console.getCdRomState());
    }
    else if (this.console.getConsoleState() == 4) {
      result = true;
    } 
    return result;
  }
  public boolean isLibOK() {
    return (210 != this.vmState);
  }
  public boolean isFloppyDevice() {
    return (1 == this.console.getFloppyType());
  }
  public boolean isCdromDevice() {
    return (1 == this.console.getCdromType());
  }
  public boolean isCdromLocalDir() {
    return (3 == this.console.getCdromType());
  }
  public int getVmState() {
    return this.vmState;
  }
  public int getConsoleState(int type) {
    return this.console.getState(type);
  }
  public String getConsoleStatement(int type) {
    int code = this.console.getState(type);
    return getStatement(code);
  }
  public String getStatement(int stateCode) {
    return this.resouceUtil.getErrMessage(stateCode);
  }
  public void setLanguage(String language) {
    this.resouceUtil.setLanguage(language);
  }
  public static boolean isSetUp() {
    return libraryInstall;
  }
  private static boolean makeLibaray(String source, String dest) {
    ClassLoader classloader = null;
    if (null == ConsoleControllers.class.getClassLoader())
    {
      return false;
    }
    classloader = ConsoleControllers.class.getClassLoader();
    byte[] buffer = new byte[4096];
    if (classloader == null || classloader.getResource(source) == null)
    {
      return false;
    }
    InputStream inputStream = null;
    FileOutputStream fileOutputStream = null;
    try {
      inputStream = classloader.getResourceAsStream(source);
      fileOutputStream = new FileOutputStream(dest);
      int i = inputStream.read(buffer, 0, buffer.length);
      while (i != -1)
      {
        fileOutputStream.write(buffer, 0, i);
        i = inputStream.read(buffer, 0, buffer.length);
      }
    } catch (IOException ioexception) {
      return false;
    } finally {
      try {
        if (null != inputStream)
        {
          inputStream.close();
        }
      }
      catch (IOException e) {
        TestPrint.println(3, "Console Controller:input stream close error when make lib");
      } finally {
        try {
          if (null != fileOutputStream)
          {
            fileOutputStream.close();
          }
        }
        catch (IOException e) {
          TestPrint.println(3, "Console Controller:Output steam close error when make lib");
        } 
      } 
    } 
    return true;
  }
  public static boolean libarayPrepare() {
    String libName = "unknown";
    String separator = System.getProperty("file.separator");
    String libDir = System.getProperty("java.io.tmpdir");
    String sys = System.getProperty("os.name");
    if (null != sys) {
      sys = sys.toLowerCase(Locale.getDefault());
      if (sys.startsWith("windows")) {
        libID = System.identityHashCode(Long.valueOf(System.currentTimeMillis()));
        libName = ResourceUtil.getConfigItem(ResourceUtil.getCONFIG_VM_LIBARY()) + libID + ResourceUtil.getConfigItem("com.huawei.vm.console.config.libExt");
        if (libDir != null && !libDir.endsWith(separator))
        {
          libDir = libDir + separator;
        }
        libDir = libDir + libName;
        File file1 = new File(libDir);
        if (file1.exists())
        {
          libraryInstall = true;
        }
        else
        {
          libraryInstall = makeLibaray(ResourceUtil.getConfigItem("com.huawei.vm.console.config.library.path") + 
              ResourceUtil.getConfigItem(ResourceUtil.getCONFIG_VM_LIBARY()) + 
              ResourceUtil.getConfigItem("com.huawei.vm.console.config.libExt"), libDir);
        }
      }
      else {
        libraryInstall = false;
      } 
    } 
    return libraryInstall;
  }
  public static final int getLibID() {
    return libID;
  }
  public static void cleanLib() {
    String libName = "unknown";
    String separator = System.getProperty("file.separator");
    String libDir = System.getProperty("java.io.tmpdir");
    String sys = System.getProperty("os.name");
    if (null == sys) {
      return;
    }
    sys = sys.toLowerCase(Locale.getDefault());
    String libDirTemp = "";
    if (sys.startsWith("windows")) {
      libName = ResourceUtil.getConfigItem(ResourceUtil.getCONFIG_VM_LIBARY());
    } else {
      return;
    } 
    if (null == libDir) {
      return;
    }
    File file = new File(libDir);
    if (!libDir.endsWith(separator))
    {
      libDir = libDir + separator;
    }
    String[] files = file.list();
    if (null == files) {
      return;
    }
    String libExt = ResourceUtil.getConfigItem("com.huawei.vm.console.config.libExt");
    for (int i = 0; i < files.length; i++) {
      if (files[i].startsWith(libName) && files[i].endsWith(libExt)) {
        libDirTemp = libDir + files[i];
        LoggerUtil.info( "libDirTemp: "+ libDirTemp );
        if (!(new File(libDirTemp)).delete())
        {
          LoggerUtil.error("delete file failed");
        }
      } 
    } 
  }
  public boolean doCheckFileExsit(String path) {
    boolean result = false;
    File file = new File(path);
    if (file.exists())
    {
      result = true;
    }
    return result;
  }
  public boolean checkFileExsit(String path) {
    synchronized (this.lock) {
      this.checkFileExist = true;
      this.pathForCreateImg = path;
      this.isCheckFileExsitOK = false;
      this.lock.notifyAll();
    } 
    boolean isExist = false;
    String flag = null;
    while (true) {
      synchronized (this.lock) {
        isExist = this.isCheckFileExsitOK;
        flag = "do";
      } 
      try {
        while (!isExist) {
          synchronized (this.lockForCheckFileExsit) {
            if (flag.equals("do"))
            {
              this.lockForCheckFileExsit.wait();
            }
          } 
          synchronized (this.lock) {
            isExist = this.isCheckFileExsitOK;
          } 
        } 
        break;
      } catch (InterruptedException ie) {}
    } 
    return this.resultForCheckFileExsit;
  }
  public void setWriteProtect(boolean isWriteProtect) {
    this.console.setWriteProtect(isWriteProtect);
  }
  public VMConsole getConsole() {
    return this.console;
  }
}
