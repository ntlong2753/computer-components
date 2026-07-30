package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Cpu;
import com.codegym.computercomponents.model.CpuImage;
import com.codegym.computercomponents.repository.CpuImageRepository;
import com.codegym.computercomponents.repository.CpuRepository;
import com.codegym.computercomponents.service.ICpuImageService;
import com.codegym.computercomponents.service.IFileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CpuImageServiceImpl implements ICpuImageService {

    private final CpuImageRepository cpuImageRepository;
    private final CpuRepository cpuRepository;
    private final IFileUploadService fileUploadService;
    private static final int MAX_IMAGES_PER_CPU = 10;

    public CpuImageServiceImpl(CpuImageRepository cpuImageRepository, 
                               CpuRepository cpuRepository, 
                               IFileUploadService fileUploadService) {
        this.cpuImageRepository = cpuImageRepository;
        this.cpuRepository = cpuRepository;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public List<CpuImage> getImagesByCpuId(Long cpuId) {
        return cpuImageRepository.findByCpuId(cpuId);
    }

    @Override
    public CpuImage addImageToCpu(Long cpuId, MultipartFile file) {
        return addImagesToCpu(cpuId, List.of(file)).stream().findFirst().orElse(null);
    }

    @Override
    public List<CpuImage> addImagesToCpu(Long cpuId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return List.of();

        long validFilesCount = files.stream().filter(f -> !f.isEmpty()).count();
        if (validFilesCount == 0) return List.of();

        // 1. Check max images limit
        long currentImageCount = cpuImageRepository.countByCpuId(cpuId);
        if (currentImageCount + validFilesCount > MAX_IMAGES_PER_CPU) {
            throw new RuntimeException("Một CPU chỉ được phép có tối đa " + MAX_IMAGES_PER_CPU + 
                " ảnh. (Hiện có " + currentImageCount + ", đang thêm " + validFilesCount + ")");
        }

        // 2. Validate Cpu exists
        Cpu cpu = cpuRepository.findById(cpuId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy CPU với ID: " + cpuId));

        // 3. Store files and save to database
        List<CpuImage> savedImages = new java.util.ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String filePath = fileUploadService.storeFile(file);
                CpuImage cpuImage = CpuImage.builder()
                        .imageUrl(filePath)
                        .cpu(cpu)
                        .build();
                savedImages.add(cpuImageRepository.save(cpuImage));
            }
        }
        return savedImages;
    }

    @Override
    public void deleteImage(Long imageId) {
        cpuImageRepository.deleteById(imageId);
    }
}
