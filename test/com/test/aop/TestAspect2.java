package com.test.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;

@Aspect
public class TestAspect2 {

  @After("execution(* com.test.service.*.*(..))")
  public void doAfter(JoinPoint jp) {
    System.out.println("log Ending method: " + jp.getTarget().getClass().getName() + "." + jp.getSignature().getName());
  }

  @Around("execution(* com.test.service.*.*(..))")
  public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    long time = System.currentTimeMillis();
    Object retVal = pjp.proceed();
    time = System.currentTimeMillis() - time;
    System.out.println("process time: " + time + " ms");
    return retVal;
  }

  @Before("execution(* com.test.service.*.*(..))")
  public void doBefore(JoinPoint jp) {
    System.out.println("log Begining method: " + jp.getTarget().getClass().getName() + "."
        + jp.getSignature().getName());
  }

  @AfterThrowing(pointcut = "execution(* com.test.service.*.*(..))", throwing = "ex")
  public void doThrowing(JoinPoint jp, Throwable ex) {
    System.out.println("method " + jp.getTarget().getClass().getName() + "." + jp.getSignature().getName()
        + " throw exception");
    System.out.println(ex.getMessage());
  }

}
