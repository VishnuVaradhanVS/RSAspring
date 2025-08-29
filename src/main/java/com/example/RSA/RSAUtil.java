package com.example.RSA;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

public class RSAUtil {
    private static KeyPair keyPair;

    // Generate keys once at startup
    static {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            keyPair = keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Failed to init RSA", e);
        }
    }

    public static PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public static PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public static byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
        return cipher.doFinal(data);
    }

    // For testing with strings
    public static String encryptText(String text) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(text.getBytes()));
    }

    public static String decryptText(String encrypted) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(encrypted);
        return new String(decrypt(decoded));
    }
}
