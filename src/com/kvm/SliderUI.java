package com.kvm;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;
public class SliderUI
  extends BasicSliderUI
{
  public SliderUI(JSlider b) {
    super(b);
  }
  public void paintThumb(Graphics g) {
    if (g instanceof Graphics2D) {
      Graphics2D g2d = (Graphics2D)g;
      g2d.setPaint(Color.white);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.fill3DRect(this.thumbRect.x + this.thumbRect.width / 4, this.thumbRect.y, this.thumbRect.width / 2, this.thumbRect.height, true);
    } 
  }
}
