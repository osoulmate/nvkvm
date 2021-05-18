package com.huawei.vm.console.utilsV1;
import java.io.IOException;
import java.io.InputStream;
public class FileTransferUtil
{
  public static int getBytes(byte[] buff, String fileName) throws IOException {
    ClassLoader classloader = FileTransferUtil.class.getClassLoader();
    int result = -1;
    InputStream inputStream = null;
    try {
      inputStream = classloader.getResourceAsStream(fileName);
      if (null != inputStream)
      {
        result = inputStream.read(buff);
      }
    }
    finally {
      if (null != inputStream)
      {
        inputStream.close();
      }
    } 
    if (result == -1)
    {
      throw new IOException("can not read bytes from file");
    }
    return result;
  }
}
