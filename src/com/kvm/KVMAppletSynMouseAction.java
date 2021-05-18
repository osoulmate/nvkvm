package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
class KVMAppletSynMouseAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private KVMInterface kvmInterface = null;
  public KVMAppletSynMouseAction(KVMInterface refer) {
    this.kvmInterface = refer;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.kvmInterface.getKvmUtil().getImagePane(this.kvmInterface.getActionBlade()).isContr()) {
      BladeThread bladeThread = (BladeThread)this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(this.kvmInterface.getActionBlade()));
      if (bladeThread.isNew()) {
        if (this.kvmInterface.getBase().isMstsc())
        {
          for (int i = 0; i < 15; i++)
          {
            bladeThread.getBladeCommu().sentData(this.kvmInterface.getKvmUtil()
                .getImagePane(this.kvmInterface.getActionBlade())
                .getPack()
                .mousePackNew((byte)-127, (byte)-127, this.kvmInterface.getActionBlade()));
          }
          bladeThread.getDrawThread().getImagePane().setRemotemstscX(0);
          bladeThread.getDrawThread().getImagePane().setRemotemstscY(0);
        }
      } else {
        bladeThread.getBladeCommu().sentData(this.kvmInterface.getKvmUtil()
            .getImagePane(this.kvmInterface.getActionBlade())
            .getPack()
            .mousePack(65535, 65535, this.kvmInterface.getActionBlade()));
      } 
      this.kvmInterface.getKvmUtil().getImagePane(this.kvmInterface.getActionBlade()).requestFocus();
    }
    else {
      JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface
          .getKvmUtil().getString("ListenOperation"));
    } 
  }
}
