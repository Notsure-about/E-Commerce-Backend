package com.example.ecommerce.Service;

import com.example.ecommerce.Dto.CartDto;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    void AddProduct(Long userId, Long productId, int quantity);
    void removeProduct(Long userId, Long productId);
    CartDto getCartByUser(Long userId);
    void clearCart(Long userId);
}
