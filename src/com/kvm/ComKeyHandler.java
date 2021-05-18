package com.kvm;
import com.huawei.vm.console.utils.TestPrint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;
class ComKeyHandler
  implements KeyListener
{
  private CombinationKey combinationKey_ComKeyHandler;
  ComKeyHandler(CombinationKey combinationKey_ComKeyHandler) {
    this.combinationKey_ComKeyHandler = combinationKey_ComKeyHandler;
  }
  public void keyPressed(KeyEvent event) {
    if (null == event.getComponent()) {
      TestPrint.println(3, "event.getComponent() is null.");
      return;
    } 
    JTextField textField = (JTextField)event.getComponent();
    if (textField == null) {
      return;
    }
    textField.setText("");
    if ((event.isActionKey() || event.isAltDown() || event.isAltGraphDown() || event
      .getKeyCode() == 27 || event.getKeyCode() == 127 || event
      .getKeyCode() == 10 || event.getKeyCode() == 32 || event
      .isControlDown() || event.isMetaDown() || event.isShiftDown()) && event
      .getKeyCode() != 524 && event.getKeyCode() != 525) {
      if (!event.isShiftDown() || event.isActionKey())
      {
        textField.setText(KeyEvent.getKeyText(event.getKeyCode()));
      }
      if ((event.isShiftDown() && (event.getKeyCode() == 27 || event
        .getKeyCode() == 17 || event.getKeyCode() == 18 || event
        .getKeyCode() == 10 || event.getKeyCode() == 127 || event.getKeyCode() == 32)) || (event
        .isShiftDown() && event.getKeyCode() == 16))
      {
        textField.setText(KeyEvent.getKeyText(event.getKeyCode()));
      }
    } 
    if (this.combinationKey_ComKeyHandler.textField1 == textField)
    {
      if (event.getKeyCode() == 8) {
        this.combinationKey_ComKeyHandler.keyValue1 = 0;
      }
      else {
        this.combinationKey_ComKeyHandler.keyValue1 = KVMUtil.translateToUSBCode(event);
      } 
    }
    if (this.combinationKey_ComKeyHandler.textField2 == textField)
    {
      if (event.getKeyCode() == 8) {
        this.combinationKey_ComKeyHandler.keyValue2 = 0;
      }
      else {
        this.combinationKey_ComKeyHandler.keyValue2 = KVMUtil.translateToUSBCode(event);
      } 
    }
    if (this.combinationKey_ComKeyHandler.textField3 == textField)
    {
      if (event.getKeyCode() == 8) {
        this.combinationKey_ComKeyHandler.keyValue3 = 0;
      }
      else {
        this.combinationKey_ComKeyHandler.keyValue3 = KVMUtil.translateToUSBCode(event);
      } 
    }
    if (this.combinationKey_ComKeyHandler.textField4 == textField)
    {
      if (event.getKeyCode() == 8) {
        this.combinationKey_ComKeyHandler.keyValue4 = 0;
      }
      else {
        this.combinationKey_ComKeyHandler.keyValue4 = KVMUtil.translateToUSBCode(event);
      } 
    }
    if (this.combinationKey_ComKeyHandler.textField5 == textField)
    {
      if (event.getKeyCode() == 8) {
        this.combinationKey_ComKeyHandler.keyValue5 = 0;
      }
      else {
        this.combinationKey_ComKeyHandler.keyValue5 = KVMUtil.translateToUSBCode(event);
      } 
    }
    if (this.combinationKey_ComKeyHandler.textField6 == textField)
    {
      if (event.getKeyCode() == 8) {
        this.combinationKey_ComKeyHandler.keyValue6 = 0;
      }
      else {
        this.combinationKey_ComKeyHandler.keyValue6 = KVMUtil.translateToUSBCode(event);
      } 
    }
  }
  public void keyReleased(KeyEvent event) {
    if (event.getKeyCode() == 154) {
      if (null == event.getComponent()) {
        TestPrint.println(3, "event.getComponent() is null.");
        return;
      } 
      JTextField textField = (JTextField)event.getComponent();
      if (textField == null) {
        return;
      }
      textField.setText("");
      textField.setText(KeyEvent.getKeyText(event.getKeyCode()));
      if (this.combinationKey_ComKeyHandler.textField1 == textField)
      {
        this.combinationKey_ComKeyHandler.keyValue1 = 70;
      }
      if (this.combinationKey_ComKeyHandler.textField2 == textField)
      {
        this.combinationKey_ComKeyHandler.keyValue2 = 70;
      }
      if (this.combinationKey_ComKeyHandler.textField3 == textField)
      {
        this.combinationKey_ComKeyHandler.keyValue3 = 70;
      }
      if (this.combinationKey_ComKeyHandler.textField4 == textField)
      {
        this.combinationKey_ComKeyHandler.keyValue4 = 70;
      }
      if (this.combinationKey_ComKeyHandler.textField5 == textField)
      {
        this.combinationKey_ComKeyHandler.keyValue5 = 70;
      }
      if (this.combinationKey_ComKeyHandler.textField6 == textField)
      {
        this.combinationKey_ComKeyHandler.keyValue6 = 70;
      }
    } 
  }
  public void keyTyped(KeyEvent event) {}
}
