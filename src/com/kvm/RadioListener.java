package com.kvm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class RadioListener
  implements ActionListener
{
  private ColorBit refer_RadioListener;
  public RadioListener(ColorBit refer_RadioListener) {
    this.refer_RadioListener = refer_RadioListener;
  }
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("8"))
    {
      this.refer_RadioListener.getKvmInterface().getKvmUtil().getImagePane(this.refer_RadioListener.getBladeNumber()).setCustBit((byte)2);
    }
    if (e.getActionCommand().equals("7"))
    {
      this.refer_RadioListener.getKvmInterface().getKvmUtil().getImagePane(this.refer_RadioListener.getBladeNumber()).setCustBit((byte)1);
    }
    if (e.getActionCommand().equals("6"))
    {
      this.refer_RadioListener.getKvmInterface().getKvmUtil().getImagePane(this.refer_RadioListener.getBladeNumber()).setCustBit((byte)0);
    }
    if (e.getActionCommand().equals("4"))
    {
      this.refer_RadioListener.getKvmInterface().getKvmUtil().getImagePane(this.refer_RadioListener.getBladeNumber()).setCustBit((byte)3);
    }
    if (this.refer_RadioListener.getKvmInterface().getKvmUtil().getImagePane(this.refer_RadioListener.getBladeNumber()) != null) {
      this.refer_RadioListener.remove();
    }
    else {
      this.refer_RadioListener.removeAll();
    } 
    this.refer_RadioListener.dispose();
  }
}
