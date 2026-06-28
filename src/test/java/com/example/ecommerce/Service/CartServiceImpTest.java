package com.example.ecommerce.Service;

import com.example.ecommerce.Dto.CartDto;
import com.example.ecommerce.Entity.Cart;
import com.example.ecommerce.Entity.CartItem;
import com.example.ecommerce.Entity.Product;
import com.example.ecommerce.Entity.User;
import com.example.ecommerce.Repository.CartRepository;
import com.example.ecommerce.Repository.ProductRepository;
import com.example.ecommerce.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImpTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    private CartServiceImp cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceImp(cartRepository, productRepository, userRepository);
    }

    @Test
    void addProduct_createsCartAndItem_whenCartDoesNotExist() {
        Long userId = 1L;
        Long productId = 10L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(productId);
        product.setPrice(100.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findUserById(userId)).thenReturn(Optional.empty());
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.AddProduct(userId, productId, 2);

        verify(cartRepository, times(2)).save(any(Cart.class));
    }

    @Test
    void addProduct_updatesExistingItem_whenProductAlreadyInCart() {
        Long userId = 1L;
        Long productId = 10L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(productId);
        product.setPrice(50.0);

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(1);
        item.setTotalPrice(50.0);
        cart.getItems().add(item);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findUserById(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.AddProduct(userId, productId, 3);

        assertEquals(4, item.getQuantity());
        assertEquals(200.0, item.getTotalPrice());
        verify(cartRepository).save(cart);
    }

    @Test
    void removeProduct_removesItem_andSavesCart() {
        Long userId = 1L;
        Long productId = 10L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(productId);
        product.setPrice(25.0);

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(2);
        item.setTotalPrice(50.0);
        cart.getItems().add(item);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findUserById(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.removeProduct(userId, productId);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }

    @Test
    void getCartByUser_returnsDto_withTotalAmount() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Product product1 = new Product();
        product1.setId(10L);
        product1.setTitle("P1");
        product1.setPrice(10.0);

        Product product2 = new Product();
        product2.setId(11L);
        product2.setTitle("P2");
        product2.setPrice(20.0);

        Cart cart = new Cart();
        cart.setId(100L);
        cart.setUser(user);

        CartItem item1 = new CartItem();
        item1.setCart(cart);
        item1.setProduct(product1);
        item1.setQuantity(2);
        item1.setTotalPrice(20.0);

        CartItem item2 = new CartItem();
        item2.setCart(cart);
        item2.setProduct(product2);
        item2.setQuantity(1);
        item2.setTotalPrice(20.0);

        cart.getItems().add(item1);
        cart.getItems().add(item2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findUserById(userId)).thenReturn(Optional.of(cart));

        CartDto dto = cartService.getCartByUser(userId);

        assertNotNull(dto);
        assertEquals(100L, dto.getCartId());
        assertEquals(userId, dto.getUserId());
        assertEquals(2, dto.getItems().size());
        assertEquals(40.0, dto.getTotalAmount());
    }
}

