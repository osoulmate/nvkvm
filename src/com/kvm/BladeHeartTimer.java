package com.kvm;
import com.library.LoggerUtil;
public class BladeHeartTimer
  extends Thread
{
  private BladeThread bladeThread = null;
  private int bladeNO = 0;
  public BladeHeartTimer(BladeThread bladeThread) {
    this.bladeThread = bladeThread;
    this.bladeNO = bladeThread.getBladeNOByBladeThread();
  }
  public final void run() {
    while (true) {
      if (!this.bladeThread.getBladeCommu().getSocket().isClosed())
      {
        this.bladeThread.getBladeCommu().sentData(this.bladeThread.getKvmInterface().getPackData().heartBeat(this.bladeNO));
      }
      try {
        Thread.sleep(5000L);
      }
      catch (InterruptedException e) {
        LoggerUtil.error(e.getClass().getName());
      } 
    } 
  }
}
