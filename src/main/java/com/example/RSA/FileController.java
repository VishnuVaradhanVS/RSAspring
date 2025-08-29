package com.example.RSA;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;

@RestController
@RequestMapping("/file")
public class FileController {
    private static final String UPLOAD_DIR = "uploads/";

    // Upload & encrypt
    @PostMapping("/encrypt")
    public ResponseEntity<String> encryptFile(@RequestParam("file") MultipartFile file) {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String encryptedPath = UPLOAD_DIR + file.getOriginalFilename() + ".enc";

            byte[] encrypted = RSAUtil.encrypt(file.getBytes());
            Files.write(Paths.get(encryptedPath), encrypted);

            return ResponseEntity.ok("File encrypted: " + encryptedPath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Encryption failed: " + e.getMessage());
        }
    }

    // Decrypt & save
    @PostMapping("/decrypt")
    public ResponseEntity<String> decryptFile(@RequestParam("file") MultipartFile file) {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String decryptedPath = UPLOAD_DIR + "decrypted_" + file.getOriginalFilename().replace(".enc", ".txt");

            byte[] decrypted = RSAUtil.decrypt(file.getBytes());
            Files.write(Paths.get(decryptedPath), decrypted);

            return ResponseEntity.ok("File decrypted: " + decryptedPath);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Decryption failed: " + e.getMessage());
        }
    }
}
