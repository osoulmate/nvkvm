package com.kvm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
class PowerPanel
  extends JDialog
{
  private static final long serialVersionUID = 1L;
  private FullScreen refer_PowerPanel;
  public JPanel mainPanel;
  public JLabel poweronMenu;
  public JLabel poweroffMenu;
  public JLabel restartMenu;
  public JLabel safetyRestartMenu;
  public JLabel usbResetMenu;
  public PowerPanel(FullScreen refer_PowerPanel) {
    this.refer_PowerPanel = refer_PowerPanel;
    createMainPanle();
    setLayout(new BorderLayout());
    if (Base.getLocal().equalsIgnoreCase("en")) {
      setSize(110, 110);
    }
    else {
      setSize(70, 105);
    } 
    getContentPane().add(this.mainPanel);
    setUndecorated(true);
    setAlwaysOnTop(true);
    setVisible(false);
    this.poweroffMenu.setBackground(new Color(204, 227, 242));
    this.poweronMenu.setBackground(new Color(204, 227, 242));
    this.restartMenu.setBackground(new Color(204, 227, 242));
    this.safetyRestartMenu.setBackground(new Color(204, 227, 242));
    this.usbResetMenu.setBackground(new Color(204, 227, 242));
    if (Base.getLocal().equalsIgnoreCase("en")) {
      this.poweroffMenu.setBounds(5, 0, 70, 20);
      this.poweronMenu.setBounds(5, 20, 70, 20);
      this.restartMenu.setBounds(5, 40, 70, 20);
      this.safetyRestartMenu.setBounds(5, 60, 100, 20);
      this.usbResetMenu.setBounds(5, 80, 70, 20);
    }
    else {
      this.poweroffMenu.setBounds(5, 0, 60, 20);
      this.poweronMenu.setBounds(5, 20, 60, 20);
      this.restartMenu.setBounds(5, 40, 60, 20);
      this.safetyRestartMenu.setBounds(5, 60, 60, 20);
      this.usbResetMenu.setBounds(5, 80, 60, 20);
    } 
  }
  private JPanel createMainPanle() {
    this.mainPanel = new JPanel();
    if (Base.getLocal().equalsIgnoreCase("en")) {
      this.mainPanel.setSize(105, 110);
    }
    else {
      this.mainPanel.setSize(70, 105);
    } 
    this.mainPanel.setBackground(new Color(204, 227, 242));
    this.mainPanel.setVisible(true);
    this.mainPanel.setLayout((LayoutManager)null);
    this.poweronMenu = new JLabel(this.refer_PowerPanel.getKvmInterface().getKvmUtil().getString("Power_On"));
    this.poweronMenu.addMouseListener(new MouseAdapter()
        {
          public void mouseEntered(MouseEvent e)
          {
            PowerPanel.this.poweronMenu.setForeground(new Color(158, 202, 232));
          }
          public void mouseExited(MouseEvent e) {
            PowerPanel.this.poweronMenu.setForeground(Color.BLACK);
          }
          public void mouseClicked(MouseEvent e) {
            PowerPanel.this.refer_PowerPanel.getPowerPanelDialog().setVisible(false);
            PowerPanel.this.poweronMenu.setForeground(Color.BLACK);
            BladeThread bladeThread = (BladeThread)PowerPanel.this.refer_PowerPanel.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(PowerPanel.this.refer_PowerPanel.getActionBlade()));
            int result = JOptionPane.showConfirmDialog(PowerPanel.this.refer_PowerPanel.getKvmInterface().getFullScreen(), PowerPanel.this
                .refer_PowerPanel.getKvmInterface().getKvmUtil().getString("Power_massage"), 
                UIManager.getString("OptionPane.titleText"), 0);
            if (0 == result)
            {
              bladeThread.getBladeCommu().sentData(PowerPanel.this.refer_PowerPanel.getKvmInterface()
                  .getKvmUtil()
                  .getImagePane(PowerPanel.this.refer_PowerPanel.getActionBlade())
                  .getPack()
                  .kvmCmdPowerControl((byte)33, bladeThread.getBladeNOByBladeThread()));
            }
          }
          public void mousePressed(MouseEvent e) {
            mouseClicked(e);
          }
        });
    this.poweroffMenu = new JLabel(this.refer_PowerPanel.getKvmInterface().getKvmUtil().getString("Power_Off"));
    this.poweroffMenu.addMouseListener(new MouseAdapter()
        {
          public void mouseEntered(MouseEvent e)
          {
            PowerPanel.this.poweroffMenu.setForeground(new Color(158, 202, 232));
          }
          public void mouseExited(MouseEvent e) {
            PowerPanel.this.poweroffMenu.setForeground(Color.BLACK);
          }
          public void mouseClicked(MouseEvent e) {
            PowerPanel.this.refer_PowerPanel.getPowerPanelDialog().setVisible(false);
            PowerPanel.this.poweroffMenu.setForeground(Color.BLACK);
            BladeThread bladeThread = (BladeThread)PowerPanel.this.refer_PowerPanel.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(PowerPanel.this.refer_PowerPanel.getActionBlade()));
            int result = JOptionPane.showConfirmDialog(PowerPanel.this.refer_PowerPanel.getKvmInterface().getFullScreen(), PowerPanel.this
                .refer_PowerPanel.getKvmInterface().getKvmUtil().getString("Power_massage"), 
                UIManager.getString("OptionPane.titleText"), 0);
            if (0 == result)
            {
              bladeThread.getBladeCommu().sentData(PowerPanel.this.refer_PowerPanel.getKvmInterface()
                  .getKvmUtil()
                  .getImagePane(PowerPanel.this.refer_PowerPanel.getActionBlade())
                  .getPack()
                  .kvmCmdPowerControl((byte)32, bladeThread.getBladeNOByBladeThread()));
            }
          }
          public void mousePressed(MouseEvent e) {
            mouseClicked(e);
          }
        });
    this.restartMenu = new JLabel(this.refer_PowerPanel.getKvmInterface().getKvmUtil().getString("Cold_Reset"));
    this.restartMenu.addMouseListener(new MouseAdapter()
        {
          public void mouseEntered(MouseEvent e)
          {
            PowerPanel.this.restartMenu.setForeground(new Color(158, 202, 232));
          }
          public void mouseExited(MouseEvent e) {
            PowerPanel.this.restartMenu.setForeground(Color.BLACK);
          }
          public void mouseClicked(MouseEvent e) {
            PowerPanel.this.refer_PowerPanel.getPowerPanelDialog().setVisible(false);
            PowerPanel.this.restartMenu.setForeground(Color.BLACK);
            BladeThread bladeThread = (BladeThread)PowerPanel.this.refer_PowerPanel.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(PowerPanel.this.refer_PowerPanel.getActionBlade()));
            int result = JOptionPane.showConfirmDialog(PowerPanel.this.refer_PowerPanel.getKvmInterface().getFullScreen(), PowerPanel.this
                .refer_PowerPanel.getKvmInterface().getKvmUtil().getString("Power_massage"), 
                UIManager.getString("OptionPane.titleText"), 0);
            if (0 == result)
            {
              bladeThread.getBladeCommu().sentData(PowerPanel.this.refer_PowerPanel.getKvmInterface()
                  .getKvmUtil()
                  .getImagePane(PowerPanel.this.refer_PowerPanel.getActionBlade())
                  .getPack()
                  .kvmCmdPowerControl((byte)34, bladeThread.getBladeNOByBladeThread()));
            }
          }
          public void mousePressed(MouseEvent e) {
            mouseClicked(e);
          }
        });
    this.safetyRestartMenu = new JLabel(this.refer_PowerPanel.getKvmInterface().getKvmUtil().getString("Graceful_Reboot"));
    this.safetyRestartMenu.addMouseListener(new MouseAdapter()
        {
          public void mouseEntered(MouseEvent e)
          {
            PowerPanel.this.safetyRestartMenu.setForeground(new Color(158, 202, 232));
          }
          public void mouseExited(MouseEvent e) {
            PowerPanel.this.safetyRestartMenu.setForeground(Color.BLACK);
          }
          public void mouseClicked(MouseEvent e) {
            PowerPanel.this.refer_PowerPanel.getPowerPanelDialog().setVisible(false);
            PowerPanel.this.safetyRestartMenu.setForeground(Color.BLACK);
            BladeThread bladeThread = (BladeThread)PowerPanel.this.refer_PowerPanel.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(PowerPanel.this.refer_PowerPanel.getActionBlade()));
            int result = JOptionPane.showConfirmDialog(PowerPanel.this.refer_PowerPanel.getKvmInterface().getFullScreen(), PowerPanel.this
                .refer_PowerPanel.getKvmInterface().getKvmUtil().getString("Power_massage"), 
                UIManager.getString("OptionPane.titleText"), 0);
            if (0 == result)
            {
              bladeThread.getBladeCommu().sentData(PowerPanel.this.refer_PowerPanel.getKvmInterface()
                  .getKvmUtil()
                  .getImagePane(PowerPanel.this.refer_PowerPanel.getActionBlade())
                  .getPack()
                  .kvmCmdPowerControl((byte)35, bladeThread.getBladeNOByBladeThread()));
            }
          }
          public void mousePressed(MouseEvent e) {
            mouseClicked(e);
          }
        });
    this.usbResetMenu = new JLabel(this.refer_PowerPanel.getKvmInterface().getKvmUtil().getString("Usb_Reset"));
    this.usbResetMenu.addMouseListener(new MouseAdapter()
        {
          public void mouseEntered(MouseEvent e)
          {
            PowerPanel.this.usbResetMenu.setForeground(new Color(158, 202, 232));
          }
          public void mouseExited(MouseEvent e) {
            PowerPanel.this.usbResetMenu.setForeground(Color.BLACK);
          }
          public void mouseClicked(MouseEvent e) {
            PowerPanel.this.refer_PowerPanel.getPowerPanelDialog().setVisible(false);
            PowerPanel.this.usbResetMenu.setForeground(Color.BLACK);
            BladeThread bladeThread = (BladeThread)PowerPanel.this.refer_PowerPanel.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(PowerPanel.this.refer_PowerPanel.getActionBlade()));
            int result = JOptionPane.showConfirmDialog(PowerPanel.this.refer_PowerPanel.getKvmInterface().getFullScreen(), PowerPanel.this
                .refer_PowerPanel.getKvmInterface().getKvmUtil().getString("Power_massage"), 
                UIManager.getString("OptionPane.titleText"), 0);
            if (0 == result)
            {
              bladeThread.getBladeCommu().sentData(PowerPanel.this.refer_PowerPanel.getKvmInterface()
                  .getKvmUtil()
                  .getImagePane(PowerPanel.this.refer_PowerPanel.getActionBlade())
                  .getPack()
                  .kvmCmdPowerControl((byte)48, bladeThread.getBladeNOByBladeThread()));
            }
          }
          public void mousePressed(MouseEvent e) {
            mouseClicked(e);
          }
        });
    this.mainPanel.add(this.poweronMenu);
    this.mainPanel.add(this.poweroffMenu);
    this.mainPanel.add(this.restartMenu);
    this.mainPanel.add(this.safetyRestartMenu);
    this.mainPanel.add(this.usbResetMenu);
    return this.mainPanel;
  }
}
