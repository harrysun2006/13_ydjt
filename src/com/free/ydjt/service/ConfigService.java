package com.free.ydjt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.free.annotation.ClearCache;
import com.free.annotation.MethodCache;
import com.free.dao.JpaDao;
import com.free.ydjt.dto.Config;

@Service
public class ConfigService {

  @Autowired
  @Qualifier("daoApp")
  protected JpaDao dao;

  public Config get(String name) {
    String hql = "select c from Config c where c.name = ?";
    List<Config> l = dao.find(Config.class, hql, name);
    return l.size() > 0 ? l.get(0) : null;
  }

  @MethodCache(name = "CONFIG_ALL")
  public List<Config> getConfigs() {
    return dao.find(Config.class, "select c from Config c order by c.name");
  }

  @ClearCache(name = "CONFIG_ALL")
  public void add(Config conf) {
    dao.add(conf);
  }

  @ClearCache(name = "CONFIG_ALL")
  public void save(Config conf) {
    dao.save(conf);
  }

}
