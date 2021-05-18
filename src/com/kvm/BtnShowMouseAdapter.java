package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
class BtnShowMouseAdapter
  extends MouseAdapter
{
  private FloatToolbar ftb_BtnShowMouse = null;
  BtnShowMouseAdapter(FloatToolbar floatToolbar) {
    this.ftb_BtnShowMouse = floatToolbar;
  }
  public void mouseEntered(MouseEvent e) {
    this.ftb_BtnShowMouse.getBtnShow().setContentAreaFilled(true);
  }
  public void mouseExited(MouseEvent e) {
    this.ftb_BtnShowMouse.getBtnShow().setContentAreaFilled(false);
  }
}
