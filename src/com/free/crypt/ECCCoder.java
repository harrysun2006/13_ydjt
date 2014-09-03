package com.free.crypt;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;

import sun.security.ec.ECKeyFactory;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;

class ECCCoder extends AsymCoderImpl {

  public ECCCoder() throws Exception {
    this.algorithm = AsymCoder.Algorithm.EC;
    initKeyPair();
  }

  protected void initKeyPair() throws Exception {
    BigInteger x1 = new BigInteger("2fe13c0537bbc11acaa07d793de4e6d5e5c94eee8", 16);
    BigInteger x2 = new BigInteger("289070fb05d38ff58321f2e800536d538ccdaa3d9", 16);
    ECPoint g = new ECPoint(x1, x2);
    // the order of generator
    BigInteger n = new BigInteger("5846006549323611672814741753598448348329118574063", 10);
    // the cofactor
    int h = 2;
    int m = 163;
    int[] ks = { 7, 6, 3 };
    ECFieldF2m ecField = new ECFieldF2m(m, ks);
    // y^2+xy=x^3+x^2+1
    BigInteger a = new BigInteger("1", 2);
    BigInteger b = new BigInteger("1", 2);
    EllipticCurve ellipticCurve = new EllipticCurve(ecField, a, b);
    ECParameterSpec ecParameterSpec = new ECParameterSpec(ellipticCurve, g, n, h);
    // 公钥
    ECPublicKey pubKey = new ECPublicKeyImpl(g, ecParameterSpec);
    BigInteger s = new BigInteger("1234006549323611672814741753598448348329118574063", 10);
    // 私钥
    ECPrivateKey privKey = new ECPrivateKeyImpl(s, ecParameterSpec);
    this.keyPair = new KeyPair(pubKey, privKey);
  }

  public byte[] sign(byte[] data) throws Exception {
    throw new Exception("sign is not implemented!");
  }

  public boolean verify(byte[] data, byte[] sign) throws Exception {
    throw new Exception("verify is not implemented!");
  }

  // 用公钥加密
  public byte[] encrypt(byte[] data, Key key) throws Exception {
    byte[] kb = key.getEncoded();
    X509EncodedKeySpec ks = new X509EncodedKeySpec(kb);
    KeyFactory kf = ECKeyFactory.INSTANCE;  
    ECPublicKey pubKey = (ECPublicKey) kf.generatePublic(ks);
    ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(pubKey.getW(), pubKey.getParams());
    // 对数据加密, Chipher不支持EC算法 未能实现
    Cipher cipher = new NullCipher();
    // Cipher cipher = Cipher.getInstance(algorithm.name(), kf.getProvider());  
    cipher.init(Cipher.ENCRYPT_MODE, pubKey, ecPublicKeySpec.getParams());  
    return cipher.doFinal(data);
    // throw new Exception("encrypt is not implemented!");
  }

  public byte[] decrypt(byte[] data, Key key) throws Exception {
    byte[] kb = key.getEncoded();
    PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(kb);
    KeyFactory kf = ECKeyFactory.INSTANCE;
    ECPrivateKey priKey = (ECPrivateKey) kf.generatePrivate(ks);
    ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(priKey.getS(), priKey.getParams());
    // 对数据解密, Chipher不支持EC算法 未能实现
    Cipher cipher = new NullCipher();
    // Cipher cipher = Cipher.getInstance(algorithm.name(), kf.getProvider());
    cipher.init(Cipher.DECRYPT_MODE, priKey, ecPrivateKeySpec.getParams());
    return cipher.doFinal(data);
    // throw new Exception("decrypt is not implemented!");
  }
}
