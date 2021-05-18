package com.kvm;
import java.util.TimerTask;
class CdRomforInsertTimer
  extends TimerTask
{
  private transient VirtualMedia refer_CdRomforInsertTimer;
  public CdRomforInsertTimer(VirtualMedia refer_CdRomforInsertTimer) {
    this.refer_CdRomforInsertTimer = refer_CdRomforInsertTimer;
  }
  public void run() {
    this.refer_CdRomforInsertTimer.getCdCon().setEnabled(true);
    this.refer_CdRomforInsertTimer.getCie().setEnabled(true);
    this.refer_CdRomforInsertTimer.getCie().setText(this.refer_CdRomforInsertTimer.getUtil()
        .getResource("flp_cd_pop_up_program"));
    this.refer_CdRomforInsertTimer.isCdBtnForEj = true;
  }
}
