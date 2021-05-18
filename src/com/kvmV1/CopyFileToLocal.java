package com.kvmV1;
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
    Base.libID = System.identityHashCode(Long.valueOf(System.currentTimeMillis()));
    if (KVMUtil.isWindowsOS()) {
      mouseLibName = Base.MOUSE_LIB + Base.libID + ".dll";
      mouseSourceLibName = "com/kvm/" + Base.MOUSE_LIB + ".dll";
    }
    else if (KVMUtil.isOsArchByName("x86_64") || KVMUtil.isOsArchByName("amd64")) {
      keyboardLibName = Base.KEYBOARD_LIB + Base.libID + ".so";
      keyboardSourceLibName = "com/kvm/" + Base.KEYBOARD_LIB + ".so";
      mouseLibName = "libkeyboard_encrypt" + Base.libID + ".so";
      mouseSourceLibName = "com/kvm/libkeyboard_encrypt_64.so";
    }
    else if (KVMUtil.isOsArchByName("i386")) {
      keyboardLibName = Base.KEYBOARD_LIB + Base.libID + ".so";
      keyboardSourceLibName = "com/kvm/" + Base.KEYBOARD_LIB + ".so";
      mouseLibName = "libkeyboard_encrypt" + Base.libID + ".so";
      mouseSourceLibName = "com/kvm/libkeyboard_encrypt_32.so";
    }
    else {
      return false;
    } 
    if (!libDir.endsWith(separator))
    {
      libDir = libDir + separator;
    }
    mouseDestLibName = libDir + mouseLibName;
    keyboardDestLibName = libDir + keyboardLibName;
    File file1 = new File(mouseDestLibName);
    if (file1.exists()) {
      libraryInstall = true;
    }
    else {
      libraryInstall = makeLibaray(mouseSourceLibName, mouseDestLibName);
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
    if (KVMUtil.isWindowsOS()) {
      mouseLibName = Base.MOUSE_LIB;
      mouseLibExt = ".dll";
    }
    else if (KVMUtil.isLinuxOS()) {
      keyboardLibName = Base.KEYBOARD_LIB;
      keyboardLibExt = ".so";
      mouseLibName = "libkeyboard_encrypt";
      mouseLibExt = ".so";
    } else {
      return;
    } 
    File file = new File(libDir);
    if (!libDir.endsWith(separator))
    {
      libDir = libDir + separator;
    }
    String[] files = file.list();
    for (int i = 0; i < files.length; i++) {
      if ((files[i].startsWith(mouseLibName) && files[i].endsWith(mouseLibExt)) || (files[i].startsWith(keyboardLibName) && files[i].endsWith(keyboardLibExt)))
      {
        (new File(libDir + files[i])).delete();
      }
    } 
  }
  private static boolean makeLibaray(String source, String dest) {
    ClassLoader classloader = CopyFileToLocal.class.getClassLoader();
    byte[] buffer = new byte[4096];
    if (classloader.getResource(source) == null)
    {
      return false;
    }
    InputStream inputStream = null;
    FileOutputStream fileOutputStream = null;
    try {
      inputStream = classloader.getResourceAsStream(source);
      fileOutputStream = new FileOutputStream(dest);
      int i = inputStream.read(buffer, 0, buffer.length);
      while (i != -1)
      {
        fileOutputStream.write(buffer, 0, i);
        i = inputStream.read(buffer, 0, buffer.length);
      }
    } catch (IOException ioexception) {
      return false;
    } finally {
      try {
        if (null != inputStream)
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
