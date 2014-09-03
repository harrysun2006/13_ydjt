package com.free.ydjt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.free.dao.JpaDao;
import com.free.ydjt.dto.Log;

@Service
public class LogService {

  @Autowired
  @Qualifier("daoApp")
  protected JpaDao dao;

  public List<Log> list() {
    return dao.find(Log.class, "select l from Log l order by l.dateline desc");
  }

  public void clear() {
    dao.execute("delete from Log l");
  }

}
