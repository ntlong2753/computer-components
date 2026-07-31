package com.codegym.computercomponents.service;

import com.codegym.computercomponents.model.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductImageService {
    List<ProductImage> getImagesByProductId(Long productId);
    ProductImage addImageToProduct(Long productId, MultipartFile file);
    List<ProductImage> addImagesToProduct(Long productId, List<MultipartFile> files);
    void deleteImage(Long imageId);
}
