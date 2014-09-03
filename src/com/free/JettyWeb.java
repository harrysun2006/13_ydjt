package com.free;

import java.io.File;

import org.mortbay.component.LifeCycle;
import org.mortbay.component.LifeCycle.Listener;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.deployer.ContextDeployer;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 启动Jetty应用服务器 -- 测试用
 * 
 * @author Harry Sun <harrysun2006@gmail.com>
 */
public class JettyWeb {

  private static final Logger logger = LoggerFactory.getLogger(JettyWeb.class);
  private static final String APP_PATH = "www";
  private static final String CONTEXT_PATH = "/";

  public static void main(String[] args) throws Exception {
    String name = args.length > 0 ? args[0] : "app";
    Mocker mocker = Mocker.init(name);
    String webXml = mocker.getWebXml();
    int port = mocker.getPort();

    File fapp = new File(APP_PATH);
    final Connector conn = new SelectChannelConnector();
    conn.setPort(port);
    conn.setMaxIdleTime(60000);
    conn.setHeaderBufferSize(20480); // 缺省4K, Chrome下可能会报错: 413 FULL head

    Listener listerner = new Listener() {

      @Override
      public void lifeCycleFailure(LifeCycle arg0, Throwable arg1) {
        logger.info("YDJT SYSTEM {} FAILED!!!", Application.VERSION);
      }

      @Override
      public void lifeCycleStarted(LifeCycle arg0) {
        logger.info("YDJT SYSTEM {} STARTED!", Application.VERSION);
      }

      @Override
      public void lifeCycleStarting(LifeCycle arg0) {
        logger.info("YDJT SYSTEM {} STARTING...", Application.VERSION);
      }

      @Override
      public void lifeCycleStopped(LifeCycle arg0) {
        logger.info("YDJT SYSTEM {} STOPPED!", Application.VERSION);
      }

      @Override
      public void lifeCycleStopping(LifeCycle arg0) {
        logger.info("YDJT SYSTEM {} STOPPING...", Application.VERSION);
      }
    };
    Server server = new Server();
    server.addLifeCycleListener(listerner);
    server.addConnector(conn);
    WebAppContext wapp = new WebAppContext(server, fapp.getCanonicalPath(), CONTEXT_PATH);

    HandlerCollection hc = new HandlerCollection();
    ContextHandlerCollection chc = new ContextHandlerCollection();
    hc.setHandlers(new Handler[] { chc, new DefaultHandler() });

    ContextDeployer cd = new ContextDeployer();
    cd.setContexts(chc);
    cd.setConfigurationDir(APP_PATH);
    cd.setDirectory(APP_PATH);
    cd.setScanInterval(5);

    wapp.setClassLoader(ClassLoader.getSystemClassLoader());
    wapp.setParentLoaderPriority(false);
    wapp.setExtractWAR(false);
    // setDescriptor() -- 使用其他文件作为web.xml
    // wapp.setDefaultsDescriptor(WEB_XML);
    // setWelcomeFiles()和web.xml中的welcome-file-list都没有用,
    // 因为/已定义在test-mvc的servlet-mapping中??
    wapp.setDescriptor(APP_PATH + "/WEB-INF/" + webXml);

    // server.addLifeCycle(wapp);
    server.addHandler(hc);
    server.start();
    server.join();
    wapp.start();
    server.addLifeCycle(cd);
  }

}
