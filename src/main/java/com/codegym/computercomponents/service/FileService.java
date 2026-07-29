package com.codegym.computercomponents.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileService {
    String storeFile(MultipartFile file) throws IOException;
}
