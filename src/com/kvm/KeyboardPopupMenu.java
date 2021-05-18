package com.kvm;
import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
public class KeyboardPopupMenu
  extends JPopupMenu
{
  private static final long serialVersionUID = 1L;
  private KVMInterface kvmInterface;
  public final JCheckBoxMenuItem selfAdaptionMenu;
  public final JCheckBoxMenuItem japaneseKeyboardMenu;
  public final JCheckBoxMenuItem frenchKeyboardMenu;
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public KeyboardPopupMenu(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
    this.selfAdaptionMenu = new JCheckBoxMenuItem(kvmInterface.getKvmUtil().getString("usKeyboard"));
    this.japaneseKeyboardMenu = new JCheckBoxMenuItem(kvmInterface.getKvmUtil().getString("japaneseKeyboard"));
    this.frenchKeyboardMenu = new JCheckBoxMenuItem(kvmInterface.getKvmUtil().getString("frenchKeyboard"));
    add(this.selfAdaptionMenu);
    add(this.japaneseKeyboardMenu);
    add(this.frenchKeyboardMenu);
    if (Base.getKeyboardLayout() == 1) {
      this.selfAdaptionMenu.setSelected(true);
      this.japaneseKeyboardMenu.setSelected(false);
      this.frenchKeyboardMenu.setSelected(false);
    }
    else if (Base.getKeyboardLayout() == 2) {
      this.selfAdaptionMenu.setSelected(false);
      this.japaneseKeyboardMenu.setSelected(true);
      this.frenchKeyboardMenu.setSelected(false);
    }
    else {
      this.selfAdaptionMenu.setSelected(false);
      this.japaneseKeyboardMenu.setSelected(false);
      this.frenchKeyboardMenu.setSelected(true);
    } 
    this.selfAdaptionMenu.addActionListener(selfAdaptionAction());
    this.japaneseKeyboardMenu.addActionListener(japaneseKeyboardAction());
    this.frenchKeyboardMenu.addActionListener(frenchKeyboardAction());
    setBackground(new Color(158, 202, 232));
    this.selfAdaptionMenu.setBackground(new Color(204, 227, 242));
    this.japaneseKeyboardMenu.setBackground(new Color(204, 227, 242));
    this.frenchKeyboardMenu.setBackground(new Color(204, 227, 242));
  }
  private ActionListener frenchKeyboardAction() {
    Action action = new FrenchKeyboardAction(this);
    return action;
  }
  private ActionListener japaneseKeyboardAction() {
    Action action = new JapaneseKeyboardAction(this);
    return action;
  }
  private ActionListener selfAdaptionAction() {
    Action action = new SelfAdaptionAction(this);
    return action;
  }
  public static void setSelected(KeyboardPopupMenu keyboardPopupMenu, JCheckBoxMenuItem menu) {
    keyboardPopupMenu.selfAdaptionMenu.setSelected(false);
    keyboardPopupMenu.japaneseKeyboardMenu.setSelected(false);
    keyboardPopupMenu.frenchKeyboardMenu.setSelected(false);
    menu.setSelected(true);
  }
}
