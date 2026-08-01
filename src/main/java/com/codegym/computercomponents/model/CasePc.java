package com.codegym.computercomponents.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
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
@Table(name = "casepc")
@PrimaryKeyJoinColumn(name = "product_id")
public class CasePc extends Product {

    @NotBlank(message = "Model không được để trống")
    @Column(nullable = false, length = 100)
    private String model;

    @NotBlank(message = "Kích thước (Form Factor) không được để trống")
    @Column(nullable = false, length = 50)
    private String formFactor;

    @Column(length = 100)
    private String motherboardSupport;

}
