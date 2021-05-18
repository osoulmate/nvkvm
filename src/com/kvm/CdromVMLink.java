package com.kvm;
import java.util.TimerTask;
class CdromVMLink
  extends TimerTask
{
  private transient VirtualMedia refer_CdromVMLink;
  public CdromVMLink(VirtualMedia refer_CdromVMLink) {
    this.refer_CdromVMLink = refer_CdromVMLink;
  }
  public void run() {
    this.refer_CdromVMLink.checkCdromVMConsole();
    this.refer_CdromVMLink.getCdVMlink().cancel();
    this.refer_CdromVMLink.setCdVMlink(null);
  }
}
