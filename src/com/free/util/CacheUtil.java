package com.free.util;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public class CacheUtil {

  private CacheUtil() {
  }

  public static Cache getDefault() {
    CacheManager cm = CacheManager.getInstance();
    Cache cache = cm.getCache("DEFAULT_CACHE");
    return cache;
  }

  public static List<Object> list() {
    return list(getDefault());
  }

  @SuppressWarnings("unchecked")
  public static List<Object> list(Cache cache) {
    return cache.getKeys();
  }

  public static void clear() {
    clear(getDefault(), "*");
  }

  public static void clear(String[] keys) {
    clear(getDefault(), keys);
  }

  public static void clear(Cache cache, String key) {
    clear(cache, new String[] { key });
  }

  @SuppressWarnings("rawtypes")
  public static void clear(Cache cache, String[] keys) {
    if (keys == null || keys.length <= 0)
      return;
    String ck;
    List ckeys = cache.getKeys();
    for (Object ckey : ckeys) {
      ck = (String) ckey;
      for (String key : keys) {
        if (key == null || "".equals(key))
          continue;
        if (ck.startsWith(key) || key.equals("*"))
          cache.remove(ckey);
      }
    }
  }

}
