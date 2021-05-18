package com.kvm;
import com.library.LoggerUtil;
import javax.swing.JDialog;
class DisposeTask
  extends Thread
{
  JDialog jd = null;
  public DisposeTask(JDialog jdt) {
    this.jd = jdt;
  }
  public void run() {
    try {
      sleep(5000L);
    }
    catch (InterruptedException e) {
      Debug.println("DisposeTask sleep error.");
      LoggerUtil.error(e.getClass().getName());
    } 
    this.jd.dispose();
    this.jd = null;
  }
}
