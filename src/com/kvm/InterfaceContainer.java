package com.kvm;
import javax.swing.JButton;
public class InterfaceContainer
{
  private int bladeNO = 0;
  private JButton bladeButton = null;
  private KVMInterface kvmInterface = null;
  private ImagePane imagePane = null;
  private FloatToolbar flatToolbar = null;
  private VirtualMedia virtualMedia = null;
  private int bRelPosition = 0;
  private int codeKey = -1;
  public int getBRelPosition() {
    return this.bRelPosition;
  }
  public void setBRelPosition(int relPosition) {
    this.bRelPosition = relPosition;
  }
  public int getBladeNumber() {
    return this.bladeNO;
  }
  public void setBladeNumber(int bladeNO) {
    this.bladeNO = bladeNO;
  }
  public KVMInterface getKvmInterface() {
    return this.kvmInterface;
  }
  public void setKvmInterface(KVMInterface kvmInterface2) {
    this.kvmInterface = kvmInterface2;
  }
  public ImagePane getImagePane() {
    return this.imagePane;
  }
  public void setImagePane(ImagePane imagePane) {
    this.imagePane = imagePane;
  }
  public VirtualMedia getVirtualMedia() {
    return this.virtualMedia;
  }
  public void setVirtualMedia(VirtualMedia virtualMedia) {
    this.virtualMedia = virtualMedia;
  }
  public JButton getBladeButton() {
    return this.bladeButton;
  }
  public void setBladeButton(JButton bladeButton) {
    this.bladeButton = bladeButton;
  }
  public FloatToolbar getFloatToolbar() {
    return this.flatToolbar;
  }
  public void setFloatToolbar(FloatToolbar flatToolbar) {
    this.flatToolbar = flatToolbar;
  }
  public int getCodeKey() {
    if (this.codeKey == -1)
    {
      if (this.kvmInterface != null)
        this.codeKey = this.kvmInterface.getCodeKey(); 
    }
    return this.codeKey;
  }
  public void setCodeKey(int codeKey) {
    this.codeKey = codeKey;
  }
}
