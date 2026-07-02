package com.example.ecommerce.controller;

import com.example.ecommerce.Dto.OrderDto;
import com.example.ecommerce.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping ("/order/user/{userId}")
    public ResponseEntity<OrderDto> placeOrder(@PathVariable Long userId){
        return ResponseEntity.ok(orderService.placeOrder(userId));
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long  orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderHistory(@PathVariable Long userId){
        return ResponseEntity.ok(orderService.getOrderHistory(userId));
    }
    @GetMapping("/user/{userId}/order/{orderId}")
    public ResponseEntity<OrderDto> getOrderByUserId(@PathVariable Long userId, @PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getOrderByUserId(userId,orderId));
    }
    @DeleteMapping("/user/{userId}/order/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long userId, @PathVariable Long orderId){
        return ResponseEntity.ok(orderService.cancelOrder(userId, orderId));
    }
}
