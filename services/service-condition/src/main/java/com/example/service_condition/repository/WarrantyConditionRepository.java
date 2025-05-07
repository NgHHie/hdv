package com.example.service_condition.repository;

import com.example.service_condition.model.WarrantyCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyConditionRepository extends JpaRepository<WarrantyCondition, Integer> {
    List<WarrantyCondition> findByIsActiveTrue();
}