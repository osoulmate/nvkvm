package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class FloppyItemsUpdate
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private transient VirtualMedia refer_FloppyItemsUpdate;
  public FloppyItemsUpdate(VirtualMedia refer_FloppyItemsUpdate) {
    this.refer_FloppyItemsUpdate = refer_FloppyItemsUpdate;
  }
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this.refer_FloppyItemsUpdate.getFd()) {
      this.refer_FloppyItemsUpdate.floppyItemsUpdate("fd");
    }
    else {
      this.refer_FloppyItemsUpdate.floppyItemsUpdate("fi");
    } 
  }
}
