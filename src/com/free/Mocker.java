package com.free;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniFactorySupport;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.apache.shiro.web.config.WebIniSecurityManagerFactory;
import org.h2.tools.Server;
import org.springframework.util.ResourceUtils;

import com.free.util.FileUtil;

/**
 * 启动测试需要的环境: h2数据库, JNDI, Spring...
 * 
 * @author Harry Sun <harrysun2006@gmail.com>
 */
public class Mocker {

  private static final String DEFAULT_NAME = "app";
  private static final int DEFAULT_PORT = 8001;
  private static final String[] H2_SERVER_ARGS = {"-tcp", "-tcpAllowOthers", "-web", "-webAllowOthers", "-baseDir", ""};

  private Properties props;
  private String name;
  private String webXml;

  private Mocker(String name) throws IOException {
    this.name = name;
    this.props = new Properties();
    File f;
    if (FileUtil.canRead("classpath:" + name + ".properties"))
      f = ResourceUtils.getFile("classpath:" + name + ".properties");
    else
      f = ResourceUtils.getFile("classpath:" + DEFAULT_NAME + ".properties");
    InputStream is = new FileInputStream(f);
    props.load(is);
    is.close();
    if ("test".equals(name))
      webXml = "web-test.xml";
    else
      webXml = "web.xml";
  }

  public static Mocker init() throws IOException, SQLException {
    return init(DEFAULT_NAME);
  }

  public static Mocker init(String name) throws IOException, SQLException {
    Mocker mocker = new Mocker(name);
    boolean h2 = mocker.getBoolean("h2.autostart", false);
    boolean jndi = mocker.getBoolean("jndi.create", true);
    String configs = mocker.getString("spring.configs", null);
    initShiro();
    if (h2) initH2();
    if (jndi) initJNDI(name);
    if (configs != null) initSpring(configs);
    return mocker;
  }

  private String getString(String key, String def) {
    return props.getProperty(key, def);
  }

  private int getInt(String key, int def) {
    return Integer.parseInt(getString(key, String.valueOf(def)));
  }

  private boolean getBoolean(String key, boolean def) {
    return Boolean.parseBoolean(getString(key, String.valueOf(def)));
  }

  public String getName() {
    return this.name;
  }

  public String getWebXml() {
    return this.webXml;
  }

  public int getPort() {
    return getPort(DEFAULT_PORT);
  }

  public int getPort(int def) {
    return getInt("web.port", def);
  }

  private static void initShiro() {
    // 1.装入INI配置, classpath:shiro.ini
    Ini ini = IniFactorySupport.loadDefaultClassPathIni();
    ini.getSection("main").put("securityManager", "com.free.misc.AutoSecurityManager");
    Factory<SecurityManager> factory = new WebIniSecurityManagerFactory(ini);
    // 2. 创建SecurityManager
    SecurityManager sm = factory.getInstance();
    // 3. 使其可访问
    SecurityUtils.setSecurityManager(sm);
  }

  /**
   * 如果没有数据库连接则启动数据库
   * @throws Exception
   */
  private static void initH2() throws SQLException {
    Connection conn = null;
    try {
      Class.forName("org.h2.Driver");
      String url = "jdbc:h2:tcp://localhost/h2/ydjt";
      conn = DriverManager.getConnection(url, "YDJT", "admin");
    } catch (Exception e1) {
    }
    if (conn == null) {
      String dir = System.getProperty("user.dir");
      H2_SERVER_ARGS[H2_SERVER_ARGS.length-1] = dir + "/data";
      Server.main(H2_SERVER_ARGS);
    }
  }

  private static void initJNDI(String name) throws IOException {
    // 初始化simple-jndi
    File f;
    if (FileUtil.canRead("classpath:META-INF/" + name))
      f = ResourceUtils.getFile("classpath:META-INF/" + name);
    else
      f = ResourceUtils.getFile("classpath:META-INF/" + DEFAULT_NAME);
    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.osjava.sj.SimpleContextFactory");
    System.setProperty("org.osjava.sj.root", f.getPath());
    System.setProperty("org.osjava.sj.delimiter", "/");
    System.setProperty("org.osjava.sj.space", "java:comp/env");
  }

  /**
   * 初始化Spring
   * 
   * @throws Excpetion
   */
  private static void initSpring(String configs) {
    Application.init(configs);
  }

}
