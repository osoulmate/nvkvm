package com.kvm;
import com.library.LoggerUtil;
import java.util.TimerTask;
import javax.swing.JButton;
class CdromLink
  extends TimerTask
{
  private transient JButton cdromBtn;
  public CdromLink(JButton button) {
    this.cdromBtn = button;
  }
  public void run() {
    int try_times = 0;
    while (true) {
      if (this.cdromBtn.isEnabled()) {
        this.cdromBtn.doClick();
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
