package com.example.service_customer.repository;

import com.example.service_customer.model.WarrantyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyHistoryRepository extends JpaRepository<WarrantyHistory, Integer> {
    List<WarrantyHistory> findByWarrantyRequestIdOrderByPerformedAtDesc(Integer warrantyRequestId);
}