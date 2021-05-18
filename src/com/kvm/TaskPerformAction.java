package com.kvm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class TaskPerformAction
  implements ActionListener
{
  private KVMUtil kvmUtil = null;
  public TaskPerformAction(KVMUtil refer) {
    this.kvmUtil = refer;
  }
  public void actionPerformed(ActionEvent evt) {
    if (this.kvmUtil.getKvmInterface().getClientSocket().getBladePresentInfo().size() != 0) {
      this.kvmUtil.setBladePreInfo(this.kvmUtil.getKvmInterface()
          .getClientSocket()
          .getBladePresentInfo()
          .remove(this.kvmUtil.getKvmInterface().getClientSocket().getBladePreIndex()));
      this.kvmUtil.setConn(true);
    }
    else {
      this.kvmUtil.setTimes(this.kvmUtil.getTimes() + 1);
    } 
  }
}
