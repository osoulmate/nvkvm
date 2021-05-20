package com.kvm;
import com.library.LoggerUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
class ClientSubmitLoginCommon
{
  private String loginUrl = "";
  private String downloadUrl = "";
  private String extraUrl = "";
  private String loginData = "";
  private String vendor = "";
  private String model = "";
  private String bmcVersion = "";
  private String host = "";
  private String userName = "";
  private String passWord = "";
  private String KvmMode = "";
  private String sessionValue = "";
  String tempInput = "";
  private String encryStr(String vendor,String data) {
	String result = "";
	if (vendor.toLowerCase().equals("inspur")) {
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
	}
	return result;
  }
  private void getEndPoint() {
	if (this.vendor.toLowerCase().equals("sugon")) {
		if(this.model.toLowerCase().equals("i620-g30")) {
			this.loginUrl = "https://"+this.host+"/api/session";
			this.downloadUrl = "https://"+this.host+"/api/kvmjnlp?&JNLPSTR=JViewer";
			this.loginData = "username="+this.userName+"&password="+this.passWord;
		}else if (this.model.toLowerCase().equals("w780-g20")) {
			this.loginUrl = "https://"+this.host+"/cgi/login.cgi";
			this.downloadUrl = "https://"+this.host+"/cgi/url_redirect.cgi?url_name=sol&url_type=jwss";
			this.loginData = "name="+this.userName+"&pwd="+this.passWord;
		}else {
			System.out.println();
		}	
	}else if (this.vendor.toLowerCase().equals("inspur")) {
		if(this.model.toLowerCase() == "nf5288m3") {
			System.out.println();
		}else if (this.model.toLowerCase().equals("nf5288m5")) {
			this.loginUrl = "https://"+this.host+"/api/session";
			this.downloadUrl = "https://"+this.host+"/video/jviewer.jnlp";
			this.extraUrl = "https://"+this.host+"/api/kvm/download";
			this.loginData = "username="+this.encryStr(this.vendor,this.userName)+"&password="
			                  +this.encryStr(this.vendor,this.passWord);
		}else {
			System.out.println();
		}		
	}else if (this.vendor.toLowerCase().equals("zte")) {
		if(this.model.toLowerCase().equals("r5300g4")) {
			this.loginUrl = "https://"+this.host+"/ext/session";
			this.downloadUrl = "https://"+this.host+"/Java/jviewer.jnlp";
			this.loginData = "username="+this.userName+"&password="+this.passWord;
		}else if (this.model.toLowerCase() == "nf5288m5") {
			System.out.println();
		}else {
			System.out.println();
		}				
	}else {
		System.out.println("no match:"+this.vendor.toLowerCase()+","+this.model.toLowerCase());
	}

  }
  private String getCSRFToken(String input) {
	  HashMap<String, String> sites = new HashMap<String, String>();
	    if (input.charAt(0) == '{' && input.charAt(input.length()-1) == '}'){
	    	String[] kv = input.replace("}","").replace("{", "").split(",");
	    	for(String k : kv) {
	    		String[] subKv = k.split(":");
	    		if (subKv.length >= 2) {
	    			sites.put(subKv[0].replace("\"", "").replace(" ", ""), subKv[1].replace("\"", "").replace(" ", ""));
	    			LoggerUtil.info("k:"+subKv[0].replace("\"", "")+" v:"+subKv[1].replace("\"", ""));
	    		}	
	    	}
	    }
	    if (sites.get("CSRFToken") != null){
	    	return sites.get("CSRFToken");
	    }else {
	    	return "";
	    }
	    
  }
  private void run(String csrfToken) throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException, IOException {
	   if (!this.extraUrl.equals("")) {
	    	this.doNext(csrfToken);
	   }
		URL requestUrl = null;
		try {
			requestUrl = new URL(this.downloadUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LoggerUtil.error(e.getClass().getName());
		}
		  TrustManager[] tm = { new MyX509TrustManager() };
	      SSLContext sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
	      sslContext.init(null, tm, new SecureRandom());
		  HttpsURLConnection httpsConn = (HttpsURLConnection)requestUrl.openConnection();
		  httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
		  httpsConn.setRequestMethod("GET");
		  if (this.extraUrl != null) {
	      httpsConn.setRequestProperty("X-CSRFTOKEN", csrfToken);
	      }
	      httpsConn.setRequestProperty("Cookie", this.sessionValue);
	      httpsConn.connect();
	      int ResponseCode = httpsConn.getResponseCode();
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
	        } catch (Exception e) {
	          LoggerUtil.error(e.getClass().getName());
	        } 
	      } 
	      try {
	        response.close();
	      }
	      catch (IOException e) {
	        try {
	          iptStream.close();
	        }
	        catch (IOException e2) {
	          LoggerUtil.error(e2.getClass().getName());
	        } 
	        LoggerUtil.error(e.getClass().getName());
	      } 
	      try {
	        iptStream.close();
	      }
	      catch (IOException e2) {
	        LoggerUtil.error(e2.getClass().getName());
	      }
	      String temp = sb.toString();
	      LoggerUtil.info("downloadUrl response:"+temp);
	      File f = new File("kvm.jnlp");
	      FileOutputStream fop = new FileOutputStream(f);
	      // 构建FileOutputStream对象,文件不存在会自动新建
	      OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
	      writer.append(temp);
	      writer.close();
	      fop.close();
  }
  private void doNext(String csrfToken) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
	URL requestUrl = null;
	try {
		requestUrl = new URL(this.extraUrl);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		LoggerUtil.error(e.getClass().getName());
	}
	  TrustManager[] tm = { new MyX509TrustManager() };
      SSLContext sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
      sslContext.init(null, tm, new SecureRandom());
	  HttpsURLConnection httpsConn = (HttpsURLConnection)requestUrl.openConnection();
	  httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
	  httpsConn.setRequestMethod("GET");
      httpsConn.setRequestProperty("X-CSRFTOKEN", csrfToken);
      httpsConn.setRequestProperty("Cookie", this.sessionValue);
      httpsConn.connect();
      int ResponseCode = httpsConn.getResponseCode();
      InputStream iptStream = httpsConn.getInputStream();
      BufferedReader response = new BufferedReader(new InputStreamReader(iptStream, "utf-8"));
      StringBuffer sb = new StringBuffer();
      if (200 == ResponseCode) {
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
        } catch (Exception e) {
          LoggerUtil.error(e.getClass().getName());
        } 
      } 
      try {
        response.close();
      }
      catch (IOException e) {
        try {
          iptStream.close();
        }
        catch (IOException e2) {
          LoggerUtil.error(e2.getClass().getName());
        } 
        LoggerUtil.error(e.getClass().getName());
      } 
      try {
        iptStream.close();
      }
      catch (IOException e2) {
        LoggerUtil.error(e2.getClass().getName());
      }
      String temp = sb.toString();
      LoggerUtil.info("extraUrl response:"+temp);
  }
  public String doLogin(String vendor,String model,String bmcVersion,String host, String userName, String passWord, String KvmMode) throws Exception {
	this.vendor = vendor;
	this.model = model;
	this.bmcVersion = bmcVersion;
	this.host = host;
	this.userName = userName;
	this.passWord = passWord;
	this.KvmMode = KvmMode;
	this.getEndPoint();
	String GetParaFromWebOutput = "";
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
    httpsConn.setDoOutput(true);
    boolean HasCertFlag = true;
    OutputStream opStream1 = null;
    try {
      opStream1 = httpsConn.getOutputStream();
      opStream1.write(PostListBypes);
      LoggerUtil.info( "PostListBypes: "+ PostListBypes );
    }
    catch (SSLHandshakeException e) {
      LoggerUtil.info( "SSLHandshakeException:"+ e );
      HasCertFlag = false;
    }
    catch (IOException e1) {
      return GetParaFromWebOutput;
    }
    finally {
      if (opStream1 != null) {
        try {
          opStream1.close();
        }
        catch (Exception e2) {
          LoggerUtil.error(e2.getClass().getName());
        } 
      }
    } 
    if (!HasCertFlag) {
      httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
      OutputStream opsStream2 = null;
      try {
        opsStream2 = httpsConn.getOutputStream();
        opsStream2.write(PostListBypes);
      }
      catch (IOException e) {
        return GetParaFromWebOutput;
      }
      finally {
        if (opsStream2 != null) {
          try {
            opsStream2.close();
          }
          catch (Exception e2) {
            LoggerUtil.error(e2.getClass().getName());
          } 
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
      this.statusCode = httpsConn.getResponseCode();
      this.expiration = httpsConn.getExpiration();
      this.request = httpsConn.getURL();
      this.expiration = httpsConn.getExpiration();
      this.lastModified = httpsConn.getLastModified();
      this.responseHeaders = httpsConn.getHeaderFields();
      this.contentType = httpsConn.getContentType();
      this.contentEncoding = httpsConn.getContentEncoding();
      */
      Map em = httpsConn.getHeaderFields();
      System.out.println("header Values:" + em.toString());
      //System.out.println("CSRFTOKEN:" + em1.get("CSRFToken"));
      String headerName = null;   
      for (int i = 1; (headerName = httpsConn.getHeaderFieldKey(i)) != null; i++) 
      {
           System.out.println(headerName+": "+httpsConn.getHeaderField(i));
      }
    }
    catch (Exception e) {
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
      } catch (Exception e) {
        LoggerUtil.error(e.getClass().getName());
      } 
    } 
    try {
      response.close();
    }
    catch (IOException e) {
      try {
        iptStream.close();
      }
      catch (IOException e2) {
        LoggerUtil.error(e2.getClass().getName());
      } 
      LoggerUtil.error(e.getClass().getName());
    } 
    try {
      iptStream.close();
    }
    catch (IOException e2) {
      LoggerUtil.error(e2.getClass().getName());
    }
    String temp = sb.toString();
    LoggerUtil.info("temp:"+temp);
    String csrfToken = this.getCSRFToken(temp);
    LoggerUtil.info("csrfToken:"+csrfToken);
    LoggerUtil.info( "extraUrl:"+ this.extraUrl);
    if (this.downloadUrl != null) {
    	this.run(csrfToken);
    }
    GetParaFromWebOutput = GetParaFromWebOutput + temp;
    return GetParaFromWebOutput;
  }
}
