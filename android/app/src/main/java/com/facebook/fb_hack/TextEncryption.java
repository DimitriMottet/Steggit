package com.facebook.fb_hack;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class TextEncryption {

    public static byte[] encrypt(String valueToEnc, String stringKey) {
        try {
            Key key = generateKey(stringKey);
            Cipher c = Cipher.getInstance("AES");
            System.out.println("Antoine");
            c.init(Cipher.ENCRYPT_MODE, key);
            return c.doFinal(valueToEnc.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(byte[] encryptedValue, String stringKey) {
        try {
            Key key = generateKey(stringKey);
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decValue = c.doFinal(encryptedValue);
            return new String(decValue);
        } catch (Exception e) {
            return null;
        }
    }

    private static Key generateKey(String stringKey) throws Exception {
        byte[] keyValue = stringKey.getBytes();
        Key key = new SecretKeySpec(keyValue, "AES");
        return key;
    }

}
