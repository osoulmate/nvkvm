package com.huawei.vm.console.managementV1;
import com.huawei.vm.console.newUtils.DeviceIO;
import com.huawei.vm.console.processV1.CreateImageFile;
import com.huawei.vm.console.utilsV1.ResourceUtil;
import com.huawei.vm.console.utilsV1.TestPrint;
import com.huawei.vm.console.utilsV1.VMException;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.UDFExtendFile;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Locale;
import java.util.Map;
public class ConsoleControllers
  implements Runnable
{
  private static final long serialVersionUID = 5808868056354429488L;
  private int vmState = 0;
  private VMConsole console;
  private CreateImageFile imageCreat;
  private String floppyDevices = "";
  private String cdromDevices = "";
  private static boolean libraryInstall = false;
  private static int libID = 0;
  private int linkType;
  private final Object lock = new Object();
  private boolean isCreateVMLink = false;
  private String devicePathForLink = null;
  private String serverIPAddressForLink = null;
  private int serverPortForLink = -1;
  private byte[] certifyIDForLink = null;
  private byte[] saltForLink = null;
  private boolean bSecretVMM = false;
  private boolean bCodeKeyNego = false;
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
  private volatile Thread appletThread;
  public ResourceUtil resouceUtil = new ResourceUtil();
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
    TestPrint.println(1, "Begin init");
    this.bSecretVMM = false;
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
    if (!"zh".equals(Locale.getDefault().getLanguage()))
    {
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
    if (null != this.appletThread)
    {
      this.appletThread = null;
    }
  }
  public void start() {
    TestPrint.println(1, "Begin start");
    try {
      this.exitFlag = false;
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
    TestPrint.println(1, "Begin stop");
    synchronized (this.lock) {
      this.exitFlag = true;
      this.lock.notifyAll();
    } 
  }
  public void destroy() {
    try {
      TestPrint.println(1, "Begin to destroy");
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
    while (!this.exitFlag) {
      TestPrint.println(1, "Applet start");
      synchronized (this.lock) {
        if (this.isCreateImage) {
          TestPrint.println(1, "Do create image file");
          this.isCreateImage = false;
          doCreateImageFile(this.deviceForCreateImage, this.imagePathForCreateImage);
          synchronized (this.lockForCreateImage)
          {
            this.isCreateImageOK = true;
            this.lockForCreateImage.notifyAll();
          }
        } else if (this.isCreateVMLink) {
          TestPrint.println(1, "Do create vm link");
          this.isCreateVMLink = false;
          doCreatVMLink(this.linkType, this.devicePathForLink, this.serverIPAddressForLink, this.serverPortForLink, this.certifyIDForLink, this.saltForLink, this.bCodeKeyNego, this.bSecretVMM, this.isWriteProtectForLink, this.localdirName, this.memoryStruct, this.util, this.srcType);
        }
        else if (this.isDestoryVMLink) {
          TestPrint.println(1, "Do destory vm link");
          this.isDestoryVMLink = false;
          if (this.isDestoryFloppyVMLink) {
            this.isDestoryFloppyVMLink = false;
            doDestroyVMLink(1);
          }
          else if (this.isDestoryCdromVMLink) {
            this.isDestoryCdromVMLink = false;
            doDestroyVMLink(2);
          } 
          synchronized (this.lockForDestoryLink)
          {
            this.isDestoryVMLinkOK = true;
            this.lockForDestoryLink.notifyAll();
          }
        }
        else if (this.isChangeCdromImg) {
          TestPrint.println(1, "Do change cdrom image file");
          this.isChangeCdromImg = false;
          this.resultForCdromImg = doChangeCdromImage(this.pathForChangeCdromImg);
          synchronized (this.lockForCdromImg)
          {
            this.isChangeCdromImgOK = true;
            this.lockForCdromImg.notifyAll();
          }
        }
        else if (this.isChangeMemoryImg) {
          TestPrint.println(1, "Do change memory image file");
          this.isChangeMemoryImg = false;
          this.resultForCdromImg = doChangeCdromImage(this.pathForChangeCdromImg);
          synchronized (this.lockForCdLocalDir)
          {
            this.isChangeCdLocalDirOK = true;
            this.lockForCdLocalDir.notifyAll();
          }
        }
        else if (this.isChangeFloppyImg) {
          TestPrint.println(1, "Do change floppy image file");
          this.isChangeFloppyImg = false;
          this.resultForFloppyImg = doChangeFloppyImage(this.pathForChangeFloppyImg);
          synchronized (this.lockForFloppyImg)
          {
            this.isChangeFloppyImgOK = true;
            this.lockForFloppyImg.notifyAll();
          }
        }
        else if (this.checkFileExist) {
          TestPrint.println(1, "Do check file exist");
          this.checkFileExist = false;
          this.resultForCheckFileExsit = doCheckFileExsit(this.pathForCreateImg);
          synchronized (this.lockForCheckFileExsit) {
            this.isCheckFileExsitOK = true;
            this.lockForCheckFileExsit.notifyAll();
          } 
        } else {
          try {
            TestPrint.println(1, "Applet waiting");
            this.lock.wait();
            TestPrint.println(1, "Applet wake up");
          }
          catch (InterruptedException e) {}
        } 
      } 
    } 
    TestPrint.println(1, "Applet stop");
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
    while (true) {
      synchronized (this.lockForCreateImage) {
        if (!this.isCreateImageOK)
        {
          try {
			this.lockForCreateImage.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
        break;
      } 
    } 
  }
  public String getDeviceForCreateImage() {
    return this.deviceForCreateImage;
  }
  public String getImagePathForCreateImage() {
    return this.imagePathForCreateImage;
  }
  public String getAbsoluteImagePath() {
    return this.imageCreat.getAbsoluteImagePath();
  }
  public String getSaveFilePath(String device) {
    int deviceType = -1;
    if (null != device && !"".equals(device) && !"null".equals(device))
    {
      if (this.floppyDevices.indexOf(device) != -1) {
        deviceType = 0;
      }
      else if (this.cdromDevices.indexOf(device) != -1) {
        deviceType = 2;
      } 
    }
    return getFileSavePath(deviceType);
  }
  public String getFileSavePath(int deviceType) {
    String title = this.resouceUtil.getResource("com.huawei.vm.console.creatImage.saveTitle");
    final Frame frame = new Frame(title);
    String fileExt = null;
    WindowAdapter listener = new WindowAdapter()
      {
        public void windowClosing(WindowEvent windowevent)
        {
          frame.setVisible(false);
        }
      };
    frame.addWindowListener(listener);
    FileDialog fd = new FileDialog(frame, title, 1);
    fd.setFilenameFilter(new FilenameFilter()
        {
          public boolean accept(File dir, String name)
          {
            return (name.endsWith(".iso") || name.endsWith(".img") || dir.isDirectory());
          }
        });
    if (2 == deviceType) {
      fileExt = "*.iso";
    }
    else if (0 == deviceType) {
      fileExt = "*.img";
    }
    else {
      fileExt = "*.iso;*.img";
    } 
    fd.setFile(fileExt);
    fd.setVisible(true);
    String filePath = null;
    if (fd.getDirectory() != null && fd.getFile() != null)
    {
      filePath = fd.getDirectory() + fd.getFile();
    }
    frame.removeWindowListener(listener);
    frame.dispose();
    fd.dispose();
    return filePath;
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
  public void doCreatVMLink(int type, String devicePath, String serverIPAddress, int serverPort, byte[] certifyID, byte[] salt, boolean bCodeKeyNego, boolean bSecret, boolean isWriteProtect, String localDirName, Map<Long, UDFExtendFile> memoryStruct, ResourceUtil util, int srcType) {
    if (null != devicePath)
    {
      if ("".equals(devicePath) || "null".equalsIgnoreCase(devicePath) || "undefined".equalsIgnoreCase(devicePath))
      {
        devicePath = null;
      }
    }
    this.console.creatVMLink(type, serverIPAddress, serverPort, certifyID, salt, bCodeKeyNego, bSecret, devicePath, srcType, isWriteProtect, localDirName, memoryStruct, util);
  }
  public String creatVMLink(int type, String devicePath, String serverIPAddress, int serverPort, byte[] certifyID, byte[] salt, boolean bCodeKeyNego, boolean bSecret, boolean isWriteProtect, String localDirName, Map<Long, UDFExtendFile> memoryStruct, ResourceUtil util, int srcType) {
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
      this.saltForLink = new byte[salt.length];
      System.arraycopy(salt, 0, this.saltForLink, 0, salt.length);
      this.bSecretVMM = bSecret;
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
  public String getDevicePathForLink() {
    return this.devicePathForLink;
  }
  public void doDestroyVMLink(int type) {
    if (211 == this.vmState) {
      return;
    }
    this.console.destoryVMLink(type);
  }
  public void destroyVMLink(int type) {
    TestPrint.println(1, "enter destroyVMLink");
    synchronized (this.lock) {
      this.isDestoryVMLink = true;
      if (2 == type) {
        TestPrint.println(1, "destroy CDROM VMLink");
        this.isDestoryCdromVMLink = true;
      }
      else if (1 == type) {
        TestPrint.println(1, "destroy FLOPY VMLink");
        this.isDestoryFloppyVMLink = true;
      } 
      this.isDestoryVMLinkOK = false;
      this.lock.notifyAll();
    } 
    while (true) {
      synchronized (this.lockForDestoryLink) {
        if (!this.isDestoryVMLinkOK)
        {
          try {
			this.lockForDestoryLink.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
        break;
      } 
    } 
    TestPrint.println(1, "EXIT destroyVMLink");
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
    while (true) {
      synchronized (this.lockForFloppyImg) {
        if (!this.isChangeFloppyImgOK)
        {
          try {
			this.lockForFloppyImg.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
        break;
      } 
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
    while (true) {
      synchronized (this.lockForCdromImg) {
        if (!this.isChangeCdromImgOK)
        {
          try {
			this.lockForCdromImg.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
        break;
      } 
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
    while (true) {
      synchronized (this.lockForCdLocalDir) {
        if (!this.isChangeCdLocalDirOK)
        {
          try {
			this.lockForCdLocalDir.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
        break;
      } 
    } 
    return this.resultForCdromImg;
  }
  public String getFloppyDevices() {
    this.floppyDevices = "";
    try {
      if (isSetUp())
      {
        this.floppyDevices = DeviceIO.getFloppyDevices();
      }
    }
    catch (UnsatisfiedLinkError ue) {}
    if (null == this.floppyDevices)
    {
      this.floppyDevices = "";
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
    TestPrint.println(1, "type is:" + type);
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
  public boolean isGetPrivilige() {
    return (211 != this.vmState);
  }
  public boolean isFloppyImage() {
    return (2 == this.console.getFloppyType());
  }
  public boolean isFloppyDevice() {
    return (1 == this.console.getFloppyType());
  }
  public boolean isCdromImage() {
    return (2 == this.console.getCdromType());
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
    ClassLoader classloader = ConsoleControllers.class.getClassLoader();
    byte[] buffer = new byte[4096];
    if (classloader.getResource(source) == null)
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
        TestPrint.println(2, "Console Controller:input stream close error when make lib");
      } finally {
        try {
          if (null != fileOutputStream)
          {
            fileOutputStream.close();
          }
        }
        catch (IOException e) {
          TestPrint.println(2, "Console Controller:Output steam close error when make lib");
        } 
      } 
    } 
    return true;
  }
  public static boolean libarayPrepare() {
    String libName = "unknown";
    String separator = System.getProperty("file.separator");
    String libDir = System.getProperty("java.io.tmpdir");
    String sys = System.getProperty("os.name").toLowerCase();
    if (sys.startsWith("windows")) {
      libID = System.identityHashCode(Long.valueOf(System.currentTimeMillis()));
      libName = ResourceUtil.getConfigItem(ResourceUtil.CONFIG_VM_LIBARY) + libID + ResourceUtil.getConfigItem("com.huawei.vm.console.config.libExt");
      if (!libDir.endsWith(separator))
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
        libraryInstall = makeLibaray(ResourceUtil.getConfigItem("com.huawei.vm.console.config.library.path") + ResourceUtil.getConfigItem(ResourceUtil.CONFIG_VM_LIBARY) + ResourceUtil.getConfigItem("com.huawei.vm.console.config.libExt"), libDir);
      }
    }
    else {
      libraryInstall = false;
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
    String sys = System.getProperty("os.name").toLowerCase();
    if (sys.startsWith("windows")) {
      libName = ResourceUtil.getConfigItem(ResourceUtil.CONFIG_VM_LIBARY);
    } else {
      return;
    } 
    File file = new File(libDir);
    if (!libDir.endsWith(separator))
    {
      libDir = libDir + separator;
    }
    String[] files = file.list();
    String libExt = ResourceUtil.getConfigItem("com.huawei.vm.console.config.libExt");
    for (int i = 0; i < files.length; i++) {
      if (files[i].startsWith(libName) && files[i].endsWith(libExt))
      {
        (new File(libDir + files[i])).delete();
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
    while (true) {
      synchronized (this.lockForCheckFileExsit) {
        if (!this.isCheckFileExsitOK)
        {
          try {
			this.lockForCheckFileExsit.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
        break;
      } 
    } 
    return this.resultForCheckFileExsit;
  }
  public void setWriteProtect(boolean isWriteProtect) {
    this.console.setWriteProtect(isWriteProtect);
  }
  public static void main(String[] args) {}
  public VMConsole getConsole() {
    return this.console;
  }
  public void setConsole(VMConsole console) {
    this.console = console;
  }
}
