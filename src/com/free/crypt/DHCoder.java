package com.free.crypt;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;

class DHCoder extends AsymCoderImpl {

  protected SecretAlgorithm sa;

  public DHCoder(SecretAlgorithm sa, int size) throws Exception {
    if (sa == null) sa = SecretAlgorithm.DES;
    this.algorithm = AsymCoder.Algorithm.DH;
    this.sa = sa;
    initKeyPair(size);
  }

  public byte[] sign(byte[] data) throws Exception {
    throw new Exception("sign is not implemented!");
  }

  public boolean verify(byte[] data, byte[] sign) throws Exception {
    throw new Exception("verify is not implemented!");
  }

  protected SecretKey getSecretKey(Key key) throws Exception {
    Key key1 = null, key2 = null;
    if (key instanceof PublicKey) {
      key1 = key;
      key2 = keyPair.getPrivate();
    } else if (key instanceof PrivateKey) {
      key1 = keyPair.getPublic();
      key2 = key;
    }
    KeyAgreement ka = KeyAgreement.getInstance(algorithm.name());
    ka.init(key2);
    ka.doPhase(key1, true);
    // 生成本地密钥
    SecretKey sk = ka.generateSecret(sa.name());
    return sk;
  }

  // 用公钥+对方私钥加密
  public byte[] encrypt(byte[] data, Key key) throws Exception {
    return crypt(data, key, Cipher.ENCRYPT_MODE);
  }

  // 用私钥+对方公钥解密
  public byte[] decrypt(byte[] data, Key key) throws Exception {
    return crypt(data, key, Cipher.DECRYPT_MODE);
  }

  protected byte[] crypt(byte[] data, Key key, int mode) throws Exception {
    SecretKey sk = getSecretKey(key);
    // 对数据加密/解密
    Cipher cipher = Cipher.getInstance(sk.getAlgorithm());
    cipher.init(mode, sk);
    return cipher.doFinal(data);
  }

}
