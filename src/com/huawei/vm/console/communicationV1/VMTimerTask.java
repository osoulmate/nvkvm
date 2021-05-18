package com.huawei.vm.console.communicationV1;
import com.huawei.vm.console.managementV1.VMConsole;
import com.huawei.vm.console.storageV1.impl.FloppyDevice;
import com.huawei.vm.console.utilsV1.TestPrint;
import com.huawei.vm.console.utilsV1.VMException;
import java.util.TimerTask;
public class VMTimerTask
  extends TimerTask
{
  public static final int STATE_IDLE = 0;
  public static final int STATE_BUSY = 1;
  public static final int TASK_INTERVAL = 1000;
  public final int HEARTBIT_INTERVAL = ProtocolCode.HEARTBIT_INTERVAL / 1000;
  public final int HEARTBIT_OVERTIMER = 4 * this.HEARTBIT_INTERVAL;
  private int heartBitCount = this.HEARTBIT_OVERTIMER;
  private final VMConsole console;
  private final CommunicationSender sender;
  private int timerSteps = 0;
  private boolean isFloppyReopenTask = false;
  private FloppyDevice floppy = null;
  public VMTimerTask(CommunicationSender sender, VMConsole console) {
    this.sender = sender;
    this.console = console;
  }
  public void run() {
    this.timerSteps = ++this.timerSteps % 10;
    heartBitTask();
    if (this.isFloppyReopenTask)
    {
      floppyReopen();
    }
  }
  private void heartBitTask() {
    this.heartBitCount--;
    if (0 == this.heartBitCount) {
      this.console.errorProcess(0, 123);
      cancel();
    } 
    if (0 == this.timerSteps)
    {
      this.sender.sendHeartbit();
    }
  }
  public final void heartBitInit() {
    this.heartBitCount = this.HEARTBIT_OVERTIMER;
  }
  public void startFloppyReopen(FloppyDevice floppy) {
    if (null != floppy) {
      this.isFloppyReopenTask = true;
      this.floppy = floppy;
    } 
  }
  private void floppyReopen() {
    try {
      if (null != this.floppy)
      {
        this.floppy.getMediumSize();
      }
    }
    catch (VMException e) {
      TestPrint.println(1, "VMTimer Task: floppyReopen--no floppy inserted.");
    } 
  }
  public void stopFloppyReopen() {
    this.isFloppyReopenTask = false;
    this.floppy = null;
  }
}
