package com.huawei.vm.console.management;
import java.util.TimerTask;
class CdromStateTimerTask
  extends TimerTask
{
  private VMConsole vc_CdromStateTimerTask = null;
  public CdromStateTimerTask(VMConsole vmConsole) {
    this.vc_CdromStateTimerTask = vmConsole;
  }
  public void run() {
    if (this.vc_CdromStateTimerTask.getCONSOLE_DEVICE() == this.vc_CdromStateTimerTask.getCdromStateBMC())
    {
      this.vc_CdromStateTimerTask.errorProcess(this.vc_CdromStateTimerTask.getConsoleType(), 122);
    }
    cancel();
  }
}
