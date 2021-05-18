package com.kvm;
import java.util.TimerTask;
class CdLocalforInsertTimer
  extends TimerTask
{
  private transient VirtualMedia refer_CdLocalforInsertTimer;
  public CdLocalforInsertTimer(VirtualMedia refer_CdLocalforInsertTimer) {
    this.refer_CdLocalforInsertTimer = refer_CdLocalforInsertTimer;
  }
  public void run() {
    this.refer_CdLocalforInsertTimer.getCdCon().setEnabled(true);
    this.refer_CdLocalforInsertTimer.getCie().setEnabled(true);
    this.refer_CdLocalforInsertTimer.getCr().setEnabled(false);
    this.refer_CdLocalforInsertTimer.getCd().setEnabled(false);
    this.refer_CdLocalforInsertTimer.getCie().setText(this.refer_CdLocalforInsertTimer.getUtil()
        .getResource("flp_cd_pop_up_program"));
    this.refer_CdLocalforInsertTimer.isCdBtnForEj = true;
  }
}
