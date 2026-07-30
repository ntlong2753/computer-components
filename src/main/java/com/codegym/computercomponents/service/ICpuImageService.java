package com.codegym.computercomponents.service;

import com.codegym.computercomponents.model.CpuImage;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ICpuImageService {
    List<CpuImage> getImagesByCpuId(Long cpuId);
    CpuImage addImageToCpu(Long cpuId, MultipartFile file);
    List<CpuImage> addImagesToCpu(Long cpuId, List<MultipartFile> files);
    void deleteImage(Long imageId);
}
