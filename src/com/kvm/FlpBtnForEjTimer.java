package com.kvm;
import java.util.TimerTask;
class FlpBtnForEjTimer
  extends TimerTask
{
  private transient VirtualMedia refer_FlpBtnForEjTimer;
  public FlpBtnForEjTimer(VirtualMedia refer_FlpBtnForEjTimer) {
    this.refer_FlpBtnForEjTimer = refer_FlpBtnForEjTimer;
  }
  public void run() {
    this.refer_FlpBtnForEjTimer.getFitext().setEnabled(true);
    this.refer_FlpBtnForEjTimer.getFlpSkim().setEnabled(true);
    this.refer_FlpBtnForEjTimer.getFie().setEnabled(true);
    this.refer_FlpBtnForEjTimer.getFie().setText(this.refer_FlpBtnForEjTimer.getUtil().getResource("flp_cd_insert"));
    this.refer_FlpBtnForEjTimer.isFlpBtnForEj = false;
    this.refer_FlpBtnForEjTimer.getFc().setEnabled(true);
    this.refer_FlpBtnForEjTimer.getFcb().setEnabled(true);
  }
}
