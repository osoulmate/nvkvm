package com.kvm;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
class DQTSliderMouseListener
  implements MouseListener
{
  private FullScreen refer_DQTSliderMouse;
  private FullToolBar fullTollBarRefer;
  public DQTSliderMouseListener(FullScreen refer_DQTSliderMouse, FullToolBar fullTollBarRefer) {
    this.refer_DQTSliderMouse = refer_DQTSliderMouse;
    this.fullTollBarRefer = fullTollBarRefer;
  }
  public void mouseClicked(MouseEvent event) {}
  public void mousePressed(MouseEvent event) {}
  public void mouseEntered(MouseEvent event) {
    this.fullTollBarRefer.setPre_slider_value(this.fullTollBarRefer.getSlider_value());
  }
  public void mouseExited(MouseEvent event) {}
  public void mouseReleased(MouseEvent event) {
    int tempDqtValue = 0;
    if (this.fullTollBarRefer.getSlider_value() != this.fullTollBarRefer.getPre_slider_value()) {
      this.fullTollBarRefer.setPre_slider_value(this.fullTollBarRefer.getSlider_value());
      if (this.fullTollBarRefer.getSlider_value() >= 60) {
        tempDqtValue = 10 + this.fullTollBarRefer.getSlider_value();
      }
      else {
        tempDqtValue = this.fullTollBarRefer.getSlider_value();
      } 
      BladeThread bladeThread = (BladeThread)this.refer_DQTSliderMouse.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.refer_DQTSliderMouse.getKvmInterface().getActionBlade()));
      bladeThread.getBladeCommu().sentData(this.refer_DQTSliderMouse.getKvmInterface()
          .getKvmUtil()
          .getImagePane(this.refer_DQTSliderMouse.getKvmInterface().getActionBlade())
          .getPack()
          .DQTModeControl((byte)tempDqtValue, bladeThread.getBladeNOByBladeThread(), (byte)1));
    } 
  }
}
