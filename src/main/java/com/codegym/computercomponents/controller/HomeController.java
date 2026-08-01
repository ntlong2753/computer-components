package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.model.Product;
import com.codegym.computercomponents.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final IProductService productService;

    @GetMapping({"/", "/home"})
    public String home(@org.springframework.web.bind.annotation.RequestParam(name = "category", required = false) String category, Model model) {
        List<Product> allProducts = productService.findAll();
        
        if (category != null && !category.isEmpty()) {
            List<Product> filteredProducts = allProducts.stream()
                .filter(p -> p.getClass().getSimpleName().equalsIgnoreCase(category))
                .collect(java.util.stream.Collectors.toList());
            model.addAttribute("products", filteredProducts);
            model.addAttribute("currentCategory", category);
        } else {
            java.util.Map<String, List<Product>> productsByCategory = allProducts.stream()
                .collect(java.util.stream.Collectors.groupingBy(p -> p.getClass().getSimpleName()));
                
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
