package com.kvm;
import com.library.LibException;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
class KVMAppletRefreshAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private KVMInterface kvmInterface = null;
  public KVMAppletRefreshAction(KVMInterface refer) {
    this.kvmInterface = refer;
  }
  public void actionPerformed(ActionEvent e) {
    int count = 0;
    byte[] tembladePresentInfo = new byte[2];
    this.kvmInterface.getClientSocket().getBladePresentInfo().clear();
    try {
      this.kvmInterface.getClient().sentData(this.kvmInterface.getPackData().reqBladePresent());
    }
    catch (LibException ex) {
      if ("IO_ERRCODE".equals(ex.getErrCode()))
      {
        JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface
            .getKvmUtil().getString("Network_interrupt_message"));
      }
    } 
    while (count < 1800) {
      if (this.kvmInterface.getClientSocket().getBladePresentInfo().size() != 0) {
        tembladePresentInfo = this.kvmInterface.getClientSocket().getBladePresentInfo().remove(this.kvmInterface.getClientSocket().getBladePreIndex());
        // Byte code: goto -> 359
      } 
      try {
        Thread.sleep(20L);
        count++;
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getClass().getName());
      } 
    } 
    JOptionPane.showMessageDialog(this.kvmInterface.getToolbar(), this.kvmInterface
        .getKvmUtil().getString("Network_interrupt_message"));
    ArrayList<Object> keyList = new ArrayList();
    Iterator<Object> iter = this.kvmInterface.getBase().getThreadGroup().keySet().iterator();
    this.kvmInterface.getTabbedpane().getModel().removeChangeListener((this.kvmInterface.getKvmUtil()).changeListener);
    while (iter.hasNext())
    {
      keyList.add(iter.next());
    }
    int num = this.kvmInterface.getBase().getThreadGroup().size();
    for (int i = 0; i < num; i++) {
      int bladeNO = Integer.parseInt((String)keyList.get(i));
      this.kvmInterface.getKvmUtil().disconnectBlade(bladeNO);
    } 
    keyList.clear();
    if (null != this.kvmInterface.getToolbar().getRefreshButton())
    {
      this.kvmInterface.getToolbar().getRefreshButton().setEnabled(false);
    }
    if (tembladePresentInfo != null && 
      !Arrays.equals(tembladePresentInfo, this.kvmInterface.getKvmUtil().getBladePreInfo())) {
      this.kvmInterface.getKvmUtil().setBladePreInfo(tembladePresentInfo);
      this.kvmInterface.getKvmUtil().setBladeEnable();
    } 
    if (this.kvmInterface.getBladeSize() > 1)
    {
      this.kvmInterface.getToolbar()
        .getRefreshButton()
        .setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1), 
            BorderFactory.createEmptyBorder(2, 2, 2, 2)));
    }
  }
}
