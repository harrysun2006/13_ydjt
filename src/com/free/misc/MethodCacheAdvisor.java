package com.free.misc;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.free.annotation.ClassCache;
import com.free.annotation.ClearCache;
import com.free.annotation.MethodCache;
import com.free.annotation.MethodNoCache;
import com.free.util.CacheUtil;

@Aspect
@Component("methodCacheAdvisor")
public class MethodCacheAdvisor implements MethodInterceptor, InitializingBean, Ordered {

  private static final Logger logger = LoggerFactory.getLogger(MethodCacheAdvisor.class);

  public static final String CACHENAME_SPLIT_CHAR = "#";

  private Cache cache = null;

  // 使用Spring注入的cache, 则ehcache的CacheManager取不到同一个cache实例
  public void setCache(Cache cache) {
    // this.cache = cache;
    if (this.cache != null)
      return;
    CacheManager cm = CacheManager.getInstance();
    this.cache = cm.getCache("DEFAULT_CACHE");
    logger.info("Using cache {}!", cache.getName());
  }

  public void afterPropertiesSet() throws Exception {
    Assert.notNull(cache, "No cache set!");
  }

  @Override
  public int getOrder() {
    return 20;
  }

  // @Before("execution(* com.free.zf.service.*.find*(..))")
  public void doBefore(JoinPoint jp) {
    System.out.println(String.format("doBefore@%s: class-%s, method-%s", MethodCacheAdvisor.class.getName(), jp
        .getTarget().getClass().getName(), jp.getSignature().getName()));
  }

  // check cache -- return/query -- return&cache
  @Around("execution(* com.free.ydjt.ajax.*.*(..)) " +
  		"|| execution(* com.free.ydjt.service.*.get*(..)) " +
  		"|| execution(* com.free.ydjt.service.*.list*(..)) " +
  		"|| execution(* com.free.ydjt.service.*.load*(..)) " +
  		"|| execution(* com.free.ydjt.service.*.query*(..))")
  public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    Class<?> clazz = pjp.getTarget().getClass();
    MethodSignature ms = (MethodSignature) pjp.getSignature();
    Method method = ms.getMethod();
    Object[] args = pjp.getArgs();
    MethodCaller mc = new MethodCaller1(pjp);
    return getResult(clazz, method, args, mc);
  }

  // clear cache after insert/update/delete
  @After("execution(* com.free.ydjt.service.*.add*(..)) " +
  		"or execution(* com.free.ydjt.service.*.delete*(..)) " +
  		"or execution(* com.free.ydjt.service.*.save*(..)) " +
  		"or execution(* com.free.ydjt.service.*.update*(..)) " +
  		"or execution(* com.free.ydjt.service.*.set*(..))")
  public void doAfter(JoinPoint jp) {
    Class<?> clazz = jp.getTarget().getClass();
    MethodSignature ms = (MethodSignature) jp.getSignature();
    Method method = ms.getMethod();
    Object[] args = jp.getArgs();
    clearResult(clazz, method, args);
  }

  // @AfterThrowing(pointcut = "execution(* com.test.service.*.*(..))", throwing = "ex")
  public void doThrowing(JoinPoint jp, Throwable ex) {
    System.out.println(String.format("doThrowing@%s: class-%s, method-%s, exception-%s",
        MethodCacheAdvisor.class.getName(), jp.getTarget().getClass().getName(), jp.getSignature().getName(),
        ex.getMessage()));
  }

  // out-dated method, using method interceptor
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Class<?> clazz = invocation.getThis().getClass();
    Method method = invocation.getMethod();
    Object[] args = invocation.getArguments();
    MethodCaller mc = new MethodCaller2(invocation);
    return getResult(clazz, method, args, mc);
  }

  // get cached result, if none then put the result into cache
  private Object getResult(Class<?> clazz, Method method, Object[] args, MethodCaller caller) throws Throwable {
    boolean flag1 = clazz.isAnnotationPresent(ClassCache.class);
    boolean flag2 = method.isAnnotationPresent(MethodCache.class);
    boolean flag3 = method.isAnnotationPresent(MethodNoCache.class);
    if ((!flag1 && !flag2) || flag3)
      return caller.invoke();
    String className = clazz.getName(); // clazz.getInterfaces()[0].getName();
    String methodName = method.getName();
    if (clazz.getCanonicalName().contains("$Proxy")) {
      logger.warn("The object has been proxyed and the advisor can't get the underlying target, not cached for {}@{}",
          methodName, className);
      return caller.invoke();
    }
    ClassCache cc;
    MethodCache mc;
    String name = null;
    String cacheKey = null;
    int expire = 0;
    if (flag1) {
      cc = clazz.getAnnotation(ClassCache.class);
      expire = cc.expire();
    }
    if (flag2) {
      mc = method.getAnnotation(MethodCache.class);
      name = mc.name();
      expire = mc.expire();
    }
    if ("".equals(name) || name == null) {
      cacheKey = getCacheKey(className, methodName, args);
    } else {
      cacheKey = getCacheKey(null, name, args);
    }
    Element element = cache.get(cacheKey);
    Object result;
    if (element == null) { // 此处使用synchronized + double check,可以略微提高效率
      synchronized (cacheKey) {
        element = cache.get(cacheKey);
        if (element == null) {
          result = caller.invoke();
          element = new Element(cacheKey, (Serializable) result);
          // annotation没有设expire值则使用ehcache.xml中自定义值
          if (expire > 0) {
            element.setTimeToIdle(expire);
            element.setTimeToLive(expire);
          }
          cache.put(element);
          logger.debug("put new value into cache[{}]", cacheKey);
        } else {
          result = element.getValue();
          logger.debug("get value from cache[{}]", cacheKey);
        }
      }
    } else {
      result = element.getValue();
      logger.debug("get value from cache[{}]", cacheKey);
    }
    return result;
  }

  // clear cached result
  private void clearResult(Class<?> clazz, Method method, Object[] args) {
    boolean flag1 = method.isAnnotationPresent(ClearCache.class);
    if (!flag1)
      return;
    ClearCache cc = method.getAnnotation(ClearCache.class);
    String[] name = cc.name();
    CacheUtil.clear(cache, name);
  }

  private String getCacheKey(String className, String methodName, Object[] args) {
    StringBuilder sb = new StringBuilder();
    if (className != null)
      sb.append(className).append(CACHENAME_SPLIT_CHAR);
    sb.append(methodName);
    if ((args != null) && (args.length > 0)) {
      for (int i = 0; i < args.length; i++) {
        if (args[i] instanceof Date) {
          Date d = (Date) args[i];
          sb.append(CACHENAME_SPLIT_CHAR).append(d.getTime());
        } else {
          sb.append(CACHENAME_SPLIT_CHAR).append(args[i]);
        }
      }
    }
    return sb.toString();
  }

  interface MethodCaller {
    public Object invoke() throws Throwable;
  }

  static final class MethodCaller1 implements MethodCaller {

    private ProceedingJoinPoint pjp;

    MethodCaller1(ProceedingJoinPoint pjp) {
      this.pjp = pjp;
    }

    public Object invoke() throws Throwable {
      return pjp.proceed();
    }
  }

  static final class MethodCaller2 implements MethodCaller {
    private MethodInvocation mi;

    MethodCaller2(MethodInvocation mi) {
      this.mi = mi;
    }

    public Object invoke() throws Throwable {
      return mi.proceed();
    }
  }

}
