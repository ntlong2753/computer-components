package com.codegym.computercomponents.repository;

import com.codegym.computercomponents.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
