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

    public CpuController(CpuService cpuService, com.codegym.computercomponents.service.FileService fileService) {
        this.cpuService = cpuService;
        this.fileService = fileService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("cpus", cpuService.findAll());
        return "cpu/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("cpu", new Cpu());
        return "cpu/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("cpu") Cpu cpu, BindingResult result, 
                       @org.springframework.web.bind.annotation.RequestParam(value = "imageFile", required = false) org.springframework.web.multipart.MultipartFile imageFile) {
        if (result.hasErrors()) {
            return "cpu/form";
        }
        
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = fileService.storeFile(imageFile);
                cpu.setImageUrl(imageUrl);
            } else if (cpu.getId() != null) {
                Cpu existingCpu = cpuService.findById(cpu.getId());
                if (existingCpu != null) {
                    cpu.setImageUrl(existingCpu.getImageUrl());
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            result.rejectValue("imageUrl", "error.cpu", "Không thể tải lên hình ảnh");
            return "cpu/form";
        }

        cpuService.save(cpu);
        return "redirect:/admin/cpu";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("cpu", cpuService.findById(id));
        return "cpu/form";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        cpuService.deleteById(id);
        return "redirect:/admin/cpu";
    }
}
