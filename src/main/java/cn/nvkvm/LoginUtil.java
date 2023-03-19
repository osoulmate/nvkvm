package cn.nvkvm;
import cn.library.LoggerUtil;

import java.io.Serializable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
//need
class LoginUtil
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String resourcePath = "KVMResource";
  private transient ResourceBundle bundle = null;

  public void setBundle(String Ianguage) {
    if (Ianguage.equals("zh")) {
      this.bundle = ResourceBundle.getBundle(this.resourcePath, new Locale("zh"), 
      ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }
    else {
      this.bundle = ResourceBundle.getBundle(this.resourcePath, new Locale("en"), 
      ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    } 
  }
  public String getString(String key) {
    String value = null;
    if (null == key)
    {
      return "key is null";
    }
    try {
      value = this.bundle.getString(key);
    }
    catch (MissingResourceException e) {
      LoggerUtil.error(e.getClass().getName());
      value = "Could not find resource: " + key + "  ";
    }
    catch (ClassCastException e1) {
      LoggerUtil.error(e1.getClass().getName());
      value = "key is not a string";
    } 
    return value;
  }
}
