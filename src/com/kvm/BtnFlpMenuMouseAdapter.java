package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
class BtnFlpMenuMouseAdapter
  extends MouseAdapter
{
  private FloatToolbar ftb_BtnFlpMenuMouse = null;
  BtnFlpMenuMouseAdapter(FloatToolbar floatToolbar) {
    this.ftb_BtnFlpMenuMouse = floatToolbar;
  }
  public void mouseEntered(MouseEvent e) {
    this.ftb_BtnFlpMenuMouse.getBtnFlpMenu().setContentAreaFilled(true);
  }
  public void mouseExited(MouseEvent e) {
    this.ftb_BtnFlpMenuMouse.getBtnFlpMenu().setContentAreaFilled(false);
  }
}
