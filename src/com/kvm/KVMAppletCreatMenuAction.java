package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
class KVMAppletCreatMenuAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private KvmAppletToolBar toolBar = null;
  public KVMAppletCreatMenuAction(KvmAppletToolBar refer) {
    this.toolBar = refer;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.toolBar.getKvmInterface().getBladeSize() > 1)
    {
      this.toolBar.getKvmInterface()
        .getToolbar()
        .getImageButton()
        .setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1), 
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));
    }
    if (this.toolBar.getKvmInterface().getBladeSize() == 1) {
      if (this.toolBar.getKvmInterface().getImageFile().isShowing())
      {
        this.toolBar.getKvmInterface().getFloatToolbar().setShowingImagep(false);
        this.toolBar.getKvmInterface().getImageFile().setVisible(false);
      }
      else
      {
        if (this.toolBar.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
          if (this.toolBar.getKvmInterface().getFloatToolbar().isShowingCD()) {
            this.toolBar.getKvmInterface().getFloatToolbar().setShowingCD(false);
            this.toolBar.getKvmInterface().getFloatToolbar().setCDVisible(false);
          } 
          if (this.toolBar.getKvmInterface().getFloatToolbar().isShowingFlp()) {
            this.toolBar.getKvmInterface().getFloatToolbar().setShowingFlp(false);
            this.toolBar.getKvmInterface().getFloatToolbar().setFlpVisible(false);
          } 
        } 
        this.toolBar.getKvmInterface().getFloatToolbar().setShowingImagep(true);
        this.toolBar.getKvmInterface().getImageFile().setVisible(true);
      }
    }
    else if (null == this.toolBar.getFrmFr()) {
      this.toolBar.createFrFrame();
    }
    else {
      this.toolBar.getFrmFr().toFront();
    } 
  }
}
