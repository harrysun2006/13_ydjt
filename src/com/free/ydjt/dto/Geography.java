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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FND_GEOGRAPHIES")
public class Geography extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = 8067462667262056274L;

  @Id
  @Column(name = "GEOGRAPH_ID")
  private String id;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE")
  private Type type;

  @Column(name = "NAME")
  private String name;

  @Column(name = "CODE")
  private String code;

  @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
  @JoinColumn(name = "PARENT_GEOGRAPH_ID", insertable = true, updatable = true)
  private Geography parent;

  @Column(name = "DESCRIPTION")
  private String description;

  @Lob
  @Column(name = "SILO")
  private String silo;

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

  public enum Type {
    COUNTRY,
    STATE,
    PROVINCE,
    CITY,
    POSTCODE,
  };

  public Geography() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Geography getParent() {
    return (Geography) unproxy(parent);
  }

  public void setParent(Geography parent) {
    this.parent = parent;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getSilo() {
    return silo;
  }

  public void setSilo(String silo) {
    this.silo = silo;
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
      .append("type=").append(type)
      .append(", code=").append(code)
      .append(", name=").append(name).append("}");
    return sb.toString();
  }
}
