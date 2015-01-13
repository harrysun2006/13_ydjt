package com.free.crypt;

import java.security.Key;

class ECCCoder extends AsymCoderImpl {

  public ECCCoder() throws Exception {
    this.algorithm = AsymCoder.Algorithm.EC;
    initKeyPair();
  }

  protected void initKeyPair() throws Exception {

  }

  public byte[] sign(byte[] data) throws Exception {
    throw new Exception("sign is not implemented!");
  }

  public boolean verify(byte[] data, byte[] sign) throws Exception {
    throw new Exception("verify is not implemented!");
  }

  // 用公钥加密
  public byte[] encrypt(byte[] data, Key key) throws Exception {
    throw new Exception("encrypt is not implemented!");
  }

  public byte[] decrypt(byte[] data, Key key) throws Exception {
    throw new Exception("decrypt is not implemented!");
  }
}
