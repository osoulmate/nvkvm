package com.kvm;
import java.awt.Font;
import java.util.TimerTask;
class DataStaTimer
  extends TimerTask
{
  private StatusBar statusBar = null;
  public DataStaTimer(StatusBar refer) {
    this.statusBar = refer;
  }
  public void run() {
    this.statusBar.getRecevieStatus().setText("Received:" + this.statusBar.getClient().getReceive());
    this.statusBar.getRecevieStatus().setFont(new Font("Time new roman", 0, 12));
    this.statusBar.getSentStatus().setText("Sent:" + this.statusBar.getClient().getSent());
    this.statusBar.getSentStatus().setFont(new Font("Time new roman", 0, 12));
    this.statusBar.getFrameNumber().setText("FrameNum:" + this.statusBar.getFrameNum());
    this.statusBar.getFrameNumber().setFont(new Font("Time new roman", 0, 12));
    this.statusBar.getClient().setReceive(0);
    this.statusBar.getClient().setSent(0);
    this.statusBar.setFrameNum(0);
  }
}
