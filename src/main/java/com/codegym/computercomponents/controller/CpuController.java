package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.model.Cpu;
import com.codegym.computercomponents.service.impl.CpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
