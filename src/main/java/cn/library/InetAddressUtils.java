package cn.library;
import java.util.regex.Pattern;
public class InetAddressUtils
{
  private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
  private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
  private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");
  private static final Pattern URL_PATTERN = Pattern.compile("^((https|http|ftp|rtsp|mms)?://)+(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?(([0-9]{1,3}\\.){3}[0-9]{1,3}|([0-9a-z_!~*'()-]+\\.)*([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\.[a-z]{2,6})(:[0-9]{1,4})?((/?)|(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$");
  public static boolean isIPv4Address(String input) {
    return IPV4_PATTERN.matcher(input).matches();
  }
  public static boolean isIPv6StdAddress(String input) {
    return IPV6_STD_PATTERN.matcher(input).matches();
  }
  public static boolean isIPv6HexCompressedAddress(String input) {
    return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
  }
  public static boolean isIPv6Address(String input) {
    return (isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input));
  }
  public static boolean isURL(String input) {
    return URL_PATTERN.matcher(input).matches();
  }
  public static boolean isPort(String input) {
    return (Integer.parseInt(input) >= 0 && Integer.parseInt(input) <= 65535);
  }
  public static String getSafeIP(String input) {
    String tempIP = input;
    return tempIP;
  }
  public static String getSafePort(String input) {
    String tempPort = input;
    if (isPort(input)) {
      tempPort = input;
    }
    else {
      LoggerUtil.error("port is null or invalid");
    } 
    return tempPort;
  }
  public static int getSafePort(int input) {
    int tempPort = input;
    if (tempPort > 0 && tempPort <= 65535) {
      tempPort = input;
    }
    else {
      LoggerUtil.error("port is null or invalid");
    } 
    return tempPort;
  }
  public static String getSafeURL(String input) {
    String tempURL = input;
    if (isURL(input)) {
      tempURL = input;
    }
    else {
      LoggerUtil.error("url is null or invalid");
    } 
    return tempURL;
  }
}
