package com.kvm;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;
public class BackColorButton
  extends JButton
{
  private static final long serialVersionUID = 1L;
  private Color color = Color.black;
  public BackColorButton() {}
  public BackColorButton(Color color) {
    this.color = color;
  }
  public void paintComponent(Graphics g1) {
    super.paintComponent(g1);
    if (g1 instanceof Graphics2D) {
      Graphics2D g = (Graphics2D)g1;
      g.setPaint(this.color);
      g.fillRect(0, 0, 10, 10);
    } 
  }
  public void setBackground(Color bg) {
    super.setBackground(bg);
    this.color = bg;
    repaint();
  }
}
