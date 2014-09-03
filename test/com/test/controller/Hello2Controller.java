package com.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Hello2Controller {

  @RequestMapping(value = "/test/hello2", method = { RequestMethod.GET, RequestMethod.POST })
  public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
    String name = request.getParameter("name");
    if (name == null)
      name = "Spring MVC 3.0";
    String message = "Hello World, " + name + "!";
    ModelAndView view = new ModelAndView("hello2", "hello_msg", message);
    return view;
  }

  @RequestMapping("/test/hello3")
  public ModelAndView check(@RequestParam String username, @RequestParam String password, HttpSession session) {
    String message = String.format("Hello World, Spring 3.0!\nusername=%s, password=%s", username, password);
    ModelAndView view = new ModelAndView("hello2", "message", message);
    return view;
  }
}