package com.example.service_customer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.service_customer.model.Purchase;

public interface PurchaseRepository  extends JpaRepository<Purchase, Integer> {
   
    List<Purchase> findByCustomerId(Integer customerId);

    @Query("SELECT p FROM Purchase p JOIN p.items i WHERE i.productId = :productId")
    Optional<Purchase> findByProductId(@Param("productId") Integer productId);

}
