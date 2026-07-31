package com.codegym.computercomponents.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "ram")
public class Ram extends Product {

    @Positive(message = "Dung lượng RAM phải lớn hơn 0")
    @Column(nullable = false)
    private Integer capacity; // Dung lượng (GB)

    @Column(length = 50)
    private String ramType; // Chuẩn RAM (DDR4, DDR5...)

    @Positive(message = "Bus RAM phải lớn hơn 0")
    @Column
    private Integer busSpeed; // Bus RAM (MHz)

}
