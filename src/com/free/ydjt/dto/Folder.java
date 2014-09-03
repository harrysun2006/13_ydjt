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
@Table(name = "FND_FOLDERS")
public class Folder extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = 2078967336617396127L;

  @Id
  @Column(name = "FOLDER_ID")
  private String id;

  @Column(name = "PARENT_ID")
  private String parentId;

  @Column(name = "CODE")
  private String code;

  @Column(name = "NAME")
  private String name;

  @Column(name = "CATEGORY")
  private String catCode;

  @Column(name = "FULL_ID")
  private String fullId;

  @Column(name = "FULL_NAME")
  private String fullName;

  @Column(name = "DISPLAY_SEQUENCE")
  private Integer sequence;

  @Enumerated(EnumType.STRING)
  @Column(name = "ENABLED_FLAG")
  private Enums.YesNo enabled = Enums.YesNo.Y;

  @Enumerated(EnumType.STRING)
  @Column(name = "READ_ONLY_FLAG")
  private Enums.YesNo readOnly = Enums.YesNo.N;

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

  public Folder() {
  }

  public Folder(String pid, String code, String name) {
    this(null, pid, code, name, null, 0);
  }

  public Folder(String pid, String code, String name, String cat) {
    this(null, pid, code, name, cat, 0);
  }

  public Folder(String id, String pid, String code, String name, String cat, int seq) {
    setId(id);
    setParentId(pid);
    setCode(code);
    setName(name);
    setCatCode(cat);
    setSequence(seq);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
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

  public String getCatCode() {
    return catCode;
  }

  public void setCatCode(String catCode) {
    this.catCode = catCode;
  }

  public String getFullId() {
    return fullId;
  }

  public void setFullId(String fullId) {
    this.fullId = fullId;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public Integer getSequence() {
    return sequence;
  }

  public void setSequence(Integer sequence) {
    this.sequence = sequence;
  }

  public Enums.YesNo getEnabled() {
    return enabled;
  }

  public void setEnabled(Enums.YesNo enabled) {
    this.enabled = enabled;
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
      .append(", name=").append(name).append("}");
    return sb.toString();
  }  
}
