package com.kvm;
import com.huawei.vm.console.utils.TestPrint;
import java.awt.Dimension;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
class KVMChangeListener
  implements ChangeListener
{
  private KVMUtil kvmUtil = null;
  public KVMChangeListener(KVMUtil refer) {
    this.kvmUtil = refer;
  }
  public void stateChanged(ChangeEvent e) {
    if (this.kvmUtil.getKvmInterface().getTabbedpane().getTabCount() > 0) {
      if (null == this.kvmUtil.getKvmInterface().getTabbedpane().getSelectedComponent()) {
        TestPrint.println(3, "tabbedpane getSelectedComponent error");
        return;
      } 
      int slotNO = ((ImagePane)this.kvmUtil.getKvmInterface().getTabbedpane().getSelectedComponent()).getBladeNumber();
      this.kvmUtil.getKvmInterface().setActionBlade(slotNO);
      ImagePane imagePane = this.kvmUtil.getImagePane(slotNO);
      this.kvmUtil.getKvmInterface()
        .getTabbedpane()
        .setPreferredSize(new Dimension(imagePane.getImagePaneWidth() + (imagePane.getLocation()).x, imagePane
            .getImagePaneHeight() + (imagePane.getLocation()).y + 5));
      this.kvmUtil.setDrawDisplay(false);
      BladeThread bladeThread = (BladeThread)this.kvmUtil.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(slotNO));
      DrawThread drawThread = bladeThread.getDrawThread();
      drawThread.getKvmUtil().setFirstJudge(true);
      bladeThread.getKvmUtil().resetBuf();
      drawThread.getlList().clear();
      drawThread.getComImage().clear();
      drawThread.setDisplay(true);
      bladeThread.getBladeCommu().sentData(this.kvmUtil.getKvmInterface()
          .getPackData()
          .contrRate(35, bladeThread.getBladeNOByBladeThread()));
      if (null != this.kvmUtil.getKvmInterface().getToolbar().getMouseSynButton())
      {
        if (bladeThread.isNew()) {
          if (!this.kvmUtil.getKvmInterface().getBase().isMstsc())
          {
            this.kvmUtil.getKvmInterface().getToolbar().getMouseSynButton().setEnabled(false);
          }
        }
        else {
          this.kvmUtil.getKvmInterface().getToolbar().getMouseSynButton().setEnabled(true);
        } 
      }
      imagePane.getKvmInterface()
        .getFloatToolbar()
        .setLocation((imagePane.getKvmInterface().getFloatToolbar().getImgwidth() - imagePane.getKvmInterface()
          .getFloatToolbar()
          .getWidth()) / 2, -1);
      imagePane.getKvmInterface().getFloatToolbar().setVisible(true);
      if (imagePane.getKvmInterface().getFloatToolbar().isVirtualMedia()) {
        imagePane.getKvmInterface()
          .getFloatToolbar()
          .setFlpLocation((imagePane.getKvmInterface().getFloatToolbar().getImgwidth() - imagePane.getKvmInterface()
            .getFloatToolbar()
            .getFlpWidth()) / 2, imagePane
            .getKvmInterface().getFloatToolbar().getHeight() - 1);
        imagePane.getKvmInterface()
          .getFloatToolbar()
          .setCDLocation((imagePane.getKvmInterface().getFloatToolbar().getImgwidth() - imagePane.getKvmInterface()
            .getFloatToolbar()
            .getCDWidth()) / 2, imagePane
            .getKvmInterface().getFloatToolbar().getHeight() - 1);
      } 
    } 
  }
}
