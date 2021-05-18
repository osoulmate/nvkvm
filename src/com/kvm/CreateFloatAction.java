package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.border.Border;
class CreateFloatAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FloatToolbar ftb_CreateFloatAction = null;
  CreateFloatAction(FloatToolbar floatToolbar) {
    this.ftb_CreateFloatAction = floatToolbar;
  }
  public void actionPerformed(ActionEvent e) {
    if (!this.ftb_CreateFloatAction.isShowPanel()) {
      this.ftb_CreateFloatAction.getFloatToolbar().setVisible(true);
      this.ftb_CreateFloatAction.setShowPanel(true);
      this.ftb_CreateFloatAction.getBtnShow().setIcon(new ImageIcon(
            getClass().getResource("resource/images/float.gif")));
    }
    else if (this.ftb_CreateFloatAction.getFloatToolbar().isShowing()) {
      if (null != this.ftb_CreateFloatAction.getVirtualMedia()) {
        this.ftb_CreateFloatAction.getVirtualMedia().getFlp().setVisible(false);
        this.ftb_CreateFloatAction.getVirtualMedia().getCdp().setVisible(false);
        this.ftb_CreateFloatAction.setShowingCD(false);
        this.ftb_CreateFloatAction.setShowingFlp(false);
        this.ftb_CreateFloatAction.getBtnCDMenu().setBorder((Border)null);
        this.ftb_CreateFloatAction.getBtnFlpMenu().setBorder((Border)null);
      } 
      if (this.ftb_CreateFloatAction.getKvmInterface().getBladeSize() == 1) {
        this.ftb_CreateFloatAction.getKvmInterface().getImageFile().setVisible(false);
        this.ftb_CreateFloatAction.setShowingImagep(false);
        this.ftb_CreateFloatAction.getBtnCreateImage().setBorder((Border)null);
      } 
      this.ftb_CreateFloatAction.getFloatToolbar().setVisible(false);
      this.ftb_CreateFloatAction.setShowPanel(false);
      this.ftb_CreateFloatAction.getBtnShow().setIcon(new ImageIcon(
            getClass().getResource("resource/images/float2.gif")));
    }
    else {
      this.ftb_CreateFloatAction.getFloatToolbar().setVisible(true);
      this.ftb_CreateFloatAction.setShowPanel(true);
      this.ftb_CreateFloatAction.getBtnShow().setIcon(new ImageIcon(
            getClass().getResource("resource/images/float.gif")));
    } 
  }
}
