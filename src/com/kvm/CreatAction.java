package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
class CreatAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private ImageFile imageFile_CreatAction = null;
  public CreatAction(ImageFile refer) {
    this.imageFile_CreatAction = refer;
  }
  public void actionPerformed(ActionEvent event) {
    if (this.imageFile_CreatAction.isImageCreate()) {
      this.imageFile_CreatAction.getVmApplet().threadStart("create image");
      if (this.imageFile_CreatAction.getVmApplet().isImageCreateOK() && null != this.imageFile_CreatAction
        .getImagePath().getText() && 
        !"".equals(this.imageFile_CreatAction.getImagePath().getText()) && 
        !this.imageFile_CreatAction.getUtil().getResource("flp_cd_none").equals(this.imageFile_CreatAction.getImageCheck().getSelectedItem())) {
        if (!this.imageFile_CreatAction.suffixCheck()) {
          return;
        }
        if (this.imageFile_CreatAction.getVmApplet().checkFileExsit(this.imageFile_CreatAction.getImagePath().getText()))
        {
          int result = JOptionPane.showConfirmDialog(this.imageFile_CreatAction.getImageCreate(), this.imageFile_CreatAction
              .getImagePath().getText() + ' ' + this.imageFile_CreatAction
              .getUtil().getResource("isImageCreateOK"), 
              UIManager.getString("OptionPane.titleText"), 0);
          this.imageFile_CreatAction.setDeviceForCreateImage((String)this.imageFile_CreatAction.getImageCheck()
              .getSelectedItem());
          this.imageFile_CreatAction.setImageForCreateImage(this.imageFile_CreatAction.getImagePath().getText());
          if (result == 0)
          {
            this.imageFile_CreatAction.getVmApplet()
              .createImageFile(this.imageFile_CreatAction.getDeviceForCreateImage(), this.imageFile_CreatAction
                .getImageForCreateImage());
            this.imageFile_CreatAction.checkImageCreate();
          }
          else
          {
            return;
          }
        }
        else
        {
          this.imageFile_CreatAction.setDeviceForCreateImage((String)this.imageFile_CreatAction.getImageCheck()
              .getSelectedItem());
          this.imageFile_CreatAction.setImageForCreateImage(this.imageFile_CreatAction.getImagePath().getText());
          this.imageFile_CreatAction.getVmApplet()
            .createImageFile(this.imageFile_CreatAction.getDeviceForCreateImage(), this.imageFile_CreatAction
              .getImageForCreateImage());
          this.imageFile_CreatAction.checkImageCreate();
        }
      }
      else if (!this.imageFile_CreatAction.getVmApplet().isLibOK()) {
        this.imageFile_CreatAction.updateCreateImageItems("disable");
        JOptionPane.showMessageDialog(this.imageFile_CreatAction.getImageCreate(), this.imageFile_CreatAction
            .getVmApplet().getStatement(this.imageFile_CreatAction.getVmApplet()
              .getVmState()));
      }
      else if (!this.imageFile_CreatAction.getVmApplet().isImageCreateOK()) {
        this.imageFile_CreatAction.updateCreateImageItems("stop");
      }
      else {
        if (null == this.imageFile_CreatAction.getImagePath().getText() || ""
          .equals(this.imageFile_CreatAction.getImagePath().getText())) {
          JOptionPane.showMessageDialog(this.imageFile_CreatAction.getImageCreate(), this.imageFile_CreatAction
              .getUtil().getResource("com.huawei.vm.console.out.401"));
          return;
        } 
        JOptionPane.showMessageDialog(this.imageFile_CreatAction.getImageCreate(), this.imageFile_CreatAction
            .getUtil().getResource("com.huawei.vm.console.out.402"));
        return;
      } 
    } else {
      this.imageFile_CreatAction.getImageCreate().setEnabled(false);
      int result = JOptionPane.showConfirmDialog(this.imageFile_CreatAction.getImageCreate(), this.imageFile_CreatAction
          .getUtil().getResource("com.huawei.vm.console.out.403"), 
          UIManager.getString("OptionPane.titleText"), 0);
      if (0 == result) {
        this.imageFile_CreatAction.getT().stop();
        this.imageFile_CreatAction.isStop();
      }
      else {
        this.imageFile_CreatAction.getImageCreate().setEnabled(true);
      } 
    } 
  }
}
