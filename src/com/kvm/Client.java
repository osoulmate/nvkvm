package com.kvm;
import com.library.InetAddressUtils;
import com.library.LibException;
import com.library.LoggerUtil;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.JOptionPane;
public class Client
{
  private static final String NET_MESSAGE = "Network_interrupt_message";
  private static final int WAIT_TIME = 5000;
  private DatagramSocket socketUDP;
  private DatagramPacket packet;
  private InetAddress address;
  private static int port;
  public static final int timeout = 0;
  private Socket socket;
  private DataOutputStream dout;
  private BufferedInputStream din;
  private int receive;
  private int sent;
  private static final int READ_SIZE = 250;
  public DatagramSocket getSocketUDP() {
    return this.socketUDP;
  }
  public void setSocketUDP(DatagramSocket socketUDP) {
    this.socketUDP = socketUDP;
  }
  public DatagramPacket getPacket() {
    return this.packet;
  }
  public void setPacket(DatagramPacket packet) {
    this.packet = packet;
  }
  public InetAddress getAddress() {
    return this.address;
  }
  public void setAddress(InetAddress address) {
    this.address = address;
  }
  public static int getPort() {
    return port;
  }
  public static void setPort(int port) {
    Client.port = InetAddressUtils.getSafePort(port);
  }
  public Socket getSocket() {
    return this.socket;
  }
  public void setSocket(Socket socket) {
    this.socket = socket;
  }
  public DataOutputStream getDout() {
    return this.dout;
  }
  public void setDout(DataOutputStream dout) {
    this.dout = dout;
  }
  public BufferedInputStream getDin() {
    return this.din;
  }
  public void setDin(BufferedInputStream din) {
    this.din = din;
  }
  public int getReceive() {
    return this.receive;
  }
  public void setReceive(int receive) {
    this.receive = receive;
  }
  public int getSent() {
    return this.sent;
  }
  public void setSent(int sent) {
    this.sent = sent;
  }
  private byte[] data = new byte[250];
  private KVMInterface kvmInterface;
  public byte[] getData() {
    byte[] tmp = this.data;
    return tmp;
  }
  public void setData(byte[] data) {
    if (null != data) {
      this.data = (byte[])data.clone();
    }
    else {
      this.data = null;
    } 
  }
  public Client() {
    try {
      this.socketUDP = new DatagramSocket(port);
    }
    catch (SocketException se) {
      LoggerUtil.error(se.getClass().getName());
    } 
    this.packet = new DatagramPacket(new byte[1024], 1024);
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public void sentData(byte[] bytes) throws LibException {
    try {
      LoggerUtil.info( "mystr:" + bytes);
      LoggerUtil.info( "this.kvmInterface.getBase().getNewConnTime:" + this.kvmInterface.getBase().getNewConnTime());
      if (this.kvmInterface.getBase().getNewConnTime() == 0L) {
        if ("TCP".equals("TCP")) {
          LoggerUtil.info( "TCP equals TCP:");
          this.dout.write(bytes);
          this.sent += bytes.length;
        } 
        if ("UDP".equals("TCP"))
        {
          LoggerUtil.info( "UDP equals TCP:");
          DatagramPacket packet = new DatagramPacket(bytes, bytes.length, this.address, port);
          this.socketUDP.send(packet);
          this.sent += bytes.length;
        }
      }
      else {
        return;
      } 
    } catch (IOException ioe) {
      this.kvmInterface.getBase().setNewConnTime(System.currentTimeMillis());
      while (System.currentTimeMillis() - this.kvmInterface.getBase().getNewConnTime() < 0L) {
        try {
          LoggerUtil.info( "retryConnect: " );
          retryConnect();
        }
        catch (IOException e) {
          continue;
        } 
        try {
          Thread.sleep(5000L);
        }
        catch (InterruptedException e) {
          LoggerUtil.error(e.getClass().getName());
        } 
        this.kvmInterface.getBase().setNewConnTime(0L);
        return;
      } 
      if (this.kvmInterface.isFullScreen()) {
        this.kvmInterface.getTabbedpane().getModel().removeChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
        this.kvmInterface.getKvmUtil().returnToWin();
        this.kvmInterface.getTabbedpane().getModel().addChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
      } 
      ArrayList<Object> keyList = new ArrayList(10);
      Iterator<Object> iter = this.kvmInterface.getBase().getThreadGroup().keySet().iterator();
      this.kvmInterface.getTabbedpane().getModel().removeChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
      boolean flag = iter.hasNext();
      while (flag) {
        keyList.add(iter.next());
        flag = iter.hasNext();
      } 
      int num = this.kvmInterface.getBase().getThreadGroup().size();
      int bladeNO = 0;
      for (int i = 0; i < num; i++) {
        bladeNO = Integer.parseInt((String)keyList.get(i));
        this.kvmInterface.getKvmUtil().disconnectBlade(bladeNO);
      } 
      keyList.clear();
      this.kvmInterface.getTabbedpane().getModel().addChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
      this.kvmInterface.getKvmUtil().setButtonEnable(false);
      if (null != this.kvmInterface.getToolbar().getRefreshButton())
      {
        this.kvmInterface.getToolbar().getRefreshButton().setEnabled(false);
      }
      byte[] bladePreInfo = this.kvmInterface.getKvmUtil().getBladePreInfo();
      Arrays.fill(bladePreInfo, (byte)0);
      this.kvmInterface.getKvmUtil().setBladePreInfo(bladePreInfo);
      this.kvmInterface.getKvmUtil().setBladeEnable();
      try {
        this.socket.close();
      }
      catch (IOException e) {
        Debug.printExc(e.getClass().getName());
      } finally {
        try {
          this.socket.close();
        }
        catch (IOException e) {
          Debug.printExc(e.getClass().getName());
        } 
      } 
      this.kvmInterface.getBase().setNewConnTime(0L);
      throw new LibException("IO_ERRCODE", ioe.getMessage());
    } 
  }
  public byte[] getSmmData() {
    try {
      if ("TCP".equals("TCP")) {
        int len = this.din.read(this.data);
        if (len < 0) {
          byte[] arrayOfByte = null;
          return arrayOfByte;
        } 
        byte[] bytes = new byte[len];
        if (len < 250) {
          System.arraycopy(this.data, 0, bytes, 0, len);
          this.receive += len;
          this.kvmInterface.getBase().setGetWaitTime(0L);
          return bytes;
        } 
        this.receive += len;
        System.arraycopy(this.data, 0, bytes, 0, len);
        this.kvmInterface.getBase().setGetWaitTime(0L);
        return bytes;
      } 
      if ("UDP".equals("TCP"))
      {
        this.socketUDP.receive(this.packet);
        int len = this.packet.getLength();
        byte[] bytes = new byte[len];
        System.arraycopy(this.packet.getData(), 0, bytes, 0, len);
        this.receive += len;
        return bytes;
      }
    } catch (IOException ex) {
      if (this.kvmInterface.getBase().getGetWaitTime() == 0L) {
        try {
          Thread.sleep(5000L);
        }
        catch (InterruptedException e) {
          LoggerUtil.error(e.getClass().getName());
        } 
        this.kvmInterface.getBase().setGetWaitTime(System.currentTimeMillis());
      } 
      if (System.currentTimeMillis() - this.kvmInterface.getBase().getGetWaitTime() < 45000L) {
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException e) {
          LoggerUtil.error(e.getClass().getName());
        } 
        byte[] arrayOfByte = null;
        return arrayOfByte;
      } 
      if (this.kvmInterface.isFullScreen()) {
        this.kvmInterface.getTabbedpane().getModel().removeChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
        this.kvmInterface.getKvmUtil().returnToWin();
        this.kvmInterface.getTabbedpane().getModel().addChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
      } 
      ArrayList<Object> keyList = new ArrayList(10);
      Iterator<Object> iter = this.kvmInterface.getBase().getThreadGroup().keySet().iterator();
      this.kvmInterface.getTabbedpane().getModel().removeChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
      boolean flag = iter.hasNext();
      while (flag) {
        keyList.add(iter.next());
        flag = iter.hasNext();
      } 
      int num = this.kvmInterface.getBase().getThreadGroup().size();
      int bladeNO = 0;
      for (int i = 0; i < num; i++) {
        bladeNO = Integer.parseInt((String)keyList.get(i));
        this.kvmInterface.getKvmUtil().disconnectBlade(bladeNO);
      } 
      keyList.clear();
      this.kvmInterface.getTabbedpane().getModel().addChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
      this.kvmInterface.getKvmUtil().setButtonEnable(false);
      if (null != this.kvmInterface.getToolbar().getRefreshButton())
      {
        this.kvmInterface.getToolbar().getRefreshButton().setEnabled(false);
      }
      byte[] bladePreInfo = this.kvmInterface.getKvmUtil().getBladePreInfo();
      Arrays.fill(bladePreInfo, (byte)0);
      this.kvmInterface.getKvmUtil().setBladePreInfo(bladePreInfo);
      this.kvmInterface.getKvmUtil().setBladeEnable();
      this.kvmInterface.getClientSocket().setConn(false);
      try {
        this.socket.close();
      }
      catch (IOException e) {
        LoggerUtil.error(e.getClass().getName());
      } 
      JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface.getKvmUtil().getString("Network_interrupt_message"));
      this.kvmInterface.getBase().setGetWaitTime(-1L);
    } 
    byte[] tmp = null;
    return tmp;
  }
  private void retryConnect() throws IOException {
    this.socket = new Socket(this.socket.getInetAddress(), this.socket.getPort());
    this.dout = new DataOutputStream(this.socket.getOutputStream());
    this.din = new BufferedInputStream(this.socket.getInputStream());
    this.dout.write(this.kvmInterface.getPackData().retryConn());
    byte[] reqBladePresentInfo = this.kvmInterface.getPackData().reqBladePresent();
    this.kvmInterface.getClientSocket().getBladePresentInfo().remove(this.kvmInterface.getClientSocket().getBladePreIndex());
    this.dout.write(reqBladePresentInfo);
    this.kvmInterface.getClientSocket().getBladePresentInfo().remove(this.kvmInterface.getClientSocket().getBladePreIndex());
    int count = 5;
    while (count > 0) {
      if (this.kvmInterface.getClientSocket()
        .getBladePresentInfo()
        .get(this.kvmInterface.getClientSocket().getBladePreIndex()) != null) {
        this.kvmInterface.getClientSocket()
          .getBladePresentInfo()
          .remove(this.kvmInterface.getClientSocket().getBladePreIndex());
        break;
      } 
      try {
        Thread.sleep(1000L);
      }
      catch (InterruptedException e) {
        LoggerUtil.error(e.getClass().getName());
      } 
      count--;
      if (count == 0)
      {
        throw new IOException();
      }
    } 
  }
}
