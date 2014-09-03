package com.free.ydjt.dto;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FND_PERSONS")
public class Person extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = 8970769949386506777L;

  @Id
  @Column(name = "PERSON_ID")
  private String id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "SHORTNAME")
  private String shortName;

  @Enumerated(EnumType.STRING)
  @Column(name = "IDENTITY_TYPE")
  private IdentityType identityType;

  @Column(name = "IDENTITY_NUMBER")
  private String identityNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "GENDER")
  private Enums.Gender gender = Enums.Gender.U;

  @Column(name = "PHONE")
  private String phone;

  @Column(name = "CELLPHONE")
  private String cellphone;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "QQ")
  private String qq;

  @Column(name = "SITE_ID")
  private String siteId;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private Enums.Status status = Enums.Status.Y;

  @Column(name = "DESCRIPTION")
  private String description;

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

  public enum IdentityType { // 证件类型
    ID, // 身份证
    DL, // 驾驶证
    PP, // 护照
    OC, // 军官证
    SC, // 学生证
  };

  public Person() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public IdentityType getIdentityType() {
    return identityType;
  }

  public void setIdentityType(IdentityType identityType) {
    this.identityType = identityType;
  }

  public String getIdentityNumber() {
    return identityNumber;
  }

  public void setIdentityNumber(String identityNumber) {
    this.identityNumber = identityNumber;
  }

  public Enums.Gender getGender() {
    return gender;
  }

  public void setGender(Enums.Gender gender) {
    this.gender = gender;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getCellphone() {
    return cellphone;
  }

  public void setCellphone(String cellphone) {
    this.cellphone = cellphone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getQq() {
    return qq;
  }

  public void setQq(String qq) {
    this.qq = qq;
  }

  public String getSiteId() {
    return siteId;
  }

  public void setSiteId(String siteId) {
    this.siteId = siteId;
  }

  public Enums.Status getStatus() {
    return status;
  }

  public void setStatus(Enums.Status status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getName()).append("{")
      .append("name=").append(name)
      .append(", identityType=").append(identityType)
      .append(", identityNumber=").append(identityNumber).append("}");
    return sb.toString();
  }
}
