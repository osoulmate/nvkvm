package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class KVMAppletFullScreenAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private KVMInterface kvmInterface = null;
  public KVMAppletFullScreenAction(KVMInterface refer) {
    this.kvmInterface = refer;
  }
  public void actionPerformed(ActionEvent e) {
    this.kvmInterface.getTabbedpane().getModel().removeChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
    this.kvmInterface.produceFullScreen();
    this.kvmInterface.getTabbedpane().getModel().addChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
    int dqtValue = (Base.getDqtzSize() + 1) * 10;
    if (dqtValue >= 70)
    {
      dqtValue -= 10;
    }
    this.kvmInterface.getFullScreen().getToolBar().getDqtSlider().setValue(dqtValue);
  }
}
