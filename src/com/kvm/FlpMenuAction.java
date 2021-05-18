package com.kvm;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
class FlpMenuAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FullScreen refer_FlpMenuAction;
  private FullToolBar fullTollBarRefer;
  public FlpMenuAction(FullScreen refer_FlpMenuAction, FullToolBar fullTollBarRefer) {
    this.refer_FlpMenuAction = refer_FlpMenuAction;
    this.fullTollBarRefer = fullTollBarRefer;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.refer_FlpMenuAction.getFlpMenu().isShowing()) {
      this.refer_FlpMenuAction.getKvmInterface().getFloatToolbar().setShowingFlp(false);
      this.refer_FlpMenuAction.getFlpMenu().setVisible(false);
      this.fullTollBarRefer.getBtnFlpMenu().setBorder(null);
    }
    else {
      if (this.refer_FlpMenuAction.getKvmInterface().getFloatToolbar().isShowingImagep()) {
        this.refer_FlpMenuAction.getKvmInterface().getFloatToolbar().setShowingImagep(false);
        this.refer_FlpMenuAction.getKvmInterface().getImageFile().setVisible(false);
        this.refer_FlpMenuAction.getImageMenu().setVisible(false);
        this.fullTollBarRefer.getBtnCreateImage().setBorder(null);
      } 
      if (this.refer_FlpMenuAction.getKvmInterface().getFloatToolbar().isShowingCD()) {
        this.refer_FlpMenuAction.getKvmInterface().getFloatToolbar().setShowingCD(false);
        this.refer_FlpMenuAction.getCdMenu().setVisible(false);
        this.fullTollBarRefer.getBtnCDMenu().setBorder(null);
      } 
      this.refer_FlpMenuAction.getKvmInterface().getFloatToolbar().setShowingFlp(true);
      this.fullTollBarRefer.getBtnFlpMenu().setBorder(BorderFactory.createBevelBorder(1));
      this.refer_FlpMenuAction.getKvmInterface().getFloatToolbar().setFlpVisibleAndLocation(true, 0, 0);
      this.refer_FlpMenuAction.getFlpMenu().getContentPane().removeAll();
      this.refer_FlpMenuAction.getFlpMenu()
        .getContentPane()
        .add(this.refer_FlpMenuAction.getKvmInterface().getFloatToolbar().getFlpPanel());
      if (KVMUtil.isWindowsOS()) {
        this.refer_FlpMenuAction.getFlpMenu()
          .setLocation(
            (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.refer_FlpMenuAction.getKvmInterface().getFloatToolbar().getFlpWidth()) / 2, this.refer_FlpMenuAction
            .getToolBarFrame().getHeight());
      }
      else if (KVMUtil.isLinuxOS()) {
        if (KVMUtil.isLinux()) {
          int offset = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.refer_FlpMenuAction.getImageParentPane().getHeight();
          if (offset > 30)
          {
            offset /= 2;
          }
          this.refer_FlpMenuAction.getFlpMenu()
            .setLocation(
              (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.refer_FlpMenuAction.getKvmInterface().getFloatToolbar().getFlpWidth()) / 2, this.refer_FlpMenuAction
              .getToolBarFrame().getHeight() + offset - 1);
        }
        else {
          this.refer_FlpMenuAction.getFlpMenu()
            .setLocation(
              (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.refer_FlpMenuAction.getKvmInterface().getFloatToolbar().getFlpWidth()) / 2, this.refer_FlpMenuAction
              .getToolBarFrame().getHeight() + this.refer_FlpMenuAction.getToolBarFrame().getY() + 21);
        } 
      } 
      this.refer_FlpMenuAction.getFlpMenu().setVisible(true);
    } 
  }
}
