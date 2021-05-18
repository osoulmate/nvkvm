package com.kvm;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
class KVMAppletCloseAction
  extends WindowAdapter
{
  private KvmAppletToolBar toolBar = null;
  public KVMAppletCloseAction(KvmAppletToolBar refer) {
    this.toolBar = refer;
  }
  public void windowClosing(WindowEvent e) {
    if (!this.toolBar.getKvmInterface().getImageFile().isImageCreate()) {
      int result = JOptionPane.showConfirmDialog(null, this.toolBar
          .getKvmInterface().getKvmUtil().getString("createFrame_message"), 
          UIManager.getString("OptionPane.titleText"), 0);
      if (0 == result)
      {
        this.toolBar.getKvmInterface().getImageFile().doStopImageCreate();
        this.toolBar.getFrmFr().dispose();
        this.toolBar.setFrmFr(null);
      }
    } else {
      this.toolBar.getFrmFr().dispose();
      this.toolBar.setFrmFr(null);
    } 
  }
}
