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
@Table(name = "FND_GROUP_ROLES")
public class GroupRole extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = -5246616118824024835L;

  @Id
  private PK pk;

  public GroupRole() {
  }

  public String getGroupId() {
    return pk == null ? null : pk.getGroupId();
  }

  public String getRoleId() {
    return pk == null ? null : pk.getRoleId();
  }

  public PK getPk() {
    return pk;
  }

  public void setPk(PK pk) {
    this.pk = pk;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getName()).append("{")
      .append("pk=").append(pk).append("}");
    return sb.toString();
  }

  public static class PK extends AnnotatedEntity implements Serializable {

    private static final long serialVersionUID = -6017261200462459649L;

    @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID", insertable = true, updatable = true)
    private Group group;

    @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID", insertable = true, updatable = true)
    private Role role;

    public PK() {
    }

    public String getGroupId() {
      Group g = getGroup();
      return g.getId();
    }

    public Group getGroup() {
      return (Group) unproxy(group);
    }

    public void setGroup(Group group) {
      this.group = group;
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
        .append("groupId=").append(getGroupId())
        .append(", roleId=").append(getRoleId()).append("}");
      return sb.toString();
    }

  }
}
