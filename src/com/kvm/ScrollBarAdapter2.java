package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
class ScrollBarAdapter2
  extends MouseAdapter
{
  private FullScreen refer_ScrollBarAdapter2;
  public ScrollBarAdapter2(FullScreen refer_ScrollBarAdapter2) {
    this.refer_ScrollBarAdapter2 = refer_ScrollBarAdapter2;
  }
  public void mousePressed(MouseEvent e) {
    this.refer_ScrollBarAdapter2.getToolBarFrame().setVisible(false);
    this.refer_ScrollBarAdapter2.getImageMenu().setVisible(false);
    this.refer_ScrollBarAdapter2.getCdMenu().setVisible(false);
    this.refer_ScrollBarAdapter2.getFlpMenu().setVisible(false);
    if (KVMUtil.isLinuxOS())
    {
      this.refer_ScrollBarAdapter2.getPowerPanelDialog().setVisible(false);
    }
  }
  public void mouseClicked(MouseEvent e) {
    mousePressed(e);
  }
}
