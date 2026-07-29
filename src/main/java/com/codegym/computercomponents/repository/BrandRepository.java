package com.codegym.computercomponents.repository;

import com.codegym.computercomponents.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByNameAndCategoryId(String name, Long categoryId);
    List<Brand> findByCategoryId(Long categoryId);
}
