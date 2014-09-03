package com.free.ydjt.dto;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FND_LOGS")
public class Log extends AnnotatedEntity implements Serializable {

  private static final long serialVersionUID = 1654342236505528510L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KG_LOG")
  @SequenceGenerator(name = "KG_LOG", sequenceName = "SEQ_LOG", allocationSize = 1)
  @Column(name = "LOG_ID")
  private Integer id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "DATELINE")
  private Calendar dateline;

  @Column(name = "CLASS")
  private String clazz;

  @Column(name = "METHOD")
  private String method;

  @Column(name = "LINE")
  private String line;

  @Enumerated(EnumType.STRING)
  @Column(name = "LEVEL")
  private Level level = Level.INFO;

  @Column(name = "SITE_ID")
  private String siteId;

  @Column(name = "USER_ID", insertable = true, updatable = true)
  private String userId;

  @Column(name = "CONTENT")
  private String content;

  @Lob
  @Column(name = "CONTEXT")
  private String context;

  @Lob
  @Column(name = "STACK")
  private String stack;

  public enum Level { // 日志级别
    NONE, OFF, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, ALL
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Calendar getDateline() {
    return dateline;
  }

  public void setDateline(Calendar dateline) {
    this.dateline = dateline;
  }

  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }

  public String getSiteId() {
    return siteId;
  }

  public void setSiteId(String siteId) {
    this.siteId = siteId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getStack() {
    return stack;
  }

  public void setStack(String stack) {
    this.stack = stack;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getName()).append("{")
      .append("id=").append(id)
      .append(", clazz=").append(clazz)
      .append(", method=").append(method)
      .append(", line=").append(line).append("}");
    return sb.toString();
  }

}
