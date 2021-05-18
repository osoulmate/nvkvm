package com.kvmV1;
import com.huawei.vm.console.managementV1.ConsoleControllers;
import com.huawei.vm.console.storageV1.impl.CDROMDriver;
import com.huawei.vm.console.uiV1.JAVAFileFileter;
import com.huawei.vm.console.utilsV1.ResourceUtil;
import com.huawei.vm.console.utilsV1.VMException;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.SabreUDFImageBuilder;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.UDFExtendFile;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.UDFImageBuilderFile;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.UDFRevision;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.handler.SerializationHandler;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
public class VirtualMedia
{
  private ResourceUtil util;
  private int cdrom = 2;
  private int floppy = 1;
  private int common = 0;
  private String floppyPath = "";
  private String cdromPath = "";
  boolean isFWP = false;
  boolean isFImage = false;
  boolean isCDImage = true;
  boolean isLocalDir = true;
  boolean isFlpBtnForCon = true;
  boolean isCdBtnForCon = true;
  boolean isFlpBtnForEj = true;
  boolean isCdBtnForEj = true;
  boolean waitingRes = false;
  private ConsoleControllers vmApplet = null;
  private CDROMDriver storageDevice = null;
  private JCheckBox fc;
  private JRadioButton fd;
  private JRadioButton fr;
  private JComboBox fdSelect;
  private JTextField fitext;
  private JButton flpSkim;
  private JButton fie;
  private JButton fcb;
  private JRadioButton cr;
  private JRadioButton cd;
  private JComboBox cdSelect;
  private JTextField cdText;
  private JButton cdCon;
  private JButton cdSkim;
  private JRadioButton cdLocal;
  private JTextField cdLocalText;
  private JButton cdLocalSkim;
  private UDFImageBuilderFile[] beforeLocalDir = null;
  private UDFImageBuilderFile[] curLocalDir = null;
  private JButton cie;
  public Floppy flp = null;
  public CdRom cdp = null;
  private Timer flpVmlink = null;
  private Timer cdVMlink = null;
  private String strIP = "";
  private int codeKey = 0;
  private byte[] negotiCodeKey = null;
  private boolean bCodeKeyNego = false;
  private boolean bSecretVMM = false;
  private KVMInterface kvmInterface = null;
  private byte[] negoSalt = null;
  private int port = 8208;
  private javax.swing.Timer cdRomVMlink = null;
  private javax.swing.Timer floppyVmlink = null;
  private String floppys = null;
  private String cdroms = null;
  public String cdImage = "";
  public String flpImage = "";
  private int bladeNO = 0;
  public String cdLocalDir = "";
  public String localDirName = null;
  public Map<Long, UDFExtendFile> memoryStruct = null;
  private static final int MAX_FILE_COUNT = 10000;
  private int srcType = 0;
  public VirtualMedia(String language, KVMInterface kvmInterface) {
    this.vmApplet = new ConsoleControllers();
    this.util = new ResourceUtil();
    setLanguage(language);
    this.flp = new Floppy();
    this.cdp = new CdRom();
    this.bSecretVMM = false;
    this.bCodeKeyNego = false;
    this.negoSalt = null;
    this.kvmInterface = kvmInterface;
  }
  public void SetBladeNO(int bladeNum) {
    this.bladeNO = bladeNum;
  }
  public int GetBladeNO() {
    return this.bladeNO;
  }
  public void setLanguage(String local) {
    this.util.dosetLanguage(local);
    this.vmApplet.setLanguage(local);
  }
  public class Floppy
    extends JPanel
  {
    private static final long serialVersionUID = 1L;
    public Floppy() {
      VirtualMedia.this.uiManager();
      setLayout((LayoutManager)null);
      setBackground(new Color(204, 227, 242));
      setSize(337, 49);
      if (KVMUtil.isLinuxOS() && Base.local.equalsIgnoreCase("en"))
      {
        setSize(380, 49);
      }
      VirtualMedia.this.fc = VirtualMedia.this.createChekBox(VirtualMedia.this.util.getResource("flp_setwrite"), VirtualMedia.this.fCheckClick(), true);
      VirtualMedia.this.fd = VirtualMedia.this.createRadioButton(VirtualMedia.this.util.getResource("flp_floppy"), true, VirtualMedia.this.floppyItemsUpdate());
      VirtualMedia.this.fdSelect = flpcreateComboBox();
      VirtualMedia.this.fdSelect.addMouseListener(VirtualMedia.this.fdmouseClicked());
      Component c = VirtualMedia.this.fdSelect.getComponent(0);
      c.addMouseListener(VirtualMedia.this.fdmouseClicked());
      VirtualMedia.this.fcb = VirtualMedia.this.createButton(VirtualMedia.this.util.getResource("flp_cd_connection"), true, VirtualMedia.this.createVFloppy());
      VirtualMedia.this.fr = VirtualMedia.this.createRadioButton(VirtualMedia.this.util.getResource("flp_cd_image_file"), false, VirtualMedia.this.floppyItemsUpdate());
      VirtualMedia.this.fitext = VirtualMedia.this.createTextFiele(12, false);
      VirtualMedia.this.flpSkim = VirtualMedia.this.createButton(VirtualMedia.this.util.getResource("flp_cd_skim_over"), false, VirtualMedia.this.fileChoose());
      VirtualMedia.this.fie = VirtualMedia.this.createButton(VirtualMedia.this.util.getResource("flp_cd_pop_up_program"), false, VirtualMedia.this.floppyEject());
      if (KVMUtil.isLinuxOS()) {
        if (Base.local.equalsIgnoreCase("en"))
        {
          VirtualMedia.this.fd.setBounds(0, 4, 90, 20);
          VirtualMedia.this.fdSelect.setBounds(93, 4, 80, 20);
          VirtualMedia.this.fc.setBounds(173, 4, 112, 20);
          VirtualMedia.this.fcb.setBounds(285, 4, 90, 20);
          VirtualMedia.this.fr.setBounds(0, 23, 90, 20);
          VirtualMedia.this.fitext.setBounds(93, 25, 82, 18);
          VirtualMedia.this.flpSkim.setBounds(177, 25, 100, 18);
          VirtualMedia.this.fie.setBounds(285, 25, 90, 18);
        }
        else
        {
          VirtualMedia.this.fd.setBounds(3, 4, 85, 20);
          VirtualMedia.this.fdSelect.setBounds(88, 4, 65, 20);
          VirtualMedia.this.fc.setBounds(158, 4, 90, 20);
          VirtualMedia.this.fcb.setBounds(252, 4, 75, 20);
          VirtualMedia.this.fr.setBounds(3, 23, 85, 20);
          VirtualMedia.this.fitext.setBounds(88, 25, 75, 18);
          VirtualMedia.this.flpSkim.setBounds(163, 25, 78, 18);
          VirtualMedia.this.fie.setBounds(252, 25, 75, 18);
        }
      } else {
        VirtualMedia.this.fd.setBounds(3, 4, 85, 20);
        VirtualMedia.this.fdSelect.setBounds(88, 4, 50, 20);
        VirtualMedia.this.fc.setBounds(138, 4, 103, 20);
        VirtualMedia.this.fcb.setBounds(240, 4, 92, 20);
        VirtualMedia.this.fr.setBounds(3, 23, 85, 20);
        VirtualMedia.this.fitext.setBounds(88, 25, 75, 18);
        VirtualMedia.this.flpSkim.setBounds(163, 25, 78, 18);
        VirtualMedia.this.fie.setBounds(240, 25, 92, 18);
      } 
      VirtualMedia.this.fd.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.fc.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.fcb.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.fr.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.fitext.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.flpSkim.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.fie.setBackground(new Color(204, 227, 242));
      add(VirtualMedia.this.fc);
      add(VirtualMedia.this.fd);
      add(VirtualMedia.this.fdSelect);
      add(VirtualMedia.this.fitext);
      add(VirtualMedia.this.fr);
      add(VirtualMedia.this.fcb);
      add(VirtualMedia.this.flpSkim);
      add(VirtualMedia.this.fie);
      setBorder(BorderFactory.createLineBorder(new Color(153, 199, 230), 1));
    }
    private JComboBox flpcreateComboBox() {
      JComboBox<String> comboBox = new JComboBox();
      VirtualMedia.this.floppys = VirtualMedia.this.vmApplet.getFloppyDevices();
      if ("".equals(VirtualMedia.this.floppys) || null == VirtualMedia.this.floppys) {
        comboBox.addItem(VirtualMedia.this.util.getResource("flp_cd_none"));
        VirtualMedia.this.floppys = null;
      }
      else {
        String[] flplit = VirtualMedia.this.floppys.split(":");
        for (int i = 0; i < flplit.length; i++) {
          if (!"".equals(flplit[i]))
          {
            comboBox.addItem(flplit[i] + ":");
          }
        } 
      } 
      return comboBox;
    }
  }
  public class CdRom
    extends JPanel
  {
    private static final long serialVersionUID = 1L;
    public CdRom() {
      VirtualMedia.this.uiManager();
      setLayout((LayoutManager)null);
      if (KVMUtil.isLinuxOS() && Base.local.equalsIgnoreCase("en")) {
        setSize(380, 69);
      }
      else {
        setSize(337, 69);
      } 
      setBackground(new Color(204, 227, 242));
      VirtualMedia.this.cr = VirtualMedia.this.createRadioButton(VirtualMedia.this.util.getResource("cd_cdroms"), true, VirtualMedia.this.cdromItemsUpdate());
      VirtualMedia.this.cdSelect = cdcreateCombobox();
      VirtualMedia.this.cdSelect.addMouseListener(VirtualMedia.this.cdmouseClicked());
      Component c = VirtualMedia.this.cdSelect.getComponent(0);
      c.addMouseListener(VirtualMedia.this.cdmouseClicked());
      VirtualMedia.this.cdCon = VirtualMedia.this.createButton(VirtualMedia.this.util.getResource("flp_cd_connection"), true, VirtualMedia.this.createVCdrom());
      VirtualMedia.this.cd = VirtualMedia.this.createRadioButton(VirtualMedia.this.util.getResource("flp_cd_image_file"), false, VirtualMedia.this.cdromItemsUpdate());
      VirtualMedia.this.cdText = VirtualMedia.this.createTextFiele(12, false);
      VirtualMedia.this.cdSkim = VirtualMedia.this.createButton(VirtualMedia.this.util.getResource("flp_cd_skim_over"), false, VirtualMedia.this.fileChoose());
      VirtualMedia.this.cie = VirtualMedia.this.createButton(VirtualMedia.this.util.getResource("flp_cd_pop_up_program"), false, VirtualMedia.this.cdromEject());
      VirtualMedia.this.cdLocalText = VirtualMedia.this.createTextFiele(12, false);
      VirtualMedia.this.cdLocalSkim = VirtualMedia.this.createButton(VirtualMedia.this.util.getResource("flp_cd_skim_over"), false, VirtualMedia.this.fileChoose());
      VirtualMedia.this.cdLocal = VirtualMedia.this.createRadioButton(VirtualMedia.this.util.getResource("flp_cd_local_file"), false, VirtualMedia.this.cdromItemsUpdate());
      if (KVMUtil.isLinuxOS()) {
        if (Base.local.equalsIgnoreCase("en"))
        {
          VirtualMedia.this.cr.setBounds(0, 4, 90, 20);
          VirtualMedia.this.cdSelect.setBounds(100, 4, 82, 20);
          VirtualMedia.this.cdCon.setBounds(280, 4, 95, 20);
          VirtualMedia.this.cd.setBounds(0, 23, 100, 20);
          VirtualMedia.this.cdText.setBounds(100, 25, 82, 20);
          VirtualMedia.this.cdSkim.setBounds(182, 25, 95, 20);
          VirtualMedia.this.cie.setBounds(280, 25, 95, 20);
          VirtualMedia.this.cdLocal.setBounds(0, 42, 100, 20);
          VirtualMedia.this.cdLocalText.setBounds(100, 46, 82, 20);
          VirtualMedia.this.cdLocalSkim.setBounds(182, 46, 95, 20);
        }
        else
        {
          VirtualMedia.this.cr.setBounds(0, 4, 85, 20);
          VirtualMedia.this.cdSelect.setBounds(98, 4, 65, 20);
          VirtualMedia.this.cdCon.setBounds(255, 4, 75, 20);
          VirtualMedia.this.cd.setBounds(0, 23, 95, 20);
          VirtualMedia.this.cdText.setBounds(98, 25, 82, 20);
          VirtualMedia.this.cdSkim.setBounds(178, 25, 75, 20);
          VirtualMedia.this.cie.setBounds(255, 25, 75, 18);
          VirtualMedia.this.cdLocal.setBounds(0, 42, 97, 20);
          VirtualMedia.this.cdLocalText.setBounds(98, 46, 82, 20);
          VirtualMedia.this.cdLocalSkim.setBounds(178, 46, 75, 20);
        }
      }
      else {
        VirtualMedia.this.cr.setBounds(3, 4, 85, 20);
        VirtualMedia.this.cdSelect.setBounds(88, 4, 50, 20);
        VirtualMedia.this.cdCon.setBounds(240, 4, 92, 20);
        VirtualMedia.this.cd.setBounds(3, 23, 85, 20);
        VirtualMedia.this.cdText.setBounds(88, 25, 75, 20);
        VirtualMedia.this.cdSkim.setBounds(163, 25, 78, 20);
        VirtualMedia.this.cie.setBounds(240, 25, 92, 20);
        VirtualMedia.this.cdLocal.setBounds(3, 42, 85, 20);
        VirtualMedia.this.cdLocalText.setBounds(88, 46, 75, 20);
        VirtualMedia.this.cdLocalSkim.setBounds(163, 46, 78, 20);
      } 
      VirtualMedia.this.cd.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.cdCon.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.cr.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.cdText.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.cdSkim.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.cie.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.cdLocal.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.cdLocalText.setBackground(new Color(204, 227, 242));
      VirtualMedia.this.cdLocalSkim.setBackground(new Color(204, 227, 242));
      add(VirtualMedia.this.cd);
      add(VirtualMedia.this.cdSelect);
      add(VirtualMedia.this.cdCon);
      add(VirtualMedia.this.cr);
      add(VirtualMedia.this.cdText);
      add(VirtualMedia.this.cdSkim);
      add(VirtualMedia.this.cie);
      add(VirtualMedia.this.cdLocal);
      add(VirtualMedia.this.cdLocalText);
      add(VirtualMedia.this.cdLocalSkim);
      setBorder(BorderFactory.createLineBorder(new Color(153, 199, 230), 1));
      setBackground(new Color(204, 227, 242));
    }
    private JComboBox cdcreateCombobox() {
      JComboBox<String> box = new JComboBox();
      VirtualMedia.this.cdroms = VirtualMedia.this.vmApplet.getCDROMDevices();
      if ("".equals(VirtualMedia.this.cdroms) || null == VirtualMedia.this.cdroms) {
        box.addItem(VirtualMedia.this.util.getResource("flp_cd_none"));
        VirtualMedia.this.cdroms = null;
      }
      else {
        String[] cdsplit = VirtualMedia.this.cdroms.split(":");
        for (int i = 0; i < cdsplit.length; i++) {
          if (!"".equals(cdsplit[i]))
          {
            box.addItem(cdsplit[i] + ":");
          }
        } 
      } 
      return box;
    }
  }
  private void uiManager() {
    if (KVMUtil.isWindowsOS()) {
      try {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      }
      catch (Exception e) {
        e.getStackTrace();
      } 
    }
  }
  private JButton createButton(String setName, boolean result, ActionListener action) {
    JButton button = new JButton();
    button.setText(setName);
    button.addActionListener(action);
    button.setEnabled(result);
    return button;
  }
  private JTextField createTextFiele(int x, boolean result) {
    JTextField textField = new JTextField(x);
    textField.setEnabled(result);
    return textField;
  }
  private JCheckBox createChekBox(String setName, ActionListener action, boolean result) {
    JCheckBox checkBox = new JCheckBox();
    checkBox.setText(setName);
    checkBox.addActionListener(action);
    checkBox.setSelected(result);
    return checkBox;
  }
  private JRadioButton createRadioButton(String setName, boolean result, ActionListener action) {
    JRadioButton radio = new JRadioButton();
    radio.setText(setName);
    radio.setSelected(result);
    radio.addActionListener(action);
    return radio;
  }
  private Action createVFloppy() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          try {
            if (VirtualMedia.this.isFlpBtnForCon) {
              VirtualMedia.this.argumentsInit(VirtualMedia.this.floppy);
              if (!VirtualMedia.this.validateFloppy()) {
                return;
              }
              VirtualMedia.this.fdSelect.setEnabled(false);
              VirtualMedia.this.fc.setEnabled(false);
              VirtualMedia.this.fcb.setEnabled(false);
              VirtualMedia.this.fd.setEnabled(false);
              VirtualMedia.this.fr.setEnabled(false);
              if (VirtualMedia.this.fr.isSelected())
              {
                VirtualMedia.this.flpSkim.setEnabled(false);
              }
              VirtualMedia.this.fitext.setEnabled(false);
              VirtualMedia.this.submitVForm(VirtualMedia.this.floppy);
            }
            else {
              int result = JOptionPane.showConfirmDialog(VirtualMedia.this.flp, VirtualMedia.this.util.getResource("com.huawei.vm.console.out.413"), UIManager.getString("OptionPane.titleText"), 0);
              if (0 == result) {
                VirtualMedia.this.floppyVmlink.stop();
                VirtualMedia.this.floppyVmlink = null;
                VirtualMedia.this.disconnectVMLink(VirtualMedia.this.floppy);
              } 
            } 
          } finally {}
        }
      };
    return action;
  }
  private Action floppyItemsUpdate() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (e.getSource() == VirtualMedia.this.fd) {
            VirtualMedia.this.floppyItemsUpdate("fd");
          }
          else {
            VirtualMedia.this.floppyItemsUpdate("fi");
          } 
        }
      };
    return action;
  }
  private Action cdromItemsUpdate() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (e.getSource() == VirtualMedia.this.cd) {
            VirtualMedia.this.cdromItemsUpdate("cd");
          }
          else if (e.getSource() == VirtualMedia.this.cr) {
            VirtualMedia.this.cdromItemsUpdate("ci");
          }
          else if (e.getSource() == VirtualMedia.this.cdLocal) {
            VirtualMedia.this.cdromItemsUpdate("cdLocal");
          } 
        }
      };
    return action;
  }
  private Action fileChoose() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (e.getSource() == VirtualMedia.this.flpSkim) {
            JFileChooser chooser = new JFileChooser(VirtualMedia.this.flpImage);
            chooser.addChoosableFileFilter((FileFilter)new JAVAFileFileter("img", VirtualMedia.this.util));
            chooser.setAcceptAllFileFilterUsed(false);
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == 0)
            {
              VirtualMedia.this.fitext.setText(chooser.getSelectedFile().getAbsolutePath());
              VirtualMedia.this.flpImage = chooser.getSelectedFile().getParent();
            }
          } else if (e.getSource() == VirtualMedia.this.cdSkim) {
            JFileChooser chooser = new JFileChooser(VirtualMedia.this.cdImage);
            chooser.addChoosableFileFilter((FileFilter)new JAVAFileFileter("iso", VirtualMedia.this.util));
            chooser.setAcceptAllFileFilterUsed(false);
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == 0)
            {
              VirtualMedia.this.cdText.setText(chooser.getSelectedFile().getAbsolutePath());
              VirtualMedia.this.cdImage = chooser.getSelectedFile().getParent();
            }
          }
          else if (e.getSource() == VirtualMedia.this.cdLocalSkim) {
            JFileChooser chooser = new JFileChooser(VirtualMedia.this.cdLocalDir);
            chooser.setFileSelectionMode(1);
            chooser.addChoosableFileFilter((FileFilter)new JAVAFileFileter("directory", VirtualMedia.this.util));
            chooser.setAcceptAllFileFilterUsed(false);
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == 0) {
              VirtualMedia.this.cdLocalText.setText(chooser.getSelectedFile().getAbsolutePath());
              VirtualMedia.this.cdLocalDir = chooser.getSelectedFile().getAbsolutePath();
              VirtualMedia.this.localDirName = chooser.getSelectedFile().getName();
            } else {
              return;
            } 
            if (!VirtualMedia.this.isValidDirectory(VirtualMedia.this.cdLocalDir)) {
              return;
            }
            Timer t = new Timer("cdLocalDir memory iso");
            TimerTask task = new TimerTask()
              {
                public void run()
                {
                  try {
                    VirtualMedia.this.cdCon.setEnabled(false);
                    VirtualMedia.this.cdLocalSkim.setEnabled(false);
                    VirtualMedia.this.cdLocalText.setEnabled(false);
                    VirtualMedia.this.cdLocalText.setEditable(false);
                    VirtualMedia.this.cd.setEnabled(false);
                    VirtualMedia.this.cr.setEnabled(false);
                    System.gc();
                    SabreUDFImageBuilder mySabreUDF = new SabreUDFImageBuilder();
                    File dirFile = new File(VirtualMedia.this.cdLocalDir);
                    try {
                      VirtualMedia.this.getDir(dirFile);
                    }
                    catch (VMException ex) {
                      VirtualMedia.this.cdCon.setEnabled(true);
                      VirtualMedia.this.cdLocalSkim.setEnabled(true);
                      VirtualMedia.this.cdLocalText.setEnabled(false);
                      VirtualMedia.this.cdLocalText.setEditable(false);
                      VirtualMedia.this.cd.setEnabled(true);
                      VirtualMedia.this.cr.setEnabled(true);
                      JOptionPane.showMessageDialog(VirtualMedia.this.cdp, VirtualMedia.this.util.getResource("com.huawei.vm.console.out.431"));
                      VirtualMedia.this.cdLocalText.setText("");
                      return;
                    } 
                    File[] childFiles = dirFile.listFiles();
                    for (int i = 0; i < childFiles.length; i++)
                    {
                      mySabreUDF.addFileToRootDirectory(childFiles[i]);
                    }
                    if (dirFile.getName().length() >= 30) {
                      mySabreUDF.setImageIdentifier(dirFile.getName().substring(0, 28) + "~");
                    }
                    else {
                      mySabreUDF.setImageIdentifier(dirFile.getName());
                    } 
                    VirtualMedia.this.beforeLocalDir = mySabreUDF.getRootUDFImageBuilderFile().getChilds();
                    mySabreUDF.writeImage("cdLocalDir\\cdLocalDir.iso", UDFRevision.Revision102);
                    VirtualMedia.this.memoryStruct = ((SerializationHandler)mySabreUDF.getSerializationHandler()).getExtendMap();
                    VirtualMedia.this.cdCon.setEnabled(true);
                    VirtualMedia.this.cdLocalSkim.setEnabled(true);
                    VirtualMedia.this.cdLocalText.setEnabled(true);
                    VirtualMedia.this.cdLocalText.setEditable(false);
                    if (VirtualMedia.this.isCdBtnForCon)
                    {
                      VirtualMedia.this.cd.setEnabled(true);
                      VirtualMedia.this.cr.setEnabled(true);
                    }
                    else
                    {
                      VirtualMedia.this.cd.setEnabled(false);
                      VirtualMedia.this.cr.setEnabled(false);
                    }
                  }
                  catch (Exception e) {
                    VirtualMedia.this.cdCon.setEnabled(true);
                    VirtualMedia.this.cdLocalSkim.setEnabled(true);
                    VirtualMedia.this.cdLocalText.setEnabled(false);
                    VirtualMedia.this.cdLocalText.setEditable(false);
                    VirtualMedia.this.cd.setEnabled(true);
                    VirtualMedia.this.cr.setEnabled(true);
                    JOptionPane.showMessageDialog(VirtualMedia.this.cdp, VirtualMedia.this.util.getResource("com.huawei.vm.console.out.428"));
                    return;
                  } 
                }
              };
            t.schedule(task, 100L);
          } 
        }
      };
    return action;
  }
  private boolean isValidDirectory(String dirPath) {
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
      return false;
    } 
    return true;
  }
  private Action floppyEject() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent arg0) {
          if (VirtualMedia.this.isFlpBtnForCon && VirtualMedia.this.isCdBtnForCon) {
            return;
          }
          int result = 0;
          if (VirtualMedia.this.isFlpBtnForEj) {
            VirtualMedia.this.fcb.setEnabled(false);
            VirtualMedia.this.fie.setEnabled(false);
            VirtualMedia.this.fitext.setEnabled(false);
            VirtualMedia.this.flpSkim.setEnabled(false);
            VirtualMedia.this.fc.setEnabled(false);
            result = VirtualMedia.this.vmApplet.changeFloppyImage("");
            if (0 != result) {
              JOptionPane.showMessageDialog(VirtualMedia.this.flp, VirtualMedia.this.vmApplet.getStatement(result));
              return;
            } 
            Timer t = new Timer("floppy Eject");
            TimerTask task = new TimerTask()
              {
                public void run()
                {
                  VirtualMedia.this.fitext.setEnabled(true);
                  VirtualMedia.this.flpSkim.setEnabled(true);
                  VirtualMedia.this.fie.setEnabled(true);
                  VirtualMedia.this.fie.setText(VirtualMedia.this.util.getResource("flp_cd_insert"));
                  VirtualMedia.this.isFlpBtnForEj = false;
                  VirtualMedia.this.fc.setEnabled(true);
                  VirtualMedia.this.fcb.setEnabled(true);
                }
              };
            t.schedule(task, 2000L);
          }
          else {
            String fImage = VirtualMedia.this.fitext.getText();
            if ("".equals(fImage) || null == fImage) {
              JOptionPane.showMessageDialog(VirtualMedia.this.flp, VirtualMedia.this.util.getResource("com.huawei.vm.console.out.404"));
              return;
            } 
            if (4 >= fImage.length() || !".img".equals(fImage.toLowerCase().substring(fImage.length() - 4))) {
              JOptionPane.showMessageDialog(VirtualMedia.this.flp, VirtualMedia.this.util.getResource("com.huawei.vm.console.out.405"));
              return;
            } 
            result = VirtualMedia.this.vmApplet.changeFloppyImage(fImage);
            if (0 != result) {
              JOptionPane.showMessageDialog(VirtualMedia.this.flp, VirtualMedia.this.vmApplet.getStatement(result));
              return;
            } 
            VirtualMedia.this.fcb.setEnabled(false);
            VirtualMedia.this.floppyPath = fImage;
            VirtualMedia.this.fie.setEnabled(false);
            VirtualMedia.this.fitext.setEnabled(false);
            VirtualMedia.this.flpSkim.setEnabled(false);
            VirtualMedia.this.fc.setEnabled(false);
            VirtualMedia.this.isFlpBtnForEj = true;
            Timer t = new Timer("floppy insert");
            TimerTask task = new TimerTask()
              {
                public void run()
                {
                  VirtualMedia.this.fcb.setEnabled(true);
                  VirtualMedia.this.fie.setEnabled(true);
                  VirtualMedia.this.fie.setText(VirtualMedia.this.util.getResource("flp_cd_pop_up_program"));
                  VirtualMedia.this.isFlpBtnForEj = true;
                }
              };
            t.schedule(task, 2000L);
          } 
        }
      };
    return action;
  }
  private Action fCheckClick() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent arg0) {
          try {
            if (VirtualMedia.this.util.getResource("flp_disconnection").equals(VirtualMedia.this.fcb.getText()))
            {
              if (VirtualMedia.this.fc.isSelected())
              {
                VirtualMedia.this.vmApplet.setWriteProtect(true);
              }
              else
              {
                VirtualMedia.this.vmApplet.setWriteProtect(false);
              }
            }
          } catch (Exception e) {
            e.getStackTrace();
          } 
        }
      };
    return action;
  }
  private Action createVCdrom() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent event) {
          try {
            if (VirtualMedia.this.isCdBtnForCon) {
              VirtualMedia.this.argumentsInit(VirtualMedia.this.cdrom);
              if (!VirtualMedia.this.validateCdrom()) {
                return;
              }
              VirtualMedia.this.cdCon.setEnabled(false);
              VirtualMedia.this.cr.setEnabled(false);
              VirtualMedia.this.cd.setEnabled(false);
              VirtualMedia.this.cdSelect.setEnabled(false);
              VirtualMedia.this.cdText.setEnabled(false);
              VirtualMedia.this.cdLocal.setEnabled(false);
              VirtualMedia.this.cdLocalSkim.setEnabled(false);
              VirtualMedia.this.cdLocalText.setEnabled(false);
              if (VirtualMedia.this.cd.isSelected())
              {
                VirtualMedia.this.cdSkim.setEnabled(false);
              }
              if (VirtualMedia.this.cdLocal.isSelected()) {
                VirtualMedia.this.cdLocalText.setEnabled(false);
                VirtualMedia.this.cdLocalSkim.setEnabled(false);
                if (VirtualMedia.this.isLocalDirChange()) {
                  JOptionPane.showMessageDialog(VirtualMedia.this.cdp, VirtualMedia.this.util.getResource("com.huawei.vm.console.out.427"));
                  VirtualMedia.this.cdCon.setEnabled(true);
                  VirtualMedia.this.cdLocalSkim.setEnabled(true);
                  VirtualMedia.this.cdLocalText.setEnabled(false);
                  VirtualMedia.this.cdLocal.setEnabled(true);
                  return;
                } 
              } 
              VirtualMedia.this.submitVForm(VirtualMedia.this.cdrom);
            }
            else {
              int result = JOptionPane.showConfirmDialog(VirtualMedia.this.cdp, VirtualMedia.this.util.getResource("com.huawei.vm.console.out.412"), UIManager.getString("OptionPane.titleText"), 0);
              if (0 == result)
              {
                VirtualMedia.this.cdRomVMlink.stop();
                VirtualMedia.this.cdRomVMlink = null;
                VirtualMedia.this.disconnectVMLink(VirtualMedia.this.cdrom);
              }
              else
              {
                return;
              }
            } 
          } catch (Exception e) {
            e.getStackTrace();
          } 
        }
      };
    return action;
  }
  private boolean isLocalDirChange() {
    SabreUDFImageBuilder mySabreUDF = new SabreUDFImageBuilder();
    File dirFile = new File(this.cdLocalDir);
    File[] childFiles = dirFile.listFiles();
    for (int i = 0; i < childFiles.length; i++) {
      try {
        mySabreUDF.addFileToRootDirectory(childFiles[i]);
      }
      catch (Exception e) {
        return true;
      } 
    } 
    this.curLocalDir = mySabreUDF.getRootUDFImageBuilderFile().getChilds();
    if (null == this.beforeLocalDir || null == this.curLocalDir)
    {
      return true;
    }
    if (this.beforeLocalDir.length != this.curLocalDir.length)
    {
      return true;
    }
    for (int j = 0; j < this.curLocalDir.length;) {
      try {
        if (this.beforeLocalDir[j].getCreationTime().equals(this.curLocalDir[j].getCreationTime()) && this.beforeLocalDir[j].getModificationTime().equals(this.curLocalDir[j].getModificationTime())) {
          j++;
          continue;
        } 
        return true;
      }
      catch (Exception e1) {
        return true;
      } 
    } 
    return false;
  }
  private Action cdromEject() {
    return cdImageEject();
  }
  private Action cdImageEject() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (VirtualMedia.this.isFlpBtnForCon && VirtualMedia.this.isCdBtnForCon) {
            return;
          }
          int result = 0;
          if (VirtualMedia.this.isCdBtnForEj) {
            VirtualMedia.this.cdCon.setEnabled(false);
            VirtualMedia.this.cdText.setEnabled(false);
            VirtualMedia.this.cdSkim.setEnabled(false);
            VirtualMedia.this.cie.setEnabled(false);
            if (VirtualMedia.this.isCDImage) {
              result = VirtualMedia.this.vmApplet.changeCD("cdrom", "", null, null);
            }
            else if (VirtualMedia.this.isLocalDir) {
              result = VirtualMedia.this.vmApplet.changeCD("cdlocal", "", VirtualMedia.this.localDirName, VirtualMedia.this.memoryStruct);
            } 
            if (0 != result) {
              JOptionPane.showMessageDialog(VirtualMedia.this.cdp, VirtualMedia.this.vmApplet.getStatement(result));
              return;
            } 
            Timer t = new Timer("cdrom Eject");
            TimerTask task = new TimerTask()
              {
                public void run()
                {
                  if (VirtualMedia.this.isCDImage) {
                    VirtualMedia.this.cdText.setEnabled(true);
                    VirtualMedia.this.cdSkim.setEnabled(true);
                    VirtualMedia.this.cdLocalSkim.setEnabled(false);
                  }
                  else if (VirtualMedia.this.isLocalDir) {
                    VirtualMedia.this.cdLocalSkim.setEnabled(true);
                    VirtualMedia.this.cdText.setEnabled(false);
                    VirtualMedia.this.cdSkim.setEnabled(false);
                    VirtualMedia.this.cd.setEnabled(false);
                    VirtualMedia.this.cr.setEnabled(false);
                  } 
                  VirtualMedia.this.cdCon.setEnabled(true);
                  VirtualMedia.this.cie.setEnabled(true);
                  VirtualMedia.this.cie.setText(VirtualMedia.this.util.getResource("flp_cd_insert"));
                  VirtualMedia.this.isCdBtnForEj = false;
                }
              };
            System.gc();
            t.schedule(task, 2000L);
          }
          else if (VirtualMedia.this.isCDImage) {
            VirtualMedia.this.cdRomforInsert();
          }
          else if (VirtualMedia.this.isLocalDir) {
            if (!VirtualMedia.this.cdLocalforInsert()) {
              return;
            }
          } 
        }
      };
    return action;
  }
  private void cdRomforInsert() {
    int result = 0;
    String cImage = this.cdText.getText();
    if (null == cImage || "".equals(cImage)) {
      JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.404"));
      return;
    } 
    if (cImage.length() <= 4 || !".iso".equals(cImage.toLowerCase().substring(cImage.length() - 4))) {
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
    TimerTask task = new TimerTask()
      {
        public void run()
        {
          VirtualMedia.this.cdCon.setEnabled(true);
          VirtualMedia.this.cie.setEnabled(true);
          VirtualMedia.this.cie.setText(VirtualMedia.this.util.getResource("flp_cd_pop_up_program"));
          VirtualMedia.this.isCdBtnForEj = true;
        }
      };
    t.schedule(task, 2000L);
  }
  private boolean cdLocalforInsert() {
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
    TimerTask task = new TimerTask()
      {
        public void run()
        {
          VirtualMedia.this.cdCon.setEnabled(true);
          VirtualMedia.this.cie.setEnabled(true);
          VirtualMedia.this.cr.setEnabled(false);
          VirtualMedia.this.cd.setEnabled(false);
          VirtualMedia.this.cie.setText(VirtualMedia.this.util.getResource("flp_cd_pop_up_program"));
          VirtualMedia.this.isCdBtnForEj = true;
        }
      };
    t.schedule(task, 2000L);
    return true;
  }
  private void argumentsInit(int type) {
    this.srcType = 0;
    if (this.floppy == type) {
      this.floppyPath = "";
      this.isFWP = false;
      this.isFImage = false;
    }
    else if (this.cdrom == type) {
      this.cdromPath = "";
      this.isCDImage = false;
      this.isLocalDir = false;
    }
    else if (this.common == type) {
      this.floppyPath = "";
      this.isFWP = false;
      this.isFImage = false;
      this.cdromPath = "";
      this.isCDImage = false;
      this.isLocalDir = false;
    } 
    this.waitingRes = false;
  }
  private boolean validateFloppy() {
    if (this.fd.isSelected()) {
      this.floppyPath = (String)this.fdSelect.getSelectedItem();
      if (this.util.getResource("flp_cd_none").equals(this.floppyPath)) {
        JOptionPane.showMessageDialog(this.flp, this.util.getResource("com.huawei.vm.console.out.406"));
        return false;
      } 
      this.isFImage = false;
      this.srcType = 0;
    }
    else if (this.fr.isSelected()) {
      this.floppyPath = this.fitext.getText();
      if ("".equals(this.fitext.getText()) || null == this.fitext.getText()) {
        JOptionPane.showMessageDialog(this.flp, this.util.getResource("com.huawei.vm.console.out.407"));
        return false;
      } 
      String flooppyImagePath = this.floppyPath.toLowerCase();
      if (4 >= flooppyImagePath.length() || !".img".equals(flooppyImagePath.substring(flooppyImagePath.length() - 4))) {
        JOptionPane.showMessageDialog(this.flp, this.util.getResource("com.huawei.vm.console.out.408"));
        return false;
      } 
      this.isFImage = true;
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
  private void submitVForm(int deviceType) {
    this.waitingRes = true;
    requestVMCodeKey();
    this.bSecretVMM = this.kvmInterface.kvmUtil.getSecretVMM(GetBladeNO());
    if (this.vmApplet.isConsoleOK(this.common)) {
      createVMLink(deviceType, this.strIP, this.port, getCodeKey(), getSalt(), this.bSecretVMM);
      if (this.floppy == deviceType)
      {
        checkFormResTimeout(this.floppy);
      }
      else if (this.cdrom == deviceType)
      {
        checkFormResTimeout(this.cdrom);
      }
    } else {
      createVMLink(deviceType, this.strIP, 8208, getCodeKey(), getSalt(), this.bSecretVMM);
    } 
  }
  private void checkFormResTimeout(int deviceType) {
    if (isFormSubmited())
    {
      if (this.common == deviceType) {
        JOptionPane.showMessageDialog(null, this.util.getResource("com.huawei.vm.console.out.410"));
        return;
      } 
    }
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
    int i = 0;
    if (this.kvmInterface == null) {
      System.out.println("requestVMCodeKey kvmInterface is null");
      return;
    } 
    if (this.kvmInterface.kvmUtil.getAuthVMM(this.bladeNO)) {
      BladeThread bladeThread = this.kvmInterface.kvmUtil.getBladeThread();
      if (bladeThread == null) {
        System.out.println("requestVMCodeKey bladeThread is null;bladeNO is " + this.bladeNO);
        return;
      } 
      bladeThread.bladeCommu.sentData(this.kvmInterface.packData.reqVMCodeKey(this.bladeNO));
      for (i = 0; i < 100; i++)
      {
        try {
          Thread.sleep(30L);
        }
        catch (InterruptedException e) {
          Debug.printExc(e.getMessage());
        } 
        if (this.bCodeKeyNego == true) {
          break;
        }
      }
    }
    else {
      System.out.println("requestVMCodeKey noAuth VMM, bladeNO " + this.bladeNO);
    } 
  }
  private void createVMLink(int deviceType, String serverIP, int serverPort, byte[] certifyID, byte[] salt, boolean bSecret) {
    if (this.cdrom == deviceType) {
      this.vmApplet.creatVMLink(deviceType, this.cdromPath, serverIP, serverPort, certifyID, salt, this.bCodeKeyNego, bSecret, this.isFWP, this.localDirName, this.memoryStruct, this.util, this.srcType);
      if (null == this.cdVMlink)
      {
        this.cdVMlink = new Timer("cdrom createVMLink");
      }
      TimerTask task = new TimerTask()
        {
          public void run()
          {
            VirtualMedia.this.checkCdromVMConsole();
            VirtualMedia.this.cdVMlink.cancel();
            VirtualMedia.this.cdVMlink = null;
          }
        };
      this.cdVMlink.schedule(task, 7000L);
    } 
    if (this.floppy == deviceType) {
      this.vmApplet.creatVMLink(deviceType, this.floppyPath, serverIP, serverPort, certifyID, salt, this.bCodeKeyNego, bSecret, this.isFWP, null, null, null, this.srcType);
      if (null == this.flpVmlink)
      {
        this.flpVmlink = new Timer("floppy createVMLink");
      }
      TimerTask task = new TimerTask()
        {
          public void run()
          {
            VirtualMedia.this.checkFloppyVMConsole();
            VirtualMedia.this.flpVmlink.cancel();
            VirtualMedia.this.flpVmlink = null;
          }
        };
      this.flpVmlink.schedule(task, 7000L);
    } 
  }
  private void floppyItemsUpdate(String type) {
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
  private void cdromItemsUpdate(String type) {
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
        this.cdLocalText.setEnabled(true);
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
      TimerTask task = new TimerTask()
        {
          public void run()
          {
            VirtualMedia.this.updateItemForDisCon(VirtualMedia.this.cdrom);
            VirtualMedia.this.cie.setEnabled(false);
            VirtualMedia.this.cie.setText(VirtualMedia.this.util.getResource("flp_cd_pop_up_program"));
            VirtualMedia.this.isCdBtnForEj = true;
            VirtualMedia.this.cdCon.setEnabled(true);
          }
        };
      t.schedule(task, 2000L);
    } 
    if (deviceType == this.floppy) {
      this.vmApplet.destroyVMLink(deviceType);
      this.fc.setEnabled(false);
      this.fitext.setEnabled(false);
      this.fie.setEnabled(false);
      this.flpSkim.setEnabled(false);
      this.fcb.setEnabled(false);
      Timer t = new Timer("floppy disconnectVMLink");
      TimerTask task = new TimerTask()
        {
          public void run()
          {
            VirtualMedia.this.updateItemForDisCon(VirtualMedia.this.floppy);
            VirtualMedia.this.fie.setEnabled(false);
            VirtualMedia.this.fcb.setEnabled(true);
            VirtualMedia.this.isFlpBtnForEj = true;
          }
        };
      t.schedule(task, 2000L);
    } 
    this.negotiCodeKey = null;
    this.bCodeKeyNego = false;
  }
  private void updateItemForDisCon(int deviceType) {
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
  private void checkFloppyVMConsole() {
    if (!this.vmApplet.isConsoleOK(this.floppy)) {
      if (this.vmApplet.isVMLinkCrt(this.floppy))
      {
        Timer date = new Timer("floppy checkVMConsole");
        TimerTask task = new TimerTask()
          {
            public void run()
            {
              VirtualMedia.this.disableConntedBtn(VirtualMedia.this.floppy);
              if (null == VirtualMedia.this.floppyVmlink)
              {
                VirtualMedia.this.floppyVmlink = new javax.swing.Timer(1000, VirtualMedia.this.doFloppyVMlink());
              }
              VirtualMedia.this.floppyVmlink.start();
            }
          };
        date.schedule(task, 800L);
      }
      else
      {
        this.isFlpBtnForCon = true;
        this.fc.setEnabled(true);
        this.fcb.setEnabled(true);
        this.fd.setEnabled(true);
        this.fr.setEnabled(true);
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
        JOptionPane.showMessageDialog(this.flp, this.util.getResource("com.huawei.vm.console.out.423") + this.vmApplet.getConsoleStatement(this.floppy));
      }
      this.isFlpBtnForCon = true;
      this.fc.setEnabled(true);
      this.fcb.setEnabled(true);
      this.fd.setEnabled(true);
      this.fr.setEnabled(true);
      if (this.fd.isSelected()) {
        this.fdSelect.setEnabled(true);
      }
      else {
        this.fitext.setEnabled(true);
        this.flpSkim.setEnabled(true);
      } 
    } 
  }
  private void disableConntedBtn(int deviceType) {
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
  private boolean validateCdrom() {
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
      String cdromImagePath = this.cdromPath.toLowerCase();
      if (this.cdromPath.length() <= 4 || !".iso".equals(cdromImagePath.substring(this.cdromPath.length() - 4))) {
        JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.416"));
        return false;
      } 
      if (!file.exists()) {
        JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.430"));
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
  private void checkCdromVMConsole() {
    if (!this.vmApplet.isConsoleOK(this.cdrom)) {
      if (this.vmApplet.isVMLinkCrt(this.cdrom))
      {
        Timer date = new Timer("cdrom checkVMConsole");
        TimerTask task = new TimerTask()
          {
            public void run()
            {
              VirtualMedia.this.disableConntedBtn(VirtualMedia.this.cdrom);
              if (null == VirtualMedia.this.cdRomVMlink)
              {
                VirtualMedia.this.cdRomVMlink = new javax.swing.Timer(1000, VirtualMedia.this.doCdRomVMlink());
              }
              VirtualMedia.this.cdRomVMlink.start();
            }
          };
        date.schedule(task, 800L);
      }
      else
      {
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
        else if (this.cdLocal.isSelected()) {
          this.cdLocalSkim.setEnabled(true);
          this.cdLocalText.setEnabled(false);
        } 
        this.vmApplet.destroyVMLink(this.cdrom);
      }
    } else {
      int state = this.vmApplet.getConsoleState(this.cdrom);
      if (0 != state)
      {
        JOptionPane.showMessageDialog(this.cdp, this.util.getResource("com.huawei.vm.console.out.422") + this.vmApplet.getConsoleStatement(this.cdrom));
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
  private Action doFloppyVMlink() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (!VirtualMedia.this.vmApplet.isVMLinkCrt(VirtualMedia.this.floppy)) {
            VirtualMedia.this.floppyVmlink.stop();
            VirtualMedia.this.floppyVmlink = null;
            VirtualMedia.this.disconnectVMLink(VirtualMedia.this.floppy);
          } 
        }
      };
    return action;
  }
  private Action doCdRomVMlink() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (!VirtualMedia.this.vmApplet.isVMLinkCrt(VirtualMedia.this.cdrom)) {
            VirtualMedia.this.storageDevice = null;
            VirtualMedia.this.cdRomVMlink.stop();
            VirtualMedia.this.cdRomVMlink = null;
            VirtualMedia.this.disconnectVMLink(VirtualMedia.this.cdrom);
          } 
          if (null == VirtualMedia.this.storageDevice)
          {
            VirtualMedia.this.storageDevice = VirtualMedia.this.vmApplet.getConsole().getCdrom();
          }
          if (null != VirtualMedia.this.storageDevice && VirtualMedia.this.storageDevice.isIsoDiskChanged) {
            if (VirtualMedia.this.cd.isSelected())
            {
              VirtualMedia.this.cdromISOEject();
            }
            if (VirtualMedia.this.cdLocal.isSelected())
            {
              VirtualMedia.this.cdLocalISOEject();
            }
            VirtualMedia.this.storageDevice.isIsoDiskChanged = false;
          } 
        }
      };
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
    System.gc();
  }
  public String getStrIP() {
    return this.strIP;
  }
  public void setStrIP(String strIP) {
    this.strIP = strIP;
  }
  public byte[] getCodeKey() {
    if (this.bCodeKeyNego)
    {
      return this.negotiCodeKey;
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
  public void setNegotiCodeKey(byte[] codeKey) {
    if (codeKey.length > 0) {
      this.negotiCodeKey = codeKey;
      this.bCodeKeyNego = true;
    } 
  }
  public byte[] getSalt() {
    return this.negoSalt;
  }
  public void setNegotiSalt(byte[] salt) {
    this.negoSalt = salt;
  }
  public boolean getSecretVMM() {
    return this.bSecretVMM;
  }
  public void setSecretVMM(boolean bSecret) {
    this.bSecretVMM = bSecret;
  }
  public int getPort() {
    return this.port;
  }
  public void setPort(int port) {
    this.port = port;
  }
  public MouseAdapter fdmouseClicked() {
    MouseAdapter mouse = new MouseAdapter()
      {
        public void mouseClicked(MouseEvent e)
        {
          if (VirtualMedia.this.fdSelect.isEnabled()) {
            String floppy = VirtualMedia.this.vmApplet.getFloppyDevices();
            if (!floppy.equals(VirtualMedia.this.floppys)) {
              VirtualMedia.this.fdSelect.removeAllItems();
              if (null == floppy || "".equals(floppy)) {
                VirtualMedia.this.floppys = floppy;
                VirtualMedia.this.fdSelect.addItem(VirtualMedia.this.util.getResource("flp_cd_none"));
              }
              else {
                VirtualMedia.this.floppys = floppy;
                String[] fdSplip = VirtualMedia.this.floppys.split(":");
                for (int i = 0; i < fdSplip.length; i++) {
                  if (!"".equals(fdSplip[i]))
                  {
                    VirtualMedia.this.fdSelect.addItem(fdSplip[i] + ":");
                  }
                } 
              } 
            } 
          } 
        }
        public void mouseEntered(MouseEvent e) {
          mouseClicked(e);
        }
      };
    return mouse;
  }
  public MouseAdapter cdmouseClicked() {
    MouseAdapter mouse = new MouseAdapter()
      {
        public void mouseClicked(MouseEvent e)
        {
          if (VirtualMedia.this.cdSelect.isEnabled()) {
            String cdrom = VirtualMedia.this.vmApplet.getCDROMDevices();
            if (!cdrom.equals(VirtualMedia.this.cdroms)) {
              VirtualMedia.this.cdSelect.removeAllItems();
              if (null == cdrom || "".equals(cdrom)) {
                VirtualMedia.this.cdroms = cdrom;
                VirtualMedia.this.cdSelect.addItem(VirtualMedia.this.util.getResource("flp_cd_none"));
              }
              else {
                VirtualMedia.this.cdroms = cdrom;
                String[] cdSplip = cdrom.split(":");
                for (int i = 0; i < cdSplip.length; i++) {
                  if (!"".equals(cdSplip[i]))
                  {
                    VirtualMedia.this.cdSelect.addItem(cdSplip[i] + ":");
                  }
                } 
              } 
            } 
          } 
        }
        public void mouseEntered(MouseEvent e) {
          mouseClicked(e);
        }
      };
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
      this.floppyVmlink.stop();
      this.floppyVmlink = null;
      disconnectVMLink(this.floppy);
    } 
    this.vmApplet.destroy();
    this.vmApplet.stop();
    System.gc();
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
