package com.codegym.computercomponents.dto;

import com.codegym.computercomponents.model.Vga;
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
public class VgaDto extends ProductDto {

    @NotBlank(message = "Hãng GPU không được để trống")
    private String gpuBrand;

    @NotBlank(message = "Model GPU không được để trống")
    private String gpuModel;

    @Min(value = 1, message = "VRAM phải lớn hơn 0")
    private Integer vram;

    private String memoryType;

    private String series;

    // Convert from DTO to Entity
    public Vga toEntity(Vga existingVga) {
        if (existingVga == null) {
            existingVga = new Vga();
        }
        existingVga.setId(this.getId());
        existingVga.setName(this.getName());
        existingVga.setBrand(this.getBrand());
        existingVga.setPrice(this.getPrice());
        existingVga.setStock(this.getStock());
        existingVga.setImageUrl(this.getImageUrl());
        existingVga.setDescription(this.getDescription());
        existingVga.setGpuBrand(this.gpuBrand);
        existingVga.setGpuModel(this.gpuModel);
        existingVga.setVram(this.vram);
        existingVga.setMemoryType(this.memoryType);
        existingVga.setSeries(this.series);
        return existingVga;
    }

    // Convert from Entity to DTO
    public static VgaDto fromEntity(Vga vga) {
        if (vga == null) return null;
        return VgaDto.builder()
                .id(vga.getId())
                .name(vga.getName())
                .brand(vga.getBrand())
                .price(vga.getPrice())
                .stock(vga.getStock())
                .imageUrl(vga.getImageUrl())
                .description(vga.getDescription())
                .gpuBrand(vga.getGpuBrand())
                .gpuModel(vga.getGpuModel())
                .vram(vga.getVram())
                .memoryType(vga.getMemoryType())
                .series(vga.getSeries())
                .build();
    }
}
