package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
class SynMouseAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FullScreen refer_SynMouseAction;
  public SynMouseAction(FullScreen refer_SynMouseAction) {
    this.refer_SynMouseAction = refer_SynMouseAction;
  }
  public void actionPerformed(ActionEvent e) {
    if (!this.refer_SynMouseAction.getKvmInterface().getKvmUtil().getImagePane(this.refer_SynMouseAction.getActionBlade()).isNew()) {
      if (this.refer_SynMouseAction.getKvmInterface()
        .getKvmUtil()
        .getImagePane(this.refer_SynMouseAction.getActionBlade())
        .isContr())
      {
        BladeThread bladeThread = (BladeThread)this.refer_SynMouseAction.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.refer_SynMouseAction.getActionBlade()));
        bladeThread.getBladeCommu().sentData(this.refer_SynMouseAction.getKvmInterface()
            .getKvmUtil()
            .getImagePane(this.refer_SynMouseAction.getActionBlade())
            .getPack()
            .mousePack(65535, 65535, this.refer_SynMouseAction.getActionBlade()));
      }
      else
      {
        JOptionPane.showMessageDialog(this.refer_SynMouseAction.getKvmInterface()
            .getKvmUtil()
            .getImagePane(this.refer_SynMouseAction.getActionBlade()), this.refer_SynMouseAction
            .getKvmInterface().getKvmUtil().getString("ListenOperation"));
      }
    }
    else if (this.refer_SynMouseAction.getKvmInterface().getBase().isMstsc()) {
      BladeThread bladeThread = (BladeThread)this.refer_SynMouseAction.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.refer_SynMouseAction.getActionBlade()));
      for (int i = 0; i < 15; i++)
      {
        bladeThread.getBladeCommu().sentData(this.refer_SynMouseAction.getKvmInterface()
            .getKvmUtil()
            .getImagePane(this.refer_SynMouseAction.getActionBlade())
            .getPack()
            .mousePackNew((byte)-127, (byte)-127, this.refer_SynMouseAction.getActionBlade()));
      }
      bladeThread.getDrawThread().getImagePane().setRemotemstscX(0);
      bladeThread.getDrawThread().getImagePane().setRemotemstscY(0);
    } 
  }
}
