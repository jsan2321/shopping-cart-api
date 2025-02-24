package com.ecoapi.goodshopping.request;

import com.ecoapi.goodshopping.model.Category;
import lombok.Data;

import java.math.BigDecimal;

// request to update a product
@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}

