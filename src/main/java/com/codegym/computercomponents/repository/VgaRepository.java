package com.codegym.computercomponents.repository;

import com.codegym.computercomponents.model.Vga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VgaRepository extends JpaRepository<Vga, Long> {
}
