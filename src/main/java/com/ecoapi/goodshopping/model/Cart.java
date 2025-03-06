package com.ecoapi.goodshopping.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    // If a cartItem is unlinked from the father collection, then that cartItem will be removed from the database automatically.
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    public void addItem(CartItem item) {
        this.items.add(item);
        item.setCart(this); // ensures that the bidirectional relationship between Cart and CartItem is maintained
        updateTotalAmount();
    }

    public void removeItem(CartItem item) {
        this.items.remove(item);
        item.setCart(null); // breaks the bidirectional relationship between the Cart and CartItem.
        updateTotalAmount();
    }

    // Recalculates the total amount of the cart based on the items in the cart.
    private void updateTotalAmount() {
        this.totalAmount = items.stream()
                                .map(CartItem::getTotalPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

