package com.free.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyObject;

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.Hibernate;
import org.hibernate.proxy.AbstractLazyInitializer;
import org.hibernate.proxy.HibernateProxy;

public class JpaUtil {

  private JpaUtil() {
  }

  public static String getSequnceName(Class<?> clazz) {
    Field[] fs = clazz.getDeclaredFields();
    for (Field f : fs) {
      if (f.isAnnotationPresent(Id.class) && f.isAnnotationPresent(SequenceGenerator.class))
        return f.getAnnotation(SequenceGenerator.class).sequenceName();
    }
    throw new RuntimeException("Sequence of " + clazz.getName() + " NOT found!");
  }

  public static String getKeyName(Object obj) {
    return obj == null ? null : getKeyName(obj.getClass());
  }

  public static String getKeyName(Class<Object> clazz) {
    Field[] fs = clazz.getDeclaredFields();
    for (Field f : fs) {
      if (f.isAnnotationPresent(Id.class))
        return f.getName();
    }
    throw new RuntimeException("Primary key of " + clazz.getName() + " NOT found!");
  }

  public static Object getKeyValue(Object obj) {
    if (obj instanceof ProxyObject) {
      ProxyObject proxy = (ProxyObject) obj;
      MethodHandler handler = proxy.getHandler();
      if (handler instanceof AbstractLazyInitializer)
        return ((AbstractLazyInitializer) handler).getIdentifier();
    }
    Class<?> clazz = getUnderlyingClass(obj);
    Field[] fs = clazz.getDeclaredFields();
    try {
      for (Field f : fs) {
        if (f.isAnnotationPresent(Id.class)) {
          f.setAccessible(true);
          return f.get(obj);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Can NOT get key value of " + clazz.getName() + "!");
    }
    throw new RuntimeException("Primary key of " + clazz.getName() + " NOT found!");
  }

  public static void setKeyValue(Object obj, Object value) {
    Class<?> clazz = getUnderlyingClass(obj);
    Field[] fs = clazz.getDeclaredFields();
    try {
      for (Field f : fs) {
        if (f.isAnnotationPresent(Id.class)) {
          f.setAccessible(true);
          f.set(obj, value);
          return;
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Can NOT set key value of " + clazz.getName() + "!");
    }
    throw new RuntimeException("Primary key of " + clazz.getName() + " NOT found!");
  }

  protected static Class<?> getUnderlyingClass(Object entity) {
    Class<?> clazz = entity.getClass();
    if (entity instanceof ProxyObject || entity instanceof HibernateProxy)
      return clazz.getSuperclass();
    else
      return clazz;
  }

  protected static Object clone(Object entity) {
    Class<?> clazz = getUnderlyingClass(entity);
    Object n = null;
    try {
      n = clazz.newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Can NOT create new instance of " + clazz.getName() + "!");
    }
    if (n != null)
      setKeyValue(n, getKeyValue(entity));
    return n;
  }

  /**
   * Un-Proxy through hibernate interface
   * 
   * @param entity
   * @return
   */
  protected static Object unproxy1(Object entity) {
    if (Hibernate.isInitialized(entity)) {
      if (entity instanceof HibernateProxy)
        return ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
      else
        return entity;
    } else {
      if (entity == null)
        return null;
      else
        return clone(entity);
    }
  }

  /**
   * Un-Proxy through javassist interface
   * 
   * @param entity
   * @return
   */
  protected static Object unproxy2(Object entity) {
    if (entity instanceof ProxyObject) {
      ProxyObject proxy = (ProxyObject) entity;
      proxy.setHandler(new QuietInvoker(proxy.getHandler()));
    }
    return entity;
  }

  public static Object unproxy(Object entity) {
    return unproxy1(entity);
  }

  protected static final class QuietInvoker implements MethodHandler {

    protected MethodHandler handler;

    protected QuietInvoker(MethodHandler handler) {
      this.handler = handler;
    }

    public Object invoke(Object entity, Method orig, Method proxy, Object[] args) throws Throwable {
      Object r = null;
      try {
        // System.out.println(orig.getName() + " : " + proxy.getName());
        r = handler.invoke(entity, orig, proxy, args);
      } catch (Throwable t) {
      }
      return r;
    }

  }
}
