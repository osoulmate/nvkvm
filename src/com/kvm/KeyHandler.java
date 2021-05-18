package com.kvm;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
class KeyHandler
  implements KeyListener
{
  private boolean num_lock_flag;
  private boolean cpas_lock_flag;
  private boolean scroll_lock_flag;
  private ImagePane imagePane = null;
  private int tab_pressed_flag;
  private int last_pressed_key_code;
  public KeyHandler(ImagePane refer) {
    this.num_lock_flag = false;
    this.cpas_lock_flag = false;
    this.scroll_lock_flag = false;
    this.tab_pressed_flag = 0;
    this.last_pressed_key_code = 0;
    this.imagePane = refer;
  }
  public void keyPressed(KeyEvent event) {
    this.last_pressed_key_code = event.getKeyCode();
    if (this.imagePane.getKvmInterface().getiImageFocused() == 1 && (144 != event
      .getKeyCode() || 20 != event.getKeyCode() || 145 != event.getKeyCode())) {
      this.imagePane.getKvmInterface().setiKeyPressControl(this.imagePane.getKvmInterface().getiKeyPressControl() + 1);
      if (this.imagePane.getKvmInterface().getiKeyPressControl() <= this.imagePane.getKvmInterface().getiKeyPressTotal() && 
        KVMUtil.isWindowsOS()) {
        return;
      }
      this.imagePane.getKvmInterface().setiKeyPressControl(0);
      this.imagePane.getKvmInterface().setiKeyPressTotal(0);
      this.imagePane.getKvmInterface().setiImageFocused(2);
    } 
    if ((144 == event.getKeyCode() && this.num_lock_flag) || (20 == event
      .getKeyCode() && this.cpas_lock_flag) || (145 == event
      .getKeyCode() && this.scroll_lock_flag)) {
      return;
    }
    if (144 == event.getKeyCode()) {
      this.num_lock_flag = true;
    }
    else if (20 == event.getKeyCode()) {
      this.cpas_lock_flag = true;
    }
    else if (145 == event.getKeyCode()) {
      this.scroll_lock_flag = true;
    } 
    if (event.isControlDown() && event.isAltDown() && event.isShiftDown() && this.imagePane
      .getKvmInterface().isFullScreen()) {
      this.imagePane.getKvmInterface().getFullScreen().setCursor(this.imagePane.getKvmInterface().getBase().getDefCursor());
      this.imagePane.setCursor(this.imagePane.getKvmInterface().getBase().getDefCursor());
      MouseDisplacementImpl.setMode(0);
      this.imagePane.getKvmInterface().getFullScreen().getToolBarFrame().setVisible(true);
      return;
    } 
    if (this.imagePane.isFocusChange() && Base.isSingleMouse() && event.isControlDown() && event.isAltDown() && event
      .isShiftDown()) {
      this.imagePane.setFocusChange(false);
      this.imagePane.setCursor(this.imagePane.getKvmInterface().getBase().getDefCursor());
      this.imagePane.getKvmInterface().setCursor(this.imagePane.getKvmInterface().getBase().getDefCursor());
    } 
    byte[] keyData = this.imagePane.getPack().getKeyData();
    if (this.imagePane.isNew()) {
      if (17 != event.getKeyCode() && 16 != event.getKeyCode() && 18 != event
        .getKeyCode() && 65406 != event.getKeyCode() && 524 != event
        .getKeyCode()) {
        int usbCode = KVMUtil.translateToUSBCode(event);
        for (int j = 2; j < keyData.length; j++) {
          if (keyData[j] == usbCode) {
            this.imagePane.getBladeThread()
              .getBladeCommu()
              .sentData(this.imagePane.getPack().keyRePressedPack(event, this.imagePane
                  .getBladeNumber(), this.imagePane
                  .isNew()));
            return;
          } 
        } 
      } else {
        int mask = 0;
        int shift = (event.getKeyLocation() == 2) ? 0 : 4;
        switch (event.getKeyCode()) {
          case 17:
            mask = 1 << shift;
            break;
          case 16:
            mask = 2 << shift;
            break;
          case 18:
            mask = 4 << shift;
            break;
          case 65406:
            mask = 64;
            break;
          case 157:
          case 524:
            mask = 128 << shift;
            break;
        } 
        if ((keyData[0] & mask) == mask) {
          return;
        }
        if (17 == event.getKeyCode() && (keyData[0] & 0x40) == 64) {
          return;
        }
      } 
      this.imagePane.getBladeThread()
        .getBladeCommu()
        .sentData(this.imagePane.getPack().keyPressedPack(event, this.imagePane.getBladeNumber(), this.imagePane.isNew()));
    }
    else {
      for (int j = 2; j < keyData.length; j++) {
        if (keyData[j] == KVMUtil.translateToUSBCode(event)) {
          return;
        }
      } 
      this.imagePane.getBladeThread()
        .getBladeCommu()
        .sentData(this.imagePane.getPack().keyPressedPack(event, this.imagePane.getBladeNumber(), this.imagePane.isNew()));
    } 
    keyData = this.imagePane.getPack().getKeyData();
    for (int i = 2; i < keyData.length; i++)
    {
      keyData[i] = 0;
    }
    this.imagePane.getPack().setKeyData(keyData);
    if (20 == event.getKeyCode() && KVMUtil.isMacOS())
    {
      keyReleased(event);
    }
  }
  public void keyReleased(KeyEvent event) {
    if (KVMUtil.isLinux() || KVMUtil.isWindowsOS()) {
      if ((130 == event.getKeyCode() && this.last_pressed_key_code != 130) || (91 == event
        .getKeyCode() && this.last_pressed_key_code != 91) || (154 == event
        .getKeyCode() && this.last_pressed_key_code != 154) || (9 == event
        .getKeyCode() && this.last_pressed_key_code != 9 && this.tab_pressed_flag == 1))
      {
        keyPressed(event);
      }
      this.tab_pressed_flag = 0;
      this.last_pressed_key_code = 0;
    } 
    if (20 == event.getKeyCode() && KVMUtil.isMacOS())
    {
      keyPressed(event);
    }
    if (144 == event.getKeyCode()) {
      this.num_lock_flag = false;
    }
    else if (20 == event.getKeyCode()) {
      this.cpas_lock_flag = false;
    }
    else if (145 == event.getKeyCode()) {
      this.scroll_lock_flag = false;
    } 
    if (this.imagePane.isControl())
    {
      if (this.imagePane.isNew()) {
        if (17 == event.getKeyCode() || 18 == event.getKeyCode() || 16 == event
          .getKeyCode() || 65406 == event.getKeyCode() || 157 == event
          .getKeyCode() || 524 == event.getKeyCode())
        {
          this.imagePane.getBladeThread()
            .getBladeCommu()
            .sentData(this.imagePane.getPack().clearKey(this.imagePane.getBladeNumber()));
        }
        else
        {
          this.imagePane.getPack().keyReleasedPack(event, this.imagePane.getBladeNumber(), this.imagePane.isNew());
        }
      }
      else if (event.getKeyCode() == 154) {
        this.imagePane.getBladeThread()
          .getBladeCommu()
          .sentData(this.imagePane.getPack().keyPressedPack(event, this.imagePane.getBladeNumber(), this.imagePane.isNew()));
        try {
          Thread.sleep(100L);
        }
        catch (InterruptedException e1) {
          Debug.printExc(e1.getClass().getName());
        } 
        this.imagePane.getBladeThread()
          .getBladeCommu()
          .sentData(this.imagePane.getPack().keyReleasedPack(event, this.imagePane.getBladeNumber(), this.imagePane.isNew()));
      }
      else {
        this.imagePane.getBladeThread()
          .getBladeCommu()
          .sentData(this.imagePane.getPack().keyReleasedPack(event, this.imagePane.getBladeNumber(), this.imagePane.isNew()));
      } 
    }
  }
  public void keyTyped(KeyEvent event) {
    this.tab_pressed_flag = 1;
  }
}
