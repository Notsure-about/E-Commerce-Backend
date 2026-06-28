package com.example.ecommerce.Dto;

import com.example.ecommerce.Entity.User;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private  Long userId;
    private List<OrderItemDto> items;
    private String orderStatus;
    private double amount;
    private String createdAt;

}
