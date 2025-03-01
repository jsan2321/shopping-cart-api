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

/*
    @Version // Add this field for optimistic locking
    private Integer version;
*/
    private BigDecimal totalAmount = BigDecimal.ZERO;

    // cascading is enabled. This allows operations (e.g., persist, merge, remove) performed on this entity to be propagated to related entities.
    // When orphanRemoval is true, JPA considers that if a child entity is no longer referenced by the parent entity, then it should be removed from the database.
    // Indicates that if a child item is removed from the relationship on the owner side, it will also be automatically removed from the database.
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
   /*     this.totalAmount = items.stream().map(item -> {
            BigDecimal unitPrice = item.getUnitPrice();
            if (unitPrice == null) {
                return BigDecimal.ZERO;
            }
            return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    */
        this.totalAmount = items.stream()
                                .map(CartItem::getTotalPrice)
                                // Reduces the stream of totalPrice values to a single BigDecimal by summing them up. For each element in the stream, it applies the add method to combine the current result with the next element.
                                // The process starts with BigDecimal.ZERO as the initial value, it serves as the starting point for the summation.
                                // BigDecimal::add is used to accumulate the sum, it combines two values and returns the result
                                // If the stream is empty, the result of the reduction will be BigDecimal.ZERO.
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

