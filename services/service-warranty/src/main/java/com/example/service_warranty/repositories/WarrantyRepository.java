package com.example.service_warranty.repositories;

import com.example.service_warranty.models.Warranty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarrantyRepository extends JpaRepository<Warranty, Long> {
    List<Warranty> findByCustomerId(Long customerId);
    List<Warranty> findByProductId(Long productId);
    Optional<Warranty> findByProductIdAndCustomerId(Long productId, Long customerId);
    List<Warranty> findByExpirationDateBefore(LocalDate date);
    List<Warranty> findByIsActiveTrue();
}