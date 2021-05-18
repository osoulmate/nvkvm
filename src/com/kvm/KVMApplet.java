package com.kvm;
import com.huawei.vm.console.utils.ResourceUtil;
import com.library.InetAddressUtils;
import com.library.LibException;
import com.library.LoggerUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.AccessControlException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.Action;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
public class KVMApplet
  extends JApplet
{
  private static final long serialVersionUID = 1L;
  private boolean kvmState = true;
  private KVMInterface kvmInterface = null;
  private transient Thread timeHeart = null;
  private boolean conn = true;
  private final long i = Long.valueOf("4294967296").intValue();
  private static final int MastInt = 2147483647;
  private String local = "ZH";
  private String strIP = "";
  public static final String protocol = "TCP";
  private transient ClientSocketCommunity clientSocket = null;
  private transient KVMUtil kvmUtil = null;
  private transient Client client = null;
  private transient PackData packData = null;
  private transient UnPackData unPackData = null;
  private transient Base base = null;
  private boolean started = false;
  private String skey = "";
  private String compress_state = "0";
  private String vmm_compress = "";
  private String verifyValue = "";
  private String verifyValueExt = "";
  private String mmVerifyValue = "";
  private String decrykey = "";
  private String compress = "";
  private String portNum = "";
  private String vmmPortNum = "";
  private String privilegeValue = "";
  private String IPA = "";
  private String IPB = "";
  private boolean appKvmFlag = false;
  public void getParaFromLocal(String[] args) {
    this.verifyValue = args[0];
    this.mmVerifyValue = args[1];
    this.decrykey = args[2];
    this.local = args[3];
    this.compress = args[4];
    this.vmm_compress = args[5];
    this.portNum = args[6];
    this.vmmPortNum = args[7];
    this.privilegeValue = args[8];
    this.IPA = args[9];
    this.IPB = args[10];
    this.verifyValueExt = args[11];
  }
  public void setAppKvmFlag() {
    this.appKvmFlag = true;
  }
  public void init() {
    try {
      String arch = System.getProperty("sun.arch.data.model");
      LoggerUtil.info( "arch: "+ arch );
      if (null != arch && arch.equals("64")) {
        ResourceUtil.setCONFIG_VM_LIBARY("com.huawei.vm.console.config.library.x64");
        Base.setMOUSE_LIB("KVMMouseDisPlace_x64");
        Base.setKEYBOARD_LIB("KVMKeyboard_x64");
      }
      else {
        ResourceUtil.setCONFIG_VM_LIBARY("com.huawei.vm.console.config.library");
        Base.setMOUSE_LIB("KVMMouseDisPlace");
        Base.setKEYBOARD_LIB("KVMKeyboard");
      }
      LoggerUtil.info( "CopyFileToLocal.cleanLib():" );
      CopyFileToLocal.cleanLib();
      if (!CopyFileToLocal.libarayPrepare())
      {
        this.kvmState = false;
      }
      LoggerUtil.info( "this.kvmState:"+this.kvmState );
    }
    catch (AccessControlException ae) {
      this.kvmState = false;
    } 
    this.base = new Base();
    Component parent = this;
    while (parent.getParent() != null)
      parent = parent.getParent(); 
    if (parent instanceof JFrame) {
      ((JFrame)parent).setResizable(true);
      ((JFrame)parent).addWindowListener(new JFrameWindowListener());
      Action emptyAction = new KVMAppletF10Action();
      ((JFrame)parent).getRootPane().getInputMap(2).put(KeyStroke.getKeyStroke("F10"), "F10");
      ((JFrame)parent).getRootPane().getActionMap().put("F10", emptyAction);
    } 
    this.kvmUtil = new KVMUtil();
    if (!this.appKvmFlag)
    {
      this.local = getParameter("local");
    }
    if (this.local == null)
    {
      this.local = "EN";
    }
    Base.setLocal(this.local);
    this.kvmUtil.setResourcePath("com.kvm.resource.KVMResource");
    if (!this.appKvmFlag)
    {
      if (parent instanceof JFrame) {
        String title = getParameter("title");
        String s = "";
        if (title == null) {
          s = this.kvmUtil.getString("Remote_Console");
        }
        else {
          s = this.kvmUtil.getString("Remote_Console") + "   " + getParameter("title");
        } 
        ((JFrame)parent).setTitle(s);
      } 
    }
    this.kvmUtil.setiWindosFocus(0);
    setSize(new Dimension(800, 600));
  }
  private void beforeStart() {
	LoggerUtil.info( "beforeStart:" );
	LoggerUtil.info( "beforeStart:"+this.clientSocket );
    while (this.clientSocket != null) {
      try {
        Thread.sleep(500L);
      }
      catch (InterruptedException e) {
        Debug.printExc(e.getClass().getName());
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
    LoggerUtil.info( "typeData: "+ typeData );
    Base.setTypeData(typeData);
    this.kvmInterface = new KVMInterface(this.kvmUtil, this.client, this.packData, this.clientSocket, this.base);
    this.client.setKvmInterface(this.kvmInterface);
    this.clientSocket.setKvmInterface(this.kvmInterface);
    this.clientSocket.setUnPackData(this.unPackData);
    this.packData.setKvmInterface(this.kvmInterface);
    this.kvmUtil.setKvmInterface(this.kvmInterface);
    this.kvmUtil.setUnPack(this.unPackData);
    this.kvmUtil.setImageData(new byte[this.base.getImageWidth() * this.base.getImageHeight()]);
  }
  private byte[] hexStringToBytes(String hexString) {
    if (hexString == null || hexString.equals("")) {
      byte[] tmp = null;
      return tmp;
    } 
    hexString = hexString.toUpperCase(getLocale());
    int length = hexString.length() / 2;
    char[] hexChars = hexString.toCharArray();
    byte[] d = new byte[length];
    for (int i = 0; i < length; i++) {
      int pos = i * 2;
      d[i] = (byte)(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
    } 
    return d;
  }
  private byte charToByte(char c) {
    return (byte)"0123456789ABCDEF".indexOf(c);
  }
  private boolean comInKVM() throws NoSuchAlgorithmException, InvalidKeySpecException {
    String ipA = "";
    String ipB = "";
    String port = "";
    if (!this.appKvmFlag) {
      ipA = InetAddressUtils.getSafeIP(getParameter("IPA"));
      ipB = InetAddressUtils.getSafeIP(getParameter("IPB"));
      port = InetAddressUtils.getSafePort(getParameter("port"));
    }
    else {
      ipA = InetAddressUtils.getSafeIP(this.IPA);
      ipB = InetAddressUtils.getSafeIP(this.IPB);
      port = InetAddressUtils.getSafePort(this.portNum);
      LoggerUtil.info("this.appKvmFlag" + this.appKvmFlag + "ipA: "+ ipA + "ipB: "+ ipB + "port: "+ port);
    } 
    this.base.setParameterIP((ipA != null) ? ipA.trim() : null);
    this.base.setParameterPort((port != null) ? Integer.parseInt(port.trim()) : 0);
    LoggerUtil.info( "this.kvmInterface.getBladeSize: "+ this.kvmInterface.getBladeSize() );
    if (this.kvmInterface.getBladeSize() > 1) {
      try {
        this.client.setSocket(KVMUtil.getProxySocket(this.base.getParameterIP(), 0, this.base
              .getParameterIP(), this.base
              .getParameterPort()));
        LoggerUtil.info( "this.client.getSocket: "+ this.client.getSocket() );
        if (null != this.client.getSocket())
        {
          Base.setIsProxy(true);
        }
      }
      catch (URISyntaxException e) {
        LoggerUtil.error(e.getClass().getName());
        Debug.printExc(e.getClass().getName());
        JOptionPane.showMessageDialog(this, this.kvmUtil.getString("Connection_failed"));
        return false;
      }
      catch (UnknownHostException e) {
        LoggerUtil.error(e.getClass().getName());
        Debug.printExc(e.getClass().getName());
        JOptionPane.showMessageDialog(this, this.kvmUtil.getString("Connection_failed"));
        return false;
      }
      catch (IOException e) {
        LoggerUtil.error(e.getClass().getName());
        JOptionPane.showMessageDialog(this, this.kvmUtil.getString("Connection_failed"));
        return false;
      } 
      if (Base.getIsProxy())
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
    if (!this.appKvmFlag)
    {
      this.vmm_compress = getParameter("vmm_compress");
    }
    if (this.vmm_compress == null) {
      Base.setVmm_compress_state(1);
    }
    else {
      Base.setVmm_compress_state(Integer.parseInt(this.vmm_compress));
    } 
    if (!this.appKvmFlag) {
      this.compress_state = getParameter("compress");
    }
    else {
      this.compress_state = this.compress;
    } 
    if (this.compress_state == null) {
      Base.setCompress(0);
    }
    else {
      Base.setCompress(Integer.parseInt(this.compress_state));
    } 
    if (!this.appKvmFlag) {
      this.skey = getParameter("decrykey");
    }
    else {
      this.skey = this.decrykey;
    } 
    if (this.skey == null || this.skey.equals("")) {
      if (0 != Base.getCompress())
      {
        return false;
      }
    }
    else {
      byte[] decry_key = hexStringToBytes(this.skey);
      if (decry_key != null) {
        if (decry_key.length != 32)
        {
          return false;
        }
        byte[] tempBuff = new byte[16];
        System.arraycopy(decry_key, 0, tempBuff, 0, 16);
        Base.setUser_key(tempBuff);
        System.arraycopy(decry_key, 16, tempBuff, 0, 16);
        Base.setUser_iv(tempBuff);
      } 
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
    if (!this.appKvmFlag)
    {
      this.verifyValueExt = getParameter("verifyValueExt");
    }
    this.kvmInterface.setVerifyValueExt(this.verifyValueExt);
    if (codeKey == null && this.verifyValueExt == null)
    {
      return false;
    }
    if (0 != Base.getCompress()) {
      this.kvmInterface.setEncodeKey(AESHandler.getcodekey(codeKey, 24, Base.getUser_iv(), this.kvmInterface.getHmac(), this.kvmInterface.getIterations()));
      int j = 0;
      byte[] tmp = new byte[1];
      for (int i = 0; i < 6; i++) {
        j = i * 4;
        tmp[0] = this.kvmInterface.getEncodeKey()[j];
        byte[] encodeKey = this.kvmInterface.getEncodeKey();
        encodeKey[j] = encodeKey[j + 3];
        encodeKey[j + 3] = tmp[0];
        tmp[0] = encodeKey[j + 1];
        encodeKey[j + 1] = encodeKey[j + 2];
        encodeKey[j + 2] = tmp[0];
        this.kvmInterface.setEncodeKey(encodeKey);
      } 
    } 
    long tempLong = 0L;
    try {
      tempLong = Long.parseLong(codeKey);
    }
    catch (Exception e) {
      return false;
    } 
    if (tempLong > 2147483647L) {
      this.kvmInterface.setCodeKey((int)(0L - this.i - tempLong));
    } else {
      try {
        this.kvmInterface.setCodeKey(Integer.parseInt(codeKey));
      }
      catch (Exception e) {
        return false;
      } 
    } 
    String mmCodeKey = "";
    if (!this.appKvmFlag) {
      mmCodeKey = getParameter("mmVerifyValue");
    }
    else {
      mmCodeKey = this.mmVerifyValue;
    } 
    if (null == mmCodeKey || "".equals(mmCodeKey))
    {
      return false;
    }
    Base.setVmmCodeKey(Integer.parseInt(mmCodeKey));
    String vmmPort = "";
    if (!this.appKvmFlag) {
      vmmPort = getParameter("vmmPort");
    }
    else {
      vmmPort = this.vmmPortNum;
    } 
    if (null == vmmPort || "".equals(vmmPort))
    {
      return false;
    }
    Base.setVmmPort(Integer.parseInt(vmmPort));
    String privilege = "";
    if (!this.appKvmFlag) {
      privilege = getParameter("privilege");
    }
    else {
      privilege = this.privilegeValue;
    } 
    if (null == privilege || "".equals(privilege))
    {
      return false;
    }
    Base.setPrivilege(Integer.parseInt(privilege));
    getBladeNames(this.kvmInterface);
    if (!this.appKvmFlag)
    {
      if (getParameter("bladeFlag") != null)
      {
        this.kvmInterface.setBladeFlag(getParameter("bladeFlag"));
      }
    }
    if (port == null)
    {
      return false;
    }
    Client.setPort(Integer.parseInt(port.trim()));
    try {
      this.client.setAddress(InetAddress.getByName(this.strIP));
      Base.setVmmConnIP(this.strIP);
    }
    catch (UnknownHostException e1) {
      Debug.printExc(e1.getClass().getName());
    } 
    return true;
  }
  public void start() {
    beforeStart();
    try {
      if (!comInKVM())
      {
        return;
      }
    }
    catch (NoSuchAlgorithmException e) {
      LoggerUtil.error(e.getClass().getName());
    }
    catch (InvalidKeySpecException e) {
      LoggerUtil.error(e.getClass().getName());
    } 
    if (this.kvmInterface.getBladeSize() == 1) {
      LoggerUtil.info( "processRack: ");
      processRack();
    }
    else {
      LoggerUtil.info( "processShelf: ");
      processShelf();
    }
    LoggerUtil.info( "super.start(): ");
    super.start();
    setStarted(true);
    Action emptyAction = new KVMAppletF10Action();
    getRootPane().getInputMap(2).put(KeyStroke.getKeyStroke("F10"), "F10");
    getRootPane().getActionMap().put("F10", emptyAction);
  }
  private void startHeart() {
    this.timeHeart = new SMMHeartTimer(this);
    this.timeHeart.setName("SmmHeartThread");
    this.timeHeart.start();
  }
  private void processShelf() {
    try {
      if (!Base.getIsProxy()) {
        Debug.println("not proxy");
        this.client.setSocket(new Socket(this.strIP, Client.getPort()));
      } 
      this.client.setDout(new DataOutputStream(this.client.getSocket().getOutputStream()));
      this.client.setDin(new BufferedInputStream(this.client.getSocket().getInputStream()));
    }
    catch (IOException e) {
      Debug.printExc(e.getClass().getName());
      JOptionPane.showMessageDialog(this, this.kvmUtil.getString("Connection_failed"));
      return;
    } 
    if (this.strIP != null) {
      boolean isRight = true;
      this.clientSocket.setConn(true);
      this.clientSocket.setName("receiveThread");
      this.clientSocket.getBladePresentInfo().clear();
      this.clientSocket.start();
      byte[] reqBladePresentInfo = this.packData.reqBladePresent();
      try {
        this.client.sentData(reqBladePresentInfo);
      }
      catch (LibException ex) {
        LoggerUtil.error(ex.getClass().getName());
        if ("IO_ERRCODE".equals(ex.getErrCode()))
        {
          JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface
              .getKvmUtil().getString("Network_interrupt_message"));
        }
      } 
      Timer timer = new Timer(1000, this.kvmUtil.taskPerformer);
      timer.start();
      while (true) {
        try {
          Thread.sleep(20L);
        }
        catch (InterruptedException e) {
          Debug.printExc(e.getClass().getName());
        } 
        if (this.kvmUtil.isConn()) {
          timer.stop();
          this.kvmUtil.setTimes(0);
          break;
        } 
        if (this.kvmUtil.getTimes() < 10) {
          continue;
        }
        timer.stop();
        isRight = false;
        JOptionPane.showMessageDialog(this, this.kvmUtil.getString("no_right"));
        this.kvmUtil.setTimes(0);
        break;
      } 
      if (isRight) {
        this.kvmUtil.setConn(false);
        getContentPane().setLayout(new BorderLayout());
        startHeart();
        getContentPane().add(this.kvmInterface, "Center");
        this.kvmUtil.setButtonEnable(false);
        this.kvmUtil.setBladeEnable();
      }
      else {
        this.clientSocket.setConn(false);
        this.clientSocket.getBladePresentInfo().clear();
        this.clientSocket.interrupt();
      } 
    } 
  }
  private void processRack() {
    this.kvmUtil.setConn(false);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(this.kvmInterface, "Center");
    this.kvmUtil.setButtonEnable(false);
    if (this.kvmInterface.getBladeSize() == 1) {
      ActionEvent event = new ActionEvent(this.kvmInterface.getToolbar(), 1, "blade1");
      LoggerUtil.info( "event: "+ event );
      this.kvmInterface.getToolbar().actionPerformed(event);
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
        int port = Integer.parseInt(portStr);
        Socket socket = null;
        try {
          socket = new Socket(ip, port);
          result = true;
        }
        catch (Exception e) {
          LoggerUtil.error(e.getClass().getName());
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
              LoggerUtil.error(e.getClass().getName());
            }
          }
        } 
      } catch (NumberFormatException nfe) {
        Debug.printExc(nfe.getClass().getName());
      } 
    }
    return result;
  }
  public void stop() {
    ArrayList<Object> keyList = new ArrayList();
    if (this.kvmInterface != null) {
      this.kvmInterface.getTabbedpane().getModel().removeChangeListener(this.kvmUtil.changeListener);
      Iterator<Object> iter = this.base.getThreadGroup().keySet().iterator();
      this.kvmInterface.getTabbedpane().getModel().removeChangeListener(this.kvmUtil.changeListener);
      while (iter.hasNext())
      {
        keyList.add(iter.next());
      }
      int num = this.base.getThreadGroup().size();
      for (int i = 0; i < num; i++) {
        int bladeNO = Integer.parseInt((String)keyList.get(i));
        ((BladeThread)this.base.getThreadGroup().get(String.valueOf(bladeNO))).getBladeCommu().setAutoFlag(false);
        this.kvmUtil.disconnectBlade(bladeNO);
      } 
      keyList.clear();
    } 
    super.stop();
    setStarted(false);
  }
  private void recyContainer() {
    this.kvmUtil.setUnPack(null);
    this.kvmUtil.setImageData(null);
    this.kvmUtil.setBladePreInfo(null);
    this.kvmUtil = null;
    if (this.client != null) {
      this.client.setData(null);
      this.client.setSocket(null);
      this.client.setSocketUDP(null);
      this.client.setPacket(null);
      this.client = null;
    } 
    this.packData = null;
    this.unPackData = null;
    this.base = null;
    if (this.kvmInterface != null) {
      if (this.kvmInterface.getFullScreen() != null) {
        this.kvmInterface.getFullScreen().getImageParentPane().removeAll();
        this.kvmInterface.getFullScreen().getImageParentPane().setLayout((LayoutManager)null);
        this.kvmInterface.getFullScreen().setImageParentPane((JPanel)null);
        this.kvmInterface.getFullScreen().setImageParentScrollPane((JScrollPane)null);
        this.kvmInterface.getFullScreen().getToolBar().removeAll();
        this.kvmInterface.getFullScreen().getToolBar().fullToolBarRelease();
        this.kvmInterface.getFullScreen().getToolBarFrame().removeAll();
        this.kvmInterface.getFullScreen().setToolBarParam((FullToolBar)null);
        this.kvmInterface.getFullScreen().setToolBarFrame((JPanel)null);
        this.kvmInterface.getFullScreen().getContentPane().removeAll();
        this.kvmInterface.getFullScreen().dispose();
        this.kvmInterface.setFullScreen((FullScreen)null);
      } 
      this.kvmInterface.getStatusBar().getDataStaTimer().cancel();
      this.kvmInterface.setStatusBar((StatusBar)null);
      this.kvmInterface.setTabbedpane((JTabbedPane)null);
      this.kvmInterface.getToolbar().releaseKVMToolBar();
      this.kvmInterface.getToolbar().removeAll();
      this.kvmInterface.setToolbar((KvmAppletToolBar)null);
      this.kvmInterface.removeAll();
      getContentPane().removeAll();
      this.kvmInterface = null;
    } 
  }
  public void destroy() {
    this.kvmUtil.setBundle(null);
    this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect().interrupt();
    this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect().setCollect(false);
    this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect().closeFile();
    if (this.kvmInterface.getFloatToolbar().getPowerMenu().getKineScopeDataCollect().isAlive()) {
      try {
        Thread.sleep(5L);
      }
      catch (InterruptedException e1) {
        LoggerUtil.error(e1.getClass().getName());
      } 
    }
    if (this.client != null && this.client.getSocket() != null) {
      try {
        this.client.sentData(this.packData.deleteUser());
      }
      catch (LibException ex) {
        if ("IO_ERRCODE".equals(ex.getErrCode()))
        {
          Debug.printExc("IO Exception : " + ex.getClass().getName());
        }
      } 
    }
    if (this.timeHeart != null)
    {
      this.conn = false;
    }
    if (this.clientSocket != null && this.clientSocket.isAlive()) {
      this.clientSocket.interrupt();
      this.clientSocket.setBladePresentInfo((Hashtable<String, byte[]>)null);
      this.clientSocket.setBladeStateInfo((Hashtable<Object, byte[]>)null);
      this.clientSocket.setKeyState((Hashtable<String, Byte>)null);
      this.clientSocket.setUnPack((UnPackData)null);
      this.clientSocket = null;
      this.base.setThreadGroup(null);
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
    super.destroy();
  }
  public boolean getStarted() {
    return this.started;
  }
  public void setStarted(boolean started) {
    this.started = started;
  }
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public void setConn(boolean conn) {
    this.conn = conn;
  }
  public boolean isConn() {
    return this.conn;
  }
  public void setClient(Client client) {
    this.client = client;
  }
  public Client getClient() {
    return this.client;
  }
  public void setPackData(PackData packData) {
    this.packData = packData;
  }
  public PackData getPackData() {
    return this.packData;
  }
}
