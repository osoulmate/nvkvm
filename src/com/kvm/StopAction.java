package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class StopAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private ImageFile imageFile_StopAction = null;
  public StopAction(ImageFile refer) {
    this.imageFile_StopAction = refer;
  }
  public void actionPerformed(ActionEvent e) {
    if (!this.imageFile_StopAction.getVmApplet().isImageCreateOK()) {
      return;
    }
    this.imageFile_StopAction.getT().stop();
    this.imageFile_StopAction.getM().stop();
    this.imageFile_StopAction.getImageCreate().setEnabled(true);
    this.imageFile_StopAction.getImagebar().setValue(0);
    this.imageFile_StopAction.updateCreateImageItems("stop");
  }
}
