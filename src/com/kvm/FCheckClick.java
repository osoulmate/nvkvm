package com.kvm;
import com.library.LoggerUtil;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class FCheckClick
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private transient VirtualMedia refer_FCheckClick;
  public FCheckClick(VirtualMedia refer_FCheckClick) {
    this.refer_FCheckClick = refer_FCheckClick;
  }
  public void actionPerformed(ActionEvent arg0) {
    try {
      if (this.refer_FCheckClick.getUtil()
        .getResource("flp_disconnection")
        .equals(this.refer_FCheckClick.getFcb().getText()))
      {
        if (this.refer_FCheckClick.getFc().isSelected())
        {
          this.refer_FCheckClick.getVmApplet().setWriteProtect(true);
        }
        else
        {
          this.refer_FCheckClick.getVmApplet().setWriteProtect(false);
        }
      }
    } catch (Exception e) {
      LoggerUtil.error(e.getClass().getName());
    } 
  }
}
