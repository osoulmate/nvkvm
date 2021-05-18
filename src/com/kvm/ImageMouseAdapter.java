package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
class ImageMouseAdapter
  extends MouseAdapter
{
  private ImageFile imageFile_ImageMouse = null;
  public ImageMouseAdapter(ImageFile refer) {
    this.imageFile_ImageMouse = refer;
  }
  public void mouseClicked(MouseEvent e) {
    if (this.imageFile_ImageMouse.getImageCheck().isEnabled()) {
      String cdrom = this.imageFile_ImageMouse.getVmApplet().getCDROMDevices();
      String floppy = this.imageFile_ImageMouse.getVmApplet().getFloppyDevices();
      String cdromFloppy = cdrom + floppy;
      if (!this.imageFile_ImageMouse.getCdflps().equals(cdromFloppy)) {
        this.imageFile_ImageMouse.setCdflps(cdromFloppy);
        if ("".equals(cdrom) && "".equals(floppy)) {
          this.imageFile_ImageMouse.getImageCheck().removeAllItems();
          this.imageFile_ImageMouse.setFloppys(null);
          this.imageFile_ImageMouse.setCdroms(null);
          this.imageFile_ImageMouse.getImageCheck().addItem(this.imageFile_ImageMouse.getUtil()
              .getResource("flp_cd_none"));
        }
        else {
          this.imageFile_ImageMouse.getImageCheck().removeAllItems();
          String[] flpSplit = floppy.split(":");
          for (int i = 0; i < flpSplit.length; i++) {
            if (!"".equals(flpSplit[i]))
            {
              this.imageFile_ImageMouse.getImageCheck().addItem(flpSplit[i] + ':');
            }
          } 
          String[] cdSplit = cdrom.split(":");
          for (int j = 0; j < cdSplit.length; j++) {
            if (!"".equals(cdSplit[j]))
            {
              this.imageFile_ImageMouse.getImageCheck().addItem(cdSplit[j] + ':');
            }
          } 
        } 
      } 
    } 
  }
  public void mouseEntered(MouseEvent e) {
    mouseClicked(e);
  }
}
