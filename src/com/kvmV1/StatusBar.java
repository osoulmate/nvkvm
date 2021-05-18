package com.kvmV1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
public class StatusBar
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  public final StatusPane recevieStatus = new StatusPane(120, 20);
  public final StatusPane sentStatus = new StatusPane(100, 20);
  public final StatusPane frameNumber = new StatusPane(100, 20);
  public Timer dataStaTimer = null;
  public int frameNum = 0;
  public Client client = null;
  public JLabel lable = null;
  public StatusBar(String str) {
    this.lable = new JLabel(str);
    Font font = new Font("Serief", 1, 12);
    this.lable.setFont(font);
    this.lable.setForeground(Color.BLUE);
    setLayout(new FlowLayout(0, 5, 3));
    add(this.lable);
    add(this.recevieStatus);
    add(this.sentStatus);
    add(this.frameNumber);
    setVisible(true);
    setStatus();
  }
  class DataStaTimer
    extends TimerTask
  {
    public void run() {
      StatusBar.this.recevieStatus.setText("Received:" + StatusBar.this.client.receive);
      StatusBar.this.sentStatus.setText("Sent:" + StatusBar.this.client.sent);
      StatusBar.this.frameNumber.setText("FrameNum:" + StatusBar.this.frameNum);
      StatusBar.this.client.receive = 0;
      StatusBar.this.client.sent = 0;
      StatusBar.this.frameNum = 0;
    }
  }
  public final void setStatus() {
    int delay = 1000;
    this.dataStaTimer = new Timer("DataStaTimer", true);
    this.dataStaTimer.scheduleAtFixedRate(new DataStaTimer(), (delay * 5), delay);
  }
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
  }
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
  public void setClient(Client client) {
    this.client = client;
  }
}
