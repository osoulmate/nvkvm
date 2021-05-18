package com.kvm;
import com.huawei.vm.console.management.ConsoleControllers;
import com.huawei.vm.console.storage.impl.CDROMDriver;
import com.huawei.vm.console.utils.ResourceUtil;
import com.huawei.vm.console.utils.VMException;
import com.library.LoggerUtil;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
public class VirtualMedia
{
  private ResourceUtil util;
  public JButton getFlpSkim() {
    return this.flpSkim;
  }
  public void setFlpSkim(JButton flpSkim) {
    this.flpSkim = flpSkim;
  }
  public ResourceUtil getUtil() {
    return this.util;
  }
  public void setUtil(ResourceUtil util) {
    this.util = util;
  }
  private int cdrom = 2;
  public int getCdrom() {
    return this.cdrom;
  }
  public void setCdrom(int cdrom) {
    this.cdrom = cdrom;
  }
  private int floppy = 1;
  public int getFloppy() {
    return this.floppy;
  }
  public void setFloppy(int floppy) {
    this.floppy = floppy;
  }
  private int common = 0;
  private String floppyPath = "";
  public String getFloppyPath() {
    return this.floppyPath;
  }
  public void setFloppyPath(String floppyPath) {
    this.floppyPath = floppyPath;
  }
  private String cdromPath = "";
  boolean isFWP = false;
  boolean isCDImage = true;
  boolean isLocalDir = true;
  boolean isFlpBtnForCon = true;
  boolean isCdBtnForCon = true;
  boolean isFlpBtnForEj = true;
  boolean isCdBtnForEj = true;
  boolean waitingRes = false;
  private ConsoleControllers vmApplet = null;
  public ConsoleControllers getVmApplet() {
    return this.vmApplet;
  }
  public void setVmApplet(ConsoleControllers vmApplet) {
    this.vmApplet = vmApplet;
  }
  private CDROMDriver storageDevice = null; private JCheckBox fc; private JRadioButton fd; private JRadioButton fr; private JComboBox fdSelect; private JTextField fitext; private JButton flpSkim; private JButton fie;
  private JButton fcb;
  public CDROMDriver getStorageDevice() {
    return this.storageDevice;
  }
  private JRadioButton cr; private JRadioButton cd; private JComboBox cdSelect; private JTextField cdText; private JButton cdCon; private JButton cdSkim; private JRadioButton cdLocal; private JTextField cdLocalText; private JButton cdLocalSkim;
  public void setStorageDevice(CDROMDriver storageDevice) {
    this.storageDevice = storageDevice;
  }
  public JCheckBox getFc() {
    return this.fc;
  }
  public void setFc(JCheckBox fc) {
    this.fc = fc;
  }
  public JRadioButton getFd() {
    return this.fd;
  }
  public void setFd(JRadioButton fd) {
    this.fd = fd;
  }
  public JRadioButton getFr() {
    return this.fr;
  }
  public void setFr(JRadioButton fr) {
    this.fr = fr;
  }
  public JComboBox getFdSelect() {
    return this.fdSelect;
  }
  public void setFdSelect(JComboBox fdSelect) {
    this.fdSelect = fdSelect;
  }
  public JTextField getFitext() {
    return this.fitext;
  }
  public void setFitext(JTextField fitext) {
    this.fitext = fitext;
  }
  public JButton getFie() {
    return this.fie;
  }
  public void setFie(JButton fie) {
    this.fie = fie;
  }
  public JButton getFcb() {
    return this.fcb;
  }
  public void setFcb(JButton fcb) {
    this.fcb = fcb;
  }
  public JRadioButton getCr() {
    return this.cr;
  }
  public void setCr(JRadioButton cr) {
    this.cr = cr;
  }
  public JRadioButton getCd() {
    return this.cd;
  }
  public void setCd(JRadioButton cd) {
    this.cd = cd;
  }
  public JComboBox getCdSelect() {
    return this.cdSelect;
  }
  public void setCdSelect(JComboBox cdSelect) {
    this.cdSelect = cdSelect;
  }
  public JTextField getCdText() {
    return this.cdText;
  }
  public void setCdText(JTextField cdText) {
    this.cdText = cdText;
  }
  public JButton getCdCon() {
    return this.cdCon;
  }
  public void setCdCon(JButton cdCon) {
    this.cdCon = cdCon;
  }
  public JButton getCdSkim() {
    return this.cdSkim;
  }
  public void setCdSkim(JButton cdSkim) {
    this.cdSkim = cdSkim;
  }
  public JRadioButton getCdLocal() {
    return this.cdLocal;
  }
  public void setCdLocal(JRadioButton cdLocal) {
    this.cdLocal = cdLocal;
  }
  public JTextField getCdLocalText() {
    return this.cdLocalText;
  }
  public void setCdLocalText(JTextField cdLocalText) {
    this.cdLocalText = cdLocalText;
  }
  public JButton getCdLocalSkim() {
    return this.cdLocalSkim;
  }
  public void setCdLocalSkim(JButton cdLocalSkim) {
    this.cdLocalSkim = cdLocalSkim;
  }
  private UDFImageBuilderFile[] beforeLocalDir = null;
  public void setBeforeLocalDir(UDFImageBuilderFile[] beforeLocalDir) {
    if (beforeLocalDir != null) {
      this.beforeLocalDir = (UDFImageBuilderFile[])beforeLocalDir.clone();
    }
    else {
      this.beforeLocalDir = null;
    } 
  }
  private UDFImageBuilderFile[] curLocalDir = null;
  private JButton cie;
  public JButton getCie() {
    return this.cie;
  }
  public void setCie(JButton cie) {
    this.cie = cie;
  }
  private Floppy flp = null;
  public Floppy getFlp() {
    return this.flp;
  }
  public void setFlp(Floppy flp) {
    this.flp = flp;
  }
  private CdRom cdp = null;
  public CdRom getCdp() {
    return this.cdp;
  }
  public void setCdp(CdRom cdp) {
    this.cdp = cdp;
  }
  private Timer flpVmlink = null;
  public Timer getFlpVmlink() {
    return this.flpVmlink;
  }
  public void setFlpVmlink(Timer flpVmlink) {
    this.flpVmlink = flpVmlink;
  }
  private Timer cdVMlink = null;
  public Timer getCdVMlink() {
    return this.cdVMlink;
  }
  public void setCdVMlink(Timer cdVMlink) {
    this.cdVMlink = cdVMlink;
  }
  private String strIP = "";
  private int codeKey = 0;
  private byte[] negotiCodeKey = null;
  public byte[] getNegotiCodeKey() {
    if (null != this.negotiCodeKey)
    {
      return (byte[])this.negotiCodeKey.clone();
    }
    byte[] tmp = null;
    return tmp;
  }
  public void setNegotiCodeKey(byte[] negotiCodeKey) {
    this.negotiCodeKey = (byte[])negotiCodeKey.clone();
  }
  private byte[] strPort = null;
  public byte[] getStrPort() {
    if (null != this.strPort)
    {
      return (byte[])this.strPort.clone();
    }
    byte[] tmp = null;
    return tmp;
  }
  public void setStrPort(byte[] strPort) {
    if (null != strPort) {
      this.strPort = (byte[])strPort.clone();
    }
    else {
      this.strPort = null;
    } 
  }
  private byte[] saltVmmData = null;
  private boolean bVmmPri = true;
  private int codeKeyNegoCount;
  private KVMInterface kvmInterface = null;
  private int port = 8208;
  private javax.swing.Timer cdRomVMlink = null;
  public javax.swing.Timer getCdRomVMlink() {
    return this.cdRomVMlink;
  }
  public void setCdRomVMlink(javax.swing.Timer timer) {
    this.cdRomVMlink = timer;
  }
  private javax.swing.Timer floppyVmlink = null;
  public javax.swing.Timer getFloppyVmlink() {
    return this.floppyVmlink;
  }
  public void setFloppyVmlink(javax.swing.Timer timer) {
    this.floppyVmlink = timer;
  }
  private String floppys = null;
  public String getFloppys() {
    return this.floppys;
  }
  public void setFloppys(String floppys) {
    this.floppys = floppys;
  }
  private String cdroms = null;
  public String getCdroms() {
    return this.cdroms;
  }
  public void setCdroms(String cdroms) {
    this.cdroms = cdroms;
  }
  private String cdImage = "";
  public String getCdImage() {
    return this.cdImage;
  }
  public void setCdImage(String cdImage) {
    this.cdImage = cdImage;
  }
  private String flpImage = "";
  public String getFlpImage() {
    return this.flpImage;
  }
  public void setFlpImage(String flpImage) {
    this.flpImage = flpImage;
  }
  private String cdLocalDir = "";
  public String getCdLocalDir() {
    return this.cdLocalDir;
  }
  public void setCdLocalDir(String cdLocalDir) {
    this.cdLocalDir = cdLocalDir;
  }
  private String localDirName = null;
  public String getLocalDirName() {
    return this.localDirName;
  }
  public void setLocalDirName(String localDirName) {
    this.localDirName = localDirName;
  }
  private Map<Long, UDFExtendFile> memoryStruct = null;
  private static final int MAX_FILE_COUNT = 10000;
  public Map<Long, UDFExtendFile> getMemoryStruct() {
    return this.memoryStruct;
  }
  public void setMemoryStruct(Map<Long, UDFExtendFile> memoryStruct) {
    this.memoryStruct = memoryStruct;
  }
  private int srcType = 0;
  public VirtualMedia(String language, KVMInterface kvmInterface2) {
    this.vmApplet = new ConsoleControllers(false);
    this.vmApplet.getConsole().setVmm_compress_state(Base.getVmm_compress_state());
    this.util = new ResourceUtil();
    setLanguage(language);
    this.flp = new Floppy(this);
    this.cdp = new CdRom(this);
    Base.setBvmmCodeKeyNego(false);
    this.codeKeyNegoCount = 0;
    this.kvmInterface = kvmInterface2;
  }
  public void setLanguage(String local) {
    this.util.dosetLanguage(local);
    this.vmApplet.setLanguage(local);
  }
  protected static void uiManager() {
    if (KVMUtil.isWindowsOS()) {
      try {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      }
      catch (Exception e) {
        LoggerUtil.error(e.getClass().getName());
      } 
    }
  }
  protected JButton createButton(String setName, boolean result, ActionListener action) {
    JButton button = new JButton();
    button.setText(setName);
    button.addActionListener(action);
    button.setEnabled(result);
    return button;
  }
  protected JTextField createTextFiele(int x, boolean result) {
    JTextField textField = new JTextField(x);
    textField.setEnabled(result);
    return textField;
  }
  protected JCheckBox createChekBox(String setName, ActionListener action, boolean result) {
    JCheckBox checkBox = new JCheckBox();
    checkBox.setText(setName);
    checkBox.addActionListener(action);
    checkBox.setSelected(result);
    return checkBox;
  }
  protected JRadioButton createRadioButton(String setName, boolean result, ActionListener action) {
    JRadioButton radio = new JRadioButton();
    radio.setText(setName);
    radio.setSelected(result);
    radio.addActionListener(action);
    return radio;
  }
  protected Action createVFloppy() {
    Action action = new CreateVFloppy(this);
    return action;
  }
  protected Action floppyItemsUpdate() {
    Action action = new FloppyItemsUpdate(this);
    return action;
  }
  protected Action cdromItemsUpdate() {
    Action action = new CdromItemsUpdate(this);
    return action;
  }
  protected Action fileChoose() {
    Action action = new FileChoose(this);
    return action;
  }
  protected boolean isValidDirectory(String dirPath) {
    try {
      File file = new File(dirPath);
      if (!file.isDirectory()) {
        JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.425"));
        return false;
      } 
      File[] subFile = file.listFiles();
      if (subFile == null || subFile.length == 0)
      {
        JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.426"));
        return false;
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.425"));
      LoggerUtil.error(e.getClass().getName());
      return false;
    } 
    return true;
  }
  protected Action floppyEject() {
    Action action = new FloppyEject(this);
    return action;
  }
  protected Action fCheckClick() {
    Action action = new FCheckClick(this);
    return action;
  }
  protected Action createVCdrom() {
    Action action = new CreateVCdrom(this);
    return action;
  }
  protected boolean isLocalDirChange() {
    UDFImageBuilder mySabreUDF = new UDFImageBuilder();
    File dirFile = new File(this.cdLocalDir);
    try {
      mySabreUDF.setRootPath(dirFile);
    } catch (Exception e) {
      LoggerUtil.error("setRootPath fail");
    } 
    ArrayList<UDFImageBuilderFile> list2 = mySabreUDF.getRootUDFImageBuilderFile().getChilds();
    UDFImageBuilderFile[] childs2 = null;
    int j2 = 0;
    if (list2 != null) {
      childs2 = new UDFImageBuilderFile[list2.size()];
      for (UDFImageBuilderFile udf : list2) {
        childs2[j2++] = udf;
      }
    } 
    this.curLocalDir = childs2;
    if (null == this.beforeLocalDir || null == this.curLocalDir)
    {
      return true;
    }
    if (this.beforeLocalDir.length != this.curLocalDir.length)
    {
      return true;
    }
    try {
      for (int j = 0; j < this.curLocalDir.length; )
      {
        if (this.beforeLocalDir[j].getCreationTime().equals(this.curLocalDir[j].getCreationTime()) && this.beforeLocalDir[j]
          .getModificationTime().equals(this.curLocalDir[j].getModificationTime())) {
          j++;
          continue;
        } 
        return true;
      }
    }
    catch (Exception e1) {
      LoggerUtil.error(e1.getClass().getName());
      return true;
    } 
    return false;
  }
  protected Action cdromEject() {
    Action action = new CdromEject(this);
    return action;
  }
  public void cdRomforInsert() {
    int result = 0;
    String cImage = this.cdText.getText();
    if (null == cImage || "".equals(cImage)) {
      JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.404"));
      return;
    } 
    if (cImage.length() <= 4 || 
      !".iso".equals(cImage.toLowerCase(Locale.getDefault()).substring(cImage.length() - 4))) {
      JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.405"));
      return;
    } 
    result = this.vmApplet.changeCdromImage(cImage);
    if (0 != result) {
      JOptionPane.showMessageDialog(this.cdp, this.vmApplet.getStatement(result));
      return;
    } 
    this.cdCon.setEnabled(false);
    this.cdromPath = cImage;
    this.cdText.setEnabled(false);
    this.cdSkim.setEnabled(false);
    this.cie.setEnabled(false);
    Timer t = new Timer("cdrom insert");
    TimerTask task = new CdRomforInsertTimer(this);
    t.schedule(task, 4000L);
  }
  public boolean cdLocalforInsert() {
    int result = 0;
    this.cdromPath = this.cdLocalText.getText();
    if (null == this.cdromPath || "".equals(this.cdromPath)) {
      JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.425"));
      return false;
    } 
    File dir = new File(this.cdromPath);
    if (!dir.isDirectory()) {
      JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.425"));
      return false;
    } 
    File[] subdir = dir.listFiles();
    if (null == subdir || subdir.length == 0) {
      JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.426"));
      return false;
    } 
    if (isLocalDirChange()) {
      JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.427"));
      return false;
    } 
    result = this.vmApplet.changeLocalDir(this.localDirName, this.memoryStruct, this.cdromPath);
    if (0 != result) {
      JOptionPane.showMessageDialog(this.cdp, this.vmApplet.getStatement(result));
      return false;
    } 
    this.cdCon.setEnabled(false);
    this.cdLocalText.setEnabled(false);
    this.cdLocalSkim.setEnabled(false);
    this.cie.setEnabled(false);
    Timer t = new Timer("cdLocal insert");
    TimerTask task = new CdLocalforInsertTimer(this);
    t.schedule(task, 4000L);
    return true;
  }
  public void argumentsInit(int type) {
    this.srcType = 0;
    if (this.floppy == type) {
      this.floppyPath = "";
      this.isFWP = false;
    }
    else if (this.cdrom == type) {
      this.cdromPath = "";
      this.isCDImage = false;
      this.isLocalDir = false;
    } 
    this.waitingRes = false;
  }
  protected boolean validateFloppy() {
    if (this.fd.isSelected()) {
      this.floppyPath = (String)this.fdSelect.getSelectedItem();
      if (this.util.getResource("flp_cd_none").equals(this.floppyPath)) {
        JOptionPane.showMessageDialog(this.flp, this.util.getResource("com.huawei.vm.console.out.406"));
        return false;
      } 
      this.srcType = 0;
    }
    else if (this.fr.isSelected()) {
      this.floppyPath = this.fitext.getText();
      if ("".equals(this.fitext.getText()) || null == this.fitext.getText()) {
        JOptionPane.showMessageDialog(this.flp, this.util.getResource("com.huawei.vm.console.out.407"));
        return false;
      } 
      String flooppyImagePath = this.floppyPath.toLowerCase(Locale.getDefault());
      if (4 >= flooppyImagePath.length() || 
        !".img".equals(flooppyImagePath.substring(flooppyImagePath.length() - 4))) {
        JOptionPane.showMessageDialog(this.flp, this.util.getResource("com.huawei.vm.console.out.408"));
        return false;
      } 
      this.srcType = 1;
    }
    else {
      JOptionPane.showMessageDialog(this.flp, this.util.getResource("com.huawei.vm.console.out.409"));
      return false;
    } 
    if (this.fc.isSelected())
    {
      this.isFWP = true;
    }
    return true;
  }
  public void submitVForm(int deviceType) {
    int tmpPort = this.port;
    this.waitingRes = true;
    requestVMCodeKey();
    byte[] codeKey = null;
    if (this.codeKeyNegoCount <= 0) {
      enabledConntedBtn(deviceType);
      return;
    } 
    if (tmpPort <= 0) {
      requestVMPort();
      if (this.strPort != null) {
        tmpPort = (this.strPort[1] & 0xFF) << 8;
        tmpPort |= this.strPort[0] & 0xFF;
      } 
    } 
    codeKey = getCodeKey();
    if (this.vmApplet.isConsoleOK(this.common)) {
      if (codeKey != null)
      {
        createVMLink(deviceType, this.strIP, tmpPort, codeKey, getVmmSalt());
      }
      if (this.floppy == deviceType)
      {
        checkFormResTimeout();
      }
      else if (this.cdrom == deviceType)
      {
        checkFormResTimeout();
      }
    }
    else if (codeKey != null) {
      createVMLink(deviceType, this.strIP, 8208, codeKey, getVmmSalt());
    } 
  }
  private void checkFormResTimeout() {
    isFormSubmited();
  }
  private boolean isFormSubmited() {
    boolean isSubmited = false;
    if (this.waitingRes) {
      this.waitingRes = false;
      isSubmited = true;
    } 
    return isSubmited;
  }
  private void requestVMCodeKey() {
    if (this.codeKeyNegoCount == 0 || !Base.getBvmmCodeKeyNego()) {
      if (this.kvmInterface == null) {
        return;
      }
      this.negotiCodeKey = null;
      byte[] VMkey = this.kvmInterface.getPackData().reqVMCodeKey(0);
      BladeThread bladeThread = this.kvmInterface.getKvmUtil().getBladeThread();
      if (bladeThread == null) {
        return;
      }
      bladeThread.getBladeCommu().sentData(VMkey);
      try {
        for (int i = 0; i < 50; i++) {
          Thread.sleep(60L);
          if (!this.bVmmPri) {
            break;
          }
          if (null != getNegotiCodeKey() && (getNegotiCodeKey()).length > 1) {
            this.codeKeyNegoCount++;
            break;
          } 
        } 
      } catch (InterruptedException e) {
        Debug.printExc(e.getClass().getName());
      } 
    } 
  }
  private void requestVMPort() {
    if (this.kvmInterface == null) {
      return;
    }
    this.strPort = null;
    byte[] VMPort = this.kvmInterface.getPackData().reqVMPort(0);
    BladeThread bladeThread = this.kvmInterface.getKvmUtil().getBladeThread();
    if (bladeThread == null) {
      return;
    }
    bladeThread.getBladeCommu().sentData(VMPort);
    byte[] port = null;
    try {
      for (int i = 0; i < 50; i++)
      {
        Thread.sleep(60L);
        port = getStrPort();
        if (port != null && port.length > 1)
        {
          break;
        }
      }
    }
    catch (InterruptedException e) {
      Debug.printExc(e.getClass().getName());
    } 
  }
  private void createVMLink(int deviceType, String serverIP, int serverPort, byte[] certifyID, byte[] salt_vmm) {
    if (this.cdrom == deviceType) {
      this.vmApplet.setPbkdf2Params(this.kvmInterface.getHmac(), this.kvmInterface.getIterations());
      this.vmApplet.creatVMLink(deviceType, this.cdromPath, serverIP, serverPort, certifyID, this.isFWP, 
          Base.getBvmmCodeKeyNego(), salt_vmm, this.localDirName, this.memoryStruct, this.util, this.srcType);
      if (null == this.cdVMlink)
      {
        this.cdVMlink = new Timer("cdrom createVMLink");
      }
      TimerTask task = new CdromVMLink(this);
      this.cdVMlink.schedule(task, 7000L);
    } 
    if (this.floppy == deviceType) {
      this.vmApplet.setPbkdf2Params(this.kvmInterface.getHmac(), this.kvmInterface.getIterations());
      this.vmApplet.creatVMLink(deviceType, this.floppyPath, serverIP, serverPort, certifyID, this.isFWP, 
          Base.getBvmmCodeKeyNego(), salt_vmm, null, null, null, this.srcType);
      if (null == this.flpVmlink)
      {
        this.flpVmlink = new Timer("floppy createVMLink");
      }
      TimerTask task = new FloppyVMLink(this);
      this.flpVmlink.schedule(task, 7000L);
    } 
  }
  protected void floppyItemsUpdate(String type) {
    if ("all".equals(type)) {
      floppyItemsUpdate("fd");
      this.fdSelect.setEnabled(true);
      this.fitext.setEnabled(true);
      this.flpSkim.setEnabled(false);
    }
    else if ("fd".equals(type)) {
      this.flpSkim.setEnabled(false);
      this.fd.setSelected(true);
      this.fr.setSelected(false);
      this.fdSelect.setEnabled(true);
      this.fitext.setEnabled(false);
      this.fie.setEnabled(false);
    }
    else if ("fi".equals(type)) {
      this.fd.setSelected(false);
      this.fr.setSelected(true);
      this.fdSelect.setEnabled(false);
      this.fitext.setEnabled(true);
      this.flpSkim.setEnabled(true);
    }
    else if ("none".equals(type)) {
      if (this.vmApplet.isLibOK())
      {
        this.fd.setEnabled(true);
      }
      this.fr.setEnabled(true);
      if (this.fd.isSelected())
      {
        this.fdSelect.setEnabled(true);
      }
      if (this.fr.isSelected()) {
        this.fitext.setEnabled(true);
        this.flpSkim.setEnabled(true);
      } 
    } 
  }
  protected void cdromItemsUpdate(String type) {
    if ("all".equals(type)) {
      this.cdText.setEnabled(false);
      this.cdSkim.setEnabled(false);
      this.cdLocalSkim.setEnabled(false);
      this.cdLocalText.setEnabled(false);
      this.cie.setEnabled(true);
    }
    else if ("cd".equals(type)) {
      this.cd.setSelected(true);
      this.cr.setSelected(false);
      this.cdSelect.setEnabled(false);
      this.cdText.setEnabled(true);
      this.cdSkim.setEnabled(true);
      this.cdLocal.setSelected(false);
      this.cdLocalSkim.setEnabled(false);
      this.cdLocalText.setEnabled(false);
    }
    else if ("ci".equals(type)) {
      this.cdText.setEnabled(false);
      this.cdSelect.setEnabled(true);
      this.cd.setSelected(false);
      this.cr.setSelected(true);
      this.cdSkim.setEnabled(false);
      this.cdLocal.setSelected(false);
      this.cdLocalSkim.setEnabled(false);
      this.cdLocalText.setEnabled(false);
    }
    else if ("cdLocal".equals(type)) {
      this.cdText.setEnabled(false);
      this.cdSelect.setEnabled(false);
      this.cd.setSelected(false);
      this.cr.setSelected(false);
      this.cdSkim.setEnabled(false);
      this.cdLocal.setSelected(true);
      this.cdLocalSkim.setEnabled(true);
      this.cdLocalText.setEnabled(false);
    }
    else if ("none".equals(type)) {
      if (this.vmApplet.isLibOK())
      {
        this.cr.setEnabled(false);
      }
      this.cd.setEnabled(false);
      this.cdLocal.setEnabled(false);
      if (this.cr.isSelected()) {
        this.cdText.setEnabled(false);
        this.cdSelect.setEnabled(true);
        this.cdSkim.setEnabled(false);
        this.cdLocalSkim.setEnabled(false);
        this.cdLocalText.setEnabled(false);
      } 
      if (this.cd.isSelected()) {
        this.cdText.setEnabled(true);
        this.cdSkim.setEnabled(true);
      } 
      if (this.cdLocal.isSelected()) {
        this.cdLocalSkim.setEnabled(true);
        this.cdLocalText.setEnabled(false);
      } 
    } 
  }
  public void disconnectVMLink(int deviceType) {
    if (deviceType == this.cdrom) {
      this.storageDevice = null;
      this.vmApplet.destroyVMLink(deviceType);
      this.cdCon.setEnabled(false);
      this.cdText.setEnabled(false);
      this.cdSkim.setEnabled(false);
      this.cdLocalSkim.setEnabled(false);
      this.cdLocalText.setEnabled(false);
      this.cie.setEnabled(false);
      Timer t = new Timer("cdrom disconnectVMLink");
      TimerTask task = new CdromDisconnectVMLink(this);
      t.schedule(task, 4000L);
    } 
    if (deviceType == this.floppy) {
      this.vmApplet.destroyVMLink(deviceType);
      this.fc.setEnabled(false);
      this.fitext.setEnabled(false);
      this.fie.setEnabled(false);
      this.flpSkim.setEnabled(false);
      this.fcb.setEnabled(false);
      Timer t = new Timer("floppy disconnectVMLink");
      TimerTask task = new FloppyDisconnectVMLink(this);
      t.schedule(task, 4000L);
    } 
    this.negotiCodeKey = null;
    this.codeKeyNegoCount = 0;
    Base.setBvmmCodeKeyNego(false);
  }
  protected void updateItemForDisCon(int deviceType) {
    if (this.floppy == deviceType) {
      this.fc.setEnabled(true);
      this.fcb.setText(this.util.getResource("flp_cd_connection"));
      this.isFlpBtnForCon = true;
      this.fie.setText(this.util.getResource("flp_cd_pop_up_program"));
      this.fc.setSelected(false);
      floppyItemsUpdate("none");
      activeButton(deviceType);
    }
    else if (this.cdrom == deviceType) {
      this.isCdBtnForCon = true;
      this.cdCon.setText(this.util.getResource("flp_cd_connection"));
      cdromItemsUpdate("none");
      this.waitingRes = false;
      activeButton(deviceType);
    } 
  }
  private void activeButton(int deviceType) {
    if (this.cdrom == deviceType) {
      this.cd.setEnabled(true);
      this.cr.setEnabled(true);
      this.cdLocal.setEnabled(true);
    }
    else if (this.floppy == deviceType) {
      this.fr.setEnabled(true);
      this.fd.setEnabled(true);
      this.fcb.setSelected(false);
    } 
  }
  protected void checkFloppyVMConsole() {
    if (!this.vmApplet.isConsoleOK(this.floppy)) {
      if (this.vmApplet.isVMLinkCrt(this.floppy))
      {
        Timer date = new Timer("floppy checkVMConsole");
        TimerTask task = new CheckFloppyVMConsole(this);
        date.schedule(task, 800L);
      }
      else
      {
        this.fc.setEnabled(true);
        this.fcb.setEnabled(true);
        this.fd.setEnabled(true);
        this.fr.setEnabled(true);
        this.isFlpBtnForCon = true;
        if (this.fd.isSelected()) {
          this.fdSelect.setEnabled(true);
        }
        else {
          this.fitext.setEnabled(true);
          this.flpSkim.setEnabled(true);
        } 
        this.vmApplet.destroyVMLink(this.floppy);
      }
    } else {
      int result = this.vmApplet.getConsoleState(this.floppy);
      if (0 != result)
      {
        JOptionPane.showMessageDialog(this.flp, this.util
            .getResource("com.huawei.vm.console.out.423") + this.vmApplet.getConsoleStatement(this.floppy));
      }
      this.isFlpBtnForCon = true;
      this.fc.setEnabled(true);
      this.fcb.setEnabled(true);
      this.fr.setEnabled(true);
      this.fd.setEnabled(true);
      if (this.fd.isSelected()) {
        this.fdSelect.setEnabled(true);
      }
      else {
        this.fitext.setEnabled(true);
        this.flpSkim.setEnabled(true);
      } 
    } 
  }
  private void enabledConntedBtn(int deviceType) {
    if (this.floppy == deviceType) {
      this.isFlpBtnForCon = true;
      this.fc.setEnabled(true);
      this.fcb.setEnabled(true);
      this.fd.setEnabled(true);
      this.fr.setEnabled(true);
      if (this.fd.isSelected())
      {
        this.fdSelect.setEnabled(true);
      }
      else
      {
        this.fitext.setEnabled(true);
        this.flpSkim.setEnabled(true);
      }
    } else if (this.cdrom == deviceType) {
      this.isCdBtnForCon = true;
      this.cie.setEnabled(false);
      this.cr.setEnabled(true);
      this.cd.setEnabled(true);
      this.cdCon.setEnabled(true);
      this.cdLocal.setEnabled(true);
      if (this.cr.isSelected()) {
        this.cdSelect.setEnabled(true);
      }
      else if (this.cd.isSelected()) {
        this.cdText.setEnabled(true);
        this.cdSkim.setEnabled(true);
      }
      else if (this.cdLocal.isSelected()) {
        this.cdLocalSkim.setEnabled(true);
        this.cdLocalText.setEnabled(false);
      } 
    } 
  }
  protected void disableConntedBtn(int deviceType) {
    if (this.floppy == deviceType) {
      this.isFlpBtnForCon = false;
      if (!this.isFlpBtnForCon)
      {
        if (this.fr.isSelected())
        {
          this.fie.setEnabled(true);
        }
        this.fcb.setEnabled(true);
        this.fc.setEnabled(false);
        this.fdSelect.setEnabled(false);
        this.fitext.setEnabled(false);
        this.flpSkim.setEnabled(false);
        this.fd.setEnabled(false);
        this.fr.setEnabled(false);
        this.fcb.setText(this.util.getResource("flp_disconnection"));
      }
    }
    else if (this.cdrom == deviceType) {
      this.isCdBtnForCon = false;
      if (!this.isCdBtnForCon) {
        if (this.cd.isSelected() || this.cdLocal.isSelected())
        {
          this.cie.setEnabled(true);
        }
        this.cdCon.setEnabled(true);
        this.cd.setEnabled(false);
        this.cr.setEnabled(false);
        this.cdSkim.setEnabled(false);
        this.cdText.setEnabled(false);
        this.cdSelect.setEnabled(false);
        this.cdLocal.setEnabled(false);
        this.cdLocalSkim.setEnabled(false);
        this.cdLocalText.setEnabled(false);
        this.cdCon.setText(this.util.getResource("flp_disconnection"));
      } 
    } 
  }
  public boolean validateCdrom() {
    if (this.cr.isSelected()) {
      this.cdromPath = (String)this.cdSelect.getSelectedItem();
      if (this.util.getResource("flp_cd_none").equals(this.cdromPath)) {
        JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.415"));
        return false;
      } 
      this.isCDImage = false;
      this.isLocalDir = false;
      this.srcType = 0;
    }
    else if (this.cd.isSelected()) {
      this.cdromPath = this.cdText.getText();
      if (null == this.cdromPath || "".equals(this.cdromPath)) {
        JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.407"));
        return false;
      } 
      File file = new File(this.cdromPath);
      if (file.isDirectory()) {
        JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.407"));
        return false;
      } 
      String cdromImagePath = this.cdromPath.toLowerCase(Locale.getDefault());
      if (this.cdromPath.length() <= 4 || !".iso".equals(cdromImagePath.substring(this.cdromPath.length() - 4))) {
        JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.416"));
        return false;
      } 
      this.isCDImage = true;
      this.isLocalDir = false;
      this.srcType = 1;
    }
    else if (this.cdLocal.isSelected()) {
      this.cdromPath = this.cdLocalText.getText();
      if (null == this.cdromPath || "".equals(this.cdromPath)) {
        JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.425"));
        return false;
      } 
      File dir = new File(this.cdromPath);
      if (!dir.isDirectory()) {
        JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.425"));
        return false;
      } 
      File[] subdir = dir.listFiles();
      if (null == subdir || subdir.length == 0) {
        JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.426"));
        return false;
      } 
      this.isLocalDir = true;
      this.isCDImage = false;
      this.srcType = 2;
    }
    else {
      JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.417"));
      return false;
    } 
    return true;
  }
  protected void checkCdromVMConsole() {
    if (!this.vmApplet.isConsoleOK(this.cdrom)) {
      if (this.vmApplet.isVMLinkCrt(this.cdrom))
      {
        Timer date = new Timer("cdrom checkVMConsole");
        TimerTask task = new CheckCdromVMConsole(this);
        date.schedule(task, 800L);
      }
      else
      {
        this.isCdBtnForCon = true;
        this.cdCon.setEnabled(true);
        this.cr.setEnabled(true);
        this.cd.setEnabled(true);
        this.cdLocal.setEnabled(true);
        this.cie.setEnabled(false);
        if (this.cr.isSelected()) {
          this.cdSelect.setEnabled(true);
        }
        else if (this.cd.isSelected()) {
          this.cdText.setEnabled(true);
          this.cdSkim.setEnabled(true);
        }
        else if (this.cdLocal.isSelected()) {
          this.cdLocalSkim.setEnabled(true);
          this.cdLocalText.setEnabled(false);
        }
        else {
          this.cie.setEnabled(false);
        } 
        this.vmApplet.destroyVMLink(this.cdrom);
      }
    } else {
      int state = this.vmApplet.getConsoleState(this.cdrom);
      if (0 != state)
      {
        JOptionPane.showMessageDialog(this.cdp, this.util
            .getResource("com.huawei.vm.console.out.422") + this.vmApplet.getConsoleStatement(this.cdrom));
      }
      this.isCdBtnForCon = true;
      this.cie.setEnabled(false);
      this.cdCon.setEnabled(true);
      this.cr.setEnabled(true);
      this.cd.setEnabled(true);
      this.cdLocal.setEnabled(true);
      if (this.cr.isSelected()) {
        this.cdSelect.setEnabled(true);
      }
      else if (this.cd.isSelected()) {
        this.cdText.setEnabled(true);
        this.cdSkim.setEnabled(true);
      }
      else {
        this.cdLocalSkim.setEnabled(true);
        this.cdLocalText.setEnabled(true);
      } 
    } 
  }
  protected Action doFloppyVMlink() {
    Action action = new DoFloppyVMlink(this);
    return action;
  }
  protected Action doCdRomVMlink() {
    Action action = new DoCdRomVMlink(this);
    return action;
  }
  public void cdromISOEject() {
    int result = 0;
    result = this.vmApplet.changeCdromImage("");
    if (0 != result) {
      JOptionPane.showMessageDialog(this.cdp, this.vmApplet.getStatement(result));
      return;
    } 
    this.cie.setText(this.util.getResource("flp_cd_insert"));
    this.cdText.setEnabled(true);
    this.cdText.setEditable(true);
    this.cdSkim.setEnabled(true);
    this.isCdBtnForEj = false;
  }
  public void cdLocalISOEject() {
    int result = 0;
    result = this.vmApplet.changeLocalDir(null, null, "");
    if (0 != result) {
      JOptionPane.showMessageDialog(this.cdp, this.vmApplet.getStatement(result));
      return;
    } 
    this.cie.setText(this.util.getResource("flp_cd_insert"));
    this.cdLocalText.setEnabled(false);
    this.cdLocalText.setEditable(false);
    this.cdLocalSkim.setEnabled(true);
    this.isCdBtnForEj = false;
  }
  public String getStrIP() {
    return this.strIP;
  }
  public void setStrIP(String strIP) {
    this.strIP = strIP;
  }
  public byte[] getVmmSalt() {
    if (null != this.saltVmmData)
    {
      return (byte[])this.saltVmmData.clone();
    }
    this.saltVmmData = new byte[16];
    return (byte[])this.saltVmmData.clone();
  }
  public byte[] getCodeKey() {
    if (Base.getBvmmCodeKeyNego()) {
      if (null != this.negotiCodeKey)
      {
        return (byte[])this.negotiCodeKey.clone();
      }
      byte[] tmp = null;
      return tmp;
    } 
    byte[] buff = new byte[4];
    buff[0] = (byte)(this.codeKey >> 24 & 0xFF);
    buff[1] = (byte)(this.codeKey >> 16 & 0xFF);
    buff[2] = (byte)(this.codeKey >> 8 & 0xFF);
    buff[3] = (byte)(this.codeKey & 0xFF);
    return buff;
  }
  public void setCodeKey(int codeKey) {
    this.codeKey = codeKey;
  }
  public void setNegotiCodeKey(byte[] codeKey, byte[] salt_vmm) {
    if (codeKey.length > 0) {
      Base.setBvmmCodeKeyNego(true);
      this.negotiCodeKey = new byte[codeKey.length];
      System.arraycopy(codeKey, 0, this.negotiCodeKey, 0, codeKey.length);
    } 
    if (salt_vmm.length > 0) {
      this.saltVmmData = new byte[salt_vmm.length];
      System.arraycopy(salt_vmm, 0, this.saltVmmData, 0, salt_vmm.length);
    } 
  }
  public void setVMMPort(byte[] port) {
    if (port.length > 0) {
      this.strPort = new byte[port.length];
      System.arraycopy(port, 0, this.strPort, 0, port.length);
    } 
  }
  public void setVMMPri(boolean bPri) {
    this.bVmmPri = bPri;
  }
  public int getPort() {
    return this.port;
  }
  public void setPort(int port) {
    this.port = port;
  }
  public MouseAdapter fdmouseClicked() {
    MouseAdapter mouse = new FdmouseClicked(this);
    return mouse;
  }
  public MouseAdapter cdmouseClicked() {
    MouseAdapter mouse = new CdmouseClicked(this);
    return mouse;
  }
  public void destoryVMM() {
    if (null == this.vmApplet) {
      return;
    }
    if (null == this.cdRomVMlink && null == this.floppyVmlink) {
      this.vmApplet.stop();
      return;
    } 
    if (null != this.cdRomVMlink) {
      this.cdRomVMlink.stop();
      this.cdRomVMlink = null;
      disconnectVMLink(this.cdrom);
    } 
    if (null != this.floppyVmlink) {
      //this.floppyVmlink.stop();
      this.floppyVmlink = null;
      disconnectVMLink(this.floppy);
    } 
    this.vmApplet.destroy();
    this.vmApplet.stop();
  }
  public long getDir(File f) throws VMException {
    long size = 0L;
    File[] flist = f.listFiles();
    if (flist != null && flist.length != 0) {
      size = flist.length;
      for (int i = 0; i < flist.length; i++) {
        if (flist[i].isDirectory())
        {
          size += getDir(flist[i]);
        }
        if (size > 10000L)
        {
          throw new VMException(337);
        }
      } 
    } 
    return size;
  }
}
