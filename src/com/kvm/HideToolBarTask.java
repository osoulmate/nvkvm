package com.kvm;
import java.util.TimerTask;
class HideToolBarTask
  extends TimerTask
{
  private KVMInterface kvmInterface = null;
  public HideToolBarTask(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public void run() {
    if (!this.kvmInterface.getFloatToolbar().isShowPanel()) {
      this.kvmInterface.getFloatToolbar().setVisible(false);
      if (this.kvmInterface.getFloatToolbar().isVirtualMedia())
      {
        this.kvmInterface.getFloatToolbar().setVirtualMediaVisible(false, false);
      }
      if (this.kvmInterface.getBladeSize() == 1)
      {
        this.kvmInterface.getImageFile().setVisible(false);
      }
      if (null != this.kvmInterface.getFloatToolbar().getPowerMenu() && this.kvmInterface
        .getFloatToolbar().getPowerMenu().isShowing())
      {
        this.kvmInterface.getFloatToolbar().getPowerMenu().setVisible(false);
      }
      if (null != this.kvmInterface.getFloatToolbar().getMouseMenu() && this.kvmInterface
        .getFloatToolbar().getMouseMenu().isShowing())
      {
        this.kvmInterface.getFloatToolbar().getMouseMenu().setVisible(false);
      }
    } 
  }
}
