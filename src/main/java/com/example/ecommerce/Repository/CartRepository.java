package com.example.ecommerce.Repository;

import com.example.ecommerce.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart>findUserById(Long userId);
}
