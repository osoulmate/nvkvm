package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class CreateKeyboardLayoutMenuButton
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FloatToolbar floatToolbar = null;
  CreateKeyboardLayoutMenuButton(FloatToolbar floatToolbar) {
    this.floatToolbar = floatToolbar;
  }
  public void actionPerformed(ActionEvent e) {
    if (null != this.floatToolbar.getVirtualMedia()) {
      this.floatToolbar.getVirtualMedia().getCdp().setVisible(false);
      this.floatToolbar.getVirtualMedia().getFlp().setVisible(false);
      this.floatToolbar.setShowingCD(false);
      this.floatToolbar.setShowingFlp(false);
    } 
    if (this.floatToolbar.getKvmInterface().getBladeSize() == 1) {
      this.floatToolbar.getKvmInterface().getImageFile().setVisible(false);
      this.floatToolbar.getBtnCreateImage().setBorder(null);
      this.floatToolbar.setShowingImagep(false);
    } 
    if (null != this.floatToolbar.getVirtualMedia()) {
      this.floatToolbar.getBtnCDMenu().setBorder(null);
      this.floatToolbar.getBtnFlpMenu().setBorder(null);
    } 
    this.floatToolbar.getKeyboardLayoutMenu().show(this.floatToolbar.getImagePanel(), this.floatToolbar
        .getKeyboardLayoutButton().getX() + this.floatToolbar.getFloatToolbar().getX(), this.floatToolbar
        .getFloatToolbar().getHeight() + this.floatToolbar.getKvmInterface().getVv() - 1);
  }
}
