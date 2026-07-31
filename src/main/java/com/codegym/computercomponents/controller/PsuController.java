package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.dto.PsuDto;
import com.codegym.computercomponents.model.Psu;
import com.codegym.computercomponents.service.IProductImageService;
import com.codegym.computercomponents.service.IPsuService;
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
@RequestMapping("/admin/psu")
public class PsuController extends BaseProductController<Psu> {

    public PsuController(IPsuService psuService, IProductImageService productImageService) {
        super(psuService, productImageService);
    }

    @Override
    protected String getViewPrefix() {
        return "psu";
    }

    @Override
    protected String getModelName() {
        return "psu";
    }

    @Override
    protected Object createEmptyDto() {
        return new PsuDto();
    }

    @Override
    protected Object convertToDto(Psu entity) {
        return PsuDto.fromEntity(entity);
    }

    @PostMapping("/api/save")
    @ResponseBody
    public ResponseEntity<?> saveAjax(@Valid @ModelAttribute PsuDto dto, 
                                      BindingResult result,
                                      @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                      @RequestParam(value = "deletedImageIds", required = false) List<Long> deletedImageIds) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Psu existing = (dto.getId() != null) ? service.findById(dto.getId()) : new Psu();
            Psu saved = service.save(dto.toEntity(existing));

            if (deletedImageIds != null && !deletedImageIds.isEmpty()) {
                for (Long imageId : deletedImageIds) {
                    productImageService.deleteImage(imageId);
                }
            }

            if (files != null && !files.isEmpty()) {
                productImageService.addImagesToProduct(saved.getId(), files);
            }
            return ResponseEntity.ok(PsuDto.fromEntity(saved));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("general", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
