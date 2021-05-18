package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
class FullScreenUsbResetAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FullScreen refer_FullScreenUsbReset;
  public FullScreenUsbResetAction(FullScreen refer_FullScreenUsbReset) {
    this.refer_FullScreenUsbReset = refer_FullScreenUsbReset;
  }
  public void actionPerformed(ActionEvent e) {
    BladeThread bladeThread = (BladeThread)this.refer_FullScreenUsbReset.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.refer_FullScreenUsbReset.getKvmInterface().getActionBlade()));
    int result = JOptionPane.showConfirmDialog(this.refer_FullScreenUsbReset.getKvmInterface().getFloatToolbar().getImagePanel(), this.refer_FullScreenUsbReset
        .getKvmInterface().getKvmUtil().getString("Power_massage"), 
        UIManager.getString("OptionPane.titleText"), 0);
    if (0 == result)
    {
      bladeThread.getBladeCommu().sentData(this.refer_FullScreenUsbReset.getKvmInterface()
          .getKvmUtil()
          .getImagePane(this.refer_FullScreenUsbReset.getKvmInterface().getActionBlade())
          .getPack()
          .kvmCmdPowerControl((byte)48, bladeThread.getBladeNOByBladeThread()));
    }
  }
}
