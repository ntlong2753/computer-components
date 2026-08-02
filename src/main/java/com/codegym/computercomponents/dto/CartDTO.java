package com.codegym.computercomponents.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<CartItemDTO> items = new ArrayList<>();
    
    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public int getTotalItems() {
        return items.stream()
                .mapToInt(CartItemDTO::getQuantity)
                .sum();
    }
}
