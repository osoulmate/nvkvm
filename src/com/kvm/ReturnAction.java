package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class ReturnAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FullScreen refer_ReturnAction;
  public ReturnAction(FullScreen refer_ReturnAction) {
    this.refer_ReturnAction = refer_ReturnAction;
  }
  public void actionPerformed(ActionEvent e) {
    this.refer_ReturnAction.getKvmInterface()
      .getTabbedpane()
      .getModel()
      .removeChangeListener((this.refer_ReturnAction.getKvmInterface().getKvmUtil()).changeListener);
    this.refer_ReturnAction.getKvmInterface().getKvmUtil().returnToWin();
    this.refer_ReturnAction.getKvmInterface()
      .getTabbedpane()
      .getModel()
      .addChangeListener((this.refer_ReturnAction.getKvmInterface().getKvmUtil()).changeListener);
  }
}
