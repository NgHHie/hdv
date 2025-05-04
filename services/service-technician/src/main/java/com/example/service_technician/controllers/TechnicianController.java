package com.example.service_technician.controllers;

import com.example.service_technician.dto.TechnicianRequest;
import com.example.service_technician.dto.TechnicianResponse;
import com.example.service_technician.services.TechnicianService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/technicians")
@RequiredArgsConstructor
@Slf4j
public class TechnicianController {
    
    private final TechnicianService technicianService;
    
    @PostMapping
    public ResponseEntity<TechnicianResponse> createTechnician(@Valid @RequestBody TechnicianRequest technicianRequest) {
        log.info("REST request to create Technician : {}", technicianRequest);
        TechnicianResponse createdTechnician = technicianService.createTechnician(technicianRequest);
        return new ResponseEntity<>(createdTechnician, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TechnicianResponse> getTechnicianById(@PathVariable Long id) {
        log.info("REST request to get Technician by ID : {}", id);
        TechnicianResponse technician = technicianService.getTechnicianById(id);
        return ResponseEntity.ok(technician);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<TechnicianResponse> getTechnicianByEmail(@PathVariable String email) {
        log.info("REST request to get Technician by email : {}", email);
        TechnicianResponse technician = technicianService.getTechnicianByEmail(email);
        return ResponseEntity.ok(technician);
    }
    
    @GetMapping
    public ResponseEntity<List<TechnicianResponse>> getAllTechnicians() {
        log.info("REST request to get all Technicians");
        List<TechnicianResponse> technicians = technicianService.getAllTechnicians();
        return ResponseEntity.ok(technicians);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<TechnicianResponse>> getActiveTechnicians() {
        log.info("REST request to get all active Technicians");
        List<TechnicianResponse> technicians = technicianService.getActiveTechnicians();
        return ResponseEntity.ok(technicians);
    }
    
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<TechnicianResponse>> getTechniciansBySpecialization(@PathVariable String specialization) {
        log.info("REST request to get Technicians with specialization : {}", specialization);
        List<TechnicianResponse> technicians = technicianService.getTechniciansBySpecialization(specialization);
        return ResponseEntity.ok(technicians);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TechnicianResponse> updateTechnician(
            @PathVariable Long id,
            @Valid @RequestBody TechnicianRequest technicianRequest) {
        log.info("REST request to update Technician : {}", technicianRequest);
        TechnicianResponse updatedTechnician = technicianService.updateTechnician(id, technicianRequest);
        return ResponseEntity.ok(updatedTechnician);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnician(@PathVariable Long id) {
        log.info("REST request to delete Technician : {}", id);
        technicianService.deleteTechnician(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<TechnicianResponse> toggleTechnicianActiveStatus(@PathVariable Long id) {
        log.info("REST request to toggle active status for Technician : {}", id);
        TechnicianResponse technician = technicianService.toggleTechnicianActiveStatus(id);
        return ResponseEntity.ok(technician);
    }
}