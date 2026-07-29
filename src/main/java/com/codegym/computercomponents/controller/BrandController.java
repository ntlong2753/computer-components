package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.model.Brand;
import com.codegym.computercomponents.model.Category;
import com.codegym.computercomponents.service.BrandService;
import com.codegym.computercomponents.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/brand")
public class BrandController {
    private final BrandService brandService;
    private final CategoryService categoryService;

    public BrandController(BrandService brandService, CategoryService categoryService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("brand", new Brand());
        model.addAttribute("categories", categoryService.findAll());
        return "brand/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("brand") Brand brand, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (brand.getName() != null && brand.getName().trim().isEmpty()) {
            result.rejectValue("name", "brand.name.empty", "Tên hãng không được để trống hoặc chỉ chứa khoảng trắng");
        }
        
        if (brand.getCategory() == null || brand.getCategory().getId() == null) {
            result.rejectValue("category", "brand.category.empty", "Vui lòng chọn danh mục sản phẩm");
        }

        if (!result.hasErrors() && brandService.existsByNameAndCategoryId(brand.getName().trim(), brand.getCategory().getId())) {
            result.rejectValue("name", "brand.name.duplicate", "Hãng sản phẩm này đã tồn tại trong danh mục đã chọn");
        }

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "brand/form";
        }
        
        brand.setName(brand.getName().trim());
        brandService.save(brand);
        
        redirectAttributes.addFlashAttribute("successMessage", "Thêm hãng sản phẩm thành công");
        return "redirect:/admin/brand/create";
    }
}
