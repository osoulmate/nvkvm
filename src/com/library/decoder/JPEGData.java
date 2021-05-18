package com.library.decoder;
public class JPEGData
{
  private static int dqttags = 4;
  public static int getDqttags() {
    return dqttags;
  }
  public static void setDqttags(int dqttags) {
    JPEGData.dqttags = dqttags;
  }
  static byte[] createSynHeadData() {
    byte[] headSynData = null;
    int dqtlen = HEAD_SYN_DQT_Y.length;
    Object[] dqtArr = { DQTZData.HEAD_SYN_DQT_Y_S[dqttags], DQTZData.HEAD_SYN_DQT_U_S[dqttags], DQTZData.HEAD_SYN_DQT_V_S[dqttags] };
    headSynData = new byte[HEAD_SYN_SOI_APPO.length + dqtlen + dqtlen + dqtlen + HEAD_SYN_SOF_420.length + HEAD_SYN_SOF_SOS.length];
    System.arraycopy(HEAD_SYN_SOI_APPO, 0, headSynData, 0, HEAD_SYN_SOI_APPO.length);
    System.arraycopy(dqtArr[0], 0, headSynData, HEAD_SYN_SOI_APPO.length, dqtlen);
    System.arraycopy(dqtArr[1], 0, headSynData, HEAD_SYN_SOI_APPO.length + dqtlen, dqtlen);
    System.arraycopy(dqtArr[2], 0, headSynData, HEAD_SYN_SOI_APPO.length + 2 * dqtlen, dqtlen);
    System.arraycopy(HEAD_SYN_SOF_444, 0, headSynData, HEAD_SYN_SOI_APPO.length + 3 * dqtlen, HEAD_SYN_SOF_420.length);
    System.arraycopy(HEAD_SYN_SOF_SOS, 0, headSynData, HEAD_SYN_SOI_APPO.length + 3 * dqtlen + HEAD_SYN_SOF_420.length, HEAD_SYN_SOF_SOS.length);
    return headSynData;
  }
  static final byte[] HEAD_SYN_SOI_APPO = new byte[] { -1, -40, -1, -32, 0, 16, 74, 70, 73, 70, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0 };
  static final byte[] HEAD_SYN_DQT_Y = new byte[] { -1, -37, 0, 67, 0, 16, 11, 12, 14, 12, 10, 16, 14, 13, 14, 18, 17, 16, 19, 24, 40, 26, 24, 22, 22, 24, 49, 35, 37, 29, 40, 58, 51, 61, 60, 57, 51, 56, 55, 64, 72, 92, 78, 64, 68, 87, 69, 55, 56, 80, 109, 81, 87, 95, 98, 103, 104, 103, 62, 77, 113, 121, 112, 100, 120, 92, 101, 103, 99 };
  static final byte[] HEAD_SYN_DQT_U = new byte[] { -1, -37, 0, 67, 1, 17, 18, 18, 24, 21, 24, 47, 26, 26, 47, 99, 66, 56, 66, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99 };
  static final byte[] HEAD_SYN_DQT_V = new byte[] { -1, -37, 0, 67, 2, 17, 18, 18, 24, 21, 24, 47, 26, 26, 47, 99, 66, 56, 66, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99 };
  static final byte[] HEAD_SYN_SOF_420 = new byte[] { -1, -64, 0, 17, 8, 0, 64, 0, 64, 3, 1, 34, 0, 2, 17, 1, 3, 17, 2 };
  static final byte[] HEAD_SYN_SOF_444 = new byte[] { -1, -64, 0, 17, 8, 0, 64, 0, 64, 3, 1, 17, 0, 2, 17, 1, 3, 17, 2 };
  static final byte[] HEAD_SYN_SOF_SOS = new byte[] { -1, -60, 0, 31, 0, 0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, -1, -60, 0, 31, 1, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, -1, -60, 0, -75, 16, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125, 1, 2, 3, 0, 4, 17, 5, 18, 33, 49, 65, 6, 19, 81, 97, 7, 34, 113, 20, 50, -127, -111, -95, 8, 35, 66, -79, -63, 21, 82, -47, -16, 36, 51, 98, 114, -126, 9, 10, 22, 23, 24, 25, 26, 37, 38, 39, 40, 41, 42, 52, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -1, -60, 0, -75, 17, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119, 0, 1, 2, 3, 17, 4, 5, 33, 49, 6, 18, 65, 81, 7, 97, 113, 19, 34, 50, -127, 8, 20, 66, -111, -95, -79, -63, 9, 35, 51, 82, -16, 21, 98, 114, -47, 10, 22, 36, 52, -31, 37, -15, 23, 24, 25, 26, 38, 39, 40, 41, 42, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -126, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -30, -29, -28, -27, -26, -25, -24, -23, -22, -14, -13, -12, -11, -10, -9, -8, -7, -6, -1, -35, 0, 4, 0, 100, -1, -38, 0, 12, 3, 1, 0, 2, 17, 3, 17, 0, 63, 0 };
  static final byte[] TAIL = new byte[] { -1, -39 };
}
