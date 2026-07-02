package com.example.ecommerce.Service;

import com.example.ecommerce.Dto.CartDto;
import com.example.ecommerce.Dto.CartItemDto;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    String AddProduct(Long userId, CartItemDto dto);
    void removeProduct(Long userId, Long productId);
    CartDto getCartByUser(Long userId);
    void clearCart(Long userId);
}
