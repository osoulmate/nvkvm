package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
class PowerMenuButtonMouseAdapter
  extends MouseAdapter
{
  private FloatToolbar ftb_PowerMenuButtonMouse = null;
  PowerMenuButtonMouseAdapter(FloatToolbar floatToolbar) {
    this.ftb_PowerMenuButtonMouse = floatToolbar;
  }
  public void mouseEntered(MouseEvent e) {
    this.ftb_PowerMenuButtonMouse.getPowerMenuButton().setContentAreaFilled(true);
  }
  public void mouseExited(MouseEvent e) {
    this.ftb_PowerMenuButtonMouse.getPowerMenuButton().setContentAreaFilled(false);
  }
}
