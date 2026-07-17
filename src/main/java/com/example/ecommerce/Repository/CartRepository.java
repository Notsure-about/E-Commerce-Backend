package com.example.ecommerce.Repository;

import com.example.ecommerce.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart>findByUserId(Long userId);
}
