package com.free.misc;

import org.apache.log4j.spi.LoggingEvent;

public interface WrapperLogger {

  public void append(LoggingEvent event, String stack, String msg) throws Throwable;

}
