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
@Table(name = "FND_LOOKUP_VALUES")
public class LookupValue extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = 1850500841145285287L;

  @Id
  private PK pk;

  @Column(name = "MEANING")
  private String meaning;

  @Column(name = "DESCRIPTION")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "ENABLED_FLAG")
  private Enums.YesNo enabled = Enums.YesNo.Y;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "EFFECTIVE_START_DATE")
  private Calendar effectiveStartDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "EFFECTIVE_END_DATE")
  private Calendar effectiveEndDate;

  @Column(name = "DISPLAY_SEQUENCE")
  private Integer displaySequence;

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

  public LookupValue() {
  }

  public String getType() {
    return pk == null ? null : pk.getType();
  }

  public String getCode() {
    return pk == null ? null : pk.getCode();
  }

  public PK getPk() {
    return pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public String getMeaning() {
    return meaning;
  }

  public void setMeaning(String meaning) {
    this.meaning = meaning;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Enums.YesNo getEnabled() {
    return enabled;
  }

  public void setEnabled(Enums.YesNo enabled) {
    this.enabled = enabled;
  }

  public Calendar getEffectiveStartDate() {
    return effectiveStartDate;
  }

  public void setEffectiveStartDate(Calendar effectiveStartDate) {
    this.effectiveStartDate = effectiveStartDate;
  }

  public Calendar getEffectiveEndDate() {
    return effectiveEndDate;
  }

  public void setEffectiveEndDate(Calendar effectiveEndDate) {
    this.effectiveEndDate = effectiveEndDate;
  }

  public Integer getDisplaySequence() {
    return displaySequence;
  }

  public void setDisplaySequence(Integer displaySequence) {
    this.displaySequence = displaySequence;
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
      .append("pk=").append(pk)
      .append(", meaning=").append(meaning).append("}");
    return sb.toString();
  }

  public static class PK extends AnnotatedEntity implements Serializable {

    private static final long serialVersionUID = 1575380658305136846L;

    @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "LOOKUP_TYPE", insertable = true, updatable = true)
    private LookupType lookupType;

    @Column(name = "LOOKUP_CODE")
    private String code;

    public PK() {
    }

    public String getType() {
      LookupType t = getLookupType();
      return t.getType();
    }

    public LookupType getLookupType() {
      return (LookupType) unproxy(lookupType);
    }

    public void setLookupType(LookupType lookupType) {
      this.lookupType = lookupType;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(getClass().getName()).append("{")
        .append("type=").append(getType())
        .append(", code=").append(code).append("}");
      return sb.toString();
    }

  }
}
