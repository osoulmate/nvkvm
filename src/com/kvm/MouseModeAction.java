package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
class MouseModeAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private MousePopupMenu popupMenu_MouseModeAction = null;
  public MouseModeAction(MousePopupMenu refer) {
    this.popupMenu_MouseModeAction = refer;
  }
  public void actionPerformed(ActionEvent e) {
    byte mouseMode = 0;
    BladeThread bladeThread = (BladeThread)this.popupMenu_MouseModeAction.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.popupMenu_MouseModeAction.getKvmInterface().getActionBlade()));
    int result = JOptionPane.showConfirmDialog(this.popupMenu_MouseModeAction.getKvmInterface().getFloatToolbar().getImagePanel(), this.popupMenu_MouseModeAction
        .getKvmInterface().getKvmUtil().getString("Power_massage"), 
        UIManager.getString("OptionPane.titleText"), 0);
    if (0 == result) {
      if (this.popupMenu_MouseModeAction.mouseModeSwitchMenu.isSelected()) {
        mouseMode = 1;
        Base.setSingleMouse(false);
        this.popupMenu_MouseModeAction.singleMouseMenu.setSelected(false);
      }
      else {
        mouseMode = 0;
      } 
      bladeThread.getBladeCommu().sentData(this.popupMenu_MouseModeAction.getKvmInterface()
          .getKvmUtil()
          .getImagePane(this.popupMenu_MouseModeAction.getKvmInterface().getActionBlade())
          .getPack()
          .mouseModeControl((byte)36, mouseMode, bladeThread.getBladeNOByBladeThread()));
    }
    else if (this.popupMenu_MouseModeAction.mouseModeSwitchMenu.isSelected()) {
      this.popupMenu_MouseModeAction.mouseModeSwitchMenu.setSelected(false);
    }
    else {
      this.popupMenu_MouseModeAction.mouseModeSwitchMenu.setSelected(true);
    } 
  }
}
