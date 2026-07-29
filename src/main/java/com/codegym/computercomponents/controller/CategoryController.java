package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.model.Category;
import com.codegym.computercomponents.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        Map<String, Object> response = new HashMap<>();

        if (name == null || name.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Tên danh mục không được để trống");
            return ResponseEntity.badRequest().body(response);
        }

        name = name.trim();
        if (categoryService.existsByName(name)) {
            response.put("success", false);
            response.put("message", "Danh mục này đã tồn tại");
            return ResponseEntity.badRequest().body(response);
        }

        Category category = new Category();
        category.setName(name);
        category = categoryService.save(category);

        response.put("success", true);
        response.put("category", category);
        return ResponseEntity.ok(response);
    }
}
