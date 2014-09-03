package com.free.ydjt.log;

import java.util.Calendar;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

import com.free.Application;
import com.free.misc.WrapperLogger;
import com.free.ydjt.dto.Log;
import com.free.ydjt.dto.User;
import com.free.ydjt.service.UserService;

public class JpaLogger implements WrapperLogger {

  public void append(LoggingEvent event, String stack, String msg) throws Throwable {
    Log log = new Log();
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(event.getTimeStamp());
    Log.Level level;
    switch (event.getLevel().toInt()) {
    case Level.ALL_INT:
      level = Log.Level.ALL;
      break;
    case Level.FATAL_INT:
      level = Log.Level.FATAL;
      break;
    case Level.ERROR_INT:
      level = Log.Level.ERROR;
      break;
    case Level.WARN_INT:
      level = Log.Level.WARN;
      break;
    case Level.INFO_INT:
      level = Log.Level.INFO;
      break;
    case Level.DEBUG_INT:
      level = Log.Level.DEBUG;
      break;
    case Level.TRACE_INT:
      level = Log.Level.TRACE;
      break;
    case Level.OFF_INT:
      level = Log.Level.OFF;
      break;
    default:
      level = Log.Level.NONE;
      break;
    }
    User me = UserService.getLoginedAccount();
    LocationInfo li = event.getLocationInformation();
    String className = (li != null) ? li.getClassName() : null;
    String methodName = (li != null) ? li.getMethodName() : "";
    String lineNumber = (li != null) ? li.getLineNumber() : "";
    String content = event.getRenderedMessage() == null || "".equals(event.getRenderedMessage()) ? msg : event
        .getRenderedMessage();
    if (className == null)
      className = event.getLoggerName();
    if (content.length() > 600)
      content = content.substring(0, 597) + "...";
    log.setClazz(className);
    log.setMethod(methodName);
    log.setLine(lineNumber);
    log.setContent(content);
    log.setDateline(cal);
    log.setLevel(level);
    log.setUserId(me == null ? "" : me.getId());
    // log.setContext(""); // how to capture the business object id in context???
    log.setStack(stack);
    JpaLoggerService ls = Application.getBean(JpaLoggerService.class);
    try {
      ls.add(log);
    } catch (Throwable t) {
    }
  }

}
