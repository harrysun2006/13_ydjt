package com.free.ydjt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.free.dao.JpaDao;

@Service
public class MiscService {

  @Autowired
  @Qualifier("daoApp")
  protected JpaDao dao;

  public Integer nextId(String name) {
    Integer id = null;
    try {
      StringBuilder sb = new StringBuilder("com.sess.ids.dto.").append(name.substring(0, 1).toUpperCase()).append(
          name.substring(1).toLowerCase());
      Class<?> clazz = Class.forName(sb.toString());
      id = dao.nextId(clazz);
    } catch (Exception e) {
    }
    return id;
  }

}
