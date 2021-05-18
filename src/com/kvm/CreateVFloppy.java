package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
class CreateVFloppy
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private transient VirtualMedia refer_CreateVFloppy;
  public CreateVFloppy(VirtualMedia refer_CreateVFloppy) {
    this.refer_CreateVFloppy = refer_CreateVFloppy;
  }
  public void actionPerformed(ActionEvent e) {
    if (this.refer_CreateVFloppy.isFlpBtnForCon) {
      this.refer_CreateVFloppy.argumentsInit(this.refer_CreateVFloppy.getFloppy());
      if (!this.refer_CreateVFloppy.validateFloppy()) {
        return;
      }
      this.refer_CreateVFloppy.getFdSelect().setEnabled(false);
      this.refer_CreateVFloppy.getFc().setEnabled(false);
      this.refer_CreateVFloppy.getFcb().setEnabled(false);
      this.refer_CreateVFloppy.getFd().setEnabled(false);
      this.refer_CreateVFloppy.getFr().setEnabled(false);
      if (this.refer_CreateVFloppy.getFr().isSelected())
      {
        this.refer_CreateVFloppy.getFlpSkim().setEnabled(false);
      }
      this.refer_CreateVFloppy.getFitext().setEnabled(false);
      this.refer_CreateVFloppy.submitVForm(this.refer_CreateVFloppy.getFloppy());
    }
    else {
      int result = JOptionPane.showConfirmDialog(this.refer_CreateVFloppy.getFlp(), this.refer_CreateVFloppy
          .getUtil().getResource("com.huawei.vm.console.out.413"), 
          UIManager.getString("OptionPane.titleText"), 0);
      if (0 == result) {
        this.refer_CreateVFloppy.getFloppyVmlink().stop();
        this.refer_CreateVFloppy.setFloppyVmlink(null);
        this.refer_CreateVFloppy.disconnectVMLink(this.refer_CreateVFloppy.getFloppy());
      } 
    } 
  }
}
