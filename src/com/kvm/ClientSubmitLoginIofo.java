package com.kvm;
import com.library.LoggerUtil;
import java.awt.Font;
import java.awt.Panel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
class ClientSubmitLoginIofo
{
  public String getParaFromWeb(String host, String userName, String passWord, String KvmMode, int port, LoginUtil loginUtil) throws Exception {
    String GetParaFromWebOutput = "";
    String ReturnFlag = "gologin";
    String url = "https://" + host + ":" + String.valueOf(port) + "/bmc/php/processparameter.php";
    URL requestUrl = null;
    TrustManager[] tm = { new MyX509TrustManager() };
    SSLContext sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
    sslContext.init(null, tm, new SecureRandom());
    StringBuffer PostList = new StringBuffer();
    PostList.append("check_pwd")
      .append("=")
      .append(passWord)
      .append("&")
      .append("logtype")
      .append("=")
      .append("1")
      .append("&")
      .append("user_name")
      .append("=")
      .append(userName)
      .append("&")
      .append("func")
      .append("=")
      .append("DirectKVM")
      .append("&")
      .append("IsKvmApp")
      .append("=")
      .append("1")
      .append("&")
      .append("KvmMode")
      .append("=")
      .append(KvmMode);
    LoggerUtil.info( "PostList: "+ PostList );
    byte[] PostListBypes = PostList.toString().getBytes("UTF-8");
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
    int n = 0;
    JLabel importChain = new JLabel(loginUtil.getString("importChain"));
    importChain.setFont(new Font(loginUtil.getString("font_name"), 0, Main.wordSize));
    if (!HasCertFlag) {
      Object[] options = { "", "", "" };
      options[0] = loginUtil.getString("import_option_yes");
      options[1] = loginUtil.getString("import_option_no");
      options[2] = loginUtil.getString("import_option_import");
      LoggerUtil.info( "Object[] options: "+ options[0] + "," + options[1] + "," + options[2] );
      n = JOptionPane.showOptionDialog(new Panel(), importChain, loginUtil
          .getString("security_remind"), 0, 3, null, options, options[0]);
      LoggerUtil.info( "security_remind: n is :"+ n );
    } 
    if (n == 1 || n == -1)
    {
      return ReturnFlag;
    }
    if (n == 2) {
      InstallCert inCert = new InstallCert();
      try {
        inCert.installKeyStore(host, port, loginUtil);
        LoggerUtil.info( "inCert.installKeyStore: "+ host +  port  + loginUtil );
      }
      catch (Exception e1) {
        LoggerUtil.error(e1.getClass().getName());
      } 
      return ReturnFlag;
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
    }
    catch (Exception e) {
      return GetParaFromWebOutput;
    } 
    response = new BufferedReader(new InputStreamReader(iptStream, "utf-8"));
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
    LoggerUtil.info( "temp: "+ temp );
    GetParaFromWebOutput = GetParaFromWebOutput + temp;
    return GetParaFromWebOutput;
  }
}
