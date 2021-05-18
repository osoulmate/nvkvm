package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
class MouseSwitchAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private MousePopupMenu popupMenu_MouseSwitchAction = null;
  public MouseSwitchAction(MousePopupMenu refer) {
    this.popupMenu_MouseSwitchAction = refer;
  }
  public void actionPerformed(ActionEvent e) {
    BladeThread bladeThread = (BladeThread)this.popupMenu_MouseSwitchAction.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.popupMenu_MouseSwitchAction.getKvmInterface().getActionBlade()));
    int result = JOptionPane.showConfirmDialog(this.popupMenu_MouseSwitchAction.getKvmInterface().getFloatToolbar().getImagePanel(), this.popupMenu_MouseSwitchAction
        .getKvmInterface().getKvmUtil().getString("Power_massage"), 
        UIManager.getString("OptionPane.titleText"), 0);
    if (result == 0) {
      if (this.popupMenu_MouseSwitchAction.singleMouseMenu.isSelected())
      {
        Base.setSingleMouse(true);
        this.popupMenu_MouseSwitchAction.mouseModeSwitchMenu.setSelected(false);
        if (Base.getIsSynMouse())
        {
          bladeThread.getBladeCommu().sentData(this.popupMenu_MouseSwitchAction.getKvmInterface()
              .getKvmUtil()
              .getImagePane(this.popupMenu_MouseSwitchAction.getKvmInterface().getActionBlade())
              .getPack()
              .mouseModeControl((byte)36, (byte)0, bladeThread.getBladeNOByBladeThread()));
        }
      }
      else
      {
        Base.setSingleMouse(false);
      }
    }
    else if (this.popupMenu_MouseSwitchAction.singleMouseMenu.isSelected()) {
      this.popupMenu_MouseSwitchAction.singleMouseMenu.setSelected(false);
    }
    else {
      this.popupMenu_MouseSwitchAction.singleMouseMenu.setSelected(true);
    } 
  }
}
