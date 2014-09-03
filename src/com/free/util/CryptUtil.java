package com.free.util;

import java.io.IOException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.free.crypt.SymCoder.Algorithm;

public class CryptUtil {

  public static final int DEFAULT_SALT_LEN = 6;

  public static enum HashAlgorithm {
    SHA, MD5
  };

  public static enum HMACAlgorithm {
    HmacMD5, HmacSHA1, HmacSHA256, HmacSHA384, HmacSHA512
  };

  public static final String PBE_ALGORITHM = "PBEWITHMD5andDES";

  public static String genSalt() {
    return genSalt(DEFAULT_SALT_LEN);
  }

  public static String genSalt(int len) {
    Random r = new Random();
    StringBuilder salt = new StringBuilder();
    int c;
    for (int i = 0; i < len; i++) {
      c = 33 + r.nextInt(94); // ! ... ~ [33, 126]
      salt.append((char) c);
    }
    return salt.toString();
  }

  public static byte[] getSalt() throws Exception {
    byte[] salt = new byte[8];
    Random random = new Random();
    random.nextBytes(salt);
    return salt;
  }

  /**
   * BKDRHash, APHash, DJBHash, JSHash, RSHash, SDBMHash, PJWHash,
   * ELFHash字符串Hash函数比较： http://www.demix.cn/h?z=28676 java使用的就是BKDRHash, seed = 31
   * 
   * @param str
   * @return
   */
  public static long BKDRHash(String str) {
    long seed = 131; // 31 131 1313 13131 131313 etc..
    long hash = 0;
    for (int i = 0; i < str.length(); i++) {
      hash = (hash * seed) + str.charAt(i);
    }
    return hash;
  }

  public static long APHash(String str) {
    long hash = 0xAAAAAAAA; // 0x0;
    for (int i = 0; i < str.length(); i++) {
      if ((i & 1) == 0) {
        hash ^= ((hash << 7) ^ str.charAt(i) ^ (hash >> 3));
      } else {
        hash ^= (~((hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
      }
    }
    return hash; // hash & 0x7FFFFFFF;
  }

  public static long DJBHash(String str) {
    long hash = 5381;
    for (int i = 0; i < str.length(); i++) {
      hash = ((hash << 5) + hash) + str.charAt(i);
    }
    return hash;
  }

  public static long JSHash(String str) {
    long hash = 1315423911;
    for (int i = 0; i < str.length(); i++) {
      hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
    }
    return hash; // hash & 0x7FFFFFFF;
  }

  public static long RSHash(String str) {
    int a = 63689;
    int b = 378551;
    long hash = 0;
    for (int i = 0; i < str.length(); i++) {
      hash = hash * a + str.charAt(i);
      a = a * b;
    }
    return hash;
  }

  public static long SDBMHash(String str) {
    long hash = 0;
    for (int i = 0; i < str.length(); i++) {
      hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
    }
    return hash;
  }

  public static long PJWHash(String str) {
    long BitsInUnsignedInt = (long) (4 * 8);
    long ThreeQuarters = (long) ((BitsInUnsignedInt * 3) / 4);
    long OneEighth = (long) (BitsInUnsignedInt / 8);
    long HighBits = (long) (0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
    long hash = 0;
    long test = 0;
    for (int i = 0; i < str.length(); i++) {
      hash = (hash << OneEighth) + str.charAt(i);
      if ((test = hash & HighBits) != 0) {
        hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
      }
    }
    return hash;
  }

  public static long ELFHash(String str) {
    long hash = 0;
    long x = 0;
    for (int i = 0; i < str.length(); i++) {
      hash = (hash << 4) + str.charAt(i);
      if ((x = hash & 0xF0000000L) != 0) {
        hash ^= (x >> 24);
      }
      hash &= ~x;
    }
    return hash;
  }

  public static long DEKHash(String str) {
    long hash = str.length();
    for (int i = 0; i < str.length(); i++) {
      hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
    }
    return hash;
  }

  public static long BPHash(String str) {
    long hash = 0;
    for (int i = 0; i < str.length(); i++) {
      hash = hash << 7 ^ str.charAt(i);
    }
    return hash;
  }

  public static long FNVHash(String str) {
    long fnv_prime = 0x811C9DC5;
    long hash = 0;
    for (int i = 0; i < str.length(); i++) {
      hash *= fnv_prime;
      hash ^= str.charAt(i);
    }
    return hash;
  }

  public static String base64Encode(byte[] b) {
    return new sun.misc.BASE64Encoder().encode(b);
  }

  public static byte[] base64Decode(String s) throws IOException {
    return new sun.misc.BASE64Decoder().decodeBuffer(s);
  }

  public static byte[] md5(byte[] data) throws Exception {
    MessageDigest md5 = MessageDigest.getInstance(HashAlgorithm.MD5.name());
    md5.update(data);
    return md5.digest();
  }

  public static byte[] sha(byte[] data) throws Exception {
    MessageDigest sha = MessageDigest.getInstance(HashAlgorithm.SHA.name());
    sha.update(data);
    return sha.digest();
  }

  public static byte[] hmac(byte[] data) throws Exception {
    return hmac(HMACAlgorithm.HmacMD5, data, null);
  }

  public static byte[] hmac(byte[] data, String key) throws Exception {
    return hmac(HMACAlgorithm.HmacMD5, data, key);
  }

  public static byte[] hmac(HMACAlgorithm algorithm, byte[] data) throws Exception {
    return hmac(algorithm, data, null);
  }

  public static byte[] hmac(HMACAlgorithm algorithm, byte[] data, String key) throws Exception {
    SecretKey sk;
    if (key == null) {
      KeyGenerator kg = KeyGenerator.getInstance(algorithm.name());
      sk = kg.generateKey();
    } else {
      sk = new SecretKeySpec(base64Decode(key), algorithm.name());
    }
    Mac mac = Mac.getInstance(sk.getAlgorithm());
    mac.init(sk);
    return mac.doFinal(data);
  }

  public static String getKey() throws Exception {
    return getKey(HMACAlgorithm.HmacMD5);
  }

  public static String getKey(HMACAlgorithm algorithm) throws Exception {
    KeyGenerator kg = KeyGenerator.getInstance(algorithm.name());
    SecretKey sk = kg.generateKey();
    return base64Encode(sk.getEncoded());
  }

  public static String getKey(Algorithm algorithm) throws Exception {
    return getKey(algorithm, null, 0);
  }

  public static String getKey(Algorithm algorithm, String seed) throws Exception {
    return getKey(algorithm, seed, 0);
  }

  public static String getKey(Algorithm algorithm, String seed, int size) throws Exception {
    SecureRandom sr = null;
    if (seed != null) {
      sr = new SecureRandom(base64Decode(seed));
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
    SecretKey sk = kg.generateKey();
    return base64Encode(sk.getEncoded());
  }

  protected static Key toKey(Algorithm algorithm, byte[] key) {
    SecretKey sk = new SecretKeySpec(key, algorithm.name());
    return sk;
  }

  public static byte[] symDecrypt(Algorithm algorithm, byte[] data, String key) throws Exception {
    return symCrypt(algorithm, data, key, Cipher.DECRYPT_MODE);
  }

  public static byte[] symEncrypt(Algorithm algorithm, byte[] data, String key) throws Exception {
    return symCrypt(algorithm, data, key, Cipher.ENCRYPT_MODE);
  }

  protected static byte[] symCrypt(Algorithm algorithm, byte[] data, String key, int mode) throws Exception {
    Key k = toKey(algorithm, base64Decode(key));
    Cipher cipher = Cipher.getInstance(algorithm.name());
    cipher.init(mode, k);
    return cipher.doFinal(data);
  }

  protected static Key getKey(String password) throws Exception {
    PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE_ALGORITHM);
    SecretKey secretKey = keyFactory.generateSecret(keySpec);
    return secretKey;
  }

  public static byte[] pbeEncrypt(byte[] data, String password, byte[] salt) throws Exception {
    return pbeCrypt(data, password, salt, Cipher.ENCRYPT_MODE);
  }

  public static byte[] pbeDecrypt(byte[] data, String password, byte[] salt) throws Exception {
    return pbeCrypt(data, password, salt, Cipher.DECRYPT_MODE);
  }

  protected static byte[] pbeCrypt(byte[] data, String password, byte[] salt, int mode) throws Exception {
    Key key = getKey(password);
    PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 100);
    Cipher cipher = Cipher.getInstance(PBE_ALGORITHM);
    cipher.init(mode, key, paramSpec);
    return cipher.doFinal(data);
  }
}
