package com.example.ecommerce.Service;

import com.example.ecommerce.Dto.OrderDto;
import com.example.ecommerce.Entity.Order;

import java.util.List;

public interface OrderService {
    OrderDto placeOrder(Long userId);
    OrderDto getOrderByUserId(Long userId, Long orderId);
    OrderDto getOrderById(Long orderId);
    String cancelOrder(Long userId , Long orderId);
    List<OrderDto> getOrderHistory(Long userId);
}
