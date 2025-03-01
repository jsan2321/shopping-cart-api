package com.ecoapi.goodshopping.service.cart;

import com.ecoapi.goodshopping.model.Cart;
import com.ecoapi.goodshopping.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
