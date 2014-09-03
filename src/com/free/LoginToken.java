package com.free;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.UsernamePasswordToken;

public class LoginToken extends UsernamePasswordToken {

  private static final long serialVersionUID = -7940996194660237883L;

  private Map<String, Object> params = new HashMap<String, Object>();

  public LoginToken() {
  }

  public LoginToken(String username, char[] password) {
    this(username, password, false, null);
  }

  public LoginToken(String username, String password) {
    this(username, password != null ? password.toCharArray() : null, false, null);
  }

  public LoginToken(String username, char[] password, String host) {
    this(username, password, false, host);
  }

  public LoginToken(String username, String password, String host) {
    this(username, password != null ? password.toCharArray() : null, false, host);
  }

  public LoginToken(String username, char[] password, boolean rememberMe) {
    this(username, password, rememberMe, null);
  }

  public LoginToken(String username, String password, boolean rememberMe) {
    this(username, password != null ? password.toCharArray() : null, rememberMe, null);
  }

  public LoginToken(String username, String password, boolean rememberMe, String host) {
    this(username, password != null ? password.toCharArray() : null, rememberMe, host);
  }

  public LoginToken(String username, char[] password, boolean rememberMe, String host) {
    super(username, password, rememberMe, host);
  }

  public void put(String key, Object value) {
    params.put(key, value);
  }

  public Object get(String key) {
    return params.get(key);
  }

  public void remove(String key) {
    params.remove(key);
  }

  public void clear() {
    params.clear();
  }

  public Set<String> keys() {
    return params.keySet();
  }

  public Collection<Object> values() {
    return params.values();
  }

  public Map<String, Object> params() {
    return params;
  }
}
