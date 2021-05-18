package com.kvmV1;
import com.KinescopeV1.KinescopeDataCollect;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
public class PowerPopupMenu
  extends JPopupMenu
{
  private static final long serialVersionUID = 1L;
  public KVMInterface kvmInterface;
  public JMenuItem poweroffMenu;
  public JMenuItem poweronMenu;
  public JMenuItem restartMenu;
  public JMenuItem safetyRestartMenu;
  public JMenuItem usbResetMenu;
  public JMenuItem savePowerOffMenu;
  public KinescopeDataCollect kineScopeDataCollect = null;
  public JMenuItem localKinescopeMenu;
  public boolean dissflag = false;
  public boolean isIdiss = false;
  public JCheckBoxMenuItem mouseModeSwitchMenu;
  public JCheckBoxMenuItem SingleMouseMenu;
  public PowerPopupMenu(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
    this.poweronMenu = new JMenuItem(kvmInterface.kvmUtil.getString("Power_On"));
    if (kvmInterface.getProductType().equals("BMC")) {
      this.poweroffMenu = new JMenuItem(kvmInterface.kvmUtil.getString("Power_Off_BMC"));
    }
    else {
      this.poweroffMenu = new JMenuItem(kvmInterface.kvmUtil.getString("Power_Off_OSCA"));
    } 
    this.restartMenu = new JMenuItem(kvmInterface.kvmUtil.getString("Cold_Reset"));
    this.safetyRestartMenu = new JMenuItem(kvmInterface.kvmUtil.getString("Graceful_Reboot"));
    this.usbResetMenu = new JMenuItem(kvmInterface.kvmUtil.getString("Usb_Reset"));
    if (kvmInterface.getProductType().equals("BMC"))
    {
      this.savePowerOffMenu = new JMenuItem(kvmInterface.kvmUtil.getString("Graceful_poweroff"));
    }
    this.localKinescopeMenu = new JMenuItem(kvmInterface.kvmUtil.getString("localKinescope"));
    this.localKinescopeMenu.addActionListener(kinescopeAction());
    this.mouseModeSwitchMenu = new JCheckBoxMenuItem(kvmInterface.kvmUtil.getString("mouse_mode_switch"));
    if (Base.isSynMouse) {
      this.mouseModeSwitchMenu.setSelected(true);
    }
    else {
      this.mouseModeSwitchMenu.setSelected(false);
    } 
    this.SingleMouseMenu = new JCheckBoxMenuItem(kvmInterface.kvmUtil.getString("single_mouse"));
    if (Base.isSingleMouse) {
      this.SingleMouseMenu.setSelected(true);
    }
    else {
      this.SingleMouseMenu.setSelected(false);
    } 
    add(this.poweronMenu);
    add(this.poweroffMenu);
    if (kvmInterface.getProductType().equals("BMC"))
    {
      add(this.savePowerOffMenu);
    }
    add(this.restartMenu);
    add(this.safetyRestartMenu);
    if (!"PS2".equals(Base.TYPE))
    {
      add(this.usbResetMenu);
    }
    add(this.localKinescopeMenu);
    if ("USB".equals(Base.TYPE)) {
      add(this.SingleMouseMenu);
      add(this.mouseModeSwitchMenu);
    } 
    this.poweronMenu.addActionListener(poweronAction());
    this.poweroffMenu.addActionListener(poweroffAction());
    if (kvmInterface.getProductType().equals("BMC"))
    {
      this.savePowerOffMenu.addActionListener(savePowerOffAction());
    }
    this.restartMenu.addActionListener(restartAction());
    this.safetyRestartMenu.addActionListener(safetyRestartAction());
    this.usbResetMenu.addActionListener(usbResetAction());
    this.SingleMouseMenu.addActionListener(singleMouseSwitchAction());
    this.mouseModeSwitchMenu.addActionListener(mouseModeSwitchAction());
    setBackground(new Color(158, 202, 232));
    this.poweroffMenu.setBackground(new Color(204, 227, 242));
    this.poweronMenu.setBackground(new Color(204, 227, 242));
    if (kvmInterface.getProductType().equals("BMC"))
    {
      this.savePowerOffMenu.setBackground(new Color(204, 227, 242));
    }
    this.restartMenu.setBackground(new Color(204, 227, 242));
    this.safetyRestartMenu.setBackground(new Color(204, 227, 242));
    this.usbResetMenu.setBackground(new Color(204, 227, 242));
    this.localKinescopeMenu.setBackground(new Color(204, 227, 242));
    this.SingleMouseMenu.setBackground(new Color(204, 227, 242));
    this.mouseModeSwitchMenu.setBackground(new Color(204, 227, 242));
  }
  public Action poweronAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          BladeThread bladeThread = PowerPopupMenu.this.kvmInterface.base.threadGroup.get(String.valueOf(PowerPopupMenu.this.kvmInterface.actionBlade));
          int result = JOptionPane.showConfirmDialog(PowerPopupMenu.this.kvmInterface.floatToolbar.imagePanel, PowerPopupMenu.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
          if (0 == result)
          {
            bladeThread.bladeCommu.sentData((PowerPopupMenu.this.kvmInterface.kvmUtil.getImagePane(PowerPopupMenu.this.kvmInterface.actionBlade)).pack.kvmCmdPowerControl((byte)33, bladeThread.getBladeNO()));
          }
        }
      };
    return action;
  }
  public Action poweroffAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          BladeThread bladeThread = PowerPopupMenu.this.kvmInterface.base.threadGroup.get(String.valueOf(PowerPopupMenu.this.kvmInterface.actionBlade));
          int result = JOptionPane.showConfirmDialog(PowerPopupMenu.this.kvmInterface.floatToolbar.imagePanel, PowerPopupMenu.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
          if (0 == result)
          {
            bladeThread.bladeCommu.sentData((PowerPopupMenu.this.kvmInterface.kvmUtil.getImagePane(PowerPopupMenu.this.kvmInterface.actionBlade)).pack.kvmCmdPowerControl((byte)32, bladeThread.getBladeNO()));
          }
        }
      };
    return action;
  }
  public Action restartAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          BladeThread bladeThread = PowerPopupMenu.this.kvmInterface.base.threadGroup.get(String.valueOf(PowerPopupMenu.this.kvmInterface.actionBlade));
          int result = JOptionPane.showConfirmDialog(PowerPopupMenu.this.kvmInterface.floatToolbar.imagePanel, PowerPopupMenu.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
          if (0 == result)
          {
            bladeThread.bladeCommu.sentData((PowerPopupMenu.this.kvmInterface.kvmUtil.getImagePane(PowerPopupMenu.this.kvmInterface.actionBlade)).pack.kvmCmdPowerControl((byte)34, bladeThread.getBladeNO()));
          }
        }
      };
    return action;
  }
  public Action safetyRestartAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          BladeThread bladeThread = PowerPopupMenu.this.kvmInterface.base.threadGroup.get(String.valueOf(PowerPopupMenu.this.kvmInterface.actionBlade));
          int result = JOptionPane.showConfirmDialog(PowerPopupMenu.this.kvmInterface.floatToolbar.imagePanel, PowerPopupMenu.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
          if (0 == result)
          {
            bladeThread.bladeCommu.sentData((PowerPopupMenu.this.kvmInterface.kvmUtil.getImagePane(PowerPopupMenu.this.kvmInterface.actionBlade)).pack.kvmCmdPowerControl((byte)35, bladeThread.getBladeNO()));
          }
        }
      };
    return action;
  }
  public Action usbResetAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          BladeThread bladeThread = PowerPopupMenu.this.kvmInterface.base.threadGroup.get(String.valueOf(PowerPopupMenu.this.kvmInterface.actionBlade));
          int result = JOptionPane.showConfirmDialog(PowerPopupMenu.this.kvmInterface.floatToolbar.imagePanel, PowerPopupMenu.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
          if (0 == result)
          {
            bladeThread.bladeCommu.sentData((PowerPopupMenu.this.kvmInterface.kvmUtil.getImagePane(PowerPopupMenu.this.kvmInterface.actionBlade)).pack.kvmCmdPowerControl((byte)48, bladeThread.getBladeNO()));
          }
        }
      };
    return action;
  }
  public Action savePowerOffAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          BladeThread bladeThread = PowerPopupMenu.this.kvmInterface.base.threadGroup.get(String.valueOf(PowerPopupMenu.this.kvmInterface.actionBlade));
          int result = JOptionPane.showConfirmDialog(PowerPopupMenu.this.kvmInterface.floatToolbar.imagePanel, PowerPopupMenu.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
          if (0 == result)
          {
            bladeThread.bladeCommu.sentData((PowerPopupMenu.this.kvmInterface.kvmUtil.getImagePane(PowerPopupMenu.this.kvmInterface.actionBlade)).pack.kvmCmdPowerControl((byte)37, bladeThread.getBladeNO()));
          }
        }
      };
    return action;
  }
  public Action kinescopeAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          JMenuItem menuItem = (JMenuItem)e.getSource();
          if (null == PowerPopupMenu.this.kineScopeDataCollect) {
            int result = JOptionPane.showConfirmDialog(PowerPopupMenu.this.kvmInterface.floatToolbar.imagePanel, PowerPopupMenu.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
            if (0 == result)
            {
              String absolutePath = "";
              JFileChooser choose = new JFileChooser("");
              choose.addChoosableFileFilter(new FileFilter()
                  {
                    public boolean accept(File f)
                    {
                      if (f.isDirectory())
                      {
                        return true;
                      }
                      String fileName = f.getName();
                      if (fileName.toLowerCase().endsWith("rep".toLowerCase()))
                      {
                        return true;
                      }
                      return false;
                    }
                    public String getDescription() {
                      return "file(*.rep)";
                    }
                  });
              choose.setFileSelectionMode(0);
              int returnval = choose.showSaveDialog(null);
              if (returnval == 0)
              {
                absolutePath = choose.getSelectedFile().getAbsolutePath().toLowerCase();
              }
              if (null == absolutePath || "".equals(absolutePath)) {
                return;
              }
              if (!absolutePath.toLowerCase().endsWith(".rep"))
              {
                absolutePath = absolutePath + ".rep";
              }
              File file = new File(absolutePath);
              if (file.exists())
              {
                int file_check = JOptionPane.showConfirmDialog(PowerPopupMenu.this.kvmInterface.floatToolbar.imagePanel, PowerPopupMenu.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
                if (file_check == 0)
                {
                  boolean isdel = file.delete();
                  if (!isdel) {
                    JOptionPane.showMessageDialog(PowerPopupMenu.this.kvmInterface.floatToolbar.imagePanel, PowerPopupMenu.this.kvmInterface.kvmUtil.getString("file_used"));
                    return;
                  } 
                  menuItem.setText(PowerPopupMenu.this.kvmInterface.kvmUtil.getString("Stop_KinScope"));
                  PowerPopupMenu.this.kineScopeDataCollect = new KinescopeDataCollect(absolutePath);
                  PowerPopupMenu.this.kineScopeDataCollect.setCollect(true);
                  PowerPopupMenu.this.kineScopeDataCollect.startCollectThread();
                  if (PowerPopupMenu.this.kvmInterface.isFullScreen && null == PowerPopupMenu.this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect)
                  {
                    PowerPopupMenu.this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect = PowerPopupMenu.this.kineScopeDataCollect;
                  }
                }
              }
              else
              {
                menuItem.setText(PowerPopupMenu.this.kvmInterface.kvmUtil.getString("Stop_KinScope"));
                PowerPopupMenu.this.kineScopeDataCollect = new KinescopeDataCollect(absolutePath);
                PowerPopupMenu.this.kineScopeDataCollect.setCollect(true);
                PowerPopupMenu.this.kineScopeDataCollect.startCollectThread();
                if (PowerPopupMenu.this.kvmInterface.isFullScreen && null == PowerPopupMenu.this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect)
                {
                  PowerPopupMenu.this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect = PowerPopupMenu.this.kineScopeDataCollect;
                }
              }
            }
          }
          else if (PowerPopupMenu.this.kineScopeDataCollect.isCollect()) {
            int result = JOptionPane.showConfirmDialog(PowerPopupMenu.this.kvmInterface.floatToolbar.imagePanel, PowerPopupMenu.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
            if (0 == result) {
              menuItem.setText(PowerPopupMenu.this.kvmInterface.kvmUtil.getString("localKinescope"));
              PowerPopupMenu.this.kineScopeDataCollect.interrupt();
              PowerPopupMenu.this.kineScopeDataCollect.setCollect(false);
              if (PowerPopupMenu.this.kineScopeDataCollect.isAlive()) {
                try {
                  Thread.sleep(5L);
                }
                catch (InterruptedException e1) {
                  e1.printStackTrace();
                } 
              }
              PowerPopupMenu.this.kineScopeDataCollect = null;
              PowerPopupMenu.this.dissflag = false;
              PowerPopupMenu.this.isIdiss = false;
              if (PowerPopupMenu.this.kvmInterface.isFullScreen && null == PowerPopupMenu.this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect)
              {
                PowerPopupMenu.this.kvmInterface.floatToolbar.powerMenu.kineScopeDataCollect = PowerPopupMenu.this.kineScopeDataCollect;
              }
            } 
          } 
        }
      };
    return action;
  }
  public Action mouseModeSwitchAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          byte mouseMode = 0;
          BladeThread bladeThread = PowerPopupMenu.this.kvmInterface.base.threadGroup.get(String.valueOf(PowerPopupMenu.this.kvmInterface.actionBlade));
          int result = JOptionPane.showConfirmDialog(PowerPopupMenu.this.kvmInterface.floatToolbar.imagePanel, PowerPopupMenu.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
          if (0 == result) {
            if (PowerPopupMenu.this.mouseModeSwitchMenu.isSelected()) {
              mouseMode = 1;
              Base.isSingleMouse = false;
              PowerPopupMenu.this.SingleMouseMenu.setSelected(false);
            }
            else {
              mouseMode = 0;
            } 
            bladeThread.bladeCommu.sentData((PowerPopupMenu.this.kvmInterface.kvmUtil.getImagePane(PowerPopupMenu.this.kvmInterface.actionBlade)).pack.mouseModeControl((byte)36, mouseMode, bladeThread.getBladeNO()));
          }
          else if (PowerPopupMenu.this.mouseModeSwitchMenu.isSelected()) {
            PowerPopupMenu.this.mouseModeSwitchMenu.setSelected(false);
          }
          else {
            PowerPopupMenu.this.mouseModeSwitchMenu.setSelected(true);
          } 
        }
      };
    return action;
  }
  public Action singleMouseSwitchAction() {
    Action action = new AbstractAction()
      {
        private static final long serialVersionUID = 1L;
        public void actionPerformed(ActionEvent e) {
          BladeThread bladeThread = PowerPopupMenu.this.kvmInterface.base.threadGroup.get(String.valueOf(PowerPopupMenu.this.kvmInterface.actionBlade));
          int result = JOptionPane.showConfirmDialog(PowerPopupMenu.this.kvmInterface.floatToolbar.imagePanel, PowerPopupMenu.this.kvmInterface.kvmUtil.getString("Power_massage"), UIManager.getString("OptionPane.titleText"), 0);
          if (0 == result) {
            if (PowerPopupMenu.this.SingleMouseMenu.isSelected())
            {
              Base.isSingleMouse = true;
              PowerPopupMenu.this.mouseModeSwitchMenu.setSelected(false);
              if (Base.isSynMouse)
              {
                bladeThread.bladeCommu.sentData((PowerPopupMenu.this.kvmInterface.kvmUtil.getImagePane(PowerPopupMenu.this.kvmInterface.actionBlade)).pack.mouseModeControl((byte)36, (byte)0, bladeThread.getBladeNO()));
              }
            }
            else
            {
              Base.isSingleMouse = false;
            }
          }
          else if (PowerPopupMenu.this.SingleMouseMenu.isSelected()) {
            PowerPopupMenu.this.SingleMouseMenu.setSelected(false);
          }
          else {
            PowerPopupMenu.this.SingleMouseMenu.setSelected(true);
          } 
        }
      };
    return action;
  }
}
