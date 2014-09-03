package com.free.crypt;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.free.util.CryptUtil;

class PBECoder implements SymCoder {

  private Algorithm algorithm;
  private String password;
  private byte[] salt;
  private Key key;
  private int size;
  private int maxSize;

  public PBECoder(Algorithm algorithm, String password, byte[] salt) throws Exception {
    if (salt == null) salt = CryptUtil.getSalt();
    if (password == null) password = CryptUtil.genSalt(8);
    this.algorithm = algorithm;
    this.password = password;
    this.salt = salt;
    this.key = initKey(password);
    this.size = key.getEncoded().length;
    this.maxSize = Cipher.getMaxAllowedKeyLength(algorithm.name());
  }

  protected Key initKey(String password) throws Exception {
    PBEKeySpec ks = new PBEKeySpec(password.toCharArray());
    SecretKeyFactory kf = SecretKeyFactory.getInstance(algorithm.name());
    return kf.generateSecret(ks);
  }

  public String getName() {
    return algorithm.name();
  }

  public String getPassword() {
    return this.password;
  }

  public Key getKey() {
    return this.key;
  }

  public int getKeySize() {
    return this.size;
  }

  public int getMaxKeySize() {
    return this.maxSize;
  }

  public byte[] getSalt() {
    return this.salt;
  }

  public byte[] encrypt(byte[] data) throws Exception {
    return crypt(data, Cipher.ENCRYPT_MODE);
  }

  public byte[] decrypt(byte[] data) throws Exception {
    return crypt(data, Cipher.DECRYPT_MODE);
  }

  protected byte[] crypt(byte[] data, int mode) throws Exception {
    PBEParameterSpec ps = new PBEParameterSpec(salt, 100);
    Cipher cipher = Cipher.getInstance(algorithm.name());
    cipher.init(mode, key, ps);
    return cipher.doFinal(data);
  }
}
