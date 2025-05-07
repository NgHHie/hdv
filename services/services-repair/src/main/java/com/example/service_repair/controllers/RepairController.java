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
    public ResponseEntity<RepairRequestResponseDto> getRepairRequestById(@PathVariable Long id) {
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
    public ResponseEntity<List<RepairRequestResponseDto>> getRepairRequestsByCustomerId(@PathVariable Long customerId) {
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
     * Move a repair request to the next state
     */
    @PostMapping("/{id}/next")
    public ResponseEntity<RepairRequestResponseDto> moveToNextState(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> notes,
            Principal principal) {
        
        String noteText = notes != null ? notes.get("notes") : null;
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to move repair request {} to next state", username, id);
        
        try {
            RepairRequestResponseDto response = repairService.moveToNextState(id, noteText, username);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.error("Failed to move to next state: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Move a repair request to the previous state
     */
    @PostMapping("/{id}/previous")
    public ResponseEntity<RepairRequestResponseDto> moveToPreviousState(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> notes,
            Principal principal) {
        
        String noteText = notes != null ? notes.get("notes") : null;
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to move repair request {} to previous state", username, id);
        
        try {
            RepairRequestResponseDto response = repairService.moveToPreviousState(id, noteText, username);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.error("Failed to move to previous state: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Cancel a repair request
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<RepairRequestResponseDto> cancelRepairRequest(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> notes,
            Principal principal) {
        
        String noteText = notes != null ? notes.get("notes") : null;
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to cancel repair request {}", username, id);
        
        try {
            RepairRequestResponseDto response = repairService.cancelRepairRequest(id, noteText, username);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.error("Failed to cancel: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Process product receipt
     */
    @PostMapping("/{id}/receive")
    public ResponseEntity<RepairRequestResponseDto> processProductReceipt(
            @PathVariable Long id,
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
     * Update repair notes
     */
    @PatchMapping("/{id}/notes")
    public ResponseEntity<RepairRequestResponseDto> updateRepairNotes(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody,
            Principal principal) {
        
        String notes = requestBody.get("notes");
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to update repair notes for repair request {}", username, id);
        
        try {
            RepairRequestResponseDto response = repairService.updateRepairNotes(id, notes, username);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Assign technician to repair
     */
    @PostMapping("/{id}/technician/{technicianId}")
    public ResponseEntity<RepairRequestResponseDto> assignTechnician(
            @PathVariable Long id,
            @PathVariable Long technicianId,
            Principal principal) {
        
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to assign technician {} to repair request {}", 
                username, technicianId, id);
        
        try {
            RepairRequestResponseDto response = repairService.assignTechnician(id, technicianId, username);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (TechnicianNotFoundException e) {
            log.error("Technician not found: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Add part to repair
     */
    @PostMapping("/{id}/parts")
    public ResponseEntity<RepairPartDto> addRepairPart(
            @PathVariable Long id,
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
        } catch (IllegalArgumentException e) {
            log.error("Invalid part data: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Remove part from repair
     */
    @DeleteMapping("/{id}/parts/{partId}")
    public ResponseEntity<Void> removeRepairPart(
            @PathVariable Long id,
            @PathVariable Long partId,
            Principal principal) {
        
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to remove part {} from repair request {}", 
                username, partId, id);
        
        try {
            repairService.removeRepairPart(id, partId, username);
            return ResponseEntity.noContent().build();
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.error("Invalid part request: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Add repair action
     */
    @PostMapping("/{id}/actions")
    public ResponseEntity<RepairActionDto> addRepairAction(
            @PathVariable Long id,
            @RequestBody RepairActionDto actionDto,
            Principal principal) {
        
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to add action to repair request {}", username, id);
        
        try {
            RepairActionDto response = repairService.addRepairAction(id, actionDto, username);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.error("Invalid action data: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Update repair cost
     */
    @PatchMapping("/{id}/cost")
    public ResponseEntity<RepairRequestResponseDto> updateRepairCost(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody,
            Principal principal) {
        
        BigDecimal cost = null;
        if (requestBody.containsKey("cost")) {
            try {
                cost = new BigDecimal(requestBody.get("cost"));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(null);
            }
        }
        
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to update repair cost for repair request {}", username, id);
        
        try {
            RepairRequestResponseDto response = repairService.updateRepairCost(id, cost, username);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Reject a repair request
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<RepairRequestResponseDto> rejectRepairRequest(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody,
            Principal principal) {
        
        String reason = requestBody.get("reason");
        String username = principal != null ? principal.getName() : "SYSTEM";
        
        log.info("Received request from {} to reject repair request {}", username, id);
        
        try {
            RepairRequestResponseDto response = repairService.rejectRepairRequest(id, reason, username);
            return ResponseEntity.ok(response);
        } catch (RepairRequestNotFoundException e) {
            log.error("Repair request not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}