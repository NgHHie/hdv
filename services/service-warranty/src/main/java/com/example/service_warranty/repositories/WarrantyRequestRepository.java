package com.example.service_warranty.repositories;

import com.example.service_warranty.models.WarrantyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyRequestRepository extends JpaRepository<WarrantyRequest, Integer> {
    List<WarrantyRequest> findByCustomerId(Integer customerId);
    List<WarrantyRequest> findByProductId(Integer productId);
    List<WarrantyRequest> findByStatus(String status);
    List<WarrantyRequest> findByCustomerIdAndStatus(Integer customerId, String status);
}