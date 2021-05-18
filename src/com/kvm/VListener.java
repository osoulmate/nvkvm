package com.kvm;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
class VListener
  implements AdjustmentListener
{
  private FullScreen refer_VListener;
  public VListener(FullScreen refer_VListener) {
    this.refer_VListener = refer_VListener;
  }
  public void adjustmentValueChanged(AdjustmentEvent e) {
    this.refer_VListener.setV(e.getValue());
    if (this.refer_VListener.getNewv() != this.refer_VListener.getV()) {
      if (this.refer_VListener.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
        this.refer_VListener.getCdMenu().setVisible(false);
        this.refer_VListener.getFlpMenu().setVisible(false);
      } 
      this.refer_VListener.getToolBarFrame().setVisible(false);
      if (this.refer_VListener.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
        this.refer_VListener.getToolBar().getBtnCDMenu().setBorder(null);
        this.refer_VListener.getToolBar().getBtnFlpMenu().setBorder(null);
      } 
    } 
    this.refer_VListener.setNewv(this.refer_VListener.getV());
    if (null != this.refer_VListener.getToolBar() && KVMUtil.isWindowsOS())
    {
      if (null != this.refer_VListener.getToolBar().getPowerMenu())
      {
        this.refer_VListener.getToolBar().getPowerMenu().setVisible(false);
      }
    }
  }
}
