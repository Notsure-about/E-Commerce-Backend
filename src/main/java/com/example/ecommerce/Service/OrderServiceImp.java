package com.example.ecommerce.Service;

import com.example.ecommerce.Dto.OrderDto;
import com.example.ecommerce.Entity.*;
import com.example.ecommerce.Exception.InvalidRequestException;
import com.example.ecommerce.Exception.ResourceNotFoundException;
import com.example.ecommerce.Repository.CartRepository;
import com.example.ecommerce.Repository.OrderRepository;
import com.example.ecommerce.Repository.ProductRepository;
import com.example.ecommerce.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class OrderServiceImp implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    public OrderDto ConvertoDto(Order order){
        return modelMapper.map(order,OrderDto.class);
    }
    public Order ConvertoEnt(OrderDto dto){
        return  modelMapper.map(dto ,Order.class);
    }
    @Override
    public OrderDto placeOrder(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","userId",userId));
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart","userId",userId ));
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new InvalidRequestException("Cart is empty");
        }
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.placed);
        List<OrderItem> items = new ArrayList<>();
        double totalAmount = 0;
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new InvalidRequestException("Insufficient stock for product: " + product.getTitle());

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
    return ConvertoDto(savedOrder);
    }
    @Override
    public String cancelOrder(Long userId, Long orderId) {

        // 1. fetch user by userId
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
        // 2. fetch order by orderId
        Order order = orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order","orderId",orderId));
//        Optional<Order> orders = orderRepository.findOrderByUser_Id(userId);
//        orders.
        // 3. check if order belongs to this user
            if(!order.getUser().getId().equals(userId)){
                throw new InvalidRequestException("Order does not belong to this user");
            }
        // 4. check if order is already delivered
            if(order.getOrderStatus() == OrderStatus.shipped){
                throw new InvalidRequestException("Order already delivered, cannot cancel");
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
        return "Order Cancelled Successfully";
    }

    @Override
    public List<OrderDto> getOrderHistory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
        List<Order> orders = orderRepository.findOrderByUser_Id(userId);
        if(orders == null || orders.isEmpty()){
            throw new InvalidRequestException("Order Not Found for this user");
        }
        return orders
                .stream()
                .map(this::ConvertoDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderByUserId(Long userId ,Long orderId) {
        User user =  userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","userId",userId));
        Order order =  orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order","orderId",orderId));
        if(!order.getUser().getId().equals(userId)){
            throw new InvalidRequestException("Order does not belong to this user");
        }
        return ConvertoDto(order);
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order","orderId",orderId));
     return ConvertoDto(order);
    }
}
