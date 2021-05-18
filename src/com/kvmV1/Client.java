package com.kvmV1;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.JOptionPane;
public class Client
{
  private static final String NET_MESSAGE = "Network_interrupt_message";
  private static final int WAIT_TIME = 5000;
  public DatagramSocket socketUDP;
  public DatagramPacket packet;
  public InetAddress address;
  public static int port;
  public static int timeout = 0;
  public Socket socket;
  public DataOutputStream dout;
  public BufferedInputStream din;
  public int receive;
  public int sent;
  private static final int READ_SIZE = 250;
  public byte[] data = new byte[250];
  public KVMInterface kvmInterface = null;
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public void sentData(byte[] bytes) throws KVMException {
    try {
      if (this.kvmInterface.base.newConnTime == 0L) {
        if ("TCP".equals(IManaKVMApplet.protocol)) {
          this.dout.write(bytes);
          this.sent += bytes.length;
        } 
        if ("UDP".equals(IManaKVMApplet.protocol))
        {
          DatagramPacket packet = new DatagramPacket(bytes, bytes.length, this.address, port);
          this.socketUDP.send(packet);
          this.sent += bytes.length;
        }
      }
      else {
        return;
      } 
    } catch (IOException ioe) {
      this.kvmInterface.base.newConnTime = System.currentTimeMillis();
      while (System.currentTimeMillis() - this.kvmInterface.base.newConnTime < 30000L) {
        try {
          retryConnect();
        }
        catch (IOException e) {
          continue;
        } 
        try {
          Thread.sleep(5000L);
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        } 
        this.kvmInterface.base.newConnTime = 0L;
        return;
      } 
      if (this.kvmInterface.isFullScreen) {
        this.kvmInterface.tabbedpane.getModel().removeChangeListener(this.kvmInterface.kvmUtil.changeListener);
        this.kvmInterface.kvmUtil.returnToWin();
        this.kvmInterface.tabbedpane.getModel().addChangeListener(this.kvmInterface.kvmUtil.changeListener);
      } 
      ArrayList<String> keyList = new ArrayList<String>();
      Iterator<String> iter = this.kvmInterface.base.threadGroup.keySet().iterator();
      this.kvmInterface.tabbedpane.getModel().removeChangeListener(this.kvmInterface.kvmUtil.changeListener);
      while (iter.hasNext())
      {
        keyList.add(iter.next());
      }
      int num = this.kvmInterface.base.threadGroup.size();
      for (int i = 0; i < num; i++) {
        int bladeNO = Integer.parseInt(keyList.get(i));
        this.kvmInterface.kvmUtil.disconnectBlade(bladeNO);
      } 
      keyList.clear();
      this.kvmInterface.tabbedpane.getModel().addChangeListener(this.kvmInterface.kvmUtil.changeListener);
      this.kvmInterface.kvmUtil.setButtonEnable(false);
      if (null != this.kvmInterface.toolbar.refreshButton)
      {
        this.kvmInterface.toolbar.refreshButton.setEnabled(false);
      }
      Arrays.fill(this.kvmInterface.kvmUtil.bladePreInfo, (byte)0);
      this.kvmInterface.kvmUtil.setBladeEnable();
      try {
        this.socket.close();
      }
      catch (IOException e) {
        Debug.printExc(e.getMessage());
      } finally {
        try {
          this.socket.close();
        }
        catch (IOException e) {
          Debug.printExc(e.getMessage());
        } 
      } 
      this.kvmInterface.base.newConnTime = 0L;
      throw new KVMException("IO_ERRCODE", ioe.getMessage());
    } 
  }
  public byte[] getSmmData() {
    try {
      if ("TCP".equals(IManaKVMApplet.protocol)) {
        int len = this.din.read(this.data);
        if (len < 0)
        {
          return null;
        }
        byte[] bytes = new byte[len];
        if (len < 250) {
          System.arraycopy(this.data, 0, bytes, 0, len);
          this.receive += len;
          this.kvmInterface.base.getWaitTime = 0L;
          return bytes;
        } 
        this.receive += len;
        System.arraycopy(this.data, 0, bytes, 0, len);
        this.kvmInterface.base.getWaitTime = 0L;
        return bytes;
      } 
      if ("UDP".equals(IManaKVMApplet.protocol))
      {
        this.socketUDP.receive(this.packet);
        int len = this.packet.getLength();
        byte[] bytes = new byte[len];
        System.arraycopy(this.packet.getData(), 0, bytes, 0, len);
        this.receive += len;
        return bytes;
      }
    } catch (IOException ex) {
      if (this.kvmInterface.base.getWaitTime == 0L) {
        try {
          Thread.sleep(5000L);
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        } 
        this.kvmInterface.base.getWaitTime = System.currentTimeMillis();
      } 
      if (System.currentTimeMillis() - this.kvmInterface.base.getWaitTime < 45000L) {
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        } 
        return null;
      } 
      if (this.kvmInterface.isFullScreen) {
        this.kvmInterface.tabbedpane.getModel().removeChangeListener(this.kvmInterface.kvmUtil.changeListener);
        this.kvmInterface.kvmUtil.returnToWin();
        this.kvmInterface.tabbedpane.getModel().addChangeListener(this.kvmInterface.kvmUtil.changeListener);
      } 
      ArrayList<String> keyList = new ArrayList<String>();
      Iterator<String> iter = this.kvmInterface.base.threadGroup.keySet().iterator();
      this.kvmInterface.tabbedpane.getModel().removeChangeListener(this.kvmInterface.kvmUtil.changeListener);
      while (iter.hasNext())
      {
        keyList.add(iter.next());
      }
      int num = this.kvmInterface.base.threadGroup.size();
      for (int i = 0; i < num; i++) {
        int bladeNO = Integer.parseInt(keyList.get(i));
        this.kvmInterface.kvmUtil.disconnectBlade(bladeNO);
      } 
      keyList.clear();
      this.kvmInterface.tabbedpane.getModel().addChangeListener(this.kvmInterface.kvmUtil.changeListener);
      this.kvmInterface.kvmUtil.setButtonEnable(false);
      if (null != this.kvmInterface.toolbar.refreshButton)
      {
        this.kvmInterface.toolbar.refreshButton.setEnabled(false);
      }
      Arrays.fill(this.kvmInterface.kvmUtil.bladePreInfo, (byte)0);
      this.kvmInterface.kvmUtil.setBladeEnable();
      this.kvmInterface.clientSocket.setConn(false);
      try {
        this.socket.close();
      }
      catch (IOException e) {}
      JOptionPane.showMessageDialog(this.kvmInterface.toolbar, this.kvmInterface.kvmUtil.getString("Network_interrupt_message"));
      this.kvmInterface.base.getWaitTime = -1L;
    } 
    return null;
  }
  private void retryConnect() throws IOException {
    this.socket = new Socket(this.socket.getInetAddress(), this.socket.getPort());
    this.dout = new DataOutputStream(this.socket.getOutputStream());
    this.din = new BufferedInputStream(this.socket.getInputStream());
    this.dout.write(this.kvmInterface.packData.retryConn());
    byte[] reqBladePresentInfo = this.kvmInterface.packData.reqBladePresent();
    this.kvmInterface.clientSocket.bladePresentInfo.remove(this.kvmInterface.clientSocket.bladePreIndex);
    this.dout.write(reqBladePresentInfo);
    this.kvmInterface.clientSocket.bladePresentInfo.remove(this.kvmInterface.clientSocket.bladePreIndex);
    int count = 5;
    while (count > 0) {
      if (this.kvmInterface.clientSocket.bladePresentInfo.get(this.kvmInterface.clientSocket.bladePreIndex) != null) {
        this.kvmInterface.clientSocket.bladePresentInfo.remove(this.kvmInterface.clientSocket.bladePreIndex);
        break;
      } 
      try {
        Thread.sleep(1000L);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      } 
      count--;
      if (count == 0)
      {
        throw new IOException();
      }
    } 
  }
}
