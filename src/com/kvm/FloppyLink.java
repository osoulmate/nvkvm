package com.kvm;
import com.library.LoggerUtil;
import java.util.TimerTask;
import javax.swing.JButton;
class FloppyLink
  extends TimerTask
{
  private transient JButton floppyBtn;
  public FloppyLink(JButton button) {
    this.floppyBtn = button;
  }
  public void run() {
    int try_times = 0;
    while (true) {
      if (this.floppyBtn.isEnabled()) {
        this.floppyBtn.doClick();
        break;
      } 
      if (try_times++ > 20) {
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
