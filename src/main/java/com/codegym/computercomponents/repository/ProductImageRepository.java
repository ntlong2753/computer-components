package com.codegym.computercomponents.repository;

import com.codegym.computercomponents.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
    
    long countByProductId(Long productId);
}
