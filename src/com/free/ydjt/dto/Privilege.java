package com.free.ydjt.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.free.ResourceType;

@Entity
@Table(name = "FND_PRIVILEGES")
public class Privilege extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = 6254484318808201814L;

  @Id
  @Column(name = "PRIVILEGE_ID")
  private String id;

  @Enumerated(EnumType.STRING)
  @Column(name = "CATEGORY")
  private ResourceType cat = ResourceType.METHOD;

  @Column(name = "CODE")
  private String code;

  @Column(name = "NAME")
  private String name;

  @Column(name = "DESCRIPTION")
  private String description;

  public Privilege() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ResourceType getCat() {
    return cat;
  }

  public void setCat(ResourceType cat) {
    this.cat = cat;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getName()).append("{")
      .append("id=").append(id)
      .append(", cat=").append(cat)
      .append(", code=").append(code)
      .append(", name=").append(name).append("}");
    return sb.toString();
  }
}
