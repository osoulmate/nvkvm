package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class CreateHelpAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FloatToolbar ftb_CreateHelp = null;
  CreateHelpAction(FloatToolbar floatToolbar) {
    this.ftb_CreateHelp = floatToolbar;
  }
  public void actionPerformed(ActionEvent e) {
    if (null == this.ftb_CreateHelp.getHelpFrm()) {
      this.ftb_CreateHelp.getBMCHelpDocument();
    }
    else {
      this.ftb_CreateHelp.getHelpFrm().setVisible(true);
      this.ftb_CreateHelp.getHelpFrm().setAlwaysOnTop(true);
      this.ftb_CreateHelp.getHelpFrm().setAlwaysOnTop(false);
    } 
  }
}
