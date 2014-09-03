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
@Table(name = "FND_CONFIGS")
public class Config extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = -3884996730109232972L;

  public static final String DEFAULT_NAME = "SYSTEM CONFIG";

  @Id
  @Column(name = "CONFIG_CODE")
  private String code;

  @Column(name = "NAME")
  private String name;

  @Column(name = "VALUE")
  private String value;
  
  @Column(name = "UI_HINT")
  private String uiHint;

  @Enumerated(EnumType.STRING)
  @Column(name = "READ_ONLY_FLAG")
  private Enums.YesNo readOnly = Enums.YesNo.Y;

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

  public Config() {
  }

  public Config(String code) {
    this(code, DEFAULT_NAME, "", "", "", true);
  }

  public Config(String code, String name, String value, String description, String hint, boolean readOnly) {
    setCode(code);
    setName(name);
    setValue(value);
    setDescription(description);
    setUiHint(hint);
    setReadOnly(Enums.YesNo.valueOf(readOnly));
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

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getUiHint() {
    return uiHint;
  }

  public void setUiHint(String uiHint) {
    this.uiHint = uiHint;
  }

  public Enums.YesNo getReadOnly() {
    return readOnly;
  }

  public void setReadOnly(Enums.YesNo readOnly) {
    this.readOnly = readOnly;
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
      .append("code=").append(code)
      .append(", name=").append(name)
      .append(", value=").append(value).append("}");
    return sb.toString();
  }

}
