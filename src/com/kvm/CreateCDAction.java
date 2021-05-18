package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
class CreateCDAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FloatToolbar ftb_CreateCDAction = null;
  CreateCDAction(FloatToolbar floatToolbar) {
    this.ftb_CreateCDAction = floatToolbar;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.ftb_CreateCDAction.getVirtualMedia().getCdp().isShowing()) {
      this.ftb_CreateCDAction.setShowingCD(false);
      this.ftb_CreateCDAction.getVirtualMedia().getCdp().setVisible(false);
      this.ftb_CreateCDAction.getBtnCDMenu().setBorder(null);
    }
    else {
      if (this.ftb_CreateCDAction.isShowingFlp()) {
        this.ftb_CreateCDAction.setShowingFlp(false);
        this.ftb_CreateCDAction.getVirtualMedia().getFlp().setVisible(false);
        this.ftb_CreateCDAction.getBtnFlpMenu().setBorder(null);
      } 
      if (this.ftb_CreateCDAction.isShowingImagep())
      {
        if (this.ftb_CreateCDAction.getKvmInterface().getBladeSize() == 1) {
          this.ftb_CreateCDAction.setShowingImagep(false);
          this.ftb_CreateCDAction.getKvmInterface().getImageFile().setVisible(false);
          this.ftb_CreateCDAction.getBtnCreateImage().setBorder(null);
        } 
      }
      this.ftb_CreateCDAction.setShowingCD(true);
      this.ftb_CreateCDAction.getVirtualMedia().getCdp().setVisible(true);
      this.ftb_CreateCDAction.getBtnCDMenu().setBorder(BorderFactory.createBevelBorder(1));
    } 
  }
}
