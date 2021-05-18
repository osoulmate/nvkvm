package com.huawei.vm.console.communication;
import com.huawei.vm.console.utils.TestPrint;
import com.huawei.vm.console.utils.VMException;
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
  public CommunicationReceiver(Socket socket) throws IOException {
    this.socket = socket;
    this.in = new BufferedInputStream(socket.getInputStream());
  }
  public boolean receiveByLimit(byte[] packet, int size, int limitTime) throws VMException {
    int curRec = 0;
    int off = 0;
    try {
      this.socket.setSoTimeout(limitTime);
    }
    catch (SocketException e1) {
      TestPrint.println(4, Thread.currentThread().getName() + " : Receiver -- Socket Excpetion:" + e1
          .getClass().getName());
    } 
    VMException Exception1 = new VMException(102);
    VMException Exception2 = new VMException(101);
    while (size > 0) {
      try {
        curRec = this.in.read(packet, off, size);
      }
      catch (InterruptedIOException interruptedioexception) {
        try {
          this.socket.setSoTimeout(0);
        }
        catch (SocketException e) {
          TestPrint.println(4, Thread.currentThread().getName() + " : Receiver -- Socket Excpetion:" + e
              .getClass().getName());
        } 
        if (0 == off)
        {
          return false;
        }
        continue;
      } catch (IOException e) {
        TestPrint.println(4, Thread.currentThread().getName() + " : Receiver -- IO Excpetion:" + e
            .getClass().getName());
        throw Exception1;
      } 
      if (curRec < 0)
      {
        throw Exception2;
      }
      off += curRec;
      size -= curRec;
    } 
    return true;
  }
  public void setOverTime(int overTime) {}
  public void receive(byte[] packet, int size) throws VMException {}
  public int receiveImmediate(byte[] packet, int size) throws VMException {
    return 0;
  }
}
