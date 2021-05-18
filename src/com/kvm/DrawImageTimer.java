package com.kvm;
import java.util.TimerTask;
class DrawImageTimer
  extends TimerTask
{
  int count = 0;
  private DrawThread dt = null;
  public DrawImageTimer(DrawThread drawThread) {
    this.dt = drawThread;
  }
  public void run() {
    if (this.dt.isDisplay())
    {
      if (this.dt.isFocusOwner()) {
        if (this.count == 10) {
          if (!this.dt.getImagePane().isFocusOwner())
          {
            if (this.dt.getBladeCommu().getAuthStatus())
            {
              this.dt.getBladeCommu().sentData(this.dt.getImagePane()
                  .getPack()
                  .clearKey(this.dt.getImagePane().getBladeNumber()));
            }
            if (this.dt.getKvmInterface().isFullScreen() && this.dt.isNew())
            {
              MouseDisplacementImpl.setMode(0);
            }
            this.dt.setFocusOwner(false);
            this.count = 0;
          }
          else
          {
            this.count = 0;
          }
        } else {
          this.count++;
        }
      }
      else if (this.dt.getImagePane().isFocusOwner()) {
        this.count = 0;
        this.dt.setFocusOwner(true);
      }
      else if (this.count == 20) {
        if (this.dt.getBladeCommu().getAuthStatus())
        {
          this.dt.getBladeCommu().sentData(this.dt.getImagePane()
              .getPack()
              .clearKey(this.dt.getImagePane().getBladeNumber()));
        }
        if (this.dt.getKvmInterface().isFullScreen() && this.dt.isNew())
        {
          MouseDisplacementImpl.setMode(0);
        }
        this.count = 0;
      }
      else {
        this.count++;
      } 
    }
  }
}
