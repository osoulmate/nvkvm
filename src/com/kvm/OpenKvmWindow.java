package com.kvm;
import com.kvmV1.IManaKVMApplet;
import com.library.LoggerUtil;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
class OpenKvmWindow
  extends Thread
{
  String[] inputParaCo = null;
  String hostString = "";
  String deviceSerialNumber = "";
  int firmwareVersion = -1;
  public void setPara(String[] inputPara, String host, String SerialNumber, int firmVersion) {
    this.inputParaCo = inputPara;
    this.hostString = host;
    this.deviceSerialNumber = SerialNumber;
    this.firmwareVersion = firmVersion;
  }
  public void run() {
    if (this.firmwareVersion == 0) {
      IManaKVMApplet kvmApplet = new IManaKVMApplet();
      JFrame jFrame = new JFrame();
      Action emptyAction = new JFrameF10Action();
      jFrame.getRootPane().getInputMap(2).put(KeyStroke.getKeyStroke("F10"), "F10");
      jFrame.getRootPane().getActionMap().put("F10", emptyAction);
      jFrame.add((Component)kvmApplet);
      jFrame.setDefaultCloseOperation(3);
      jFrame.setSize(1000, 820);
      Toolkit kit = Toolkit.getDefaultToolkit();
      Dimension screenSize = kit.getScreenSize();
      int screenWidth = screenSize.width / 2;
      int screenHeight = screenSize.height / 2;
      int height = jFrame.getHeight();
      int width = jFrame.getWidth();
      jFrame.setLocation(screenWidth - width / 2, screenHeight - height / 2);
      if (this.deviceSerialNumber.equals("")) {
        jFrame.setTitle("Remote Virtual Console   IP : " + this.hostString);
      }
      else {
        jFrame.setTitle("Remote Virtual Console   IP : " + this.hostString + "   SN : " + this.deviceSerialNumber);
      } 
      kvmApplet.setAppKvmFlag();
      kvmApplet.getParaFromLocal(this.inputParaCo);
      kvmApplet.init();
      jFrame.setVisible(true);
      kvmApplet.start();
    }
    else if (this.firmwareVersion > 0) {
      LoggerUtil.info( "KVMApplet kvmApplet: ");
      KVMApplet kvmApplet = new KVMApplet();
      JFrame jFrame = new JFrame();
      Action emptyAction = new JFrameF10Action();
      jFrame.getRootPane().getInputMap(2).put(KeyStroke.getKeyStroke("F10"), "F10");
      jFrame.getRootPane().getActionMap().put("F10", emptyAction);
      jFrame.add(kvmApplet);
      jFrame.setDefaultCloseOperation(3);
      jFrame.setSize(1000, 820);
      Toolkit kit = Toolkit.getDefaultToolkit();
      Dimension screenSize = kit.getScreenSize();
      int screenWidth = screenSize.width / 2;
      int screenHeight = screenSize.height / 2;
      int height = jFrame.getHeight();
      int width = jFrame.getWidth();
      jFrame.setLocation(screenWidth - width / 2, screenHeight - height / 2);
      if (this.deviceSerialNumber.equals("")) {
        jFrame.setTitle("Remote Virtual Console   IP : " + this.hostString);
      }
      else {
        jFrame.setTitle("Remote Virtual Console   IP : " + this.hostString + "   SN : " + this.deviceSerialNumber);
      } 
      kvmApplet.setAppKvmFlag();
      LoggerUtil.info( "kvmApplet.getParaFromLocal(this.inputParaCo):"+this.inputParaCo);
      kvmApplet.getParaFromLocal(this.inputParaCo);
      LoggerUtil.info( "kvmApplet.init() ");
      kvmApplet.init();
      jFrame.setVisible(true);
      kvmApplet.start();
    } 
  }
}
