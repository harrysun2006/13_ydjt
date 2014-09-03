package com.free.misc;

import java.util.Collection;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.apache.shiro.web.subject.WebSubjectContext;

/**
 * 跟踪源代码并参考http://shiro.apache.org/web.html, 实现自动Mock Web容器, 便于测试! 1. web环境中,
 * 通过IniShiroFilter创建Factory, 有一些缺省的filters (anon, authc, ...). 2.
 * Mocker中创建WebIniSecurityManagerFactory类 , 也会有这些filters. 3.
 * 使用本类和AutoWebSubjectContext类, 自动Mock HttpServletRequest和HttpServletResponse,
 * 这样: 在shiro.ini的[main]中配置authc.loginUrl在无web容器时运行就不会报错了, 方便测试! 4.
 * 在shiro.ini的[urls]中进行配置, 即使在无web容器时运行也不会报错!!!
 * 
 * @author Harry Sun harrysun2006@gmail.com
 * 
 */
public class AutoSecurityManager extends DefaultWebSecurityManager {

  public AutoSecurityManager() {
    super();
    setSubjectFactory(new DefaultWebSubjectFactory());
    setRememberMeManager(new CookieRememberMeManager());
    setSessionManager(new ServletContainerSessionManager());
  }

  public AutoSecurityManager(Realm singleRealm) {
    this();
    setRealm(singleRealm);
  }

  public AutoSecurityManager(Collection<Realm> realms) {
    this();
    setRealms(realms);
  }

  @Override
  protected SubjectContext createSubjectContext() {
    return new AutoWebSubjectContext();
  }

  @Override
  protected SubjectContext copy(SubjectContext sc) {
    if (sc instanceof WebSubjectContext) {
      return new AutoWebSubjectContext((WebSubjectContext) sc);
    }
    return super.copy(sc);
  }

}
