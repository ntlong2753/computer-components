package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.dto.RamDto;
import com.codegym.computercomponents.model.Ram;
import com.codegym.computercomponents.service.IRamService;
import com.codegym.computercomponents.service.IProductImageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/ram")
public class RamController extends BaseProductController<Ram> {

    public RamController(IRamService ramService, IProductImageService productImageService) {
        super(ramService, productImageService);
    }

    @Override
    protected String getViewPrefix() {
        return "ram";
    }

    @Override
    protected String getModelName() {
        return "ram";
    }

    @Override
    protected Object createEmptyDto() {
        return new RamDto();
    }

    @Override
    protected Object convertToDto(Ram entity) {
        return RamDto.fromEntity(entity);
    }

    @PostMapping("/api/save")
    @ResponseBody
    public ResponseEntity<?> saveAjax(@Valid @ModelAttribute RamDto ramDto, 
                                      BindingResult result,
                                      @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                      @RequestParam(value = "deletedImageIds", required = false) List<Long> deletedImageIds) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Ram existingRam = (ramDto.getId() != null) ? service.findById(ramDto.getId()) : new Ram();
            Ram savedRam = service.save(ramDto.toEntity(existingRam));

            if (deletedImageIds != null && !deletedImageIds.isEmpty()) {
                for (Long imageId : deletedImageIds) {
                    productImageService.deleteImage(imageId);
                }
            }

            if (files != null && !files.isEmpty()) {
                productImageService.addImagesToProduct(savedRam.getId(), files);
            }
            return ResponseEntity.ok(RamDto.fromEntity(savedRam));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("general", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
