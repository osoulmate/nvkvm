package com.kvm;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
class DQTSliderAction
  implements ChangeListener
{
  private FullScreen refer_DQTSliderAction;
  private FullToolBar fullTollBarRefer;
  public DQTSliderAction(FullScreen refer_DQTSliderAction, FullToolBar fullTollBarRefer) {
    this.refer_DQTSliderAction = refer_DQTSliderAction;
    this.fullTollBarRefer = fullTollBarRefer;
  }
  public void stateChanged(ChangeEvent e) {
    int tempDqtValue = 0;
    int pre_slider_value = this.fullTollBarRefer.getSlider_value();
    BladeThread bladeThread = (BladeThread)this.refer_DQTSliderAction.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.refer_DQTSliderAction.getKvmInterface().getActionBlade()));
    if (this.fullTollBarRefer.getSlider_value() < this.fullTollBarRefer.getDqtSlider().getValue()) {
      this.fullTollBarRefer.setSlider_value(this.fullTollBarRefer.getDqtSlider().getValue());
      if (this.fullTollBarRefer.getSlider_value() % 10 >= 5) {
        this.fullTollBarRefer.setSlider_value((this.fullTollBarRefer.getSlider_value() / 10 + 1) * 10);
      }
      else {
        this.fullTollBarRefer.setSlider_value(this.fullTollBarRefer.getSlider_value() / 10 * 10);
      } 
      this.fullTollBarRefer.getDqtSlider().setValue(this.fullTollBarRefer.getSlider_value());
    }
    else if (this.fullTollBarRefer.getDqtSlider().getValue() < this.fullTollBarRefer.getSlider_value()) {
      this.fullTollBarRefer.setSlider_value(this.fullTollBarRefer.getDqtSlider().getValue());
      if (this.fullTollBarRefer.getSlider_value() % 10 > 5) {
        this.fullTollBarRefer.setSlider_value((this.fullTollBarRefer.getSlider_value() / 10 + 1) * 10);
      }
      else {
        this.fullTollBarRefer.setSlider_value(this.fullTollBarRefer.getSlider_value() / 10 * 10);
      } 
      this.fullTollBarRefer.getDqtSlider().setValue(this.fullTollBarRefer.getSlider_value());
    } 
    if (this.fullTollBarRefer.getSlider_value() >= 60) {
      tempDqtValue = 10 + this.fullTollBarRefer.getSlider_value();
    }
    else {
      tempDqtValue = this.fullTollBarRefer.getSlider_value();
    } 
    if (this.fullTollBarRefer.getSlider_value() != pre_slider_value)
    {
      bladeThread.getBladeCommu().sentData(this.refer_DQTSliderAction.getKvmInterface()
          .getKvmUtil()
          .getImagePane(this.refer_DQTSliderAction.getKvmInterface().getActionBlade())
          .getPack()
          .DQTModeControl((byte)tempDqtValue, bladeThread.getBladeNOByBladeThread(), (byte)2));
    }
  }
}
