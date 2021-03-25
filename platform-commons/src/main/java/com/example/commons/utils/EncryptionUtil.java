package com.example.commons.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * @description: RSAUtil
 * @author: mingyi ge
 * @date: 2021/1/4 14:43
 */
public class EncryptionUtil {

    /**
     * AES 对称加密
     */
    static class AES {

        private static final String ALGORITHM = "AES";

        public static final Integer AES_KEY_SIZE = 128;

        /**
         * 生成秘钥
         */
        public static String generateKey() {
            KeyGenerator keygen = null;
            try {
                keygen = KeyGenerator.getInstance(ALGORITHM);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            // 16 字节 == 128 bit
            keygen.init(AES_KEY_SIZE, new SecureRandom());
            SecretKey secretKey = keygen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }

        /**
         * 生成密钥
         */
        private static SecretKeySpec getSecretKeySpec(String secretKeyStr) {
            return new SecretKeySpec(Base64.getDecoder().decode(secretKeyStr), ALGORITHM);
        }

        /**
         * 加密
         */
        public static String encrypt(String content, String secretKey) throws Exception {
            Key key = getSecretKeySpec(secretKey);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));
        }

        /**
         * 解密
         */
        public static String decrypt(String content, String secretKey) throws Exception {
            Key key = getSecretKeySpec(secretKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(content)), StandardCharsets.UTF_8);
        }

        /**
         * 解密
         */
        public static String decryptWithUrlDecoder(String content, String secretKey) throws Exception {
            Key key = getSecretKeySpec(secretKey);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getUrlDecoder().decode(content.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        }
    }


    /**
     * RSA 对称加密
     */
    static class RSA {
        public static final Integer RSA_KEY_SIZE = 1024;

        public static final String KEY_ALGORITHM = "RSA";
        public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

        private static final String PUBLIC_KEY = "RSAPublicKey";
        private static final String PRIVATE_KEY = "RSAPrivateKey";


        /**
         * 加密
         *
         * @param str 代加密字符串
         * @param key 密钥
         * @return 加密后的内容
         */
        public static String encrypt(String str, Key key) throws Exception {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return encryptBASE64(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        }

        /**
         * 解密
         *
         * @param str 代解密字符串
         * @param key 密钥
         * @return 解密后的内容
         */
        public static String decrypt(String str, Key key) throws Exception {
            byte[] inputByte = decryptBASE64(str);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(inputByte));
        }

        //-------------------------------------------------------------------


        /**
         * 私钥生成签名
         *
         * @param str        str
         * @param privateKey 私钥
         * @return 签名
         */
        public static String privateSinge(String str, PrivateKey privateKey) throws Exception {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] signed = signature.sign();
            return encryptBASE64(signed);
        }

        /**
         * 公钥验证
         *
         * @param str       str
         * @param sign      签名
         * @param publicKey 公钥
         */
        public static boolean publicVerify(String str, String sign, PublicKey publicKey) {
            try {
                Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
                signature.initVerify(publicKey);
                signature.update(str.getBytes(StandardCharsets.UTF_8));
                return signature.verify(decryptBASE64(sign));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }


        /**
         * 获取PublicKey格式的公钥
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
         * 获取PrivateKey格式的公钥
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
         * 初始化公钥和私钥
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
    }


    public static void main(String[] args) throws Exception {
        //RSA
        for (int i = 0; i < 1; i++) {
            Map<String, Object> keyMap = RSA.initKey();
            String publicKeyStr = RSA.getPublicKeyStr(keyMap);
            System.out.println("JAVA生成RSA公钥：" + publicKeyStr);
            String privateKeyStr = RSA.getPrivateKeyStr(keyMap);
            System.out.println("JAVA生成RSA私钥：" + privateKeyStr);

            //
            PublicKey publicKey = RSA.getPublicKey(publicKeyStr);
            PrivateKey privateKey = RSA.getPrivateKey(privateKeyStr);

            // 加密测试
            String text = "123abc";
            String s = RSA.encrypt(text, publicKey);
            String s1 = RSA.decrypt(s, privateKey);
            System.out.println(s1);

            // 加密测试2
            String text2 = "123abc";
            String ss = RSA.encrypt(text2, privateKey);
            String ss1 = RSA.decrypt(ss, publicKey);
            System.out.println(ss1);

            // 加签测试
            String str = "aaabbbccc";
            String singe = RSA.privateSinge(str, privateKey);
            boolean b = RSA.publicVerify(str, singe, publicKey);
            System.out.println(b);
        }


        // AES
        String key = AES.generateKey();
        String text = "123abc";
        String encrypt = AES.encrypt(text, key);
        String decrypt = AES.decrypt(encrypt, key);
        System.out.println(decrypt);
    }

}
