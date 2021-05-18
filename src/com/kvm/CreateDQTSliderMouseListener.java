package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
class CreateDQTSliderMouseListener
  extends MouseAdapter
{
  private FloatToolbar ftb_CreateDQTSliderMouse = null;
  CreateDQTSliderMouseListener(FloatToolbar floatToolbar) {
    this.ftb_CreateDQTSliderMouse = floatToolbar;
  }
  public void mousePressed(MouseEvent event) {}
  public void mouseClicked(MouseEvent event) {}
  public void mouseEntered(MouseEvent event) {
    this.ftb_CreateDQTSliderMouse.setPre_slider_value(this.ftb_CreateDQTSliderMouse.getSlider_value());
  }
  public void mouseExited(MouseEvent event) {}
  public void mouseReleased(MouseEvent event) {
    int tempDqtValue = 0;
    if (this.ftb_CreateDQTSliderMouse.getPre_slider_value() != this.ftb_CreateDQTSliderMouse.getSlider_value()) {
      this.ftb_CreateDQTSliderMouse.setPre_slider_value(this.ftb_CreateDQTSliderMouse.getSlider_value());
      if (this.ftb_CreateDQTSliderMouse.getSlider_value() >= 60) {
        tempDqtValue = this.ftb_CreateDQTSliderMouse.getSlider_value() + 10;
      }
      else {
        tempDqtValue = this.ftb_CreateDQTSliderMouse.getSlider_value();
      } 
      BladeThread bladeThread = (BladeThread)this.ftb_CreateDQTSliderMouse.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.ftb_CreateDQTSliderMouse.getKvmInterface().getActionBlade()));
      bladeThread.getBladeCommu().sentData(this.ftb_CreateDQTSliderMouse.getKvmInterface()
          .getKvmUtil()
          .getImagePane(this.ftb_CreateDQTSliderMouse.getKvmInterface().getActionBlade())
          .getPack()
          .DQTModeControl((byte)tempDqtValue, bladeThread.getBladeNOByBladeThread(), (byte)1));
    } 
  }
}
