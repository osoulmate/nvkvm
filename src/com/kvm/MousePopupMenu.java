package com.kvm;
import java.awt.Color;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
public class MousePopupMenu
  extends JPopupMenu
{
  private static final long serialVersionUID = 1L;
  private KVMInterface kvmInterface;
  public final JCheckBoxMenuItem mouseModeSwitchMenu;
  public final JCheckBoxMenuItem singleMouseMenu;
  public final JMenuItem usbResetMenu;
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public MousePopupMenu(KVMInterface kvmInterface2) {
    this.kvmInterface = kvmInterface2;
    this.mouseModeSwitchMenu = new JCheckBoxMenuItem(kvmInterface2.getKvmUtil().getString("mouse_mode_switch"));
    if (Base.getIsSynMouse()) {
      this.mouseModeSwitchMenu.setSelected(true);
    }
    else {
      this.mouseModeSwitchMenu.setSelected(false);
    } 
    this.singleMouseMenu = new JCheckBoxMenuItem(kvmInterface2.getKvmUtil().getString("single_mouse"));
    if ("USB".equals("USB")) {
      add(this.mouseModeSwitchMenu);
      add(this.singleMouseMenu);
    } 
    this.singleMouseMenu.addActionListener(singleMouseSwitchAction());
    this.mouseModeSwitchMenu.addActionListener(mouseModeSwitchAction());
    setBackground(new Color(158, 202, 232));
    this.singleMouseMenu.setBackground(new Color(204, 227, 242));
    this.mouseModeSwitchMenu.setBackground(new Color(204, 227, 242));
    this.usbResetMenu = new JMenuItem(kvmInterface2.getKvmUtil().getString("Mouse_Key_Reset"));
    add(this.usbResetMenu);
    this.usbResetMenu.addActionListener(usbResetAction());
    this.usbResetMenu.setBackground(new Color(204, 227, 242));
  }
  public Action mouseModeSwitchAction() {
    Action action = new MouseModeAction(this);
    return action;
  }
  public Action singleMouseSwitchAction() {
    Action action = new MouseSwitchAction(this);
    return action;
  }
  public Action usbResetAction() {
    Action action = new USBReserAction(this);
    return action;
  }
}
