package com.kvm;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;
class KvmMenuMouseAdapter
  extends MouseAdapter
{
  private FloatToolbar ftb_KvmMenuMouseAdapter = null;
  KvmMenuMouseAdapter(FloatToolbar floatToolbar) {
    this.ftb_KvmMenuMouseAdapter = floatToolbar;
  }
  public void mouseEntered(MouseEvent e) {
    if (!this.ftb_KvmMenuMouseAdapter.getKvmInterface().isFullScreen()) {
      this.ftb_KvmMenuMouseAdapter.getImagePanel()
        .getKvmInterface()
        .setCursor(this.ftb_KvmMenuMouseAdapter.getKvmInterface().getBase().getDefCursor());
      this.ftb_KvmMenuMouseAdapter.getImagePanel().setCursor(this.ftb_KvmMenuMouseAdapter.getKvmInterface()
          .getBase()
          .getDefCursor());
    } 
  }
  public void mousePressed(MouseEvent e) {
    if (!this.ftb_KvmMenuMouseAdapter.getKvmInterface().isFullScreen()) {
      if (null != this.ftb_KvmMenuMouseAdapter.getVirtualMedia()) {
        if (this.ftb_KvmMenuMouseAdapter.getVirtualMedia().getFlp().isShowing())
        {
          if (e.getX() <= this.ftb_KvmMenuMouseAdapter.getVirtualMedia().getFlp().getX() || e
            .getX() >= this.ftb_KvmMenuMouseAdapter.getVirtualMedia().getFlp().getWidth() + this.ftb_KvmMenuMouseAdapter
            .getVirtualMedia().getFlp().getX() || e
            .getY() <= this.ftb_KvmMenuMouseAdapter.getVirtualMedia().getFlp().getY() || e.getY() >= this.ftb_KvmMenuMouseAdapter.getVirtualMedia()
            .getFlp()
            .getY() + this.ftb_KvmMenuMouseAdapter
            .getVirtualMedia().getFlp().getHeight()) {
            this.ftb_KvmMenuMouseAdapter.getVirtualMedia().getFlp().setVisible(false);
            this.ftb_KvmMenuMouseAdapter.getFloatToolbar().setShowingFlp(false);
            this.ftb_KvmMenuMouseAdapter.getFloatToolbar().getBtnFlpMenu().setBorder((Border)null);
          } 
        }
        if (this.ftb_KvmMenuMouseAdapter.getVirtualMedia().getCdp().isShowing())
        {
          if (e.getX() <= this.ftb_KvmMenuMouseAdapter.getVirtualMedia().getCdp().getX() || e
            .getX() >= this.ftb_KvmMenuMouseAdapter.getVirtualMedia().getCdp().getWidth() + this.ftb_KvmMenuMouseAdapter
            .getVirtualMedia().getCdp().getX() || e
            .getY() <= this.ftb_KvmMenuMouseAdapter.getVirtualMedia().getCdp().getY() || e.getY() >= this.ftb_KvmMenuMouseAdapter.getVirtualMedia()
            .getCdp()
            .getY() + this.ftb_KvmMenuMouseAdapter
            .getVirtualMedia().getCdp().getHeight()) {
            this.ftb_KvmMenuMouseAdapter.getVirtualMedia().getCdp().setVisible(false);
            this.ftb_KvmMenuMouseAdapter.getKvmInterface().getFloatToolbar().setShowingCD(false);
            this.ftb_KvmMenuMouseAdapter.getKvmInterface().getFloatToolbar().getBtnCDMenu().setBorder((Border)null);
          } 
        }
      } 
      if (this.ftb_KvmMenuMouseAdapter.getKvmInterface().getBladeSize() == 1 && this.ftb_KvmMenuMouseAdapter
        .getKvmInterface().getImageFile().isShowing())
      {
        if (e.getX() <= this.ftb_KvmMenuMouseAdapter.getKvmInterface().getImageFile().getX() || e
          .getX() >= this.ftb_KvmMenuMouseAdapter.getKvmInterface().getImageFile().getWidth() + this.ftb_KvmMenuMouseAdapter
          .getKvmInterface().getImageFile().getX() || e
          .getY() <= this.ftb_KvmMenuMouseAdapter.getKvmInterface().getImageFile().getY() || e.getY() >= this.ftb_KvmMenuMouseAdapter.getKvmInterface()
          .getImageFile()
          .getY() + this.ftb_KvmMenuMouseAdapter
          .getKvmInterface().getImageFile().getHeight()) {
          this.ftb_KvmMenuMouseAdapter.getKvmInterface().getImageFile().setVisible(false);
          this.ftb_KvmMenuMouseAdapter.getFloatToolbar().setShowingImagep(false);
          this.ftb_KvmMenuMouseAdapter.getFloatToolbar().getBtnCreateImage().setBorder((Border)null);
        } 
      }
    } 
  }
}
