package com.kvm;
import java.io.File;
import javax.swing.filechooser.FileFilter;
class ActionPerformedFileFilter
  extends FileFilter
{
  private FloatToolbarVideoAction va = null;
  ActionPerformedFileFilter(FloatToolbarVideoAction videoAction) {
    this.va = videoAction;
  }
  public String getDescription() {
    return "file(*.rep)";
  }
  public boolean accept(File f) {
    if (f.isDirectory())
    {
      return true;
    }
    String fileName = f.getName();
    if (fileName.toLowerCase(this.va.getFtb().getLocale()).endsWith("rep".toLowerCase(this.va.getFtb().getLocale())))
    {
      return true;
    }
    return false;
  }
}
