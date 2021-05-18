package com.kvm;
import com.library.InetAddressUtils;
import com.library.LibException;
import com.library.LoggerUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
public class BladeCommu
{
  private long disConnTime = 0L;
  private boolean isRequest = true;
  private transient KVMInterface kvmInterface = null;
  private static final int READ_SIZE = 4096;
  private static final int TIME_OUT = 20000;
  private static final int WAIT_TIME = 15000;
  private byte[] data = new byte[4096];
  private Client client = null;
  private int bladeNO = 0;
  private String ip = null;
  private int port = 0;
  private boolean authStatus = false;
  private boolean isAutoFlag = true;
  private Socket socket;
  private BufferedOutputStream dout;
  private BufferedInputStream din;
  public boolean getAuthStatus() {
    return this.authStatus;
  }
  public void setAuthStatus(boolean status) {
    this.authStatus = status;
  }
  public Socket getSocket() {
    return this.socket;
  }
  public void setSocket(Socket socket) {
    this.socket = socket;
  }
  public void setClient(Client client) {
    this.client = client;
  }
  public BladeCommu(String ip, int port) throws LibException {
    long startTime = System.currentTimeMillis();
    try {
      this.ip = InetAddressUtils.getSafeIP(ip);
      this.port = InetAddressUtils.getSafePort(port);
      this.socket = new Socket(this.ip, this.port);
      Debug.printExc("create socket success.");
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
          LoggerUtil.error(e.getClass().getName());
        } 
      }
      throw new LibException(ioe.getMessage());
    }
    catch (Exception e) {
      LoggerUtil.error(e.getClass().getName());
    } 
  }
  public void reConnBlade(int reConnNum, int maxNum) throws LibException {
    try {
      if (this.socket != null) {
        this.authStatus = false;
        this.socket.close();
      } 
      if (this.kvmInterface.getFloatToolbar().getVirtualMedia().getVmApplet().getConsole().getTimerTask() != null)
        this.kvmInterface.getFloatToolbar().getVirtualMedia().getVmApplet().getConsole().getTimerTask().cancel(); 
      this.kvmInterface.getFloatToolbar().getVirtualMedia().getVmApplet().getConsole().errorProcess(0, 123);
    }
    catch (IOException e) {
      Debug.printExc(e.getClass().getName());
    } 
    if (reConnNum > maxNum) {
      return;
    }
    try {
      if (null == this.ip) {
        return;
      }
      this.socket = new Socket();
      SocketAddress socketAddress = new InetSocketAddress(this.ip, this.port);
      this.socket.connect(socketAddress, 5000);
      this.socket.setSoTimeout(20000);
      this.socket.setTcpNoDelay(true);
      this.din = new BufferedInputStream(this.socket.getInputStream());
      this.dout = new BufferedOutputStream(this.socket.getOutputStream(), 1);
      BladeReconnect reconnect = new BladeReconnect(this.bladeNO, this.dout, this.kvmInterface);
      reconnect.start();
      sentData(this.kvmInterface.getPackData().mouseModeControl((byte)36, (byte)2, this.bladeNO));
    }
    catch (IOException ioe) {
      reConnNum++;
      if (reConnNum <= maxNum) {
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException interruptedException) {}
        reConnBlade(reConnNum, maxNum);
        return;
      } 
      throw new LibException(ioe.getMessage());
    } 
  }
  public void sentData(byte[] bytes) {
    try {
      LoggerUtil.info( "sentData(byte[] bytes): "+ bytes);
      this.dout.write(bytes);
      this.dout.flush();
      LoggerUtil.info( "this.dout.flush()");
      this.client.setSent(this.client.getSent() + bytes.length);
      LoggerUtil.info( "this.client.setSent:"+this.client.getSent() +","+ bytes.length);
    }
    catch (IOException ioe) {
      Debug.printExc(ioe.getClass().getName());
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
        this.client.setReceive(this.client.getReceive() + len);
        return bytes;
      } 
      this.client.setReceive(this.client.getReceive() + len);
      if (this.data != null)
      {
        return (byte[])this.data.clone();
      }
      byte[] tmp = null;
      return tmp;
    }
    catch (IOException ex) {
      if (this.isAutoFlag)
      {
        if (this.kvmInterface.getBladeList().size() == 1) {
          try
          {
            reConnBlade(1, 50);
            int len = this.din.read(this.data);
            if (len < 4096) {
              if (len < 0)
              {
                throw new IOException("Data's length is negative");
              }
              byte[] bytes = new byte[len];
              System.arraycopy(this.data, 0, bytes, 0, len);
              this.client.setReceive(this.client.getReceive() + len);
              return bytes;
            } 
            this.client.setReceive(this.client.getReceive() + len);
            if (null != this.data)
            {
              return (byte[])this.data.clone();
            }
            byte[] arrayOfByte = null;
            return arrayOfByte;
          }
          catch (LibException e)
          {
            LoggerUtil.error(e.getClass().getName());
          }
          catch (IOException e)
          {
            LoggerUtil.error(e.getClass().getName());
          }
        }
        else if (this.disConnTime == 0L) {
          repeatConnBlade();
        } else {
          try {
            Thread.sleep(10000L);
          }
          catch (InterruptedException e) {
            LoggerUtil.error(e.getClass().getName());
          } 
          try {
            if (this.socket.isClosed() && this.client.getSocket().isConnected() && this.isRequest)
            {
              this.client.sentData(this.kvmInterface.getPackData().reqBladeState(this.bladeNO, Base.getConnMode()));
              this.kvmInterface.getClientSocket()
                .getBladeMap()
                .put(String.valueOf(this.bladeNO), Integer.valueOf(this.bladeNO));
            }
          } catch (LibException e) {
            LoggerUtil.error(e.getClass().getName());
          } 
          byte[] arrayOfByte = null;
          return arrayOfByte;
        } 
      }
      try {
        this.socket.close();
      }
      catch (IOException e1) {
        Debug.printExc(e1.getClass().getName());
      } 
      this.kvmInterface.getKvmUtil().getBladeThread().getDrawThread().getImagePane().quitConn("No_bladeData");
      byte[] tmp = null;
      return tmp;
    } 
  }
  private byte[] repeatConnBlade() {
    this.disConnTime = System.currentTimeMillis();
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
    try {
      Thread.sleep(1000L);
    }
    catch (InterruptedException e1) {
      LoggerUtil.error(e1.getClass().getName());
    } 
    try {
      this.client.sentData(this.kvmInterface.getPackData().reqBladeState(this.bladeNO, Base.getConnMode()));
      this.kvmInterface.getClientSocket().getBladeMap().put(String.valueOf(this.bladeNO), Integer.valueOf(this.bladeNO));
    }
    catch (LibException e) {
      byte[] arrayOfByte = null;
      return arrayOfByte;
    } 
    byte[] tmp = null;
    return tmp;
  }
  public void setBladeNumber(int bladeNO) {
    this.bladeNO = bladeNO;
  }
  public boolean isAutoFlag() {
    return this.isAutoFlag;
  }
  public void setAutoFlag(boolean isAutoFlag) {
    this.isAutoFlag = isAutoFlag;
  }
  public void setKvmInterface(KVMInterface kvmInterface2) {
    this.kvmInterface = kvmInterface2;
  }
  public void setRequest(boolean isRequest) {
    this.isRequest = isRequest;
  }
}
