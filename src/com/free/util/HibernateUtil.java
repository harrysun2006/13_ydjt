package com.free.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

public class HibernateUtil {

  private HibernateUtil() {
  }

  /**
   * 比较2个对象是否相等(比较对象中除Collection/Map/非序列化属性之外的所有属性)
   * 
   * @param o1
   * @param o2
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static boolean equals(Object o1, Object o2) {
    if ((o1 == null && o2 == null) || o1 == o2)
      return true;
    // Oracle数据库保存空字符串为null
    if ((o1 == null && "".equals(o2)) || (o2 == null && "".equals(o1)))
      return true;
    if (o1 == null || o2 == null)
      return false;
    if (o1.getClass() != o2.getClass())
      return false;
    if (o1.getClass().isArray() && o2.getClass().isArray()) {
      Object[] o1s = (Object[]) o1;
      Object[] o2s = (Object[]) o2;
      if (o1s.length != o2s.length)
        return false;
      for (int i = 0; i < o1s.length; i++) {
        if (equals(o1s[i], o2s[i]))
          continue;
        else
          return false;
      }
    } else {
      Class c = o1.getClass();
      if (Number.class.isAssignableFrom(c) || Comparable.class.isAssignableFrom(c))
        return o1.equals(o2);
      Field[] fds = c.getDeclaredFields();
      Object v1, v2;
      for (int i = 0; i < fds.length; i++) {
        try {
          fds[i].setAccessible(true);
          v1 = fds[i].get(o1);
          v2 = fds[i].get(o2);
          if (v1 == null && v2 == null)
            continue;
          // Oracle数据库保存空字符串为null
          if ((v1 == null && "".equals(v2)) || (v2 == null && "".equals(v1)))
            continue;
          if (v1 == null || v2 == null)
            return false;
          if (v1 instanceof Collection || v2 instanceof Map)
            continue;
          if (!(v1 instanceof Serializable) || !(v2 instanceof Serializable))
            continue;
          if (!Hibernate.isInitialized(v1) || v1 instanceof HibernateProxy || !Hibernate.isInitialized(v2)
              || v2 instanceof HibernateProxy)
            continue;
          if (!v1.equals(v2))
            return false;
        } catch (Exception e) {
          return false;
        }
      }
    }
    return true;
  }

}
