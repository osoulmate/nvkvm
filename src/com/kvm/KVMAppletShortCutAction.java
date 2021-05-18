package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
class KVMAppletShortCutAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private KVMInterface kvmInterface = null;
  public KVMAppletShortCutAction(KVMInterface refer) {
    this.kvmInterface = refer;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.kvmInterface.getKvmUtil().getImagePane(this.kvmInterface.getActionBlade()).isContr()) {
      this.kvmInterface.produceComKey();
    }
    else {
      JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface
          .getKvmUtil().getString("ListenOperation"));
    } 
    if (this.kvmInterface.getBladeSize() > 1)
    {
      this.kvmInterface.getToolbar()
        .getCombineKey()
        .setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1), 
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));
    }
  }
}
