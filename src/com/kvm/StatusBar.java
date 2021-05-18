package com.kvm;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Timer;
import javax.swing.JLabel;
import javax.swing.JPanel;
public class StatusBar
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  private final StatusPane recevieStatus = new StatusPane(120, 20);
  public StatusPane getRecevieStatus() {
    return this.recevieStatus;
  }
  private final StatusPane sentStatus = new StatusPane(100, 20);
  public StatusPane getSentStatus() {
    return this.sentStatus;
  }
  private final StatusPane frameNumber = new StatusPane(100, 20);
  public StatusPane getFrameNumber() {
    return this.frameNumber;
  }
  private transient Timer dataStaTimer = null;
  public Timer getDataStaTimer() {
    return this.dataStaTimer;
  }
  public void setDataStaTimer(Timer dataStaTimer) {
    this.dataStaTimer = dataStaTimer;
  }
  private int frameNum = 0;
  public int getFrameNum() {
    return this.frameNum;
  }
  public void setFrameNum(int frameNum) {
    this.frameNum = frameNum;
  }
  private transient Client client = null;
  public Client getClient() {
    return this.client;
  }
  private ToolBarPanel toolBarPanel = null;
  public StatusBar(String str) {
    setLayout(new BorderLayout());
    JLabel lable = new JLabel(str);
    add(lable, "West");
    if (Base.getLocal().equalsIgnoreCase("zh"))
    {
      lable.setFont(new Font("黑体", 0, 6));
    }
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(0, 5, 3));
    panel.add(this.recevieStatus);
    panel.add(this.sentStatus);
    panel.add(this.frameNumber);
    add(panel, "East");
    setVisible(true);
    setStatus();
  }
  public final void setStatus() {
    int delay = 1000;
    this.dataStaTimer = new Timer("DataStaTimer", true);
    this.dataStaTimer.schedule(new DataStaTimer(this), delay * 5L, delay);
  }
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
  }
  public void setClient(Client client) {
    this.client = client;
  }
  public void setToolBarPanel(ToolBarPanel toolBarPanel2) {
    this.toolBarPanel = toolBarPanel2;
  }
}
