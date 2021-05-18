package com.kvm;
import com.Kinescope.KinescopeDataCollect;
import java.awt.Color;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
public class PowerPopupMenu
  extends JPopupMenu
{
  private static final long serialVersionUID = 1L;
  private KVMInterface kvmInterface;
  private JMenuItem poweroffMenu;
  private JMenuItem poweronMenu;
  private JMenuItem restartMenu;
  private JMenuItem safetyRestartMenu;
  private JMenuItem usbResetMenu;
  private JMenuItem savePowerOffMenu;
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  private transient KinescopeDataCollect kineScopeDataCollect = null;
  public final JMenuItem localKinescopeMenu;
  public KinescopeDataCollect getKineScopeDataCollect() {
    return this.kineScopeDataCollect;
  }
  public void setKineScopeDataCollect(KinescopeDataCollect kineScopeDataCollect) {
    this.kineScopeDataCollect = kineScopeDataCollect;
  }
  private boolean dissflag = false;
  public boolean isDissflag() {
    return this.dissflag;
  }
  public void setDissflag(boolean dissflag) {
    this.dissflag = dissflag;
  }
  private boolean isIdiss = false;
  public boolean isIdiss() {
    return this.isIdiss;
  }
  private JCheckBoxMenuItem mouseModeSwitchMenu; private JCheckBoxMenuItem singleMouseMenu;
  public void setIdiss(boolean isIdiss) {
    this.isIdiss = isIdiss;
  }
  public JCheckBoxMenuItem getMouseModeSwitchMenu() {
    return this.mouseModeSwitchMenu;
  }
  public void setMouseModeSwitchMenu(JCheckBoxMenuItem mouseModeSwitchMenu) {
    this.mouseModeSwitchMenu = mouseModeSwitchMenu;
  }
  public JCheckBoxMenuItem getSingleMouseMenu() {
    return this.singleMouseMenu;
  }
  public void setSingleMouseMenu(JCheckBoxMenuItem singleMouseMenu) {
    this.singleMouseMenu = singleMouseMenu;
  }
  public PowerPopupMenu(KVMInterface kvmInterface2) {
    this.kvmInterface = kvmInterface2;
    this.poweronMenu = new JMenuItem(kvmInterface2.getKvmUtil().getString("Power_On"));
    this.poweroffMenu = new JMenuItem(kvmInterface2.getKvmUtil().getString("Power_Off"));
    this.restartMenu = new JMenuItem(kvmInterface2.getKvmUtil().getString("Cold_Reset"));
    this.safetyRestartMenu = new JMenuItem(kvmInterface2.getKvmUtil().getString("Graceful_Reboot"));
    this.usbResetMenu = new JMenuItem(kvmInterface2.getKvmUtil().getString("Usb_Reset"));
    this.savePowerOffMenu = new JMenuItem(kvmInterface2.getKvmUtil().getString("Graceful_poweroff"));
    this.localKinescopeMenu = new JMenuItem(kvmInterface2.getKvmUtil().getString("localKinescope"));
    this.localKinescopeMenu.addActionListener(kinescopeAction());
    this.mouseModeSwitchMenu = new JCheckBoxMenuItem(kvmInterface2.getKvmUtil().getString("mouse_mode_switch"));
    if (Base.getIsSynMouse()) {
      this.mouseModeSwitchMenu.setSelected(true);
    }
    else {
      this.mouseModeSwitchMenu.setSelected(false);
    } 
    this.singleMouseMenu = new JCheckBoxMenuItem(kvmInterface2.getKvmUtil().getString("single_mouse"));
    add(this.poweronMenu);
    add(this.poweroffMenu);
    add(this.savePowerOffMenu);
    add(this.restartMenu);
    add(this.safetyRestartMenu);
    this.poweronMenu.addActionListener(poweronAction());
    this.poweroffMenu.addActionListener(poweroffAction());
    this.savePowerOffMenu.addActionListener(savePowerOffAction());
    this.restartMenu.addActionListener(restartAction());
    this.safetyRestartMenu.addActionListener(safetyRestartAction());
    this.usbResetMenu.addActionListener(usbResetAction());
    this.singleMouseMenu.addActionListener(singleMouseSwitchAction());
    this.mouseModeSwitchMenu.addActionListener(mouseModeSwitchAction());
    setBackground(new Color(158, 202, 232));
    this.poweroffMenu.setBackground(new Color(204, 227, 242));
    this.poweronMenu.setBackground(new Color(204, 227, 242));
    this.savePowerOffMenu.setBackground(new Color(204, 227, 242));
    this.restartMenu.setBackground(new Color(204, 227, 242));
    this.safetyRestartMenu.setBackground(new Color(204, 227, 242));
    this.usbResetMenu.setBackground(new Color(204, 227, 242));
    this.localKinescopeMenu.setBackground(new Color(204, 227, 242));
    this.singleMouseMenu.setBackground(new Color(204, 227, 242));
    this.mouseModeSwitchMenu.setBackground(new Color(204, 227, 242));
  }
  public Action poweronAction() {
    Action action = new PowerOnAction(this);
    return action;
  }
  public Action poweroffAction() {
    Action action = new PowerOffAction(this);
    return action;
  }
  public Action savePowerOffAction() {
    Action action = new SavePowerOffAction(this);
    return action;
  }
  public Action restartAction() {
    Action action = new RestartAction(this);
    return action;
  }
  public Action safetyRestartAction() {
    Action action = new SafeRestartAction(this);
    return action;
  }
  public Action usbResetAction() {
    Action action = new USBResetAction(this);
    return action;
  }
  public Action kinescopeAction() {
    Action action = new ScopeAction(this);
    return action;
  }
  public Action mouseModeSwitchAction() {
    Action action = new MouseModeSwitchAction(this);
    return action;
  }
  public Action singleMouseSwitchAction() {
    Action action = new SingleMouseSwitchAction(this);
    return action;
  }
}
