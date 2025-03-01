package com.ecoapi.goodshopping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal unitPrice; // BigDecimal is used for precise decimal arithmetic, avoiding rounding errors that can occur with double or float.
    private BigDecimal totalPrice; // BigDecimal objects are immutable, so each addition operation creates a new BigDecimal object.

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // In a bidirectional @OneToMany relationship, the @ManyToOne side (in this case, CartItem) is the owning side.
    //Changes to the owning side are automatically synchronized with the database when the owning entity is saved.
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public void setTotalPrice() {
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }
}

