package com.codegym.computercomponents.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private String imageUrl;
    private int quantity;
    private int stock;
}
