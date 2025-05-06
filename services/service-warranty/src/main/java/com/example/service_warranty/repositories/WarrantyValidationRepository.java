package com.example.service_warranty.repositories;

import com.example.service_warranty.models.WarrantyValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarrantyValidationRepository extends JpaRepository<WarrantyValidation, Integer> {
    Optional<WarrantyValidation> findByWarrantyRequestId(Integer warrantyRequestId);
}