package com.codegym.computercomponents.dto;

import com.codegym.computercomponents.model.Storage;
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
public class StorageDto {
    private Long id;

    @NotBlank(message = "Tên ổ cứng không được để trống")
    private String name;

    @NotBlank(message = "Hãng không được để trống")
    private String brand;

    @NotBlank(message = "Loại ổ cứng không được để trống")
    private String type; // HDD / SSD

    @NotNull(message = "Dung lượng không được để trống")
    @Positive(message = "Dung lượng phải lớn hơn 0")
    private Integer capacity;

    @NotBlank(message = "Đơn vị dung lượng không được để trống")
    private String capacityUnit;

    private Integer rpm;

    private Integer cache;
    private String cacheUnit;

    private String connectionType;
    private String pcieGen;
    
    private Integer readSpeed;
    private Integer writeSpeed;

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    @DecimalMax(value = "999999999.99", message = "Giá không được vượt quá 999,999,999.99")
    private BigDecimal price;

    @NotNull(message = "Tồn kho không được để trống")
    @PositiveOrZero(message = "Tồn kho không được âm")
    private Integer stock;

    private String description;
    
    private String imageUrl;

    public Storage toEntity(Storage storage) {
        storage.setName(this.name);
        storage.setBrand(this.brand);
        storage.setType(this.type);
        storage.setCapacity(this.capacity);
        storage.setCapacityUnit(this.capacityUnit);
        storage.setCache(this.cache);
        storage.setCacheUnit(this.cacheUnit);
        
        if ("HDD".equalsIgnoreCase(this.type)) {
            storage.setRpm(this.rpm);
            storage.setConnectionType(null);
            storage.setPcieGen(null);
            storage.setReadSpeed(null);
            storage.setWriteSpeed(null);
        } else {
            storage.setRpm(null);
            storage.setConnectionType(this.connectionType);
            storage.setPcieGen(this.pcieGen);
            storage.setReadSpeed(this.readSpeed);
            storage.setWriteSpeed(this.writeSpeed);
        }

        storage.setPrice(this.price);
        storage.setStock(this.stock);
        storage.setDescription(this.description);
        return storage;
    }

    public static StorageDto fromEntity(Storage storage) {
        return StorageDto.builder()
                .id(storage.getId())
                .name(storage.getName())
                .brand(storage.getBrand())
                .type(storage.getType())
                .capacity(storage.getCapacity())
                .capacityUnit(storage.getCapacityUnit())
                .cache(storage.getCache())
                .cacheUnit(storage.getCacheUnit())
                .rpm(storage.getRpm())
                .connectionType(storage.getConnectionType())
                .pcieGen(storage.getPcieGen())
                .readSpeed(storage.getReadSpeed())
                .writeSpeed(storage.getWriteSpeed())
                .price(storage.getPrice())
                .stock(storage.getStock())
                .description(storage.getDescription())
                .imageUrl(storage.getImageUrl())
                .build();
    }
}
