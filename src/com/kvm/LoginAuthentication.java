package com.kvm;
import com.library.LoggerUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
class LoginAuthentication
{
  public static final String LOAD_SHARE_LIBRARY = "export LD_LIBRARY_PATH=`pwd`";
  public static final String LAN_INTERFACE = "-I lanplus";
  public static final String IPMI_REQUEST_PARAM = "raw 0x30 0x94 0xDB 0x07 0x00 0x21 0x06 0x00 0x00 0x37";
  private int keyNumber;
  public static final String IPMI_REQUEST_PARAM_KVM_IBMC = "raw 0x30 0x93 0xDB 0x07 0x00 0x38 0x0a 0x00 0x01 0xff 0x00 0x00 0x01 0x00";
  public static final String IPMI_REQUEST_PARAM_KVM_IMANA = "raw 0x30 0x93 0xDB 0x07 0x00 0x10 0x04 0x02 0x01 0x00 0x00";
  public static final String IPMI_REQUEST_PARAM_VMM_IBMC = "raw 0x30 0x93 0xDB 0x07 0x00 0x38 0x0b 0x00 0x01 0xff 0x00 0x00 0x01 0x00";
  public static final String IPMI_REQUEST_PARAM_VMM_IMANA = "raw 0x30 0x93 0xDB 0x07 0x00 0x10 0x04 0x02 0x02 0x00 0x00";
  public static final String IPMI_REQUEST_PARAM_ENCRY_INFO = "raw 0x30 0x94 0xDB 0x07 0x00 0x20";
  public static final String IPMI_REQUEST_PARAM_OFFSET_LENGTH = "0x00 0xff";
  public int getKeyNumber() {
    return this.keyNumber;
  }
  public void setKeyNumber(int keyNumber) {
    this.keyNumber = keyNumber;
  }
  public String doLoginAuth(String host, String username, String passWord, String KvmMode, int port, LoginUtil loginUtil) throws Exception {
    String returnFlag = "gologin";
    String hexUsername = "";
    String hexKvmMode = "";
    String key = "";
    StringBuffer ipmiOEM = new StringBuffer();
    hexUsername = stringToHex(username);
    hexKvmMode = "0x0" + Integer.toHexString(Integer.parseInt(KvmMode));
    byte[] keyByteArray = getRandomAndToByteArray();
    StringBuffer keyArray = new StringBuffer();
    for (int i = 0; i < keyByteArray.length; i++)
    {
      keyArray.append("0x0").append(keyByteArray[i]).append(" ");
    }
    key = keyArray.toString();
    ipmiOEM.append("-H").append(" ")
      .append(host)
      .append(" ")
      .append("-p")
      .append(" ")
      .append(String.valueOf(port))
      .append(" ")
      .append("-I lanplus")
      .append(" ")
      .append("-U")
      .append(" ")
      .append(username)
      .append(" ")
      .append("-P")
      .append(" ")
      .append(passWord)
      .append(" ")
      .append("raw 0x30 0x94 0xDB 0x07 0x00 0x21 0x06 0x00 0x00 0x37")
      .append(" ")
      .append("0x00 0x24 ")
      .append(key)
      .append("0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 ")
      .append("0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 ")
      .append(hexUsername)
      .append("0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 ")
      .append(hexKvmMode);
    LoggerUtil.info( "ipmiOEM: "+ ipmiOEM );
    Process process = null;
    SendIPMIInDiffOS sendIPMIInDiffOS = new SendIPMIInDiffOS();
    if (sendIPMIInDiffOS.useIpmitoolInDiffOS(ipmiOEM.toString()) == null)
    {
      return "gologin";
    }
    process = sendIPMIInDiffOS.useIpmitoolInDiffOS(ipmiOEM.toString());
    InputStream is = process.getInputStream();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(is, "utf-8"));
      String successMsg = null;
      while ((successMsg = br.readLine()) != null) {
        String outPutMsg = new String(successMsg.getBytes("utf-8"), "utf-8");
        returnFlag = "success";
      } 
      return returnFlag;
    }
    catch (IOException e) {
      throw new IOException(e);
    }
    finally {
      if (null != br) {
        try {
          br.close();
        }
        catch (IOException e) {
          LoggerUtil.error("br.close() failed!");
        } 
      }
    } 
  }
  public String getKVMPort(String host, String username, String passWord, int port, int firmVersion, LoginUtil loginUtil) throws Exception {
    String kvmPort = "fal";
    StringBuffer ipmiOEM = new StringBuffer();
    ipmiOEM.append("-I lanplus").append(" ")
      .append("-H")
      .append(" ")
      .append(host)
      .append(" ")
      .append("-p")
      .append(" ")
      .append(port)
      .append(" ")
      .append("-U")
      .append(" ")
      .append(username)
      .append(" ")
      .append("-P")
      .append(" ")
      .append(passWord)
      .append(" ");
    if (firmVersion == 0) {
      ipmiOEM.append("raw 0x30 0x93 0xDB 0x07 0x00 0x10 0x04 0x02 0x01 0x00 0x00");
    }
    else if (firmVersion > 0) {
      ipmiOEM.append("raw 0x30 0x93 0xDB 0x07 0x00 0x38 0x0a 0x00 0x01 0xff 0x00 0x00 0x01 0x00");
    } 
    LoggerUtil.info( "ipmiOEM: "+ ipmiOEM );
    Process process = null;
    SendIPMIInDiffOS sendIPMIInDiffOS = new SendIPMIInDiffOS();
    if (sendIPMIInDiffOS.useIpmitoolInDiffOS(ipmiOEM.toString()) == null)
    {
      return "fal";
    }
    process = sendIPMIInDiffOS.useIpmitoolInDiffOS(ipmiOEM.toString());
    InputStream is = process.getInputStream();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(is, "utf-8"));
      String successMsg = null;
      List<String> respMsg = new ArrayList<>();
      while ((successMsg = br.readLine()) != null) {
        String outPutMsg = new String(successMsg.getBytes("utf-8"), "utf-8");
        respMsg.add(outPutMsg);
      } 
      if (respMsg.size() > 0) {
        String result = ((String)respMsg.get(respMsg.size() - 1)).replace(" ", "");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < result.length() - 1; i += 2) {
          list.add(result.substring(i, i + 2));
        }
        StringBuffer portMessage = new StringBuffer();
        if (firmVersion == 0) {
          portMessage.append(list.get(list.size() - 1)).append(list.get(list.size() - 2));
        }
        else if (firmVersion > 0) {
          portMessage.append(list.get(list.size() - 1)).append(list.get(list.size() - 2))
            .append(list.get(list.size() - 3)).append(list.get(list.size() - 4));
        } 
        Integer temp = Integer.valueOf(Integer.parseInt(portMessage.toString(), 16));
        kvmPort = String.valueOf(temp);
      } 
      return kvmPort;
    }
    catch (IOException e) {
      throw new IOException(e);
    }
    finally {
      if (null != br) {
        try {
          br.close();
        }
        catch (IOException e) {
          LoggerUtil.error("br.close() failed!");
        } 
      }
    } 
  }
  public String getVMMPort(String host, String username, String passWord, int port, int firmVersion, LoginUtil loginUtil) throws Exception {
    String vmmPort = "fal";
    StringBuffer ipmiOEM = new StringBuffer();
    ipmiOEM.append("-I lanplus").append(" ")
      .append("-H")
      .append(" ")
      .append(host)
      .append(" ")
      .append("-p")
      .append(" ")
      .append(port)
      .append(" ")
      .append("-U")
      .append(" ")
      .append(username)
      .append(" ")
      .append("-P")
      .append(" ")
      .append(passWord)
      .append(" ");
    if (firmVersion == 0) {
      ipmiOEM.append("raw 0x30 0x93 0xDB 0x07 0x00 0x10 0x04 0x02 0x02 0x00 0x00");
    }
    else if (firmVersion > 0) {
      ipmiOEM.append("raw 0x30 0x93 0xDB 0x07 0x00 0x38 0x0b 0x00 0x01 0xff 0x00 0x00 0x01 0x00");
    } 
    LoggerUtil.info( "ipmiOEM: "+ ipmiOEM );
    Process process = null;
    SendIPMIInDiffOS sendIPMIInDiffOS = new SendIPMIInDiffOS();
    if (sendIPMIInDiffOS.useIpmitoolInDiffOS(ipmiOEM.toString()) == null)
    {
      return "fal";
    }
    process = sendIPMIInDiffOS.useIpmitoolInDiffOS(ipmiOEM.toString());
    InputStream is = process.getInputStream();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(is, "utf-8"));
      String successMsg = null;
      List<String> respMsg = new ArrayList<>();
      while ((successMsg = br.readLine()) != null) {
        String outPutMsg = new String(successMsg.getBytes("utf-8"), "utf-8");
        respMsg.add(outPutMsg);
      } 
      if (respMsg.size() > 0) {
        String result = ((String)respMsg.get(respMsg.size() - 1)).replace(" ", "");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < result.length() - 1; i += 2) {
          list.add(result.substring(i, i + 2));
        }
        StringBuffer portMessage = new StringBuffer();
        if (firmVersion == 0) {
          portMessage.append(list.get(list.size() - 1)).append(list.get(list.size() - 2));
        }
        else if (firmVersion > 0) {
          portMessage.append(list.get(list.size() - 1)).append(list.get(list.size() - 2))
            .append(list.get(list.size() - 3)).append(list.get(list.size() - 4));
        } 
        Integer temp = Integer.valueOf(Integer.parseInt(portMessage.toString(), 16));
        vmmPort = String.valueOf(temp);
      } 
      return vmmPort;
    }
    catch (IOException e) {
      throw new IOException(e);
    }
    finally {
      if (null != br) {
        try {
          br.close();
        }
        catch (IOException e) {
          LoggerUtil.error("br.close() failed!");
        } 
      }
    } 
  }
  public String getEncryInfo(String host, String username, String passWord, int port, String param) throws NumberFormatException, UnsupportedEncodingException, IOException {
    String encryInfo = "fal";
    StringBuffer ipmiOEM = new StringBuffer();
    ipmiOEM.append("-I lanplus").append(" ")
      .append("-H")
      .append(" ")
      .append(host)
      .append(" ")
      .append("-p")
      .append(" ")
      .append(port)
      .append(" ")
      .append("-U")
      .append(" ")
      .append(username)
      .append(" ")
      .append("-P")
      .append(" ")
      .append(passWord)
      .append(" ")
      .append("raw 0x30 0x94 0xDB 0x07 0x00 0x20")
      .append(" ")
      .append(param)
      .append(" ")
      .append("0x00 0xff");
    LoggerUtil.info( "ipmiOEM: "+ ipmiOEM );
    Process process = null;
    SendIPMIInDiffOS sendIPMIInDiffOS = new SendIPMIInDiffOS();
    if (sendIPMIInDiffOS.useIpmitoolInDiffOS(ipmiOEM.toString()) == null)
    {
      return "fal";
    }
    process = sendIPMIInDiffOS.useIpmitoolInDiffOS(ipmiOEM.toString());
    InputStream is = process.getInputStream();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(is, "utf-8"));
      String successMsg = null;
      List<String> respMsg = new ArrayList<>();
      while ((successMsg = br.readLine()) != null) {
        String outPutMsg = new String(successMsg.getBytes("utf-8"), "utf-8");
        respMsg.add(outPutMsg);
      } 
      if (respMsg.size() > 0) {
        String result = ((String)respMsg.get(respMsg.size() - 1)).replace(" ", "");
        Integer temp = Integer.valueOf(Integer.parseInt(result.substring(result.length() - 2, result.length()), 16));
        encryInfo = String.valueOf(temp);
      } 
      return encryInfo;
    }
    catch (IOException e) {
      throw new IOException(e);
    }
    finally {
      if (null != br) {
        try {
          br.close();
        }
        catch (IOException e) {
          LoggerUtil.error("br.close() failed!");
        } 
      }
    } 
  }
  private byte[] getRandomAndToByteArray() {
    SecureRandom random = new SecureRandom();
    int temp = random.nextInt(10);
    setKeyNumber(temp);
    byte[] byteArray = new byte[4];
    byteArray[3] = (byte)(temp & 0xFF);
    byteArray[2] = (byte)(temp >> 8 & 0xFF);
    byteArray[1] = (byte)(temp >> 16 & 0xFF);
    byteArray[0] = (byte)(temp >> 24 & 0xFF);
    return byteArray;
  }
  private String stringToHex(String str) {
    StringBuffer resultSB = new StringBuffer();
    for (int i = 0; i < str.length(); i++) {
      int cha = str.charAt(i);
      String resultstr = Integer.toHexString(cha);
      resultSB.append("0x").append(resultstr).append(" ");
    } 
    String result = resultSB.toString();
    return result;
  }
}
