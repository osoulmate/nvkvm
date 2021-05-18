package com.kvm;
import com.huawei.vm.console.ui.JAVAFileFileter;
import com.library.LoggerUtil;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
class FileChoose
  extends AbstractAction
{
  private static final long serialVersionUID = 1L;
  private transient VirtualMedia refer_FileChoose;
  public FileChoose(VirtualMedia refer_FileChoose) {
    this.refer_FileChoose = refer_FileChoose;
  }
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this.refer_FileChoose.getFlpSkim()) {
      JFileChooser chooser = new JFileChooser(this.refer_FileChoose.getFlpImage());
      chooser.addChoosableFileFilter((FileFilter)new JAVAFileFileter("img", this.refer_FileChoose.getUtil()));
      chooser.setAcceptAllFileFilterUsed(false);
      int returnVal = chooser.showOpenDialog(null);
      if (returnVal == 0)
      {
        try {
          this.refer_FileChoose.getFitext().setText(chooser.getSelectedFile().getCanonicalPath());
        }
        catch (IOException e1) {
          LoggerUtil.error("Invalid file name");
        } 
        this.refer_FileChoose.setFlpImage(chooser.getSelectedFile().getParent());
      }
    } else if (e.getSource() == this.refer_FileChoose.getCdSkim()) {
      JFileChooser chooser = new JFileChooser(this.refer_FileChoose.getCdImage());
      chooser.addChoosableFileFilter((FileFilter)new JAVAFileFileter("iso", this.refer_FileChoose.getUtil()));
      chooser.setAcceptAllFileFilterUsed(false);
      int returnVal = chooser.showOpenDialog(null);
      if (returnVal == 0)
      {
        try {
          this.refer_FileChoose.getCdText().setText(chooser.getSelectedFile().getCanonicalPath());
        }
        catch (IOException e1) {
          LoggerUtil.error("Invalid file name");
        } 
        this.refer_FileChoose.setCdImage(chooser.getSelectedFile().getParent());
      }
    }
    else if (e.getSource() == this.refer_FileChoose.getCdLocalSkim()) {
      JFileChooser chooser = new JFileChooser(this.refer_FileChoose.getCdLocalDir());
      chooser.setFileSelectionMode(1);
      chooser.addChoosableFileFilter((FileFilter)new JAVAFileFileter("directory", this.refer_FileChoose.getUtil()));
      chooser.setAcceptAllFileFilterUsed(false);
      int returnVal = chooser.showOpenDialog(null);
      if (returnVal == 0) {
        try {
          this.refer_FileChoose.getCdLocalText().setText(chooser.getSelectedFile().getCanonicalPath());
        }
        catch (IOException e1) {
          LoggerUtil.error("Invalid file name");
        } 
        try {
          this.refer_FileChoose.setCdLocalDir(chooser.getSelectedFile().getCanonicalPath());
        }
        catch (IOException e1) {
          LoggerUtil.error("Invalid file name");
        } 
        this.refer_FileChoose.setLocalDirName(chooser.getSelectedFile().getName());
      } else {
        return;
      } 
      if (!this.refer_FileChoose.isValidDirectory(this.refer_FileChoose.getCdLocalDir())) {
        return;
      }
      Timer t = new Timer("cdLocalDir memory iso");
      TimerTask task = new FileChooseTimer(this.refer_FileChoose);
      t.schedule(task, 100L);
    } 
  }
}
