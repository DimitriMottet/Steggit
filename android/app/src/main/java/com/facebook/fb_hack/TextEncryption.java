package com.facebook.fb_hack;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class TextEncryption {

    public static byte[] encrypt(String valueToEnc, String stringKey) {
        try {
            Key key = generateKey(passwordToKey(stringKey));
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, key);
            return c.doFinal(valueToEnc.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(byte[] encryptedValue, String stringKey) {
        try {
            Key key = generateKey(passwordToKey(stringKey));
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

    private static String passwordToKey(String password)
    {
        String key = password;
        if (key == "")
            key = "a";
        if (key.length() <= 8)
            key += key;
        for(int i = key.length(); i < 16; i++) {
            key = key + key.charAt(key.length() - 1);
        }

        if (key.length() > 16)
            key = key.substring(0, 16);

        return key;
    }

}
