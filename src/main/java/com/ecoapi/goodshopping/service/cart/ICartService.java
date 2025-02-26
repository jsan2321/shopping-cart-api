package com.ecoapi.goodshopping.service.cart;

import com.ecoapi.goodshopping.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Long initializeNewCart();

}
