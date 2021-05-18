package com.kvm;
import com.huawei.vm.console.utils.TestPrint;
import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import javax.swing.JPanel;
class ToolBarPanel
  extends JPanel
  implements ContainerListener
{
  private static final long serialVersionUID = 1L;
  public boolean contains(int x, int y) {
    Component c = getParent();
    if (c != null) {
      Rectangle r = c.getBounds();
      return (x >= 0 && x < r.width && y >= 0 && y < r.height);
    } 
    return super.contains(x, y);
  }
  public void componentAdded(ContainerEvent e) {
    if (null == e.getContainer()) {
      TestPrint.println(3, "e.getContainer() is null.");
      return;
    } 
    Container c = e.getContainer().getParent();
    if (c != null) {
      c.getParent().validate();
      c.getParent().repaint();
    } 
  }
  public void componentRemoved(ContainerEvent e) {
    if (null == e.getContainer()) {
      TestPrint.println(3, "e.getContainer() is null.");
      return;
    } 
    Container c = e.getContainer().getParent();
    if (c != null) {
      c.getParent().validate();
      c.getParent().repaint();
    } 
  }
}
