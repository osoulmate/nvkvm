package com.kvm;
import com.library.LoggerUtil;
import javax.swing.JOptionPane;
class Showthread
  extends Thread
{
  private int type;
  private BladeThread bt = null;
  public Showthread() {}
  public Showthread(BladeThread bladeThread) {
    this.bt = bladeThread;
  }
  public void showthread(int state) {
    this.type = state;
  }
  public void run() {
    if (this.bt.getKvmInterface().isFullScreen())
    {
      this.bt.getKvmInterface().getKvmUtil().returnToWin();
    }
    switch (this.type) {
      case 1:
        JOptionPane.showMessageDialog(this.bt.getKvmInterface().getFloatToolbar().getImagePanel(), this.bt
            .getKvmInterface().getKvmUtil().getString("power_pri"));
        return;
      case 2:
      case 3:
        JOptionPane.showMessageDialog(this.bt.getKvmInterface().getFloatToolbar().getImagePanel(), this.bt
            .getKvmInterface().getKvmUtil().getString("vmm_pri"));
        return;
    } 
    LoggerUtil.error("err_type");
  }
}
