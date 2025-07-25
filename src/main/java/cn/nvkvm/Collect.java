package cn.nvkvm;
import cn.library.LoggerUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory;
class Collect
{
  public String VENDOR = "";
  public String MODEL = "";
  public String BMC_VERSION = "";
  HashMap<String, String> extra = new HashMap<String, String>();
  private final static Log log = LogFactory.getLog(Main.class);
  public Collect(String host, String username, String passWord) throws Exception {
    StringBuffer ipmiOEM = new StringBuffer();
    ipmiOEM.append("-H").append(" ")
      .append(host)
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
      .append("fru list 0");
    //LoggerUtil.info( "ipmiOEM: "+ ipmiOEM );
    Process process = null;
    SendIPMIInDiffOS sendIPMIInDiffOS = new SendIPMIInDiffOS();
    if (sendIPMIInDiffOS.useIpmitoolInDiffOS(ipmiOEM.toString()) == null)
    {
      this.VENDOR = "";
      this.MODEL = "";
      this.BMC_VERSION = "";
    }
    process = sendIPMIInDiffOS.useIpmitoolInDiffOS(ipmiOEM.toString());
    InputStream is = process.getInputStream();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(is, "utf-8"));
      String successMsg = null;
      while ((successMsg = br.readLine()) != null) {
    	  String outPutMsg = new String(successMsg.getBytes("utf-8"), "utf-8").replace(" ","");
    	  String vendorPattern = "ProductManufacturer:(.*)";
    	  String modelPattern = "ProductName:(.*)";
    	  String bmcVersionPattern = "BoardExtra:BMCVersion(.*)";
    	  //zte同机型不同产品序号下载jnlp方法不同
    	  String productPartNumberPattern = "ProductPartNumber:(.*)";
    	  Pattern r1 = Pattern.compile(vendorPattern);
    	  Pattern r2 = Pattern.compile(modelPattern);
    	  Pattern r3 = Pattern.compile(bmcVersionPattern);
    	  Pattern r4 = Pattern.compile(productPartNumberPattern);
    	  Matcher m1 = r1.matcher(outPutMsg);
    	  Matcher m2 = r2.matcher(outPutMsg);
    	  Matcher m3 = r3.matcher(outPutMsg);
    	  Matcher m4 = r4.matcher(outPutMsg);
    	  //LoggerUtil.info("ipmi output: "+ outPutMsg );
    	  log.info(outPutMsg );
    	  if(m1.find()) {this.VENDOR = m1.group(1);}
    	  if(m2.find()) {this.MODEL = m2.group(1);}
    	  if(m3.find()) {this.BMC_VERSION = m3.group(1);}
    	  if(m4.find()) {this.extra.put("productPartNumber",m4.group(1));}
      } 
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
  
  public String getVendor() {
	  return this.VENDOR;	
  }
  public String getModel() {
	  return this.MODEL;	
  }
  public String getBmcVersion() {
	  return this.BMC_VERSION;	
  }
  public HashMap<String, String> getExtra() {
	  return this.extra;	
  }  
}
