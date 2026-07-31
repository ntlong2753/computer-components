package com.codegym.computercomponents.dto;

import com.codegym.computercomponents.model.Cpu;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
public class CpuDto extends ProductDto {

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
        existingCpu.setId(this.getId());
        existingCpu.setName(this.getName());
        existingCpu.setBrand(this.getBrand());
        existingCpu.setPrice(this.getPrice());
        existingCpu.setStock(this.getStock());
        existingCpu.setImageUrl(this.getImageUrl());
        existingCpu.setDescription(this.getDescription());
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
