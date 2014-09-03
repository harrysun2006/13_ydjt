package com.free.ydjt.ajax;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.free.Application;
import com.free.Patcher;
import com.free.Setting;
import com.free.util.JSONUtil;
import com.free.ydjt.dto.User;
import com.free.ydjt.service.UserService;

@Controller
// implements ServletContextAware有问题, 导致controller无法正常工作???
public class PageController {

  protected ModelAndView mav(String name, Map<String, Object> data) {
    ModelAndView mav = new ModelAndView(name);
    String title = Application.getString("app.title", Application.get("app.title"));
    User u = UserService.getLoginedAccount();
    mav.addObject("APP_TITLE", title);
    mav.addObject("APP_VER", Application.VERSION);
    mav.addObject("APP_CONTEXT_PATH", Application.CONTEXT_PATH);
    mav.addObject("APP_PAGE", name);
    mav.addObject("APP_DATA", JSONUtil.getSafeJSON(data));
    // TODO: 读取合并app, site, user level profile
    mav.addObject("APP_SETTING", JSONUtil.getSafeJSON(Setting.SETTINGS));
    mav.addObject("APP_ACC", u == null ? false : JSONUtil.getSafeJSON(u));
    return mav;
  }

  protected ModelAndView mav(String name) {
    return mav(name, null);
  }

  /**
   * 检查系统是否需要设置, 是则进入setup页面; 否则进入index页面
   * @return
   */
  @RequestMapping(value = {"", "/index"}, method = RequestMethod.GET)
  public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ModelAndView mav;
    if (Patcher.STATUS == Patcher.Status.STARTER) {
      mav = mav("setup");
    } else {
      mav = mav("index");
    }
    if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
      Writer w = response.getWriter();
      response.addHeader("ERROR-CODE", "not.login");
      w.write("not.login"); // 必须输出非json格式的内容, 否则客户端$.ajax将回调success
      w.flush();
      w.close();
    }
    return mav;
  }

  @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
  public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService.logout();
    ModelAndView mav = index(request, response);
    return mav;
  }

  @RequestMapping(value = "/error/code", method = RequestMethod.GET)
  protected ModelAndView error(@PathVariable("code") String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
    Map<String, Object> data = new HashMap<String, Object>();
    ModelAndView mav = mav("error", data);
    mav.addObject("ERROR", JSONUtil.getSafeJSON(request.getAttribute("error")));
    if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
      Writer w = response.getWriter();
      response.addHeader("ERROR-CODE", code);
      w.write(String.format("error.%s", code)); // 必须输出非json格式的内容, 否则客户端$.ajax将回调success
      w.flush();
      w.close();
    }
    return mav;
  }

  @RequestMapping(value = "/test", method = RequestMethod.GET)
  public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("memo", "Harry Sun");
    data.put("me'mo", "Harry Sun\"s Me'mo!");
    ModelAndView mav = mav("test", data);
    // 之后还会有RequestDispatcher,
    // 此处的request和response和页面中的${pageContext.request}/${pageContext.response}不一样!!!
    mav.addObject("userAgent", request.getHeader("userAgent"));
    mav.addObject("contentType", response.getContentType());
    return mav;
  }

}
