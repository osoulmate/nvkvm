package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
class MouseModeSwitchAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private PowerPopupMenu popupMenu = null;
  public MouseModeSwitchAction(PowerPopupMenu refer) {
    this.popupMenu = refer;
  }
  public void actionPerformed(ActionEvent e) {
    byte mouseMode = 0;
    BladeThread bladeThread = (BladeThread)this.popupMenu.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.popupMenu.getKvmInterface().getActionBlade()));
    int result = JOptionPane.showConfirmDialog(this.popupMenu.getKvmInterface().getFloatToolbar().getImagePanel(), this.popupMenu
        .getKvmInterface().getKvmUtil().getString("Power_massage"), 
        UIManager.getString("OptionPane.titleText"), 0);
    if (0 == result) {
      if (this.popupMenu.getMouseModeSwitchMenu().isSelected()) {
        mouseMode = 1;
      }
      else {
        mouseMode = 0;
      } 
      bladeThread.getBladeCommu().sentData(this.popupMenu.getKvmInterface()
          .getKvmUtil()
          .getImagePane(this.popupMenu.getKvmInterface().getActionBlade())
          .getPack()
          .mouseModeControl((byte)36, mouseMode, bladeThread.getBladeNOByBladeThread()));
    }
    else if (this.popupMenu.getMouseModeSwitchMenu().isSelected()) {
      this.popupMenu.getMouseModeSwitchMenu().setSelected(false);
    }
    else {
      this.popupMenu.getMouseModeSwitchMenu().setSelected(true);
    } 
  }
}
