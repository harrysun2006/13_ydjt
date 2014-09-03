package com.free.misc;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.SimpleHash;

public class ShiroCredentialsMatcher extends HashedCredentialsMatcher {

  @Override
  public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
    boolean f = token instanceof RememberMeAuthenticationToken ? ((RememberMeAuthenticationToken) token).isRememberMe() : false;
    Object tokenHashedCredentials = f ? getCredentials(token) : hashProvidedCredentials(token, info);
    Object accountCredentials = getCredentials(info);
    return equals(tokenHashedCredentials, accountCredentials);
  }

  @Override
  protected Object getCredentials(AuthenticationToken token) {
    Object credentials = token.getCredentials();
    byte[] storedBytes = toBytes(credentials);
    if (credentials instanceof String || credentials instanceof char[]) {
      // account.credentials were a char[] or String, so
      // we need to do text decoding first:
      if (isStoredCredentialsHexEncoded()) {
        storedBytes = Hex.decode(storedBytes);
      } else {
        storedBytes = Base64.decode(storedBytes);
      }
    }
    SimpleHash hash = (SimpleHash) newHashInstance();
    hash.setBytes(storedBytes);
    return hash;
  }
}
