package com.codegym.computercomponents.controller;

import com.codegym.computercomponents.model.Product;
import com.codegym.computercomponents.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> search(@RequestParam("q") String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        Page<Product> productPage = productRepository.searchByKeyword(query.trim(), PageRequest.of(0, 5)); // Limit 5 kết quả
        
        // Map sang Map đơn giản để tránh JSON loop (vì Product có nhiều field phức tạp)
        List<Map<String, Object>> results = productPage.getContent().stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("name", p.getName());
            map.put("price", p.getPrice());
            
            // Lấy ảnh đầu tiên nếu có
            String firstImage = "https://via.placeholder.com/60x60/1e293b/ffffff?text=No+Image";
            if (p.getImageUrl() != null && !p.getImageUrl().isBlank()) {
                firstImage = p.getImageUrl().split(",")[0];
            }
            map.put("imageUrl", firstImage);
            map.put("category", p.getClass().getSimpleName());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }
}
