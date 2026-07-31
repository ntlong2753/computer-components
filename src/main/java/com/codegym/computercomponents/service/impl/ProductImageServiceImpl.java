package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Product;
import com.codegym.computercomponents.model.ProductImage;
import com.codegym.computercomponents.repository.ProductImageRepository;
import com.codegym.computercomponents.repository.ProductRepository;
import com.codegym.computercomponents.service.IProductImageService;
import com.codegym.computercomponents.service.IFileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductImageServiceImpl implements IProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final IFileUploadService fileUploadService;
    private static final int MAX_IMAGES_PER_PRODUCT = 10;

    public ProductImageServiceImpl(ProductImageRepository productImageRepository, 
                                   ProductRepository productRepository, 
                                   IFileUploadService fileUploadService) {
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public List<ProductImage> getImagesByProductId(Long productId) {
        return productImageRepository.findByProductId(productId);
    }

    @Override
    public ProductImage addImageToProduct(Long productId, MultipartFile file) {
        return addImagesToProduct(productId, List.of(file)).stream().findFirst().orElse(null);
    }

    @Override
    public List<ProductImage> addImagesToProduct(Long productId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return List.of();

        long validFilesCount = files.stream().filter(f -> !f.isEmpty()).count();
        if (validFilesCount == 0) return List.of();

        // 1. Check max images limit
        long currentImageCount = productImageRepository.countByProductId(productId);
        if (currentImageCount + validFilesCount > MAX_IMAGES_PER_PRODUCT) {
            throw new RuntimeException("Một sản phẩm chỉ được phép có tối đa " + MAX_IMAGES_PER_PRODUCT + 
                " ảnh. (Hiện có " + currentImageCount + ", đang thêm " + validFilesCount + ")");
        }

        // 2. Validate Product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Sản phẩm với ID: " + productId));

        // 3. Store files and save to database
        List<ProductImage> savedImages = new java.util.ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String filePath = fileUploadService.storeFile(file);
                ProductImage productImage = ProductImage.builder()
                        .imageUrl(filePath)
                        .product(product)
                        .build();
                savedImages.add(productImageRepository.save(productImage));
            }
        }
        return savedImages;
    }

    @Override
    public void deleteImage(Long imageId) {
        productImageRepository.deleteById(imageId);
    }
}
