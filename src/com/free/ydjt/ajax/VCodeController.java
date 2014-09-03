package com.free.ydjt.ajax;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.Captcha;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.free.Constants;
import com.free.util.CaptchaUtil;

@Controller
public class VCodeController {

  @RequestMapping(value = "/vcode", method = RequestMethod.GET)
  public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Captcha captcha = CaptchaUtil.get();
    Subject cu = SecurityUtils.getSubject();
    cu.getSession().setAttribute(Constants.CAPTCHA_SESSION_KEY, captcha.getAnswer());
    BufferedImage bi = captcha.getImage();
    response.setDateHeader("Expires", 0);
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    response.addHeader("Cache-Control", "post-check=0, pre-check=0");
    response.setHeader("Pragma", "no-cache");
    response.setContentType("image/jpeg");
    ServletOutputStream out = response.getOutputStream();
    ImageIO.write(bi, "jpg", out);
    try {
      out.flush();
    } finally {
      if (out != null)
        out.close();
    }
    return null;
  }

}
