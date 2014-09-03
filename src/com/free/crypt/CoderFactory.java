package com.free.crypt;

import com.free.crypt.AsymCoder.SecretAlgorithm;
import com.free.crypt.AsymCoder.SignatureAlgorithm;

public class CoderFactory {

  public static final SignatureAlgorithm RSA_DEFAULT_SIGNATURE_ALGO = SignatureAlgorithm.MD5withRSA;
  public static final int RSA_DEFAULT_KEY_SIZE = 1024;

  public static final SecretAlgorithm DH_DEFAULT_SECRET_ALGO = SecretAlgorithm.DES;
  public static final int DH_DEFAULT_KEY_SIZE = 1024;

  public static final String DSA_DEFAULT_SEED = "0f22507a10bbddd07d8a3082122966e3";
  public static final int DSA_DEFAULT_KEY_SIZE = 1024;

  private CoderFactory() {
  }

  public static AsymCoder getAsymCoder(AsymCoder.Algorithm algorithm) throws Exception {
    return getAsymCoder(algorithm, DSA_DEFAULT_SEED);
  }

  public static AsymCoder getAsymCoder(AsymCoder.Algorithm algorithm, String seed) throws Exception {
    AsymCoder ac = null;
    if (algorithm == AsymCoder.Algorithm.RSA) {
      ac = new RSACoder(RSA_DEFAULT_SIGNATURE_ALGO, RSA_DEFAULT_KEY_SIZE);
    } else if (algorithm == AsymCoder.Algorithm.DH) {
      ac = new DHCoder(DH_DEFAULT_SECRET_ALGO, DH_DEFAULT_KEY_SIZE);
    } else if (algorithm == AsymCoder.Algorithm.DSA) {
      ac = new DSACoder(seed, DSA_DEFAULT_KEY_SIZE);
    } else if (algorithm == AsymCoder.Algorithm.EC) {
      ac = new ECCCoder();
    }
    return ac;
  }

  public static SymCoder getSymCoder(SymCoder.Algorithm algorithm) throws Exception {
    return getSymCoder(algorithm, null, 0);
  }

  public static SymCoder getSymCoder(SymCoder.Algorithm algorithm, String seed) throws Exception {
    return getSymCoder(algorithm, seed, 0);
  }

  public static SymCoder getSymCoder(SymCoder.Algorithm algorithm, String seed, int size) throws Exception {
    SymCoder sy;
    if (algorithm == SymCoder.Algorithm.PBEWITHMD5andDES) {
      sy = new PBECoder(algorithm, seed, null);
    } else {
      sy = new SymCoderImpl(algorithm, seed, size);
    }
    return sy;
  }

  public static SymCoder getSymCoder(SymCoder.Algorithm algorithm, String password, byte[] salt) throws Exception {
    SymCoder sy;
    if (algorithm == SymCoder.Algorithm.PBEWITHMD5andDES) {
      sy = new PBECoder(algorithm, password, salt);
    } else {
      sy = new SymCoderImpl(algorithm, password, 0);
    }
    return sy;
  }

}
