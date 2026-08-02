package com.codegym.computercomponents.service.impl;

import com.codegym.computercomponents.model.Product;
import com.codegym.computercomponents.repository.*;
import com.codegym.computercomponents.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
    public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CpuRepository cpuRepository;
    private final VgaRepository vgaRepository;
    private final RamRepository ramRepository;
    private final MainboardRepository mainboardRepository;
    private final StorageRepository storageRepository;
    private final PsuRepository psuRepository;
    private final CasePcRepository casePcRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + id));
    }

    @Override
    public Page<Product> findByCategory(String category, Pageable pageable) {
        if (category == null) {
            return Page.empty();
        }
        return switch (category.toLowerCase()) {
            case "vga" -> vgaRepository.findAll(pageable).map(p -> (Product) p);
            case "cpu" -> cpuRepository.findAll(pageable).map(p -> (Product) p);
            case "ram" -> ramRepository.findAll(pageable).map(p -> (Product) p);
            case "mainboard" -> mainboardRepository.findAll(pageable).map(p -> (Product) p);
            case "storage" -> storageRepository.findAll(pageable).map(p -> (Product) p);
            case "psu" -> psuRepository.findAll(pageable).map(p -> (Product) p);
            case "casepc" -> casePcRepository.findAll(pageable).map(p -> (Product) p);
            default -> Page.empty();
        };
    }

    @Override
    public Page<Product> findByCategoryAndKeyword(String category, String keyword, Pageable pageable) {
        if (category == null || category.trim().isEmpty()) {
            if (keyword != null && !keyword.trim().isEmpty()) {
                return productRepository.searchByKeyword(keyword.trim(), pageable);
            }
            return Page.empty();
        }
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return findByCategory(category, pageable);
        }

        // Fetch all products of the category to filter in-memory
        List<? extends Product> allInCategory = switch (category.toLowerCase()) {
            case "vga" -> vgaRepository.findAll();
            case "cpu" -> cpuRepository.findAll();
            case "ram" -> ramRepository.findAll();
            case "mainboard" -> mainboardRepository.findAll();
            case "storage" -> storageRepository.findAll();
            case "psu" -> psuRepository.findAll();
            case "casepc" -> casePcRepository.findAll();
            default -> List.of();
        };

        String lowerKeyword = keyword.toLowerCase().trim();
        List<Product> filtered = allInCategory.stream()
                .filter(p -> (p.getName() != null && p.getName().toLowerCase().contains(lowerKeyword)) ||
                             (p.getBrand() != null && p.getBrand().toLowerCase().contains(lowerKeyword)))
                .map(p -> (Product) p)
                .toList();

        // Paginate manually
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        
        List<Product> pageContent = (start <= end && start < filtered.size()) 
                                    ? filtered.subList(start, end) 
                                    : List.of();

        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, filtered.size());
    }
}
