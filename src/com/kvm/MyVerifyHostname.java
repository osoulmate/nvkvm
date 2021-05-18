package com.kvm;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
class MyVerifyHostname
  implements HostnameVerifier
{
  String loc_host = "";
  public MyVerifyHostname(String input) {
    this.loc_host = input;
  }
  public boolean verify(String arg0, SSLSession arg1) {
    if (arg0.equals(this.loc_host)) {
      return true;
    }
    return false;
  }
}
