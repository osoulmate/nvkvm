package com.library;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
class LogFormatter
  extends Formatter
{
  private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
  private final Object lock = new Object();
  public String format(LogRecord record) {
    String dateSte = null;
    synchronized (this.lock) {
      dateSte = df.format(new Date(record.getMillis()));
    } 
    StringBuilder builder = new StringBuilder(1000);
    builder.append("[").append(dateSte).append("]");
    builder.append("[").append(record.getLevel()).append("] ");
    builder.append(formatMessage(record));
    builder.append("\n");
    return builder.toString();
  }
  public String getHead(Handler h) {
    return super.getHead(h);
  }
  public String getTail(Handler h) {
    return super.getTail(h);
  }
}
