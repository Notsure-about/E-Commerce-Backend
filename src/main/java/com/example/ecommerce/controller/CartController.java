package com.example.ecommerce.controller;

import com.example.ecommerce.Dto.CartDto;
import com.example.ecommerce.Dto.CartItemDto;
import com.example.ecommerce.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<String> addProductToCart(@PathVariable Long userId,
                                                   @RequestBody CartItemDto dto) {
        return ResponseEntity.ok(cartService.AddProduct(userId, dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUser(userId));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable Long userId,
                                                        @PathVariable Long productId) {
        cartService.removeProduct(userId, productId);
        return ResponseEntity.ok("Product removed from cart");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared");
    }
}
