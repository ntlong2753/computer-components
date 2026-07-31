package com.codegym.computercomponents.dto;

import com.codegym.computercomponents.model.Ram;
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
public class RamDto {
    private Long id;

    @NotBlank(message = "Tên RAM không được để trống")
    private String name;

    @NotBlank(message = "Hãng không được để trống")
    private String brand;

    @NotNull(message = "Dung lượng không được để trống")
    @Positive(message = "Dung lượng phải lớn hơn 0")
    private Integer capacity;

    private String ramType;

    @NotNull(message = "Bus RAM không được để trống")
    @Positive(message = "Bus RAM phải lớn hơn 0")
    private Integer busSpeed;

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    @DecimalMax(value = "999999999.99", message = "Giá không được vượt quá 999,999,999.99")
    private BigDecimal price;

    @NotNull(message = "Tồn kho không được để trống")
    @PositiveOrZero(message = "Tồn kho không được âm")
    private Integer stock;

    private String description;
    
    private String imageUrl;

    public Ram toEntity(Ram ram) {
        ram.setName(this.name);
        ram.setBrand(this.brand);
        ram.setCapacity(this.capacity);
        ram.setRamType(this.ramType);
        ram.setBusSpeed(this.busSpeed);
        ram.setPrice(this.price);
        ram.setStock(this.stock);
        ram.setDescription(this.description);
        // imageUrl được xử lý riêng qua ProductImageService
        return ram;
    }

    public static RamDto fromEntity(Ram ram) {
        return RamDto.builder()
                .id(ram.getId())
                .name(ram.getName())
                .brand(ram.getBrand())
                .capacity(ram.getCapacity())
                .ramType(ram.getRamType())
                .busSpeed(ram.getBusSpeed())
                .price(ram.getPrice())
                .stock(ram.getStock())
                .description(ram.getDescription())
                .imageUrl(ram.getImageUrl())
                .build();
    }
}
