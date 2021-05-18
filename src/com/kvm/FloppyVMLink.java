package com.kvm;
import java.util.TimerTask;
class FloppyVMLink
  extends TimerTask
{
  private transient VirtualMedia refer_FloppyVMLink;
  public FloppyVMLink(VirtualMedia refer_FloppyVMLink) {
    this.refer_FloppyVMLink = refer_FloppyVMLink;
  }
  public void run() {
    this.refer_FloppyVMLink.checkFloppyVMConsole();
    this.refer_FloppyVMLink.getFlpVmlink().cancel();
    this.refer_FloppyVMLink.setFlpVmlink(null);
  }
}
