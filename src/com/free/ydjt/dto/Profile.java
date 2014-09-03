package com.free.ydjt.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "FND_PROFILES")
public class Profile extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = 854597200759948817L;

  @Id
  private PK pk;

  @Lob
  @Column(name = "SILO")
  private String silo;

  public Profile() {
  }

  public String getOwnerId() {
    return pk == null ? null : pk.getOwnerId();
  }

  public String getOwnerType() {
    return pk == null || pk.getOwnerType() == null ? null : pk.getOwnerType().name();
  }

  public String getLayoutType() {
    return pk == null || pk.getLayoutType() == null ? null : pk.getLayoutType().name();
  }

  public PK getPk() {
    return pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public String getSilo() {
    return silo;
  }

  public void setSilo(String silo) {
    this.silo = silo;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getName()).append("{")
      .append("pk=").append(pk).append("}");
    return sb.toString();
  }

  public static class PK extends AnnotatedEntity implements Serializable {

    private static final long serialVersionUID = 2864947574035390924L;

    @Column(name = "OWNER_ID")
    private String ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "OWNER_TYPE")
    private OwnerType ownerType = OwnerType.USER;

    @Enumerated(EnumType.STRING)
    @Column(name = "LAYOUT_TYPE")
    private LayoutType layoutType = LayoutType.PC;

    public enum OwnerType {
      USER,   // 用户
      PERSON, // 人
      GROUP,  // 用户组
      ROLE,   // 角色
      SITE,   // 站点
    };

    public enum LayoutType {
      PC,
      PAD7,
      PAD9,
      PAD10,
      UNKNOWN,
    };

    public PK() {
    }

    public String getOwnerId() {
      return ownerId;
    }

    public void setOwnerId(String ownerId) {
      this.ownerId = ownerId;
    }

    public OwnerType getOwnerType() {
      return ownerType;
    }

    public void setOwnerType(OwnerType ownerType) {
      this.ownerType = ownerType;
    }

    public LayoutType getLayoutType() {
      return layoutType;
    }

    public void setLayoutType(LayoutType layoutType) {
      this.layoutType = layoutType;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(getClass().getName()).append("{")
        .append("ownerId=").append(ownerId)
        .append(", ownerType=").append(ownerType)
        .append(", layoutType=").append(layoutType).append("}");
      return sb.toString();
    }
  }
}
