package com.free.misc;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.free.Application;

/**
 * 启动Web容器时, 将Web的Root Application Context绑定到Application中, 从而和测试环境保持一致!
 * 
 * @author Harry Sun harrysun2006@gmail.com
 * 
 */
public class ContextLoaderListener extends org.springframework.web.context.ContextLoaderListener {

  public void contextInitialized(ServletContextEvent event) {
    super.contextInitialized(event);
    Application.init();
    ServletContext context = event.getServletContext();
    if (context != null) {
      Application.CONTEXT_PATH = context.getContextPath();
      if (Application.UPLOAD_PATH.startsWith("."))
        Application.UPLOAD_PATH = context.getRealPath("/") + File.separatorChar + Application.UPLOAD_PATH;
      File f = new File(Application.UPLOAD_PATH);
      if (!f.exists())
        f.mkdirs();
      try {
        Application.UPLOAD_PATH = f.getCanonicalPath() + File.separatorChar;
      } catch (IOException e) {
        Application.UPLOAD_PATH = f.getAbsolutePath() + File.separatorChar;
      }

    }
  }

}
