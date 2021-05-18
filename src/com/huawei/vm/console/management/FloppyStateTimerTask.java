package com.huawei.vm.console.management;
import java.util.TimerTask;
class FloppyStateTimerTask
  extends TimerTask
{
  private VMConsole vc_FloppyStateTimerTask = null;
  public FloppyStateTimerTask(VMConsole vmConsole) {
    this.vc_FloppyStateTimerTask = vmConsole;
  }
  public void run() {
    if (this.vc_FloppyStateTimerTask.getCONSOLE_DEVICE() == this.vc_FloppyStateTimerTask.getFloppyState())
    {
      this.vc_FloppyStateTimerTask.errorProcess(this.vc_FloppyStateTimerTask.getConsoleType(), 122);
    }
    cancel();
  }
}
