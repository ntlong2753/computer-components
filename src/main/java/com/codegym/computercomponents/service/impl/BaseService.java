package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Product;
import com.codegym.computercomponents.repository.ProductImageRepository;
import com.codegym.computercomponents.service.IBaseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class BaseService<T extends Product, R extends JpaRepository<T, Long>> implements IBaseService<T> {

    protected final R repository;
    protected final ProductImageRepository productImageRepository;

    public BaseService(R repository, ProductImageRepository productImageRepository) {
        this.repository = repository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với id = " + id));
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productImageRepository.deleteAll(productImageRepository.findByProductId(id));
        repository.deleteById(id);
    }
}
