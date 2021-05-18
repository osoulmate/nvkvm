package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class FullScreenMouseModeAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FullScreen refer_FullScreenMouseMode;
  public FullScreenMouseModeAction(FullScreen refer_FullScreenMouseMode) {
    this.refer_FullScreenMouseMode = refer_FullScreenMouseMode;
  }
  public void actionPerformed(ActionEvent e) {
    BladeThread bladeThread = (BladeThread)this.refer_FullScreenMouseMode.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.refer_FullScreenMouseMode.getActionBlade()));
    if (this.refer_FullScreenMouseMode.getKvmInterface().getBase().isMstsc()) {
      if (bladeThread.isNew())
      {
        if (this.refer_FullScreenMouseMode.getKvmInterface().isFullScreen()) {
          this.refer_FullScreenMouseMode.getKvmInterface()
            .getFullScreen()
            .setCursor((this.refer_FullScreenMouseMode.getKvmInterface().getBase()).myCursor);
          this.refer_FullScreenMouseMode.getKvmInterface()
            .getFullScreen()
            .getToolBar()
            .getMouseSynButton()
            .setEnabled(false);
        }
        else if (null != this.refer_FullScreenMouseMode.getKvmInterface().getToolbar().getMouseSynButton()) {
          this.refer_FullScreenMouseMode.getKvmInterface().getToolbar().getMouseSynButton().setEnabled(false);
        } 
      }
      this.refer_FullScreenMouseMode.getKvmInterface().getBase().setMstsc(false);
    }
    else {
      if (bladeThread.isNew()) {
        this.refer_FullScreenMouseMode.getImagePane().setCursor(this.refer_FullScreenMouseMode.getKvmInterface()
            .getBase()
            .getDefCursor());
        if (this.refer_FullScreenMouseMode.getKvmInterface().isFullScreen()) {
          this.refer_FullScreenMouseMode.getKvmInterface()
            .getFullScreen()
            .getToolBar()
            .getMouseSynButton()
            .setEnabled(true);
        }
        else if (null != this.refer_FullScreenMouseMode.getKvmInterface().getToolbar().getMouseSynButton()) {
          this.refer_FullScreenMouseMode.getKvmInterface().getToolbar().getMouseSynButton().setEnabled(true);
        } 
      } 
      this.refer_FullScreenMouseMode.getKvmInterface().getBase().setMstsc(true);
    } 
  }
}
