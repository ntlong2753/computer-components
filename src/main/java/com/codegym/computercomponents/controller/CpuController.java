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

    public CpuController(CpuService cpuService) {
        this.cpuService = cpuService;
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
    public String save(@Valid @ModelAttribute("cpu") Cpu cpu, BindingResult result) {
        if (result.hasErrors()) {
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
