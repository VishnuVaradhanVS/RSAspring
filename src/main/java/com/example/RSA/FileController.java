package com.example.RSA;

import com.example.RSA.HybridFileEncryptor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;

@RestController
@RequestMapping("/secure-file")
public class FileController {
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestParam("file") MultipartFile file) {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String inputPath = UPLOAD_DIR + file.getOriginalFilename();
            String encryptedPath = UPLOAD_DIR + file.getOriginalFilename() + ".enc";
            String keyPath = UPLOAD_DIR + file.getOriginalFilename() + ".key";

            Files.write(Paths.get(inputPath), file.getBytes());

            HybridFileEncryptor.encryptFile(inputPath, encryptedPath, keyPath);

            return ResponseEntity.ok("Encrypted file: " + encryptedPath + "\nKey file: " + keyPath);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestParam("encryptedFile") MultipartFile encFile,
                                          @RequestParam("keyFile") MultipartFile keyFile) {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String encryptedPath = UPLOAD_DIR + encFile.getOriginalFilename();
            String keyPath = UPLOAD_DIR + keyFile.getOriginalFilename();
            String outputPath = UPLOAD_DIR + "decrypted_" + encFile.getOriginalFilename().replace(".enc", "");

            Files.write(Paths.get(encryptedPath), encFile.getBytes());
            Files.write(Paths.get(keyPath), keyFile.getBytes());

            HybridFileEncryptor.decryptFile(encryptedPath, keyPath, outputPath);

            return ResponseEntity.ok("Decrypted file: " + outputPath);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
