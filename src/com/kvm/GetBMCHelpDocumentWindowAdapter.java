package com.kvm;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
class GetBMCHelpDocumentWindowAdapter
  extends WindowAdapter
{
  private FloatToolbar ftb_GetBMCHelpDocument = null;
  GetBMCHelpDocumentWindowAdapter(FloatToolbar floatToolbar) {
    this.ftb_GetBMCHelpDocument = floatToolbar;
  }
  public void windowClosing(WindowEvent arg0) {
    super.windowClosing(arg0);
    this.ftb_GetBMCHelpDocument.setHelpFrm(null);
  }
}
