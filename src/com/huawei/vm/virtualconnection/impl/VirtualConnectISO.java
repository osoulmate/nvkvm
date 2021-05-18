package com.huawei.vm.virtualconnection.impl;
import com.huawei.vm.console.management.ConsoleControllers;
import com.huawei.vm.console.utils.ResourceUtil;
import com.huawei.vm.virtualconnection.VirtualConnect;
import com.kvm.UDFExtendFile;
import com.kvm.UDFImageBuilder;
import com.library.InetAddressUtils;
import com.library.LoggerUtil;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;
public class VirtualConnectISO
  extends VirtualConnect
{
  private static final int CDROM = 2;
  private static final int CDROM_IMAGE = 1;
  private static final int CDROM_DIR = 2;
  private static final int MAX_FILE_COUNT = 10000;
  private static final int ERR_VIRCONN_OK = 0;
  private static final int ERR_VIRCONN_CONN_ALREADY_EXIST = 501;
  private static final int ERR_VIRCONN_PARA_TYPE_ERROR = 502;
  private static final int ERR_VIRCONN_PARA_IP_ERROR = 503;
  private static final int ERR_VIRCONN_PARA_PORT_ERROR = 504;
  private static final int ERR_VIRCONN_PARA_PATH_NULL = 505;
  private static final int ERR_VIRCONN_PARA_PATH_INVALID = 506;
  private static final int ERR_VIRCONN_CONN_NOT_EXIST = 507;
  private static final int ERR_VIRCONN_CREATE_FAILED_CONSOLE_NOT_ACTIVE = 508;
  private static final int ERR_VIRCONN_CREATE_FAILED_CONSOLE_STILL_IDLE = 509;
  private static final int ERR_VIRCONN_FILE_COUNT_OUT_LIMIT = 510;
  private static final int ERR_VIRCONN_OPEN_DIRECTORY_FAILED = 511;
  private ResourceUtil util = null;
  private ConsoleControllers vmApplet = null;
  private Map<Long, UDFExtendFile> memoryStruct = null;
  private String localDirName = null;
  private Timer cdRomVMlink = null;
  private boolean vmmconnected = false;
  public VirtualConnectISO() {
    this.util = new ResourceUtil();
    this.util.dosetLanguage("en");
    this.vmApplet = new ConsoleControllers();
    this.vmApplet.setLanguage("en");
  }
  public VirtualConnectISO(String threadName) {
    this.util = new ResourceUtil();
    this.util.dosetLanguage("en");
    this.vmApplet = new ConsoleControllers();
    this.vmApplet.setLanguage("en");
  }
  public int disconnect() {
    if (!this.vmmconnected)
    {
      return 507;
    }
    disconnectVMLink();
    return 0;
  }
  public String getErrMsg(int errCode) {
    return this.vmApplet.getStatement(errCode);
  }
  private void disconnectVMLink() {
    this.vmApplet.destroyVMLink(2);
    java.util.Timer t = new java.util.Timer("cdrom disconnectVMLink");
    TimerTask task = new TimerTask()
      {
        public void run()
        {
          VirtualConnectISO.this.vmmconnected = false;
        }
      };
    t.schedule(task, 2000L);
  }
  private Action doCdRomVMlink() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (!VirtualConnectISO.this.vmApplet.isVMLinkCrt(2)) {
            VirtualConnectISO.this.cdRomVMlink.stop();
            VirtualConnectISO.this.cdRomVMlink = null;
            VirtualConnectISO.this.disconnectVMLink();
          } 
        }
      };
    return action;
  }
  private int checkCdromVMConsole() {
    int state_code = 0;
    if (!this.vmApplet.isConsoleOK(2)) {
      if (this.vmApplet.isVMLinkCrt(2))
      {
    	java.util.Timer date = new java.util.Timer("cdrom checkVMConsole");
        TimerTask task = new TimerTask()
          {
            public void run()
            {
              if (null == VirtualConnectISO.this.cdRomVMlink)
              {
                VirtualConnectISO.this.cdRomVMlink = new Timer(1000, VirtualConnectISO.this.doCdRomVMlink());
              }
              VirtualConnectISO.this.cdRomVMlink.start();
            }
          };
        date.schedule(task, 800L);
        this.vmmconnected = true;
        state_code = 0;
      }
      else
      {
        this.vmmconnected = false;
        this.vmApplet.destroyVMLink(2);
        state_code = this.vmApplet.getConsoleState(2);
        if (0 == state_code)
        {
          state_code = 508;
        }
      }
    }
    else {
      this.vmmconnected = false;
      state_code = this.vmApplet.getConsoleState(2);
      if (0 == state_code)
      {
        state_code = 509;
      }
    } 
    return state_code;
  }
  private long countFile(File f) {
    long size = 0L;
    long count = 0L;
    File[] flist = f.listFiles();
    if (flist != null && flist.length != 0) {
      size = flist.length;
      for (int i = 0; i < flist.length; i++) {
        if (flist[i].isDirectory()) {
          count = countFile(flist[i]);
          if (count < 0L)
          {
            return -1L;
          }
          size += count;
        } 
        if (size > 10000L)
        {
          return -1L;
        }
      } 
    } 
    return size;
  }
  private int createMemoryISO(String dirPath) {
    int ret = 0;
    try {
      UDFImageBuilder mySabreUDF = new UDFImageBuilder();
      File dirFile = new File(dirPath);
      if (countFile(dirFile) < 0L)
      {
        return 510;
      }
      mySabreUDF.setRootPath(dirFile);
      if (dirFile.getName().length() >= 30) {
        mySabreUDF.setImageIdentifier(dirFile.getName()
            .substring(0, 28) + '~');
      }
      else {
        mySabreUDF.setImageIdentifier(dirFile.getName());
      } 
      mySabreUDF.excute();
      this.localDirName = dirFile.getName();
      this
        .memoryStruct = mySabreUDF.getExtendMap();
    }
    catch (RuntimeException e) {
      ret = 511;
    }
    catch (Exception e) {
      ret = 511;
    } 
    return ret;
  }
  private boolean isValidDirectory(String path) {
    boolean result = true;
    try {
      File file = new File(path);
      if (!file.isDirectory())
      {
        return false;
      }
      File[] subFile = file.listFiles();
      if (subFile == null || subFile.length == 0)
      {
        return false;
      }
    }
    catch (Exception e) {
      result = false;
    } 
    return result;
  }
  private boolean isValidPath(String path) {
    if (null == path || "".equals(path))
    {
      return false;
    }
    if (isValidDirectory(path))
    {
      return true;
    }
    String cdromImagePath = path.toLowerCase(Locale.getDefault());
    if (path.length() > 4 && ".iso"
      .equals(cdromImagePath.substring(path.length() - 4)))
    {
      return true;
    }
    return false;
  }
  private int parameterCheck(int type, String serverIp, int serverPort, String path) {
    if (this.vmmconnected)
    {
      return 501;
    }
    if (2 != type)
    {
      return 502;
    }
    if (!InetAddressUtils.isIPv4Address(serverIp) && 
      !InetAddressUtils.isIPv6Address(serverIp))
    {
      return 503;
    }
    if (serverPort < 1 || serverPort > 65535)
    {
      return 504;
    }
    if (!isValidPath(path))
    {
      return 506;
    }
    return 0;
  }
  public int connect(int type, String serverIp, int serverPort, byte[] certifyID, byte[] salt, boolean bCodeKeyNego, boolean bSecret, String path) {
    int result = 0;
    int srcType = 1;
    result = parameterCheck(type, serverIp, serverPort, path);
    if (result != 0)
    {
      return result;
    }
    this.vmApplet.getConsole().setVmm_compress_state(bSecret ? 1 : 0);
    if (salt == null)
    {
      salt = new byte[16];
    }
    if (isValidDirectory(path)) {
      srcType = 2;
      result = createMemoryISO(path);
      if (result != 0)
      {
        return result;
      }
    } 
    this.vmApplet.creatVMLink(2, path, serverIp, serverPort, certifyID, false, bCodeKeyNego, salt, this.localDirName, this.memoryStruct, this.util, srcType);
    try {
      Thread.sleep(7000L);
    }
    catch (InterruptedException e) {
      LoggerUtil.error("wait for connect error!");
    } 
    return checkCdromVMConsole();
  }
  public int connect(int type, String serverIp, int serverPort, long verifyId, String path) {
    byte[] codeKey = new byte[4];
    codeKey[0] = (byte)(int)(verifyId >> 24L & 0xFFL);
    codeKey[1] = (byte)(int)(verifyId >> 16L & 0xFFL);
    codeKey[2] = (byte)(int)(verifyId >> 8L & 0xFFL);
    codeKey[3] = (byte)(int)(verifyId & 0xFFL);
    return connect(type, serverIp, serverPort, codeKey, null, false, false, path);
  }
}
