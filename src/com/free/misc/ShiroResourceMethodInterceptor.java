package com.free.misc;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;

public class ShiroResourceMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {

  /**
   * Default no-argument constructor that ensures this interceptor looks for
   * {@link RequiresRoles RequiresRoles} annotations in a method declaration.
   */
  public ShiroResourceMethodInterceptor() {
    super(new ShiroResourceAnnotationHandler());
  }

  /**
   * @param resolver
   * @since 1.1
   */
  public ShiroResourceMethodInterceptor(AnnotationResolver resolver) {
    super(new ShiroResourceAnnotationHandler(), resolver);
  }
}