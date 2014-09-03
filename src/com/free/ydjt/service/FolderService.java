package com.free.ydjt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.free.Application;
import com.free.dao.JpaDao;
import com.free.ydjt.dto.Folder;

@Service
public class FolderService {

  @Autowired
  @Qualifier("daoApp")
  protected JpaDao dao;

  public List<Folder> list(String category) {
    String hql = "select f from Folder f where f.catCode=:cat";
    return dao.find(Folder.class, hql, category);
  }

  public Folder get(String id) {
    return dao.get(Folder.class, id);
  }

  public void add(Folder f) {
    if (f.getId() == null) f.setId(Application.uuid());
    Folder pf = f.getParentId() == null ? null : get(f.getParentId());
    if (pf != null) {
      f.setFullId(pf.getFullId() + "/" + f.getId());
      f.setFullName(pf.getFullName() + "/" + f.getName());
      f.setCatCode(pf.getCatCode());
    } else {
      f.setFullId(f.getId());
      f.setFullName(f.getName());
    }
    dao.add(f);
  }
}
