package com.ecoapi.goodshopping.service.cart;

import com.ecoapi.goodshopping.exceptions.ResourceNotFoundException;
import com.ecoapi.goodshopping.model.Cart;
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
    //private final AtomicLong cartIdGenerator = new AtomicLong(0);

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
    @Transactional
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

    // This method is designed to initialize a new shopping cart for a user.
    // It follows a reuse-first approach, where it first checks if there is an empty or inactive cart that can be reused.
    // If no empty cart exists, it creates a new one.
    @Override
    @Transactional // Ensures that the method runs within a transactional context. If any part of the method fails, the entire transaction is rolled back.
    public Long initializeNewCart() {
        //Cart newCart = new Cart();
        //Long newCartId = cartIdGenerator.incrementAndGet();
        //newCart.setId(newCartId);
        //return cartRepository.save(newCart).getId();
        // Check if there's an existing cart with no items (an empty cart)
        Optional<Cart> existingCart = cartRepository.findByItemsIsEmpty();

        if (existingCart.isPresent()) { // An Empty Cart Exists
            // Reuse the existing cart
            //Cart cart = existingCart.get();
            // return cart.getId();
            return existingCart.get().getId(); // If an empty cart exists, retrieves its ID and returns it.
        } else { // No Empty Cart Exists (empty Optional)
            // Create a new cart
            Cart newCart = new Cart();
            //Cart savedCart = cartRepository.save(newCart);
            //return savedCart.getId();
            return cartRepository.save(newCart).getId(); // Retrieves the ID of the newly saved cart and returns it.
        }
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
