package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.dto.VgaDto;
import com.codegym.computercomponents.model.Vga;
import com.codegym.computercomponents.service.impl.VgaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.codegym.computercomponents.service.IProductImageService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/admin/vga")
public class VgaController extends BaseProductController<Vga> {

    public VgaController(VgaService vgaService, IProductImageService productImageService) {
        super(vgaService, productImageService);
    }

    @Override
    protected String getViewPrefix() {
        return "vga";
    }

    @Override
    protected String getModelName() {
        return "vga";
    }

    @Override
    protected Object createEmptyDto() {
        return new VgaDto();
    }

    @Override
    protected Object convertToDto(Vga entity) {
        return VgaDto.fromEntity(entity);
    }

    @PostMapping("/api/save")
    @ResponseBody
    public ResponseEntity<?> saveAjax(@Valid @ModelAttribute VgaDto vgaDto, 
                                      BindingResult result,
                                      @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                      @RequestParam(value = "deletedImageIds", required = false) List<Long> deletedImageIds) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Vga existingVga = (vgaDto.getId() != null) ? service.findById(vgaDto.getId()) : new Vga();
            Vga savedVga = service.save(vgaDto.toEntity(existingVga));

            if (deletedImageIds != null && !deletedImageIds.isEmpty()) {
                for (Long imageId : deletedImageIds) {
                    productImageService.deleteImage(imageId);
                }
            }

            // Chuyển việc tải lên hàng loạt ảnh cho Service xử lý
            if (files != null && !files.isEmpty()) {
                productImageService.addImagesToProduct(savedVga.getId(), files);
            }
            return ResponseEntity.ok(VgaDto.fromEntity(savedVga));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("general", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
