package com.kvm;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
class KVMWindowAdapter
  extends WindowAdapter
{
  private KvmAppletToolBar toolBar = null;
  public KVMWindowAdapter(KvmAppletToolBar refer) {
    this.toolBar = refer;
  }
  public void windowClosing(WindowEvent arg0) {
    super.windowClosing(arg0);
    this.toolBar.setHelpFrm(null);
  }
}
