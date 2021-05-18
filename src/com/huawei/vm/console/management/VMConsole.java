package com.huawei.vm.console.management;
import com.huawei.vm.console.communication.CommunicationReceiver;
import com.huawei.vm.console.communication.CommunicationSender;
import com.huawei.vm.console.communication.ProtocolProcessor;
import com.huawei.vm.console.communication.Receiver;
import com.huawei.vm.console.communication.VMTimerTask;
import com.huawei.vm.console.process.SFF8020iProcessor;
import com.huawei.vm.console.process.UFIProcessor;
import com.huawei.vm.console.storage.impl.CDROMDevice;
import com.huawei.vm.console.storage.impl.CDROMDriver;
import com.huawei.vm.console.storage.impl.CDROMImage;
import com.huawei.vm.console.storage.impl.CDROMLocalDir;
import com.huawei.vm.console.storage.impl.FloppyDevice;
import com.huawei.vm.console.storage.impl.FloppyDriver;
import com.huawei.vm.console.storage.impl.FloppyImage;
import com.huawei.vm.console.utils.ResourceUtil;
import com.huawei.vm.console.utils.TestPrint;
import com.huawei.vm.console.utils.VMException;
import com.kvm.AESHandler;
import com.kvm.Base;
import com.kvm.UDFExtendFile;
import com.library.LoggerUtil;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
public class VMConsole
{
  public static final int COMMOM_TYPE = 0;
  public static final int FLOPPY_TYPE = 1;
  public static final int CDROM_TYPE = 2;
  public static final int CONSOLE_IDLE = 0;
  public static final int CONSOLE_INIT = 1;
  public static final int CONSOLE_CERTIFY = 2;
  public static final int CERTIFY_TIMEOUT = 10000;
  private static final int CONSOLE_DEVICE = 3;
  public static final int DEVICE_TIMEOUT = 10000;
  public static final int CONSOLE_ACTIVE = 4;
  public static final int CONNECT_TIMEOUT = 20000;
  public static final int CONSOLE_OK = 0;
  private Socket socket;
  private Timer timer;
  private VMTimerTask timerTask;
  private TimerTask tempTask;
  private Receiver receiver;
  public VMTimerTask getTimerTask() {
    return this.timerTask;
  }
  private CommunicationSender sender = null;
  private Thread senderThread;
  private ProtocolProcessor processor;
  private Thread processorThread;
  private UFIProcessor UFIProcessor;
  private Thread UFIThread;
  private SFF8020iProcessor SFFProcessor;
  private Thread SFFThread;
  private FloppyDriver floppy;
  private CDROMDriver cdrom;
  public static final int MEDIA_NONE_DEVICE = 0;
  public static final int MEDIA_DEVICE = 1;
  public static final int MEDIA_IMAGE = 2;
  public static final int MEDIA_LOCAL_DIR = 3;
  private int floppyType;
  private int cdromType;
  private int consoleState;
  private int floppyState;
  private int cdromState;
  private int errCode = 0;
  private int floppyErrCode = 0;
  private int cdromErrCode = 0;
  private byte[] sessionid = null;
  private static final int SECRETKEY_LEN = 16;
  private static final int SECRET_SESSIONID_LEN = 24;
  private static final int IV_LEN = 16;
  private int consoleType = 0;
  private final Object lock = new Object();
  private int vmm_compress_state = 0;
  private byte[] secretKey = null;
  private byte[] secretIv = null;
  public int getVmm_compress_state() {
    return this.vmm_compress_state;
  }
  public void setVmm_compress_state(int vmm_compress_state) {
    this.vmm_compress_state = vmm_compress_state;
  }
  public byte[] getSecretKey() {
    if (null != this.secretKey)
    {
      return (byte[])this.secretKey.clone();
    }
    byte[] tmp = null;
    return tmp;
  }
  public void setSecretKey(byte[] secretKey) {
    if (secretKey != null) {
      this.secretKey = (byte[])secretKey.clone();
    }
    else {
      this.secretKey = null;
    } 
  }
  public byte[] getSecretIvBMC() {
    return (byte[])this.secretIv.clone();
  }
  public void setSecretIvBMC(byte[] secretIv) {
    if (secretIv != null) {
      this.secretIv = (byte[])secretIv.clone();
    }
    else {
      this.secretIv = null;
    } 
  }
  private boolean cdromReconnState = false;
  public boolean getCdromReconnState() {
    return this.cdromReconnState;
  }
  public void setCdromReconnState(boolean state) {
    this.cdromReconnState = state;
  }
  private boolean floppyReconnState = false;
  public boolean getFloppyReconnState() {
    return this.floppyReconnState;
  }
  public void setFloppyReconnState(boolean state) {
    this.floppyReconnState = state;
  }
  public VMConsole() {
    this.consoleState = 0;
    this.cdromState = 0;
    this.floppyState = 0;
  }
  public void creatVMLink(int type, String serverIPAddress, int serverPort, byte[] certifyID, String devicePath, int srcType, boolean isWriteProtect, boolean bCodeKeyNego, byte[] vmm_salt, String localDirName, Map<Long, UDFExtendFile> memoryStruct, ResourceUtil util, String hmac, int iter) throws NoSuchAlgorithmException, InvalidKeySpecException {
    try {
      createCommon(type, serverIPAddress, serverPort, certifyID);
      if (null != devicePath)
      {
        if (1 == type && this.floppyState != 4) {
          this.floppyErrCode = 0;
          this.floppyState = 1;
          if (srcType == 1) {
            this.floppyType = 2;
            this.floppy = (FloppyDriver)new FloppyImage(devicePath, true);
          }
          else {
            this.floppyType = 1;
            this.floppy = (FloppyDriver)new FloppyDevice(devicePath, this.timerTask);
          } 
          this.floppy.setWriteProtect(isWriteProtect);
        }
        else if (2 == type && this.cdromState != 4) {
          this.cdromErrCode = 0;
          this.cdromState = 1;
          if (srcType != 0) {
            File file = new File(devicePath);
            if (!file.exists())
            {
              throw new VMException(336);
            }
            if (srcType == 1)
            {
              this.cdromType = 2;
              this.cdrom = (CDROMDriver)new CDROMImage(devicePath, true);
            }
            else if (srcType == 2)
            {
              this.cdromType = 3;
              this.cdrom = (CDROMDriver)new CDROMLocalDir(devicePath, true, localDirName, memoryStruct, util);
              this.cdrom.setLocalDirName(localDirName);
              this.cdrom.setMemoryStructMap(memoryStruct);
            }
          }
          else {
            this.cdromType = 1;
            this.cdrom = (CDROMDriver)new CDROMDevice(devicePath);
          } 
        } 
      }
      if (null == this.floppy && null == this.cdrom)
      {
        throw new VMException(301);
      }
      createSecretCertifyCode(certifyID, bCodeKeyNego, vmm_salt, hmac, iter);
      sentCertifyCode(type);
      sentVirtualCommand(type);
    }
    catch (VMException ve) {
      errorProcess(type, ve.getKey());
    }
    catch (IOException ie) {
      errorProcess(type, 103);
    } 
  }
  private void createCommon(int type, String serverIP, int serverPort, byte[] certifyID) throws VMException, IOException {
    if (0 != this.consoleState) {
      return;
    }
    commonInit();
    this.errCode = 0;
    this.cdromErrCode = 0;
    this.floppyErrCode = 0;
    this.consoleState = 1;
    if (2 == type) {
      this.cdromState = 1;
    }
    else if (1 == type) {
      this.floppyState = 1;
    } 
    connect(serverIP, serverPort);
    this.receiver = (Receiver)new CommunicationReceiver(this.socket);
    this.sender = new CommunicationSender(this.socket, this);
    this.timerTask = new VMTimerTask(this.sender, this);
    this.sender.enableHeartbit(this.timerTask);
    this.processor = new ProtocolProcessor(this.receiver, this);
    this.processor.enableHeartbit(this.timerTask);
    this.processorThread = new Thread((Runnable)this.processor);
    this.processorThread.setName("Protocol Processor");
    this.processorThread.start();
    this.senderThread = new Thread((Runnable)this.sender);
    this.senderThread.setName("Sender");
    this.senderThread.start();
  }
  private void createSecretCertifyCode(byte[] certifyID, boolean bCodeKeyNego, byte[] vmm_salt, String hmac, int iter) throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] completeKey = null;
    char[] chararray = null;
    byte[] tempBuff = new byte[16];
    try {
      String str = new String(certifyID, "UTF-8");
      chararray = str.toCharArray();
    }
    catch (UnsupportedEncodingException e) {
      LoggerUtil.error("UnsupportedEncoding");
      LoggerUtil.error(e.getClass().getName());
    } 
    completeKey = AESHandler.getvmmcodekey(chararray, 56, vmm_salt, hmac, iter);
    this.sessionid = new byte[24];
    System.arraycopy(completeKey, 0, this.sessionid, 0, 24);
    System.arraycopy(completeKey, 24, tempBuff, 0, 16);
    setSecretKey(tempBuff);
    System.arraycopy(completeKey, 40, tempBuff, 0, 16);
    setSecretIvBMC(tempBuff);
  }
  private byte[] getSessionID() {
    return this.sessionid;
  }
  private byte[] getLocalIP() {
    InetAddress addr = null;
    byte[] ipAddr = null;
    if (null == this.socket) {
      TestPrint.println(3, "socket is null.");
      return ipAddr;
    } 
    addr = this.socket.getLocalAddress();
    ipAddr = addr.getAddress();
    return ipAddr;
  }
  private void sentCertifyCode(int type) {
    byte[] ip = getLocalIP();
    if (null == ip) {
      TestPrint.println(3, "ip is null.");
      return;
    } 
    if (this.consoleState == 1) {
      byte[] pack = ProtocolProcessor.connectPak(getSessionID(), ip, 
          ResourceUtil.getConfigItem("com.huawei.vm.console.config.version"));
      this.consoleState = 2;
      if (2 == type) {
        this.cdromState = 2;
      }
      else if (1 == type) {
        this.floppyState = 2;
      } 
      this.consoleType = type;
      this.tempTask = new ConsoleCertifyTimerTask(this);
      if (null == this.timer)
      {
        this.timer = new Timer("vmm sentCertifyCode");
      }
      this.timer.schedule(this.tempTask, 10000L);
      this.sender.sendImmediate(pack, pack.length);
    } 
  }
  private void sentVirtualCommand(int type) {
    byte[] pack = null;
    if (2 == type && 4 == this.consoleState && 1 == this.cdromState && 0 != this.cdromType) {
      createSFFProcessor();
      pack = ProtocolProcessor.devicesPak(2);
      this.cdromState = 3;
      this.consoleType = type;
      this.tempTask = new CdromStateTimerTask(this);
      if (null == this.timer)
      {
        this.timer = new Timer("cdrom sentVirtualCommand");
      }
      this.timer.schedule(this.tempTask, 10000L);
      this.sender.sendImmediate(pack, 12);
    }
    else if (1 == type && 4 == this.consoleState && 1 == this.floppyState && 0 != this.floppyType) {
      createUFIProcessor();
      pack = ProtocolProcessor.devicesPak(1);
      this.floppyState = 3;
      this.consoleType = type;
      this.tempTask = new FloppyStateTimerTask(this);
      if (null == this.timer)
      {
        this.timer = new Timer("floppy sentVirtualCommand");
      }
      this.timer.schedule(this.tempTask, 10000L);
      this.sender.sendImmediate(pack, 12);
    } 
  }
  public void processAck(int ackCode) {
    if (2 == this.consoleState && 0 == ackCode) {
      this.tempTask.cancel();
      byte[] pack = null;
      if (0 != this.cdromType) {
        createSFFProcessor();
        pack = ProtocolProcessor.devicesPak(2);
        this.cdromState = 3;
      }
      else {
        createUFIProcessor();
        pack = ProtocolProcessor.devicesPak(1);
        this.floppyState = 3;
      } 
      this.consoleState = 3;
      this.tempTask = new ConsoleStateTimerTask(this);
      if (null == this.timer)
      {
        this.timer = new Timer("vmm processAck");
      }
      this.timer.schedule(this.tempTask, 10000L);
      this.sender.sendImmediate(pack, 12);
    }
    else if ((3 == this.consoleState || 4 == this.consoleState) && 16 == ackCode) {
      if (null != this.tempTask) {
        this.tempTask.cancel();
        this.tempTask = null;
      } 
      if (3 == this.consoleState)
      {
        this.timer.schedule((TimerTask)this.timerTask, 0L, 1000L);
      }
      this.consoleState = 4;
      if (3 == this.cdromState) {
        this.cdromState = 4;
        this.cdromReconnState = true;
      } 
      if (3 == this.floppyState)
      {
        this.floppyState = 4;
        this.floppyReconnState = true;
      }
    }
    else if ((2 == this.cdromState || 2 == this.floppyState) && 49 == ackCode) {
      if (2 == this.cdromState)
      {
        errorProcess(2, 401);
      }
      else if (2 == this.floppyState)
      {
        errorProcess(1, 401);
      }
      else
      {
        errorProcess(0, this.errCode);
      }
    }
    else if (2 == this.cdromState) {
      errorProcess(2, ackCode);
    }
    else if (2 == this.floppyState) {
      errorProcess(1, ackCode);
    }
    else {
      errorProcess(0, this.errCode);
    } 
  }
  private void createSFFProcessor() {
    this.SFFProcessor = new SFF8020iProcessor(this.cdrom, this.sender);
    this.processor.setSFFArray(this.SFFProcessor.getArray());
    this.SFFThread = new Thread((Runnable)this.SFFProcessor);
    this.SFFThread.setName("SFF Processor");
    this.SFFThread.start();
  }
  private void createUFIProcessor() {
    this.UFIProcessor = new UFIProcessor(this.floppy, this.sender);
    this.processor.setUFIArray(this.UFIProcessor.getArray());
    this.UFIThread = new Thread((Runnable)this.UFIProcessor);
    this.UFIThread.setName("UFI Processor");
    this.UFIThread.start();
  }
  public void closeVM(int vmType, int closeReason) {
    switch (vmType) {
      case 0:
        errorProcess(0, closeReason);
        break;
      case 2:
        errorProcess(2, closeReason);
        break;
      case 1:
        errorProcess(1, closeReason);
        break;
    } 
  }
  private void setErrCode(int type, int errCode) {
    if (0 == type)
    {
      this.errCode = errCode;
    }
    if (2 == type)
    {
      this.cdromErrCode = errCode;
    }
    if (1 == type)
    {
      this.floppyErrCode = errCode;
    }
  }
  public void errorProcess(int type, int errCode) {
    synchronized (this.lock) {
      int switcher = this.consoleState;
      if (type == 0) {
        switcher = this.consoleState;
      }
      else if (type == 2) {
        switcher = this.cdromState;
      }
      else if (type == 1) {
        switcher = this.floppyState;
      } 
      switch (switcher) {
        case 1:
          switch (errCode) {
            case 110:
            case 220:
            case 223:
            case 253:
            case 301:
            case 320:
            case 321:
            case 326:
            case 327:
            case 335:
              setErrCode(type, errCode);
              destoryVMLink(type);
              break;
            case 102:
            case 103:
            case 104:
            case 105:
            case 210:
              setErrCode(type, errCode);
              setErrCode(0, errCode);
              destoryVMLink(0);
              break;
          } 
          break;
        case 2:
          switch (errCode) {
            case 335:
              setErrCode(type, errCode);
              destoryVMLink(type);
              break;
            case 1:
            case 2:
            case 34:
            case 35:
            case 101:
            case 102:
            case 121:
              setErrCode(type, errCode);
              setErrCode(0, errCode);
              destoryVMLink(0);
              break;
            case 401:
              if (2 == type) {
                setErrCode(0, errCode);
                setErrCode(type, errCode);
                destoryVMLink(type); break;
              } 
              if (1 == type) {
                setErrCode(0, errCode);
                setErrCode(type, errCode);
                destoryVMLink(type);
              } 
              break;
          } 
          break;
        case 3:
          switch (errCode) {
            case 122:
            case 335:
              setErrCode(type, errCode);
              destoryVMLink(type);
              break;
            case 17:
            case 34:
            case 35:
            case 101:
            case 102:
              setErrCode(type, errCode);
              setErrCode(0, errCode);
              destoryVMLink(0);
              break;
          } 
          break;
        case 4:
          switch (errCode) {
            case 34:
            case 35:
            case 101:
            case 102:
            case 123:
              setErrCode(type, errCode);
              setErrCode(0, errCode);
              destoryVMLink(0);
              break;
            case 335:
              setErrCode(type, errCode);
              destoryVMLink(type);
              break;
          } 
          break;
      } 
    } 
  }
  public void destoryVMLink(int type) {
    synchronized (this.lock) {
      if (0 == this.consoleState) {
        return;
      }
      if (0 == type || (1 == type && 0 == this.cdromState) || (2 == type && 0 == this.floppyState)) {
        if (4 == this.consoleState && 0 == this.errCode) {
          this.sender.sendImmediate(ProtocolProcessor.vmLinkClosePak((byte)0, (byte)0), 12);
          this.cdromReconnState = false;
          this.floppyReconnState = false;
        } 
        try {
          int i = 0;
          while (i < 1)
          {
            this.lock.wait(2L);
            i++;
          }
        }
        catch (InterruptedException e) {
          LoggerUtil.error(e.getClass().getName());
        } 
        Base.setBvmmCodeKeyNego(false);
        destroyCommonConn();
        if ((0 == type || 2 == type) && 4 == this.cdromState)
        {
          destroyCdromConn();
        }
        if ((0 == type || 1 == type) && 4 == this.floppyState)
        {
          destroyFloppyConn();
        }
        initAll();
      }
      else if (1 == type && 4 == this.cdromState) {
        if (4 == this.floppyState && 0 == this.errCode) {
          this.sender.sendImmediate(ProtocolProcessor.vmLinkClosePak((byte)1, (byte)0), 12);
          this.floppyReconnState = false;
        } 
        destroyFloppyConn();
        floppyInit();
      }
      else if (2 == type && 4 == this.floppyState) {
        if (4 == this.cdromState && 0 == this.errCode) {
          this.sender.sendImmediate(ProtocolProcessor.vmLinkClosePak((byte)2, (byte)0), 12);
          this.cdromReconnState = false;
        } 
        destroyCdromConn();
        cdromInit();
      } 
    } 
  }
  private void destroyCommonConn() {
    if (null != this.processorThread) {
      this.processor.setExit();
      this.processorThread.interrupt();
    } 
    if (null != this.senderThread) {
      this.sender.setExit();
      this.senderThread.interrupt();
    } 
    try {
      this.socket.close();
    }
    catch (IOException ie) {
      LoggerUtil.error(ie.getClass().getName());
    } 
  }
  private void destroyCdromConn() {
    if (null != this.SFFProcessor) {
      this.SFFProcessor.setExit();
      this.SFFThread.interrupt();
    } 
  }
  private void destroyFloppyConn() {
    if (null != this.UFIProcessor) {
      this.UFIProcessor.setExit();
      this.UFIThread.interrupt();
    } 
  }
  public void changeFloppyImg(String imagePath) throws VMException {
    if (null != this.floppy)
    {
      this.floppy.changeDisk(imagePath);
    }
  }
  public void changeCDROMImg(String localDirName, Map<Long, UDFExtendFile> memoryStruct, String imagePath) throws VMException {
    if (null != this.cdrom)
    {
      if (this.cdrom instanceof CDROMLocalDir) {
        this.cdrom.changeLocalDirDisk(localDirName, memoryStruct, imagePath);
      }
      else {
        this.cdrom.changeDisk(imagePath);
      } 
    }
  }
  private void connect(String serverIP, int serverPort) throws VMException {
    if (null == serverIP || 0 > serverPort)
    {
      throw new VMException(110);
    }
    try {
      this.socket = new Socket();
      this.socket.connect(new InetSocketAddress(serverIP, serverPort), 20000);
      if (!this.socket.isConnected())
      {
        throw new VMException(103);
      }
      try {
        this.socket.setTcpNoDelay(true);
      }
      catch (IOException ioe) {
        LoggerUtil.error(ioe.getClass().getName());
      }
    } catch (SocketTimeoutException se) {
      throw new VMException(105);
    }
    catch (IOException ie) {
      throw new VMException(103);
    } 
  }
  private void initAll() {
    commonInit();
    floppyInit();
    cdromInit();
  }
  private void commonInit() {
    this.processor = null;
    this.processorThread = null;
    this.receiver = null;
    if (null != this.timerTask) {
      this.timerTask.cancel();
      this.timerTask = null;
    } 
    if (null != this.tempTask) {
      this.tempTask.cancel();
      this.tempTask = null;
    } 
    if (null != this.timer) {
      this.timer.cancel();
      this.timer = null;
    } 
    this.sender = null;
    this.senderThread = null;
    this.socket = null;
    this.consoleState = 0;
    this.cdromState = 0;
    this.floppyState = 0;
  }
  private void floppyInit() {
    this.UFIThread = null;
    this.UFIProcessor = null;
    if (null != this.floppy) {
      try {
        this.floppy.close();
      }
      catch (VMException e) {
        LoggerUtil.error(e.getClass().getName());
      } 
      this.floppy = null;
    } 
    this.floppyType = 0;
    this.floppyState = 0;
  }
  private void cdromInit() {
    this.SFFThread = null;
    this.SFFProcessor = null;
    if (null != this.cdrom) {
      try {
        this.cdrom.close();
      }
      catch (VMException e) {
        LoggerUtil.error(e.getClass().getName());
      } 
      this.cdrom = null;
    } 
    this.cdromType = 0;
    this.cdromState = 0;
  }
  public int getState(int type) {
    int result = 0;
    if (0 == type) {
      result = this.errCode;
    }
    else if (1 == type) {
      result = this.floppyErrCode;
    }
    else if (2 == type) {
      result = this.cdromErrCode;
    } 
    return result;
  }
  public int getConsoleState() {
    return this.consoleState;
  }
  public int getFloppyState() {
    return this.floppyState;
  }
  public int getCdRomState() {
    return this.cdromState;
  }
  public void setWriteProtect(boolean isWriteProtect) {
    this.floppy.setWriteProtect(isWriteProtect);
  }
  public int getFloppyType() {
    return this.floppyType;
  }
  public int getCdromType() {
    return this.cdromType;
  }
  public void setResouceUtil(ResourceUtil resouceUtil) {}
  public CDROMDriver getCdrom() {
    return this.cdrom;
  }
  public int getCONSOLE_DEVICE() {
    return 3;
  }
  public int getCdromStateBMC() {
    return this.cdromState;
  }
  public int getConsoleType() {
    return this.consoleType;
  }
}
