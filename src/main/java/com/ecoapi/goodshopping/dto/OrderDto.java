package com.ecoapi.goodshopping.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// ModelMapper automatically maps fields with matching names and types between the Order entity and the OrderDto.
// - orderId (in Order) → id (in OrderDto)
// - orderDate (in Order) → orderDate (in OrderDto)
// - totalAmount (in Order) → totalAmount (in OrderDto)
// - orderStatus (in Order) → status (in OrderDto)
// - orderItems (in Order) → items (in OrderDto)
@Data
public class OrderDto {
    private Long id;
    private Long userId;
    //private LocalDateTime orderDate;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private String status;
    // For nested objects (e.g., orderItems → items), ModelMapper recursively maps the fields of the nested objects (OrderItem → OrderItemDto).
    private List<OrderItemDto> items;
}
