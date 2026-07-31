package com.codegym.computercomponents.service;

import com.codegym.computercomponents.model.Product;
import java.util.List;

public interface IBaseService<T extends Product> {
    List<T> findAll();
    T findById(Long id);
    T save(T entity);
    void deleteById(Long id);
}
