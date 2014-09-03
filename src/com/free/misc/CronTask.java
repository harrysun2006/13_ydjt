package com.free.misc;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.free.Application;
import com.free.util.CommonUtil;

public class CronTask {

  protected static final Logger logger = LoggerFactory.getLogger(CronTask.class);

  public void sayHello() {
    System.out.println("Say Hello: " + CommonUtil.formatCalendar(Application.DATETIME_FORMAT, Calendar.getInstance()));
  }
}
