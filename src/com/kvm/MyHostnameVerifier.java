package com.kvm;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
class MyHostnameVerifier
  implements HostnameVerifier
{
  public boolean verify(String hostname, SSLSession session) {
    return true;
  }
}
