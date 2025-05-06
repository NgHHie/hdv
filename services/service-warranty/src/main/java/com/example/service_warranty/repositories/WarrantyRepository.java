package com.example.service_warranty.repositories;

import com.example.service_warranty.models.Warranty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarrantyRepository extends JpaRepository<Warranty, Integer> {
    List<Warranty> findByCustomerId(Integer customerId);
    List<Warranty> findByProductId(Integer productId);
    Optional<Warranty> findByProductIdAndCustomerId(Integer productId, Integer customerId);
    List<Warranty> findByExpirationDateBefore(LocalDate date);
    List<Warranty> findByIsActiveTrue();
}