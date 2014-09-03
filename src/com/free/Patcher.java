package com.free;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.free.dao.JpaDao;
import com.free.util.FileUtil;
import com.free.util.JSONUtil;
import com.free.ydjt.dto.Config;
import com.free.ydjt.service.ConfigService;
import com.free.ydjt.service.SiteService;
import com.free.ydjt.service.UserService;

/**
 * 暂时不考虑程序的升级(可能通过jar替换)，参考http://www.iteye.com/topic/136427
 * OSGI粒度小一点的， 可以单个jar包的reload 
 * 目前只考虑配置数据(seed data)的升级及设置数据(如数据库、上传路径等的设置，未设置时使用缺省值)的检查 
 * @author Harry Sun harrysun2006@gmail.com
 *
 */
public class Patcher {

  public static enum Status {
    UNKNOWN,   // 未知
    STARTER,   // 起始 -- seed data已载入db, 需要设置?
    READY;     // 可正常使用
  };

  public static Status STATUS = Status.UNKNOWN;

  private static final String INIT_DATA_FILE = "data/init.json";

  @Autowired
  @Qualifier("daoZF")
  protected JpaDao dao;

  // TODO: 检查并返回系统状态
  @SuppressWarnings("unchecked")
  protected static void check() throws IOException {
    // 检查系统站点
    SiteService ss = Application.getBean(SiteService.class);
    Application.MY_SITE = ss.sys();
    // 检查系统用户
    UserService us = Application.getBean(UserService.class);
    Application.SYS_USER = us.sys();
    STATUS = Status.READY;
  }

  protected static void install() throws IOException {
    String s = FileUtil.read(INIT_DATA_FILE);
    Map<String, Object> data = JSONUtil.getObject(s, Map.class);
    ConfigService cs = Application.getBean(ConfigService.class);
    Config conf;
    List<Map<String, Object>> items, l;
    // T_DICT:
    /*
    items = (List<Map<String, Object>>) data.get("T_DICT");
    DictService ds = Application.getBean(DictService.class);
    Dict dict;
    Dict.Cat dc;
    for (Map<String, Object> d1 : items) {
      dc = Dict.Cat.valueOf((String) d1.get("cat"));
      if (dc == null)
        continue;
      l = (List<Map<String, Object>>) d1.get("items");
      for (Map<String, Object> d2 : l) {
        dict = ds.get(dc, (String) d2.get("code"));
        if (dict == null) {
          dict = new Dict();
          dict.setAttributes(d2);
          dict.setCat(dc);
          ds.add(dict);
        }
      }
    }
    */
    /**
     * T_CONFIG:ids.use.vcode(是否启用验证码) zf.use.vcode: {"name":"zf.use.vcode",
     * "description":"是否使用验证码", "value":"false", "readOnly":false, "hint":
     * "{\"type\":\"select\",\"list\":[{\"text\":\"启用\",\"value\":\"true\"},{\"text\":\"禁用\",\"value\":\"false\"}]}"
     * }
     */
    /*
    items = (List<Map<String, Object>>) data.get("T_CONFIG");
    String name, value, description, hint;
    boolean readOnly = false;
    Integer idx;
    for (Map<String, Object> c1 : items) {
      if (!c1.containsKey("name") || !c1.containsKey("value")) continue;
      name = (String) c1.get("name");
      value = (String) c1.get("value");
      readOnly = c1.containsKey("readOnly") ? (Boolean) c1.get("readOnly") : true;
      description = c1.containsKey("description") ? (String) c1.get("description") : null;
      hint = c1.containsKey("hint") ? (String) c1.get("hint") : null;
      conf = cs.get(name);
      if (conf == null) {
        conf = new Config(name, value, description, hint, readOnly);
        cs.add(conf);
      } else if (readOnly) { // readonly=true的配置通过init.json修改, 其他通过后台管理修改
        conf.setValue(value);
        conf.setReadOnly(readOnly ? 'Y' : 'N');
        conf.setDescription(description);
        conf.setHint(hint);
        cs.save(conf);
      }
    }
    */
  }

}
