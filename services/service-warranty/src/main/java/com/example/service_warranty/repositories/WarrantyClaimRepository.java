package com.example.service_warranty.repositories;

import com.example.service_warranty.models.WarrantyClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyClaimRepository extends JpaRepository<WarrantyClaim, Integer> {
    List<WarrantyClaim> findByWarrantyId(Integer warrantyId);
    List<WarrantyClaim> findByRepairId(Integer repairId);
}