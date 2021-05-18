package com.kvm;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
class HAdjustmentListener
  implements AdjustmentListener
{
  private KVMInterface kvmInterface = null;
  public HAdjustmentListener(KVMInterface refer) {
    this.kvmInterface = refer;
  }
  public void adjustmentValueChanged(AdjustmentEvent e) {
    if (null == this.kvmInterface.getFloatToolbar()) {
      return;
    }
    this.kvmInterface.setH(e.getValue());
    if (this.kvmInterface.getNewh() != this.kvmInterface.getH())
    {
      if (this.kvmInterface.getFloatToolbar().getImgwidth() >= Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
        this.kvmInterface.getFloatToolbar()
          .setLocation(
            (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.kvmInterface.getFloatToolbar().getWidth()) / 2 + this.kvmInterface
            .getH(), this.kvmInterface
            .getVv() - 1);
        if (this.kvmInterface.getFloatToolbar().isVirtualMedia()) {
          this.kvmInterface.getFloatToolbar().setFlpLocation(
              (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.kvmInterface.getFloatToolbar().getFlpWidth()) / 2 + this.kvmInterface
              .getH(), this.kvmInterface
              .getFloatToolbar().getHeight() + this.kvmInterface.getVv() - 1);
          this.kvmInterface.getFloatToolbar().setCDLocation(
              (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.kvmInterface.getFloatToolbar().getCDWidth()) / 2 + this.kvmInterface
              .getH(), this.kvmInterface
              .getFloatToolbar().getHeight() + this.kvmInterface.getVv() - 1);
        } 
        if (this.kvmInterface.getBladeSize() == 1)
        {
          this.kvmInterface.getImageFile().setLocation(
              (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.kvmInterface.getImageFile().getWidth()) / 2 + this.kvmInterface
              .getH(), this.kvmInterface
              .getFloatToolbar().getHeight() + this.kvmInterface.getVv() - 1);
        }
      }
      else {
        this.kvmInterface.getFloatToolbar()
          .setLocation((this.kvmInterface.getFloatToolbar().getImgwidth() - this.kvmInterface.getFloatToolbar()
            .getWidth()) / 2 + this.kvmInterface.getH(), this.kvmInterface
            .getVv() - 1);
        if (this.kvmInterface.getFloatToolbar().isVirtualMedia()) {
          this.kvmInterface.getFloatToolbar()
            .setFlpLocation((this.kvmInterface.getFloatToolbar().getImgwidth() - this.kvmInterface.getFloatToolbar()
              .getFlpWidth()) / 2 + this.kvmInterface
              .getH(), this.kvmInterface
              .getFloatToolbar().getHeight() + this.kvmInterface.getVv() - 1);
          this.kvmInterface.getFloatToolbar()
            .setCDLocation((this.kvmInterface.getFloatToolbar().getImgwidth() - this.kvmInterface.getFloatToolbar()
              .getCDWidth()) / 2 + this.kvmInterface
              .getH(), this.kvmInterface
              .getFloatToolbar().getHeight() + this.kvmInterface.getVv() - 1);
        } 
        if (this.kvmInterface.getBladeSize() == 1)
        {
          this.kvmInterface.getImageFile()
            .setLocation((this.kvmInterface.getFloatToolbar().getImgwidth() - this.kvmInterface.getImageFile()
              .getWidth()) / 2 + this.kvmInterface.getH(), this.kvmInterface
              .getFloatToolbar().getHeight() + this.kvmInterface.getVv() - 1);
        }
      } 
    }
    this.kvmInterface.setNewh(this.kvmInterface.getH());
    if (null != this.kvmInterface.getFloatToolbar() && null != this.kvmInterface.getFloatToolbar().getPowerMenu())
    {
      this.kvmInterface.getFloatToolbar().getPowerMenu().setVisible(false);
    }
  }
}
