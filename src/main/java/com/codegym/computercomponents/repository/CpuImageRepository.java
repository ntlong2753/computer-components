package com.codegym.computercomponents.repository;

import com.codegym.computercomponents.model.CpuImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpuImageRepository extends JpaRepository<CpuImage, Long> {
    List<CpuImage> findByCpuId(Long cpuId);
    
    long countByCpuId(Long cpuId);
}
