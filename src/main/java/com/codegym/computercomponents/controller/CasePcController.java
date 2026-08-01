package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.dto.CasePcDto;
import com.codegym.computercomponents.model.CasePc;
import com.codegym.computercomponents.service.impl.CasePcService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.codegym.computercomponents.service.IProductImageService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/casepc")
public class CasePcController extends BaseProductController<CasePc> {

    public CasePcController(CasePcService casePcService, IProductImageService productImageService) {
        super(casePcService, productImageService);
    }

    @Override
    protected String getViewPrefix() {
        return "casepc";
    }

    @Override
    protected String getModelName() {
        return "casePcs";
    }

    @Override
    protected Object createEmptyDto() {
        return new CasePcDto();
    }

    @Override
    protected Object convertToDto(CasePc entity) {
        return CasePcDto.fromEntity(entity);
    }

    @PostMapping("/api/save")
    @ResponseBody
    public ResponseEntity<?> saveAjax(@Valid @ModelAttribute CasePcDto casePcDto, 
                                      BindingResult result,
                                      @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                      @RequestParam(value = "deletedImageIds", required = false) List<Long> deletedImageIds) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            CasePc existingCase = (casePcDto.getId() != null) ? service.findById(casePcDto.getId()) : new CasePc();
            CasePc savedCase = service.save(casePcDto.toEntity(existingCase));

            if (deletedImageIds != null && !deletedImageIds.isEmpty()) {
                for (Long imageId : deletedImageIds) {
                    productImageService.deleteImage(imageId);
                }
            }

            if (files != null && !files.isEmpty()) {
                productImageService.addImagesToProduct(savedCase.getId(), files);
            }
            return ResponseEntity.ok(CasePcDto.fromEntity(savedCase));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("general", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
