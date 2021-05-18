package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
class CreateFlpAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FloatToolbar ftb_CreateFlpAction = null;
  CreateFlpAction(FloatToolbar floatToolbar) {
    this.ftb_CreateFlpAction = floatToolbar;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.ftb_CreateFlpAction.getVirtualMedia().getFlp().isShowing()) {
      this.ftb_CreateFlpAction.setShowingFlp(false);
      this.ftb_CreateFlpAction.getVirtualMedia().getFlp().setVisible(false);
      this.ftb_CreateFlpAction.getBtnFlpMenu().setBorder(null);
    }
    else {
      if (this.ftb_CreateFlpAction.isShowingCD()) {
        this.ftb_CreateFlpAction.setShowingCD(false);
        this.ftb_CreateFlpAction.getVirtualMedia().getCdp().setVisible(false);
        this.ftb_CreateFlpAction.getBtnCDMenu().setBorder(null);
      } 
      if (this.ftb_CreateFlpAction.isShowingImagep())
      {
        if (this.ftb_CreateFlpAction.getKvmInterface().getBladeSize() == 1) {
          this.ftb_CreateFlpAction.setShowingImagep(false);
          this.ftb_CreateFlpAction.getKvmInterface().getImageFile().setVisible(false);
          this.ftb_CreateFlpAction.getBtnCreateImage().setBorder(null);
        } 
      }
      this.ftb_CreateFlpAction.setShowingFlp(true);
      this.ftb_CreateFlpAction.getVirtualMedia().getFlp().setVisible(true);
      this.ftb_CreateFlpAction.getBtnFlpMenu().setBorder(BorderFactory.createBevelBorder(1));
    } 
  }
}
