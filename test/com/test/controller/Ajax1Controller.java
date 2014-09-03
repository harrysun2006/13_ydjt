package com.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test/ajax1")
public class Ajax1Controller {

  /**
   * 根据映射跳转到指定的页面
   */
  @RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
  public String index() {
    // 解析 /WEB-INF/page/test-ajax1.html
    return "test-ajax1";
  }

  /**
   * 提交表单并进行运算.
   */
  @RequestMapping(value = "/add", method = { RequestMethod.GET, RequestMethod.POST })
  @ResponseBody
  public Integer add(@RequestParam(value = "x", required = true) Integer x,
      @RequestParam(value = "y", required = true) Integer y) {
    // 实现运算
    Integer sum = x + y;
    System.out.println("sum: " + sum);
    // @ResponseBody 会自动的将返回值转换成JSON格式
    // 但是你必须添加jackson的jar包!!!
    return sum;
  }

  /**
   * @RequestMapping(value = "/get-user/{userId}", method = {RequestMethod.GET,
   *                       RequestMethod.POST})
   * @ResponseBody public User getUser(@PathVariable("userId") Integer userId) {
   *               Map<Integer, User> users = new HashMap<Integer, User>(); for
   *               (User u : User.XIYOUJI) users.put(u.getId(), u); User r =
   *               users.get(userId); System.out.println("get-user: " + r);
   *               return r; }
   * @RequestMapping(value = "/user-list", method = {RequestMethod.GET,
   *                       RequestMethod.POST})
   * @ResponseBody public List<User> getUserList() { List<User> users = new
   *               ArrayList<User>(); for (User u : User.XIYOUJI) users.add(u);
   *               return users; }
   * @RequestMapping(value = "/user-map", method = {RequestMethod.GET,
   *                       RequestMethod.POST})
   * @ResponseBody public Map<Integer, User> getUserMap() { Map<Integer, User>
   *               users = new HashMap<Integer, User>(); for (User u :
   *               User.XIYOUJI) users.put(u.getId(), u); return users; }
   **/

}
