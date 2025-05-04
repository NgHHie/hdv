package com.example.service_customer.repository;



import com.example.service_customer.model.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
    List<PurchaseItem> findByPurchaseId(Long purchaseId);
    List<PurchaseItem> findByProductId(Long productId);
    
    @Query("SELECT pi FROM PurchaseItem pi WHERE pi.warrantyExpirationDate IS NOT NULL")
    List<PurchaseItem> findAllWithNotNullWarrantyExpirationDate();
    
    @Query("SELECT pi FROM PurchaseItem pi WHERE pi.purchase.id = ?1 AND pi.warrantyExpirationDate IS NOT NULL")
    List<PurchaseItem> findByPurchaseIdAndWarrantyExpirationDateNotNull(Long purchaseId);
    
    @Query("SELECT pi FROM PurchaseItem pi WHERE pi.purchase.customer.id = ?1 AND pi.warrantyExpirationDate IS NOT NULL")
    List<PurchaseItem> findByPurchaseCustomerIdAndWarrantyExpirationDateNotNull(Integer customerId);
}