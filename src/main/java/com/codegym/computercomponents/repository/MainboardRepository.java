package com.codegym.computercomponents.repository;

import com.codegym.computercomponents.model.Mainboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainboardRepository extends JpaRepository<Mainboard, Long> {
}
