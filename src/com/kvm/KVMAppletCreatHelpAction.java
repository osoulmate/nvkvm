package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
class KVMAppletCreatHelpAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private KvmAppletToolBar toolBar = null;
  public KVMAppletCreatHelpAction(KvmAppletToolBar refer) {
    this.toolBar = refer;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.toolBar.getKvmInterface().getBladeSize() > 1) {
      this.toolBar.getKvmInterface()
        .getToolbar()
        .getHelpButton()
        .setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1), 
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));
      if (null == this.toolBar.getHelpFrm()) {
        this.toolBar.getMMHelpDocument();
      }
      else {
        this.toolBar.getHelpFrm().setAlwaysOnTop(true);
        this.toolBar.getHelpFrm().setAlwaysOnTop(false);
      } 
    } 
  }
}
