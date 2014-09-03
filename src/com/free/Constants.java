package com.free;

import java.util.Calendar;

import com.free.util.DateUtil;

/**
 * 定义应用程序常量
 * 
 * @author Harry Sun <harrysun2006@gmail.com>
 */
public interface Constants {

  // 最大日期值 9999-12-31 0:0:0.0
  public static final Calendar MAX_DATE = DateUtil.getMaxCalendar();

  // 最小日期值 1901-01-01 0:0:0.0
  public static final Calendar MIN_DATE = DateUtil.getMinCalendar();

  public static final String CAPTCHA_SESSION_KEY = "#ids_captcha";

  public static final String CRITERIA_ALL = "@ALL";
}
