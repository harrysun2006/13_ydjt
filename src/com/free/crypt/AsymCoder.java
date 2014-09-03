package com.free.crypt;

import java.security.Key;
import java.security.KeyPair;

public interface AsymCoder {

  public static enum Algorithm {
    DH,
    DSA, // DSA只是一种算法, 和RSA不同之处在于它不能用作加密和解密, 也不能进行密钥交换, 只用于数字签名和认证, 它比RSA要快很多.
    EC,
    RSA;
  };

  public static enum SignatureAlgorithm {
    MD5withRSA;
  };

  public static enum SecretAlgorithm {
    DES;
  };

  public String getName();

  public String getSeed();

  public KeyPair getKeyPair() throws Exception;

  public KeyPair getPeerKeyPair() throws Exception;

  public byte[] sign(byte[] data) throws Exception;

  public boolean verify(byte[] data, byte[] sign) throws Exception;

  public byte[] encrypt(byte[] data, Key key) throws Exception;

  public byte[] decrypt(byte[] data, Key key) throws Exception;

}