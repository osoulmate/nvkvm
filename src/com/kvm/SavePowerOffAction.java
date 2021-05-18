package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
class SavePowerOffAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private PowerPopupMenu popupMenu = null;
  public SavePowerOffAction(PowerPopupMenu refer) {
    this.popupMenu = refer;
  }
  public void actionPerformed(ActionEvent e) {
    BladeThread bladeThread = (BladeThread)this.popupMenu.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.popupMenu.getKvmInterface().getActionBlade()));
    int result = JOptionPane.showConfirmDialog(this.popupMenu.getKvmInterface().getFloatToolbar().getImagePanel(), this.popupMenu
        .getKvmInterface().getKvmUtil().getString("Power_massage"), 
        UIManager.getString("OptionPane.titleText"), 0);
    if (0 == result)
    {
      bladeThread.getBladeCommu().sentData(this.popupMenu.getKvmInterface()
          .getKvmUtil()
          .getImagePane(this.popupMenu.getKvmInterface().getActionBlade())
          .getPack()
          .kvmCmdPowerControl((byte)37, bladeThread.getBladeNOByBladeThread()));
    }
  }
}
