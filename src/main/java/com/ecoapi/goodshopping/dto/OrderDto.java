package com.ecoapi.goodshopping.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private String status;
    // For nested objects (e.g., orderItems → items), ModelMapper recursively maps the fields of the nested objects (OrderItem → OrderItemDto).
    private List<OrderItemDto> items;
}
