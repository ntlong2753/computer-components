package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.model.Product;
import com.codegym.computercomponents.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private IProductService productService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "index";
    }
}
