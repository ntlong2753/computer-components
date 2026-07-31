package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.model.Product;
import com.codegym.computercomponents.model.ProductImage;
import com.codegym.computercomponents.service.IBaseService;
import com.codegym.computercomponents.service.IProductImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseProductController<T extends Product> {

    protected final IBaseService<T> service;
    protected final IProductImageService productImageService;

    public BaseProductController(IBaseService<T> service, IProductImageService productImageService) {
        this.service = service;
        this.productImageService = productImageService;
    }

    // Các phương thức abstract để lớp con cung cấp thông tin đặc thù
    protected abstract String getViewPrefix(); // Trả về "cpu", "vga", "ram"
    protected abstract String getModelName(); // Trả về "cpu", "vga", "ram"
    protected abstract Object createEmptyDto(); // Trả về new CpuDto(), new VgaDto()...
    protected abstract Object convertToDto(T entity); // Trả về CpuDto.fromEntity(entity)...

    @GetMapping
    public String list(Model model) {
        model.addAttribute(getModelName() + "s", service.findAll());
        return getViewPrefix() + "/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute(getModelName(), createEmptyDto());
        return getViewPrefix() + "/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        T entity = service.findById(id);
        model.addAttribute(getModelName(), convertToDto(entity));
        return getViewPrefix() + "/form";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return "redirect:/admin/" + getViewPrefix();
    }

    @PostMapping("/{id}/images")
    public String uploadImage(@PathVariable("id") Long id,
                              @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn một file để tải lên.");
            return "redirect:/admin/" + getViewPrefix();
        }
        try {
            productImageService.addImageToProduct(id, file);
            redirectAttributes.addFlashAttribute("message", "Tải ảnh lên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/" + getViewPrefix();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> getApi(@PathVariable("id") Long id) {
        try {
            T entity = service.findById(id);
            List<ProductImage> images = productImageService.getImagesByProductId(id);
            Map<String, Object> response = new HashMap<>();
            response.put(getModelName(), convertToDto(entity));
            response.put("images", images);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
