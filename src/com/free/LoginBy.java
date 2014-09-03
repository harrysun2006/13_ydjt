package com.free;

public enum LoginBy {
  cellphone,
  email,
  login,
  QQ,
  AUTO;

  public static String key() {
    return "LOGIN_BY";
  }
};
