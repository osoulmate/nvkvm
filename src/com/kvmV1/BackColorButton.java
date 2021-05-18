package com.kvmV1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;
import javax.swing.JFrame;
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
    Graphics2D g = (Graphics2D)g1;
    g.setPaint(this.color);
    g.fillRect(0, 0, 10, 10);
  }
  public void setBackground(Color bg) {
    super.setBackground(bg);
    this.color = bg;
    repaint();
  }
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setSize(new Dimension(100, 100));
    BackColorButton bb = new BackColorButton();
    bb.setText("a");
    bb.setPreferredSize(new Dimension(10, 10));
    bb.setBackground(Color.blue);
    frame.getContentPane().add(bb);
    frame.setVisible(true);
  }
}
