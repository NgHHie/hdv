package com.example.service_order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.service_order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
   
    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.items i WHERE i.warrantyExpiration IS NOT NULL")
    List<Order> findAllOrdersWithNotNullWarrantyItems();
}