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
@Table(name = "psu")
public class Psu extends Product {

    @Positive(message = "Công suất phải lớn hơn 0")
    @Column(nullable = false)
    private Integer wattage;

    @Column(length = 50)
    private String efficiency;

    @Column(length = 50)
    private String formFactor;

}
