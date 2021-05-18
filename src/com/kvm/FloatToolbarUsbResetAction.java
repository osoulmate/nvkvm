package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
class FloatToolbarUsbResetAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FloatToolbar ftb_FloatToolbarUsb = null;
  FloatToolbarUsbResetAction(FloatToolbar floatToolbar) {
    this.ftb_FloatToolbarUsb = floatToolbar;
  }
  public void actionPerformed(ActionEvent e) {
    BladeThread bladeThread = (BladeThread)this.ftb_FloatToolbarUsb.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.ftb_FloatToolbarUsb.getKvmInterface().getActionBlade()));
    int result = JOptionPane.showConfirmDialog(this.ftb_FloatToolbarUsb.getKvmInterface().getFloatToolbar().getImagePanel(), this.ftb_FloatToolbarUsb
        .getKvmInterface().getKvmUtil().getString("Power_massage"), 
        UIManager.getString("OptionPane.titleText"), 0);
    if (result == 0)
    {
      bladeThread.getBladeCommu().sentData(this.ftb_FloatToolbarUsb.getKvmInterface()
          .getKvmUtil()
          .getImagePane(this.ftb_FloatToolbarUsb.getKvmInterface().getActionBlade())
          .getPack()
          .kvmCmdPowerControl((byte)48, bladeThread.getBladeNOByBladeThread()));
    }
  }
}
