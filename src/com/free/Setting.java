package com.free;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.free.dao.JpaDao;
import com.free.util.CommonUtil;
import com.free.ydjt.dto.Config;
import com.free.ydjt.service.ConfigService;

/**
 * 应用程序全局设置 -- CONFIG
 * 
 * @author Harry Sun harrysun2006@gmail.com
 * 
 */
@Component
public class Setting {

  // 设置
  public static final Map<String, Config> SETTINGS = new LinkedHashMap<String, Config>();

  public static final String APP_FORMAT_DATE = "app.format.date";

  public static final String APP_FORMAT_DATETIME = "app.format.datetime";

  public static final String APP_USE_VCODE = "app.use.vcode";

  @Autowired
  @Qualifier("daoApp")
  protected JpaDao dao;

  public static void load() {
    ConfigService cs = Application.getBean(ConfigService.class);
    List<Config> confs = cs.getConfigs();
    for (Config conf : confs)
      SETTINGS.put(conf.getCode(), conf);
  }

  public static void reload() {
    SETTINGS.clear();
    load();
  }

  public static Config get(String code) {
    return SETTINGS.get(code);
  }

  public static void set(String code, Config conf) {
    SETTINGS.put(code, conf);
  }

  public static String getValue(String code) {
    Config conf = get(code);
    return conf == null ? "" : conf.getValue();
  }

  public static String getValue(String code, String def) {
    Config conf = get(code);
    return conf == null ? def : conf.getValue();
  }

  public static void setValue(String code, String value) {
    Config conf = get(code);
    ConfigService cs = Application.getBean(ConfigService.class);
    if (conf == null) {
      conf = new Config(code);
      conf.setValue(value);
      cs.add(conf);
    } else {
      conf.setValue(value);
      cs.save(conf);
    }
    SETTINGS.put(code, conf);
  }

  public static Integer getInteger(String code) {
    return getInteger(code, null);
  }

  public static Integer getInteger(String code, Integer def) {
    Config conf = get(code);
    return conf == null ? def : CommonUtil.getInt(conf.getValue());
  }

  public static boolean getBoolean(String code) {
    return getBoolean(code, false);
  }

  public static boolean getBoolean(String code, boolean def) {
    Config conf = get(code);
    return conf == null ? def : CommonUtil.getBoolean(conf.getValue());
  }

  public static void setBoolean(String code, boolean b) {
    Config conf = get(code);
    if (conf == null) conf = new Config(code);
    conf.setValue(String.valueOf(b));
    ConfigService cs = Application.getBean(ConfigService.class);
    cs.save(conf);
    SETTINGS.put(code, conf);
  }

}
