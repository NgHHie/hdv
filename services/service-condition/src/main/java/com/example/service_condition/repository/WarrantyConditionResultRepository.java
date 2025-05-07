package com.example.service_condition.repository;

import com.example.service_condition.model.WarrantyConditionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyConditionResultRepository extends JpaRepository<WarrantyConditionResult, Integer> {
    List<WarrantyConditionResult> findByWarrantyRequestId(Integer warrantyRequestId);
}