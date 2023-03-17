package com.kvm;
import com.library.LoggerUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

class ClientSubmitLoginCommon
{
    private Boolean isHttps = true;
    private Boolean useTemplate = false;
    private Boolean addCookie = false;
    private Boolean isResetDownloadUrl = false;
    private Boolean addCsrfToken = false;
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
    //private String kvmMode = "";
    private String sessionValue = "";
    //private String csrfToken = "";
    private String loginRes = "";
    private String secondAuthRes = "";
    private String downloadRes = "";
    private TrustManager[] tm = { new MyX509TrustManager() };
    private SSLContext sslContext;
    //private HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
    private HttpsURLConnection httpsConn;
    private HttpURLConnection httpConn;
    private HashMap<String, String> extraCookie = new HashMap<String, String>();
    public void MyThread() {
        Thread t = new Thread() {
            public void run() {
              //System.out.println("thread run...");
              try {
                  Runtime.getRuntime().exec("javaws kvm.jnlp").getInputStream();
              }catch (IOException e) {
                  e.printStackTrace();
              }
              //System.out.println("thread end.");
            }
        };
        t.start();
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
        if (this.vendor.equalsIgnoreCase("lnvo")) {
            this.vendor = "lenovo";
        }
        if (this.vendor.equalsIgnoreCase("sugon")) {
            if(this.model.equalsIgnoreCase("i620-g30")) {
                this.addCsrfToken = true;
                this.loginUrl = "/api/session";
                this.downloadUrl = "/api/kvmjnlp?&JNLPSTR=JViewer";
                this.loginData = "username="+this.userName+"&password="+this.passWord;
            }else if (this.model.equalsIgnoreCase("i620-g20")) {
          	    this.addCookie = true;
          	    this.isHttps = false;
                this.loginUrl = "/rpc/WEBSES/create.asp";
                this.downloadUrl = "/Java/jviewer.jnlp?EXTRNIP="+this.host+"&JNLPSTR=JViewer";
                this.loginData = "WEBVAR_USERNAME="+this.userName+"&WEBVAR_PASSWORD="+this.passWord;
            }else if (this.model.equalsIgnoreCase("w780-g20")) {
                this.loginUrl = "/cgi/login.cgi";
                //this.downloadUrl = "/cgi/url_redirect.cgi?url_name=sol&url_type=jwss";
                this.downloadUrl = "/cgi/url_redirect.cgi?url_name=ikvm&url_type=jwsk";
                this.loginData = "name="+this.userName+"&pwd="+this.passWord;
            }else if (this.model.equalsIgnoreCase("w580-g20")) {
                this.loginUrl = "/cgi/login.cgi";
                this.downloadUrl = "/cgi/url_redirect.cgi?url_name=ikvm&url_type=jwsk";
                this.loginData = "name="+this.userName+"&pwd="+this.passWord;
            }else if (this.model.equalsIgnoreCase("i640-g15")) {
            	this.useTemplate = true;
                this.loginUrl = "/cgi/login.cgi";
                this.downloadUrl = "/cgi/url_redirect.cgi?url_name=ikvm&url_type=jwsk";
                this.loginData = "name="+this.userName+"&pwd="+this.passWord;
            }else {
            	// W580-G20
                this.loginUrl = "/cgi/login.cgi";
                this.downloadUrl = "/cgi/url_redirect.cgi?url_name=ikvm&url_type=jwsk";
                this.loginData = "name="+this.userName+"&pwd="+this.passWord;
            }
        }else if (this.vendor.equalsIgnoreCase("fiberhome")) {
            if (this.model.equalsIgnoreCase("fitserver")) {
                this.loginUrl = "/cgi/login.cgi";
                this.downloadUrl = "/cgi/url_redirect.cgi?url_name=ikvm&url_type=jwsk";
                this.loginData = "name="+this.userName+"&pwd="+this.passWord;
            }else {
                this.loginUrl = "/cgi/login.cgi";
                this.downloadUrl = "/cgi/url_redirect.cgi?url_name=ikvm&url_type=jwsk";
                this.loginData = "name="+this.userName+"&pwd="+this.passWord;
            }
        }else if (this.vendor.equalsIgnoreCase("CMSOFT")) {
            if (this.model.equalsIgnoreCase("BCS2000")) {
            	this.addCookie = true;
                this.loginUrl = "/rpc/WEBSES/create.asp";
                this.downloadUrl = "/Java/jviewer.jnlp";
                this.loginData = "WEBVAR_USERNAME="+this.userName+"&WEBVAR_PASSWORD="+this.passWord;
            }else {
            	this.addCookie = true;
                this.loginUrl = "/cgi/login.cgi";
                this.downloadUrl = "/cgi/url_redirect.cgi?url_name=ikvm&url_type=jwsk";
                this.loginData = "WEBVAR_USERNAME="+this.userName+"&WEBVAR_PASSWORD="+this.passWord;
            }
        }else if (this.vendor.equalsIgnoreCase("inspur")) {
              if (this.model.equalsIgnoreCase("nf5288m5")) {
                  this.loginUrl = "/api/session";
                  this.extraUrl = "/api/kvm/download";
                  this.downloadUrl = "/video/jviewer.jnlp";
                  if(!this.extraVendor.get("productPartNumber").equals("0")) {
                    this.loginData = "username="+this.encryStr(this.userName)+"&password="
                                    +this.encryStr(this.passWord);
                  }else {
                      this.loginData = "username="+this.userName+"&password="+this.passWord;
                  }
              }else if (this.model.equalsIgnoreCase("nf5270m3")) {
            	  //this.isHttps = false;
            	  this.addCookie = true;
            	  this.useTemplate = true;
                  this.loginUrl = "/rpc/WEBSES/create.asp";
                  this.downloadUrl = "/Java/jviewer.jnlp?EXTRNIP="+this.host+"&JNLPSTR=JViewer";
                  this.loginData = "WEBVAR_USERNAME="+this.userName+"&WEBVAR_PASSWORD="+this.passWord;
              }else if (this.model.equalsIgnoreCase("nf8420m3")) {
            	  //this.isHttps = false;
            	  this.addCookie = true;
            	  this.useTemplate = true;
                  this.loginUrl = "/rpc/WEBSES/create.asp";
                  this.downloadUrl = "/Java/jviewer.jnlp?EXTRNIP="+this.host+"&JNLPSTR=JViewer";
                  this.loginData = "WEBVAR_USERNAME="+this.userName+"&WEBVAR_PASSWORD="+this.passWord;
              }else {
                  this.loginUrl = "/api/session";
                  this.extraUrl = "/api/kvm/download";
                  this.downloadUrl = "/video/jviewer.jnlp";
                  this.loginData = "username="+this.userName+"&password="+this.passWord;
              }
        }else if (this.vendor.equalsIgnoreCase("zte")) {
          if (this.model.equalsIgnoreCase("r5300g4")) {
                this.loginUrl = "/ext/session";
                this.downloadUrl = "/Java/jviewer.jnlp";
                if(this.bmcVersion.equalsIgnoreCase("03.08.0100")) {
                	this.loginData = "username="+this.userName+"&password="+this.passWord;
                }else if(this.bmcVersion.equalsIgnoreCase("03.13.0200")) {
                	//this.addCsrfToken = true;
                    this.loginData = "username=zteroot&password=nZ2MK3ly4yzff0C5cykeRIlPyLE2vyd4HbLtQa4p%2F0yn5cBmWCef%2FcS3zkH2%2FdC6xU3IcvkYeP%2BUfrGfHpsWkhL%2B%2BQoRVWtJIPM%2F9rOX2yjGeZ%2FBTht9n9rj0B6ornffEYew49twbGrt%2B0gX2sSVx9HlshDIHgbPtvnKg4l%2BGtvh4fO5oU5Mj%2BR5m8Kfq2TcX4BeKQD2G6Nwh6GbtXJPVg1j2%2FwvFMm6IC%2BKtDNAHf99dBYdolrjOCSQIxoJDMBdQwWrrw7KzP732rakUKM64WcmZZMzXl7nG%2BsN169Di5KDFPZwHDhHE5Zkv24QtSX5Jon3KoqYa10SWSWyY1dUPA%3D%3D";
                }else if(this.bmcVersion.equalsIgnoreCase("03.12.0300")) {
                	//this.addCsrfToken = true;
                	this.downloadUrl = "/Java/jviewer.jnlp?EXTRNIP="+this.host+"&JNLPSTR=JViewer";
                    this.loginData = "username=zteroot&password=nZ2MK3ly4yzff0C5cykeRIlPyLE2vyd4HbLtQa4p%2F0yn5cBmWCef%2FcS3zkH2%2FdC6xU3IcvkYeP%2BUfrGfHpsWkhL%2B%2BQoRVWtJIPM%2F9rOX2yjGeZ%2FBTht9n9rj0B6ornffEYew49twbGrt%2B0gX2sSVx9HlshDIHgbPtvnKg4l%2BGtvh4fO5oU5Mj%2BR5m8Kfq2TcX4BeKQD2G6Nwh6GbtXJPVg1j2%2FwvFMm6IC%2BKtDNAHf99dBYdolrjOCSQIxoJDMBdQwWrrw7KzP732rakUKM64WcmZZMzXl7nG%2BsN169Di5KDFPZwHDhHE5Zkv24QtSX5Jon3KoqYa10SWSWyY1dUPA%3D%3D";
                }else {
                	this.downloadUrl = "/Java/jviewer.jnlp?EXTRNIP="+this.host+"&JNLPSTR=JViewer";
                    this.loginData = "username=zteroot&password=nZ2MK3ly4yzff0C5cykeRIlPyLE2vyd4HbLtQa4p%2F0yn5cBmWCef%2FcS3zkH2%2FdC6xU3IcvkYeP%2BUfrGfHpsWkhL%2B%2BQoRVWtJIPM%2F9rOX2yjGeZ%2FBTht9n9rj0B6ornffEYew49twbGrt%2B0gX2sSVx9HlshDIHgbPtvnKg4l%2BGtvh4fO5oU5Mj%2BR5m8Kfq2TcX4BeKQD2G6Nwh6GbtXJPVg1j2%2FwvFMm6IC%2BKtDNAHf99dBYdolrjOCSQIxoJDMBdQwWrrw7KzP732rakUKM64WcmZZMzXl7nG%2BsN169Di5KDFPZwHDhHE5Zkv24QtSX5Jon3KoqYa10SWSWyY1dUPA%3D%3D";
                }
          }else{
                this.loginUrl = "/ext/session";
                this.downloadUrl = "/Java/jviewer.jnlp";
                this.loginData = "username="+this.userName+"&password="+this.passWord;
          }
        }else if (this.vendor.toLowerCase().contains("huawei")) {
             this.loginUrl = "/bmc/php/processparameter.php";
             this.downloadUrl = "/bmc/pages/remote/kvm.php?kvmway=0";
             this.loginData ="check_pwd="+this.passWord
                                +"&logtype=0&user_name="+this.userName
                                +"&func=AddSession&IsKvmApp=0";
        }else if (this.vendor.toLowerCase().contains("h3c")) {
          if (this.model.equalsIgnoreCase("uniserverr4900g3")) {
            this.useTemplate = true;
            this.addCookie = true;
            this.addCsrfToken = true;
            this.loginUrl = "/api/session";
            this.downloadUrl = "/api/settings/media/instance";
            this.extraUrl = "/api/kvm/token";
            this.loginData = "username="+this.encryStr(this.userName)
                             +"&password="+this.encryStr(this.passWord)+"&log_type=1";
          }else if(this.model.equalsIgnoreCase("uniserverr4700g3")){
            this.useTemplate = true;
            this.addCookie = true;
            this.addCsrfToken = true;
            this.loginUrl = "/api/session";
            this.downloadUrl = "/api/settings/media/instance";
            this.extraUrl = "/api/kvm/token";
            this.loginData = "username="+this.encryStr(this.userName)
                             +"&password="+this.encryStr(this.passWord)+"&log_type=1";
          }else{
            this.useTemplate = true;
            this.addCookie = true;
            this.addCsrfToken = true;
            this.loginUrl = "/api/session";
            this.downloadUrl = "/api/settings/media/instance";
            this.extraUrl = "/api/kvm/token";
            this.loginData = "username="+this.encryStr(this.userName)
                             +"&password="+this.encryStr(this.passWord)+"&log_type=1";
          }
        }else if (this.vendor.equalsIgnoreCase("dell")) {
             this.loginUrl = "/data/login";
             this.isResetDownloadUrl = true;
             this.loginData = "user="+this.userName+"&password="+this.passWord;
        }else if (this.vendor.equalsIgnoreCase("hp")) {
            this.loginUrl = "/json/login_session";
            this.downloadUrl = "/html/java_irc.html?lang=cn";
            this.useTemplate = true;
            this.loginData =  "{\n" + "\"method\": \"login\",\n"
                              + "\"user_login\": \""+this.userName+"\",\n"
                              + "\"password\": \""+this.passWord+"\"\n"
                              + "}";
        }else if (this.vendor.equalsIgnoreCase("lenovo")) {
            this.loginUrl = "/data/login";
            this.isResetDownloadUrl = true;
            this.loginData = "user="+this.userName+"&password="+this.passWord;
        }else {
            LoggerUtil.info("not match:"+this.vendor.toLowerCase()+","+this.model.toLowerCase());
            return false;
        }
        return true;
    }
    private void resetDownloadUrl() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        if(this.vendor.equalsIgnoreCase("dell")) {
            URL requestUrl = new URL("https://"+this.host+"/data?get=svcTag,sysDesc");
            this.sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
            this.sslContext.init(null, this.tm, new SecureRandom());
            this.httpsConn = (HttpsURLConnection)requestUrl.openConnection();
            this.httpsConn.setSSLSocketFactory(this.sslContext.getSocketFactory());
            String st2 = this.extraCookie.getOrDefault("ST","notfound").split(",")[1].split("=")[1];
            String st1 = this.extraCookie.getOrDefault("ST","notfound").split(",")[0].split("=")[1];
            LoggerUtil.info("ST2:"+st2);
            LoggerUtil.info("sessionValue:"+this.sessionValue);
            this.httpsConn.setRequestMethod("POST");
            this.httpsConn.setRequestProperty("Cookie", this.sessionValue);
            this.httpsConn.setRequestProperty("ST2", st2);
            int status = this.httpsConn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(this.httpsConn.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            this.httpsConn.disconnect();
            System.out.println("Response status: " + status);
            System.out.println(content.toString());
            HashMap<String, String> mysites= this.strToJson(content.toString());
            this.downloadUrl ="https://"+this.host+"/viewer.jnlp("+this.host
                    +"@0@idrac-"+mysites.get("svcTag")
                    +"%2C+PowerEdge+R730xd%2C+@1621845998222@ST1="+st1+")";
            LoggerUtil.info("downloadUrl:"+this.downloadUrl);
        }else if (this.vendor.equalsIgnoreCase("lenovo")) {
            this.downloadUrl = "https://"+this.host
                    +"/designs/imm/viewer("+this.host+"@443@0@1622021861882@1@0@0@jnlp@"
                    +this.userName
                    +"@0@0@0@0@1).jnlp?"
                    +this.extraCookie.getOrDefault("token1_name","notfound")+"="
                    +this.extraCookie.getOrDefault("token1_value","notfound");
        }
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
          }else if(this.vendor.equalsIgnoreCase("inspur")) {
	    	  String sessionCookiePattern = "'SESSION_COOKIE' : '(.*)' }";
	    	  Pattern cookie = Pattern.compile(sessionCookiePattern);
	    	  Matcher m1 = cookie.matcher(input);
	    	  if(m1.find()) {this.sessionValue="SessionCookie="+m1.group(1)+";";}
          }else if(this.vendor.equalsIgnoreCase("sugon")) {
	    	  String sessionCookiePattern = "'SESSION_COOKIE' : '(.*)' }";
	    	  Pattern cookie = Pattern.compile(sessionCookiePattern);
	    	  Matcher m1 = cookie.matcher(input);
	    	  if(m1.find()) {this.sessionValue="SessionCookie="+m1.group(1).split("'")[0]+";";}
          }else if(this.vendor.equalsIgnoreCase("CMSOFT")) {
	    	  String sessionCookiePattern = "'SESSION_COOKIE' : '(.*)' }";
	    	  Pattern cookie = Pattern.compile(sessionCookiePattern);
	    	  Matcher m1 = cookie.matcher(input);
	    	  if(m1.find()) {this.sessionValue="SessionCookie="+m1.group(1).split("'")[0]+";";}
          }
          return sites;
    }
      private void resetSession(){
          if(this.vendor.equalsIgnoreCase("h3c")) {
            this.sessionValue = "product_board_id=g3; "+"node_board_id=255; "+
                          "productID_num=16978692; "+"lang=auto; "+"safe_switch=0; "+
                          "oem_flag=1; "+"buid_time=2021; "+"extended_privilege=259; "+
                          "privilege_id=4; "+"un=admin; "+"ldap_user=0; "+
                          "ad_user=0; "+"network_access=0; "+"user_access=0; "+
                          "basic_access=0; "+"power_access=0; "+"firmware_access=0; "+
                          "health_access=0; "+"remote_access=0; "+"kvm_access=1; "+
                          "vmedia_access=1; "+"custom_id=6; "+"CSRF="+this.extraCookie.get("CSRFToken")+";"+
                          "session_id="+this.extraCookie.get("racsession_id")+"; "+this.sessionValue;
          }else if(this.vendor.equalsIgnoreCase("hp")) {
               this.sessionValue ="sessionUrl=https%253A%2F%2F"+this.host
                       +"%2F; sessionLang=en; sessionKey="+this.extraCookie.get("session_key")
                       +"; irc=last%3Djrc";
          }
      }
    public String[] run() throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        String exceptionSite = "";
        String[] downloadRes= {"",""};
        String[] doNextRes = null;
        URL requestUrl = null;
        int responseCode = 0;
        InputStream iptStream = null;
        BufferedReader response = null;
        StringBuffer sb = new StringBuffer();
        //如果addCookie为真，执行resetSession方法重写Cookies;
        if(this.addCookie) {
            this.resetSession();
        }
        //如果this.extraUrl不为空，执行doNext()方法进行二次认证;
        if (!this.extraUrl.equals("")) {
            try {
                doNextRes = this.doNext();
            } catch (Exception e) {
                LoggerUtil.error(e.getClass().getName());
            }
            if(doNextRes[0].equals("0")) {
                LoggerUtil.info("doNext: failure");
                downloadRes[0] = "0";
                downloadRes[1] = doNextRes[1];
                return downloadRes;
            };
        }
        if (this.isHttps) {
            if(this.model.equalsIgnoreCase("nf8420m3")) {
            	exceptionSite = "http://"+this.host;
            }else if (this.model.equalsIgnoreCase("nf5270m3")) {
            	exceptionSite = "http://"+this.host;
            }else if (this.model.equalsIgnoreCase("i640-g15")) {
            	exceptionSite = "http://"+this.host;
            }else {
            	exceptionSite = "https://"+this.host;
            }
            this.downloadUrl = "https://"+this.host+this.downloadUrl;
            try {
                if(this.isResetDownloadUrl)
                    this.resetDownloadUrl();
                requestUrl = new URL(this.downloadUrl);
                this.sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
                this.sslContext.init(null, this.tm, new SecureRandom());
                this.httpsConn = (HttpsURLConnection)requestUrl.openConnection();
                this.httpsConn.setSSLSocketFactory(this.sslContext.getSocketFactory());
                this.httpsConn.setRequestMethod("GET");
                //LoggerUtil.info("sessionValue:"+this.sessionValue);
                this.httpsConn.setRequestProperty("Cookie", this.sessionValue);
                if (this.addCsrfToken) {
                	//LoggerUtil.info("add X-CSRFTOKEN:"+this.extraCookie.get("CSRFToken"));
                	this.httpsConn.setRequestProperty("X-CSRFTOKEN", this.extraCookie.get("CSRFToken"));
                }
                if (this.model.equalsIgnoreCase("i640-g15")) {
                	this.httpsConn.setRequestProperty("Referer", "https://"+this.host+"/cgi/url_redirect.cgi?url_name=man_ikvm");
                }
                responseCode = this.httpsConn.getResponseCode();
                System.out.println("Downloading from host "+this.host+" jnlp...");
                iptStream = this.httpsConn.getInputStream();
                response = new BufferedReader(new InputStreamReader(iptStream, "utf-8"));
            }catch (Exception e) {
                LoggerUtil.error(e.getClass().getName());
                downloadRes[0] = "0";
                downloadRes[1] = e.getClass().getName();
                return downloadRes;
            }
        }else {
            exceptionSite = "http://"+this.host;
            this.downloadUrl = "http://"+this.host+this.downloadUrl;
            try {
                requestUrl = new URL(this.downloadUrl);
                this.httpConn = (HttpURLConnection)requestUrl.openConnection();
                this.httpConn.setRequestMethod("GET");
                //LoggerUtil.info("sessionValue:"+this.sessionValue);
                this.httpConn.setRequestProperty("Cookie", this.sessionValue);
                if (this.addCsrfToken) {
                    this.httpsConn.setRequestProperty("X-CSRFTOKEN", this.extraCookie.get("CSRFToken"));
                    //LoggerUtil.info("add X-CSRFTOKEN:"+this.extraCookie.get("CSRFToken"));
                }
                responseCode = this.httpConn.getResponseCode();
                System.out.println("Downloading from host "+this.host+" jnlp...");
                iptStream = this.httpConn.getInputStream();
                response = new BufferedReader(new InputStreamReader(iptStream, "utf-8"));
            }catch (IOException e) {
                LoggerUtil.info(e.getClass().getName());
            }
        }
        if (200 == responseCode) {
            int intC = 0;
            try {
                while ((intC = response.read()) != -1)
                {
                    char c = (char)intC;
                    sb.append(c);
                }
                response.close();
                iptStream.close();
            } catch (Exception e) {
                LoggerUtil.error(e.getClass().getName());
                downloadRes[0] = "0";
                downloadRes[1] = e.getClass().getName();
                return downloadRes;
            } 
        } 
        this.downloadRes = sb.toString();
        if(this.useTemplate) {
            JnlpTemplate myTemplate = new JnlpTemplate(this.vendor,
                                                       this.model,
                                                       this.host,
                                                       this.isHttps,
                                                       this.loginRes,
                                                       this.secondAuthRes,
                                                       this.downloadRes);
            String myjnlp = myTemplate.getTemplate();
            this.downloadRes = myjnlp;
        }
        LoggerUtil.info("downloadUrl response:"+responseCode);
        //获取java例外站点文件
        String exceptionSites =System.getProperty("user.home")
                                +File.separator+"AppData"
                                +File.separator+"LocalLow"
                                +File.separator+"Sun"
                                +File.separator+"Java"
                                +File.separator+"Deployment"
                                +File.separator+"security"
                                +File.separator+"exception.sites";
        //LoggerUtil.info("javaTempPath:"+exceptionSites);
        if (KVMUtil.isWindowsOS()) {
        File file=new File(exceptionSites);
        if(!file.exists())
        {
            try{
                file.createNewFile();
            } catch (IOException e) {
                LoggerUtil.error(e.getClass().getName());
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
                    //LoggerUtil.info("Add Java exceptionSites:"+exceptionSites);
                    FileOutputStream fo = new FileOutputStream(file);
                    OutputStreamWriter writer = new OutputStreamWriter(fo, "UTF-8");
                    writer.append(exceptionSite+"\r\n"); 
                    writer.close();
                    fo.close();
                }
           }catch(IOException e) {
               LoggerUtil.info("Add Java exceptionSites occurs error:"+e);
           }
        }}
        File f = new File("kvm.jnlp");
        FileOutputStream fop = null;
        OutputStreamWriter w = null;
        try {
            fop = new FileOutputStream(f);
            w = new OutputStreamWriter(fop, "UTF-8");
            w.append(this.downloadRes);
            w.close();
            fop.close();
        } catch (Exception e) {
            LoggerUtil.error(e.getClass().getName());
        }
        System.out.println("Download jnlp from "+this.host+" success!");
        this.MyThread();
        downloadRes[0] = String.valueOf(responseCode);
        downloadRes[1] = "success";
        return downloadRes;
    }
    private String[] doNext() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        URL requestUrl = null;
        int responseCode = 0;
        InputStream iptStream = null;
        BufferedReader response = null;
        StringBuffer sb = new StringBuffer();
        String[] nextRes = {"",""};
        if (this.isHttps) {
            this.extraUrl = "https://"+this.host+this.extraUrl;
            try {
                //LoggerUtil.info("extraUrl:"+this.extraUrl);
                requestUrl = new URL(this.extraUrl);
                this.sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
                this.sslContext.init(null, this.tm, new SecureRandom());
                this.httpsConn = (HttpsURLConnection)requestUrl.openConnection();
                this.httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
                this.httpsConn.setRequestMethod("GET");
                this.httpsConn.setRequestProperty("X-CSRFTOKEN", this.extraCookie.get("CSRFToken"));
                //LoggerUtil.info("Cookie:"+this.sessionValue);
                this.httpsConn.setRequestProperty("Cookie", this.sessionValue);
                responseCode = this.httpsConn.getResponseCode();
                iptStream = this.httpsConn.getInputStream();
                response = new BufferedReader(new InputStreamReader(iptStream, "utf-8"));
            } catch (Exception e) {
                LoggerUtil.error(e.getClass().getName());
                nextRes[0]="0";
                nextRes[1]=e.getClass().getName();
                return nextRes;
            }
        }else {
            this.extraUrl = "http://"+this.host+this.extraUrl;
            try {
                //LoggerUtil.info("extraUrl:"+this.extraUrl);
                requestUrl = new URL(this.extraUrl);
                this.httpConn = (HttpURLConnection)requestUrl.openConnection();
                this.httpConn.setRequestMethod("GET");
                this.httpConn.setRequestProperty("X-CSRFTOKEN", this.extraCookie.get("CSRFToken"));
                //LoggerUtil.info("Cookie:"+this.sessionValue);
                this.httpConn.setRequestProperty("Cookie", this.sessionValue);
                responseCode = this.httpConn.getResponseCode();
                iptStream = this.httpsConn.getInputStream();
                response = new BufferedReader(new InputStreamReader(iptStream, "utf-8"));
            } catch (Exception e) {
                LoggerUtil.error(e.getClass().getName());
                nextRes[0]="0";
                nextRes[1]=e.getClass().getName();
                return nextRes;
            }
        }
        if (200 == responseCode) {
            int intC = 0;
            try {
                while ((intC = response.read()) != -1)
                {
                  char c = (char)intC;
                  sb.append(c);
                }
                response.close();
                iptStream.close();
            }catch (Exception e) {
                LoggerUtil.error(e.getClass().getName());
                nextRes[0]="0";
                nextRes[1]=e.getClass().getName();
                return nextRes;
            } 
        } 
        this.secondAuthRes = sb.toString();
        LoggerUtil.info("Second Auth response:"+responseCode);
        nextRes[0]=String.valueOf(responseCode);
        nextRes[1]=this.secondAuthRes;
        return nextRes;
    }
    public String[] doLogin(String vendor,String model,String bmcVersion,HashMap<?, ?> extraVendor,
                            String host, String userName, String passWord, String kvmMode) throws Exception {
        this.vendor = vendor;
        this.model = model;
        this.bmcVersion = bmcVersion;
        this.extraVendor = extraVendor;
        this.host = host;
        this.userName = userName;
        this.passWord = passWord;
        //this.kvmMode = kvmMode;
        String [] GetParaFromWebOutput = {"",""};
        URL requestUrl = null;
        int responseCode = 0;
        InputStream iptStream = null;
        OutputStream opsStream = null;
        BufferedReader response = null;
        StringBuffer sb = new StringBuffer();
        if(!this.getEndPoint()) {
            GetParaFromWebOutput[0] = "0";
            GetParaFromWebOutput[1] = "notMatch";
            return GetParaFromWebOutput;
        }
        //LoggerUtil.info( "loginData:"+ this.loginData);
        byte[] PostListBypes = this.loginData.getBytes("UTF-8");
        if (this.isHttps) {
            try {
                this.loginUrl = "https://"+this.host+this.loginUrl;
                //LoggerUtil.info( "loginUrl:"+ this.loginUrl);
                requestUrl = new URL(this.loginUrl);
                this.sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
                this.sslContext.init(null, this.tm, new SecureRandom());
                HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
                this.httpsConn = (HttpsURLConnection)requestUrl.openConnection();
                this.httpsConn.setSSLSocketFactory(this.sslContext.getSocketFactory());
                this.httpsConn.setRequestMethod("POST");
                this.httpsConn.setDoOutput(true);
                if (this.vendor.equalsIgnoreCase("lenovo")) {
                    this.httpsConn.setRequestProperty("Referer", "https://"+this.host+"/designs/imm/index.php");
                }else if(this.vendor.equalsIgnoreCase("hp")) {
                    this.httpsConn.setRequestProperty("Content-Type", "application/json");
                }
                opsStream = this.httpsConn.getOutputStream();
                opsStream.write(PostListBypes);
                opsStream.close();
                responseCode = this.httpsConn.getResponseCode();
                iptStream = this.httpsConn.getInputStream();
                response = new BufferedReader(new InputStreamReader(iptStream, "utf-8"));
            }catch (Exception e) {
                LoggerUtil.error(e.getClass().getName());
                GetParaFromWebOutput[0] = "0";
                GetParaFromWebOutput[1] = e.getClass().getName();
                return GetParaFromWebOutput;
            } 
        }else {
            try {
                this.loginUrl = "http://"+this.host+this.loginUrl;
                LoggerUtil.info( "loginUrl:"+ this.loginUrl);
                requestUrl = new URL(this.loginUrl);
                this.httpConn = (HttpURLConnection)requestUrl.openConnection();
                this.httpConn.setRequestMethod("POST");
                this.httpConn.setDoOutput(true);
                opsStream = this.httpConn.getOutputStream();
                opsStream.write(this.loginData.getBytes());
                //LoggerUtil.info( "write data:"+ this.loginData.getBytes());
                opsStream.close();
                responseCode = this.httpConn.getResponseCode();
                //LoggerUtil.info( "responseCode:"+ responseCode);
                iptStream = this.httpConn.getInputStream();
                //LoggerUtil.info( "iptStream:"+ iptStream);
                response = new BufferedReader(new InputStreamReader(iptStream, "utf-8"));
                //LoggerUtil.info( "response:"+ response);
            }catch (Exception e) {
                LoggerUtil.error(e.getClass().getName());
                GetParaFromWebOutput[0] = "0";
                GetParaFromWebOutput[1] = e.getClass().getName();
                return GetParaFromWebOutput;
            } 
        }
        if(200 == responseCode) {
        	String resCookice="";
        	if (this.isHttps) {
        		resCookice = this.httpsConn.getHeaderField("Set-Cookie");
        	}else {
        		resCookice = this.httpConn.getHeaderField("Set-Cookie");
        	}
            String[] sessionId = resCookice.split(";");
            this.sessionValue = sessionId[0]+";";
            int intC = 0;
            try {
                while ((intC = response.read()) != -1)
                {
                    char c = (char)intC;
                    sb.append(c);
                }
                response.close();
                iptStream.close();
            }catch (Exception e) {
                LoggerUtil.error(e.getClass().getName());
                GetParaFromWebOutput[0] = "0";
                GetParaFromWebOutput[1] = e.getClass().getName();
                return GetParaFromWebOutput;
            } 
        } 
        this.loginRes = sb.toString();
        LoggerUtil.info("Login Response:"+responseCode);
        this.extraCookie=this.strToJson(this.loginRes);
        GetParaFromWebOutput[0] = String.valueOf(responseCode);
        GetParaFromWebOutput[1] = this.loginRes;
        System.out.println("Login in "+this.host+" ResponseCode:  "+responseCode);
        return GetParaFromWebOutput;
    }
}

