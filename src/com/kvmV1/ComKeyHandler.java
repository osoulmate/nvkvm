package com.kvmV1;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;
class ComKeyHandler
  implements KeyListener
{
  private CombinationKey combinationKey;
  public ComKeyHandler(CombinationKey combinationKey) {
    this.combinationKey = combinationKey;
  }
  public void keyPressed(KeyEvent event) {
    JTextField textField = (JTextField)event.getComponent();
    textField.setText("");
    if ((event.isActionKey() || event.isAltDown() || event.isAltGraphDown() || event.getKeyCode() == 27 || event.getKeyCode() == 127 || event.getKeyCode() == 10 || event.getKeyCode() == 32 || event.isControlDown() || event.isMetaDown() || event.isShiftDown()) && event.getKeyCode() != 524 && event.getKeyCode() != 525) {
      if (!event.isShiftDown() || event.isActionKey())
      {
        textField.setText(KeyEvent.getKeyText(event.getKeyCode()));
      }
      if ((event.isShiftDown() && (event.getKeyCode() == 27 || event.getKeyCode() == 17 || event.getKeyCode() == 18 || event.getKeyCode() == 10 || event.getKeyCode() == 127 || event.getKeyCode() == 32)) || (event.isShiftDown() && event.getKeyCode() == 16))
      {
        textField.setText(KeyEvent.getKeyText(event.getKeyCode()));
      }
    } 
    if (textField == this.combinationKey.textField1)
    {
      if (event.getKeyCode() == 8) {
        this.combinationKey.keyValue1 = 0;
      }
      else {
        this.combinationKey.keyValue1 = KVMUtil.translateToUSBCode(event);
      } 
    }
    if (textField == this.combinationKey.textField2)
    {
      if (event.getKeyCode() == 8) {
        this.combinationKey.keyValue2 = 0;
      }
      else {
        this.combinationKey.keyValue2 = KVMUtil.translateToUSBCode(event);
      } 
    }
    if (textField == this.combinationKey.textField3)
    {
      if (event.getKeyCode() == 8) {
        this.combinationKey.keyValue3 = 0;
      }
      else {
        this.combinationKey.keyValue3 = KVMUtil.translateToUSBCode(event);
      } 
    }
    if (textField == this.combinationKey.textField4)
    {
      if (event.getKeyCode() == 8) {
        this.combinationKey.keyValue4 = 0;
      }
      else {
        this.combinationKey.keyValue4 = KVMUtil.translateToUSBCode(event);
      } 
    }
    if (textField == this.combinationKey.textField5)
    {
      if (event.getKeyCode() == 8) {
        this.combinationKey.keyValue5 = 0;
      }
      else {
        this.combinationKey.keyValue5 = KVMUtil.translateToUSBCode(event);
      } 
    }
    if (textField == this.combinationKey.textField6)
    {
      if (event.getKeyCode() == 8) {
        this.combinationKey.keyValue6 = 0;
      }
      else {
        this.combinationKey.keyValue6 = KVMUtil.translateToUSBCode(event);
      } 
    }
  }
  public void keyReleased(KeyEvent event) {
    if (event.getKeyCode() == 154) {
      JTextField textField = (JTextField)event.getComponent();
      textField.setText("");
      textField.setText(KeyEvent.getKeyText(event.getKeyCode()));
      if (textField == this.combinationKey.textField1)
      {
        this.combinationKey.keyValue1 = 70;
      }
      if (textField == this.combinationKey.textField2)
      {
        this.combinationKey.keyValue2 = 70;
      }
      if (textField == this.combinationKey.textField3)
      {
        this.combinationKey.keyValue3 = 70;
      }
      if (textField == this.combinationKey.textField4)
      {
        this.combinationKey.keyValue4 = 70;
      }
      if (textField == this.combinationKey.textField5)
      {
        this.combinationKey.keyValue5 = 70;
      }
      if (textField == this.combinationKey.textField6)
      {
        this.combinationKey.keyValue6 = 70;
      }
    } 
  }
  public void keyTyped(KeyEvent event) {}
}
