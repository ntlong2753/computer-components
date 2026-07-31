package com.codegym.computercomponents.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "storage")
public class Storage extends Product {

    @NotBlank(message = "Loại ổ cứng không được để trống")
    @Column(nullable = false, length = 10)
    private String type; // "HDD" hoặc "SSD"

    @Positive(message = "Dung lượng phải lớn hơn 0")
    @Column(nullable = false)
    private Integer capacity;

    @NotBlank(message = "Đơn vị dung lượng không được để trống")
    @Column(nullable = false, length = 10)
    private String capacityUnit; // "GB" hoặc "TB"

    // --- Trường chung HDD & SSD ---
    @Positive(message = "Bộ nhớ đệm phải lớn hơn 0")
    @Column
    private Integer cache;

    @Column(length = 10)
    private String cacheUnit; // "MB" hoặc "GB"

    // --- HDD riêng ---
    @Positive(message = "Tốc độ vòng quay phải lớn hơn 0")
    @Column
    private Integer rpm;

    // --- SSD riêng ---
    @Column(length = 50)
    private String connectionType; // Chuẩn kết nối (M.2 NVMe, SATA 3...)

    @Column(length = 50)
    private String pcieGen; // Chuẩn PCIe (PCIe 4.0 x4...)

    @Positive(message = "Tốc độ đọc phải lớn hơn 0")
    @Column
    private Integer readSpeed;

    @Positive(message = "Tốc độ ghi phải lớn hơn 0")
    @Column
    private Integer writeSpeed;

}
