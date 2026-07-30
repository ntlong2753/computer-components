package com.codegym.computercomponents.dto;

import com.codegym.computercomponents.model.Cpu;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class CpuDto {

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

    @NotBlank(message = "Socket không được để trống")
    private String socket;

    private String series;

    private String segment;

    private String modelNumber;

    private String suffix;

    @Min(value = 1, message = "Số nhân phải >= 1")
    private Integer coreCount;

    @Min(value = 1, message = "Số luồng phải >= 1")
    private Integer threadCount;

    @Positive(message = "Xung nhịp phải lớn hơn 0")
    private Double baseClockGHz;

    // Convert from DTO to Entity
    public Cpu toEntity(Cpu existingCpu) {
        if (existingCpu == null) {
            existingCpu = new Cpu();
        }
        existingCpu.setId(this.id);
        existingCpu.setName(this.name);
        existingCpu.setBrand(this.brand);
        existingCpu.setPrice(this.price);
        existingCpu.setStock(this.stock);
        existingCpu.setImageUrl(this.imageUrl);
        existingCpu.setDescription(this.description);
        existingCpu.setSocket(this.socket);
        existingCpu.setSeries(this.series);
        existingCpu.setSegment(this.segment);
        existingCpu.setModelNumber(this.modelNumber);
        existingCpu.setSuffix(this.suffix);
        existingCpu.setCoreCount(this.coreCount);
        existingCpu.setThreadCount(this.threadCount);
        existingCpu.setBaseClockGHz(this.baseClockGHz);
        return existingCpu;
    }

    // Convert from Entity to DTO
    public static CpuDto fromEntity(Cpu cpu) {
        if (cpu == null) return null;
        return CpuDto.builder()
                .id(cpu.getId())
                .name(cpu.getName())
                .brand(cpu.getBrand())
                .price(cpu.getPrice())
                .stock(cpu.getStock())
                .imageUrl(cpu.getImageUrl())
                .description(cpu.getDescription())
                .socket(cpu.getSocket())
                .series(cpu.getSeries())
                .segment(cpu.getSegment())
                .modelNumber(cpu.getModelNumber())
                .suffix(cpu.getSuffix())
                .coreCount(cpu.getCoreCount())
                .threadCount(cpu.getThreadCount())
                .baseClockGHz(cpu.getBaseClockGHz())
                .build();
    }
}
