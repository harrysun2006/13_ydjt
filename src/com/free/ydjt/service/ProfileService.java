package com.free.ydjt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.free.dao.JpaDao;

@Service
public class ProfileService {

  @Autowired
  @Qualifier("daoApp")
  protected JpaDao dao;

}
