package com.kvm;
import java.util.TimerTask;
public class StatReceiveTask
  extends TimerTask
{
  int count = 0;
  private ImagePane imagePane = null;
  public StatReceiveTask(ImagePane refer) {
    this.imagePane = refer;
  }
  public void run() {
    if (this.count < 10) {
      if (this.imagePane.getImageReceive() != 0)
      {
        this.count = 0;
        this.imagePane.setImageReceive(0);
      }
    } else {
      this.count = 0;
      this.imagePane.quitConn();
    } 
  }
}
