package com.codegym.computercomponents.repository;

import com.codegym.computercomponents.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUserId(Long userId);
    List<UserAddress> findByUserUsername(String username);
}
