package com.kvm;
import java.util.TimerTask;
class FloppyEjectTimer
  extends TimerTask
{
  private transient VirtualMedia refer_FloppyEjectTimer;
  public FloppyEjectTimer(VirtualMedia refer_FloppyEjectTimer) {
    this.refer_FloppyEjectTimer = refer_FloppyEjectTimer;
  }
  public void run() {
    this.refer_FloppyEjectTimer.getFcb().setEnabled(true);
    this.refer_FloppyEjectTimer.getFie().setEnabled(true);
    this.refer_FloppyEjectTimer.getFie().setText(this.refer_FloppyEjectTimer.getUtil().getResource("flp_cd_pop_up_program"));
    this.refer_FloppyEjectTimer.isFlpBtnForEj = true;
  }
}
