package com.example.service_order.repository;



import com.example.service_order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
   
    @Query("SELECT oi FROM OrderItem oi WHERE oi.warrantyExpiration IS NOT NULL")
    List<OrderItem> findAllWithNotNullWarrantyExpiration();
    
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.warrantyExpiration IS NOT NULL")
    List<OrderItem> findByOrderIdAndNotNullWarrantyExpiration(Long orderId);
    

  
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.customerId = :userId AND oi.warrantyExpiration IS NOT NULL")
    List<OrderItem> findItemsWithWarrantyByUserId(@Param("userId") Long userId);
 
    List<OrderItem> findByOrderCustomerIdAndWarrantyExpirationIsNotNull(Long customerId);
}