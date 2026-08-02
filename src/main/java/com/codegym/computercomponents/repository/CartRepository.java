package com.codegym.computercomponents.repository;

import com.codegym.computercomponents.model.AppUser;
import com.codegym.computercomponents.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(AppUser user);
}
