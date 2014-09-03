package com.free.misc;

import java.util.Properties;

import org.osjava.sj.loader.convert.Converter;

import com.jolbox.bonecp.BoneCPDataSource;

public class DataSourceConverter implements Converter {

  public Object convert(Properties props, String type) {
    if (props.containsKey("driver") && !props.containsKey("driverClass"))
      props.put("driverClass", props.get("driver"));
    if (props.containsKey("url") && !props.containsKey("jdbcUrl"))
      props.put("jdbcUrl", props.get("url"));
    if (props.containsKey("user") && !props.containsKey("username"))
      props.put("username", props.get("user"));

    String driver = props.getProperty("driverClass");
    String url = props.getProperty("jdbcUrl");
    String username = props.getProperty("username");
    String password = props.getProperty("password");

    if (driver == null)
      throw new RuntimeException("Required property 'driverClass'");
    if (url == null)
      throw new RuntimeException("Required subelement 'jdbcUrl'");
    if (username == null)
      throw new RuntimeException("Required subelement 'username'");
    if (password == null)
      throw new RuntimeException("Required subelement 'password'");

    BoneCPDataSource ds = new BoneCPDataSource();
    try {
      ds.setProperties(props);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return ds;
  }

}