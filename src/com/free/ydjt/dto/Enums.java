package com.free.ydjt.dto;

public interface Enums {

  public enum YesNo {
    Y, // 是
    N; // 否

    public static YesNo valueOf(Boolean b) {
      return b == null || !b.booleanValue() ? N : Y;
    }

    public static YesNo valueOf(Number n) {
      return n == null || n.intValue() == 0 ? N : Y; 
    }
  };

  public enum Gender {
    F, // 女
    M, // 男
    U; // 未知
  };

  public enum Status {
    Y, // 有效
    N, // 无效
    D; // 已删除
  };

  public enum Role { 
    admin, // 管理员
    poweruser, // 高级用户
    user, // 操作用户
    guest, 
    view, // 查看
    none; // N/A
  };

}
