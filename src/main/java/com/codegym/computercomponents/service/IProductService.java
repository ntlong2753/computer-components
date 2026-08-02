package com.codegym.computercomponents.service;

import com.codegym.computercomponents.model.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IProductService {
    List<Product> findAll();
    Product findById(Long id);
    Page<Product> findByCategory(String category, Pageable pageable);
}
