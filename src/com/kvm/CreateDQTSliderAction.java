package com.kvm;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
class CreateDQTSliderAction
  implements ChangeListener
{
  private FloatToolbar ftb_CreateDQTSlider = null;
  CreateDQTSliderAction(FloatToolbar floatToolbar) {
    this.ftb_CreateDQTSlider = floatToolbar;
  }
  public void stateChanged(ChangeEvent e) {
    int pre_slider_value = 0;
    pre_slider_value = this.ftb_CreateDQTSlider.getSlider_value();
    int tempDqtValue = 0;
    BladeThread bladeThread = (BladeThread)this.ftb_CreateDQTSlider.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.ftb_CreateDQTSlider.getKvmInterface().getActionBlade()));
    if (this.ftb_CreateDQTSlider.getSlider_value() < this.ftb_CreateDQTSlider.getDqtSlider().getValue()) {
      this.ftb_CreateDQTSlider.setSlider_value(this.ftb_CreateDQTSlider.getDqtSlider().getValue());
      if (this.ftb_CreateDQTSlider.getSlider_value() % 10 >= 5) {
        this.ftb_CreateDQTSlider.setSlider_value(this.ftb_CreateDQTSlider.getSlider_value() / 10 * 10 + 10);
      }
      else {
        this.ftb_CreateDQTSlider.setSlider_value(this.ftb_CreateDQTSlider.getSlider_value() / 10 * 10);
      } 
      this.ftb_CreateDQTSlider.getDqtSlider().setValue(this.ftb_CreateDQTSlider.getSlider_value());
    }
    else if (this.ftb_CreateDQTSlider.getSlider_value() > this.ftb_CreateDQTSlider.getDqtSlider().getValue()) {
      this.ftb_CreateDQTSlider.setSlider_value(this.ftb_CreateDQTSlider.getDqtSlider().getValue());
      if (this.ftb_CreateDQTSlider.getSlider_value() % 10 > 5) {
        this.ftb_CreateDQTSlider.setSlider_value(this.ftb_CreateDQTSlider.getSlider_value() / 10 * 10 + 10);
      }
      else {
        this.ftb_CreateDQTSlider.setSlider_value(this.ftb_CreateDQTSlider.getSlider_value() / 10 * 10);
      } 
      this.ftb_CreateDQTSlider.getDqtSlider().setValue(this.ftb_CreateDQTSlider.getSlider_value());
    } 
    if (this.ftb_CreateDQTSlider.getSlider_value() >= 60) {
      tempDqtValue = this.ftb_CreateDQTSlider.getSlider_value() + 10;
    }
    else {
      tempDqtValue = this.ftb_CreateDQTSlider.getSlider_value();
    } 
    if (pre_slider_value != this.ftb_CreateDQTSlider.getSlider_value())
    {
      bladeThread.getBladeCommu().sentData(this.ftb_CreateDQTSlider.getKvmInterface()
          .getKvmUtil()
          .getImagePane(this.ftb_CreateDQTSlider.getKvmInterface().getActionBlade())
          .getPack()
          .DQTModeControl((byte)tempDqtValue, bladeThread.getBladeNOByBladeThread(), (byte)2));
    }
  }
}
