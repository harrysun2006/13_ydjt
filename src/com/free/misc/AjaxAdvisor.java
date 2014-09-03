package com.free.misc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.web.portlet.ModelAndView;

import com.free.Application;
import com.free.util.JSONUtil;

@Aspect
public class AjaxAdvisor implements InitializingBean, Ordered {

  public void afterPropertiesSet() throws Exception {
  }

  @Override
  public int getOrder() {
    return 90;
  }

  /**
   * 抛错时r == null!! 无法获取方法的局部变量, 根据被拦截方法返回值类型返回约定的异常!
   * @param pjp
   * @return
   * @throws Throwable
   */
  @Around("execution(* com.free.ydjt.ajax.*.*(..))")
  public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    Object r = null;
    try {
      r = pjp.proceed();
    } catch (Throwable t) {
      r = getException(pjp, t);
    }
    return r;
  }

  // @AfterThrowing(pointcut = "execution(* com.free.zf.ajax.*.*(..))", throwing = "ex")
  public void doThrowing(JoinPoint jp, Throwable ex) {
    System.out.println("method " + jp.getTarget().getClass().getName() + "." + jp.getSignature().getName()
        + " throw exception");
    System.out.println(ex.getMessage());
  }

  protected Object getException(Throwable t) {
    Object e;
    if (t instanceof NullPointerException) e = "空指针错误";
    else e = t.getLocalizedMessage();
    return e;
  }

  protected Object getException(ProceedingJoinPoint pjp, Throwable t) {
    Signature sig = pjp.getSignature();
    if (!(sig instanceof MethodSignature)) return null;
    MethodSignature msig = (MethodSignature) sig;
    Method m = msig.getMethod();
    Class<?> c = m.getReturnType();
    Object r;
    Object e = getException(t);
    if (c.isAssignableFrom(Map.class)) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("ERROR", e);
      r = map;
    } else if (c.isAssignableFrom(ModelAndView.class)) {
      ModelAndView mav = new ModelAndView("error");
      String title = Application.getString("app.title", Application.get("app.title"));
      mav.addObject("APP_TITLE", title);
      mav.addObject("APP_VER", Application.VERSION);
      mav.addObject("APP_CONTEXT_PATH", Application.CONTEXT_PATH);
      mav.addObject("APP_PAGE", "error");
      mav.addObject("APP_ERROR", JSONUtil.getSafeJSON(e));
      r = mav;
    } else if (c.isAssignableFrom(String.class)) {
      r = String.valueOf(e);
    } else {
      r = t;
    }
    return r;
  }

}