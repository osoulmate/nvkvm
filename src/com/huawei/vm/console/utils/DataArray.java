package com.huawei.vm.console.utils;
import com.huawei.vm.console.communication.ProtocolCode;
import java.util.LinkedList;
import java.util.NoSuchElementException;
public class DataArray
{
  private final LinkedList<DataElement> linkedList = new LinkedList<>();
  private final LinkedList<byte[]> bigArrList = (LinkedList)new LinkedList<>();
  private final LinkedList<byte[]> smallArrList = (LinkedList)new LinkedList<>();
  private final int bigArrSize = ProtocolCode.CDROM_PACKET_SIZE + 12 + 20;
  private static final int smallArrSize = 12;
  private int capacity = 256;
  private int elementNumber = 0;
  private int totalNum = 0;
  private int cmdCount = 0;
  private final Object blockLock = new Object();
  private final Object arrBlockLock = new Object();
  public DataArray() {}
  public DataArray(int capacity) {
    if (capacity > 0)
    {
      this.capacity = capacity;
    }
  }
  public void initArrList(int bigArrCap, int smallArrCap) {
    for (int i = 0; i < bigArrCap; i++)
    {
      this.bigArrList.addLast(new byte[this.bigArrSize]);
    }
    for (int j = 0; j < smallArrCap; j++)
    {
      this.smallArrList.addLast(new byte[12]);
    }
  }
  public DataElement getFirst() {
    DataElement data = null;
    try {
      data = this.linkedList.getFirst();
    }
    catch (NoSuchElementException e) {
      data = null;
    } 
    return data;
  }
  public DataElement getAndRemoveFirst() {
    DataElement data = null;
    try {
      synchronized (this.blockLock)
      {
        data = this.linkedList.removeFirst();
        this.elementNumber--;
      }
    } catch (NoSuchElementException e) {
      data = null;
    } 
    return data;
  }
  public DataElement getAndRemoveFirstByBlock() {
    DataElement data = null;
    synchronized (this.blockLock) {
      while (true) {
        try {
          data = this.linkedList.removeFirst();
          this.elementNumber--;
          break;
        } catch (NoSuchElementException e) {
          try {
            this.blockLock.wait();
          }
          catch (InterruptedException e1) {
            break;
          } 
        } 
      } 
    } 
    return data;
  }
  public boolean addLast(DataElement element) {
    boolean result = false;
    if (this.elementNumber >= this.capacity) {
      result = false;
    }
    else {
      if (null != element)
      {
        synchronized (this.blockLock) {
          this.linkedList.addLast(element);
          this.elementNumber++;
          this.blockLock.notifyAll();
        } 
      }
      result = true;
    } 
    return result;
  }
  public void addMore(DataElement element) {
    if (null != element)
    {
      synchronized (this.blockLock) {
        this.linkedList.addLast(element);
        this.elementNumber++;
        this.blockLock.notifyAll();
      } 
    }
  }
  public int getCapacity() {
    return this.capacity;
  }
  public int getDataNum() {
    return this.linkedList.size();
  }
  public byte[] getByteArr(int arrLen) {
    TestPrint.println(1, Thread.currentThread().getName() + "--Get a arr. Total -" + ++this.totalNum);
    byte[] arr = null;
    synchronized (this.arrBlockLock) {
      try {
        if (12 < arrLen)
        {
          arr = this.bigArrList.removeFirst();
        }
        else
        {
          arr = this.smallArrList.removeFirst();
        }
      }
      catch (NoSuchElementException e) {
        if (12 < arrLen) {
          arr = new byte[this.bigArrSize];
        }
        else {
          arr = new byte[12];
        } 
      } 
    } 
    return arr;
  }
  public void release(byte[] arr) {
    if (null != arr)
    {
      synchronized (this.arrBlockLock) {
        for (int i = 0; i < arr.length; i++)
        {
          arr[i] = 0;
        }
        if (12 == arr.length) {
          this.smallArrList.addLast(arr);
        }
        else if (this.bigArrSize == arr.length) {
          this.bigArrList.addLast(arr);
        } 
      } 
    }
  }
  public int getBigArrNum() {
    return this.bigArrList.size();
  }
  public int getSmallArrNum() {
    return this.smallArrList.size();
  }
  public int getElementNumber() {
    return this.elementNumber;
  }
  public void setElementNumber(int elementNumber) {
    this.elementNumber = elementNumber;
  }
  public void setCmdCount(int cmdCount) {
    this.cmdCount = cmdCount;
  }
  public int getCmdCount() {
    return this.cmdCount;
  }
}
