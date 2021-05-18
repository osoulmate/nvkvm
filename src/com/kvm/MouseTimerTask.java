package com.kvm;
import com.library.LoggerUtil;
class MouseTimerTask
  extends Thread
{
  private ImagePane imagePane = null;
  byte[] bytes;
  public MouseTimerTask(ImagePane refer) {
    this.bytes = new byte[8];
    this.imagePane = refer;
  }
  public void run() {
    while (!Base.getIsSynMouse()) {
      if (!this.imagePane.isFocusOwner()) {
        if (this.imagePane.isFocusChange() == true) {
          byte[] mousData = this.imagePane.getPack().getMousData();
          mousData[0] = 0;
          mousData[3] = 0;
          this.imagePane.getPack().setMousData(mousData);
          this.imagePane.getBladeThread()
            .getBladeCommu()
            .sentData(this.imagePane.getPack().mousePackNew((byte)0, (byte)0, this.imagePane.getBladeNumber()));
        } 
        this.imagePane.setFocusChange(false);
      } 
      if (this.imagePane.isNew()) {
        if (!this.imagePane.getKvmInterface().getBase().isMstsc() && this.imagePane.isFocusChange() && 
          !Base.getIsSynMouse())
        {
          int i = MouseDisplacementImpl.getMouseDisplacement(this.bytes);
          byte[] mousData = this.imagePane.getPack().getMousData();
          if (i == 1 && (this.bytes[0] != mousData[0] || this.bytes[1] != 0 || this.bytes[2] != 0 || this.bytes[3] != 0))
          {
            mousData[0] = this.bytes[0];
            mousData[3] = this.bytes[3];
            this.imagePane.getPack().setMousData(mousData);
            this.imagePane.getBladeThread()
              .getBladeCommu()
              .sentData(this.imagePane.getPack().mousePackNew(this.bytes[1], this.bytes[2], this.imagePane.getBladeNumber()));
          }
        }
      } else {
        this.imagePane.setIfmove(true);
        if (this.imagePane.getRemoteX() == 65535 && this.imagePane.getRemoteY() == 65535) {
          if (!this.imagePane.isNew())
          {
            this.imagePane.getBladeThread()
              .getBladeCommu()
              .sentData(this.imagePane.getPack().mousePack(65535, 65535, this.imagePane.getBladeNumber()));
          }
        }
        else {
          if (this.imagePane.isActionFlage()) {
            this.imagePane.setActionFlage(false);
            return;
          } 
          if (this.imagePane.isIsmove()) {
            this.imagePane.setIsmove(false);
            return;
          } 
          if (this.imagePane.getRemotePreX() == this.imagePane.getRemoteX() && this.imagePane
            .getRemotePreY() == this.imagePane.getRemoteY() && this.imagePane
            .getRemoteX() != this.imagePane.getPosiX() - this.imagePane.getImagePaneX() && this.imagePane
            .getRemoteY() != this.imagePane.getPosiY() - this.imagePane.getImagePaneY())
          {
            if (!this.imagePane.isNew()) {
              byte[] bytes = this.imagePane.getPack().mousePack(this.imagePane.getPosiX() - this.imagePane.getImagePaneX(), this.imagePane
                  .getPosiY() - this.imagePane.getImagePaneY(), this.imagePane
                  .getBladeNumber());
              this.imagePane.getBladeThread().getBladeCommu().sentData(bytes);
              this.imagePane.setIfmove(false);
              this.imagePane.setIsmove(true);
            } 
          }
        } 
      } 
      try {
        Thread.sleep(20L);
      }
      catch (InterruptedException e) {
        LoggerUtil.error(e.getClass().getName());
      } 
    } 
  }
}
