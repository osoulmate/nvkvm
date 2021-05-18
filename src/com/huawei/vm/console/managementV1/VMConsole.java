package com.huawei.vm.console.managementV1;
import com.huawei.vm.console.communicationV1.CommunicationReceiver;
import com.huawei.vm.console.communicationV1.CommunicationSender;
import com.huawei.vm.console.communicationV1.ProtocolProcessor;
import com.huawei.vm.console.communicationV1.Receiver;
import com.huawei.vm.console.communicationV1.VMTimerTask;
import com.huawei.vm.console.processV1.SFF8020iProcessor;
import com.huawei.vm.console.processV1.UFIProcessor;
import com.huawei.vm.console.storageV1.impl.CDROMDevice;
import com.huawei.vm.console.storageV1.impl.CDROMDriver;
import com.huawei.vm.console.storageV1.impl.CDROMImage;
import com.huawei.vm.console.storageV1.impl.CDROMLocalDir;
import com.huawei.vm.console.storageV1.impl.FloppyDevice;
import com.huawei.vm.console.storageV1.impl.FloppyDriver;
import com.huawei.vm.console.storageV1.impl.FloppyImage;
import com.huawei.vm.console.utilsV1.ResourceUtil;
import com.huawei.vm.console.utilsV1.TestPrint;
import com.huawei.vm.console.utilsV1.VMException;
import com.kvmV1.AESHandler;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.UDFExtendFile;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
  public static final int CONSOLE_DEVICE = 3;
  public static final int DEVICE_TIMEOUT = 10000;
  public static final int CONSOLE_ACTIVE = 4;
  public static final int CONNECT_TIMEOUT = 20000;
  public static final int CONSOLE_OK = 0;
  private Socket socket;
  private Timer timer;
  private VMTimerTask timerTask;
  private TimerTask tempTask;
  private Receiver receiver;
  private CommunicationSender sender;
  private Thread senderThread;
  private ProtocolProcessor processor;
  private Thread processorThread;
  private UFIProcessor UFIProcessor;
  private Thread UFIThread;
  private SFF8020iProcessor SFFProcessor;
  private Thread SFFThread;
  private FloppyDriver floppy;
  private CDROMDriver cdrom;
  private ResourceUtil resouceUtil;
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
  boolean bSecretVMM = false;
  private byte[] secretKey = null;
  private byte[] secretIV = null;
  private byte[] sessionid = null;
  private static final int SECRETKEY_LEN = 16;
  private static final int SECRETIV_LEN = 16;
  private static final int SECRET_SESSIONID_LEN = 24;
  public VMConsole() {
    this.consoleState = 0;
    this.cdromState = 0;
    this.floppyState = 0;
  }
  public void creatVMLink(int type, String serverIPAddress, int serverPort, byte[] certifyID, byte[] salt, boolean bCodeKeyNego, boolean bSecret, String devicePath, int srcType, boolean isWriteProtect, String localDirName, Map<Long, UDFExtendFile> memoryStruct, ResourceUtil util) {
    try {
      createCommon(type, serverIPAddress, serverPort);
      if (null != devicePath)
      {
        if (1 == type && this.floppyState != 4) {
          TestPrint.println(1, "VMConsole : Create floppy=" + devicePath);
          this.floppyErrCode = 0;
          this.floppyState = 1;
          if (srcType == 1) {
            this.floppyType = 2;
            this.floppy = (FloppyDriver)new FloppyImage(devicePath, true);
            TestPrint.println(1, "VM Console : create link -- floppy" + this.floppy);
          }
          else {
            this.floppyType = 1;
            this.floppy = (FloppyDriver)new FloppyDevice(devicePath, this.timerTask);
          } 
          this.floppy.setWriteProtect(isWriteProtect);
        }
        else if (2 == type && this.cdromState != 4) {
          TestPrint.println(1, "VMConsole : Create cdrom");
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
      this.bSecretVMM = bSecret;
      createSecretCertifyCode(certifyID, salt, bCodeKeyNego);
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
  private void createCommon(int type, String serverIP, int serverPort) throws VMException, IOException {
    if (0 != this.consoleState) {
      return;
    }
    TestPrint.println(1, "VMConsole : enter common init");
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
    TestPrint.println(1, "VMConsole : exit common init");
  }
  private void createSecretCertifyCode(byte[] certifyID, byte[] salt, boolean bCodeKeyNego) {
    int certifyIDLen = certifyID.length;
    if (bCodeKeyNego == true) {
      byte[] completeKey = null;
      StringBuilder strBuilder = new StringBuilder(certifyIDLen);
      for (int i = 0; i < certifyIDLen; i++)
      {
        strBuilder.append((char)certifyID[i]);
      }
      try {
        completeKey = AESHandler.generateStoredPasswordHash(strBuilder.toString().toCharArray(), 56, salt);
      }
      catch (Exception ex) {
        TestPrint.println(1, "VMConsole : create secret certify code failed");
      } 
      this.secretKey = new byte[16];
      this.secretIV = new byte[16];
      this.sessionid = new byte[24];
      System.arraycopy(completeKey, 0, this.sessionid, 0, 24);
      System.arraycopy(completeKey, 24, this.secretKey, 0, 16);
      System.arraycopy(completeKey, 40, this.secretIV, 0, 16);
    }
    else {
      this.sessionid = new byte[certifyIDLen];
      System.arraycopy(certifyID, 0, this.sessionid, 0, certifyIDLen);
    } 
  }
  private byte[] getSessionID() {
    return this.sessionid;
  }
  public byte[] getSecretKey() {
    return this.secretKey;
  }
  public byte[] getSecretIV() {
    return this.secretIV;
  }
  private byte[] getLocalIP() {
    InetAddress addr = null;
    byte[] ipAddr = null;
    if (this.socket != null) {
      addr = this.socket.getLocalAddress();
      ipAddr = addr.getAddress();
    } 
    return ipAddr;
  }
  private void sentCertifyCode(final int type) {
    if (this.consoleState == 1) {
      TestPrint.println(1, "enter sentCertifyCode");
      byte[] ip = getLocalIP();
      byte[] pack = ProtocolProcessor.connectPak(getSessionID(), ip, ResourceUtil.getConfigItem("com.huawei.vm.console.config.version"));
      this.consoleState = 2;
      if (2 == type) {
        this.cdromState = 2;
      }
      else if (1 == type) {
        this.floppyState = 2;
      } 
      this.tempTask = new TimerTask()
        {
          public void run()
          {
            if (2 == VMConsole.this.consoleState)
            {
              VMConsole.this.errorProcess(type, 121);
            }
            cancel();
          }
        };
      if (null == this.timer)
      {
        this.timer = new Timer("vmm sentCertifyCode");
      }
      this.timer.schedule(this.tempTask, 10000L);
      this.sender.sendImmediate(pack, pack.length);
      TestPrint.println(1, "exit sentCertifyCode");
    } 
  }
  private void sentVirtualCommand(final int type) {
    byte[] pack = null;
    if (2 == type && 4 == this.consoleState && 1 == this.cdromState && 0 != this.cdromType) {
      TestPrint.println(1, "enter sentVirtualCommand cdrom");
      createSFFProcessor();
      pack = ProtocolProcessor.devicesPak(2);
      this.cdromState = 3;
      this.tempTask = new TimerTask()
        {
          public void run()
          {
            if (3 == VMConsole.this.cdromState)
            {
              VMConsole.this.errorProcess(type, 122);
            }
            cancel();
          }
        };
      if (null == this.timer)
      {
        this.timer = new Timer("cdrom sentVirtualCommand");
      }
      this.timer.schedule(this.tempTask, 10000L);
      this.sender.sendImmediate(pack, 12);
    }
    else if (1 == type && 4 == this.consoleState && 1 == this.floppyState && 0 != this.floppyType) {
      TestPrint.println(1, "enter sentVirtualCommand floppy");
      createUFIProcessor();
      pack = ProtocolProcessor.devicesPak(1);
      this.floppyState = 3;
      this.tempTask = new TimerTask()
        {
          public void run()
          {
            if (3 == VMConsole.this.floppyState)
            {
              VMConsole.this.errorProcess(type, 122);
            }
            cancel();
          }
        };
      if (null == this.timer)
      {
        this.timer = new Timer("floppy sentVirtualCommand");
      }
      this.timer.schedule(this.tempTask, 10000L);
      this.sender.sendImmediate(pack, 12);
      TestPrint.println(1, "sent floppy VirtualCommand=========" + pack.length);
    } 
  }
  public void processAck(int ackCode) {
    if (2 == this.consoleState && 0 == ackCode) {
      this.tempTask.cancel();
      byte[] pack = null;
      if (0 != this.cdromType) {
        TestPrint.println(1, "enter processAck :virtual cdrom");
        createSFFProcessor();
        pack = ProtocolProcessor.devicesPak(2);
        this.cdromState = 3;
      }
      else {
        TestPrint.println(1, "enter processAck :virtual floppy");
        createUFIProcessor();
        pack = ProtocolProcessor.devicesPak(1);
        this.floppyState = 3;
      } 
      this.consoleState = 3;
      this.tempTask = new TimerTask()
        {
          public void run()
          {
            if (3 == VMConsole.this.consoleState)
            {
              VMConsole.this.errorProcess(0, 122);
            }
            cancel();
          }
        };
      if (null == this.timer)
      {
        this.timer = new Timer("vmm processAck");
      }
      this.timer.schedule(this.tempTask, 10000L);
      this.sender.sendImmediate(pack, 12);
    }
    else if ((3 == this.consoleState || 4 == this.consoleState) && 16 == ackCode) {
      TestPrint.println(1, "come in ACK_DEVICE_CREAT,ackCode==" + ackCode);
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
        TestPrint.println(1, "this.cdromState = CONSOLE_ACTIVE;");
      } 
      if (3 == this.floppyState)
      {
        this.floppyState = 4;
        TestPrint.println(1, "this.floppyState = CONSOLE_ACTIVE;");
      }
    }
    else if ((2 == this.cdromState || 2 == this.floppyState) && 49 == ackCode) {
      TestPrint.print(1, "ProtocolCode.CN_EXIST==ackCode");
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
    else {
      TestPrint.println(1, "enter processAck else");
      if (2 == this.cdromState) {
        errorProcess(2, ackCode);
      }
      else if (2 == this.floppyState) {
        errorProcess(1, ackCode);
      }
      else {
        errorProcess(0, this.errCode);
      } 
    } 
  }
  private void createSFFProcessor() {
    TestPrint.println(1, "enter createSFFProcessor");
    this.SFFProcessor = new SFF8020iProcessor(this.cdrom, this.sender);
    this.processor.setSFFArray(this.SFFProcessor.getArray());
    this.SFFProcessor.setSecretKey(this.bSecretVMM, this.secretKey, this.secretIV);
    this.SFFThread = new Thread((Runnable)this.SFFProcessor);
    this.SFFThread.setName("SFF Processor");
    this.SFFThread.start();
    TestPrint.println(1, "exit createSFFProcessor");
  }
  private void createUFIProcessor() {
    TestPrint.println(1, "enter createUFIProcessor");
    this.UFIProcessor = new UFIProcessor(this.floppy, this.sender);
    this.UFIProcessor.setSecretKey(this.bSecretVMM, this.secretKey, this.secretIV);
    this.processor.setUFIArray(this.UFIProcessor.getArray());
    this.UFIThread = new Thread((Runnable)this.UFIProcessor);
    this.UFIThread.setName("UFI Processor");
    this.UFIThread.start();
    TestPrint.println(1, "exit createUFIProcessor");
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
  public synchronized void errorProcess(int type, int errCode) {
    TestPrint.println(3, "VMConsole : An error happend:" + errCode + ":" + this.resouceUtil.getErrMessage(errCode));
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
          case 336:
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
  public void destoryVMLink(int type) {
    synchronized (this) {
      if (0 == this.consoleState) {
        return;
      }
      TestPrint.println(1, "VMConsole : Begin destroy!");
      if (0 == type || (1 == type && 0 == this.floppyState) || (2 == type && 0 == this.cdromState)) {
        if (4 == this.consoleState && 0 == this.errCode) {
          TestPrint.println(1, "VMConsole : Begin destroy connect!");
          this.sender.sendImmediate(ProtocolProcessor.vmLinkClosePak((byte)0, (byte)0), 12);
        } 
        destroyCommonConn();
        if ((0 == type || 2 == type) && 4 == this.cdromState) {
          TestPrint.println(1, "VMConsole : Begin destroy cdrom!");
          destroyCdromConn();
        } 
        if ((0 == type || 1 == type) && 4 == this.floppyState) {
          TestPrint.println(1, "VMConsole : Begin destroy floppy!");
          destroyFloppyConn();
        } 
        initAll();
      }
      else if (1 == type && 4 != this.floppyState && 0 != this.floppyState) {
        if (4 != this.cdromState)
        {
          destroyCommonConn();
          initAll();
        }
        else
        {
          floppyInit();
        }
      }
      else if (1 == type && 4 == this.floppyState) {
        if (4 == this.floppyState && 0 == this.errCode) {
          TestPrint.println(1, "VMConsole : Begin destroy floppy only!");
          this.sender.sendImmediate(ProtocolProcessor.vmLinkClosePak((byte)1, (byte)1), 12);
        } 
        destroyFloppyConn();
        floppyInit();
      }
      else if (2 == type && 4 == this.cdromState) {
        if (4 == this.cdromState && 0 == this.errCode) {
          TestPrint.println(1, "VMConsole : Begin destroy cdrom only!");
          this.sender.sendImmediate(ProtocolProcessor.vmLinkClosePak((byte)2, (byte)2), 12);
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
      TestPrint.println(1, "VMConsole : Socket close error!");
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
        TestPrint.println(1, "VMConsole : It seems can not set TCP_NODELAY");
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
    TestPrint.println(1, "VMConsole : Finished init.Console is idle");
  }
  private void floppyInit() {
    this.UFIThread = null;
    this.UFIProcessor = null;
    if (null != this.floppy) {
      try {
        this.floppy.close();
        TestPrint.println(1, "VMVconole ---FLOPPY closed");
      }
      catch (VMException e) {
        TestPrint.println(1, "VMConsole : Floppy close error.");
      } 
      this.floppy = null;
    } 
    this.floppyType = 0;
    this.floppyState = 0;
    TestPrint.println(1, "VMConsole : floppy init -- Finished init.floppyState is idle");
  }
  private void cdromInit() {
    this.SFFThread = null;
    this.SFFProcessor = null;
    if (null != this.cdrom) {
      try {
        this.cdrom.close();
        TestPrint.println(1, "VMVconole ---CDROM closed");
      }
      catch (VMException e) {
        TestPrint.println(1, "VMConsole : CDROM close error.");
      } 
      this.cdrom = null;
    } 
    this.cdromType = 0;
    this.cdromState = 0;
    TestPrint.println(1, "VMConsole : cdrom init -- Finished init.cdromState is idle");
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
  public void setResouceUtil(ResourceUtil resouceUtil) {
    this.resouceUtil = resouceUtil;
  }
  public CDROMDriver getCdrom() {
    return this.cdrom;
  }
  public void setCdrom(CDROMDriver cdrom) {
    this.cdrom = cdrom;
  }
}
