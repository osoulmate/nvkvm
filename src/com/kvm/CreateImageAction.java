package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
class CreateImageAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FloatToolbar ftb_CreateImageAction = null;
  CreateImageAction(FloatToolbar floatToolbar) {
    this.ftb_CreateImageAction = floatToolbar;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.ftb_CreateImageAction.getKvmInterface().getBladeSize() == 1)
    {
      if (this.ftb_CreateImageAction.getKvmInterface().getImageFile().isShowing()) {
        this.ftb_CreateImageAction.getFloatToolbar().setShowingImagep(false);
        this.ftb_CreateImageAction.getKvmInterface().getImageFile().setVisible(false);
        this.ftb_CreateImageAction.getBtnCreateImage().setBorder(null);
      }
      else {
        if (null != this.ftb_CreateImageAction.getVirtualMedia()) {
          if (this.ftb_CreateImageAction.getFloatToolbar().isShowingCD()) {
            this.ftb_CreateImageAction.getFloatToolbar().setShowingCD(false);
            this.ftb_CreateImageAction.getVirtualMedia().getCdp().setVisible(false);
            this.ftb_CreateImageAction.getFloatToolbar().getBtnCDMenu().setBorder(null);
          } 
          if (this.ftb_CreateImageAction.getFloatToolbar().isShowingFlp()) {
            this.ftb_CreateImageAction.getFloatToolbar().setShowingCD(false);
            this.ftb_CreateImageAction.getVirtualMedia().getFlp().setVisible(false);
            this.ftb_CreateImageAction.getFloatToolbar().getBtnFlpMenu().setBorder(null);
          } 
        } 
        this.ftb_CreateImageAction.getFloatToolbar().setShowingImagep(true);
        this.ftb_CreateImageAction.getKvmInterface().getImageFile().setVisible(true);
        this.ftb_CreateImageAction.getBtnCreateImage()
          .setBorder(BorderFactory.createBevelBorder(1));
      } 
    }
  }
}
