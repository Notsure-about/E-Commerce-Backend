package com.example.ecommerce.Service;

import com.example.ecommerce.Dto.CartDto;
import com.example.ecommerce.Dto.CartItemDto;
import com.example.ecommerce.Entity.Cart;
import com.example.ecommerce.Entity.CartItem;
import com.example.ecommerce.Entity.Product;
import com.example.ecommerce.Entity.User;
import com.example.ecommerce.Exception.ResourceNotFoundException;
import com.example.ecommerce.Repository.CartRepository;
import com.example.ecommerce.Repository.ProductRepository;
import com.example.ecommerce.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImp implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private  ProductRepository productRepository;
    @Autowired
    private  UserRepository userRepository;

    @Transactional
    @Override
    public String AddProduct(Long userId, CartItemDto dto) {
        //extract the details from dto
        System.out.println("AddProduct called with userId: " + userId);
        Long productId = dto.getProductId();
        int quantity = dto.getQuantity();
        if(quantity<=0){
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id",userId));
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newcart = new Cart();
            newcart.setUser(user);
            return cartRepository.save(newcart);
        });
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product","id",productId));

        CartItem existingItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        int alreadyInCart = existingItem != null ? existingItem.getQuantity() : 0;
        int requestedTotal = alreadyInCart + quantity;

        if (requestedTotal > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock for this product");
        }

        if (existingItem != null) {
            existingItem.setQuantity(requestedTotal);
            existingItem.setTotalPrice(product.getPrice() * requestedTotal);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(product.getPrice() * quantity);
            cart.getItems().add(cartItem);
        }

        cartRepository.save(cart);
        return "Product is Added Successfully";
    }

    @Override
    public void removeProduct(Long userId, Long productId) {
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","Id", userId));
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart","userId",userId));

        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem","productId",productId));

        cart.getItems().remove(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart","userId",userId));

        List<CartItemDto> itemDtos = cart.getItems()
                .stream()
                .map(this::mapToCartItemDto)
                .collect(Collectors.toList());

        double totalAmount = itemDtos.stream()
                .mapToDouble(CartItemDto::getTotalPrice)
                .sum();

        CartDto cartDto = new CartDto();
        cartDto.setCartId(cart.getId());
        cartDto.setUserId(cart.getUser().getId());
        cartDto.setItems(itemDtos);
        cartDto.setTotalAmount(totalAmount);

        return cartDto;
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart","userId",userId));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private CartItemDto mapToCartItemDto(CartItem cartItem) {
        Product product = cartItem.getProduct();
        CartItemDto dto = new CartItemDto();
        dto.setProductId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());
        dto.setQuantity(cartItem.getQuantity());
        dto.setTotalPrice(cartItem.getTotalPrice());
        return dto;
    }
}
