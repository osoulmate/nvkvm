package com.kvm;
import com.library.LoggerUtil;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
class CreateVCdrom
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private transient VirtualMedia refer_CreateVCdrom;
  public CreateVCdrom(VirtualMedia refer_CreateVCdrom) {
    this.refer_CreateVCdrom = refer_CreateVCdrom;
  }
  public void actionPerformed(ActionEvent event) {
    try {
      if (this.refer_CreateVCdrom.isCdBtnForCon) {
        this.refer_CreateVCdrom.argumentsInit(this.refer_CreateVCdrom.getCdrom());
        if (!this.refer_CreateVCdrom.validateCdrom()) {
          return;
        }
        this.refer_CreateVCdrom.getCdCon().setEnabled(false);
        this.refer_CreateVCdrom.getCr().setEnabled(false);
        this.refer_CreateVCdrom.getCd().setEnabled(false);
        this.refer_CreateVCdrom.getCdSelect().setEnabled(false);
        this.refer_CreateVCdrom.getCdText().setEnabled(false);
        this.refer_CreateVCdrom.getCdLocal().setEnabled(false);
        this.refer_CreateVCdrom.getCdLocalSkim().setEnabled(false);
        this.refer_CreateVCdrom.getCdLocalText().setEnabled(false);
        if (this.refer_CreateVCdrom.getCd().isSelected())
        {
          this.refer_CreateVCdrom.getCdSkim().setEnabled(false);
        }
        if (this.refer_CreateVCdrom.getCdLocal().isSelected()) {
          this.refer_CreateVCdrom.getCdLocalText().setEnabled(false);
          this.refer_CreateVCdrom.getCdLocalSkim().setEnabled(false);
          if (this.refer_CreateVCdrom.isLocalDirChange()) {
            JOptionPane.showMessageDialog(this.refer_CreateVCdrom.getCdp(), this.refer_CreateVCdrom.getUtil()
                .getResource("com.huawei.vm.console.out.427"));
            this.refer_CreateVCdrom.getCdCon().setEnabled(true);
            this.refer_CreateVCdrom.getCdLocalSkim().setEnabled(true);
            this.refer_CreateVCdrom.getCdLocalText().setEnabled(false);
            this.refer_CreateVCdrom.getCdLocal().setEnabled(true);
            return;
          } 
        } 
        this.refer_CreateVCdrom.submitVForm(this.refer_CreateVCdrom.getCdrom());
      }
      else {
        int result = JOptionPane.showConfirmDialog(this.refer_CreateVCdrom.getCdp(), this.refer_CreateVCdrom
            .getUtil().getResource("com.huawei.vm.console.out.412"), 
            UIManager.getString("OptionPane.titleText"), 0);
        if (0 == result)
        {
          this.refer_CreateVCdrom.getCdRomVMlink().stop();
          this.refer_CreateVCdrom.setCdRomVMlink(null);
          this.refer_CreateVCdrom.disconnectVMLink(this.refer_CreateVCdrom.getCdrom());
        }
        else
        {
          return;
        }
      } 
    } catch (Exception e) {
      LoggerUtil.error(e.getClass().getName());
    } 
  }
}
