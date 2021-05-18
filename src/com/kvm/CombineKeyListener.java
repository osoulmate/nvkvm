package com.kvm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class CombineKeyListener
  implements ActionListener
{
  private final int bladeNO_CombineKeyListener;
  private final PackData pack_CombineKeyListener;
  private CombinationKey combinationKey_CombineKeyListener;
  CombineKeyListener(CombinationKey combinationKey_CombineKeyListener) {
    this.bladeNO_CombineKeyListener = combinationKey_CombineKeyListener.bladeNO_CombinationKey;
    this.pack_CombineKeyListener = combinationKey_CombineKeyListener.pack_CombinationKey;
    this.combinationKey_CombineKeyListener = combinationKey_CombineKeyListener;
  }
  public void actionPerformed(ActionEvent e) {
    byte[] combinKeyReq = null;
    if (e.getActionCommand().equals(this.combinationKey_CombineKeyListener.b1.getActionCommand())) {
      try {
        combinKeyReq = this.pack_CombineKeyListener.combinKeyCS(this.bladeNO_CombineKeyListener);
        this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu().sentData(combinKeyReq);
        Thread.sleep(100L);
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getClass().getName());
      } 
      combinKeyReq = this.pack_CombineKeyListener.resetKey(this.bladeNO_CombineKeyListener);
      this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu().sentData(combinKeyReq);
      this.combinationKey_CombineKeyListener.dispose();
      this.combinationKey_CombineKeyListener = null;
      return;
    } 
    if (e.getActionCommand().equals(this.combinationKey_CombineKeyListener.b2.getActionCommand())) {
      try {
        combinKeyReq = this.pack_CombineKeyListener.combinKeyCE(this.bladeNO_CombineKeyListener);
        this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu().sentData(combinKeyReq);
        Thread.sleep(100L);
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getClass().getName());
      } 
      combinKeyReq = this.pack_CombineKeyListener.resetKey(this.bladeNO_CombineKeyListener);
      this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu().sentData(combinKeyReq);
      this.combinationKey_CombineKeyListener.dispose();
      this.combinationKey_CombineKeyListener = null;
      return;
    } 
    if (e.getActionCommand().equals(this.combinationKey_CombineKeyListener.b3.getActionCommand())) {
      Debug.println("Ctrl+Alt+Del");
      try {
        this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu()
          .sentData(this.pack_CombineKeyListener.combinKeyCtrlAltDel(this.bladeNO_CombineKeyListener));
        Thread.sleep(100L);
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getClass().getName());
      } 
      this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu()
        .sentData(this.pack_CombineKeyListener.resetKey(this.bladeNO_CombineKeyListener));
      this.combinationKey_CombineKeyListener.dispose();
      this.combinationKey_CombineKeyListener = null;
      return;
    } 
    if (e.getActionCommand().equals(this.combinationKey_CombineKeyListener.b4.getActionCommand())) {
      try {
        combinKeyReq = this.pack_CombineKeyListener.combinKeyAT(this.bladeNO_CombineKeyListener);
        this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu().sentData(combinKeyReq);
        Thread.sleep(100L);
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getClass().getName());
      } 
      combinKeyReq = this.pack_CombineKeyListener.resetKey(this.bladeNO_CombineKeyListener);
      this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu().sentData(combinKeyReq);
      this.combinationKey_CombineKeyListener.dispose();
      this.combinationKey_CombineKeyListener = null;
      return;
    } 
    if (e.getActionCommand().equals(this.combinationKey_CombineKeyListener.b5.getActionCommand())) {
      try {
        combinKeyReq = this.pack_CombineKeyListener.combinKeyCSP(this.bladeNO_CombineKeyListener);
        this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu().sentData(combinKeyReq);
        Thread.sleep(100L);
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getClass().getName());
      } 
      combinKeyReq = this.pack_CombineKeyListener.resetKey(this.bladeNO_CombineKeyListener);
      this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu().sentData(combinKeyReq);
      this.combinationKey_CombineKeyListener.dispose();
      this.combinationKey_CombineKeyListener = null;
      return;
    } 
    if (e.getActionCommand().equals(this.combinationKey_CombineKeyListener.b6.getActionCommand())) {
      try {
        combinKeyReq = this.pack_CombineKeyListener.clearKey(this.bladeNO_CombineKeyListener);
        this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu().sentData(combinKeyReq);
        Thread.sleep(100L);
        this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu().sentData(combinKeyReq);
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getClass().getName());
      } 
      this.combinationKey_CombineKeyListener.dispose();
      this.combinationKey_CombineKeyListener = null;
      return;
    } 
    if (e.getActionCommand().equals(this.combinationKey_CombineKeyListener.send.getActionCommand())) {
      try {
        this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu()
          .sentData(this.pack_CombineKeyListener.combinKeyCustom(this.bladeNO_CombineKeyListener, this.combinationKey_CombineKeyListener));
        resetVirtualKey();
        Thread.sleep(100L);
        this.combinationKey_CombineKeyListener.bladeThread.getBladeCommu()
          .sentData(this.pack_CombineKeyListener.resetKey(this.bladeNO_CombineKeyListener));
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getClass().getName());
      } 
      this.combinationKey_CombineKeyListener.dispose();
      this.combinationKey_CombineKeyListener = null;
      return;
    } 
  }
  private void resetVirtualKey() {
    this.pack_CombineKeyListener.setMeta(0);
    this.pack_CombineKeyListener.setCustomkeystate(0);
  }
  public void remove() {
    this.combinationKey_CombineKeyListener.b1.removeActionListener(this);
    this.combinationKey_CombineKeyListener.b2.removeActionListener(this);
    this.combinationKey_CombineKeyListener.b3.removeActionListener(this);
    this.combinationKey_CombineKeyListener.b4.removeActionListener(this);
    this.combinationKey_CombineKeyListener.b5.removeActionListener(this);
    this.combinationKey_CombineKeyListener.b6.removeActionListener(this);
    this.combinationKey_CombineKeyListener.send.removeActionListener(this);
    this.combinationKey_CombineKeyListener.bPanel.removeAll();
    this.combinationKey_CombineKeyListener.tPanel.removeAll();
    this.combinationKey_CombineKeyListener.bPanel1.removeAll();
    this.combinationKey_CombineKeyListener.bPanel2.removeAll();
    this.combinationKey_CombineKeyListener.removeAll();
    if (!this.combinationKey_CombineKeyListener.kvmInterface.isFullScreen()) {
      this.combinationKey_CombineKeyListener.kvmInterface.getKvmUtil()
        .getImagePane(this.bladeNO_CombineKeyListener)
        .requestFocus();
    }
    else {
      ImagePane imagePane = this.combinationKey_CombineKeyListener.kvmInterface.getKvmUtil().getImagePane(this.bladeNO_CombineKeyListener);
      if (imagePane.isNew())
      {
        if (this.combinationKey_CombineKeyListener.kvmInterface.getBase().isMstsc()) {
          this.combinationKey_CombineKeyListener.kvmInterface.getFullScreen()
            .setCursor(this.combinationKey_CombineKeyListener.kvmInterface.getBase().getDefCursor());
          imagePane.setCursor(this.combinationKey_CombineKeyListener.kvmInterface.getBase().getDefCursor());
        }
        else {
          this.combinationKey_CombineKeyListener.kvmInterface.getFullScreen()
            .setCursor((this.combinationKey_CombineKeyListener.kvmInterface.getBase()).myCursor);
          imagePane.setCursor((this.combinationKey_CombineKeyListener.kvmInterface.getBase()).myCursor);
        } 
      }
    } 
  }
  public void removeAll() {
    this.combinationKey_CombineKeyListener.b1.removeActionListener(this);
    this.combinationKey_CombineKeyListener.b2.removeActionListener(this);
    this.combinationKey_CombineKeyListener.b3.removeActionListener(this);
    this.combinationKey_CombineKeyListener.b4.removeActionListener(this);
    this.combinationKey_CombineKeyListener.b5.removeActionListener(this);
    this.combinationKey_CombineKeyListener.b6.removeActionListener(this);
    this.combinationKey_CombineKeyListener.send.removeActionListener(this);
    this.combinationKey_CombineKeyListener.bPanel1.removeAll();
    this.combinationKey_CombineKeyListener.bPanel2.removeAll();
    this.combinationKey_CombineKeyListener.bPanel.removeAll();
    this.combinationKey_CombineKeyListener.tPanel.removeAll();
    this.combinationKey_CombineKeyListener.removeAll();
  }
}
