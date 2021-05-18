package com.kvm;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
class FullScreenKeyHandler
  implements KeyListener
{
  private FullScreen refer_FullScreenKey;
  public FullScreenKeyHandler(FullScreen refer_FullScreenKey) {
    this.refer_FullScreenKey = refer_FullScreenKey;
  }
  public void keyPressed(KeyEvent event) {
    if (this.refer_FullScreenKey.getKvmInterface().isFullScreen() && event.isControlDown() && event.isAltDown() && event
      .isShiftDown()) {
      this.refer_FullScreenKey.getKvmInterface()
        .getFullScreen()
        .setCursor(this.refer_FullScreenKey.getKvmInterface().getBase().getDefCursor());
      this.refer_FullScreenKey.setCursor(this.refer_FullScreenKey.getKvmInterface().getBase().getDefCursor());
      MouseDisplacementImpl.setMode(0);
      this.refer_FullScreenKey.getKvmInterface().getFullScreen().getToolBarFrame().setVisible(true);
    } 
  }
  public void keyReleased(KeyEvent event) {}
  public void keyTyped(KeyEvent event) {}
}
