package com.kvm;
import com.huawei.vm.console.ui.JAVAFileFileter;
import com.library.LoggerUtil;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
class ChooseAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private ImageFile imageFile_ChooseAction = null;
  public ChooseAction(ImageFile refer) {
    this.imageFile_ChooseAction = refer;
  }
  public void actionPerformed(ActionEvent e) {
    JFileChooser choose = new JFileChooser(this.imageFile_ChooseAction.getImage());
    choose.addChoosableFileFilter((FileFilter)new JAVAFileFileter("img", "iso"));
    choose.setFileSelectionMode(0);
    int returnval = choose.showSaveDialog(choose);
    if (returnval == 0) {
      String cdRomdervaer = this.imageFile_ChooseAction.getVmApplet().getCDROMDevices();
      String derver = (String)this.imageFile_ChooseAction.getImageCheck().getSelectedItem();
      if (cdRomdervaer.contains(derver)) {
        String absolutePath = null;
        try {
          absolutePath = choose.getSelectedFile().getCanonicalPath().toLowerCase(this.imageFile_ChooseAction.getLocale());
        }
        catch (IOException e1) {
          LoggerUtil.error("Invalid file name");
        } 
        if (absolutePath == null)
        {
          LoggerUtil.error("image path is incorrect!");
        }
        else
        {
          int length = absolutePath.length();
          int isoIndex = absolutePath.lastIndexOf(".iso");
          int imgIndex = absolutePath.lastIndexOf(".img");
          if (-1 != isoIndex || isoIndex + 4 == length) {
            try
            {
              this.imageFile_ChooseAction.getImagePath().setText(choose.getSelectedFile().getCanonicalPath());
            }
            catch (IOException e1)
            {
              LoggerUtil.error("Invalid file name");
            }
          } else if (-1 != imgIndex || imgIndex + 4 == length) {
            try {
              this.imageFile_ChooseAction.getImagePath().setText(choose.getSelectedFile().getCanonicalPath());
            }
            catch (IOException e1) {
              LoggerUtil.error("Invalid file name");
            } 
          } else {
            try {
              this.imageFile_ChooseAction.getImagePath().setText(choose.getSelectedFile().getCanonicalPath() + ".iso");
            }
            catch (IOException e1) {
              LoggerUtil.error("Invalid file name");
            } 
          } 
          this.imageFile_ChooseAction.setImage(choose.getSelectedFile().getParent());
        }
      } else {
        String cdAbsoluPath = null;
        try {
          cdAbsoluPath = choose.getSelectedFile().getCanonicalPath().toLowerCase(this.imageFile_ChooseAction.getLocale());
        }
        catch (IOException e1) {
          LoggerUtil.error("Invalid file name");
        } 
        if (cdAbsoluPath == null) {
          LoggerUtil.error("image path is incorrect!");
        }
        else {
          int length = cdAbsoluPath.length();
          int isoIndex = cdAbsoluPath.lastIndexOf(".iso");
          int imgIndex = cdAbsoluPath.lastIndexOf(".img");
          if (-1 != isoIndex || isoIndex + 4 == length) {
            try
            {
              this.imageFile_ChooseAction.getImagePath().setText(choose.getSelectedFile().getCanonicalPath());
            }
            catch (IOException e1)
            {
              LoggerUtil.error("Invalid file name");
            }
          } else if (-1 != imgIndex || imgIndex + 4 == length) {
            try {
              this.imageFile_ChooseAction.getImagePath().setText(choose.getSelectedFile().getCanonicalPath());
            }
            catch (IOException e1) {
              LoggerUtil.error("Invalid file name");
            } 
          } else {
            try {
              this.imageFile_ChooseAction.getImagePath().setText(choose.getSelectedFile().getCanonicalPath() + ".img");
            }
            catch (IOException e1) {
              LoggerUtil.error("Invalid file name");
            } 
          } 
          this.imageFile_ChooseAction.setImage(choose.getSelectedFile().getParent());
        } 
      } 
    } 
  }
}
