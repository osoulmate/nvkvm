package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
class ScrollBarAdapter
  extends MouseAdapter
{
  private FullScreen refer_ScrollBarAdapter;
  public ScrollBarAdapter(FullScreen refer_ScrollBarAdapter) {
    this.refer_ScrollBarAdapter = refer_ScrollBarAdapter;
  }
  public void mouseClicked(MouseEvent e) {
    mousePressed(e);
  }
  public void mousePressed(MouseEvent e) {
    this.refer_ScrollBarAdapter.getToolBarFrame().setVisible(false);
    this.refer_ScrollBarAdapter.getImageMenu().setVisible(false);
    this.refer_ScrollBarAdapter.getCdMenu().setVisible(false);
    this.refer_ScrollBarAdapter.getFlpMenu().setVisible(false);
    if (KVMUtil.isLinuxOS())
    {
      this.refer_ScrollBarAdapter.getPowerPanelDialog().setVisible(false);
    }
  }
}
