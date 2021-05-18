package com.kvm;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
class HListener
  implements AdjustmentListener
{
  private FullScreen refer_HListener;
  public HListener(FullScreen refer_HListener) {
    this.refer_HListener = refer_HListener;
  }
  public void adjustmentValueChanged(AdjustmentEvent e) {
    this.refer_HListener.setH(e.getValue());
    if (this.refer_HListener.getNewh() != this.refer_HListener.getH()) {
      if (this.refer_HListener.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
        this.refer_HListener.getCdMenu().setVisible(false);
        this.refer_HListener.getFlpMenu().setVisible(false);
      } 
      this.refer_HListener.getToolBarFrame().setVisible(false);
      if (this.refer_HListener.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
        this.refer_HListener.getToolBar().getBtnCDMenu().setBorder(null);
        this.refer_HListener.getToolBar().getBtnFlpMenu().setBorder(null);
      } 
    } 
    this.refer_HListener.setNewh(this.refer_HListener.getH());
    if (null != this.refer_HListener.getToolBar() && KVMUtil.isWindowsOS())
    {
      if (null != this.refer_HListener.getToolBar().getPowerMenu())
      {
        this.refer_HListener.getToolBar().getPowerMenu().setVisible(false);
      }
    }
  }
}
