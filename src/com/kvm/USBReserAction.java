package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
class USBReserAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private MousePopupMenu popupMenu_USBReserAction = null;
  public USBReserAction(MousePopupMenu refer) {
    this.popupMenu_USBReserAction = refer;
  }
  public void actionPerformed(ActionEvent e) {
    BladeThread bladeThread = (BladeThread)this.popupMenu_USBReserAction.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.popupMenu_USBReserAction.getKvmInterface().getActionBlade()));
    int result = JOptionPane.showConfirmDialog(this.popupMenu_USBReserAction.getKvmInterface().getFloatToolbar().getImagePanel(), this.popupMenu_USBReserAction
        .getKvmInterface().getKvmUtil().getString("Power_massage"), 
        UIManager.getString("OptionPane.titleText"), 0);
    if (result == 0)
    {
      bladeThread.getBladeCommu().sentData(this.popupMenu_USBReserAction.getKvmInterface()
          .getKvmUtil()
          .getImagePane(this.popupMenu_USBReserAction.getKvmInterface().getActionBlade())
          .getPack()
          .kvmCmdPowerControl((byte)48, bladeThread.getBladeNOByBladeThread()));
    }
  }
}
