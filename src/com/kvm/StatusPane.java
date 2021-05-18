package com.kvm;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
class StatusPane
  extends JLabel
{
  private static final long serialVersionUID = 1L;
  public StatusPane(int length, int width) {
    setBackground(Color.lightGray);
    setForeground(Color.black);
    setHorizontalAlignment(0);
    setBorder(BorderFactory.createBevelBorder(1));
    setPreferredSize(new Dimension(length, width));
  }
}
