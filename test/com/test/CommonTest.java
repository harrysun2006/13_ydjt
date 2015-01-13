package com.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.Provider;
import java.security.Security;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.h2.tools.Server;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.free.Application;
import com.free.Mocker;
import com.free.crypt.AsymCoder;
import com.free.crypt.CoderFactory;
import com.free.crypt.SymCoder;
import com.free.util.CacheUtil;
import com.free.util.CommonUtil;
import com.free.util.CryptUtil;
import com.free.util.CryptUtil.HMACAlgorithm;
import com.free.util.JDBCUtil;
import com.free.ydjt.dto.Folder;
import com.free.ydjt.dto.User;
import com.free.ydjt.service.FolderService;
import com.free.ydjt.service.UserService;
import com.jolbox.bonecp.BoneCPDataSource;
import com.test.aop.AOPTest;
import com.test.service.AService;
import com.test.service.BServiceImpl;


/**
 * 测试一些组件用法等
 * 
 * @author Harry Sun <harrysun2006@gmail.com>
 */
public class CommonTest {

	private static final String[] H2_SERVER_ARGS = {"-tcp", "-tcpAllowOthers", "-web", "-webAllowOthers", "-baseDir", ""};
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  // 测试UUID
  protected static void test01() throws Exception {
    // 82f24d0e-cae6-44a2-9471-5ca74a003868
    // 1ccc2e21-a19c-49f8-bb8e-94acb82b934e
    UUID uid = UUID.randomUUID();
    System.out.println(String.format("%08X%08X", uid.getMostSignificantBits(), uid.getLeastSignificantBits()));
    String id = uid.toString();
    // id = "1263e5a5-cf2b-4bde-9b00-c4685329fd86";
    System.out.println(id);
    byte[] b = CommonUtil.hex2bytes(id);
    String s = CryptUtil.base64Encode(b);
    System.out.println(s);
    byte[] c = CryptUtil.base64Decode(s);
    System.out.println(CommonUtil.bytes2hex(c).toUpperCase());
    byte[] d = CryptUtil.md5(b);
    System.out.println(CryptUtil.base64Encode(d));
  }

  // 测试DecimalFormat
  protected static void test02() throws Exception {
    DecimalFormat df = new DecimalFormat("#,###.##");
    System.out.println(df.format(100*Double.parseDouble("1234567.89874")));
  }

  // 测试BoneCP数据源
  protected static void test03() throws Exception {
    BoneCPDataSource ds = new BoneCPDataSource();
    // ds.setJdbcUrl("jdbc:h2:tcp://localhost/h2/db;schema=IDS");
    // ds.setUsername("sa");
    // ds.setPassword("sa");
    ds.setDriverClass("oracle.jdbc.driver.OracleDriver");
    ds.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:XE");
    ds.setUsername("ZF");
    ds.setPassword("admin");
    ds.setMaxConnectionsPerPartition(1);
    ds.setMinConnectionsPerPartition(1);
    Connection conn = ds.getConnection();
    String sql = "SELECT * FROM T_DICT";
    Object[] data = JDBCUtil.query(conn, sql);
    conn.close();
    ds.close();
    for (Object d : data)
      System.out.println(d);
  }

  // 测试AOP, 使用spring定义切面
  protected static void test10() throws Exception {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("test-aop1.xml");
    AOPTest test = new AOPTest();
    test.setAService((AService) ctx.getBean("aService"));
    test.setBService((BServiceImpl) ctx.getBean("bService"));
    test.testCall();
    test.testThrow();
  }

  // 测试AOP, 使用spring定义切面, 切面类使用注解
  protected static void test11() throws Exception {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("test-aop2.xml");
    AOPTest test = new AOPTest();
    test.setAService((AService) ctx.getBean("aService"));
    test.setBService((BServiceImpl) ctx.getBean("bService"));
    test.testCall();
    test.testThrow();
  }

  // Kuwo Music, 转换txt文件(歌曲序号)到play list文件(序号十六进制)
  protected static void test12() throws Exception {
    File fi = new File("D:\\a.txt");
    File fo = new File("D:\\C8XZ.pl");
    LineNumberReader r = new LineNumberReader(new FileReader(fi));
    OutputStream os = new FileOutputStream(fo);
    String s;
    while ((s = r.readLine()) != null) {
      os.write(CommonUtil.int2bytes(Integer.parseInt(s)));
    }
    os.close();
    r.close();
  }

  protected static final String TEST_TEXT = "MD5、SHA以及HMAC是单向加密，任何数据加密后只会产生唯一的一个加密串，通常用来校验数据在传输过程中是否被修改。其中HMAC算法有一个密钥，增强了数据传输过程中的安全性，强化了算法外的不可控因素。"
      + "\nDES-Data Encryption Standard,即数据加密算法。是IBM公司于1975年研究成功并公开发表的。";
  protected static final String TEST_TEXT1 = "E47010F2275657C7E0435360F00A1716";

  // 测试Hash算法: md5, sha, mac
  protected static void test20() throws Exception {
    String inputStr = TEST_TEXT;
    System.out.println("原文:\n" + inputStr);

    byte[] inputData = inputStr.getBytes();
    String code = CryptUtil.base64Encode(inputData);
    System.out.println("BASE64加密后:\n" + code);
    byte[] output = CryptUtil.base64Decode(code);
    String outputStr = new String(output);
    System.out.println("BASE64解密后:\n" + outputStr);

    // 验证BASE64加密解密一致性
    Assert.assertEquals(inputStr, outputStr);

    // 验证MD5对于同一内容加密是否一致
    Assert.assertArrayEquals(CryptUtil.md5(inputData), CryptUtil.md5(inputData));

    // 验证SHA对于同一内容加密是否一致
    Assert.assertArrayEquals(CryptUtil.sha(inputData), CryptUtil.sha(inputData));   

    String md5 = CommonUtil.bytes2hex(CryptUtil.md5(inputData));
    System.out.println("MD5:\n" + md5);

    String sha = CommonUtil.bytes2hex(CryptUtil.sha(inputData));
    System.out.println("SHA:\n" + sha);

    HMACAlgorithm[] algorithms = {HMACAlgorithm.HmacMD5, HMACAlgorithm.HmacSHA1, HMACAlgorithm.HmacSHA256, HMACAlgorithm.HmacSHA384, HMACAlgorithm.HmacSHA512};
    String key, mac;
    for (int i = 0; i < algorithms.length; i++) {
      key = CryptUtil.getKey();
      System.out.println(String.format("Mac密钥算法: %s, Mac密钥: %s", algorithms[i], key));

      // 验证HMAC对于同一内容，同一密钥加密是否一致
      Assert.assertArrayEquals(CryptUtil.hmac(inputData, key), CryptUtil.hmac(inputData, key));
      mac = CommonUtil.bytes2hex(CryptUtil.hmac(inputData, key));
      System.out.println(String.format("密文: %s", mac));
    }
  }

  // 测试对称加密解密算法: DES, AES, Blowfish...
  protected static void test21() throws Exception {
    String text = TEST_TEXT;
    String seed = "Panda@21Panda@21Panda@21Panda@21Panda@21Panda@21";
    String k1, k2, de;
    byte[] b;
    SymCoder.Algorithm[] as = {SymCoder.Algorithm.DES, SymCoder.Algorithm.DESede, 
        SymCoder.Algorithm.AES, SymCoder.Algorithm.Blowfish, 
        SymCoder.Algorithm.RC2, SymCoder.Algorithm.RC4};
    int[][] ss = {{56, 128}, {112, 168, 192}, {64, 128, 192, 256}, {32, 92, 128, 448}, {40, 64, 128, 1024}, {40, 64, 128, 1024}};
    for (int i = 0; i < as.length; i++) {
      for (int j = 0; j < ss[i].length; j++) {
        try {
          System.out.println(String.format("%s(%d):", as[i].name(), ss[i][j]));
          k1 = CryptUtil.getKey(as[i], seed, ss[i][j]);
          k2 = CryptUtil.getKey(as[i], seed, ss[i][j]);
          b = CryptUtil.symEncrypt(as[i], text.getBytes(), k1);
          de = new String(CryptUtil.symDecrypt(as[i], b, k2), "UTF-8");
          System.out.println(String.format("key1=%s\nkey2=%s\n%s\n%s", k1, k2, CryptUtil.base64Encode(b), de));
        } catch (Exception e) {
          System.out.println(e);
        }
      }
    }
  }

  // 测试对称加密解密算法: DES, AES, Blowfish...
  protected static void test22() throws Exception {
    String text = TEST_TEXT;
    String seed = "Panda@21Panda@21Panda@21Panda@21Panda@21Panda@21";
    String de;
    byte[] b;
    SymCoder.Algorithm[] as = {SymCoder.Algorithm.DES, SymCoder.Algorithm.DESede, 
        SymCoder.Algorithm.AES, SymCoder.Algorithm.Blowfish, 
        SymCoder.Algorithm.RC2, SymCoder.Algorithm.RC4};
    int[][] ss = {{56, 128}, {112, 168, 192}, {64, 128, 192, 256}, {32, 92, 128, 448}, {40, 64, 128, 1024}, {40, 64, 128, 1024}};
    SymCoder sc;
    for (int i = 0; i < as.length; i++) {
      for (int j = 0; j < ss[i].length; j++) {
        try {
          sc = CoderFactory.getSymCoder(as[i], seed, ss[i][j]);
          System.out.println(String.format("%s(%d):", sc.getName(), sc.getKeySize()));
          b = sc.encrypt(text.getBytes());
          de = new String(sc.decrypt(b), "UTF-8");
          System.out.println(String.format("key=%s\n%s\n%s", CryptUtil.base64Encode(sc.getKey().getEncoded()), CryptUtil.base64Encode(b), de));
        } catch (Exception e) {
          System.out.println(e);
        }
      }
    }
  }

  // 测试PBE加解密算法
  protected static void test23() throws Exception {
    String text = TEST_TEXT;
    String password = "Panda@21Panda@21Panda@21Panda@21Panda@21Panda@21";
    byte[] salt = CryptUtil.getSalt();
    // byte[] salt = CryptUtil.base64Decode("W6E+gj9QqVk=");
    byte[] data = CryptUtil.pbeEncrypt(text.getBytes(), password, salt);
    String de = new String(CryptUtil.pbeDecrypt(data, password, salt));
    System.out.println(String.format("text: %s\npassword: %s\nsalt: %s\nencrypt: %s\ndecrypt: %s", 
        text, password, CryptUtil.base64Encode(salt), CryptUtil.base64Encode(data), de));
    SymCoder sc = CoderFactory.getSymCoder(SymCoder.Algorithm.PBEWITHMD5andDES, password, salt);
    data = sc.encrypt(text.getBytes());
    de = new String(sc.decrypt(data));
    System.out.println(String.format("text: %s\npassword: %s\nsalt: %s\nkey(%d, %d): %s\nencrypt: %s\ndecrypt: %s", 
        text, password, CryptUtil.base64Encode(salt), sc.getMaxKeySize(), sc.getKeySize(),
        CryptUtil.base64Encode(sc.getKey().getEncoded()), CryptUtil.base64Encode(data), de));
  }

  // 测试RSA加解密算法
  protected static void test24() throws Exception {
    String text = TEST_TEXT;
    String de = null;
    String seed = "0f22507a10bbddd07d8a3082122966e3";
    byte[] d, b, s;
    boolean v;
    KeyPair kp;
    Key pub, priv;
    AsymCoder.Algorithm[] as = {AsymCoder.Algorithm.RSA};
    AsymCoder ac;
    d = text.getBytes();
    for (int i = 0; i < as.length; i++) {
      try {
        ac = CoderFactory.getAsymCoder(as[i], seed);
        System.out.println(String.format("%s:", ac.getName()));
        kp = ac.getKeyPair();
        pub = kp.getPublic();
        priv = kp.getPrivate();
        b = ac.encrypt(d, priv);
        s = ac.sign(b);
        v = ac.verify(b, s);
        de = new String(ac.decrypt(b, pub), "UTF-8");
        System.out.println(String.format("text:\n%s\npublic key:\n%s\nprivate key:\n%s\nencrypt:\n%s\nsignature:\n%s\nvalid:%s\ndecrypt:\n%s", 
            text, CryptUtil.base64Encode(pub.getEncoded()), CryptUtil.base64Encode(priv.getEncoded()), 
            CryptUtil.base64Encode(b), CryptUtil.base64Encode(s), String.valueOf(v), de));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  // 测试DH加解密算法, (公钥1+私钥2)'s密文 == (公钥2+私钥1)'s密文
  protected static void test25() throws Exception {
    String text = TEST_TEXT;
    String de = null;
    byte[] d, b, s = {};
    boolean v = false;
    KeyPair kp, kp2;
    Key pub, priv;
    AsymCoder.Algorithm[] as = {AsymCoder.Algorithm.DH};
    AsymCoder ac;
    d = text.getBytes();
    for (int i = 0; i < as.length; i++) {
      try {
        ac = CoderFactory.getAsymCoder(as[i]);
        System.out.println(String.format("%s:", ac.getName()));
        kp = ac.getKeyPair();
        pub = kp.getPublic();
        priv = kp.getPrivate();
        kp2 = ac.getPeerKeyPair();
        b = ac.encrypt(d, kp2.getPrivate());
        // s = ac.sign(b);
        // v = ac.verify(b, s);
        de = new String(ac.decrypt(b, kp2.getPublic()), "UTF-8");
        System.out.println(String.format("text:\n%s\npublic key:\n%s\nprivate key:\n%s\nencrypt:\n%s\nsignature:\n%s\nvalid:%s\ndecrypt:\n%s", 
            text, CryptUtil.base64Encode(pub.getEncoded()), CryptUtil.base64Encode(priv.getEncoded()), 
            CryptUtil.base64Encode(b), CryptUtil.base64Encode(s), String.valueOf(v), de));
        b = ac.encrypt(d, kp2.getPublic());
        de = new String(ac.decrypt(b, kp2.getPrivate()), "UTF-8");
        System.out.println(String.format("encrypt:\n%s\ndecrypt:\n%s", CryptUtil.base64Encode(b), de));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  // 测试DSA算法,只用于数字签名
  protected static void test26() throws Exception {
    String text = TEST_TEXT;
    String seed = "0f22507a10bbddd07d8a3082122966e3";
    String de = null;
    byte[] b = {}, d, s;
    boolean v;
    KeyPair kp;
    Key pub, priv;
    AsymCoder.Algorithm[] as = {AsymCoder.Algorithm.DSA};
    AsymCoder ac;
    d = text.getBytes();
    for (int i = 0; i < as.length; i++) {
      try {
        ac = CoderFactory.getAsymCoder(as[i], seed);
        System.out.println(String.format("%s:", ac.getName()));
        kp = ac.getKeyPair();
        pub = kp.getPublic();
        priv = kp.getPrivate();
        b = ac.encrypt(d, priv);
        s = ac.sign(d);
        v = ac.verify(d, s);
        de = new String(ac.decrypt(b, pub), "UTF-8");
        // de = new String(b, "UTF-8");
        System.out.println(String.format("text:\n%s\npublic key:\n%s\nprivate key:\n%s\nencrypt:\n%s\nsignature:\n%s\nvalid:%s\ndecrypt:\n%s", 
            text, CryptUtil.base64Encode(pub.getEncoded()), CryptUtil.base64Encode(priv.getEncoded()), 
            CryptUtil.base64Encode(b), CryptUtil.base64Encode(s), String.valueOf(v), de));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  // 测试ECC算法
  protected static void test27() throws Exception {
    String text = TEST_TEXT;
    String de = null;
    byte[] b = {}, d, s = {};
    boolean v = false;
    KeyPair kp;
    Key pub, priv;
    AsymCoder.Algorithm[] as = {AsymCoder.Algorithm.EC};
    AsymCoder ac;
    d = text.getBytes();
    for (int i = 0; i < as.length; i++) {
      try {
        ac = CoderFactory.getAsymCoder(as[i]);
        System.out.println(String.format("%s:", ac.getName()));
        kp = ac.getKeyPair();
        pub = kp.getPublic();
        priv = kp.getPrivate();
        b = ac.encrypt(d, pub);
        // s = ac.sign(d);
        // v = ac.verify(d, s);
        de = new String(ac.decrypt(b, priv), "UTF-8");
        // de = new String(b, "UTF-8");
        System.out.println(String.format("text:\n%s\npublic key:\n%s\nprivate key:\n%s\nencrypt:\n%s\nsignature:\n%s\nvalid:%s\ndecrypt:\n%s", 
            text, CryptUtil.base64Encode(pub.getEncoded()), CryptUtil.base64Encode(priv.getEncoded()), 
            CryptUtil.base64Encode(b), CryptUtil.base64Encode(s), String.valueOf(v), de));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  protected static void test28() throws Exception {
    Security.addProvider(new BouncyCastleProvider());
    System.out.println("------列出加密服务提供者------");
    Provider[] pro=Security.getProviders();
    for(Provider p:pro) {
      System.out.println(String.format("Provider: %s; version: %s; info: %s", p.getName(), p.getVersion(), p.getInfo()));
    }
    System.out.println("");
    System.out.println("------列出系统支持的消息摘要算法------");
    for(String s:Security.getAlgorithms("MessageDigest")) {
      System.out.println(s);
    }
    System.out.println("------列出系统支持的生成公钥和私钥对的算法------");
    for(String s:Security.getAlgorithms("KeyPairGenerator")) {
      System.out.println(s);
    }
  }

  // 测试UserService
  protected static void test50() throws Exception {
    UserService us = Application.getBean(UserService.class);
    List<User> l = us.list();
    System.out.println(String.format("Total %d users in system!", l.size()));
    for (User u : l)
      System.out.println(u);
  }

  // add user
  protected static void test51() throws Exception {
    String salt = CryptUtil.genSalt();
    User u = new User("demo", "测试账号", salt, new Md5Hash("", salt).toBase64());
    UserService us = Application.getBean(UserService.class);
    us.add(u);
  }

/*
  // add user's roles
  protected static void test52() throws Exception {
    UserService us = Application.getBean(UserService.class);
    User u = us.get("test");
    us.addRoles(u, new Role[] { Role.user, Role.view, Role.admin, Role.poweruser });
    // us.addRole(u, Role.admin);
  }

  // remove user's roles
  protected static void test53() throws Exception {
    UserService us = Application.getBean(UserService.class);
    User u = us.get("test");
    // us.delRoles(u, new Role[]{Role.view, Role.admin});
    us.delRoles(u);
  }

  // get admin's count
  protected static void test54() throws Exception {
    UserService us = Application.getBean(UserService.class);
    System.out.println("admin count: " + us.countAdmin());
  }

  // shiro aop at @Service
  protected static void test55() throws Exception {
    UserService us = Application.getBean(UserService.class);
    UserService.login("admin", "123456");
    User u = us.get("admin");
    us.enable(u);
  }
*/

  // cache aop
  protected static void test56() throws Exception {
    System.out.println(new Md5Hash("123456", "DCBA").toBase64());
    UserService us = Application.getBean(UserService.class);
    List<User> l = us.list();
    User u = us.get("admin");
    System.out.println(l.size() + ": " + u);
    List<Object> caches = CacheUtil.list();
    System.out.println(caches.size());
  }

  // audit aop
  protected static void test60() throws Exception {
    FolderService fs = Application.getBean(FolderService.class);
    Folder f = new Folder("771327ED25FF4FFF9E6979B8CF75CCB9", "F000001", "苏教版");
    fs.add(f);
    f = new Folder(f.getId(), "F000002", "初一");
    fs.add(f);
    f = new Folder(f.getId(), "F000003", "物理");
    fs.add(f);
  }

  protected static void test61() throws Exception {
    System.out.println(validateTfn("787451725") ? "Y" : "N");
  }

  protected static boolean validateTfn(String tfn) {
    if (tfn == null || tfn.length() == 0)
      return true;

    if (tfn.matches("^[0-9]{9}$")) {
      if ("000000000".equals(tfn) || "111111111".equals(tfn) || "333333333".equals(tfn) || "444444444".equals(tfn)
          || "987654321".equals(tfn))
        return true;

      int t1 = Character.digit(tfn.charAt(0), 10);
      int t2 = Character.digit(tfn.charAt(1), 10);
      int t3 = Character.digit(tfn.charAt(2), 10);
      int t4 = Character.digit(tfn.charAt(3), 10);
      int t5 = Character.digit(tfn.charAt(4), 10);
      int t6 = Character.digit(tfn.charAt(5), 10);
      int t7 = Character.digit(tfn.charAt(6), 10);
      int t8 = Character.digit(tfn.charAt(7), 10);
      int t9 = Character.digit(tfn.charAt(8), 10);

      if ((t1 * 10 + t2 * 7 + t3 * 8 + t4 * 4 + t5 * 6 + t6 * 3 + t7 * 5 + t8 * 2 + t9 * 1) % 11 == 0)
        return true;
    }

    return false;
  }

  protected static void test62() throws Exception {
    String s = "E47010F2275657C7E0435360F00A1716";
    
  }

  protected static void test63() throws Exception {
    String dir = System.getProperty("user.dir");
    H2_SERVER_ARGS[H2_SERVER_ARGS.length-1] = dir + "/data";
    Server.main(H2_SERVER_ARGS);
  }

  public static void main(String[] argv) throws Exception {
    try {
      // test28();
      // test01(); System.exit(0);
      // Mocker.init("test");
      // Mocker.init();
      // UserService.login("admin", "admin");
      // test51();
    	test63();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // System.exit(0);
  }

}
