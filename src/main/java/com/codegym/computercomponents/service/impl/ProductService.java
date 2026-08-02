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
}
