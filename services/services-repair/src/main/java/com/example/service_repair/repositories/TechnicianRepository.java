package com.example.service_repair.repositories;

import com.example.service_repair.models.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Integer> {
    Optional<Technician> findByEmail(String email);
    List<Technician> findByIsActiveTrue();
    List<Technician> findBySpecialization(String specialization);
}

