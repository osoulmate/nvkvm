package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
class BtnCDMenuMouseAdapter
  extends MouseAdapter
{
  private FloatToolbar ftb_BtnCDMenuMouse = null;
  BtnCDMenuMouseAdapter(FloatToolbar floatToolbar) {
    this.ftb_BtnCDMenuMouse = floatToolbar;
  }
  public void mouseEntered(MouseEvent e) {
    this.ftb_BtnCDMenuMouse.getBtnCDMenu().setContentAreaFilled(true);
  }
  public void mouseExited(MouseEvent e) {
    this.ftb_BtnCDMenuMouse.getBtnCDMenu().setContentAreaFilled(false);
  }
}
