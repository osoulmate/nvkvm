package com.kvm;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
class CDMenuAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FullScreen refer_CDMenuAction;
  private FullToolBar fullTollBarRefer;
  public CDMenuAction(FullScreen refer_CDMenuAction, FullToolBar fullTollBarRefer) {
    this.refer_CDMenuAction = refer_CDMenuAction;
    this.fullTollBarRefer = fullTollBarRefer;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.refer_CDMenuAction.getCdMenu().isShowing()) {
      this.refer_CDMenuAction.getKvmInterface().getFloatToolbar().setShowingCD(false);
      this.refer_CDMenuAction.getCdMenu().setVisible(false);
      this.fullTollBarRefer.getBtnCDMenu().setBorder(null);
    }
    else {
      if (this.refer_CDMenuAction.getKvmInterface().getFloatToolbar().isShowingImagep()) {
        this.refer_CDMenuAction.getKvmInterface().getFloatToolbar().setShowingImagep(false);
        this.refer_CDMenuAction.getKvmInterface().getImageFile().setVisible(false);
        this.refer_CDMenuAction.getImageMenu().setVisible(false);
        this.fullTollBarRefer.getBtnCreateImage().setBorder(null);
      } 
      if (this.refer_CDMenuAction.getKvmInterface().getFloatToolbar().isShowingFlp()) {
        this.refer_CDMenuAction.getKvmInterface().getFloatToolbar().setShowingFlp(false);
        this.refer_CDMenuAction.getFlpMenu().setVisible(false);
        this.fullTollBarRefer.getBtnFlpMenu().setBorder(null);
      } 
      this.refer_CDMenuAction.getKvmInterface().getFloatToolbar().setShowingCD(true);
      this.fullTollBarRefer.getBtnCDMenu().setBorder(BorderFactory.createBevelBorder(1));
      this.refer_CDMenuAction.getKvmInterface().getFloatToolbar().setCDVisibleAndLocation(true, 0, 0);
      this.refer_CDMenuAction.getCdMenu().getContentPane().removeAll();
      this.refer_CDMenuAction.getCdMenu()
        .getContentPane()
        .add(this.refer_CDMenuAction.getKvmInterface().getFloatToolbar().getCDPanel());
      if (KVMUtil.isWindowsOS()) {
        this.refer_CDMenuAction.getCdMenu()
          .setLocation(
            (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.refer_CDMenuAction.getKvmInterface().getFloatToolbar().getCDWidth()) / 2, this.refer_CDMenuAction
            .getToolBarFrame().getHeight());
      }
      else if (KVMUtil.isLinuxOS()) {
        if (KVMUtil.isLinux()) {
          int offset = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.refer_CDMenuAction.getImageParentPane().getHeight();
          if (offset > 30)
          {
            offset /= 2;
          }
          this.refer_CDMenuAction.getCdMenu()
            .setLocation(
              (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.refer_CDMenuAction.getKvmInterface().getFloatToolbar().getCDWidth()) / 2, this.refer_CDMenuAction
              .getToolBarFrame().getHeight() + offset - 1);
        }
        else {
          this.refer_CDMenuAction.getCdMenu()
            .setLocation(
              (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.refer_CDMenuAction.getKvmInterface().getFloatToolbar().getCDWidth()) / 2, this.refer_CDMenuAction
              .getToolBarFrame().getHeight() + this.refer_CDMenuAction.getToolBarFrame().getY() + 21);
        } 
      } 
      this.refer_CDMenuAction.getCdMenu().setVisible(true);
    } 
  }
}
