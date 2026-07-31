package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.dto.MainboardDto;
import com.codegym.computercomponents.model.Mainboard;
import com.codegym.computercomponents.service.IMainboardService;
import com.codegym.computercomponents.service.IProductImageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/mainboard")
public class MainboardController extends BaseProductController<Mainboard> {

    public MainboardController(IMainboardService mainboardService, IProductImageService productImageService) {
        super(mainboardService, productImageService);
    }

    @Override
    protected String getViewPrefix() {
        return "mainboard";
    }

    @Override
    protected String getModelName() {
        return "mainboard";
    }

    @Override
    protected Object createEmptyDto() {
        return new MainboardDto();
    }

    @Override
    protected Object convertToDto(Mainboard entity) {
        return MainboardDto.fromEntity(entity);
    }

    @PostMapping("/api/save")
    @ResponseBody
    public ResponseEntity<?> saveAjax(@Valid @ModelAttribute MainboardDto dto, 
                                      BindingResult result,
                                      @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                      @RequestParam(value = "deletedImageIds", required = false) List<Long> deletedImageIds) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Mainboard existing = (dto.getId() != null) ? service.findById(dto.getId()) : new Mainboard();
            Mainboard saved = service.save(dto.toEntity(existing));

            if (deletedImageIds != null && !deletedImageIds.isEmpty()) {
                for (Long imageId : deletedImageIds) {
                    productImageService.deleteImage(imageId);
                }
            }

            if (files != null && !files.isEmpty()) {
                productImageService.addImagesToProduct(saved.getId(), files);
            }
            return ResponseEntity.ok(MainboardDto.fromEntity(saved));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("general", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
