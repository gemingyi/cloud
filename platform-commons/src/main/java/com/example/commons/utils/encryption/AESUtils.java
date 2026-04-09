package com.example.commons.utils.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtils {


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


    public static void main(String[] args) throws Exception {
        // AES
        String key = AESUtils.generateKey();
        String text = "123abc";
        String encrypt = AESUtils.encrypt(text, key);
        String decrypt = AESUtils.decrypt(encrypt, key);
        System.out.println(decrypt);
    }
}
