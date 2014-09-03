package com.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/test/ajax2")
@SessionAttributes("users")
public class Ajax2Controller {

  /**
   * @Autowired private UserService us;
   * @RequestMapping(value = "/list", method = RequestMethod.GET)
   * @ModelAttribute("users")
   * @ResponseBody public Map<String, Object> getUsers() {
   * 
   *               List<User> list = new ArrayList<User>(); for (User u :
   *               User.XIYOUJI) list.add(u); Map<String, Object> modelMap = new
   *               HashMap<String, Object>(); modelMap.put("total",
   *               list.size()); modelMap.put("data", list);
   *               modelMap.put("success", "true"); return modelMap; }
   * @SuppressWarnings("unchecked")
   * @RequestMapping(value = "/add", method = RequestMethod.POST)
   * @ResponseBody // 二种方式: A HttpEntity<User> B使用注解 @ResponseBody public
   *               Map<String, Object> addUser(HttpEntity<User> model,
   *               HttpServletRequest request) { System.out.println("add user: "
   *               + model.getBody());
   * 
   *               Map<String, Object> map = (Map<String, Object>)
   *               request.getSession().getAttribute("users"); if (null == map)
   *               { map = getUsers(); } List<User> list = (List<User>)
   *               map.get("data"); User u = model.getBody(); list.add(u);
   *               map.put("total", list.size()); map.put("data", list);
   *               request.getSession().setAttribute("users", map); us.add(u);
   *               if (u.getAge() >= 100) throw new
   *               RuntimeException("exception@controller, rollback!"); return
   *               map; }
   **/

  @RequestMapping(value = { "", "/" })
  public String index() {
    return "test-ajax2";
  }
}
