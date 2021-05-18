package com.kvm;
import com.Kinescope.KinescopeDataCollect;
import com.library.LoggerUtil;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
class VideoAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FullScreen refer_VideoAction;
  private FullToolBar fullTollBarRefer;
  public VideoAction(FullScreen refer_VideoAction, FullToolBar fullTollBarRefer) {
    this.refer_VideoAction = refer_VideoAction;
    this.fullTollBarRefer = fullTollBarRefer;
  }
  public void actionPerformed(ActionEvent e) {
    if (null == this.fullTollBarRefer.getPowerMenu().getKineScopeDataCollect()) {
      int result = JOptionPane.showConfirmDialog(this.refer_VideoAction.getKvmInterface().getFloatToolbar().getImagePanel(), this.refer_VideoAction
          .getKvmInterface().getKvmUtil().getString("Power_massage"), 
          UIManager.getString("OptionPane.titleText"), 0);
      if (0 == result)
      {
        String absolutePath = "";
        StringBuffer temp = new StringBuffer();
        JFileChooser choose = new JFileChooser("");
        choose.addChoosableFileFilter(new FileFilter()
            {
              public boolean accept(File f)
              {
                if (f.isDirectory())
                {
                  return true;
                }
                String fileName = f.getName();
                if (fileName.toLowerCase(VideoAction.this.fullTollBarRefer.getLocale())
                  .endsWith("rep".toLowerCase(VideoAction.this.fullTollBarRefer.getLocale())))
                {
                  return true;
                }
                return false;
              }
              public String getDescription() {
                return "file(*.rep)";
              }
            });
        choose.setFileSelectionMode(0);
        int returnval = choose.showSaveDialog(null);
        if (returnval == 0) {
          try {
            temp.append(choose.getSelectedFile()
                .getCanonicalPath()
                .toLowerCase(this.fullTollBarRefer.getLocale()));
            absolutePath = temp.toString();
          }
          catch (IOException e1) {
            LoggerUtil.error("Invalid file name");
          } 
        }
        if (0 == absolutePath.length()) {
          return;
        }
        if (!absolutePath.toLowerCase(this.fullTollBarRefer.getLocale()).endsWith(".rep")) {
          temp.append(".rep");
          absolutePath = temp.toString();
        } 
        File file = new File(absolutePath);
        if (!file.renameTo(file) && file.exists()) {
          JOptionPane.showMessageDialog(this.refer_VideoAction.getKvmInterface().getFloatToolbar().getImagePanel(), this.refer_VideoAction
              .getKvmInterface().getKvmUtil().getString("Video_file_message"));
          return;
        } 
        if (file.exists()) {
          int file_check = JOptionPane.showConfirmDialog(this.refer_VideoAction.getKvmInterface()
              .getFloatToolbar()
              .getImagePanel(), this.refer_VideoAction
              .getKvmInterface().getKvmUtil().getString("Power_massage"), 
              UIManager.getString("OptionPane.titleText"), 0);
          if (file_check == 0)
          {
            if (!file.delete())
            {
              LoggerUtil.error("delete file failed");
            }
            this.fullTollBarRefer.getVideoButton().setIcon(new ImageIcon(
                  getClass().getResource("resource/images/video_stop.png")));
            this.fullTollBarRefer.getPowerMenu().setKineScopeDataCollect(new KinescopeDataCollect(absolutePath));
            this.fullTollBarRefer.getPowerMenu().getKineScopeDataCollect().setCollect(true);
            this.fullTollBarRefer.getPowerMenu().getKineScopeDataCollect().startCollectThread();
            this.fullTollBarRefer.getVideoButton().setToolTipText(this.refer_VideoAction.getKvmInterface()
                .getKvmUtil()
                .getString("Stop_KinScope"));
            if (null == this.refer_VideoAction.getKvmInterface()
              .getFloatToolbar()
              .getPowerMenu()
              .getKineScopeDataCollect() && this.refer_VideoAction
              .getKvmInterface().isFullScreen())
            {
              this.refer_VideoAction.getKvmInterface()
                .getFloatToolbar()
                .getPowerMenu()
                .setKineScopeDataCollect(this.fullTollBarRefer.getPowerMenu().getKineScopeDataCollect());
            }
          }
        } else {
          this.fullTollBarRefer.getVideoButton().setIcon(new ImageIcon(
                getClass().getResource("resource/images/video_stop.png")));
          this.fullTollBarRefer.getPowerMenu().setKineScopeDataCollect(new KinescopeDataCollect(absolutePath));
          this.fullTollBarRefer.getPowerMenu().getKineScopeDataCollect().setCollect(true);
          this.fullTollBarRefer.getPowerMenu().getKineScopeDataCollect().startCollectThread();
          this.fullTollBarRefer.getVideoButton().setToolTipText(this.refer_VideoAction.getKvmInterface()
              .getKvmUtil()
              .getString("Stop_KinScope"));
          if (this.refer_VideoAction.getKvmInterface().isFullScreen() && this.refer_VideoAction
            .getKvmInterface().getFloatToolbar().getPowerMenu().getKineScopeDataCollect() == null)
          {
            this.refer_VideoAction.getKvmInterface()
              .getFloatToolbar()
              .getPowerMenu()
              .setKineScopeDataCollect(this.fullTollBarRefer.getPowerMenu().getKineScopeDataCollect());
          }
        }
      }
    }
    else if (this.fullTollBarRefer.getPowerMenu().getKineScopeDataCollect().isCollect()) {
      int result = JOptionPane.showConfirmDialog(this.refer_VideoAction.getKvmInterface().getFloatToolbar().getImagePanel(), this.refer_VideoAction
          .getKvmInterface().getKvmUtil().getString("Power_massage"), 
          UIManager.getString("OptionPane.titleText"), 0);
      if (result == 0) {
        this.fullTollBarRefer.getVideoButton().setIcon(new ImageIcon(
              getClass().getResource("resource/images/video_start.png")));
        this.fullTollBarRefer.getVideoButton().setToolTipText(this.refer_VideoAction.getKvmInterface()
            .getKvmUtil()
            .getString("localKinescope"));
        this.fullTollBarRefer.getPowerMenu().getKineScopeDataCollect().interrupt();
        this.fullTollBarRefer.getPowerMenu().getKineScopeDataCollect().setCollect(false);
        this.fullTollBarRefer.getPowerMenu().getKineScopeDataCollect().closeFile();
        if (this.fullTollBarRefer.getPowerMenu().getKineScopeDataCollect().isAlive()) {
          try {
            Thread.sleep(5L);
          }
          catch (InterruptedException e1) {
            LoggerUtil.error(e1.getClass().getName());
          } 
        }
        this.fullTollBarRefer.getPowerMenu().setKineScopeDataCollect(null);
        this.fullTollBarRefer.getPowerMenu().setDissflag(false);
        this.fullTollBarRefer.getPowerMenu().setIdiss(false);
        if (this.refer_VideoAction.getKvmInterface().isFullScreen() && this.refer_VideoAction
          .getKvmInterface()
          .getFloatToolbar()
          .getPowerMenu()
          .getKineScopeDataCollect() == null)
        {
          this.refer_VideoAction.getKvmInterface()
            .getFloatToolbar()
            .getPowerMenu()
            .setKineScopeDataCollect(this.fullTollBarRefer.getPowerMenu().getKineScopeDataCollect());
        }
        Base.setStartVideo(1);
      } 
    } 
  }
}
