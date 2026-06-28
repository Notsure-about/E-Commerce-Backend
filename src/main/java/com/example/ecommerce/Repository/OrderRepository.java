package com.example.ecommerce.Repository;

import com.example.ecommerce.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Component

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findOrderByUser(Long userId);
}
