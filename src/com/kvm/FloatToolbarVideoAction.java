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
class FloatToolbarVideoAction
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private FloatToolbar ftb_FloatToolbarVideo = null;
  FloatToolbarVideoAction(FloatToolbar floatToolbar) {
    this.ftb_FloatToolbarVideo = floatToolbar;
  }
  public void actionPerformed(ActionEvent e) {
    BladeThread bladeThread = (BladeThread)this.ftb_FloatToolbarVideo.getKvmInterface().getBase().getThreadGroup().get(String.valueOf(this.ftb_FloatToolbarVideo.getKvmInterface().getActionBlade()));
    if (this.ftb_FloatToolbarVideo.getPowerMenu().getKineScopeDataCollect() == null) {
      int result = JOptionPane.showConfirmDialog(this.ftb_FloatToolbarVideo.getKvmInterface().getFloatToolbar().getImagePanel(), this.ftb_FloatToolbarVideo
          .getKvmInterface().getKvmUtil().getString("Power_massage"), 
          UIManager.getString("OptionPane.titleText"), 0);
      if (result == 0)
      {
        String absolutePath = "";
        StringBuffer temp = new StringBuffer();
        JFileChooser choose = new JFileChooser("");
        choose.addChoosableFileFilter(new ActionPerformedFileFilter(this));
        choose.setFileSelectionMode(0);
        int returnval = choose.showSaveDialog(null);
        if (0 == returnval) {
          try {
            temp.append(choose.getSelectedFile()
                .getCanonicalPath()
                .toLowerCase(this.ftb_FloatToolbarVideo.getLocale()));
            absolutePath = temp.toString();
          }
          catch (IOException e1) {
            LoggerUtil.error("Invalid file name");
          } 
        }
        if (absolutePath.length() == 0) {
          return;
        }
        if (!absolutePath.toLowerCase(this.ftb_FloatToolbarVideo.getLocale()).endsWith(".rep")) {
          temp.append(".rep");
          absolutePath = temp.toString();
        } 
        File file = new File(absolutePath);
        if (!file.renameTo(file) && file.exists()) {
          JOptionPane.showMessageDialog(this.ftb_FloatToolbarVideo.getKvmInterface()
              .getFloatToolbar()
              .getImagePanel(), this.ftb_FloatToolbarVideo
              .getKvmInterface().getKvmUtil().getString("Video_file_message"));
          return;
        } 
        if (file.exists()) {
          int file_check = JOptionPane.showConfirmDialog(this.ftb_FloatToolbarVideo.getKvmInterface()
              .getFloatToolbar()
              .getImagePanel(), this.ftb_FloatToolbarVideo
              .getKvmInterface().getKvmUtil().getString("Power_massage"), 
              UIManager.getString("OptionPane.titleText"), 0);
          if (0 == file_check)
          {
            if (!file.delete())
            {
              LoggerUtil.error("delete file failed");
            }
            this.ftb_FloatToolbarVideo.getVideoButton().setIcon(new ImageIcon(
                  getClass().getResource("resource/images/video_stop.png")));
            this.ftb_FloatToolbarVideo.getPowerMenu().setKineScopeDataCollect(new KinescopeDataCollect(absolutePath));
            this.ftb_FloatToolbarVideo.getPowerMenu().getKineScopeDataCollect().setCollect(true);
            this.ftb_FloatToolbarVideo.getPowerMenu().getKineScopeDataCollect().startCollectThread();
            this.ftb_FloatToolbarVideo.getVideoButton().setToolTipText(this.ftb_FloatToolbarVideo.getKvmInterface()
                .getKvmUtil()
                .getString("Stop_KinScope"));
            if (this.ftb_FloatToolbarVideo.getKvmInterface().isFullScreen() && null == this.ftb_FloatToolbarVideo
              .getKvmInterface()
              .getFloatToolbar()
              .getPowerMenu()
              .getKineScopeDataCollect())
            {
              this.ftb_FloatToolbarVideo.getKvmInterface()
                .getFloatToolbar()
                .getPowerMenu()
                .setKineScopeDataCollect(this.ftb_FloatToolbarVideo.getPowerMenu().getKineScopeDataCollect());
            }
          }
        } else {
          this.ftb_FloatToolbarVideo.getVideoButton().setIcon(new ImageIcon(
                getClass().getResource("resource/images/video_stop.png")));
          this.ftb_FloatToolbarVideo.getPowerMenu()
            .setKineScopeDataCollect(new KinescopeDataCollect(absolutePath));
          this.ftb_FloatToolbarVideo.getPowerMenu().getKineScopeDataCollect().setCollect(true);
          this.ftb_FloatToolbarVideo.getPowerMenu().getKineScopeDataCollect().startCollectThread();
          this.ftb_FloatToolbarVideo.getVideoButton().setToolTipText(this.ftb_FloatToolbarVideo.getKvmInterface()
              .getKvmUtil()
              .getString("Stop_KinScope"));
          if (this.ftb_FloatToolbarVideo.getKvmInterface().isFullScreen() && null == this.ftb_FloatToolbarVideo
            .getKvmInterface()
            .getFloatToolbar()
            .getPowerMenu()
            .getKineScopeDataCollect())
          {
            this.ftb_FloatToolbarVideo.getKvmInterface()
              .getFloatToolbar()
              .getPowerMenu()
              .setKineScopeDataCollect(this.ftb_FloatToolbarVideo.getPowerMenu().getKineScopeDataCollect());
          }
        } 
        bladeThread.getBladeCommu().sentData(this.ftb_FloatToolbarVideo.getKvmInterface()
            .getKvmUtil()
            .getImagePane(this.ftb_FloatToolbarVideo.getKvmInterface().getActionBlade())
            .getPack()
            .kvmCmdvideoControl(bladeThread.getBladeNOByBladeThread()));
      }
    }
    else if (this.ftb_FloatToolbarVideo.getPowerMenu().getKineScopeDataCollect().isCollect()) {
      int result = JOptionPane.showConfirmDialog(this.ftb_FloatToolbarVideo.getKvmInterface()
          .getFloatToolbar()
          .getImagePanel(), this.ftb_FloatToolbarVideo
          .getKvmInterface().getKvmUtil().getString("Power_massage"), 
          UIManager.getString("OptionPane.titleText"), 0);
      if (0 == result) {
        this.ftb_FloatToolbarVideo.getVideoButton().setIcon(new ImageIcon(
              getClass().getResource("resource/images/video_start.png")));
        this.ftb_FloatToolbarVideo.getVideoButton().setToolTipText(this.ftb_FloatToolbarVideo.getKvmInterface()
            .getKvmUtil()
            .getString("localKinescope"));
        this.ftb_FloatToolbarVideo.getPowerMenu().getKineScopeDataCollect().interrupt();
        this.ftb_FloatToolbarVideo.getPowerMenu().getKineScopeDataCollect().setCollect(false);
        this.ftb_FloatToolbarVideo.getPowerMenu().getKineScopeDataCollect().closeFile();
        if (this.ftb_FloatToolbarVideo.getPowerMenu().getKineScopeDataCollect().isAlive()) {
          try {
            Thread.sleep(5L);
          }
          catch (InterruptedException e1) {
            LoggerUtil.error(e1.getClass().getName());
          } 
        }
        this.ftb_FloatToolbarVideo.getPowerMenu().setKineScopeDataCollect(null);
        this.ftb_FloatToolbarVideo.getPowerMenu().setDissflag(false);
        this.ftb_FloatToolbarVideo.getPowerMenu().setIdiss(false);
        if (this.ftb_FloatToolbarVideo.getKvmInterface().isFullScreen() && null == this.ftb_FloatToolbarVideo
          .getKvmInterface()
          .getFloatToolbar()
          .getPowerMenu()
          .getKineScopeDataCollect())
        {
          this.ftb_FloatToolbarVideo.getKvmInterface()
            .getFloatToolbar()
            .getPowerMenu()
            .setKineScopeDataCollect(this.ftb_FloatToolbarVideo.getPowerMenu().getKineScopeDataCollect());
        }
        Base.setStartVideo(1);
        bladeThread.getBladeCommu().sentData(this.ftb_FloatToolbarVideo.getKvmInterface()
            .getKvmUtil()
            .getImagePane(this.ftb_FloatToolbarVideo.getKvmInterface().getActionBlade())
            .getPack()
            .kvmCmdvideounControl(bladeThread.getBladeNOByBladeThread()));
      } 
    } 
  }
  public FloatToolbar getFtb() {
    return this.ftb_FloatToolbarVideo;
  }
  public void setFtb(FloatToolbar ftb_FloatToolbarVideo) {
    this.ftb_FloatToolbarVideo = ftb_FloatToolbarVideo;
  }
}
