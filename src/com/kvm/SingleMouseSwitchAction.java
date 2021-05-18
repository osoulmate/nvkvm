package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
class SingleMouseSwitchAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private PowerPopupMenu popupMenu = null;
  public SingleMouseSwitchAction(PowerPopupMenu refer) {
    this.popupMenu = refer;
  }
  public void actionPerformed(ActionEvent e) {
    BladeThread bladeThread = (BladeThread)this.popupMenu.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.popupMenu.getKvmInterface().getActionBlade()));
    int result = JOptionPane.showConfirmDialog(this.popupMenu.getKvmInterface().getFloatToolbar().getImagePanel(), this.popupMenu
        .getKvmInterface().getKvmUtil().getString("Power_massage"), 
        UIManager.getString("OptionPane.titleText"), 0);
    if (0 == result) {
      if (this.popupMenu.getSingleMouseMenu().isSelected())
      {
        Base.setSingleMouse(true);
        this.popupMenu.getMouseModeSwitchMenu().setSelected(false);
        if (Base.getIsSynMouse())
        {
          bladeThread.getBladeCommu().sentData(this.popupMenu.getKvmInterface()
              .getKvmUtil()
              .getImagePane(this.popupMenu.getKvmInterface().getActionBlade())
              .getPack()
              .mouseModeControl((byte)36, (byte)0, bladeThread.getBladeNOByBladeThread()));
        }
      }
      else
      {
        Base.setSingleMouse(false);
      }
    }
    else if (this.popupMenu.getSingleMouseMenu().isSelected()) {
      this.popupMenu.getSingleMouseMenu().setSelected(false);
    }
    else {
      this.popupMenu.getSingleMouseMenu().setSelected(true);
    } 
  }
}
