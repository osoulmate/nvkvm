package com.huawei.vm.console.management;
import java.util.TimerTask;
class ConsoleStateTimerTask
  extends TimerTask
{
  private VMConsole vc_ConsoleStateTimerTask = null;
  public ConsoleStateTimerTask(VMConsole vmConsole) {
    this.vc_ConsoleStateTimerTask = vmConsole;
  }
  public void run() {
    if (this.vc_ConsoleStateTimerTask.getCONSOLE_DEVICE() == this.vc_ConsoleStateTimerTask.getConsoleState())
    {
      this.vc_ConsoleStateTimerTask.errorProcess(0, 122);
    }
    cancel();
  }
}
