package com.kvmV1;
import com.huawei.vm.console.managementV1.ConsoleControllers;
import com.huawei.vm.console.uiV1.JAVAFileFileter;
import com.huawei.vm.console.utilsV1.ResourceUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
public class ImageFile
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  public boolean isImageCreate = true;
  private String deviceForCreateImage = "";
  private String imageForCreateImage = "";
  private String floppys = "";
  private String cdroms = "";
  private String cdflps = "";
  private String image = "";
  private ConsoleControllers vmApplet = null;
  private JButton imageSaveBtn;
  private JButton imageCreate;
  public JProgressBar imagebar;
  private JLabel imagPan;
  private JLabel imageQd;
  private JLabel createJd;
  private JTextField imagePath;
  private ResourceUtil util;
  private JComboBox imageCheck;
  public Timer t = null;
  private Timer m = null;
  private boolean isMulBtnForCon = true;
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
      if (Base.local.equalsIgnoreCase("en"))
      {
        this.imageQd.setBounds(8, 4, 65, 20);
        this.imageCheck.setBounds(64, 4, 85, 18);
        this.imagPan.setBounds(160, 4, 35, 20);
        this.imagePath.setBounds(188, 4, 62, 18);
        this.imageSaveBtn.setBounds(256, 4, 69, 18);
      }
      else
      {
        this.imageQd.setBounds(8, 4, 65, 20);
        this.imageCheck.setBounds(64, 4, 70, 18);
        this.imagPan.setBounds(138, 4, 55, 20);
        this.imagePath.setBounds(188, 4, 62, 18);
        this.imageSaveBtn.setBounds(256, 4, 69, 18);
      }
    }
    else {
      this.imageQd.setBounds(8, 4, 65, 20);
      this.imageCheck.setBounds(68, 4, 50, 18);
      this.imagPan.setBounds(131, 4, 55, 20);
      this.imagePath.setBounds(188, 4, 60, 18);
      this.imageSaveBtn.setBounds(256, 4, 69, 18);
    } 
    this.createJd.setBounds(8, 23, 65, 20);
    this.imagebar.setBounds(68, 26, 180, 16);
    this.imageCreate.setBounds(256, 25, 69, 18);
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
        e.getStackTrace();
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
          comboBox.addItem(flplit[i] + ":");
        }
      } 
      String[] cdsplit = this.cdroms.split(":");
      for (int j = 0; j < cdsplit.length; j++) {
        if (!"".equals(cdsplit[j]))
        {
          comboBox.addItem(cdsplit[j] + ":");
        }
      } 
    } 
    comboBox.setPreferredSize(new Dimension(0, 0));
    comboBox.setEnabled(true);
    return comboBox;
  }
  private Action fileChoose() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          JFileChooser choose = new JFileChooser(ImageFile.this.image);
          choose.addChoosableFileFilter((FileFilter)new JAVAFileFileter("img", "iso"));
          choose.setFileSelectionMode(0);
          int returnval = choose.showSaveDialog(choose);
          if (returnval == 0) {
            String cdRomdervaer = ImageFile.this.vmApplet.getCDROMDevices();
            String derver = (String)ImageFile.this.imageCheck.getSelectedItem();
            if (cdRomdervaer.contains(derver)) {
              String absolutePath = choose.getSelectedFile().getAbsolutePath().toLowerCase();
              int length = absolutePath.length();
              int isoIndex = absolutePath.lastIndexOf(".iso");
              int imgIndex = absolutePath.lastIndexOf(".img");
              if (-1 != isoIndex || isoIndex + 4 == length) {
                ImageFile.this.imagePath.setText(choose.getSelectedFile().getAbsolutePath());
              }
              else if (-1 != imgIndex || imgIndex + 4 == length) {
                ImageFile.this.imagePath.setText(choose.getSelectedFile().getAbsolutePath());
              }
              else {
                ImageFile.this.imagePath.setText(choose.getSelectedFile().getAbsolutePath() + ".iso");
              } 
              ImageFile.this.image = choose.getSelectedFile().getParent();
            }
            else {
              String cdAbsoluPath = choose.getSelectedFile().getAbsolutePath().toLowerCase();
              int length = cdAbsoluPath.length();
              int isoIndex = cdAbsoluPath.lastIndexOf(".iso");
              int imgIndex = cdAbsoluPath.lastIndexOf(".img");
              if (-1 != isoIndex || isoIndex + 4 == length) {
                ImageFile.this.imagePath.setText(choose.getSelectedFile().getAbsolutePath());
              }
              else if (-1 != imgIndex || imgIndex + 4 == length) {
                ImageFile.this.imagePath.setText(choose.getSelectedFile().getAbsolutePath());
              }
              else {
                ImageFile.this.imagePath.setText(choose.getSelectedFile().getAbsolutePath() + ".img");
              } 
              ImageFile.this.image = choose.getSelectedFile().getParent();
            } 
          } 
        }
      };
    return action;
  }
  private Action createImage() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent event) {
          if (ImageFile.this.isImageCreate) {
            ImageFile.this.vmApplet.threadStart("create image");
            if (ImageFile.this.vmApplet.isImageCreateOK() && null != ImageFile.this.imagePath.getText() && !"".equals(ImageFile.this.imagePath.getText()) && !ImageFile.this.util.getResource("flp_cd_none").equals(ImageFile.this.imageCheck.getSelectedItem())) {
              if (!ImageFile.this.suffixCheck()) {
                return;
              }
              if (ImageFile.this.vmApplet.checkFileExsit(ImageFile.this.imagePath.getText()))
              {
                int result = JOptionPane.showConfirmDialog(ImageFile.this.imageCreate, ImageFile.this.imagePath.getText() + " " + ImageFile.this.util.getResource("isImageCreateOK"), UIManager.getString("OptionPane.titleText"), 0);
                ImageFile.this.deviceForCreateImage = (String)ImageFile.this.imageCheck.getSelectedItem();
                ImageFile.this.imageForCreateImage = ImageFile.this.imagePath.getText();
                if (result == 0)
                {
                  ImageFile.this.vmApplet.createImageFile(ImageFile.this.deviceForCreateImage, ImageFile.this.imageForCreateImage);
                  ImageFile.this.checkImageCreate();
                }
                else
                {
                  return;
                }
              }
              else
              {
                ImageFile.this.deviceForCreateImage = (String)ImageFile.this.imageCheck.getSelectedItem();
                ImageFile.this.imageForCreateImage = ImageFile.this.imagePath.getText();
                ImageFile.this.vmApplet.createImageFile(ImageFile.this.deviceForCreateImage, ImageFile.this.imageForCreateImage);
                ImageFile.this.checkImageCreate();
              }
            }
            else if (!ImageFile.this.vmApplet.isLibOK()) {
              ImageFile.this.updateCreateImageItems("disable");
              JOptionPane.showMessageDialog(ImageFile.this.imageCreate, ImageFile.this.vmApplet.getStatement(ImageFile.this.vmApplet.getVmState()));
            }
            else if (!ImageFile.this.vmApplet.isImageCreateOK()) {
              ImageFile.this.updateCreateImageItems("stop");
            }
            else {
              if (null == ImageFile.this.imagePath.getText() || "".equals(ImageFile.this.imagePath.getText())) {
                JOptionPane.showMessageDialog(ImageFile.this.imageCreate, ImageFile.this.util.getResource("com.huawei.vm.console.out.401"));
                return;
              } 
              JOptionPane.showMessageDialog(ImageFile.this.imageCreate, ImageFile.this.util.getResource("com.huawei.vm.console.out.402"));
              return;
            } 
          } else {
            ImageFile.this.imageCreate.setEnabled(false);
            int result = JOptionPane.showConfirmDialog(ImageFile.this.imageCreate, ImageFile.this.util.getResource("com.huawei.vm.console.out.403"), UIManager.getString("OptionPane.titleText"), 0);
            if (0 == result) {
              ImageFile.this.t.stop();
              ImageFile.this.isStop();
            }
            else {
              ImageFile.this.imageCreate.setEnabled(true);
            } 
          } 
        }
      };
    return action;
  }
  public void isStop() {
    this.vmApplet.stopImageCreate();
    this.m.start();
  }
  private Action isStopCreate() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          if (!ImageFile.this.vmApplet.isImageCreateOK()) {
            return;
          }
          ImageFile.this.t.stop();
          ImageFile.this.m.stop();
          ImageFile.this.imageCreate.setEnabled(true);
          ImageFile.this.imagebar.setValue(0);
          ImageFile.this.updateCreateImageItems("stop");
        }
      };
    return action;
  }
  private Action timerMethod() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent arg0) {
          int res = ImageFile.this.vmApplet.getImageCreateProcess();
          if (ImageFile.this.isMulBtnForCon) {
            if (res > 100) {
              ImageFile.this.t.stop();
              ImageFile.this.imagebar.setValue(0);
              ImageFile.this.updateCreateImageItems("stop");
              JOptionPane.showMessageDialog(ImageFile.this.imageCreate, ImageFile.this.vmApplet.getStatement(res));
            }
            else {
              int value = ImageFile.this.imagebar.getValue();
              if (value < 100)
              {
                ImageFile.this.imagebar.setValue(res);
              }
              else if (value == 100)
              {
                ImageFile.this.imagePath.setText(ImageFile.this.vmApplet.getAbsoluteImagePath());
                JOptionPane.showMessageDialog(ImageFile.this.imageCreate, ImageFile.this.util.getResource("create_succeed"));
                ImageFile.this.t.stop();
                ImageFile.this.imagebar.setValue(0);
                ImageFile.this.imageSaveBtn.setEnabled(true);
                ImageFile.this.imageCheck.setEnabled(true);
                ImageFile.this.imagePath.setEnabled(true);
                ImageFile.this.imageCreate.setText(ImageFile.this.util.getResource("make_image_file_make"));
                ImageFile.this.isImageCreate = true;
              }
            } 
          } else {
            ImageFile.this.t.stop();
            ImageFile.this.imagebar.setValue(0);
            ImageFile.this.imageCreate.setText(ImageFile.this.util.getResource("make_image_file_make"));
            ImageFile.this.isImageCreate = true;
          } 
        }
      };
    return action;
  }
  private void checkImageCreate() {
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
  private void updateCreateImageItems(String type) {
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
  private boolean suffixCheck() {
    String cdType = this.vmApplet.getCDROMDevices();
    String imageType = (String)this.imageCheck.getSelectedItem();
    String fileName = this.imagePath.getText().toLowerCase();
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
    MouseAdapter mouse = new MouseAdapter()
      {
        public void mouseClicked(MouseEvent e)
        {
          if (ImageFile.this.imageCheck.isEnabled()) {
            String cdrom = ImageFile.this.vmApplet.getCDROMDevices();
            String floppy = ImageFile.this.vmApplet.getFloppyDevices();
            String cdromFloppy = cdrom + floppy;
            if (!ImageFile.this.cdflps.equals(cdromFloppy)) {
              ImageFile.this.cdflps = cdromFloppy;
              if ("".equals(cdrom) && "".equals(floppy)) {
                ImageFile.this.imageCheck.removeAllItems();
                ImageFile.this.floppys = null;
                ImageFile.this.cdroms = null;
                ImageFile.this.imageCheck.addItem(ImageFile.this.util.getResource("flp_cd_none"));
              }
              else {
                ImageFile.this.imageCheck.removeAllItems();
                String[] flpSplit = floppy.split(":");
                for (int i = 0; i < flpSplit.length; i++) {
                  if (!"".equals(flpSplit[i]))
                  {
                    ImageFile.this.imageCheck.addItem(flpSplit[i] + ":");
                  }
                } 
                String[] cdSplit = cdrom.split(":");
                for (int j = 0; j < cdSplit.length; j++) {
                  if (!"".equals(cdSplit[j]))
                  {
                    ImageFile.this.imageCheck.addItem(cdSplit[j] + ":");
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
}
