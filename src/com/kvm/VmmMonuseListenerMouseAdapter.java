package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
class VmmMonuseListenerMouseAdapter
  extends MouseAdapter
{
  private FloatToolbar ftb_VmmMonuseListenerMouse = null;
  VmmMonuseListenerMouseAdapter(FloatToolbar floatToolbar) {
    this.ftb_VmmMonuseListenerMouse = floatToolbar;
  }
  public void mouseEntered(MouseEvent e) {
    if (!this.ftb_VmmMonuseListenerMouse.getKvmInterface().isFullScreen()) {
      this.ftb_VmmMonuseListenerMouse.getImagePanel()
        .getKvmInterface()
        .setCursor(this.ftb_VmmMonuseListenerMouse.getKvmInterface().getBase().getDefCursor());
      this.ftb_VmmMonuseListenerMouse.getImagePanel().setCursor(this.ftb_VmmMonuseListenerMouse.getKvmInterface()
          .getBase()
          .getDefCursor());
    } 
  }
}
