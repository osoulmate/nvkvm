package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
class BtnCreateImageMouseAdapter
  extends MouseAdapter
{
  private FloatToolbar ftb_BtnCreateImageMouse = null;
  BtnCreateImageMouseAdapter(FloatToolbar floatToolbar) {
    this.ftb_BtnCreateImageMouse = floatToolbar;
  }
  public void mouseEntered(MouseEvent e) {
    this.ftb_BtnCreateImageMouse.getBtnCreateImage().setContentAreaFilled(true);
  }
  public void mouseExited(MouseEvent e) {
    this.ftb_BtnCreateImageMouse.getBtnCreateImage().setContentAreaFilled(false);
  }
}
