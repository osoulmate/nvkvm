package com.kvm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
class SelfAdaptionAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  KeyboardPopupMenu keyboardPopupMenu = null;
  public SelfAdaptionAction(KeyboardPopupMenu keyboardPopupMenu) {
    this.keyboardPopupMenu = keyboardPopupMenu;
  }
  public void actionPerformed(ActionEvent e) {
    KeyboardPopupMenu.setSelected(this.keyboardPopupMenu, this.keyboardPopupMenu.selfAdaptionMenu);
    Base.setKeyboardLayout(1);
  }
}
