package com.kvm;
import com.library.LoggerUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
public class CopyFileToLocal
{
  public static boolean libarayPrepare() {
    String mouseLibName = "unknown";
    String mouseSourceLibName = "unknown";
    String mouseDestLibName = "unknown";
    String keyboardLibName = "unknown";
    String keyboardSourceLibName = "unknown";
    String keyboardDestLibName = "unknown";
    String separator = System.getProperty("file.separator");
    String libDir = System.getProperty("java.io.tmpdir");
    boolean libraryInstall = false;
    Base.setLibID(System.identityHashCode(Long.valueOf(System.currentTimeMillis())));
    if (KVMUtil.isWindowsOS()) {
      mouseLibName = Base.getMOUSE_LIB() + Base.getLibID() + ".dll";
      LoggerUtil.info( "mouseLibName: "+ mouseLibName );
      mouseSourceLibName = "com/kvm/" + Base.getMOUSE_LIB() + ".dll";
      LoggerUtil.info( "mouseSourceLibName: "+ mouseSourceLibName );
    }
    else if (KVMUtil.isOsArchByName("x86_64") || KVMUtil.isOsArchByName("amd64")) {
      keyboardLibName = Base.getKEYBOARD_LIB() + Base.getLibID() + ".so";
      keyboardSourceLibName = "com/kvm/" + Base.getKEYBOARD_LIB() + ".so";
      mouseLibName = "libkeyboard_encrypt" + Base.getLibID() + ".so";
      mouseSourceLibName = "com/kvm/libkeyboard_encrypt_64.so";
    }
    else if (KVMUtil.isOsArchByName("i386")) {
      keyboardLibName = Base.getKEYBOARD_LIB() + Base.getLibID() + ".so";
      keyboardSourceLibName = "com/kvm/" + Base.getKEYBOARD_LIB() + ".so";
      mouseLibName = "libkeyboard_encrypt" + Base.getLibID() + ".so";
      mouseSourceLibName = "com/kvm/libkeyboard_encrypt_32.so";
    }
    else {
      return false;
    } 
    if (libDir != null && !libDir.endsWith(separator))
    {
      libDir = libDir + separator;
    }
    mouseDestLibName = libDir + mouseLibName;
    keyboardDestLibName = libDir + keyboardLibName;
    File file1 = new File(mouseDestLibName);
    LoggerUtil.info( "mouseDestLibName: "+ mouseDestLibName );
    if (file1.exists()) {
      libraryInstall = true;
      LoggerUtil.info( "libraryInstall: "+ libraryInstall );
    }
    else {
      libraryInstall = makeLibaray(mouseSourceLibName, mouseDestLibName);
      LoggerUtil.info( "libraryInstall: "+ mouseSourceLibName + "," + mouseDestLibName );
    } 
    if (KVMUtil.isLinuxOS()) {
      File file2 = new File(keyboardDestLibName);
      if (!file2.exists())
      {
        makeLibaray(keyboardSourceLibName, keyboardDestLibName);
      }
    } 
    return libraryInstall;
  }
  public static void cleanLib() {
    String mouseLibName = "unknown";
    String mouseLibExt = "unknown";
    String keyboardLibName = "unknown";
    String keyboardLibExt = "unknown";
    String separator = System.getProperty("file.separator");
    String libDir = System.getProperty("java.io.tmpdir");
    String libDirTemp = "";
    if (KVMUtil.isWindowsOS()) {
      mouseLibName = Base.getMOUSE_LIB();
      mouseLibExt = ".dll";
    }
    else if (KVMUtil.isLinuxOS()) {
      keyboardLibName = Base.getKEYBOARD_LIB();
      keyboardLibExt = ".so";
      mouseLibName = "libkeyboard_encrypt";
      mouseLibExt = ".so";
    } else {
      return;
    } 
    if (null == libDir) {
      return;
    }
    File file = new File(libDir);
    if (!libDir.endsWith(separator))
    {
      libDir = libDir + separator;
    }
    String[] files = file.list();
    if (null == files) {
      return;
    }
    for (int i = 0; i < files.length; i++) {
      if ((files[i].startsWith(mouseLibName) && files[i].endsWith(mouseLibExt)) || (files[i]
        .startsWith(keyboardLibName) && files[i].endsWith(keyboardLibExt))) {
        libDirTemp = libDir + files[i];
        LoggerUtil.info( "libDirTemp: "+ libDirTemp );
        if (!(new File(libDirTemp)).delete())
        {
          LoggerUtil.error("delete file failed");
        }
      } 
    } 
  }
  private static boolean makeLibaray(String source, String dest) {
    if (null == CopyFileToLocal.class.getClassLoader())
    {
      return false;
    }
    ClassLoader classloader = CopyFileToLocal.class.getClassLoader();
    if (classloader == null || classloader.getResource(source) == null)
    {
      return false;
    }
    byte[] buffer = new byte[4096];
    InputStream inputStream = null;
    FileOutputStream fileOutputStream = null;
    try {
      inputStream = classloader.getResourceAsStream(source);
      fileOutputStream = new FileOutputStream(dest);
      int i = inputStream.read(buffer, 0, buffer.length);
      while (-1 != i)
      {
        fileOutputStream.write(buffer, 0, i);
        i = inputStream.read(buffer, 0, buffer.length);
      }
    } catch (IOException e) {
      return false;
    } finally {
      try {
        if (inputStream != null)
        {
          inputStream.close();
        }
      }
      catch (IOException e) {
        Debug.println("Console Controller:input stream close error when make lib");
      } finally {
        try {
          if (null != fileOutputStream)
          {
            fileOutputStream.close();
          }
        }
        catch (IOException e) {
          Debug.println("Console Controller:Output steam close error when make lib");
        } 
      } 
    } 
    return true;
  }
}
