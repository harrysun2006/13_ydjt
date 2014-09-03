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
@Table(name = "FND_USER_ROLES")
public class UserRole extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = -9217420045514426743L;

  @Id
  private PK pk;

  public UserRole() {
  }

  public String getUserId() {
    return pk == null ? null : pk.getUserId();
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

    private static final long serialVersionUID = 5309768028223634405L;

    @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", insertable = true, updatable = true)
    private User user;

    @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID", insertable = true, updatable = true)
    private Role role;

    public PK() {
    }

    public String getUserId() {
      User u = getUser();
      return u.getId();
    }

    public User getUser() {
      return (User) unproxy(user);
    }

    public void setUser(User user) {
      this.user = user;
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
        .append("userId=").append(getUserId())
        .append(", roleId=").append(getRoleId()).append("}");
      return sb.toString();
    }
  }
}
