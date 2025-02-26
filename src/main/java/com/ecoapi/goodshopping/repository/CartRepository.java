package com.ecoapi.goodshopping.repository;

import com.ecoapi.goodshopping.model.Cart;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE) // Lock the entity
    Optional<Cart> findById(Long id);
    // Find a cart with no items
    Optional<Cart> findByItemsIsEmpty();
}
