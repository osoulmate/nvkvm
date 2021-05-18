package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class HelpAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FullScreen refer_HelpAction;
  public HelpAction(FullScreen refer_HelpAction) {
    this.refer_HelpAction = refer_HelpAction;
  }
  public void actionPerformed(ActionEvent arg0) {
    if (null == this.refer_HelpAction.getKvmInterface().getFloatToolbar().getHelpFrm()) {
      this.refer_HelpAction.getKvmInterface().getFloatToolbar().getBMCHelpDocument();
    }
    else {
      this.refer_HelpAction.getKvmInterface().getFloatToolbar().getHelpFrm().setVisible(true);
    } 
    this.refer_HelpAction.getKvmInterface().getFloatToolbar().getHelpFrm().setAlwaysOnTop(true);
  }
}
