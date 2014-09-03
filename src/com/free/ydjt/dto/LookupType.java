package com.free.ydjt.dto;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FND_LOOKUP_TYPES")
public class LookupType extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = 9109394561209755229L;

  @Id
  @Column(name = "LOOKUP_TYPE")
  private String type;

  @Column(name = "SITE_ID")
  private String siteId;

  @Column(name = "DESCRIPTION")
  private String description;

  @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
  @JoinColumn(name = "CREATED_BY", insertable = true, updatable = true)
  private User createdBy;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATION_DATE")
  private Calendar creationDate;

  @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
  @JoinColumn(name = "LAST_UPDATED_BY", insertable = true, updatable = true)
  private User lastUpdatedBy;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_DATE")
  private Calendar lastUpdateDate;

  public LookupType() {
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSiteId() {
    return siteId;
  }

  public void setSiteId(String siteId) {
    this.siteId = siteId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public User getCreatedBy() {
    return (User) unproxy(createdBy);
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public Calendar getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Calendar creationDate) {
    this.creationDate = creationDate;
  }

  public User getLastUpdatedBy() {
    return (User) unproxy(lastUpdatedBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
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
      .append("type=").append(type).append("}");
    return sb.toString();
  }
}
