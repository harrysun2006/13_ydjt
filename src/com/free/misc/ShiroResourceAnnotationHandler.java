package com.free.misc;

import java.lang.annotation.Annotation;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;

import com.free.annotation.Resource;

public class ShiroResourceAnnotationHandler extends AuthorizingAnnotationHandler {

  /**
   * Default no-argument constructor that ensures this handler looks for
   * {@link com.free.annotation.RequiresRoles RequiresRoles} annotations.
   */
  public ShiroResourceAnnotationHandler() {
    super(Resource.class);
  }

  /**
   * Ensures that the calling <code>Subject</code> has the Annotation's
   * specified roles, and if not, throws an <code>AuthorizingException</code>
   * indicating that access is denied.
   * 
   * @param a
   *          the RequiresRoles annotation to use to check for one or more roles
   * @throws org.apache.shiro.authz.AuthorizationException
   *           if the calling <code>Subject</code> does not have the role(s)
   *           necessary to proceed.
   */
  public void assertAuthorized(Annotation a) throws AuthorizationException {
    if (!(a instanceof Resource))
      return;

    Resource ra = (Resource) a;
    String id = ra.id();
    ra.annotationType();
    // getSubject().checkPermission(id);
  }

}
