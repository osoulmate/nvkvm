package com.kvmV1;
public class BladeHeartTimer
  extends Thread
{
  private BladeThread bladeThread = null;
  private int bladeNO = 0;
  public BladeHeartTimer(BladeThread bladeThread) {
    this.bladeThread = bladeThread;
    this.bladeNO = bladeThread.getBladeNO();
  }
  public void run() {
    while (true) {
      if (!this.bladeThread.bladeCommu.socket.isClosed())
      {
        this.bladeThread.bladeCommu.sentData(this.bladeThread.kvmInterface.packData.heartBeat(this.bladeNO));
      }
      try {
        Thread.sleep(5000L);
      }
      catch (InterruptedException e) {}
    } 
  }
}
