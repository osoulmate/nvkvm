package cn.nvkvm;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
class BackgroundPanel
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  private transient Image image = null;
  public BackgroundPanel(Image image) {
    this.image = image;
  }
  protected void paintComponent(Graphics g) {
    g.drawImage(this.image, 0, 0, getWidth(), getHeight(), this);
  }
}
