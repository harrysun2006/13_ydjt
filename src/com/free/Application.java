package com.free;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.free.ydjt.dto.Site;
import com.free.ydjt.dto.User;

/**
 * 定义应用程序变量及方法, 初始化应用程序<br>
 * 1. 设置时区<br>
 * 2. 读取应用配置<br>
 * 3. 初始化Spring<br>
 * 4. 读取缓存数据 使用AOP打印log信息<br>
 * 
 * 配置方式: Application/Web Application -> Spring Context -> Properties
 * 
 * @author Harry Sun <harrysun2006@gmail.com>
 */
public class Application {

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  private static final String[] EMPTY_STRINGS = {};

  // 版本
  public static final String VERSION = "1.0.130106";

  // 配置项 - 上传路径
  public static final String PROP_UPLOAD_PATH = "upload.path";

  // 默认语言资源包
  public static final String RESOURCE_BUNDLE = "langs/app";

  // 默认Spring配置文件
  public static final String SPRING_CONFIGS = "classpath:/META-INF/app-spring3.xml";

  // 默认上传路径
  public static String UPLOAD_PATH = "../upload/";

  // 可以通过FND_CONFIG进行配置
  // 默认日期格式
  public static String DATE_FORMAT = "yyyy-MM-dd";

  // 默认日期时间格式
  public static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  // 上传文件最大字节数
  public static final int MAX_UPLOAD_SIZE = 20 * 1024 * 1024; // 20M

  public static final int THRESHOLD_SIZE = 2 * 1024 * 1024; // 2M

  public static String CONTEXT_PATH = "";

  // 本Site
  protected static Site MY_SITE = null;

  // 系统用户
  protected static User SYS_USER = null;

  // dump uuid
  public static final String DUMP_UUID = "00000000000000000000000000000000";

  // 主键比较器
  public static final Comparator<Object> ID_COMPARATOR = new IdentityComparator();

  protected static Properties props = new Properties();

  protected static ApplicationContext ctx = null;

  protected static boolean isBlank(String text) {
    return (text == null) || text.trim().length() <= 0;
  }

  public static void init() {
    init(SPRING_CONFIGS);
  }

  public static void init(Map<Object, Object> env) {
    props.putAll(env);
    init(SPRING_CONFIGS);
  }

  public static void init(String configs) {
    String[] cs = isBlank(configs) ? EMPTY_STRINGS : configs.split(",");
    for (int i = 0; i < cs.length; i++)
      cs[i] = cs[i].trim();
    boolean fileConfig = false;
    // 设置时区
    // TimeZone UTC = TimeZone.getTimeZone("UTC");
    // TimeZone.setDefault(UTC);
    // 初始化Spring, Web容器 -> Create New
    ctx = ContextLoader.getCurrentWebApplicationContext();
    if (ctx == null) {
      ctx = new ClassPathXmlApplicationContext(cs);
      fileConfig = true;
    }
    // 初始化Properties
    Properties config = ctx.getBean("config", Properties.class);
    if (config != null) props.putAll(config);
    // 设置静态变量
    UPLOAD_PATH = getString(PROP_UPLOAD_PATH, UPLOAD_PATH);
    // 检查并初始化系统配置
    try {
      Patcher.check();
    } catch (Exception e) {
      logger.error("", e);
    }
    // 读取应用配置
    Setting.load();
    DATE_FORMAT = Setting.getValue(Setting.APP_FORMAT_DATE, DATE_FORMAT);
    DATETIME_FORMAT = Setting.getValue(Setting.APP_FORMAT_DATETIME, DATETIME_FORMAT);
    // 读取缓存数据
    // 启动日志
    logger.info("TimeZone: {}.", TimeZone.getDefault().getID());
    logger.info("Hibernate version: {}!", org.hibernate.Version.getVersionString());
    logger.info("Spring version: {}", org.springframework.core.SpringVersion.getVersion());
    if (fileConfig)
      logger.info("Spring configs: {}.", configs);
    StringBuilder sb;
    if (getBoolean("log.sysenv", false)) {
      sb = new StringBuilder("System enviroment variables:\n");
      Map<String, String> env = System.getenv();
      for (Entry<String, String> e : env.entrySet()) {
        sb.append(e.getKey()).append(" = ").append(e.getValue()).append("\n");
      }
      logger.info(sb.toString());
    }
    if (getBoolean("log.sysprops", false)) {
      sb = new StringBuilder("System property values:\n");
      Properties props = System.getProperties();
      for (Entry<Object, Object> e : props.entrySet()) {
        sb.append(e.getKey()).append(" = ").append(e.getValue()).append("\n");
      }
      logger.info(sb.toString());
    }
  }

  /**
   * props方法
   * 
   * @param key
   * @return
   */
  public static boolean containsKey(String key) {
    return props.containsKey(key);
  }

  public static String[] getArray(String key) {
    return getArray(key, ",");
  }

  public static String[] getArray(String key, String delimiter) {
    String value = getString(key);
    return isBlank(value) ? null : value.split(delimiter);
  }

  public static boolean getBoolean(String key) {
    return getBoolean(key, false);
  }

  public static boolean getBoolean(String key, boolean defaultValue) {
    String value = getString(key);
    if (isBlank(value))
      return defaultValue;
    value = value.trim();
    return (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("T")
        || value.equalsIgnoreCase("Y") || Boolean.getBoolean(value));
  }

  public static int getInt(String key) {
    return getInt(key, 0);
  }

  public static int getInt(String key, int defaultValue) {
    String value = getString(key);
    return isBlank(value) ? defaultValue : Integer.parseInt(value);
  }

  public static long getLong(String key) {
    return getLong(key, 0L);
  }

  public static long getLong(String key, long defaultValue) {
    String value = getString(key);
    return isBlank(value) ? defaultValue : Long.parseLong(value);
  }

  public static double getDouble(String key) {
    return getDouble(key, 0);
  }

  public static double getDouble(String key, int defaultValue) {
    String value = getString(key);
    return isBlank(value) ? defaultValue : Double.parseDouble(value);
  }

  public static String getString(String key) {
    return getString(key, "");
  }

  public static String getString(String key, String defaultValue) {
    if (ctx == null)
      init();
    return props.getProperty(key, defaultValue);
  }

  /**
   * 语言资源包方法
   * 
   * @param code
   * @return
   */
  public static String get(String code) {
    return get(RESOURCE_BUNDLE, code, null, code);
  }

  public static String get(String code, String defaultValue) {
    return get(RESOURCE_BUNDLE, code, null, defaultValue);
  }

  public static String get(String code, Object[] params) {
    return get(RESOURCE_BUNDLE, code, params, null);
  }

  public static String get(String bundle, String code, Object[] params, String defaultValue) {
    String r;
    try {
      ResourceBundle rb = ResourceBundle.getBundle(bundle);
      r = MessageFormat.format(rb.getString(code), params);
    } catch (Exception e) {
      if (defaultValue != null) {
        r = defaultValue;
      } else {
        StringBuilder sb = new StringBuilder();
        sb.append(code).append("[");
        if (params != null) {
          for (int i = 0; i < params.length; i++) {
            sb.append(params[i]).append(", ");
          }
          if (params.length > 0)
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        r = sb.toString();
      }
    }
    return r;
  }

  public static ApplicationContext getContext() {
    if (ctx == null)
      init();
    return ctx;
  }

  public static void refresh() {
    if (ctx != null && ctx instanceof ConfigurableApplicationContext)
      ((ConfigurableApplicationContext) ctx).refresh();
    else
      init();
  }

  public static Object getBean(String name) {
    return getContext().getBean(name);
  }

  public static <T> T getBean(Class<T> clazz) {
    return getContext().getBean(clazz);
  }

  public static String uuid() {
    UUID uid = UUID.randomUUID();
    return String.format("%08X%08X", uid.getMostSignificantBits(), uid.getLeastSignificantBits());
  }

  public static Site mySite() {
    return (MY_SITE == null) ? null : MY_SITE;
  }

  public static String mySiteId() {
    return (MY_SITE == null) ? DUMP_UUID : MY_SITE.getId();
  }
}
