package com.codegym.computercomponents.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadService {
    /**
     * Store a file and return its path.
     * @param file the file to store
     * @return the relative path to the saved file
     */
    String storeFile(MultipartFile file);
}
