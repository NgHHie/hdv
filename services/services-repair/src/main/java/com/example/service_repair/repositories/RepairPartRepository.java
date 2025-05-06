package com.example.service_repair.repositories;

import com.example.service_repair.models.RepairPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairPartRepository extends JpaRepository<RepairPart, Integer> {
    List<RepairPart> findByRepairRequestId(Integer repairId);
}