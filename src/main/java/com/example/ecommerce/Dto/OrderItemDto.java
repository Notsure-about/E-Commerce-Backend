package com.example.ecommerce.Dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private Long productId;
    private String productTitle;
    private int quantity;
    private double priceAtPurchase;
    private double lineTotal;


}
