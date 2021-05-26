package com.kvm;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
class JFrameWindowListener
  implements WindowListener
{
  public void windowOpened(WindowEvent e) {}
  public void windowClosing(WindowEvent e) {}
  public void windowClosed(WindowEvent e) {
    //System.exit(0);
  }
  public void windowIconified(WindowEvent e) {}
  public void windowDeiconified(WindowEvent e) {}
  public void windowActivated(WindowEvent e) {}
  public void windowDeactivated(WindowEvent e) {}
}
