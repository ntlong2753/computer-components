package com.codegym.computercomponents.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
@Entity
@Table(name = "vga")
@PrimaryKeyJoinColumn(name = "product_id")
public class Vga extends Product {

    @NotBlank(message = "Hãng GPU không được để trống")
    @Column(nullable = false, length = 100)
    private String gpuBrand; // VD: NVIDIA, AMD, Intel

    @NotBlank(message = "Model GPU không được để trống")
    @Column(nullable = false, length = 100)
    private String gpuModel; // VD: RTX 4060, RX 7600

    @Min(value = 1, message = "VRAM phải lớn hơn 0")
    private Integer vram; // VD: 8, 12, 16 (GB)

    @Column(length = 50)
    private String memoryType; // VD: GDDR6, GDDR6X

    @Column(length = 100)
    private String series; // VD: GeForce RTX 40 Series, Radeon RX 7000 Series
}
