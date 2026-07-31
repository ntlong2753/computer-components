package com.codegym.computercomponents.dto;

import com.codegym.computercomponents.model.Psu;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PsuDto {
    private Long id;

    @NotBlank(message = "Tên nguồn không được để trống")
    private String name;

    @NotBlank(message = "Hãng không được để trống")
    private String brand;

    @NotNull(message = "Công suất không được để trống")
    @Positive(message = "Công suất phải lớn hơn 0")
    private Integer wattage;

    @NotBlank(message = "Chuẩn nguồn không được để trống")
    private String efficiency;

    @NotBlank(message = "Kích thước không được để trống")
    private String formFactor;

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    @DecimalMax(value = "999999999.99", message = "Giá không được vượt quá 999,999,999.99")
    private BigDecimal price;

    @NotNull(message = "Tồn kho không được để trống")
    @PositiveOrZero(message = "Tồn kho không được âm")
    private Integer stock;

    private String description;
    
    private String imageUrl;

    public Psu toEntity(Psu entity) {
        entity.setName(this.name);
        entity.setBrand(this.brand);
        entity.setWattage(this.wattage);
        entity.setEfficiency(this.efficiency);
        entity.setFormFactor(this.formFactor);
        entity.setPrice(this.price);
        entity.setStock(this.stock);
        entity.setDescription(this.description);
        return entity;
    }

    public static PsuDto fromEntity(Psu entity) {
        return PsuDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .wattage(entity.getWattage())
                .efficiency(entity.getEfficiency())
                .formFactor(entity.getFormFactor())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}
