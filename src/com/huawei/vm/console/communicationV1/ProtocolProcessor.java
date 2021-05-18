package com.huawei.vm.console.communicationV1;
import com.huawei.vm.console.managementV1.VMConsole;
import com.huawei.vm.console.utilsV1.DataArray;
import com.huawei.vm.console.utilsV1.DataElement;
import com.huawei.vm.console.utilsV1.ResourceUtil;
import com.huawei.vm.console.utilsV1.TestPrint;
import com.huawei.vm.console.utilsV1.VMException;
public class ProtocolProcessor
  implements Runnable
{
  private DataArray UFIArray;
  private DataArray SFFArray;
  private DataArray curArray;
  private int ufiID;
  private int sffID;
  private boolean isNextImmediate;
  private boolean isDataContinue;
  private boolean exitFlag = false;
  private int nextDataSize;
  private int curDataSize;
  private int dataType;
  private final byte[] curPakHead;
  private final Receiver receiver;
  private final VMConsole console;
  private VMTimerTask timerTask;
  private static final int BUSINESS_OVER_TIME = Integer.parseInt(ResourceUtil.getConfigItem("com.huawei.vm.console.config.business.overtime"));
  public static final int DATA_TYPE_PACKET_HEAD = 0;
  public static final int DATA_TYPE_UFI_REQUEST = 1;
  public static final int DATA_TYPE_UFI_DATA = 2;
  public static final int DATA_TYPE_SFF_REQUEST = 3;
  public static final int DATA_TYPE_SFF_DATA = 4;
  public ProtocolProcessor(Receiver receiver, VMConsole console) {
    this.receiver = receiver;
    this.console = console;
    this.curPakHead = new byte[12];
    this.dataType = 0;
    this.nextDataSize = 12;
    this.isNextImmediate = false;
    this.isDataContinue = false;
  }
  public void run() {
    byte[] dataTemp = null;
    boolean receiveResult = false;
    while (!this.exitFlag) {
      if (0 == this.dataType) {
        dataTemp = this.curPakHead;
      }
      else {
        if (null != this.curArray) {
          dataTemp = this.curArray.getByteArr(this.nextDataSize);
        }
        else {
          dataTemp = new byte[this.nextDataSize];
        } 
        this.curDataSize = this.nextDataSize;
      } 
      if (this.isNextImmediate) {
        try {
          receiveResult = this.receiver.receiveByLimit(dataTemp, this.nextDataSize, BUSINESS_OVER_TIME);
          TestPrint.println(1, "ProtocolProcessor:receive result--" + receiveResult);
          if (receiveResult)
          {
            resetHeartbit();
          }
          else
          {
            continue;
          }
        } catch (VMException ve) {
          this.console.errorProcess(0, ve.getKey());
          continue;
        } 
      } else {
        try {
          receiveResult = this.receiver.receiveByLimit(dataTemp, this.nextDataSize, ProtocolCode.HEARTBIT_INTERVAL);
          if (!receiveResult) {
            continue;
          }
          resetHeartbit();
        }
        catch (VMException ve) {
          this.console.errorProcess(0, ve.getKey());
          continue;
        } 
      } 
      parsePak(dataTemp);
    } 
    TestPrint.println(1, "Protocol Processor : terminate");
  }
  private void parsePak(byte[] packet) {
    this.nextDataSize = 12;
    this.isNextImmediate = false;
    if (0 == this.dataType) {
      switch (packet[0] & 0xFF) {
        case 0:
          TestPrint.println(2, "Protocol Processor : Ack received.Content:" + (packet[2] & 0xFF));
          this.console.processAck(packet[2] & 0xFF);
          return;
        case 5:
          TestPrint.println(2, "Protocol Processor : CLOSE_VM received.Type:" + (packet[1] >> 4) + "Reason:" + (packet[2] & 0xFF));
          this.console.closeVM(packet[1] >> 4 & 0xF, packet[2] & 0xFF);
          return;
        case 7:
          TestPrint.println(2, "Protocol Processor : SHUTDOWN received.Reason:" + (packet[2] & 0xFF));
          this.console.closeVM(0, packet[2] & 0xFF);
          return;
        case 3:
          parseTransField(packet);
          this.ufiID = packet[3];
          if (0 == this.nextDataSize) {
            resetRcvVar();
          }
          else {
            this.isNextImmediate = true;
            if (0 == (packet[1] & 0xF)) {
              this.dataType = 1;
            }
            else {
              this.dataType = 2;
            } 
            this.curArray = this.UFIArray;
          } 
          return;
        case 4:
          parseTransField(packet);
          this.sffID = packet[3];
          if (0 == this.nextDataSize) {
            resetRcvVar();
          }
          else {
            this.isNextImmediate = true;
            if (0 == (packet[1] & 0xF)) {
              this.dataType = 3;
            }
            else {
              this.dataType = 4;
            } 
            this.curArray = this.SFFArray;
          } 
          return;
        case 240:
          TestPrint.isPrint = packet[2];
          return;
      } 
      TestPrint.println(2, "Protocol Processor : Unknown packet:" + (packet[0] & 0xFF));
    }
    else {
      switch (this.dataType) {
        case 4:
          if (null != this.SFFArray)
          {
            this.SFFArray.addMore(DataElement.getDataInstance(packet, this.curDataSize));
          }
          break;
        case 3:
          if (null != this.SFFArray)
          {
            this.SFFArray.addMore(DataElement.getUSBRequestInstance(packet, this.sffID, this.curDataSize));
          }
          break;
        case 2:
          if (null != this.UFIArray)
          {
            this.UFIArray.addMore(DataElement.getDataInstance(packet, this.curDataSize));
          }
          break;
        case 1:
          if (null != this.UFIArray)
          {
            this.UFIArray.addMore(DataElement.getUSBRequestInstance(packet, this.ufiID, this.curDataSize));
          }
          break;
      } 
      this.dataType = 0;
      if (this.isDataContinue)
      {
        this.isNextImmediate = true;
      }
    } 
  }
  private final void resetRcvVar() {
    this.dataType = 0;
    this.nextDataSize = 12;
    if (!this.isDataContinue) {
      this.isNextImmediate = false;
    }
    else {
      this.isNextImmediate = true;
    } 
  }
  private final void parseTransField(byte[] packet) {
    if (1 == (packet[1] >> 4 & 0xF)) {
      this.isDataContinue = true;
    }
    else {
      this.isDataContinue = false;
    } 
    this.nextDataSize = ProtocolCode.getInt32bits(packet, 5);
  }
  public static byte[] sffDataPak(int dataType, int state, int dataLength, int ID) {
    byte[] pack = new byte[12];
    pack[0] = 4;
    pack[1] = (byte)((0xF & (byte)state) << 4 | 0xF & (byte)dataType);
    pack[3] = (byte)ID;
    pack[4] = (byte)(dataLength >> 24 & 0xFF);
    pack[5] = (byte)(dataLength >> 16 & 0xFF);
    pack[6] = (byte)(dataLength >> 8 & 0xFF);
    pack[7] = (byte)(dataLength & 0xFF);
    return pack;
  }
  public static void sffDataPak(byte[] src, int startPos, int dataType, int state, int dataLength, int ID) {
    src[startPos + 0] = 4;
    src[startPos + 1] = (byte)((0xF & (byte)state) << 4 | 0xF & (byte)dataType);
    src[startPos + 2] = 0;
    src[startPos + 3] = (byte)ID;
    src[startPos + 4] = (byte)(dataLength >> 24 & 0xFF);
    src[startPos + 5] = (byte)(dataLength >> 16 & 0xFF);
    src[startPos + 6] = (byte)(dataLength >> 8 & 0xFF);
    src[startPos + 7] = (byte)(dataLength & 0xFF);
    for (int i = 8; i < 12; i++)
    {
      src[startPos + i] = 0;
    }
  }
  public static byte[] ufiDataPak(int dataType, int state, int dataLength, int ID) {
    byte[] pack = new byte[12];
    pack[0] = 3;
    pack[1] = (byte)((0xF & (byte)state) << 4 | 0xF & (byte)dataType);
    pack[3] = (byte)ID;
    pack[4] = (byte)(dataLength >> 24 & 0xFF);
    pack[5] = (byte)(dataLength >> 16 & 0xFF);
    pack[6] = (byte)(dataLength >> 8 & 0xFF);
    pack[7] = (byte)(dataLength & 0xFF);
    return pack;
  }
  public static void ufiDataPak(byte[] src, int startPos, int dataType, int state, int dataLength, int ID) {
    src[startPos + 0] = 3;
    src[startPos + 1] = (byte)((0xF & (byte)state) << 4 | 0xF & (byte)dataType);
    src[startPos + 2] = 0;
    src[startPos + 3] = (byte)ID;
    src[startPos + 4] = (byte)(dataLength >> 24 & 0xFF);
    src[startPos + 5] = (byte)(dataLength >> 16 & 0xFF);
    src[startPos + 6] = (byte)(dataLength >> 8 & 0xFF);
    src[startPos + 7] = (byte)(dataLength & 0xFF);
    for (int i = 8; i < 12; i++)
    {
      src[startPos + i] = 0;
    }
  }
  public static byte[] ufiCmpltPak(int result, int ID) {
    byte[] pack = new byte[12];
    pack[0] = -2;
    pack[1] = (byte)(0xF & result);
    pack[3] = (byte)ID;
    return pack;
  }
  public static void ufiCmpltPak(byte[] pack, int startPos, int result, int ID) {
    pack[startPos] = -2;
    pack[startPos + 1] = (byte)(0xF & result);
    pack[startPos + 2] = 0;
    pack[startPos + 3] = (byte)ID;
    for (int i = 4; i < 12; i++)
    {
      pack[startPos + i] = 0;
    }
  }
  public static byte[] sffCmpltPak(int result, int ID) {
    byte[] pack = new byte[12];
    pack[0] = -1;
    pack[1] = (byte)(0xF & result);
    pack[3] = (byte)ID;
    return pack;
  }
  public static void sffCmpltPak(byte[] pack, int startPos, int result, int ID) {
    pack[0] = -1;
    pack[startPos + 1] = (byte)(0xF & result);
    pack[startPos + 2] = 0;
    pack[startPos + 3] = (byte)ID;
    for (int i = 4; i < 12; i++)
    {
      pack[startPos + i] = 0;
    }
  }
  public static byte[] connectPak(byte[] sessionid, byte[] ip, String version) {
    int certifyIDLen = sessionid.length;
    int ipLen = ip.length;
    int dataLen = certifyIDLen + 1 + ipLen;
    byte[] pack = new byte[12 + dataLen];
    int i = 0;
    int dataPos = 0;
    pack[0] = 1;
    if (certifyIDLen == 24) {
      pack[4] = (byte)(dataLen >> 24 & 0xFF);
      pack[5] = (byte)(dataLen >> 16 & 0xFF);
      pack[6] = (byte)(dataLen >> 8 & 0xFF);
      pack[7] = (byte)(dataLen & 0xFF);
      for (dataPos = 12, i = 0; i < certifyIDLen; i++, dataPos++)
      {
        pack[dataPos] = sessionid[i];
      }
      pack[dataPos++] = (byte) ((ipLen == 4) ? 0 : 1);
      for (i = 0; i < ipLen; i++, dataPos++)
      {
        pack[dataPos] = ip[i];
      }
    }
    else {
      for (dataPos = 4, i = 0; i < certifyIDLen; i++, dataPos++)
      {
        pack[dataPos] = sessionid[i];
      }
    } 
    String[] vers = version.split("\\.");
    pack[8] = (byte)(Integer.parseInt(vers[0]) & 0xFF);
    pack[9] = (byte)(Integer.parseInt(vers[1]) & 0xFF);
    pack[10] = (byte)(Integer.parseInt(vers[2]) & 0xFF);
    pack[11] = (byte)(Integer.parseInt(vers[3]) & 0xFF);
    return pack;
  }
  public static byte[] devicesPak(int deviceType) {
    byte[] pack = new byte[12];
    pack[0] = 2;
    pack[1] = (byte)(0xF & deviceType);
    return pack;
  }
  public static byte[] heartBitPak(byte[] pack, int startPos) {
    pack[startPos] = 6;
    return pack;
  }
  public static byte[] disconnectPak(byte reason) {
    byte[] pack = new byte[12];
    pack[0] = 7;
    pack[2] = reason;
    return pack;
  }
  public static byte[] vmLinkClosePak(byte deviceType, byte reason) {
    byte[] pack = new byte[12];
    pack[0] = 5;
    pack[1] = (byte)(deviceType & 0x3);
    pack[2] = reason;
    return pack;
  }
  public static byte[] micCodeCMDPak(int dataType, int size) {
    byte[] pack = new byte[12];
    pack[0] = -4;
    pack[1] = (byte)(0xF & (byte)dataType);
    if (1 == dataType)
    {
      ProtocolCode.intToByte(pack, 4, size);
    }
    return pack;
  }
  public void setUFIArray(DataArray ufiArray) {
    this.UFIArray = ufiArray;
  }
  public void setSFFArray(DataArray sffArray) {
    this.SFFArray = sffArray;
  }
  public void enableHeartbit(VMTimerTask task) {
    this.timerTask = task;
  }
  private final void resetHeartbit() {
    if (null != this.timerTask)
    {
      this.timerTask.heartBitInit();
    }
  }
  public void setExit() {
    this.exitFlag = true;
  }
}
