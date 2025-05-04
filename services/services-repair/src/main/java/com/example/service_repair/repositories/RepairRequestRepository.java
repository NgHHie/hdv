package com.example.service_repair.repositories;

import com.example.service_repair.models.RepairRequest;
import com.example.service_repair.constants.RepairStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {
    List<RepairRequest> findByCustomerId(Long customerId);
    List<RepairRequest> findByProductId(Long productId);
    List<RepairRequest> findByWarrantyId(Long warrantyId);
    List<RepairRequest> findByStatus(RepairStatus status);
    List<RepairRequest> findByCustomerIdAndStatus(Long customerId, RepairStatus status);
    List<RepairRequest> findByTechnicianId(Long technicianId);
    
    @Query("SELECT r FROM RepairRequest r WHERE r.createdAt BETWEEN ?1 AND ?2")
    List<RepairRequest> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT r FROM RepairRequest r WHERE r.status IN ?1")
    List<RepairRequest> findByStatusIn(List<RepairStatus> statuses);
}
