package com.kvm;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
class ColorBitWindowAdapter
  extends WindowAdapter
{
  private ColorBit refer_ColorBitWindowAdapter;
  public ColorBitWindowAdapter(ColorBit refer) {
    this.refer_ColorBitWindowAdapter = refer;
  }
  public void windowClosing(WindowEvent e) {
    if (this.refer_ColorBitWindowAdapter.getKvmInterface().getKvmUtil().getImagePane(this.refer_ColorBitWindowAdapter.getBladeNumber()) != null) {
      this.refer_ColorBitWindowAdapter.remove();
    }
    else {
      this.refer_ColorBitWindowAdapter.removeAll();
    } 
    this.refer_ColorBitWindowAdapter.dispose();
  }
}
