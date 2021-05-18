package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
class HelpButtonMouseAdapter
  extends MouseAdapter
{
  private FloatToolbar ftb_HelpButtonMouse = null;
  HelpButtonMouseAdapter(FloatToolbar floatToolbar) {
    this.ftb_HelpButtonMouse = floatToolbar;
  }
  public void mouseEntered(MouseEvent e) {
    this.ftb_HelpButtonMouse.getHelpButton().setContentAreaFilled(true);
  }
  public void mouseExited(MouseEvent e) {
    this.ftb_HelpButtonMouse.getHelpButton().setContentAreaFilled(false);
  }
}
