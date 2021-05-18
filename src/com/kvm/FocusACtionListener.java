package com.kvm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class FocusACtionListener
  implements ActionListener
{
  private ImagePane imagePane = null;
  public FocusACtionListener(ImagePane refer) {
    this.imagePane = refer;
  }
  public void actionPerformed(ActionEvent evt) {
    this.imagePane.setFocus();
  }
}
