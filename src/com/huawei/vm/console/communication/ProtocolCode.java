package com.huawei.vm.console.communication;
import com.huawei.vm.console.utils.ResourceUtil;
public class ProtocolCode
{
  public static final int PACKET_HEAD_SIZE = 12;
  public static final int IP_TYPE_SIZE = 1;
  public static final int IPV4_SIZE = 4;
  public static final int SECRET_CERTIFYID_SIZE = 24;
  public static final byte ACK = 0;
  public static final byte ACK_CERTIFY_PASS = 0;
  public static final byte ACK_CERTIFY_ID_FAIL = 1;
  public static final byte ACK_CERTIFY_VER_NOTSUP = 2;
  public static final byte ACK_DEVICE_CREAT = 16;
  public static final byte ACK_DEVICE_FAIL_ENUM = 17;
  public static final byte ACK_CLOSE_UPDATA = 34;
  public static final byte ACK_CLOSE_IPCONFIG = 35;
  public static final int CN_EXIST = 49;
  public static final byte CERTIFY_ID = 1;
  public static final int CERTIFY_ID_POSITION = 5;
  public static final int VERTION_POSITION = 9;
  public static final byte DEVICE_TYPE = 2;
  public static final int DEVICE_TYPE_FLOPPY = 1;
  public static final int DEVICE_TYPE_CDROM = 2;
  public static final byte UFI_DATA = 3;
  public static final byte SFF_DATA = 4;
  public static final byte UFI_SFF_DATA_COMMAND = 0;
  public static final byte UFI_SFF_DATA_DATA = 1;
  public static final byte UFI_SFF_DATA_CONTINUE = 1;
  public static final byte UFI_SFF_DATA_END = 3;
  public static final byte CLOSE_VM = 5;
  public static final byte CLOSE_VM_TYPE_LINK = 0;
  public static final byte CLOSE_VM_TYPE_FLOPPY = 1;
  public static final byte CLOSE_VM_TYPE_CDROM = 2;
  public static final byte HEARTBIT = 6;
  public static final byte SHUTDOWN = 7;
  public static final byte UFI_COMMAND_COMPLETE = -2;
  public static final byte SFF_COMMAND_COMPLETE = -1;
  public static final byte CONSOLE_PRINT_CONTROLLER = -16;
  public static final byte UFI_SFF_CMD_OK = 0;
  public static final byte UFI_SFF_CMD_FAIL = 1;
  public static final byte MIC_FILE_CMD = -4;
  public static final int CDROM_PACKET_SIZE = Integer.parseInt(ResourceUtil.getConfigItem("com.huawei.vm.console.config.cdrom.datapacket.size"));
  public static final int FLOPPY_PACKET_SIZE = Integer.parseInt(ResourceUtil.getConfigItem("com.huawei.vm.console.config.floppy.datapacket.size"));
  public static final int HEARTBIT_INTERVAL = Integer.parseInt(ResourceUtil.getConfigItem("com.huawei.vm.console.config.heartBit.interval"));
  public static final int HEARTBIT_OVERTIME = 3;
  public static int getInt32bits(byte[] byteArr, int position) {
    byte byte1 = byteArr[position - 1];
    byte byte2 = byteArr[position + 0];
    byte byte3 = byteArr[position + 1];
    return (byte1 & 0xFF) << 24 | (byte2 & 0xFF) << 16 | (byte3 & 0xFF) << 8 | byteArr[position + 2] & 0xFF;
  }
  public static int getInt24bits(byte[] byteArr, int position) {
    byte byte1 = byteArr[position - 1];
    byte byte2 = byteArr[position + 0];
    return (byte1 & 0xFF) << 16 | (byte2 & 0xFF) << 8 | byteArr[position + 1] & 0xFF;
  }
  public static int getInt16bits(byte[] byteArr, int position) {
    byte byte1 = byteArr[position - 1];
    return (byte1 & 0xFF) << 8 | byteArr[position] & 0xFF;
  }
  public static void intToByte(byte[] bytedest, int offset, int intsrc) {
    bytedest[offset + 3] = (byte)intsrc;
    bytedest[offset + 2] = (byte)(intsrc >> 8);
    bytedest[offset + 1] = (byte)(intsrc >> 16);
    bytedest[offset] = (byte)(intsrc >> 24);
  }
}
