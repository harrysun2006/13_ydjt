package com.test.aop;

import com.test.service.AService;
import com.test.service.BServiceImpl;

public class AOPTest {

  private AService aService;
  private BServiceImpl bService;

  /**
   * 测试正常调用
   */
  public void testCall() {
    System.out.println("SpringTest JUnit test");
    aService.fooA("JUnit test fooA");
    aService.barA();
    bService.fooB();
    bService.barB("JUnit test barB", 0);
  }

  /**
   * 测试After-Throwing
   */
  public void testThrow() {
    try {
      bService.barB("JUnit call barB", 1);
    } catch (IllegalArgumentException e) {
    }
  }

  public void setAService(AService service) {
    aService = service;
  }

  public void setBService(BServiceImpl service) {
    bService = service;
  }

}