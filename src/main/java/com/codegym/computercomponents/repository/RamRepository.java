package com.codegym.computercomponents.repository;

import com.codegym.computercomponents.model.Ram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RamRepository extends JpaRepository<Ram, Long> {
}
