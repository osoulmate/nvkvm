package com.kvm;
import java.util.TimerTask;
import javax.swing.Timer;
class CheckCdromVMConsole
  extends TimerTask
{
  private transient VirtualMedia refer_CheckCdromVMConsole;
  public CheckCdromVMConsole(VirtualMedia refer_CheckCdromVMConsole) {
    this.refer_CheckCdromVMConsole = refer_CheckCdromVMConsole;
  }
  public void run() {
    this.refer_CheckCdromVMConsole.disableConntedBtn(this.refer_CheckCdromVMConsole.getCdrom());
    if (null == this.refer_CheckCdromVMConsole.getCdRomVMlink())
    {
      this.refer_CheckCdromVMConsole.setCdRomVMlink(new Timer(1000, this.refer_CheckCdromVMConsole.doCdRomVMlink()));
    }
    ((javax.swing.Timer) this.refer_CheckCdromVMConsole.getCdRomVMlink()).start();
  }
}
