package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class CreateMouseMenuButton
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FloatToolbar ftb_CreateMouseMenuButton = null;
  CreateMouseMenuButton(FloatToolbar floatToolbar) {
    this.ftb_CreateMouseMenuButton = floatToolbar;
  }
  public void actionPerformed(ActionEvent e) {
    if (null != this.ftb_CreateMouseMenuButton.getVirtualMedia()) {
      this.ftb_CreateMouseMenuButton.getVirtualMedia().getCdp().setVisible(false);
      this.ftb_CreateMouseMenuButton.getVirtualMedia().getFlp().setVisible(false);
      this.ftb_CreateMouseMenuButton.setShowingCD(false);
      this.ftb_CreateMouseMenuButton.setShowingFlp(false);
    } 
    if (this.ftb_CreateMouseMenuButton.getKvmInterface().getBladeSize() == 1) {
      this.ftb_CreateMouseMenuButton.getKvmInterface().getImageFile().setVisible(false);
      this.ftb_CreateMouseMenuButton.getBtnCreateImage().setBorder(null);
      this.ftb_CreateMouseMenuButton.setShowingImagep(false);
    } 
    if (null != this.ftb_CreateMouseMenuButton.getVirtualMedia()) {
      this.ftb_CreateMouseMenuButton.getBtnCDMenu().setBorder(null);
      this.ftb_CreateMouseMenuButton.getBtnFlpMenu().setBorder(null);
    } 
    this.ftb_CreateMouseMenuButton.getMouseMenu().show(this.ftb_CreateMouseMenuButton.getImagePanel(), this.ftb_CreateMouseMenuButton
        .getMouseMenuButton().getX() + this.ftb_CreateMouseMenuButton.getFloatToolbar().getX(), this.ftb_CreateMouseMenuButton
        .getFloatToolbar().getHeight() + this.ftb_CreateMouseMenuButton
        .getKvmInterface().getVv() - 1);
  }
}
