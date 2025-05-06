package com.example.service_customer.repository;



import com.example.service_customer.model.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Integer> {
    List<PurchaseItem> findByPurchaseId(Integer purchaseId);
    Optional<PurchaseItem> findByProductId(Integer productId);
}