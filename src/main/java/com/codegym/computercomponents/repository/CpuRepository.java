package com.codegym.computercomponents.repository;


import com.codegym.computercomponents.model.Cpu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data tự sinh code CRUD (save, findById, findAll, deleteById...)
 * chỉ từ việc khai báo interface này - không cần viết implementation.
 */
@Repository
public interface CpuRepository extends JpaRepository<Cpu, Long> {
}
