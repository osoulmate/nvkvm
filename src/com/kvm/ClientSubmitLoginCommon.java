package com.kvm;
import com.library.LoggerUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.FileReader; 
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException; 
class ClientSubmitLoginCommon
{
  private Boolean isHttps = true;
  private Boolean useTemplate = false;
  private Boolean addCookie = false;
  private String loginUrl = "";
  private String downloadUrl = "";
  private String extraUrl = "";
  private String loginData = "";
  private String vendor = "";
  private String model = "";
  private String bmcVersion = "";
  private HashMap<?, ?> extraVendor;
  private String host = "";
  private String userName = "";
  private String passWord = "";
  private String kvmMode = "";
  private String sessionValue = "";
  private String csrfToken = "";
  private String loginRes = "";
  private String secondAuthRes = "";
  private String downloadRes = "";
  HashMap<String, String> extraCookie = new HashMap<String, String>();
  public void MyThread() {
      //System.out.println("main start...");
      Thread t = new Thread() {
          public void run() {
            System.out.println("thread run...");
            try {
                Runtime.getRuntime().exec("javaws kvm.jnlp").getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("thread end.");
          }
      };
      t.start();
      //System.out.println("main end...");
  }

  private String encryStr(String data) {
    String result = "";
    if (this.vendor.equalsIgnoreCase("inspur")) {
        for(int i=0;i<data.length();i++) {
            char ch =data.charAt(i);
            Integer charCode = Integer.valueOf(ch);
            charCode = charCode^127;
            if (result.equals("")) {
              result = result+String.valueOf(Integer.toHexString(charCode)).replace("0x", "");
            }else {
              result = result+"-"+String.valueOf(Integer.toHexString(charCode)).replace("0x", "");
            }
        
        }
    }else if (this.vendor.equalsIgnoreCase("h3c")){
    	try {
			result = Base64.getEncoder().encodeToString(data.getBytes("utf-8"));
			result = Base64.getEncoder().encodeToString(result.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    return result;
  }
  private Boolean getEndPoint() {
	if (this.vendor.equalsIgnoreCase("newh3ctechnologiesco.,ltd.") || this.vendor.equalsIgnoreCase("newh3c")) {
		this.vendor = "h3c";
	}
    if (this.vendor.equalsIgnoreCase("sugon")) {
        if(this.model.equalsIgnoreCase("i620-g30")) {
            this.loginUrl = "/api/session";
            this.downloadUrl = "/api/kvmjnlp?&JNLPSTR=JViewer";
            this.loginData = "username="+this.userName+"&password="+this.passWord;
        }else if (this.model.equalsIgnoreCase("w780-g20")) {
            this.loginUrl = "/cgi/login.cgi";
            this.downloadUrl = "/cgi/url_redirect.cgi?url_name=sol&url_type=jwss";
            this.loginData = "name="+this.userName+"&pwd="+this.passWord;
        }else {
        	LoggerUtil.info("no match:"+this.vendor.toLowerCase()+","+this.model.toLowerCase());
        }   
    }else if (this.vendor.equalsIgnoreCase("inspur")) {
        if(this.model.equalsIgnoreCase("nf5280m5")) {
            System.out.println();
        }else if (this.model.equalsIgnoreCase("nf5288m5")) {
            this.loginUrl = "/api/session";
            this.extraUrl = "/api/kvm/download";
            this.downloadUrl = "/video/jviewer.jnlp";
            if(!this.extraVendor.get("productPartNumber").equals("0")) {
              this.loginData = "username="+this.encryStr(this.userName)+"&password="
                              +this.encryStr(this.passWord);
            }else {
                this.loginData = "username="+this.userName+"&password="+this.passWord;
            }
        }else {
        	LoggerUtil.info("no match:"+this.vendor.toLowerCase()+","+this.model.toLowerCase());
        }       
    }else if (this.vendor.equalsIgnoreCase("zte")) {
        if(this.model.equalsIgnoreCase("r5300g4")) {
            this.loginUrl = "/ext/session";
            this.downloadUrl = "/Java/jviewer.jnlp";
            if(this.bmcVersion.equalsIgnoreCase("03.13.0200")) {
            	this.loginData = "username=zteroot&password=nZ2MK3ly4yzff0C5cykeRIlPyLE2vyd4HbLtQa4p%2F0yn5cBmWCef%2FcS3zkH2%2FdC6xU3IcvkYeP%2BUfrGfHpsWkhL%2B%2BQoRVWtJIPM%2F9rOX2yjGeZ%2FBTht9n9rj0B6ornffEYew49twbGrt%2B0gX2sSVx9HlshDIHgbPtvnKg4l%2BGtvh4fO5oU5Mj%2BR5m8Kfq2TcX4BeKQD2G6Nwh6GbtXJPVg1j2%2FwvFMm6IC%2BKtDNAHf99dBYdolrjOCSQIxoJDMBdQwWrrw7KzP732rakUKM64WcmZZMzXl7nG%2BsN169Di5KDFPZwHDhHE5Zkv24QtSX5Jon3KoqYa10SWSWyY1dUPA%3D%3D";
            }else {
              this.loginData = "username="+this.userName+"&password="+this.passWord;
            }
        }else if (this.model.toLowerCase() == "nf5288m5") {
        	LoggerUtil.info("no match:"+this.vendor.toLowerCase()+","+this.model.toLowerCase());
        }else {
        	LoggerUtil.info("no match:"+this.vendor.toLowerCase()+","+this.model.toLowerCase());
        }               
    }else if (this.vendor.equalsIgnoreCase("h3c")) {
        if(this.model.equalsIgnoreCase("uniserverr4900g3")) {
        	this.useTemplate = true;
        	this.addCookie = true;
            this.loginUrl = "/api/session";
            this.downloadUrl = "/api/settings/media/instance";
            this.extraUrl = "/api/kvm/token";
            this.loginData = "username="+this.encryStr(this.userName)+"&password="
                              +this.encryStr(this.passWord)+"&log_type=1";
        }else if (this.model.equalsIgnoreCase("uniserverr4900g4")) {
            this.loginUrl = "/api/session";
            this.downloadUrl = "/video/jviewer.jnlp";
            this.extraUrl = "/api/kvm/download";
            this.loginData = "username="+this.encryStr(this.userName)+"&password="
                              +this.encryStr(this.passWord);
        }else {
        	LoggerUtil.info("no match:"+this.vendor.toLowerCase()+","+this.model.toLowerCase());
        }       
    }else if (this.vendor.equalsIgnoreCase("dell")) {
         this.loginUrl = "/data/login";
         this.downloadUrl = "/viewer.jnlp";
         this.loginData = "user="+this.userName+"&password="+this.passWord;     
    }
    else if (this.vendor.equalsIgnoreCase("lenovo")) {
        this.loginUrl = "/data/login";
        this.downloadUrl = "/viewer.jnlp";
        this.loginData = "user="+this.userName+"&password="+this.passWord;     
   }
    else {
    	LoggerUtil.info("no match:"+this.vendor.toLowerCase()+","+this.model.toLowerCase());
        return false;
    }
    return true;
  }
  private void getSvcTag() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
	  URL requestUrl = new URL("https://"+this.host+"/data?get=svcTag,sysDesc");
	  TrustManager[] tm = { new MyX509TrustManager() };
	  SSLContext sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
	  sslContext.init(null, tm, new SecureRandom());
	  //HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
	  HttpsURLConnection con = (HttpsURLConnection)requestUrl.openConnection();
	  con.setSSLSocketFactory(sslContext.getSocketFactory());
	  String st2 = this.extraCookie.getOrDefault("ST","notfound").split(",")[1].split("=")[1];
	  String st1 = this.extraCookie.getOrDefault("ST","notfound").split(",")[0].split("=")[1];
	  LoggerUtil.info("ST2:"+st2);
	  LoggerUtil.info("sessionValue:"+this.sessionValue);
	  con.setRequestMethod("POST");
	  con.setRequestProperty("Cookie", this.sessionValue);
	  con.setRequestProperty("ST2", st2);
	  int status = con.getResponseCode();
	  BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	  String inputLine;
	  StringBuffer content = new StringBuffer();
	  while((inputLine = in.readLine()) != null) {
	  	content.append(inputLine);
	  }
	  in.close();
	  con.disconnect();
	  System.out.println("Response status: " + status);
	  System.out.println(content.toString());
	  HashMap<String, String> mysites= this.strToJson(content.toString());
	  this.downloadUrl ="https://"+this.host+"/viewer.jnlp("+this.host
			  +"@0@idrac-"+mysites.get("svcTag")
			  +"%2C+PowerEdge+R730xd%2C+@1621845998222@ST1="+st1+")";
	  LoggerUtil.info("downloadUrl:"+this.downloadUrl);
	
}

private HashMap<String, String> strToJson(String input) {
    HashMap<String, String> sites = new HashMap<String, String>();
      if (input.charAt(0) == '{' && input.charAt(input.length()-1) == '}'){
        String[] kv = input.replace("}","").replace("{", "").split(",");
        for(String k : kv) {
          String[] subKv = k.split(":");
          if (subKv.length >= 2) {
            sites.put(subKv[0].replace("\"", "").replace(" ", ""), subKv[1].replace("\"", "").replace(" ", ""));
            //LoggerUtil.info("k:"+subKv[0].replace("\"", "")+" v:"+subKv[1].replace("\"", ""));
          }
        }
      }
      if (this.vendor.equalsIgnoreCase("dell")) {
    	  String stPattern = "<forwardUrl>(.*)</forwardUrl>";
    	  String sysDescPattern = "<sysDesc>(.*)</sysDesc>";
    	  String svcTagPattern = "<svcTag>BMCVersion(.*)</svcTag>";
    	  Pattern r1 = Pattern.compile(stPattern);
    	  Pattern r2 = Pattern.compile(sysDescPattern);
    	  Pattern r3 = Pattern.compile(svcTagPattern);
    	  Matcher m1 = r1.matcher(input);
    	  Matcher m2 = r2.matcher(input);
    	  Matcher m3 = r3.matcher(input);
    	  if(m1.find()) {sites.put("ST",m1.group(1));}
    	  if(m2.find()) {sites.put("sysDesc",m2.group(1));}
    	  if(m3.find()) {sites.put("svcTag",m3.group(1));}
      }
      return sites;
  }
  private void resetSession(){
    if(this.model.equalsIgnoreCase("uniserverr4900g3")) {
	  this.sessionValue = "product_board_id=g3; "+
	          		"node_board_id=255; "+
	          		"productID_num=16978692; "+
	          		"lang=auto; "+
	          		"safe_switch=0; "+
	          		"oem_flag=1; "+
	          		"buid_time=2021; "+
	          		"extended_privilege=259; "+
	          		"privilege_id=4; "+
	          		"un=admin; "+
	          		"ldap_user=0; "+
	          		"ad_user=0; "+
	          		"network_access=0; "+
	          		"user_access=0; "+
	          		"basic_access=0; "+
	          		"power_access=0; "+
	          		"firmware_access=0; "+
	          		"health_access=0; "+
	          		"remote_access=0; "+
	          		"kvm_access=1; "+
	          		"vmedia_access=1; "+
	          		"custom_id=6; "+
	          		"CSRF="+this.csrfToken+";"+
	          		"session_id="+this.extraCookie.get("racsession_id")+"; "+this.sessionValue;   	
	 }
  }
  public String[] run() throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
	    String exceptionSite = "";
	    String[] downloadRes= {"",""};
	    String[] doNextRes = null;
	    URL requestUrl = null;
	    //如果addCookie为真，执行resetSession方法重写Cookies;
	    if(this.addCookie) {
	      this.resetSession();
	    }
	    //如果this.extraUrl不为空，执行doNext()方法进行二次认证;
	    if (!this.extraUrl.equals("")) {
	      if (this.isHttps) {
	          this.extraUrl = "https://"+this.host+this.extraUrl;
	          exceptionSite = "https://"+this.host;
	      }else {
	          this.extraUrl = "http://"+this.host+this.extraUrl;
	          exceptionSite = "http://"+this.host;
	      }
	      try {
	          doNextRes = this.doNext();
	      } catch (KeyManagementException e) {
	       e.printStackTrace();
	      } catch (NoSuchAlgorithmException e) {
	       e.printStackTrace();
	      } catch (NoSuchProviderException e) {
	       e.printStackTrace();
	      } catch (IOException e) {
	       e.printStackTrace();
	      }
	      if(doNextRes[0].equals("0")) {
	        LoggerUtil.info("doNext: failure");
	        downloadRes[0] = "0";
	        downloadRes[1] = doNextRes[1];
	        return downloadRes;
	      };
	    }else {
	      if (this.isHttps) {
	          exceptionSite = "https://"+this.host;
	      }else {
	          exceptionSite = "http://"+this.host;
	      }       
	    }
	    try {
	      requestUrl = new URL(this.downloadUrl);
	    }catch (MalformedURLException e) {
	      LoggerUtil.error(e.getClass().getName());
	      downloadRes[0] = "0";
	      downloadRes[1] = e.getClass().getName();
	      return downloadRes;
	    }
	    TrustManager[] tm = { new MyX509TrustManager() };
	    SSLContext sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
	    sslContext.init(null, tm, new SecureRandom());
	    HttpsURLConnection httpsConn = null;
	    try {
	        httpsConn = (HttpsURLConnection)requestUrl.openConnection();
	    } catch (IOException e1) {
	        e1.printStackTrace();
	        LoggerUtil.info( "requestUrl.openConnection(): "+ e1.getClass().getName() );
	    }
	    httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
	    try {
	        httpsConn.setRequestMethod("GET");
	    } catch (ProtocolException e1) {
	        e1.printStackTrace();
	        LoggerUtil.info( "httpsConn.setRequestMethod: "+ e1.getClass().getName() );
	    }
	    //已知需要二次认证的设备型号http请求头需添加X-CSRFTOKEN字段。
	    if (!this.csrfToken.equals("notfound") && !this.bmcVersion.equalsIgnoreCase("03.13.0200")) {	
	      LoggerUtil.info("extraUrl:"+this.extraUrl);
	      httpsConn.setRequestProperty("X-CSRFTOKEN", this.csrfToken);
	      LoggerUtil.info("add X-CSRFTOKEN:"+this.csrfToken);
	    }
	    LoggerUtil.info("sessionValue:"+this.sessionValue);
	    httpsConn.setRequestProperty("Cookie", this.sessionValue);
	    try {
	        httpsConn.connect();
	    } catch (IOException e1) {
	        e1.printStackTrace();
	        LoggerUtil.info( "httpsConn.connect: "+ e1.getClass().getName() );
	    }
	    int ResponseCode = 0;
	    try {
	        ResponseCode = httpsConn.getResponseCode();
	    } catch (IOException e1) {
	        e1.printStackTrace();
	    }
	    InputStream iptStream = null;
	    try {
	    	System.out.println("Downloading jnlp...");
	        iptStream = httpsConn.getInputStream();
	    } catch (IOException e1) {
	    	LoggerUtil.info( "httpsConn.getInputStream: "+ e1.getClass().getName()+";ResponseCode:"+ResponseCode );
	    }
	    BufferedReader response = null;
	    try {
	        response = new BufferedReader(new InputStreamReader(iptStream, "utf-8"));
	    } catch (UnsupportedEncodingException e1) {
	        e1.printStackTrace();
	    }
	    StringBuffer sb = new StringBuffer();
	    if (200 == ResponseCode) {
	      int intC = 0;
	      try {
	        while ((intC = response.read()) != -1)
	        {
	          char c = (char)intC;
	          sb.append(c);
	        }
	      } catch (Exception e) {
	        LoggerUtil.error(e.getClass().getName());
	        downloadRes[0] = "0";
	        downloadRes[1] = e.getClass().getName();
	        return downloadRes;
	      } 
	    } 
	    try {
	      response.close();
	      iptStream.close();
	    }catch (IOException e) {
	      LoggerUtil.error(e.getClass().getName());
	      downloadRes[0] = "0";
	      downloadRes[1] = e.getClass().getName();
	      return downloadRes;
	    } 
	    String temp = sb.toString();
	    this.downloadRes = temp;
	    LoggerUtil.info("downloadUrl response:"+temp);
	    if(this.useTemplate) {
	        JnlpTemplate myTemplate = new JnlpTemplate(this.vendor,
	                                                   this.model,
	                                                   this.host,
	                                                   this.isHttps,
	                                                   this.loginRes,
	                                                   this.secondAuthRes,
	                                                   this.downloadRes);
	        String myjnlp = myTemplate.getTemplate();
	        temp = myjnlp;
	    }
	    //获取java例外站点文件
	    String exceptionSites =System.getProperty("user.home")+File.separator+"AppData"
	                                                    +File.separator+"LocalLow"
	                                                    +File.separator+"Sun"
	                                                    +File.separator+"Java"
	                                                    +File.separator+"Deployment"
	                                                    +File.separator+"security"
	                                                    +File.separator+"exception.sites";
	    LoggerUtil.info("javaTempPath:"+exceptionSites);
	    File file=new File(exceptionSites);    
	    if(!file.exists())    
	    {    
	        try{    
	            file.createNewFile();    
	        } catch (IOException e) {     
	            e.printStackTrace();    
	        }    
	    }else {
	        try {
	        FileInputStream fi = new FileInputStream(file);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(fi,"UTF-8"));
	        String line = null;
	        Boolean flag = false;
	        String initExceptionSite = exceptionSite;
	        while ((line = reader.readLine()) != null) {
	            if(line.replace(" ", "").equalsIgnoreCase(initExceptionSite)) {
	                flag = true;
	                break;
	            }else {
	                exceptionSite = exceptionSite +"\r\n"+line;
	            }
	        }
	        fi.close();
	        reader.close();
	        if(!flag) {
	            LoggerUtil.info("Add Java exceptionSites:"+exceptionSites);
	            FileOutputStream fo = new FileOutputStream(file);
	            OutputStreamWriter writer = new OutputStreamWriter(fo, "UTF-8");
	            writer.append(exceptionSite+"\r\n"); 
	            writer.close();
	            fo.close();
	        }
	       }catch(IOException e) {
	           LoggerUtil.info("Add Java exceptionSites occurs error:"+e);
	       }
	    }
	    File f = new File("kvm.jnlp");
	    FileOutputStream fop = null;
	    try {
	        fop = new FileOutputStream(f);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    OutputStreamWriter w = null;
	    try {
	        w = new OutputStreamWriter(fop, "UTF-8");
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    try {
	        w.append(temp);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    try {
	        w.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    try {
	        fop.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    System.out.println("Download jnlp success!");
	    this.MyThread();
	    downloadRes[0] = String.valueOf(ResponseCode);
	    downloadRes[1] = "success";
	    return downloadRes;
	  }
  private String[] doNext() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
    URL requestUrl = null;
    String[] nextRes = {"",""};
    try {
    	LoggerUtil.info("extraUrl:"+this.extraUrl);
        requestUrl = new URL(this.extraUrl);
    } catch (MalformedURLException e) {
        LoggerUtil.error(e.getClass().getName());
        nextRes[0]="0";
        nextRes[1]=e.getClass().getName();
        return nextRes;
    }
    TrustManager[] tm = { new MyX509TrustManager() };
    SSLContext sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
    sslContext.init(null, tm, new SecureRandom());
    HttpsURLConnection httpsConn = (HttpsURLConnection)requestUrl.openConnection();
    httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
    httpsConn.setRequestMethod("GET");
    httpsConn.setRequestProperty("X-CSRFTOKEN", this.csrfToken);
    LoggerUtil.info("Cookie:"+this.sessionValue);
    httpsConn.setRequestProperty("Cookie", this.sessionValue);
    httpsConn.connect();
    int ResponseCode = httpsConn.getResponseCode();
    LoggerUtil.info("Second Auth ResponseCode:"+ResponseCode);
    InputStream iptStream = httpsConn.getInputStream();
    BufferedReader response = new BufferedReader(new InputStreamReader(iptStream, "utf-8"));
    StringBuffer sb = new StringBuffer();
    if (200 == ResponseCode) {
      int intC = 0;
      try {
        while ((intC = response.read()) != -1)
        {
          char c = (char)intC;
          sb.append(c);
        }
      }catch (Exception e) {
        LoggerUtil.error(e.getClass().getName());
        nextRes[0]="0";
        nextRes[1]=e.getClass().getName();
        return nextRes;
      } 
    } 
    try {
      response.close();
      iptStream.close();
    }catch (IOException e) {
      LoggerUtil.error(e.getClass().getName());
      nextRes[0]="0";
      nextRes[1]=e.getClass().getName();
      return nextRes;
    } 
    String temp = sb.toString();
    this.secondAuthRes = temp;
    LoggerUtil.info("Second Auth response:"+temp);
    nextRes[0]=String.valueOf(ResponseCode);
    nextRes[1]=temp;
    return nextRes;
  }
  public String[] doLogin(String vendor,String model,String bmcVersion,HashMap<?, ?> extraVendor,String host, String userName, String passWord, String kvmMode) throws Exception {
    this.vendor = vendor;
    this.model = model;
    this.bmcVersion = bmcVersion;
    this.extraVendor = extraVendor;
    this.host = host;
    this.userName = userName;
    this.passWord = passWord;
    this.kvmMode = kvmMode;
    String [] GetParaFromWebOutput = {"",""};
    if(!this.getEndPoint()) {
    	GetParaFromWebOutput[0] = "0";
    	GetParaFromWebOutput[1] = "no match vendor";
    	return GetParaFromWebOutput;
    }
    if (this.isHttps) {
    	this.loginUrl = "https://"+this.host+this.loginUrl;
    	this.downloadUrl = "https://"+this.host+this.downloadUrl;
    }else {
    	this.loginUrl = "http://"+this.host+this.loginUrl;
    	this.downloadUrl = "http://"+this.host+this.downloadUrl;
    }
    String url = this.loginUrl;
    LoggerUtil.info( "loginUrl:"+ url);
    LoggerUtil.info( "loginData:"+ this.loginData);
    URL requestUrl = null;
    TrustManager[] tm = { new MyX509TrustManager() };
    SSLContext sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
    sslContext.init(null, tm, new SecureRandom());
    byte[] PostListBypes = this.loginData.getBytes("UTF-8");
    try {
      requestUrl = new URL(url);
    }
    catch (MalformedURLException e) {
      LoggerUtil.error(e.getClass().getName());
      return GetParaFromWebOutput;
    } 
    HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
    HttpsURLConnection httpsConn = (HttpsURLConnection)requestUrl.openConnection();
    httpsConn.setRequestMethod("POST");
    if (this.vendor.equalsIgnoreCase("lenovo")) {
    	httpsConn.setRequestProperty("Referer", "https://"+this.host+"/designs/imm/index.php");
    }
    httpsConn.setDoOutput(true);
    httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
    OutputStream opsStream1 = null;
    try {
      opsStream1 = httpsConn.getOutputStream();
      opsStream1.write(PostListBypes);
    }catch (IOException e) {
      LoggerUtil.error(e.getClass().getName());
  	  GetParaFromWebOutput[0] = "0";
  	  GetParaFromWebOutput[1] = e.getClass().getName();
      return GetParaFromWebOutput;
    }finally {
      if (opsStream1 != null) {
        try {
          opsStream1.close();
        }
        catch (Exception e) {
          LoggerUtil.error(e.getClass().getName());
      	  GetParaFromWebOutput[0] = "0";
      	  GetParaFromWebOutput[1] = e.getClass().getName();
        } 
      }
    } 
    BufferedReader response = null;
    InputStream iptStream = null;
    int ResponseCode = 0;
    try {
      ResponseCode = httpsConn.getResponseCode();
      iptStream = httpsConn.getInputStream();
      /*
      String headerName = null;   
      for (int i = 1; (headerName = httpsConn.getHeaderFieldKey(i)) != null; i++) 
      {
    	  LoggerUtil.info(headerName+": "+httpsConn.getHeaderField(i));
      }*/
    }catch (Exception e) {
      GetParaFromWebOutput[0] = "0";
      GetParaFromWebOutput[1] = e.getClass().getName();
      return GetParaFromWebOutput;
    } 
    response = new BufferedReader(new InputStreamReader(iptStream, "utf-8"));
    StringBuffer sb = new StringBuffer();
    if (200 == ResponseCode) {
      String resCookice = httpsConn.getHeaderField("Set-Cookie");
      String[] sessionId = resCookice.split(";");
      this.sessionValue = sessionId[0];
      int intC = 0;
      try {
        while ((intC = response.read()) != -1)
        {
          char c = (char)intC;
          if (c == '\n') {
            break;
          }
          if (sb.length() >= 1024) {
            break;
          }
          sb.append(c);
        }
      }catch (Exception e) {
        LoggerUtil.error(e.getClass().getName());
    	GetParaFromWebOutput[0] = "0";
      	GetParaFromWebOutput[1] = e.getClass().getName();
      	return GetParaFromWebOutput;
      } 
    } 
    try {
      response.close();
      iptStream.close();
    }catch (IOException e) {
      LoggerUtil.error(e.getClass().getName());
    } 
    String temp = sb.toString();
    this.loginRes = temp;
    LoggerUtil.info("Login Response:"+temp);
    this.extraCookie= this.strToJson(temp);
    this.csrfToken = this.extraCookie.getOrDefault("CSRFToken","notfound"); 
    LoggerUtil.info("csrfToken:"+this.csrfToken);
    GetParaFromWebOutput[0] = String.valueOf(ResponseCode);
    GetParaFromWebOutput[1] = GetParaFromWebOutput[1] + temp;
    System.out.println("Login in Response:  "+temp);
    if (this.vendor.equalsIgnoreCase("dell")) {
    	this.getSvcTag();
    }else if (this.vendor.equalsIgnoreCase("lenovo")) {
    	this.downloadUrl = "https://"+this.host
    			+"/designs/imm/viewer("+this.host+"@443@0@1622021861882@1@0@0@jnlp@USERID@0@0@0@0@1).jnlp?"
    			+this.extraCookie.getOrDefault("token1_name","notfound")+"="
    			+this.extraCookie.getOrDefault("token1_value","notfound");
    }
    return GetParaFromWebOutput;
  }
}


