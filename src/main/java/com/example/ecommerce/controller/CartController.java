package com.example.ecommerce.controller;

import com.example.ecommerce.Dto.CartDto;
import com.example.ecommerce.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add/{userId}/{productId}/{quantity}")
    public ResponseEntity<String> addProductToCart(@PathVariable Long userId,
                                                   @PathVariable Long productId,
                                                   @PathVariable int quantity) {
        cartService.AddProduct(userId, productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added to cart");
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
