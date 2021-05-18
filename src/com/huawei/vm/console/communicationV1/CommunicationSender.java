package com.huawei.vm.console.communicationV1;
import com.huawei.vm.console.managementV1.VMConsole;
import com.huawei.vm.console.utilsV1.DataArray;
import com.huawei.vm.console.utilsV1.DataElement;
import com.huawei.vm.console.utilsV1.TestPrint;
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
  public CommunicationSender(Socket socket, VMConsole console) throws IOException {
    this.console = console;
    this.out = new BufferedOutputStream(socket.getOutputStream());
    this.sendArray = new DataArray();
    this.sendArray.initArrList(30, 10);
  }
  public void run() {
    while (!this.exitFlag) {
      try {
        DataElement curElement = this.sendArray.getAndRemoveFirstByBlock();
        if (null == curElement) {
          TestPrint.println(1, Thread.currentThread().getName() + " : Sender -- interrupted");
          continue;
        } 
        byte[] dataBuffer = curElement.getContent();
        int dataLen = curElement.getContentLength();
        if (null != dataBuffer && 0 < dataLen)
        {
          this.out.write(dataBuffer, 0, dataLen);
          this.out.flush();
          resetHeartbit(dataBuffer);
          TestPrint.printArray(2, dataBuffer, 0, dataLen, Thread.currentThread().getName() + " : Sender --packet");
          this.sendArray.release(dataBuffer);
        }
      }
      catch (IOException ioe) {
        TestPrint.println(4, Thread.currentThread().getName() + " : Sender -- IOException:" + ioe.getMessage());
        this.console.errorProcess(0, 102);
        break;
      } 
    } 
    TestPrint.println(1, Thread.currentThread().getName() + " : Sender -- Terminate. big arr num:" + this.sendArray.getBigArrNum() + "; small arr num:" + this.sendArray.getSmallArrNum() + "; data num:" + this.sendArray.getDataNum());
  }
  public final boolean send(byte[] data, int dataLength) {
    synchronized (this) {
      return this.sendArray.addLast(DataElement.getDataInstance(data, dataLength));
    } 
  }
  public byte[] getByteArr(int size) {
    return this.sendArray.getByteArr(size);
  }
  public final void sendImmediate(byte[] data, int dataLength) {
    synchronized (this) {
      this.sendArray.addMore(DataElement.getDataInstance(data, dataLength));
    } 
  }
  public final void sendHeartbit() {
    byte[] heartBit = getByteArr(12);
    ProtocolProcessor.heartBitPak(heartBit, 0);
    DataElement heartBitElement = DataElement.getComPakInstance(heartBit, 12);
    synchronized (this) {
      this.sendArray.addMore(heartBitElement);
    } 
  }
  public void setExit() {
    this.exitFlag = true;
  }
  public void enableHeartbit(VMTimerTask task) {
    this.timerTask = task;
  }
  public DataArray getArray() {
    return this.sendArray;
  }
  private final void resetHeartbit(byte[] data) {
    if (null != this.timerTask && 12 == data.length && 6 != data[0])
    {
      this.timerTask.heartBitInit();
    }
  }
}
