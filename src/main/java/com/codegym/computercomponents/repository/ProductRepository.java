package com.codegym.computercomponents.repository;


import com.codegym.computercomponents.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Dùng khi cần thao tác/tìm kiếm trên TẤT CẢ loại linh kiện cùng lúc
 * (VD: trang danh mục công khai ở Phase 5). Hibernate tự UNION/JOIN
 * các bảng con (cpu, vga, ram...) nhờ chiến lược JOINED.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
