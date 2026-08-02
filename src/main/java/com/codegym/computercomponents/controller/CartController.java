package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.dto.CartDTO;
import com.codegym.computercomponents.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/cart")
    public String viewCart(Model model) {
        CartDTO cart = cartService.getCart();
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/api/cart/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity) {
        CartDTO cart = cartService.addToCart(productId, quantity);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Đã thêm sản phẩm vào giỏ hàng");
        response.put("cartItemCount", cart.getTotalItems());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/cart/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCart(@RequestParam Long productId, @RequestParam int quantity) {
        CartDTO cart = cartService.updateQuantity(productId, quantity);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("cartItemCount", cart.getTotalItems());
        response.put("totalPrice", cart.getTotalPrice());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/cart/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromCart(@RequestParam Long productId) {
        CartDTO cart = cartService.removeFromCart(productId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("cartItemCount", cart.getTotalItems());
        response.put("totalPrice", cart.getTotalPrice());
        
        return ResponseEntity.ok(response);
    }
}
