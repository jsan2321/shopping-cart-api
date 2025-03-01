package com.ecoapi.goodshopping.repository;

import com.ecoapi.goodshopping.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    //@Lock(LockModeType.PESSIMISTIC_WRITE) // Lock the entity... ensuring no other transaction modifies the cart while working with it
    //Optional<Cart> findById(Long id);

    // This method finds a Cart entity that has no associated items (i.e., an empty cart). IsEmpty specifies that the query should look for carts where the items collection is empty.
    // It returns an Optional<Cart>, which means:
    //- If a cart with no items is found, it returns the Cart object wrapped in an Optional.
    //- If no such cart exists, it returns an empty Optional
    // SQL Query: SELECT * FROM cart c WHERE NOT EXISTS (SELECT 1 FROM cart_item i WHERE i.cart_id = c.id);
    Optional<Cart> findByItemsIsEmpty();
    /*
    @Query("SELECT c FROM Cart c WHERE c.items IS EMPTY")
    Optional<Cart> findEmptyCart();
    */
    Cart findByUserId(Long userId);
}
