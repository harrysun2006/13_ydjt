package com.test;

import java.io.File;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.deployer.ContextDeployer;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * 启动Jetty应用服务器 -- 测试用
 * 
 * @author Harry Sun <harrysun2006@gmail.com>
 */
public class CommonJetty {

  public static void main(String[] args) throws Exception {
    int port = Integer.parseInt(args.length > 0 ? args[0] : "80");
    String path = args.length > 1 ? args[1] : "www";
    String context = args.length > 2 ? args[2] : "/";

    File fapp = new File(path);
    final Connector conn = new SelectChannelConnector();
    conn.setPort(port);
    conn.setMaxIdleTime(60000);
    conn.setHeaderBufferSize(20480); // 缺省4K, Chrome下可能会报错: 413 FULL head

    Server server = new Server();
    server.addConnector(conn);
    WebAppContext wapp = new WebAppContext(server, fapp.getCanonicalPath(), context);

    HandlerCollection hc = new HandlerCollection();
    ContextHandlerCollection chc = new ContextHandlerCollection();
    hc.setHandlers(new Handler[] { chc, new DefaultHandler() });

    ContextDeployer cd = new ContextDeployer();
    cd.setContexts(chc);
    cd.setConfigurationDir(path);
    cd.setDirectory(path);
    cd.setScanInterval(5);

    wapp.setClassLoader(ClassLoader.getSystemClassLoader());
    wapp.setParentLoaderPriority(false);
    wapp.setExtractWAR(false);

    // server.addLifeCycle(wapp);
    server.addHandler(hc);
    server.start();
    server.join();
    wapp.start();
    server.addLifeCycle(cd);
  }

}
