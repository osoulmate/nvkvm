package com.kvmV1;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class CombineKeyListener
  implements ActionListener
{
  private final int bladeNO;
  private final PackData pack;
  private CombinationKey combinationKey;
  CombineKeyListener(CombinationKey combinationKey) {
    this.bladeNO = combinationKey.bladeNO;
    this.pack = combinationKey.pack;
    this.combinationKey = combinationKey;
  }
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(this.combinationKey.b1.getActionCommand())) {
      Debug.println("Ctrl+Shift");
      byte[] combinKeyReq = this.pack.combinKeyCS(this.bladeNO);
      try {
        this.combinationKey.bladeThread.bladeCommu.sentData(combinKeyReq);
        Thread.sleep(100L);
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getMessage());
      } 
      combinKeyReq = this.pack.resetKey(this.bladeNO);
      this.combinationKey.bladeThread.bladeCommu.sentData(combinKeyReq);
      this.combinationKey.dispose();
      this.combinationKey = null;
      return;
    } 
    if (e.getActionCommand().equals(this.combinationKey.b2.getActionCommand())) {
      Debug.println("Ctrl+Esc");
      byte[] combinKeyReq = this.pack.combinKeyCE(this.bladeNO);
      try {
        this.combinationKey.bladeThread.bladeCommu.sentData(combinKeyReq);
        Thread.sleep(100L);
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getMessage());
      } 
      combinKeyReq = this.pack.resetKey(this.bladeNO);
      this.combinationKey.bladeThread.bladeCommu.sentData(combinKeyReq);
      this.combinationKey.dispose();
      this.combinationKey = null;
      return;
    } 
    if (e.getActionCommand().equals(this.combinationKey.b3.getActionCommand())) {
      Debug.println("Ctrl+Alt+Del");
      try {
        this.combinationKey.bladeThread.bladeCommu.sentData(this.pack.combinKeyCtrlAltDel(this.bladeNO));
        Thread.sleep(50L);
        this.combinationKey.bladeThread.bladeCommu.sentData(this.pack.combinKeyCtrlAlt(this.bladeNO));
        Thread.sleep(50L);
        this.combinationKey.bladeThread.bladeCommu.sentData(this.pack.combinKeyCtrlAltDel(this.bladeNO));
        Thread.sleep(100L);
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getMessage());
      } 
      this.combinationKey.bladeThread.bladeCommu.sentData(this.pack.resetKey(this.bladeNO));
      this.combinationKey.dispose();
      this.combinationKey = null;
      return;
    } 
    if (e.getActionCommand().equals(this.combinationKey.b4.getActionCommand())) {
      Debug.println("Alt+Tab");
      byte[] combinKeyReq = this.pack.combinKeyAT(this.bladeNO);
      try {
        this.combinationKey.bladeThread.bladeCommu.sentData(combinKeyReq);
        Thread.sleep(100L);
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getMessage());
      } 
      combinKeyReq = this.pack.resetKey(this.bladeNO);
      this.combinationKey.bladeThread.bladeCommu.sentData(combinKeyReq);
      this.combinationKey.dispose();
      this.combinationKey = null;
      return;
    } 
    if (e.getActionCommand().equals(this.combinationKey.b5.getActionCommand())) {
      Debug.println("Ctrl+Space");
      byte[] combinKeyReq = this.pack.combinKeyCSP(this.bladeNO);
      try {
        this.combinationKey.bladeThread.bladeCommu.sentData(combinKeyReq);
        Thread.sleep(100L);
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getMessage());
      } 
      combinKeyReq = this.pack.resetKey(this.bladeNO);
      this.combinationKey.bladeThread.bladeCommu.sentData(combinKeyReq);
      this.combinationKey.dispose();
      this.combinationKey = null;
      return;
    } 
    if (e.getActionCommand().equals(this.combinationKey.b6.getActionCommand())) {
      Debug.println("ResetKeyBoard");
      byte[] combinKeyReq = this.pack.clearKey(this.bladeNO);
      try {
        this.combinationKey.bladeThread.bladeCommu.sentData(combinKeyReq);
        Thread.sleep(100L);
        this.combinationKey.bladeThread.bladeCommu.sentData(combinKeyReq);
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getMessage());
      } 
      this.combinationKey.dispose();
      this.combinationKey = null;
      return;
    } 
    if (e.getActionCommand().equals(this.combinationKey.send.getActionCommand())) {
      try {
        this.combinationKey.bladeThread.bladeCommu.sentData(this.pack.combinKeyCustom(this.bladeNO, this.combinationKey));
        resetVirtualKey();
        Thread.sleep(100L);
        this.combinationKey.bladeThread.bladeCommu.sentData(this.pack.resetKey(this.bladeNO));
      }
      catch (InterruptedException e1) {
        Debug.printExc(e1.getMessage());
      } 
      this.combinationKey.dispose();
      this.combinationKey = null;
      return;
    } 
  }
  private void resetVirtualKey() {
    this.pack.customkeystate = 0;
  }
  public void remove() {
    this.combinationKey.b1.removeActionListener(this);
    this.combinationKey.b2.removeActionListener(this);
    this.combinationKey.b3.removeActionListener(this);
    this.combinationKey.b4.removeActionListener(this);
    this.combinationKey.b5.removeActionListener(this);
    this.combinationKey.b6.removeActionListener(this);
    this.combinationKey.send.removeActionListener(this);
    this.combinationKey.bPanel1.removeAll();
    this.combinationKey.bPanel2.removeAll();
    this.combinationKey.bPanel.removeAll();
    this.combinationKey.tPanel.removeAll();
    this.combinationKey.removeAll();
    if (!this.combinationKey.kvmInterface.isFullScreen) {
      this.combinationKey.kvmInterface.kvmUtil.getImagePane(this.bladeNO).requestFocus();
    }
    else {
      ImagePane imagePane = this.combinationKey.kvmInterface.kvmUtil.getImagePane(this.bladeNO);
      if (imagePane.isNew())
      {
        if (this.combinationKey.kvmInterface.base.isMstsc) {
          this.combinationKey.kvmInterface.fullScreen.setCursor(this.combinationKey.kvmInterface.base.defCursor);
          imagePane.setCursor(this.combinationKey.kvmInterface.base.defCursor);
        }
        else {
          this.combinationKey.kvmInterface.fullScreen.setCursor(this.combinationKey.kvmInterface.base.myCursor);
          imagePane.setCursor(this.combinationKey.kvmInterface.base.myCursor);
        } 
      }
    } 
  }
  public void removeAll() {
    this.combinationKey.b1.removeActionListener(this);
    this.combinationKey.b2.removeActionListener(this);
    this.combinationKey.b3.removeActionListener(this);
    this.combinationKey.b4.removeActionListener(this);
    this.combinationKey.b5.removeActionListener(this);
    this.combinationKey.b6.removeActionListener(this);
    this.combinationKey.send.removeActionListener(this);
    this.combinationKey.bPanel1.removeAll();
    this.combinationKey.bPanel2.removeAll();
    this.combinationKey.bPanel.removeAll();
    this.combinationKey.tPanel.removeAll();
    this.combinationKey.removeAll();
  }
}
