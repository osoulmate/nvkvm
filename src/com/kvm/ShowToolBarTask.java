package com.kvm;
import java.util.TimerTask;
class ShowToolBarTask
  extends TimerTask
{
  private KVMInterface kvmInterface = null;
  public ShowToolBarTask(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public void run() {
    this.kvmInterface.getFloatToolbar().setVisible(true);
    if (this.kvmInterface.getFloatToolbar().isVirtualMedia())
    {
      this.kvmInterface.getFloatToolbar().setVirtualMediaVisible(this.kvmInterface.getFloatToolbar().isShowingCD(), this.kvmInterface
          .getFloatToolbar().isShowingFlp());
    }
    if (this.kvmInterface.getBladeSize() == 1)
    {
      this.kvmInterface.getImageFile().setVisible(this.kvmInterface.getFloatToolbar().isShowingImagep());
    }
  }
}
