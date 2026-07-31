package com.codegym.computercomponents.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class ProductDto {

    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    private String brand;

    @Positive(message = "Giá phải lớn hơn 0")
    @DecimalMax(value = "999999999.99", message = "Giá không được vượt quá 999,999,999.99")
    private BigDecimal price;

    @PositiveOrZero(message = "Tồn kho không được âm")
    private Integer stock;

    private String imageUrl;

    private String description;
}
