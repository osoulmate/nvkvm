package com.huawei.vm.console.management;
import java.util.TimerTask;
class ConsoleCertifyTimerTask
  extends TimerTask
{
  private VMConsole vc_ConsoleCertifyTimerTask = null;
  public ConsoleCertifyTimerTask(VMConsole vmConsole) {
    this.vc_ConsoleCertifyTimerTask = vmConsole;
  }
  public void run() {
    if (2 == this.vc_ConsoleCertifyTimerTask.getConsoleState())
    {
      this.vc_ConsoleCertifyTimerTask.errorProcess(this.vc_ConsoleCertifyTimerTask.getConsoleType(), 121);
    }
    cancel();
  }
}
