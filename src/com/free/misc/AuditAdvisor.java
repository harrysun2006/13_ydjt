package com.free.misc;

import java.lang.reflect.Method;
import java.util.Calendar;

import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;

import com.free.ydjt.dto.User;
import com.free.ydjt.service.UserService;

@Aspect
public class AuditAdvisor implements InitializingBean, Ordered {

  public void afterPropertiesSet() throws Exception {
  }

  @Override
  public int getOrder() {
    return 30;
  }

  /**
   * @param pjp
   * @return
   * @throws Throwable
   */
  @Around("execution(* com.free.ydjt.service.*.add*(..)) " +
      "|| execution(* com.free.ydjt.service.*.save*(..)) ")
  public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    MethodSignature ms = (MethodSignature) pjp.getSignature();
    Method method = ms.getMethod();
    String name = method.getName();
    Object[] args = pjp.getArgs();
    if (name.startsWith("add")) processArgs4Add(args);
    else processArgs4Save(args);
    return pjp.proceed();
  }

  protected void processArgs4Add(Object[] args) {
    for (Object o : args) process4Add(o);
  }

  protected void process4Add(Object obj) {
    if (obj == null) return;
    Calendar cal = Calendar.getInstance();
    User lu = UserService.getLoginedAccount();
    setField(obj, "createdBy", lu.getId());
    setField(obj, "creationDate", cal);
    setField(obj, "lastUpdatedBy", lu.getId());
    setField(obj, "lastUpdateDate", cal);
  }

  protected void processArgs4Save(Object[] args) {
    for (Object o : args) process4Save(o);
  }

  protected void process4Save(Object obj) {
    if (obj == null) return;
    Calendar cal = Calendar.getInstance();
    User lu = UserService.getLoginedAccount();
    setField(obj, "lastUpdatedBy", lu.getId());
    setField(obj, "lastUpdateDate", cal);
  }

  protected void setField(Object obj, String fname, Object value) {
    setField(obj, fname, value, false);
  }

  protected void setField(Object obj, String name, Object value, boolean override) {
    try {
      Object v = PropertyUtils.getProperty(obj, name);
      if (v == null || override) PropertyUtils.setProperty(obj, name, value);
    } catch (Exception e) {
    }
  }

}