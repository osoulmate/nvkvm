package com.huawei.vm.console.utilsV1;
public class VMException
  extends Exception
{
  private static final long serialVersionUID = -8502642749605159358L;
  private int key;
  private String msg;
  public static final int SOCKET_CLOSED = 101;
  public static final int SOCKET_NET_ERROR = 102;
  public static final int SOCKET_CANNOT_CONNECT = 103;
  public static final int SOCKET_UNKNOWN_HOST = 104;
  public static final int SOCKET_TIME_OUT = 105;
  public static final int DERVER_CDROM_FLOPPY = 401;
  public static final int ERR_INVALID_START_ARGUMENT = 110;
  public static final int ERR_CERTIFY_NO_RESPONSE = 121;
  public static final int ERR_DEVICE_NO_RESPONSE = 122;
  public static final int ERR_HEARTBIT_NO_RESPONSE = 123;
  public static final int PACKET_TOO_SMALL = 201;
  public static final int RECEIVE_OVER_TIME = 202;
  public static final int RECEIVE_SIZE_INCORRECT = 203;
  public static final int DLL_NOT_EXTRACT = 210;
  public static final int HAS_NO_PERMISSION = 211;
  public static final int DEVICE_OPEN_ERR = 220;
  public static final int DEVICE_CLOST_ERR = 221;
  public static final int FILE_END_REACHED = 222;
  public static final int DEVICE_ON_USING = 223;
  public static final int SENSE_ID_CRC_ERROR = 250;
  public static final int SENSE_LOGICAL_BLOCK_ADDRESS_OUT_OF_RANGE = 251;
  public static final int SENSE_INVALID_FIELD_IN_COMMAND_PACKET = 252;
  public static final int SENSE_MEDIA_NOT_PRESENT = 253;
  public static final int SENSE_WRITE_PROTECT = 254;
  public static final int NO_DEVICE_CREAT = 301;
  public static final int IMAGE_FILE_NOT_EXIST = 320;
  public static final int IMAGE_FILE_IS_DIRECTION = 321;
  public static final int IMAGE_FILE_CREATE_ERROR = 322;
  public static final int IMAGE_DISK_SPACE_NOT_ENOUGH = 323;
  public static final int IMAGE_DEVICE_UNSUPPORT = 324;
  public static final int IMAGE_DEVICE_READ_ERROR = 325;
  public static final int IMAGE_FILE_ON_USING = 326;
  public static final int IMAGE_FILE_OPEN_ERROR = 327;
  public static final int DEVICE_HAS_NO_MEDIUM = 331;
  public static final int IMAGE_USER_STOP = 332;
  public static final int DEVICE_NAME_IS_NULL = 333;
  public static final int IMAGE_NAME_IS_NULL = 334;
  public static final int IMAGE_FILE_CAPACITY_ERROR = 335;
  public static final int FILE_OR_DIR_NOT_EXISTS = 336;
  public static final int OPEN_LOCALDIR_FAILED = 337;
  public static final int LOCALDIR_FILE_NUM_EXCEED_LIMIT = 338;
  public VMException() {}
  public VMException(int key) {
    super(String.valueOf(key));
    this.key = key;
  }
  public VMException(int key, String msg) {
    super(msg);
    this.key = key;
    this.msg = msg;
  }
  public int getKey() {
    return this.key;
  }
  public void setKey(int key) {
    this.key = key;
  }
  public String getMsg() {
    return this.msg;
  }
  public void setMsg(String msg) {
    this.msg = msg;
  }
}
