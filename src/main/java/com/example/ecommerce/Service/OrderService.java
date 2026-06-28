package com.example.ecommerce.Service;

import com.example.ecommerce.Dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto placeOrder(Long userId);
    OrderDto getOrderByUserId(Long userId, Long orderId);
    OrderDto getOrderById(Long orderId);
}
