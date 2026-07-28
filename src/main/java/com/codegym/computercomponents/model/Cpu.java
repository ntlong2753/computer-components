package com.codegym.computercomponents.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "cpu")
@PrimaryKeyJoinColumn(name = "product_id")
public class Cpu extends Product {

    @NotBlank(message = "Socket không được để trống")
    @Column(nullable = false, length = 20)
    private String socket; // VD: LGA1700, AM5, AM4

    @Min(value = 1, message = "Số nhân phải >= 1")
    private Integer coreCount;

    @Min(value = 1, message = "Số luồng phải >= 1")
    private Integer threadCount;

    @Positive(message = "Xung nhịp phải lớn hơn 0")
    private Double baseClockGHz;

/*
    @AssertTrue(message = "Số luồng phải lớn hơn hoặc bằng số nhân")
    @Transient
    public boolean isThreadCountValid() {
        if (coreCount == null || threadCount == null) return true; // để @Min/@NotNull xử lý riêng
        return threadCount >= coreCount;
    }
*/

}
