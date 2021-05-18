package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class DoCdRomVMlink
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private transient VirtualMedia refer_DoCdRomVMlink;
  DoCdRomVMlink(VirtualMedia refer_DoCdRomVMlink) {
    this.refer_DoCdRomVMlink = refer_DoCdRomVMlink;
  }
  public void actionPerformed(ActionEvent e) {
    if (!this.refer_DoCdRomVMlink.getVmApplet().isVMLinkCrt(this.refer_DoCdRomVMlink.getCdrom())) {
      this.refer_DoCdRomVMlink.setStorageDevice(null);
      this.refer_DoCdRomVMlink.getCdRomVMlink().stop();
      this.refer_DoCdRomVMlink.setCdRomVMlink(null);
      this.refer_DoCdRomVMlink.disconnectVMLink(this.refer_DoCdRomVMlink.getCdrom());
    } 
    if (null == this.refer_DoCdRomVMlink.getStorageDevice())
    {
      this.refer_DoCdRomVMlink.setStorageDevice(this.refer_DoCdRomVMlink.getVmApplet().getConsole().getCdrom());
    }
    if (null != this.refer_DoCdRomVMlink.getStorageDevice() && this.refer_DoCdRomVMlink
      .getStorageDevice().isIsoDiskChanged()) {
      if (this.refer_DoCdRomVMlink.getCd().isSelected())
      {
        this.refer_DoCdRomVMlink.cdromISOEject();
      }
      if (this.refer_DoCdRomVMlink.getCdLocal().isSelected())
      {
        this.refer_DoCdRomVMlink.cdLocalISOEject();
      }
      this.refer_DoCdRomVMlink.getStorageDevice().setIsoDiskChanged(false);
    } 
  }
}
