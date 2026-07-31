package com.codegym.computercomponents.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "mainboard")
public class Mainboard extends Product {

    @Column(length = 100)
    private String model;

    @Column(length = 50)
    private String socket;

    @Column(length = 50)
    private String chipset;

    @Column(length = 50)
    private String ramType;

    @Column(length = 50)
    private String formFactor;

}
