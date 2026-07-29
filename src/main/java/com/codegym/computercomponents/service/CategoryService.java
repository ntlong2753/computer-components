package com.codegym.computercomponents.service;

import com.codegym.computercomponents.model.Category;
import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    boolean existsByName(String name);
}
