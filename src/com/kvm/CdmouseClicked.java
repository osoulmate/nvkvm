package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
class CdmouseClicked
  extends MouseAdapter
{
  private VirtualMedia refer_CdmouseClicked;
  public CdmouseClicked(VirtualMedia refer_CdmouseClicked) {
    this.refer_CdmouseClicked = refer_CdmouseClicked;
  }
  public void mouseClicked(MouseEvent e) {
    if (this.refer_CdmouseClicked.getCdSelect().isEnabled()) {
      String cdrom = this.refer_CdmouseClicked.getVmApplet().getCDROMDevices();
      this.refer_CdmouseClicked.getCdSelect().removeAllItems();
      if (0 == cdrom.length()) {
        this.refer_CdmouseClicked.setCdroms(cdrom);
        this.refer_CdmouseClicked.getCdSelect().addItem(this.refer_CdmouseClicked.getUtil().getResource("flp_cd_none"));
      }
      else {
        this.refer_CdmouseClicked.setCdroms(cdrom);
        String[] cdSplip = cdrom.split(":");
        for (int i = 0; i < cdSplip.length; i++) {
          if (!"".equals(cdSplip[i]))
          {
            this.refer_CdmouseClicked.getCdSelect().addItem(cdSplip[i] + ':');
          }
        } 
      } 
    } 
  }
  public void mouseEntered(MouseEvent e) {
    mouseClicked(e);
  }
}
