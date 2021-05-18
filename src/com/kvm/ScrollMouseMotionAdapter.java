package com.kvm;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
class ScrollMouseMotionAdapter
  extends MouseMotionAdapter
{
  private KVMInterface kvmInterface = null;
  public ScrollMouseMotionAdapter(KVMInterface refer) {
    this.kvmInterface = refer;
  }
  public void mouseMoved(MouseEvent e) {
    if (null != this.kvmInterface.getFloatToolbar()) {
      this.kvmInterface.getFloatToolbar().getImagePanel().setCursor(this.kvmInterface.getBase().getDefCursor());
      this.kvmInterface.getFloatToolbar()
        .getImagePanel()
        .getKvmInterface()
        .setCursor(this.kvmInterface.getBase().getDefCursor());
    } 
  }
}
