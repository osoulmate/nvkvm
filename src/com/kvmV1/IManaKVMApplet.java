package com.kvmV1;
import com.huawei.vm.console.utilsV1.ResourceUtil;
import com.library.LoggerUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.Timer;
public class IManaKVMApplet
  extends JApplet
{
  private static final long serialVersionUID = 1L;
  private KVMInterface kvmInterface = null;
  private static boolean isStart = false;
  private Thread timeHeart = null;
  private boolean conn = true;
  private final long i = Long.valueOf("4294967296").intValue();
  private static final int MastInt = 2147483647;
  public String local = "ZH";
  public String strIP = "";
  public static String protocol = "TCP";
  public volatile ClientSocketCommunity clientSocket = null;
  public KVMUtil kvmUtil = null;
  public Client client = null;
  public PackData packData = null;
  public UnPackData unPackData = null;
  public Base base = null;
  public boolean started = false;
  private String secureKvm = "";
  private String productType = "";
  private String verifyValue = "";
  private String mmVerifyValue = "";
  private String vmmPortNum = "";
  private String portNum = "";
  private String privilegeValue = "";
  private String IPA = "";
  private String IPB = "";
  private boolean appKvmFlag = false;
  public void setAppKvmFlag() {
    this.appKvmFlag = true;
  }
  public void getParaFromLocal(String[] args) {
    this.local = args[0];
    this.secureKvm = args[1];
    this.productType = args[2];
    this.verifyValue = args[3];
    this.mmVerifyValue = args[4];
    this.portNum = args[5];
    this.vmmPortNum = args[6];
    this.privilegeValue = args[7];
    this.IPA = args[8];
    this.IPB = args[8];
  }
  public void init() {
    try {
      String arch = System.getProperty("sun.arch.data.model");
      System.out.println("java version digit = " + arch);
      if (arch.equals("64")) {
        ResourceUtil.CONFIG_VM_LIBARY = "com.huawei.vm.console.config.library.x64";
        Base.MOUSE_LIB = "KVMMouseDisPlace_iMana_x64";
        Base.KEYBOARD_LIB = "KVMKeyboard_iMana_x64";
      }
      else {
        ResourceUtil.CONFIG_VM_LIBARY = "com.huawei.vm.console.config.library";
        Base.MOUSE_LIB = "KVMMouseDisPlace_iMana";
        Base.KEYBOARD_LIB = "KVMKeyboard_iMana";
      } 
      CopyFileToLocal.cleanLib();
      CopyFileToLocal.libarayPrepare();
    }
    catch (AccessControlException ae) {}
    this.base = new Base();
    this.kvmUtil = new KVMUtil();
    if (!this.appKvmFlag) {
      this.local = getParameter("local");
    }
    if (this.local == null)
      this.local = "EN"; 
    Base.local = this.local;
    this.kvmUtil.resourcePath = "com.kvm.resource.KVMResource";
    if (!this.appKvmFlag) {
      this.secureKvm = getParameter("securekvm");
    }
    if (this.secureKvm == null) {
      Base.securekvm = false;
      Base.securevmm = false;
    }
    else if (this.secureKvm.trim().equals(new String("1"))) {
      Base.securekvm = true;
      Base.securevmm = false;
    }
    else if (this.secureKvm.trim().equals(new String("2"))) {
      Base.securekvm = false;
      Base.securevmm = true;
    }
    else if (this.secureKvm.trim().equals(new String("3"))) {
      Base.securekvm = true;
      Base.securevmm = true;
    }
    else {
      Base.securekvm = false;
      Base.securevmm = false;
    } 
    this.kvmUtil.setiWindosFocus(0);
  }
  private void beforeStart() {
    while (this.clientSocket != null) {
      try {
        Thread.sleep(500L);
      }
      catch (InterruptedException e) {
        Debug.printExc(e.getMessage());
      } 
    } 
    this.client = new Client();
    this.packData = new PackData();
    this.unPackData = new UnPackData();
    this.clientSocket = new ClientSocketCommunity();
    long typeData = 0L;
    if (!this.appKvmFlag) {
      typeData = (getParameter("typeData") == null) ? Long.parseLong("4294967297") : Long.parseLong(getParameter("typeData"));
    }
    else {
      typeData = Long.parseLong("4294967297");
    } 
    Base.typeData = typeData;
    if (!this.appKvmFlag) {
      this.productType = getParameter("productType");
    }
    this.kvmInterface = new KVMInterface(this.kvmUtil, this.client, this.packData, this.clientSocket, this.base);
    this.kvmInterface.setProductType(this.productType);
    this.client.setKvmInterface(this.kvmInterface);
    this.clientSocket.setKvmInterface(this.kvmInterface);
    this.clientSocket.setUnPackData(this.unPackData);
    this.packData.setKvmInterface(this.kvmInterface);
    this.kvmUtil.setKvmInterface(this.kvmInterface);
    this.kvmUtil.setUnPack(this.unPackData);
    this.kvmUtil.setImageData(new byte[this.base.imageWidth * this.base.imageHeight]);
  }
  private boolean comInKVM() {
    isStart = true;
    String ipA = "";
    String ipB = "";
    String port = "";
    if (!this.appKvmFlag) {
      ipA = getParameter("IPA");
      ipB = getParameter("IPB");
      port = getParameter("port");
    }
    else {
      ipA = this.IPA;
      ipB = this.IPB;
      port = this.portNum;
    } 
    this.base.parameterIP = (ipA != null) ? ipA.trim() : null;
    this.base.parameterPort = (port != null) ? Integer.valueOf(port.trim()).intValue() : 2198;
    if (this.kvmInterface.getBladeSize() > 1) {
      try {
        this.client.socket = KVMUtil.getProxySocket(this.base.parameterIP, 80, this.base.parameterIP, this.base.parameterPort);
        if (null != this.client.socket)
        {
          Base.isProxy = true;
        }
      }
      catch (URISyntaxException e) {
        e.printStackTrace();
        Debug.printExc(e.getMessage());
        JOptionPane.showMessageDialog(this, this.kvmUtil.getString("Connection_failed"));
        return false;
      }
      catch (UnknownHostException e) {
        e.printStackTrace();
        Debug.printExc(e.getMessage());
        JOptionPane.showMessageDialog(this, this.kvmUtil.getString("Connection_failed"));
        return false;
      }
      catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, this.kvmUtil.getString("Connection_failed"));
        return false;
      } 
      if (Base.isProxy)
      {
        this.strIP = ipA;
      }
      else if (socketTest(ipA, port))
      {
        this.strIP = ipA;
      }
      else if (socketTest(ipB, port))
      {
        this.strIP = ipB;
      }
    }
    else {
      this.strIP = (ipA == null) ? ipB : ipA;
    } 
    if (this.strIP == null || this.strIP.equals("")) {
      JOptionPane.showMessageDialog(this, this.kvmUtil.getString("Connection_failed"));
      return false;
    } 
    String codeKey = "";
    if (!this.appKvmFlag) {
      codeKey = getParameter("verifyValue");
    }
    else {
      codeKey = this.verifyValue;
    } 
    String codeKey_new = "";
    if (codeKey.length() < 72) {
      if (codeKey.length() < 8) {
        codeKey_new = getkeyValue(8 - codeKey.length()) + codeKey + getkeyValue(64);
      }
      else {
        codeKey_new = codeKey + getkeyValue(72 - codeKey.length());
      } 
      codeKey = codeKey_new;
    } 
    long tempLong = 0L;
    try {
      byte[] tmp_byte = new byte[4];
      String tmp_hexstring = null;
      for (int i = 0; i < 4; i++) {
        tmp_hexstring = codeKey.substring(i * 2, i * 2 + 2);
        try {
          tmp_byte[i] = (byte)(Integer.parseInt(tmp_hexstring, 16) & 0xFF);
        }
        catch (NumberFormatException ex) {}
      } 
      long tmp_key = (tmp_byte[3] & 0xFF);
      tmp_key += ((tmp_byte[2] & 0xFF) << 8);
      tmp_key += ((tmp_byte[1] & 0xFF) << 16);
      tmp_key += ((tmp_byte[0] & 0xFF) << 24);
      tempLong = tmp_key;
    }
    catch (Exception e) {
      return false;
    } 
    if (tempLong > 2147483647L) {
      this.kvmInterface.codeKey = (int)(0L - this.i - tempLong);
    } else {
      try {
        this.kvmInterface.codeKey = (int)tempLong;
      }
      catch (Exception e) {
        return false;
      } 
    } 
    setNegotiateKey(codeKey);
    Base.initSessionIDAndKey(this.kvmInterface.codeKey);
    char[] mmCodeKey = null;
    if (!this.appKvmFlag) {
      mmCodeKey = getParameter("mmVerifyValue").toCharArray();
    }
    else {
      mmCodeKey = this.mmVerifyValue.toCharArray();
    } 
    if (null != mmCodeKey && !"".equals(mmCodeKey))
    {
      Base.vmmCodeKey = this.kvmInterface.codeKey;
    }
    String vmmPort = "";
    if (!this.appKvmFlag) {
      vmmPort = getParameter("vmmPort");
    }
    else {
      vmmPort = this.vmmPortNum;
    } 
    if (null == vmmPort || "".equals(vmmPort))
    {
      vmmPort = "8208";
    }
    Base.vmmPort = Integer.parseInt(vmmPort);
    String privilege = "";
    if (!this.appKvmFlag) {
      privilege = getParameter("privilege");
    }
    else {
      privilege = this.privilegeValue;
    } 
    if (null == privilege || "".equals(privilege))
    {
      privilege = "4";
    }
    Base.privilege = Integer.parseInt(privilege);
    getBladeNames(this.kvmInterface);
    if (!this.appKvmFlag && 
      getParameter("bladeFlag") != null) {
      this.kvmInterface.setBladeFlag(getParameter("bladeFlag"));
    }
    if (port == null)
    {
      port = "2198";
    }
    Client.port = Integer.parseInt(port.trim());
    try {
      this.client.address = InetAddress.getByName(this.strIP);
      Base.vmmConnIP = this.strIP;
    }
    catch (UnknownHostException e1) {
      Debug.printExc(e1.getMessage());
    } 
    return true;
  }
  private String getkeyValue(int length) {
    String tmp = "";
    for (int i = 0; i < length; i++)
    {
      tmp = tmp + "0";
    }
    return tmp;
  }
  private void setNegotiateKey(String keyvalue) {
    String negotiatePass = keyvalue;
    String saltString = null;
    if (negotiatePass != null && negotiatePass.length() >= 36) {
      Base.negotiatepass = new byte[16];
      saltString = keyvalue.substring(40, 72);
      Base.negotiateiv = new byte[16];
      String tmp_hexstring = "";
      for (int i = 0; i < 16; i++) {
        tmp_hexstring = saltString.substring(i * 2, i * 2 + 2);
        try {
          Base.negotiateiv[i] = (byte)(Integer.parseInt(tmp_hexstring, 16) & 0xFF);
        }
        catch (NumberFormatException ex) {
          ex.printStackTrace();
        } 
      } 
    } 
    Base.negotiatesalt = new byte[16];
    System.arraycopy(Base.negotiateiv, 0, Base.negotiatesalt, 0, 16);
  }
  public void start() {
    beforeStart();
    if (!comInKVM()) {
      return;
    }
    if (this.kvmInterface.getBladeSize() == 1) {
      LoggerUtil.info( "processRack: ");
      processRack();
    }
    else {
      LoggerUtil.info( "processShelf: ");
      processShelf();
    } 
    super.start();
    setStarted(true);
  }
  private void startHeart() {
    this.timeHeart = new SMMHeartTimer();
    this.timeHeart.setName("SmmHeartThread");
    this.timeHeart.start();
  }
  private void processShelf() {
    try {
      if (!Base.isProxy) {
        Debug.println("not proxy");
        Debug.println("control ip::" + this.strIP + ":::CONTROL PORT::::" + Client.port);
        this.client.socket = new Socket(this.strIP, Client.port);
      } 
      this.client.dout = new DataOutputStream(this.client.socket.getOutputStream());
      this.client.din = new BufferedInputStream(this.client.socket.getInputStream());
    }
    catch (IOException e) {
      Debug.printExc(e.getMessage());
      JOptionPane.showMessageDialog(this, this.kvmUtil.getString("Connection_failed"));
      return;
    } 
    if (this.strIP != null) {
      boolean isRight = true;
      this.clientSocket.setConn(true);
      this.clientSocket.setName("receiveThread");
      this.clientSocket.bladePresentInfo.clear();
      this.clientSocket.start();
      byte[] reqBladePresentInfo = this.packData.reqBladePresent();
      try {
        this.client.sentData(reqBladePresentInfo);
      }
      catch (KVMException ex) {
        ex.printStackTrace();
        if ("IO_ERRCODE".equals(ex.getErrCode()))
        {
          JOptionPane.showMessageDialog(this.kvmInterface.toolbar, this.kvmInterface.kvmUtil.getString("Network_interrupt_message"));
        }
      } 
      Timer timer = new Timer(1000, this.kvmUtil.taskPerformer);
      timer.start();
      while (true) {
        try {
          Thread.sleep(20L);
        }
        catch (InterruptedException e) {
          Debug.printExc(e.getMessage());
        } 
        if (this.kvmUtil.isConn) {
          timer.stop();
          this.kvmUtil.times = 0;
          break;
        } 
        if (this.kvmUtil.times < 10) {
          continue;
        }
        timer.stop();
        isRight = false;
        JOptionPane.showMessageDialog(this, this.kvmUtil.getString("no_right"));
        this.kvmUtil.times = 0;
        break;
      } 
      if (isRight) {
        this.kvmUtil.isConn = false;
        getContentPane().setLayout(new BorderLayout());
        startHeart();
        getContentPane().add(this.kvmInterface, "Center");
        this.kvmUtil.setButtonEnable(false);
        this.kvmUtil.setBladeEnable();
      }
      else {
        this.clientSocket.setConn(false);
        this.clientSocket.bladePresentInfo.clear();
        this.clientSocket = null;
      } 
    } 
  }
  private void processRack() {
    this.kvmUtil.isConn = false;
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(this.kvmInterface, "Center");
    this.kvmUtil.setButtonEnable(false);
    if (this.kvmInterface.getBladeSize() == 1) {
      ActionEvent event = new ActionEvent(this.kvmInterface.toolbar, 1, "blade1");
      this.kvmInterface.toolbar.actionPerformed(event);
    } 
  }
  private void getBladeNames(KVMInterface kvmInterface) {
    String bladeNames = "";
    if (!this.appKvmFlag) {
      bladeNames = getParameter("bladeNames");
    }
    else {
      bladeNames = null;
    } 
    if (bladeNames != null)
    {
      kvmInterface.setBladeTip(bladeNames.split("#"));
    }
    Debug.println("kvmInterface::" + bladeNames);
    kvmInterface.setBladeNames(bladeNames);
  }
  public static boolean socketTest(String ip, String portStr) {
    boolean result = false;
    if (null != ip && !"".equals(ip) && null != portStr && !"".equals(portStr)) {
      try {
        int port = Integer.valueOf(portStr).intValue();
        Socket socket = null;
        try {
          socket = new Socket(ip, port);
          result = true;
        }
        catch (Exception e) {
          result = false;
        }
        finally {
          if (null != socket) {
            try
            {
              socket.close();
            }
            catch (IOException e)
            {
              e.printStackTrace();
            }
          }
        } 
      } catch (NumberFormatException nfe) {
        Debug.printExc(nfe.getMessage());
      } 
    }
    return result;
  }
  public void stop() {
    ArrayList<String> keyList = new ArrayList<String>();
    if (this.kvmInterface != null) {
      this.kvmInterface.tabbedpane.getModel().removeChangeListener(this.kvmUtil.changeListener);
      Iterator<String> iter = this.base.threadGroup.keySet().iterator();
      if (this.kvmInterface != null)
      {
        this.kvmInterface.tabbedpane.getModel().removeChangeListener(this.kvmUtil.changeListener);
      }
      while (iter.hasNext())
      {
        keyList.add(iter.next());
      }
      int num = this.base.threadGroup.size();
      for (int i = 0; i < num; i++) {
        int bladeNO = Integer.parseInt(keyList.get(i));
        ((BladeThread)this.base.threadGroup.get(String.valueOf(bladeNO))).bladeCommu.setAutoFlag(false);
        this.kvmUtil.disconnectBlade(bladeNO);
      } 
      keyList.clear();
      keyList = null;
      iter = null;
    } 
    super.stop();
    setStarted(false);
  }
  private void recyContainer() {
    isStart = false;
    this.kvmUtil.setUnPack(null);
    this.kvmUtil.imageData = null;
    this.kvmUtil.bladePreInfo = null;
    this.kvmUtil = null;
    if (this.client != null) {
      this.client.data = null;
      this.client.socket = null;
      this.client.socketUDP = null;
      this.client.packet = null;
      this.client = null;
    } 
    this.packData = null;
    this.unPackData = null;
    this.base = null;
    if (this.kvmInterface != null) {
      if (this.kvmInterface.fullScreen != null) {
        this.kvmInterface.fullScreen.imageParentPane.removeAll();
        this.kvmInterface.fullScreen.imageParentPane.setLayout(null);
        this.kvmInterface.fullScreen.imageParentPane = null;
        this.kvmInterface.fullScreen.imageParentScrollPane = null;
        this.kvmInterface.fullScreen.toolBar.removeAll();
        this.kvmInterface.fullScreen.toolBar.fullToolBarRelease();
        this.kvmInterface.fullScreen.toolBarFrame.removeAll();
        this.kvmInterface.fullScreen.toolBar = null;
        this.kvmInterface.fullScreen.toolBarFrame = null;
        this.kvmInterface.fullScreen.getContentPane().removeAll();
        this.kvmInterface.fullScreen.dispose();
        this.kvmInterface.fullScreen = null;
      } 
      this.kvmInterface.statusBar.dataStaTimer.cancel();
      this.kvmInterface.statusBar = null;
      this.kvmInterface.tabbedpane = null;
      this.kvmInterface.toolbar.releaseKVMToolBar();
      this.kvmInterface.toolbar.removeAll();
      this.kvmInterface.toolbar = null;
      this.kvmInterface.removeAll();
      getContentPane().removeAll();
      this.kvmInterface = null;
    } 
  }
  public ActionListener taskPerformer = new ActionListener()
    {
      public void actionPerformed(ActionEvent evt) {}
    };
  public void destroy() {
    this.kvmUtil.setBundle(null);
    if (this.client != null && this.client.socket != null) {
      try {
        this.client.sentData(this.packData.deleteUser());
      }
      catch (KVMException ex) {
        if ("IO_ERRCODE".equals(ex.getErrCode()))
        {
          Debug.printExc("IO Exception : " + ex.getErrDesc());
        }
      } 
    }
    if (this.timeHeart != null)
    {
      this.conn = false;
    }
    if (this.clientSocket != null && this.clientSocket.isAlive()) {
      this.clientSocket = null;
      this.clientSocket.bladePresentInfo = null;
      this.clientSocket.bladeStateInfo = null;
      this.clientSocket.keyState = null;
      this.clientSocket.unPack = null;
      this.clientSocket = null;
      this.base.threadGroup = null;
    } 
    if (this.base.getHookNum() != 0)
    {
      if (MouseDisplacementImpl.removeHook(this.base.getHookNum()) == 0) {
        Debug.println("removeHook fail");
      }
      else {
        this.base.setHookNum(0);
      } 
    }
    recyContainer();
    CopyFileToLocal.cleanLib();
    System.gc();
    super.destroy();
  }
  class SMMHeartTimer
    extends Thread
  {
    public void run() {
      IManaKVMApplet.this.conn = true;
      while (IManaKVMApplet.this.conn) {
        try {
          Thread.sleep(2000L);
          if (IManaKVMApplet.this.client != null && IManaKVMApplet.this.packData != null)
          {
            IManaKVMApplet.this.client.sentData(IManaKVMApplet.this.packData.heartBeat());
          }
        }
        catch (KVMException ex) {
          if ("IO_ERRCODE".equals(ex.getErrCode()))
          {
            break;
          }
        }
        catch (InterruptedException e) {}
      } 
    }
  }
  public boolean getStarted() {
    return this.started;
  }
  public void setStarted(boolean started) {
    this.started = started;
  }
  public static boolean isStart() {
    return isStart;
  }
  public static void setStart(boolean isStart) {
    IManaKVMApplet.isStart = isStart;
  }
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
}
