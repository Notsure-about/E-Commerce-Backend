package com.example.ecommerce.Service;

import com.example.ecommerce.Dto.OrderDto;
import com.example.ecommerce.Entity.*;
import com.example.ecommerce.Repository.CartRepository;
import com.example.ecommerce.Repository.OrderRepository;
import com.example.ecommerce.Repository.ProductRepository;
import com.example.ecommerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderServiceImp implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public OrderDto placeOrder(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        Cart cart = cartRepository.findUserById(userId).orElseThrow(() -> new RuntimeException("cart not found"));
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("cart is empty");
        }
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.placed);
        List<OrderItem> items = new ArrayList<>();
        double totalAmount = 0;
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("product out of stock");

            }
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItem.setOrder(order);
            items.add(orderItem);
            orderItem.setLineTotal(product.getPrice() * item.getQuantity());
            totalAmount += orderItem.getLineTotal();
            //deduct stock
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }
        order.setItems(items);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // Clear the cart after placing order
        cart.getItems().clear();
        cartRepository.save(cart);
    return null;
    }
    public void cancelOrder(Long userId, Long orderId) {

        // 1. fetch user by userId
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("user not found"));
        // 2. fetch order by orderId
        Order order = orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("order not found"));
//        Optional<Order> orders = orderRepository.findOrderByUser(userId);
//        orders.
        // 3. check if order belongs to this user
            if(!order.getUser().getId().equals(userId)){
                throw new RuntimeException("order does not belong to this user");
            }
        // 4. check if order is already delivered
            if(order.getOrderStatus() == OrderStatus.shipped){
                throw new RuntimeException("order is already shipped");
            }
        // 5. loop through order items and restore stock
             for( OrderItem item : order.getItems()){
                 Product product = item.getProduct();
                 product.setStock(product.getStock() + item.getQuantity());
                 productRepository.save(product);
             }
        // 6. set status to CANCELLED
           order.setOrderStatus(OrderStatus.cancelled);
        // 7. save the order
        orderRepository.save(order);
    }

    @Override
    public OrderDto getOrderByUserId(Long userId ,Long orderId) {
        User user =  userRepository.findById(userId).orElseThrow(()-> new RuntimeException("user not found"));
        Order order =  orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("order not found"));
        if(!order.getUser().getId().equals(userId)){
            throw new RuntimeException("order does not belong to the user");
        }
        return order;
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("order not found"));

    }
}
