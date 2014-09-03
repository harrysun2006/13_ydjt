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
@Table(name = "FND_USER_GROUPS")
public class UserGroup extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = 7638944654005938878L;

  @Id
  private PK pk;

  public UserGroup() {
  }

  public String getUserId() {
    return pk == null ? null : pk.getUserId();
  }

  public String getGroupId() {
    return pk == null ? null : pk.getGroupId();
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

    private static final long serialVersionUID = -1482570249289335273L;

    @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", insertable = true, updatable = true)
    private User user;

    @ManyToOne(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID", insertable = true, updatable = true)
    private Group group;

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

    public String getGroupId() {
      Group g = getGroup();
      return g.getId();
    }

    public Group getGroup() {
      return group;
    }

    public void setGroup(Group group) {
      this.group = group;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(getClass().getName()).append("{")
        .append("userId=").append(getUserId())
        .append(", groupId=").append(getGroupId()).append("}");
      return sb.toString();
    }
  }
}
