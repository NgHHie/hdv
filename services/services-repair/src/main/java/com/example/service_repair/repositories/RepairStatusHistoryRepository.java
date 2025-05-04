package com.example.service_repair.repositories;

import com.example.service_repair.models.RepairStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairStatusHistoryRepository extends JpaRepository<RepairStatusHistory, Long> {
    List<RepairStatusHistory> findByRepairRequestId(Long repairId);
    List<RepairStatusHistory> findByRepairRequestIdOrderByCreatedAtDesc(Long repairId);
}
