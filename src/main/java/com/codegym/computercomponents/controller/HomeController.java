package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.model.Product;
import com.codegym.computercomponents.service.IProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final IProductService productService;

    @GetMapping({"/", "/home"})
    public String home(
            @org.springframework.web.bind.annotation.RequestParam(name = "category", required = false) String category,
            @org.springframework.web.bind.annotation.RequestParam(name = "keyword", required = false) String keyword,
            @org.springframework.web.bind.annotation.RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {
        
        if ((category != null && !category.isEmpty()) || (keyword != null && !keyword.isEmpty())) {
            Pageable pageable = PageRequest.of(page, 10); // 10 products per page
            Page<Product> productPage = productService.findByCategoryAndKeyword(category, keyword, pageable);
            model.addAttribute("productPage", productPage);
            
            if (category != null && !category.isEmpty()) {
                model.addAttribute("currentCategory", category);
            } else {
                model.addAttribute("currentCategory", "Kết quả tìm kiếm cho: " + keyword);
            }
            
            if (keyword != null && !keyword.isEmpty()) {
                model.addAttribute("currentKeyword", keyword);
            }
        } else {
            List<Product> allProducts = productService.findAll();
            java.util.Map<String, List<Product>> productsByCategory = allProducts.stream()
                .collect(java.util.stream.Collectors.groupingBy(Product::getCategoryName));
                
            for (java.util.Map.Entry<String, List<Product>> entry : productsByCategory.entrySet()) {
                List<Product> list = entry.getValue();
                if (list.size() > 5) {
                    entry.setValue(list.subList(0, 5));
                }
            }
            model.addAttribute("productsByCategory", productsByCategory);
        }

        return "index";
    }
}
