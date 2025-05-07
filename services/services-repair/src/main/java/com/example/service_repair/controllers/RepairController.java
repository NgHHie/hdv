package com.example.service_repair.controllers;

import com.example.service_repair.dto.*;
import com.example.service_repair.exception.RepairRequestNotFoundException;
import com.example.service_repair.exception.TechnicianNotFoundException;
import com.example.service_repair.constants.RepairStatus;
import com.example.service_repair.services.RepairService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/repairs")
@RequiredArgsConstructor
@Slf4j
public class RepairController {

    private final RepairService repairService;

    /**
     * Create a new repair request
     */
    @PostMapping
    public ResponseEntity<RepairRequestResponseDto> createRepairRequest(@RequestBody RepairRequestDto repairRequestDto) {
        log.info("Received request to create repair request");
        RepairRequestResponseDto response = repairService.createRepairRequest(repairRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get a repair request by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RepairRequestResponseDto> getRepairRequestById(@PathVariable Integer id) {
        log.info("Received request to get repair request with ID: {}", id);
        try {
            RepairRequestResponseDto response = repairService.getRepairRequestById(id);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all repair requests for a customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<RepairRequestResponseDto>> getRepairRequestsByCustomerId(@PathVariable Integer customerId) {
        log.info("Received request to get repair requests for customer: {}", customerId);
        List<RepairRequestResponseDto> responses = repairService.getRepairRequestsByCustomerId(customerId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get repair requests by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RepairRequestResponseDto>> getRepairRequestsByStatus(@PathVariable RepairStatus status) {
        log.info("Received request to get repair requests with status: {}", status);
        List<RepairRequestResponseDto> responses = repairService.getRepairRequestsByStatus(status);
        return ResponseEntity.ok(responses);
    }

    /**
     * Process product receipt
     */
    @PostMapping("/{id}/receive")
    public ResponseEntity<RepairRequestResponseDto> processProductReceipt(
            @PathVariable Integer id,
            @RequestBody TechnicianDto technician,
            Principal principal) {
        
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to process product receipt for repair request {}", username, id);
        
        try {
            RepairRequestResponseDto response = repairService.processProductReceipt(id, technician, username);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.error("Failed to process receipt: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Start diagnostic process
     */
    @PostMapping("/{id}/start-diagnosis")
    public ResponseEntity<RepairRequestResponseDto> startDiagnosis(
            @PathVariable Integer id,
            Principal principal) {
        
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to start diagnosis for repair request {}", username, id);
        
        try {
            RepairRequestResponseDto response = repairService.startDiagnosis(id, username);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.error("Invalid state transition: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Complete diagnosis and start repair
     */
    @PostMapping("/{id}/complete-diagnosis")
    public ResponseEntity<RepairRequestResponseDto> completeDiagnosis(
            @PathVariable Integer id,
            @RequestBody DiagnosisDto diagnosisDto,
            Principal principal) {
        
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to complete diagnosis for repair request {}", username, id);
        
        try {
            RepairRequestResponseDto response = repairService.completeDiagnosisAndStartRepair(id, diagnosisDto, username);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.error("Invalid state transition: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Add part to repair
     */
    @PostMapping("/{id}/parts")
    public ResponseEntity<RepairPartDto> addRepairPart(
            @PathVariable Integer id,
            @RequestBody RepairPartDto partDto,
            Principal principal) {
        
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to add part to repair request {}", username, id);
        
        try {
            RepairPartDto response = repairService.addRepairPart(id, partDto, username);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.error("Invalid state for adding parts: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Complete repair
     */
    @PostMapping("/{id}/complete-repair")
    public ResponseEntity<RepairRequestResponseDto> completeRepair(
            @PathVariable Integer id,
            @RequestBody RepairCompletionDto completionDto,
            Principal principal) {
        
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to complete repair for repair request {}", username, id);
        
        try {
            RepairRequestResponseDto response = repairService.completeRepair(id, completionDto, username);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.error("Invalid state transition: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}