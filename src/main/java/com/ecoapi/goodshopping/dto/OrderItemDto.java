package com.ecoapi.goodshopping.dto;

import lombok.Data;

import java.math.BigDecimal;

//This mapping is possible because ModelMapper can traverse the object graph and map nested fields.
@Data
public class OrderItemDto {
    private Long productId;
    private String productName;
    private String productBrand;
    private int quantity;
    private BigDecimal price;
}

