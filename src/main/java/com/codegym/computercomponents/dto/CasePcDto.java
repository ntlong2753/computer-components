package com.codegym.computercomponents.dto;

import com.codegym.computercomponents.model.CasePc;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CasePcDto extends ProductDto {

    @NotBlank(message = "Model không được để trống")
    private String model;

    @NotBlank(message = "Kích thước (Form Factor) không được để trống")
    private String formFactor;

    private String motherboardSupport;

    private String color;

    // Convert from DTO to Entity
    public CasePc toEntity(CasePc existingCase) {
        if (existingCase == null) {
            existingCase = new CasePc();
        }
        existingCase.setId(this.getId());
        existingCase.setName(this.getName());
        existingCase.setBrand(this.getBrand());
        existingCase.setPrice(this.getPrice());
        existingCase.setStock(this.getStock());
        existingCase.setImageUrl(this.getImageUrl());
        existingCase.setDescription(this.getDescription());
        
        existingCase.setModel(this.model);
        existingCase.setFormFactor(this.formFactor);
        existingCase.setMotherboardSupport(this.motherboardSupport);
        existingCase.setColor(this.color);
        
        return existingCase;
    }

    // Convert from Entity to DTO
    public static CasePcDto fromEntity(CasePc casePc) {
        if (casePc == null) return null;
        return CasePcDto.builder()
                .id(casePc.getId())
                .name(casePc.getName())
                .brand(casePc.getBrand())
                .price(casePc.getPrice())
                .stock(casePc.getStock())
                .imageUrl(casePc.getImageUrl())
                .description(casePc.getDescription())
                .model(casePc.getModel())
                .formFactor(casePc.getFormFactor())
                .motherboardSupport(casePc.getMotherboardSupport())
                .color(casePc.getColor())
                .build();
    }
}
