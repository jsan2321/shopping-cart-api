package com.ecoapi.goodshopping.service.cart;

import com.ecoapi.goodshopping.exceptions.ResourceNotFoundException;
import com.ecoapi.goodshopping.model.Cart;
import com.ecoapi.goodshopping.model.User;
import com.ecoapi.goodshopping.repository.CartItemRepository;
import com.ecoapi.goodshopping.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public Cart getCart(Long id) {
        return cartRepository.findById(id)
                             .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    @Override
    @Transactional
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                       .orElseGet(() -> {
                           Cart cart = new Cart();
                           cart.setUser(user);
                           return cartRepository.save(cart);
                       });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
