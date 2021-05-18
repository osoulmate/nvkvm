package com.kvm;
import java.util.TimerTask;
class CdromDisconnectVMLink
  extends TimerTask
{
  private transient VirtualMedia refer_CdromDisconnectVMLink;
  public CdromDisconnectVMLink(VirtualMedia refer_CdromDisconnectVMLink) {
    this.refer_CdromDisconnectVMLink = refer_CdromDisconnectVMLink;
  }
  public void run() {
    this.refer_CdromDisconnectVMLink.updateItemForDisCon(this.refer_CdromDisconnectVMLink.getCdrom());
    this.refer_CdromDisconnectVMLink.getCie().setEnabled(false);
    this.refer_CdromDisconnectVMLink.getCie().setText(this.refer_CdromDisconnectVMLink.getUtil()
        .getResource("flp_cd_pop_up_program"));
    this.refer_CdromDisconnectVMLink.isCdBtnForEj = true;
    this.refer_CdromDisconnectVMLink.getCdCon().setEnabled(true);
  }
}
