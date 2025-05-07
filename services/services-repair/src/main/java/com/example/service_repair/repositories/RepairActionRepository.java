package com.example.service_repair.repositories;

import com.example.service_repair.models.RepairAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RepairActionRepository extends JpaRepository<RepairAction, Integer> {
    List<RepairAction> findByRepairRequestId(Integer repairId);
    List<RepairAction> findByPerformedById(Integer technicianId);
    List<RepairAction> findByPerformedAtBetween(LocalDateTime start, LocalDateTime end);
    List<RepairAction> findByActionType(String actionType);
}