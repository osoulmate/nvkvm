package com.kvm;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
public class FloatPanel
  extends JPanel
  implements MouseMotionListener
{
  private static final long serialVersionUID = 1L;
  private boolean showtoolBar = false;
  public KVMInterface kvmInterface = null;
  private int locate;
  public void setShowtoolBar(boolean showtoolBar) {
    this.showtoolBar = showtoolBar;
  }
  public FloatPanel(KVMInterface kvmInterface2) {
    this.locate = 0;
    this.kvmInterface = kvmInterface2;
    addMouseMotionListener(this);
  } public boolean showTool(int position) {
    boolean showFlag = false;
    if (this.locate == 0) {
      showFlag = (position <= 2);
    }
    else {
      showFlag = (position > 2);
    } 
    return showFlag;
  }
  public void setFlag(boolean flag) {
    if (flag) {
      this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(true);
      repaint();
      validate();
    }
    else {
      this.kvmInterface.getFullScreen().getToolBarFrame().setVisible(false);
      repaint();
      validate();
    } 
  }
  public void mouseDragged(MouseEvent arg0) {}
  public void mouseMoved(MouseEvent e) {
    if (this.showtoolBar)
    {
      if (showTool(e.getY())) {
        setFlag(true);
      }
      else {
        setFlag(false);
      } 
    }
  }
}
