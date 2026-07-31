package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.dto.StorageDto;
import com.codegym.computercomponents.model.Storage;
import com.codegym.computercomponents.service.IProductImageService;
import com.codegym.computercomponents.service.IStorageService;
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
@RequestMapping("/admin/storage")
public class StorageController extends BaseProductController<Storage> {

    public StorageController(IStorageService storageService, IProductImageService productImageService) {
        super(storageService, productImageService);
    }

    @Override
    protected String getViewPrefix() {
        return "storage";
    }

    @Override
    protected String getModelName() {
        return "storage";
    }

    @Override
    protected Object createEmptyDto() {
        return new StorageDto();
    }

    @Override
    protected Object convertToDto(Storage entity) {
        return StorageDto.fromEntity(entity);
    }

    @PostMapping("/api/save")
    @ResponseBody
    public ResponseEntity<?> saveAjax(@Valid @ModelAttribute StorageDto storageDto, 
                                      BindingResult result,
                                      @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                      @RequestParam(value = "deletedImageIds", required = false) List<Long> deletedImageIds) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Storage existingStorage = (storageDto.getId() != null) ? service.findById(storageDto.getId()) : new Storage();
            Storage savedStorage = service.save(storageDto.toEntity(existingStorage));

            if (deletedImageIds != null && !deletedImageIds.isEmpty()) {
                for (Long imageId : deletedImageIds) {
                    productImageService.deleteImage(imageId);
                }
            }

            if (files != null && !files.isEmpty()) {
                productImageService.addImagesToProduct(savedStorage.getId(), files);
            }
            return ResponseEntity.ok(StorageDto.fromEntity(savedStorage));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("general", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
