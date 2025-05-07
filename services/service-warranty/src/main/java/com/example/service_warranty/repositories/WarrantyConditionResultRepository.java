package com.example.service_warranty.repositories;

import com.example.service_warranty.models.WarrantyConditionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyConditionResultRepository extends JpaRepository<WarrantyConditionResult, Integer> {
    List<WarrantyConditionResult> findByWarrantyRequestId(Integer warrantyRequestId);
}