package com.kvm;
import com.Kinescope.KinescopeDataCollect;
import com.library.LoggerUtil;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
class ScopeAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private PowerPopupMenu popupMenu = null;
  ScopeAction(PowerPopupMenu refer) {
    this.popupMenu = refer;
  }
  public void actionPerformed(ActionEvent e) {
    JMenuItem menuItem = (JMenuItem)e.getSource();
    if (null == this.popupMenu.getKineScopeDataCollect()) {
      int result = JOptionPane.showConfirmDialog(this.popupMenu.getKvmInterface().getFloatToolbar().getImagePanel(), this.popupMenu
          .getKvmInterface().getKvmUtil().getString("Power_massage"), 
          UIManager.getString("OptionPane.titleText"), 0);
      if (result == 0)
      {
        String absolutePath = "";
        StringBuffer temp = new StringBuffer();
        JFileChooser choose = new JFileChooser("");
        choose.addChoosableFileFilter(new ChooseFileFilter(this.popupMenu));
        choose.setFileSelectionMode(0);
        int returnval = choose.showSaveDialog(null);
        if (returnval == 0) {
          try {
            temp.append(choose.getSelectedFile()
                .getCanonicalPath()
                .toLowerCase(this.popupMenu.getLocale()));
            absolutePath = temp.toString();
          }
          catch (IOException e1) {
            LoggerUtil.error("Invalid file name");
          } 
        }
        if (absolutePath.length() == 0) {
          return;
        }
        if (!absolutePath.toLowerCase(this.popupMenu.getLocale()).endsWith(".rep")) {
          temp.append(".rep");
          absolutePath = temp.toString();
        } 
        File file = new File(absolutePath);
        if (!file.renameTo(file) && file.exists()) {
          JOptionPane.showMessageDialog(this.popupMenu.getKvmInterface().getFloatToolbar().getImagePanel(), this.popupMenu
              .getKvmInterface().getKvmUtil().getString("Video_file_message"));
          return;
        } 
        if (file.exists())
        {
          int file_check = JOptionPane.showConfirmDialog(this.popupMenu.getKvmInterface().getFloatToolbar().getImagePanel(), this.popupMenu
              .getKvmInterface().getKvmUtil().getString("Power_massage"), 
              UIManager.getString("OptionPane.titleText"), 0);
          if (file_check == 0)
          {
            if (!file.delete())
            {
              LoggerUtil.error("delete file failed");
            }
            menuItem.setText(this.popupMenu.getKvmInterface().getKvmUtil().getString("Stop_KinScope"));
            this.popupMenu.setKineScopeDataCollect(new KinescopeDataCollect(absolutePath));
            this.popupMenu.getKineScopeDataCollect().setCollect(true);
            this.popupMenu.getKineScopeDataCollect().startCollectThread();
            if (this.popupMenu.getKvmInterface().isFullScreen() && null == this.popupMenu
              .getKvmInterface()
              .getFloatToolbar()
              .getPowerMenu()
              .getKineScopeDataCollect())
            {
              this.popupMenu.getKvmInterface()
                .getFloatToolbar()
                .getPowerMenu()
                .setKineScopeDataCollect(this.popupMenu.getKineScopeDataCollect());
            }
          }
        }
        else
        {
          menuItem.setText(this.popupMenu.getKvmInterface().getKvmUtil().getString("Stop_KinScope"));
          this.popupMenu.setKineScopeDataCollect(new KinescopeDataCollect(absolutePath));
          this.popupMenu.getKineScopeDataCollect().setCollect(true);
          this.popupMenu.getKineScopeDataCollect().startCollectThread();
          if (this.popupMenu.getKvmInterface().isFullScreen() && null == this.popupMenu
            .getKvmInterface()
            .getFloatToolbar()
            .getPowerMenu()
            .getKineScopeDataCollect())
          {
            this.popupMenu.getKvmInterface()
              .getFloatToolbar()
              .getPowerMenu()
              .setKineScopeDataCollect(this.popupMenu.getKineScopeDataCollect());
          }
        }
      }
    }
    else if (this.popupMenu.getKineScopeDataCollect().isCollect()) {
      int result = JOptionPane.showConfirmDialog(this.popupMenu.getKvmInterface().getFloatToolbar().getImagePanel(), this.popupMenu
          .getKvmInterface().getKvmUtil().getString("Power_massage"), 
          UIManager.getString("OptionPane.titleText"), 0);
      if (0 == result) {
        menuItem.setText(this.popupMenu.getKvmInterface().getKvmUtil().getString("localKinescope"));
        this.popupMenu.getKineScopeDataCollect().interrupt();
        this.popupMenu.getKineScopeDataCollect().setCollect(false);
        this.popupMenu.getKineScopeDataCollect().closeFile();
        if (this.popupMenu.getKineScopeDataCollect().isAlive()) {
          try {
            Thread.sleep(5L);
          }
          catch (InterruptedException e1) {
            LoggerUtil.error(e1.getClass().getName());
          } 
        }
        this.popupMenu.setKineScopeDataCollect((KinescopeDataCollect)null);
        this.popupMenu.setDissflag(false);
        this.popupMenu.setIdiss(false);
        if (this.popupMenu.getKvmInterface().isFullScreen() && null == this.popupMenu
          .getKvmInterface()
          .getFloatToolbar()
          .getPowerMenu()
          .getKineScopeDataCollect())
        {
          this.popupMenu.getKvmInterface()
            .getFloatToolbar()
            .getPowerMenu()
            .setKineScopeDataCollect(this.popupMenu.getKineScopeDataCollect());
        }
      } 
    } 
  }
}
