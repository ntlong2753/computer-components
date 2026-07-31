package com.codegym.computercomponents.dto;

import com.codegym.computercomponents.model.Mainboard;
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
public class MainboardDto {
    private Long id;

    @NotBlank(message = "Tên Mainboard không được để trống")
    private String name;

    @NotBlank(message = "Hãng không được để trống")
    private String brand;

    @NotBlank(message = "Model không được để trống")
    private String model;

    @NotBlank(message = "Socket không được để trống")
    private String socket;

    @NotBlank(message = "Chipset không được để trống")
    private String chipset;

    @NotBlank(message = "Chuẩn RAM không được để trống")
    private String ramType;

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

    public Mainboard toEntity(Mainboard entity) {
        entity.setName(this.name);
        entity.setBrand(this.brand);
        entity.setModel(this.model);
        entity.setSocket(this.socket);
        entity.setChipset(this.chipset);
        entity.setRamType(this.ramType);
        entity.setFormFactor(this.formFactor);
        entity.setPrice(this.price);
        entity.setStock(this.stock);
        entity.setDescription(this.description);
        return entity;
    }

    public static MainboardDto fromEntity(Mainboard entity) {
        return MainboardDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .socket(entity.getSocket())
                .chipset(entity.getChipset())
                .ramType(entity.getRamType())
                .formFactor(entity.getFormFactor())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}
