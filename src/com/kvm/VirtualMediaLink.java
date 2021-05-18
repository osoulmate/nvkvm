package com.kvm;
import com.huawei.vm.console.management.VMConsole;
import com.library.LoggerUtil;
import java.util.TimerTask;
import javax.swing.JButton;
class VirtualMediaLink
  extends TimerTask
{
  private transient JButton cdromBtn;
  private transient JButton floppyBtn;
  private transient VMConsole console;
  public VirtualMediaLink(JButton cd, JButton fp, VMConsole con) {
    this.cdromBtn = cd;
    this.floppyBtn = fp;
    this.console = con;
  }
  public void run() {
    boolean cdclick = false;
    boolean fpclick = false;
    int try_times = 0;
    while (true) {
      if (this.cdromBtn.isEnabled() && !cdclick) {
        this.cdromBtn.doClick();
        cdclick = true;
      } 
      if (this.floppyBtn.isEnabled() && !fpclick && cdclick && this.console
        .getCdromStateBMC() == 4) {
        this.floppyBtn.doClick();
        fpclick = true;
        break;
      } 
      if (try_times++ > 30) {
        break;
      }
      try {
        Thread.sleep(500L);
      }
      catch (InterruptedException e) {
        LoggerUtil.error(e.getClass().getName());
      } 
    } 
  }
}
