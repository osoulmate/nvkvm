package com.kvm;
import java.awt.Dimension;
import java.awt.Toolkit;
class OpenCopyRight
  extends Thread
{
  public void run() {
    CopyRight AboutFrame = new CopyRight();
    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screenSize = kit.getScreenSize();
    int screenWidth = screenSize.width / 2;
    int screenHeight = screenSize.height / 2;
    int height = AboutFrame.getHeight();
    int width = AboutFrame.getWidth();
    AboutFrame.setLocation(screenWidth - width / 2, screenHeight - height / 2);
    AboutFrame.setVisible(true);
  }
}
