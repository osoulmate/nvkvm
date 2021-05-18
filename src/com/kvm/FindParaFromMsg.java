package com.kvm;
class FindParaFromMsg
{
  String tempInput = "";
  int startCount = 0;
  int endCount = 0;
  int addStartCount = 0;
  int addEndCount = 0;
  int[] indexStartArray = new int[8];
  int[] indexEndArray = new int[8];
  int[] addIndexStartArray = new int[20];
  int[] addIndexEndArray = new int[20];
  public void decodeJsonValue(String input) {
    this.tempInput = input;
    int InputLength = input.length();
    for (int index = 0; index < InputLength; index++) {
      if (input.charAt(index) == '[') {
        this.indexStartArray[this.startCount] = index;
        this.startCount++;
      }
      else if (input.charAt(index) == ']') {
        this.indexEndArray[this.endCount] = index;
        this.endCount++;
      } 
    } 
  }
  public void decodeAddValue(String input) {
    this.tempInput = input;
    int InputLength = input.length();
    for (int index = 0; index < InputLength; index++) {
      if (input.charAt(index) == '<') {
        this.addIndexStartArray[this.addStartCount] = index;
        this.addStartCount++;
      }
      else if (input.charAt(index) == '>') {
        this.addIndexEndArray[this.addEndCount] = index;
        this.addEndCount++;
      } 
    } 
  }
  public String findErrorCode() {
    String ErrorCode = this.tempInput.substring(this.indexStartArray[0] + 1, this.indexEndArray[0]);
    return ErrorCode;
  }
  public String findVerifyValue() {
    String VerifyValue = this.tempInput.substring(this.indexStartArray[1] + 1, this.indexEndArray[1]);
    return VerifyValue;
  }
  public String findDecrykey() {
    String Decrykey = this.tempInput.substring(this.indexStartArray[2] + 2, this.indexEndArray[2] - 1);
    return Decrykey;
  }
  public String findPrivilege() {
    String Privilege = this.tempInput.substring(this.indexStartArray[3] + 1, this.indexEndArray[3]);
    return Privilege;
  }
  public String findCompress() {
    String Compress = this.tempInput.substring(this.indexStartArray[4] + 1, this.indexEndArray[4]);
    return Compress;
  }
  public String findVmmCompress() {
    String VmmCompress = this.tempInput.substring(this.indexStartArray[5] + 1, this.indexEndArray[5]);
    return VmmCompress;
  }
  public String findKvmPort() {
    String KvmPort = this.tempInput.substring(this.indexStartArray[6] + 1, this.indexEndArray[6]);
    return KvmPort;
  }
  public String findVmmPort() {
    String VmmPort = this.tempInput.substring(this.indexStartArray[7] + 1, this.indexEndArray[7]);
    return VmmPort;
  }
  public String findSerialNumber() {
    if (this.addStartCount < 1)
    {
      return "";
    }
    String SerialNumber = this.tempInput.substring(this.addIndexStartArray[0] + 1, this.addIndexEndArray[0]);
    return SerialNumber;
  }
  public String findVerifyValueExt() {
    if (this.addStartCount < 2)
    {
      return "";
    }
    String VerifyValueExt = this.tempInput.substring(this.addIndexStartArray[1] + 1, this.addIndexEndArray[1]);
    return VerifyValueExt;
  }
}
