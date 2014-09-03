package com.free.crypt;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

abstract class AsymCoderImpl implements AsymCoder {

  protected Algorithm algorithm;
  protected String seed;
  protected KeyPair keyPair;

  protected void initKeyPair(int size) throws Exception {
    KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm.name());
    kpg.initialize(size);
    keyPair = kpg.generateKeyPair();
  }

  public String getName() {
    return algorithm.name();
  }

  public String getSeed() {
    return this.seed;
  }

  public KeyPair getKeyPair() throws Exception {
    return keyPair;
  }

  public KeyPair getPeerKeyPair() throws Exception {
    byte[] kb = keyPair.getPublic().getEncoded();
    KeySpec ks = new X509EncodedKeySpec(kb);
    KeyFactory kf = KeyFactory.getInstance(algorithm.name());
    PublicKey pubKey = kf.generatePublic(ks);
    // 由甲方公钥构建乙方密钥
    DHParameterSpec params = ((DHPublicKey) pubKey).getParams();
    KeyPairGenerator kpg = KeyPairGenerator.getInstance(kf.getAlgorithm());
    kpg.initialize(params);
    return kpg.generateKeyPair();
  }

  public Key getPublicKey() {
    return keyPair.getPublic();
  }

  public Key getPrivateKey() {
    return keyPair.getPrivate();
  }

  protected Signature getSignature() throws Exception {
    return Signature.getInstance(algorithm.name());
  }

  // 用私钥对信息生成数字签名
  public byte[] sign(byte[] data) throws Exception {
    Signature s = getSignature();
    s.initSign(keyPair.getPrivate());
    s.update(data);
    return s.sign();
  }

  // 用公钥验证签名是否正确
  public boolean verify(byte[] data, byte[] sign) throws Exception {
    Signature s = getSignature();
    s.initVerify(keyPair.getPublic());
    s.update(data);
    return s.verify(sign);
  }

  // 用公钥/私钥加密
  public byte[] encrypt(byte[] data, Key key) throws Exception {
    Cipher cipher = new NullCipher();
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return cipher.doFinal(data);
  }

  // 用私钥/公钥解密
  public byte[] decrypt(byte[] data, Key key) throws Exception {
    Cipher cipher = new NullCipher();
    cipher.init(Cipher.DECRYPT_MODE, key);
    return cipher.doFinal(data);
  }

}
