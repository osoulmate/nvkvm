package com.kvm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class MouseMenuButtonListener
  implements ActionListener
{
  private FullScreen refer_MouseMenuButton;
  private FullToolBar fullTollBarRefer;
  public MouseMenuButtonListener(FullScreen refer_MouseMenuButton, FullToolBar fullTollBarRefer) {
    this.refer_MouseMenuButton = refer_MouseMenuButton;
    this.fullTollBarRefer = fullTollBarRefer;
  }
  public void actionPerformed(ActionEvent e) {
    this.refer_MouseMenuButton.getToolBarFrame().setVisible(true);
    if (this.refer_MouseMenuButton.getKvmInterface().getFloatToolbar().isShowingImagep()) {
      this.refer_MouseMenuButton.getKvmInterface().getFloatToolbar().setShowingImagep(false);
      this.refer_MouseMenuButton.getKvmInterface().getImageFile().setVisible(false);
      this.refer_MouseMenuButton.getImageMenu().setVisible(false);
      this.fullTollBarRefer.getBtnCreateImage().setBorder(null);
    } 
    if (this.refer_MouseMenuButton.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
      this.refer_MouseMenuButton.getCdMenu().setVisible(false);
      this.refer_MouseMenuButton.getFlpMenu().setVisible(false);
      this.fullTollBarRefer.getBtnCDMenu().setBorder(null);
      this.fullTollBarRefer.getBtnFlpMenu().setBorder(null);
    } 
    this.fullTollBarRefer.getMouseMenu().show(this.refer_MouseMenuButton.getImageParentPane(), this.fullTollBarRefer
        .getMouseMenuButton().getX() + this.refer_MouseMenuButton.getToolBarFrame().getX() + this.refer_MouseMenuButton
        .getH(), this.refer_MouseMenuButton
        .getToolBarFrame().getHeight() + this.refer_MouseMenuButton.getV() - 1);
  }
}
