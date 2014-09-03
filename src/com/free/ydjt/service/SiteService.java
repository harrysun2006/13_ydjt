package com.free.ydjt.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.free.Application;
import com.free.dao.JpaDao;
import com.free.ydjt.dto.Site;

@Service
public class SiteService {

  @Autowired
  @Qualifier("daoApp")
  protected JpaDao dao;

  public List<Site> list() {
    String hql = "select s from Site s";
    return dao.find(Site.class, hql);
  }

  public Site sys() {
    String hql = "select s from Site s where s.cat = ?";
    List<Site> sites = dao.find(Site.class, hql, Site.Cat.ME);
    Site me;
    if (sites.size() == 1) {
      me = sites.get(0);
    } else {
      me = new Site(Application.uuid(), "SITE_ME", "SITE_ME", Site.Cat.ME);
      Calendar cal = Calendar.getInstance();
      me.setCreatedBy(Application.DUMP_UUID);
      me.setCreationDate(cal);
      me.setLastUpdatedBy(Application.DUMP_UUID);
      me.setLastUpdateDate(cal);
      dao.execute("delete Site s where s.cat = ?", Site.Cat.ME);
      dao.add(me);
    }
    return me;
  }
}
