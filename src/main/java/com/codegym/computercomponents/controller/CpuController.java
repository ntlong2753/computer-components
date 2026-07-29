package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.model.Cpu;
import com.codegym.computercomponents.service.impl.CpuService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/cpu")
public class CpuController {
    private final CpuService cpuService;
    private final com.codegym.computercomponents.service.FileService fileService;
    private final com.codegym.computercomponents.service.BrandService brandService;

    public CpuController(CpuService cpuService, 
                         com.codegym.computercomponents.service.FileService fileService,
                         com.codegym.computercomponents.service.BrandService brandService) {
        this.cpuService = cpuService;
        this.fileService = fileService;
        this.brandService = brandService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("cpus", cpuService.findAll());
        return "cpu/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("cpu", new Cpu());
        model.addAttribute("brands", brandService.findAll());
        return "cpu/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("cpu") Cpu cpu, BindingResult result, Model model,
                       @org.springframework.web.bind.annotation.RequestParam(value = "imageFiles", required = false) org.springframework.web.multipart.MultipartFile[] imageFiles) {
        if (result.hasErrors()) {
            model.addAttribute("brands", brandService.findAll());
            return "cpu/form";
        }
        
        try {
            // Get existing images if it's an update
            if (cpu.getId() != null) {
                Cpu existingCpu = cpuService.findById(cpu.getId());
                if (existingCpu != null && existingCpu.getImages() != null) {
                    cpu.setImages(existingCpu.getImages());
                }
            }

            // Count valid new images
            int newValidImagesCount = 0;
            if (imageFiles != null) {
                for (org.springframework.web.multipart.MultipartFile file : imageFiles) {
                    if (!file.isEmpty()) newValidImagesCount++;
                }
            }

            int totalImages = cpu.getImages().size() + newValidImagesCount;
            if (totalImages > 10) {
                result.rejectValue("images", "error.cpu", "Sản phẩm chỉ được phép có tối đa 10 ảnh.");
                model.addAttribute("brands", brandService.findAll());
                return "cpu/form";
            }

            if (imageFiles != null) {
                for (org.springframework.web.multipart.MultipartFile file : imageFiles) {
                    if (!file.isEmpty()) {
                        // Max 10MB per file validation is usually handled by Spring Multipart Config, 
                        // but we can double check here.
                        if (file.getSize() > 10 * 1024 * 1024) {
                            result.rejectValue("images", "error.cpu", "Kích thước mỗi ảnh tối đa là 10MB.");
                            model.addAttribute("brands", brandService.findAll());
                            return "cpu/form";
                        }
                        String imageUrl = fileService.storeFile(file);
                        com.codegym.computercomponents.model.ProductImage img = new com.codegym.computercomponents.model.ProductImage();
                        img.setImageUrl(imageUrl);
                        img.setProduct(cpu);
                        cpu.getImages().add(img);
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            result.rejectValue("images", "error.cpu", "Không thể tải lên hình ảnh");
            model.addAttribute("brands", brandService.findAll());
            return "cpu/form";
        }

        cpuService.save(cpu);
        return "redirect:/admin/cpu";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("cpu", cpuService.findById(id));
        model.addAttribute("brands", brandService.findAll());
        return "cpu/form";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        cpuService.deleteById(id);
        return "redirect:/admin/cpu";
    }
}
