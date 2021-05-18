package com.kvm;

import java.util.TimerTask;
import javax.swing.Timer;

class CheckFloppyVMConsole extends TimerTask {
  private transient VirtualMedia refer_CheckFloppyVMConsole;
  
  public CheckFloppyVMConsole(VirtualMedia refer_CheckFloppyVMConsole) {
    this.refer_CheckFloppyVMConsole = refer_CheckFloppyVMConsole;
  }
  
  public void run() {
    this.refer_CheckFloppyVMConsole.disableConntedBtn(this.refer_CheckFloppyVMConsole.getFloppy());
    if (null == this.refer_CheckFloppyVMConsole.getFloppyVmlink())
      this.refer_CheckFloppyVMConsole.setFloppyVmlink(new Timer(1000, this.refer_CheckFloppyVMConsole.doFloppyVMlink())); 
      this.refer_CheckFloppyVMConsole.getFloppyVmlink().start();
  }
}
