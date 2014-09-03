package com.free.misc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.subject.WebSubjectContext;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class AutoWebSubjectContext extends DefaultWebSubjectContext {

  private static final long serialVersionUID = 7913165195132282310L;

  public AutoWebSubjectContext() {
  }

  public AutoWebSubjectContext(WebSubjectContext context) {
    super(context);
  }

  @Override
  public String resolveHost() {
    String host = super.resolveHost();
    return (host == null) ? "mock" : host;
  }

  public ServletRequest getServletRequest() {
    ServletRequest request = super.getServletRequest();
    if (request == null)
      request = new MockHttpServletRequest();
    return request;
  }

  public ServletResponse getServletResponse() {
    ServletResponse response = super.getServletResponse();
    if (response == null)
      response = new MockHttpServletResponse();
    return response;
  }

}
