package com.example.demo.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
    List<OrderItem> findByOrderId(String orderId);


    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.userId = :userId AND oi.order.status = 'SUCCESS'")
    List<OrderItem> findSuccessfulOrderItemsByUserId(int userId);


}