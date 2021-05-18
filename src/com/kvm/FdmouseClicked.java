package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
class FdmouseClicked
  extends MouseAdapter
{
  private VirtualMedia refer_FdmouseClicked;
  public FdmouseClicked(VirtualMedia refer_FdmouseClicked) {
    this.refer_FdmouseClicked = refer_FdmouseClicked;
  }
  public void mouseClicked(MouseEvent e) {
    if (this.refer_FdmouseClicked.getFdSelect().isEnabled()) {
      String floppy = this.refer_FdmouseClicked.getVmApplet().getFloppyDevices();
      this.refer_FdmouseClicked.getFdSelect().removeAllItems();
      if (0 == floppy.length()) {
        this.refer_FdmouseClicked.setFloppys(floppy);
        this.refer_FdmouseClicked.getFdSelect().addItem(this.refer_FdmouseClicked.getUtil().getResource("flp_cd_none"));
      }
      else {
        this.refer_FdmouseClicked.setFloppys(floppy);
        String[] fdSplip = this.refer_FdmouseClicked.getFloppys().split(":");
        for (int i = 0; i < fdSplip.length; i++) {
          if (!"".equals(fdSplip[i]))
          {
            this.refer_FdmouseClicked.getFdSelect().addItem(fdSplip[i] + ':');
          }
        } 
      } 
    } 
  }
  public void mouseEntered(MouseEvent e) {
    mouseClicked(e);
  }
}
