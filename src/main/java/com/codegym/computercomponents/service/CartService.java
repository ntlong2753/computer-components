package com.codegym.computercomponents.service;

import com.codegym.computercomponents.dto.CartDTO;
import com.codegym.computercomponents.dto.CartItemDTO;
import com.codegym.computercomponents.model.AppUser;
import com.codegym.computercomponents.model.Cart;
import com.codegym.computercomponents.model.CartItem;
import com.codegym.computercomponents.model.Product;
import com.codegym.computercomponents.repository.CartRepository;
import com.codegym.computercomponents.repository.ProductRepository;
import com.codegym.computercomponents.repository.UserRepository;
import com.codegym.computercomponents.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final String COOKIE_CART_KEY = "GUEST_CART";
    private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7 days

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    /**
     * Lấy giỏ hàng hiện tại dưới dạng DTO (để hiển thị)
     */
    @Transactional(readOnly = true)
    public CartDTO getCart() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            // Lấy từ DB
            String username = auth.getName();
            Optional<AppUser> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                Optional<Cart> cartOpt = cartRepository.findByUser(userOpt.get());
                if (cartOpt.isPresent()) {
                    return convertToDTO(cartOpt.get());
                }
            }
        }
        // Lấy từ cookie
        CartDTO cookieCart = CookieUtils.getObjectFromCookie(request, COOKIE_CART_KEY, CartDTO.class);
        if (cookieCart == null) {
            cookieCart = new CartDTO();
            // Don't save empty cart immediately to avoid unnecessary cookies
        }
        return cookieCart;
    }

    /**
     * Thêm sản phẩm vào giỏ
     */
    @Transactional
    public CartDTO addToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            // Thêm vào DB
            String username = auth.getName();
            AppUser user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

                Optional<CartItem> existingItemOpt = cart.getItems().stream()
                        .filter(item -> item.getProduct().getId().equals(productId))
                        .findFirst();

                if (existingItemOpt.isPresent()) {
                    CartItem existingItem = existingItemOpt.get();
                    existingItem.setQuantity(existingItem.getQuantity() + quantity);
                } else {
                    CartItem newItem = new CartItem();
                    newItem.setProduct(product);
                    newItem.setQuantity(quantity);
                    cart.addItem(newItem);
                }
                cart = cartRepository.save(cart);
                return convertToDTO(cart);
            }
        }

        // Thêm vào cookie
        CartDTO cookieCart = CookieUtils.getObjectFromCookie(request, COOKIE_CART_KEY, CartDTO.class);
        if (cookieCart == null) {
            cookieCart = new CartDTO();
        }

        Optional<CartItemDTO> existingItemOpt = cookieCart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItemDTO existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItemDTO newItem = new CartItemDTO();
            newItem.setProductId(product.getId());
            newItem.setProductName(product.getName());
            newItem.setPrice(product.getPrice());
            newItem.setStock(product.getStock());
            if (product.getImageUrl() != null && !product.getImageUrl().isBlank()) {
                newItem.setImageUrl(product.getImageUrl().split(",")[0]);
            }
            newItem.setQuantity(quantity);
            cookieCart.getItems().add(newItem);
        }
        CookieUtils.setObjectToCookie(response, COOKIE_CART_KEY, cookieCart, COOKIE_MAX_AGE);
        return cookieCart;
    }

    /**
     * Xóa sản phẩm khỏi giỏ
     */
    @Transactional
    public CartDTO removeFromCart(Long productId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = auth.getName();
            AppUser user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                Cart cart = cartRepository.findByUser(user).orElse(null);
                if (cart != null) {
                    cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
                    cart = cartRepository.save(cart);
                    return convertToDTO(cart);
                }
                return new CartDTO();
            }
        }

        // Cookie
        CartDTO cookieCart = CookieUtils.getObjectFromCookie(request, COOKIE_CART_KEY, CartDTO.class);
        if (cookieCart != null) {
            cookieCart.getItems().removeIf(item -> item.getProductId().equals(productId));
            CookieUtils.setObjectToCookie(response, COOKIE_CART_KEY, cookieCart, COOKIE_MAX_AGE);
            return cookieCart;
        }
        return new CartDTO();
    }

    /**
     * Cập nhật số lượng
     */
    @Transactional
    public CartDTO updateQuantity(Long productId, int quantity) {
        if (quantity <= 0) {
            return removeFromCart(productId);
        }
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = auth.getName();
            AppUser user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                Cart cart = cartRepository.findByUser(user).orElse(null);
                if (cart != null) {
                    cart.getItems().stream()
                            .filter(item -> item.getProduct().getId().equals(productId))
                            .findFirst()
                            .ifPresent(item -> item.setQuantity(quantity));
                    cart = cartRepository.save(cart);
                    return convertToDTO(cart);
                }
                return new CartDTO();
            }
        }

        // Cookie
        CartDTO cookieCart = CookieUtils.getObjectFromCookie(request, COOKIE_CART_KEY, CartDTO.class);
        if (cookieCart != null) {
            cookieCart.getItems().stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst()
                    .ifPresent(item -> item.setQuantity(quantity));
            CookieUtils.setObjectToCookie(response, COOKIE_CART_KEY, cookieCart, COOKIE_MAX_AGE);
            return cookieCart;
        }
        return new CartDTO();
    }

    /**
     * Merge giỏ hàng từ cookie vào user khi đăng nhập
     */
    @Transactional
    public void mergeGuestCartIntoUserCart(String username, HttpServletRequest req, HttpServletResponse res) {
        CartDTO cookieCart = CookieUtils.getObjectFromCookie(req, COOKIE_CART_KEY, CartDTO.class);
        if (cookieCart == null || cookieCart.getItems().isEmpty()) {
            return; // Không có gì để gộp
        }

        AppUser user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return;

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        for (CartItemDTO cookieItem : cookieCart.getItems()) {
            Product product = productRepository.findById(cookieItem.getProductId()).orElse(null);
            if (product != null) {
                Optional<CartItem> existingItemOpt = cart.getItems().stream()
                        .filter(item -> item.getProduct().getId().equals(cookieItem.getProductId()))
                        .findFirst();

                if (existingItemOpt.isPresent()) {
                    // Cộng dồn số lượng
                    CartItem existingItem = existingItemOpt.get();
                    existingItem.setQuantity(existingItem.getQuantity() + cookieItem.getQuantity());
                } else {
                    CartItem newItem = new CartItem();
                    newItem.setProduct(product);
                    newItem.setQuantity(cookieItem.getQuantity());
                    cart.addItem(newItem);
                }
            }
        }

        cartRepository.save(cart);
        // Xóa giỏ hàng trong cookie sau khi đã gộp
        CookieUtils.deleteCookie(res, COOKIE_CART_KEY);
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        for (CartItem item : cart.getItems()) {
            CartItemDTO itemDTO = new CartItemDTO();
            Product p = item.getProduct();
            itemDTO.setProductId(p.getId());
            itemDTO.setProductName(p.getName());
            itemDTO.setPrice(p.getPrice());
            itemDTO.setStock(p.getStock());
            if (p.getImageUrl() != null && !p.getImageUrl().isBlank()) {
                itemDTO.setImageUrl(p.getImageUrl().split(",")[0]);
            }
            itemDTO.setQuantity(item.getQuantity());
            dto.getItems().add(itemDTO);
        }
        return dto;
    }
}
