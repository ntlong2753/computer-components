package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.model.Cpu;
import com.codegym.computercomponents.service.impl.CpuService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.codegym.computercomponents.service.ICpuImageService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/admin/cpu")
public class CpuController {
    private final CpuService cpuService;
    private final ICpuImageService cpuImageService;

    public CpuController(CpuService cpuService, ICpuImageService cpuImageService) {
        this.cpuService = cpuService;
        this.cpuImageService = cpuImageService;
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

    @PostMapping("/{id}/images")
    public String uploadImage(@PathVariable Long id, 
                              @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn một file để tải lên.");
            return "redirect:/admin/cpu";
        }
        try {
            cpuImageService.addImageToCpu(id, file);
            redirectAttributes.addFlashAttribute("message", "Tải ảnh lên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/cpu";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> getCpuApi(@PathVariable Long id) {
        Cpu cpu = cpuService.findById(id);
        if (cpu == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cpu);
    }

    @PostMapping("/api/save")
    @ResponseBody
    public ResponseEntity<?> saveAjax(@Valid @ModelAttribute Cpu cpu, 
                                      BindingResult result,
                                      @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Cpu savedCpu = cpuService.save(cpu);

            // Delegate image bulk upload to the service
            if (files != null && !files.isEmpty()) {
                cpuImageService.addImagesToCpu(savedCpu.getId(), files);
            }
            return ResponseEntity.ok(savedCpu);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("general", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
