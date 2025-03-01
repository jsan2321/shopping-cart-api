package com.ecoapi.goodshopping.dto;

import lombok.Data;

import java.math.BigDecimal;

// ModelMapper maps the OrderItem fields to the OrderItemDto fields:
// - product.getId() → productId
// - product.getName() → productName
// - product.getBrand() → productBrand
// - quantity → quantity
// - price → price
//This mapping is possible because ModelMapper can traverse the object graph and map nested fields.
@Data
public class OrderItemDto {
    private Long productId;
    private String productName;
    private String productBrand;
    private int quantity;
    private BigDecimal price;
}

