package com.free.crypt;

import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

class DSACoder extends AsymCoderImpl {

  public DSACoder(String seed, int size) throws Exception {
    this.algorithm = AsymCoder.Algorithm.DSA;
    initKeyPair(seed, size);
  }

  protected void initKeyPair(String seed, int size) throws Exception {
    KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm.name());
    //初始化随机产生器
    SecureRandom sr = new SecureRandom();
    sr.setSeed(seed.getBytes());
    kpg.initialize(size, sr);
    keyPair = kpg.genKeyPair();
  }

  // 用公钥/私钥加密
  public byte[] encrypt(byte[] data, Key key) throws Exception {
    throw new Exception("encrypt is not implemented!");
  }

  // 用私钥/公钥解密
  public byte[] decrypt(byte[] data, Key key) throws Exception {
    throw new Exception("decrypt is not implemented!");
  }
}
