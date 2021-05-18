package com.kvm;
import java.io.File;
import javax.swing.filechooser.FileFilter;
class ChooseFileFilter
  extends FileFilter
{
  private PowerPopupMenu popupMenu = null;
  public ChooseFileFilter(PowerPopupMenu refer) {
    this.popupMenu = refer;
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
    if (fileName.toLowerCase(this.popupMenu.getLocale()).endsWith("rep".toLowerCase(this.popupMenu.getLocale())))
    {
      return true;
    }
    return false;
  }
}
