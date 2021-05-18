package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
class KVMAppletDisConnBladeAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private KVMInterface kvmInterface = null;
  public KVMAppletDisConnBladeAction(KVMInterface refer) {
    this.kvmInterface = refer;
  }
  public void actionPerformed(ActionEvent e) {
    if (!this.kvmInterface.isFullScreen()) {
      this.kvmInterface.getTabbedpane().getModel().removeChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
      BladeThread bladeThread = (BladeThread)this.kvmInterface.getBase().getThreadGroup().get(String.valueOf(this.kvmInterface.getActionBlade()));
      bladeThread.getBladeCommu().setAutoFlag(false);
      this.kvmInterface.getKvmUtil().disconnectBlade(this.kvmInterface.getActionBlade());
      this.kvmInterface.getTabbedpane().getModel().addChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
      if (this.kvmInterface.getBladeSize() > 1)
      {
        this.kvmInterface.getToolbar()
          .getDisConnectBladeButton()
          .setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1), 
              BorderFactory.createEmptyBorder(2, 2, 2, 2)));
      }
    } 
  }
}
