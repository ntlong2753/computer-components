package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Brand;
import com.codegym.computercomponents.repository.BrandRepository;
import com.codegym.computercomponents.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand findById(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }

    @Override
    public boolean existsByNameAndCategoryId(String name, Long categoryId) {
        return brandRepository.existsByNameAndCategoryId(name, categoryId);
    }

    @Override
    public List<Brand> findByCategoryId(Long categoryId) {
        return brandRepository.findByCategoryId(categoryId);
    }
}
