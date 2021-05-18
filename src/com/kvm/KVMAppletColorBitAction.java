package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
class KVMAppletColorBitAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private KVMInterface kvmInterface = null;
  public KVMAppletColorBitAction(KVMInterface refer) {
    this.kvmInterface = refer;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.kvmInterface.getKvmUtil().getImagePane(this.kvmInterface.getActionBlade()).isContr()) {
      this.kvmInterface.colorBit();
    }
    else {
      JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface
          .getKvmUtil().getString("ListenOperation"));
    } 
  }
}
