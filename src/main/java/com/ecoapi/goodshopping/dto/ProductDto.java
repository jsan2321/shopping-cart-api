package com.ecoapi.goodshopping.dto;

import com.ecoapi.goodshopping.model.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

// DTO to control which field, the data, are exposed in the API response...
@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
    private List<ImageDto> images; // do not include the entire object
}
