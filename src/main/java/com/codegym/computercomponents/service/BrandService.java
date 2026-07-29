package com.codegym.computercomponents.service;

import com.codegym.computercomponents.model.Brand;
import java.util.List;

public interface BrandService {
    List<Brand> findAll();
    Brand findById(Long id);
    Brand save(Brand brand);
    void deleteById(Long id);
    boolean existsByNameAndCategoryId(String name, Long categoryId);
    List<Brand> findByCategoryId(Long categoryId);
}
