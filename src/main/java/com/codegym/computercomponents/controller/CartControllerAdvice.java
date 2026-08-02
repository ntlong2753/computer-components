package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.service.CartService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class CartControllerAdvice {

    private final CartService cartService;

    @ModelAttribute("globalCartItemCount")
    public int getGlobalCartItemCount() {
        return cartService.getCart().getTotalItems();
    }
}
