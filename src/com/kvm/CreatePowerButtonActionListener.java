package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class CreatePowerButtonActionListener
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FloatToolbar ftb_CreatePowerButton = null;
  CreatePowerButtonActionListener(FloatToolbar floatToolbar) {
    this.ftb_CreatePowerButton = floatToolbar;
  }
  public void actionPerformed(ActionEvent e) {
    if (null != this.ftb_CreatePowerButton.getVirtualMedia()) {
      this.ftb_CreatePowerButton.getVirtualMedia().getCdp().setVisible(false);
      this.ftb_CreatePowerButton.getVirtualMedia().getFlp().setVisible(false);
      this.ftb_CreatePowerButton.setShowingCD(false);
      this.ftb_CreatePowerButton.setShowingFlp(false);
    } 
    if (this.ftb_CreatePowerButton.getKvmInterface().getBladeSize() == 1) {
      this.ftb_CreatePowerButton.getKvmInterface().getImageFile().setVisible(false);
      this.ftb_CreatePowerButton.getBtnCreateImage().setBorder(null);
      this.ftb_CreatePowerButton.setShowingImagep(false);
    } 
    if (null != this.ftb_CreatePowerButton.getVirtualMedia()) {
      this.ftb_CreatePowerButton.getBtnCDMenu().setBorder(null);
      this.ftb_CreatePowerButton.getBtnFlpMenu().setBorder(null);
    } 
    this.ftb_CreatePowerButton.getPowerMenu().show(this.ftb_CreatePowerButton.getImagePanel(), this.ftb_CreatePowerButton
        .getPowerMenuButton().getX() + this.ftb_CreatePowerButton.getFloatToolbar().getX(), this.ftb_CreatePowerButton
        .getFloatToolbar().getHeight() + this.ftb_CreatePowerButton.getKvmInterface().getVv() - 1);
  }
}
