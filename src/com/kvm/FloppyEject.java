package com.kvm;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
class FloppyEject
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private transient VirtualMedia refer_FloppyEject;
  public FloppyEject(VirtualMedia refer_FloppyEject) {
    this.refer_FloppyEject = refer_FloppyEject;
  }
  public void actionPerformed(ActionEvent arg0) {
    if (this.refer_FloppyEject.isFlpBtnForCon && this.refer_FloppyEject.isCdBtnForCon) {
      return;
    }
    int result = 0;
    if (this.refer_FloppyEject.isFlpBtnForEj) {
      this.refer_FloppyEject.getFcb().setEnabled(false);
      this.refer_FloppyEject.getFie().setEnabled(false);
      this.refer_FloppyEject.getFitext().setEnabled(false);
      this.refer_FloppyEject.getFlpSkim().setEnabled(false);
      this.refer_FloppyEject.getFc().setEnabled(false);
      result = this.refer_FloppyEject.getVmApplet().changeFloppyImage("");
      if (0 != result) {
        JOptionPane.showMessageDialog(this.refer_FloppyEject.getFlp(), this.refer_FloppyEject.getVmApplet()
            .getStatement(result));
        return;
      } 
      Timer t = new Timer("floppy Eject");
      TimerTask task = new FlpBtnForEjTimer(this.refer_FloppyEject);
      t.schedule(task, 2000L);
    }
    else {
      String fImage = this.refer_FloppyEject.getFitext().getText();
      if ("".equals(fImage) || null == fImage) {
        JOptionPane.showMessageDialog(this.refer_FloppyEject.getFlp(), this.refer_FloppyEject.getUtil()
            .getResource("com.huawei.vm.console.out.404"));
        return;
      } 
      if (4 >= fImage.length() || 
        !".img".equals(fImage.toLowerCase(Locale.getDefault()).substring(fImage.length() - 4))) {
        JOptionPane.showMessageDialog(this.refer_FloppyEject.getFlp(), this.refer_FloppyEject.getUtil()
            .getResource("com.huawei.vm.console.out.405"));
        return;
      } 
      result = this.refer_FloppyEject.getVmApplet().changeFloppyImage(fImage);
      if (0 != result) {
        JOptionPane.showMessageDialog(this.refer_FloppyEject.getFlp(), this.refer_FloppyEject.getVmApplet()
            .getStatement(result));
        return;
      } 
      this.refer_FloppyEject.getFcb().setEnabled(false);
      this.refer_FloppyEject.setFloppyPath(fImage);
      this.refer_FloppyEject.getFie().setEnabled(false);
      this.refer_FloppyEject.getFitext().setEnabled(false);
      this.refer_FloppyEject.getFlpSkim().setEnabled(false);
      this.refer_FloppyEject.getFc().setEnabled(false);
      this.refer_FloppyEject.isFlpBtnForEj = true;
      Timer t = new Timer("floppy insert");
      TimerTask task = new FloppyEjectTimer(this.refer_FloppyEject);
      t.schedule(task, 2000L);
    } 
  }
}
