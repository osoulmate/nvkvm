package com.kvm;
import com.huawei.vm.console.management.ConsoleControllers;
import com.huawei.vm.console.utils.ResourceUtil;
import com.library.LoggerUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
public class ImageFile
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  private boolean isImageCreate = true;
  public boolean isImageCreate() {
    return this.isImageCreate;
  }
  public void setImageCreate(boolean isImageCreate) {
    this.isImageCreate = isImageCreate;
  }
  private String deviceForCreateImage = "";
  public String getDeviceForCreateImage() {
    return this.deviceForCreateImage;
  }
  public void setDeviceForCreateImage(String deviceForCreateImage) {
    this.deviceForCreateImage = deviceForCreateImage;
  }
  private String imageForCreateImage = "";
  public String getImageForCreateImage() {
    return this.imageForCreateImage;
  }
  public void setImageForCreateImage(String imageForCreateImage) {
    this.imageForCreateImage = imageForCreateImage;
  }
  private String floppys = "";
  public String getFloppys() {
    return this.floppys;
  }
  public void setFloppys(String floppys) {
    this.floppys = floppys;
  }
  private String cdroms = "";
  public String getCdroms() {
    return this.cdroms;
  }
  public void setCdroms(String cdroms) {
    this.cdroms = cdroms;
  }
  private String cdflps = "";
  public String getCdflps() {
    return this.cdflps;
  }
  public void setCdflps(String cdflps) {
    this.cdflps = cdflps;
  }
  private String image = "";
  public String getImage() {
    return this.image;
  }
  public void setImage(String image) {
    this.image = image;
  }
  private transient ConsoleControllers vmApplet = null; private JButton imageSaveBtn; private JButton imageCreate; private JProgressBar imagebar;
  private JLabel imagPan;
  public ConsoleControllers getVmApplet() {
    return this.vmApplet;
  }
  private JLabel imageQd; private JLabel createJd; private JTextField imagePath; private transient ResourceUtil util; private JComboBox imageCheck;
  public void setVmApplet(ConsoleControllers vmApplet) {
    this.vmApplet = vmApplet;
  }
  public JButton getImageSaveBtn() {
    return this.imageSaveBtn;
  }
  public void setImageSaveBtn(JButton imageSaveBtn) {
    this.imageSaveBtn = imageSaveBtn;
  }
  public JButton getImageCreate() {
    return this.imageCreate;
  }
  public void setImageCreate(JButton imageCreate) {
    this.imageCreate = imageCreate;
  }
  public JProgressBar getImagebar() {
    return this.imagebar;
  }
  public void setImagebar(JProgressBar imagebar) {
    this.imagebar = imagebar;
  }
  public JTextField getImagePath() {
    return this.imagePath;
  }
  public void setImagePath(JTextField imagePath) {
    this.imagePath = imagePath;
  }
  public ResourceUtil getUtil() {
    return this.util;
  }
  public void setUtil(ResourceUtil util) {
    this.util = util;
  }
  public JComboBox getImageCheck() {
    return this.imageCheck;
  }
  public void setImageCheck(JComboBox imageCheck) {
    this.imageCheck = imageCheck;
  }
  private Timer t = null;
  public Timer getT() {
    return this.t;
  }
  public void setT(Timer t) {
    this.t = t;
  }
  private Timer m = null;
  public Timer getM() {
    return this.m;
  }
  public void setM(Timer m) {
    this.m = m;
  }
  private boolean isMulBtnForCon = true;
  public boolean isMulBtnForCon() {
    return this.isMulBtnForCon;
  }
  public void setMulBtnForCon(boolean isMulBtnForCon) {
    this.isMulBtnForCon = isMulBtnForCon;
  }
  public ImageFile(String language) {
    uiManager();
    setLayout((LayoutManager)null);
    setSize(337, 49);
    this.m = new Timer(100, isStopCreate());
    this.t = new Timer(1000, timerMethod());
    setBackground(new Color(204, 227, 242));
    this.util = new ResourceUtil();
    this.vmApplet = new ConsoleControllers(true);
    this.util.dosetLanguage(language);
    this.vmApplet.setLanguage(language);
    this.vmApplet.getConsole().setVmm_compress_state(Base.getVmm_compress_state());
    this.imageQd = createLabel(this.util.getResource("make_image_file_select_driver"));
    this.imageCheck = createComboBox();
    this.imageCheck.addMouseListener(imageMouseClicked());
    Component c = this.imageCheck.getComponent(0);
    c.addMouseListener(imageMouseClicked());
    this.imagPan = createLabel(this.util.getResource("make_image_file_saved_path"));
    this.imagePath = createTextFiele(0, true);
    this.imageSaveBtn = createButton(this.util.getResource("flp_cd_skim_over"), true, fileChoose());
    this.createJd = createLabel(this.util.getResource("make_image_file_make_process"));
    this.imagebar = createProgressBar();
    this.imageCreate = createButton(this.util.getResource("make_image_file_make"), true, createImage());
    if (KVMUtil.isLinuxOS()) {
      if (Base.getLocal().equalsIgnoreCase("en"))
      {
        this.imageQd.setBounds(8, 4, 65, 20);
        this.imageCheck.setBounds(74, 4, 85, 18);
        this.imagPan.setBounds(170, 4, 35, 20);
        this.imagePath.setBounds(198, 4, 62, 18);
        this.imageSaveBtn.setBounds(266, 4, 69, 18);
      }
      else
      {
        this.imageQd.setBounds(8, 4, 65, 20);
        this.imageCheck.setBounds(74, 4, 70, 18);
        this.imagPan.setBounds(148, 4, 55, 20);
        this.imagePath.setBounds(198, 4, 62, 18);
        this.imageSaveBtn.setBounds(266, 4, 69, 18);
      }
    }
    else {
      this.imageQd.setBounds(8, 4, 65, 20);
      this.imageCheck.setBounds(78, 4, 50, 18);
      this.imagPan.setBounds(141, 4, 55, 20);
      this.imagePath.setBounds(198, 4, 60, 18);
      this.imageSaveBtn.setBounds(266, 4, 69, 18);
    } 
    this.createJd.setBounds(8, 23, 65, 20);
    this.imagebar.setBounds(78, 26, 180, 16);
    this.imageCreate.setBounds(266, 25, 69, 18);
    this.imageQd.setBackground(new Color(204, 227, 242));
    this.imagPan.setBackground(new Color(204, 227, 242));
    this.imagePath.setBackground(new Color(204, 227, 242));
    this.imageSaveBtn.setBackground(new Color(204, 227, 242));
    this.createJd.setBackground(new Color(204, 227, 242));
    this.imagebar.setBackground(new Color(204, 227, 242));
    this.imageCreate.setBackground(new Color(204, 227, 242));
    add(this.imageQd);
    add(this.imageCheck);
    add(this.imagPan);
    add(this.imageSaveBtn);
    add(this.imagePath);
    add(this.imagebar);
    add(this.createJd);
    add(this.imageCreate);
    setVisible(true);
  }
  private void uiManager() {
    if (KVMUtil.isWindowsOS()) {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e) {
        LoggerUtil.error(e.getClass().getName());
      } 
    }
  }
  public void doStopImageCreate() {
    this.t.stop();
    isStop();
  }
  private JProgressBar createProgressBar() {
    JProgressBar progressBar = new JProgressBar();
    progressBar.setMinimum(0);
    progressBar.setMaximum(100);
    progressBar.setValue(0);
    progressBar.setStringPainted(true);
    progressBar.setPreferredSize(new Dimension(1, 2));
    return progressBar;
  }
  private JTextField createTextFiele(int x, boolean result) {
    JTextField textField = new JTextField(x);
    textField.setEnabled(result);
    return textField;
  }
  private JButton createButton(String setName, boolean result, ActionListener action) {
    JButton button = new JButton();
    button.setText(setName);
    button.addActionListener(action);
    button.setEnabled(result);
    return button;
  }
  private JLabel createLabel(String setName) {
    JLabel label = new JLabel();
    label.setText(setName);
    return label;
  }
  private JComboBox createComboBox() {
    JComboBox<String> comboBox = new JComboBox();
    this.floppys = this.vmApplet.getFloppyDevices();
    this.cdroms = this.vmApplet.getCDROMDevices();
    this.cdflps = this.floppys + this.cdroms;
    if ("".equals(this.floppys) && "".equals(this.cdroms)) {
      this.floppys = null;
      this.cdroms = null;
      comboBox.addItem(this.util.getResource("flp_cd_none"));
    }
    else {
      String[] flplit = this.floppys.split(":");
      for (int i = 0; i < flplit.length; i++) {
        if (!"".equals(flplit[i]))
        {
          comboBox.addItem(flplit[i] + ':');
        }
      } 
      String[] cdsplit = this.cdroms.split(":");
      for (int j = 0; j < cdsplit.length; j++) {
        if (!"".equals(cdsplit[j]))
        {
          comboBox.addItem(cdsplit[j] + ':');
        }
      } 
    } 
    comboBox.setPreferredSize(new Dimension(0, 0));
    comboBox.setEnabled(true);
    return comboBox;
  }
  private Action fileChoose() {
    Action action = new ChooseAction(this);
    return action;
  }
  private Action createImage() {
    Action action = new CreatAction(this);
    return action;
  }
  public void isStop() {
    this.vmApplet.stopImageCreate();
    this.m.start();
  }
  private Action isStopCreate() {
    Action action = new StopAction(this);
    return action;
  }
  private Action timerMethod() {
    Action action = new TimerAction(this);
    return action;
  }
  protected void checkImageCreate() {
    int process = this.vmApplet.getImageCreateProcess();
    if (!this.vmApplet.isImageCreateOK() && 0 <= process && process < 100) {
      this.t.start();
      this.imagebar.setValue(0);
      updateCreateImageItems("create");
    }
    else if (100 == process) {
      this.imagebar.setValue(0);
      JOptionPane.showMessageDialog(this, this.util.getResource("com.huawei.vm.console.out.420"));
      updateCreateImageItems("stop");
    }
    else {
      if (0 > process || process > 100)
      {
        JOptionPane.showMessageDialog(this, this.vmApplet.getStatement(process));
      }
      this.t.stop();
      updateCreateImageItems("stop");
    } 
  }
  protected void updateCreateImageItems(String type) {
    if ("create".equals(type)) {
      this.imageCreate.setText(this.util.getResource("make_image_file_stop"));
      this.imageCheck.setEnabled(false);
      this.imagePath.setEnabled(false);
      this.imageSaveBtn.setEnabled(false);
      this.isImageCreate = false;
    }
    else if ("stop".equals(type)) {
      this.imageCreate.setText(this.util.getResource("make_image_file_make"));
      this.imageCheck.setEnabled(true);
      this.imageSaveBtn.setEnabled(true);
      this.imagePath.setEnabled(true);
      this.isImageCreate = true;
    }
    else if ("disable".equals(type)) {
      this.imagePath.setEnabled(true);
      this.imageSaveBtn.setEnabled(true);
      this.imageCheck.setEnabled(true);
    } 
  }
  protected boolean suffixCheck() {
    String cdType = this.vmApplet.getCDROMDevices();
    String imageType = (String)this.imageCheck.getSelectedItem();
    String fileName = this.imagePath.getText().toLowerCase(getLocale());
    int length = fileName.length();
    if (cdType.contains(imageType)) {
      int isoindex = fileName.lastIndexOf(".iso");
      if (-1 == isoindex || isoindex + 4 != length)
      {
        JOptionPane.showMessageDialog(this, this.util.getResource("com.huawei.vm.console.out.418"));
        return false;
      }
    } else {
      int isoindex = fileName.lastIndexOf(".img");
      if (-1 == isoindex || isoindex + 4 != length) {
        JOptionPane.showMessageDialog(this, this.util.getResource("com.huawei.vm.console.out.419"));
        return false;
      } 
    } 
    return true;
  }
  public MouseAdapter imageMouseClicked() {
    MouseAdapter mouse = new ImageMouseAdapter(this);
    return mouse;
  }
}
