package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class FrenchKeyboardAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  KeyboardPopupMenu keyboardPopupMenu = null;
  public FrenchKeyboardAction(KeyboardPopupMenu keyboardPopupMenu) {
    this.keyboardPopupMenu = keyboardPopupMenu;
  }
  public void actionPerformed(ActionEvent e) {
    KeyboardPopupMenu.setSelected(this.keyboardPopupMenu, this.keyboardPopupMenu.frenchKeyboardMenu);
    Base.setKeyboardLayout(3);
  }
}
