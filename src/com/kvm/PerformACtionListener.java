package com.kvm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class PerformACtionListener
  implements ActionListener
{
  private ImagePane imagePane = null;
  public PerformACtionListener(ImagePane refer) {
    this.imagePane = refer;
  }
  public void actionPerformed(ActionEvent evt) {
    this.imagePane.setIfmove(true);
    if (this.imagePane.getRemoteX() == 65535 && this.imagePane.getRemoteY() == 65535) {
      if (!this.imagePane.isNew())
      {
        this.imagePane.getBladeThread()
          .getBladeCommu()
          .sentData(this.imagePane.getPack().mousePack(65535, 65535, this.imagePane.getBladeNumber()));
      }
    }
    else {
      if (this.imagePane.isActionFlage()) {
        this.imagePane.setActionFlage(false);
        return;
      } 
      if (this.imagePane.isIsmove()) {
        this.imagePane.setIsmove(false);
        return;
      } 
      if (this.imagePane.getRemotePreX() == this.imagePane.getRemoteX() && this.imagePane
        .getRemotePreY() == this.imagePane.getRemoteY() && this.imagePane
        .getRemoteX() != this.imagePane.getPosiX() - this.imagePane.getImagePaneX() && this.imagePane
        .getRemoteY() != this.imagePane.getPosiY() - this.imagePane.getImagePaneY()) {
        byte[] bytes = this.imagePane.getPack().mousePack(this.imagePane.getPosiX() - this.imagePane.getImagePaneX(), this.imagePane
            .getPosiY() - this.imagePane.getImagePaneY(), this.imagePane
            .getBladeNumber());
        this.imagePane.getBladeThread().getBladeCommu().sentData(bytes);
        this.imagePane.setIfmove(false);
        this.imagePane.setIsmove(true);
      } 
    } 
  }
}
