package com.example.service_customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.service_customer.model.Purchase;

public interface PurchaseRepository  extends JpaRepository<Purchase, Long> {
   
    List<Purchase> findByCustomerId(Integer customerId);

}
