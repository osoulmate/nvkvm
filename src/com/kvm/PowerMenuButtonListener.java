package com.kvm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class PowerMenuButtonListener
  implements ActionListener
{
  private FullScreen refer_PowerMenuButton;
  private FullToolBar fullTollBarRefer;
  public PowerMenuButtonListener(FullScreen refer_PowerMenuButton, FullToolBar fullTollBarRefer) {
    this.refer_PowerMenuButton = refer_PowerMenuButton;
    this.fullTollBarRefer = fullTollBarRefer;
  }
  public void actionPerformed(ActionEvent e) {
    this.refer_PowerMenuButton.getToolBarFrame().setVisible(true);
    if (this.refer_PowerMenuButton.getKvmInterface().getFloatToolbar().isShowingImagep()) {
      this.refer_PowerMenuButton.getKvmInterface().getFloatToolbar().setShowingImagep(false);
      this.refer_PowerMenuButton.getKvmInterface().getImageFile().setVisible(false);
      this.refer_PowerMenuButton.getImageMenu().setVisible(false);
      this.fullTollBarRefer.getBtnCreateImage().setBorder(null);
    } 
    if (this.refer_PowerMenuButton.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
      this.refer_PowerMenuButton.getCdMenu().setVisible(false);
      this.refer_PowerMenuButton.getFlpMenu().setVisible(false);
      this.fullTollBarRefer.getBtnCDMenu().setBorder(null);
      this.fullTollBarRefer.getBtnFlpMenu().setBorder(null);
    } 
    this.fullTollBarRefer.getPowerMenu().show(this.refer_PowerMenuButton.getImageParentPane(), this.fullTollBarRefer
        .getPowerMenuButton().getX() + this.refer_PowerMenuButton.getToolBarFrame().getX() + this.refer_PowerMenuButton
        .getH(), this.refer_PowerMenuButton
        .getToolBarFrame().getHeight() + this.refer_PowerMenuButton.getV() - 1);
  }
}
