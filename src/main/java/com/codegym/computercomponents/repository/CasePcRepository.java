package com.codegym.computercomponents.repository;

import com.codegym.computercomponents.model.CasePc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasePcRepository extends JpaRepository<CasePc, Long> {
}
