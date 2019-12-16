package com.gram.landlord_client.sdk.socket.util;

import android.util.Base64;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtils {

    public static String DESAndBase64Encrypt(String msg, String key, String iv) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "DES"), new IvParameterSpec(iv.getBytes()));
            byte[] encrypted = cipher.doFinal(msg.getBytes());
            return new String(Base64.encode(encrypted, Base64.DEFAULT), Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] DESEncrypt(byte[] msg, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key,"DES"), new IvParameterSpec(iv));
            return cipher.doFinal(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String DESAndBase64Decrypt(String decryptStr, String key, String iv) {
        try {
            byte[] decryptBytes = Base64.decode(decryptStr.getBytes(Charset.forName("UTF-8")), Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "DES"), new IvParameterSpec(iv.getBytes()));
            byte[] original = cipher.doFinal(decryptBytes);
            return new String(original, Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] DESDecrypt(byte[] decryptBytes, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DES"), new IvParameterSpec(iv));
            return cipher.doFinal(decryptBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String clearText = "田所浩二喜欢蔡徐坤";
        //key和iv的长度必须是8
        String key = "12345678";
        String iv = "87654321";
        System.out.println("明文: " + clearText + "\n密钥: " + key);
        String encryptText = DESAndBase64Encrypt(clearText, key, iv);
        System.out.println("加密后: " + encryptText);
        String decryptText = DESAndBase64Decrypt(encryptText, key, iv);
        System.out.println("解密后: " + decryptText);
    }
}
