package com.free.ydjt.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.Transient;

@Entity
@Table(name = "FND_USERS")
public class User extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = -5659807635225500056L;

  @Id
  @Column(name = "USER_ID")
  private String id;

  @Column(name = "LOGIN_NAME")
  private String loginName;

  @Column(name = "NICK_NAME")
  private String nickName;

  @Column(name = "CELLPHONE")
  private String cellphone;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "QQ")
  private String qq;

  @Column(name = "SALT")
  private String salt;

  @Column(name = "PASSWORD")
  private String password;

  @Column(name = "SITE_ID")
  private String siteId;

  @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
  @JoinColumn(name = "PERSON_ID", insertable = true, updatable = true)
  private Person person;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "LAST_LOGIN_DATE")
  private Calendar lastLoginDate;

  @Column(name = "LAST_LOGIN_IP")
  private String lastLoginIp;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private Enums.Status status = Enums.Status.Y;

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

  @Transient
  private Set<String> privileges = new HashSet<String>();

  public User() {
  }

  public User(String loginName, String nickName, String salt, String password) {
    this(null, loginName, nickName, null, null, null, salt, password, null);
  }

  public User(String id, String loginName, String nickName, String cellphone, String email, String qq, String salt, String password, String siteId) {
    setId(id);
    setLoginName(loginName);
    setNickName(nickName);
    setCellphone(cellphone);
    setEmail(email);
    setQq(qq);
    setSalt(salt);
    setPassword(password);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
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

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSiteId() {
    return siteId;
  }

  public void setSiteId(String siteId) {
    this.siteId = siteId;
  }

  public Person getPerson() {
    return (Person) unproxy(person);
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public Calendar getLastLoginDate() {
    return lastLoginDate;
  }

  public void setLastLoginDate(Calendar lastLoginDate) {
    this.lastLoginDate = lastLoginDate;
  }

  public String getLastLoginIp() {
    return lastLoginIp;
  }

  public void setLastLoginIp(String lastLoginIp) {
    this.lastLoginIp = lastLoginIp;
  }

  public Enums.Status getStatus() {
    return status;
  }

  public void setStatus(Enums.Status status) {
    this.status = status;
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

  public Set<String> getPrivileges() {
    return privileges;
  }

  public void setPrivileges(Set<String> privileges) {
    this.privileges = privileges;
  }

  public void addPrivilege(String privilege) {
    privileges.add(privilege);
  }

  public void removePrivilege(String privilege) {
    privileges.remove(privilege);
  }

  public void clearPrivileges() {
    privileges.clear();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getName()).append("{")
      .append("loginName=").append(loginName)
      .append(", siteId=").append(siteId).append("}");
    return sb.toString();
  }
}
