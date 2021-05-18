package com.kvmV1;
public class BladeState
{
  private boolean enable = false;
  private String bladeIP = "";
  private int bladePort = 0;
  private boolean isNew = false;
  public String getBladeIP() {
    return this.bladeIP;
  }
  public void setBladeIP(String bladeIP) {
    this.bladeIP = bladeIP;
  }
  public int getBladePort() {
    return this.bladePort;
  }
  public void setBladePort(int bladePort) {
    this.bladePort = bladePort;
  }
  public boolean isEnable() {
    return this.enable;
  }
  public void setEnable(boolean enable) {
    this.enable = enable;
  }
  public boolean isNew() {
    return this.isNew;
  }
  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }
  public boolean getSecureKvm() {
    return Base.securekvm;
  }
  public boolean getSecureVmm() {
    return Base.securevmm;
  }
  public boolean getAuthVmm() {
    return true;
  }
}
