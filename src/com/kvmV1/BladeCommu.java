package com.kvmV1;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import javax.swing.JLabel;
public class BladeCommu
{
  private long disConnTime = 0L;
  private boolean isRequest = true;
  private KVMInterface kvmInterface = null;
  private static final int READ_SIZE = 4096;
  private static final int TIME_OUT = 20000;
  private static final int WAIT_TIME = 15000;
  private byte[] data = new byte[4096];
  private Client client = null;
  private int bladeNO = 0;
  private String ip = null;
  private int port = 0;
  private boolean isAutoFlag = true;
  public Socket socket;
  public BufferedOutputStream dout;
  public BufferedInputStream din;
  public void setClient(Client client) {
    this.client = client;
  }
  public BladeCommu(String ip, int port) throws KVMException {
    long startTime = System.currentTimeMillis();
    try {
      this.ip = ip;
      this.port = port;
      if (Base.isProxy) {
        this.socket = KVMUtil.getProxySocket((getKvmInterface()).base.parameterIP, 80, ip, this.port);
      }
      else {
        Debug.printExc("not proxy");
        Debug.printExc("port::" + this.port);
        Debug.printExc("ip::" + this.ip);
        this.socket = new Socket(ip, port);
        Debug.printExc("create socket success.");
      } 
      this.socket.setSoTimeout(20000);
      this.socket.setTcpNoDelay(true);
      this.din = new BufferedInputStream(this.socket.getInputStream());
      this.dout = new BufferedOutputStream(this.socket.getOutputStream(), 1);
    }
    catch (IOException ioe) {
      long waitTime = System.currentTimeMillis() - startTime;
      if (waitTime < 15000L) {
        try {
          Thread.sleep(15000L - waitTime);
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        } 
      }
      throw new KVMException(ioe.getMessage());
    }
    catch (URISyntaxException e) {
      long waitTime = System.currentTimeMillis() - startTime;
      if (waitTime < 15000L) {
        try {
          Thread.sleep(15000L - waitTime);
        }
        catch (InterruptedException ie) {
          ie.printStackTrace();
        } 
      }
      e.printStackTrace();
      throw new KVMException(e.getMessage());
    }
    catch (Exception e) {
      e.printStackTrace();
    } 
  }
  public void reConnBlade(int reConnNum, int maxNum) throws KVMException {
    try {
      if (this.socket != null)
      {
        this.socket.close();
      }
    }
    catch (IOException e) {
      Debug.printExc(e.getMessage());
    } 
    if (reConnNum > maxNum) {
      return;
    }
    try {
      if (null == this.ip) {
        return;
      }
      this.socket = new Socket(this.ip, this.port);
      this.socket.setSoTimeout(20000);
      this.socket.setTcpNoDelay(true);
      this.din = new BufferedInputStream(this.socket.getInputStream());
      this.dout = new BufferedOutputStream(this.socket.getOutputStream(), 1);
      this.dout.write(this.kvmInterface.packData.reConnectBlade(this.bladeNO, (this.kvmInterface.kvmUtil.getImagePane(this.bladeNO)).colorBit));
      sentData(this.kvmInterface.packData.mouseModeControl((byte)36, (byte)2, this.bladeNO));
    }
    catch (IOException ioe) {
      reConnNum++;
      if (reConnNum <= maxNum) {
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException e) {}
        reConnBlade(reConnNum, maxNum);
        return;
      } 
      throw new KVMException(ioe.getMessage());
    } 
  }
  public void sentData(byte[] bytes) {
    try {
      this.dout.write(bytes);
      this.dout.flush();
      this.client.sent += bytes.length;
    }
    catch (IOException ioe) {
      Debug.printExc(ioe.getMessage());
    } 
  }
  public byte[] getData() {
    try {
      int len = this.din.read(this.data);
      if (len < 4096) {
        if (len < 0)
        {
          throw new IOException("Data's length is negative");
        }
        byte[] bytes = new byte[len];
        System.arraycopy(this.data, 0, bytes, 0, len);
        this.client.receive += len;
        return bytes;
      } 
      this.client.receive += len;
      return this.data;
    }
    catch (IOException ex) {
      if (this.isAutoFlag)
      {
        if (this.kvmInterface.bladeList.size() == 1) {
          try
          {
            this.kvmInterface.tabbedpane.getModel().removeChangeListener(this.kvmInterface.kvmUtil.changeListener);
            this.socket.close();
            this.kvmInterface.statusBar.remove(this.kvmInterface.statusBar.lable);
            this.kvmInterface.statusBar.remove(this.kvmInterface.statusBar.recevieStatus);
            this.kvmInterface.statusBar.remove(this.kvmInterface.statusBar.sentStatus);
            this.kvmInterface.statusBar.remove(this.kvmInterface.statusBar.frameNumber);
            String label = this.kvmInterface.kvmUtil.getString("timeout");
            this.kvmInterface.statusBar.lable = new JLabel(label);
            Font font = new Font("Serief", 1, 12);
            this.kvmInterface.statusBar.lable.setFont(font);
            this.kvmInterface.statusBar.lable.setForeground(Color.BLUE);
            this.kvmInterface.statusBar.setLayout(new FlowLayout(0, 5, 3));
            this.kvmInterface.statusBar.add(this.kvmInterface.statusBar.lable);
            this.kvmInterface.statusBar.add(this.kvmInterface.statusBar.recevieStatus);
            this.kvmInterface.statusBar.add(this.kvmInterface.statusBar.sentStatus);
            this.kvmInterface.statusBar.add(this.kvmInterface.statusBar.frameNumber);
            this.kvmInterface.kvmUtil.disconnectBlade(this.bladeNO);
            this.kvmInterface.tabbedpane.getModel().addChangeListener(this.kvmInterface.kvmUtil.changeListener);
          }
          catch (IOException e1)
          {
            Debug.printExc(e1.getMessage());
          }
        }
        else {
          if (this.disConnTime == 0L) {
            repeatConnBlade();
          } else {
            try {
              Thread.sleep(10000L);
            }
            catch (InterruptedException e) {
              e.printStackTrace();
            } 
            try {
              if (this.socket.isClosed() && this.client.socket.isConnected() && this.isRequest)
              {
                this.client.sentData(this.kvmInterface.packData.reqBladeState(this.bladeNO, Base.connMode));
                this.kvmInterface.clientSocket.bladeMap.put(String.valueOf(this.bladeNO), Integer.valueOf(this.bladeNO));
              }
            }
            catch (KVMException e) {}
          } 
          try {
            this.socket.close();
          }
          catch (IOException e1) {
            Debug.printExc(e1.getMessage());
          } 
        } 
      }
      return null;
    } 
  }
  private byte[] repeatConnBlade() {
    this.disConnTime = System.currentTimeMillis();
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
    try {
      Thread.sleep(1000L);
    }
    catch (InterruptedException e1) {
      e1.printStackTrace();
    } 
    try {
      this.client.sentData(this.kvmInterface.packData.reqBladeState(this.bladeNO, Base.connMode));
      this.kvmInterface.clientSocket.bladeMap.put(String.valueOf(this.bladeNO), Integer.valueOf(this.bladeNO));
    }
    catch (KVMException e) {
      return null;
    } 
    return null;
  }
  public int getBladeNO() {
    return this.bladeNO;
  }
  public void setBladeNO(int bladeNO) {
    this.bladeNO = bladeNO;
  }
  public boolean isAutoFlag() {
    return this.isAutoFlag;
  }
  public void setAutoFlag(boolean isAutoFlag) {
    this.isAutoFlag = isAutoFlag;
  }
  public Client getClient() {
    return this.client;
  }
  public void setKvmInterface(KVMInterface kvmInterface) {
    this.kvmInterface = kvmInterface;
  }
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public void setRequest(boolean isRequest) {
    this.isRequest = isRequest;
  }
  public String getIp() {
    return this.ip;
  }
  public void setIp(String ip) {
    this.ip = ip;
  }
}
