package com.library.decoder;
import java.awt.Color;
public class ColorConverter
{
  public static byte[] rgb2yuv(int R, int G, int B) {
    byte[] yuv = new byte[3];
    yuv[0] = (byte)(int)(0.299D * R + 0.587D * G + 0.114D * B);
    yuv[1] = (byte)(int)(-0.147D * R - 0.289D * G + 0.437D * B);
    yuv[2] = (byte)(int)(0.615D * R - 0.515D * G - 0.1D * B);
    return yuv;
  }
  public static byte[] yuv2rgb(int y, int u, int v) {
    byte[] rgb = new byte[3];
    int R = (int)(y + 1.14D * v);
    int G = (int)(y - 0.394D * u - 0.581D * v);
    int B = (int)(y + 2.028D * u);
    if (R < 0)
    {
      R = 0;
    }
    if (G < 0)
    {
      G = 0;
    }
    if (B < 0)
    {
      B = 0;
    }
    if (R > 255)
    {
      R = 255;
    }
    if (G > 255)
    {
      G = 255;
    }
    if (B > 255)
    {
      B = 255;
    }
    rgb[0] = (byte)R;
    rgb[0] = (byte)G;
    rgb[0] = (byte)B;
    return rgb;
  }
  public static byte rgb888Tobgr233(byte r, byte g, byte b) {
    byte bgr233 = 0;
    bgr233 = (byte)(b & 0xC0 | g & 0xE0 | r & 0xE0);
    return bgr233;
  }
  public static byte yuv2bgr233(byte y, byte u, byte v) {
    byte bgr233 = 0;
    byte R = (byte)(int)(y + 1.14D * v);
    byte G = (byte)(int)(y - 0.394D * u - 0.581D * v);
    byte B = (byte)(int)(y + 2.028D * u);
    if (R < 0)
    {
      R = 0;
    }
    if (G < 0)
    {
      G = 0;
    }
    if (B < 0)
    {
      B = 0;
    }
    if (R > 255)
    {
      R = -1;
    }
    if (G > 255)
    {
      G = -1;
    }
    if (B > 255)
    {
      B = -1;
    }
    bgr233 = (byte)(B & 0xC0 | G & 0xE0 | R & 0xE0);
    rgb2yuv(255, 0, 0);
    return bgr233;
  }
  public static int[] rgb2ycbcr(int R, int G, int B) {
    int[] ycbcr = new int[3];
    ycbcr[0] = (int)(0.257D * R + 0.504D * G + 0.098D * B + 16.0D);
    ycbcr[1] = (int)(-0.148D * R - 0.291D * G + 0.439D * B + 128.0D);
    ycbcr[2] = (int)(0.439D * R - 0.368D * G - 0.071D * B + 128.0D);
    if (ycbcr[1] > 255)
    {
      ycbcr[1] = 255;
    }
    if (ycbcr[2] > 255)
    {
      ycbcr[2] = 255;
    }
    return ycbcr;
  }
  public static Color ycbcr2rgbIscolor(int Y, int Cb, int Cr) {
    Y &= 0xFF;
    Cb &= 0xFF;
    Cr &= 0xFF;
    int R = (int)(1.164D * (Y - 16) + 1.596D * (Cr - 128));
    int G = (int)(1.164D * (Y - 16) - 0.813D * (Cr - 128) - 0.392D * (Cb - 128));
    int B = (int)(1.164D * (Y - 16) + 2.017D * (Cb - 128));
    if (R < 0)
    {
      R = 0;
    }
    if (G < 0)
    {
      G = 0;
    }
    if (B < 0)
    {
      B = 0;
    }
    if (R > 255)
    {
      R = 255;
    }
    if (G > 255)
    {
      G = 255;
    }
    if (B > 255)
    {
      B = 255;
    }
    Color rgb = new Color(R, G, B);
    return rgb;
  }
  public static int ycbcr2rgb(int Y, int Cb, int Cr) {
    Y &= 0xFF;
    Cb &= 0xFF;
    Cr &= 0xFF;
    int R = (int)(Y + 1.402D * (Cr - 128));
    int G = (int)(Y - 0.34414D * (Cb - 128) - 0.71414D * (Cr - 128));
    int B = (int)(Y + 1.772D * (Cb - 128));
    if (R < 0)
    {
      R = 0;
    }
    if (G < 0)
    {
      G = 0;
    }
    if (B < 0)
    {
      B = 0;
    }
    if (R > 255)
    {
      R = 255;
    }
    if (G > 255)
    {
      G = 255;
    }
    if (B > 255)
    {
      B = 255;
    }
    int rgb = R << 16 | G << 8 | B;
    return rgb;
  }
  public static byte ycbcr2rgb332(int Y, int Cb, int Cr) {
    byte bgr233 = 0;
    Y &= 0xFF;
    Cb &= 0xFF;
    Cr &= 0xFF;
    int R = (int)(1.164D * (Y - 16) + 1.596D * (Math.abs(Cr) - 128));
    int G = (int)(1.164D * (Y - 16) - 0.813D * (Cr - 128) - 0.392D * (Cb - 128));
    int B = (int)(1.164D * (Y - 16) + 2.017D * (Cb - 128));
    if (R < 0)
    {
      R = 0;
    }
    if (G < 0)
    {
      G = 0;
    }
    if (B < 0)
    {
      B = 0;
    }
    if (R > 255)
    {
      R = 255;
    }
    if (G > 255)
    {
      G = 255;
    }
    if (B > 255)
    {
      B = 255;
    }
    bgr233 = (byte)(B & 0xC0 | (G & 0xE0) >> 2 | (R & 0xE0) >> 5);
    return bgr233;
  }
}
