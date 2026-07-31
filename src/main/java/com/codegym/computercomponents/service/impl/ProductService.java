package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Product;
import com.codegym.computercomponents.repository.ProductRepository;
import com.codegym.computercomponents.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
