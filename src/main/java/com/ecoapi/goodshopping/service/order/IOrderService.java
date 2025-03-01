package com.ecoapi.goodshopping.service.order;

import com.ecoapi.goodshopping.dto.OrderDto;
import com.ecoapi.goodshopping.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
