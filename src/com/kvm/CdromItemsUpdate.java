package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class CdromItemsUpdate
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private transient VirtualMedia refer_CdromItemsUpdate;
  public CdromItemsUpdate(VirtualMedia refer_CdromItemsUpdate) {
    this.refer_CdromItemsUpdate = refer_CdromItemsUpdate;
  }
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this.refer_CdromItemsUpdate.getCd()) {
      this.refer_CdromItemsUpdate.cdromItemsUpdate("cd");
    }
    else if (e.getSource() == this.refer_CdromItemsUpdate.getCr()) {
      this.refer_CdromItemsUpdate.cdromItemsUpdate("ci");
    }
    else if (e.getSource() == this.refer_CdromItemsUpdate.getCdLocal()) {
      this.refer_CdromItemsUpdate.cdromItemsUpdate("cdLocal");
    } 
  }
}
