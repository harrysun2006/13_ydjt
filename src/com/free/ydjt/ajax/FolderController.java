package com.free.ydjt.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/folder")
public class FolderController {

  @RequestMapping(value = "/tree", method = { RequestMethod.GET, RequestMethod.POST })
  public @ResponseBody Map<String, Object> tree(@RequestBody Map<String, Object> data) {
    Map<String, Object> r = new HashMap<String, Object>();
    List<Map<String, Object>> t0 = new ArrayList<Map<String, Object>>();
    Map<String, Object> n0 = new HashMap<String, Object>();
    n0.put("id", 1);
    n0.put("text", "AAA");
    t0.add(n0);
    List<Map<String, Object>> t1 = new ArrayList<Map<String, Object>>();
    Map<String, Object> n1 = new HashMap<String, Object>();
    n1.put("id", 2);
    n1.put("text", "BBB");
    t1.add(n1);
    n0.put("children", t1);
    r.put("tree", t0);
    return r;
  }
}
