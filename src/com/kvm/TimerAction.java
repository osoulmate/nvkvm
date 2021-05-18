package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
class TimerAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private ImageFile imageFile_TimerAction = null;
  public TimerAction(ImageFile refer) {
    this.imageFile_TimerAction = refer;
  }
  public void actionPerformed(ActionEvent arg0) {
    int res = this.imageFile_TimerAction.getVmApplet().getImageCreateProcess();
    if (this.imageFile_TimerAction.isMulBtnForCon()) {
      if (res > 100) {
        this.imageFile_TimerAction.getT().stop();
        this.imageFile_TimerAction.getImagebar().setValue(0);
        this.imageFile_TimerAction.updateCreateImageItems("stop");
        JOptionPane.showMessageDialog(this.imageFile_TimerAction.getImageCreate(), this.imageFile_TimerAction
            .getVmApplet().getStatement(res));
      }
      else {
        int value = this.imageFile_TimerAction.getImagebar().getValue();
        if (value < 100)
        {
          this.imageFile_TimerAction.getImagebar().setValue(res);
        }
        else if (value == 100)
        {
          this.imageFile_TimerAction.getImagePath().setText(this.imageFile_TimerAction.getVmApplet()
              .getAbsoluteImagePath());
          JOptionPane.showMessageDialog(this.imageFile_TimerAction.getImageCreate(), this.imageFile_TimerAction
              .getUtil().getResource("create_succeed"));
          this.imageFile_TimerAction.getT().stop();
          this.imageFile_TimerAction.getImagebar().setValue(0);
          this.imageFile_TimerAction.getImageSaveBtn().setEnabled(true);
          this.imageFile_TimerAction.getImageCheck().setEnabled(true);
          this.imageFile_TimerAction.getImagePath().setEnabled(true);
          this.imageFile_TimerAction.getImageCreate().setText(this.imageFile_TimerAction.getUtil()
              .getResource("make_image_file_make"));
          this.imageFile_TimerAction.setImageCreate(true);
        }
      } 
    } else {
      this.imageFile_TimerAction.getT().stop();
      this.imageFile_TimerAction.getImagebar().setValue(0);
      this.imageFile_TimerAction.getImageCreate().setText(this.imageFile_TimerAction.getUtil()
          .getResource("make_image_file_make"));
      this.imageFile_TimerAction.setImageCreate(true);
    } 
  }
}
