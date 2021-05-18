package com.kvm;
import com.library.LoggerUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
class FirmwareRevision
{
  public static final String LOAD_SHARE_LIBRARY = "export LD_LIBRARY_PATH=`pwd`";
  public static final String LAN_INTERFACE = "-I lanplus";
  public static final String IPMI_REQUEST_PARAM = "raw 0x06 0x01";
  public int getRevision(String host, String user_name, String pwdStr, int port) throws Exception {
    int firmwareRevision = -1;
    StringBuffer ipmiOEM = new StringBuffer();
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
      .append(user_name)
      .append(" ")
      .append("-P")
      .append(" ")
      .append(pwdStr)
      .append(" ")
      .append("raw 0x06 0x01");
    Process process = null;
    SendIPMIInDiffOS sendIPMIInDiffOS = new SendIPMIInDiffOS();
    if (sendIPMIInDiffOS.useIpmitoolInDiffOS(ipmiOEM.toString()) == null)
    {
      return firmwareRevision;
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
        int hexVersion = Integer.parseInt(list.get(13), 16);
        String binaryStr = Integer.toBinaryString(hexVersion);
        String version = "";
        if (binaryStr.length() > 4) {
          version = binaryStr.substring(0, binaryStr.length() - 4);
        } else {
          version = "0000";
        } 
        firmwareRevision = Integer.parseInt(version, 2);
      } 
      return firmwareRevision;
    }
    catch (IOException e) {
      throw new IOException(e);
    }
    finally {
      if (null != br)
        try {
          br.close();
        }
        catch (IOException e) {
          LoggerUtil.error("br.close() failed!");
        }  
    } 
  }
}
