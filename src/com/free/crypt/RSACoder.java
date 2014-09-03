package com.free.crypt;

import java.security.Key;
import java.security.KeyFactory;
import java.security.Signature;

import javax.crypto.Cipher;

class RSACoder extends AsymCoderImpl {

  protected SignatureAlgorithm sa;

  public RSACoder(SignatureAlgorithm sa, int size) throws Exception {
    if (sa == null) sa = SignatureAlgorithm.MD5withRSA;
    this.algorithm = AsymCoder.Algorithm.RSA;
    this.sa = sa;
    initKeyPair(size);
  }

  protected Signature getSignature() throws Exception {
    return Signature.getInstance(sa.name());
  }

  // 用公钥/私钥加密
  public byte[] encrypt(byte[] data, Key key) throws Exception {
    return crypt(data, key, Cipher.ENCRYPT_MODE);
  }

  // 用私钥/公钥解密
  public byte[] decrypt(byte[] data, Key key) throws Exception {
    return crypt(data, key, Cipher.DECRYPT_MODE);
  }

  protected byte[] crypt(byte[] data, Key key, int mode) throws Exception {
    KeyFactory kf = KeyFactory.getInstance(algorithm.name());
    // 对数据加密/解密
    Cipher cipher = Cipher.getInstance(kf.getAlgorithm());
    cipher.init(mode, key);
    return cipher.doFinal(data);
  }

}
