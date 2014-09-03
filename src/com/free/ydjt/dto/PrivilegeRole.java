package com.free.ydjt.dto;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FND_PRIVILEGE_ROLES")
public class PrivilegeRole extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = 596504451378599907L;

  @Id
  private PK pk;

  public PrivilegeRole() {
  }

  public String getPrivilegeId() {
    return pk == null ? null : pk.getPrivilegeId();
  }

  public String getRoleId() {
    return pk == null ? null : pk.getRoleId();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getName()).append("{")
      .append("pk=").append(pk).append("}");
    return sb.toString();
  }

  public static class PK extends AnnotatedEntity implements Serializable {

    private static final long serialVersionUID = 6403444052814853807L;

    @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRIVILEGE_ID", insertable = true, updatable = true)
    private Privilege privilege;

    @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID", insertable = true, updatable = true)
    private Role role;

    public PK() {
    }

    public String getPrivilegeId() {
      Privilege p = getPrivilege();
      return p.getId();
    }

    public Privilege getPrivilege() {
      return (Privilege) unproxy(privilege);
    }

    public void setPrivilege(Privilege privilege) {
      this.privilege = privilege;
    }

    public String getRoleId() {
      Role r = getRole();
      return r.getId();
    }

    public Role getRole() {
      return (Role) unproxy(role);
    }

    public void setRole(Role role) {
      this.role = role;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(getClass().getName()).append("{")
        .append("privilegeId=").append(getPrivilegeId())
        .append(", roleId=").append(getRoleId()).append("}");
      return sb.toString();
    }
  }

}
