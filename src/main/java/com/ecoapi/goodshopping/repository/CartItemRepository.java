package com.ecoapi.goodshopping.repository;

import com.ecoapi.goodshopping.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
