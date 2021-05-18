package com.kvm;
import com.library.LoggerUtil;
import java.io.BufferedOutputStream;
import java.io.IOException;
class BladeReconnect
  extends Thread
{
  private transient KVMInterface kvmInterface = null;
  private BufferedOutputStream dout = null;
  private int bladeNO = 0;
  public BladeReconnect(int bladeNO, BufferedOutputStream dout, KVMInterface kvmInterface) {
    this.bladeNO = bladeNO;
    this.dout = dout;
    this.kvmInterface = kvmInterface;
  }
  public final void run() {
    try {
      this.kvmInterface.setReconnectState(true);
      if (this.kvmInterface.isNeedConsultation()) {
        this.dout.write(this.kvmInterface.getPackData().getSuiteList(this.bladeNO));
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException e) {
          Debug.printExc(e.getClass().getName());
        } 
      } 
      this.dout.write(this.kvmInterface.getPackData().connectBlade(this.bladeNO, this.kvmInterface
            .getKvmUtil().getImagePane(this.bladeNO).getColorBit()));
    }
    catch (IOException ioe) {
      LoggerUtil.error(ioe.getClass().getName());
    } 
  }
}
