package com.kvm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class KeyboardLayoutMenuButtonListener
  implements ActionListener
{
  private FullScreen fullScreen;
  private FullToolBar fullTollBarRefer;
  public KeyboardLayoutMenuButtonListener(FullScreen fullScreen, FullToolBar fullTollBarRefer) {
    this.fullScreen = fullScreen;
    this.fullTollBarRefer = fullTollBarRefer;
  }
  public void actionPerformed(ActionEvent e) {
    this.fullScreen.getToolBarFrame().setVisible(true);
    if (this.fullScreen.getKvmInterface().getFloatToolbar().isShowingImagep()) {
      this.fullScreen.getKvmInterface().getFloatToolbar().setShowingImagep(false);
      this.fullScreen.getKvmInterface().getImageFile().setVisible(false);
      this.fullScreen.getImageMenu().setVisible(false);
      this.fullTollBarRefer.getBtnCreateImage().setBorder(null);
    } 
    if (this.fullScreen.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
      this.fullScreen.getCdMenu().setVisible(false);
      this.fullScreen.getFlpMenu().setVisible(false);
      this.fullTollBarRefer.getBtnCDMenu().setBorder(null);
      this.fullTollBarRefer.getBtnFlpMenu().setBorder(null);
    } 
    this.fullTollBarRefer.getKeyboardLayoutMenu().show(this.fullScreen.getImageParentPane(), this.fullTollBarRefer
        .getKeyboardLayoutButton().getX() + this.fullScreen.getToolBarFrame().getX() + this.fullScreen
        .getH(), this.fullScreen
        .getToolBarFrame().getHeight() + this.fullScreen.getV() - 1);
  }
}
