package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class FileServiceImpl implements FileService {

    @Value("${upload.path:uploads/}")
    private String uploadPath;

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Tính mã Hash SHA-256 từ nội dung ảnh để phát hiện ảnh trùng lặp
        String hash;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream is = file.getInputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }
            byte[] hashBytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Không tìm thấy thuật toán hash SHA-256", e);
        }

        String newFilename = hash + extension;
        Path targetPath = Paths.get(uploadPath).resolve(newFilename);

        // Nếu file ảnh có cùng mã hash đã tồn tại trong thư mục, không tạo mới file nữa
        if (!Files.exists(targetPath)) {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        return "/images/" + newFilename;
    }
}
