package com.codegym.computercomponents.repository;

import com.codegym.computercomponents.model.Psu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPsuRepository extends JpaRepository<Psu, Long> {
}
