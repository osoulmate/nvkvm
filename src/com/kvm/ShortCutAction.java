package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
class ShortCutAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FullScreen refer_ShortCutAction;
  public ShortCutAction(FullScreen refer_ShortCutAction) {
    this.refer_ShortCutAction = refer_ShortCutAction;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.refer_ShortCutAction.getKvmInterface().getFloatToolbar().isShowingImagep()) {
      this.refer_ShortCutAction.getKvmInterface().getFloatToolbar().setShowingImagep(false);
      this.refer_ShortCutAction.getKvmInterface().getImageFile().setVisible(false);
      this.refer_ShortCutAction.getImageMenu().setVisible(false);
      this.refer_ShortCutAction.getToolBar().getBtnCreateImage().setBorder(null);
    } 
    if (this.refer_ShortCutAction.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
      this.refer_ShortCutAction.getCdMenu().setVisible(false);
      this.refer_ShortCutAction.getFlpMenu().setVisible(false);
      this.refer_ShortCutAction.getToolBar().getBtnCDMenu().setBorder(null);
      this.refer_ShortCutAction.getToolBar().getBtnFlpMenu().setBorder(null);
    } 
    this.refer_ShortCutAction.getToolBarFrame().setVisible(false);
    if (this.refer_ShortCutAction.getKvmInterface()
      .getKvmUtil()
      .getImagePane(this.refer_ShortCutAction.getActionBlade())
      .isContr()) {
      this.refer_ShortCutAction.produceComKey(this.refer_ShortCutAction.getActionBlade());
    }
    else {
      JOptionPane.showMessageDialog(this.refer_ShortCutAction.getKvmInterface()
          .getKvmUtil()
          .getImagePane(this.refer_ShortCutAction.getActionBlade()), this.refer_ShortCutAction
          .getKvmInterface().getKvmUtil().getString("ListenOperation"));
    } 
  }
}
