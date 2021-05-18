package com.kvm;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
class CdromEject
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private transient VirtualMedia refer_CdromEject;
  public CdromEject(VirtualMedia refer_CdromEject) {
    this.refer_CdromEject = refer_CdromEject;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.refer_CdromEject.isFlpBtnForCon && this.refer_CdromEject.isCdBtnForCon) {
      return;
    }
    int result = 0;
    if (this.refer_CdromEject.isCdBtnForEj) {
      this.refer_CdromEject.getCdCon().setEnabled(false);
      this.refer_CdromEject.getCdText().setEnabled(false);
      this.refer_CdromEject.getCdSkim().setEnabled(false);
      this.refer_CdromEject.getCie().setEnabled(false);
      if (this.refer_CdromEject.isCDImage) {
        result = this.refer_CdromEject.getVmApplet().changeCD("cdrom", "", null, null);
      }
      else if (this.refer_CdromEject.isLocalDir) {
        result = this.refer_CdromEject.getVmApplet().changeCD("cdlocal", "", this.refer_CdromEject
            .getLocalDirName(), this.refer_CdromEject
            .getMemoryStruct());
      } 
      if (0 != result) {
        JOptionPane.showMessageDialog(this.refer_CdromEject.getCdp(), this.refer_CdromEject
            .getVmApplet().getStatement(result));
        return;
      } 
      Timer t = new Timer("cdrom Eject");
      TimerTask task = new CdromEjectTimer(this.refer_CdromEject);
      t.schedule(task, 3000L);
    }
    else if (this.refer_CdromEject.isCDImage) {
      this.refer_CdromEject.cdRomforInsert();
    }
    else if (this.refer_CdromEject.isLocalDir) {
      if (!this.refer_CdromEject.cdLocalforInsert())
        return; 
    } 
  }
}
