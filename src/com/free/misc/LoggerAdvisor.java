package com.free.misc;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;

@Aspect
public class LoggerAdvisor implements InitializingBean, Ordered {

  private static final Logger logger = LoggerFactory.getLogger(LoggerAdvisor.class);

  public void afterPropertiesSet() throws Exception {
  }

  @Override
  public int getOrder() {
    return 60;
  }

  @AfterThrowing(pointcut = "execution(* com.free.ydjt.service.*.*(..)) " +
  		"|| execution(* com.free.ydjt.ajax.*.*(..))", throwing = "ex")
  public void doThrowing(JoinPoint jp, Throwable ex) {
    // 使用AOP后, 在log4j appender中的LoggingEvent无法得到实际的出错位置信息!
    logger.error(ex.getLocalizedMessage(), ex);
  }

  public Object invoke(MethodInvocation invocation) throws Throwable {
    return invocation.proceed();
  }

}
