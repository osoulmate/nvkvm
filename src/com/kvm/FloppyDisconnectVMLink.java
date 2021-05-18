package com.kvm;
import java.util.TimerTask;
class FloppyDisconnectVMLink
  extends TimerTask
{
  private transient VirtualMedia refer_FloppyDisconnectVMLink;
  public FloppyDisconnectVMLink(VirtualMedia refer_FloppyDisconnectVMLink) {
    this.refer_FloppyDisconnectVMLink = refer_FloppyDisconnectVMLink;
  }
  public void run() {
    this.refer_FloppyDisconnectVMLink.updateItemForDisCon(this.refer_FloppyDisconnectVMLink.getFloppy());
    this.refer_FloppyDisconnectVMLink.getFie().setEnabled(false);
    this.refer_FloppyDisconnectVMLink.getFcb().setEnabled(true);
    this.refer_FloppyDisconnectVMLink.isFlpBtnForEj = true;
  }
}
