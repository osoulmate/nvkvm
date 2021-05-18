package com.kvm;
import java.util.TimerTask;
class CdromEjectTimer
  extends TimerTask
{
  private transient VirtualMedia refer_CdromEjectTimer;
  public CdromEjectTimer(VirtualMedia refer_CdromEjectTimer) {
    this.refer_CdromEjectTimer = refer_CdromEjectTimer;
  }
  public void run() {
    if (this.refer_CdromEjectTimer.isCDImage) {
      this.refer_CdromEjectTimer.getCdText().setEnabled(true);
      this.refer_CdromEjectTimer.getCdSkim().setEnabled(true);
      this.refer_CdromEjectTimer.getCdLocalSkim().setEnabled(false);
    }
    else if (this.refer_CdromEjectTimer.isLocalDir) {
      this.refer_CdromEjectTimer.getCdLocalSkim().setEnabled(true);
      this.refer_CdromEjectTimer.getCdText().setEnabled(false);
      this.refer_CdromEjectTimer.getCdSkim().setEnabled(false);
      this.refer_CdromEjectTimer.getCd().setEnabled(false);
      this.refer_CdromEjectTimer.getCr().setEnabled(false);
    } 
    this.refer_CdromEjectTimer.getCdCon().setEnabled(true);
    this.refer_CdromEjectTimer.getCie().setEnabled(true);
    this.refer_CdromEjectTimer.getCie().setText(this.refer_CdromEjectTimer.getUtil().getResource("flp_cd_insert"));
    this.refer_CdromEjectTimer.isCdBtnForEj = false;
  }
}
