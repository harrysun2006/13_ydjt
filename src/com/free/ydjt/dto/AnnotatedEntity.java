package com.free.ydjt.dto;

import com.free.util.JpaUtil;

public class AnnotatedEntity {

  @Override
  public int hashCode() {
    Object key = JpaUtil.getKeyValue(this);
    return key == null ? 0 : key.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final AnnotatedEntity other = (AnnotatedEntity) obj;
    Object key = JpaUtil.getKeyValue(this);
    Object okey = JpaUtil.getKeyValue(other);
    if (key == null) {
      if (okey != null) return false;
    } else {
      if (!key.equals(okey)) return false;
    }
    return true;
  }

  public static Object unproxy(Object entity) {
    return JpaUtil.unproxy(entity);
  }

}
