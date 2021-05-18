package com.kvmV1;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
public class LoggerUtil
{
  private static final Logger log;
  private static String location = null;
  static {
    log = Logger.getLogger(LoggerUtil.class.getName());
    log.setLevel(Level.ALL);
    ConsoleHandler consoleHandler = new ConsoleHandler();
    LogFormatter fm = new LogFormatter();
    consoleHandler.setFormatter(fm);
    log.addHandler(consoleHandler);
    log.setUseParentHandlers(false);
  }
  public static String replaceBlank(String value) {
    return value.replaceAll("\t|\r|\n", "");
  }
  public static void info(String msg) {
    getLocation();
    log.warning(replaceBlank(location + msg));
  }
  public static void warning(String msg) {
    getLocation();
    log.warning(replaceBlank(location + msg));
  }
  public static void error(String msg) {
    getLocation();
    log.warning(replaceBlank(location + msg));
  }
  private static synchronized Logger getLocation() {
    StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
    location = "class:" + stacks[3].getClassName() + ",method:" + stacks[3].getMethodName() + ",file:" + stacks[3].getFileName() + ",line number:" + stacks[3].getLineNumber() + ',';
    return log;
  }
}
