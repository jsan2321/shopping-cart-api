package com.ecoapi.goodshopping.service.cart;

import com.ecoapi.goodshopping.exceptions.ResourceNotFoundException;
import com.ecoapi.goodshopping.model.Cart;
import com.ecoapi.goodshopping.repository.CartItemRepository;
import com.ecoapi.goodshopping.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    @Override
    @Transactional
    public Cart getCart(Long id) {
        /*Cart cart = cartRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);*/
        return cartRepository.findById(id)
                             .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    @Override
    @Transactional
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        //cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
/*
        return cart.getItems()
                   .stream()
                   .map(CartItem::getTotalPrice)
                   .reduce(BigDecimal.ZERO, BigDecimal::add);
*/
    }

    @Override
    //@Transactional
    public Long initializeNewCart() {
        Cart newCart = new Cart();
        //Long newCartId = cartIdGenerator.incrementAndGet();
        //newCart.setId(newCartId);
        return cartRepository.save(newCart).getId();
    }

}
