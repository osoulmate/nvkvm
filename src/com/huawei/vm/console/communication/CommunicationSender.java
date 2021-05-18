package com.huawei.vm.console.communication;
import com.huawei.vm.console.management.VMConsole;
import com.huawei.vm.console.utils.DataArray;
import com.huawei.vm.console.utils.DataElement;
import com.huawei.vm.console.utils.TestPrint;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
public class CommunicationSender
  implements Runnable
{
  private final DataArray sendArray;
  private VMTimerTask timerTask;
  private final VMConsole console;
  private final BufferedOutputStream out;
  private boolean exitFlag = false;
  private final Object lock = new Object();
  public VMConsole getConsole() {
    return this.console;
  }
  public CommunicationSender(Socket socket, VMConsole console) throws IOException {
    this.console = console;
    this.out = new BufferedOutputStream(socket.getOutputStream());
    this.sendArray = new DataArray();
    this.sendArray.initArrList(30, 10);
  }
  public void run() {
    DataElement curElement = null;
    byte[] dataBuffer = null;
    int dataLen = 0;
    while (!this.exitFlag) {
      try {
        curElement = this.sendArray.getAndRemoveFirstByBlock();
        if (null == curElement) {
          TestPrint.println(1, Thread.currentThread().getName() + " : Sender -- interrupted");
          continue;
        } 
        dataBuffer = curElement.getContent();
        dataLen = curElement.getContentLength();
        if (0 < dataLen)
        {
          this.out.write(dataBuffer, 0, dataLen);
          this.out.flush();
          resetHeartbit(dataBuffer);
          this.sendArray.release(dataBuffer);
        }
      }
      catch (IOException ioe) {
        this.console.errorProcess(0, 102);
        break;
      } 
    } 
  }
  public final boolean send(byte[] data, int dataLength) {
    synchronized (this.lock) {
      return this.sendArray.addLast(DataElement.getDataInstance(data, dataLength));
    } 
  }
  public byte[] getByteArr(int size) {
    return this.sendArray.getByteArr(size);
  }
  public final void sendImmediate(byte[] data, int dataLength) {
    synchronized (this.lock) {
      this.sendArray.addMore(DataElement.getDataInstance(data, dataLength));
    } 
  }
  public final void sendHeartbit() {
    byte[] heartBit = getByteArr(12);
    ProtocolProcessor.heartBitPak(heartBit, 0);
    DataElement heartBitElement = DataElement.getComPakInstance(heartBit, 12);
    synchronized (this.lock) {
      this.sendArray.addMore(heartBitElement);
    } 
  }
  public void setExit() {
    this.exitFlag = true;
  }
  public boolean getExit() {
    return this.exitFlag;
  }
  public void enableHeartbit(VMTimerTask task) {
    this.timerTask = task;
  }
  private void resetHeartbit(byte[] data) {
    if (null != this.timerTask && 12 == data.length && 6 != data[0])
    {
      this.timerTask.heartBitInit();
    }
  }
}
