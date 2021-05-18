package com.huawei.vm.console.communicationV1;
import com.huawei.vm.console.utilsV1.ResourceUtil;
import com.huawei.vm.console.utilsV1.TestPrint;
import com.huawei.vm.console.utilsV1.VMException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.SocketException;
public class CommunicationReceiver
  implements Receiver
{
  private final Socket socket;
  private final BufferedInputStream in;
  private int overTime = 3000;
  public CommunicationReceiver(Socket socket) throws IOException {
    this.socket = socket;
    this.in = new BufferedInputStream(socket.getInputStream());
    this.overTime = Integer.parseInt(ResourceUtil.getConfigItem("com.huawei.vm.console.config.receiver.overtime"));
  }
  public CommunicationReceiver(Socket socket, int overTime) throws IOException {
    this(socket);
    this.overTime = overTime;
  }
  public void receive(byte[] packet, int size) throws VMException {
    int curRec = 0;
    int off = 0;
    try {
      this.socket.setSoTimeout(0);
    }
    catch (SocketException e1) {
      TestPrint.println(4, Thread.currentThread().getName() + " : Receiver -- Socket Excpetion on set time out:" + e1.getMessage());
    } 
    while (size > 0) {
      try {
        curRec = this.in.read(packet, off, size);
      }
      catch (IOException e) {
        TestPrint.println(4, Thread.currentThread().getName() + " : Receiver -- IO Excpetion:" + e.getMessage());
        throw new VMException(102);
      } 
      if (curRec < 0)
      {
        throw new VMException(101);
      }
      off += curRec;
      size -= curRec;
    } 
  }
  public int receiveImmediate(byte[] packet, int size) throws VMException {
    int curRec = 0;
    int off = 0;
    try {
      this.socket.setSoTimeout(this.overTime);
    }
    catch (SocketException e1) {
      TestPrint.println(4, Thread.currentThread().getName() + " : Receiver -- Socket Excpetion:" + e1.getMessage());
    } 
    while (size > 0) {
      try {
        curRec = this.in.read(packet, off, size);
      }
      catch (InterruptedIOException interruptedioexception) {
        break;
      }
      catch (IOException e) {
        TestPrint.println(4, Thread.currentThread().getName() + " : Receiver -- IO Excpetion:" + e.getMessage());
        throw new VMException(102);
      } 
      if (curRec < 0)
      {
        throw new VMException(101);
      }
      off += curRec;
      size -= curRec;
    } 
    return off;
  }
  public boolean receiveByLimit(byte[] packet, int size, int limitTime) throws VMException {
    int curRec = 0;
    int off = 0;
    try {
      this.socket.setSoTimeout(limitTime);
    }
    catch (SocketException e1) {
      TestPrint.println(4, Thread.currentThread().getName() + " : Receiver -- Socket Excpetion:" + e1.getMessage());
    } 
    while (size > 0) {
      try {
        curRec = this.in.read(packet, off, size);
      }
      catch (InterruptedIOException interruptedioexception) {
        try {
          this.socket.setSoTimeout(0);
        }
        catch (SocketException e) {
          TestPrint.println(4, Thread.currentThread().getName() + " : Receiver -- Socket Excpetion:" + e.getMessage());
        } 
        if (0 == off)
        {
          return false;
        }
        continue;
      } catch (IOException e) {
        TestPrint.println(4, Thread.currentThread().getName() + " : Receiver -- IO Excpetion:" + e.getMessage());
        throw new VMException(102);
      } 
      if (curRec < 0)
      {
        throw new VMException(101);
      }
      off += curRec;
      size -= curRec;
    } 
    TestPrint.printArray(2, packet, 0, off, Thread.currentThread().getName() + " : Receiver -- packet");
    return true;
  }
  public void setOverTime(int overTime) {
    if (0 < overTime)
    {
      this.overTime = overTime;
    }
  }
}
