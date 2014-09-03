package com.free.crypt;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import com.free.util.CryptUtil;

class SymCoderImpl implements SymCoder {

  private Algorithm algorithm;
  private String seed;
  private Key key;
  private int size;
  private int maxSize;

  public SymCoderImpl(Algorithm algorithm, String seed, int size) throws Exception {
    this.key = initKey(algorithm, seed, size);
    if (size == 0) size = key.getEncoded().length;
    this.algorithm = algorithm;
    this.seed = seed;
    this.size = size;
    this.maxSize = Cipher.getMaxAllowedKeyLength(algorithm.name());
  }

  protected Key initKey(Algorithm algorithm, String seed, int size) throws Exception {
    SecureRandom sr = null;
    if (seed != null) {
      sr = new SecureRandom(CryptUtil.base64Decode(seed));
    } else {
      sr = new SecureRandom();
    }
    KeyGenerator kg = KeyGenerator.getInstance(algorithm.name());
    if (size == 0) {
      kg.init(sr);
    } else {
      // if (algorithm.check(size)) kg.init(size, sr);
      kg.init(size, sr);
    }
    return kg.generateKey();
  }

  public String getName() {
    return algorithm.name();
  }

  public String getPassword() {
    return this.seed;
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
    return null;
  }

  public byte[] encrypt(byte[] data) throws Exception {
    return crypt(data, Cipher.ENCRYPT_MODE);
  }

  public byte[] decrypt(byte[] data) throws Exception {
    return crypt(data, Cipher.DECRYPT_MODE);
  }

  protected byte[] crypt(byte[] data, int mode) throws Exception {
    Cipher cipher = Cipher.getInstance(algorithm.name());
    cipher.init(mode, key);
    return cipher.doFinal(data);
  }
}
