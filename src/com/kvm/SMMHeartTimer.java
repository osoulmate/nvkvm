package com.kvm;
import com.library.LibException;
class SMMHeartTimer
  extends Thread
{
  private KVMApplet kvmApplet = null;
  public SMMHeartTimer(KVMApplet refer) {
    this.kvmApplet = refer;
  }
  public void run() {
    this.kvmApplet.setConn(true);
    while (this.kvmApplet.isConn()) {
      try {
        Thread.sleep(2000L);
        if (this.kvmApplet.getClient() != null && this.kvmApplet.getPackData() != null)
        {
          this.kvmApplet.getClient().sentData(this.kvmApplet.getPackData().heartBeat());
        }
      }
      catch (LibException ex) {
        if ("IO_ERRCODE".equals(ex.getErrCode()))
        {
          break;
        }
      }
      catch (InterruptedException interruptedException) {}
    } 
  }
}
