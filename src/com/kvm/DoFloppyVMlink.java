package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class DoFloppyVMlink
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private transient VirtualMedia refer_DoFloppyVMlink;
  public DoFloppyVMlink(VirtualMedia refer_DoFloppyVMlink) {
    this.refer_DoFloppyVMlink = refer_DoFloppyVMlink;
  }
  public void actionPerformed(ActionEvent e) {
    if (!this.refer_DoFloppyVMlink.getVmApplet().isVMLinkCrt(this.refer_DoFloppyVMlink.getFloppy())) {
      this.refer_DoFloppyVMlink.getFloppyVmlink().stop();
      this.refer_DoFloppyVMlink.setFloppyVmlink(null);
      this.refer_DoFloppyVMlink.disconnectVMLink(this.refer_DoFloppyVMlink.getFloppy());
    } 
  }
}
