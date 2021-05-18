package com.kvm;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
class ImageAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FullScreen refer_ImageAction;
  private FullToolBar fullTollBarRefer;
  public ImageAction(FullScreen refer_ImageAction, FullToolBar fullTollBarRefer) {
    this.refer_ImageAction = refer_ImageAction;
    this.fullTollBarRefer = fullTollBarRefer;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.refer_ImageAction.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
      this.refer_ImageAction.getCdMenu().setVisible(false);
      this.refer_ImageAction.getFlpMenu().setVisible(false);
      this.fullTollBarRefer.getBtnCDMenu().setBorder(null);
      this.fullTollBarRefer.getBtnFlpMenu().setBorder(null);
    } 
    if (this.refer_ImageAction.getKvmInterface().getFloatToolbar().isShowingImagep()) {
      this.refer_ImageAction.getKvmInterface().getFloatToolbar().setShowingImagep(false);
      this.refer_ImageAction.getKvmInterface().getImageFile().setVisible(false);
      this.refer_ImageAction.getImageMenu().setVisible(false);
      this.fullTollBarRefer.getBtnCreateImage().setBorder(null);
    }
    else {
      this.refer_ImageAction.getKvmInterface().getFloatToolbar().setShowingImagep(true);
      this.refer_ImageAction.getKvmInterface().getImageFile().setVisible(true);
      this.fullTollBarRefer.getBtnCreateImage().setBorder(BorderFactory.createBevelBorder(1));
      this.refer_ImageAction.getImageMenu().getContentPane().removeAll();
      this.refer_ImageAction.getImageMenu().getContentPane().add(this.refer_ImageAction.getKvmInterface().getImageFile());
      if (KVMUtil.isWindowsOS()) {
        this.refer_ImageAction.getImageMenu()
          .setLocation(
            (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.refer_ImageAction.getKvmInterface().getImageFile().getWidth()) / 2, this.refer_ImageAction
            .getToolBarFrame().getHeight());
      }
      else if (KVMUtil.isLinuxOS()) {
        if (KVMUtil.isLinux()) {
          int offset = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.refer_ImageAction.getImageParentPane().getHeight();
          if (offset > 30)
          {
            offset /= 2;
          }
          this.refer_ImageAction.getImageMenu()
            .setLocation(
              (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.refer_ImageAction.getKvmInterface().getImageFile().getWidth()) / 2, this.refer_ImageAction
              .getToolBarFrame().getHeight() + offset - 1);
        }
        else {
          this.refer_ImageAction.getImageMenu()
            .setLocation(
              (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.refer_ImageAction.getKvmInterface().getImageFile().getWidth()) / 2, this.refer_ImageAction
              .getToolBarFrame().getHeight() + this.refer_ImageAction.getToolBarFrame().getY() + 21);
        } 
      } 
      this.refer_ImageAction.getImageMenu().setVisible(true);
    } 
  }
}
