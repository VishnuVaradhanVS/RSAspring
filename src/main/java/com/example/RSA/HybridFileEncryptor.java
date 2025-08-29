package com.example.RSA;

import com.example.RSA.AESUtil;
import com.example.RSA.RSAUtil;

import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.io.*;
import java.nio.file.*;
import java.security.PublicKey;
import java.security.PrivateKey;

public class HybridFileEncryptor {
    // Encrypt file with AES + RSA
    public static void encryptFile(String inputPath, String outputPath, String keyPath) throws Exception {
        byte[] fileData = Files.readAllBytes(Paths.get(inputPath));

        // 1. Generate AES key
        SecretKey aesKey = AESUtil.generateAESKey();

        // 2. Encrypt file with AES
        byte[] encryptedData = AESUtil.encrypt(fileData, aesKey);

        // 3. Encrypt AES key with RSA public key
        PublicKey publicKey = RSAUtil.getPublicKey();
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedKey = rsaCipher.doFinal(aesKey.getEncoded());

        // Save encrypted file
        Files.write(Paths.get(outputPath), encryptedData);

        // Save encrypted AES key separately
        Files.write(Paths.get(keyPath), encryptedKey);
    }

    // Decrypt file
    public static void decryptFile(String encryptedPath, String keyPath, String outputPath) throws Exception {
        byte[] encryptedData = Files.readAllBytes(Paths.get(encryptedPath));
        byte[] encryptedKey = Files.readAllBytes(Paths.get(keyPath));

        // 1. Decrypt AES key with RSA private key
        PrivateKey privateKey = RSAUtil.getPrivateKey();
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] aesKeyBytes = rsaCipher.doFinal(encryptedKey);
        SecretKey aesKey = AESUtil.fromBytes(aesKeyBytes);

        // 2. Decrypt file with AES
        byte[] decryptedData = AESUtil.decrypt(encryptedData, aesKey);

        // Save file
        Files.write(Paths.get(outputPath), decryptedData);
    }
}

