package com.example.commons.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


/**
 * @description:    非对称加密 RSA工具类
 * @author: mingyi ge
 * @date: 2021/1/4 14:43
 */
public class SignatureUtil {
    public static final String KEY_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
    public static final Integer RSA_KEY_SIZE = 1024;


    /**
     * 公钥加密
     * @param str 待加密字符串
     * @param publicKey 公钥
     */
    public static String publicEncrypt(String str, PublicKey publicKey) throws Exception {
        //base64编码的公钥
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return encryptBASE64(cipher.doFinal(str.getBytes("UTF-8")));
    }

    /**
     * 私钥解密
     * @param str   被加密的字符串
     * @param privateKey    私钥
     */
    public static String privateDecrypt(String str, PrivateKey privateKey) throws Exception {
        //base64解码加密后的字符串
        byte[] inputByte = decryptBASE64(str);
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(inputByte));
    }

    //-------------------------------------------------------------------


    /**
     * 私钥生成签名
     * @param str   str
     * @param privateKey    私钥
     * @return  签名
     */
    public static String privateSinge(String str, PrivateKey privateKey) throws Exception {
        java.security.Signature signature = java.security.Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(str.getBytes("UTF-8"));
        byte[] signed = signature.sign();
        return encryptBASE64(signed);
    }

    /**
     * 公钥验证签名
     * @param str   str
     * @param sign  签名
     * @param publicKey 公钥
     */
    public static boolean publicCheck(String str, String sign, PublicKey publicKey) throws Exception {
        try {
            java.security.Signature signature = java.security.Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(str.getBytes("UTF-8"));
            return signature.verify(decryptBASE64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }




    /**
     * @function: 获取PublicKey格式的公钥，本例中未使用
     * @param: [key]
     * @return: java.security.PublicKey
     * @auther: chengjunyu
     * @date: 2019/12/8 16:10
     */
    public static PublicKey getPublicKey(String key) {
        PublicKey publicKey = null;
        try {
            byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            // RSA对称加密算法
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            // 取公钥匙对象
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return publicKey;
    }


    /**
     * @function: 获取PublicKey格式的私钥，本例中未使用
     * @param: [key]
     * @return: java.security.PrivateKey
     * @auther: chengjunyu
     * @date: 2019/12/8 16:10
     */
    public static PrivateKey getPrivateKey(String key) {
        PrivateKey privateKey = null;
        try {
            byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * @function: 初始化公钥和私钥
     * @param: []
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     * @auther: chengjunyu
     * @date: 2019/12/8 14:34
     */
    public static Map<String, Object> initKey() {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(RSA_KEY_SIZE);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    //获得公钥字符串
    public static String getPublicKeyStr(Map<String, Object> keyMap) {
        //获得map中的公钥对象 转为key对象
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    //获得私钥字符串
    public static String getPrivateKeyStr(Map<String, Object> keyMap) {
        //获得map中的私钥对象 转为key对象
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }


    //编码返回字符串
    public static String encryptBASE64(byte[] key) {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    //解码返回byte
    public static byte[] decryptBASE64(String key) {
        byte[] bytes = null;
        try {
            return (new BASE64Decoder()).decodeBuffer(key);
        } catch (IOException e) {
            return bytes;
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 4; i++) {
            Map<String, Object> keyMap = initKey();
            String publicKeyStr = getPublicKeyStr(keyMap);
            System.out.println("JAVA生成RSA公钥：" + publicKeyStr);
            String privateKeyStr = getPrivateKeyStr(keyMap);
            System.out.println("JAVA生成RSA私钥：" + privateKeyStr);

            //
            PublicKey publicKey = getPublicKey(publicKeyStr);
            PrivateKey privateKey = getPrivateKey(privateKeyStr);

            // 加密测试
            String text = "123abc";
            String s = publicEncrypt(text, publicKey);
            String s1 = privateDecrypt(s, privateKey);
            System.out.println();

            // 加签测试
            String str = "aaabbbccc";
            String singe = privateSinge(str, privateKey);
            boolean b = publicCheck(str, singe, publicKey);
            System.out.println();
        }
    }

}
