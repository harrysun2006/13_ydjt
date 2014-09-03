package com.free.ydjt.dto;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FND_SITES")
public class Site extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = 2807598030654127223L;

  @Id
  @Column(name = "SITE_ID")
  private String id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "NAME")
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "CATEGORY")
  private Cat cat;

  @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
  @JoinColumn(name = "ADDRESS_ID", insertable = true, updatable = true)
  private Address address;

  @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
  @JoinColumn(name = "CONTACT_ID", insertable = true, updatable = true)
  private Person contact;

  @Column(name = "CREATED_BY")
  private String createdBy;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATION_DATE")
  private Calendar creationDate;

  @Column(name = "LAST_UPDATED_BY")
  private String lastUpdatedBy;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_DATE")
  private Calendar lastUpdateDate;

  public enum Cat { // 站点类别, 特性(如小学、初中、高中等)使用标签实现
    ME,      // 本站点
    OTHER,   // 其他站点, 通过证书导入实现站点之间资源共享认证(含有效期)
  }

  public Site() {
  }

  public Site(String id, String code, String name, Cat cat) {
    setId(id);
    setCode(code);
    setName(name);
    setCat(cat);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Cat getCat() {
    return cat;
  }

  public void setCat(Cat cat) {
    this.cat = cat;
  }

  public Address getAddress() {
    return (Address) unproxy(address);
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Person getContact() {
    return (Person) unproxy(contact);
  }

  public void setContact(Person contact) {
    this.contact = contact;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Calendar getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Calendar creationDate) {
    this.creationDate = creationDate;
  }

  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public Calendar getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(Calendar lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  };

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getName()).append("{")
      .append("id=").append(id)
      .append(", cat=").append(cat)
      .append(", code=").append(code)
      .append(", name=").append(name).append("}");
    return sb.toString();
  }
}
