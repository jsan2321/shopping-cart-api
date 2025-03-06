package com.ecoapi.goodshopping.repository;

import com.ecoapi.goodshopping.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // This method finds a Cart entity that has no associated items (i.e., an empty cart)
    Optional<Cart> findByItemsIsEmpty();

    Cart findByUserId(Long userId);
}
