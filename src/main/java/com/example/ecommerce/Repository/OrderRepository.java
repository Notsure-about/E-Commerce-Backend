package com.example.ecommerce.Repository;

import com.example.ecommerce.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findOrderByUser_Id(Long userId);
}
