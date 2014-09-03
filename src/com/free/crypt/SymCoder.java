package com.free.crypt;

import java.security.Key;

import javax.crypto.Cipher;

public interface SymCoder {

  public static enum Algorithm {
    DES,      // key size must be equal to 56
    DESede,   // TripleDES, key size must be equal to 112 or 168
    AES,      // key size must be equal to 128, 192 or 256, but 192 and 256 bits may not be available
    Blowfish, // key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
    RC2,      // key size must be between 40 and 1024 bits
    RC4,      // ARCFOUR, key size must be between 40 and 1024 bits
    PBEWITHMD5andDES;

    public boolean check(int size) throws Exception {
      boolean r = false;
      int max = Cipher.getMaxAllowedKeyLength(name());
      if (size > max) {
        throw new Exception(name() + "'s maximum key size must be <= " + max + "!");
      }
      if (this == DES) {
        if (size == 56) r = true;
        else throw new Exception(name() + "'s key size must be equal to 56!");
      } else if (this == DESede) {
        if (size == 112 || size == 168) r = true;
        else throw new Exception(name() + "'s key size must be equal to 112 or 168!");
      } else if (this == AES) {
        if (size == 128 || size == 192 || size == 256) r = true;
        else throw new Exception(name() + "'s key size must be equal to 128, 192 or 256, but 192 and 256 bits may not be available!");
      } else if (this == Blowfish) {
        if (size % 8 == 0 && size >= 32 && size <= 448) r = true;
        else throw new Exception(name() + "'s key size must be multiple of 8, and can only range from 32 to 448 (inclusive)!");
      } else if (this == RC2) {
        if (size >= 40 && size <= 1024) r = true;
        else throw new Exception(name() + "'s key size must be between 40 and 1024 bits!");
      } else if (this == RC4) {
        if (size >= 40 && size <= 1024) r = true;
        else throw new Exception(name() + "'s key size must be between 40 and 1024 bits!");
      }
      return r;
    }
  };

  public String getName();

  public String getPassword();

  public Key getKey();

  public int getKeySize();

  public int getMaxKeySize();

  public byte[] getSalt();

  public byte[] encrypt(byte[] data) throws Exception;

  public byte[] decrypt(byte[] data) throws Exception;

}
