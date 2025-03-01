package com.ecoapi.goodshopping.repository;

import com.ecoapi.goodshopping.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
