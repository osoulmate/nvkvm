package com.huawei.vm.console.ui;
import com.huawei.vm.console.utils.ResourceUtil;
import java.io.File;
import java.util.Locale;
import javax.swing.filechooser.FileFilter;
public class JAVAFileFileter
  extends FileFilter
{
  String ext = "";
  String evt = "";
  ResourceUtil util;
  public JAVAFileFileter(String ext, String evt) {
    this.ext = ext;
    this.evt = evt;
  }
  public JAVAFileFileter(String ext, ResourceUtil util) {
    this.ext = ext;
    this.util = util;
  }
  public boolean accept(File f) {
    if (f.isDirectory())
    {
      return true;
    }
    String fileName = f.getName();
    int index = fileName.lastIndexOf('.');
    if (index > 0 && index < fileName.length() - 1) {
      String extension = fileName.substring(index + 1).toLowerCase(Locale.getDefault());
      if (this.ext.equalsIgnoreCase(extension) || this.evt.equalsIgnoreCase(extension))
      {
        return true;
      }
      if (this.ext.equalsIgnoreCase(extension))
      {
        return true;
      }
    } 
    return false;
  }
  public String getDescription() {
    if ("img".equalsIgnoreCase(this.ext) && "iso".equalsIgnoreCase(this.evt))
    {
      return "file(*.iso,*.img)";
    }
    if ("img".equalsIgnoreCase(this.ext))
    {
      return "file(*.img)";
    }
    if ("iso".equalsIgnoreCase(this.ext))
    {
      return "file(*.iso)";
    }
    if ("directory".equalsIgnoreCase(this.ext))
    {
      return this.util.getResource("flp_cd_local_open_dialog");
    }
    return "";
  }
}
