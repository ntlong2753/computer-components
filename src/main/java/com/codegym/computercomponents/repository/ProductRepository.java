package com.codegym.computercomponents.repository;


import com.codegym.computercomponents.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR CAST(p.id AS string) = :keyword")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
